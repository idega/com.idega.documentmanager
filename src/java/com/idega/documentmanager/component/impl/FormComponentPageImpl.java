package com.idega.documentmanager.component.impl;

import com.idega.documentmanager.business.component.ButtonArea;
import com.idega.documentmanager.business.component.Page;
import com.idega.documentmanager.business.component.properties.PropertiesPage;
import com.idega.documentmanager.component.FormComponentButtonArea;
import com.idega.documentmanager.component.FormComponentPage;
import com.idega.documentmanager.component.properties.impl.ComponentPropertiesPage;
import com.idega.documentmanager.manager.XFormsManagerPage;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.3 $
 *
 * Last modified: $Date: 2007/11/15 09:24:15 $ by $Author: civilis $
 */
public class FormComponentPageImpl extends FormComponentContainerImpl implements Page, FormComponentPage {
	
	protected boolean first = false;
	protected String button_area_id;
	protected FormComponentPage previous_page;
	protected FormComponentPage next_page;
	
	public PropertiesPage getProperties() {
		
		if(properties == null) {
			ComponentPropertiesPage properties = new ComponentPropertiesPage();
			properties.setComponent(this);
			this.properties = properties;
		}
		
		return (PropertiesPage)properties;
	}
	
	@Override
	public XFormsManagerPage getXFormsManager() {
		
		return getContext().getXformsManagerFactory().getXformsManagerPage();
	}
	
	@Override
	public void remove() {
		
		if(getType().equals(FormComponentFactory.page_type_thx)) {
			//TODO: log warning
			System.out.println("removing page thx");
		}
		
		super.remove();
		parent.componentsOrderChanged();
	}
	
	public ButtonArea getButtonArea() {
		
		return button_area_id == null ? null : (ButtonArea)getContainedComponent(button_area_id);
	}
	public ButtonArea createButtonArea(String component_after_this_id) throws NullPointerException {
		
		if(getButtonArea() != null)
			throw new IllegalArgumentException("Button area already exists in the page, remove first");
		
		return (ButtonArea)addComponent(FormComponentFactory.fbc_button_area, component_after_this_id);
	}
	public void setButtonAreaComponentId(String button_area_id) {
		this.button_area_id = button_area_id;
	}
	public void setPageSiblings(FormComponentPage previous, FormComponentPage next) {
		
		next_page = next;
		previous_page = previous;
	}
	public void pagesSiblingsChanged() {
		getXFormsManager().pageContextChanged(this);
		FormComponentButtonArea button_area = (FormComponentButtonArea)getButtonArea();
		if(button_area == null)
			return;
		
		button_area.setPageSiblings(previous_page, next_page);
	}
	public FormComponentPage getPreviousPage() {
		return previous_page;
	}
	public FormComponentPage getNextPage() {
		return next_page;
	}
	public void announceLastPage(String last_page_id) {
		FormComponentButtonArea button_area = (FormComponentButtonArea)getButtonArea();
		
		if(button_area == null)
			return;
		
		button_area.announceLastPage(last_page_id);
	}
}