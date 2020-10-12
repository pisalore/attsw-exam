package com.unifi.attws.exam.repository.postgres;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import com.unifi.attws.exam.model.Exhibition;
import com.unifi.attws.exam.repository.ExhibitionRepository;

public class PostgresExhibitionRepository implements ExhibitionRepository {

	private EntityManager entityManager;

	@Override
	public List<Exhibition> findAllExhibitions() {
		List<Exhibition> exhibitions = entityManager.createQuery("FROM Exhibition", Exhibition.class).getResultList();
		return exhibitions;
	}
	
	@Override
	public Exhibition findExhibitionById(UUID exhibitionId) {
			return entityManager.find(Exhibition.class, exhibitionId);
		
	}

	@Override
	public List<Exhibition> findExhibitionsByMuseumId(UUID museumId) {
		if (museumId == null) {
			throw new IllegalArgumentException("Museum ID cannot be null.");
		}

		return findAllExhibitions()
				.stream()
				.filter(e -> e.getMuseumId().equals(museumId))
				.collect(Collectors.toList());

	}

	@Override
	public Exhibition addNewExhibition(Exhibition newExhibition) {
		entityManager.persist(newExhibition);
		return newExhibition;

	}

	@Override
	public void updateExhibition(Exhibition updatedExhibition) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteExhibition(Exhibition deletedExhibition) {
		// TODO Auto-generated method stub

	}

	public PostgresExhibitionRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}
