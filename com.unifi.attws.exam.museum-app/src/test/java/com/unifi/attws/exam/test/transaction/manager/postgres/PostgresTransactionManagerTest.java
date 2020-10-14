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
import com.unifi.attws.exam.model.Museum;
import com.unifi.attws.exam.repository.MuseumRepository;
import com.unifi.attws.exam.repository.postgres.PostgresMuseumRepository;
import com.unifi.attws.exam.transaction.manager.postgres.PostgresTransactionManager;

public class PostgresTransactionManagerTest {

	private MuseumRepository postgresMuseumRepository;
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

	}

	@Test
	public void testInsertNewMuseumInPostgresDatabaseTransactionallyCommit() throws RepositoryException {
		Museum museum = createTestMuseum("Uffizi", 10);
		transactionManager.doInTransaction((museumRepository, exhibitionRepository) -> {
			return postgresMuseumRepository.addMuseum(museum);
		});

		assertThat(postgresMuseumRepository.findAllMuseums()).contains(museum);
		assertThat(postgresMuseumRepository.findAllMuseums()).hasSize(1);
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

	@After
	public void closeEntityManager() {
		entityManager.clear();
		entityManager.close();
	}

	@AfterClass
	public static void closeSessionFactory() {
		sessionFactory.close();
	}

	public Museum createTestMuseum(String museumName, int numOfRooms) {
		return new Museum(museumName, numOfRooms);
	}

}
