package com.idega.documentmanager.component.properties.impl;

import com.idega.documentmanager.business.component.properties.PropertiesMultiUploadDescription;
import com.idega.documentmanager.component.beans.LocalizedStringBean;
/**
 * @author <a href="mailto:arunas@idega.com">Arūnas Vasmanas</a>
 * @version $Revision: 1.2 $
 * 
 * Last modified: $Date: 2008/07/31 09:58:08 $ by $Author: arunas $
 */
public class ComponentPropertiesMultiUploadDescription extends ComponentProperties implements PropertiesMultiUploadDescription{
    
    private LocalizedStringBean addButtonLabel;
    private LocalizedStringBean removeButtonLabel;
    private LocalizedStringBean descriptionLabel;
    private LocalizedStringBean uploadingFileDesc;
    
    public LocalizedStringBean getAddButtonLabel() {
	return addButtonLabel;
    }

    public void setAddButtonLabel(LocalizedStringBean addButtonLabel) {
	this.addButtonLabel = addButtonLabel;
	component.update(ConstUpdateType.ADD_BUTTON_LABEL);
    }

    public LocalizedStringBean getRemoveButtonLabel() {
	return removeButtonLabel;
    }

    public void setRemoveButtonLabel(LocalizedStringBean removeButtonLabel) {
	this.removeButtonLabel = removeButtonLabel;
	component.update(ConstUpdateType.REMOVE_BUTTON_LABEL);
    }
    
    public void setPlainRemoveButtonLabel(LocalizedStringBean removeButtonLabel) {
	this.removeButtonLabel = removeButtonLabel;
	
    }
    
    public void setPlainAddButtonLabel(LocalizedStringBean addButtonLabel) {
	this.addButtonLabel = addButtonLabel;
    }
    
    public void setPlainDescriptionButtonLabel(LocalizedStringBean descriptionButtonLabel) {
	this.descriptionLabel = descriptionButtonLabel;
	
    }
    
    public LocalizedStringBean getDescriptionLabel() {
	return descriptionLabel;
    }

    public void setDescriptionLabel(LocalizedStringBean descriptionButtonLabel) {
	this.descriptionLabel = descriptionButtonLabel;
	component.update(ConstUpdateType.DESCRIPTION_BUTTON_LABEL);
	
    }
    
    public LocalizedStringBean getUploadingFileDescription() {
	return uploadingFileDesc;
    }

    public void setUploadingFileDescription(LocalizedStringBean uploadingFileDesc) {
	this.uploadingFileDesc = uploadingFileDesc;
	component.update(ConstUpdateType.UPLOADING_FILE_DESC);
    }
    
    public void setPlainUploadingFileDescription(LocalizedStringBean uploadingFileDesc) {
	this.uploadingFileDesc = uploadingFileDesc;
    }
  

}
