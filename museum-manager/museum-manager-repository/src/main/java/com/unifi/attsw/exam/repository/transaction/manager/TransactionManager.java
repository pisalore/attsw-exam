package com.unifi.attsw.exam.repository.transaction.manager;

import com.unifi.attsw.exam.repository.repository.exception.RepositoryException;
import com.unifi.attsw.exam.repository.transaction.manager.code.ExhibitionTransactionCode;
import com.unifi.attsw.exam.repository.transaction.manager.code.MuseumTransactionCode;
import com.unifi.attsw.exam.repository.transaction.manager.code.TransactionCode;

public interface TransactionManager {
	<T> T doInTransaction(TransactionCode<T> code) throws RepositoryException;

	<T> T doInTransactionMuseum(MuseumTransactionCode<T> code) throws RepositoryException;
	
	<T> T doInTransactionExhibition(ExhibitionTransactionCode<T> code) throws RepositoryException;

}
