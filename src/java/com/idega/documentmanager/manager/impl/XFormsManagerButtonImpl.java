package com.idega.documentmanager.manager.impl;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.idega.documentmanager.business.component.ConstButtonType;
import com.idega.documentmanager.component.FormComponent;
import com.idega.documentmanager.component.FormComponentButtonArea;
import com.idega.documentmanager.component.FormComponentPage;
import com.idega.documentmanager.component.FormDocument;
import com.idega.documentmanager.component.beans.ComponentButtonDataBean;
import com.idega.documentmanager.component.beans.ComponentDataBean;
import com.idega.documentmanager.component.impl.FormComponentFactory;
import com.idega.documentmanager.context.DMContext;
import com.idega.documentmanager.manager.XFormsManagerButton;
import com.idega.documentmanager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2007/10/05 12:27:16 $ by $Author: civilis $
 */
public class XFormsManagerButtonImpl extends XFormsManagerImpl implements XFormsManagerButton {
	
	@Override
	public void loadXFormsComponentFromDocument(DMContext context, String component_id) {
		
		FormComponent component = context.getComponent();
		
		super.loadXFormsComponentFromDocument(component.getContext(), component_id);
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		Element button_element = xformsComponentDataBean.getElement();
		String button_type = button_element.getAttribute(FormManagerUtil.name_att);
		
		if(button_type != null)
			component.setType(button_type);

		loadToggleElement(component.getContext());
	}
	
	protected void loadToggleElement(DMContext context) {
		
		FormComponent component = context.getComponent();
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
	
	public void renewButtonPageContextPages(DMContext context, FormComponentPage previous, FormComponentPage next) {
		
		FormComponent component = context.getComponent();
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		Element toggle_element = ((ComponentButtonDataBean)xformsComponentDataBean).getToggleElement();
		
		if(toggle_element == null)
			toggle_element = createToggleElement(component.getContext());
		
		if(!component.getType().equals(ConstButtonType.reset_form_button) && toggle_element == null)
			throw new NullPointerException("Incorrect button: toggle element missing. Must be provided for button type: "+component.getType());
		
		if(component.getType().equals(ConstButtonType.previous_page_button)) {
			
			renewNextPrevButtons(component.getContext(), prev_button, previous, toggle_element);
			
		} else if(component.getType().equals(ConstButtonType.next_page_button)) {
			
			renewNextPrevButtons(component.getContext(), next_button, next, toggle_element);
			
		} else if(component.getType().equals(ConstButtonType.submit_form_button)) {

			FormDocument formDocument = component.getFormDocument();
			
			formDocument.registerForLastPage(((FormComponentButtonArea)component.getParent()).getCurrentPage().getId());
			
			if(formDocument.getThxPage() == null)
				throw new NullPointerException("Thanks page not found");
			
			toggle_element.setAttribute(FormManagerUtil.case_att, formDocument.getThxPage().getId());
		}
	}
	
	private static final int next_button = 1;
	private static final int prev_button = 2;
	
	protected void renewNextPrevButtons(DMContext context, int button_type, FormComponentPage relevant_page, Element toggle_element) {

		
		FormComponent component = context.getComponent();
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		if(relevant_page == null) {
			
			((ComponentButtonDataBean)xformsComponentDataBean).setToggleElement(null);
			toggle_element.getParentNode().removeChild(toggle_element);
			removeSetValues(component.getContext());
			
		} else {
			toggle_element.setAttribute(FormManagerUtil.case_att, relevant_page.getId());
			
			FormDocument formDocument = component.getFormDocument();
			
			if(formDocument.getProperties().isStepsVisualizationUsed()) {

				if(FormComponentFactory.page_type_thx.equals(relevant_page.getType())) {
					removeSetValues(component.getContext());
				} else {
					
					Element setval = FormManagerUtil.getElementByIdFromDocument(formDocument.getXformsDocument(), FormManagerUtil.body_tag, component.getId()+FormManagerUtil.set_section_vis_cur);
					
					if(setval == null) {
						
						setval = createSetValue(component.getContext(), true);
						setval = (Element)toggle_element.getParentNode().insertBefore(setval, toggle_element);
					}
					setval.setAttribute(FormManagerUtil.ref_s_att, "instance('"+FormManagerUtil.sections_visualization_instance_id+"')/section[id='"+component.getParent().getParentPage().getId()+"']/@selected");
					
					setval = FormManagerUtil.getElementByIdFromDocument(formDocument.getXformsDocument(), FormManagerUtil.body_tag, component.getId()+FormManagerUtil.set_section_vis_rel);
					
					if(setval == null) {
						
						setval = createSetValue(component.getContext(), false);
						setval = (Element)toggle_element.getParentNode().insertBefore(setval, toggle_element);
					}
					
					setval.setAttribute(FormManagerUtil.ref_s_att, "instance('"+FormManagerUtil.sections_visualization_instance_id+"')/section[id='"+relevant_page.getId()+"']/@selected");
				}
			} else
				removeSetValues(component.getContext());
		}
	}
	
	private void removeSetValues(DMContext context) {
		
		FormComponent component = context.getComponent();
		FormDocument formDocument = component.getFormDocument();
		
		Element setval = FormManagerUtil.getElementByIdFromDocument(formDocument.getXformsDocument(), FormManagerUtil.body_tag, component.getId()+FormManagerUtil.set_section_vis_cur);
		
		if(setval != null)
			setval.getParentNode().removeChild(setval);
		
		setval = FormManagerUtil.getElementByIdFromDocument(formDocument.getXformsDocument(), FormManagerUtil.body_tag, component.getId()+FormManagerUtil.set_section_vis_rel);
		
		if(setval != null)
			setval.getParentNode().removeChild(setval);
	}
	
	private Element createSetValue(DMContext context, boolean current) {

		FormComponent component = context.getComponent();
		
		Document xforms_doc = component.getFormDocument().getXformsDocument();
		Element set_value = xforms_doc.createElement(FormManagerUtil.setvalue_tag);
		set_value.setAttribute("ev:event", FormManagerUtil.DOMActivate_att_val);
		set_value.setAttribute(FormManagerUtil.value_att, "instance('"+FormManagerUtil.sections_visualization_instance_id+"')/class_exp[@for='"+(current ? "false" : "true")+"']/@for");
		set_value.setAttribute(FormManagerUtil.id_att, component.getId()+(current ? FormManagerUtil.set_section_vis_cur : FormManagerUtil.set_section_vis_rel));
		return set_value;
	}
	
	@Override
	public void addComponentToDocument(DMContext context) {
		FormComponent component = context.getComponent();
		super.addComponentToDocument(component.getContext());
		loadToggleElement(component.getContext());
	}
	
	public void setLastPageToSubmitButton(DMContext context, String last_page_id) {
		((ComponentButtonDataBean)context.getComponent().getXformsComponentDataBean()).getToggleElement().setAttribute(FormManagerUtil.case_att, last_page_id);
	}
	
	protected Element createToggleElement(DMContext context) {
		
		ComponentDataBean xformsComponentDataBean = context.getComponent().getXformsComponentDataBean();
		
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
}