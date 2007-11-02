package com.idega.documentmanager.util;

import java.io.IOException;
import java.io.StringWriter;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.chiba.xml.dom.DOMUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.idega.documentmanager.component.beans.LocalizedStringBean;
import com.idega.util.xml.XPathUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.8 $
 *
 * Last modified: $Date: 2007/11/02 15:04:56 $ by $Author: civilis $
 */
public class FormManagerUtil {
	
	public static final String model_tag = "xf:model";
	public static final String label_tag = "xf:label";
	public static final String alert_tag = "xf:alert";
	public static final String help_tag = "xf:help";
	public static final String head_tag = "head";
	public static final String id_att = "id";
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
	public static final String CTID = "fbcomp_";
	public static final String loc_key_identifier = "lockey_";
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
	public static final String item_label_tag = "item_label";
	public static final String item_value_tag = "item_value";
	public static final String localized_entries_tag = "localizedEntries";
	public static final String default_language_tag = "default_language";
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
	public static final String xpath_false = "false()";
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
	
	private static final String line_sep = "line.separator";
	private static final String xml_mediatype = "text/html";
	private static final String utf_8_encoding = "UTF-8";
	
	private static OutputFormat output_format;
	
	private static XPathUtil formInstanceModelElementXPath;
	private static XPathUtil formIdElementXPath;
	private static XPathUtil formSubmissionInstanceElementXPath;
	private static XPathUtil submissionElementXPath;
	private static XPathUtil formTitleOutputElementXPath;
	private static DocumentBuilderFactory factory;
	
	private FormManagerUtil() { }
	
	public static DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
		
		DocumentBuilderFactory factory = getDocumentBuilderFactory();
		
		synchronized (factory) {
			return factory.newDocumentBuilder();
		}
	}
	
	private static synchronized DocumentBuilderFactory getDocumentBuilderFactory() {
	
		if(factory == null) {
			factory = DocumentBuilderFactory.newInstance();
		    factory.setNamespaceAware(true);
		    factory.setValidating(false);
		    factory.setAttribute("http://apache.org/xml/properties/dom/document-class-name",
			"org.apache.xerces.dom.DocumentImpl");
		}
		return factory;
	}
	
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
		
		return DOMUtil.getElementByAttributeValue(start_element, "*", attribute_name, attribute_value);
	}
	
	public static void insertNodesetElement(Document form_xforms, Element nodeset, Element new_nodeset_element) {
		
		Element container = 
			(Element)((Element)form_xforms
					.getElementsByTagName(instance_tag).item(0))
					.getElementsByTagName("data").item(0);
		container.appendChild(new_nodeset_element);
	}
	
	/**
	 * Puts localized text on element. Localization is saved on the xforms document.
	 * 
	 * @param new_key - new localization message key
	 * @param old_key - old key, if provided, is used for replacing with new_key
	 * @param element - element, to change or put localization message
	 * @param xforms - xforms document
	 * @param loc_string - localized message
	 * @throws NullPointerException - something necessary not provided
	 */
	public static void putLocalizedText(String new_key, String old_key, Element element, Document xforms, LocalizedStringBean loc_string) throws NullPointerException {
		
		if(xforms == null)
			throw new NullPointerException("XForms document not provided");
		
		String ref = element.getAttribute(ref_s_att);
		
		if(ref == null || new_key != null) {
			
			if(new_key == null)
				throw new NullPointerException("Localization to element not initialized and key for new localization string not presented");
			
//			add key to ref
			ref = new StringBuffer(loc_ref_part1)
			.append(new_key)
			.append(loc_ref_part2)
			.toString();
			
			element.setAttribute(ref_s_att, ref);
			
		} else if(ref != null && isRefFormCorrect(ref)) {
//			get key from ref
			
			new_key = getKeyFromRef(ref);
			
		} else
			throw new NullPointerException("Ref and key not specified or ref has incorrect format");
		
		Element loc_model = getElementByIdFromDocument(xforms, head_tag, data_mod);
		
		Element loc_strings = (Element)loc_model.getElementsByTagName(loc_tag).item(0);
		
		if(old_key != null) {
			
			NodeList loc_tags = loc_strings.getElementsByTagName(old_key);
			
//			find and rename those elements
			
			for (int i = 0; i < loc_tags.getLength(); i++) {
				
				Element loc_tag = (Element)loc_tags.item(i);
				xforms.renameNode(loc_tag, loc_tag.getNamespaceURI(), new_key);
			}
		}
		
		NodeList loc_tags = loc_strings.getElementsByTagName(new_key);
		
		Collection<Locale> lang_key_set = loc_string.getLanguagesKeySet();
		
		Collection<String> lang_strings = new ArrayList<String>();
		
		for (Iterator<Locale> iter = lang_key_set.iterator(); iter.hasNext();) {
			
			lang_strings.add(iter.next().getLanguage());
		}
		
		for (int i = 0; i < loc_tags.getLength(); i++) {
			
			Element loc_tag = (Element)loc_tags.item(i);
			
			if(!lang_strings.contains(loc_tag.getAttribute(lang_att))) {
				
				loc_strings.removeChild(loc_tag);
			}
		}
		lang_strings = null;
		
		for (Iterator<Locale> iter = lang_key_set.iterator(); iter.hasNext();) {
			Locale locale = iter.next();
			
			boolean val_set = false;
			
			if(loc_tags != null) {
				
				for (int i = 0; i < loc_tags.getLength(); i++) {
					
					Element loc_tag = (Element)loc_tags.item(i);
					
					if(loc_tag.getAttribute(lang_att).equals(locale.getLanguage())) {
						
						if(loc_string.getString(locale) != null)
							setElementsTextNodeValue(loc_tag, loc_string.getString(locale));
						
						val_set = true;
						break;
					}
				}
			}
			
			if(loc_tags == null || !val_set) {
				
//				create new localization element
				Element new_loc_el = xforms.createElement(new_key);
				new_loc_el.setAttribute(lang_att, locale.getLanguage());
				new_loc_el.appendChild(xforms.createTextNode(""));
				setElementsTextNodeValue(new_loc_el, loc_string.getString(locale) == null ? "" : loc_string.getString(locale));
				loc_strings.appendChild(new_loc_el);
			}
		}
	}
	
	public static String getComponentLocalizationKey(String component_id, String loc_key) {
		
		if(!isLocalizationKeyCorrect(loc_key))
			return null;
		
		String new_loc_key = new StringBuffer(loc_key_identifier)
		.append(component_id)
		.append(loc_key.substring(loc_key_identifier.length()))
		.toString();
		
		return new_loc_key;
	}
	
	public static String getKeyFromRef(String ref) {
		return ref.substring(ref.indexOf(slash)+1, ref.indexOf("["));
	}
	
	public static boolean isRefFormCorrect(String ref) {

		return ref != null && ref.startsWith(loc_ref_part1) && ref.endsWith(loc_ref_part2) && !ref.contains(" "); 
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
		@SuppressWarnings("unchecked")
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
		return loc_key != null && !loc_key.contains(" ") && loc_key.startsWith(loc_key_identifier);
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
	 * Component type starts with "fbcomp_"
	 * </p>
	 * <p>
	 * example:
	 * </p>
	 * <p>
	 * &lt;form_components&gt;<br />
		&lt;div class="input" id="fbcomp_text"&gt;<br />
			&lt;label class="label" for="fbcomp_text-value" id="fbcomp_text-label"&gt;			Single Line Field		&lt;/label&gt;<br />
			&lt;input class="value" id="fbcomp_text-value" name="d_fbcomp_text"	type="text" value="" /&gt;<br />
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
		
		@SuppressWarnings("unchecked")
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
	
	public static Element getComponentsContainerElement(Document xforms_doc) {

		Element body_element = (Element)xforms_doc.getElementsByTagName(body_tag).item(0);
		return (Element)body_element.getElementsByTagName(switch_tag).item(0);
	}
	
	public static boolean isEmpty(String str) {
		return str == null || str.equals("");
	}
	
	
	public static void modifyXFormsDocumentForViewing(Document xforms_doc) {
		
		NodeList tags = xforms_doc.getElementsByTagName(case_tag);
		Element switch_element = (Element)xforms_doc.getElementsByTagName(switch_tag).item(0);
		Element switch_parent = (Element)switch_element.getParentNode();
		
		for (int i = 0; i < tags.getLength(); i++) {
			
			@SuppressWarnings("unchecked")
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
	
	public static synchronized Element getFormInstanceModelElement(Document context) {
		
		if(formInstanceModelElementXPath == null)
			formInstanceModelElementXPath = new XPathUtil(".//xf:model[xf:instance/@id='data-instance']");
		
		return (Element)formInstanceModelElementXPath.getNode(context);
	}
	
	private static synchronized Element getFormIdElement(Node context) {
		
		if(formIdElementXPath == null)
			formIdElementXPath = new XPathUtil(".//xf:instance/data/form_id");
		
		return (Element)formIdElementXPath.getNode(context);
	}
	
	public static synchronized Element getSubmissionElement(Document context) {
		
		if(submissionElementXPath == null)
			submissionElementXPath = new XPathUtil(".//xf:submission[@id='submit_data_submission']");
		
		return (Element)submissionElementXPath.getNode(context);
	}
	
	public static synchronized Element getFormSubmissionInstanceElement(Document context) {
		
		if(formSubmissionInstanceElementXPath == null)
			formSubmissionInstanceElementXPath = new XPathUtil(".//xf:instance[@id='data-instance']");
		
		return (Element)formSubmissionInstanceElementXPath.getNode(context);
	}
	
	private static synchronized Element getFormTitleOutputElement(Node context) {
		
		if(formTitleOutputElementXPath == null)
			formTitleOutputElementXPath = new XPathUtil(".//h:title/xf:output");
		
		return (Element)formTitleOutputElementXPath.getNode(context);
	}
	
	public static String getFormId(Document xformsDoc) {
		
		Element model = getFormInstanceModelElement(xformsDoc);
		return model.getAttribute(id_att);
	}
	
	public static void setFormId(Document xformsDoc, String formId) {
		
		Element model = getFormInstanceModelElement(xformsDoc);
		Element formIdEl = getFormIdElement(xformsDoc);
		
		model.setAttribute(FormManagerUtil.id_att, formId);
		FormManagerUtil.setElementsTextNodeValue(formIdEl, formId);
	}
	
	public static void main(String[] args) {
		
		try {
			DocumentBuilder db = getDocumentBuilder();
			Document doc = db.parse("/Users/civilis/dev/workspace/eplatform-4/com.idega.documentmanager/resources/templates/form-template.xhtml");
			
			System.out.println("document: ");
			
			setFormId(doc, "XXXasdasdas");
			
			//DOMUtil.prettyPrintDOM(doc);
			
//			System.out.println("title: "+getFormTitle(doc));
//			
//			XPathUtil util = new XPathUtil("//h:title/xf:output");
//			DOMUtil.prettyPrintDOM(util.getNode(doc));

//			xxxx
			
//			nodesetElementXPath = new XPathUtil(".//xf:instance[@id='data-instance']/data/$nodesetPath");
			
			XPathUtil util = new XPathUtil(".//xf:instance[@id='data-instance']/data/child::node()[name(.) = $nodesetPath]");
			util.setVariable("nodesetPath", "form_id");
			Element trigger = (Element)util.getNode(doc);
			
			DOMUtil.prettyPrintDOM(trigger);
			
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}