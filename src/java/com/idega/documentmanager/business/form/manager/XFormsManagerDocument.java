package com.idega.documentmanager.business.form.manager;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.documentmanager.business.form.manager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 */
public class XFormsManagerDocument extends XFormsManagerContainer {

	protected Element autofill_action;
	protected Element form_data_model;
	
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
			}
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
}