package com.util;


/**
 * Interface to handle async url request and json results
 * @author brajesh
 *
 */
public interface AsyncURLHandler {

	public void handleException(Exception e);
	
	public void handleSuccess(String result);
}
