package com.unifi.attsw.exam.test.transaction.manager.postgres;

import static org.assertj.core.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.unifi.attsw.exam.exception.RepositoryException;
import com.unifi.attsw.exam.model.Exhibition;
import com.unifi.attsw.exam.model.Museum;
import com.unifi.attsw.exam.repository.ExhibitionRepository;
import com.unifi.attsw.exam.repository.MuseumRepository;
import com.unifi.attsw.exam.repository.postgres.PostgresExhibitionRepository;
import com.unifi.attsw.exam.repository.postgres.PostgresMuseumRepository;
import com.unifi.attsw.exam.transaction.manager.postgres.PostgresTransactionManager;

public class PostgresTransactionManagerTest {

	private static final int NUM_CONSTANT1 = 10;
	private static final String MUSEUM1_TEST = "museum1_test";
	private static final String EXHIBITION1_TEST = "exhibition1_test";

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
		transactionManager.doInTransaction((museumRepository, exhibitionRepository) -> {
			return museumRepository.addMuseum(museum);
		});

		assertThat(postgresMuseumRepository.findAllMuseums()).containsExactly(museum);
	}

	@Test
	public void testInsertNullMuseumInPostgresDatabaseShouldRollbackAndThrow() throws RepositoryException {

		assertThatThrownBy(() -> transactionManager.doInTransaction((museumRepository, exhibitionRepository) -> {
			return museumRepository.addMuseum(null);
		})).isInstanceOf(RepositoryException.class);

		assertThat(postgresMuseumRepository.findAllMuseums()).isEmpty();
	}

	@Test
	public void testInsertNullExhibitionInPostgresDatabaseShouldRollbackAndThrow() throws RepositoryException {

		assertThatThrownBy(() -> transactionManager.doInTransaction((museumRepository, exhibitionRepository) -> {
			return exhibitionRepository.addNewExhibition(null);
		})).isInstanceOf(RepositoryException.class);

		assertThat(postgresExhibitionRepository.findAllExhibitions()).isEmpty();
	}

	@Test
	public void testInsertNewExhibitionWithoutMuseumShouldRollbackAndThrow() throws RepositoryException {
		Exhibition exhibition = createExhibition(EXHIBITION1_TEST, NUM_CONSTANT1);

		assertThatThrownBy(() -> transactionManager.doInTransaction((museumRepository, exhibitionRepository) -> {
			return exhibitionRepository.addNewExhibition(exhibition);
		})).isInstanceOf(RepositoryException.class);
	}

	@Test
	public void testInsertNewExhibitionTransactionallyCommit() throws RepositoryException {
		Museum museum = createTestMuseum(MUSEUM1_TEST, 10);
		Exhibition exhibition = createExhibition(EXHIBITION1_TEST, NUM_CONSTANT1);

		transactionManager.doInTransaction((museumRepository, exhibitionRepository) -> {
			museumRepository.addMuseum(museum);
			exhibition.setMuseumId(museum.getId());
			return exhibitionRepository.addNewExhibition(exhibition);
		});

		assertThat(postgresMuseumRepository.findAllMuseums()).containsExactly(museum);
		assertThat(postgresExhibitionRepository.findAllExhibitions()).containsExactly(exhibition)
				.extracting(Exhibition::getMuseumId).contains(museum.getId());
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
