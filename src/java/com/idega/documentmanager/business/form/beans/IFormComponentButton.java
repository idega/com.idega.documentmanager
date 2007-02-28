package com.idega.documentmanager.business.form.beans;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 */
public interface IFormComponentButton extends IFormComponent {

	public abstract void setSiblingsAndParentPages(IFormComponentPage previous, IFormComponentPage next);
	
	public abstract void setLastPageId(String last_page_id);
}