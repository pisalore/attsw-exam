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

	private static final String MUSEUM_1 = "b433da18-ba5a-4b86-92af-ba11be6314e7";
	private static final String EXHIBITION_1 = "49d13e51-2277-4911-929f-c9c067e2e8b4";
	private static final String EXHIBITION_2 = "b2cb1474-24ff-41eb-a8d7-963f32f6822d";

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
		Exhibition exhibition1 = postgresExhibitionRepository.findExhibitionById(UUID.fromString(EXHIBITION_1));
		Exhibition exhibition2 = postgresExhibitionRepository.findExhibitionById(UUID.fromString(EXHIBITION_2));

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
		Exhibition exhibition1 = postgresExhibitionRepository.findExhibitionById(UUID.fromString(EXHIBITION_1));
		Exhibition exhibition2 = postgresExhibitionRepository.findExhibitionById(UUID.fromString(EXHIBITION_2));

		assertThat(postgresExhibitionRepository.findExhibitionsByMuseumId(UUID.fromString(MUSEUM_1)))
				.containsExactly(exhibition1, exhibition2);

	}

	@Test
	public void testAddNewExhibitionWhenRelatedMuseumIdExists() {
		Exhibition exhibition = new Exhibition("test exhibition", 100);
		exhibition.setMuseumId(UUID.fromString(MUSEUM_1));
		postgresExhibitionRepository.addNewExhibition(exhibition);
		entityManager.flush();

		assertThat(postgresExhibitionRepository.findAllExhibitions()).hasSize(3).extracting(Exhibition::getId)
				.contains(exhibition.getId());
	}

	@Test
	public void testUpdateExhibitionWithNullEntityShouldThrow() {
		assertThatThrownBy(() -> postgresExhibitionRepository.updateExhibition(null))
				.isInstanceOf(IllegalArgumentException.class);

	}

	@Test
	public void testUpdateExhibition() {
		Exhibition exhibition1 = postgresExhibitionRepository.findExhibitionById(UUID.fromString(EXHIBITION_1));
		exhibition1.setBookedSeats(10);
		postgresExhibitionRepository.updateExhibition(exhibition1);

		assertThat(postgresExhibitionRepository.findExhibitionById(UUID.fromString(EXHIBITION_1)).getBookedSeats())
				.isEqualTo(10);

	}

	@Test
	public void testUpdateExhibitionWithDetachedEntityShouldThrow() {
		Exhibition exhibition1 = postgresExhibitionRepository.findExhibitionById(UUID.fromString(EXHIBITION_1));
		exhibition1.setId(UUID.randomUUID());

		assertThatThrownBy(() -> postgresExhibitionRepository.updateExhibition(null))
				.isInstanceOf(IllegalArgumentException.class);

	}

	@Test
	public void testRemoveNullExhibitionShouldThrow() {
		assertThatThrownBy(() -> postgresExhibitionRepository.deleteExhibition(null))
				.isInstanceOf(IllegalArgumentException.class);
	}
	
	@Test
	public void testRemoveExhibition() {
		Exhibition exhibition1 = postgresExhibitionRepository.findExhibitionById(UUID.fromString(EXHIBITION_1));
		Exhibition exhibition2 = postgresExhibitionRepository.findExhibitionById(UUID.fromString(EXHIBITION_2));
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
