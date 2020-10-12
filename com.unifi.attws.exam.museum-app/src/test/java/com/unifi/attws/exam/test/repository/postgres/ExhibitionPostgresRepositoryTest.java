package com.unifi.attws.exam.test.repository.postgres;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.assertj.core.api.Assertions.*;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.unifi.attws.exam.model.Exhibition;
import com.unifi.attws.exam.repository.ExhibitionRepository;
import com.unifi.attws.exam.repository.postgres.PostgresExhibitionRepository;

public class ExhibitionPostgresRepositoryTest {

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
		assertThat(postgresExhibitionRepository.findExhibitionById(UUID.randomUUID())).isNull();
	}

	@Test
	public void testFindExhibitionByIdWhenIdIsNullShouldThrow() {
		assertThatThrownBy(() -> postgresExhibitionRepository.findExhibitionById(null))
				.isInstanceOf(IllegalArgumentException.class);
	}
	
	@Test
	public void testAddNewNullExhibitionEntityShouldThrow() {
		assertThatThrownBy(() -> postgresExhibitionRepository.addNewExhibition(null))
		.isInstanceOf(IllegalArgumentException.class);
		
		assertThat(postgresExhibitionRepository.findAllExhibitions()).isEmpty();
		
	}
	
	@Test
	public void testAddNewExhibition() {
		Exhibition exhibition = new Exhibition("test exhibition", 100);
		 postgresExhibitionRepository.addNewExhibition(exhibition);
		 
		 assertThat(postgresExhibitionRepository.findAllExhibitions())
		 	.hasSize(1)
		 	.extracting(Exhibition::getId)
		 	.contains(exhibition.getId());
	}

	@After
	public void closeEntityManager() {
		entityManager.getTransaction().commit();
		entityManager.clear();
		entityManager.close();
	}

}
