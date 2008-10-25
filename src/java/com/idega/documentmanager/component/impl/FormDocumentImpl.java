package com.idega.documentmanager.component.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.documentmanager.business.PersistedFormDocument;
import com.idega.documentmanager.business.PersistenceManager;
import com.idega.documentmanager.business.component.Component;
import com.idega.documentmanager.business.component.Page;
import com.idega.documentmanager.business.component.PageThankYou;
import com.idega.documentmanager.business.component.properties.ParametersManager;
import com.idega.documentmanager.business.component.properties.PropertiesDocument;
import com.idega.documentmanager.business.ext.FormVariablesHandler;
import com.idega.documentmanager.component.FormComponent;
import com.idega.documentmanager.component.FormComponentPage;
import com.idega.documentmanager.component.beans.LocalizedStringBean;
import com.idega.documentmanager.component.properties.impl.ComponentPropertiesDocument;
import com.idega.documentmanager.component.properties.impl.ConstUpdateType;
import com.idega.documentmanager.context.DMContext;
import com.idega.documentmanager.form.impl.Form;
import com.idega.documentmanager.generator.ComponentsGenerator;
import com.idega.documentmanager.generator.impl.ComponentsGeneratorImpl;
import com.idega.documentmanager.manager.XFormsManagerDocument;
import com.idega.documentmanager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.23 $
 *
 * Last modified: $Date: 2008/10/25 18:30:18 $ by $Author: civilis $
 */
public class FormDocumentImpl extends FormComponentContainerImpl implements com.idega.documentmanager.business.Document, com.idega.documentmanager.component.FormDocument {
	
	private String confirmationPageId;
	private FormVariablesHandler formVariablesHandler;
	private String thxPageId;
	private LocalizedStringBean formTitle;
	private LocalizedStringBean formErrorMsg;
	private Long formId;
	private PropertiesDocument properties;
	private List<String> registeredForLastPageIdPages;
	private ParametersManager parametersManager;
	private String formType;
	private Map<Locale, Document> localizedComponentsDocuments;
	
	private Form form;
	private DMContext context;
	
	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}

	public void setContainerElement(Element container_element) {
		getXFormsManager().setComponentsContainer(this, container_element);
	}
	
	@Override
	public XFormsManagerDocument getXFormsManager() {
		
		return getContext().getXformsManagerFactory().getXformsManagerDocument();
	}
	
	public Document getXformsDocument() {
		return getContext().getXformsXmlDoc();
	}
	
	public void populateSubmissionDataWithXML(Document submission, boolean clean) {
		
		getXFormsManager().populateSubmissionDataWithXML(this, submission, clean);
	}
	
	public void setFormDocumentModified(boolean changed) {
		getForm().setFormDocumentModified(changed);
		
		if (changed)
			getLocalizedComponentsDocuments().clear();
	}
	
	public boolean isFormDocumentModified() {
		return getForm().isFormDocumentModified();
	}
	
	public Document getComponentsXml(Locale locale) {
		
		Map<Locale, Document> localizedComponentsDocuments = getLocalizedComponentsDocuments();
		
		Document doc;
		
		if(!localizedComponentsDocuments.containsKey(locale)) {
			
			try {

//				TODO: change this to spring bean etc (now relies on impl)
				ComponentsGenerator componentsGenerator = ComponentsGeneratorImpl.getInstance();
				Document xformClone = (Document)getContext().getXformsXmlDoc().cloneNode(true);

				FormManagerUtil.modifyXFormsDocumentForViewing(xformClone);
				FormManagerUtil.setCurrentFormLocale(xformClone, locale);
				FormManagerUtil.modifyFormForLocalisationInFormbuilder(xformClone);
				
				componentsGenerator.setDocument(xformClone);

				doc = componentsGenerator.generateHtmlComponentsDocument();
				
				localizedComponentsDocuments.put(locale, doc);
				getForm().setFormDocumentModified(false);
				
			} catch (Exception e) {
				
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Exception while generating components html document", e);
				doc = localizedComponentsDocuments.isEmpty() ? null : localizedComponentsDocuments.values().iterator().next();
			}
			
		} else {
			doc = localizedComponentsDocuments.get(locale);
		}
		
		return doc;
	}
	
	public Long getFormId() {
		
		if(formId == null) {
			
			try {
				formId = new Long(FormManagerUtil.getFormId(getContext().getXformsXmlDoc()));
				
			} catch (Exception e) { }
		}
		
		return formId;
	}
	
	public void setFormId(Long formId) {
		
		FormManagerUtil.setFormId(getContext().getXformsXmlDoc(), String.valueOf(formId));
		this.formId = formId;
	}
	
	@Override
	public String getId() {
		return String.valueOf(getFormId());
	}
	
	public Locale getDefaultLocale() {
		return getForm().getDefaultLocale();
	}

	public Page addPage(String nextSiblingPageId) {
		Page page = (Page)super.addComponent(FormComponentFactory.page_type, nextSiblingPageId);
		componentsOrderChanged();
		return page;
	}
	@Override
	public Component addComponent(String component_type, String component_after_this_id) throws NullPointerException {
		throw new NullPointerException("Use addPage method instead");
	}
	public Page getPage(String page_id) {
		return (Page)getContainedComponent(page_id);
	}
//	@Override
//	public void tellComponentId(String component_id) {
//		getForm().tellComponentId(component_id);
//	}
	public List<String> getContainedPagesIdList() {
		return getContainedComponentsIds();
	}
	
	public String generateNewComponentId() {
		return getForm().generateNewComponentId();
	}
	
	public String getFormSourceCode() throws Exception {
		return getForm().getXFormsDocumentSourceCode();
	}
	
	public void setFormSourceCode(String newSourceCode) throws Exception {
		getForm().setXFormsDocumentSourceCode(newSourceCode);
	}
	
	public LocalizedStringBean getFormTitle() {
		
		if(formTitle == null)
			formTitle = FormManagerUtil.getFormTitle(getContext().getXformsXmlDoc());
		
		return formTitle;
	}
		
	public LocalizedStringBean getFormErrorMsg() {
	    
	    if (formErrorMsg == null) 
			formErrorMsg = FormManagerUtil.getFormErrorMsg(getContext().getXformsXmlDoc());
	    
	    return formErrorMsg;
	}
	
	protected void clearFormTitle() {
		formTitle = null;
	}
	
	protected void clearFormId() {
		formId = null;
	}
	
	public void setFormTitle(LocalizedStringBean formTitle) {
		
		if(formTitle == null)
			throw new NullPointerException("Form title is not provided.");
		
		FormManagerUtil.setFormTitle(getContext().getXformsXmlDoc(), formTitle);
		this.formTitle = formTitle;
	}
	
	public void setFormErrorMsg(LocalizedStringBean formError) {
	    
	    	if(formError == null)
	    	    throw new NullPointerException("Form error message is not provided.");
	
	    	FormManagerUtil.setFormErrorMsg(getContext().getXformsXmlDoc(), formError);
	    	this.formErrorMsg = formError;
    
	}
	
	public void rearrangeDocument() {
		rearrangeComponents();
	}
	@Override
	public void remove() {
		throw new NullPointerException("You shall not remove ME!!!!");
	}
	@Override
	public void componentsOrderChanged() {

		Map<String, FormComponent> contained_components = getContainedComponents();
		int components_amount = getContainedComponents().size();
		int i = 0;
		setConfirmationPageId(null);
		setThxPageId(null);
		
		for (String comp_id : getContainedComponentsIds()) {
			
			FormComponentPage page = (FormComponentPage)contained_components.get(comp_id);
			if(page == null)
				throw new NullPointerException("Component, which id was provided in list was not found. Provided: "+getId());
			
			page.setPageSiblings(
					i == 0 ? null : (FormComponentPage)contained_components.get(getContainedComponentsIds().get(i - 1)),
					(i+1) == components_amount ? null : (FormComponentPage)contained_components.get(getContainedComponentsIds().get(i + 1))
			);
			
			page.pagesSiblingsChanged();
			
			if(page.getType().equals(FormComponentFactory.confirmation_page_type))
				setConfirmationPageId(page.getId());
			else if(page.getType().equals(FormComponentFactory.page_type_thx))
				setThxPageId(page.getId());
				
			i++;
		}
		announceRegisteredForLastPage();
	}
	protected void announceRegisteredForLastPage() {
		
		for (String registered_id : getRegisteredForLastPageIdPages())
			((FormComponentPage)getComponent(registered_id)).announceLastPage(getThxPageId());
	}
	
	@Override
	public void rearrangeComponents() {
		
		List<String> contained_components_id_list = getContainedComponentsIds();
		int components_amount = contained_components_id_list.size();
		Map<String, FormComponent> contained_components = getContainedComponents();
		
		for (int i = components_amount-1; i >= 0; i--) {
			
			String component_id = contained_components_id_list.get(i);
			
			if(contained_components.containsKey(component_id)) {
				
				FormComponentPage page = (FormComponentPage)contained_components.get(component_id);
				
				if(i != components_amount-1) {
					
					page.setNextSiblingRerender(
						contained_components.get(
								contained_components_id_list.get(i+1)
						)
					);
				} else
					page.setNextSiblingRerender(null);
				
				page.setPageSiblings(
						i == 0 ? null : (FormComponentPage)contained_components.get(getContainedComponentsIds().get(i - 1)),
						(i+1) == components_amount ? null : (FormComponentPage)contained_components.get(getContainedComponentsIds().get(i + 1))
				);
				page.pagesSiblingsChanged();
				
			} else
				throw new NullPointerException("Component, which id was provided in list was not found. Provided: "+component_id);
		}
	}
	@Override
	public void loadContainerComponents() {
		super.loadContainerComponents();
		componentsOrderChanged();
	}
	
	@Override
	public void setProperties() {
		
		ComponentPropertiesDocument properties = (ComponentPropertiesDocument)getProperties();
		
		if(properties == null)
			return;
		
		properties.setPlainStepsVisualizationUsed(getXFormsManager().getIsStepsVisualizationUsed(this));
		properties.setPlainSubmissionAction(getXFormsManager().getSubmissionAction(this));
	}
	
	public void save() throws Exception {
		
		PersistenceManager persistenceManager = getContext().getPersistenceManager();
		
		if(persistenceManager == null)
			throw new NullPointerException("Persistence manager not set");
		
		PersistedFormDocument formDocument = persistenceManager.saveForm(this);
		setFormType(formDocument.getFormType());
		setFormId(formDocument.getFormId());
	}
	
	public Page getConfirmationPage() {
	
		return (Page)getFormConfirmationPage();
	}
	public FormComponentPage getFormConfirmationPage() {
	
		return getConfirmationPageId() == null ? null : (FormComponentPage)getContainedComponent(getConfirmationPageId());
	}
	public PageThankYou getThxPage() {
		
		return getThxPageId() == null ? null : (PageThankYou)getContainedComponent(getThxPageId());
	}
	
	protected List<String> getRegisteredForLastPageIdPages() {
		
		if(registeredForLastPageIdPages == null)
			registeredForLastPageIdPages = new ArrayList<String>();
		
		return registeredForLastPageIdPages;
	}
	
	public void registerForLastPage(String register_page_id) {
		
		if(!getContainedComponents().containsKey(register_page_id))
			throw new IllegalArgumentException("I don't contain provided page id: "+register_page_id);
		
		if(!getRegisteredForLastPageIdPages().contains(register_page_id))
			getRegisteredForLastPageIdPages().add(register_page_id);
	}
	
	public Page addConfirmationPage(String nextSiblingPageId) {

		if(getConfirmationPage() != null)
			throw new IllegalArgumentException("Confirmation page already exists in the form");
		
		Page page = (Page)super.addComponent(FormComponentFactory.confirmation_page_type, nextSiblingPageId);
		
		componentsOrderChanged();
		addToConfirmationPage();
		
		return page;
	}
	
	public Element getAutofillModelElement() {
		
		return getXFormsManager().getAutofillAction(this);
	}
	
	public Element getFormDataModelElement() {
		
		return getXFormsManager().getFormDataModelElement(this);
	}
	
	public Element getFormMainDataInstanceElement() {
		
		return getXFormsManager().getFormMainDataInstanceElement(this);
	}
	
	public void clear() {
		
		getLocalizedComponentsDocuments().clear();
		setConfirmationPageId(null);
		setThxPageId(null);
		setRegisteredForLastPageIdPages(null);
		clearFormTitle();
		clearFormId();
		parametersManager = null;
		super.clear();
	}
	
	@Override
	public void unregisterComponent(String component_id) {
		
		super.unregisterComponent(component_id);
		getRegisteredForLastPageIdPages().remove(component_id);
	}
	
	@Override
	public PropertiesDocument getProperties() {
		
		if(properties == null) {
			ComponentPropertiesDocument properties = new ComponentPropertiesDocument();
			properties.setComponent(this);
			this.properties = properties;
		}
		
		return properties;
	}
	
	public Element getSectionsVisualizationInstanceElement() {
	
		return getXFormsManager().getSectionsVisualizationInstanceElement(this);
	}
	
	@Override
	public void update(ConstUpdateType what) {
		
		getXFormsManager().update(this, what);
		
		switch (what) {
		
		case STEPS_VISUALIZATION_USED:
			
			rearrangeComponents();
			break;
			
		case SUBMISSION_ACTION:
			
			break;
			
		default:
			break;
		}
	}

	public String getConfirmationPageId() {
		return confirmationPageId;
	}

	public void setConfirmationPageId(String confirmationPageId) {
		this.confirmationPageId = confirmationPageId;
	}

	public String getThxPageId() {
		return thxPageId;
	}

	public void setThxPageId(String thxPageId) {
		this.thxPageId = thxPageId;
	}

	public void setRegisteredForLastPageIdPages(
			List<String> registeredForLastPageIdPages) {
		this.registeredForLastPageIdPages = registeredForLastPageIdPages;
	}
	
	public ParametersManager getParametersManager() {
		
		if(parametersManager == null) {
			parametersManager = new ParametersManager();
			parametersManager.setFormDocumentComponent(this);
		}
		
		return parametersManager;
	}

	public Element getSubmissionElement() {
		return FormManagerUtil.getSubmissionElement(getXformsDocument());
	}
	
	public Element getSubmissionInstanceElement() {
		return FormManagerUtil.getFormSubmissionInstanceElement(getXformsDocument());
	}
	
	public FormVariablesHandler getFormVariablesHandler() {
		
		if(formVariablesHandler == null)
			formVariablesHandler = new FormVariablesHandler(getXformsDocument());
		
		return formVariablesHandler;
	}
	
	public String getFormType() {
		return formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;
	}

	public Map<Locale, Document> getLocalizedComponentsDocuments() {
		
		if(localizedComponentsDocuments == null)
			localizedComponentsDocuments = new HashMap<Locale, Document>(4);
		
		return localizedComponentsDocuments;
	}
	
//	@Override
	public boolean isReadonly() {
		return getXFormsManager().isReadonly(this);
	}

//	@Override
	public void setReadonly(boolean readonly) {

		getXFormsManager().setReadonly(this, readonly);
	}
	
//	@Override
	public void setPdfForm(boolean generatePdf) {

		getXFormsManager().setPdfForm(this, generatePdf);
	}
	
//	@Override
	public boolean isPdfForm() {

		return getXFormsManager().isPdfForm(this);
	}
	
	public DMContext getContext() {
		return context;
	}

	public void setContext(DMContext context) {
		this.context = context;
	}
	
	public Document getComponentsXforms() {
		return getContext().getCacheManager().getComponentsXforms();
	}
}