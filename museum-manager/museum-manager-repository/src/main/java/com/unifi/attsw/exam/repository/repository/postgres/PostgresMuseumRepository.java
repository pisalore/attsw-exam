package com.unifi.attsw.exam.repository.repository.postgres;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import javax.persistence.EntityManager;

import com.unifi.attsw.exam.repository.model.Museum;
import com.unifi.attsw.exam.repository.repository.MuseumRepository;

public class PostgresMuseumRepository implements MuseumRepository {

	private EntityManager entityManager;

	public PostgresMuseumRepository(EntityManager entityManager) {
		this.entityManager = entityManager;

	}

	@Override
	public List<Museum> findAllMuseums() {
		return entityManager.createQuery("FROM Museum", Museum.class).getResultList();
	}

	@Override
	public Museum findMuseumById(UUID id) {
		Museum foundMuseum = entityManager.find(Museum.class, id);
		if (foundMuseum == null) {
			throw new NoSuchElementException("Cannot find entity with id: " + id);
		}
		return foundMuseum;

	}

	@Override
	public Museum findMuseumByName(String museumToFind) {
		List<Museum> museums = entityManager.createQuery("FROM Museum", Museum.class).getResultList();
		return museums.stream().filter(m -> m.getName().equals(museumToFind)).findAny().orElse(null);

	}

	@Override
	public Museum addMuseum(Museum museum) {
		entityManager.persist(museum);
			return museum;
	}

	@Override
	public Museum updateMuseum(Museum updatedMuseum) {
		return entityManager.merge(updatedMuseum);
	}

	@Override
	public void deleteMuseum(Museum museumToRemove) {
		entityManager.remove(museumToRemove);
	}

}
