package com.idega.documentmanager.business;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.w3c.dom.Document;

import com.idega.documentmanager.generator.ComponentsGenerator;
import com.idega.documentmanager.generator.impl.ComponentsGeneratorImpl;
import com.idega.documentmanager.util.FormManagerUtil;


/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2008/05/23 08:41:20 $ by $Author: anton $
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