package com.unifi.attsw.exam.presentation.view.swing;

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
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import com.unifi.attsw.exam.presentation.controller.swing.MuseumSwingController;
import com.unifi.attsw.exam.core.service.MuseumManagerService;
import com.unifi.attsw.exam.core.service.impl.MuseumManagerServiceImpl;
import com.unifi.attsw.exam.presentation.view.swing.ExhibitionSwingView;
import com.unifi.attsw.exam.presentation.view.swing.MuseumSwingView;
import com.unifi.attsw.exam.repository.model.Museum;
import com.unifi.attsw.exam.repository.transaction.manager.postgres.PostgresTransactionManager;

@RunWith(GUITestRunner.class)
public class MuseumSwingViewIT extends AssertJSwingJUnitTestCase {

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
	private static ExhibitionSwingView exhibitionView;

	private static MuseumSwingController museumSwingController;

	@BeforeClass
	public static void beforeClass() {
		sessionFactory = Persistence.createEntityManagerFactory("postgres");
	}

	@Override
	protected void onSetUp() {
		entityManager = sessionFactory.createEntityManager();
		transactionManager = new PostgresTransactionManager(entityManager);
		museumManager = new MuseumManagerServiceImpl(transactionManager);

		entityManager.getTransaction().begin();
		entityManager.createNativeQuery("TRUNCATE TABLE Museums CASCADE").executeUpdate();
		entityManager.getTransaction().commit();

		GuiActionRunner.execute(() -> {
			museumSwingView = new MuseumSwingView();
			museumSwingController = new MuseumSwingController(museumManager, museumSwingView, exhibitionView);
			museumSwingView.setMuseumController(museumSwingController);
			return museumSwingView;
		});

		window = new FrameFixture(robot(), museumSwingView);
		window.show();
	}

	@Test
	@GUITest
	public void testGetAllMuseums() {
		populateDatabase();
		window.button(JButtonMatcher.withText("Find all")).click();
		assertThat(window.list().contents()).isNotEmpty();
	}

	@Test
	@GUITest
	public void testAddButtonSuccess() {
		populateDatabase();
		window.textBox("museum").enterText(MUSEUM3_TEST);
		window.textBox("rooms").enterText(NUM_CONST);
		window.button(JButtonMatcher.withText("Add")).click();
		assertThat(window.list().contents()).isNotEmpty();
	}

	@Test
	@GUITest
	public void testAddButtonError() {
		populateDatabase();
		window.textBox("museum").enterText(MUSEUM1_TEST);
		window.textBox("rooms").enterText(NUM_CONST);
		window.button(JButtonMatcher.withText("Add")).click();
		assertThat(window.list().contents()).isEmpty();
		window.label("errorMessageLabel").requireText("Impossibile to add Museum: " + MUSEUM1_TEST);
	}

	@Test
	@GUITest
	public void testDeleteButtonSuccess() {
		populateDatabase();
		GuiActionRunner.execute(() -> museumSwingController.getAllMuseums());
		window.list().selectItem(0);
		window.button(JButtonMatcher.withText("Delete Selected")).click();
		assertThat(window.list().contents()).hasSize(1);
	}

	@Test
	@GUITest
	public void testDeleteButtonError() {
		populateDatabase();
		Museum notExistingMuseum = new Museum(MUSEUM3_TEST, 10);
		GuiActionRunner.execute(() -> museumSwingView.getMuseumListModel().addElement(notExistingMuseum));
		window.list().selectItem(0);
		window.button(JButtonMatcher.withText("Delete Selected")).click();
		window.label("errorMessageLabel").requireText("Impossible to delete Museum: " + MUSEUM3_TEST);
	}

	@AfterClass
	public static void afterClass() {
		entityManager.clear();
		entityManager.close();
		sessionFactory.close();
	}

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
