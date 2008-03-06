package com.idega.documentmanager.manager;

import com.idega.documentmanager.component.FormComponent;
/**
 * @author <a href="mailto:arunas@idega.com">Arūnas Vasmanas</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2008/03/06 12:21:15 $ by $Author: arunas $
 */
public interface XFormsManagerMultiUpload extends XFormsManager{
	
	public abstract void loadXFormsComponentByTypeFromComponentsXForm(FormComponent component,
			String componentType) throws NullPointerException;

	public abstract void addComponentToDocument(FormComponent component);
	
	public abstract void removeMultiUploadComponentSourcesFromXFormsDocument(
			FormComponent component);

	
}
