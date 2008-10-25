package com.idega.documentmanager.manager.impl;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.chiba.xml.dom.DOMUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.idega.documentmanager.business.UnsupportedXFormException;
import com.idega.documentmanager.business.component.properties.PropertiesDocument;
import com.idega.documentmanager.component.FormComponent;
import com.idega.documentmanager.component.beans.ComponentDataBean;
import com.idega.documentmanager.component.beans.ComponentDocumentDataBean;
import com.idega.documentmanager.component.properties.impl.ConstUpdateType;
import com.idega.documentmanager.manager.XFormsManagerDocument;
import com.idega.documentmanager.util.FormManagerUtil;
import com.idega.util.xml.XPathUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.12 $
 *
 * Last modified: $Date: 2008/10/25 18:30:18 $ by $Author: civilis $
 */
public class XFormsManagerDocumentImpl extends XFormsManagerContainerImpl implements XFormsManagerDocument {
	
	private XPathUtil readonlyXPath = new XPathUtil(".//readonly");
	private XPathUtil generatePdfXPath = new XPathUtil(".//generatePdf");

	private XPathUtil controlXPath = new XPathUtil("./control");

	public void setComponentsContainer(FormComponent component, Element element) {
		
		ComponentDataBean xformsComponentDataBean = newXFormsComponentDataBeanInstance();
		component.setXformsComponentDataBean(xformsComponentDataBean);
		
		xformsComponentDataBean.setElement(element);
	}
	
	public Element getAutofillAction(FormComponent component) {
		
		ComponentDocumentDataBean componentDocumentDataBean = (ComponentDocumentDataBean)component.getXformsComponentDataBean();
		
		if(componentDocumentDataBean.getAutofillAction() == null) {
			
			Document xform = component.getFormDocument().getXformsDocument();
			
			Element autofillModel = FormManagerUtil.getElementById(xform, FormManagerUtil.autofill_model_id);
			
			if(autofillModel == null) {
				autofillModel = FormManagerUtil.getItemElementById(component.getFormDocument().getComponentsXforms(), "autofill-model");
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
	
	public void populateSubmissionDataWithXML(FormComponent component, Document submission, boolean clean) {
		
		Element mainDataInstance = getFormMainDataInstanceElement(component);
		
		if(clean) {

//			just replacing instance contents with submission
			@SuppressWarnings("unchecked")
			List<Element> oldChildren = DOMUtil.getChildElements(mainDataInstance);
			
			if(oldChildren != null) {
				
				for (Element element : oldChildren) {
				
					element.getParentNode().removeChild(element);
				}
			}
			
			@SuppressWarnings("unchecked")
			List<Element> newChildren = DOMUtil.getChildElements(submission);
			
			if(newChildren != null) {
				
				Document dataInstDoc = mainDataInstance.getOwnerDocument();
			
				for (Element element : newChildren) {
					
					Node node = dataInstDoc.importNode(element, true);
					mainDataInstance.appendChild(node);
				}
			}
			
		} else {
			
//			would replace only matching instance nodes
			throw new UnsupportedOperationException("Not supported yet");
		}
	}
	
	public Element getFormMainDataInstanceElement(FormComponent component) {
		
		ComponentDocumentDataBean componentDocumentDataBean = (ComponentDocumentDataBean)component.getXformsComponentDataBean();
		
		Element mainDataInstance = componentDocumentDataBean.getFormMainDataInstanceElement();
		
		if(mainDataInstance == null) {
		
			mainDataInstance = FormManagerUtil.getFormSubmissionInstanceElement(component.getFormDocument().getXformsDocument());
			componentDocumentDataBean.setFormMainDataInstanceElement(mainDataInstance);
		}
		
		if(mainDataInstance == null)
			throw new UnsupportedXFormException("Main data instance element not found in document");
		
		return mainDataInstance;
	}
	
	public Element getFormDataModelElement(FormComponent component) {
		
		ComponentDocumentDataBean componentDocumentDataBean = (ComponentDocumentDataBean)component.getXformsComponentDataBean();
		
		Element dataModel = componentDocumentDataBean.getFormDataModel();
		
		if(dataModel == null) {
		
			dataModel = FormManagerUtil.getElementById(component.getFormDocument().getXformsDocument(), FormManagerUtil.submission_model);
			componentDocumentDataBean.setFormDataModel(dataModel);
		}
		
		if(dataModel == null)
			throw new UnsupportedXFormException("Submission model element not found in document");
		
		return dataModel;
	}
	
	public Element getSectionsVisualizationInstanceElement(FormComponent component) {

		ComponentDocumentDataBean componentDocumentDataBean = (ComponentDocumentDataBean)component.getXformsComponentDataBean();
		
		if(componentDocumentDataBean.getSectionsVisualizationInstance() == null) {
			
			Document xforms_doc = component.getFormDocument().getXformsDocument();
			
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
		
		Document xforms_doc = component.getFormDocument().getXformsDocument();
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
		Element submissionElement = FormManagerUtil.getSubmissionElement(component.getFormDocument().getXformsDocument());
		submissionElement.setAttribute(FormManagerUtil.action_att, props.getSubmissionAction());
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
	
	public String getSubmissionAction(FormComponent component) {
		
		Element submission = FormManagerUtil.getSubmissionElement(component.getFormDocument().getXformsDocument());
		return submission.getAttribute(FormManagerUtil.action_att);
	}
	
//	@Override
	public boolean isReadonly(FormComponent component) {
		
		Document xformsDoc = component.getFormDocument().getXformsDocument();
		
		Element controlInstance = FormManagerUtil.getElementById(xformsDoc, FormManagerUtil.controlInstanceID);
		
		if(controlInstance != null) {

			Element readonlyElement = readonlyXPath.getNode(controlInstance);
			
			if(readonlyElement != null) {
				
				return FormManagerUtil.true_string.equals(readonlyElement.getTextContent());
			}
		}
		
		return false;
	}
	
//	@Override
	public void setReadonly(FormComponent component, boolean readonly) {
		
		Document xformsDoc = component.getFormDocument().getXformsDocument();
		
		Element controlInstance = FormManagerUtil.getElementById(xformsDoc, FormManagerUtil.controlInstanceID);
		
		if(controlInstance != null) {

			Element readonlyElement = readonlyXPath.getNode(controlInstance);
			
			if(readonlyElement == null) {
				
				readonlyElement = controlInstance.getOwnerDocument().createElement("readonly");
				Element controlElement = controlXPath.getNode(controlInstance);
				readonlyElement = (Element)controlElement.appendChild(readonlyElement);
				readonlyElement.setTextContent(readonly ? FormManagerUtil.true_string : FormManagerUtil.false_string);
			}else 
				readonlyElement.setTextContent(readonly ? FormManagerUtil.true_string : FormManagerUtil.false_string);
			
			
		} else {
			Logger.getLogger(getClass().getName()).log(Level.WARNING, "setReadonly called on form, but no control instance found. Ignoring.");
		}
	}
	
//	@Override
	public void setPdfForm(FormComponent component, boolean generatePdf) {
		
		Document xformsDoc = component.getFormDocument().getXformsDocument();
		
		Element controlInstance = FormManagerUtil.getElementById(xformsDoc, FormManagerUtil.controlInstanceID);

		if(controlInstance != null) {

			Element generatePdfElement = generatePdfXPath.getNode(controlInstance);
			if(generatePdfElement == null) {
				
				generatePdfElement = controlInstance.getOwnerDocument().createElement("generatePdf");
				Element controlElement = controlXPath.getNode(controlInstance);
				generatePdfElement = (Element)controlElement.appendChild(generatePdfElement);
				generatePdfElement.setTextContent(generatePdf ? FormManagerUtil.true_string : FormManagerUtil.false_string);

			}else 
				generatePdfElement.setTextContent(generatePdf ? FormManagerUtil.true_string : FormManagerUtil.false_string);
			
		} else {
			Logger.getLogger(getClass().getName()).log(Level.WARNING, "setPdfForm called on form, but no control instance found. Ignoring.");
		}
	}
	
//	@Override
	public boolean isPdfForm(FormComponent component) {
		Document xformsDoc = component.getFormDocument().getXformsDocument();
		
		Element controlInstance = FormManagerUtil.getElementById(xformsDoc, FormManagerUtil.controlInstanceID);
		
		if(controlInstance != null) {

			Element generatePdfElement = generatePdfXPath.getNode(controlInstance);
			
			if(generatePdfElement != null) {
				
				return FormManagerUtil.true_string.equals(generatePdfElement.getTextContent());
			}
		}
		
		return false;
	}
}