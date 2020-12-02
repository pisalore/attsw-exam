package com.unifi.attsw.exam.controller;

import com.unifi.attsw.exam.exception.RepositoryException;
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
	
	public void getAllMuseums() {
		try {
			museumView.showAllMuseums(museumService.getAllMuseums());
		} catch (RepositoryException | IllegalStateException ex) {
			museumView.showError("Impossibile to get museums.", null);
			return;
		}
	}
	
	public void getAllExhibitions() {
		try {
			exhibitionView.showAllExhibitions(museumService.getAllExhibitions());
		} catch (RepositoryException | IllegalStateException ex) {
			museumView.showError("Impossibile to get all exhibitions.", null);
			return;
		}
	}
	
	public void getAllMuseumExhibitions(String museumName) {
		try {
			Museum museum = museumService.getMuseumByName(museumName);
			exhibitionView.showMuseumExhibitions(museumService.getAllMuseumExhibitions(museum));
		} catch (RuntimeException ex) {
			exhibitionView.showError("Impossibile to get all exhibitions.", null);
			return;
		}
	}

	public void saveMuseum(Museum museum) {
		try {
			museumService.saveMuseum(museum);
			museumView.museumAdded(museum);
		} catch (RuntimeException ex) {
			museumView.showError("Impossibile to add Museum: ", museum);
			return;
		}
	}

	public void saveExhibition(String museumName, Exhibition exhibition) {
		try {
			museumService.addNewExhibition(museumName, exhibition);
			exhibitionView.exhibitionAdded(exhibition);
		} catch (RuntimeException ex) {
			exhibitionView.showError("Impossible to add Exhibition: ", exhibition);
			return;
		}
	}

	public void deleteMuseum(Museum museum) {
		try {
			museumService.deleteMuseum(museum);
			museumView.museumRemoved(museum);
		} catch (RuntimeException ex) {
			museumView.showError("Impossible to delete Museum: ", museum);
		}
	}

	public void deleteExhibition(Exhibition exhibition) {
		try {
			museumService.deleteExhibition(exhibition);
			exhibitionView.exhibitionRemoved(exhibition);
		} catch (RuntimeException ex) {
			exhibitionView.showError("Impossible to delete Exhibition: ", exhibition);
		}
	}

	public void bookExhibitionSeat(Exhibition exhibition) {
		try {
			museumService.bookExhibitionSeat(exhibition);
		} catch (RuntimeException ex) {
			exhibitionView.showError("Impossible to book a seat for Exhibition: ", exhibition);
		}
	}

}
