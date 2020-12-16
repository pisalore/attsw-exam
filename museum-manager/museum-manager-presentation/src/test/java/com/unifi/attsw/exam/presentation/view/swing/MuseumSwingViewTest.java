package com.unifi.attsw.exam.presentation.view.swing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import javax.swing.DefaultListModel;

import org.assertj.swing.annotation.GUITest;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.unifi.attsw.exam.presentation.controller.swing.MuseumSwingController;
import com.unifi.attsw.exam.presentation.view.swing.MuseumSwingView;
import com.unifi.attsw.exam.repository.model.Museum;

@RunWith(GUITestRunner.class)
public class MuseumSwingViewTest extends AssertJSwingJUnitTestCase {

	private static final String MUSEUM1_TEST = "museum1_test";
	private static final String MUSEUM2_TEST = "museum2_test";
	private static final String NUM_CONST = "10";

	private FrameFixture window;

	private MuseumSwingView museumSwingView;

	@Mock
	private MuseumSwingController museumSwingController;

	@Override
	protected void onSetUp() {
		MockitoAnnotations.initMocks(this);
		GuiActionRunner.execute(() -> {
			museumSwingView = new MuseumSwingView();
			museumSwingView.setMuseumController(museumSwingController);
			return museumSwingView;
		});
		window = new FrameFixture(robot(), museumSwingView);
		window.show();
	}

	@Test
	public void testMuseumDashboardControlsInitialState() {
		window.label(JLabelMatcher.withText("Museum"));
		window.textBox("museum").requireEnabled();
		window.label(JLabelMatcher.withText("Rooms"));
		window.textBox("rooms").requireEnabled();
		window.button(JButtonMatcher.withText("Add")).requireDisabled();
		window.button(JButtonMatcher.withText("Find all")).requireEnabled();
		window.list("museumList");
		window.button(JButtonMatcher.withText("Delete Selected")).requireDisabled();
		window.button(JButtonMatcher.withText("Exhibitions Dashboard")).requireEnabled();
		window.label("errorMessageLabel").requireText(" ");
	}

	@Test
	public void testWhenMuseumNameAndRoomsAreFilledAddButtonShouldBeEnabled() {
		window.textBox("museum").enterText(MUSEUM1_TEST);
		window.textBox("rooms").enterText(NUM_CONST);
		window.button(JButtonMatcher.withText("Add")).requireEnabled();
	}

	@Test
	public void testWhenEitherMuseumOrRoomsAreBlankAddButtonShouldBeDisabled() {
		JTextComponentFixture museumTextBox = window.textBox("museum");
		JTextComponentFixture roomsTextBox = window.textBox("rooms");

		museumTextBox.enterText(MUSEUM1_TEST);
		roomsTextBox.enterText(" ");
		window.button(JButtonMatcher.withText("Add")).requireDisabled();

		museumTextBox.setText("");
		roomsTextBox.setText("");

		museumTextBox.enterText(" ");
		roomsTextBox.enterText(NUM_CONST);
		window.button(JButtonMatcher.withText("Add")).requireDisabled();

	}

	@Test
	@GUITest
	public void testDeleteButtonShouldBeEnabledOnlyWhenAMuseumIsSelected() {
		GuiActionRunner.execute(() -> museumSwingView.getMuseumListModel().addElement(new Museum(MUSEUM1_TEST, 10)));
		window.list("museumList").selectItem(0);
		window.button(JButtonMatcher.withText("Delete Selected")).requireEnabled();
		window.list("museumList").clearSelection();
		window.button(JButtonMatcher.withText("Delete Selected")).requireDisabled();
	}

	@Test
	@GUITest
	public void testShowAllMuseums() {
		Museum museum1 = new Museum(MUSEUM1_TEST, 10);
		Museum museum2 = new Museum(MUSEUM2_TEST, 10);
		GuiActionRunner.execute(() -> museumSwingView.showAllMuseums(Arrays.asList(museum1, museum2)));
		String[] listContents = window.list().contents();
		assertThat(listContents).containsExactly("museum1_test - Total Rooms: 10 - Occupied Rooms: 0",
				"museum2_test - Total Rooms: 10 - Occupied Rooms: 0");
	}

	@Test
	@GUITest
	public void testShowErrorShouldShowTheMessageInLabel() {
		Museum museum1 = new Museum(MUSEUM1_TEST, 10);
		GuiActionRunner.execute(() -> museumSwingView.showError("error message: ", museum1));
		window.label("errorMessageLabel").requireText("error message: " + museum1.getName());

	}
	
	@Test
	@GUITest
	public void testShowGeneralErrorShouldShowTheMessageInLabel() {
		GuiActionRunner.execute(() -> museumSwingView.showError("error message: ", null));
		window.label("errorMessageLabel").requireText("error message: ");

	}

	@Test
	public void testMuseumAddedShouldAddTheMuseumToTheListAndResetTheErrorLabel() {
		GuiActionRunner.execute(() -> museumSwingView.museumAdded(new Museum(MUSEUM1_TEST, 10)));
		String[] listContents = window.list().contents();
		assertThat(listContents).containsExactly("museum1_test - Total Rooms: 10 - Occupied Rooms: 0");
		window.label("errorMessageLabel").requireText(" ");
	}

	@Test
	public void testMuseumRemovedShouldRemoveTheMuseumFromTheListAndResetTheErrorLabel() {
		// setup
		Museum museum1 = new Museum(MUSEUM1_TEST, 10);
		Museum museum2 = new Museum(MUSEUM2_TEST, 10);
		GuiActionRunner.execute(() -> {
			DefaultListModel<Museum> museumsListModel = museumSwingView.getMuseumListModel();
			museumsListModel.addElement(museum1);
			museumsListModel.addElement(museum2);
		});
		// execute
		GuiActionRunner.execute(() -> museumSwingView.museumRemoved(new Museum(MUSEUM1_TEST, 10)));
		// verify
		String[] listContents = window.list().contents();
		assertThat(listContents).containsExactly("museum2_test - Total Rooms: 10 - Occupied Rooms: 0");
		window.label("errorMessageLabel").requireText(" ");
	}

	@Test
	public void testFindAllButtonShouldDelegateToControllerGetMuseums() {
		window.button(JButtonMatcher.withText("Find all")).click();
		verify(museumSwingController).getAllMuseums();
	}

	@Test
	public void testAddButtonShouldDelegateToControllerSaveMuseum() {
		window.textBox("museum").enterText(MUSEUM1_TEST);
		window.textBox("rooms").enterText(NUM_CONST);
		window.button(JButtonMatcher.withText("Add")).click();
		verify(museumSwingController).saveMuseum(new Museum(MUSEUM1_TEST, 10));
	}

	@Test
	public void testRemovedSelectedButtonShouldDelegateToControllerDeleteMuseum() {
		// setup
		Museum museum1 = new Museum(MUSEUM1_TEST, 10);
		Museum museum2 = new Museum(MUSEUM2_TEST, 10);
		GuiActionRunner.execute(() -> {
			DefaultListModel<Museum> museumsListModel = museumSwingView.getMuseumListModel();
			museumsListModel.addElement(museum1);
			museumsListModel.addElement(museum2);
		});
		window.list("museumList").selectItem(1);
		window.button(JButtonMatcher.withText("Delete Selected")).click();
		verify(museumSwingController).deleteMuseum(museum2);
	}

	@Test
	public void testChangeViewToExhibitionsDashboard() {
		window.button(JButtonMatcher.withText("Exhibitions Dashboard")).click();
		verify(museumSwingController).openExhibitionsDashboard();
	}

}
