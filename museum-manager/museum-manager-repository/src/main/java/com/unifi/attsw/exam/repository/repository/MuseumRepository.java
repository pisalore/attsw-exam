package com.unifi.attsw.exam.repository.repository;

import java.util.List;
import java.util.UUID;

import com.unifi.attsw.exam.repository.model.Museum;

/**
 * 
 * Repository layer interface for Museum entity
 * 
 */
public interface MuseumRepository {
	/**
	 * Get all the Museums from Database
	 * 
	 * @return The List of all the persisted museums
	 */
	public List<Museum> findAllMuseums();

	/**
	 * Returns the Museum with the given ID
	 * 
	 * @param museumId The Museum ID
	 * @return The Museum with the specified ID if exists, else null
	 */
	public Museum findMuseumById(UUID museumId);

	/**
	 * Returns the Museum with the given name
	 * 
	 * @param museumName The Museum name
	 * @return The Museum with the specified name if exists, else null
	 */
	public Museum findMuseumByName(String museumName);

	/**
	 * Persists a new Museum to Database
	 * 
	 * @param newMuseum The Museum to save
	 * @return The saved Museum
	 */
	public Museum addMuseum(Museum newMuseum);

	/**
	 * Updates a given existing Museum
	 * 
	 * @param updatedMuseum The Museum to update
	 * @return The updated Museum
	 */
	public Museum updateMuseum(Museum updatedMuseum);

	/**
	 * Deletes a Museum
	 * 
	 * @param museumToRemove The existing Museum to be removed
	 */
	public void deleteMuseum(Museum museumToRemove);

}
