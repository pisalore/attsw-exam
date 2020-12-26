package com.unifi.attsw.exam.core.controller;

import com.unifi.attsw.exam.repository.model.Exhibition;
import com.unifi.attsw.exam.repository.model.Museum;

/**
 * 
 * The Museum Manager Application controller.
 *
 */
public interface MuseumController {

	/**
	 * Communicates with the Service layer in order to get all Museums.
	 */
	public void getAllMuseums();

	/**
	 * Communicates with the Service layer in order to get all Exhibitions.
	 */
	public void getAllExhibitions();

	/**
	 * Communicates with the Service layer in order to get all the specified Museum
	 * Exhibitions.
	 * 
	 * @param museumName The name of Museum the looked for exhibitions belong to.
	 */
	public void getAllMuseumExhibitions(String museumName);

	/**
	 * Communicates with the Service layer in order to save a Museum.
	 * 
	 * @param museum The Museum to save passed from the View layer.
	 */
	public void saveMuseum(Museum museum);

	/**
	 * Communicates with the Service layer in order to save an Exhibition.
	 * 
	 * @param museumName The name of Museum to which the new Exhibition belongs.
	 * @param exhibition The Exhibition to save passed from the View layer.
	 */
	public void saveExhibition(String museumName, Exhibition exhibition);

	/**
	 * Communicates with the Service layer in order to delete a Museum.
	 * 
	 * @param museum The Museum to delete passed from the View layer.
	 */
	public void deleteMuseum(Museum museum);

	/**
	 * Communicates with the Service layer in order to delete an Exhibition.
	 * 
	 * @param exhibition The Exhibition to delete passed from the View layer.
	 */
	public void deleteExhibition(Exhibition exhibition);

	/**
	 * Communicates with the Service layer in order to book the given Exhibition.
	 * 
	 * @param exhibition The Exhibition to book.
	 */
	public void bookExhibitionSeat(Exhibition exhibition);

	/**
	 * Opens the Exhibition View Dashboard component.
	 */
	public void openExhibitionsDashboard();

	/**
	 * Opens the Museum View Dashboard component.
	 */
	public void openMuseumDashboard();

}
