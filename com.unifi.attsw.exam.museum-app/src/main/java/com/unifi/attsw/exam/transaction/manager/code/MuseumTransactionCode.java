package com.unifi.attsw.exam.transaction.manager.code;

import com.unifi.attsw.exam.repository.MuseumRepository;
import com.unifi.attsw.exam.transaction.manager.functions.MuseumRepositoryFunction;

@FunctionalInterface
public interface MuseumTransactionCode<T> extends MuseumRepositoryFunction<MuseumRepository, T> {

}
