package com.idega.documentmanager.util;

import java.io.IOException;
import java.io.StringWriter;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.chiba.xml.dom.DOMUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.idega.documentmanager.component.beans.LocalizedStringBean;
import com.idega.documentmanager.component.datatypes.ComponentType;
import com.idega.util.CoreConstants;
import com.idega.util.xml.XPathUtil;
import com.idega.util.xml.XmlUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.22 $
 *
 * Last modified: $Date: 2008/06/05 08:37:51 $ by $Author: arunas $
 */
public class FormManagerUtil {
	
	public static final String model_tag = "xf:model";
	public static final String label_tag = "xf:label";
	public static final String alert_tag = "xf:alert";
	public static final String help_tag = "xf:help";
	public static final String head_tag = "head";
	public static final String id_att = "id";
	public static final String at_att = "at";
	public static final String type_att = "type";
	public static final String slash = "/";
	public static final String fb_ = "fb_";
	public static final String loc_ref_part1 = "instance('localized_strings')/";
	public static final String loc_ref_part2 = "[@lang=instance('localized_strings')/current_language]";
	public static final String inst_start = "instance('";
	public static final String inst_end = "')";
	public static final String data_mod = "data_model";
	public static final String loc_tag = "localized_strings";
	public static final String output_tag = "xf:output";
	public static final String ref_s_att = "ref";
	public static final String lang_att = "lang";
	public static final String CTID = "fbc_";
	public static final String localized_entries = "localizedEntries";
	public static final String body_tag = "body";
	public static final String bind_att = "bind";
	public static final String bind_tag = "xf:bind";
	public static final String name_att = "name";
	public static final String schema_tag = "xs:schema";
	public static final String form_id = "form_id";
	public static final String title_tag = "title";
	public static final String nodeset_att = "nodeset";
	public static final String switch_tag = "xf:switch";
	public static final String group_tag = "xf:group";
	public static final String case_tag = "xf:case";
	public static final String submit_tag = "xf:submit";
	public static final String itemset_tag = "xf:itemset";
	public static final String item_tag = "xf:item";
	public static final String model_att = "model";
	public static final String src_att = "src";
	public static final String context_att_pref = "context:";
	public static final String item_label_tag = "itemLabel";
	public static final String item_value_tag = "itemValue";
	public static final String localized_entries_tag = "localizedEntries";
	public static final String default_language_tag = "default_language";
	public static final String current_language_tag = "current_language";
	public static final String form_id_tag = "form_id";
	public static final String submission_tag = "xf:submission";
	public static final String page_tag = "page";
	public static final String toggle_tag = "xf:toggle";
	public static final String number_att = "number";
	public static final String case_att = "case";
	public static final String p3ptype_att = "p3ptype";
	public static final String instance_tag = "xf:instance";
	public static final String setvalue_tag = "xf:setvalue";
	public static final String div_tag = "div";
	public static final String trigger_tag = "xf:trigger";
	public static final String preview = "preview";
	public static final String component_tag = "component";
	public static final String component_id_att = "component_id";
	public static final String autofill_model_id = "x-autofill-model";
	public static final String xmlns_att = "xmlns";
	public static final String relevant_att = "relevant";
	public static final String autofill_instance_ending = "_autofill-instance";
	public static final String autofill_setvalue_ending = "-autofill-setvalue";
	public static final String value_att = "value";
	public static final String autofill_key_prefix = "fb-afk-";
	public static final String refresh_tag = "xf:refresh";
	public static final String sections_visualization_id = "sections_visualization";
	public static final String sections_visualization_instance_id = "sections_visualization_instance";
	public static final String section_item = "section_item";
	public static final String sections_visualization_instance_item = "sections_visualization_instance_item";
	public static final String sections_visualization_item = "sections_visualization_item";
	public static final String set_section_vis_cur = "_set_section_vis_cur";
	public static final String set_section_vis_rel = "_set_section_vis_rel";
	public static final String event_att = "ev:event";
	public static final String DOMActivate_att_val = "DOMActivate";
	public static final String xforms_namespace_uri = "http://www.w3.org/2002/xforms";
	public static final String event_namespace_uri = "http://www.w3.org/2001/xml-events";
	public static final String mapping_att = "mapping";
	public static final String action_att = "action";
	public static final String required_att = "required";
	public static final String readonly_att = "readonly";
	public static final String xpath_true = "true()";
	public static final String xpath_false = "false()";
	public static final String datatype_tag = "datatype";
	public static final String accessSupport_att = "accessSupport";
	public static final String submission_model = "submission_model";
	
	private static final String line_sep = "line.separator";
	private static final String xml_mediatype = "text/html";
	private static final String utf_8_encoding = "UTF-8";
	
	private static OutputFormat output_format;
	
	private static XPathUtil formInstanceModelElementXPath;
	private static XPathUtil defaultFormModelElementXPath;
	private static XPathUtil formModelElementXPath;
	private static XPathUtil formIdElementXPath;
	private static XPathUtil formSubmissionInstanceElementXPath;
	private static XPathUtil parentElementXPath;
	private static XPathUtil instanceElementXPath;
	private static XPathUtil submissionElementXPath;
	private static XPathUtil formTitleOutputElementXPath;
	private static XPathUtil instanceElementByIdXPath;
	private static XPathUtil formSubmissionInstanceDataElementXPath;
	private static XPathUtil localizedStringElementXPath;
	private static XPathUtil elementByIdXPath;
	private static XPathUtil elementsContainingAttributeXPath;
	private static XPathUtil localizaionSetValueElement;
	private static XPathUtil formErrorMessageXPath;
	
	private final static String elementNameVariable = "elementName";
	private final static String attributeNameVariable = "attributeName";
	
	private FormManagerUtil() { }
	
	/**
	 * 
	 * @param doc - document, to search for an element
	 * @param start_tag - where to start. Could be just null, then document root element is taken.
	 * @param id_value
	 * @return - <b>reference</b> to element in document
	 */
	public static Element getElementByIdFromDocument(Document doc, String start_tag, String id_value) {
		
		return getElementByAttributeFromDocument(doc, start_tag, id_att, id_value);
	}
	
	/**
	 * 
	 * @param doc - document, to search for an element
	 * @param start_tag - where to start. Could be just null, then document root element is taken.
	 * @param attribute_name - what name attribute should be searched for
	 * @param attribute_value
	 * @return - Reference to element in document
	 */
	public static Element getElementByAttributeFromDocument(Document doc, String start_tag, String attribute_name, String attribute_value) {
		
		Element start_element;
		
		if(start_tag != null)
			start_element = (Element)doc.getElementsByTagName(start_tag).item(0);
		else
			start_element = doc.getDocumentElement();
		
		return DOMUtil.getElementByAttributeValue(start_element, CoreConstants.STAR, attribute_name, attribute_value);
	}
	
	public static void insertNodesetElement(Document form_xforms, Element new_nodeset_element) {
		
		Element container = 
			(Element)((Element)form_xforms
					.getElementsByTagName(instance_tag).item(0))
					.getElementsByTagName("data").item(0);
		container.appendChild(new_nodeset_element);
	}
	
	/**
	 * Puts localized text on element. Localization is saved on the xforms document.
	 * 
	 * @param key - new localization message key
	 * @param oldKey - old key, if provided, is used for replacing with new_key
	 * @param element - element, to change or put localization message
	 * @param xform - xforms document
	 * @param localizedStr - localized message
	 * @throws NullPointerException - something necessary not provided
	 */
	public static void putLocalizedText(String key, String oldKey, Element element, Document xform, LocalizedStringBean localizedStr) throws NullPointerException {
		
		String ref = element.getAttribute(ref_s_att);
		
		if(FormManagerUtil.isEmpty(ref) && FormManagerUtil.isEmpty(key))
			throw new NullPointerException("Localization to element not initialized and key for new localization string not presented.");
		
		if(key != null) {
//			creating new key
			
			ref = new StringBuffer(loc_ref_part1)
			.append(key)
			.append(loc_ref_part2)
			.toString();
			
			element.setAttribute(ref_s_att, ref);
			
		} else if(isRefFormCorrect(ref)) {
//			get key from ref
			key = getKeyFromRef(ref);
			
		} else
			throw new NullPointerException("Ref and key not specified or ref has incorrect format. Ref: "+ref);
		
		if(!data_mod.equals(element.getAttribute(model_att)))
			element.setAttribute(model_att, data_mod);
		
		Element localizationStringsElement = FormManagerUtil.getLocalizedStringElement(xform);
		
		if(oldKey != null) {
			
			NodeList oldLocalizationTags = localizationStringsElement.getElementsByTagName(oldKey);
			
//			find and rename those elements
			for (int i = 0; i < oldLocalizationTags.getLength(); i++) {
				
				Element localizationTag = (Element)oldLocalizationTags.item(i);
				xform.renameNode(localizationTag, localizationTag.getNamespaceURI(), key);
			}
		}
		
		NodeList localizationTags = localizationStringsElement.getElementsByTagName(key);
		
		List<String> locales = new ArrayList<String>();
		
		for (Locale locale : localizedStr.getLanguagesKeySet())
			locales.add(locale.getLanguage());
		
		
//		removing elements that correspond to locale, which we don't need (anymore)
		List<Node> nodesToRemove = new ArrayList<Node>();
		
		for (int i = 0; i < localizationTags.getLength(); i++) {
			
			Element localizationTag = (Element)localizationTags.item(i);
			
			if(!locales.contains(localizationTag.getAttribute(lang_att)))
				nodesToRemove.add(localizationTag);
		}
		
		for (Node node : nodesToRemove)
			node.getParentNode().removeChild(node);
		
		localizationTags = localizationStringsElement.getElementsByTagName(key);
		
		for (Locale locale : localizedStr.getLanguagesKeySet()) {
			
			boolean valueSet = false;
			
			if(localizationTags != null) {
				
				for (int i = 0; i < localizationTags.getLength(); i++) {
					
					Element localizationTag = (Element)localizationTags.item(i);
					
					if(localizationTag.getAttribute(lang_att).equals(locale.getLanguage())) {
						
						if(localizedStr.getString(locale) != null)
							setElementsTextNodeValue(localizationTag, localizedStr.getString(locale));
						
						valueSet = true;
						break;
					}
				}
			}
			
			if(localizationTags == null || !valueSet) {
				
//				create new localization element
				Element localizationElement = xform.createElement(key);
				localizationElement.setAttribute(lang_att, locale.getLanguage());
				localizationElement.setTextContent(localizedStr.getString(locale) == null ? CoreConstants.EMPTY : localizedStr.getString(locale));
				localizationStringsElement.appendChild(localizationElement);
			}
		}
	}
	
	public static String getComponentLocalizationKey(String componentId, String localizationKey) {
		
		if(!isLocalizationKeyCorrect(localizationKey))
			return null;
		
		return new StringBuilder(componentId)
		.append(localizationKey.contains(CoreConstants.DOT) ? localizationKey.substring(localizationKey.lastIndexOf(CoreConstants.DOT)) : CoreConstants.EMPTY)
		.toString();
	}
	
	public static String getKeyFromRef(String ref) {
		return ref.substring(ref.indexOf(slash)+1, ref.indexOf("["));
	}
	
	public static boolean isRefFormCorrect(String ref) {

		return ref != null && ref.startsWith(loc_ref_part1) && ref.endsWith(loc_ref_part2) && !ref.contains(CoreConstants.SPACE); 
	}
	
	public static LocalizedStringBean getLocalizedStrings(String key, Document xforms_doc) {

		Element loc_model = getElementByIdFromDocument(xforms_doc, head_tag, data_mod);
		Element loc_strings = (Element)loc_model.getElementsByTagName(loc_tag).item(0);
		
		NodeList key_elements = loc_strings.getElementsByTagName(key);
		LocalizedStringBean loc_str_bean = new LocalizedStringBean();
		
		for (int i = 0; i < key_elements.getLength(); i++) {
			
			Element key_element = (Element)key_elements.item(i);
			
			String lang_code = key_element.getAttribute("lang");
			
			if(lang_code != null) {
				
				String content = getElementsTextNodeValue(key_element);
				loc_str_bean.setString(new Locale(lang_code), content == null ? "" : content);
			}
		}
		
		return loc_str_bean;
	}
	
	public static LocalizedStringBean getLabelLocalizedStrings(Element component, Document xforms_doc) {
		
		NodeList labels = component.getElementsByTagName(FormManagerUtil.label_tag);
		
		if(labels == null || labels.getLength() == 0)
			return new LocalizedStringBean();
		
		Element label = (Element)labels.item(0);
		
		return getElementLocalizedStrings(label, xforms_doc);
	}
	
	public static LocalizedStringBean getElementLocalizedStrings(Element element, Document xforms_doc) {
		
		String ref = element.getAttribute(FormManagerUtil.ref_s_att);
		
		if(!isRefFormCorrect(ref))
			return new LocalizedStringBean();
		
		String key = getKeyFromRef(ref);
		
		return getLocalizedStrings(key, xforms_doc);
	}
	
	public static void clearLocalizedMessagesFromDocument(Document doc) {
		
		Element loc_model = getElementByIdFromDocument(doc, head_tag, data_mod);
		Element loc_strings = (Element)loc_model.getElementsByTagName(loc_tag).item(0);
		List<Element> loc_elements = DOMUtil.getChildElements(loc_strings);
		
		for (Iterator<Element> iter = loc_elements.iterator(); iter.hasNext();)
			FormManagerUtil.removeTextNodes(iter.next());
	}
	
	public static Locale getDefaultFormLocale(Document form_xforms) {
		
		Element loc_model = getElementByIdFromDocument(form_xforms, head_tag, data_mod);
		Element loc_strings = (Element)loc_model.getElementsByTagName(loc_tag).item(0);
		NodeList default_language_node_list = loc_strings.getElementsByTagName(default_language_tag);
		
		String lang = null;
		if(default_language_node_list != null && default_language_node_list.getLength() != 0) {
			lang = getElementsTextNodeValue(default_language_node_list.item(0));
		}		
		if(lang == null)
			lang = "en";			
		
		return new Locale(lang);
	}
	
	public static void setCurrentFormLocale(Document form_xforms, Locale locale) {
		Element loc_model = getElementByIdFromDocument(form_xforms, head_tag, data_mod);
		Element loc_strings = (Element)loc_model.getElementsByTagName(loc_tag).item(0);
		NodeList current_language_node_list = loc_strings.getElementsByTagName(current_language_tag);
		
		if(current_language_node_list != null && current_language_node_list.getLength() != 0) {
			String localeStr = locale.toString().toLowerCase();
			setElementsTextNodeValue(current_language_node_list.item(0), localeStr);
		}		
	}
	
	public static LocalizedStringBean getErrorLabelLocalizedStrings(Element component, Document xforms_doc) {
		
		NodeList alerts = component.getElementsByTagName(FormManagerUtil.alert_tag);
		
		if(alerts == null || alerts.getLength() == 0)
			return new LocalizedStringBean();
		
		Element output = (Element)((Element)alerts.item(0)).getElementsByTagName(FormManagerUtil.output_tag).item(0);
		
		String ref = output.getAttribute(ref_s_att);
		
		if(!isRefFormCorrect(ref))
			return new LocalizedStringBean();
		
		String key = getKeyFromRef(ref);
		
		return getLocalizedStrings(key, xforms_doc);
	}
	
	public static LocalizedStringBean getHelpTextLocalizedStrings(Element component, Document xforms_doc) {
		
		NodeList helps = component.getElementsByTagName(FormManagerUtil.help_tag);
		
		if(helps == null || helps.getLength() == 0)
			return new LocalizedStringBean();
		
		Element output = (Element)((Element)helps.item(0)).getElementsByTagName(FormManagerUtil.output_tag).item(0);
		
		String ref = output.getAttribute(ref_s_att);
		
		if(!isRefFormCorrect(ref))
			return new LocalizedStringBean();
		
		String key = getKeyFromRef(ref);
		
		return getLocalizedStrings(key, xforms_doc);
	}
	
	public static boolean isLocalizationKeyCorrect(String loc_key) {
		return !isEmpty(loc_key) && !loc_key.contains(CoreConstants.SPACE);
	}
	
	public static String getElementsTextNodeValue(Node element) {
		
		NodeList children = element.getChildNodes();
		StringBuffer text_value = new StringBuffer();
		
		for (int i = 0; i < children.getLength(); i++) {
			
			Node child = children.item(i);
			
			if(child != null && child.getNodeType() == Node.TEXT_NODE) {
				String node_value = child.getNodeValue();
				
				if(node_value != null && node_value.length() > 0)
					text_value.append(node_value);
			}
		}
		
		return text_value.toString();
	}
	
	public static void setElementsTextNodeValue(Node element, String value) {
		
		NodeList children = element.getChildNodes();
		List<Node> childs_to_remove = new ArrayList<Node>();
		
		for (int i = 0; i < children.getLength(); i++) {
			
			Node child = children.item(i);
			
			if(child != null && child.getNodeType() == Node.TEXT_NODE)
				childs_to_remove.add(child);
		}
		
		for (Iterator<Node> iter = childs_to_remove.iterator(); iter.hasNext();)
			element.removeChild(iter.next());
		
		Node text_node = element.getOwnerDocument().createTextNode(value);
		element.appendChild(text_node);
	}
	
	/**
	 * <p>
	 * @param components_xml - components xml document, which passes the structure described:
	 * <p>
	 * optional document root name - form_components
	 * </p>
	 * <p>
	 * Component is encapsulated into div tag, which contains tag id as component type.
	 * Every component div container is child of root.
	 * </p>
	 * <p>
	 * Component type starts with "fbc_"
	 * </p>
	 * <p>
	 * example:
	 * </p>
	 * <p>
	 * &lt;form_components&gt;<br />
		&lt;div class="input" id="fbc_text"&gt;<br />
			&lt;label class="label" for="fbc_text-value" id="fbc_text-label"&gt;			Single Line Field		&lt;/label&gt;<br />
			&lt;input class="value" id="fbc_text-value" name="d_fbc_text"	type="text" value="" /&gt;<br />
		&lt;/div&gt;<br />
	&lt;/form_components&gt;
	 * </p>
	 * </p>
	 * 
	 * IMPORTANT: types should be unique
	 * 
	 * @return List of components types (Strings)
	 */
	public static List<String> gatherAvailableComponentsTypes(Document components_xml) {
		
		Element root = components_xml.getDocumentElement();
		
		if(!root.hasChildNodes())
			return null;
		
		NodeList children = root.getChildNodes();
		List<String> components_types = new ArrayList<String>();
		
		for (int i = 0; i < children.getLength(); i++) {
			
			Node child = children.item(i);
			
			if(child.getNodeType() == Node.ELEMENT_NODE) {
				
				String element_id = ((Element)child).getAttribute(FormManagerUtil.id_att);
				
				if(element_id != null && 
						element_id.startsWith(FormManagerUtil.CTID) &&
						!components_types.contains(element_id)
				)
					components_types.add(element_id);
			}
		}
		
		return components_types;
	}
	
	public static Element getItemElementById(Document item_doc, String item_id) {
		
		Element item = FormManagerUtil.getElementByIdFromDocument(item_doc, head_tag, item_id);
		if(item == null)
			return null;
		
		return DOMUtil.getFirstChildElement(item);
	}
	
	public static void removeTextNodes(Node node) {
		
		NodeList children = node.getChildNodes();
		List<Node> childs_to_remove = new ArrayList<Node>();
		
		for (int i = 0; i < children.getLength(); i++) {
			
			Node child = children.item(i);
			
			if(child.getNodeType() == Node.TEXT_NODE) {
				
				childs_to_remove.add(child);
				
			} else {
				
				if(child.hasChildNodes())
					removeTextNodes(child);
			}
		}
		
		for (Iterator<Node> iter = childs_to_remove.iterator(); iter.hasNext();) {
			node.removeChild(iter.next());
		}
	}
	
	public static List<String[]> getComponentsTagNamesAndIds(Document xforms_doc) {
		
		Element body_element = (Element)xforms_doc.getElementsByTagName(body_tag).item(0);
		Element switch_element = (Element)body_element.getElementsByTagName(switch_tag).item(0);
		
		List<Element> components_elements = DOMUtil.getChildElements(switch_element);
		List<String[]> components_tag_names_and_ids = new ArrayList<String[]>();
		
		for (Iterator<Element> iter = components_elements.iterator(); iter.hasNext();) {
			
			Element component_element = iter.next();
			String[] tag_name_and_id = new String[2];
			tag_name_and_id[0] = component_element.getTagName();
			tag_name_and_id[1] = component_element.getAttribute(id_att);
			
			components_tag_names_and_ids.add(tag_name_and_id);
		}
		
		return components_tag_names_and_ids;
	}
	
	private static OutputFormat getOutputFormat() {
		
		if(output_format == null) {
			
			OutputFormat output_format = new OutputFormat();
			output_format.setOmitXMLDeclaration(true);
			output_format.setLineSeparator(System.getProperty(line_sep));
			output_format.setIndent(4);
			output_format.setIndenting(true);
			output_format.setMediaType(xml_mediatype);
			output_format.setEncoding(utf_8_encoding);
			FormManagerUtil.output_format = output_format;
		}
		
		return output_format;
	}
	
	public static String serializeDocument(Document document) throws IOException {
		
		StringWriter writer = new StringWriter();
		XMLSerializer serializer = new XMLSerializer(writer, getOutputFormat());
		serializer.asDOMSerializer();
		serializer.serialize(document.getDocumentElement());
		
		return writer.toString();
	}
	
	private static Pattern non_xml_pattern; 
	
	public static String escapeNonXmlTagSymbols(String string) {
		
		StringBuffer result = new StringBuffer();

	    StringCharacterIterator iterator = new StringCharacterIterator(string);
	    
	    Character character =  iterator.current();
	    
	    if(non_xml_pattern == null) {
	    	synchronized (FormManagerUtil.class) {
				
	    		if(non_xml_pattern == null) {
	    			non_xml_pattern = Pattern.compile("[a-zA-Z0-9{-}{_}]");
	    		}
			}
	    }
	    
	    while (character != CharacterIterator.DONE ) {
	    	
	    	if(non_xml_pattern.matcher(character.toString()).matches())
	    		result.append(character);
	        
	        character = iterator.next();
	    }
	    
	    return result.toString();
	}
		
	public static int parseIdNumber(String id) {
		
		if(id == null)
			return 0;
		
		return Integer.parseInt(id.substring(CTID.length()));
	}
	
	public static Element getComponentsContainerElement(Document xform) {

		Element bodyElement = (Element)xform.getElementsByTagName(body_tag).item(0);
		return (Element)bodyElement.getElementsByTagName(switch_tag).item(0);
	}
	
	public static boolean isEmpty(String str) {
		return str == null || CoreConstants.EMPTY.equals(str);
	}
	
	public static void modifyFormForLocalisationInFormbuilder(Document xforms_doc) {
		Element setvalue_element = getDataModelSetValueElement(xforms_doc);

		setvalue_element.getParentNode().removeChild(setvalue_element);
	}
		
	
	public static void modifyXFormsDocumentForViewing(Document xforms_doc) {
		
		NodeList tags = xforms_doc.getElementsByTagName(case_tag);
		Element switch_element = (Element)xforms_doc.getElementsByTagName(switch_tag).item(0);
		Element switch_parent = (Element)switch_element.getParentNode();
		
		for (int i = 0; i < tags.getLength(); i++) {
			List<Element> case_children = DOMUtil.getChildElements(tags.item(i));
			for (Element case_child : case_children) {
				switch_parent.appendChild(case_child);
			}
		}
		
		switch_element.getParentNode().removeChild(switch_element);
	}
	
	public static Map<String, List<String>> getCategorizedComponentsTypes(Document form_components_doc) {
		
		Element instance_element = getElementByIdFromDocument(form_components_doc, head_tag, "component_categories");
		NodeList categories = instance_element.getElementsByTagName("category");
		Map<String, List<String>> categorized_types = new HashMap<String, List<String>>();
		
		for (int i = 0; i < categories.getLength(); i++) {
			
			Element category = (Element)categories.item(i);
			NodeList components = category.getElementsByTagName(component_tag);
			List<String> component_types = new ArrayList<String>();

			for (int j = 0; j < components.getLength(); j++) {
				
				Element component = (Element)components.item(j);
				component_types.add(component.getAttribute(component_id_att));
			}
			
			String category_name = category.getAttribute(name_att);
			categorized_types.put(category_name, component_types);
		}
		
		return categorized_types;
	}
	
	public static Element createAutofillInstance(Document xforms_doc) {
		
		Element inst_el = xforms_doc.createElement(instance_tag);
		inst_el.setAttribute(xmlns_att, "");
		inst_el.setAttribute(relevant_att, xpath_false);
		
		return inst_el;
	}
	
	public static void setFormTitle(Document xformsXmlDoc, LocalizedStringBean formTitle) {
	    
		Element output = getFormTitleOutputElement(xformsXmlDoc);
		putLocalizedText(null, null, output, xformsXmlDoc, formTitle);
	}
	
	public static LocalizedStringBean getFormTitle(Document xformsDoc) {
		
		Element output = getFormTitleOutputElement(xformsDoc);
		return getElementLocalizedStrings(output, xformsDoc);
	}

	public static void setFormErrorMsg(Document xformsXmlDoc, LocalizedStringBean formError) {
		
		Element message = getFormErrorMessageElement(xformsXmlDoc);
		putLocalizedText(null, null, message, xformsXmlDoc, formError);
	}

	public static LocalizedStringBean getFormErrorMsg(Document xformsDoc) {
		
		Element message = getFormErrorMessageElement(xformsDoc);
		return getElementLocalizedStrings(message, xformsDoc);
	}
	
	public static synchronized Element getFormInstanceModelElement(Document context) {
		
		if(formInstanceModelElementXPath == null)
			formInstanceModelElementXPath = new XPathUtil(".//xf:model[xf:instance/@id='data-instance']");
		
		return (Element)formInstanceModelElementXPath.getNode(context);
	}
	
	public static synchronized Element getDefaultFormModelElement(Document context) {
		
		if(defaultFormModelElementXPath == null)
			defaultFormModelElementXPath = new XPathUtil(".//xf:model");
		
		return (Element)defaultFormModelElementXPath.getNode(context);
	}
	
	public static synchronized XPathUtil getFormModelElementByIdXPath() {
		
		if(formModelElementXPath == null)
			formModelElementXPath = new XPathUtil(".//xf:model[@id=$modelId]");
		
		return formModelElementXPath;
	}
	
	private static synchronized Element getFormIdElement(Node context) {
		
		if(formIdElementXPath == null)
			formIdElementXPath = new XPathUtil(".//xf:instance/data/form_id");
		
		return (Element)formIdElementXPath.getNode(context);
	}
	
	public static synchronized Element getSubmissionElement(Node context) {
		
		if(submissionElementXPath == null)
			submissionElementXPath = new XPathUtil(".//xf:submission[@id='submit_data_submission']");
		
		return (Element)submissionElementXPath.getNode(context);
	}
	
	public static synchronized Element getFormSubmissionInstanceElement(Document context) {
		
		if(formSubmissionInstanceElementXPath == null)
			formSubmissionInstanceElementXPath = new XPathUtil(".//xf:instance[@id='data-instance']");
		
		return (Element)formSubmissionInstanceElementXPath.getNode(context);
	}
	
	public static synchronized Element getFormSubmissionInstanceDataElement(Document context) {
		
		if(formSubmissionInstanceDataElementXPath == null)
			formSubmissionInstanceDataElementXPath = new XPathUtil(".//xf:instance[@id='data-instance']/data");
		
		return (Element)formSubmissionInstanceDataElementXPath.getNode(context);
	}
	
	public static synchronized Element getParentElement(Element context) {
		
		if(parentElementXPath == null)
			parentElementXPath = new XPathUtil("..");
		
		return (Element)parentElementXPath.getNode(context);
	}
	
	public static synchronized Element getInstanceElement(Element context) {
		
		if(instanceElementXPath == null)
			instanceElementXPath = new XPathUtil(".//xf:instance");
		
		return (Element)instanceElementXPath.getNode(context);
	}
	
	public static synchronized XPathUtil getInstanceElementByIdXPath() {
		
		if(instanceElementByIdXPath == null)
			instanceElementByIdXPath = new XPathUtil(".//xf:instance[@id=$instanceId]");
		
		return instanceElementByIdXPath;
	}
	
	private static synchronized Element getFormTitleOutputElement(Node context) {
		
		if(formTitleOutputElementXPath == null)
			formTitleOutputElementXPath = new XPathUtil(".//h:title/xf:output");
		
		return (Element)formTitleOutputElementXPath.getNode(context);
	}
	
	private static synchronized Element getFormErrorMessageElement(Node context) {
	    
		if(formErrorMessageXPath == null)
		    formErrorMessageXPath = new XPathUtil(".//xf:action[@id='submission-error']/xf:message");
	   
		
		return (Element)formErrorMessageXPath.getNode(context);
		
	}
	
	public static String getFormId(Document xformsDoc) {
		return getFormIdElement(xformsDoc).getTextContent();
	}
	
	public static void setFormId(Document xformsDoc, String formId) {
		getFormIdElement(xformsDoc).setTextContent(formId);
	}
	
	public static synchronized Element getLocalizedStringElement(Node context) {
		
		if(localizedStringElementXPath == null)
			localizedStringElementXPath = new XPathUtil(".//xf:instance[@id='localized_strings']/localized_strings");
		
		return (Element)localizedStringElementXPath.getNode(context);
	}
	
	public static synchronized Element getElementById(Node context, String id) {
		
		if(elementByIdXPath == null)
			elementByIdXPath = new XPathUtil(".//*[@id=$id]");
		
		elementByIdXPath.clearVariables();
		elementByIdXPath.setVariable(id_att, id);
		
		return (Element)elementByIdXPath.getNode(context);
	}
	
	public static synchronized NodeList getElementsContainingAttribute(Node context, String elementName, String attributeName) {
		
		if(isEmpty(elementName))
			elementName = CoreConstants.STAR;
		
		if(isEmpty(attributeName))
			attributeName = CoreConstants.STAR;
		
		if(elementsContainingAttributeXPath == null)
			elementsContainingAttributeXPath = new XPathUtil(".//*[($elementName = '*' or name(.) = $elementName) and ($attributeName = '*' or attribute::*[name(.) = $attributeName])]");
	
		elementsContainingAttributeXPath.clearVariables();
		elementsContainingAttributeXPath.setVariable(elementNameVariable, elementName);
		elementsContainingAttributeXPath.setVariable(attributeNameVariable, attributeName);
		
		return elementsContainingAttributeXPath.getNodeset(context);
	}
	
	private static synchronized Element getDataModelSetValueElement(Node context) {
		
		if(localizaionSetValueElement == null)
			localizaionSetValueElement = new XPathUtil(".//xf:setvalue[@model='data_model']");
		
		return (Element)localizaionSetValueElement.getNode(context);
	}
	
	public static Map<String, List<ComponentType>> getComponentsTypesByDatatype(Document form_components_doc) {
		
		Element instance_element = getElementByIdFromDocument(form_components_doc, head_tag, "components-datatypes-mappings");
		NodeList list = instance_element.getElementsByTagName("component");
		
		Map<String, List<ComponentType>> types = new HashMap<String, List<ComponentType>>();
		
		for (int i = 0; i < list.getLength(); i++) {
			
			Element component = (Element) list.item(i);
			String componentId = component.getAttribute(component_id_att);
			String accessSupport = component.getAttribute(accessSupport_att);
			ComponentType type = new ComponentType(componentId, accessSupport);
			
			NodeList datatypes = component.getElementsByTagName(datatype_tag);
			
			for (int j = 0; j < datatypes.getLength(); j++) {
				
				Element datatype = (Element)datatypes.item(j);
				String value = datatype.getTextContent();
				
				if(types.containsKey(value)) {
					types.get(value).add(type);
				} else {
					List<ComponentType> newList = new ArrayList<ComponentType>();
					newList.add(type);
					types.put(value, newList);
				}
			}
		}
		
		return types;
		
	}
	
	
	public static void main(String[] args) {
		
		try {
			DocumentBuilder db = XmlUtil.getDocumentBuilder();
			Document doc = db.parse("/Users/civilis/dev/workspace/eplatform-4/com.idega.documentmanager/resources/templates/form-components.xhtml");

			NodeList nodes = getElementsContainingAttribute(doc, null, "nodeset");
			
			System.out.println("xx:"+nodes.getLength());
			//DOMUtil.prettyPrintDOM(nodes.item(0));
			
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}