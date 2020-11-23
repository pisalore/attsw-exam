package com.unifi.attsw.exam.view;

import java.util.List;

import com.unifi.attsw.exam.model.Museum;

public interface MuseumView {
	
	void showAllMuseums(List<Museum> museums);
	
	void showError(String message, Museum museum);
	
	public void museumAdded(Museum museum);
	
	public void museumRemoved(Museum museum);

}
