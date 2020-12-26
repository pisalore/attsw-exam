package com.unifi.attsw.exam.repository.transaction.manager.functions;

/**
 * 
 * A Functional Interface representing the apply Function accepting in input
 * both repositories for Museum and Exhibition
 *
 * 
 */
@FunctionalInterface
public interface RepositoryFunction<M, E, T> {
	public T apply(M museumRepository, E exhibitionRepository);

}
