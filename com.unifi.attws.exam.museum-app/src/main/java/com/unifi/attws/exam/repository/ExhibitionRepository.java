package com.unifi.attws.exam.repository;

import java.util.List;
import java.util.UUID;

import com.unifi.attws.exam.model.Exhibition;;

public interface ExhibitionRepository {
	public List<Exhibition> findAllExhibition();
	public List<Exhibition> findExhibitionsByMuseum(UUID museumId);
	public Exhibition findExhibitionById(UUID exhibitionId);
	public void addNewExhibition(Exhibition newExhibition);
	public void updateExhibition(Exhibition updatedExhibition);
	public void deleteExhibition(Exhibition deletedExhibition);
	
}
