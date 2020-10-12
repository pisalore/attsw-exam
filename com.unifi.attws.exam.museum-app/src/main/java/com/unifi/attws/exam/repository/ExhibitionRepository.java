package com.unifi.attws.exam.repository;

import java.util.List;
import java.util.UUID;

import com.unifi.attws.exam.model.Exhibition;;

public interface ExhibitionRepository {
	public List<Exhibition> findAllExhibitions();
	public Exhibition findExhibitionById(UUID exhibitionId);
	public List<Exhibition> findExhibitionsByMuseumId(UUID museumId);
	public Exhibition addNewExhibition(Exhibition newExhibition);
	public void updateExhibition(Exhibition updatedExhibition);
	public void deleteExhibition(Exhibition deletedExhibition);
	
}
