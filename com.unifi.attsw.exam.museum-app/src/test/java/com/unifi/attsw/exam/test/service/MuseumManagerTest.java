package com.unifi.attsw.exam.test.service;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.AdditionalAnswers.answer;
import static org.mockito.ArgumentMatchers.any;

import com.unifi.attsw.exam.exception.RepositoryException;
import com.unifi.attsw.exam.model.Museum;
import com.unifi.attsw.exam.repository.ExhibitionRepository;
import com.unifi.attsw.exam.repository.MuseumRepository;
import com.unifi.attsw.exam.service.MuseumManagerService;
import com.unifi.attsw.exam.service.impl.MuseumManagerServiceImpl;
import com.unifi.attsw.exam.transaction.manager.TransactionCode;
import com.unifi.attsw.exam.transaction.manager.TransactionManager;

public class MuseumManagerTest {

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

		when(transactionManager.doInTransaction(any()))
				.thenAnswer(answer((TransactionCode<?> code) -> code.apply(museumRepository, exhibitionRepository)));

		museumManager = new MuseumManagerServiceImpl(transactionManager);
	}

	@Test
	public void testSaveMuseum() throws RepositoryException {
		Museum museum = new Museum("test1", 10);
		museumManager.saveMuseum(museum);
		verify(museumRepository).addMuseum(museum);
	}

}
