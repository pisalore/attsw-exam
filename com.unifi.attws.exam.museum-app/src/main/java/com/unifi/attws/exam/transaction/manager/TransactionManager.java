package com.unifi.attws.exam.transaction.manager;

import com.unifi.attws.exam.repository.MuseumRepository;

public interface TransactionManager {
	<T> T doInTransaction(TransactionCode<T> code, MuseumRepository repo);

}
