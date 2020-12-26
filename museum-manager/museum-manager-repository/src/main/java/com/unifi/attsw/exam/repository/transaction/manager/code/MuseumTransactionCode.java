package com.unifi.attsw.exam.repository.transaction.manager.code;

import java.util.function.Function;

import com.unifi.attsw.exam.repository.repository.MuseumRepository;

/**
 * 
 * A Functional Interface representing code to be executed dealing with Museum
 * entities
 *
 * 
 */
@FunctionalInterface
public interface MuseumTransactionCode<T> extends Function<MuseumRepository, T> {

}
