package com.unifi.attsw.exam.transaction.manager;

import com.unifi.attsw.exam.repository.exception.RepositoryException;
import com.unifi.attsw.exam.transaction.manager.code.ExhibitionTransactionCode;
import com.unifi.attsw.exam.transaction.manager.code.MuseumTransactionCode;
import com.unifi.attsw.exam.transaction.manager.code.TransactionCode;

public interface TransactionManager {
	<T> T doInTransaction(TransactionCode<T> code) throws RepositoryException;

	<T> T doInTransactionMuseum(MuseumTransactionCode<T> code) throws RepositoryException;
	
	<T> T doInTransactionExhibition(ExhibitionTransactionCode<T> code) throws RepositoryException;

}
