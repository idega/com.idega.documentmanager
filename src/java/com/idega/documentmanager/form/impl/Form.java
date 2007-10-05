package com.idega.documentmanager.form.impl;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.data.StringInputStream;
import com.idega.documentmanager.business.PersistenceManager;
import com.idega.documentmanager.component.FormComponent;
import com.idega.documentmanager.component.beans.LocalizedStringBean;
import com.idega.documentmanager.component.impl.FormDocumentImpl;
import com.idega.documentmanager.context.DMContext;
import com.idega.documentmanager.util.FormManagerUtil;
import com.idega.documentmanager.util.InitializationException;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2007/10/05 11:42:37 $ by $Author: civilis $
 */
public class Form {
	
	protected static Logger logger = Logger.getLogger(Form.class.getName());
	
	protected String formId;
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
	
	public static Form createDocument(String formId, LocalizedStringBean formName, DMContext context) throws NullPointerException, Exception {

		if(formId == null)
			throw new NullPointerException("Form id is not provided");
		
		Form formDoc = new Form(context);
		formDoc.setContext(context);
		formDoc.formDocument.setLoad(true);
		
		context.setXformsXmlDoc(context.getCacheManager().getFormXformsTemplateCopy());
		
		if(formName != null)
			formDoc.setFormTitle(formName);
		
		formDoc.loadDocumentInternal(formId);
		formDoc.putIdValues();

		return formDoc;
	}
	
	public com.idega.documentmanager.business.Document getDocument() {
		
		return formDocument;
	}
	
	protected void putIdValues() {
		Element model = FormManagerUtil.getElementByIdFromDocument(getContext().getXformsXmlDoc(), FormManagerUtil.head_tag, FormManagerUtil.form_id);
		model.setAttribute(FormManagerUtil.id_att, formId);
		
		Element form_id_element = (Element)model.getElementsByTagName(FormManagerUtil.form_id_tag).item(0);
		FormManagerUtil.setElementsTextNodeValue(form_id_element, formId);
	
	}
	public String generateNewComponentId() {
		
		return FormManagerUtil.CTID+(++lastComponentId);
	}
	
	public Document getXformsDocument() {
		return getContext().getXformsXmlDoc();
	}
	
	protected Map<String, FormComponent> getFormComponents() {
		
		if(formComponents == null)
			formComponents = new HashMap<String, FormComponent>();
		
		return formComponents;
	}
	
	public void persist() throws Exception {
		
		PersistenceManager persistenceManager = getContext().getPersistenceManager();
		
		if(persistenceManager == null)
			throw new NullPointerException("Persistence manager not set");
		
		persistenceManager.saveForm(getFormId(), getXformsDocument());
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
	public String getFormId() {
		return formId;
	}
	public Document getFormXFormsDocumentClone() {
		return (Document)getContext().getXformsXmlDoc().cloneNode(true);
	}
	
	protected void loadDocumentInternal(String formId) throws Exception {
		
		Document xformsXmlDoc = getContext().getXformsXmlDoc();
		
		this.formId = formId != null ? formId : FormManagerUtil.getFormId(xformsXmlDoc);
		
		formDocument.setContainerElement(FormManagerUtil.getComponentsContainerElement(xformsXmlDoc));
		formDocument.loadContainerComponents();
		formDocument.setProperties();
	}
	
	public static Form loadDocument(String formId, DMContext context) throws InitializationException, Exception {
		
		if(formId == null)
			throw new NullPointerException("Form id was not provided");
		
		Form formDoc = new Form(context);
		formDoc.setContext(context);
		formDoc.formDocument.setLoad(true);
		
		Document xformsDoc = formDoc.loadXFormsDocument(formId);
		context.setXformsXmlDoc(xformsDoc);
		
		formDoc.loadDocumentInternal(formId);
		
		return formDoc;
	}
	
	public static Form loadDocument(Document xformsXmlDoc, DMContext context) throws InitializationException, Exception {
		
		Form form_doc = new Form(context);
		form_doc.formDocument.setLoad(true);
		form_doc.setContext(context);
		context.setXformsXmlDoc(xformsXmlDoc);
		
		form_doc.loadDocumentInternal(null);
		
		return form_doc;
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
			builder = FormManagerUtil.getDocumentBuilder();
		
		clear();
		formDocument.setLoad(true);
		
		Document xformsXmlDoc = builder.parse(new StringInputStream(srcXml));
		getContext().setXformsXmlDoc(xformsXmlDoc);
		loadDocumentInternal(null);
	}
	
	public void setFormTitle(LocalizedStringBean form_name) {
		
		if(form_name == null)
			throw new NullPointerException("Form name is not provided.");
		
		Document xformsXmlDoc = getContext().getXformsXmlDoc();
		
		Element title = (Element)xformsXmlDoc.getElementsByTagName(FormManagerUtil.title_tag).item(0);
		Element output = (Element)title.getElementsByTagName(FormManagerUtil.output_tag).item(0);
		
		try {
			
			FormManagerUtil.putLocalizedText(null, null, output, xformsXmlDoc, form_name);
			
		} catch (Exception e) {
			logger.log(Level.WARNING, "Could not set localized text for title element", e);
		}
	}
	
	public LocalizedStringBean getFormTitle() {
		
		return FormManagerUtil.getTitleLocalizedStrings(getContext().getXformsXmlDoc());
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
		formId = null;
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