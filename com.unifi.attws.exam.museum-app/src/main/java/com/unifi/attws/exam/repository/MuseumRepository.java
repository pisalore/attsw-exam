package com.unifi.attws.exam.repository;

import java.util.List;

import com.unifi.attws.exam.model.Museum;
import com.unifi.attws.exam.repository.postgres.RepoException;

public interface MuseumRepository {
	
	public List<Museum> findAllMuseums();
	public Museum retrieveMuseumById(Long id);
	public RepoException addMuseum(Museum museum);
	public Museum updateMuseum(Museum updatedMuseum);
	public void deleteMuseum(Museum museumToRemove);
	
}
