package com.idega.documentmanager.business.form.beans;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.data.StringInputStream;
import com.idega.documentmanager.business.PersistenceManager;
import com.idega.documentmanager.business.form.manager.CacheManager;
import com.idega.documentmanager.business.form.manager.util.FormManagerUtil;
import com.idega.documentmanager.business.form.manager.util.InitializationException;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 */
public class FormDocument implements IFormDocument {
	
	protected static Log logger = LogFactory.getLog(FormDocument.class);
	
	protected Document form_xforms;
	protected String form_id;
	protected FormComponentDocument pages_container;
	
	private Locale default_document_locale;
	private Map<String, IFormComponent> form_components;
	private Document components_xml;
	private int last_component_id = 0;
	private boolean document_changed = true;
	private PersistenceManager persistence_manager;
	private DocumentBuilder builder;
	
	public FormDocument() {
		
		pages_container = new FormComponentDocument();
		pages_container.setFormDocument(this);
		pages_container.setFormDocument(pages_container);
	}
	
	public static FormDocument createDocument(String form_id, LocalizedStringBean form_name, PersistenceManager persistence_manager) throws NullPointerException, Exception {

		if(form_id == null)
			throw new NullPointerException("Form id is not provided");
		
		FormDocument form_doc = new FormDocument();
		form_doc.persistence_manager = persistence_manager;
		
		form_doc.form_xforms = CacheManager.getInstance().getFormXformsTemplateCopy();
		
		form_doc.pages_container.setLoad(true);
		
		if(form_name != null)
			form_doc.setFormTitle(form_name);
		
		form_doc.loadDocumentInternal(form_doc.getXformsDocument(), form_id);
		form_doc.putIdValues();

		return form_doc;
	}
	
	public com.idega.documentmanager.business.form.Document getDocument() {
		
		return pages_container;
	}
	
	protected void putIdValues() {
		Element model = FormManagerUtil.getElementByIdFromDocument(form_xforms, FormManagerUtil.head_tag, FormManagerUtil.form_id);
		model.setAttribute(FormManagerUtil.id_att, form_id);
		
		Element form_id_element = (Element)model.getElementsByTagName(FormManagerUtil.form_id_tag).item(0);
		FormManagerUtil.setElementsTextNodeValue(form_id_element, form_id);
	
	}
	public String generateNewComponentId() {
		
		return FormManagerUtil.CTID+(++last_component_id);
	}
	
	public Document getXformsDocument() {
		return form_xforms;
	}
	protected Map<String, IFormComponent> getFormComponents() {
		
		if(form_components == null)
			form_components = new HashMap<String, IFormComponent>();
		
		return form_components;
	}
	
	public void persist() throws Exception {
		
		if(persistence_manager == null) {
			logger.error("Tried to call persist when persistence manager not set");
			throw new NullPointerException("Persistence manager not set");
		}
		persistence_manager.saveForm(getFormId(), getXformsDocument());
	}
	
	public void setFormDocumentModified(boolean changed) {
		document_changed = changed;
	}
	
	public boolean isFormDocumentModified() {
		return document_changed;
	}
	public Document getComponentsXml() {
		
		return components_xml;
	}
	public void setComponentsXml(Document xml) {
		components_xml = xml;
	}
	public String getFormId() {
		return form_id;
	}
	public Document getFormXFormsDocumentClone() {
		return (Document)form_xforms.cloneNode(true);
	}
	
	protected void loadDocumentInternal(Document xforms_doc, String form_id) throws Exception {
		
		this.form_xforms = xforms_doc;
		
		if(form_id != null)
			this.form_id = form_id;
		else
			this.form_id = FormManagerUtil.getFormId(xforms_doc);
		
		pages_container.setContainerElement(FormManagerUtil.getComponentsContainerElement(xforms_doc));
		pages_container.loadContainerComponents();
	}
	
	public static FormDocument loadDocument(String form_id, PersistenceManager persistence_manager) throws InitializationException, Exception {
		
		if(form_id == null)
			throw new NullPointerException("Form id was not provided");
		
		FormDocument form_doc = new FormDocument();
		form_doc.persistence_manager = persistence_manager;
		form_doc.pages_container.setLoad(true);
		
		Document xforms_doc = form_doc.loadXFormsDocument(form_id);
		form_doc.loadDocumentInternal(xforms_doc, form_id);
		
		return form_doc;
	}
	
	public static FormDocument loadDocument(Document xforms_doc, PersistenceManager persistence_manager) throws InitializationException, Exception {
		
		FormDocument form_doc = new FormDocument();
		form_doc.persistence_manager = persistence_manager;
		form_doc.pages_container.setLoad(true);
		
		form_doc.loadDocumentInternal(xforms_doc, null);
		
		return form_doc;
	}
	
	protected Document loadXFormsDocument(String form_id) throws Exception {
		
		if(persistence_manager == null) {
			logger.error("Tried to call loadXFormsDocument when persistence manager not set");
			throw new NullPointerException("Persistence manager not set");
		}
		
		Document xforms_doc = persistence_manager.loadFormAndLock(form_id);
		
		if(xforms_doc == null)
			throw new NullPointerException("Form document was not found by provided id");
		
		return xforms_doc;
	}
	
	public String getXFormsDocumentSourceCode() throws Exception {
		
		return FormManagerUtil.serializeDocument(form_xforms);
	}
	public void setXFormsDocumentSourceCode(String src_code) throws Exception {
		
		if(builder == null)
			builder = FormManagerUtil.getDocumentBuilder();
		
		Document new_xforms_document = builder.parse(new StringInputStream(src_code));
		clear();
		pages_container.setLoad(true);
		loadDocumentInternal(new_xforms_document, null);
	}
	
	public void setFormTitle(LocalizedStringBean form_name) {
		
		if(form_name == null)
			throw new NullPointerException("Form name is not provided.");
		
		Element title = (Element)form_xforms.getElementsByTagName(FormManagerUtil.title_tag).item(0);
		Element output = (Element)title.getElementsByTagName(FormManagerUtil.output_tag).item(0);
		
		try {
			
			FormManagerUtil.putLocalizedText(null, null, output, form_xforms, form_name);
			
		} catch (Exception e) {
			logger.error("Could not set localized text for title element", e);
		}
	}
	
	public LocalizedStringBean getFormTitle() {
		
		return FormManagerUtil.getTitleLocalizedStrings(form_xforms);
	}
	
	public Locale getDefaultLocale() {
		
		if(default_document_locale == null)
			default_document_locale = FormManagerUtil.getDefaultFormLocale(form_xforms);
		
		return default_document_locale;
	}
	
	public void tellComponentId(String component_id) {
		
		int id_number = FormManagerUtil.parseIdNumber(component_id);
		
		if(id_number > last_component_id)
			last_component_id = id_number;
	}
	
	public void clear() {
		
		form_xforms = null;
		form_id = null;
		default_document_locale = null;
		form_components = null;
		components_xml = null;
		last_component_id = 0;
		document_changed = true;
		pages_container.clear();
		pages_container.setFormDocument(this);
		pages_container.setFormDocument(pages_container);
	}
	
	public void setPersistenceManager(PersistenceManager persistence_manager) {
		this.persistence_manager = persistence_manager;
	}
}