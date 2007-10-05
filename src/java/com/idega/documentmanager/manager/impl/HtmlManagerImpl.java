package com.idega.documentmanager.manager.impl;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.idega.documentmanager.component.FormComponent;
import com.idega.documentmanager.component.FormDocument;
import com.idega.documentmanager.generator.ComponentsGenerator;
import com.idega.documentmanager.generator.impl.ComponentsGeneratorImpl;
import com.idega.documentmanager.manager.HtmlManager;
import com.idega.documentmanager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2007/10/05 11:42:29 $ by $Author: civilis $
 */
public class HtmlManagerImpl implements HtmlManager {
	
	protected Element unlocalized_html_component;
	protected Map<Locale, Element> localized_html_components;
	protected FormComponent component;
	
	protected CacheManager cache_manager;
	protected FormDocument form_document;
	
	/* (non-Javadoc)
	 * @see com.idega.documentmanager.manager.impl.HtmlManager#getHtmlRepresentation(java.util.Locale)
	 */
	public Element getHtmlRepresentation(Locale locale) throws Exception {
		
		Map<Locale, Element> localized_html_components = getLocalizedHtmlComponents();
		Element localized_element = localized_html_components.get(locale);
		localized_element = null;
		unlocalized_html_component = null;
		
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
	
	/* (non-Javadoc)
	 * @see com.idega.documentmanager.manager.impl.HtmlManager#getDefaultHtmlRepresentationByType(java.lang.String)
	 */
	public Element getDefaultHtmlRepresentationByType(String component_type) {
		
		CacheManager cm = CacheManager.getInstance();
		
		Element html_element = cm.getCachedDefaultComponentLocalization(component_type); 
		
		if(html_element != null)
			return html_element;
		
		html_element = FormManagerUtil.getElementByIdFromDocument(cm.getComponentsXml(), null, component_type);
		
		if(html_element == null)
			throw new NullPointerException("Html component was not found by provided type: "+component_type);
		
		Document xforms_doc = cm.getComponentsXforms();
		html_element = getFormHtmlComponentLocalization(FormManagerUtil.getDefaultFormLocale(xforms_doc).getLanguage(), xforms_doc, html_element);
		
		cm.cacheDefaultComponentLocalization(component_type, html_element);
		
		return html_element;
	}
	
	protected Map<Locale, Element> getLocalizedHtmlComponents() {
		
		if(localized_html_components == null)
			localized_html_components = new HashMap<Locale, Element>();
		
		return localized_html_components;
	}
	
	/* (non-Javadoc)
	 * @see com.idega.documentmanager.manager.impl.HtmlManager#clearHtmlComponents()
	 */
	public void clearHtmlComponents() {
		
		getLocalizedHtmlComponents().clear();
		unlocalized_html_component = null;
	}
	
	protected Element getFormHtmlComponentLocalization(String loc_str, Document xforms_doc, Element unlocalized_element) {
		
		Element loc_model = FormManagerUtil.getElementByIdFromDocument(
				xforms_doc, FormManagerUtil.head_tag, FormManagerUtil.data_mod
		);
		Element loc_strings = (Element)loc_model.getElementsByTagName(FormManagerUtil.loc_tag).item(0);
		Element localized_component = (Element)unlocalized_element.cloneNode(true);
		
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
						
						String lang = loc_el.getAttribute(FormManagerUtil.lang_att);
						
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
	
	protected Element getFormHtmlComponentLocalization(String loc_str) {
		
		return getFormHtmlComponentLocalization(loc_str, form_document.getXformsDocument(), unlocalized_html_component);
	}
	
	/* (non-Javadoc)
	 * @see com.idega.documentmanager.manager.impl.HtmlManager#setFormComponent(com.idega.documentmanager.component.FormComponent)
	 */
	public void setFormComponent(FormComponent component) {
		this.component = component;
	}
	
	/* (non-Javadoc)
	 * @see com.idega.documentmanager.manager.impl.HtmlManager#setCacheManager(com.idega.documentmanager.manager.impl.CacheManager)
	 */
	public void setCacheManager(CacheManager cache_manager) {
		this.cache_manager = cache_manager;
	}
	
	protected Document getXFormsDocumentHtmlRepresentation() throws Exception {
		
		Document components_xml = form_document.getComponentsXml();
		
		if(components_xml == null || form_document.isFormDocumentModified() || true) {
			
			ComponentsGenerator components_generator = ComponentsGeneratorImpl.getInstance();
			Document doc_clone = (Document)form_document.getXformsDocument().cloneNode(true);
			FormManagerUtil.modifyXFormsDocumentForViewing(doc_clone);
			
			components_generator.setDocument(doc_clone);
			components_xml = components_generator.generateHtmlComponentsDocument();
			
			form_document.setComponentsXml(components_xml);
			form_document.setFormDocumentModified(false);
		}
		
		return components_xml;
	}
	/* (non-Javadoc)
	 * @see com.idega.documentmanager.manager.impl.HtmlManager#setFormDocument(com.idega.documentmanager.component.FormDocument)
	 */
	public void setFormDocument(FormDocument form_document) {
		this.form_document = form_document;
	}
}