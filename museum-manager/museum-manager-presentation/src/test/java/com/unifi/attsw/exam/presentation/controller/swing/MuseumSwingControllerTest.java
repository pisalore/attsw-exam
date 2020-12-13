package com.unifi.attsw.exam.presentation.controller.swing;

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

import com.unifi.attsw.exam.presentation.controller.swing.MuseumSwingController;
import com.unifi.attsw.exam.core.service.MuseumManagerService;
import com.unifi.attsw.exam.presentation.view.swing.ExhibitionSwingView;
import com.unifi.attsw.exam.presentation.view.swing.MuseumSwingView;
import com.unifi.attsw.exam.repository.model.Exhibition;
import com.unifi.attsw.exam.repository.model.Museum;
import com.unifi.attsw.exam.repository.repository.exception.RepositoryException;

@RunWith(MockitoJUnitRunner.class)
public class MuseumSwingControllerTest {

	private static final String MUSEUM1_TEST = "museum1_test";
	private static final String EXHIBITION1_TEST = "exhibition1_test";

	private static final int NUM_CONSTANT1 = 10;

	@Mock
	private MuseumManagerService museumService;

	@Mock
	private MuseumSwingView museumView;

	@Mock
	private ExhibitionSwingView exhibitionView;

	@InjectMocks
	private MuseumSwingController museumSwingController;

	private InOrder inOrder;

	@Before
	public void setUp() {
		inOrder = inOrder(museumService, museumView, exhibitionView);
	}

	@Test
	public void testShowAllMuseums() throws RepositoryException {
		List<Museum> museums = asList(new Museum(MUSEUM1_TEST, NUM_CONSTANT1));
		when(museumService.getAllMuseums()).thenReturn(museums);
		museumSwingController.getAllMuseums();
		inOrder.verify(museumView).showAllMuseums(museums);
	}
	
	@Test
	public void testShowAllMuseumsError() throws RepositoryException {
		doThrow(new RepositoryException()).when(museumService).getAllMuseums();
		museumSwingController.getAllMuseums();
		inOrder.verify(museumView).showError("Impossibile to get museums.", null);
	}

	@Test
	public void testShowAllExhibitions() throws RepositoryException {
		List<Exhibition> exhibitions = asList(new Exhibition(EXHIBITION1_TEST, NUM_CONSTANT1));
		when(museumService.getAllExhibitions()).thenReturn(exhibitions);
		museumSwingController.getAllExhibitions();
		inOrder.verify(exhibitionView).showAllExhibitions(exhibitions);
	}
	
	@Test
	public void testShowAllExhibitionsError() throws RepositoryException {
		doThrow(new RepositoryException()).when(museumService).getAllExhibitions();
		museumSwingController.getAllExhibitions();
		inOrder.verify(museumView).showError("Impossibile to get all exhibitions.", null);
	}

	@Test
	public void testShowAllMuseumExhibitions() throws RepositoryException {
		List<Exhibition> exhibitions = asList(new Exhibition(EXHIBITION1_TEST, NUM_CONSTANT1));
		Museum museum = new Museum(MUSEUM1_TEST, NUM_CONSTANT1);
		when(museumService.getAllMuseumExhibitions(museum)).thenReturn(exhibitions);
		when(museumService.getMuseumByName(MUSEUM1_TEST)).thenReturn(museum);

		museumSwingController.getAllMuseumExhibitions(MUSEUM1_TEST);
		inOrder.verify(museumService).getMuseumByName(MUSEUM1_TEST);
		inOrder.verify(museumService).getAllMuseumExhibitions(museum);
		inOrder.verify(exhibitionView).showMuseumExhibitions(exhibitions);
	}

	@Test
	public void testShowAllNullMuseumExhibitionsShouldThrow() throws RepositoryException {
		doThrow(new RuntimeException()).when(museumService).getMuseumByName(null);

		museumSwingController.getAllMuseumExhibitions(null);

		inOrder.verify(museumService).getMuseumByName(null);
		inOrder.verify(exhibitionView).showError("Impossibile to get all exhibitions.", null);
	}

	@Test
	public void testShowAllExhibitionsFromNotExistingMuseumShouldThrow() throws RepositoryException {
		doThrow(new RuntimeException()).when(museumService).getMuseumByName(MUSEUM1_TEST);
		museumSwingController.getAllMuseumExhibitions(MUSEUM1_TEST);

		inOrder.verify(museumService).getMuseumByName(MUSEUM1_TEST);
		inOrder.verify(exhibitionView).showError("Impossibile to get all exhibitions.", null);
	}

	@Test
	public void testSaveNullMuseumShouldThrow() {
		doThrow(new RuntimeException()).when(museumService).saveMuseum(null);
		museumSwingController.saveMuseum(null);

		inOrder.verify(museumService).saveMuseum(null);
		inOrder.verify(museumView).showError("Impossibile to add Museum: ", null);
		verifyNoMoreInteractions(museumService);
	}

	@Test
	public void testSaveMuseumWhichDoesNotExist() {
		Museum museum = new Museum(MUSEUM1_TEST, NUM_CONSTANT1);
		museumSwingController.saveMuseum(museum);

		inOrder.verify(museumService).saveMuseum(museum);
		inOrder.verify(museumView).museumAdded(museum);
		verifyNoMoreInteractions(museumService);
	}

	@Test
	public void testAddNewNullExhibitionShouldThrow() {
		doThrow(new RuntimeException()).when(museumService).addNewExhibition(MUSEUM1_TEST, null);
		museumSwingController.saveExhibition(MUSEUM1_TEST, null);

		inOrder.verify(museumService).addNewExhibition(MUSEUM1_TEST, null);
		inOrder.verify(exhibitionView).showError("Impossible to add Exhibition: ", null);
		verifyNoMoreInteractions(museumService, exhibitionView);
	}

	@Test
	public void testAddNewExhibitionWhenMuseumDoesNotExistShouldThrow() {
		Exhibition exhibition = new Exhibition(EXHIBITION1_TEST, NUM_CONSTANT1);
		doThrow(new RuntimeException()).when(museumService).addNewExhibition(MUSEUM1_TEST, exhibition);
		museumSwingController.saveExhibition(MUSEUM1_TEST, exhibition);

		inOrder.verify(museumService).addNewExhibition(MUSEUM1_TEST, exhibition);
		inOrder.verify(exhibitionView).showError("Impossible to add Exhibition: ", exhibition);
		verifyNoMoreInteractions(museumService, exhibitionView);

	}

	@Test
	public void testAddNewExhibitionWhenMuseumNameIsNullShouldThrow() {
		Exhibition exhibition = new Exhibition(EXHIBITION1_TEST, NUM_CONSTANT1);
		doThrow(new RuntimeException()).when(museumService).addNewExhibition(null, exhibition);
		museumSwingController.saveExhibition(null, exhibition);

		inOrder.verify(museumService).addNewExhibition(null, exhibition);
		inOrder.verify(exhibitionView).showError("Impossible to add Exhibition: ", exhibition);
		verifyNoMoreInteractions(museumService, exhibitionView);

	}

	@Test
	public void testAddNewExhibition() {
		Exhibition exhibition = new Exhibition(EXHIBITION1_TEST, NUM_CONSTANT1);
		museumSwingController.saveExhibition(MUSEUM1_TEST, exhibition);

		inOrder.verify(museumService).addNewExhibition(MUSEUM1_TEST, exhibition);
		inOrder.verify(exhibitionView).exhibitionAdded(exhibition);
		verifyNoMoreInteractions(museumService);
	}

	@Test
	public void testDeleteNullMuseumShouldThrow() {
		doThrow(new RuntimeException()).when(museumService).deleteMuseum(null);
		museumSwingController.deleteMuseum(null);

		inOrder.verify(museumService).deleteMuseum(null);
		inOrder.verify(museumView).showError("Impossible to delete Museum: ", null);
		verifyNoMoreInteractions(museumService, museumView);
	}

	@Test
	public void testDeleteMuseumWhichDoesNotExistShouldThrow() {
		Museum museum = new Museum(MUSEUM1_TEST, NUM_CONSTANT1);
		doThrow(new RuntimeException()).when(museumService).deleteMuseum(museum);
		museumSwingController.deleteMuseum(museum);

		inOrder.verify(museumService).deleteMuseum(museum);
		inOrder.verify(museumView).showError("Impossible to delete Museum: ", museum);
		verifyNoMoreInteractions(museumService, museumView);
	}

	@Test
	public void testDeleteMuseum() {
		Museum museum = new Museum(MUSEUM1_TEST, NUM_CONSTANT1);
		museumSwingController.deleteMuseum(museum);

		inOrder.verify(museumService).deleteMuseum(museum);
		verifyNoMoreInteractions(museumService);
	}

	@Test
	public void testDeleteNullExhibitionShouldThrow() {
		doThrow(new RuntimeException()).when(museumService).deleteExhibition(null);
		museumSwingController.deleteExhibition(null);

		inOrder.verify(museumService).deleteExhibition(null);
		inOrder.verify(exhibitionView).showError("Impossible to delete Exhibition: ", null);
		verifyNoMoreInteractions(museumService, exhibitionView);
	}

	@Test
	public void testDeleteExhibitionWhichDoesNotExistShouldThrow() {
		Exhibition exhibition = new Exhibition(EXHIBITION1_TEST, NUM_CONSTANT1);
		doThrow(new RuntimeException()).when(museumService).deleteExhibition(exhibition);
		museumSwingController.deleteExhibition(exhibition);

		inOrder.verify(museumService).deleteExhibition(exhibition);
		inOrder.verify(exhibitionView).showError("Impossible to delete Exhibition: ", exhibition);
		verifyNoMoreInteractions(museumService, exhibitionView);
	}

	@Test
	public void testDeleteExhibition() {
		Exhibition exhibition = new Exhibition(EXHIBITION1_TEST, NUM_CONSTANT1);
		museumSwingController.deleteExhibition(exhibition);

		inOrder.verify(museumService).deleteExhibition(exhibition);
		verifyNoMoreInteractions(museumService);
	}

	@Test
	public void testBookNullExhibitionSeatShouldThrow() {
		doThrow(new RuntimeException()).when(museumService).bookExhibitionSeat(null);
		museumSwingController.bookExhibitionSeat(null);

		inOrder.verify(museumService).bookExhibitionSeat(null);
		inOrder.verify(exhibitionView).showError("Impossible to book a seat for Exhibition: ", null);
		verifyNoMoreInteractions(museumService, exhibitionView);
	}

	@Test
	public void testBookExhibitionWhenAllSeatsAreBookedShouldThrow() {
		Exhibition exhibition = new Exhibition(EXHIBITION1_TEST, NUM_CONSTANT1);
		exhibition.setBookedSeats(NUM_CONSTANT1);

		doThrow(new RuntimeException()).when(museumService).bookExhibitionSeat(exhibition);
		museumSwingController.bookExhibitionSeat(exhibition);

		inOrder.verify(museumService).bookExhibitionSeat(exhibition);
		inOrder.verify(exhibitionView).showError("Impossible to book a seat for Exhibition: ", exhibition);
		verifyNoMoreInteractions(museumService, exhibitionView);
	}

	@Test
	public void testBookExhibition() {
		Exhibition exhibition = new Exhibition(EXHIBITION1_TEST, NUM_CONSTANT1);
		museumSwingController.bookExhibitionSeat(exhibition);

		inOrder.verify(museumService).bookExhibitionSeat(exhibition);
		inOrder.verify(exhibitionView).exhibitionBooked();
		verifyNoMoreInteractions(museumService, exhibitionView);
	}

	@Test
	public void testOpenMuseumsDashboard() {
		museumSwingController.openMuseumDashboard();
		inOrder.verify(museumView).setVisible(true);
		inOrder.verify(exhibitionView).setVisible(false);
	}

	@Test
	public void testOpenExhibitionsDashboard() {
		museumSwingController.openExhibitionsDashboard();
		inOrder.verify(exhibitionView).setVisible(true);
		inOrder.verify(museumView).setVisible(false);
	}

}
