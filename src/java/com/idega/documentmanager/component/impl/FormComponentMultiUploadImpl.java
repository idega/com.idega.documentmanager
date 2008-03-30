package com.idega.documentmanager.component.impl;

import com.idega.documentmanager.business.component.ComponentMultiUpload;
import com.idega.documentmanager.manager.XFormsManagerMultiUpload;;
/**
 * @author <a href="mailto:arunas@idega.com">Arūnas Vasmanas</a>
 * @version $Revision: 1.4 $
 *
 * Last modified: $Date: 2008/03/30 15:22:16 $ by $Author: civilis $
 */
public class FormComponentMultiUploadImpl extends FormComponentImpl implements ComponentMultiUpload{
	
	@Override
	public XFormsManagerMultiUpload getXFormsManager() {
		
		return getContext().getXformsManagerFactory().getXformsManagerMultiUpload();
	}
	
	@Override
	public void setReadonly(boolean readonly) {
		
		if(readonly) {

//			TODO: temporary, needs to transform to link list for downloading files
			remove();
		}
	}
}