package com.idega.documentmanager.component.properties.impl;

import com.idega.documentmanager.business.component.properties.PropertiesComponent;
import com.idega.documentmanager.component.FormComponent;
import com.idega.documentmanager.component.beans.LocalizedStringBean;
import com.idega.documentmanager.util.FormManagerUtil;
import com.idega.jbpm.variables.Variable;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.8 $
 *
 * Last modified: $Date: 2008/08/12 06:07:39 $ by $Author: arunas $
 */
public class ComponentProperties implements PropertiesComponent {
	
	private boolean required = false;
	private boolean readonly = false;
	private LocalizedStringBean label;
	private LocalizedStringBean errorMsg;
	private LocalizedStringBean helpText;
	private LocalizedStringBean validationText;
	private String p3ptype;
	private String autofillKey;
	private Variable variable;
	
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
		.append("\nreadonly: ")
		.append(readonly)
		.append("\nvalidationText: ")
		.append(validationText)
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
	public Variable getVariable() {
		return variable;
	}
	public void setVariable(Variable variable) {
		this.variable = variable;
		component.update(ConstUpdateType.VARIABLE_NAME);
	}
	
	public void setVariable(String variableStringRepresentation) {
		setVariable(FormManagerUtil.isEmpty(variableStringRepresentation) ? null : Variable.parseDefaultStringRepresentation(variableStringRepresentation));
	}
	
	public void setPlainVariable(Variable variable) {
		this.variable = variable;
	}
	public boolean isReadonly() {
		return readonly;
	}
	public void setReadonly(boolean readonly) {
		
		this.readonly = readonly;
		component.update(ConstUpdateType.READ_ONLY);
	}
	
	public void setPlainReadonly(boolean readonly) {
		this.readonly = readonly;
	}
	
	public LocalizedStringBean getValidationText() {
	    return validationText;
	}
	
	public void setValidationText(LocalizedStringBean validationText) {
	    this.validationText = validationText;
	    component.update(ConstUpdateType.VALIDATION);
	}
	
	public void setPlainValidationText(LocalizedStringBean validationText) {
	    this.validationText = validationText;
	}
	
}