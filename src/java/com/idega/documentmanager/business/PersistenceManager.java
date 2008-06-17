package com.idega.documentmanager.business;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.idega.documentmanager.component.FormDocument;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.5 $
 * 
 * Last modified: $Date: 2008/06/17 12:16:11 $ by $Author: civilis $
 */
public interface PersistenceManager {

	public abstract PersistedFormDocument loadForm(Long formId);
	
	public abstract PersistedFormDocument saveForm(FormDocument document) throws IllegalAccessException;
	
	public abstract PersistedFormDocument takeForm(Long formId);
	
	public abstract List<PersistedForm> getStandaloneForms();
	
	public abstract void saveSubmittedData(Long formId, InputStream is, String identifier) throws IOException;
}