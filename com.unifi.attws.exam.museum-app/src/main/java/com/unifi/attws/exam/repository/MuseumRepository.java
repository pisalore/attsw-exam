package com.unifi.attws.exam.repository;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityExistsException;

import com.unifi.attws.exam.model.Museum;
import com.unifi.attws.exam.repository.postgres.RepoException;

public interface MuseumRepository {
	
	public List<Museum> findAllMuseums();
	public Museum retrieveMuseumById(UUID id);
	public RepoException addMuseum(Museum museum) throws EntityExistsException;
	public RepoException updateMuseum(Museum updatedMuseum);
	public void deleteMuseum(Museum museumToRemove);
	
}
