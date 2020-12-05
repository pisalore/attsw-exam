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
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import static org.assertj.swing.launcher.ApplicationLauncher.application;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GUITestRunner.class)
public class MuseumSwingAppE2E extends AssertJSwingJUnitTestCase {

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
		populateDatabase();

		application("com.unifi.attsw.exam.app.swing.MuseumSwingApp")
				.withArgs("--database-url=" + sessionFactory.getProperties().get("javax.persistence.jdbc.url"),
						"--database-user=museum_manager", "--database-password=attsw")
				.start();

		museumWindow = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class, true) {
			protected boolean isMatching(JFrame frame) {
				return "Museum Dashboard".equals(frame.getTitle());
			}
		}).using(robot());

		exhibitionWindow = WindowFinder.findFrame(new GenericTypeMatcher<JFrame>(JFrame.class, true) {
			protected boolean isMatching(JFrame frame) {
				return "Exhibitions Dashboard".equals(frame.getTitle());
			}
		}).using(robot());

	}

	@Test
	@GUITest
	public void testWhenapplicationStartsSHouldShowMuseumandExhibitions() {
		String[] listAllMueums = museumWindow.list().contents();
		String[] listAllExhibitions = exhibitionWindow.list("listAllExh").contents();

		assertThat(listAllMueums).containsExactly("museum1_test - Total Rooms: 10 - Occupied Rooms: 0",
				"museum2_test - Total Rooms: 10 - Occupied Rooms: 0");

		assertThat(listAllExhibitions).containsExactly("exhibition1_test - Total Seats: 100 - Booked Seats: 0",
				"exhibition2_test - Total Seats: 100 - Booked Seats: 0");

	}

	@Test
	@GUITest
	public void testFindAllMuseums() {
		String[] listAllMueums = museumWindow.list().contents();
		museumWindow.button(JButtonMatcher.withText("Find all")).click();

		assertThat(listAllMueums).isNotEmpty().containsExactly("museum1_test - Total Rooms: 10 - Occupied Rooms: 0",
				"museum2_test - Total Rooms: 10 - Occupied Rooms: 0");

	}

	@Test
	@GUITest
	public void testFindAllExhibitions() {
		String[] listAllExhibitions = exhibitionWindow.list("listAllExh").contents();
		exhibitionWindow.button(JButtonMatcher.withText("Find all")).click();

		assertThat(listAllExhibitions).containsExactly("exhibition1_test - Total Seats: 100 - Booked Seats: 0",
				"exhibition2_test - Total Seats: 100 - Booked Seats: 0");
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
