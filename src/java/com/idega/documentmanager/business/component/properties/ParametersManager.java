package com.idega.documentmanager.business.component.properties;

import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Element;

import com.idega.documentmanager.component.FormDocument;
import com.idega.documentmanager.util.FormManagerUtil;
import com.idega.util.CoreConstants;
import com.idega.util.URIUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2007/12/04 14:02:28 $ by $Author: civilis $
 */
public class ParametersManager {
	
	private FormDocument document;

	public void update(Map<String, String> parameters) {

		Element submissionElement = document.getSubmissionElement();
		String action = submissionElement.getAttribute(FormManagerUtil.action_att);
		
		if(FormManagerUtil.isEmpty(action))
			return;
		
		URIUtil uriUtil = new URIUtil(action);
		
		for (Entry<String, String> param : parameters.entrySet()) {
		
			if(!FormManagerUtil.isEmpty(param.getKey())) {
			
				uriUtil.setParameter(param.getKey(), param.getValue() == null ? CoreConstants.EMPTY : param.getValue());
			}
		}
		
		submissionElement.setAttribute(FormManagerUtil.action_att, uriUtil.getUri());
	}
	
	public void cleanUpdate(Map<String, String> parameters) {
		
//		TODO: clean existing parameters before
		update(parameters);
	}
	
	public Map<String, String> resolve() {
		
		String action = document.getSubmissionElement().getAttribute(FormManagerUtil.action_att);
		
		if(FormManagerUtil.isEmpty(action))
			return null;
		
		return new URIUtil(action).getParameters();
	}
	
	public void setFormDocumentComponent(FormDocument document) {
		this.document = document;
	}
	
	public FormDocument getFormDocumentComponent() {
		return document;
	}
}