package com.unifi.attsw.exam.repository.postgres;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import com.unifi.attsw.exam.model.Exhibition;
import com.unifi.attsw.exam.repository.ExhibitionRepository;

public class PostgresExhibitionRepository implements ExhibitionRepository {

	private EntityManager entityManager;
	
	public PostgresExhibitionRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<Exhibition> findAllExhibitions() {
		List<Exhibition> exhibitions = entityManager.createQuery("FROM Exhibition", Exhibition.class).getResultList();
		return exhibitions;
	}

	@Override
	public Exhibition findExhibitionById(UUID exhibitionId) {
		try {
			return entityManager.find(Exhibition.class, exhibitionId);
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException("Cannot find entity, invalid or null id: " + exhibitionId);
		}

	}

	@Override
	public List<Exhibition> findExhibitionsByMuseumId(UUID museumId) {
		if (museumId == null) {
			throw new IllegalArgumentException("Museum ID cannot be null.");
		}

		return findAllExhibitions().stream().filter(e -> e.getMuseumId().equals(museumId)).collect(Collectors.toList());

	}

	@Override
	public Exhibition addNewExhibition(Exhibition newExhibition) {
		try {
			entityManager.persist(newExhibition);
			return newExhibition;
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException();
		}

	}

	@Override
	public Exhibition updateExhibition(Exhibition updatedExhibition) {
		try {
			return entityManager.merge(updatedExhibition);
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException();
		}

	}

	@Override
	public void deleteExhibition(Exhibition deletedExhibition) {
		try {
			entityManager.remove(deletedExhibition);
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException();
		}

	}

}
