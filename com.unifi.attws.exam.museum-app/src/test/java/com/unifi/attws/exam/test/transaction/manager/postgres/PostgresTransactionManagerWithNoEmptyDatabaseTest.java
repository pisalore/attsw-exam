package com.unifi.attws.exam.test.transaction.manager.postgres;

import static org.assertj.core.api.Assertions.*;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.unifi.attws.exam.exception.RepositoryException;
import com.unifi.attws.exam.model.Exhibition;
import com.unifi.attws.exam.model.Museum;
import com.unifi.attws.exam.repository.ExhibitionRepository;
import com.unifi.attws.exam.repository.MuseumRepository;
import com.unifi.attws.exam.repository.postgres.PostgresExhibitionRepository;
import com.unifi.attws.exam.repository.postgres.PostgresMuseumRepository;
import com.unifi.attws.exam.transaction.manager.postgres.PostgresTransactionManager;
import org.testcontainers.jdbc.ContainerDatabaseDriver;

public class PostgresTransactionManagerWithNoEmptyDatabaseTest {
	private final String JDBC_CONTAINER_URL = "jdbc:tc:postgresql:9.6.8:///databasename?TC_INITSCRIPT=file:src/test/resources/META-INF/init_postgresql.sql";

	private static final UUID MUSEUM_ID_1 = UUID.fromString("b433da18-ba5a-4b86-92af-ba11be6314e7");
	private static final UUID MUSEUM_ID_2 = UUID.fromString("94fe3013-9ebb-432e-ab55-e612dc797851");

	private static final UUID EXHIBITION_ID_1 = UUID.fromString("49d13e51-2277-4911-929f-c9c067e2e8b4");
	private static final UUID EXHIBITION_ID_2 = UUID.fromString("b2cb1474-24ff-41eb-a8d7-963f32f6822d");

	private static final int NUM_CONSTANT1 = 10;
	private static final String MUSEUM1_TEST = "museum1_test";

	private MuseumRepository postgresMuseumRepository;
	private ExhibitionRepository postgresExhibitionRepository;
	private PostgresTransactionManager transactionManager;
	private EntityManagerFactory sessionFactory;
	private static EntityManager entityManager;

	@Before
	public void setUp() throws Exception {
		sessionFactory = Persistence.createEntityManagerFactory("postgres.not-empty.database");
		entityManager = sessionFactory.createEntityManager();
		transactionManager = new PostgresTransactionManager(entityManager);

		postgresMuseumRepository = new PostgresMuseumRepository(transactionManager.getEntityManager());
		postgresExhibitionRepository = new PostgresExhibitionRepository(entityManager);
	}

	@Test
	public void testInsertMuseumWithSameNameInPostgresDatabaseShouldRollbackAndThrow() throws RepositoryException {
		Museum museum = createTestMuseum(MUSEUM1_TEST, NUM_CONSTANT1);

		assertThatThrownBy(() -> transactionManager.doInTransaction((museumRepository, exhibitionRepository) -> {
			return museumRepository.addMuseum(museum);
		})).isInstanceOf(RepositoryException.class);

		assertThat(postgresMuseumRepository.findAllMuseums()).hasSize(2).extracting(Museum::getName)
				.contains(museum.getName());
	}

	@Test
	public void testDeleteNullExhibitionInPostgresDatabaseShouldThrowAndRollback() {
		assertThatThrownBy(() -> transactionManager.doInTransaction((museumRepository, exhibitionRepository) -> {
			exhibitionRepository.deleteExhibition(null);
			return null;
		})).isInstanceOf(RepositoryException.class);

		assertThat(postgresExhibitionRepository.findAllExhibitions()).hasSize(2);
	}

	@Test
	public void testDeleteExhibitionInPostgresDatabaseCommit() throws RepositoryException {
		Exhibition exhibition = postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_1);

		transactionManager.doInTransaction((museumRepository, exhibitionRepository) -> {
			exhibitionRepository.deleteExhibition(exhibition);
			return null;
		});

		assertThat(postgresExhibitionRepository.findAllExhibitions()).hasSize(1).extracting(Exhibition::getId)
				.containsExactly(EXHIBITION_ID_2);

	}

	@Test
	public void testDeleteRemovedExhibitionInPostgresDatabaseShouldThrowAndRollback() throws RepositoryException {
		Exhibition exhibition = postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_1);

		transactionManager.doInTransaction((museumRepository, exhibitionRepository) -> {
			exhibitionRepository.deleteExhibition(exhibition);
			return null;
		});

		assertThatThrownBy(() -> transactionManager.doInTransaction((museumRepository, exhibitionRepository) -> {
			exhibitionRepository.deleteExhibition(exhibition);
			return null;
		})).isInstanceOf(RepositoryException.class);

		assertThat(postgresExhibitionRepository.findAllExhibitions()).hasSize(1).extracting(Exhibition::getId)
				.doesNotContain(exhibition.getId()).contains(EXHIBITION_ID_2);
	}

	@Test
	public void testDeleteNullMuseumInPostgresDatabaseShouldThrowAndRollback() {
		assertThatThrownBy(() -> transactionManager.doInTransaction((museumRepository, exhibitionRepository) -> {
			museumRepository.deleteMuseum(null);
			return null;
		})).isInstanceOf(RepositoryException.class);

		assertThat(postgresMuseumRepository.findAllMuseums()).hasSize(2);
	}

	@Test
	public void testDeleteMuseumReferencedMuseumByExhibitionInPostgresDatabaseShouldThrowAndRollback()
			throws RepositoryException {
		Museum museum = postgresMuseumRepository.findMuseumById(MUSEUM_ID_1);
		assertThatThrownBy(() -> transactionManager.doInTransaction((museumRepository, exhibitionRepository) -> {
			museumRepository.deleteMuseum(museum);
			return null;
		})).isInstanceOf(RepositoryException.class);

		assertThat(postgresMuseumRepository.findAllMuseums()).hasSize(2).extracting(Museum::getId)
				.contains(MUSEUM_ID_1);
	}

	@Test
	public void testCorrectlyDeleteMuseumWithNoExhibitionsInPostgresDatabaseCommit() throws RepositoryException {
		Museum museumToBeDeleted = postgresMuseumRepository.findMuseumById(MUSEUM_ID_2);
		Museum museum = postgresMuseumRepository.findMuseumById(MUSEUM_ID_1);

		Exhibition exhibition1 = postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_1);
		Exhibition exhibition2 = postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_2);

		transactionManager.doInTransaction((museumRepository, exhibitionRepository) -> {
			museumRepository.deleteMuseum(museumToBeDeleted);
			return null;
		});

		assertThat(postgresExhibitionRepository.findAllExhibitions()).containsExactly(exhibition1, exhibition2)
				.extracting(Exhibition::getMuseumId).contains(MUSEUM_ID_1);

		assertThat(postgresMuseumRepository.findAllMuseums()).contains(museum);
	}

	@Test
	public void testCorrectlyDeleteMuseumReferencedByExhibitionsInPostgresDatabaseCommit() throws RepositoryException {
		Museum museum = postgresMuseumRepository.findMuseumById(MUSEUM_ID_1);
		Exhibition exhibition1 = postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_1);
		Exhibition exhibition2 = postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_2);

		transactionManager.doInTransaction((museumRepository, exhibitionRepository) -> {
			exhibitionRepository.deleteExhibition(exhibition1);
			exhibitionRepository.deleteExhibition(exhibition2);
			museumRepository.deleteMuseum(museum);
			return null;
		});

		assertThat(postgresExhibitionRepository.findAllExhibitions()).isEmpty();

		assertThat(postgresMuseumRepository.findAllMuseums()).hasSize(1).extracting(Museum::getId)
				.contains(MUSEUM_ID_2);

	}

	@Test
	public void testUpdateNullMuseumInPostgresDatabaseShouldThrowAndRollback() {
		assertThatThrownBy(() -> transactionManager.doInTransaction((museumRepository, exhibitionRepository) -> {
			return museumRepository.updateMuseum(null);
		})).isInstanceOf(RepositoryException.class);
	}

	@Test
	public void testUpdateRemovedMuseumInPosgresDatabaseShouldThrowAndRollback() {
		Museum museum = postgresMuseumRepository.findMuseumById(MUSEUM_ID_1);
		assertThatThrownBy(() -> transactionManager.doInTransaction((museumRepository, exhibitionRepository) -> {
			museumRepository.deleteMuseum(museum);
			return museumRepository.updateMuseum(museum);
		})).isInstanceOf(RepositoryException.class);
	}

	@After
	public void tearDown() {
		// THis force to restart a docker container and re-execute the initialization script provided for not empty database.
		ContainerDatabaseDriver.killContainer(JDBC_CONTAINER_URL);

		entityManager.clear();
		entityManager.close();
		sessionFactory.close();
	}

	/**
	 * 
	 * Utility methods
	 * 
	 */

	public Museum createTestMuseum(String museumName, int numOfRooms) {
		return new Museum(museumName, numOfRooms);
	}

	public Exhibition createExhibition(String exhibitionName, int numOfSeats) {
		return new Exhibition(exhibitionName, numOfSeats);

	}

}
