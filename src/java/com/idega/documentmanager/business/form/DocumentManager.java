package com.idega.documentmanager.business.form;

import java.util.List;

import org.w3c.dom.Document;

import com.idega.documentmanager.business.PersistenceManager;
import com.idega.documentmanager.business.form.beans.LocalizedStringBean;

/**
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 *
 */
public interface DocumentManager {

	/**
	 * creates primary user form document and stores it depending
	 * 
	 * @param form_id - cannot be null
	 * @param name - form name. Can be null, then default is used.
	 *  
	 * @throws NullPointerException - form_id was not provided
	 * @throws Exception - some kind of other error occured
	 * 
	 * @return Created form document
	 */
	public abstract com.idega.documentmanager.business.form.Document createForm(String form_id, LocalizedStringBean name)
			throws NullPointerException, Exception;

	/**
	 * 
	 * @return List of available form components types by category
	 */
	public List<String> getAvailableFormComponentsTypesList(ConstComponentCategory category);

	/**
	 * Open and load document by form id
	 * 
	 * @param form_id
	 * @throws NullPointerException - form_id is not provided
	 * @throws Exception
	 * 
	 * @return loaded document
	 */
	public abstract com.idega.documentmanager.business.form.Document openForm(String form_id) throws NullPointerException, Exception;
	
	public abstract com.idega.documentmanager.business.form.Document openForm(Document xforms_doc) throws NullPointerException, Exception;
	
	public abstract com.idega.documentmanager.business.form.Document getCurrentDocument();
	
	public abstract void setPersistenceManager(PersistenceManager persistence_manager);
}