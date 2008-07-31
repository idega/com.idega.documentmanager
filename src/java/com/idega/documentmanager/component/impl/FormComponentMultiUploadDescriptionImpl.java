package com.idega.documentmanager.component.impl;

import org.w3c.dom.Element;

import com.idega.documentmanager.business.component.ComponentMultiUploadDescription;
import com.idega.documentmanager.business.component.properties.PropertiesMultiUploadDescription;
import com.idega.documentmanager.component.properties.impl.ComponentPropertiesMultiUploadDescription;
import com.idega.documentmanager.component.properties.impl.ConstUpdateType;
import com.idega.documentmanager.manager.XFormsManagerMultiUploadDescription;
import com.idega.documentmanager.util.FormManagerUtil;
import com.idega.util.CoreConstants;
/**
 * @author <a href="mailto:arunas@idega.com">Arūnas Vasmanas</a>
 * @version $Revision: 1.3 $
 *
 * Last modified: $Date: 2008/07/31 09:58:08 $ by $Author: arunas $
 */
public class FormComponentMultiUploadDescriptionImpl extends FormComponentImpl implements ComponentMultiUploadDescription{
	
	@Override
	public XFormsManagerMultiUploadDescription getXFormsManager() {
		
		return getContext().getXformsManagerFactory().getXformsManagerMultiUploadDescription();
	}
	
	@Override
	public void setReadonly(boolean readonly) {
		
		super.setReadonly(readonly);
		
		Element bindElement = this.getXformsComponentDataBean().getBind().getBindElement();
		Element groupElem = this.getXformsComponentDataBean().getElement();
		
		if(readonly) {
		    bindElement.setAttribute(FormManagerUtil.relevant_att, FormManagerUtil.xpath_false);
		    groupElem.setAttribute(FormManagerUtil.bind_att, bindElement.getAttribute("id"));
//			TODO: needs to transform to link list for downloading files
		}else {
		    
		    if (!CoreConstants.EMPTY.equals(bindElement.getAttribute(FormManagerUtil.relevant_att)))
			bindElement.removeAttribute(FormManagerUtil.relevant_att);
		    	groupElem.removeAttribute(FormManagerUtil.bind_att);
		    
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
		properties.setPlainUploadingFileDescription(getXFormsManager().getUploadingFileDescription(this));
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
		case UPLOADING_FILE_DESC:
		    	getHtmlManager().clearHtmlComponents(this);
			getFormDocument().setFormDocumentModified(true);
			break;

		default:
			break;
		}
	}
	
}