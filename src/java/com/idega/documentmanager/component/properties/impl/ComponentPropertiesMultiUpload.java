package com.idega.documentmanager.component.properties.impl;

import com.idega.documentmanager.business.component.properties.PropertiesMultiUpload;
import com.idega.documentmanager.component.beans.LocalizedStringBean;

public class ComponentPropertiesMultiUpload extends ComponentProperties implements PropertiesMultiUpload{
    
    private LocalizedStringBean addButtonLabel;
    private LocalizedStringBean removeButtonLabel;
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
