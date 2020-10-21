package com.unifi.attsw.exam.transaction.manager;

import com.unifi.attsw.exam.exception.RepositoryException;

public interface TransactionManager {
	<T> T doInTransaction(TransactionCode<T> code) throws RepositoryException;

}
