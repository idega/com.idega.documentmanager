package com.idega.documentmanager.business.form.manager;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.idega.documentmanager.business.form.manager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 */
public class HtmlManagerButton extends HtmlManager {
	
	@Override
	protected Element getFormHtmlComponentLocalization(String loc_str) {
		
		Element loc_model = FormManagerUtil.getElementByIdFromDocument(
				form_document.getXformsDocument(), FormManagerUtil.head_tag, FormManagerUtil.data_mod
		);
		Element loc_strings = (Element)loc_model.getElementsByTagName(FormManagerUtil.loc_tag).item(0);
		Element localized_component = (Element)unlocalized_html_component.cloneNode(true);
		
		NodeList descendants = localized_component.getElementsByTagName("input");
		Element input_element = (Element)descendants.item(0);
		String localization_key = input_element.getAttribute("value");
		
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
			}
			
			if(localized_string == null)
				throw new NullPointerException(
						"Could not find localization value by provided key= "+localization_key+", language= "+loc_str);
			
			input_element.setAttribute("value", localized_string);
		}
		
		return localized_component;
	}
}