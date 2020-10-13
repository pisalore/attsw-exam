package com.unifi.attws.exam.test.repository.postgres;

import static org.assertj.core.api.Assertions.*;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.unifi.attws.exam.model.Museum;
import com.unifi.attws.exam.repository.MuseumRepository;
import com.unifi.attws.exam.repository.postgres.PostgresMuseumRepository;

public class MuseumPostgresRepositoryTest {

	private MuseumRepository postgresMuseumRepository;
	private static EntityManager entityManager;
	
	@Before
	public void setUp() throws Exception {
		EntityManagerFactory sessionFactory = Persistence.createEntityManagerFactory("postgres");
		entityManager = sessionFactory.createEntityManager();
		postgresMuseumRepository = new PostgresMuseumRepository(entityManager);
		entityManager.getTransaction().begin();

	}

	@Test
	public void testfindAllMuseumsMuseumsWhenNoMuseumsArePersisted() {
		assertThat(postgresMuseumRepository.findAllMuseums()).isEmpty();

	}
	
	@Test
	public void testFindMuseumByNullIdShouldThrow() {
		assertThatThrownBy(() -> postgresMuseumRepository.retrieveMuseumById(null))
		.isInstanceOf(IllegalArgumentException.class);	
	}
	
	@Test
	public void testFindMuseumByIdWhenNoMuseumsArePresent() {
		assertThat(postgresMuseumRepository.retrieveMuseumById(UUID.randomUUID())).isNull();
	}
	
	@Test
	public void testAddNewNullMuseumEntityShouldThrow() {
		assertThatThrownBy(() -> postgresMuseumRepository.addMuseum(null)).isInstanceOf(IllegalArgumentException.class);
		
		assertThat(postgresMuseumRepository.findAllMuseums()).isEmpty();
	}

	@Test
	public void testAddNewMuseum() {
		Museum museum = createTestMuseum("MoMa", 10);
		postgresMuseumRepository.addMuseum(museum);
		entityManager.flush();
		assertThat(postgresMuseumRepository.findAllMuseums())
		.hasSize(1)
		.extracting(Museum::getId)
		.contains(museum.getId());
	}


	@Test
	public void testFindMuseumByIdWhenMuseumIsPresent() {
		Museum museum1 = createTestMuseum("Pompidou", 50);
		Museum museum2 = createTestMuseum("Louvre", 10);
		postgresMuseumRepository.addMuseum(museum1);
		postgresMuseumRepository.addMuseum(museum2);
		entityManager.flush();
		assertThat(postgresMuseumRepository.retrieveMuseumById(museum1.getId())).isEqualTo(museum1);
	}
	

	@Test
	public void testUpdateMuseumWhenExists() {
		Museum museum1 = createTestMuseum("Pompidou", 50);
		postgresMuseumRepository.addMuseum(museum1);
		Museum museum2 = postgresMuseumRepository.retrieveMuseumById(museum1.getId());
		museum2.setOccupiedRooms(1);
		postgresMuseumRepository.updateMuseum(museum2);
		entityManager.flush();
		assertThat(postgresMuseumRepository.retrieveMuseumById(museum1.getId()).getOccupiedRooms()).isEqualTo(1);
		assertThat(postgresMuseumRepository.findAllMuseums()).containsExactly(museum1);

	}

	
	@After
	public void closeEntityManager() {
		entityManager.getTransaction().commit();
		entityManager.clear();
		entityManager.close();
	}

	/*
	 * Museum utility
	 */

	public Museum createTestMuseum(String museumName, int numOfRooms) {
		return new Museum(museumName, numOfRooms);
	}

}
