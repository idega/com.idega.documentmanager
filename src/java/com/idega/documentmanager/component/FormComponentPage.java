package com.idega.documentmanager.component;

import com.idega.documentmanager.business.component.ButtonArea;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 */
public interface FormComponentPage extends FormComponentContainer {

	public abstract void setButtonAreaComponentId(String button_area_id);
	
	public abstract void setPageSiblings(FormComponentPage previous, FormComponentPage next);
	
	public abstract void pagesSiblingsChanged();
	
	public abstract FormComponentPage getPreviousPage();
	
	public abstract FormComponentPage getNextPage();
	
	public abstract void announceLastPage(String last_page_id);
	
	public abstract ButtonArea getButtonArea();
}