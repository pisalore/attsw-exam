package com.unifi.attsw.exam.view;

import java.util.List;

import com.unifi.attsw.exam.model.Exhibition;

public interface ExhibitionView {

	public void showAllExhibitions(List<Exhibition> exhibitions);

	public void showMuseumExhibitions(List<Exhibition> exhibitions);
	
	public void exhibitionAdded(Exhibition exhibition);
	
	public void exhibitionRemoved(Exhibition exhibition);

	public void showError(String message, Exhibition exhibition);

	public void exhibitionBooked();

}
