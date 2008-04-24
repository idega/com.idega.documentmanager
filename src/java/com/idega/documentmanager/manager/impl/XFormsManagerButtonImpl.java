package com.idega.documentmanager.manager.impl;

import org.chiba.xml.dom.DOMUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.idega.documentmanager.business.component.ConstButtonType;
import com.idega.documentmanager.business.component.properties.PropertiesButton;
import com.idega.documentmanager.component.FormComponent;
import com.idega.documentmanager.component.FormComponentButtonArea;
import com.idega.documentmanager.component.FormComponentPage;
import com.idega.documentmanager.component.FormDocument;
import com.idega.documentmanager.component.beans.ComponentButtonDataBean;
import com.idega.documentmanager.component.beans.ComponentDataBean;
import com.idega.documentmanager.component.impl.FormComponentFactory;
import com.idega.documentmanager.component.properties.impl.ConstUpdateType;
import com.idega.documentmanager.manager.XFormsManagerButton;
import com.idega.documentmanager.util.FormManagerUtil;
import com.idega.documentmanager.xform.Bind;
import com.idega.documentmanager.xform.Nodeset;
import com.idega.util.xml.XPathUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.12 $
 *
 * Last modified: $Date: 2008/04/24 23:49:12 $ by $Author: laddi $
 */
public class XFormsManagerButtonImpl extends XFormsManagerImpl implements XFormsManagerButton {
	
	private static final String actionTaken = "actionTaken";
	private static final String bindIdVariable = "bindId";
	
	private XPathUtil sendSubmissionXPath;
	
	@Override
	public void loadXFormsComponentFromDocument(FormComponent component) {
		
		super.loadXFormsComponentFromDocument(component);
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		Element button_element = xformsComponentDataBean.getElement();
		String button_type = button_element.getAttribute(FormManagerUtil.name_att);
		
		if(button_type != null)
			component.setType(button_type);

		loadToggleElement(component);
	}
	
	protected void loadToggleElement(FormComponent component) {
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		ComponentButtonDataBean xFormsComponentButtonDataBean = (ComponentButtonDataBean)xformsComponentDataBean;
		
		NodeList toggles = xFormsComponentButtonDataBean.getElement().getElementsByTagName(FormManagerUtil.toggle_tag);
		
		if(toggles == null)
			return;
		
		Element toggle_element = (Element)toggles.item(0);
		xFormsComponentButtonDataBean.setToggleElement(toggle_element);
	}
	
	@Override
	protected ComponentDataBean newXFormsComponentDataBeanInstance() {
		return new ComponentButtonDataBean();
	}
	
	public void renewButtonPageContextPages(FormComponent component, FormComponentPage previous, FormComponentPage next) {
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		Element toggle_element = ((ComponentButtonDataBean)xformsComponentDataBean).getToggleElement();
		
		if(toggle_element == null)
			toggle_element = createToggleElement(component);
		
		if(!component.getType().equals(ConstButtonType.RESET_FORM_BUTTON.toString()) && toggle_element == null)
			throw new NullPointerException("Incorrect button: toggle element missing. Must be provided for button type: "+component.getType());
		
		if(component.getType().equals(ConstButtonType.PREVIOUS_PAGE_BUTTON.toString())) {
			
			renewNextPrevButtons(component, prev_button, previous, toggle_element);
			
		} else if(component.getType().equals(ConstButtonType.NEXT_PAGE_BUTTON.toString())) {
			
			renewNextPrevButtons(component, next_button, next, toggle_element);
			
		} else if(component.getType().equals(ConstButtonType.SUBMIT_FORM_BUTTON.toString())) {

			FormDocument formDocument = component.getFormDocument();
			
			formDocument.registerForLastPage(((FormComponentButtonArea)component.getParent()).getCurrentPage().getId());
			
			if(formDocument.getThxPage() == null)
				throw new NullPointerException("Thanks page not found");
			
			toggle_element.setAttribute(FormManagerUtil.case_att, formDocument.getThxPage().getId());
		}
	}
	
	private static final int next_button = 1;
	private static final int prev_button = 2;
	
	protected void renewNextPrevButtons(FormComponent component, int button_type, FormComponentPage relevant_page, Element toggle_element) {

		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		if(relevant_page == null) {
			
			((ComponentButtonDataBean)xformsComponentDataBean).setToggleElement(null);
			toggle_element.getParentNode().removeChild(toggle_element);
			removeSetValues(component);
			
		} else {
			toggle_element.setAttribute(FormManagerUtil.case_att, relevant_page.getId());
			
			FormDocument formDocument = component.getFormDocument();
			
			if(formDocument.getProperties().isStepsVisualizationUsed()) {

				if(FormComponentFactory.page_type_thx.equals(relevant_page.getType())) {
					removeSetValues(component);
				} else {
					
					Element setval = FormManagerUtil.getElementByIdFromDocument(component.getContext().getXformsXmlDoc(), FormManagerUtil.body_tag, component.getId()+FormManagerUtil.set_section_vis_cur);
					
					if(setval == null) {
						
						setval = createSetValue(component, true);
						setval = (Element)toggle_element.getParentNode().insertBefore(setval, toggle_element);
					}
					setval.setAttribute(FormManagerUtil.ref_s_att, "instance('"+FormManagerUtil.sections_visualization_instance_id+"')/section[id='"+component.getParent().getParentPage().getId()+"']/@selected");
					
					setval = FormManagerUtil.getElementByIdFromDocument(component.getContext().getXformsXmlDoc(), FormManagerUtil.body_tag, component.getId()+FormManagerUtil.set_section_vis_rel);
					
					if(setval == null) {
						
						setval = createSetValue(component, false);
						setval = (Element)toggle_element.getParentNode().insertBefore(setval, toggle_element);
					}
					
					setval.setAttribute(FormManagerUtil.ref_s_att, "instance('"+FormManagerUtil.sections_visualization_instance_id+"')/section[id='"+relevant_page.getId()+"']/@selected");
				}
			} else
				removeSetValues(component);
		}
	}
	
	private void removeSetValues(FormComponent component) {
		
		Element setval = FormManagerUtil.getElementByIdFromDocument(component.getContext().getXformsXmlDoc(), FormManagerUtil.body_tag, component.getId()+FormManagerUtil.set_section_vis_cur);
		
		if(setval != null)
			setval.getParentNode().removeChild(setval);
		
		setval = FormManagerUtil.getElementByIdFromDocument(component.getContext().getXformsXmlDoc(), FormManagerUtil.body_tag, component.getId()+FormManagerUtil.set_section_vis_rel);
		
		if(setval != null)
			setval.getParentNode().removeChild(setval);
	}
	
	private Element createSetValue(FormComponent component, boolean current) {

		Document xform = component.getContext().getXformsXmlDoc();
		Element setValue = xform.createElementNS(FormManagerUtil.xforms_namespace_uri, FormManagerUtil.setvalue_tag);
		setValue.setAttribute(FormManagerUtil.event_att, FormManagerUtil.DOMActivate_att_val);
		setValue.setAttribute(FormManagerUtil.value_att, 
				new StringBuilder("instance('").append(FormManagerUtil.sections_visualization_instance_id).append("')/class_exp[@for='")
				.append(current ? "false" : "true").append("']/@for").toString());
		
		setValue.setAttribute(FormManagerUtil.id_att, component.getId()+(current ? FormManagerUtil.set_section_vis_cur : FormManagerUtil.set_section_vis_rel));
		return setValue;
	}
	
	@Override
	public void addComponentToDocument(FormComponent component) {
		super.addComponentToDocument(component);
		loadToggleElement(component);
	}
	
	public void setLastPageToSubmitButton(FormComponent component, String last_page_id) {
		((ComponentButtonDataBean)component.getXformsComponentDataBean()).getToggleElement().setAttribute(FormManagerUtil.case_att, last_page_id);
	}
	
	protected Element createToggleElement(FormComponent component) {
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		Element toggle_element = FormManagerUtil.getItemElementById(CacheManager.getInstance().getComponentsXforms(), "toggle-element");
		Element button_element = xformsComponentDataBean.getElement();
		NodeList refreshs = button_element.getElementsByTagName(FormManagerUtil.refresh_tag);
		toggle_element = (Element)button_element.getOwnerDocument().importNode(toggle_element, true);
		
		if(refreshs == null || refreshs.getLength() == 0)
			toggle_element = (Element)button_element.appendChild(toggle_element);
		else
			toggle_element = (Element)button_element.insertBefore(toggle_element, refreshs.item(refreshs.getLength()-1));
		
		((ComponentButtonDataBean)xformsComponentDataBean).setToggleElement(toggle_element);
		return toggle_element;
	}
	
	/**
	 * Creates xf:setvalue element for button element (if doesn't exist already), fills it's text content with referAction parameter value.
	 * Creates action bind and action nodeset if doesn't exist.
	 * 
	 * @param component - current button component
	 * @param referAction - action information to insert to setvalue text content. If referAction == null, 
	 * xf:setvalue element is removed and the check for any additional action references is performed. 
	 * If no found, the action bind and action nodeset are removed as well.
	 */
	public void setReferAction(FormComponent component, String referAction) {
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		Element buttonElement = xformsComponentDataBean.getElement();
		
		if(referAction == null)
			removeReferAction(buttonElement);
		else
			setOrcreateReferAction(buttonElement, referAction);
	}
	
	private void removeReferAction(Element buttonElement) {
		
		Element setValueEl = getReferActionSetValueElement(buttonElement);
		
		if(setValueEl == null)
			return;
		
		String bindId = setValueEl.getAttribute(FormManagerUtil.bind_att);
		String modelId = setValueEl.getAttribute(FormManagerUtil.model_att);
		setValueEl.getParentNode().removeChild(setValueEl);
		
		if(bindId == null)
			return;
		
		//check if the last one who points to this bind
		XPathUtil xutil = getBindToExistsXPath();
		NodeList bindedTo;
		Document xform = buttonElement.getOwnerDocument();
		
		synchronized (xutil) {
			xutil.clearVariables();
			xutil.setVariable(bindIdVariable, bindId);
			bindedTo = xutil.getNodeset(xform);
		}
		
		if(bindedTo == null || bindedTo.getLength() == 0) {
			
			Bind bind = Bind.locate(xform, bindId, modelId);
			if(bind != null) {
				
				Nodeset nodeset = bind.getNodeset();
				
				if(nodeset != null)
					nodeset.remove();
				
				bind.remove();
			}
		}
	}
	
	private void setOrcreateReferAction(Element buttonElement, String referAction) {
		
		Element setValueEl = getReferActionSetValueElement(buttonElement);
		
		if(setValueEl == null) {
			
			Document xform = buttonElement.getOwnerDocument();
			setValueEl = xform.createElementNS(FormManagerUtil.xforms_namespace_uri, FormManagerUtil.setvalue_tag);
			setValueEl.setAttribute(FormManagerUtil.event_att, FormManagerUtil.DOMActivate_att_val);
			
			if(!DOMUtil.hasElementChildren(buttonElement)) {
				buttonElement.appendChild(setValueEl);
				
			} else {
				
				Element elToAppendAfter = getLabelElement(buttonElement);
				
				if(elToAppendAfter == null)
					elToAppendAfter = DOMUtil.getFirstChildElement(buttonElement);
				
				Element next = DOMUtil.getNextSiblingElement(elToAppendAfter);
				
				if(next != null)
					buttonElement.insertBefore(setValueEl, next);
				else
					buttonElement.appendChild(setValueEl);
			}
			
			Bind bind = Bind.locate(xform, actionTaken, null);
			
			if(bind == null) {
				
				Element modelElement = FormManagerUtil.getFormInstanceModelElement(xform);
				
				Nodeset nodeset = Nodeset.locate(modelElement, actionTaken);
				
				if(nodeset == null)
					nodeset = Nodeset.create(modelElement, actionTaken);
				
				nodeset.setMapping("string:"+actionTaken);
					
//				create
				bind = Bind.create(xform, actionTaken, null, nodeset);
				bind.setType("string");
			}
			
			setValueEl.setAttribute(FormManagerUtil.bind_att, bind.getId());
		}
		
		setValueEl.setTextContent(referAction);
	}
	
	public String getReferAction(FormComponent component) {
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		Element buttonElement = xformsComponentDataBean.getElement();
		Element setValueEl = getReferActionSetValueElement(buttonElement);
		
		return setValueEl == null ? null : setValueEl.getTextContent();
	}
	
	private XPathUtil referActionSetValueElementXPath;
	private XPathUtil labelElementXPath;
	private XPathUtil bindToExistsXPath;
	
	private synchronized Element getReferActionSetValueElement(Node context) {
		
		if(referActionSetValueElementXPath == null)
			referActionSetValueElementXPath = new XPathUtil(new StringBuilder(".//xf:setvalue[@bind='").append(actionTaken).append("']").toString());
		
		return (Element)referActionSetValueElementXPath.getNode(context);
	}
	
	private synchronized Element getLabelElement(Node context) {
		
		if(labelElementXPath == null)
			labelElementXPath = new XPathUtil(".//xf:label");
		
		return (Element)labelElementXPath.getNode(context);
	}
	
	private synchronized XPathUtil getBindToExistsXPath() {
		
		if(bindToExistsXPath == null)
			bindToExistsXPath = new XPathUtil(".//*[@bind=$bindId]");
		
		return bindToExistsXPath;
	}
	
	@Override
	public void update(FormComponent component, ConstUpdateType what) {
		
		super.update(component, what);
	
		switch (what) {
			case BUTTON_REFER_TO_ACTION:
				
				updateReferAction(component);
				break;
				
			default:
				break;
		}
	}
	
	protected void updateReferAction(FormComponent component) {
		
		PropertiesButton properties = (PropertiesButton)component.getProperties();
		String referAction = properties.getReferAction();
		setReferAction(component, referAction);
	}
	
	@Override
	protected void updateReadonly(FormComponent component) {
		
		setReadonly(component, component.getProperties().isReadonly());
	}
	
	@Override
	public void setReadonly(FormComponent component, boolean readonly) {
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		Bind bind = xformsComponentDataBean.getBind();
		
		if(bind == null) {

			bind = Bind.create(component.getContext().getXformsXmlDoc(), "bind."+component.getId(), null, null);
			xformsComponentDataBean.setBind(bind);
			xformsComponentDataBean.getElement().setAttribute(FormManagerUtil.bind_att, bind.getId());
			Nodeset nodeset = Nodeset.create(FormManagerUtil.getFormInstanceModelElement(component.getContext().getXformsXmlDoc()), bind.getId());
			bind.setNodeset(nodeset);
		}
		
		bind.setIsRelevant(!readonly);
	}
	
	@Override
	public boolean isReadonly(FormComponent component) {
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		Bind bind = xformsComponentDataBean.getBind();
		
		return bind != null && bind.getNodeset().getContent().equals(FormManagerUtil.xpath_false);
	}
	
	private synchronized Element getSendSubmissionElement(Node context) {
		
		if(sendSubmissionXPath == null)
			sendSubmissionXPath = new XPathUtil(".//xf:send[@submission='submit_data_submission']");
		
		return (Element)sendSubmissionXPath.getNode(context);
	}
	
	public boolean isSubmitButton(FormComponent component) {

		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		return getSendSubmissionElement(xformsComponentDataBean.getElement()) != null;
	}
}