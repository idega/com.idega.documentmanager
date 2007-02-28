package com.idega.documentmanager.business.form.beans;

import java.util.Locale;

import org.w3c.dom.Document;

import com.idega.documentmanager.business.form.Page;

/**
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 *
 */
public interface IFormComponentParent {
	
	public abstract Document getXformsDocument();
	
	public abstract Page getPage(String component_id);
	
	public abstract IFormComponent getContainedComponent(String component_id);
	
	public abstract void setFormDocumentModified(boolean changed);
	
	public abstract boolean isFormDocumentModified();
	
	public abstract Document getComponentsXml();
	
	public abstract void setComponentsXml(Document xml);
	
	public abstract String getFormId();
	
	public abstract Locale getDefaultLocale();
}