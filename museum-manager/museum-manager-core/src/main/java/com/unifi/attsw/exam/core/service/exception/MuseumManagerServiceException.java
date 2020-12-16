package com.unifi.attsw.exam.core.service.exception;

@SuppressWarnings("serial")
public class MuseumManagerServiceException extends RuntimeException {
	
	public MuseumManagerServiceException(String message, Throwable ex) {
		super(message);
	}

	public MuseumManagerServiceException() {
	}
}
