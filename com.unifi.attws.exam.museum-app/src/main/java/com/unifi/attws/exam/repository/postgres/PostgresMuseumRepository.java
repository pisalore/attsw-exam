package com.unifi.attws.exam.repository.postgres;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;

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
		try {
			return entityManager.find(Museum.class, id);
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public Museum addMuseum(Museum museum) {
		try {
			entityManager.persist(museum);
			return museum;
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException();
		}

	}

	@Override
	public Museum updateMuseum(Museum updatedMuseum) {
		try {
			return entityManager.merge(updatedMuseum);
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void deleteMuseum(Museum museumToRemove) {
		try {
			entityManager.remove(museumToRemove);
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException();
		}

	}

}
