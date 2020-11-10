package com.unifi.attsw.exam.controller;

import com.unifi.attsw.exam.model.Museum;
import com.unifi.attsw.exam.service.MuseumManagerService;

public class MuseumController {

	private MuseumManagerService museumService;

	public MuseumController(MuseumManagerService museumService) {
		this.museumService = museumService;
	}

	public void saveMuseum(Museum museum) {
		try {
			museumService.saveMuseum(museum);
		} catch (RuntimeException ex) {
			// show error in view
			return;
		}
	}

}
