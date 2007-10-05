package com.idega.documentmanager.component;

import java.util.Locale;

import org.w3c.dom.Document;

import com.idega.documentmanager.business.component.Page;

/**
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 *
 */
public interface FormComponentParent {
	
	public abstract Document getXformsDocument();
	
	public abstract Page getPage(String component_id);
	
	public abstract FormComponent getContainedComponent(String component_id);
	
	public abstract void setFormDocumentModified(boolean changed);
	
	public abstract boolean isFormDocumentModified();
	
	public abstract Document getComponentsXml();
	
	public abstract void setComponentsXml(Document xml);
	
	public abstract String getFormId();
	
	public abstract Locale getDefaultLocale();
}