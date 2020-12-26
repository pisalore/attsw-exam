package com.unifi.attsw.exam.repository.repository.exception;

/**
 * 
 * Exception to be thrown when an error occurs during database's operations
 *
 */
public class RepositoryException extends Exception {

	private static final long serialVersionUID = 1L;

	public RepositoryException(String message, Throwable ex) {
		super(message);
	}

	public RepositoryException(String message) {
		super(message);
	}

}
