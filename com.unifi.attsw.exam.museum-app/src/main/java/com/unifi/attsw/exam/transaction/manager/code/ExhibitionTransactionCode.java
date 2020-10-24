package com.unifi.attsw.exam.transaction.manager.code;

import java.util.function.Function;

import com.unifi.attsw.exam.repository.ExhibitionRepository;

@FunctionalInterface
public interface ExhibitionTransactionCode<T> extends Function<ExhibitionRepository, T> {
	
}
