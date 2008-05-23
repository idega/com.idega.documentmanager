package com.idega.documentmanager.manager.impl;

import java.util.Locale;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.documentmanager.component.FormComponent;
import com.idega.documentmanager.component.FormDocument;
import com.idega.documentmanager.component.beans.ComponentDataBean;
import com.idega.documentmanager.generator.ComponentsGenerator;
import com.idega.documentmanager.generator.impl.ComponentsGeneratorImpl;
import com.idega.documentmanager.manager.HtmlManager;
import com.idega.documentmanager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.7 $
 *
 * Last modified: $Date: 2008/05/23 08:41:20 $ by $Author: anton $
 */
public class HtmlManagerImpl implements HtmlManager {
	
	public Element getHtmlRepresentation(FormComponent component, Locale locale) throws Exception {
		
		ComponentDataBean componentDataBean = component.getXformsComponentDataBean();
		
		Map<Locale, Element> localizedHtmlRepresentations = componentDataBean.getLocalizedHtmlComponents();
		Element localizedRepresentation = localizedHtmlRepresentations.get(locale);
		
		if(localizedRepresentation != null)
			return localizedRepresentation;
		
//		change document locale here
		localizedRepresentation = FormManagerUtil.getElementById(getXFormsDocumentHtmlRepresentation(component, locale), component.getId());
		
		if(localizedRepresentation == null)
			throw new NullPointerException("Component html representation couldn't be found in the form html representation document.");
		
		localizedHtmlRepresentations.put(locale, localizedRepresentation);
		
		return localizedRepresentation;
	}
	
	public void clearHtmlComponents(FormComponent component) {
		component.getXformsComponentDataBean().getLocalizedHtmlComponents().clear();
	}
	
	protected Document getXFormsDocumentHtmlRepresentation(FormComponent component, Locale locale) throws Exception {
		FormDocument formDocument = component.getFormDocument();
		Document componentsXml = formDocument.getComponentsXml(component, locale);
		
//		if(componentsXml == null || formDocument.isFormDocumentModified()) {
//			
//			ComponentsGenerator componentsGenerator = ComponentsGeneratorImpl.getInstance();
//			Document xformClone = (Document)component.getContext().getXformsXmlDoc().cloneNode(true);
//			FormManagerUtil.modifyXFormsDocumentForViewing(xformClone);
//			
//			componentsGenerator.setDocument(xformClone);
//			componentsXml = componentsGenerator.generateHtmlComponentsDocument();
//			
//			formDocument.setComponentsXml(componentsXml);
//			formDocument.setFormDocumentModified(false);
//		}
		
		return componentsXml;
	}
}