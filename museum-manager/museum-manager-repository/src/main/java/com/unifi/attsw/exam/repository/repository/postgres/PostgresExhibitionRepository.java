package com.unifi.attsw.exam.repository.repository.postgres;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import com.unifi.attsw.exam.repository.model.Exhibition;
import com.unifi.attsw.exam.repository.repository.ExhibitionRepository;

public class PostgresExhibitionRepository implements ExhibitionRepository {

	private EntityManager entityManager;

	public PostgresExhibitionRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public List<Exhibition> findAllExhibitions() {
		return entityManager.createQuery("FROM Exhibition", Exhibition.class).getResultList();
	}

	@Override
	public Exhibition findExhibitionById(UUID exhibitionId) {
		try {
			return entityManager.find(Exhibition.class, exhibitionId);
		} catch (IllegalArgumentException ex) {
			throw new IllegalArgumentException("Cannot find entity, invalid or null id: " + exhibitionId, ex);
		}

	}

	@Override
	public Exhibition findExhibitionByName(String exhibitionToFind) {
		List<Exhibition> exhibitions = entityManager.createQuery("FROM Exhibition", Exhibition.class).getResultList();
		return exhibitions.stream().filter(e -> e.getName().equals(exhibitionToFind)).findAny().orElse(null);

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
		entityManager.persist(newExhibition);
		return newExhibition;

	}

	@Override
	public Exhibition updateExhibition(Exhibition updatedExhibition) {
			return entityManager.merge(updatedExhibition);

	}

	@Override
	public void deleteExhibition(Exhibition deletedExhibition) {
		entityManager.remove(deletedExhibition);

	}

}
