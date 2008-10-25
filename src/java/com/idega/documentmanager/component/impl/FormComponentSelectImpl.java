package com.idega.documentmanager.component.impl;

import com.idega.documentmanager.business.component.ComponentSelect;
import com.idega.documentmanager.business.component.properties.PropertiesSelect;
import com.idega.documentmanager.component.properties.impl.ComponentPropertiesSelect;
import com.idega.documentmanager.component.properties.impl.ConstUpdateType;
import com.idega.documentmanager.manager.XFormsManagerSelect;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.5 $
 *
 * Last modified: $Date: 2008/10/25 18:30:19 $ by $Author: civilis $
 */
public class FormComponentSelectImpl extends FormComponentImpl implements ComponentSelect {
	
	@Override
	public XFormsManagerSelect getXFormsManager() {
		
		return getFormDocument().getContext().getXformsManagerFactory().getXformsManagerSelect();
	}
	
	@Override
	public PropertiesSelect getProperties() {
		
		if(properties == null) {
			ComponentPropertiesSelect properties = new ComponentPropertiesSelect();
			properties.setComponent(this);
			this.properties = properties;
		}
		
		return (PropertiesSelect)properties;
	}
	
	@Override
	protected void setProperties() {
		
		super.setProperties();
		
		ComponentPropertiesSelect properties = (ComponentPropertiesSelect)getProperties();
		XFormsManagerSelect xforms_manager = getXFormsManager();
		
		properties.setDataSrcUsedPlain(xforms_manager.getDataSrcUsed(this));
		properties.setExternalDataSrcPlain(xforms_manager.getExternalDataSrc(this));
		properties.setItemsetPlain(xforms_manager.getItemset(this));
	}
	
	@Override
	public void update(ConstUpdateType what) {
		super.update(what);
		
		switch (what) {
		case DATA_SRC_USED:
			getHtmlManager().clearHtmlComponents(this);
			getFormDocument().setFormDocumentModified(true);
			break;
			
		case ITEMSET:
			getHtmlManager().clearHtmlComponents(this);
			getFormDocument().setFormDocumentModified(true);
			break;
			
		case EXTERNAL_DATA_SRC:
			getHtmlManager().clearHtmlComponents(this);
			getFormDocument().setFormDocumentModified(true);
			break;

		default:
			break;
		}
	}
	
	public void remove() {
		
		getXFormsManager().removeSelectComponentSourcesFromXFormsDocument(this);
		super.remove();
	}
}