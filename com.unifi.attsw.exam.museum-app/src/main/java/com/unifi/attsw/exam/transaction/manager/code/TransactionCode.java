package com.unifi.attsw.exam.transaction.manager.code;


import com.unifi.attsw.exam.repository.ExhibitionRepository;
import com.unifi.attsw.exam.repository.MuseumRepository;
import com.unifi.attsw.exam.transaction.manager.functions.RepositoryFunction;

@FunctionalInterface
public interface TransactionCode<T> extends RepositoryFunction<MuseumRepository, ExhibitionRepository, T> {

}
