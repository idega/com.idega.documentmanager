package com.idega.documentmanager.business;

import org.w3c.dom.Document;


/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.4 $
 *
 * Last modified: $Date: 2008/10/07 13:06:14 $ by $Author: civilis $
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
	/*
	public void setLocale(Locale locale) {
		Document doc = getXformsDocument();
		
		//ComponentsGenerator componentsGenerator = ComponentsGeneratorImpl.getInstance();
		FormManagerUtil.modifyXFormsDocumentForViewing(doc);
		FormManagerUtil.setCurrentFormLocale(doc, locale);
		setXformsDocument(doc);
			
//		componentsGenerator.setDocument(doc);
//		try {
//			doc = componentsGenerator.generateHtmlComponentsDocument();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	*/
}