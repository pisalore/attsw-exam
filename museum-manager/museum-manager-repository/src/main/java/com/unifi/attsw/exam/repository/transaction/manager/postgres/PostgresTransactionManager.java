package com.unifi.attsw.exam.repository.transaction.manager.postgres;

import javax.persistence.EntityManager;

import com.unifi.attsw.exam.repository.repository.ExhibitionRepository;
import com.unifi.attsw.exam.repository.repository.MuseumRepository;
import com.unifi.attsw.exam.repository.repository.exception.RepositoryException;
import com.unifi.attsw.exam.repository.repository.postgres.PostgresExhibitionRepository;
import com.unifi.attsw.exam.repository.repository.postgres.PostgresMuseumRepository;
import com.unifi.attsw.exam.repository.transaction.manager.TransactionManager;
import com.unifi.attsw.exam.repository.transaction.manager.code.ExhibitionTransactionCode;
import com.unifi.attsw.exam.repository.transaction.manager.code.MuseumTransactionCode;
import com.unifi.attsw.exam.repository.transaction.manager.code.TransactionCode;

public class PostgresTransactionManager implements TransactionManager {

	EntityManager entityManager;
	MuseumRepository museumRepository;
	ExhibitionRepository exhibitionRepository;

	private static final String ERROR_MESSAGE = "Something went wrong committing to database, rollback";

	public PostgresTransactionManager(EntityManager entityManager) {
		this.entityManager = entityManager;
		museumRepository = new PostgresMuseumRepository(this.entityManager);
		exhibitionRepository = new PostgresExhibitionRepository(this.entityManager);
	}

	@Override
	public <T> T doInTransaction(TransactionCode<T> query) throws RepositoryException {
		this.entityManager.getTransaction().begin();

		try {
			T response = query.apply(museumRepository, exhibitionRepository);
			this.entityManager.getTransaction().commit();
			return response;
		} catch (Exception ex) {
			this.entityManager.getTransaction().rollback();
			throw new RepositoryException(ERROR_MESSAGE, ex);
		}
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public <T> T doInTransactionMuseum(MuseumTransactionCode<T> query) throws RepositoryException {
		this.entityManager.getTransaction().begin();
		try {
			T response = query.apply(museumRepository);
			this.entityManager.getTransaction().commit();
			return response;
		} catch (Exception ex) {
			this.entityManager.getTransaction().rollback();
			throw new RepositoryException(ERROR_MESSAGE, ex);
		}
	}

	@Override
	public <T> T doInTransactionExhibition(ExhibitionTransactionCode<T> query) throws RepositoryException {
		this.entityManager.getTransaction().begin();

		try {
			T response = query.apply(exhibitionRepository);
			this.entityManager.getTransaction().commit();
			return response;
		} catch (Exception ex) {
			this.entityManager.getTransaction().rollback();
			throw new RepositoryException(ERROR_MESSAGE, ex);
		}
	}

}