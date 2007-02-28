package com.idega.documentmanager.business.form.manager;

import org.chiba.xml.dom.DOMUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.documentmanager.business.form.beans.FormComponentFactory;
import com.idega.documentmanager.business.form.manager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 */
public class XFormsManagerPage extends XFormsManagerContainer {

	@Override
	public void loadXFormsComponentFromDocument(String component_id) {
		super.loadXFormsComponentFromDocument(component_id);
		checkForSpecialTypes();
		Element case_element = xforms_component.getElement();
		xforms_component.setElement((Element)case_element.getElementsByTagName(FormManagerUtil.group_tag).item(0));
	}
	
	@Override
	public void addComponentToDocument() {
		
		super.addComponentToDocument();
		Element group_element = xforms_component.getElement();
		
		String component_id = group_element.getAttribute(FormManagerUtil.id_att);
		Element case_element = group_element.getOwnerDocument().createElement(FormManagerUtil.case_tag);
		String name = group_element.getAttribute(FormManagerUtil.name_att);
		if(name != null && !name.equals("")) {
			
			group_element.removeAttribute(FormManagerUtil.name_att);
			case_element.setAttribute(FormManagerUtil.name_att, name);
		}
		group_element.getParentNode().replaceChild(case_element, group_element);
		group_element.removeAttribute(FormManagerUtil.id_att);
		case_element.setAttribute(FormManagerUtil.id_att, component_id);
		case_element.appendChild(group_element);
		
		checkForSpecialTypes();
	}
	
	protected void checkForSpecialTypes() {
		String component_name = xforms_component.getElement().getAttribute(FormManagerUtil.name_att);
		if(component_name != null && 
				component_name.equals(FormComponentFactory.confirmation_page_type) ||
				component_name.equals(FormComponentFactory.page_type_thx))
			component.setType(component_name);
	}
	
	@Override
	public void removeComponentFromXFormsDocument() {
		
		removeComponentLocalization();
		removeComponentBindings();
		
		Element element_to_remove = xforms_component.getElement();
		element_to_remove.getParentNode().getParentNode().removeChild(element_to_remove.getParentNode());
	}
	
	@Override
	public void moveComponent(String before_component_id) {
		
		if(component_parent == null)
			throw new NullPointerException("Parent form document not provided");
		
		Document xforms_doc = form_document.getXformsDocument();
		
		Element element_to_move = (Element)xforms_component.getElement().getParentNode();
		Element element_to_insert_before = null;

		if(before_component_id != null) {
			
			element_to_insert_before = FormManagerUtil.getElementByIdFromDocument(xforms_doc, FormManagerUtil.body_tag, before_component_id);
		} else {

			Element components_container = (Element)element_to_move.getParentNode();
			element_to_insert_before = DOMUtil.getLastChildElement(components_container);
		}
		
		xforms_component.setElement(
				(Element)((Element)((Element)element_to_move.getParentNode()).insertBefore(element_to_move, element_to_insert_before))
				.getElementsByTagName(FormManagerUtil.group_tag).item(0)
		);
	}
}