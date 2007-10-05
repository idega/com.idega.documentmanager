package com.idega.documentmanager.component.beans;

import org.w3c.dom.Element;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 */
public class XFormsComponentSelectDataBean extends XFormsComponentDataBean {
	
	private Element local_itemset_instance;
	private Element external_itemset_instance;
	
	@Override
	public Object clone() {
		
		XFormsComponentSelectDataBean clone = (XFormsComponentSelectDataBean)super.clone();
		
		try {
			clone = (XFormsComponentSelectDataBean)super.clone();
			
		} catch (Exception e) {
			
			clone = new XFormsComponentSelectDataBean();
		}
		
		if(local_itemset_instance != null)
			clone.setLocalItemsetInstance((Element)local_itemset_instance.cloneNode(true));
		
		if(external_itemset_instance != null)
			clone.setExternalItemsetInstance((Element)external_itemset_instance.cloneNode(true));
		
		return clone;
	}
	
	@Override
	protected XFormsComponentDataBean getDataBeanInstance() {
		
		return new XFormsComponentSelectDataBean();
	}

	public Element getExternalItemsetInstance() {
		return external_itemset_instance;
	}

	public void setExternalItemsetInstance(Element external_itemset_instance) {
		this.external_itemset_instance = external_itemset_instance;
	}

	public Element getLocalItemsetInstance() {
		return local_itemset_instance;
	}

	public void setLocalItemsetInstance(Element local_itemset_instance) {
		this.local_itemset_instance = local_itemset_instance;
	}
}
