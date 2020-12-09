package com.unifi.attsw.exam.repository.transaction.manager.functions;

@FunctionalInterface
public interface RepositoryFunction<MuseumRepository, ExhibitionRepository, T> {
	public T apply(MuseumRepository museumRepository, ExhibitionRepository exhibitionRepository);

}
