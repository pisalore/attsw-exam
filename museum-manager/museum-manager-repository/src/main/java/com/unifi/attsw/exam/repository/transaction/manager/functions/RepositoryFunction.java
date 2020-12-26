package com.unifi.attsw.exam.repository.transaction.manager.functions;

/**
 * 
 * A Functional Interface representing the apply RepositoryFunction accepting in input
 * both repositories for Museum and Exhibition
 *
 * 
 */
@FunctionalInterface
public interface RepositoryFunction<M, E, T> {
	/**
	 * 
	 * @param museumRepository The Repository for Museum entities
	 * @param exhibitionRepository The Repository for Exhibition entities
	 * @return The executed statement result
	 */
	public T apply(M museumRepository, E exhibitionRepository);

}
