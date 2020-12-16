package com.unifi.attsw.exam.repository.repository.exception;

@SuppressWarnings("serial")
public class RepositoryException extends Exception {

	public RepositoryException(String message, Throwable ex) {
		super(message);
	}

	public RepositoryException() {
		super();
	}

}
