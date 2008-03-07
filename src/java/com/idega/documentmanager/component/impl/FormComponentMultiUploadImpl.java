package com.idega.documentmanager.component.impl;

import com.idega.documentmanager.business.component.ComponentMultiUpload;
import com.idega.documentmanager.manager.XFormsManagerMultiUpload;;
/**
 * @author <a href="mailto:arunas@idega.com">Arūnas Vasmanas</a>
 * @version $Revision: 1.3 $
 *
 * Last modified: $Date: 2008/03/07 13:44:15 $ by $Author: civilis $
 */
public class FormComponentMultiUploadImpl extends FormComponentImpl implements ComponentMultiUpload{
	
	@Override
	public XFormsManagerMultiUpload getXFormsManager() {
		
		return getContext().getXformsManagerFactory().getXformsManagerMultiUpload();
	}
}