package com.idega.documentmanager.component;

import java.util.Locale;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.documentmanager.business.component.Page;
import com.idega.documentmanager.business.component.PageThankYou;
import com.idega.documentmanager.business.component.properties.PropertiesDocument;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 */
public interface FormDocument extends FormComponentContainer {

	public abstract Document getXformsDocument();
	
	public abstract void setFormDocumentModified(boolean changed);
	
	public abstract boolean isFormDocumentModified();
	
	public abstract Document getComponentsXml();
	
	public abstract void setComponentsXml(Document xml);
	
	public abstract String getFormId();

	public abstract Locale getDefaultLocale();
	
	public abstract Page getConfirmationPage();
	
	public abstract PageThankYou getThxPage();
	
	public abstract void registerForLastPage(String register_page_id);
	
	public abstract String generateNewComponentId();
	
	public abstract Element getAutofillModelElement();
	
	public abstract Element getFormDataModelElement();
	
	public abstract Element getSectionsVisualizationInstanceElement();
	
	public abstract PropertiesDocument getProperties();
}