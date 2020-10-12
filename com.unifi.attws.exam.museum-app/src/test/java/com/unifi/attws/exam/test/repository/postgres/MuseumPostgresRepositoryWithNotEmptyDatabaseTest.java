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
	public void testfindAllMuseumsMuseumsWhenNoMuseumsArePersisted() {
		assertThat(postgresMuseumRepository.findAllMuseums()).isNotEmpty();

	}

	@Test
	public void testFindAllMuseumsWhenSeveralMuseumsArePersisted() {
		Museum museum1 = postgresMuseumRepository
				.retrieveMuseumById(UUID.fromString("b433da18-ba5a-4b86-92af-ba11be6314e7"));
		Museum museum2 = postgresMuseumRepository
				.retrieveMuseumById(UUID.fromString("94fe3013-9ebb-432e-ab55-e612dc797851"));
		assertThat(postgresMuseumRepository.findAllMuseums()).containsExactly(museum1, museum2);

	}

	@Test
	public void testAddDetachedEntityMuseumShouldThrow() {
		Museum museum1 = postgresMuseumRepository
				.retrieveMuseumById(UUID.fromString("b433da18-ba5a-4b86-92af-ba11be6314e7"));
		Museum museum2 = new Museum("test3", 20);
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
		Museum museum1 = postgresMuseumRepository
				.retrieveMuseumById(UUID.fromString("b433da18-ba5a-4b86-92af-ba11be6314e7"));
		entityManager.remove(museum1);
		museum1.setName("test_update");
		assertThatThrownBy(() -> postgresMuseumRepository.updateMuseum(museum1))
				.isInstanceOf(IllegalArgumentException.class);

	}

	@Test
	public void testUpdateMuseumWithNonEntityObjectShouldThrow() {
		assertThatThrownBy(() -> postgresMuseumRepository.updateMuseum(null))
				.isInstanceOf(IllegalArgumentException.class);
		assertThat(postgresMuseumRepository.findAllMuseums()).isEqualTo(persistedMuseums);
	}

	@Test
	public void testRemoveDetachedEntityMuseumShouldThrow() {
		Museum museum1 = postgresMuseumRepository
				.retrieveMuseumById(UUID.fromString("b433da18-ba5a-4b86-92af-ba11be6314e7"));
		Museum museum2 = new Museum("test3", 20);
		museum2.setId(museum1.getId());

		assertThatThrownBy(() -> postgresMuseumRepository.deleteMuseum(museum2))
				.isInstanceOf(IllegalArgumentException.class);
		assertThat(postgresMuseumRepository.findAllMuseums()).isEqualTo(persistedMuseums);

	}

	@Test
	public void testRemoveNonEntityObjectShouldThrow() {
		assertThatThrownBy(() -> postgresMuseumRepository.deleteMuseum(null))
				.isInstanceOf(IllegalArgumentException.class);
		assertThat(postgresMuseumRepository.findAllMuseums()).isEqualTo(persistedMuseums);
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
