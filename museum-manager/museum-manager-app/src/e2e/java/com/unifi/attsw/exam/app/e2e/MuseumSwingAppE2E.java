package com.unifi.attsw.exam.app.e2e;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JFrame;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JListFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import static org.assertj.swing.launcher.ApplicationLauncher.application;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GUITestRunner.class)
public class MuseumSwingAppE2E extends AssertJSwingJUnitTestCase {

	private static final String MUSEUM1_TEST = "museum1_test";
	private static final String MUSEUM3_TEST = "museum3_test";

	private static final String EXHIBITION1_TEST = "exhibition1_test";
	private static final String EXHIBITION3_TEST = "exhibition3_test";

	private static final String NUM_CONST = "10";

	private static EntityManagerFactory sessionFactory;
	private static EntityManager entityManager;
	private FrameFixture museumWindow;
	private FrameFixture exhibitionWindow;

	@BeforeClass
	public static void beforeClass() {
		sessionFactory = Persistence.createEntityManagerFactory("postgres.e2e-tests");
	}

	@Override
	protected void onSetUp() throws Exception {
		entityManager = sessionFactory.createEntityManager();

		entityManager.getTransaction().begin();
		entityManager.createNativeQuery("TRUNCATE TABLE Museums CASCADE").executeUpdate();
		entityManager.getTransaction().commit();

		populateDatabase();

		application("com.unifi.attsw.exam.app.MuseumSwingApp")
				.withArgs("--database-url=" + sessionFactory.getProperties().get("javax.persistence.jdbc.url"),
						"--database-user=museum_manager", "--database-password=attsw")
				.start();

		museumWindow = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class, true) {
			protected boolean isMatching(JFrame frame) {
				return "Museum Dashboard".equals(frame.getTitle());
			}
		}).using(robot());

	}

	@Test
	@GUITest
	public void testAddMuseumSuccess() {
		museumWindow.textBox("museum").enterText(MUSEUM3_TEST);
		museumWindow.textBox("rooms").enterText(NUM_CONST);
		museumWindow.button(JButtonMatcher.withText("Add")).click();

		assertThat(museumWindow.list().contents()).contains("museum3_test - Total Rooms: 10 - Occupied Rooms: 0");
	}

	@Test
	@GUITest
	public void testAddMuseumError() {
		museumWindow.textBox("museum").enterText(MUSEUM1_TEST);
		museumWindow.textBox("rooms").enterText(NUM_CONST);
		museumWindow.button(JButtonMatcher.withText("Add")).click();

		museumWindow.label("errorMessageLabel").requireText("Impossibile to add Museum: " + MUSEUM1_TEST);

	}

	@Test
	@GUITest
	public void testDeleteMuseum() {
		museumWindow.list().selectItem(0);
		museumWindow.button(JButtonMatcher.withText("Delete Selected")).click();

		assertThat(museumWindow.list().contents())
				.containsExactly("museum2_test - Total Rooms: 10 - Occupied Rooms: 0");

	}

	@Test
	@GUITest
	public void testAddExhibition() {
		museumWindow.button(JButtonMatcher.withText("Exhibitions Dashboard")).click();

		exhibitionWindow = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class, true) {
			protected boolean isMatching(JFrame frame) {
				return "Exhibitions Dashboard".equals(frame.getTitle());
			}
		}).using(robot());

		JListFixture listAllExhibitions = exhibitionWindow.list("listAllExh");

		exhibitionWindow.textBox("exhibitionTextField").enterText(EXHIBITION3_TEST);
		exhibitionWindow.textBox("totalSeatsTextField").enterText(NUM_CONST);
		exhibitionWindow.textBox("museumNameTextField").enterText(MUSEUM1_TEST);

		exhibitionWindow.button(JButtonMatcher.withText("Add Exhibition")).click();
		assertThat(listAllExhibitions.contents()).contains("exhibition3_test - Total Seats: 10 - Booked Seats: 0");
	}

	@Test
	@GUITest
	public void testAddExhibitionError() {
		museumWindow.button(JButtonMatcher.withText("Exhibitions Dashboard")).click();

		exhibitionWindow = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class, true) {
			protected boolean isMatching(JFrame frame) {
				return "Exhibitions Dashboard".equals(frame.getTitle());
			}
		}).using(robot());

		exhibitionWindow.textBox("exhibitionTextField").enterText(EXHIBITION1_TEST);
		exhibitionWindow.textBox("totalSeatsTextField").enterText(NUM_CONST);
		exhibitionWindow.textBox("museumNameTextField").enterText(MUSEUM1_TEST);

		exhibitionWindow.button(JButtonMatcher.withText("Add Exhibition")).click();
		exhibitionWindow.label("errorLabel").requireText("Impossible to add Exhibition: " + EXHIBITION1_TEST);

	}

	@Test
	@GUITest
	public void testDeleteExhibition() {
		museumWindow.button(JButtonMatcher.withText("Exhibitions Dashboard")).click();

		exhibitionWindow = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class, true) {
			protected boolean isMatching(JFrame frame) {
				return "Exhibitions Dashboard".equals(frame.getTitle());
			}
		}).using(robot());

		exhibitionWindow.list("listAllExh").selectItem(0);
		exhibitionWindow.button(JButtonMatcher.withText("Delete")).click();

		assertThat(exhibitionWindow.list("listAllExh").contents())
				.containsExactly("exhibition2_test - Total Seats: 100 - Booked Seats: 0");
	}

	@Test
	@GUITest
	public void testBookExhibition() {
		museumWindow.button(JButtonMatcher.withText("Exhibitions Dashboard")).click();

		exhibitionWindow = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class, true) {
			protected boolean isMatching(JFrame frame) {
				return "Exhibitions Dashboard".equals(frame.getTitle());
			}
		}).using(robot());

		exhibitionWindow.list("listAllExh").selectItem(0);
		exhibitionWindow.button(JButtonMatcher.withText("Book")).click();

		assertThat(exhibitionWindow.list("listAllExh").contents())
				.contains("exhibition1_test - Total Seats: 100 - Booked Seats: 1");
	}

	@Test
	@GUITest
	public void testFindMuseumExhibitions() {
		museumWindow.button(JButtonMatcher.withText("Exhibitions Dashboard")).click();

		exhibitionWindow = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class, true) {
			protected boolean isMatching(JFrame frame) {
				return "Exhibitions Dashboard".equals(frame.getTitle());
			}
		}).using(robot());

		JListFixture listMuseumExhibitions = exhibitionWindow.list("listMuseumExh");

		exhibitionWindow.textBox("findMuseumTextField").enterText(MUSEUM1_TEST);
		exhibitionWindow.button(JButtonMatcher.withText("Find Museum Exh.")).click();
		assertThat(listMuseumExhibitions.contents()).hasSize(2);
	}

	@AfterClass
	public static void afterClass() {
		entityManager.clear();
		entityManager.close();
		sessionFactory.close();
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
