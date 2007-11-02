package com.idega.documentmanager.component.impl;

import com.idega.documentmanager.business.component.Button;
import com.idega.documentmanager.business.component.properties.PropertiesButton;
import com.idega.documentmanager.component.FormComponentButton;
import com.idega.documentmanager.component.FormComponentButtonArea;
import com.idega.documentmanager.component.FormComponentPage;
import com.idega.documentmanager.component.properties.impl.ComponentPropertiesButton;
import com.idega.documentmanager.manager.HtmlManagerButton;
import com.idega.documentmanager.manager.XFormsManagerButton;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.5 $
 *
 * Last modified: $Date: 2007/11/02 15:04:56 $ by $Author: civilis $
 */
public class FormComponentButtonImpl extends FormComponentImpl implements Button, FormComponentButton {
	
	@Override
	public XFormsManagerButton getXFormsManager() {
		return getContext().getXformsManagerFactory().getXformsManagerButton();
	}
	
	@Override
	public void render() {
		boolean load = this.load;
		super.render();
		FormComponentButtonArea my_button_area = (FormComponentButtonArea)parent;
		if(!load)
			setSiblingsAndParentPages(my_button_area.getPreviousPage(), my_button_area.getNextPage());
		((FormComponentButtonArea)parent).setButtonMapping(getType(), getId());
	}
	
	public void setSiblingsAndParentPages(FormComponentPage previous, FormComponentPage next) {
		getXFormsManager().renewButtonPageContextPages(this, previous, next);
	}
	
	public void setLastPageId(String last_page_id) {
		getXFormsManager().setLastPageToSubmitButton(this, last_page_id);
	}
	
	@Override
	protected HtmlManagerButton getHtmlManager() {
		
		return getContext().getHtmlManagerFactory().getHtmlManagerButton();
	}
	
	@Override
	public PropertiesButton getProperties() {
		
		if(properties == null) {
			ComponentPropertiesButton properties = new ComponentPropertiesButton();
			properties.setComponent(this);
			this.properties = properties;
		}
		
		return (PropertiesButton)properties;
	}
	
	@Override
	protected void setProperties() {
		
		super.setProperties();
		
		ComponentPropertiesButton properties = (ComponentPropertiesButton)getProperties();
		XFormsManagerButton xformsManager = getXFormsManager();
		
		properties.setReferAction(xformsManager.getReferAction(this));
	}
}