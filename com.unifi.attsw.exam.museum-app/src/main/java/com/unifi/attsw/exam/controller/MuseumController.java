package com.unifi.attsw.exam.controller;

import com.unifi.attsw.exam.model.Exhibition;
import com.unifi.attsw.exam.model.Museum;
import com.unifi.attsw.exam.service.MuseumManagerService;
import com.unifi.attsw.exam.view.ExhibitionView;
import com.unifi.attsw.exam.view.MuseumView;

public class MuseumController {

	private MuseumManagerService museumService;
	private MuseumView museumView;
	private ExhibitionView exhibitionView;

	public MuseumController(MuseumManagerService museumService, MuseumView museumView, ExhibitionView exhibitionView) {
		this.museumService = museumService;
		this.museumView = museumView;
		this.exhibitionView = exhibitionView;
	}

	public void saveMuseum(Museum museum) {
		try {
			museumService.saveMuseum(museum);
		} catch (RuntimeException ex) {
			museumView.showError("Impossibile to add Museum.", museum);
			return;
		}
	}

	public void saveExhibition(String museumName, Exhibition exhibition) {
		try {
			museumService.addNewExhibition(museumName, exhibition);
		} catch (RuntimeException ex) {
			exhibitionView.showError("Impossible to add Exhibition.", exhibition);
			return;
		}
	}

}
