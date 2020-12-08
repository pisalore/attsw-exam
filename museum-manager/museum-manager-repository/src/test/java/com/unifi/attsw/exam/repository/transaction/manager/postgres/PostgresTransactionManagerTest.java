package com.unifi.attsw.exam.repository.transaction.manager.postgres;

import static org.assertj.core.api.Assertions.*;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testcontainers.jdbc.ContainerDatabaseDriver;

import com.unifi.attsw.exam.repository.model.Exhibition;
import com.unifi.attsw.exam.repository.model.Museum;
import com.unifi.attsw.exam.repository.repository.ExhibitionRepository;
import com.unifi.attsw.exam.repository.repository.MuseumRepository;
import com.unifi.attsw.exam.repository.repository.exception.RepositoryException;
import com.unifi.attsw.exam.repository.repository.postgres.PostgresExhibitionRepository;
import com.unifi.attsw.exam.repository.repository.postgres.PostgresMuseumRepository;
import com.unifi.attsw.exam.repository.transaction.manager.postgres.PostgresTransactionManager;

public class PostgresTransactionManagerTest {
	private final String JDBC_CONTAINER_URL = "jdbc:tc:postgresql:9.6.8:///databasenameTC_INITSCRIPT=file:src/test/resources/META-INF/postgres_init_scripts/init_postgresql_empty.sql";

	private static final UUID invalidUUID = UUID.fromString("2796027d-21cc-4883-b088-514d4b3090a1");

	private static final int NUM_CONSTANT1 = 10;
	private static final String MUSEUM1_TEST = "museum1_test";

	private static final String EXHIBITION1_TEST = "exhibition1_test";
	private static final String EXHIBITION2_TEST = "exhibition2_test";

	private MuseumRepository postgresMuseumRepository;
	private ExhibitionRepository postgresExhibitionRepository;
	private PostgresTransactionManager transactionManager;
	private static EntityManagerFactory sessionFactory;
	private static EntityManager entityManager;

	@Before
	public void setUp() throws Exception {
		sessionFactory = Persistence.createEntityManagerFactory("postgres");
		entityManager = sessionFactory.createEntityManager();
		postgresExhibitionRepository = new PostgresExhibitionRepository(entityManager);

		transactionManager = new PostgresTransactionManager(entityManager);
		postgresMuseumRepository = new PostgresMuseumRepository(transactionManager.getEntityManager());
		postgresExhibitionRepository = new PostgresExhibitionRepository(entityManager);

	}

	@Test
	public void testInsertNewMuseumInPostgresDatabaseTransactionallyCommit() throws RepositoryException {
		Museum museum = createTestMuseum(MUSEUM1_TEST, NUM_CONSTANT1);
		transactionManager.doInTransactionMuseum(museumRepository -> {
			return museumRepository.addMuseum(museum);
		});

		assertThat(postgresMuseumRepository.findAllMuseums()).containsExactly(museum);
	}

	@Test
	public void testInsertNullMuseumInPostgresDatabaseShouldRollbackAndThrow() throws RepositoryException {

		assertThatThrownBy(() -> transactionManager.doInTransactionMuseum((museumRepository) -> {
			return museumRepository.addMuseum(null);
		})).isInstanceOf(RepositoryException.class);

		assertThat(postgresMuseumRepository.findAllMuseums()).isEmpty();
	}

	@Test
	public void testInsertNullExhibitionInPostgresDatabaseShouldRollbackAndThrow() throws RepositoryException {

		assertThatThrownBy(() -> transactionManager.doInTransactionExhibition(exhibitionRepository -> {
			return exhibitionRepository.addNewExhibition(null);
		})).isInstanceOf(RepositoryException.class);

		assertThat(postgresExhibitionRepository.findAllExhibitions()).isEmpty();
	}

	@Test
	public void testInsertNewExhibitionWithoutMuseumShouldRollbackAndThrow() throws RepositoryException {
		Exhibition exhibition = createExhibition(EXHIBITION1_TEST, NUM_CONSTANT1);

		assertThatThrownBy(() -> transactionManager.doInTransactionExhibition(exhibitionRepository -> {
			return exhibitionRepository.addNewExhibition(exhibition);
		})).isInstanceOf(RepositoryException.class);
	}

	@Test
	public void testInsertExhibitionWithInvalidMuseumIdShouldRollbackandThrow() {
		Museum museum = createTestMuseum(MUSEUM1_TEST, 10);
		Exhibition exhibition = createExhibition(EXHIBITION1_TEST, NUM_CONSTANT1);

		assertThatThrownBy(() -> transactionManager.doInTransaction((museumRepository, exhibitionRepository) -> {
			exhibition.setMuseumId(invalidUUID);
			museumRepository.addMuseum(museum);
			exhibitionRepository.addNewExhibition(exhibition);
			return null;
		})).isInstanceOf(RepositoryException.class);
	}

	@Test
	public void testInsertNewExhibitionTransactionallyCommit() throws RepositoryException {
		Museum museum = createTestMuseum(MUSEUM1_TEST, 10);
		Exhibition exhibition = createExhibition(EXHIBITION1_TEST, NUM_CONSTANT1);

		transactionManager.doInTransaction((museumRepository, exhibitionRepository) -> {
			museumRepository.addMuseum(museum);
			exhibition.setMuseumId(museum.getId());
			exhibitionRepository.addNewExhibition(exhibition);
			return null;
		});

		assertThat(postgresMuseumRepository.findAllMuseums()).containsExactly(museum);
		assertThat(postgresExhibitionRepository.findAllExhibitions()).containsExactly(exhibition)
				.extracting(Exhibition::getMuseumId).contains(museum.getId());
	}

	@Test
	public void testInsertSeveralExhibitionWithWrongMuseumIdInPostgresDatabaseShouldRollbackAndThrow()
			throws RepositoryException {
		Museum museum = createTestMuseum(MUSEUM1_TEST, 10);
		Exhibition exhibition1 = createExhibition(EXHIBITION1_TEST, NUM_CONSTANT1);
		Exhibition exhibition2 = createExhibition(EXHIBITION2_TEST, NUM_CONSTANT1);

		assertThatThrownBy(() -> transactionManager.doInTransaction((museumRepository, exhibitionRepository) -> {
			museumRepository.addMuseum(museum);
			exhibition1.setMuseumId(museum.getId());
			exhibition2.setMuseumId(invalidUUID);
			exhibitionRepository.addNewExhibition(exhibition1);
			exhibitionRepository.addNewExhibition(exhibition2);
			return null;
		})).isInstanceOf(RepositoryException.class);

		assertThat(postgresMuseumRepository.findAllMuseums()).isEmpty();
		assertThat(postgresExhibitionRepository.findAllExhibitions()).isEmpty();
	}

	@Test
	public void testInsertSeveralExhibitionToOneMuseumInPostgresDatabaseCommit() throws RepositoryException {
		Museum museum = createTestMuseum(MUSEUM1_TEST, 10);
		Exhibition exhibition1 = createExhibition(EXHIBITION1_TEST, NUM_CONSTANT1);
		Exhibition exhibition2 = createExhibition(EXHIBITION2_TEST, NUM_CONSTANT1);

		transactionManager.doInTransaction((museumRepository, exhibitionRepository) -> {
			museumRepository.addMuseum(museum);
			exhibition1.setMuseumId(museum.getId());
			exhibition2.setMuseumId(museum.getId());
			exhibitionRepository.addNewExhibition(exhibition1);
			exhibitionRepository.addNewExhibition(exhibition2);
			return null;
		});

		assertThat(postgresMuseumRepository.findAllMuseums()).containsExactly(museum);
		assertThat(postgresExhibitionRepository.findAllExhibitions()).containsExactly(exhibition1, exhibition2)
				.extracting(Exhibition::getMuseumId).contains(museum.getId());
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
