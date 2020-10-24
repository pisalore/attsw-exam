package com.unifi.attsw.exam.transaction.manager;

import com.unifi.attsw.exam.exception.RepositoryException;
import com.unifi.attsw.exam.transaction.manager.code.MuseumTransactionCode;
import com.unifi.attsw.exam.transaction.manager.code.TransactionCode;

public interface TransactionManager {
	<T> T doInTransaction(TransactionCode<T> code) throws RepositoryException;
	<T> T doInTransaction(MuseumTransactionCode<T> code) throws RepositoryException;

}
