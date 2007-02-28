package com.idega.documentmanager.business.form.manager;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.idega.documentmanager.business.form.beans.IFormComponent;
import com.idega.documentmanager.business.form.beans.IFormComponentContainer;
import com.idega.documentmanager.business.form.beans.IFormComponentDocument;
import com.idega.documentmanager.business.form.manager.generators.FormComponentsGenerator;
import com.idega.documentmanager.business.form.manager.generators.IComponentsGenerator;
import com.idega.documentmanager.business.form.manager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 */
public class HtmlManager {
	
	protected Element unlocalized_html_component;
	protected Map<Locale, Element> localized_html_components;
	protected IFormComponent component;
	
	protected CacheManager cache_manager;
	protected IFormComponentContainer component_parent;
	protected IFormComponentDocument form_document;
	
	public Element getHtmlRepresentation(Locale locale) throws Exception {
		
		Map<Locale, Element> localized_html_components = getLocalizedHtmlComponents();
		Element localized_element = localized_html_components.get(locale);
		
		if(localized_element != null)
			return localized_element;
		
		if(unlocalized_html_component == null) {
			
			Element html_component = FormManagerUtil.getElementByIdFromDocument(getXFormsDocumentHtmlRepresentation(), null, component.getId());
			
			if(html_component == null) {

				throw new NullPointerException("Component cannot be found in document.");
			}
			unlocalized_html_component = html_component;
		}
		
		localized_element = getFormHtmlComponentLocalization(locale.getLanguage());
		localized_html_components.put(locale, localized_element);
		
		return localized_element;
	}
	
	protected Map<Locale, Element> getLocalizedHtmlComponents() {
		
		if(localized_html_components == null)
			localized_html_components = new HashMap<Locale, Element>();
		
		return localized_html_components;
	}
	
	public void clearHtmlComponents() {
		
		getLocalizedHtmlComponents().clear();
		unlocalized_html_component = null;
	}
	
	protected Element getFormHtmlComponentLocalization(String loc_str) {
		
		Element loc_model = FormManagerUtil.getElementByIdFromDocument(
				form_document.getXformsDocument(), FormManagerUtil.head_tag, FormManagerUtil.data_mod
		);
		Element loc_strings = (Element)loc_model.getElementsByTagName(FormManagerUtil.loc_tag).item(0);
		Element localized_component = (Element)unlocalized_html_component.cloneNode(true);
		
		NodeList descendants = localized_component.getElementsByTagName("*");
		
		for (int i = 0; i < descendants.getLength(); i++) {
			
			Node desc = descendants.item(i);
			String localization_key = FormManagerUtil.getElementsTextNodeValue(desc).trim();
			
			if(FormManagerUtil.isLocalizationKeyCorrect(localization_key)) {
				
				NodeList localization_strings_elements = loc_strings.getElementsByTagName(localization_key);
				
				String localized_string = null;
				
				if(localization_strings_elements != null) {
					
					for (int j = 0; j < localization_strings_elements.getLength(); j++) {
						
						Element loc_el = (Element)localization_strings_elements.item(j);
						
						String lang = loc_el.getAttribute("lang");
						
						if(lang != null && lang.equals(loc_str)) {
							
							localized_string = FormManagerUtil.getElementsTextNodeValue(loc_el);
							break;
						}
					}
					if(localized_string == null && localization_strings_elements.getLength() > 0)
						return null;
				}
				
				if(localized_string == null)
					throw new NullPointerException(
							"Could not find localization value by provided key= "+localization_key+", language= "+loc_str);
				
				FormManagerUtil.setElementsTextNodeValue(desc, localized_string);
			}
		}
		
		return localized_component;
	}
	
	public void setFormComponent(IFormComponent component) {
		this.component = component;
	}
	
	public void setCacheManager(CacheManager cache_manager) {
		this.cache_manager = cache_manager;
	}
	
	public void setComponentParent(IFormComponentContainer component_parent) {
		
		this.component_parent = component_parent;
	}
	
	protected Document getXFormsDocumentHtmlRepresentation() throws Exception {
		
		Document components_xml = form_document.getComponentsXml();

		if(components_xml == null || form_document.isFormDocumentModified()) {
			
			IComponentsGenerator components_generator = FormComponentsGenerator.getInstance();
			Document doc_clone = (Document)form_document.getXformsDocument().cloneNode(true);
			FormManagerUtil.modifyXFormsDocumentForViewing(doc_clone);
			components_generator.setDocument(doc_clone);
			components_xml = components_generator.generateBaseComponentsDocument();
			
			form_document.setComponentsXml(components_xml);
			form_document.setFormDocumentModified(false);
		}
		
		return components_xml;
	}
	public void setFormDocument(IFormComponentDocument form_document) {
		this.form_document = form_document;
	}
}