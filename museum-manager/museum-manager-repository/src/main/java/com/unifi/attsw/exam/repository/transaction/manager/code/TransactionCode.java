package com.unifi.attsw.exam.repository.transaction.manager.code;

import com.unifi.attsw.exam.repository.repository.ExhibitionRepository;
import com.unifi.attsw.exam.repository.repository.MuseumRepository;
import com.unifi.attsw.exam.repository.transaction.manager.functions.RepositoryFunction;

/**
 * 
 * A Functional Interface representing code to be executed dealing with both
 * Exhibition and Museum entities
 *
 * 
 */
@FunctionalInterface
public interface TransactionCode<T> extends RepositoryFunction<MuseumRepository, ExhibitionRepository, T> {

}
