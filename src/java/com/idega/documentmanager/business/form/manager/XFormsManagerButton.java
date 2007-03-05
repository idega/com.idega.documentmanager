package com.idega.documentmanager.business.form.manager;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.idega.documentmanager.business.form.ConstButtonType;
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
			
			if(previous == null) {
				((XFormsComponentButtonDataBean)this.xforms_component).setToggleElement(null);
				toggle_element.getParentNode().removeChild(toggle_element);
			} else 
				toggle_element.setAttribute(FormManagerUtil.case_att, previous.getId());
			
		} else if(component.getType().equals(ConstButtonType.next_page_button)) {
			
			if(next == null) {
				((XFormsComponentButtonDataBean)this.xforms_component).setToggleElement(null);
				toggle_element.getParentNode().removeChild(toggle_element);
			} else 
				toggle_element.setAttribute(FormManagerUtil.case_att, next.getId());
			
		} else if(component.getType().equals(ConstButtonType.submit_form_button)) {

			form_document.registerForLastPage(((IFormComponentButtonArea)component_parent).getCurrentPage().getId());
			
			if(form_document.getThxPage() == null)
				throw new NullPointerException("Thanks page not found");
			
			toggle_element.setAttribute(FormManagerUtil.case_att, form_document.getThxPage().getId());
		}
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