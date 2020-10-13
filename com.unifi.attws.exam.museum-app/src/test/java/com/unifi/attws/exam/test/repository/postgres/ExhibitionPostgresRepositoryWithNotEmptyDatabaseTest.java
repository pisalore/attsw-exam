package com.unifi.attws.exam.test.repository.postgres;

import static org.assertj.core.api.Assertions.*;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.unifi.attws.exam.model.Exhibition;
import com.unifi.attws.exam.repository.ExhibitionRepository;
import com.unifi.attws.exam.repository.postgres.PostgresExhibitionRepository;

public class ExhibitionPostgresRepositoryWithNotEmptyDatabaseTest {

	private static final UUID MUSEUM_ID_1 = UUID.fromString("b433da18-ba5a-4b86-92af-ba11be6314e7");

	private static final UUID EXHIBITION_ID_1 = UUID.fromString("49d13e51-2277-4911-929f-c9c067e2e8b4");
	private static final UUID EXHIBITION_ID_2 = UUID.fromString("b2cb1474-24ff-41eb-a8d7-963f32f6822d");
	
	private static final int UTILITY_CONST_NUM = 10;

	private ExhibitionRepository postgresExhibitionRepository;
	private static EntityManager entityManager;

	@Before
	public void setUp() {
		EntityManagerFactory sessionFactory = Persistence.createEntityManagerFactory("postgres.not-empty.database");
		entityManager = sessionFactory.createEntityManager();
		postgresExhibitionRepository = new PostgresExhibitionRepository(entityManager);
		entityManager.getTransaction().begin();

	}

	// Check if the sql script has been executed.
	@Test
	public void testfindAllMuseumsMuseumsWhenNoMuseumsArePersisted() {
		assertThat(postgresExhibitionRepository.findAllExhibitions()).isNotEmpty();

	}

	@Test
	public void testFindAllExhibitionsWhenSeveralExhibitionsArePersisted() {
		Exhibition exhibition1 = postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_1);
		Exhibition exhibition2 = postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_2);

		assertThat(postgresExhibitionRepository.findAllExhibitions()).containsExactly(exhibition1, exhibition2);
	}

	@Test
	public void testFindExhibitionByIdWhenSeveralExhibitionsArePersistedButIdDoesNotMatch() {
		assertThat(postgresExhibitionRepository.findExhibitionById(UUID.randomUUID())).isNull();
	}

	@Test
	public void testFindExhibitionsByMuseumIdWhenNoMuseumsArePersisted() {
		assertThat(postgresExhibitionRepository.findExhibitionsByMuseumId(UUID.randomUUID())).isEmpty();
	}

	@Test
	public void testFindExhibitionByMuseumIdWhenGivenIdIsNullShouldThrow() {

		assertThatThrownBy(() -> postgresExhibitionRepository.findExhibitionsByMuseumId(null))
				.isInstanceOf(IllegalArgumentException.class).hasMessage("Museum ID cannot be null.");
	}

	@Test
	public void testFindExhibitionsByMuseumIdWhenMuseumsArePersisted() {
		Exhibition exhibition1 = postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_1);
		Exhibition exhibition2 = postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_2);

		assertThat(postgresExhibitionRepository.findExhibitionsByMuseumId(MUSEUM_ID_1)).containsExactly(exhibition1,
				exhibition2);

	}

	@Test
	public void testAddNewExhibitionWhenRelatedMuseumIdExists() {
		Exhibition exhibition = new Exhibition("test exhibition", UTILITY_CONST_NUM);
		exhibition.setMuseumId(MUSEUM_ID_1);
		postgresExhibitionRepository.addNewExhibition(exhibition);

		assertThat(postgresExhibitionRepository.findAllExhibitions()).hasSize(3).extracting(Exhibition::getId)
				.contains(exhibition.getId());
	}
	

	@Test
	public void testUpdateExhibitionWithNullEntityShouldThrow() {
		assertThatThrownBy(() -> postgresExhibitionRepository.updateExhibition(null))
				.isInstanceOf(IllegalArgumentException.class);

	}

	@Test
	public void testUpdateRemovedExhibitionShouldThrow() {
		Exhibition exhibition1 = postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_1);
		postgresExhibitionRepository.deleteExhibition(exhibition1);
		exhibition1.setTotalSeats(UTILITY_CONST_NUM);
		assertThatThrownBy(() -> postgresExhibitionRepository.updateExhibition(exhibition1))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void testUpdateExhibitionWithDetachedEntityShouldThrow() {
		Exhibition exhibition1 = postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_1);
		exhibition1.setId(UUID.randomUUID());

		assertThatThrownBy(() -> postgresExhibitionRepository.updateExhibition(null))
				.isInstanceOf(IllegalArgumentException.class);

	}

	@Test
	public void testUpdateExhibition() {
		Exhibition exhibition1 = postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_1);
		exhibition1.setBookedSeats(UTILITY_CONST_NUM);
		postgresExhibitionRepository.updateExhibition(exhibition1);

		assertThat(postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_1).getBookedSeats()).isEqualTo(UTILITY_CONST_NUM);

	}

	@Test
	public void testRemoveNullExhibitionShouldThrow() {
		assertThatThrownBy(() -> postgresExhibitionRepository.deleteExhibition(null))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void testRemoveExhibition() {
		Exhibition exhibition1 = postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_1);
		Exhibition exhibition2 = postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_2);
		postgresExhibitionRepository.deleteExhibition(exhibition1);

		assertThat(postgresExhibitionRepository.findAllExhibitions()).containsExactly(exhibition2);
	}

	@After
	public void closeEntityManager() {
		entityManager.getTransaction().rollback();
		entityManager.clear();
		entityManager.close();
	}

}
