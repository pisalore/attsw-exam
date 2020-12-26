package com.unifi.attsw.exam.core.service.exception;

/**
 * 
 * Custom Service exception for communicate to Controller about errors occurred
 * in underlying layers.
 *
 */
public class MuseumManagerServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MuseumManagerServiceException(String message, Throwable ex) {
		super(message);
	}

}
