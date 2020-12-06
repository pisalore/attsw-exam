package com.unifi.attsw.exam.controller;

import com.unifi.attsw.exam.model.Exhibition;
import com.unifi.attsw.exam.model.Museum;

public interface MuseumController {
	
	public void getAllMuseums();
	
	public void getAllExhibitions();
	
	public void getAllMuseumExhibitions(String museumName);
	
	public void saveMuseum(Museum museum);
	
	public void saveExhibition(String museumName, Exhibition exhibition);
	
	public void deleteMuseum(Museum museum);
	
	public void deleteExhibition(Exhibition exhibition);
	
	public void bookExhibitionSeat(Exhibition exhibition);
	
	public void openExhibitionsDashboard();
	
	public void openMuseumDashboard();

}
