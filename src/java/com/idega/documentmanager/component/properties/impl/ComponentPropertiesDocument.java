package com.idega.documentmanager.component.properties.impl;

import com.idega.documentmanager.business.component.properties.PropertiesDocument;
import com.idega.documentmanager.component.beans.LocalizedStringBean;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2007/10/06 13:07:12 $ by $Author: civilis $
 */
public class ComponentPropertiesDocument extends ComponentProperties implements PropertiesDocument {
	
	private boolean stepsVisualizationUsed = false;
	private String submissionAction;
	
	public String getSubmissionAction() {
		return submissionAction;
	}
	public void setSubmissionAction(String submissionAction) {

		setPlainSubmissionAction(submissionAction);
		component.update(ConstUpdateType.SUBMISSION_ACTION);
	}
	public boolean isStepsVisualizationUsed() {
		return stepsVisualizationUsed;
	}
	public void setPlainSubmissionAction(String submissionAction) {
		
		if(submissionAction == null || submissionAction.equals(""))
			throw new NullPointerException("Submission action was empty: "+submissionAction);
		
		this.submissionAction = submissionAction;
	}
	public void setStepsVisualizationUsed(boolean steps_visualization_used) {
		if(true)
			return;
		this.stepsVisualizationUsed = steps_visualization_used;
		component.update(ConstUpdateType.STEPS_VISUALIZATION_USED);
	}
	public void setPlainStepsVisualizationUsed(boolean steps_visualization_used) {
		this.stepsVisualizationUsed = steps_visualization_used;
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