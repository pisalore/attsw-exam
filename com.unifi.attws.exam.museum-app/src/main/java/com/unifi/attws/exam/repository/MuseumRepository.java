package com.unifi.attws.exam.repository;

import java.util.List;
import java.util.UUID;

import com.unifi.attws.exam.model.Museum;

public interface MuseumRepository {

	public List<Museum> findAllMuseums();

	public Museum findMuseumById(UUID id);

	public Museum addMuseum(Museum museum);

	public Museum updateMuseum(Museum updatedMuseum);

	public void deleteMuseum(Museum museumToRemove);

}
