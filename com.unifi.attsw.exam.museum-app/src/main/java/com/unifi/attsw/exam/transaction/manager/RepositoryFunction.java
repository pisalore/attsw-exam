package com.unifi.attsw.exam.transaction.manager;

@FunctionalInterface
public interface RepositoryFunction<MuseumRepository, ExhibitionRepository, T> {
	public T apply(MuseumRepository museumRepository, ExhibitionRepository exhibitionRepository);

}
