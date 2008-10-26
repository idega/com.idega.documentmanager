package com.idega.documentmanager.component.impl;

import com.idega.documentmanager.business.component.ComponentPlain;
import com.idega.documentmanager.business.component.properties.PropertiesPlain;
import com.idega.documentmanager.component.properties.impl.ComponentPropertiesPlain;
import com.idega.documentmanager.component.properties.impl.ConstUpdateType;
import com.idega.documentmanager.manager.XFormsManagerPlain;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.9 $
 *
 * Last modified: $Date: 2008/10/26 16:47:10 $ by $Author: anton $
 */
public class FormComponentPlainImpl extends FormComponentImpl implements ComponentPlain {

	@Override
	protected void setProperties() {
		
		ComponentPropertiesPlain properties = (ComponentPropertiesPlain)getProperties();
		
		if(properties == null)
			return;
		
		properties.setText(getXFormsManager().getText(this));
	}
	
	@Override
	public PropertiesPlain getProperties() {
		
		if(properties == null) {
			ComponentPropertiesPlain properties = new ComponentPropertiesPlain();
			properties.setComponent(this);
			this.properties = properties;
		}
		
		return (PropertiesPlain)properties;
	}
	
	@Override
	public XFormsManagerPlain getXFormsManager() {
		
		return getContext().getXformsManagerFactory().getXformsManagerPlain();
	}
	
	@Override
	public void update(ConstUpdateType what) {
		
		getXFormsManager().update(this, what);
		
		switch (what) {
		case LABEL:
		    	getHtmlManager().clearHtmlComponents(this);
			getFormDocument().setFormDocumentModified(true);
			break;
		case TEXT:
		    	getHtmlManager().clearHtmlComponents(this);
			getFormDocument().setFormDocumentModified(true);
			break;
			
		case AUTOFILL_KEY:
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void changeBindNames() { }
}