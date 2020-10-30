package com.unifi.attsw.exam.repository.postgres;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import javax.persistence.EntityManager;

import com.unifi.attsw.exam.model.Museum;
import com.unifi.attsw.exam.repository.MuseumRepository;

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
	public Museum findMuseumById(UUID id) {
		Museum foundMuseum = entityManager.find(Museum.class, id);
		if (foundMuseum == null) {
			throw new NoSuchElementException("Cannot find entity with id: " + id);
		}
		return entityManager.find(Museum.class, id);

	}

	@Override
	public Museum findMuseumByName(String museumToFind) {
		List<Museum> museums = entityManager.createQuery("FROM Museum", Museum.class).getResultList();
		return museums.stream().filter(m -> m.getName().equals(museumToFind)).findAny().orElse(null);

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
