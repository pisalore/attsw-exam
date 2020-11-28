package com.unifi.attsw.exam.test.view.swing;

import static org.junit.Assert.*;

import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.unifi.attsw.exam.controller.MuseumController;
import com.unifi.attsw.exam.view.swing.ExhibitionSwingView;

@RunWith(GUITestRunner.class)
public class ExhibitionSwingViewTest extends AssertJSwingJUnitTestCase {

	private FrameFixture window;

	private ExhibitionSwingView exhibitionView;

	@Mock
	private MuseumController museumController;

	@Override
	protected void onSetUp() {
		MockitoAnnotations.initMocks(this);
		GuiActionRunner.execute(() -> {
			exhibitionView = new ExhibitionSwingView();
			exhibitionView.setMuseumController(museumController);
			return exhibitionView;
		});
		window = new FrameFixture(robot(), exhibitionView);
		window.show();

	}
	
	@Test
	public void testExhibitionDashboardControlsInitialState() {
		window.label(JLabelMatcher.withText("Exhibition"));
		window.label(JLabelMatcher.withText("Total Seats"));
		window.label(JLabelMatcher.withText("Museum"));
		window.label(JLabelMatcher.withText("error1"));
		window.label(JLabelMatcher.withText("error2"));
		window.label(JLabelMatcher.withText("Museum Name"));
		window.label(JLabelMatcher.withText("Exhibitions list for Museum"));
		window.textBox("exhibitionTextField").requireEnabled();
		window.textBox("totalSeatstextField").requireEnabled();
		window.textBox("museumNameTextField").requireEnabled();
		window.textBox("findMuseumtextField").requireEnabled();
		window.button(JButtonMatcher.withText("Add Exhibition")).requireDisabled();
		window.button(JButtonMatcher.withText("Find all")).requireEnabled();
		window.button(JButtonMatcher.withText("Find Museum Exh.")).requireDisabled();
		window.button(JButtonMatcher.withText("Book")).requireDisabled();
		window.button(JButtonMatcher.withText("Delete")).requireDisabled();
		window.list("listAllExh");
		window.list("listMuseumExh");
	}

}
