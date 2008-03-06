package com.idega.documentmanager.component.impl;

import com.idega.documentmanager.business.component.ComponentMultiUpload;
import com.idega.documentmanager.manager.XFormsManagerMultiUpload;;

public class FormComponentMultiUploadImpl extends FormComponentImpl implements ComponentMultiUpload{
	
	@Override
	public XFormsManagerMultiUpload getXFormsManager() {
		
		return getContext().getXformsManagerFactory().getXformsManagerMultiUpload();
	}
	
	public void remove() {
		getXFormsManager().removeMultiUploadComponentSourcesFromXFormsDocument(this);    
		super.remove();
	}
}
