package com.unifi.attsw.exam.core.view;

import java.util.List;

import com.unifi.attsw.exam.repository.model.Museum;

/**
 * 
 * The Museum View interface.
 *
 */
public interface MuseumView {

	/**
	 * Show all the retrieved Museums.
	 * 
	 * @param museums The Museums to show.
	 */
	public void showAllMuseums(List<Museum> museums);

	/**
	 * Show the error message from exception occurred in underlying layers.
	 * 
	 * @param message The error message.
	 * @param museum  The involved Museum.
	 */
	public void showError(String message, Museum museum);

	/**
	 * Update the view when a Museum is correctly persisted.
	 * 
	 * @param museum The persisted Museum.
	 */
	public void museumAdded(Museum museum);

	/**
	 * Update the view when a Museum is correctly deleted.
	 * 
	 * @param museum The deleted Museum.
	 */
	public void museumRemoved(Museum museum);

}
