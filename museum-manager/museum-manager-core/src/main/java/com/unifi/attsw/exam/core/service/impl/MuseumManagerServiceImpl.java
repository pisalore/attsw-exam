package com.unifi.attsw.exam.core.service.impl;

import java.util.List;
import java.util.NoSuchElementException;

import com.unifi.attsw.exam.core.service.MuseumManagerService;
import com.unifi.attsw.exam.repository.model.Exhibition;
import com.unifi.attsw.exam.repository.model.Museum;
import com.unifi.attsw.exam.repository.repository.ExhibitionRepository;
import com.unifi.attsw.exam.repository.repository.MuseumRepository;
import com.unifi.attsw.exam.repository.repository.exception.RepositoryException;
import com.unifi.attsw.exam.repository.transaction.manager.TransactionManager;

public class MuseumManagerServiceImpl implements MuseumManagerService {

	private TransactionManager transactionManager;

	public MuseumManagerServiceImpl(TransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	@Override
	public List<Museum> getAllMuseums() throws RepositoryException {
		return transactionManager.doInTransactionMuseum(MuseumRepository::findAllMuseums);
	}

	@Override
	public List<Exhibition> getAllExhibitions() throws RepositoryException {
		return transactionManager.doInTransactionExhibition(ExhibitionRepository::findAllExhibitions);
	}

	@Override
	public List<Exhibition> getAllMuseumExhibitions(Museum museum) {
		try {
			return transactionManager.doInTransaction((museumRepository, exhibitionRepository) -> {
				return exhibitionRepository.findExhibitionsByMuseumId(museum.getId());
			});
		} catch (NullPointerException | RepositoryException ex) {
			throw new RuntimeException("Impossible to get Exhibitions for the selected Museum: " + museum, ex);
		}

	}

	@Override
	public Museum getMuseumByName(String museumName) {
		try {
			Museum museum = transactionManager.doInTransactionMuseum(museumRepository -> {
				return museumRepository.findMuseumByName(museumName);
			});
			if (museum == null) {
				throw new NoSuchElementException("Impossible to find the specified Museum: " + museumName);
			}
			return museum;
		} catch (RepositoryException | NoSuchElementException ex) {
			throw new RuntimeException("Impossible to find Museum", ex);
		}
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
			throw new RuntimeException("Impossibile to add Museum.", ex);
		}

	}

	@Override
	public void deleteMuseum(Museum museum) {
		try {
			Museum museumToRemove = transactionManager.doInTransactionMuseum(museumRepository -> {
				return museumRepository.findMuseumByName(museum.getName());
			});

			if (museumToRemove == null) {
				throw new NoSuchElementException("The selected museum does not exist!");
			}

			transactionManager.doInTransaction((museumRepository, exhibitionRepository) -> {
				List<Exhibition> museumToRemoveExhibitions = exhibitionRepository
						.findExhibitionsByMuseumId(museumToRemove.getId());
				museumToRemoveExhibitions.forEach(e -> exhibitionRepository.deleteExhibition(e));
				museumRepository.deleteMuseum(museumToRemove);
				return null;
			});
		} catch (NullPointerException | NoSuchElementException | RepositoryException ex) {
			throw new RuntimeException("Impossible to delete Museum.", ex);
		}

	}

	@Override
	public Exhibition getExhibitionByName(String exhibitionName) {
		try {
			Exhibition exhibition = transactionManager.doInTransactionExhibition(exhibitionRepository -> {
				return exhibitionRepository.findExhibitionByName(exhibitionName);
			});
			if (exhibition == null) {
				throw new NoSuchElementException("Impossible to find the specified Exhibition: " + exhibitionName);
			}
			return exhibition;
		} catch (RepositoryException | NoSuchElementException ex) {
			throw new RuntimeException("Impossible to find Exhibition", ex);
		}
	}

	@Override
	public Exhibition addNewExhibition(String museumName, Exhibition exhibition) {
		try {
			Museum museum = transactionManager.doInTransactionMuseum(museumRepository -> {
				return museumRepository.findMuseumByName(museumName);
			});

			if (museum == null) {
				throw new NoSuchElementException("The selected museum does not exist!");
			}

			return transactionManager.doInTransaction((museumRepository, exhibitionRepository) -> {

				exhibition.setMuseumId(museum.getId());
				int occupiedRooms = museum.getOccupiedRooms();
				int rooms = museum.getTotalRooms();
				if (occupiedRooms >= rooms) {
					throw new IllegalArgumentException("Impossibile to add new Exhibition: all rooms are occupied!");
				}
				museum.setOccupiedRooms(occupiedRooms + 1);
				return exhibitionRepository.addNewExhibition(exhibition);
			});
		} catch (NoSuchElementException | RepositoryException | IllegalArgumentException ex) {
			throw new RuntimeException("Impossible to add Exhibition.", ex);
		}
	}

	@Override
	public void deleteExhibition(Exhibition exhibition) {
		try {
			Museum museum = transactionManager.doInTransactionMuseum(museumRepository -> {
				return museumRepository.findMuseumById(exhibition.getMuseumId());
			});
			
			Exhibition exhibitionToRemove = transactionManager.doInTransactionExhibition(exhibitionRepository -> {
				return exhibitionRepository.findExhibitionByName(exhibition.getName());
			});

			if (exhibitionToRemove == null) {
				throw new NoSuchElementException("The selected exhibition does not exist!");
			}
			transactionManager.doInTransaction((museumRepository, exhibitionRepository) -> {
				exhibitionRepository.deleteExhibition(exhibitionToRemove);
				int occupiedRooms = museum.getOccupiedRooms();
				museum.setOccupiedRooms(occupiedRooms - 1);
				return null;
			});

		} catch (NoSuchElementException | NullPointerException | RepositoryException ex) {
			throw new RuntimeException("Impossible to delete Exhibition.", ex);
		}
	}

	@Override
	public void bookExhibitionSeat(Exhibition exhibition) {
		try {
			Exhibition existingExhibition = transactionManager.doInTransactionExhibition(exhibitionRepository -> {
				return exhibitionRepository.findExhibitionById(exhibition.getId());
			});

			if (existingExhibition.getBookedSeats() == existingExhibition.getTotalSeats()) {
				throw new UnsupportedOperationException(
						"Impossibile to book a seat for " + exhibition.getName() + ": all seats are booked");
			}
			exhibition.setBookedSeats(exhibition.getBookedSeats() + 1);
			transactionManager.doInTransactionExhibition(exhibitionRepository -> {
				return exhibitionRepository.updateExhibition(exhibition);
			});

		} catch (UnsupportedOperationException | RepositoryException ex) {
			throw new RuntimeException("Impossible to book a seat.", ex);
		}

	}

}
