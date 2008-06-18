package com.idega.documentmanager.component.beans;

import org.w3c.dom.Element;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2008/06/18 08:01:30 $ by $Author: civilis $
 */
public class ComponentDocumentDataBean extends ComponentDataBean {

	private Element autofillAction;
	private Element formDataModel;
	private Element formMainDataInstanceElement;
	private Element sectionsVisualizationInstance;
	
	public Element getAutofillAction() {
		return autofillAction;
	}

	public void setAutofillAction(Element autofillAction) {
		this.autofillAction = autofillAction;
	}

	public Element getFormDataModel() {
		return formDataModel;
	}

	public void setFormDataModel(Element formDataModel) {
		this.formDataModel = formDataModel;
	}

	public Element getSectionsVisualizationInstance() {
		return sectionsVisualizationInstance;
	}

	public void setSectionsVisualizationInstance(
			Element sectionsVisualizationInstance) {
		this.sectionsVisualizationInstance = sectionsVisualizationInstance;
	}

	@Override
	public Object clone() {
		
		ComponentDocumentDataBean clone = (ComponentDocumentDataBean)super.clone();
		
		try {
			clone = (ComponentDocumentDataBean)super.clone();
			
		} catch (Exception e) {
			
			clone = new ComponentDocumentDataBean();
		}
		
		if(getAutofillAction() != null)
			clone.setAutofillAction((Element)getAutofillAction().cloneNode(true));
		
		if(getFormDataModel() != null)
			clone.setFormDataModel((Element)getFormDataModel().cloneNode(true));
		
		if(getSectionsVisualizationInstance() != null)
			clone.setSectionsVisualizationInstance((Element)getSectionsVisualizationInstance().cloneNode(true));
		
		return clone;
	}
	
	@Override
	protected ComponentDataBean getDataBeanInstance() {
		
		return new ComponentDocumentDataBean();
	}

	public Element getFormMainDataInstanceElement() {
		return formMainDataInstanceElement;
	}

	public void setFormMainDataInstanceElement(Element formMainDataInstanceElement) {
		this.formMainDataInstanceElement = formMainDataInstanceElement;
	}
}
