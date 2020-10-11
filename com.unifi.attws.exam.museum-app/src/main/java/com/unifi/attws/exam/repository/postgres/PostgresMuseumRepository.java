package com.unifi.attws.exam.repository.postgres;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import com.unifi.attws.exam.model.Museum;
import com.unifi.attws.exam.repository.MuseumRepository;

public class PostgresMuseumRepository implements MuseumRepository {

	private EntityManager entityManager;

	public PostgresMuseumRepository(EntityManager entityManager) {
		this.entityManager = entityManager;

	}

	@Override
	public List<Museum> findAllMuseums() {
		List<Museum> museums = entityManager.createQuery("FROM Museum", Museum.class).getResultList();
		return museums;
	}

	@Override
	public Museum retrieveMuseumById(UUID id) {
		return entityManager.find(Museum.class, id);
	}

	@Override
	public Museum addMuseum(Museum museum) {
		try {
			entityManager.persist(museum);
		} catch (PersistenceException ex) {
			throw new PersistenceException();
		}
		return museum;

	}

	@Override
	public void updateMuseum(Museum updatedMuseum) {
		try {
			entityManager.merge(updatedMuseum);
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void deleteMuseum(Museum museumToRemove) {
		try {
			entityManager.remove(museumToRemove);

		} catch (PersistenceException ex) {
			throw new PersistenceException();
		}

	}

}
