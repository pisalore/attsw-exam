package com.unifi.attsw.exam.transaction.manager.postgres;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import com.unifi.attsw.exam.exception.RepositoryException;
import com.unifi.attsw.exam.repository.ExhibitionRepository;
import com.unifi.attsw.exam.repository.MuseumRepository;
import com.unifi.attsw.exam.repository.postgres.PostgresExhibitionRepository;
import com.unifi.attsw.exam.repository.postgres.PostgresMuseumRepository;
import com.unifi.attsw.exam.transaction.manager.TransactionCode;
import com.unifi.attsw.exam.transaction.manager.TransactionManager;

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
		this.entityManager.getTransaction().begin();

		try {
			query.apply(museumRepository, exhibitionRepository);
			this.entityManager.flush();
			this.entityManager.getTransaction().commit();
		} catch (PersistenceException | IllegalArgumentException ex) {
			this.entityManager.getTransaction().rollback();
			throw new RepositoryException("Something went wrong committing to database, rollback");
		}
		
		return null;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

}
