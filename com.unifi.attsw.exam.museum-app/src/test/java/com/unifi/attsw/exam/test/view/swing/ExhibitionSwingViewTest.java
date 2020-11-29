package com.unifi.attsw.exam.test.view.swing;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JListFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.unifi.attsw.exam.controller.MuseumController;
import com.unifi.attsw.exam.model.Exhibition;
import com.unifi.attsw.exam.view.swing.ExhibitionSwingView;

@RunWith(GUITestRunner.class)
public class ExhibitionSwingViewTest extends AssertJSwingJUnitTestCase {

	private static final String MUSEUM1_TEST = "museum1_test";

	private static final String EXHIBITION1_TEST = "exhibition1_test";
	private static final String EXHIBITION2_TEST = "exhibition2_test";
	private static final String NUM_CONST = "10";

	private FrameFixture window;

	private ExhibitionSwingView exhibitionSwingView;

	@Mock
	private MuseumController museumController;

	@Override
	protected void onSetUp() {
		MockitoAnnotations.initMocks(this);
		GuiActionRunner.execute(() -> {
			exhibitionSwingView = new ExhibitionSwingView();
			exhibitionSwingView.setMuseumController(museumController);
			return exhibitionSwingView;
		});
		window = new FrameFixture(robot(), exhibitionSwingView);
		window.show();

	}

	@Test
	public void testExhibitionDashboardControlsInitialState() {
		window.label(JLabelMatcher.withText("Exhibition"));
		window.label(JLabelMatcher.withText("Total Seats"));
		window.label(JLabelMatcher.withText("Museum"));
		window.label("errorLabel").requireText(" ");
		window.label(JLabelMatcher.withText("Museum Name"));
		window.label(JLabelMatcher.withText("Exhibitions list for Museum"));
		window.textBox("exhibitionTextField").requireEnabled();
		window.textBox("totalSeatsTextField").requireEnabled();
		window.textBox("museumNameTextField").requireEnabled();
		window.textBox("findMuseumTextField").requireEnabled();
		window.button(JButtonMatcher.withText("Add Exhibition")).requireDisabled();
		window.button(JButtonMatcher.withText("Find all")).requireEnabled();
		window.button(JButtonMatcher.withText("Find Museum Exh.")).requireDisabled();
		window.button(JButtonMatcher.withText("Book Selected")).requireDisabled();
		window.button(JButtonMatcher.withText("Delete Selected")).requireDisabled();
		window.list("listAllExh");
		window.list("listMuseumExh");
	}

	@Test
	public void testWhenExhibitionNameAndSeatsandMuseumNameAreFilledAddExhibitionButtonShouldBeEnabled() {
		window.textBox("exhibitionTextField").enterText(EXHIBITION1_TEST);
		window.textBox("totalSeatsTextField").enterText(NUM_CONST);
		window.textBox("museumNameTextField").enterText(MUSEUM1_TEST);
		window.button(JButtonMatcher.withText("Add Exhibition")).requireEnabled();
	}

	@Test
	public void testWhenMuseumNameIsFilledFindMuseumExhibitionButtonShouldBeEnabled() {
		window.textBox("findMuseumTextField").enterText(MUSEUM1_TEST);
		window.button(JButtonMatcher.withText("Find Museum Exh.")).requireEnabled();
	}

	@Test
	public void testWhenExhibitionOrSeatsOrMuseumAreBlankAddExhibitionButtonShouldBeDisabled() {
		JTextComponentFixture exhibitionTextBox = window.textBox("exhibitionTextField");
		JTextComponentFixture seatsTextBox = window.textBox("totalSeatsTextField");
		JTextComponentFixture museumNameTextBox = window.textBox("museumNameTextField");

		exhibitionTextBox.enterText(EXHIBITION1_TEST);
		seatsTextBox.enterText(" ");
		museumNameTextBox.enterText(MUSEUM1_TEST);
		window.button(JButtonMatcher.withText("Add Exhibition")).requireDisabled();

		exhibitionTextBox.setText("");
		seatsTextBox.setText("");
		museumNameTextBox.setText("");

		exhibitionTextBox.enterText(" ");
		seatsTextBox.enterText(NUM_CONST);
		museumNameTextBox.enterText(MUSEUM1_TEST);
		window.button(JButtonMatcher.withText("Add Exhibition")).requireDisabled();

		exhibitionTextBox.setText("");
		seatsTextBox.setText("");
		museumNameTextBox.setText("");

		exhibitionTextBox.enterText(EXHIBITION1_TEST);
		seatsTextBox.enterText(NUM_CONST);
		museumNameTextBox.enterText(" ");
		window.button(JButtonMatcher.withText("Add Exhibition")).requireDisabled();

	}

	@Test
	public void testDeleteButtonShouldBeEnabledOnlyWhenAnExhibitionIsSelected() {
		GuiActionRunner.execute(() -> {
			exhibitionSwingView.getAllExhibitionsListModel().addElement(new Exhibition(EXHIBITION1_TEST, 10));
			exhibitionSwingView.getMuseumsExhibitionListModel().addElement(new Exhibition(EXHIBITION2_TEST, 10));
		});
		JListFixture listAllExhibitions = window.list("listAllExh");
		JListFixture listMuseumExhibitions = window.list("listMuseumExh");
		JButtonFixture deleteButton = window.button(JButtonMatcher.withText("Delete Selected"));

		listAllExhibitions.selectItem(0);
		deleteButton.requireEnabled();
		listAllExhibitions.clearSelection();
		deleteButton.requireDisabled();

		listMuseumExhibitions.selectItem(0);
		deleteButton.requireEnabled();
		listMuseumExhibitions.clearSelection();
		deleteButton.requireDisabled();

	}

	@Test
	public void testBookButtonshouldBeEnabledOnlyWhenAnExhibitionIsSelected() {
		GuiActionRunner.execute(() -> {
			exhibitionSwingView.getAllExhibitionsListModel().addElement(new Exhibition(EXHIBITION1_TEST, 10));
			exhibitionSwingView.getMuseumsExhibitionListModel().addElement(new Exhibition(EXHIBITION2_TEST, 10));
		});

		JListFixture listAllExhibitions = window.list("listAllExh");
		JListFixture listMuseumExhibitions = window.list("listMuseumExh");
		JButtonFixture bookButton = window.button(JButtonMatcher.withText("Book Selected"));

		listAllExhibitions.selectItem(0);
		bookButton.requireEnabled();
		listAllExhibitions.clearSelection();
		bookButton.requireDisabled();

		listMuseumExhibitions.selectItem(0);
		bookButton.requireEnabled();
		listMuseumExhibitions.clearSelection();
		bookButton.requireDisabled();
	}

	@Test
	public void testShowAllExhibitions() {
		JListFixture listAllExhibitions = window.list("listAllExh");
		Exhibition exhibition1 = new Exhibition(EXHIBITION1_TEST, 10);
		Exhibition exhibition2 = new Exhibition(EXHIBITION2_TEST, 10);
		GuiActionRunner.execute(() -> exhibitionSwingView.showAllExhibitions(Arrays.asList(exhibition1, exhibition2)));
		String[] listContents = listAllExhibitions.contents();
		assertThat(listContents).containsExactly("exhibition1_test - Total Seats: 10 - Booked Seats: 0",
				"exhibition2_test - Total Seats: 10 - Booked Seats: 0");

	}

	@Test
	public void testShowMuseumExhibitions() {
		JListFixture listMuseumExhibitions = window.list("listMuseumExh");
		Exhibition exhibition1 = new Exhibition(EXHIBITION1_TEST, 10);
		Exhibition exhibition2 = new Exhibition(EXHIBITION2_TEST, 10);
		GuiActionRunner
				.execute(() -> exhibitionSwingView.showMuseumExhibitions(Arrays.asList(exhibition1, exhibition2)));
		String[] listContents = listMuseumExhibitions.contents();
		assertThat(listContents).containsExactly("exhibition1_test - Total Seats: 10 - Booked Seats: 0",
				"exhibition2_test - Total Seats: 10 - Booked Seats: 0");

	}

	@Test
	public void testShowErrorShouldShowTheMessageInErrorLabel() {
		Exhibition exhibition1 = new Exhibition(EXHIBITION1_TEST, 10);
		GuiActionRunner.execute(() -> exhibitionSwingView.showError("error message: ", exhibition1));
		window.label("errorLabel").requireText("error message: " + exhibition1.getName());

	}

	@Test
	@GUITest
	public void testExhibitionAddedShouldAddTheExhibitionAndResetMuseumExhibitionPart() {
		Exhibition exhibition1 = new Exhibition(EXHIBITION1_TEST, 10);
		window.textBox("findMuseumTextField").setText(MUSEUM1_TEST);
		
		GuiActionRunner.execute(() -> {
			exhibitionSwingView.getAllExhibitionsListModel().addElement(exhibition1);
			exhibitionSwingView.getMuseumsExhibitionListModel().addElement(exhibition1);
			exhibitionSwingView.exhibitionAdded((new Exhibition(EXHIBITION2_TEST, 10)));
		});
		JListFixture listAllExhibitions = window.list("listAllExh");
		JListFixture listMuseumExhibitions = window.list("listMuseumExh");

		String[] listAllMuseumsContents = listAllExhibitions.contents();
		String[] listMuseumExhibitionContents = listMuseumExhibitions.contents();

		window.label("errorLabel").requireText(" ");
		window.textBox("findMuseumTextField").requireText(" ");
		assertThat(listAllMuseumsContents).containsExactly("exhibition1_test - Total Seats: 10 - Booked Seats: 0",
				"exhibition2_test - Total Seats: 10 - Booked Seats: 0");

		assertThat(listMuseumExhibitionContents).isEmpty();
	}

}
