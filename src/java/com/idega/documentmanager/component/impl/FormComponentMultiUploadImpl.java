package com.idega.documentmanager.component.impl;

import com.idega.documentmanager.business.component.ComponentMultiUpload;
import com.idega.documentmanager.manager.XFormsManagerMultiUpload;;
/**
 * @author <a href="mailto:arunas@idega.com">Arūnas Vasmanas</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2008/03/06 12:21:21 $ by $Author: arunas $
 */
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
