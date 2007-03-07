package com.idega.documentmanager.business.form.beans;

import java.util.Locale;

import org.w3c.dom.Element;

import com.idega.documentmanager.business.form.ComponentPlain;
import com.idega.documentmanager.business.form.PropertiesPlain;
import com.idega.documentmanager.business.form.manager.CacheManager;
import com.idega.documentmanager.business.form.manager.IXFormsManager;
import com.idega.documentmanager.business.form.manager.XFormsManagerPlain;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 */
public class FormComponentPlain extends FormComponent implements ComponentPlain {

	@Override
	protected void setProperties() {
		
		ComponentPropertiesPlain properties = (ComponentPropertiesPlain)getProperties();
		
		if(properties == null)
			return;
		
		properties.setText(((XFormsManagerPlain)getXFormsManager()).getText());
	}
	
	@Override
	public PropertiesPlain getProperties() {
		
		if(properties == null) {
			ComponentPropertiesPlain properties = new ComponentPropertiesPlain();
			properties.setParentComponent(this);
			this.properties = properties;
		}
		
		return (PropertiesPlain)properties;
	}
	
	@Override
	public IXFormsManager getXFormsManager() {
		
		if(xforms_manager == null) {
			
			xforms_manager = new XFormsManagerPlain();
			xforms_manager.setCacheManager(CacheManager.getInstance());
			xforms_manager.setComponentParent(parent);
			xforms_manager.setFormComponent(this);
			xforms_manager.setFormDocument(form_document);
		}
		
		return xforms_manager;
	}
	
	@Override
	public void update(ConstUpdateType what) {
		
		getXFormsManager().update(what);
		
		int update = what.getUpdateType();
		
		switch (update) {
		case ConstUpdateType.text:
			getHtmlManager().clearHtmlComponents();
			break;
			
		case ConstUpdateType.autofill_key:
			break;

		default:
			break;
		}
	}
	
	@Override
	public Element getHtmlRepresentation(Locale locale) throws Exception {
		
		return getXFormsManager().getComponentElement();
	}
	
	@Override
	protected void changeBindNames() { }
}