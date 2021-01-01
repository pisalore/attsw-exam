package com.unifi.attsw.exam.presentation.view.swing;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JListFixture;
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
import com.unifi.attsw.exam.repository.model.Exhibition;
import com.unifi.attsw.exam.repository.transaction.manager.postgres.PostgresTransactionManager;

@RunWith(GUITestRunner.class)
public class ExhibitionSwingViewIT extends AssertJSwingJUnitTestCase {

	private static final String MUSEUM1_TEST = "museum1_test";

	private static final String EXHIBITION1_TEST = "exhibition1_test";
	private static final String EXHIBITION3_TEST = "exhibition3_test";
	private static final String NUM_CONST = "10";

	private FrameFixture window;
	private static EntityManagerFactory sessionFactory;
	private static EntityManager entityManager;
	private static PostgresTransactionManager transactionManager;
	private static MuseumManagerService museumManager;
	private static ExhibitionSwingView exhibitionSwingView;

	// I want to test only the integration of controller and Exhibition swing view
	@Mock
	private static MuseumSwingView museumView;

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
			exhibitionSwingView = new ExhibitionSwingView();
			museumSwingController = new MuseumSwingController(museumManager, museumView, exhibitionSwingView);
			exhibitionSwingView.setMuseumController(museumSwingController);
			return exhibitionSwingView;
		});

		window = new FrameFixture(robot(), exhibitionSwingView);
		window.show();
	}

	@Test
	@GUITest
	public void testGetAllExhibitions() {
		populateDatabase();

		JListFixture listAllExhibitions = window.list("listAllExh");
		window.button(JButtonMatcher.withText("Find all")).click();
		assertThat(listAllExhibitions.contents()).isNotEmpty();
	}

	@Test
	@GUITest
	public void testAddExhibitionButtonSuccess() {
		populateDatabase();

		JListFixture listAllExhibitions = window.list("listAllExh");
		window.textBox("exhibitionTextField").enterText(EXHIBITION3_TEST);
		window.textBox("totalSeatsTextField").enterText(NUM_CONST);
		window.textBox("museumNameTextField").enterText(MUSEUM1_TEST);

		window.button(JButtonMatcher.withText("Add Exhibition")).click();
		assertThat(listAllExhibitions.contents()).hasSize(1);
	}

	@Test
	@GUITest
	public void testAddExhibitionButtonError() {
		populateDatabase();

		JListFixture listAllExhibitions = window.list("listAllExh");
		window.textBox("exhibitionTextField").enterText(EXHIBITION1_TEST);
		window.textBox("totalSeatsTextField").enterText(NUM_CONST);
		window.textBox("museumNameTextField").enterText(MUSEUM1_TEST);

		window.button(JButtonMatcher.withText("Add Exhibition")).click();
		window.label("errorLabel").requireText("Impossible to add Exhibition: " + EXHIBITION1_TEST);
		assertThat(listAllExhibitions.contents()).isEmpty();
	}

	@Test
	@GUITest
	public void testGetMuseumExhibitions() {
		populateDatabase();
		JListFixture listMuseumExhibitions = window.list("listMuseumExh");

		window.textBox("findMuseumTextField").enterText(MUSEUM1_TEST);
		window.button(JButtonMatcher.withText("Find Museum Exh.")).click();
		assertThat(listMuseumExhibitions.contents()).hasSize(2);
	}

	@Test
	@GUITest
	public void testDeleteExhibition() {
		populateDatabase();
		GuiActionRunner.execute(() -> museumSwingController.getAllExhibitions());
		// select an exhibition
		window.list("listAllExh").selectItem(0);
		window.button(JButtonMatcher.withText("Delete")).click();

		window.button(JButtonMatcher.withText("Find all")).click();
		assertThat(window.list("listAllExh").contents()).hasSize(1);
	}

	@Test
	@GUITest
	public void testDeleteButtonError() {
		populateDatabase();
		Exhibition notExistingExhibition = new Exhibition(EXHIBITION3_TEST, 10);
		GuiActionRunner
				.execute(() -> exhibitionSwingView.getAllExhibitionsListModel().addElement(notExistingExhibition));
		JListFixture listAllExhibitions = window.list("listAllExh");
		listAllExhibitions.selectItem(0);
		window.button(JButtonMatcher.withText("Delete")).click();
		window.label("errorLabel").requireText("Impossible to delete Exhibition: " + EXHIBITION3_TEST);
	}

	@Test
	@GUITest
	public void testBookExhibition() {
		populateDatabase();
		JListFixture listAllExhibitions = window.list("listAllExh");
		GuiActionRunner.execute(() -> museumSwingController.getAllExhibitions());
		// select an exhibition
		listAllExhibitions.selectItem(0);
		window.button(JButtonMatcher.withText("Book")).click();

		window.button(JButtonMatcher.withText("Find all")).click();
		String[] listContents = listAllExhibitions.contents();
		assertThat(listContents).contains("exhibition1_test - Total Seats: 100 - Booked Seats: 1");
	}

	@Test
	@GUITest
	public void testBookExhibitionError() {
		populateDatabase();
		Exhibition notExistingExhibition = new Exhibition(EXHIBITION3_TEST, 10);
		GuiActionRunner
				.execute(() -> exhibitionSwingView.getAllExhibitionsListModel().addElement(notExistingExhibition));
		JListFixture listAllExhibitions = window.list("listAllExh");
		listAllExhibitions.selectItem(0);
		window.button(JButtonMatcher.withText("Book")).click();
		window.label("errorLabel").requireText("Impossible to book a seat for Exhibition: " + EXHIBITION3_TEST);
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
