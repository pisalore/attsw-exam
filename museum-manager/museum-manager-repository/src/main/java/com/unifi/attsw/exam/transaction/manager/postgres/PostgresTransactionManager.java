package com.unifi.attsw.exam.transaction.manager.postgres;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import com.unifi.attsw.exam.repository.exception.RepositoryException;
import com.unifi.attsw.exam.repository.ExhibitionRepository;
import com.unifi.attsw.exam.repository.MuseumRepository;
import com.unifi.attsw.exam.repository.postgres.PostgresExhibitionRepository;
import com.unifi.attsw.exam.repository.postgres.PostgresMuseumRepository;
import com.unifi.attsw.exam.transaction.manager.TransactionManager;
import com.unifi.attsw.exam.transaction.manager.code.ExhibitionTransactionCode;
import com.unifi.attsw.exam.transaction.manager.code.MuseumTransactionCode;
import com.unifi.attsw.exam.transaction.manager.code.TransactionCode;

public class PostgresTransactionManager implements TransactionManager {

	EntityManager entityManager;
	MuseumRepository museumRepository;
	ExhibitionRepository exhibitionRepository;

	public PostgresTransactionManager(EntityManager entityManager) {
		this.entityManager = entityManager;
		museumRepository = new PostgresMuseumRepository(this.entityManager);
		exhibitionRepository = new PostgresExhibitionRepository(this.entityManager);
	}

	@Override
	public <T> T doInTransaction(TransactionCode<T> query) throws RepositoryException {
		startTransaction();

		try {
			T response = query.apply(museumRepository, exhibitionRepository);
			commit();
			return response;
		} catch (PersistenceException | IllegalArgumentException ex) {
			rollback();
			throw new RepositoryException("Something went wrong committing to database, rollback");
		}
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public <T> T doInTransactionMuseum(MuseumTransactionCode<T> query) throws RepositoryException {
		startTransaction();

		try {
			T response = query.apply(museumRepository);
			commit();
			return response;
		} catch (PersistenceException | IllegalArgumentException ex) {
			rollback();
			throw new RepositoryException("Something went wrong committing to database, rollback");
		}
	}

	@Override
	public <T> T doInTransactionExhibition(ExhibitionTransactionCode<T> query) throws RepositoryException {
		startTransaction();

		try {
			T response = query.apply(exhibitionRepository);
			commit();
			return response;
		} catch (PersistenceException | IllegalArgumentException ex) {
			rollback();
			throw new RepositoryException("Something went wrong committing to database, rollback");
		}
	}

	public void startTransaction() {
		this.entityManager.getTransaction().begin();
	}

	public void commit() {
		this.entityManager.getTransaction().commit();
	}

	public void rollback() {
		this.entityManager.getTransaction().rollback();
	}

}
