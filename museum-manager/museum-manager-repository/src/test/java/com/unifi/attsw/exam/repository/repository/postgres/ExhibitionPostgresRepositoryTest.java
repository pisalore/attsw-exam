package com.unifi.attsw.exam.repository.repository.postgres;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.unifi.attsw.exam.repository.model.Exhibition;
import com.unifi.attsw.exam.repository.repository.ExhibitionRepository;
import com.unifi.attsw.exam.repository.repository.postgres.PostgresExhibitionRepository;

public class ExhibitionPostgresRepositoryTest {

	private static final UUID MUSEUM_ID_1 = UUID.fromString("b433da18-ba5a-4b86-92af-ba11be6314e7");

	private static final UUID EXHIBITION_ID_1 = UUID.fromString("49d13e51-2277-4911-929f-c9c067e2e8b4");
	private static final UUID EXHIBITION_ID_2 = UUID.fromString("b2cb1474-24ff-41eb-a8d7-963f32f6822d");
	private static final UUID invalidUUID = UUID.fromString("2796027d-21cc-4883-b088-514d4b3090a1");

	private static final String EXHIBITION_TEST_1 = "exhibition1_test";
	private static final String EXHIBITION_NOT_PERSISTED = "exhibition_not_persisted";

	private static final int UTILITY_CONST_NUM = 10;

	private ExhibitionRepository postgresExhibitionRepository;
	private static EntityManagerFactory sessionFactory;
	private static EntityManager entityManager;

	@BeforeClass
	public static void beforeClass() {
		sessionFactory = Persistence.createEntityManagerFactory("postgres");
	}

	@Before
	public void setUp() {
		entityManager = sessionFactory.createEntityManager();
		postgresExhibitionRepository = new PostgresExhibitionRepository(entityManager);

		entityManager.getTransaction().begin();
		entityManager.createNativeQuery("TRUNCATE TABLE Museums CASCADE").executeUpdate();
		entityManager.getTransaction().commit();

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

	@Test
	public void testFindAllExhibitionsWhenSeveralExhibitionsArePersisted() {
		populateDatabase();
		Exhibition exhibition1 = postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_1);
		Exhibition exhibition2 = postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_2);

		assertThat(postgresExhibitionRepository.findAllExhibitions()).containsExactly(exhibition1, exhibition2);
	}

	@Test
	public void testFindExhibitionByIdWhenSeveralExhibitionsArePersistedButIdDoesNotMatch() {
		populateDatabase();
		assertThat(postgresExhibitionRepository.findExhibitionById(invalidUUID)).isNull();
	}

	@Test
	public void testFindExhibitionsByMuseumIdWhenNoMuseumsArePersisted() {
		populateDatabase();
		assertThat(postgresExhibitionRepository.findExhibitionsByMuseumId(invalidUUID)).isEmpty();
	}

	@Test
	public void testFindExhibitionByMuseumIdWhenGivenIdIsNullShouldThrow() {
		assertThatThrownBy(() -> postgresExhibitionRepository.findExhibitionsByMuseumId(null))
				.isInstanceOf(IllegalArgumentException.class).hasMessage("Museum ID cannot be null.");
	}

	@Test
	public void testFindExhibitionsByMuseumIdWhenMuseumAndrelativeExhibitionsArePersisted() {
		populateDatabase();
		Exhibition exhibition1 = postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_1);
		Exhibition exhibition2 = postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_2);

		assertThat(postgresExhibitionRepository.findExhibitionsByMuseumId(MUSEUM_ID_1)).containsExactly(exhibition1,
				exhibition2);
	}

	@Test
	public void testFindExhibitionByNameWhenMuseumIsPresent() {
		populateDatabase();
		Exhibition exhibition1 = postgresExhibitionRepository.findExhibitionByName(EXHIBITION_TEST_1);
		List<Exhibition> persistedExhibitions = postgresExhibitionRepository.findAllExhibitions();
		assertThat(persistedExhibitions).extracting(Exhibition::getName).contains(exhibition1.getName());
	}

	@Test
	public void testFindExhibitionByNameOfNotExistingExhibitionReturnsNull() {
		populateDatabase();
		assertThat(postgresExhibitionRepository.findExhibitionByName(EXHIBITION_NOT_PERSISTED)).isNull();

	}

	@Test
	public void testAddNewExhibitionWhenRelatedMuseumIdExists() {
		populateDatabase();
		Exhibition exhibition = new Exhibition("test exhibition", UTILITY_CONST_NUM);
		exhibition.setMuseumId(MUSEUM_ID_1);
		
		entityManager.getTransaction().begin();
		postgresExhibitionRepository.addNewExhibition(exhibition);
		entityManager.getTransaction().commit();

		assertThat(postgresExhibitionRepository.findAllExhibitions()).hasSize(3).extracting(Exhibition::getId)
				.contains(exhibition.getId());
	}

	@Test
	public void testUpdateExhibitionWithNullEntityShouldThrow() {
		populateDatabase();
		assertThatThrownBy(() -> postgresExhibitionRepository.updateExhibition(null))
				.isInstanceOf(IllegalArgumentException.class);

	}

	@Test
	public void testUpdateRemovedExhibitionShouldThrow() {
		populateDatabase();
		Exhibition exhibition1 = postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_1);
		postgresExhibitionRepository.deleteExhibition(exhibition1);
		exhibition1.setTotalSeats(UTILITY_CONST_NUM);
		assertThatThrownBy(() -> postgresExhibitionRepository.updateExhibition(exhibition1))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void testUpdateExhibitionWithDetachedEntityShouldThrow() {
		populateDatabase();
		Exhibition exhibition1 = postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_1);
		exhibition1.setId(invalidUUID);

		assertThatThrownBy(() -> postgresExhibitionRepository.updateExhibition(null))
				.isInstanceOf(IllegalArgumentException.class);

	}

	@Test
	public void testUpdateExhibition() {
		populateDatabase();
		Exhibition exhibition1 = postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_1);
		exhibition1.setBookedSeats(UTILITY_CONST_NUM);
		postgresExhibitionRepository.updateExhibition(exhibition1);

		assertThat(postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_1).getBookedSeats())
				.isEqualTo(UTILITY_CONST_NUM);

	}

	@Test
	public void testRemoveNullExhibitionShouldThrow() {
		assertThatThrownBy(() -> postgresExhibitionRepository.deleteExhibition(null))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void testRemoveExhibition() {
		populateDatabase();
		Exhibition exhibition1 = postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_1);
		Exhibition exhibition2 = postgresExhibitionRepository.findExhibitionById(EXHIBITION_ID_2);
		
		entityManager.getTransaction().begin();
		postgresExhibitionRepository.deleteExhibition(exhibition1);
		entityManager.getTransaction().commit();


		assertThat(postgresExhibitionRepository.findAllExhibitions()).containsExactly(exhibition2);
	}

	@After
	public void closeEntityManager() {
		entityManager.clear();
		entityManager.close();
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

		entityManager
				.createNativeQuery("INSERT INTO exhibitions(id, museum_id, exhibition_name, total_seats, booked_seats)"
						+ "VALUES ('49d13e51-2277-4911-929f-c9c067e2e8b4', 'b433da18-ba5a-4b86-92af-ba11be6314e7', 'exhibition1_test', 100, 0);")
				.executeUpdate();
		entityManager
				.createNativeQuery("INSERT INTO exhibitions(id, museum_id, exhibition_name, total_seats, booked_seats)"
						+ "VALUES ('b2cb1474-24ff-41eb-a8d7-963f32f6822d', 'b433da18-ba5a-4b86-92af-ba11be6314e7', 'exhibition2_test', 100, 0);")
				.executeUpdate();

		entityManager.getTransaction().commit();
	}

}
