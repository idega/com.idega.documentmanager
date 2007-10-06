package com.idega.documentmanager.manager.impl;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.documentmanager.business.component.properties.PropertiesDocument;
import com.idega.documentmanager.component.FormComponent;
import com.idega.documentmanager.component.FormDocument;
import com.idega.documentmanager.component.beans.ComponentDataBean;
import com.idega.documentmanager.component.beans.ComponentDocumentDataBean;
import com.idega.documentmanager.component.properties.impl.ConstUpdateType;
import com.idega.documentmanager.manager.XFormsManagerDocument;
import com.idega.documentmanager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.4 $
 *
 * Last modified: $Date: 2007/10/06 07:05:40 $ by $Author: civilis $
 */
public class XFormsManagerDocumentImpl extends XFormsManagerContainerImpl implements XFormsManagerDocument {

	public void setComponentsContainer(FormComponent component, Element element) {
		
		ComponentDataBean xformsComponentDataBean = newXFormsComponentDataBeanInstance();
		component.setXformsComponentDataBean(xformsComponentDataBean);
		
		xformsComponentDataBean.setElement(element);
	}
	
	public Element getAutofillAction(FormComponent component) {
		
		ComponentDocumentDataBean componentDocumentDataBean = (ComponentDocumentDataBean)component.getXformsComponentDataBean();
		
		if(componentDocumentDataBean.getAutofillAction() == null) {
			
			Document xforms_doc = component.getFormDocument().getXformsDocument();
			
			Element autofill_model = 
				FormManagerUtil.getElementByIdFromDocument(xforms_doc, FormManagerUtil.head_tag, FormManagerUtil.autofill_model_id);
			
			if(autofill_model == null) {
				autofill_model = FormManagerUtil.getItemElementById(CacheManager.getInstance().getComponentsXforms(), "autofill-model");
				autofill_model = (Element)xforms_doc.importNode(autofill_model, true);
				Element head_element = (Element)xforms_doc.getElementsByTagName(FormManagerUtil.head_tag).item(0);
				autofill_model = (Element)head_element.appendChild(autofill_model);
				autofill_model.setAttribute(FormManagerUtil.id_att, FormManagerUtil.autofill_model_id);
				componentDocumentDataBean.setAutofillAction((Element)autofill_model.getElementsByTagName("*").item(0));
			} else
				componentDocumentDataBean.setAutofillAction(autofill_model); 
		}
		
		return componentDocumentDataBean.getAutofillAction();
	}
	
	public Element getFormDataModelElement(FormComponent component) {
		
		ComponentDocumentDataBean componentDocumentDataBean = (ComponentDocumentDataBean)component.getXformsComponentDataBean();
		
		if(componentDocumentDataBean.getFormDataModel() == null) {
			
			FormDocument formDocument = component.getFormDocument();
			
			componentDocumentDataBean.setFormDataModel(FormManagerUtil.getElementByIdFromDocument(formDocument.getXformsDocument(), FormManagerUtil.head_tag, formDocument.getFormId()));

			if(componentDocumentDataBean.getFormDataModel() == null)
				throw new NullPointerException("Form model element not found. Incorrect xforms document.");
		}
		
		return componentDocumentDataBean.getFormDataModel();
	}
	
	public Element getSectionsVisualizationInstanceElement(FormComponent component) {

		ComponentDocumentDataBean componentDocumentDataBean = (ComponentDocumentDataBean)component.getXformsComponentDataBean();
		
		if(componentDocumentDataBean.getSectionsVisualizationInstance() == null) {
			
			FormDocument formDocument = component.getFormDocument();
		
			Element instance = FormManagerUtil.getElementByIdFromDocument(formDocument.getXformsDocument(), FormManagerUtil.head_tag, FormManagerUtil.sections_visualization_instance_id);
			
			if(instance == null) {
				
				instance = FormManagerUtil.getItemElementById(CacheManager.getInstance().getComponentsXforms(), FormManagerUtil.sections_visualization_instance_item);
				Document xforms_doc = formDocument.getXformsDocument(); 
				instance = (Element)xforms_doc.importNode(instance, true);
				Element data_model = FormManagerUtil.getElementByIdFromDocument(xforms_doc, FormManagerUtil.head_tag, FormManagerUtil.data_mod);
				instance = (Element)data_model.appendChild(instance);
			}
			
			componentDocumentDataBean.setSectionsVisualizationInstance(instance);
		}
		
		return componentDocumentDataBean.getSectionsVisualizationInstance();
	}
	
	
	public boolean getIsStepsVisualizationUsed(FormComponent component) {
		
		Document xforms_doc = component.getFormDocument().getXformsDocument();
		return null != FormManagerUtil.getElementByIdFromDocument(xforms_doc, FormManagerUtil.body_tag, FormManagerUtil.sections_visualization_id);
	}
	
	@Override
	public void update(FormComponent component, ConstUpdateType what) {
		
		int update = what.getUpdateType();
		
		switch (update) {
			case ConstUpdateType.steps_visualization_used:
				updateStepsVisualizationUsed(component);
				break;
				
			default: 
				break;
		}
	}
	
	protected void updateStepsVisualizationUsed(FormComponent component) {
		
		PropertiesDocument props = (PropertiesDocument)component.getProperties();
		Document xforms_doc = component.getFormDocument().getXformsDocument();
		
		if(props.isStepsVisualizationUsed()) {

			Element add = FormManagerUtil.getElementByIdFromDocument(xforms_doc, FormManagerUtil.body_tag, FormManagerUtil.sections_visualization_id);
			
			if(add == null) {

				add = FormManagerUtil.getItemElementById(CacheManager.getInstance().getComponentsXforms(), FormManagerUtil.sections_visualization_item);
				add = (Element)xforms_doc.importNode(add, true);
				Element switch_el = FormManagerUtil.getComponentsContainerElement(xforms_doc);
				switch_el.getParentNode().insertBefore(add, switch_el);
			}
			
			getSectionsVisualizationInstanceElement(component);
			
		} else {
			
			ComponentDocumentDataBean componentDocumentDataBean = (ComponentDocumentDataBean)component.getXformsComponentDataBean();
			
			Element rem = FormManagerUtil.getElementByIdFromDocument(xforms_doc, FormManagerUtil.body_tag, FormManagerUtil.sections_visualization_id);
			
			if(rem != null)
				rem.getParentNode().removeChild(rem);
			
			if(componentDocumentDataBean.getSectionsVisualizationInstance() != null) {
				rem = componentDocumentDataBean.getSectionsVisualizationInstance();
				componentDocumentDataBean.setSectionsVisualizationInstance(null);
			} else
				rem = FormManagerUtil.getElementByIdFromDocument(xforms_doc, FormManagerUtil.body_tag, FormManagerUtil.sections_visualization_id);
			
			if(rem != null)
				rem.getParentNode().removeChild(rem);
		}
	}
	
	@Override
	protected ComponentDataBean newXFormsComponentDataBeanInstance() {
		return new ComponentDocumentDataBean();
	}
}