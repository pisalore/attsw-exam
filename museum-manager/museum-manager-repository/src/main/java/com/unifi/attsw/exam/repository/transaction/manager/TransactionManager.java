package com.unifi.attsw.exam.repository.transaction.manager;

import com.unifi.attsw.exam.repository.repository.exception.RepositoryException;
import com.unifi.attsw.exam.repository.transaction.manager.code.ExhibitionTransactionCode;
import com.unifi.attsw.exam.repository.transaction.manager.code.MuseumTransactionCode;
import com.unifi.attsw.exam.repository.transaction.manager.code.TransactionCode;

/**
 * 
 * The mid-level layer interface between Repository and Service which manages
 * Database operations.
 *
 */
public interface TransactionManager {
	/**
	 * Transaction Manager method for handling Museum and Exhibition operations
	 * 
	 * @param <T> The generic type argument
	 * @param code The Database statement to execute interacting with both Museum
	 *             and Exhibition entities
	 * @return The query result
	 * @throws RepositoryException if an error occurs while operating with database
	 * 
	 */
	<T> T doInTransaction(TransactionCode<T> code) throws RepositoryException;

	/**
	 * Transaction Manager method for handling Museum operations
	 * 
	 * @param <T> The generic type argument
	 * @param code The Database statement to execute interacting with Museum entity
	 * @return The query result
	 * @throws RepositoryException if an error occurs while operating with database
	 * 
	 */
	<T> T doInTransactionMuseum(MuseumTransactionCode<T> code) throws RepositoryException;

	/**
	 * Transaction Manager method for handling Exhibition operations
	 * 
	 * @param <T> The generic type argument
	 * @param code The Database statement to execute interacting with Exhibition
	 *             entity
	 * @return The query result
	 * @throws RepositoryException if an error occurs while operating with database
	 * 
	 */
	<T> T doInTransactionExhibition(ExhibitionTransactionCode<T> code) throws RepositoryException;

}
