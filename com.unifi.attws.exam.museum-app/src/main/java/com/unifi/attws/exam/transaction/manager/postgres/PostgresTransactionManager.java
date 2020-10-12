package com.unifi.attws.exam.transaction.manager.postgres;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import com.unifi.attws.exam.repository.MuseumRepository;
import com.unifi.attws.exam.repository.postgres.PostgresMuseumRepository;
import com.unifi.attws.exam.transaction.manager.TransactionCode;
import com.unifi.attws.exam.transaction.manager.TransactionManager;

public class PostgresTransactionManager implements TransactionManager {

	EntityManager entityManager;
	MuseumRepository repoInstance;

	public PostgresTransactionManager() {
		EntityManagerFactory sessionFactory = Persistence.createEntityManagerFactory("postgres.not-empty.database");
		entityManager = sessionFactory.createEntityManager();
		repoInstance = new PostgresMuseumRepository(entityManager);
	}

	@Override
	public <T> T doInTransaction(TransactionCode<T> query) {
		this.entityManager.getTransaction().begin();

		try {
			query.apply(repoInstance);
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
