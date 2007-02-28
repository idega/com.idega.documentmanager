package com.idega.documentmanager.business.form.beans;

import com.idega.documentmanager.business.form.ButtonArea;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 */
public interface IFormComponentPage extends IFormComponentContainer {

	public abstract void setButtonAreaComponentId(String button_area_id);
	
	public abstract void setPageSiblings(IFormComponentPage previous, IFormComponentPage next);
	
	public abstract void pagesSiblingsChanged();
	
	public abstract IFormComponentPage getPreviousPage();
	
	public abstract IFormComponentPage getNextPage();
	
	public abstract void announceLastPage(String last_page_id);
	
	public abstract ButtonArea getButtonArea();
}