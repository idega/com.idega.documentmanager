package com.idega.documentmanager.business.ext;

import java.util.Map;

import org.w3c.dom.Element;

import com.idega.documentmanager.business.component.properties.DocumentMetaInformationManager;
import com.idega.documentmanager.component.FormDocument;
import com.idega.documentmanager.util.FormManagerUtil;
import com.idega.util.URIUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2007/10/22 20:34:37 $ by $Author: civilis $
 */
public class SimpleCaseFormCreateDMIManager implements DocumentMetaInformationManager {

	private FormDocument document;
	private SimpleCaseFormCreateMetaInf metaInf;
	
	private static final String pdIdParam = "pdId";
	private static final String type = "simpleCaseFormCreateMetaInf";
	private static final String userIdParam = "userId";
	private static final String caseCategoryIdParam = "caseCatId";
	private static final String caseTypeParam = "caseTypeId";
	
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
			
			SimpleCaseFormCreateMetaInf scmetaInf = new SimpleCaseFormCreateMetaInf();

			scmetaInf.setProcessDefinitionId(!params.containsKey(pdIdParam) ? null : params.get(pdIdParam));
			scmetaInf.setProcessDefinitionId(!params.containsKey(userIdParam) ? null : params.get(userIdParam));
			scmetaInf.setProcessDefinitionId(!params.containsKey(caseCategoryIdParam) ? null : params.get(caseCategoryIdParam));
			scmetaInf.setProcessDefinitionId(!params.containsKey(caseTypeParam) ? null : params.get(caseTypeParam));
			
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
		
		if(metaInf != null && !(metaInf instanceof SimpleCaseFormCreateMetaInf))
			throw new IllegalArgumentException("Wrong meta inf argument provided. Should be of class: "+SimpleCaseFormCreateMetaInf.class.getName()+", but got: "+metaInf.getClass().getName());
		
		Element submissionEl = document.getSubmissionElement();
		String action = submissionEl.getAttribute(FormManagerUtil.action_att);
		
		if(action == null || "".equals(action))
			return;
		
		SimpleCaseFormCreateMetaInf scmetaInf = (SimpleCaseFormCreateMetaInf)metaInf;
		
		URIUtil uriUtil = new URIUtil(action);
		uriUtil.setParameter(type, "1");
		
		if(scmetaInf != null) {
			
			uriUtil.setParameter(pdIdParam, scmetaInf.getProcessDefinitionId() == null ? "" : scmetaInf.getProcessDefinitionId());
			uriUtil.setParameter(userIdParam, scmetaInf.getInitiatorId() == null ? "" : scmetaInf.getInitiatorId());
			uriUtil.setParameter(caseCategoryIdParam, scmetaInf.getCaseCategoryId() == null ? "" : scmetaInf.getCaseCategoryId());
			uriUtil.setParameter(caseTypeParam, scmetaInf.getCaseTypeId() == null ? "" : scmetaInf.getCaseTypeId());
		}
		
		submissionEl.setAttribute(FormManagerUtil.action_att, uriUtil.getUri());
		this.metaInf = scmetaInf;
	}
}