package com.idega.documentmanager.manager;

import com.idega.documentmanager.component.FormComponent;
import com.idega.documentmanager.component.beans.LocalizedStringBean;
import com.idega.documentmanager.component.properties.impl.ConstUpdateType;
/**
 * @author <a href="mailto:arunas@idega.com">Arūnas Vasmanas</a>
 * @version $Revision: 1.5 $
 *
 * Last modified: $Date: 2008/07/31 09:58:08 $ by $Author: arunas $
 */
public interface XFormsManagerMultiUpload extends XFormsManager{
	
	public abstract void loadXFormsComponentByTypeFromComponentsXForm(FormComponent component,
			String componentType) throws NullPointerException;

	public abstract void addComponentToDocument(FormComponent component);
	
	public abstract LocalizedStringBean getRemoveButtonLabel(FormComponent component);
	
	public abstract LocalizedStringBean getAddButtonLabel(FormComponent component);
	
	public abstract LocalizedStringBean getUploadingFileDescription(FormComponent component);
	
	public abstract void update(FormComponent component, ConstUpdateType what);
}