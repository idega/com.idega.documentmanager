package com.idega.documentmanager.util;

/**
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 *
 */
public class InitializationException extends Exception {

	private static final long serialVersionUID = -472204824125054830L;

	public InitializationException(String message) {
		super("InitializationException: " + message);
	}
	
	public InitializationException(String message, Throwable e) {
		super("InitializationException: " + message, e);
	}
	
	public InitializationException(Throwable e) {
		super("InitializationException", e);
	}
}
