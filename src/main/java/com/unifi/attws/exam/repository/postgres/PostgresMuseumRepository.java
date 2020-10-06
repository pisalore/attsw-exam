package com.unifi.attws.exam.repository.postgres;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.unifi.attws.exam.model.Museum;
import com.unifi.attws.exam.repository.MuseumRepository;

public class PostgresMuseumRepository implements MuseumRepository{
	
	private EntityManagerFactory sessionFactory;
	private EntityManager entityManager;
	
	

	public PostgresMuseumRepository() {
		sessionFactory = Persistence.createEntityManagerFactory("postgres");
		entityManager = sessionFactory.createEntityManager();
		
	}

	@Override
	public List<Museum> findAllMuseums() {
		List<Museum> museums = entityManager.createQuery("FROM Museum", Museum.class).getResultList();
		return museums;
	}

	@Override
	public Museum retrieveMuseumById(Long id) {
		List<Museum> museums = findAllMuseums();
		return museums.stream()
		.filter(m -> m.getId() == id)
		.findFirst()
		.orElse(null);
	}

	@Override
	public void addMuseum(Museum museum) {
		entityManager.getTransaction().begin();
		entityManager.persist(museum);
		entityManager.flush();
		entityManager.getTransaction().commit();
	}

	@Override
	public Museum updateMuseum(Museum updatedMuseum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteMuseum() {
		// TODO Auto-generated method stub
		
	}

}
