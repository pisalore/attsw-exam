package com.unifi.attsw.exam.e2e.swing.app;

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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GUITestRunner.class)
public class ExhibitionSwingAppE2E extends AssertJSwingJUnitTestCase {

	private static final String MUSEUM1_TEST = "museum1_test";

	private static final String EXHIBITION1_TEST = "exhibition1_test";
	private static final String EXHIBITION3_TEST = "exhibition3_test";

	private static final String NUM_CONST = "10";

	private static EntityManagerFactory sessionFactory;
	private static EntityManager entityManager;
	private FrameFixture exhibitionWindow;

	@BeforeClass
	public static void beforeClass() {
		sessionFactory = Persistence.createEntityManagerFactory("postgres.e2e-tests");
	}

	@Override
	protected void onSetUp() throws Exception {
		entityManager = sessionFactory.createEntityManager();
		populateDatabase();

		application("com.unifi.attsw.exam.app.swing.MuseumSwingApp")
				.withArgs("--database-url=" + sessionFactory.getProperties().get("javax.persistence.jdbc.url"),
						"--database-user=museum_manager", "--database-password=attsw")
				.start();

		exhibitionWindow = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class, true) {
			protected boolean isMatching(JFrame frame) {
				return "Exhibitions Dashboard".equals(frame.getTitle());
			}
		}).using(robot());

	}

	@Test
	@GUITest
	public void testFindAllExhibitions() {
		exhibitionWindow.moveToFront();
		String[] listAllExhibitions = exhibitionWindow.list("listAllExh").contents();
		exhibitionWindow.button(JButtonMatcher.withText("Find all")).click();

		assertThat(listAllExhibitions).containsExactly("exhibition1_test - Total Seats: 100 - Booked Seats: 0",
				"exhibition2_test - Total Seats: 100 - Booked Seats: 0");
	}


	@Test
	@GUITest
	public void testAddExhibition() {
		exhibitionWindow.moveToFront();
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
		exhibitionWindow.moveToFront();
		exhibitionWindow.textBox("exhibitionTextField").enterText(EXHIBITION1_TEST);
		exhibitionWindow.textBox("totalSeatsTextField").enterText(NUM_CONST);
		exhibitionWindow.textBox("museumNameTextField").enterText(MUSEUM1_TEST);

		exhibitionWindow.button(JButtonMatcher.withText("Add Exhibition")).click();
		exhibitionWindow.label("errorLabel").requireText("Impossible to add Exhibition: " + EXHIBITION1_TEST);

	}

	@Test
	@GUITest
	public void testDeleteExhibition() {
		exhibitionWindow.moveToFront();
		exhibitionWindow.textBox("exhibitionTextField").enterText(EXHIBITION1_TEST);
		
		exhibitionWindow.list("listAllExh").selectItem(0);
		exhibitionWindow.button(JButtonMatcher.withText("Delete")).click();

		assertThat(exhibitionWindow.list("listAllExh").contents())
				.containsExactly("exhibition2_test - Total Seats: 100 - Booked Seats: 0");
	}

	@Test
	@GUITest
	public void testBookExhibition() {
		exhibitionWindow.moveToFront();
		exhibitionWindow.textBox("exhibitionTextField").enterText(EXHIBITION1_TEST);

		exhibitionWindow.list("listAllExh").selectItem(0);
		exhibitionWindow.button(JButtonMatcher.withText("Book")).click();

		assertThat(exhibitionWindow.list("listAllExh").contents())
				.contains("exhibition1_test - Total Seats: 100 - Booked Seats: 1");
	}
	
	@Test
	@GUITest
	public void testFindMuseumExhibitions() {
		exhibitionWindow.moveToFront();
		JListFixture listMuseumExhibitions = exhibitionWindow.list("listMuseumExh");

		exhibitionWindow.textBox("findMuseumTextField").enterText(MUSEUM1_TEST);
		exhibitionWindow.button(JButtonMatcher.withText("Find Museum Exh.")).click();
		assertThat(listMuseumExhibitions.contents()).hasSize(2);
	}

	@After
	public void afterTearDown() {
		entityManager.getTransaction().begin();
		entityManager.createNativeQuery("TRUNCATE TABLE Museums CASCADE").executeUpdate();
		entityManager.getTransaction().commit();

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
