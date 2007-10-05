package com.idega.documentmanager.component;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 */
public interface FormComponentButton extends FormComponent {

	public abstract void setSiblingsAndParentPages(FormComponentPage previous, FormComponentPage next);
	
	public abstract void setLastPageId(String last_page_id);
}