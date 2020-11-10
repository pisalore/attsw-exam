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
import com.unifi.attsw.exam.model.Exhibition;
import com.unifi.attsw.exam.model.Museum;
import com.unifi.attsw.exam.service.MuseumManagerService;
import com.unifi.attsw.exam.view.ExhibitionView;
import com.unifi.attsw.exam.view.MuseumView;

@RunWith(MockitoJUnitRunner.class)
public class MuseumControllerTest {

	private static final String MUSEUM1_TEST = "museum1_test";
	private static final String EXHIBITION1_TEST = "exhibition1_test";
	
	private static final int NUM_CONSTANT1 = 10;

	@Mock
	private MuseumManagerService museumService;
	
	@Mock
	private MuseumView museumView;
	
	@Mock
	private ExhibitionView exhibitionView;

	@InjectMocks
	private MuseumController museumController;

	private InOrder inOrder;
	
	@Before
	public void setUp() {
		inOrder = inOrder(museumService, museumView, exhibitionView);
	}
	
	@Test
	public void testSaveNullMuseumShouldThrow() {
		doThrow(new RuntimeException()).when(museumService).saveMuseum(null);
		museumController.saveMuseum(null);
		
		inOrder.verify(museumService).saveMuseum(null);
		inOrder.verify(museumView).showError("Impossibile to add Museum.", null);
		verifyNoMoreInteractions(museumService);
	}

	@Test
	public void testSaveMuseumWhichDoesNotExist() {
		Museum museum = new Museum(MUSEUM1_TEST, NUM_CONSTANT1);
		museumController.saveMuseum(museum);
		
		inOrder.verify(museumService).saveMuseum(museum);
		verifyNoMoreInteractions(museumService);
	}
	
	@Test
	public void testAddNewNullExhibitionShouldThrow() {
		doThrow(new RuntimeException()).when(museumService).addNewExhibition(MUSEUM1_TEST, null);
		museumController.saveExhibition(MUSEUM1_TEST, null);
		
		inOrder.verify(museumService).addNewExhibition(MUSEUM1_TEST, null);
		inOrder.verify(exhibitionView).showError("Impossible to add Exhibition.", null);
		verifyNoMoreInteractions(museumService, exhibitionView);
	}
	
	@Test
	public void testAddNewExhibitionWhenMuseumDoesNotExistShouldThrow() {
		Exhibition exhibition = new Exhibition(EXHIBITION1_TEST, NUM_CONSTANT1);
		doThrow(new RuntimeException()).when(museumService).addNewExhibition(MUSEUM1_TEST, exhibition);
		museumController.saveExhibition(MUSEUM1_TEST, exhibition);
		
		inOrder.verify(museumService).addNewExhibition(MUSEUM1_TEST, exhibition);
		inOrder.verify(exhibitionView).showError("Impossible to add Exhibition.", exhibition);
		verifyNoMoreInteractions(museumService, exhibitionView);
		
	}
	
	@Test
	public void testAddNewExhibitionWhenMuseumNameIsNullShouldThrow() {
		Exhibition exhibition = new Exhibition(EXHIBITION1_TEST, NUM_CONSTANT1);
		doThrow(new RuntimeException()).when(museumService).addNewExhibition(null, exhibition);
		museumController.saveExhibition(null, exhibition);
		
		inOrder.verify(museumService).addNewExhibition(null, exhibition);
		inOrder.verify(exhibitionView).showError("Impossible to add Exhibition.", exhibition);
		verifyNoMoreInteractions(museumService, exhibitionView);
		
	}
	
	@Test
	public void testAddNewExhibition() {
		Exhibition exhibition = new Exhibition(EXHIBITION1_TEST, NUM_CONSTANT1);
		museumController.saveExhibition(MUSEUM1_TEST, exhibition);
		
		inOrder.verify(museumService).addNewExhibition(MUSEUM1_TEST, exhibition);
		verifyNoMoreInteractions(museumService);
	}
	

}
