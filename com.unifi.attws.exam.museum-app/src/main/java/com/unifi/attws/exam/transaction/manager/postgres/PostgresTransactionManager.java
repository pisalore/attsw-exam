package com.unifi.attws.exam.transaction.manager.postgres;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import com.unifi.attws.exam.repository.ExhibitionRepository;
import com.unifi.attws.exam.repository.MuseumRepository;
import com.unifi.attws.exam.repository.postgres.PostgresExhibitionRepository;
import com.unifi.attws.exam.repository.postgres.PostgresMuseumRepository;
import com.unifi.attws.exam.transaction.manager.TransactionCode;
import com.unifi.attws.exam.transaction.manager.TransactionManager;

public class PostgresTransactionManager implements TransactionManager {

	EntityManager entityManager;
	MuseumRepository museumRepository;
	ExhibitionRepository exhibitionRepository;

	public PostgresTransactionManager() {
		EntityManagerFactory sessionFactory = Persistence.createEntityManagerFactory("postgres.not-empty.database");
		entityManager = sessionFactory.createEntityManager();
		museumRepository = new PostgresMuseumRepository(entityManager);
		exhibitionRepository = new PostgresExhibitionRepository(entityManager);
	}

	@Override
	public <T> T doInTransaction(TransactionCode<T> query) {
		this.entityManager.getTransaction().begin();

		try {
			query.apply(museumRepository, exhibitionRepository);
		} catch (PersistenceException ex) {
			this.entityManager.flush();
			this.entityManager.getTransaction().rollback();
			return null;
		}
		this.entityManager.getTransaction().commit();
		return null;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}
