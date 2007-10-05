package com.idega.documentmanager.component.impl;

import java.util.HashMap;
import java.util.Map;

import com.idega.documentmanager.business.component.Button;
import com.idega.documentmanager.business.component.ButtonArea;
import com.idega.documentmanager.business.component.ConstButtonType;
import com.idega.documentmanager.component.FormComponentButton;
import com.idega.documentmanager.component.FormComponentButtonArea;
import com.idega.documentmanager.component.FormComponentPage;


/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2007/10/05 11:42:31 $ by $Author: civilis $
 */
public class FormComponentButtonAreaImpl extends FormComponentContainerImpl implements ButtonArea, FormComponentButtonArea {

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
		((FormComponentPage)parent).setButtonAreaComponentId(getId());
	}
	@Override
	public void remove() {
		super.remove();
		((FormComponentPage)parent).setButtonAreaComponentId(null);
	}
	public void setButtonMapping(String button_type, String button_id) {
		
		getButtonsTypeIdMapping().put(button_type, button_id);
	}
	public void setPageSiblings(FormComponentPage previous, FormComponentPage next) {
		
		FormComponentButton button = (FormComponentButton)getButton(new ConstButtonType(ConstButtonType.previous_page_button));
		
		if(button != null)
			button.setSiblingsAndParentPages(previous, next);
		
		button = (FormComponentButton)getButton(new ConstButtonType(ConstButtonType.next_page_button));
		
		if(button != null)
			button.setSiblingsAndParentPages(previous, next);
	}
	public FormComponentPage getPreviousPage() {
		return ((FormComponentPage)parent).getPreviousPage();
	}
	public FormComponentPage getNextPage() {
		return ((FormComponentPage)parent).getNextPage();
	}
	public FormComponentPage getCurrentPage() {
		return (FormComponentPage)parent;
	}
	
	public void announceLastPage(String last_page_id) {
		
		FormComponentButton submit_button = (FormComponentButton)getButton(new ConstButtonType(ConstButtonType.submit_form_button));
		
		if(submit_button == null)
			return;
		
		submit_button.setLastPageId(last_page_id);
	}
}