package com.unifi.attsw.exam.service.impl;

import java.util.List;
import java.util.NoSuchElementException;

import com.unifi.attsw.exam.exception.RepositoryException;
import com.unifi.attsw.exam.model.Exhibition;
import com.unifi.attsw.exam.model.Museum;
import com.unifi.attsw.exam.service.MuseumManagerService;
import com.unifi.attsw.exam.transaction.manager.TransactionManager;

public class MuseumManagerServiceImpl implements MuseumManagerService {

	private TransactionManager transactionManager;

	public MuseumManagerServiceImpl(TransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	@Override
	public List<Museum> getAllMuseums() throws RepositoryException {
		return transactionManager.doInTransactionMuseum(museumRepository -> {
			return museumRepository.findAllMuseums();
		});
	}

	@Override
	public Museum saveMuseum(Museum museum) {
		try {
			return transactionManager.doInTransactionMuseum(museumRepository -> {
				Museum existingMuseum = museumRepository.findMuseumByName(museum.getName());
				if (existingMuseum == null) {
					return museumRepository.addMuseum(museum);
				}
				return museumRepository.updateMuseum(museum);

			});
		} catch (NullPointerException | RepositoryException ex) {
			throw new RuntimeException("Impossibile to add Museum.");
		}

	}

	@Override
	public void deleteMuseum(Museum museum) {
		try {
			transactionManager.doInTransaction((museumRepository, exhibitionRepository) -> {
				Museum museumToRemove = museumRepository.findMuseumByName(museum.getName());
				if (museumToRemove == null) {
					throw new NoSuchElementException("The selected museum does not exist!");
				}
				List<Exhibition> museumToRemoveExhibitions = exhibitionRepository
						.findExhibitionsByMuseumId(museumToRemove.getId());
				museumToRemoveExhibitions.forEach(e -> exhibitionRepository.deleteExhibition(e));
				museumRepository.deleteMuseum(museumToRemove);
				return null;
			});
		} catch (NullPointerException | NoSuchElementException | RepositoryException ex) {
			throw new RuntimeException("Impossible to delete Museum.");
		}

	}

	@Override
	public Exhibition addNewExhibition(String museumName, Exhibition exhibition) {
		try {
			return transactionManager.doInTransaction((museumRepository, exhibitionRepository) -> {
				Museum museum = museumRepository.findMuseumByName(museumName);
				if (museum == null) {
					throw new NoSuchElementException("The selected museum does not exist!");
				}
				exhibition.setMuseumId(museum.getId());
				museum.setOccupiedRooms(museum.getOccupiedRooms() + 1);
				return exhibitionRepository.addNewExhibition(exhibition);
			});
		} catch (NoSuchElementException | RepositoryException ex) {
			throw new RuntimeException("Impossible to add Exhibition.");
		}
	}

}
