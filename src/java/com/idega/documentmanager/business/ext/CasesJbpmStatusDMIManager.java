package com.idega.documentmanager.business.ext;

import java.util.Map;

import org.w3c.dom.Element;

import com.idega.documentmanager.business.component.properties.DocumentMetaInformationManager;
import com.idega.documentmanager.component.FormDocument;
import com.idega.documentmanager.util.FormManagerUtil;
import com.idega.util.URIUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2007/11/07 15:02:29 $ by $Author: civilis $
 */
public class CasesJbpmStatusDMIManager implements DocumentMetaInformationManager {

	
//	<caseStatus mapping="string:caseStatus" />
	private FormDocument document;
	private SimpleCaseFormProceedMetaInf metaInf;
	
	public static final String piIdParam = "piId";
	public static final String type = "simpleCaseFormProceedMetaInf";
	
	public Object resolve() {

		if(document == null)
			throw new NullPointerException("FormDocument not set. The manager needs to be set to Document");

		if(metaInf == null) {
			
			Element submissionEl = document.getSubmissionElement();
			String action = submissionEl.getAttribute(FormManagerUtil.action_att);
			
			if(action == null || "".equals(action))
				return null;
			
			URIUtil uriUtil = new URIUtil(action);
			
			Map<String, String> params = uriUtil.getParameters();
			
			if(!params.containsKey(type))
				return null;
			
			SimpleCaseFormProceedMetaInf scmetaInf = new SimpleCaseFormProceedMetaInf();

			scmetaInf.setProcessInstanceId(!params.containsKey(piIdParam) ? null : params.get(piIdParam));
			
			metaInf = scmetaInf;
		}
		
		return metaInf;
	}

	public void setDocumentComponent(FormDocument document) {
		
		this.document = document;
	}
	
	public void update(Object metaInf) {
		
		if(document == null)
			throw new NullPointerException("FormDocument not set. The manager needs to be set to Document");
		
		if(metaInf != null && !(metaInf instanceof CasesJbpmStatusMetaInf))
			throw new IllegalArgumentException("Wrong meta inf argument provided. Should be of class: "+CasesJbpmStatusMetaInf.class.getName()+", but got: "+metaInf.getClass().getName());
		
		Element submissionEl = document.getSubmissionElement();
		String action = submissionEl.getAttribute(FormManagerUtil.action_att);
		
		if(action == null || "".equals(action))
			return;
		
		SimpleCaseFormProceedMetaInf scmetaInf = (SimpleCaseFormProceedMetaInf)metaInf;
		
		URIUtil uriUtil = new URIUtil(action);
		uriUtil.setParameter(type, "1");
		
		if(scmetaInf != null) {
			
			uriUtil.setParameter(piIdParam, scmetaInf.getProcessInstanceId() == null ? "" : scmetaInf.getProcessInstanceId());
		}
		
		submissionEl.setAttribute(FormManagerUtil.action_att, uriUtil.getUri());
		this.metaInf = scmetaInf;
	}
}