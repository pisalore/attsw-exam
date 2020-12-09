package com.unifi.attsw.exam.repository.transaction.manager.code;

import java.util.function.Function;

import com.unifi.attsw.exam.repository.repository.ExhibitionRepository;

@FunctionalInterface
public interface ExhibitionTransactionCode<T> extends Function<ExhibitionRepository, T> {
	
}
