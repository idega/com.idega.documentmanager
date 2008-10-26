package com.idega.documentmanager.manager.impl;

import org.chiba.xml.dom.DOMUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.documentmanager.component.FormComponent;
import com.idega.documentmanager.component.FormComponentPage;
import com.idega.documentmanager.component.FormDocument;
import com.idega.documentmanager.component.beans.ComponentDataBean;
import com.idega.documentmanager.component.impl.FormComponentFactory;
import com.idega.documentmanager.manager.XFormsManagerPage;
import com.idega.documentmanager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.8 $
 *
 * Last modified: $Date: 2008/10/26 16:47:10 $ by $Author: anton $
 */
public class XFormsManagerPageImpl extends XFormsManagerContainerImpl implements XFormsManagerPage {

	@Override
	public void loadXFormsComponentFromDocument(FormComponent component) {
		
		super.loadXFormsComponentFromDocument(component);
		checkForSpecialTypes(component);
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		Element case_element = xformsComponentDataBean.getElement();
		xformsComponentDataBean.setElement((Element)case_element.getElementsByTagName(FormManagerUtil.group_tag).item(0));
	}
	
	@Override
	public void addComponentToDocument(FormComponent component) {
		
		super.addComponentToDocument(component);
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		Element group_element = xformsComponentDataBean.getElement();
		
		String component_id = group_element.getAttribute(FormManagerUtil.id_att);
		Element case_element = group_element.getOwnerDocument().createElementNS(FormManagerUtil.idega_namespace, FormManagerUtil.idegans_case_tag);
		String name = group_element.getAttribute(FormManagerUtil.name_att);
		if(name != null && name.length() != 0) {
			
			group_element.removeAttribute(FormManagerUtil.name_att);
			case_element.setAttribute(FormManagerUtil.name_att, name);
		}
		group_element.getParentNode().replaceChild(case_element, group_element);
		group_element.removeAttribute(FormManagerUtil.id_att);
		case_element.setAttribute(FormManagerUtil.id_att, component_id);
		case_element.appendChild(group_element);
		
		checkForSpecialTypes(component);
		pageContextChanged(component);
	}
	
	protected void checkForSpecialTypes(FormComponent component) {
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		String component_name = xformsComponentDataBean.getElement().getAttribute(FormManagerUtil.name_att);
		if(component_name != null && 
				component_name.equals(FormComponentFactory.confirmation_page_type) ||
				component_name.equals(FormComponentFactory.page_type_thx))
			component.setType(component_name);
	}
	
	@Override
	public void removeComponentFromXFormsDocument(FormComponent component) {
		
		removeComponentLocalization(component);
		removeComponentBindings(component);
		removeSectionVisualization(component);
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		Element element_to_remove = xformsComponentDataBean.getElement();
		element_to_remove.getParentNode().getParentNode().removeChild(element_to_remove.getParentNode());
	}
	
	private void removeSectionVisualization(FormComponent component) {
		
		Element section = FormManagerUtil.getElementByIdFromDocument(component.getContext().getXformsXmlDoc(), FormManagerUtil.head_tag, component.getId()+"_section");
		
		if(section != null)
			section.getParentNode().removeChild(section);
	}
	
	@Override
	public void moveComponent(FormComponent component, String before_component_id) {
		
		if(component.getParent() == null)
			throw new NullPointerException("Parent form document not provided");
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		Document xforms_doc = component.getContext().getXformsXmlDoc();
		
		Element element_to_move = (Element)xformsComponentDataBean.getElement().getParentNode();
		Element element_to_insert_before = null;

		if(before_component_id != null) {
			
			element_to_insert_before = FormManagerUtil.getElementByIdFromDocument(xforms_doc, FormManagerUtil.body_tag, before_component_id);
		} else {

			Element components_container = (Element)element_to_move.getParentNode();
			element_to_insert_before = DOMUtil.getLastChildElement(components_container);
		}
		
		xformsComponentDataBean.setElement(
				(Element)((Element)((Element)element_to_move.getParentNode()).insertBefore(element_to_move, element_to_insert_before))
				.getElementsByTagName(FormManagerUtil.group_tag).item(0)
		);
	}
	
	@Override
	protected Element getInsertBeforeComponentElement(FormComponent component_after_this) {
		return (Element)component_after_this.getXformsComponentDataBean().getElement().getParentNode();
	}
	
	public void pageContextChanged(FormComponent component) {
		
		FormDocument formDocument = component.getFormDocument();
		
		if(!formDocument.getProperties().isStepsVisualizationUsed() || FormComponentFactory.page_type_thx.equals(component.getType()))
			return;
		
		Element instance = formDocument.getSectionsVisualizationInstanceElement();
		Element section = FormManagerUtil.getElementByIdFromDocument(component.getContext().getXformsXmlDoc(), FormManagerUtil.head_tag, component.getId()+"_section");
		
		if(section == null) {
			
			section = FormManagerUtil.getItemElementById(CacheManager.getInstance().getComponentsXforms(), FormManagerUtil.section_item);
			section = (Element)component.getContext().getXformsXmlDoc().importNode(section, true);
			section.setAttribute(FormManagerUtil.id_att, component.getId()+"_section");
			Element id_el = (Element)section.getElementsByTagName(FormManagerUtil.id_att).item(0);
			FormManagerUtil.setElementsTextNodeValue(id_el, component.getId());
		} else
			section.getParentNode().removeChild(section);

		int seq_idx = component.getParent().getContainedComponentsIdList().indexOf(component.getId())+1;
		
		if(seq_idx == 1)
			section.setAttribute("selected", "true");
		else
			section.setAttribute("selected", "false");
		
		Element seq_el = (Element)section.getElementsByTagName("seq_index").item(0);
		FormManagerUtil.setElementsTextNodeValue(seq_el, String.valueOf(seq_idx));
		
		FormComponentPage page = (FormComponentPage)component;
		FormComponentPage next = page.getNextPage();
		FormComponentPage prev = page.getPreviousPage();
		
		boolean inserted = false;
		
		if(next != null)
			inserted = insertSectionIntoContext(component, false, next, instance, section);
		
		if(prev != null && !inserted)
			inserted = insertSectionIntoContext(component, true, prev, instance, section);
		
		if(!inserted)
			insertSectionIntoContext(component, false, null, instance, section);
	}
	
	private boolean insertSectionIntoContext(FormComponent component, boolean prev, FormComponentPage relevant_page, Element instance, Element section) {
		
		
		if(relevant_page != null) {
			Element relevant_section = FormManagerUtil.getElementByIdFromDocument(component.getContext().getXformsXmlDoc(), FormManagerUtil.head_tag, relevant_page.getId()+"_section");
			
			if(relevant_section == null)
				return false;
			
			if(!prev)
				relevant_section.getParentNode().insertBefore(section, relevant_section);
			else
				DOMUtil.insertAfter(section, relevant_section);
			
		} else {

			Element class_exp = (Element)instance.getElementsByTagName("class_exp").item(0);
			class_exp.getParentNode().insertBefore(section, class_exp);
		}
		
		return true;
	}
}