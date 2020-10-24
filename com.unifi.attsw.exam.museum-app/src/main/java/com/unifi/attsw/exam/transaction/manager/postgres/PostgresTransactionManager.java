package com.unifi.attsw.exam.transaction.manager.postgres;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import com.unifi.attsw.exam.exception.RepositoryException;
import com.unifi.attsw.exam.repository.ExhibitionRepository;
import com.unifi.attsw.exam.repository.MuseumRepository;
import com.unifi.attsw.exam.repository.postgres.PostgresExhibitionRepository;
import com.unifi.attsw.exam.repository.postgres.PostgresMuseumRepository;
import com.unifi.attsw.exam.transaction.manager.TransactionManager;
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
			query.apply(museumRepository, exhibitionRepository);
			commit();
		} catch (PersistenceException | IllegalArgumentException ex) {
			rollback();
			throw new RepositoryException("Something went wrong committing to database, rollback");
		}

		return null;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public <T> T doInTransaction(MuseumTransactionCode<T> query) throws RepositoryException {
		startTransaction();

		try {
			query.apply(museumRepository);
			commit();
		} catch (PersistenceException | IllegalArgumentException ex) {
			rollback();
			throw new RepositoryException("Something went wrong committing to database, rollback");
		}

		return null;
	}

	public void startTransaction() {
		this.entityManager.getTransaction().begin();
	}

	public void commit() {
		this.entityManager.flush();
		this.entityManager.getTransaction().commit();
	}

	public void rollback() {
		this.entityManager.getTransaction().rollback();
	}

}
