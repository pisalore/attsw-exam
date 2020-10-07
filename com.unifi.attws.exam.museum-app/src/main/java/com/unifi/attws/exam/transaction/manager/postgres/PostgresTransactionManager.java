package com.unifi.attws.exam.transaction.manager.postgres;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.unifi.attws.exam.repository.MuseumRepository;
import com.unifi.attws.exam.repository.postgres.RepoException;
import com.unifi.attws.exam.transaction.manager.TransactionCode;
import com.unifi.attws.exam.transaction.manager.TransactionManager;

public class PostgresTransactionManager implements TransactionManager {

	EntityManager entityManager;

	public PostgresTransactionManager() {
		EntityManagerFactory sessionFactory = Persistence.createEntityManagerFactory("postgres");
		entityManager = sessionFactory.createEntityManager();
	}

	@Override
	public <T> T doInTransaction(TransactionCode<T> query, MuseumRepository repoInstance) {
		this.entityManager.getTransaction().begin();

		T retval = query.apply(repoInstance);

		this.entityManager.flush();
		RepoException ex = (RepoException) retval;
		
		if (ex.getMessage().toLowerCase() == "ok") {
			this.entityManager.getTransaction().commit();
			
		} else {
			this.entityManager.getTransaction().rollback();
		}
		
		return retval;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}
