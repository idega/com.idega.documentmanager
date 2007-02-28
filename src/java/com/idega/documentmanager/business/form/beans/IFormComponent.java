package com.idega.documentmanager.business.form.beans;

import com.idega.documentmanager.business.form.PropertiesComponent;
import com.idega.documentmanager.business.form.manager.IXFormsManager;

/**
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 *
 */
public interface IFormComponent {

	public abstract void render();

	public abstract void setComponentAfterThis(IFormComponent component);
	
	public abstract IFormComponent getComponentAfterThis();
	
	public abstract void setComponentAfterThisRerender(IFormComponent component);

	public abstract String getId();

	public abstract void setId(String id);

	public abstract void setType(String type);
	
	public abstract String getType();

	public abstract PropertiesComponent getProperties();
	
	public abstract void remove();
	
	public abstract void setLoad(boolean load);
	
	public abstract void setParent(IFormComponentContainer parent);
	
	public abstract IXFormsManager getComponentXFormsManager();
	
	public abstract void setFormDocument(IFormComponentDocument form_document);
	
	public abstract void addToConfirmationPage();
}