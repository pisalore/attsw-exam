package com.unifi.attsw.exam.it.view.swing;

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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import com.unifi.attsw.exam.controller.swingController.MuseumSwingController;
import com.unifi.attsw.exam.model.Exhibition;
import com.unifi.attsw.exam.service.MuseumManagerService;
import com.unifi.attsw.exam.service.impl.MuseumManagerServiceImpl;
import com.unifi.attsw.exam.transaction.manager.postgres.PostgresTransactionManager;
import com.unifi.attsw.exam.view.swing.ExhibitionSwingView;
import com.unifi.attsw.exam.view.swing.MuseumSwingView;

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

	// I want to test only the integration of controller and Museum swing view
	@Mock
	private static MuseumSwingView museumView;

	private static MuseumSwingController museumSwingController;

	@Override
	protected void onSetUp() {
		sessionFactory = Persistence.createEntityManagerFactory("postgres.not-empty.database");
		entityManager = sessionFactory.createEntityManager();
		transactionManager = new PostgresTransactionManager(entityManager);
		museumManager = new MuseumManagerServiceImpl(transactionManager);

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
		JListFixture listAllExhibitions = window.list("listAllExh");

		window.button(JButtonMatcher.withText("Find all")).click();
		assertThat(listAllExhibitions.contents()).isNotEmpty();
	}

	@Test
	@GUITest
	public void testAddExhibitionButtonSuccess() {
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
		JListFixture listAllExhibitions = window.list("listAllExh");

		window.textBox("exhibitionTextField").enterText(EXHIBITION1_TEST);
		window.textBox("totalSeatsTextField").enterText(NUM_CONST);
		window.textBox("museumNameTextField").enterText(MUSEUM1_TEST);

		window.button(JButtonMatcher.withText("Add Exhibition")).click();
		window.label("errorLabel").requireText("Impossible to add Exhibition: " + EXHIBITION1_TEST);
		assertThat(listAllExhibitions.contents()).hasSize(0);
	}

	@Test
	@GUITest
	public void testGetMuseumExhibitions() {
		JListFixture listMuseumExhibitions = window.list("listMuseumExh");

		window.textBox("findMuseumTextField").enterText(MUSEUM1_TEST);
		window.button(JButtonMatcher.withText("Find Museum Exh.")).click();
		assertThat(listMuseumExhibitions.contents()).hasSize(2);
	}

	@Test
	@GUITest
	public void testDeleteExhibition() {
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
		Exhibition notExistingExhibition = new Exhibition(EXHIBITION3_TEST, 10);
		GuiActionRunner
				.execute(() -> exhibitionSwingView.getAllExhibitionsListModel().addElement(notExistingExhibition));
		JListFixture listAllExhibitions = window.list("listAllExh");
		listAllExhibitions.selectItem(0);
		window.button(JButtonMatcher.withText("Book")).click();
		window.label("errorLabel")
				.requireText("Impossible to book a seat for Exhibition: " + EXHIBITION3_TEST);
	}

	@Override
	protected void onTearDown() {
		entityManager.clear();
		entityManager.close();
		sessionFactory.close();
	}

}
