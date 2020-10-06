package com.unifi.attws.exam.repository;

import java.util.List;

import com.unifi.attws.exam.model.Museum;

public interface MuseumRepository {
	
	public List<Museum> findAllMuseums();
	public Museum retrieveMuseumById(Long id);
	public void createMuseum(String museumName, int numberOfRooms);
	public Museum updateMuseum(Museum updatedMuseum);
	public void deleteMuseum();
	
}
