package com.idega.documentmanager.component.impl;

import com.idega.documentmanager.business.component.ComponentMultiUpload;
import com.idega.documentmanager.business.component.properties.PropertiesMultiUpload;
import com.idega.documentmanager.component.properties.impl.ComponentPropertiesMultiUpload;
import com.idega.documentmanager.component.properties.impl.ConstUpdateType;
import com.idega.documentmanager.manager.XFormsManagerMultiUpload;
/**
 * @author <a href="mailto:arunas@idega.com">Arūnas Vasmanas</a>
 * @version $Revision: 1.6 $
 *
 * Last modified: $Date: 2008/04/07 10:44:39 $ by $Author: arunas $
 */
public class FormComponentMultiUploadImpl extends FormComponentImpl implements ComponentMultiUpload{
	
	@Override
	public XFormsManagerMultiUpload getXFormsManager() {
		
		return getContext().getXformsManagerFactory().getXformsManagerMultiUpload();
	}
	
	@Override
	public void setReadonly(boolean readonly) {
		
		super.setReadonly(readonly);
		if(readonly) {

//			TODO: needs to transform to link list for downloading files
		}
	}
	
	@Override
	public PropertiesMultiUpload getProperties(){
		if(properties == null) {
			ComponentPropertiesMultiUpload properties = new ComponentPropertiesMultiUpload();
			properties.setComponent(this);
			this.properties = properties;
		}
		
		return (PropertiesMultiUpload)properties;
	    
	}
	
	@Override
	protected void setProperties() {
	    	
	    	super.setProperties();
		ComponentPropertiesMultiUpload properties = (ComponentPropertiesMultiUpload)getProperties();
		properties.setPlainRemoveButtonLabel(getXFormsManager().getRemoveButtonLabel(this));
		properties.setPlainAddButtonLabel(getXFormsManager().getAddButtonLabel(this));
	}
	
	@Override
	public void update(ConstUpdateType what) {
		
	    	getXFormsManager().update(this, what);
		
		switch (what) {
		case ADD_BUTTON_LABEL:
			getHtmlManager().clearHtmlComponents(this);
			getFormDocument().setFormDocumentModified(true);
			break;
			
		case REMOVE_BUTTON_LABEL:
			getHtmlManager().clearHtmlComponents(this);
			getFormDocument().setFormDocumentModified(true);
			break;
		case LABEL:
			getHtmlManager().clearHtmlComponents(this);
			getFormDocument().setFormDocumentModified(true);
			break;	
			

		default:
			break;
		}
	}
	
}