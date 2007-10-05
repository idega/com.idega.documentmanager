package com.idega.documentmanager.manager.impl;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.documentmanager.business.component.properties.PropertiesDocument;
import com.idega.documentmanager.component.FormComponent;
import com.idega.documentmanager.component.FormDocument;
import com.idega.documentmanager.component.beans.XFormsComponentDataBean;
import com.idega.documentmanager.component.properties.impl.ConstUpdateType;
import com.idega.documentmanager.context.DMContext;
import com.idega.documentmanager.manager.XFormsManagerDocument;
import com.idega.documentmanager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2007/10/05 11:42:30 $ by $Author: civilis $
 */
public class XFormsManagerDocumentImpl extends XFormsManagerContainerImpl implements XFormsManagerDocument {

	protected Element autofill_action;
	protected Element form_data_model;
	protected Element sections_visualization_instance;
	
	public void setComponentsContainer(DMContext context, Element element) {
		
		FormComponent component = context.getComponent();
		
		XFormsComponentDataBean xformsComponentDataBean = newXFormsComponentDataBeanInstance();
		component.setXformsComponentDataBean(xformsComponentDataBean);
		
		xformsComponentDataBean.setElement(element);
	}
	
	public Element getAutofillAction(DMContext context) {
		
		if(autofill_action == null) {
			
			Document xforms_doc = context.getComponent().getFormDocument().getXformsDocument();
			
			Element autofill_model = 
				FormManagerUtil.getElementByIdFromDocument(xforms_doc, FormManagerUtil.head_tag, FormManagerUtil.autofill_model_id);
			
			if(autofill_model == null) {
				autofill_model = FormManagerUtil.getItemElementById(CacheManager.getInstance().getComponentsXforms(), "autofill-model");
				autofill_model = (Element)xforms_doc.importNode(autofill_model, true);
				Element head_element = (Element)xforms_doc.getElementsByTagName(FormManagerUtil.head_tag).item(0);
				autofill_model = (Element)head_element.appendChild(autofill_model);
				autofill_model.setAttribute(FormManagerUtil.id_att, FormManagerUtil.autofill_model_id);
				this.autofill_action = (Element)autofill_model.getElementsByTagName("*").item(0);
			} else
				autofill_action = autofill_model; 
		}
		
		return autofill_action;
	}
	
	public Element getFormDataModelElement(DMContext context) {
		
		if(form_data_model == null) {
			
			FormDocument formDocument = context.getComponent().getFormDocument();
			
			form_data_model = FormManagerUtil.getElementByIdFromDocument(formDocument.getXformsDocument(), FormManagerUtil.head_tag, formDocument.getFormId());

			if(form_data_model == null)
				throw new NullPointerException("Form model element not found. Incorrect xforms document.");
		}
		
		return form_data_model;
	}
	
	public Element getSectionsVisualizationInstanceElement(DMContext context) {

		if(sections_visualization_instance == null) {
			
			FormDocument formDocument = context.getComponent().getFormDocument();
		
			Element instance = FormManagerUtil.getElementByIdFromDocument(formDocument.getXformsDocument(), FormManagerUtil.head_tag, FormManagerUtil.sections_visualization_instance_id);
			
			if(instance == null) {
				
				instance = FormManagerUtil.getItemElementById(CacheManager.getInstance().getComponentsXforms(), FormManagerUtil.sections_visualization_instance_item);
				Document xforms_doc = formDocument.getXformsDocument(); 
				instance = (Element)xforms_doc.importNode(instance, true);
				Element data_model = FormManagerUtil.getElementByIdFromDocument(xforms_doc, FormManagerUtil.head_tag, FormManagerUtil.data_mod);
				instance = (Element)data_model.appendChild(instance);
			}
			
			sections_visualization_instance = instance;
		}
		
		return sections_visualization_instance;
	}
	
	
	public boolean getIsStepsVisualizationUsed(DMContext context) {
		
		Document xforms_doc = context.getComponent().getFormDocument().getXformsDocument();
		return null != FormManagerUtil.getElementByIdFromDocument(xforms_doc, FormManagerUtil.body_tag, FormManagerUtil.sections_visualization_id);
	}
	
	@Override
	public void update(DMContext context, ConstUpdateType what) {
		
		FormComponent component = context.getComponent();
		int update = what.getUpdateType();
		
		switch (update) {
			case ConstUpdateType.steps_visualization_used:
				updateStepsVisualizationUsed(component.getContext());
				break;
				
			default: 
				break;
		}
	}
	
	protected void updateStepsVisualizationUsed(DMContext context) {
		
		FormComponent component = context.getComponent();
		
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
			
			getSectionsVisualizationInstanceElement(component.getContext());
			
		} else {
			
			Element rem = FormManagerUtil.getElementByIdFromDocument(xforms_doc, FormManagerUtil.body_tag, FormManagerUtil.sections_visualization_id);
			
			if(rem != null)
				rem.getParentNode().removeChild(rem);
			
			if(sections_visualization_instance != null) {
				rem = sections_visualization_instance;
				sections_visualization_instance = null;
			} else
				rem = FormManagerUtil.getElementByIdFromDocument(xforms_doc, FormManagerUtil.body_tag, FormManagerUtil.sections_visualization_id);
			
			if(rem != null)
				rem.getParentNode().removeChild(rem);
		}
	}
}