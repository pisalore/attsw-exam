package com.unifi.attsw.exam.service;

import java.util.List;

import com.unifi.attsw.exam.exception.RepositoryException;
import com.unifi.attsw.exam.model.Museum;

public interface MuseumManagerService {
	Museum saveMuseum(Museum museum) throws RepositoryException;
	
	List<Museum> getAllMuseums() throws RepositoryException;
}
