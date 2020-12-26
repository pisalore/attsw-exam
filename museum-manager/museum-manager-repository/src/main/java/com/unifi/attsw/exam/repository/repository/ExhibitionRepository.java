package com.unifi.attsw.exam.repository.repository;

import java.util.List;
import java.util.UUID;

import com.unifi.attsw.exam.repository.model.Exhibition;

/**
 * 
 * Repository layer interface for Exhibition entity
 * 
 */
public interface ExhibitionRepository {
	
	/**
	 * Get all the Exhibitions from Database
	 * @return The List of all the persisted exhibitions
	 */
	public List<Exhibition> findAllExhibitions();
	
	/**
	 * Returns the Exhibition with the given ID
	 * @param exhibitionId The Exhibition ID
	 * @return The Exhibition with the specified ID if exists, else null
	 */
	public Exhibition findExhibitionById(UUID exhibitionId);
	
	/**
	 * Returns the Museum with the given name
	 * @param exhibitionName The Exhibition name
	 * @return The Exhibition with the specified name if exists, else null
	 */
	public Exhibition findExhibitionByName(String exhibitionName);
	
	/**
	 * Get all the Exhibitions belonging to a specified Museum
	 * @param museumId The ID of the Museum to which searched Exhibitions belong
	 * @return The List of all the Exhibitions which belong to a given Museum
	 */
	public List<Exhibition> findExhibitionsByMuseumId(UUID museumId);
	
	/**
	 * Persists a new Exhibition to Database
	 * @param newExhibition The Exhibition to save
	 * @return The saved Exhibition
	 */
	public Exhibition addNewExhibition(Exhibition newExhibition);
	
	/**
	 * Updates a given existing Exhibition
	 * @param updatedExhibition The Exhibition to update
	 * @return The updated Exhibition
	 */
	public Exhibition updateExhibition(Exhibition updatedExhibition);
	
	/**
	 * Deletes an Exhibition
	 * @param exhibitionToRemove The existing Exhibition to be removed
	 */
	public void deleteExhibition(Exhibition exhibitionToRemove);

}
