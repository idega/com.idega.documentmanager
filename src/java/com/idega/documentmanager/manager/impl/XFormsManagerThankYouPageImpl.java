package com.idega.documentmanager.manager.impl;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.idega.documentmanager.business.component.properties.PropertiesThankYouPage;
import com.idega.documentmanager.component.FormComponent;
import com.idega.documentmanager.component.beans.LocalizedStringBean;
import com.idega.documentmanager.component.beans.ComponentDataBean;
import com.idega.documentmanager.component.properties.impl.ConstUpdateType;
import com.idega.documentmanager.manager.XFormsManagerThankYouPage;
import com.idega.documentmanager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.6 $
 *
 * Last modified: $Date: 2008/10/25 18:30:18 $ by $Author: civilis $
 */
public class XFormsManagerThankYouPageImpl extends XFormsManagerPageImpl implements XFormsManagerThankYouPage {

	@Override
	public void update(FormComponent component, ConstUpdateType what) {
		
		super.update(component, what);
		
		switch (what) {
		case THANKYOU_TEXT:
			updateThankYouText(component);
			break;

		default:
			break;
		}
	}

	protected void updateThankYouText(FormComponent component) {
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		PropertiesThankYouPage props = (PropertiesThankYouPage)component.getProperties();
		LocalizedStringBean loc_str = props.getText();
		
		NodeList outputs = xformsComponentDataBean.getElement().getElementsByTagName(FormManagerUtil.output_tag);
		
		if(outputs == null || outputs.getLength() == 0)
			return;
		
		Element output = (Element)outputs.item(0);
		
		FormManagerUtil.putLocalizedText(null, null, 
				output,
				component.getFormDocument().getXformsDocument(),
				loc_str
		);
	}
	
	public LocalizedStringBean getThankYouText(FormComponent component) {
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		NodeList outputs = xformsComponentDataBean.getElement().getElementsByTagName(FormManagerUtil.output_tag);
		
		if(outputs == null || outputs.getLength() == 0)
			return new LocalizedStringBean();
		
		Element output = (Element)outputs.item(0);
		
		return FormManagerUtil.getElementLocalizedStrings(output, component.getFormDocument().getXformsDocument());
	}
}