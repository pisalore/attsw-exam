package com.unifi.attsw.exam.transaction.manager.code;

import java.util.function.Function;

import com.unifi.attsw.exam.repository.MuseumRepository;

@FunctionalInterface
public interface MuseumTransactionCode<T> extends Function<MuseumRepository, T> {

}
