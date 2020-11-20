package com.unifi.attsw.exam.test.view.swing;

import static org.junit.Assert.*;

import javax.swing.JTextField;

import org.assertj.swing.annotation.GUITest;
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
	}

}
