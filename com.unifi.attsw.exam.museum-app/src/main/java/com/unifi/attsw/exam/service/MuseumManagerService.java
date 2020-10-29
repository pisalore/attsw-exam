package com.unifi.attsw.exam.service;

import java.util.List;

import com.unifi.attsw.exam.exception.RepositoryException;
import com.unifi.attsw.exam.model.Museum;

public interface MuseumManagerService {
	public Museum saveMuseum(Museum museum) throws RepositoryException;
	
	public List<Museum> getAllMuseums() throws RepositoryException;
	
	public void deleteMuseum(Museum museum) throws RepositoryException;
	
	
}
