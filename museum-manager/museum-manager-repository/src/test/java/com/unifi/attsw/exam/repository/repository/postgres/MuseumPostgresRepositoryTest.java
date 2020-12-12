package com.unifi.attsw.exam.repository.repository.postgres;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.unifi.attsw.exam.repository.model.Museum;
import com.unifi.attsw.exam.repository.repository.MuseumRepository;
import com.unifi.attsw.exam.repository.repository.postgres.PostgresMuseumRepository;

public class MuseumPostgresRepositoryTest {

	private static final UUID MUSEUM_ID_1 = UUID.fromString("b433da18-ba5a-4b86-92af-ba11be6314e7");
	private static final UUID MUSEUM_ID_2 = UUID.fromString("94fe3013-9ebb-432e-ab55-e612dc797851");
	private static final String MUSEUM_TEST_1 = "museum1_test";
	private static final String MUSEUM_TEST_2 = "museum2_test";
	private static final int NUM_OF_ROOMS = 10;
	private static final String MUSEUM_NOT_PERSISTED_TEST = "museum_not_persisted_test";

	private static final UUID invalidUUID = UUID.fromString("2796027d-21cc-4883-b088-514d4b3090a1");

	private MuseumRepository postgresMuseumRepository;
	private static EntityManagerFactory sessionFactory;
	private static EntityManager entityManager;

	@BeforeClass
	public static void beforeClass() {
		sessionFactory = Persistence.createEntityManagerFactory("postgres");
	}

	@Before
	public void setUp() throws Exception {
		entityManager = sessionFactory.createEntityManager();
		postgresMuseumRepository = new PostgresMuseumRepository(entityManager);

		entityManager.getTransaction().begin();
		entityManager.createNativeQuery("TRUNCATE TABLE Museums CASCADE").executeUpdate();
		entityManager.getTransaction().commit();

	}

	@Test
	public void testfindAllMuseumsMuseumsWhenNoMuseumsArePersisted() {
		assertThat(postgresMuseumRepository.findAllMuseums()).isEmpty();

	}

	@Test
	public void testFindMuseumByNullIdShouldThrow() {
		assertThatThrownBy(() -> postgresMuseumRepository.findMuseumById(null))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void testFindMuseumByIdWhenNoMuseumsArePresent() {
		assertThatThrownBy(() -> postgresMuseumRepository.findMuseumById(invalidUUID))
				.isInstanceOf(NoSuchElementException.class).hasMessage("Cannot find entity with id: " + invalidUUID);
		;
	}

	@Test
	public void testFindMuseumByNullName() {
		assertThat(postgresMuseumRepository.findMuseumByName(null)).isNull();

	}

	@Test
	public void testAddNewNullMuseumEntityShouldThrow() {
		assertThatThrownBy(() -> postgresMuseumRepository.addMuseum(null)).isInstanceOf(IllegalArgumentException.class);
		assertThat(postgresMuseumRepository.findAllMuseums()).isEmpty();
	}

	@Test
	public void testAddNewMuseum() {
		Museum museum = createTestMuseum(MUSEUM_TEST_1, NUM_OF_ROOMS);

		entityManager.getTransaction().begin();
		postgresMuseumRepository.addMuseum(museum);
		entityManager.getTransaction().commit();
		assertThat(postgresMuseumRepository.findAllMuseums()).hasSize(1).extracting(Museum::getId)
				.contains(museum.getId());
	}

	@Test
	public void testFindMuseumByIdWhenMuseumIsPresent() {
		populateDatabase();
		Museum museum1 = postgresMuseumRepository.findMuseumById(MUSEUM_ID_1);
		assertThat(postgresMuseumRepository.findMuseumById(museum1.getId())).isEqualTo(museum1);
	}

	@Test
	public void testFindMuseumByNameWhenMuseumIsPresent() {
		populateDatabase();
		List<Museum> persistedMuseums = postgresMuseumRepository.findAllMuseums();
		Museum museum1 = postgresMuseumRepository.findMuseumByName(MUSEUM_TEST_1);
		assertThat(persistedMuseums).extracting(Museum::getName).contains(museum1.getName());
	}

	@Test
	public void testFindMuseumByNameOfNotExistingMuseumReturnsNull() {
		populateDatabase();
		assertThat(postgresMuseumRepository.findMuseumByName(MUSEUM_NOT_PERSISTED_TEST)).isNull();

	}

	@Test
	public void testAddDetachedEntityMuseumChangingIdShouldThrow() {
		populateDatabase();
		List<Museum> persistedMuseums = postgresMuseumRepository.findAllMuseums();
		Museum museum1 = postgresMuseumRepository.findMuseumById(MUSEUM_ID_1);
		Museum museum2 = createTestMuseum(MUSEUM_TEST_1, NUM_OF_ROOMS);
		museum2.setId(museum1.getId());
		assertThatThrownBy(() -> postgresMuseumRepository.addMuseum(museum2)).isInstanceOf(PersistenceException.class);
		assertThat(postgresMuseumRepository.findAllMuseums()).isEqualTo(persistedMuseums);
	}

	@Test
	public void testAddNullEntityShouldThrow() {
		assertThatThrownBy(() -> postgresMuseumRepository.addMuseum(null)).isInstanceOf(IllegalArgumentException.class);

	}

	@Test
	public void testUpdateMuseumWhenEntityHasBeenRemovedShouldThrow() {
		populateDatabase();
		Museum museum1 = postgresMuseumRepository.findMuseumById(MUSEUM_ID_1);
		postgresMuseumRepository.deleteMuseum(museum1);
		museum1.setName(MUSEUM_TEST_2);
		assertThatThrownBy(() -> postgresMuseumRepository.updateMuseum(museum1))
				.isInstanceOf(IllegalArgumentException.class);

	}

	@Test
	public void testUpdateMuseumWithNullEntityObjectShouldThrow() {
		populateDatabase();
		List<Museum> persistedMuseums = postgresMuseumRepository.findAllMuseums();
		assertThatThrownBy(() -> postgresMuseumRepository.updateMuseum(null))
				.isInstanceOf(IllegalArgumentException.class);
		assertThat(postgresMuseumRepository.findAllMuseums()).isEqualTo(persistedMuseums);
	}

	@Test
	public void testUpdateMuseumWhenExists() {
		populateDatabase();
		List<Museum> persistedMuseums = postgresMuseumRepository.findAllMuseums();
		Museum museum1 = postgresMuseumRepository.findMuseumById(MUSEUM_ID_1);
		museum1.setOccupiedRooms(1);
		entityManager.getTransaction().begin();
		postgresMuseumRepository.updateMuseum(museum1);
		entityManager.getTransaction().commit();
		assertThat(postgresMuseumRepository.findMuseumById(museum1.getId()).getOccupiedRooms()).isEqualTo(1);
		assertThat(postgresMuseumRepository.findAllMuseums()).containsAll(persistedMuseums);

	}

	@Test
	public void testRemoveDetachedEntityMuseumChangingIdShouldThrow() {
		populateDatabase();
		List<Museum> persistedMuseums = postgresMuseumRepository.findAllMuseums();
		Museum museum1 = postgresMuseumRepository.findMuseumById(MUSEUM_ID_1);
		Museum museum2 = new Museum(MUSEUM_TEST_2, NUM_OF_ROOMS);
		museum2.setId(museum1.getId());

		assertThatThrownBy(() -> postgresMuseumRepository.deleteMuseum(museum2))
				.isInstanceOf(IllegalArgumentException.class);
		assertThat(postgresMuseumRepository.findAllMuseums()).isEqualTo(persistedMuseums);

	}

	@Test
	public void testRemoveNullEntityObjectShouldThrow() {
		populateDatabase();
		List<Museum> persistedMuseums = postgresMuseumRepository.findAllMuseums();
		assertThatThrownBy(() -> postgresMuseumRepository.deleteMuseum(null))
				.isInstanceOf(IllegalArgumentException.class);
		assertThat(postgresMuseumRepository.findAllMuseums()).isEqualTo(persistedMuseums);
	}

	@Test
	public void testRemoveMuseumWhenTheMuseumExistsAndHasNotExhibitions() {
		populateDatabase();
		Museum museum1 = postgresMuseumRepository.findMuseumById(MUSEUM_ID_2);
		entityManager.getTransaction().begin();
		postgresMuseumRepository.deleteMuseum(museum1);
		entityManager.getTransaction().commit();
		assertThat(postgresMuseumRepository.findAllMuseums()).hasSize(1);
	}

	@After
	public void closeEntityManager() {
		entityManager.clear();
		entityManager.close();
	}

	/*
	 * Museum utility
	 */

	public Museum createTestMuseum(String museumName, int numOfRooms) {
		return new Museum(museumName, numOfRooms);
	}

	/*
	 * Utility method to populate database with data
	 */

	private void populateDatabase() {
		entityManager.getTransaction().begin();
		entityManager
				.createNativeQuery("INSERT INTO museums (id, museum_name, number_of_occupied_rooms, number_of_rooms)"
						+ "VALUES ( 'b433da18-ba5a-4b86-92af-ba11be6314e7' , 'museum1_test', 0, 10);")
				.executeUpdate();
		entityManager
				.createNativeQuery("INSERT INTO museums (id, museum_name, number_of_occupied_rooms, number_of_rooms)"
						+ "VALUES ( '94fe3013-9ebb-432e-ab55-e612dc797851' , 'museum2_test', 0, 10);")
				.executeUpdate();

		entityManager.getTransaction().commit();
	}

}
