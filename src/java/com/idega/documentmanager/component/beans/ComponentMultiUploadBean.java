package com.idega.documentmanager.component.beans;

import org.w3c.dom.Element;
/**
 * @author <a href="mailto:arunas@idega.com">Arūnas Vasmanas</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2008/03/06 12:21:30 $ by $Author: arunas $
 */
public class ComponentMultiUploadBean  extends ComponentDataBean {
	
	private Element multi_upload_instance;
	
	@Override
	public Object clone() {
		
		ComponentMultiUploadBean clone = (ComponentMultiUploadBean)super.clone();
		
		try {
			clone = (ComponentMultiUploadBean)super.clone();
			
		} catch (Exception e) {
			
			clone = new ComponentMultiUploadBean();
		}
		
		if(multi_upload_instance != null)
			clone.setMultiUploadInstance((Element)multi_upload_instance.cloneNode(true));
		return clone;
	}
	
	@Override
	protected ComponentDataBean getDataBeanInstance() {
		
		return new ComponentMultiUploadBean();
	}

	public Element getMultiUploadInstance() {
		return multi_upload_instance;
	}

	public void setMultiUploadInstance(Element multi_upload_instance) {
		this.multi_upload_instance = multi_upload_instance;
	}

}
