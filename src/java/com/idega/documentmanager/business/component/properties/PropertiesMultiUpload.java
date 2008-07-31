package com.idega.documentmanager.business.component.properties;

import com.idega.documentmanager.component.beans.LocalizedStringBean;

public interface PropertiesMultiUpload extends PropertiesComponent{
    
    public abstract LocalizedStringBean getRemoveButtonLabel();

    public abstract void setRemoveButtonLabel(LocalizedStringBean removeButtonLabel);
    
    public abstract LocalizedStringBean getAddButtonLabel();

    public abstract void setAddButtonLabel(LocalizedStringBean addButtonLabel);
    
    public abstract LocalizedStringBean getUploadingFileDescription();

    public abstract void setUploadingFileDescription(LocalizedStringBean addButtonLabel);
    
    
}
