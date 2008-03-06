package com.idega.documentmanager.manager;

import com.idega.documentmanager.component.FormComponent;

public interface XFormsManagerMultiUpload extends XFormsManager{
	
	public abstract void loadXFormsComponentByTypeFromComponentsXForm(FormComponent component,
			String componentType) throws NullPointerException;

	public abstract void addComponentToDocument(FormComponent component);
	
	public abstract void removeMultiUploadComponentSourcesFromXFormsDocument(
			FormComponent component);

	
}
