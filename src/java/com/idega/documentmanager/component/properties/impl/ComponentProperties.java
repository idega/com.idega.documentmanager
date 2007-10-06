package com.idega.documentmanager.component.properties.impl;

import com.idega.documentmanager.business.component.properties.PropertiesComponent;
import com.idega.documentmanager.component.FormComponent;
import com.idega.documentmanager.component.beans.LocalizedStringBean;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2007/10/06 13:07:12 $ by $Author: civilis $
 */
public class ComponentProperties implements PropertiesComponent {
	
	private boolean required;
	private LocalizedStringBean label;
	private LocalizedStringBean errorMsg;
	private LocalizedStringBean helpText;
	private String p3ptype;
	private String autofillKey;
	private String variableName;
	
	protected FormComponent component;
	
	public LocalizedStringBean getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(LocalizedStringBean error_msg) {
		this.errorMsg = error_msg;
		component.update(ConstUpdateType.ERROR_MSG);
	}
	public LocalizedStringBean getLabel() {
		return label;
	}
	public void setLabel(LocalizedStringBean label) {
		this.label = label;
		component.update(ConstUpdateType.LABEL);
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
		component.update(ConstUpdateType.CONSTRAINT_REQUIRED);
	}
	public void setPlainLabel(LocalizedStringBean label) {
		this.label = label;
	}
	public void setPlainRequired(boolean required) {
		this.required = required;
	}
	public void setPlainErrorMsg(LocalizedStringBean error_msg) {
		this.errorMsg = error_msg;
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
		.append(errorMsg)
		.append("\np3ptype: ")
		.append(p3ptype)
		.append("\nautofill key: ")
		.append(autofillKey)
		.append("\nhelp text: ")
		.append(helpText)
		
		.toString();
	}

	public String getP3ptype() {
		return p3ptype;
	}
	public void setP3ptype(String p3ptype) {
		this.p3ptype = p3ptype;
		component.update(ConstUpdateType.P3P_TYPE);
	}
	public void setPlainP3ptype(String p3ptype) {
		this.p3ptype = p3ptype;
	}
	public String getAutofillKey() {
		return autofillKey;
	}
	public void setAutofillKey(String autofill_key) {
		
		this.autofillKey = autofill_key;
		component.update(ConstUpdateType.AUTOFILL_KEY);
	}
	public void setPlainAutofillKey(String autofill_key) {
		this.autofillKey = autofill_key;
	}
	public LocalizedStringBean getHelpText() {
		return helpText;
	}
	public void setHelpText(LocalizedStringBean help_text) {
		this.helpText = help_text;
		component.update(ConstUpdateType.HELP_TEXT);
	}
	public void setPlainHelpText(LocalizedStringBean help_text) {
		this.helpText = help_text;
	}
	public String getVariableName() {
		return variableName;
	}
	public void setVariableName(String variableName) {
		this.variableName = "".equals(variableName) ? null : variableName;
		component.update(ConstUpdateType.VARIABLE_NAME);
	}
	public void setPlainVariableName(String variableName) {
		this.variableName = "".equals(variableName) ? null : variableName;
	}
}