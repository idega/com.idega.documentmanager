package com.idega.documentmanager.business.component.properties;

import com.idega.documentmanager.component.beans.LocalizedStringBean;
/**
 * @author <a href="mailto:arunas@idega.com">Arūnas Vasmanas</a>
 * @version $Revision: 1.2 $
 * 
 * Last modified: $Date: 2008/07/31 09:58:08 $ by $Author: arunas $
 */
public interface PropertiesMultiUploadDescription extends PropertiesComponent{
    
    public abstract LocalizedStringBean getRemoveButtonLabel();

    public abstract void setRemoveButtonLabel(LocalizedStringBean removeButtonLabel);
    
    public abstract LocalizedStringBean getAddButtonLabel();

    public abstract void setAddButtonLabel(LocalizedStringBean addButtonLabel);
    
    public abstract LocalizedStringBean getDescriptionLabel();

    public abstract void setDescriptionLabel(LocalizedStringBean addButtonLabel);
    
    public abstract LocalizedStringBean getUploadingFileDescription();

    public abstract void setUploadingFileDescription(LocalizedStringBean addButtonLabel);
    
    
}
