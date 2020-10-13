package com.unifi.attws.exam.test.repository.postgres;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.unifi.attws.exam.model.Museum;
import com.unifi.attws.exam.repository.MuseumRepository;
import com.unifi.attws.exam.repository.postgres.PostgresMuseumRepository;

public class MuseumPostgresRepositoryWithNotEmptyDatabaseTest {

	private static final UUID MUSEUM_ID_1 = UUID.fromString("b433da18-ba5a-4b86-92af-ba11be6314e7");
	private static final UUID MUSEUM_ID_2 = UUID.fromString("94fe3013-9ebb-432e-ab55-e612dc797851");
	private static final String MUSEUM_TEST_1 = "Museum_test_1";
	private static final String MUSEUM_TEST_2 = "Museum_test_2";
	private static final int NUM_OF_ROOMS = 10;

	private MuseumRepository postgresMuseumRepository;
	private static EntityManager entityManager;
	private List<Museum> persistedMuseums;

	@Before
	public void setUp() throws Exception {
		EntityManagerFactory sessionFactory = Persistence.createEntityManagerFactory("postgres.not-empty.database");
		entityManager = sessionFactory.createEntityManager();
		postgresMuseumRepository = new PostgresMuseumRepository(entityManager);
		persistedMuseums = postgresMuseumRepository.findAllMuseums();
		entityManager.getTransaction().begin();
	}

	// Check if the sql script has been executed.
	@Test
	public void testfindAllMuseumsWhenMuseumsArePersisted() {
		Museum museum1 = postgresMuseumRepository.findMuseumById(MUSEUM_ID_1);
		Museum museum2 = postgresMuseumRepository.findMuseumById(MUSEUM_ID_2);
		assertThat(postgresMuseumRepository.findAllMuseums()).containsExactly(museum1, museum2);

	}

	@Test
	public void testFindMuseumByIdWhenMuseumIsPresent() {
		Museum museum1 = postgresMuseumRepository.findMuseumById(MUSEUM_ID_1);
		assertThat(postgresMuseumRepository.findMuseumById(museum1.getId())).isEqualTo(museum1);
	}

	@Test
	public void testAddDetachedEntityMuseumChangingIdShouldThrow() {
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
		Museum museum1 = postgresMuseumRepository.findMuseumById(MUSEUM_ID_1);
		postgresMuseumRepository.deleteMuseum(museum1);
		museum1.setName(MUSEUM_TEST_2);
		assertThatThrownBy(() -> postgresMuseumRepository.updateMuseum(museum1))
				.isInstanceOf(IllegalArgumentException.class);

	}

	@Test
	public void testUpdateMuseumWithNullEntityObjectShouldThrow() {
		assertThatThrownBy(() -> postgresMuseumRepository.updateMuseum(null))
				.isInstanceOf(IllegalArgumentException.class);
		assertThat(postgresMuseumRepository.findAllMuseums()).isEqualTo(persistedMuseums);
	}

	@Test
	public void testUpdateMuseumWhenExists() {
		Museum museum1 = postgresMuseumRepository.findMuseumById(MUSEUM_ID_1);
		museum1.setOccupiedRooms(1);
		postgresMuseumRepository.updateMuseum(museum1);
		entityManager.flush();
		assertThat(postgresMuseumRepository.findMuseumById(museum1.getId()).getOccupiedRooms()).isEqualTo(1);
		assertThat(postgresMuseumRepository.findAllMuseums()).containsAll(persistedMuseums);

	}

	@Test
	public void testRemoveDetachedEntityMuseumChangingIdShouldThrow() {
		Museum museum1 = postgresMuseumRepository.findMuseumById(MUSEUM_ID_1);
		Museum museum2 = new Museum(MUSEUM_TEST_2, NUM_OF_ROOMS);
		museum2.setId(museum1.getId());

		assertThatThrownBy(() -> postgresMuseumRepository.deleteMuseum(museum2))
				.isInstanceOf(IllegalArgumentException.class);
		assertThat(postgresMuseumRepository.findAllMuseums()).isEqualTo(persistedMuseums);

	}

	@Test
	public void testRemoveNullEntityObjectShouldThrow() {
		assertThatThrownBy(() -> postgresMuseumRepository.deleteMuseum(null))
				.isInstanceOf(IllegalArgumentException.class);
		assertThat(postgresMuseumRepository.findAllMuseums()).isEqualTo(persistedMuseums);
	}

	@Test
	public void testRemoveMuseumWhenTheMuseumExistsAndHasNotExhibitions() {
		Museum museum1 = postgresMuseumRepository.findMuseumById(MUSEUM_ID_2);
		postgresMuseumRepository.deleteMuseum(museum1);
		entityManager.flush();
		assertThat(postgresMuseumRepository.findAllMuseums()).hasSize(1);
	}

	@After
	public void closeEntityManager() {
		entityManager.getTransaction().rollback();
		entityManager.clear();
		entityManager.close();

	}

	public Museum createTestMuseum(String museumName, int numOfRooms) {
		return new Museum(museumName, numOfRooms);
	}

}
