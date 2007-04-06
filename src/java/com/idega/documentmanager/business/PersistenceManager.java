package com.idega.documentmanager.business;

import java.util.List;

import javax.faces.model.SelectItem;

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
	
	public abstract Document loadFormNoLock(String formId);

	public abstract List<SelectItem> getForms();

	public abstract Document loadSubmittedData(String formId, String submittedDataFilename) throws Exception;

	public abstract List<SubmittedDataBean> listSubmittedData(String formId) throws Exception;

	/**
	 * 
	 * @param form_id - form id to remove
	 * @param remove_submitted_data - remove submitted data for this form
	 * @throws Exception
	 */
	public abstract void removeForm(String form_id, boolean remove_submitted_data) throws FormLockException, Exception;
	
	public abstract void duplicateForm(String form_id, String new_title_for_default_locale) throws Exception;
	
	public abstract String generateFormId(String name);
	
	public abstract String getSubmittedDataResourcePath(String formId, String submittedDataFilename);
	
	public abstract void unlockForm(String form_id);
}