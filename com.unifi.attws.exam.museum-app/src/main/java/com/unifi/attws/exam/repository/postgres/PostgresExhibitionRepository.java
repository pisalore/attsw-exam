package com.unifi.attws.exam.repository.postgres;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;

import com.unifi.attws.exam.model.Exhibition;
import com.unifi.attws.exam.repository.ExhibitionRepository;

public class PostgresExhibitionRepository implements ExhibitionRepository{
	
	private EntityManager entityManager;


	@Override
	public List<Exhibition> findAllExhibition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Exhibition> findExhibitionsByMuseum(UUID museumId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Exhibition findExhibitionById(UUID exhibitionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addNewExhibition(Exhibition newExhibition) {
		// TODO Auto-generated method stub
		
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
