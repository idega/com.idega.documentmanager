package com.idega.documentmanager.component.properties.impl;

import com.idega.documentmanager.business.component.properties.PropertiesComponent;
import com.idega.documentmanager.component.FormComponent;
import com.idega.documentmanager.component.beans.LocalizedStringBean;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 */
public class ComponentProperties implements PropertiesComponent {
	
	private boolean required;
	private LocalizedStringBean label;
	private LocalizedStringBean error_msg;
	private LocalizedStringBean help_text;
	private String p3ptype;
	private String autofill_key;
	
	protected FormComponent component;
	
	public LocalizedStringBean getErrorMsg() {
		return error_msg;
	}
	public void setErrorMsg(LocalizedStringBean error_msg) {
		this.error_msg = error_msg;
		component.update(new ConstUpdateType(ConstUpdateType.error_msg));
	}
	public LocalizedStringBean getLabel() {
		return label;
	}
	public void setLabel(LocalizedStringBean label) {
		this.label = label;
		component.update(new ConstUpdateType(ConstUpdateType.label));
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
		component.update(new ConstUpdateType(ConstUpdateType.constraint_required));
	}
	public void setPlainLabel(LocalizedStringBean label) {
		this.label = label;
	}
	public void setPlainRequired(boolean required) {
		this.required = required;
	}
	public void setPlainErrorMsg(LocalizedStringBean error_msg) {
		this.error_msg = error_msg;
	}
	public void setComponent(FormComponent component) {
		this.component = component;
	}
	public String toString() {
		return new StringBuffer()
		.append("\nrequired: ")
		.append(required)
		.append("\nlabel: ")
		.append(label)
		.append("\nerror_msg: ")
		.append(error_msg)
		.append("\np3ptype: ")
		.append(p3ptype)
		.append("\nautofill key: ")
		.append(autofill_key)
		.append("\nhelp text: ")
		.append(help_text)
		
		.toString();
	}

	public String getP3ptype() {
		return p3ptype;
	}
	public void setP3ptype(String p3ptype) {
		this.p3ptype = p3ptype;
		component.update(new ConstUpdateType(ConstUpdateType.p3p_type));
	}
	public void setPlainP3ptype(String p3ptype) {
		this.p3ptype = p3ptype;
	}
	public String getAutofillKey() {
		return autofill_key;
	}
	public void setAutofillKey(String autofill_key) {
		
		this.autofill_key = autofill_key;
		component.update(new ConstUpdateType(ConstUpdateType.autofill_key));
	}
	public void setPlainAutofillKey(String autofill_key) {
		this.autofill_key = autofill_key;
	}
	public LocalizedStringBean getHelpText() {
		return help_text;
	}
	public void setHelpText(LocalizedStringBean help_text) {
		this.help_text = help_text;
		component.update(new ConstUpdateType(ConstUpdateType.help_text));
	}
	public void setPlainHelpText(LocalizedStringBean help_text) {
		this.help_text = help_text;
	}
}