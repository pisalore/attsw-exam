package com.unifi.attws.exam.test.transaction.manager.postgres;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
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

public class PostgresTransactionManagerWithNoEmptyDatabaseTest {

	private static final int NUM_CONSTANT1 = 10;
	private static final String MUSEUM1_TEST = "museum1_test";

	private MuseumRepository postgresMuseumRepository;
	private PostgresTransactionManager transactionManager;
	private EntityManagerFactory sessionFactory;
	private static EntityManager entityManager;


	@Before
	public void setUp() throws Exception {		
		sessionFactory = Persistence.createEntityManagerFactory("postgres.not-empty.database");
		entityManager = sessionFactory.createEntityManager();
		transactionManager = new PostgresTransactionManager(entityManager);
		
		postgresMuseumRepository = new PostgresMuseumRepository(transactionManager.getEntityManager());
	}


	@Test
	public void testInsertMuseumWithSameNameInPostgresDatabaseShouldRollbackAndThrow() throws RepositoryException {
		Museum museum = createTestMuseum(MUSEUM1_TEST, NUM_CONSTANT1);

		assertThatThrownBy(() -> transactionManager.doInTransaction((museumRepository, exhibitionRepository) -> {
			return museumRepository.addMuseum(museum);
		})).isInstanceOf(RepositoryException.class);

		assertThat(postgresMuseumRepository.findAllMuseums())
			.extracting(Museum::getName)
			.contains(museum.getName());
	}
	
	@After
	public void tearDown() {
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
