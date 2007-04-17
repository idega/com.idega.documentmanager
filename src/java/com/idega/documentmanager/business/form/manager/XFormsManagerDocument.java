package com.idega.documentmanager.business.form.manager;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.idega.documentmanager.business.form.PropertiesDocument;
import com.idega.documentmanager.business.form.beans.ConstUpdateType;
import com.idega.documentmanager.business.form.manager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 */
public class XFormsManagerDocument extends XFormsManagerContainer {

	protected Element autofill_action;
	protected Element form_data_model;
	protected Element sections_visualization_instance;
	
	public void setComponentsContainer(Element element) {
		
		xforms_component = newXFormsComponentDataBeanInstance();
		xforms_component.setElement(element);
	}
	
	public Element getAutofillAction() {
		
		if(autofill_action == null) {
			
			Document xforms_doc = form_document.getXformsDocument();
			
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
	
	public Element getFormDataModelElement() {
		
		if(form_data_model == null) {
			form_data_model = FormManagerUtil.getElementByIdFromDocument(form_document.getXformsDocument(), FormManagerUtil.head_tag, form_document.getFormId());

			if(form_data_model == null)
				throw new NullPointerException("Form model element not found. Incorrect xforms document.");
		}
		
		return form_data_model;
	}
	
	public Element getSectionsVisualizationInstanceElement() {

		if(sections_visualization_instance == null) {
		
			Element instance = FormManagerUtil.getElementByIdFromDocument(form_document.getXformsDocument(), FormManagerUtil.head_tag, FormManagerUtil.sections_visualization_instance_id);
			
			if(instance == null) {
				
				instance = FormManagerUtil.getItemElementById(CacheManager.getInstance().getComponentsXforms(), FormManagerUtil.sections_visualization_instance_item);
				Document xforms_doc = form_document.getXformsDocument(); 
				instance = (Element)xforms_doc.importNode(instance, true);
				Element data_model = FormManagerUtil.getElementByIdFromDocument(xforms_doc, FormManagerUtil.head_tag, FormManagerUtil.data_mod);
				instance = (Element)data_model.appendChild(instance);
			}
			
			sections_visualization_instance = instance;
		}
		
		return sections_visualization_instance;
	}
	
	
	public boolean getIsStepsVisualizationUsed() {
		
		Document xforms_doc = form_document.getXformsDocument();
		return null != FormManagerUtil.getElementByIdFromDocument(xforms_doc, FormManagerUtil.body_tag, FormManagerUtil.sections_visualization_id);
	}
	
	@Override
	public void update(ConstUpdateType what) {
		
		int update = what.getUpdateType();
		
		switch (update) {
			case ConstUpdateType.steps_visualization_used:
				updateStepsVisualizationUsed();
				break;
				
			default: 
				break;
		}
	}
	
	protected void updateStepsVisualizationUsed() {
		
		PropertiesDocument props = (PropertiesDocument)component.getProperties();
		Document xforms_doc = form_document.getXformsDocument();
		
		if(props.isStepsVisualizationUsed()) {

			Element add = FormManagerUtil.getElementByIdFromDocument(xforms_doc, FormManagerUtil.body_tag, FormManagerUtil.sections_visualization_id);
			
			if(add == null) {

				add = FormManagerUtil.getItemElementById(CacheManager.getInstance().getComponentsXforms(), FormManagerUtil.sections_visualization_item);
				add = (Element)xforms_doc.importNode(add, true);
				Element switch_el = FormManagerUtil.getComponentsContainerElement(xforms_doc);
				switch_el.getParentNode().insertBefore(add, switch_el);
			}
			
			getSectionsVisualizationInstanceElement();
			
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