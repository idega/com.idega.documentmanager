package com.idega.documentmanager.component.properties.impl;

import com.idega.documentmanager.business.component.properties.PropertiesDocument;
import com.idega.documentmanager.component.beans.LocalizedStringBean;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 */
public class ComponentPropertiesDocument extends ComponentProperties implements PropertiesDocument {
	
	private boolean steps_visualization_used = false; 
	
	public boolean isStepsVisualizationUsed() {
		return steps_visualization_used;
	}
	public void setStepsVisualizationUsed(boolean steps_visualization_used) {
		if(true)
			return;
		this.steps_visualization_used = steps_visualization_used;
		component.update(new ConstUpdateType(ConstUpdateType.steps_visualization_used));
	}
	public void setPlainStepsVisualizationUsed(boolean steps_visualization_used) {
		this.steps_visualization_used = steps_visualization_used;
	}
	
//	methods not used ---------

	public String getAutofillKey() {
		throw new RuntimeException("Wrong method call");
	}

	public LocalizedStringBean getErrorMsg() {
		throw new RuntimeException("Wrong method call");
	}

	public LocalizedStringBean getHelpText() {
		throw new RuntimeException("Wrong method call");
	}

	public LocalizedStringBean getLabel() {
		throw new RuntimeException("Wrong method call");
	}

	public String getP3ptype() {
		throw new RuntimeException("Wrong method call");
	}

	public boolean isRequired() {
		throw new RuntimeException("Wrong method call");
	}

	public void setAutofillKey(String autofill_key) {
		throw new RuntimeException("Wrong method call");
	}

	public void setErrorMsg(LocalizedStringBean error_msg) {
		throw new RuntimeException("Wrong method call");
	}

	public void setHelpText(LocalizedStringBean help_text) {
		throw new RuntimeException("Wrong method call");
	}

	public void setLabel(LocalizedStringBean label) {
		throw new RuntimeException("Wrong method call");
	}

	public void setP3ptype(String p3ptype) {
		throw new RuntimeException("Wrong method call");
	}

	public void setRequired(boolean required) {
		throw new RuntimeException("Wrong method call");
	}
}