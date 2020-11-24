package com.unifi.attsw.exam.it.view.swing;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.testcontainers.jdbc.ContainerDatabaseDriver;

import com.unifi.attsw.exam.controller.MuseumController;
import com.unifi.attsw.exam.model.Museum;
import com.unifi.attsw.exam.service.MuseumManagerService;
import com.unifi.attsw.exam.service.impl.MuseumManagerServiceImpl;
import com.unifi.attsw.exam.transaction.manager.postgres.PostgresTransactionManager;
import com.unifi.attsw.exam.view.ExhibitionView;
import com.unifi.attsw.exam.view.swing.MuseumSwingView;

@RunWith(GUITestRunner.class)
public class MuseumSwingViewIT extends AssertJSwingJUnitTestCase {

	private final static String JDBC_CONTAINER_URL = "jdbc:tc:postgresql:9.6.8:///databasenameTC_INITSCRIPT=file:src/test/resources/META-INF/postgres_init_scripts/init_postgresql_empty.sql";

	private static final String MUSEUM3_TEST = "museum3_test";
	// already existing name in Museum table
	private static final String MUSEUM1_TEST = "museum1_test";
	private static final String NUM_CONST = "10";

	private FrameFixture window;
	private static EntityManagerFactory sessionFactory;
	private static EntityManager entityManager;
	private static PostgresTransactionManager transactionManager;
	private static MuseumManagerService museumManager;
	private static MuseumSwingView museumSwingView;

	// I want to test only the integration of controller and Museum swing view
	@Mock
	private static ExhibitionView exhibitionView;

	private static MuseumController museumController;

	@Override
	protected void onSetUp() {
		sessionFactory = Persistence.createEntityManagerFactory("postgres.not-empty.database");
		entityManager = sessionFactory.createEntityManager();
		transactionManager = new PostgresTransactionManager(entityManager);
		museumManager = new MuseumManagerServiceImpl(transactionManager);

		GuiActionRunner.execute(() -> {
			museumSwingView = new MuseumSwingView();
			museumController = new MuseumController(museumManager, museumSwingView, exhibitionView);
			museumSwingView.setMuseumController(museumController);
			return museumSwingView;
		});

		window = new FrameFixture(robot(), museumSwingView);
		window.show();
	}

	@Test
	@GUITest
	public void testGetAllMuseums() {
		window.button(JButtonMatcher.withText("Find all")).click();
		assertThat(window.list().contents()).isNotEmpty();
	}

	@Test
	@GUITest
	public void testAddButtonSuccess() {
		window.textBox("museum").enterText(MUSEUM3_TEST);
		window.textBox("rooms").enterText(NUM_CONST);
		window.button(JButtonMatcher.withText("Add")).click();
		assertThat(window.list().contents()).isNotEmpty();
	}

	@Test
	@GUITest
	public void testAddButtonError() {
		window.textBox("museum").enterText(MUSEUM1_TEST);
		window.textBox("rooms").enterText(NUM_CONST);
		window.button(JButtonMatcher.withText("Add")).click();
		assertThat(window.list().contents()).isEmpty();
		window.label("errorMessageLabel").requireText("Impossibile to add Museum: " + MUSEUM1_TEST);
	}

	@Test
	@GUITest
	public void testDeleteButtonSuccess() {
		GuiActionRunner.execute(() -> museumController.getAllMuseums());
		window.list().selectItem(0);
		window.button(JButtonMatcher.withText("Delete Selected")).click();
		assertThat(window.list().contents()).hasSize(1);
	}
	
	@Test
	@GUITest
	public void testDeleteButtonError() {
		Museum notExistingMuseum = new Museum(MUSEUM3_TEST, 10);
		GuiActionRunner.execute(
				() -> museumSwingView.getMuseumListModel().addElement(notExistingMuseum));
		window.list().selectItem(0);
		window.button(JButtonMatcher.withText("Delete Selected")).click();
		window.label("errorMessageLabel").requireText("Impossible to delete Museum: " + MUSEUM3_TEST);
	}

	@Override
	protected void onTearDown() {
		entityManager.clear();
		entityManager.close();
		sessionFactory.close();
		// This force to restart a docker container and re-execute the initialization
		// script provided for not empty database.
		ContainerDatabaseDriver.killContainer(JDBC_CONTAINER_URL);
	}

}
