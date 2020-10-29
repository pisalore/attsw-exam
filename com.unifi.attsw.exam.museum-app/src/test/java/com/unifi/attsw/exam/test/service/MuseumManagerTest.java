package com.unifi.attsw.exam.test.service;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.AdditionalAnswers.answer;
import static org.mockito.ArgumentMatchers.any;
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

public class MuseumManagerTest {

	private static final String MUSEUM1_TEST = "museum1_test";
	private static final String MUSEUM2_TEST = "museum2_test";
	private static final int NUM_CONSTANT1 = 10;

	@Mock
	TransactionManager transactionManager;

	@Mock
	MuseumRepository museumRepository;

	@Mock
	ExhibitionRepository exhibitionRepository;

	private MuseumManagerService museumManager;

	@Before
	public void setUp() throws RepositoryException {
		MockitoAnnotations.initMocks(this);

		when(transactionManager.doInTransactionMuseum(any()))
				.thenAnswer(answer((MuseumTransactionCode<?> code) -> code.apply(museumRepository)));

		museumManager = new MuseumManagerServiceImpl(transactionManager);
	}

	@Test
	public void testGetAllMuseums() throws RepositoryException {
		Museum museum1 = createTestMuseum("Museum", 10);
		Museum museum2 = createTestMuseum(MUSEUM2_TEST, NUM_CONSTANT1);
		when(museumRepository.findAllMuseums()).thenReturn(asList(museum1, museum2));
		List<Museum> museums = museumManager.getAllMuseums();
		verify(museumRepository).findAllMuseums();
		assertThat(museums).containsAll(asList(museum1, museum2));
	}

	@Test
	public void testSaveMuseumWhenMuseumDoesNotExistAddNew() throws RepositoryException {
		Museum museum1 = createTestMuseum(MUSEUM1_TEST, NUM_CONSTANT1);
		Museum museum2 = createTestMuseum(MUSEUM2_TEST, NUM_CONSTANT1);
		when(museumRepository.findAllMuseums()).thenReturn(asList(museum1));
		museumManager.saveMuseum(museum2);
		verify(museumRepository).addMuseum(museum2);
	}

	@Test
	public void testSaveMuseumWhenMuseumExistsUpdate() throws RepositoryException {
		Museum museum1 = createTestMuseum("Museum", 10);
		when(museumRepository.findAllMuseums()).thenReturn(asList(museum1));
		museumManager.saveMuseum(museum1);
		verify(museumRepository).updateMuseum(museum1);
	}
	
	@Test
	public void testDeleteMuseum() throws RepositoryException {
		Museum museum1 = createTestMuseum(MUSEUM1_TEST, NUM_CONSTANT1);
		when(museumRepository.findAllMuseums()).thenReturn(asList(museum1));
		museumManager.deleteMuseum(museum1);
		verify(museumRepository).deleteMuseum(museum1);
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
