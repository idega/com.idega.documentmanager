package com.idega.documentmanager.business;

import org.w3c.dom.Document;


/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2008/04/10 01:08:00 $ by $Author: civilis $
 */
public class PersistedFormDocument {
	
	private Document xformsDocument;
	private Long formId;
	private String formType;
	
	public Document getXformsDocument() {
		return xformsDocument;
	}
	public void setXformsDocument(Document xformsDocument) {
		this.xformsDocument = xformsDocument;
	}
	public Long getFormId() {
		return formId;
	}
	public void setFormId(Long formId) {
		this.formId = formId;
	}
	public String getFormType() {
		return formType;
	}
	public void setFormType(String formType) {
		this.formType = formType;
	}
}