package com.unifi.attsw.exam.transaction.manager.postgres;

import static org.assertj.core.api.Assertions.*;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testcontainers.jdbc.ContainerDatabaseDriver;

import com.unifi.attsw.exam.repository.exception.RepositoryException;
import com.unifi.attsw.exam.model.Exhibition;
import com.unifi.attsw.exam.model.Museum;
import com.unifi.attsw.exam.repository.ExhibitionRepository;
import com.unifi.attsw.exam.repository.MuseumRepository;
import com.unifi.attsw.exam.repository.postgres.PostgresExhibitionRepository;
import com.unifi.attsw.exam.repository.postgres.PostgresMuseumRepository;
import com.unifi.attsw.exam.transaction.manager.postgres.PostgresTransactionManager;

public class PostgresTransactionManagerWithNoEmptyDatabaseTest {
	private final String JDBC_CONTAINER_URL = "jdbc:tc:postgresql:9.6.8:///databasename?TC_INITSCRIPT=file:src/test/resources/META-INF/postgres_init_scripts/init_postgresql_not_empty.sql";

	private static final UUID MUSEUM_ID_1 = UUID.fromString("b433da18-ba5a-4b86-92af-ba11be6314e7");
	private static final UUID MUSEUM_ID_2 = UUID.fromString("94fe3013-9ebb-432e-ab55-e612dc797851");

	private static final UUID EXHIBITION_ID_1 = UUID.fromString("49d13e51-2277-4911-929f-c9c067e2e8b4");
	private static final UUID EXHIBITION_ID_2 = UUID.fromString("b2cb1474-24ff-41eb-a8d7-963f32f6822d");

	private static final String MUSEUM1_TEST = "museum1_test";
	private static final String EXHIBITION1_TEST = "exhibition1_test";

	private static final int NUM_CONSTANT1 = 10;

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
	public void testInsertExhibitionWithSameNameInPostgresDatabaseShouldRollbackAndThrow() throws RepositoryException {
		Exhibition exhibition = createExhibition(EXHIBITION1_TEST, NUM_CONSTANT1);

		assertThatThrownBy(() -> transactionManager.doInTransactionExhibition(exhibitionRepository -> {
			return exhibitionRepository.addNewExhibition(exhibition);
		})).isInstanceOf(RepositoryException.class);

		assertThat(postgresExhibitionRepository.findAllExhibitions()).hasSize(2).extracting(Exhibition::getName)
				.contains(exhibition.getName());
	}

	@Test
	public void testInsertMuseumWithSameNameInPostgresDatabaseShouldRollbackAndThrow() throws RepositoryException {
		Museum museum = createTestMuseum(MUSEUM1_TEST, NUM_CONSTANT1);

		assertThatThrownBy(() -> transactionManager.doInTransactionMuseum(museumRepository -> {
			return museumRepository.addMuseum(museum);
		})).isInstanceOf(RepositoryException.class);

		assertThat(postgresMuseumRepository.findAllMuseums()).hasSize(2).extracting(Museum::getName)
				.contains(museum.getName());
	}

	@Test
	public void testDeleteNullExhibitionInPostgresDatabaseShouldThrowAndRollback() {
		assertThatThrownBy(() -> transactionManager.doInTransactionExhibition(exhibitionRepository -> {
			exhibitionRepository.deleteExhibition(null);
			return null;
		})).isInstanceOf(RepositoryException.class);

		assertThat(postgresExhibitionRepository.findAllExhibitions()).hasSize(2);
	}

	@Test
	public void testDeleteRemovedExhibitionInPostgresDatabaseShouldThrowAndRollback() throws RepositoryException {
		Exhibition exhibition = postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_1);

		transactionManager.doInTransactionExhibition(exhibitionRepository -> {
			exhibitionRepository.deleteExhibition(exhibition);
			return null;
		});

		assertThatThrownBy(() -> transactionManager.doInTransactionExhibition(exhibitionRepository -> {
			exhibitionRepository.deleteExhibition(exhibition);
			return null;
		})).isInstanceOf(RepositoryException.class);

		assertThat(postgresExhibitionRepository.findAllExhibitions()).hasSize(1).extracting(Exhibition::getId)
				.doesNotContain(exhibition.getId()).contains(EXHIBITION_ID_2);
	}

	@Test
	public void testDeleteExhibitionInPostgresDatabaseCommit() throws RepositoryException {
		Exhibition exhibition = postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_1);

		transactionManager.doInTransactionExhibition(exhibitionRepository -> {
			exhibitionRepository.deleteExhibition(exhibition);
			return null;
		});

		assertThat(postgresExhibitionRepository.findAllExhibitions()).hasSize(1).extracting(Exhibition::getId)
				.containsExactly(EXHIBITION_ID_2);

	}

	@Test
	public void testDeleteNullMuseumInPostgresDatabaseShouldThrowAndRollback() {
		assertThatThrownBy(() -> transactionManager.doInTransactionMuseum(museumRepository -> {
			museumRepository.deleteMuseum(null);
			return null;
		})).isInstanceOf(RepositoryException.class);

		assertThat(postgresMuseumRepository.findAllMuseums()).hasSize(2);
	}

	@Test
	public void testDeleteMuseumReferencedMuseumByExhibitionInPostgresDatabaseShouldThrowAndRollback()
			throws RepositoryException {
		Museum museum = postgresMuseumRepository.findMuseumById(MUSEUM_ID_1);
		assertThatThrownBy(() -> transactionManager.doInTransactionMuseum(museumRepository -> {
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

		transactionManager.doInTransactionMuseum((museumRepository) -> {
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
		assertThatThrownBy(() -> transactionManager.doInTransactionMuseum((museumRepository) -> {
			return museumRepository.updateMuseum(null);
		})).isInstanceOf(RepositoryException.class);
	}

	@Test
	public void testUpdateRemovedMuseumInPosgresDatabaseShouldThrowAndRollback() {
		Museum museum = postgresMuseumRepository.findMuseumById(MUSEUM_ID_1);
		assertThatThrownBy(() -> transactionManager.doInTransactionMuseum((museumRepository) -> {
			museumRepository.deleteMuseum(museum);
			return museumRepository.updateMuseum(museum);
		})).isInstanceOf(RepositoryException.class);

		assertThat(postgresMuseumRepository.findAllMuseums()).hasSize(2).extracting(Museum::getId).contains(MUSEUM_ID_1,
				MUSEUM_ID_2);

	}

	@Test
	public void testUpdateMuseumInPostgresDatabaseCommit() throws RepositoryException {
		Museum museum = postgresMuseumRepository.findMuseumById(MUSEUM_ID_1);
		transactionManager.doInTransactionMuseum((museumRepository) -> {
			museum.setOccupiedRooms(5);
			museum.setRooms(50);
			return museumRepository.updateMuseum(museum);
		});

		assertThat(postgresMuseumRepository.findMuseumById(MUSEUM_ID_1).getOccupiedRooms()).isEqualTo(5);
		assertThat(postgresMuseumRepository.findMuseumById(MUSEUM_ID_1).getTotalRooms()).isEqualTo(50);
	}

	@Test
	public void testUpdateNullExhibitionInPostgresDatabaseShouldRollbackAndThrow() {
		assertThatThrownBy(() -> transactionManager.doInTransactionExhibition(exhibitionRepository -> {
			return exhibitionRepository.updateExhibition(null);
		})).isInstanceOf(RepositoryException.class);
	}

	@Test
	public void testUpdateRemovedExhibitionInPostgresDatabaseShouldRollbackAndThrow() throws RepositoryException {
		Exhibition exhibition = postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_1);

		assertThatThrownBy(() -> transactionManager.doInTransactionExhibition(exhibitionRepository -> {
			exhibitionRepository.deleteExhibition(exhibition);
			return exhibitionRepository.updateExhibition(exhibition);
		})).isInstanceOf(RepositoryException.class);

		assertThat(postgresExhibitionRepository.findAllExhibitions()).hasSize(2).extracting(Exhibition::getId)
				.contains(EXHIBITION_ID_1, EXHIBITION_ID_2);
	}

	@Test
	public void testUpdateExhibitionInPostgresDatabaseCommit() throws RepositoryException {
		Exhibition exhibition = postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_1);

		transactionManager.doInTransactionExhibition(exhibitionRepository -> {
			exhibition.setBookedSeats(5);
			exhibition.setTotalSeats(50);
			return exhibitionRepository.updateExhibition(exhibition);
		});

		assertThat(postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_1).getBookedSeats()).isEqualTo(5);
		assertThat(postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_1).getTotalSeats()).isEqualTo(50);
	}

	@After
	public void tearDown() {
		entityManager.clear();
		entityManager.close();
		sessionFactory.close();
		// This force to restart a docker container and re-execute the initialization
		// script provided for not empty database.
		ContainerDatabaseDriver.killContainer(JDBC_CONTAINER_URL);

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
