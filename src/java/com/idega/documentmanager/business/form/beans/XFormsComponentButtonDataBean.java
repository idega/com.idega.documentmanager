package com.idega.documentmanager.business.form.beans;

import org.w3c.dom.Element;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 */
public class XFormsComponentButtonDataBean extends XFormsComponentDataBean {

	private Element toggle_element;

	public Element getToggleElement() {
		return toggle_element;
	}
	public void setToggleElement(Element toggle_element) {
		this.toggle_element = toggle_element;
	}
	
	@Override
	public Object clone() {
		
		XFormsComponentButtonDataBean clone = (XFormsComponentButtonDataBean)super.clone();
		
		try {
			clone = (XFormsComponentButtonDataBean)super.clone();
			
		} catch (Exception e) {
			
			clone = new XFormsComponentButtonDataBean();
		}
		
		if(toggle_element != null)
			clone.setToggleElement((Element)toggle_element.cloneNode(true));
		
		return clone;
	}
	
	@Override
	protected XFormsComponentDataBean getDataBeanInstance() {
		
		return new XFormsComponentButtonDataBean();
	}
}