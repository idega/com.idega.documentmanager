package com.idega.documentmanager.component.properties.impl;

import com.idega.documentmanager.business.component.properties.PropertiesPlain;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2007/10/06 13:07:12 $ by $Author: civilis $
 */
public class ComponentPropertiesPlain extends ComponentProperties implements PropertiesPlain {
	
	private String text;
	
	public String toString() {
		return new StringBuffer()
		.append("\ntext: ")
		.append(text)
		.append("\nautofill key: ")
		.append(getAutofillKey())
		
		.toString();
	}

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
		component.update(ConstUpdateType.TEXT);
	}
	public void setPlainText(String text) {
		this.text = text;
	}
}