package com.idega.documentmanager.business;

import java.util.Locale;

import org.w3c.dom.Document;

import com.idega.documentmanager.util.FormManagerUtil;


/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.3 $
 *
 * Last modified: $Date: 2008/06/18 08:00:10 $ by $Author: civilis $
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
}