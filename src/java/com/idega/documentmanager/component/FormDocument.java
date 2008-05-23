package com.idega.documentmanager.component;

import java.util.Locale;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.documentmanager.business.component.Page;
import com.idega.documentmanager.business.component.PageThankYou;
import com.idega.documentmanager.business.component.properties.PropertiesDocument;
import com.idega.documentmanager.component.beans.LocalizedStringBean;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.6 $
 *
 * Last modified: $Date: 2008/05/23 08:41:20 $ by $Author: anton $
 */
public interface FormDocument extends FormComponentContainer {

	public abstract void setFormDocumentModified(boolean changed);
	
	public abstract boolean isFormDocumentModified();
	
	public abstract Document getComponentsXml(FormComponent component, Locale locale);
	
	public abstract void setComponentsXml(Document xml);
	
	public abstract Long getFormId();
	
	public abstract Locale getDefaultLocale();
	
	public abstract Page getConfirmationPage();
	
	public abstract PageThankYou getThxPage();
	
	public abstract void registerForLastPage(String register_page_id);
	
	public abstract String generateNewComponentId();
	
	public abstract Element getAutofillModelElement();
	
	public abstract Element getFormDataModelElement();
	
	public abstract Element getSubmissionElement();
	
	public abstract Element getSectionsVisualizationInstanceElement();
	
	public abstract PropertiesDocument getProperties();
	
	public abstract String getFormType();
	
	public abstract LocalizedStringBean getFormTitle();
}