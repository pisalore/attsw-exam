package com.unifi.attws.exam.repository.postgres;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityExistsException;
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
		return entityManager.find(Museum.class, id);
	}

	@Override
	public RepoException addMuseum(Museum museum) {
		try {
			entityManager.persist(museum);
		
		} catch (EntityExistsException ex) {
			throw new EntityExistsException();
//			return new RepoException(ex.getMessage());
		}
		
		return new RepoException("ok");

	}

	@Override
	public RepoException updateMuseum(Museum updatedMuseum) {
		try {
			entityManager.merge(updatedMuseum);
		
		} catch (IllegalArgumentException ex) {
			
			return new RepoException(ex.getMessage());
		}
		
		return new RepoException("ok");
	}

	@Override
	public void deleteMuseum(Museum museumToRemove) {
		entityManager.getTransaction().begin();
		entityManager.remove(museumToRemove);
		entityManager.flush();
		entityManager.getTransaction().commit();

	}

}
