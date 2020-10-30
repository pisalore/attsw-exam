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

import java.util.List;
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
import com.unifi.attsw.exam.transaction.manager.code.TransactionCode;

@RunWith(MockitoJUnitRunner.class)
public class MuseumManagerTest {

	private static final String MUSEUM1_TEST = "museum1_test";
	private static final String MUSEUM2_TEST = "museum2_test";

	private static final String EXHIBITION1_TEST = "exhibition1_test";
	private static final String EXHIBITION2_TEST = "exhibition2_test";

	private static final int NUM_CONSTANT1 = 10;

	@Mock
	private TransactionManager transactionManager;

	@Mock
	private MuseumRepository museumRepository;

	@Mock
	private ExhibitionRepository exhibitionRepository;

	@Spy
	private Museum museum = createTestMuseum(MUSEUM1_TEST, NUM_CONSTANT1);

	@Rule
	public VerificationCollector collector = MockitoJUnit.collector();

	private MuseumManagerService museumManager;

	@Before
	public void setUp() throws RepositoryException {
		when(transactionManager.doInTransactionMuseum(any()))
				.thenAnswer(answer((MuseumTransactionCode<?> code) -> code.apply(museumRepository)));

		when(transactionManager.doInTransaction(any()))
				.thenAnswer(answer((TransactionCode<?> code) -> code.apply(museumRepository, exhibitionRepository)));

		museumManager = new MuseumManagerServiceImpl(transactionManager);

	}

	@Test
	public void testGetAllMuseumsWhenNoArePersisted() throws RepositoryException {
		when(museumRepository.findAllMuseums()).thenReturn(asList());
		List<Museum> museums = museumManager.getAllMuseums();
		verify(museumRepository).findAllMuseums();
		assertThat(museums).isEmpty();
	}

	@Test
	public void testGetAllMuseumsWhenMuseumsArePersisted() throws RepositoryException {
		Museum museum1 = createTestMuseum("Museum", 10);
		Museum museum2 = createTestMuseum(MUSEUM2_TEST, NUM_CONSTANT1);
		when(museumRepository.findAllMuseums()).thenReturn(asList(museum1, museum2));
		List<Museum> museums = museumManager.getAllMuseums();
		verify(museumRepository).findAllMuseums();
		assertThat(museums).containsAll(asList(museum1, museum2));
		verifyNoMoreInteractions(museumRepository);
	}

	@Test
	public void testSaveNullMuseumShouldThrow() {
		assertThatThrownBy(() -> museumManager.saveMuseum(null)).isInstanceOf(RuntimeException.class)
				.hasMessage("Impossibile to add Museum.");
	}

	@Test
	public void testSaveNewMuseum() throws RepositoryException {
		Museum museum1 = createTestMuseum(MUSEUM1_TEST, NUM_CONSTANT1);
		when(museumRepository.findMuseumByName(MUSEUM1_TEST)).thenReturn(null);
		museumManager.saveMuseum(museum1);
		verify(museumRepository).findMuseumByName(MUSEUM1_TEST);
		verify(museumRepository).addMuseum(museum1);
		verifyNoMoreInteractions(museumRepository);
	}

	@Test
	public void testSaveMuseumWhenMuseumExistsUpdate() throws RepositoryException {
		Museum museum1 = createTestMuseum(MUSEUM1_TEST, 10);
		when(museumRepository.findMuseumByName(MUSEUM1_TEST)).thenReturn(museum1);
		museumManager.saveMuseum(museum1);
		verify(museumRepository).findMuseumByName(MUSEUM1_TEST);
		verify(museumRepository).updateMuseum(museum1);
		verifyNoMoreInteractions(museumRepository);
	}

	@Test
	public void testDeleteMuseumWithNoExhibitions() throws RepositoryException {
		Museum museum1 = createTestMuseum(MUSEUM1_TEST, NUM_CONSTANT1);
		when(exhibitionRepository.findExhibitionsByMuseumId(museum1.getId())).thenReturn(asList());
		when(museumRepository.findMuseumByName(MUSEUM1_TEST)).thenReturn(museum1);
		museumManager.deleteMuseum(museum1);
		InOrder inOrder = inOrder(exhibitionRepository, museumRepository);
		inOrder.verify(museumRepository).findMuseumByName(MUSEUM1_TEST);
		inOrder.verify(exhibitionRepository).findExhibitionsByMuseumId(museum1.getId());
		verify(museumRepository).deleteMuseum(museum1);
		verifyNoMoreInteractions(museumRepository);
	}

	@Test
	public void testDeleteMuseumWithExhibitions() throws RepositoryException {
		Museum museum1 = createTestMuseum(MUSEUM1_TEST, NUM_CONSTANT1);

		Exhibition exhibition1 = createExhibition(EXHIBITION1_TEST, NUM_CONSTANT1);
		Exhibition exhibition2 = createExhibition(EXHIBITION2_TEST, NUM_CONSTANT1);

		when(museumRepository.findMuseumByName(MUSEUM1_TEST)).thenReturn(museum1);
		when(exhibitionRepository.findExhibitionsByMuseumId(museum1.getId()))
				.thenReturn(asList(exhibition1, exhibition2));
		museumManager.deleteMuseum(museum1);

		InOrder inOrder = inOrder(exhibitionRepository, museumRepository);
		inOrder.verify(museumRepository).findMuseumByName(MUSEUM1_TEST);
		inOrder.verify(exhibitionRepository).findExhibitionsByMuseumId(museum1.getId());
		inOrder.verify(exhibitionRepository).deleteExhibition(exhibition1);
		inOrder.verify(exhibitionRepository).deleteExhibition(exhibition2);
		inOrder.verify(museumRepository).deleteMuseum(museum1);
		verifyNoMoreInteractions(museumRepository);
	}

	@Test
	public void testDeleteNullMuseumShouldThrow() throws RepositoryException {
		assertThatThrownBy(() -> museumManager.deleteMuseum(null)).isInstanceOf(RuntimeException.class)
				.hasMessage("Impossible to delete Museum.");

	}

	@Test
	public void testDeleteMuseumWhichDoesNotExistShouldThrow() throws RepositoryException {
		Museum museum1 = createTestMuseum(MUSEUM1_TEST, NUM_CONSTANT1);

		assertThatThrownBy(() -> museumManager.deleteMuseum(museum1)).isInstanceOf(RuntimeException.class)
				.hasMessage("Impossible to delete Museum.");

	}

	@Test
	public void testAddNewExhibition() throws RepositoryException {
		Exhibition exhibition = createExhibition(EXHIBITION1_TEST, NUM_CONSTANT1);

		when(museumRepository.findMuseumByName(MUSEUM1_TEST)).thenReturn(museum);
		museumManager.addNewExhibition(museum.getName(), exhibition);
		verify(museumRepository).findMuseumByName(MUSEUM1_TEST);
		verify(exhibitionRepository).addNewExhibition(exhibition);
		verify(museum).setOccupiedRooms(museum.getOccupiedRooms() + 1);
		verifyNoMoreInteractions(museumRepository, exhibitionRepository);
	}

	@Test
	public void testAddNewExhibitionToAMuseumWhichDoesNotExistShouldThrow() throws RepositoryException {
		Exhibition exhibition = createExhibition(EXHIBITION1_TEST, NUM_CONSTANT1);

		when(museumRepository.findMuseumByName(MUSEUM1_TEST)).thenReturn(null);
		assertThatThrownBy(() -> museumManager.addNewExhibition(MUSEUM1_TEST, exhibition))
				.isInstanceOf(RuntimeException.class).hasMessage("Impossible to add Exhibition.");
	}

	/**
	 * 
	 * Utility methods
	 * 
	 */

	public Museum createTestMuseum(String museumName, int numOfRooms) {
		return new Museum(museumName, numOfRooms);
	}

	public Exhibition createExhibition(String exhibitionName, int numOfSeats) {
		return new Exhibition(exhibitionName, numOfSeats);

	}

}
