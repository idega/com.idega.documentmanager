package com.idega.documentmanager.business.ext;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpression;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.documentmanager.business.component.properties.DocumentMetaInformationManager;
import com.idega.documentmanager.component.FormDocument;
import com.idega.documentmanager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2007/10/22 15:38:17 $ by $Author: civilis $
 */
public class SimpleCaseFormCreateDMIManager implements DocumentMetaInformationManager {

	private FormDocument document;
	private SimpleCaseFormCreateMetaInf metaInf;
	
	private static final String pdIdElName = "pd_id";
	private static final String userIdElName = "user_id";
	private static final String caseCategoryIdElName = "case_category_id";
	private static final String caseTypeElName = "case_type_id";
	
	public Object resolve() {

		if(document == null)
			throw new NullPointerException("FormDocument not set. The manager needs to be set to Document");

		if(metaInf == null) {
			
			Element modelEl = document.getFormSubmissionInstanceModelElement();
			Element el = getSimpleCasesProcessDefinitionMetaInfElement(modelEl);
			
			if(el == null)
				return null;
			
			SimpleCaseFormCreateMetaInf scmetaInf = new SimpleCaseFormCreateMetaInf();
			
			Element idEl = (Element)el.getElementsByTagName(pdIdElName).item(0);
			scmetaInf.setProcessDefinitionId(idEl == null ? null : idEl.getTextContent());
			
			idEl = (Element)el.getElementsByTagName(userIdElName).item(0);
			scmetaInf.setInitiatorId(idEl == null ? null : idEl.getTextContent());
			
			idEl = (Element)el.getElementsByTagName(caseCategoryIdElName).item(0);
			scmetaInf.setCaseCategoryId(idEl == null ? null : idEl.getTextContent());
			
			idEl = (Element)el.getElementsByTagName(caseTypeElName).item(0);
			scmetaInf.setCaseTypeId(idEl == null ? null : idEl.getTextContent());
			
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
		
		Element modelEl = document.getFormSubmissionInstanceModelElement();
		Element el = getSimpleCasesProcessDefinitionMetaInfElement(modelEl);
		
		if(el != null)
			el.getParentNode().removeChild(el);
		
		SimpleCaseFormCreateMetaInf scmetaInf = (SimpleCaseFormCreateMetaInf)metaInf;
		
		if(scmetaInf != null) {
			
			Document xformsDoc = document.getContext().getXformsXmlDoc();
			
			Element metaInfEl = xformsDoc.createElement(simpleCasesProcessDefinitionMetaInfElementName);
			
			Element miel = xformsDoc.createElement(userIdElName);
			miel.setTextContent(scmetaInf.getInitiatorId() == null ? "" : scmetaInf.getInitiatorId());
			metaInfEl.appendChild(miel);
			miel = xformsDoc.createElement(pdIdElName);
			miel.setTextContent(scmetaInf.getProcessDefinitionId() == null ? "" : scmetaInf.getProcessDefinitionId());
			metaInfEl.appendChild(miel);
			miel = xformsDoc.createElement(caseCategoryIdElName);
			miel.setTextContent(scmetaInf.getCaseCategoryId() == null ? "" : scmetaInf.getCaseCategoryId());
			metaInfEl.appendChild(miel);
			miel = xformsDoc.createElement(caseTypeElName);
			miel.setTextContent(scmetaInf.getCaseTypeId() == null ? "" : scmetaInf.getCaseTypeId());
			metaInfEl.appendChild(miel);
			
			Element instance = getFormSubmissionInstanceElement(modelEl);
			instance.appendChild(metaInfEl);
		}
		
		this.metaInf = scmetaInf;
	}
	
	private static XPathExpression simpleCasesProcessDefinitionMetaInfElementExp;
	private static XPathExpression instanceElementExp;
	private static final String simpleCasesProcessDefinitionMetaInfElementName = "simpleCasesProcessDefinitionMetaInf";
	
	private static synchronized XPathExpression getFormSubmissionInstanceElementExp() {
		
		if(instanceElementExp != null)
			return instanceElementExp;
		
		instanceElementExp = FormManagerUtil.compileXPathForXForms("//xf:instance/data");
		return instanceElementExp;
	}
	
	private static synchronized XPathExpression getSimpleCasesProcessDefinitionMetaInfElementExp() {
		
		if(simpleCasesProcessDefinitionMetaInfElementExp != null)
			return simpleCasesProcessDefinitionMetaInfElementExp;
		
		simpleCasesProcessDefinitionMetaInfElementExp = FormManagerUtil.compileXPathForXForms("//"+simpleCasesProcessDefinitionMetaInfElementName);
		return simpleCasesProcessDefinitionMetaInfElementExp;
	}
	
	public static Element getSimpleCasesProcessDefinitionMetaInfElement(Element submissionModelEl) {
		
		try {
			XPathExpression exp = getSimpleCasesProcessDefinitionMetaInfElementExp();
			
			synchronized (exp) {
				return (Element)exp.evaluate(submissionModelEl, XPathConstants.NODE);
			}
				
		} catch (XPathException e) {
			throw new RuntimeException("Could not evaluate XPath expression: " + e.getMessage(), e);
		}
	}
	
	public static Element getFormSubmissionInstanceElement(Element submissionModelEl) {
		
		try {
			XPathExpression exp = getFormSubmissionInstanceElementExp();
			
			synchronized (exp) {
				return (Element)exp.evaluate(submissionModelEl, XPathConstants.NODE);
			}
				
		} catch (XPathException e) {
			throw new RuntimeException("Could not evaluate XPath expression: " + e.getMessage(), e);
		}
	}
}