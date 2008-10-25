package com.idega.documentmanager.manager;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.documentmanager.component.FormComponent;
import com.idega.documentmanager.component.properties.impl.ConstUpdateType;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.5 $
 *
 * Last modified: $Date: 2008/10/25 18:30:19 $ by $Author: civilis $
 */
public interface XFormsManagerDocument extends XFormsManagerContainer {

	public abstract void setComponentsContainer(FormComponent component,
			Element element);

	public abstract Element getAutofillAction(FormComponent component);

	public abstract Element getFormDataModelElement(FormComponent component);
	
	public abstract Element getFormMainDataInstanceElement(FormComponent component);

	public abstract Element getSectionsVisualizationInstanceElement(
			FormComponent component);

	public abstract boolean getIsStepsVisualizationUsed(FormComponent component);

	public abstract void update(FormComponent component, ConstUpdateType what);

	public abstract String getSubmissionAction(FormComponent component);
	
	public abstract void populateSubmissionDataWithXML(FormComponent component, Document submission, boolean clean);
	
	public abstract void setReadonly(FormComponent component, boolean readonly);
	
	public abstract boolean isReadonly(FormComponent component);
	
	public abstract boolean isPdfForm(FormComponent component); 

	public void setPdfForm(FormComponent component, boolean generatePdf);
}