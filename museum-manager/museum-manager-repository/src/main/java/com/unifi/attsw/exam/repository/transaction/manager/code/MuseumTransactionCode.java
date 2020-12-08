package com.unifi.attsw.exam.repository.transaction.manager.code;

import java.util.function.Function;

import com.unifi.attsw.exam.repository.repository.MuseumRepository;

@FunctionalInterface
public interface MuseumTransactionCode<T> extends Function<MuseumRepository, T> {

}
