package com.idega.documentmanager.component.impl;

import com.idega.documentmanager.business.component.ComponentMultiUpload;
import com.idega.documentmanager.manager.XFormsManagerMultiUpload;;
/**
 * @author <a href="mailto:arunas@idega.com">Arūnas Vasmanas</a>
 * @version $Revision: 1.5 $
 *
 * Last modified: $Date: 2008/03/30 15:44:00 $ by $Author: civilis $
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
}