package com.unifi.attsw.exam.repository.transaction.manager.functions;

@FunctionalInterface
public interface RepositoryFunction<M, E, T> {
	public T apply(M museumRepository, E exhibitionRepository);

}
