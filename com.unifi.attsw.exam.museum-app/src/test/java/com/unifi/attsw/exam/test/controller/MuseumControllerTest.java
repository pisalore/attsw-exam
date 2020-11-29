package com.unifi.attsw.exam.test.controller;

import org.junit.Before;

//import static org.assertj.core.api.Assertions.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;

import java.util.List;

import com.unifi.attsw.exam.controller.MuseumController;
import com.unifi.attsw.exam.exception.RepositoryException;
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
	public void testShowAllMuseums() throws RepositoryException {
		List<Museum> museums = asList(new Museum(MUSEUM1_TEST, NUM_CONSTANT1));
		when(museumService.getAllMuseums()).thenReturn(museums);
		museumController.getAllMuseums();
		inOrder.verify(museumView).showAllMuseums(museums);
	}

	@Test
	public void testShowAllExhibitions() throws RepositoryException {
		List<Exhibition> exhibitions = asList(new Exhibition(EXHIBITION1_TEST, NUM_CONSTANT1));
		when(museumService.getAllExhibitions()).thenReturn(exhibitions);
		museumController.getAllExhibitions();
		inOrder.verify(exhibitionView).showAllExhibitions(exhibitions);
	}

	@Test
	public void testShowAllMuseumExhibitions() throws RepositoryException {
		List<Exhibition> exhibitions = asList(new Exhibition(EXHIBITION1_TEST, NUM_CONSTANT1));
		Museum museum = new Museum(MUSEUM1_TEST, NUM_CONSTANT1);
		when(museumService.getAllMuseumExhibitions(museum)).thenReturn(exhibitions);
		when(museumService.getMuseumByName(MUSEUM1_TEST)).thenReturn(museum);
		
		museumController.getAllMuseumExhibitions(MUSEUM1_TEST);
		inOrder.verify(museumService).getMuseumByName(MUSEUM1_TEST);
		inOrder.verify(museumService).getAllMuseumExhibitions(museum);
		inOrder.verify(exhibitionView).showMuseumExhibitions(exhibitions);
	}
	
	@Test
	public void testShowAllNullMuseumExhibitionsShouldThrow() throws RepositoryException {
		doThrow(new RuntimeException()).when(museumService).getMuseumByName(null);
		
		museumController.getAllMuseumExhibitions(null);

		inOrder.verify(museumService).getMuseumByName(null);
		inOrder.verify(exhibitionView).showError("Impossibile to get all exhibitions.", null);
	}
	
	@Test
	public void testShowAllExhibitionsFromNotExistingMuseumShouldThrow() throws RepositoryException {
		doThrow(new RuntimeException()).when(museumService).getMuseumByName(MUSEUM1_TEST);
		museumController.getAllMuseumExhibitions(MUSEUM1_TEST);

		inOrder.verify(museumService).getMuseumByName(MUSEUM1_TEST);
		inOrder.verify(exhibitionView).showError("Impossibile to get all exhibitions.", null);
	}

	@Test
	public void testSaveNullMuseumShouldThrow() {
		doThrow(new RuntimeException()).when(museumService).saveMuseum(null);
		museumController.saveMuseum(null);

		inOrder.verify(museumService).saveMuseum(null);
		inOrder.verify(museumView).showError("Impossibile to add Museum: ", null);
		verifyNoMoreInteractions(museumService);
	}

	@Test
	public void testSaveMuseumWhichDoesNotExist() {
		Museum museum = new Museum(MUSEUM1_TEST, NUM_CONSTANT1);
		museumController.saveMuseum(museum);

		inOrder.verify(museumService).saveMuseum(museum);
		inOrder.verify(museumView).museumAdded(museum);
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

	@Test
	public void testDeleteNullMuseumShouldThrow() {
		doThrow(new RuntimeException()).when(museumService).deleteMuseum(null);
		museumController.deleteMuseum(null);

		inOrder.verify(museumService).deleteMuseum(null);
		inOrder.verify(museumView).showError("Impossible to delete Museum: ", null);
		verifyNoMoreInteractions(museumService, museumView);
	}

	@Test
	public void testDeleteMuseumWhichDoesNotExistShouldThrow() {
		Museum museum = new Museum(MUSEUM1_TEST, NUM_CONSTANT1);
		doThrow(new RuntimeException()).when(museumService).deleteMuseum(museum);
		museumController.deleteMuseum(museum);

		inOrder.verify(museumService).deleteMuseum(museum);
		inOrder.verify(museumView).showError("Impossible to delete Museum: ", museum);
		verifyNoMoreInteractions(museumService, museumView);
	}

	@Test
	public void testDeleteMuseum() {
		Museum museum = new Museum(MUSEUM1_TEST, NUM_CONSTANT1);
		museumController.deleteMuseum(museum);

		inOrder.verify(museumService).deleteMuseum(museum);
		verifyNoMoreInteractions(museumService);
	}

	@Test
	public void testDeleteNullExhibitionShouldThrow() {
		doThrow(new RuntimeException()).when(museumService).deleteExhibition(null);
		museumController.deleteExhibition(null);

		inOrder.verify(museumService).deleteExhibition(null);
		inOrder.verify(exhibitionView).showError("Impossible to delete Exhbition.", null);
		verifyNoMoreInteractions(museumService, exhibitionView);
	}

	@Test
	public void testDeleteExhibitionWhichDoesNotExistShouldThrow() {
		Exhibition exhibition = new Exhibition(EXHIBITION1_TEST, NUM_CONSTANT1);
		doThrow(new RuntimeException()).when(museumService).deleteExhibition(exhibition);
		museumController.deleteExhibition(exhibition);

		inOrder.verify(museumService).deleteExhibition(exhibition);
		inOrder.verify(exhibitionView).showError("Impossible to delete Exhbition.", exhibition);
		verifyNoMoreInteractions(museumService, exhibitionView);
	}

	@Test
	public void testDeleteExhibition() {
		Exhibition exhibition = new Exhibition(EXHIBITION1_TEST, NUM_CONSTANT1);
		museumController.deleteExhibition(exhibition);

		inOrder.verify(museumService).deleteExhibition(exhibition);
		verifyNoMoreInteractions(museumService);
	}

	@Test
	public void testBookNullExhibitionSeatShouldThrow() {
		doThrow(new RuntimeException()).when(museumService).bookExhibitionSeat(null);
		museumController.bookExhibitionSeat(null);

		inOrder.verify(museumService).bookExhibitionSeat(null);
		inOrder.verify(exhibitionView).showError("Impossible to book a seat for Exhibition.", null);
		verifyNoMoreInteractions(museumService, exhibitionView);
	}

	@Test
	public void testBookExhibitionWhenAllSeatsAreBookedShouldThrow() {
		Exhibition exhibition = new Exhibition(EXHIBITION1_TEST, NUM_CONSTANT1);
		exhibition.setBookedSeats(NUM_CONSTANT1);

		doThrow(new RuntimeException()).when(museumService).bookExhibitionSeat(exhibition);
		museumController.bookExhibitionSeat(exhibition);

		inOrder.verify(museumService).bookExhibitionSeat(exhibition);
		inOrder.verify(exhibitionView).showError("Impossible to book a seat for Exhibition.", exhibition);
		verifyNoMoreInteractions(museumService, exhibitionView);
	}

	@Test
	public void testBookExhibition() {
		Exhibition exhibition = new Exhibition(EXHIBITION1_TEST, NUM_CONSTANT1);
		museumController.bookExhibitionSeat(exhibition);

		inOrder.verify(museumService).bookExhibitionSeat(exhibition);
		verifyNoMoreInteractions(museumService, exhibitionView);
	}

}
