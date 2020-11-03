package com.unifi.attsw.exam.test.repository.postgres;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.assertj.core.api.Assertions.*;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.unifi.attsw.exam.repository.ExhibitionRepository;
import com.unifi.attsw.exam.repository.postgres.PostgresExhibitionRepository;

public class ExhibitionPostgresRepositoryTest {

	private static final UUID invalidUUID = UUID.fromString("2796027d-21cc-4883-b088-514d4b3090a1");

	private ExhibitionRepository postgresExhibitionRepository;
	private static EntityManager entityManager;

	@Before
	public void setUp() {
		EntityManagerFactory sessionFactory = Persistence.createEntityManagerFactory("postgres");
		entityManager = sessionFactory.createEntityManager();
		postgresExhibitionRepository = new PostgresExhibitionRepository(entityManager);
		entityManager.getTransaction().begin();

	}

	@Test
	public void testFindAllExhibitionWhenNoExhibitionsArePersisted() {
		assertThat(postgresExhibitionRepository.findAllExhibitions()).isEmpty();
	}

	@Test
	public void testFindExhibitionByIdWhenNoExhibitionsArePersisted() {
		assertThat(postgresExhibitionRepository.findExhibitionById(invalidUUID)).isNull();
	}

	@Test
	public void testFindExhibitionByIdWhenIdIsNullShouldThrow() {
		assertThatThrownBy(() -> postgresExhibitionRepository.findExhibitionById(null))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Cannot find entity, invalid or null id: " + null);
	}
	
	@Test
	public void testFindMuseumByNullName() {
		assertThat(postgresExhibitionRepository.findExhibitionByName(null)).isNull();

	}

	@Test
	public void testAddNewNullExhibitionEntityShouldThrow() {
		assertThatThrownBy(() -> postgresExhibitionRepository.addNewExhibition(null))
				.isInstanceOf(IllegalArgumentException.class);

		assertThat(postgresExhibitionRepository.findAllExhibitions()).isEmpty();

	}

	@After
	public void closeEntityManager() {
		entityManager.getTransaction().rollback();
		entityManager.clear();
		entityManager.close();
	}

}
