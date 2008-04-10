package com.idega.documentmanager.business;

import java.util.List;

import com.idega.documentmanager.component.FormDocument;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.4 $
 * 
 * Last modified: $Date: 2008/04/10 01:08:00 $ by $Author: civilis $
 */
public interface PersistenceManager {

	public abstract PersistedFormDocument loadForm(Long formId);
	
	public abstract PersistedFormDocument saveForm(FormDocument document) throws IllegalAccessException;
	
	public abstract PersistedFormDocument takeForm(Long formId);
	
	public abstract List<PersistedForm> getStandaloneForms();
}