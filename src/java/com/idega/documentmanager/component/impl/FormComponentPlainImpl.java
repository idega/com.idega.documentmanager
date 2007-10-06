package com.idega.documentmanager.component.impl;

import java.util.Locale;

import org.w3c.dom.Element;

import com.idega.documentmanager.business.component.ComponentPlain;
import com.idega.documentmanager.business.component.properties.PropertiesPlain;
import com.idega.documentmanager.component.properties.impl.ComponentPropertiesPlain;
import com.idega.documentmanager.component.properties.impl.ConstUpdateType;
import com.idega.documentmanager.manager.XFormsManagerPlain;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.4 $
 *
 * Last modified: $Date: 2007/10/06 13:07:12 $ by $Author: civilis $
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
		case TEXT:
			getHtmlManager().clearHtmlComponents(this);
			break;
			
		case AUTOFILL_KEY:
			break;

		default:
			break;
		}
	}
	
	@Override
	public Element getHtmlRepresentation(Locale locale) throws Exception {

//		TODO: wtf?
		return (Element)getXformsComponentDataBean().getElement().cloneNode(true);
	}
	
	@Override
	protected void changeBindNames() { }
}