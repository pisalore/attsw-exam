package com.unifi.attsw.exam.test.controller;

import org.junit.Before;

//import static org.assertj.core.api.Assertions.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

import com.unifi.attsw.exam.controller.MuseumController;
import com.unifi.attsw.exam.model.Museum;
import com.unifi.attsw.exam.service.MuseumManagerService;

@RunWith(MockitoJUnitRunner.class)
public class MuseumControllerTest {

	private static final String MUSEUM1_TEST = "museum1_test";
	private static final int NUM_CONSTANT1 = 10;

	@Mock
	private MuseumManagerService museumService;

	@InjectMocks
	private MuseumController museumController;

	private InOrder inOrder;
	
	@Before
	public void setUp() {
		inOrder = inOrder(museumService);
	}
	
	@Test
	public void testSaveNullMuseumShouldThrow() {
		doThrow(new NullPointerException()).when(museumService).saveMuseum(null);
		museumController.saveMuseum(null);
		
		inOrder.verify(museumService).saveMuseum(null);
		verifyNoMoreInteractions(museumService);
	}

	@Test
	public void testSaveMuseumWhichDoesNotExist() {
		Museum museum = new Museum(MUSEUM1_TEST, NUM_CONSTANT1);
		museumController.saveMuseum(museum);
		
		inOrder.verify(museumService).saveMuseum(museum);
		verifyNoMoreInteractions(museumService);
	}
	
	

}
