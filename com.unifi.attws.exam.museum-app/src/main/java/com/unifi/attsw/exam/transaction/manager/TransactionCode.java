package com.unifi.attws.exam.transaction.manager;


import com.unifi.attws.exam.repository.ExhibitionRepository;
import com.unifi.attws.exam.repository.MuseumRepository;

@FunctionalInterface
public interface TransactionCode<T> extends RepositoryFunction<MuseumRepository, ExhibitionRepository, T> {

}
