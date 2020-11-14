package com.unifi.attsw.exam.it.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.unifi.attsw.exam.exception.RepositoryException;
import com.unifi.attsw.exam.model.Exhibition;
import com.unifi.attsw.exam.model.Museum;
import com.unifi.attsw.exam.service.MuseumManagerService;
import com.unifi.attsw.exam.service.impl.MuseumManagerServiceImpl;
import com.unifi.attsw.exam.transaction.manager.postgres.PostgresTransactionManager;

public class MuseumManagerServiceIT {

	private static final String MUSEUM1_TEST = "museum1_test";
	private static final String MUSEUM2_TEST = "museum2_test";
	private static final String MUSEUM3_TEST = "museum3_test";

	private static final String EXHIBITION1_TEST = "exhibition1_test";
	private static final String EXHIBITION2_TEST = "exhibition2_test";

	private static EntityManagerFactory sessionFactory;
	private static EntityManager entityManager;
	private static PostgresTransactionManager transactionManager;
	private static MuseumManagerService museumManager;

	@BeforeClass
	public static void setUpClass() {
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
	public void testGetMuseumExhibitions() {
		Museum museum = museumManager.getMuseumByName(MUSEUM1_TEST);
		List<Exhibition> exhibitions = museumManager.getAllMuseumExhibitions(museum);
		assertThat(exhibitions).extracting(Exhibition::getName).contains(EXHIBITION1_TEST, EXHIBITION2_TEST);
	}

	@Test
	public void testAddNewMuseumWithAlreadyExistingNameShouldThrow() {
		Museum museum = new Museum(MUSEUM1_TEST, 10);
		assertThatThrownBy(() -> {
			museumManager.saveMuseum(museum);
		}).isInstanceOf(RuntimeException.class).hasMessage("Impossibile to add Museum.");
	}
	
	@Test
	public void testAddNewMuseum() throws RepositoryException {
		Museum museum = new Museum (MUSEUM3_TEST, 10);
		museumManager.saveMuseum(museum);
		List<Museum> museums = museumManager.getAllMuseums();
		assertThat(museums).extracting(Museum::getName).contains(MUSEUM1_TEST, MUSEUM2_TEST, MUSEUM3_TEST);
		
	}
	
	@AfterClass
	public static void tearDown() {
		entityManager.clear();
		entityManager.close();
		sessionFactory.close();
	}
}
