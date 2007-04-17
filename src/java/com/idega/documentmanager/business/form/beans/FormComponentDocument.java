package com.idega.documentmanager.business.form.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.documentmanager.business.form.Component;
import com.idega.documentmanager.business.form.Page;
import com.idega.documentmanager.business.form.PageThankYou;
import com.idega.documentmanager.business.form.PropertiesDocument;
import com.idega.documentmanager.business.form.manager.CacheManager;
import com.idega.documentmanager.business.form.manager.IXFormsManager;
import com.idega.documentmanager.business.form.manager.XFormsManagerDocument;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 */
public class FormComponentDocument extends FormComponentContainer implements com.idega.documentmanager.business.form.Document, IFormComponentDocument {
	
	protected IFormDocument document;
	protected String confirmation_page_id;
	protected String thx_page_id;
	protected PropertiesDocument properties;
	
	protected List<String> registered_for_last_page_id_pages;
	
	public void setContainerElement(Element container_element) {
		((XFormsManagerDocument)getXFormsManager()).setComponentsContainer(container_element);
	}
	
	@Override
	public IXFormsManager getXFormsManager() {
		
		if(xforms_manager == null) {
			
			xforms_manager = new XFormsManagerDocument();
			xforms_manager.setCacheManager(CacheManager.getInstance());
			xforms_manager.setComponentParent(parent);
			xforms_manager.setFormComponent(this);
			xforms_manager.setFormDocument(form_document);
		}
		
		return xforms_manager;
	}
	
	public Document getXformsDocument() {
		return document.getXformsDocument();
	}
	
	public void setFormDocumentModified(boolean changed) {
		document.setFormDocumentModified(changed);
	}
	
	public boolean isFormDocumentModified() {
		return document.isFormDocumentModified();
	}
	
	public Document getComponentsXml() {
		return document.getComponentsXml();
	}
	
	public void setComponentsXml(Document xml) {
		document.setComponentsXml(xml);
	}
	
	public String getFormId() {
		return document.getFormId();
	}
	
	@Override
	public String getId() {
		return document.getFormId();
	}
	
	public Locale getDefaultLocale() {
		return document.getDefaultLocale();
	}
	public void setFormDocument(IFormDocument document) {
		this.document = document;
	}
	public Page addPage(String page_after_this_id) throws NullPointerException {
		Page page = (Page)super.addComponent(FormComponentFactory.page_type, page_after_this_id);
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
	@Override
	public void tellComponentId(String component_id) {
		document.tellComponentId(component_id);
	}
	public List<String> getContainedPagesIdList() {
		return getContainedComponentsIdList();
	}
	
	public String generateNewComponentId() {
		return document.generateNewComponentId();
	}
	
	public String getFormSourceCode() throws Exception {
		return document.getXFormsDocumentSourceCode();
	}
	
	public void setFormSourceCode(String new_source_code) throws Exception {
		document.setXFormsDocumentSourceCode(new_source_code);
	}
	
	public LocalizedStringBean getFormTitle() {
		return document.getFormTitle();
	}
	
	public void setFormTitle(LocalizedStringBean form_name) throws Exception {
		document.setFormTitle(form_name);
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

		Map<String, IFormComponent> contained_components = getContainedComponents();
		int components_amount = getContainedComponents().size();
		int i = 0;
		confirmation_page_id = null;
		thx_page_id = null;
		
		for (String comp_id : getContainedComponentsIdList()) {
			
			IFormComponentPage page = (IFormComponentPage)contained_components.get(comp_id);
			if(page == null)
				throw new NullPointerException("Component, which id was provided in list was not found. Provided: "+component_id);
			
			page.setPageSiblings(
					i == 0 ? null : (IFormComponentPage)contained_components.get(getContainedComponentsIdList().get(i - 1)),
					(i+1) == components_amount ? null : (IFormComponentPage)contained_components.get(getContainedComponentsIdList().get(i + 1))
			);
			
			page.pagesSiblingsChanged();
			
			if(page.getType().equals(FormComponentFactory.confirmation_page_type))
				confirmation_page_id = page.getId();
			else if(page.getType().equals(FormComponentFactory.page_type_thx))
				thx_page_id = page.getId();
				
			i++;
		}
		announceRegisteredForLastPage();
	}
	protected void announceRegisteredForLastPage() {
		
		for (String registered_id : getRegisteredForLastPageIdPages())
			((IFormComponentPage)getComponent(registered_id)).announceLastPage(thx_page_id);
	}
	
	@Override
	public void rearrangeComponents() {
		
		List<String> contained_components_id_list = getContainedComponentsIdList();
		int components_amount = contained_components_id_list.size();
		Map<String, IFormComponent> contained_components = getContainedComponents();
		
		for (int i = components_amount-1; i >= 0; i--) {
			
			String component_id = contained_components_id_list.get(i);
			
			if(contained_components.containsKey(component_id)) {
				
				IFormComponentPage page = (IFormComponentPage)contained_components.get(component_id);
				
				if(i != components_amount-1) {
					
					page.setComponentAfterThisRerender(
						contained_components.get(
								contained_components_id_list.get(i+1)
						)
					);
				} else
					page.setComponentAfterThisRerender(null);
				
				page.setPageSiblings(
						i == 0 ? null : (IFormComponentPage)contained_components.get(getContainedComponentsIdList().get(i - 1)),
						(i+1) == components_amount ? null : (IFormComponentPage)contained_components.get(getContainedComponentsIdList().get(i + 1))
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
	protected void setProperties() {
		
		ComponentPropertiesDocument properties = (ComponentPropertiesDocument)getProperties();
		
		if(properties == null)
			return;
		
		properties.setPlainStepsVisualizationUsed(((XFormsManagerDocument)getXFormsManager()).getIsStepsVisualizationUsed());
	}
	
	public void save() throws Exception {
		document.persist();
	}
	public Page getConfirmationPage() {
	
		return confirmation_page_id == null ? null : (Page)getContainedComponent(confirmation_page_id);
	}
	public PageThankYou getThxPage() {
		
		return thx_page_id == null ? null : (PageThankYou)getContainedComponent(thx_page_id);
	}
	
	protected List<String> getRegisteredForLastPageIdPages() {
		
		if(registered_for_last_page_id_pages == null)
			registered_for_last_page_id_pages = new ArrayList<String>();
		
		return registered_for_last_page_id_pages;
	}
	
	public void registerForLastPage(String register_page_id) {
		
		if(!getContainedComponents().containsKey(register_page_id))
			throw new IllegalArgumentException("I don't contain provided page id: "+register_page_id);
		
		if(!getRegisteredForLastPageIdPages().contains(register_page_id))
			getRegisteredForLastPageIdPages().add(register_page_id);
	}
	
	public Page addConfirmationPage(String page_after_this_id) {

		if(getConfirmationPage() != null)
			throw new IllegalArgumentException("Confirmation page already exists in the form");
		
		Page page = (Page)super.addComponent(FormComponentFactory.confirmation_page_type, page_after_this_id);
		componentsOrderChanged();
		
		addToConfirmationPage();
		
		return page;
	}
	
	public Element getAutofillModelElement() {
		
		return ((XFormsManagerDocument)getXFormsManager()).getAutofillAction();
	}
	
	public Element getFormDataModelElement() {
		
		return ((XFormsManagerDocument)getXFormsManager()).getFormDataModelElement();
	}
	
	public void clear() {
		
		confirmation_page_id = null;
		thx_page_id = null;
		registered_for_last_page_id_pages = null;
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
			properties.setParentComponent(this);
			this.properties = properties;
		}
		
		return properties;
	}
	
	public Element getSectionsVisualizationInstanceElement() {
	
		return ((XFormsManagerDocument)getXFormsManager()).getSectionsVisualizationInstanceElement();
	}
	
	@Override
	public void update(ConstUpdateType what) {
		
		getXFormsManager().update(what);
		
		if(what.getUpdateType() == ConstUpdateType.steps_visualization_used) {
			rearrangeComponents();
		}
	}
}