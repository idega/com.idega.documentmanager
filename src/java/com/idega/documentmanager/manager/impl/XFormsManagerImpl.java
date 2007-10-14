package com.idega.documentmanager.manager.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chiba.xml.dom.DOMUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.idega.documentmanager.business.component.properties.PropertiesComponent;
import com.idega.documentmanager.component.FormComponent;
import com.idega.documentmanager.component.FormComponentButtonArea;
import com.idega.documentmanager.component.FormComponentContainer;
import com.idega.documentmanager.component.FormComponentPage;
import com.idega.documentmanager.component.beans.LocalizedStringBean;
import com.idega.documentmanager.component.beans.ComponentDataBean;
import com.idega.documentmanager.component.properties.impl.ConstUpdateType;
import com.idega.documentmanager.manager.XFormsManager;
import com.idega.documentmanager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.6 $
 *
 * Last modified: $Date: 2007/10/14 06:55:13 $ by $Author: civilis $
 */
public class XFormsManagerImpl implements XFormsManager {
	
	private static Log logger = LogFactory.getLog(XFormsManagerImpl.class);
	
	private static final String simple_type = "xs:simpleType";
	private static final String complex_type = "xs:complexType";
	private static final String required_att = "required";
	private static final String true_xpath = "true()";
	
	public void loadXFormsComponentByType(FormComponent component, String componentType) throws NullPointerException {
		
		CacheManager cacheManager = component.getContext().getCacheManager();
		cacheManager.checkForComponentType(componentType);
		
		ComponentDataBean xformsComponentDataBean = cacheManager.getCachedXformsComponent(componentType);
		
		if(xformsComponentDataBean != null) {
			xformsComponentDataBean = (ComponentDataBean)xformsComponentDataBean.clone();
			component.setXformsComponentDataBean(xformsComponentDataBean);
			return;
		}
		
		Document componentsXFormsXml = cacheManager.getComponentsXforms();
		Element componentXFormsElement = FormManagerUtil.getElementByIdFromDocument(componentsXFormsXml, FormManagerUtil.body_tag, componentType);
		
		if(componentXFormsElement == null) {
			String msg = "Component cannot be found in components xforms document by provided type: "+componentType;
			logger.error(msg+
				" Should not happen. Take a look, why component is registered in components_types, but is not present in components xforms document.");
			throw new NullPointerException(msg);
		}
		
		synchronized (XFormsManagerImpl.class) {
			
			xformsComponentDataBean = cacheManager.getCachedXformsComponent(componentType); 

			if(xformsComponentDataBean != null) {
				xformsComponentDataBean = (ComponentDataBean)xformsComponentDataBean.clone();
				component.setXformsComponentDataBean(xformsComponentDataBean);
				return;
			}
			
			loadXFormsComponent(component, componentsXFormsXml, componentXFormsElement);
			cacheManager.cacheXformsComponent(componentType, (ComponentDataBean)component.getXformsComponentDataBean().clone());
		}
	}
	
	public void loadXFormsComponentFromDocument(FormComponent component, String component_id) {
		
		Document xforms_doc = component.getContext().getXformsXmlDoc();
		Element my_element = FormManagerUtil.getElementByIdFromDocument(xforms_doc, FormManagerUtil.body_tag, component_id);
		
		loadXFormsComponent(component, xforms_doc, my_element);
	}
	
	protected ComponentDataBean newXFormsComponentDataBeanInstance() {
		return new ComponentDataBean();
	}
	
	protected void loadXFormsComponent(FormComponent component, Document componentsXForms, Element componentXFormsElement) {
		
		ComponentDataBean xformsComponentDataBean = newXFormsComponentDataBeanInstance();
		xformsComponentDataBean.setElement(componentXFormsElement);
		component.setXformsComponentDataBean(xformsComponentDataBean);
		
		getBindingsAndNodesets(component, componentsXForms);
		getExtKeyElements(component, componentsXForms);
	}
	
	protected void getExtKeyElements(FormComponent component, Document components_xforms) {
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		xformsComponentDataBean.setKeyExtInstance(FormManagerUtil.getElementByIdFromDocument(components_xforms, FormManagerUtil.head_tag, component.getId()+FormManagerUtil.autofill_instance_ending));
		xformsComponentDataBean.setKeySetvalue(FormManagerUtil.getElementByIdFromDocument(components_xforms, FormManagerUtil.head_tag, component.getId()+FormManagerUtil.autofill_setvalue_ending));
	}
	
	protected void setBindingsAndNodesets(FormComponent component) {
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		if(xformsComponentDataBean.getBind() != null) {
			
//			insert bind element
			String componentId = component.getId();
			Document xformsDoc = component.getContext().getXformsXmlDoc();
			
			String bind_id = FormManagerUtil.bind_att+'.'+componentId;
			xformsComponentDataBean.getElement().setAttribute(FormManagerUtil.bind_att, bind_id);
			
			Element element = (Element)xformsDoc.importNode(xformsComponentDataBean.getBind(), true);
			xformsComponentDataBean.setBind(element);
			
			String new_form_schema_type = insertBindElement(component, element, bind_id);
			
			if(new_form_schema_type != null) {

				copySchemaType(component.getContext().getCacheManager().getComponentsXsd(), xformsDoc, new_form_schema_type, componentId+new_form_schema_type);
			}
			insertNodesetElement(component, bind_id);
		}
	}
	
	protected void getBindingsAndNodesets(FormComponent component, Document componentsXForms) {

		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		String bindTo = xformsComponentDataBean.getElement().getAttribute(FormManagerUtil.bind_att);
		
		if(!FormManagerUtil.isEmpty(bindTo)) {
			
//			get binding
			Element binding = 
				FormManagerUtil.getElementByIdFromDocument(componentsXForms, FormManagerUtil.head_tag, bindTo);
			
			if(binding == null)
				throw new NullPointerException("Binding not found by provided bind value: "+bindTo);
			
			xformsComponentDataBean.setBind(binding);
			setNodeset(component, componentsXForms);
		}
	}
	
	protected void setNodeset(FormComponent component, Document components_xforms) {
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
//		get nodeset
		String nodeset_to = xformsComponentDataBean.getBind().getAttribute(FormManagerUtil.nodeset_att);
		
		if(nodeset_to == null)
			return;
		
		String instance_id = null;
		
		if(nodeset_to.contains(FormManagerUtil.inst_start)) {
			instance_id = nodeset_to.substring(
					nodeset_to.indexOf(FormManagerUtil.inst_start)
					+FormManagerUtil.inst_start.length(),
					nodeset_to.indexOf(FormManagerUtil.inst_end)
			);
		}
		
		if(nodeset_to.contains(FormManagerUtil.slash))
			nodeset_to = nodeset_to.substring(0, nodeset_to.indexOf(FormManagerUtil.slash));
		
		Element nodeset = null;
		
		if(instance_id != null) {
			nodeset = (Element)(FormManagerUtil.getElementByIdFromDocument(components_xforms, FormManagerUtil.head_tag, instance_id)).getElementsByTagName(nodeset_to).item(0);
		} else {
			nodeset = (Element)((Element)components_xforms.getElementsByTagName(FormManagerUtil.instance_tag).item(0)).getElementsByTagName(nodeset_to).item(0);
		}
		
		xformsComponentDataBean.setNodeset(nodeset);
	}
	
	public void addComponentToDocument(FormComponent component) {
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		Document xforms_doc = component.getContext().getXformsXmlDoc();
		Element component_element = xformsComponentDataBean.getElement();
		
		component_element = (Element)xforms_doc.importNode(component_element, true);
		
		String component_id = component.getId();
		component_element.setAttribute(FormManagerUtil.id_att, component_id);
		
		localizeComponent(component_id, component_element, xforms_doc, component.getContext().getCacheManager().getComponentsXforms());
		
		if(removeTextNodes())
			FormManagerUtil.removeTextNodes(component_element);
		xformsComponentDataBean.setElement(component_element);
		
		setBindingsAndNodesets(component);
		
		FormComponentContainer parent = component.getParent();
		
		if(component.getComponentAfterThis() == null) {
			
			Element components_container = parent.getXformsComponentDataBean().getElement();
			component_element = (Element)components_container.appendChild(component_element);
			parent.getContainedComponentsIdList().add(component_id);
			
		} else {
			
			Element component_after_me = getInsertBeforeComponentElement(component.getComponentAfterThis());
			component_element = (Element)component_after_me.getParentNode().insertBefore(component_element, component_after_me);
			
			List<String> parent_components_id_list = parent.getContainedComponentsIdList();
			
			String component_after_this_id = component.getComponentAfterThis().getId();
			
			for (int i = 0; i < parent_components_id_list.size(); i++) {
				
				if(parent_components_id_list.get(i).equals(component_after_this_id)) {
					parent_components_id_list.add(i, component_id);
					break;
				}
			}
		}
		xformsComponentDataBean.setElement(component_element);
	}
	
	protected boolean removeTextNodes() {
		return true;
	}
	
	protected Element getInsertBeforeComponentElement(FormComponent component_after_this) {
		
		return component_after_this.getXformsComponentDataBean().getElement();
	}
	
	protected void localizeComponent(String comp_id, Element component_container, Document xforms_doc_to, Document xforms_doc_from) {
		
		NodeList children = component_container.getElementsByTagName("*");
		
		for (int i = 0; i < children.getLength(); i++) {
			
			Element child = (Element)children.item(i);
			
			String ref = child.getAttribute(FormManagerUtil.ref_s_att);
			
			if(FormManagerUtil.isRefFormCorrect(ref)) {
				
				String key = FormManagerUtil.getKeyFromRef(ref);
				FormManagerUtil.putLocalizedText(
					FormManagerUtil.getComponentLocalizationKey(comp_id, key), null, child, xforms_doc_to,
					FormManagerUtil.getLocalizedStrings(key, xforms_doc_from)
				);
			}
		}
	}
	
	/**
	 * <p>
	 * Copies schema type from one schema document to another by provided type name.
	 * </p>
	 * <p>
	 * <b><i>WARNING: </i></b>currently doesn't support cascading types copying,
	 * i.e., when one type depends on another
	 * </p>
	 * 
	 * @param src - schema document to copy from
	 * @param dest - schema document to copy to
	 * @param src_type_name - name of type to copy
	 * @throws NullPointerException - some params were null or such type was not found in src document
	 */
	protected void copySchemaType(Document src, Document dest, String src_type_name, String dest_type_name) throws NullPointerException {
		
		if(src == null || dest == null || src_type_name == null) {
			
			String err_msg = 
			new StringBuilder("\nEither parameter is not provided:")
			.append("\nsrc: ")
			.append(String.valueOf(src))
			.append("\ndest: ")
			.append(String.valueOf(dest))
			.append("\ntype_name: ")
			.append(src_type_name)
			.toString();
			
			throw new NullPointerException(err_msg);
		}
		
		Element root = src.getDocumentElement();
		
//		check among simple types
		
		Element type_to_copy = getSchemaTypeToCopy(root.getElementsByTagName(simple_type), src_type_name);
		
		if(type_to_copy == null) {
//			check among complex types
			
			type_to_copy = getSchemaTypeToCopy(root.getElementsByTagName(complex_type), src_type_name);
		}
		
		if(type_to_copy == null)
			throw new NullPointerException("Schema type was not found by provided name: "+src_type_name);
		
		type_to_copy = (Element)dest.importNode(type_to_copy, true);
		type_to_copy.setAttribute(FormManagerUtil.name_att, dest_type_name);
		
		((Element)dest.getElementsByTagName(FormManagerUtil.schema_tag).item(0)).appendChild(type_to_copy);
	}
	
	private Element getSchemaTypeToCopy(NodeList types, String type_name_required) {
		
		for (int i = 0; i < types.getLength(); i++) {
			
			Element simple_type = (Element)types.item(i); 
			String name_att = simple_type.getAttribute(FormManagerUtil.name_att);
			
			if(name_att != null && name_att.equals(type_name_required))
				return simple_type;
		}
		
		return null;
	}
	
	protected void updateConstraintRequired(FormComponent component) {
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		PropertiesComponent props = component.getProperties();
		Element bind = xformsComponentDataBean.getBind();
		
		if(bind == null) {
			logger.error("Bind element not set in xforms_component data bean. See where component is rendered for cause.");
			throw new NullPointerException("Bind element is not set");
		}
		
		if(props.isRequired())
			
			bind.setAttribute(required_att, true_xpath);
		
		else
			bind.removeAttribute(required_att);
	}
	
	public void update(FormComponent component, ConstUpdateType what) {
		
		switch (what) {
		case LABEL:
			updateLabel(component);
			break;
			
		case ERROR_MSG:
			updateErrorMsg(component);
			break;
			
		case HELP_TEXT:
			updateHelpText(component);
			break;
			
		case CONSTRAINT_REQUIRED:
			updateConstraintRequired(component);
			break;
			
		case P3P_TYPE:
			updateP3pType(component);
			break;
			
		case AUTOFILL_KEY:
			updateAutofillKey(component);
			break;
			
		case VARIABLE_NAME:
			updateVariableName(component);
			break;

		default:
			break;
		}
	}
	
	protected void updateLabel(FormComponent component) {
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		PropertiesComponent props = component.getProperties();
		LocalizedStringBean locStr = props.getLabel();
		
		NodeList labels = xformsComponentDataBean.getElement().getElementsByTagName(FormManagerUtil.label_tag);
		
		if(labels == null || labels.getLength() == 0)
			return;
		
		Element label = (Element)labels.item(0);
		
		FormManagerUtil.putLocalizedText(null, null, 
				label,
				component.getContext().getXformsXmlDoc(),
				locStr
		);
	}
	
	protected void updateErrorMsg(FormComponent component) {
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		PropertiesComponent props = component.getProperties();
		
		Element element = xformsComponentDataBean.getElement();
		NodeList alerts = element.getElementsByTagName(FormManagerUtil.alert_tag);
		
		if(alerts == null || alerts.getLength() == 0) {
			
			Element alert = FormManagerUtil.getItemElementById(component.getContext().getCacheManager().getComponentsXforms(), "alert");
			
			Document xforms_doc = component.getContext().getXformsXmlDoc();
			
			alert = (Element)xforms_doc.importNode(alert, true);
			element.appendChild(alert);
			Element output = (Element)alert.getElementsByTagName(FormManagerUtil.output_tag).item(0);
			
			String new_err_id = new StringBuilder(FormManagerUtil.loc_key_identifier)
			.append(component.getId())
			.append('.')
			.append("error")
			.toString();
			
			FormManagerUtil.putLocalizedText(
					new_err_id, FormManagerUtil.localized_entries, output, xforms_doc, props.getErrorMsg());
		} else {
			
			Element alert = (Element)alerts.item(0);
			Element output = (Element)alert.getElementsByTagName(FormManagerUtil.output_tag).item(0);
			
			FormManagerUtil.putLocalizedText(
					null, null, output, component.getContext().getXformsXmlDoc(), props.getErrorMsg());
		}
	}
	
	protected void updateHelpText(FormComponent component) {
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		PropertiesComponent props = component.getProperties();
		
		Element element = xformsComponentDataBean.getElement();
		NodeList helps = element.getElementsByTagName(FormManagerUtil.help_tag);
		
		if(helps == null || helps.getLength() == 0) {
			
			Element help = FormManagerUtil.getItemElementById(component.getContext().getCacheManager().getComponentsXforms(), "help");
			
			Document xforms_doc = component.getContext().getXformsXmlDoc();
			
			help = (Element)xforms_doc.importNode(help, true);
			element.appendChild(help);
			Element output = (Element)help.getElementsByTagName(FormManagerUtil.output_tag).item(0);
			
			String new_help_id = new StringBuilder(FormManagerUtil.loc_key_identifier)
			.append(component.getId())
			.append('.')
			.append("help")
			.toString();
			
			FormManagerUtil.putLocalizedText(
					new_help_id, FormManagerUtil.localized_entries, output, xforms_doc, props.getHelpText());
		} else {
			
			Element alert = (Element)helps.item(0);
			Element output = (Element)alert.getElementsByTagName(FormManagerUtil.output_tag).item(0);
			
			FormManagerUtil.putLocalizedText(
					null, null, output, component.getContext().getXformsXmlDoc(), props.getHelpText());
		}
	}
	
	public void moveComponent(FormComponent component, String before_component_id) {
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		Document xforms_doc = component.getContext().getXformsXmlDoc();
		
		Element element_to_move = xformsComponentDataBean.getElement();
		Element element_to_insert_before = null;

		if(before_component_id != null) {
			
			element_to_insert_before = FormManagerUtil.getElementByIdFromDocument(xforms_doc, FormManagerUtil.body_tag, before_component_id);
		} else {

			Element components_container = (Element)element_to_move.getParentNode();
			element_to_insert_before = DOMUtil.getLastChildElement(components_container);
		}
		
		xformsComponentDataBean.setElement(
				(Element)((Element)element_to_move.getParentNode()).insertBefore(element_to_move, element_to_insert_before)
		);
		
		changePreviewElementOrder(component);
	}
	
	protected void changePreviewElementOrder(FormComponent component) {
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		Element preview_element = xformsComponentDataBean.getPreviewElement();
		
		if(preview_element == null)
			return;
		
		FormComponent comp_after_this = component.getComponentAfterThis();
		
		if(comp_after_this != null) {
			
			Element comp_after_preview = comp_after_this.getXformsComponentDataBean().getPreviewElement();
			
			if(comp_after_preview == null)
				return;
			
			xformsComponentDataBean.setPreviewElement(
					(Element)comp_after_preview.getParentNode().insertBefore(preview_element, comp_after_preview)
			);
			
		} else {
			FormComponentPage confirmation_page = (FormComponentPage)component.getFormDocument().getConfirmationPage();
			
			if(confirmation_page == null)
				throw new NullPointerException("Confirmation page not found, but preview element exists.");
			
			FormComponentButtonArea button_area = (FormComponentButtonArea)confirmation_page.getButtonArea();
			
			appendPreviewElement(component, confirmation_page.getXformsComponentDataBean().getElement(), 
					button_area == null ? null : button_area.getXformsComponentDataBean().getElement()
			);
		}
	}
	
	public void removeComponentFromXFormsDocument(FormComponent component) {
		
		removeComponentLocalization(component);
		removeComponentBindings(component);
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		Element remove_this = xformsComponentDataBean.getPreviewElement();
		if(remove_this != null)
			remove_this.getParentNode().removeChild(remove_this);
		
		remove_this = xformsComponentDataBean.getKeyExtInstance();
		if(remove_this != null)
			remove_this.getParentNode().removeChild(remove_this);
		
		remove_this = xformsComponentDataBean.getKeySetvalue();
		if(remove_this != null)
			remove_this.getParentNode().removeChild(remove_this);
		
		remove_this = xformsComponentDataBean.getElement();
		if(remove_this != null)
			remove_this.getParentNode().removeChild(remove_this);
	}
	
	protected void removeComponentBindings(FormComponent component) {
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		Document xforms_doc = component.getContext().getXformsXmlDoc();
		Element bind_element = xformsComponentDataBean.getBind();
		
		if(bind_element != null) {
			
			String schema_type_att_value = bind_element.getAttribute(FormManagerUtil.type_att);
			
			if(schema_type_att_value != null && schema_type_att_value.startsWith(component.getId())) {
				
				Element schema_element = (Element)xforms_doc.getElementsByTagName(FormManagerUtil.schema_tag).item(0);
				
				Element type_element_to_remove = DOMUtil.getElementByAttributeValue(schema_element, "*", FormManagerUtil.name_att, schema_type_att_value);
				
				if(type_element_to_remove != null)
					schema_element.removeChild(type_element_to_remove);
			}
			bind_element.getParentNode().removeChild(bind_element);
		}
		Element nodeset = xformsComponentDataBean.getNodeset();
		
		if(nodeset != null)
			nodeset.getParentNode().removeChild(nodeset);
	}
	
	protected void removeComponentLocalization(FormComponent component) {
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		NodeList children = xformsComponentDataBean.getElement().getElementsByTagName("*");
		
		Element loc_model = FormManagerUtil.getElementByIdFromDocument(
				component.getContext().getXformsXmlDoc(), FormManagerUtil.head_tag, FormManagerUtil.data_mod);
		
		Element loc_strings = (Element)loc_model.getElementsByTagName(FormManagerUtil.loc_tag).item(0);
		
		for (int i = 0; i < children.getLength(); i++) {
			
			Element child = (Element)children.item(i);
			
			String ref = child.getAttribute(FormManagerUtil.ref_s_att);
			
			if(FormManagerUtil.isRefFormCorrect(ref)) {
				
				String key = FormManagerUtil.getKeyFromRef(ref);
				
//				those elements should be the child nodes
				NodeList localization_elements = loc_strings.getElementsByTagName(key);
				
				if(localization_elements != null) {
					
					int elements_count = localization_elements.getLength();
					
					for (int j = 0; j < elements_count; j++) {
						
						loc_strings.removeChild(localization_elements.item(0));
					}
				}
			}
		}
	}

	public String insertBindElement(FormComponent component, Element new_bind_element, String bind_id) {
		
		new_bind_element.setAttribute(FormManagerUtil.id_att, bind_id);
	
		Element model = component.getFormDocument().getFormDataModelElement();
		model.appendChild(new_bind_element);
		
		String type_att = new_bind_element.getAttribute(FormManagerUtil.type_att);
		
		if(type_att != null && type_att.startsWith(FormManagerUtil.fb_)) {
			
			new_bind_element.setAttribute(FormManagerUtil.type_att, component.getId()+type_att);
			return type_att;
		}
		return null;
	}
	
	protected void insertNodesetElement(FormComponent component, String bind_id) {
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		if(xformsComponentDataBean.getNodeset() != null) {
			
			Document xforms_doc = component.getContext().getXformsXmlDoc();
//			insert nodeset element
			Element newNodesetElement = (Element)xforms_doc.importNode(xformsComponentDataBean.getNodeset(), true);
			newNodesetElement = (Element)xforms_doc.renameNode(newNodesetElement, newNodesetElement.getNamespaceURI(), bind_id);
			
			FormManagerUtil.insertNodesetElement(
					xforms_doc, xformsComponentDataBean.getNodeset(), newNodesetElement
			);
			
			xformsComponentDataBean.setNodeset(newNodesetElement);
		}
	}
	
	public void changeBindName(FormComponent component, String new_bind_name) {

		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		Element bind_element = xformsComponentDataBean.getBind();
		
		if(bind_element == null)
			return;
		
		new_bind_name = FormManagerUtil.escapeNonXmlTagSymbols(new_bind_name.replace(' ', '_'));
		Element nodeset_element = xformsComponentDataBean.getNodeset();
		String prev_nodeset = bind_element.getAttribute(FormManagerUtil.nodeset_att);
		
		if(prev_nodeset.contains(FormManagerUtil.slash))
			prev_nodeset = prev_nodeset.substring(prev_nodeset.indexOf(FormManagerUtil.slash));
		else
			prev_nodeset = null;
		
		bind_element.setAttribute(FormManagerUtil.nodeset_att, prev_nodeset == null ? new_bind_name : new_bind_name+prev_nodeset);
		nodeset_element = (Element)nodeset_element.getOwnerDocument().renameNode(nodeset_element, nodeset_element.getNamespaceURI(), new_bind_name);
		xformsComponentDataBean.setNodeset(nodeset_element);
		
		if(xformsComponentDataBean.getPreviewElement() != null)
			xformsComponentDataBean.getPreviewElement().setAttribute(
					FormManagerUtil.ref_s_att, bind_element.getAttribute(FormManagerUtil.nodeset_att)
			);
	}
	
	protected void updateP3pType(FormComponent component) {
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		PropertiesComponent props = component.getProperties();
		String p3ptype = props.getP3ptype();
		
		if(p3ptype == null)
			xformsComponentDataBean.getBind().removeAttribute(FormManagerUtil.p3ptype_att);
		else
			xformsComponentDataBean.getBind().setAttribute(FormManagerUtil.p3ptype_att, p3ptype);
	}
	
	protected void updateVariableName(FormComponent component) {
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		if(xformsComponentDataBean.getNodeset() == null)
			return;
		
		PropertiesComponent props = component.getProperties();
		
		if(props.getVariableName() == null)
			xformsComponentDataBean.getNodeset().removeAttribute(FormManagerUtil.mapping_att);
		else
			xformsComponentDataBean.getNodeset().setAttribute(FormManagerUtil.mapping_att, props.getVariableName());
	}
	
	protected void updateAutofillKey(FormComponent component) {
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		PropertiesComponent props = component.getProperties();
		String autofill_key = props.getAutofillKey();
		
		if(autofill_key == null && (xformsComponentDataBean.getKeyExtInstance() != null || xformsComponentDataBean.getKeySetvalue() != null)) {
			
			Element rem_el = xformsComponentDataBean.getKeyExtInstance();
			if(rem_el != null)
				rem_el.getParentNode().removeChild(rem_el);
			xformsComponentDataBean.setKeyExtInstance(null);
			
			rem_el = xformsComponentDataBean.getKeySetvalue();
			if(rem_el != null)
				rem_el.getParentNode().removeChild(rem_el);
			xformsComponentDataBean.setKeySetvalue(null);
			
		} else if(autofill_key != null) {
			
			autofill_key = FormManagerUtil.autofill_key_prefix+autofill_key;
			
			String src = FormManagerUtil.context_att_pref+autofill_key;
			
			if(xformsComponentDataBean.getKeyExtInstance() != null) {
				
				xformsComponentDataBean.getKeyExtInstance().setAttribute(FormManagerUtil.src_att, src);
				
			} else {

				Element model = component.getFormDocument().getFormDataModelElement();
				Element inst_el = model.getOwnerDocument().createElementNS(model.getNamespaceURI(), FormManagerUtil.instance_tag);
				inst_el.setAttribute(FormManagerUtil.relevant_att, FormManagerUtil.xpath_false);
				
				inst_el = (Element)model.appendChild(inst_el);
				inst_el.setAttribute(FormManagerUtil.src_att, src);
				inst_el.setAttribute(FormManagerUtil.id_att, component.getId()+FormManagerUtil.autofill_instance_ending);
				xformsComponentDataBean.setKeyExtInstance(inst_el);
			}
			
			String value = 
				new StringBuilder(FormManagerUtil.inst_start)
				.append(xformsComponentDataBean.getKeyExtInstance().getAttribute(FormManagerUtil.id_att))
				.append(FormManagerUtil.inst_end)
				.append(FormManagerUtil.slash)
				.append(autofill_key)
				.toString();
			
			if(xformsComponentDataBean.getKeySetvalue() != null) {
				
				xformsComponentDataBean.getKeySetvalue().setAttribute(FormManagerUtil.value_att, value);
				
			} else {
				Element autofill_model = component.getFormDocument().getAutofillModelElement();
				Element setval_el = autofill_model.getOwnerDocument().createElementNS(autofill_model.getNamespaceURI(), FormManagerUtil.setvalue_tag);
				setval_el = (Element)autofill_model.appendChild(setval_el);
				setval_el.setAttribute(FormManagerUtil.bind_att, xformsComponentDataBean.getBind().getAttribute(FormManagerUtil.id_att));
				setval_el.setAttribute(FormManagerUtil.value_att, value);
				setval_el.setAttribute(FormManagerUtil.model_att, component.getFormDocument().getFormDataModelElement().getAttribute(FormManagerUtil.id_att));
				setval_el.setAttribute(FormManagerUtil.id_att, component.getId()+FormManagerUtil.autofill_setvalue_ending);
				xformsComponentDataBean.setKeySetvalue(setval_el);
			}
		}
	}	
	public LocalizedStringBean getLocalizedStrings(FormComponent component) {
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		return FormManagerUtil.getLabelLocalizedStrings(xformsComponentDataBean.getElement(), component.getContext().getXformsXmlDoc());
	}
	
	public boolean getIsRequired(FormComponent component) {
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		Element bind = xformsComponentDataBean.getBind();
		return bind != null && bind.hasAttribute(required_att) && bind.getAttribute(required_att).equals(true_xpath);
	}
	
	public LocalizedStringBean getErrorLabelLocalizedStrings(FormComponent component) {
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		return FormManagerUtil.getErrorLabelLocalizedStrings(xformsComponentDataBean.getElement(), component.getContext().getXformsXmlDoc());
	}
	public LocalizedStringBean getHelpText(FormComponent component) {
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		return FormManagerUtil.getHelpTextLocalizedStrings(xformsComponentDataBean.getElement(), component.getContext().getXformsXmlDoc());
	}
	
	public String getVariableName(FormComponent component) {
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		if(xformsComponentDataBean.getNodeset() != null)
			return xformsComponentDataBean.getNodeset().getAttribute(FormManagerUtil.mapping_att);
		
		return null;
	}
	
	public void loadConfirmationElement(FormComponent component, FormComponentPage confirmation_page) {
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		Element preview_element = FormManagerUtil.getElementByIdFromDocument(
				component.getContext().getXformsXmlDoc(), FormManagerUtil.body_tag, FormManagerUtil.preview+'.'+component.getId());
		
		if(preview_element != null) {
			xformsComponentDataBean.setPreviewElement(preview_element);
			return;
		}
		
		if(confirmation_page == null)
			return;
		
		Element bind_element = xformsComponentDataBean.getBind();
		
		if(bind_element == null)
			return;

//		creating new preview element
		FormComponent component_after_this = component.getComponentAfterThis();
		Element page_element = confirmation_page.getXformsComponentDataBean().getElement();
		
		if(component_after_this != null) {
			
			Element preview_after = null;

//			if preview_after == null, that could mean 2 things:
//			- errornous form xforms document (ignore)
//			- form component is not "normal" component (default), taking next if exists
			while (component_after_this != null &&
					(preview_after = component_after_this.getXformsComponentDataBean().getPreviewElement()) == null
			)
				component_after_this = component_after_this.getComponentAfterThis();
			
			if(preview_after == null)
				appendPreviewElement(component, page_element, 
						confirmation_page.getButtonArea() == null ? null : 
							((FormComponent)confirmation_page.getButtonArea()).getXformsComponentDataBean().getElement()
				);
			else {
				
				Element output_element = createPreviewElement(component);
				output_element = (Element)preview_after.getParentNode().insertBefore(output_element, preview_after);
				xformsComponentDataBean.setPreviewElement(output_element);
			}
			
		} else
			appendPreviewElement(component, page_element, 
					confirmation_page.getButtonArea() == null ? null : 
						((FormComponent)confirmation_page.getButtonArea()).getXformsComponentDataBean().getElement()
			);
	}
	
	protected Element createPreviewElement(FormComponent component) {
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		Element output_element = component.getContext().getXformsXmlDoc().createElementNS(xformsComponentDataBean.getElement().getNamespaceURI(), FormManagerUtil.output_tag);
		
		output_element.setAttribute(FormManagerUtil.id_att, FormManagerUtil.preview+'.'+component.getId());
		output_element.setAttribute(FormManagerUtil.bind_att, xformsComponentDataBean.getBind().getAttribute(FormManagerUtil.id_att));
		
		Element component_element = xformsComponentDataBean.getElement();
		Element component_label = DOMUtil.getChildElement(component_element, FormManagerUtil.label_tag);
		
		if(component_label != null) {
			
			Element cloned_label = (Element)component_label.cloneNode(true);
			output_element.appendChild(cloned_label);
		}
		return output_element;
	}
	
	protected void appendPreviewElement(FormComponent component, Element page_element, Element button_area) {
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		Element output_element = createPreviewElement(component);
		
		if(button_area == null)
			output_element = (Element)page_element.appendChild(output_element);
		else
			output_element = (Element)button_area.getParentNode().insertBefore(output_element, button_area);
		
		xformsComponentDataBean.setPreviewElement(output_element);
	}
	
	public String getAutofillKey(FormComponent component) {
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		Element inst = xformsComponentDataBean.getKeyExtInstance();
		
		if(inst == null)
			return null;
		String src = inst.getAttribute(FormManagerUtil.src_att);
		String key = src.substring(FormManagerUtil.context_att_pref.length());
		
		return key.startsWith(FormManagerUtil.autofill_key_prefix) ? 
			key.substring(FormManagerUtil.autofill_key_prefix.length()) :
			key;
	}
}