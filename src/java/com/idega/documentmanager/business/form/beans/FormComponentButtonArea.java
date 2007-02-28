package com.idega.documentmanager.business.form.beans;

import java.util.HashMap;
import java.util.Map;

import com.idega.documentmanager.business.form.Button;
import com.idega.documentmanager.business.form.ButtonArea;
import com.idega.documentmanager.business.form.ConstButtonType;


/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 */
public class FormComponentButtonArea extends FormComponentContainer implements ButtonArea, IFormComponentButtonArea {

	protected Map<String, String> buttons_type_id_mapping;

	public Button getButton(ConstButtonType button_type) {

		if(button_type == null)
			throw new NullPointerException("Button type provided null");
		
		return !getButtonsTypeIdMapping().containsKey(button_type.getButtonType()) ? null : 
			(Button)getContainedComponent(getButtonsTypeIdMapping().get(button_type.getButtonType()));
	}
	
	public Button addButton(ConstButtonType button_type, String component_after_this_id) throws NullPointerException {
		
		if(button_type == null)
			throw new NullPointerException("Button type provided null");
		
		if(getButtonsTypeIdMapping().containsKey(button_type))
			throw new IllegalArgumentException("Button by type provided: "+button_type+" already exists in the button area, remove first");
		
		return (Button)addComponent(button_type.getButtonType(), component_after_this_id);
	}
	
	protected Map<String, String> getButtonsTypeIdMapping() {
		
		if(buttons_type_id_mapping == null)
			buttons_type_id_mapping = new HashMap<String, String>();
		
		return buttons_type_id_mapping;
	}
	
	@Override
	public void render() {
		super.render();
		((IFormComponentPage)parent).setButtonAreaComponentId(getId());
	}
	@Override
	public void remove() {
		super.remove();
		((IFormComponentPage)parent).setButtonAreaComponentId(null);
	}
	public void setButtonMapping(String button_type, String button_id) {
		
		getButtonsTypeIdMapping().put(button_type, button_id);
	}
	public void setPageSiblings(IFormComponentPage previous, IFormComponentPage next) {
		
		IFormComponentButton button = (IFormComponentButton)getButton(new ConstButtonType(ConstButtonType.previous_page_button));
		
		if(button != null)
			button.setSiblingsAndParentPages(previous, next);
		
		button = (IFormComponentButton)getButton(new ConstButtonType(ConstButtonType.next_page_button));
		
		if(button != null)
			button.setSiblingsAndParentPages(previous, next);
	}
	public IFormComponentPage getPreviousPage() {
		return ((IFormComponentPage)parent).getPreviousPage();
	}
	public IFormComponentPage getNextPage() {
		return ((IFormComponentPage)parent).getNextPage();
	}
	public IFormComponentPage getCurrentPage() {
		return (IFormComponentPage)parent;
	}
	
	public void announceLastPage(String last_page_id) {
		
		IFormComponentButton submit_button = (IFormComponentButton)getButton(new ConstButtonType(ConstButtonType.submit_form_button));
		
		if(submit_button == null)
			return;
		
		submit_button.setLastPageId(last_page_id);
	}
}