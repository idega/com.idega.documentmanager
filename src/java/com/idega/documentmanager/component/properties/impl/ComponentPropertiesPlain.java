package com.idega.documentmanager.component.properties.impl;

import com.idega.documentmanager.business.component.properties.PropertiesPlain;
import com.idega.documentmanager.component.beans.LocalizedStringBean;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.3 $
 *
 * Last modified: $Date: 2007/11/15 09:24:15 $ by $Author: civilis $
 */
public class ComponentPropertiesPlain extends ComponentProperties implements PropertiesPlain {
	
	private LocalizedStringBean text;
	
	public LocalizedStringBean getText() {
		return text;
	}

	public void setText(LocalizedStringBean text) {
		this.text = text;
		component.update(ConstUpdateType.TEXT);
	}
	
	public void setPlainText(LocalizedStringBean text) {
		this.text = text;
	}
}