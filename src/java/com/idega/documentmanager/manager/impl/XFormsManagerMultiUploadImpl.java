package com.idega.documentmanager.manager.impl;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.idega.documentmanager.component.FormComponent;
import com.idega.documentmanager.component.beans.ComponentDataBean;
import com.idega.documentmanager.component.beans.ComponentMultiUploadBean;
import com.idega.documentmanager.manager.XFormsManagerMultiUpload;
import com.idega.documentmanager.util.FormManagerUtil;
import com.idega.util.xml.XPathUtil;


public class XFormsManagerMultiUploadImpl extends XFormsManagerImpl implements XFormsManagerMultiUpload{
	

	private static final String upload_data_source = "_upd";
	@Override
	protected ComponentDataBean newXFormsComponentDataBeanInstance() {
		return new ComponentMultiUploadBean();
	}
	
	@Override
	protected void loadXFormsComponentDataBean(FormComponent component, Document xform, Element componentElement) {
		
		super.loadXFormsComponentDataBean(component, xform, componentElement);
		
		ComponentMultiUploadBean xfMultiUploadBean = (ComponentMultiUploadBean)component.getXformsComponentDataBean();
		
		componentElement = xfMultiUploadBean.getElement();
		
		String componentElementId = componentElement.getAttribute(FormManagerUtil.id_att);
	
		Element externalUploadInstance = FormManagerUtil.getElementByIdFromDocument(xform, FormManagerUtil.head_tag, getMultiUploadInstanceIdenfiner(componentElementId));
		
		xfMultiUploadBean.setMultiUploadInstance(externalUploadInstance);
	}
	
	
	private String getMultiUploadInstanceIdenfiner(String element_id) {
		
		return element_id+upload_data_source;
	}
	
	
	public void removeMultiUploadComponentSourcesFromXFormsDocument(FormComponent component) {
		
		ComponentMultiUploadBean xforms_component = (ComponentMultiUploadBean)component.getXformsComponentDataBean();
		Element data_src_element = xforms_component.getMultiUploadInstance();
		
		if(data_src_element != null)
			data_src_element.getParentNode().removeChild(data_src_element);
	}
	
	// component attach to document
	
	@Override
	public void addComponentToDocument(FormComponent component) {
		
		super.addComponentToDocument(component);
		
		Document xforms_document = component.getContext().getXformsXmlDoc();
		
		ComponentMultiUploadBean xfMultiUploadComponentBean = (ComponentMultiUploadBean)component.getXformsComponentDataBean();
		
		Element new_xforms_element =xfMultiUploadComponentBean.getMultiUploadInstance();
		
		Element data_model_instance_element = FormManagerUtil.getElementByIdFromDocument(xforms_document, FormManagerUtil.head_tag, FormManagerUtil.data_mod);
		
		if(new_xforms_element != null) {
					
			new_xforms_element = (Element)xforms_document.importNode(new_xforms_element, true);
			new_xforms_element.setAttribute(FormManagerUtil.id_att, getMultiUploadInstanceIdenfiner(component.getId()));
			new_xforms_element = (Element)data_model_instance_element.appendChild(new_xforms_element);
			xfMultiUploadComponentBean.setMultiUploadInstance(new_xforms_element);
			
			
			XPathUtil util = new XPathUtil("./descendant::xf:insert[@at='last()']");
			Element insert = (Element)util.getNode(xfMultiUploadComponentBean.getElement());
			insert.setAttribute(FormManagerUtil.nodeset_att, constructInsertNodeset(component.getId()));
			insert.setAttribute(FormManagerUtil.origin_att, constructInsertOrigin(component.getId()));
		
			util = new XPathUtil("./descendant::xf:repeat[@id='data_repeat']");
			insert = (Element)util.getNode(xfMultiUploadComponentBean.getElement());
			insert.setAttribute(FormManagerUtil.nodeset_att, constructInsertNodeset(component.getId()));
		
			util = new XPathUtil("./descendant::xf:upload");
			insert = (Element)util.getNode(xfMultiUploadComponentBean.getElement());
			insert.setAttribute(FormManagerUtil.bind_att, constuructUploadBind(component.getId()));
		
			util = new XPathUtil("./descendant::xf:delete");
			insert = (Element)util.getNode(xfMultiUploadComponentBean.getElement());
			insert.setAttribute(FormManagerUtil.nodeset_att, constructInsertNodeset(component.getId()));
		
		}
	}
	
	private String constructInsertNodeset(String component_id) {
		
		StringBuffer buf = new StringBuffer();		
		buf.append("instance('data-instance')/Add_One_").append(component_id).append("/attachment");
		return buf.toString();
		
	}
	
	private String constructInsertOrigin (String component_id) {
		
		StringBuffer buf = new StringBuffer();
		buf.append("instance('").append(component_id).append("_upd')");
		return buf.toString();
		
	}
	
	private String constuructUploadBind (String component_id) {
		
		StringBuffer buf = new StringBuffer();
		buf.append("bind.").append(component_id);
		return buf.toString();
		
	}
}
