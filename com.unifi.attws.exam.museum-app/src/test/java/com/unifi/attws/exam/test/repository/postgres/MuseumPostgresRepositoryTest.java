package com.unifi.attws.exam.test.repository.postgres;

import static org.assertj.core.api.Assertions.*;

import java.util.UUID;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import com.unifi.attws.exam.model.Museum;
import com.unifi.attws.exam.repository.MuseumRepository;
import com.unifi.attws.exam.repository.postgres.PostgresMuseumRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MuseumPostgresRepositoryTest {

	MuseumRepository postgresMuseumRepository;
	static EntityManager entityManager;

	private static final Logger LOGGER = LogManager.getLogger(MuseumPostgresRepositoryTest.class);

	@Before
	public void setUp() throws Exception {
		EntityManagerFactory sessionFactory = Persistence.createEntityManagerFactory("postgres");
		entityManager = sessionFactory.createEntityManager();
		postgresMuseumRepository = new PostgresMuseumRepository(entityManager);
	}

	@Test
	public void testfindAllMuseumsMuseumsWhenNoMuseumsArePersisted() {
		assertThat(postgresMuseumRepository.findAllMuseums()).isEmpty();

	}

	@Test
	public void testFindMuseumByIdWhenNoMuseumsArePresent() {
		assertThat(postgresMuseumRepository.retrieveMuseumById(UUID.randomUUID())).isNull();
	}

	@Test
	public void testAddNewMuseumToPostgresDBWhenTransactionSuccess() {
		Museum museum = createTestMuseum("MoMa", 10);
		addMuseumToPostgresDButility(museum);
		assertThat(postgresMuseumRepository.findAllMuseums()).containsExactly(museum);

	}


	@Test
	public void testFindAllMuseumsWhenSeveralMuseumsArePersisted() {
		Museum museum1 = createTestMuseum("Uffizi", 50);
		Museum museum2 = createTestMuseum("Louvre", 10);
		addMuseumToPostgresDButility(museum1);
		addMuseumToPostgresDButility(museum2);
		assertThat(postgresMuseumRepository.findAllMuseums()).containsExactly(museum1, museum2);

	}

	@Test
	public void testFindMuseumByIdWhenMuseumIsPresent() {
		Museum museum1 = createTestMuseum("Pompidou", 50);
		Museum museum2 = createTestMuseum("Louvre", 10);
		addMuseumToPostgresDButility(museum1);
		addMuseumToPostgresDButility(museum2);
		assertThat(postgresMuseumRepository.retrieveMuseumById(museum1.getId())).isEqualTo(museum1);
	}

	@Test
	public void testUpdateMuseumWhenExists() {
		Museum museum1 = createTestMuseum("Pompidou", 50);
		addMuseumToPostgresDButility(museum1);
		Museum museum2 = postgresMuseumRepository.retrieveMuseumById(museum1.getId());
		museum2.setOccupiedRooms(1);
		addMuseumToPostgresDButility(museum2);
		assertThat(postgresMuseumRepository.retrieveMuseumById(museum1.getId()).getOccupiedRooms()).isEqualTo(1);
		assertThat(postgresMuseumRepository.findAllMuseums()).containsExactly(museum1);

	}

	// @Test
	public void testMuseumToRemoveWhenTheMuseumExists() {
		Museum museum1 = createTestMuseum("Pompidou", 50);
		postgresMuseumRepository.addMuseum(museum1);
		postgresMuseumRepository.deleteMuseum(museum1);
		assertThat(postgresMuseumRepository.findAllMuseums()).isEmpty();
	}

	@AfterClass
	public static void closeEntityManager() {
		entityManager.close();
	}

	/*
	 * Museum utility
	 */

	public Museum createTestMuseum(String museumName, int numOfRooms) {
		return new Museum(museumName, numOfRooms);
	}
	
	public void addMuseumToPostgresDButility(Museum museum) {
		entityManager.getTransaction().begin();
		postgresMuseumRepository.addMuseum(museum);
		entityManager.getTransaction().commit();
		
	}

}
