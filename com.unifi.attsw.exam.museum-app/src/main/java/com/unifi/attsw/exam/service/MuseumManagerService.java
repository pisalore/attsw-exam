package com.unifi.attsw.exam.service;

import com.unifi.attsw.exam.exception.RepositoryException;
import com.unifi.attsw.exam.model.Museum;

public interface MuseumManagerService {
	Museum saveMuseum(Museum museum) throws RepositoryException;

}
