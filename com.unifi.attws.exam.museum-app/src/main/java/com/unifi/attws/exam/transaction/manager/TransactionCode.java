package com.unifi.attws.exam.transaction.manager;

import java.util.function.Function;

import com.unifi.attws.exam.repository.MuseumRepository;

@FunctionalInterface
public interface TransactionCode<T> extends Function<MuseumRepository, T> {

}
