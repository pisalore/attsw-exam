package com.unifi.attsw.exam.service;

import java.util.List;

import com.unifi.attsw.exam.exception.RepositoryException;
import com.unifi.attsw.exam.model.Exhibition;
import com.unifi.attsw.exam.model.Museum;

public interface MuseumManagerService {
	public List<Museum> getAllMuseums() throws RepositoryException;

	public List<Exhibition> getAllExhibitions() throws RepositoryException;

	public List<Exhibition> getAllMuseumExhibitions(Museum museum);
	
	public Museum getMuseumByName(String museumName);

	public Museum saveMuseum(Museum museum);

	public void deleteMuseum(Museum museum);

	public Exhibition addNewExhibition(String museumName, Exhibition exhibition);

	public void deleteExhibition(Exhibition exhibition);
	
	public void bookExhibitionSeat(Exhibition exhibition);

}
