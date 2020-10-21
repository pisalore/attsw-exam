package com.unifi.attsw.exam.transaction.manager;


import com.unifi.attsw.exam.repository.ExhibitionRepository;
import com.unifi.attsw.exam.repository.MuseumRepository;

@FunctionalInterface
public interface TransactionCode<T> extends RepositoryFunction<MuseumRepository, ExhibitionRepository, T> {

}
