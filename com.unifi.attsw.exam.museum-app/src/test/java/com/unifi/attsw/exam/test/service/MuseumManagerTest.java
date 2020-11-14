package com.unifi.attsw.exam.test.service;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.*;

import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.VerificationCollector;
import static org.mockito.Mockito.*;
import static org.mockito.AdditionalAnswers.answer;
import static org.mockito.ArgumentMatchers.any;

import java.util.NoSuchElementException;
import java.util.UUID;

import static java.util.Arrays.asList;

import com.unifi.attsw.exam.exception.RepositoryException;
import com.unifi.attsw.exam.model.Exhibition;
import com.unifi.attsw.exam.model.Museum;
import com.unifi.attsw.exam.repository.ExhibitionRepository;
import com.unifi.attsw.exam.repository.MuseumRepository;
import com.unifi.attsw.exam.service.MuseumManagerService;
import com.unifi.attsw.exam.service.impl.MuseumManagerServiceImpl;
import com.unifi.attsw.exam.transaction.manager.TransactionManager;
import com.unifi.attsw.exam.transaction.manager.code.MuseumTransactionCode;
import com.unifi.attsw.exam.transaction.manager.code.ExhibitionTransactionCode;
import com.unifi.attsw.exam.transaction.manager.code.TransactionCode;

@RunWith(MockitoJUnitRunner.class)
public class MuseumManagerTest {

	private static final String MUSEUM1_TEST = "museum1_test";
	private static final UUID MUSEUM_ID_1 = UUID.fromString("b433da18-ba5a-4b86-92af-ba11be6314e7");

	private static final String MUSEUM2_TEST = "museum2_test";
	private static final UUID MUSEUM_ID_2 = UUID.fromString("b433da18-ba5a-4b86-92af-ba11be6314e2");

	private static final String EXHIBITION1_TEST = "exhibition1_test";
	private static final UUID EXHIBITION_ID_1 = UUID.fromString("49d13e51-2277-4911-929f-c9c067e2e8b4");

	private static final String EXHIBITION2_TEST = "exhibition2_test";
	private static final UUID EXHIBITION_ID_2 = UUID.fromString("b2cb1474-24ff-41eb-a8d7-963f32f6822d");

	private static final int NUM_CONSTANT1 = 10;

	@Mock
	private TransactionManager transactionManager;

	@Mock
	private MuseumRepository museumRepository;

	@Mock
	private ExhibitionRepository exhibitionRepository;

	@Spy
	private Museum museum = createTestMuseum(MUSEUM1_TEST, NUM_CONSTANT1, MUSEUM_ID_1);

	@Spy
	private Exhibition exhibition = createExhibition(EXHIBITION1_TEST, NUM_CONSTANT1, EXHIBITION_ID_1);

	@Rule
	public VerificationCollector collector = MockitoJUnit.collector();

	private MuseumManagerService museumManager;

	private InOrder inOrder;

	@Before
	public void setUp() throws RepositoryException {
		when(transactionManager.doInTransactionMuseum(any()))
				.thenAnswer(answer((MuseumTransactionCode<?> code) -> code.apply(museumRepository)));

		when(transactionManager.doInTransactionExhibition(any()))
				.thenAnswer(answer((ExhibitionTransactionCode<?> code) -> code.apply(exhibitionRepository)));

		when(transactionManager.doInTransaction(any()))
				.thenAnswer(answer((TransactionCode<?> code) -> code.apply(museumRepository, exhibitionRepository)));

		museumManager = new MuseumManagerServiceImpl(transactionManager);

		inOrder = inOrder(exhibitionRepository, museumRepository, museum, exhibition);

	}

	@Test
	public void testGetAllMuseumsWhenNoArePersisted() throws RepositoryException {
		when(museumRepository.findAllMuseums()).thenReturn(asList());
		museumManager.getAllMuseums();
		inOrder.verify(museumRepository).findAllMuseums();
		verifyNoMoreInteractions(museumRepository);
	}

	@Test
	public void testGetAllMuseumsWhenMuseumsArePersisted() throws RepositoryException {
		Museum museum1 = createTestMuseum(MUSEUM1_TEST, NUM_CONSTANT1, MUSEUM_ID_1);
		Museum museum2 = createTestMuseum(MUSEUM2_TEST, NUM_CONSTANT1, MUSEUM_ID_2);
		when(museumRepository.findAllMuseums()).thenReturn(asList(museum1, museum2));

		museumManager.getAllMuseums();
		verify(museumRepository).findAllMuseums();
		verifyNoMoreInteractions(museumRepository);
	}

	@Test
	public void testGetAllExhibitionWhenNoExhibitionIsPersisted() throws RepositoryException {
		when(exhibitionRepository.findAllExhibitions()).thenReturn(asList());
		museumManager.getAllExhibitions();
		inOrder.verify(exhibitionRepository).findAllExhibitions();
		verifyNoMoreInteractions(exhibitionRepository);
	}

	@Test
	public void testGetAllExhibitionWhenExhibitionsArePersisted() throws RepositoryException {
		Exhibition exhibition1 = createExhibition(EXHIBITION1_TEST, NUM_CONSTANT1, EXHIBITION_ID_1);
		Exhibition exhibition2 = createExhibition(EXHIBITION2_TEST, NUM_CONSTANT1, EXHIBITION_ID_2);
		when(exhibitionRepository.findAllExhibitions()).thenReturn(asList(exhibition1, exhibition2));

		museumManager.getAllExhibitions();
		inOrder.verify(exhibitionRepository).findAllExhibitions();
		verifyNoMoreInteractions(exhibitionRepository);
	}

	@Test
	public void testGetMuseumByNullNameShouldThrow() {
		assertThatThrownBy(() -> {
			museumManager.getMuseumByName(null);
			doThrow(NoSuchElementException.class).doNothing();
			inOrder.verify(museumRepository).findMuseumByName(MUSEUM1_TEST);
			inOrder.verifyNoMoreInteractions();
		}).isInstanceOf(RuntimeException.class).hasMessage("Impossible to find Museum");

	}
	
	@Test
	public void testGetMuseumThatDoesNotExistByNameShouldThrow() {
		assertThatThrownBy(() -> {
			when(museumRepository.findMuseumByName(MUSEUM1_TEST)).thenReturn(null);
			museumManager.getMuseumByName(MUSEUM1_TEST);
			doThrow(NoSuchElementException.class).doNothing();
			inOrder.verify(museumRepository).findMuseumByName(MUSEUM1_TEST);
			inOrder.verifyNoMoreInteractions();
			
		}).isInstanceOf(RuntimeException.class).hasMessage("Impossible to find Museum");

	}
	
	@Test
	public void testGetMuseumByName() {
		Museum museum = createTestMuseum(MUSEUM1_TEST, NUM_CONSTANT1, MUSEUM_ID_1);
		when(museumRepository.findMuseumByName(MUSEUM1_TEST)).thenReturn(museum);
		museumManager.getMuseumByName(MUSEUM1_TEST);
		inOrder.verify(museumRepository).findMuseumByName(MUSEUM1_TEST);
		inOrder.verifyNoMoreInteractions();
	}

	@Test
	public void testGetMuseumExhibitionsWhenMuseumIsNullShouldThrow() {
		assertThatThrownBy(() -> {
			museumManager.getAllMuseumExhibitions(null);
			doThrow(new NullPointerException()).when(museum).getId();
			inOrder.verify(exhibitionRepository).findExhibitionsByMuseumId(museum.getId());
			verifyNoMoreInteractions(exhibitionRepository);
		}).isInstanceOf(RuntimeException.class)
				.hasMessage("Impossible to get Exhibitions for the selected Museum: " + null);
	}

	@Test
	public void testGetMuseumExhibitionsWhenNoExhibitionIsPersisted() {
		when(exhibitionRepository.findExhibitionsByMuseumId(museum.getId())).thenReturn(asList());

		museumManager.getAllMuseumExhibitions(museum);
		inOrder.verify(exhibitionRepository).findExhibitionsByMuseumId(museum.getId());
		verifyNoMoreInteractions(exhibitionRepository);
	}

	@Test
	public void testGetMuseumExhibitions() throws RepositoryException {
		Exhibition exhibition1 = createExhibition(EXHIBITION1_TEST, NUM_CONSTANT1, EXHIBITION_ID_1);
		Exhibition exhibition2 = createExhibition(EXHIBITION2_TEST, NUM_CONSTANT1, EXHIBITION_ID_2);
		when(exhibitionRepository.findExhibitionsByMuseumId(museum.getId()))
				.thenReturn(asList(exhibition1, exhibition2));

		museumManager.getAllMuseumExhibitions(museum);
		inOrder.verify(exhibitionRepository).findExhibitionsByMuseumId(museum.getId());
		verifyNoMoreInteractions(exhibitionRepository);
	}

	@Test
	public void testSaveNullMuseumShouldThrow() {
		museum = null;
		assertThatThrownBy(() -> {
			museumManager.saveMuseum(null);
			doThrow(new NullPointerException()).when(museum).getName();
			inOrder.verify(museumRepository).addMuseum(museum);
			inOrder.verify(museum).getName();
			verifyNoMoreInteractions(museumRepository, museum);
		}).isInstanceOf(RuntimeException.class).hasMessage("Impossibile to add Museum.");
	}

	@Test
	public void testSaveNewMuseum() throws RepositoryException {
		when(museumRepository.findMuseumByName(MUSEUM1_TEST)).thenReturn(null);
		museumManager.saveMuseum(museum);
		inOrder.verify(museumRepository).findMuseumByName(MUSEUM1_TEST);
		inOrder.verify(museumRepository).addMuseum(museum);
		verifyNoMoreInteractions(museumRepository);
	}

	@Test
	public void testSaveMuseumWhenMuseumExistsUpdate() throws RepositoryException {
		when(museumRepository.findMuseumByName(MUSEUM1_TEST)).thenReturn(museum);
		museumManager.saveMuseum(museum);
		verify(museumRepository).findMuseumByName(MUSEUM1_TEST);
		verify(museumRepository).updateMuseum(museum);
		verifyNoMoreInteractions(museumRepository);
	}

	@Test
	public void testDeleteMuseumWithNoExhibitions() throws RepositoryException {
		when(exhibitionRepository.findExhibitionsByMuseumId(museum.getId())).thenReturn(asList());
		when(museumRepository.findMuseumByName(MUSEUM1_TEST)).thenReturn(museum);
		museumManager.deleteMuseum(museum);

		inOrder.verify(museumRepository).findMuseumByName(MUSEUM1_TEST);
		inOrder.verify(exhibitionRepository).findExhibitionsByMuseumId(museum.getId());
		verify(museumRepository).deleteMuseum(museum);
		verifyNoMoreInteractions(museumRepository);
	}

	@Test
	public void testDeleteMuseumWithExhibitions() throws RepositoryException {
		Exhibition exhibition1 = createExhibition(EXHIBITION1_TEST, NUM_CONSTANT1, EXHIBITION_ID_1);
		Exhibition exhibition2 = createExhibition(EXHIBITION2_TEST, NUM_CONSTANT1, EXHIBITION_ID_2);

		when(museumRepository.findMuseumByName(MUSEUM1_TEST)).thenReturn(museum);
		when(exhibitionRepository.findExhibitionsByMuseumId(museum.getId()))
				.thenReturn(asList(exhibition1, exhibition2));
		museumManager.deleteMuseum(museum);

		inOrder.verify(museumRepository).findMuseumByName(MUSEUM1_TEST);
		inOrder.verify(exhibitionRepository).findExhibitionsByMuseumId(museum.getId());
		inOrder.verify(exhibitionRepository).deleteExhibition(exhibition1);
		inOrder.verify(exhibitionRepository).deleteExhibition(exhibition2);
		inOrder.verify(museumRepository).deleteMuseum(museum);
		verifyNoMoreInteractions(museumRepository);
	}

	@Test
	public void testDeleteNullMuseumShouldThrow() throws RepositoryException {
		assertThatThrownBy(() -> {
			museumManager.deleteMuseum(null);
			doThrow(new NullPointerException()).when(museum).getName();
		}).isInstanceOf(RuntimeException.class).hasMessage("Impossible to delete Museum.");

	}

	@Test
	public void testDeleteMuseumWhichDoesNotExistShouldThrow() throws RepositoryException {
		assertThatThrownBy(() -> {
			when(museumRepository.findMuseumByName(MUSEUM1_TEST)).thenReturn(null);
			museumManager.deleteMuseum(museum);
			doThrow(new NoSuchElementException("The selected museum does not exist!")).doNothing();

		}).isInstanceOf(RuntimeException.class).hasMessage("Impossible to delete Museum.");

	}

	@Test
	public void testAddNewExhibitionWhenRoomsAreNotAvailableShouldThrow() throws RepositoryException {
		museum.setOccupiedRooms(NUM_CONSTANT1);

		assertThatThrownBy(() -> {
			when(museumRepository.findMuseumByName(MUSEUM1_TEST)).thenReturn(museum);
			museumManager.addNewExhibition(museum.getName(), exhibition);
			doThrow(new IllegalArgumentException("Impossibile to add new Exhibition: all rooms are occupied!"))
					.when(museum.getOccupiedRooms() >= museum.getTotalRooms());

			inOrder.verify(museumRepository).findMuseumByName(MUSEUM1_TEST);
			inOrder.verify(exhibition).setMuseumId(museum.getId());
			inOrder.verify(museum).getOccupiedRooms();
			inOrder.verify(museum).getTotalRooms();
			verifyNoMoreInteractions(museumRepository, exhibitionRepository);
		}).isInstanceOf(RuntimeException.class).hasMessage("Impossible to add Exhibition.");
	}

	@Test
	public void testAddNewExhibitionWhenRoomsAreAvailable() throws RepositoryException {
		int ouccupiedRooms = museum.getOccupiedRooms();
		when(museumRepository.findMuseumByName(MUSEUM1_TEST)).thenReturn(museum);
		museumManager.addNewExhibition(museum.getName(), exhibition);

		inOrder.verify(museumRepository).findMuseumByName(MUSEUM1_TEST);
		inOrder.verify(exhibition).setMuseumId(museum.getId());
		inOrder.verify(museum).getOccupiedRooms();
		inOrder.verify(museum).getTotalRooms();
		inOrder.verify(museum).setOccupiedRooms(ouccupiedRooms + 1);
		inOrder.verify(exhibitionRepository).addNewExhibition(exhibition);
		verifyNoMoreInteractions(museumRepository, exhibitionRepository);
	}

	@Test
	public void testAddNewExhibitionToAMuseumWhichDoesNotExistShouldThrow() throws RepositoryException {
		when(museumRepository.findMuseumByName(MUSEUM1_TEST)).thenReturn(null);
		assertThatThrownBy(() -> {
			museumManager.addNewExhibition(MUSEUM1_TEST, exhibition);
			when(museumRepository.findMuseumByName(MUSEUM1_TEST)).thenReturn(null);
			doThrow(new NoSuchElementException("The selected museum does not exist!")).doNothing();
		}).isInstanceOf(RuntimeException.class).hasMessage("Impossible to add Exhibition.");
	}

	@Test
	public void testDeleteNullExhibitionShouldThrow() throws RepositoryException {
		assertThatThrownBy(() -> {
			museumManager.deleteExhibition(null);
			doThrow(new NullPointerException()).when(exhibition).getName();
		}).isInstanceOf(RuntimeException.class).hasMessage("Impossible to delete Exhibition.");

	}

	@Test
	public void testDeleteExhibitionhichDoesNotExistShouldThrow() throws RepositoryException {
		assertThatThrownBy(() -> {
			museumManager.deleteExhibition(exhibition);
			when(exhibitionRepository.findExhibitionByName(EXHIBITION1_TEST)).thenReturn(null);
			doThrow(new NoSuchElementException("The selected exhibition does not exist!")).doNothing();

		}).isInstanceOf(RuntimeException.class).hasMessage("Impossible to delete Exhibition.");

	}

	@Test
	public void testDeleteExhibition() throws RepositoryException {
		when(exhibitionRepository.findExhibitionByName(EXHIBITION1_TEST)).thenReturn(exhibition);
		museumManager.deleteExhibition(exhibition);
		inOrder.verify(exhibitionRepository).findExhibitionByName(EXHIBITION1_TEST);
		inOrder.verify(exhibitionRepository).deleteExhibition(exhibition);
		verifyNoMoreInteractions(exhibitionRepository);
	}

	@Test
	public void testBookExhibitionWhenAllSeatsAreBookedShouldThrow() {
		assertThatThrownBy(() -> {
			exhibition.setBookedSeats(exhibition.getTotalSeats());
			when(exhibitionRepository.findExhibitionById(EXHIBITION_ID_1)).thenReturn(exhibition);
			museumManager.bookExhibitionSeat(exhibition);
			doThrow(new UnsupportedOperationException(
					"Impossibile to book a seat for " + exhibition.getName() + ": all seats are booked"))
							.when(exhibition.getBookedSeats() >= exhibition.getBookedSeats());
			inOrder.verify(exhibition).getBookedSeats();
			inOrder.verify(exhibition).getTotalSeats();
		}).isInstanceOf(RuntimeException.class).hasMessage("Impossible to book a seat.");

	}

	@Test
	public void testBookExhibitionWhenSeatsAreAvailable() {
		when(exhibitionRepository.findExhibitionById(EXHIBITION_ID_1)).thenReturn(exhibition);
		museumManager.bookExhibitionSeat(exhibition);
		inOrder.verify(exhibition).setBookedSeats(exhibition.getBookedSeats() + 1);
		inOrder.verify(exhibitionRepository).updateExhibition(exhibition);

	}

	/**
	 * 
	 * Utility methods
	 * 
	 */

	public Museum createTestMuseum(String museumName, int numOfRooms, UUID id) {
		Museum museum = new Museum(museumName, numOfRooms);
		museum.setId(id);
		return museum;
	}

	public Exhibition createExhibition(String exhibitionName, int numOfSeats, UUID id) {
		Exhibition exhibition = new Exhibition(exhibitionName, numOfSeats);
		exhibition.setId(EXHIBITION_ID_1);
		return exhibition;

	}

}
