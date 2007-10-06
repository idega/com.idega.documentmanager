package com.idega.documentmanager.component.beans;

import org.w3c.dom.Element;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2007/10/06 06:17:50 $ by $Author: civilis $
 */
public class ComponentSelectDataBean extends ComponentDataBean {
	
	private Element local_itemset_instance;
	private Element external_itemset_instance;
	
	@Override
	public Object clone() {
		
		ComponentSelectDataBean clone = (ComponentSelectDataBean)super.clone();
		
		try {
			clone = (ComponentSelectDataBean)super.clone();
			
		} catch (Exception e) {
			
			clone = new ComponentSelectDataBean();
		}
		
		if(local_itemset_instance != null)
			clone.setLocalItemsetInstance((Element)local_itemset_instance.cloneNode(true));
		
		if(external_itemset_instance != null)
			clone.setExternalItemsetInstance((Element)external_itemset_instance.cloneNode(true));
		
		return clone;
	}
	
	@Override
	protected ComponentDataBean getDataBeanInstance() {
		
		return new ComponentSelectDataBean();
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
