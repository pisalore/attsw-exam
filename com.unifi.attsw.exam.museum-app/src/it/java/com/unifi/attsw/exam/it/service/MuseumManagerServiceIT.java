package com.unifi.attsw.exam.it.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.unifi.attsw.exam.exception.RepositoryException;
import com.unifi.attsw.exam.model.Museum;
import com.unifi.attsw.exam.service.MuseumManagerService;
import com.unifi.attsw.exam.service.impl.MuseumManagerServiceImpl;
import com.unifi.attsw.exam.transaction.manager.postgres.PostgresTransactionManager;

public class MuseumManagerServiceIT {

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

	// Check if the sql script has been executed.
	@Test
	public void testGetAllMuseums() throws RepositoryException {
		List<Museum> museums = museumManager.getAllMuseums();
		assertThat(museums).isNotEmpty();
	}

	@AfterClass
	public static void tearDown() {
		entityManager.clear();
		entityManager.close();
		sessionFactory.close();
	}
}
