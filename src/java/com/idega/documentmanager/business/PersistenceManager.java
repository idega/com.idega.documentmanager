package com.idega.documentmanager.business;

import org.w3c.dom.Document;

/**
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 *
 */
public interface PersistenceManager {

	public void saveForm(String form_id, Document document) throws Exception;
	
	public abstract Document loadFormAndLock(String form_id) throws FormLockException, Exception;
}