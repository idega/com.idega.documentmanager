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
 * @version $Revision: 1.6 $
 *
 * Last modified: $Date: 2008/03/26 10:47:56 $ by $Author: arunas $
 */
public class XFormsManagerMultiUploadImpl extends XFormsManagerImpl implements XFormsManagerMultiUpload{
	

	private static final String INSERT_TAG = "./descendant::xf:insert[@at='last()']";
	private static final String REPEAT_TAG ="./descendant::xf:repeat";
	private static final String DELETE_TAG="./descendant::xf:delete";
	private static final String INSTANCE = "instance('data-instance')/Add_One_";
	private static final String BIND = "bind.";
	private static final String REPEAT = "upload_entries_";
	private static final String ENTRY = "/entry";
	private static final String AT_START = "index('";
	private static final String AT_END = "')";
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
			insert.setAttribute(FormManagerUtil.bind_att, constructRepeatBind(component.getId()));
			insert.setAttribute(FormManagerUtil.id_att, constructRepeatId(component.getId()));
				
			util = new XPathUtil(DELETE_TAG);
			insert = (Element)util.getNode(xfMultiUploadComponentBean.getElement());
			insert.setAttribute(FormManagerUtil.nodeset_att, constructInsertNodeset(component.getId()));
			insert.setAttribute(FormManagerUtil.at_att, constructDeleteAt(component.getId()));
			
		
	}
	
	private String constructInsertNodeset(String component_id) {
		
		StringBuffer buf = new StringBuffer();		
		buf.append(INSTANCE).append(component_id).append(ENTRY);
		return buf.toString();
		
	}
	
	private String constructRepeatBind (String component_id) {
		
		StringBuffer buf = new StringBuffer();
		buf.append(BIND).append(component_id);
		return buf.toString();
		
	}
	private String constructRepeatId (String component_id){
	    StringBuffer buf = new StringBuffer();
	    buf.append(REPEAT).append(component_id);
	    return buf.toString();
	}
	private String constructDeleteAt (String component_id){
	    StringBuffer buf = new StringBuffer();
	    buf.append(AT_START).append(REPEAT).append(component_id).append(AT_END);
	    return buf.toString();
	}
	
}
