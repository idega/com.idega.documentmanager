package com.idega.documentmanager.manager.impl;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.idega.documentmanager.component.FormComponent;
import com.idega.documentmanager.component.beans.ComponentDataBean;
import com.idega.documentmanager.component.beans.ComponentMultiUploadBean;
import com.idega.documentmanager.manager.XFormsManagerMultiUpload;
import com.idega.documentmanager.util.FormManagerUtil;
import com.idega.util.xml.XPathUtil;

/**
 * @author <a href="mailto:arunas@idega.com">Arūnas Vasmanas</a>
 * @version $Revision: 1.4 $
 *
 * Last modified: $Date: 2008/03/14 12:10:03 $ by $Author: arunas $
 */
public class XFormsManagerMultiUploadImpl extends XFormsManagerImpl implements XFormsManagerMultiUpload{
	

	private static final String INSERT_TAG = "./descendant::xf:insert[@at='last()']";
	private static final String REPEAT_TAG ="./descendant::xf:repeat[@id='upload-entries']";
	private static final String DELETE_TAG="./descendant::xf:delete";
	private static final String INSTANCE = "instance('data-instance')/Add_One_";
	private static final String OUTPUT="./descendant::xf:output";
	private static final String OUTPUT_VALUE_BEGIN="count(preceding-sibling::Add_One_";
	private static final String OUTPUT_VALUE_END=") + 1";
	private static final String BIND = "bind.";
	@Override
	protected ComponentDataBean newXFormsComponentDataBeanInstance() {
		return new ComponentMultiUploadBean();
	}
	
	@Override
	protected void loadXFormsComponentDataBean(FormComponent component, Document xform, Element componentElement) {	
		super.loadXFormsComponentDataBean(component, xform, componentElement);
	}
	
	@Override
	public void removeComponentFromXFormsDocument(FormComponent component) {
		
		ComponentMultiUploadBean xforms_component = (ComponentMultiUploadBean)component.getXformsComponentDataBean();
		Element data_src_element = xforms_component.getMultiUploadInstance();
		
		if(data_src_element != null)
			data_src_element.getParentNode().removeChild(data_src_element);
		
		super.removeComponentFromXFormsDocument(component);
	}
	
	// component attach to document
	
	@Override
	public void addComponentToDocument(FormComponent component) {
		
		super.addComponentToDocument(component);
		
		ComponentMultiUploadBean xfMultiUploadComponentBean = (ComponentMultiUploadBean)component.getXformsComponentDataBean();
		
			XPathUtil util = new XPathUtil(INSERT_TAG);
			Element insert = (Element)util.getNode(xfMultiUploadComponentBean.getElement());
			insert.setAttribute(FormManagerUtil.nodeset_att, constructInsertNodeset(component.getId()));
		
			util = new XPathUtil(REPEAT_TAG);
			insert = (Element)util.getNode(xfMultiUploadComponentBean.getElement());
			insert.setAttribute(FormManagerUtil.bind_att, constuructRepeatdBind(component.getId()));
				
			util = new XPathUtil(DELETE_TAG);
			insert = (Element)util.getNode(xfMultiUploadComponentBean.getElement());
			insert.setAttribute(FormManagerUtil.nodeset_att, constructInsertNodeset(component.getId()));
			
			util = new XPathUtil(OUTPUT);
			insert = (Element)util.getNode(xfMultiUploadComponentBean.getElement());
			insert.setAttribute(FormManagerUtil.value_att, constructOutputValue(component.getId()));

	}
	
	private String constructInsertNodeset(String component_id) {
		
		StringBuffer buf = new StringBuffer();		
		buf.append(INSTANCE).append(component_id);
		return buf.toString();
		
	}
	
	private String constuructRepeatdBind (String component_id) {
		
		StringBuffer buf = new StringBuffer();
		buf.append(BIND).append(component_id);
		return buf.toString();
		
	}
	
	private String constructOutputValue(String component_id) {
		
		StringBuffer buf = new StringBuffer();		
		buf.append(OUTPUT_VALUE_BEGIN).append(component_id).append(OUTPUT_VALUE_END);
		return buf.toString();
		
	}
}
