package com.idega.documentmanager.component.properties.impl;

import com.idega.documentmanager.business.component.properties.PropertiesThankYouPage;
import com.idega.documentmanager.component.beans.LocalizedStringBean;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 */
public class ComponentPropertiesThankYouPage extends ComponentPropertiesPage implements PropertiesThankYouPage {

	private LocalizedStringBean text;

	public LocalizedStringBean getText() {
		return text;
	}

	public void setText(LocalizedStringBean text) {
		this.text = text;
		component.update(new ConstUpdateType(ConstUpdateType.thankyou_text));
	}
	
	public void setPlainText(LocalizedStringBean text) {
		this.text = text;
	}
}