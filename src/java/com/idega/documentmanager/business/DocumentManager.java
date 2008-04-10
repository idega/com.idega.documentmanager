package com.idega.documentmanager.business;

import java.util.List;

import org.chiba.xml.xslt.TransformerService;
import org.w3c.dom.Document;

import com.idega.documentmanager.business.component.ConstComponentCategory;
import com.idega.documentmanager.component.beans.LocalizedStringBean;
import com.idega.documentmanager.component.datatypes.ComponentType;
import com.idega.documentmanager.component.datatypes.ConstComponentDatatype;
import com.idega.documentmanager.manager.impl.CacheManager;
import com.idega.documentmanager.util.InitializationException;
import com.idega.idegaweb.IWMainApplication;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.7 $
 *
 * Last modified: $Date: 2008/04/10 01:08:00 $ by $Author: civilis $
 */
public interface DocumentManager {

	public com.idega.documentmanager.business.Document createForm(LocalizedStringBean formName, String formType);

	/**
	 * 
	 * @return List of available form components types by category
	 */
	public List<String> getAvailableFormComponentsTypesList(ConstComponentCategory category);
	
	/**
	 * 
	 * @return List of available form components types by datatype
	 */
	public List<ComponentType> getComponentsByDatatype(ConstComponentDatatype category);

	public com.idega.documentmanager.business.Document openForm(Long formId);
	
	public com.idega.documentmanager.business.Document openForm(Document xformsDoc);
	
	public abstract void setPersistenceManager(PersistenceManager persistence_manager);
	
	public abstract void init(IWMainApplication iwma) throws InitializationException;
	
	public abstract boolean isInited();
	
	public abstract void setCacheManager(CacheManager cacheManager);
	
	public abstract void setTransformerService(TransformerService transformerService);
	
	public abstract void setComponentsXforms(Document componentsXforms);
	
	public abstract void setComponentsXsd(Document componentsXsd);
	
	public abstract void setFormXformsTemplate(Document formXformsTemplate);
}