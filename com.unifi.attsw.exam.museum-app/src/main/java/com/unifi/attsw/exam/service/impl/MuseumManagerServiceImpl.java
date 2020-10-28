package com.unifi.attsw.exam.service.impl;

import java.util.List;

import com.unifi.attsw.exam.exception.RepositoryException;
import com.unifi.attsw.exam.model.Museum;
import com.unifi.attsw.exam.service.MuseumManagerService;
import com.unifi.attsw.exam.transaction.manager.TransactionManager;

public class MuseumManagerServiceImpl implements MuseumManagerService {
	
	private TransactionManager transactionManager;

	public MuseumManagerServiceImpl(TransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	@Override
	public Museum saveMuseum(Museum museum) throws RepositoryException {
		return transactionManager.doInTransactionMuseum(museumRepository -> {
			List<Museum> museums = museumRepository.findAllMuseums();
			Museum existingMuseum = museums.stream().filter(m -> m.getName() == museum.getName()).findAny()
					.orElse(null);
			if (existingMuseum == null) {
				return museumRepository.addMuseum(museum);
			}
			return museumRepository.updateMuseum(museum);

		});

	}

}
