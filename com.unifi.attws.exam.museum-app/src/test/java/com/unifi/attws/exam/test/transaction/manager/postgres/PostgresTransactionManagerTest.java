package com.unifi.attws.exam.test.transaction.manager.postgres;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import com.unifi.attws.exam.exception.RepositoryException;
import com.unifi.attws.exam.model.Exhibition;
import com.unifi.attws.exam.model.Museum;
import com.unifi.attws.exam.repository.ExhibitionRepository;
import com.unifi.attws.exam.repository.MuseumRepository;
import com.unifi.attws.exam.repository.postgres.PostgresExhibitionRepository;
import com.unifi.attws.exam.repository.postgres.PostgresMuseumRepository;
import com.unifi.attws.exam.transaction.manager.postgres.PostgresTransactionManager;

public class PostgresTransactionManagerTest {

	private MuseumRepository postgresMuseumRepository;
	private ExhibitionRepository postgresExhibitionRepository;
	private PostgresTransactionManager transactionManager;
	private static EntityManagerFactory sessionFactory;
	private static EntityManager entityManager;

	@Rule
	public final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER = new PostgreSQLContainer<>().withDatabaseName("attws")
			.withUsername("attws").withPassword("attws");

	@Before
	public void setUp() throws Exception {
		Map<String, String> properties = new HashMap<>();
		properties.put("javax.persistence.jdbc.url", POSTGRE_SQL_CONTAINER.getJdbcUrl());
		properties.put("javax.persistence.jdbc.user", POSTGRE_SQL_CONTAINER.getUsername());
		properties.put("javax.persistence.jdbc.password", POSTGRE_SQL_CONTAINER.getPassword());
		properties.put("javax.persistence.jdbc.driver", POSTGRE_SQL_CONTAINER.getDriverClassName());
		properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
		properties.put("hibernate.hbm2ddl.auto", "create");

		sessionFactory = Persistence.createEntityManagerFactory("postgres.transaction-manager", properties);

		entityManager = sessionFactory.createEntityManager();

		transactionManager = new PostgresTransactionManager(entityManager);
		postgresMuseumRepository = new PostgresMuseumRepository(transactionManager.getEntityManager());
		postgresExhibitionRepository = new PostgresExhibitionRepository(entityManager);

	}

	@Test
	public void testInsertNewMuseumInPostgresDatabaseTransactionallyCommit() throws RepositoryException {
		Museum museum = createTestMuseum("Uffizi", 10);
		transactionManager.doInTransaction((museumRepository, exhibitionRepository) -> {
			return postgresMuseumRepository.addMuseum(museum);
		});

		assertThat(postgresMuseumRepository.findAllMuseums()).containsExactly(museum);
	}

	@Test
	public void testInsertMuseumWithSameNameInPostgresDatabaseShouldRollbackAndThrow() throws RepositoryException {
		Museum museum1 = createTestMuseum("Uffizi", 10);
		Museum museum2 = createTestMuseum("Uffizi", 10);

		transactionManager.doInTransaction((museumRepository, exhibitionRepository) -> {
			return postgresMuseumRepository.addMuseum(museum1);
		});

		assertThatThrownBy(() -> transactionManager.doInTransaction((museumRepository, exhibitionRepository) -> {
			return postgresMuseumRepository.addMuseum(museum2);
		})).isInstanceOf(RepositoryException.class);

		assertThat(postgresMuseumRepository.findAllMuseums()).contains(museum1);
	}

	@Test
	public void testInsertNewExhibitionWithoutMuseumShouldRollbackAndThrow() throws RepositoryException {
		Exhibition exhibition = createExhibition("exhibition", 10);

		assertThatThrownBy(() -> transactionManager.doInTransaction((museumRepository, exhibitionRepository) -> {
			return exhibitionRepository.addNewExhibition(exhibition);
		})).isInstanceOf(RepositoryException.class);
	}

	@Test
	public void testInsertNewExhibitionTransactionallyCommit() throws RepositoryException {
		Museum museum = createTestMuseum("Uffizi", 10);
		Exhibition exhibition = createExhibition("exhibition", 10);

		transactionManager.doInTransaction((museumRepository, exhibitionRepository) -> {
			postgresMuseumRepository.addMuseum(museum);
			exhibition.setMuseumId(museum.getId());
			return exhibitionRepository.addNewExhibition(exhibition);
		});

		assertThat(postgresMuseumRepository.findAllMuseums()).containsExactly(museum);
		assertThat(postgresExhibitionRepository.findAllExhibitions())
			.containsExactly(exhibition)
			.extracting(Exhibition::getMuseumId)
			.contains(museum.getId());
	}

	@After
	public void closeEntityManager() {
		entityManager.clear();
		entityManager.close();
	}

	@AfterClass
	public static void closeSessionFactory() {
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
