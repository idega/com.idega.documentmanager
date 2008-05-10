package com.idega.documentmanager.component.impl;

import com.idega.documentmanager.business.component.ComponentMultiUploadDescription;
import com.idega.documentmanager.business.component.properties.PropertiesMultiUploadDescription;
import com.idega.documentmanager.component.properties.impl.ComponentPropertiesMultiUploadDescription;
import com.idega.documentmanager.component.properties.impl.ConstUpdateType;
import com.idega.documentmanager.manager.XFormsManagerMultiUploadDescription;
/**
 * @author <a href="mailto:arunas@idega.com">Arūnas Vasmanas</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2008/05/10 11:49:08 $ by $Author: arunas $
 */
public class FormComponentMultiUploadDescriptionImpl extends FormComponentImpl implements ComponentMultiUploadDescription{
	
	@Override
	public XFormsManagerMultiUploadDescription getXFormsManager() {
		
		return getContext().getXformsManagerFactory().getXformsManagerMultiUploadDescription();
	}
	
	@Override
	public void setReadonly(boolean readonly) {
		
		super.setReadonly(readonly);
		if(readonly) {

//			TODO: needs to transform to link list for downloading files
		}
	}
	
	@Override
	public PropertiesMultiUploadDescription getProperties(){
		if(properties == null) {
			ComponentPropertiesMultiUploadDescription properties = new ComponentPropertiesMultiUploadDescription();
			properties.setComponent(this);
			this.properties = properties;
		}
		
		return (PropertiesMultiUploadDescription)properties;
	    
	}
	
	@Override
	protected void setProperties() {
	    	
	    	super.setProperties();
		ComponentPropertiesMultiUploadDescription properties = (ComponentPropertiesMultiUploadDescription)getProperties();
		properties.setPlainRemoveButtonLabel(getXFormsManager().getRemoveButtonLabel(this));
		properties.setPlainAddButtonLabel(getXFormsManager().getAddButtonLabel(this));
		properties.setPlainDescriptionButtonLabel(getXFormsManager().getDescriptionButtonLabel(this));
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
		case DESCRIPTION_BUTTON_LABEL:
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