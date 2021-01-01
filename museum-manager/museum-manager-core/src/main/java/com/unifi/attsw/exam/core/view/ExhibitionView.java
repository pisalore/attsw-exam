package com.unifi.attsw.exam.core.view;

import java.util.List;

import com.unifi.attsw.exam.repository.model.Exhibition;

/**
 * 
 * The Exhibition View interface.
 *
 */
public interface ExhibitionView {

	/**
	 * Show all the retrieved Exhibitions.
	 * 
	 * @param exhibitions The Exhibitions to show.
	 */
	public void showAllExhibitions(List<Exhibition> exhibitions);

	/**
	 * Show all the retrieved Exhibitions for a given Museum.
	 * 
	 * @param exhibitions The Exhibitions of a given Museum to show.
	 */
	public void showMuseumExhibitions(List<Exhibition> exhibitions);

	/**
	 * Update the view when an Exhibition is correctly persisted.
	 * 
	 * @param exhibition The persisted Exhibition.
	 */
	public void exhibitionAdded(Exhibition exhibition);

	/**
	 * Update the view when an Exhibition is correctly deleted.
	 * 
	 * @param exhibition The deleted Exhibition.
	 */
	public void exhibitionRemoved(Exhibition exhibition);

	/**
	 * Show the error message from exception occurred in underlying layers.
	 * 
	 * @param message    The error message.
	 * @param exhibition The involved Exhibition.
	 */
	public void showError(String message, Exhibition exhibition);

	/**
	 * Clear the view when an Exhibition is correctly booked.
	 */
	public void exhibitionBooked();

}
