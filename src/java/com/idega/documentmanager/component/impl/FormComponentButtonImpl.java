package com.idega.documentmanager.component.impl;

import com.idega.documentmanager.business.component.Button;
import com.idega.documentmanager.component.FormComponentButton;
import com.idega.documentmanager.component.FormComponentButtonArea;
import com.idega.documentmanager.component.FormComponentPage;
import com.idega.documentmanager.manager.HtmlManager;
import com.idega.documentmanager.manager.XFormsManagerButton;
import com.idega.documentmanager.manager.impl.HtmlManagerButtonImpl;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2007/10/05 11:42:31 $ by $Author: civilis $
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
		getXFormsManager().renewButtonPageContextPages(getContext(), previous, next);
	}
	
	public void setLastPageId(String last_page_id) {
		getXFormsManager().setLastPageToSubmitButton(getContext(), last_page_id);
	}
	
	@Override
	protected HtmlManager getHtmlManager() {
		
		if(html_manager == null) {
			
			html_manager = new HtmlManagerButtonImpl();
			html_manager.setCacheManager(getContext().getCacheManager());
			html_manager.setFormComponent(this);
			html_manager.setFormDocument(getFormDocument());
		}
		
		return html_manager;
	}
}