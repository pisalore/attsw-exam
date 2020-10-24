package com.unifi.attsw.exam.service.impl;

import com.unifi.attsw.exam.exception.RepositoryException;
import com.unifi.attsw.exam.model.Museum;
import com.unifi.attsw.exam.service.MuseumManagerService;
import com.unifi.attsw.exam.transaction.manager.TransactionManager;

public class MuseumManagerServiceImpl implements MuseumManagerService{
	
	private TransactionManager transactionManager;

	public MuseumManagerServiceImpl(TransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	@Override
	public Museum saveMuseum(Museum museum) throws RepositoryException {
		return transactionManager.doInTransaction((museumRepository, exhibitionRepository) -> {
			return museumRepository.addMuseum(museum);
		});
	}

}
