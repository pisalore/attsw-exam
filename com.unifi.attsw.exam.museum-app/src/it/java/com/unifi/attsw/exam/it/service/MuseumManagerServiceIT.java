package com.unifi.attsw.exam.it.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testcontainers.jdbc.ContainerDatabaseDriver;

import com.unifi.attsw.exam.exception.RepositoryException;
import com.unifi.attsw.exam.model.Exhibition;
import com.unifi.attsw.exam.model.Museum;
import com.unifi.attsw.exam.service.MuseumManagerService;
import com.unifi.attsw.exam.service.impl.MuseumManagerServiceImpl;
import com.unifi.attsw.exam.transaction.manager.postgres.PostgresTransactionManager;

public class MuseumManagerServiceIT {
	private final static String JDBC_CONTAINER_URL = "jdbc:tc:postgresql:9.6.8:///databasenameTC_INITSCRIPT=file:src/test/resources/META-INF/postgres_init_scripts/init_postgresql_empty.sql";

	private static final String MUSEUM1_TEST = "museum1_test";
	private static final String MUSEUM2_TEST = "museum2_test";
	private static final String MUSEUM3_TEST = "museum3_test";
	private static final int NUM_CONST = 10;

	private static final UUID MUSEUM_ID_1 = UUID.fromString("b433da18-ba5a-4b86-92af-ba11be6314e7");

	private static final String EXHIBITION1_TEST = "exhibition1_test";
	private static final String EXHIBITION2_TEST = "exhibition2_test";
	private static final String EXHIBITION3_TEST = "exhibition3_test";

	private static EntityManagerFactory sessionFactory;
	private static EntityManager entityManager;
	private static PostgresTransactionManager transactionManager;
	private static MuseumManagerService museumManager;

	@Before
	public void setUp() {
		sessionFactory = Persistence.createEntityManagerFactory("postgres.not-empty.database");
		entityManager = sessionFactory.createEntityManager();
		transactionManager = new PostgresTransactionManager(entityManager);
		museumManager = new MuseumManagerServiceImpl(transactionManager);
	}

	@Test
	public void testGetAllMuseums() throws RepositoryException {
		List<Museum> museums = museumManager.getAllMuseums();
		assertThat(museums).extracting(Museum::getName).contains(MUSEUM1_TEST, MUSEUM2_TEST);
	}

	@Test
	public void testGetAllExhibitions() throws RepositoryException {
		List<Exhibition> exhibitions = museumManager.getAllExhibitions();
		assertThat(exhibitions).extracting(Exhibition::getName).contains(EXHIBITION1_TEST, EXHIBITION2_TEST);
	}

	@Test
	public void testGetMuseumByName() {
		Museum museum = museumManager.getMuseumByName(MUSEUM1_TEST);
		assertThat(museum).extracting(Museum::getName, Museum::getId).containsExactly(MUSEUM1_TEST, MUSEUM_ID_1);
	}

	@Test
	public void testGetMuseumExhibitions() {
		Museum museum = museumManager.getMuseumByName(MUSEUM1_TEST);
		List<Exhibition> exhibitions = museumManager.getAllMuseumExhibitions(museum);
		assertThat(exhibitions).extracting(Exhibition::getName).contains(EXHIBITION1_TEST, EXHIBITION2_TEST);
	}

	@Test
	public void testAddNewMuseumWithAlreadyExistingNameShouldThrow() {
		Museum museum = new Museum(MUSEUM1_TEST, NUM_CONST);
		assertThatThrownBy(() -> {
			museumManager.saveMuseum(museum);
		}).isInstanceOf(RuntimeException.class).hasMessage("Impossibile to add Museum.");
	}

	@Test
	public void testAddNewMuseum() throws RepositoryException {
		Museum museum = new Museum(MUSEUM3_TEST, NUM_CONST);
		museumManager.saveMuseum(museum);
		List<Museum> museums = museumManager.getAllMuseums();
		assertThat(museums).extracting(Museum::getName).contains(MUSEUM1_TEST, MUSEUM2_TEST, MUSEUM3_TEST);

	}

	@Test
	public void testDeleteMuseum() throws RepositoryException {
		Museum museum = museumManager.getMuseumByName(MUSEUM1_TEST);
		museumManager.deleteMuseum(museum);
		List<Museum> museums = museumManager.getAllMuseums();
		List<Exhibition> exhibitions = museumManager.getAllExhibitions();
		assertThat(museums).extracting(Museum::getName).containsExactly(MUSEUM2_TEST);
		assertThat(exhibitions).isEmpty();
	}

	@Test
	public void testAddNewExhibitionToANotExistingMuseumShouldThrow() {
		Exhibition exhibition = createTestExhibition(EXHIBITION3_TEST, NUM_CONST);

		assertThatThrownBy(() -> {
			museumManager.addNewExhibition(MUSEUM3_TEST, exhibition);
		}).isInstanceOf(RuntimeException.class).hasMessage("Impossible to add Exhibition.");
	}

	@Test
	public void testaddNewExhibition() {
		Exhibition exhibition = createTestExhibition(EXHIBITION3_TEST, NUM_CONST);
		Museum museum = museumManager.getMuseumByName(MUSEUM1_TEST);
		museumManager.addNewExhibition(museum.getName(), exhibition);
		List<Exhibition> exhibitions = museumManager.getAllMuseumExhibitions(museum);
		assertThat(exhibitions).extracting(Exhibition::getName).containsExactly(EXHIBITION1_TEST, EXHIBITION2_TEST,
				EXHIBITION3_TEST);
	}
	
//	@Test
//	public void deleteExhibition() {
//		
//	}

	@After
	public void tearDown() {
		entityManager.clear();
		entityManager.close();
		sessionFactory.close();
		// This force to restart a docker container and re-execute the initialization
		// script provided for not empty database.
		ContainerDatabaseDriver.killContainer(JDBC_CONTAINER_URL);
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
}
