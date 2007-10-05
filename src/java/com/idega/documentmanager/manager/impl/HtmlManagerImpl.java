package com.idega.documentmanager.manager.impl;

import java.util.Locale;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.idega.documentmanager.component.FormComponent;
import com.idega.documentmanager.component.FormDocument;
import com.idega.documentmanager.component.beans.ComponentDataBean;
import com.idega.documentmanager.context.DMContext;
import com.idega.documentmanager.generator.ComponentsGenerator;
import com.idega.documentmanager.generator.impl.ComponentsGeneratorImpl;
import com.idega.documentmanager.manager.HtmlManager;
import com.idega.documentmanager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2007/10/05 12:27:16 $ by $Author: civilis $
 */
public class HtmlManagerImpl implements HtmlManager {
	
	public Element getHtmlRepresentation(DMContext context, Locale locale) throws Exception {
		
		FormComponent component = context.getComponent();
		ComponentDataBean componentDataBean = component.getXformsComponentDataBean();
		
		Map<Locale, Element> localized_html_components = componentDataBean.getLocalizedHtmlComponents();
		Element localized_element = localized_html_components.get(locale);
		
		if(localized_element != null)
			return localized_element;
		
		if(componentDataBean.getUnlocalizedHtmlComponent() == null) {
			
			Element html_component = FormManagerUtil.getElementByIdFromDocument(getXFormsDocumentHtmlRepresentation(component.getContext()), null, component.getId());
			
			if(html_component == null)
				throw new NullPointerException("Component cannot be found in document.");
			
			componentDataBean.setUnlocalizedHtmlComponent(html_component);
		}
		
		localized_element = getFormHtmlComponentLocalization(component.getContext(), locale.getLanguage());
		localized_html_components.put(locale, localized_element);
		
		return localized_element;
	}
	
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
	
	public void clearHtmlComponents(DMContext context) {
		
		ComponentDataBean componentDataBean = context.getComponent().getXformsComponentDataBean();
		componentDataBean.getLocalizedHtmlComponents().clear();
		componentDataBean.setUnlocalizedHtmlComponent(null);
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
	
	protected Element getFormHtmlComponentLocalization(DMContext context, String loc_str) {
		
		FormComponent component = context.getComponent();
		return getFormHtmlComponentLocalization(loc_str, component.getFormDocument().getXformsDocument(), component.getXformsComponentDataBean().getUnlocalizedHtmlComponent());
	}
	
	protected Document getXFormsDocumentHtmlRepresentation(DMContext context) throws Exception {
		
		FormComponent component = context.getComponent();
		FormDocument formDocument = component.getFormDocument();
		
		Document components_xml = formDocument.getComponentsXml();
		
		if(components_xml == null || formDocument.isFormDocumentModified() || true) {
			
			ComponentsGenerator components_generator = ComponentsGeneratorImpl.getInstance();
			Document doc_clone = (Document)formDocument.getXformsDocument().cloneNode(true);
			FormManagerUtil.modifyXFormsDocumentForViewing(doc_clone);
			
			components_generator.setDocument(doc_clone);
			components_xml = components_generator.generateHtmlComponentsDocument();
			
			formDocument.setComponentsXml(components_xml);
			formDocument.setFormDocumentModified(false);
		}
		
		return components_xml;
	}
}