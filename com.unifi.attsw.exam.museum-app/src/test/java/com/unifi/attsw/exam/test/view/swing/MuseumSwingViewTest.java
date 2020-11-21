package com.unifi.attsw.exam.test.view.swing;

import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JLabelMatcher;
import org.assertj.swing.core.matcher.JTextComponentMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.junit.runner.GUITestRunner;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.unifi.attsw.exam.view.swing.MuseumSwingView;

@RunWith(GUITestRunner.class)
public class MuseumSwingViewTest extends AssertJSwingJUnitTestCase{
	
	private FrameFixture window;
	
	private MuseumSwingView museumSwingView;

	@Override
	protected void onSetUp() {
		GuiActionRunner.execute(() -> {
			museumSwingView = new MuseumSwingView();
			return museumSwingView;
		});
		window = new FrameFixture(robot(), museumSwingView);
		window.show();
		
	}
	
	@Test
	public void testMuseumDashboardControlsInitialState() {
		window.label(JLabelMatcher.withText("Museum"));
		window.textBox(JTextComponentMatcher.withText("museum")).requireEnabled();
		window.label(JLabelMatcher.withText("Rooms"));
		window.textBox(JTextComponentMatcher.withText("rooms")).requireEnabled();
		window.button(JButtonMatcher.withText("Add")).requireDisabled();
		window.button(JButtonMatcher.withText("Find all")).requireEnabled();
		window.list("museumList");
		window.button(JButtonMatcher.withText("Delete Selected")).requireDisabled();
	}

}
