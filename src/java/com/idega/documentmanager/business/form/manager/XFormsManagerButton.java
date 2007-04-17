package com.idega.documentmanager.business.form.manager;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.idega.documentmanager.business.form.ConstButtonType;
import com.idega.documentmanager.business.form.beans.FormComponentFactory;
import com.idega.documentmanager.business.form.beans.IFormComponentButtonArea;
import com.idega.documentmanager.business.form.beans.IFormComponentPage;
import com.idega.documentmanager.business.form.beans.XFormsComponentButtonDataBean;
import com.idega.documentmanager.business.form.beans.XFormsComponentDataBean;
import com.idega.documentmanager.business.form.manager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 */
public class XFormsManagerButton extends XFormsManager {
	
	@Override
	public void loadXFormsComponentFromDocument(String component_id) {
		super.loadXFormsComponentFromDocument(component_id);
		Element button_element = xforms_component.getElement();
		String button_type = button_element.getAttribute(FormManagerUtil.name_att);
		
		if(button_type != null)
			component.setType(button_type);

		loadToggleElement();
	}
	
	protected void loadToggleElement() {
		
		XFormsComponentButtonDataBean xforms_component = (XFormsComponentButtonDataBean)this.xforms_component;
		
		NodeList toggles = xforms_component.getElement().getElementsByTagName(FormManagerUtil.toggle_tag);
		
		if(toggles == null)
			return;
		
		Element toggle_element = (Element)toggles.item(0);
		((XFormsComponentButtonDataBean)this.xforms_component).setToggleElement(toggle_element);
	}
	
	@Override
	protected XFormsComponentDataBean newXFormsComponentDataBeanInstance() {
		return new XFormsComponentButtonDataBean();
	}
	
	public void renewButtonPageContextPages(IFormComponentPage previous, IFormComponentPage next) {
		
		Element toggle_element = ((XFormsComponentButtonDataBean)xforms_component).getToggleElement();
		
		if(toggle_element == null)
			toggle_element = createToggleElement();
		
		if(!component.getType().equals(ConstButtonType.reset_form_button) && toggle_element == null)
			throw new NullPointerException("Incorrect button: toggle element missing. Must be provided for button type: "+component.getType());
		
		if(component.getType().equals(ConstButtonType.previous_page_button)) {
			
			renewNextPrevButtons(prev_button, previous, toggle_element);
			
		} else if(component.getType().equals(ConstButtonType.next_page_button)) {
			
			renewNextPrevButtons(next_button, next, toggle_element);
			
		} else if(component.getType().equals(ConstButtonType.submit_form_button)) {

			form_document.registerForLastPage(((IFormComponentButtonArea)component_parent).getCurrentPage().getId());
			
			if(form_document.getThxPage() == null)
				throw new NullPointerException("Thanks page not found");
			
			toggle_element.setAttribute(FormManagerUtil.case_att, form_document.getThxPage().getId());
		}
	}
	
	private static final int next_button = 1;
	private static final int prev_button = 2;
	
	protected void renewNextPrevButtons(int button_type, IFormComponentPage relevant_page, Element toggle_element) {

		if(relevant_page == null) {
			
			((XFormsComponentButtonDataBean)this.xforms_component).setToggleElement(null);
			toggle_element.getParentNode().removeChild(toggle_element);
			removeSetValues();
			
		} else {
			toggle_element.setAttribute(FormManagerUtil.case_att, relevant_page.getId());
			
			if(form_document.getProperties().isStepsVisualizationUsed()) {

				if(FormComponentFactory.page_type_thx.equals(relevant_page.getType())) {
					removeSetValues();
				} else {
					
					Element setval = FormManagerUtil.getElementByIdFromDocument(form_document.getXformsDocument(), FormManagerUtil.body_tag, component.getId()+FormManagerUtil.set_section_vis_cur);
					
					if(setval == null) {
						
						setval = createSetValue(true);
						setval = (Element)toggle_element.getParentNode().insertBefore(setval, toggle_element);
					}
					setval.setAttribute(FormManagerUtil.ref_s_att, "instance('"+FormManagerUtil.sections_visualization_instance_id+"')/section[id='"+component_parent.getParentPage().getId()+"']/@selected");
					
					setval = FormManagerUtil.getElementByIdFromDocument(form_document.getXformsDocument(), FormManagerUtil.body_tag, component.getId()+FormManagerUtil.set_section_vis_rel);
					
					if(setval == null) {
						
						setval = createSetValue(false);
						setval = (Element)toggle_element.getParentNode().insertBefore(setval, toggle_element);
					}
					
					setval.setAttribute(FormManagerUtil.ref_s_att, "instance('"+FormManagerUtil.sections_visualization_instance_id+"')/section[id='"+relevant_page.getId()+"']/@selected");
				}
			} else
				removeSetValues();
		}
	}
	
	private void removeSetValues() {
		
		Element setval = FormManagerUtil.getElementByIdFromDocument(form_document.getXformsDocument(), FormManagerUtil.body_tag, component.getId()+FormManagerUtil.set_section_vis_cur);
		
		if(setval != null)
			setval.getParentNode().removeChild(setval);
		
		setval = FormManagerUtil.getElementByIdFromDocument(form_document.getXformsDocument(), FormManagerUtil.body_tag, component.getId()+FormManagerUtil.set_section_vis_rel);
		
		if(setval != null)
			setval.getParentNode().removeChild(setval);
	}
	
	private Element createSetValue(boolean current) {

		Document xforms_doc = form_document.getXformsDocument();
		Element set_value = xforms_doc.createElement(FormManagerUtil.setvalue_tag);
		set_value.setAttribute("ev:event", FormManagerUtil.DOMActivate_att_val);
		set_value.setAttribute(FormManagerUtil.value_att, "instance('"+FormManagerUtil.sections_visualization_instance_id+"')/class_exp[@for='"+(current ? "false" : "true")+"']/@for");
		set_value.setAttribute(FormManagerUtil.id_att, component.getId()+(current ? FormManagerUtil.set_section_vis_cur : FormManagerUtil.set_section_vis_rel));
		return set_value;
	}
	
	@Override
	public void addComponentToDocument() {
		super.addComponentToDocument();
		loadToggleElement();
	}
	
	public void setLastPageToSubmitButton(String last_page_id) {
		((XFormsComponentButtonDataBean)this.xforms_component).getToggleElement().setAttribute(FormManagerUtil.case_att, last_page_id);
	}
	
	protected Element createToggleElement() {
		
		Element toggle_element = FormManagerUtil.getItemElementById(CacheManager.getInstance().getComponentsXforms(), "toggle-element");
		Element button_element = xforms_component.getElement();
		NodeList refreshs = button_element.getElementsByTagName(FormManagerUtil.refresh_tag);
		toggle_element = (Element)button_element.getOwnerDocument().importNode(toggle_element, true);
		
		if(refreshs == null || refreshs.getLength() == 0)
			toggle_element = (Element)button_element.appendChild(toggle_element);
		else
			toggle_element = (Element)button_element.insertBefore(toggle_element, refreshs.item(refreshs.getLength()-1));
		
		((XFormsComponentButtonDataBean)xforms_component).setToggleElement(toggle_element);
		return toggle_element;
	}
}