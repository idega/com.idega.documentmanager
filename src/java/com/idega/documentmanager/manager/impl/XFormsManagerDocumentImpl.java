package com.idega.documentmanager.manager.impl;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.documentmanager.business.component.properties.PropertiesDocument;
import com.idega.documentmanager.component.FormComponent;
import com.idega.documentmanager.component.beans.ComponentDataBean;
import com.idega.documentmanager.component.beans.ComponentDocumentDataBean;
import com.idega.documentmanager.component.properties.impl.ConstUpdateType;
import com.idega.documentmanager.manager.XFormsManagerDocument;
import com.idega.documentmanager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.7 $
 *
 * Last modified: $Date: 2007/11/15 09:24:15 $ by $Author: civilis $
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
			
			Document xform = component.getContext().getXformsXmlDoc();
			
			Element autofillModel = FormManagerUtil.getElementById(xform, FormManagerUtil.autofill_model_id);
			
			if(autofillModel == null) {
				autofillModel = FormManagerUtil.getItemElementById(component.getContext().getCacheManager().getComponentsXforms(), "autofill-model");
				autofillModel = (Element)xform.importNode(autofillModel, true);
				Element headElement = (Element)xform.getElementsByTagName(FormManagerUtil.head_tag).item(0);
				autofillModel = (Element)headElement.appendChild(autofillModel);
				autofillModel.setAttribute(FormManagerUtil.id_att, FormManagerUtil.autofill_model_id);
				componentDocumentDataBean.setAutofillAction((Element)autofillModel.getElementsByTagName("*").item(0));
			} else
				componentDocumentDataBean.setAutofillAction(autofillModel); 
		}
		
		return componentDocumentDataBean.getAutofillAction();
	}
	
	public Element getFormDataModelElement(FormComponent component) {
		
		ComponentDocumentDataBean componentDocumentDataBean = (ComponentDocumentDataBean)component.getXformsComponentDataBean();
		
		if(componentDocumentDataBean.getFormDataModel() == null) {
			
			componentDocumentDataBean.setFormDataModel(FormManagerUtil.getElementById(component.getContext().getXformsXmlDoc(), FormManagerUtil.submission_model));

			if(componentDocumentDataBean.getFormDataModel() == null)
				throw new NullPointerException("Submission model element not found by submission model id: "+FormManagerUtil.submission_model);
		}
		
		return componentDocumentDataBean.getFormDataModel();
	}
	
	public Element getSectionsVisualizationInstanceElement(FormComponent component) {

		ComponentDocumentDataBean componentDocumentDataBean = (ComponentDocumentDataBean)component.getXformsComponentDataBean();
		
		if(componentDocumentDataBean.getSectionsVisualizationInstance() == null) {
			
			Document xforms_doc = component.getContext().getXformsXmlDoc();
			
			Element instance = FormManagerUtil.getElementByIdFromDocument(xforms_doc, FormManagerUtil.head_tag, FormManagerUtil.sections_visualization_instance_id);
			
			if(instance == null) {
				
				instance = FormManagerUtil.getItemElementById(CacheManager.getInstance().getComponentsXforms(), FormManagerUtil.sections_visualization_instance_item);
				instance = (Element)xforms_doc.importNode(instance, true);
				Element data_model = FormManagerUtil.getElementById(xforms_doc, FormManagerUtil.data_mod);
				instance = (Element)data_model.appendChild(instance);
			}
			
			componentDocumentDataBean.setSectionsVisualizationInstance(instance);
		}
		
		return componentDocumentDataBean.getSectionsVisualizationInstance();
	}
	
	
	public boolean getIsStepsVisualizationUsed(FormComponent component) {
		
		Document xforms_doc = component.getContext().getXformsXmlDoc();
		return null != FormManagerUtil.getElementByIdFromDocument(xforms_doc, FormManagerUtil.body_tag, FormManagerUtil.sections_visualization_id);
	}
	
	@Override
	public void update(FormComponent component, ConstUpdateType what) {
		
		switch (what) {
			case STEPS_VISUALIZATION_USED:
				updateStepsVisualizationUsed(component);
				break;
			
			case SUBMISSION_ACTION:
				updateSubmissionAction(component);
				break;
				
			default: 
				break;
		}
	}
	
	protected void updateSubmissionAction(FormComponent component) {
		
		PropertiesDocument props = (PropertiesDocument)component.getProperties();
		Element submissionElement = FormManagerUtil.getSubmissionElement(component.getContext().getXformsXmlDoc());
		submissionElement.setAttribute(FormManagerUtil.action_att, props.getSubmissionAction());
	}
	
	protected void updateStepsVisualizationUsed(FormComponent component) {
		
		PropertiesDocument props = (PropertiesDocument)component.getProperties();
		Document xforms_doc = component.getContext().getXformsXmlDoc();
		
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
	
	public String getSubmissionAction(FormComponent component) {
		
		Element submission = FormManagerUtil.getSubmissionElement(component.getContext().getXformsXmlDoc());
		return submission.getAttribute(FormManagerUtil.action_att);
	}
}