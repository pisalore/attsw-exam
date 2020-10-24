package com.unifi.attsw.exam.test.repository.postgres;

import static org.assertj.core.api.Assertions.*;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.unifi.attsw.exam.model.Museum;
import com.unifi.attsw.exam.repository.MuseumRepository;
import com.unifi.attsw.exam.repository.postgres.PostgresMuseumRepository;

public class MuseumPostgresRepositoryTest {

	private static final String MUSEUM_TEST_1 = "museum1_test";
	private static final int NUM_OF_ROOMS = 10;
	private static final UUID invalidUUID = UUID.fromString("2796027d-21cc-4883-b088-514d4b3090a1");

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
		assertThatThrownBy(() -> postgresMuseumRepository.findMuseumById(null))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Cannot find entity, invalid or null id: " + null);

		assertThat(postgresMuseumRepository.findAllMuseums()).isEmpty();
	}

	@Test
	public void testFindMuseumByIdWhenNoMuseumsArePresent() {
		assertThat(postgresMuseumRepository.findMuseumById(invalidUUID)).isNull();
	}

	@Test
	public void testAddNewNullMuseumEntityShouldThrow() {
		assertThatThrownBy(() -> postgresMuseumRepository.addMuseum(null)).isInstanceOf(IllegalArgumentException.class);
		assertThat(postgresMuseumRepository.findAllMuseums()).isEmpty();
	}

	@Test
	public void testAddNewMuseum() {
		Museum museum = createTestMuseum(MUSEUM_TEST_1, NUM_OF_ROOMS);
		postgresMuseumRepository.addMuseum(museum);
		entityManager.flush();
		assertThat(postgresMuseumRepository.findAllMuseums()).hasSize(1).extracting(Museum::getId)
				.contains(museum.getId());
	}

	@After
	public void closeEntityManager() {
		entityManager.getTransaction().rollback();
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
