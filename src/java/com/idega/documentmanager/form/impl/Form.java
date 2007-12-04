package com.idega.documentmanager.form.impl;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;

import com.idega.data.StringInputStream;
import com.idega.documentmanager.business.PersistenceManager;
import com.idega.documentmanager.component.FormComponent;
import com.idega.documentmanager.component.beans.LocalizedStringBean;
import com.idega.documentmanager.component.impl.FormDocumentImpl;
import com.idega.documentmanager.context.DMContext;
import com.idega.documentmanager.util.FormManagerUtil;
import com.idega.documentmanager.util.InitializationException;
import com.idega.util.xml.XmlUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.4 $
 *
 * Last modified: $Date: 2007/12/04 14:02:28 $ by $Author: civilis $
 */
public class Form {
	
	protected static Logger logger = Logger.getLogger(Form.class.getName());
	
	protected FormDocumentImpl formDocument;
	
	private Locale defaultDocumentLocale;
	private Map<String, FormComponent> formComponents;
	private Document componentsXml;
	private int lastComponentId = 0;
	private boolean documentChanged = true;
	private DocumentBuilder builder;
	private DMContext context;
	
	public Form(DMContext context) {
		
		formDocument = new FormDocumentImpl();
		formDocument.setContext(context);
		formDocument.setForm(this);
		formDocument.setFormDocument(formDocument);
	}
	
	public static Form createDocument(String formId, LocalizedStringBean formTitle, DMContext context) throws NullPointerException, Exception {

		if(formId == null)
			throw new NullPointerException("Form id is not provided");
		
		Form form = new Form(context);
		form.setContext(context);
		form.formDocument.setLoad(true);
		
		context.setXformsXmlDoc(context.getCacheManager().getFormXformsTemplateCopy());
		form.loadDocumentInternal(formId);
		
		if(formTitle != null)
			form.formDocument.setFormTitle(formTitle);
		
		form.formDocument.setFormId(formId);

		return form;
	}
	
	public com.idega.documentmanager.business.Document getDocument() {
		
		return formDocument;
	}
	
	public String generateNewComponentId() {
		
		return FormManagerUtil.CTID+(++lastComponentId);
	}
	
	protected Map<String, FormComponent> getFormComponents() {
		
		if(formComponents == null)
			formComponents = new HashMap<String, FormComponent>();
		
		return formComponents;
	}
	
	public void setFormDocumentModified(boolean changed) {
		documentChanged = changed;
	}
	
	public boolean isFormDocumentModified() {
		return documentChanged;
	}
	public Document getComponentsXml() {
		
		return componentsXml;
	}
	public void setComponentsXml(Document xml) {
		componentsXml = xml;
	}
	public Document getFormXFormsDocumentClone() {
		return (Document)getContext().getXformsXmlDoc().cloneNode(true);
	}
	
	protected void loadDocumentInternal(String formId) throws Exception {
		
		Document xformsXmlDoc = getContext().getXformsXmlDoc();
		
		formDocument.setFormId(formId != null ? formId : FormManagerUtil.getFormId(xformsXmlDoc));
		formDocument.setContainerElement(FormManagerUtil.getComponentsContainerElement(xformsXmlDoc));
		formDocument.loadContainerComponents();
		formDocument.setProperties();
	}
	
	public static Form loadDocument(String formId, DMContext context) throws InitializationException, Exception {
		
		if(formId == null)
			throw new NullPointerException("Form id was not provided");
		
		Form form = new Form(context);
		form.setContext(context);
		form.formDocument.setLoad(true);
		
		Document xformsDoc = form.loadXFormsDocument(formId);
		context.setXformsXmlDoc(xformsDoc);
		
		form.loadDocumentInternal(formId);
		
		return form;
	}
	
	public static Form loadDocument(Document xformsXmlDoc, DMContext context) throws InitializationException, Exception {
		
		Form form = new Form(context);
		form.formDocument.setLoad(true);
		form.setContext(context);
		context.setXformsXmlDoc(xformsXmlDoc);
		
		form.loadDocumentInternal(null);
		
		return form;
	}
	
	public static Form loadDocument(String formId, Document xformsXmlDoc, DMContext context) throws InitializationException, Exception {
		
		Form form = loadDocument(xformsXmlDoc, context);
		form.formDocument.setFormId(formId);
		
		return form;
	}
	
	public static Form loadDocumentAndGenerateId(Document xformsXmlDoc, DMContext context) throws InitializationException, Exception {
		
		Form form = loadDocument(xformsXmlDoc, context);
		
		String defaultFormName = form.getDocument().getFormTitle().getString(form.getDefaultLocale());
		form.formDocument.setFormId(context.getPersistenceManager().generateFormId(defaultFormName));
		
		return form;
	}
	
	protected Document loadXFormsDocument(String form_id) throws Exception {
		
		PersistenceManager persistenceManager = getContext().getPersistenceManager();
		
		if(persistenceManager == null)
			throw new NullPointerException("Persistence manager not set");
		
		Document xforms_doc = persistenceManager.loadFormAndLock(form_id);
		
		if(xforms_doc == null)
			throw new NullPointerException("Form document was not found by provided id");
		
		return xforms_doc;
	}
	
	public String getXFormsDocumentSourceCode() throws Exception {
		
		return FormManagerUtil.serializeDocument(getContext().getXformsXmlDoc());
	}
	public void setXFormsDocumentSourceCode(String srcXml) throws Exception {
		
		if(builder == null)
			builder = XmlUtil.getDocumentBuilder();
		
		clear();
		formDocument.setLoad(true);
		
		Document xformsXmlDoc = builder.parse(new StringInputStream(srcXml));
		getContext().setXformsXmlDoc(xformsXmlDoc);
		loadDocumentInternal(null);
	}
	
	public Locale getDefaultLocale() {
		
		if(defaultDocumentLocale == null)
			defaultDocumentLocale = FormManagerUtil.getDefaultFormLocale(getContext().getXformsXmlDoc());
		
		return defaultDocumentLocale;
	}
	
	public void tellComponentId(String component_id) {
		
		int id_number = FormManagerUtil.parseIdNumber(component_id);
		
		if(id_number > lastComponentId)
			lastComponentId = id_number;
	}
	
	public void clear() {
		
		getContext().setXformsXmlDoc(null);
		defaultDocumentLocale = null;
		formComponents = null;
		componentsXml = null;
		lastComponentId = 0;
		documentChanged = true;
		formDocument.clear();
		formDocument.setForm(this);
		formDocument.setFormDocument(formDocument);
	}
	
	public DMContext getContext() {
		return context;
	}

	public void setContext(DMContext context) {
		this.context = context;
	}
}