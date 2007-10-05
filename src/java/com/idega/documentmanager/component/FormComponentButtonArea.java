package com.idega.documentmanager.component;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 */
public interface FormComponentButtonArea extends FormComponentContainer {

	public abstract void setButtonMapping(String button_type, String button_id);
	
	public abstract void setPageSiblings(FormComponentPage previous, FormComponentPage next);
	
	public abstract FormComponentPage getPreviousPage();
	
	public abstract FormComponentPage getNextPage();
	
	public abstract FormComponentPage getCurrentPage();
	
	public abstract void announceLastPage(String last_page_id);
}