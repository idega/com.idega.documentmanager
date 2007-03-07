package com.idega.documentmanager.business.form.beans;

import com.idega.documentmanager.business.form.PropertiesPlain;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
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
		parent_component.update(new ConstUpdateType(ConstUpdateType.text));
	}
	public void setPlainText(String text) {
		this.text = text;
	}
}