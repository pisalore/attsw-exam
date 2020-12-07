package com.unifi.attsw.exam.repository;

import java.util.List;
import java.util.UUID;

import com.unifi.attsw.exam.model.Exhibition;;

public interface ExhibitionRepository {
	public List<Exhibition> findAllExhibitions();

	public Exhibition findExhibitionById(UUID exhibitionId);
	
	public Exhibition findExhibitionByName(String exhibitionName);

	public List<Exhibition> findExhibitionsByMuseumId(UUID museumId);

	public Exhibition addNewExhibition(Exhibition newExhibition);

	public Exhibition updateExhibition(Exhibition updatedExhibition);

	public void deleteExhibition(Exhibition deletedExhibition);

}
