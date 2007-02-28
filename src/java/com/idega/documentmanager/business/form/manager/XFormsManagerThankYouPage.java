package com.idega.documentmanager.business.form.manager;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.idega.documentmanager.business.form.PropertiesThankYouPage;
import com.idega.documentmanager.business.form.beans.ConstUpdateType;
import com.idega.documentmanager.business.form.beans.LocalizedStringBean;
import com.idega.documentmanager.business.form.manager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 */
public class XFormsManagerThankYouPage extends XFormsManagerPage {

	@Override
	public void update(ConstUpdateType what) {
		super.update(what);
		
		int update = what.getUpdateType();
		
		switch (update) {
		case ConstUpdateType.thankyou_text:
			updateThankYouText();
			break;

		default:
			break;
		}
	}

	protected void updateThankYouText() {
		
		PropertiesThankYouPage props = (PropertiesThankYouPage)component.getProperties();
		LocalizedStringBean loc_str = props.getText();
		
		NodeList outputs = xforms_component.getElement().getElementsByTagName(FormManagerUtil.output_tag);
		
		if(outputs == null || outputs.getLength() == 0)
			return;
		
		Element output = (Element)outputs.item(0);
		
		FormManagerUtil.putLocalizedText(null, null, 
				output,
				form_document.getXformsDocument(),
				loc_str
		);
	}
	
	public LocalizedStringBean getThankYouText() {
		
		NodeList outputs = xforms_component.getElement().getElementsByTagName(FormManagerUtil.output_tag);
		
		if(outputs == null || outputs.getLength() == 0)
			return new LocalizedStringBean();
		
		Element output = (Element)outputs.item(0);
		
		return FormManagerUtil.getElementLocalizedStrings(output, form_document.getXformsDocument());
	}
}