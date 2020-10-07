package com.unifi.attws.exam.test.repository.postgres;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Before;
import org.junit.Test;

import com.unifi.attws.exam.model.Museum;
import com.unifi.attws.exam.repository.MuseumRepository;
import com.unifi.attws.exam.repository.postgres.PostgresMuseumRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MuseumPostgresRepositoryTest {
	
	MuseumRepository postgresMuseumRepository;
	
	private static final Logger LOGGER = LogManager.getLogger(MuseumPostgresRepositoryTest.class);

	@Before
	public void setUp() throws Exception {
		EntityManagerFactory sessionFactory = Persistence.createEntityManagerFactory("postgres");
		EntityManager entityManager = sessionFactory.createEntityManager();
		postgresMuseumRepository = new PostgresMuseumRepository(entityManager);

	}

	@Test
	public void testfindAllMuseumsMuseumsWhenNoMuseumsArePersisted() {
		assertThat(postgresMuseumRepository.findAllMuseums()).isEmpty();

	}
	
	@Test
	public void testfindAllMuseumsMuseumsWhenSeveralMuseumsArePersisted() {
		Museum museum1 = createTestMuseum("Uffizi", 50);
		Museum museum2 = createTestMuseum("Louvre", 10);
		postgresMuseumRepository.addMuseum(museum1);
		postgresMuseumRepository.addMuseum(museum2);
		assertThat(postgresMuseumRepository.findAllMuseums()).containsExactly(museum1, museum2);

	}
	
	
	@Test
	public void testAddNewMuseumToPostgresDB() {
		Museum museum = createTestMuseum("MoMa", 10);
		postgresMuseumRepository.addMuseum(museum);
		assertThat(postgresMuseumRepository.findAllMuseums()).containsExactly(museum);
		
	}
	
	@Test
	public void testFindMuseumByIdWhenNoMuseumsArePresent() {
		assertThat(postgresMuseumRepository.retrieveMuseumById(new Long(1))).isNull();
	}
	
	@Test
	public void testFindMuseumByIdWhenMuseumIsPresent() {
		Museum museum1 = createTestMuseum("Pompidou", 50);
		Museum museum2 = createTestMuseum("Louvre", 10);
		postgresMuseumRepository.addMuseum(museum1);
		postgresMuseumRepository.addMuseum(museum2);
		assertThat(postgresMuseumRepository.retrieveMuseumById(new Long(1))).isEqualTo(museum1);
	}
	
	@Test
	public void testUpdateMuseumWhenExists() {
		Museum museum1 = createTestMuseum("Pompidou", 50);
		postgresMuseumRepository.addMuseum(museum1);
		Museum museum2 = postgresMuseumRepository.retrieveMuseumById(new Long(1));
		museum2.setOccupiedRooms(1);
		postgresMuseumRepository.updateMuseum(museum2);
		assertThat(postgresMuseumRepository.retrieveMuseumById(new Long(1)).getOccupiedRooms()).isEqualTo(1);

	}
	
	@Test
	public void testMuseumToRemoveWhenTheMuseumExists() {
		Museum museum1 = createTestMuseum("Pompidou", 50);
		postgresMuseumRepository.addMuseum(museum1);
		postgresMuseumRepository.deleteMuseum(museum1);
		assertThat(postgresMuseumRepository.findAllMuseums()).isEmpty();
	}
	
	
	/*
	 * Museum utility
	 */
	
	public Museum createTestMuseum(String museumName, int numOfRooms) {
		return new Museum(museumName, numOfRooms);
	}
	

}
