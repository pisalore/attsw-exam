package com.unifi.attws.exam.test.repository.postgres;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.unifi.attws.exam.model.Museum;
import com.unifi.attws.exam.repository.MuseumRepository;
import com.unifi.attws.exam.repository.postgres.PostgresMuseumRepository;

public class MuseumPostgresRepositoryWithNotEmptyDatabaseTest {

	private MuseumRepository postgresMuseumRepository;
	private static EntityManager entityManager;

	private static final Logger LOGGER = LogManager.getLogger(MuseumPostgresRepositoryTest.class);

	@Before
	public void setUp() throws Exception {
		EntityManagerFactory sessionFactory = Persistence.createEntityManagerFactory("postgres.not-empty.database");
		entityManager = sessionFactory.createEntityManager();
		postgresMuseumRepository = new PostgresMuseumRepository(entityManager);
		entityManager.getTransaction().begin();

	}

	@Test
	public void testfindAllMuseumsMuseumsWhenNoMuseumsArePersisted() {
		LOGGER.info(postgresMuseumRepository.findAllMuseums());
		assertThat(postgresMuseumRepository.findAllMuseums()).isNotEmpty();

	}

	@Test
	public void testAddMuseumWithExistingIdShouldThrow() {
		Museum museum1 = postgresMuseumRepository
				.retrieveMuseumById(UUID.fromString("b433da18-ba5a-4b86-92af-ba11be6314e7"));
		Museum museum2 = new Museum("test3", 20);
		museum2.setId(museum1.getId());

		assertThatThrownBy(() -> postgresMuseumRepository.addMuseum(museum2))
			.isInstanceOf(PersistenceException.class);
		
		assertThat(postgresMuseumRepository.findAllMuseums()).hasSize(2);
		
	}

	@After
	public void closeEntityManager() {
		entityManager.getTransaction().rollback();
		entityManager.clear();
		entityManager.close();
	}

}
