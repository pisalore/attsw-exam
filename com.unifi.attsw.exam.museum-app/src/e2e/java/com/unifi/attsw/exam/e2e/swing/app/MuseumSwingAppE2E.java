package com.unifi.attsw.exam.e2e.swing.app;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JFrame;

import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.finder.WindowFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JListFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import static org.assertj.swing.launcher.ApplicationLauncher.application;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GUITestRunner.class)
public class MuseumSwingAppE2E extends AssertJSwingJUnitTestCase {

	private static EntityManagerFactory sessionFactory;
	private EntityManager entityManager;
	private FrameFixture museumWindow;
	private FrameFixture exhibitionWindow;

	@BeforeClass
	public static void beforeClass() {
		sessionFactory = Persistence.createEntityManagerFactory("postgres.e2e-tests.not-empty.database");
	}

	@Override
	protected void onSetUp() throws Exception {
		entityManager = sessionFactory.createEntityManager();

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
	public void testWhenapplicationStartsSHouldShowMuseumandExhibitions() {
		String[] listAllMueums = museumWindow.list().contents();
		String[] listAllExhibitions = exhibitionWindow.list("listAllExh").contents();

		assertThat(listAllMueums).containsExactly("museum1_test - Total Rooms: 10 - Occupied Rooms: 0",
				"museum2_test - Total Rooms: 10 - Occupied Rooms: 0");
		
		assertThat(listAllExhibitions).containsExactly("exhibition1_test - Total Seats: 100 - Booked Seats: 0",
				"exhibition2_test - Total Seats: 100 - Booked Seats: 0");

	}

	@After
	public void afterTearDown() {
		entityManager.getTransaction().begin();
		entityManager.createNativeQuery("TRUNCATE TABLE Museums CASCADE").executeUpdate();
//		entityManager.createNativeQuery("ALTER SEQUENCE hibernate_sequence RESTART WITH 10").executeUpdate();
		entityManager.getTransaction().commit();

	}

}
