package com.unifi.attws.exam.transaction.manager;

import com.unifi.attws.exam.exception.RepositoryException;

public interface TransactionManager {
	<T> T doInTransaction(TransactionCode<T> code) throws RepositoryException;

}
