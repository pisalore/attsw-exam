package com.unifi.attws.exam.repository.postgres;

import java.util.List;

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
	public Museum retrieveMuseumById(Long id) {
		List<Museum> museums = findAllMuseums();
		return museums.stream().filter(m -> m.getId() == id).findFirst().orElse(null);
	}

	@Override
	public RepoException addMuseum(Museum museum) {
		try {
			entityManager.persist(museum);
		
		} catch (EntityExistsException ex) {
			
			return new RepoException(ex.getMessage());
		}
		
		return new RepoException("ok");

	}

	@Override
	public Museum updateMuseum(Museum updatedMuseum) {
		entityManager.getTransaction().begin();
		entityManager.merge(updatedMuseum);
		entityManager.flush();
		entityManager.getTransaction().commit();
		return updatedMuseum;
	}

	@Override
	public void deleteMuseum(Museum museumToRemove) {
		entityManager.getTransaction().begin();
		entityManager.remove(museumToRemove);
		entityManager.flush();
		entityManager.getTransaction().commit();

	}

}
