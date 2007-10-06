package com.idega.documentmanager.component.properties.impl;

import com.idega.documentmanager.business.component.properties.PropertiesThankYouPage;
import com.idega.documentmanager.component.beans.LocalizedStringBean;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2007/10/06 13:07:12 $ by $Author: civilis $
 */
public class ComponentPropertiesThankYouPage extends ComponentPropertiesPage implements PropertiesThankYouPage {

	private LocalizedStringBean text;

	public LocalizedStringBean getText() {
		return text;
	}

	public void setText(LocalizedStringBean text) {
		this.text = text;
		component.update(ConstUpdateType.THANKYOU_TEXT);
	}
	
	public void setPlainText(LocalizedStringBean text) {
		this.text = text;
	}
}