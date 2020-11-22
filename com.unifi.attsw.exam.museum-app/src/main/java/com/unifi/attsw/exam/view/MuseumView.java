package com.unifi.attsw.exam.view;

import java.util.List;

import com.unifi.attsw.exam.model.Museum;

public interface MuseumView {
	
	void showError(String message, Museum museum);
	
	void showAllMuseums(List<Museum> museums);

}
