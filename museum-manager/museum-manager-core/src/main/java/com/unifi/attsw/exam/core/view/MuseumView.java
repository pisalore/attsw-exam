package com.unifi.attsw.exam.core.view;

import java.util.List;

import com.unifi.attsw.exam.repository.model.Museum;

public interface MuseumView {
	
	public void showAllMuseums(List<Museum> museums);
	
	public void showError(String message, Museum museum);
	
	public void museumAdded(Museum museum);
	
	public void museumRemoved(Museum museum);

}
