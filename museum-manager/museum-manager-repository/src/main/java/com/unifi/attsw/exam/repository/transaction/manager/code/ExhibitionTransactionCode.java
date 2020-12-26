package com.unifi.attsw.exam.repository.transaction.manager.code;

import java.util.function.Function;

import com.unifi.attsw.exam.repository.repository.ExhibitionRepository;

/**
 * 
 * A Functional Interface representing code to be executed dealing with
 * Exhibition entities
 *
 * 
 */
@FunctionalInterface
public interface ExhibitionTransactionCode<T> extends Function<ExhibitionRepository, T> {

}
