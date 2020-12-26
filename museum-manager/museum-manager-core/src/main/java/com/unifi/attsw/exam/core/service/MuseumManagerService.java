package com.unifi.attsw.exam.core.service;

import java.util.List;

import com.unifi.attsw.exam.core.service.exception.MuseumManagerServiceException;
import com.unifi.attsw.exam.repository.model.Exhibition;
import com.unifi.attsw.exam.repository.model.Museum;
import com.unifi.attsw.exam.repository.repository.exception.RepositoryException;

/**
 * 
 * The Service layer which handle application requests both for Museum and
 * Exhibition management operations.
 * 
 */

public interface MuseumManagerService {

	/**
	 * Communicates with Persistence layer in order to get all persisted Museums.
	 * 
	 * @return A List with all the persisted Museum in database.
	 * @throws RepositoryException if a database level error occurs.
	 */
	public List<Museum> getAllMuseums() throws RepositoryException;

	/**
	 * Communicates with Persistence layer in order to get all persisted
	 * Exhibitions.
	 * 
	 * @return A List with all the persisted Exhibitions in database.
	 * @throws RepositoryException if a database level error occurs.
	 */
	public List<Exhibition> getAllExhibitions() throws RepositoryException;

	/**
	 * Communicates with Persistence layer in order to get all persisted Exhibitions
	 * which belongs to a given Museum.
	 * 
	 * @param museum The Museum the looked for exhibitions belong to.
	 * @return A List with the exhibitions which belongs to the given Museum.
	 * @throws MuseumManagerServiceException if an error occurs both at service or
	 *                                       database level.
	 */
	public List<Exhibition> getAllMuseumExhibitions(Museum museum) throws MuseumManagerServiceException;

	/**
	 * Communicates with Persistence layer in order to find the Museum with the
	 * specified given name.
	 * 
	 * @param museumName The looked for Museum name.
	 * @return The Museum with the specified name if exists, else null.
	 * @throws MuseumManagerServiceException if an error occurs both at service or
	 *                                       database level.
	 */
	public Museum getMuseumByName(String museumName) throws MuseumManagerServiceException;

	/**
	 * 
	 * Communicates with Persistence layer in order to save or update the given
	 * Museum.
	 * 
	 * @param museum The Museum to save.
	 * @return The saved Museum.
	 * @throws MuseumManagerServiceException if an error occurs both at service or
	 *                                       database level.
	 */
	public Museum saveMuseum(Museum museum) throws MuseumManagerServiceException;

	/**
	 * Communicates with Persistence layer in order to delete the given Museum.
	 * 
	 * @param museum The Museum to delete.
	 * @throws MuseumManagerServiceException if an error occurs both at service or
	 *                                       database level.
	 */
	public void deleteMuseum(Museum museum) throws MuseumManagerServiceException;

	/**
	 * Communicates with Persistence layer in order to find the Exhibition with the
	 * specified given name.
	 * 
	 * @param exhibitionName The looked for Exhibition name.
	 * @return The Exhibition with the specified name if exists, else null.
	 * @throws MuseumManagerServiceException if an error occurs both at service or
	 *                                       database level.
	 */
	public Exhibition getExhibitionByName(String exhibitionName) throws MuseumManagerServiceException;

	/**
	 * Communicates with Persistence layer in order to save a new Exhibition.
	 * 
	 * @param museumName The name of the Museum to which assign the given new
	 *                   Exhibition.
	 * @param exhibition The Exhibition to save.
	 * @return The saved Exhibition.
	 * @throws MuseumManagerServiceException if an error occurs both at service or
	 *                                       database level.
	 */
	public Exhibition addNewExhibition(String museumName, Exhibition exhibition) throws MuseumManagerServiceException;

	/**
	 * Communicates with Persistence layer in order to delete the given Exhibition.
	 * 
	 * @param exhibition The Exhibition to delete.
	 * @throws MuseumManagerServiceException if an error occurs both at service or
	 *                                       database level.
	 */
	public void deleteExhibition(Exhibition exhibition) throws MuseumManagerServiceException;

	/**
	 * Communicates with Persistence layer in order to book a seat for the given
	 * Exhibition.
	 * 
	 * @param exhibition The exhibition to book
	 * @throws MuseumManagerServiceException if an error occurs both at service or
	 *                                       database level.
	 */
	public void bookExhibitionSeat(Exhibition exhibition) throws MuseumManagerServiceException;

}
