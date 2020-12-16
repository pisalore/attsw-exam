package com.unifi.attsw.exam.core.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.unifi.attsw.exam.core.service.MuseumManagerService;
import com.unifi.attsw.exam.core.service.exception.MuseumManagerServiceException;
import com.unifi.attsw.exam.core.service.impl.MuseumManagerServiceImpl;
import com.unifi.attsw.exam.repository.model.Exhibition;
import com.unifi.attsw.exam.repository.model.Museum;
import com.unifi.attsw.exam.repository.repository.exception.RepositoryException;
import com.unifi.attsw.exam.repository.transaction.manager.postgres.PostgresTransactionManager;

public class MuseumManagerServiceIT {

	private static final String MUSEUM1_TEST = "museum1_test";
	private static final String MUSEUM2_TEST = "museum2_test";
	private static final String MUSEUM3_TEST = "museum3_test";
	private static final int NUM_CONST = 10;

	private static final UUID MUSEUM_ID_1 = UUID.fromString("b433da18-ba5a-4b86-92af-ba11be6314e7");

	private static final String EXHIBITION1_TEST = "exhibition1_test";
	private static final String EXHIBITION2_TEST = "exhibition2_test";
	private static final String EXHIBITION3_TEST = "exhibition3_test";

	private static final UUID EXHIBITION_ID_1 = UUID.fromString("49d13e51-2277-4911-929f-c9c067e2e8b4");

	private static EntityManagerFactory sessionFactory;
	private static EntityManager entityManager;
	private static PostgresTransactionManager transactionManager;
	private static MuseumManagerService museumManager;

	@BeforeClass
	public static void beforeClass() {
		sessionFactory = Persistence.createEntityManagerFactory("postgres");
	}

	@Before
	public void setUp() {
		entityManager = sessionFactory.createEntityManager();
		transactionManager = new PostgresTransactionManager(entityManager);
		museumManager = new MuseumManagerServiceImpl(transactionManager);

		entityManager.getTransaction().begin();
		entityManager.createNativeQuery("TRUNCATE TABLE Museums CASCADE").executeUpdate();
		entityManager.getTransaction().commit();
	}

	@Test
	public void testGetAllMuseums() throws RepositoryException {
		populateDatabase();
		List<Museum> museums = museumManager.getAllMuseums();
		assertThat(museums).extracting(Museum::getName).contains(MUSEUM1_TEST, MUSEUM2_TEST);
	}

	@Test
	public void testGetAllExhibitions() throws RepositoryException {
		populateDatabase();
		List<Exhibition> exhibitions = museumManager.getAllExhibitions();
		assertThat(exhibitions).extracting(Exhibition::getName).contains(EXHIBITION1_TEST, EXHIBITION2_TEST);
	}

	@Test
	public void testGetMuseumByName() throws MuseumManagerServiceException {
		populateDatabase();
		Museum museum = museumManager.getMuseumByName(MUSEUM1_TEST);
		assertThat(museum).extracting(Museum::getName, Museum::getId).containsExactly(MUSEUM1_TEST, MUSEUM_ID_1);
	}

	@Test
	public void testGetExhibitionByName() throws MuseumManagerServiceException {
		populateDatabase();
		Exhibition exhibition = museumManager.getExhibitionByName(EXHIBITION1_TEST);
		assertThat(exhibition).extracting(Exhibition::getName, Exhibition::getId).containsExactly(EXHIBITION1_TEST,
				EXHIBITION_ID_1);
	}

	@Test
	public void testGetMuseumExhibitions() throws MuseumManagerServiceException {
		populateDatabase();
		Museum museum = museumManager.getMuseumByName(MUSEUM1_TEST);
		List<Exhibition> exhibitions = museumManager.getAllMuseumExhibitions(museum);
		assertThat(exhibitions).extracting(Exhibition::getName).contains(EXHIBITION1_TEST, EXHIBITION2_TEST);
	}

	@Test
	public void testAddNewMuseum() throws RepositoryException, MuseumManagerServiceException {
		populateDatabase();
		Museum museum = new Museum(MUSEUM3_TEST, NUM_CONST);
		museumManager.saveMuseum(museum);
		List<Museum> museums = museumManager.getAllMuseums();
		assertThat(museums).extracting(Museum::getName).contains(MUSEUM1_TEST, MUSEUM2_TEST, MUSEUM3_TEST);

	}

	@Test
	public void testDeleteMuseum() throws RepositoryException, MuseumManagerServiceException {
		populateDatabase();
		Museum museum = museumManager.getMuseumByName(MUSEUM1_TEST);
		museumManager.deleteMuseum(museum);
		List<Museum> museums = museumManager.getAllMuseums();
		List<Exhibition> exhibitions = museumManager.getAllExhibitions();
		assertThat(museums).extracting(Museum::getName).containsExactly(MUSEUM2_TEST);
		assertThat(exhibitions).isEmpty();
	}

	@Test
	public void testAddNewExhibitionToANotExistingMuseumShouldThrow() {
		populateDatabase();
		Exhibition exhibition = createTestExhibition(EXHIBITION3_TEST, NUM_CONST);

		assertThatThrownBy(() -> {
			museumManager.addNewExhibition(MUSEUM3_TEST, exhibition);
		}).isInstanceOf(RuntimeException.class).hasMessage("The selected museum does not exist!");
	}

	@Test
	public void testAddNewExhibition() throws MuseumManagerServiceException {
		populateDatabase();
		Exhibition exhibition = createTestExhibition(EXHIBITION3_TEST, NUM_CONST);
		Museum museum = museumManager.getMuseumByName(MUSEUM1_TEST);
		museumManager.addNewExhibition(museum.getName(), exhibition);
		List<Exhibition> exhibitions = museumManager.getAllMuseumExhibitions(museum);
		assertThat(exhibitions).extracting(Exhibition::getName).containsExactly(EXHIBITION1_TEST, EXHIBITION2_TEST,
				EXHIBITION3_TEST);
	}

	@Test
	public void testDeleteExhibition() throws RepositoryException, MuseumManagerServiceException {
		populateDatabase();
		Museum museum1 = museumManager.getMuseumByName(MUSEUM1_TEST);
		Exhibition exhibition = museumManager.getExhibitionByName(EXHIBITION1_TEST);
		museumManager.deleteExhibition(exhibition);
		List<Exhibition> allExhibitions = museumManager.getAllExhibitions();
		List<Exhibition> museum1TestExhibitions = museumManager.getAllMuseumExhibitions(museum1);

		assertThat(allExhibitions).extracting(Exhibition::getName).containsExactly(EXHIBITION2_TEST);
		assertThat(museum1TestExhibitions).extracting(Exhibition::getName).containsExactly(EXHIBITION2_TEST);

	}

	@Test
	public void testBookExhibition() throws MuseumManagerServiceException {
		populateDatabase();
		Exhibition exhibition = museumManager.getExhibitionByName(EXHIBITION1_TEST);
		int bookedExhibitionsBefore = exhibition.getBookedSeats();
		museumManager.bookExhibitionSeat(exhibition);
		int bookedExhibitionsAfter = exhibition.getBookedSeats();
		assertThat(bookedExhibitionsAfter).isEqualTo(bookedExhibitionsBefore + 1);
	}

	@AfterClass
	public static void tearDown() {
		entityManager.clear();
		entityManager.close();
		sessionFactory.close();
	}

	/**
	 * 
	 * Utility methods
	 * 
	 */

	public Museum createTestMuseum(String museumName, int numOfRooms) {
		return new Museum(museumName, numOfRooms);
	}

	public Exhibition createTestExhibition(String exhibitionName, int numOfSeats) {
		return new Exhibition(exhibitionName, numOfSeats);

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
