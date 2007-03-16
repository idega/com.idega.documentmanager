package com.idega.documentmanager.business.form.beans;

import java.util.List;
import java.util.Locale;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.documentmanager.business.form.Component;
import com.idega.documentmanager.business.form.PropertiesComponent;
import com.idega.documentmanager.business.form.manager.CacheManager;
import com.idega.documentmanager.business.form.manager.HtmlManager;
import com.idega.documentmanager.business.form.manager.IXFormsManager;
import com.idega.documentmanager.business.form.manager.XFormsManager;
import com.idega.documentmanager.business.form.manager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 */
public class FormComponent implements IFormComponent, IComponentPropertiesParent, Component {
	
	protected IFormComponent component_after_me;
	protected String component_id;
	protected String type;
	
	protected IFormComponentContainer parent;
	
	protected PropertiesComponent properties;
	protected boolean created = false;
	protected boolean load = false;
	
	protected IXFormsManager xforms_manager;
	protected HtmlManager html_manager;
	protected IFormComponentDocument form_document;
	
	public void render() {
		
		Document xforms_doc = form_document.getXformsDocument();
		
		if(xforms_doc == null)
			throw new NullPointerException("Form Xforms document was not provided");
		
		if(load || !created) {
			
			IXFormsManager xforms_manager = getXFormsManager();
			
			if(load) {
				
				xforms_manager.loadXFormsComponentFromDocument(component_id);
				
			} else {
				xforms_manager.loadXFormsComponentByType(type);
				xforms_manager.addComponentToDocument();
			}
			
			setProperties();
			
			if(!load)
				changeBindNames();
			
			form_document.setFormDocumentModified(true);
			tellAboutMe();
			
			if(FormComponentFactory.getInstance().isNormalFormElement(this)) {

				if(load) {
					
					getXFormsManager().loadConfirmationElement(null);
					
				} else {
					
					IFormComponentPage confirmation_page = (IFormComponentPage)form_document.getConfirmationPage();
					
					if(confirmation_page != null) {
						getXFormsManager().loadConfirmationElement(confirmation_page);
					}
				}
			}
			
			created = true;
			load = false;
		}
	}
	
	public void addToConfirmationPage() {
		
		if(FormComponentFactory.getInstance().isNormalFormElement(this)) {
			
			IFormComponentPage confirmation_page = (IFormComponentPage)form_document.getConfirmationPage();
			
			if(confirmation_page != null) {
				getXFormsManager().loadConfirmationElement(confirmation_page);
			}
		}
	}
	
	protected void setProperties() {
		
		ComponentProperties properties = (ComponentProperties)getProperties();
		
		if(properties == null)
			return;
		
		properties.setPlainLabel(getXFormsManager().getLocalizedStrings());
		properties.setPlainRequired(false);
		properties.setPlainErrorMsg(getXFormsManager().getErrorLabelLocalizedStrings());
		properties.setPlainAutofillKey(getXFormsManager().getAutofillKey());
		properties.setPlainHelpText(getXFormsManager().getHelpText());
	}
	
	protected void changeBindNames() {
		
		LocalizedStringBean localized_label = getProperties().getLabel();
		String default_locale_label = localized_label.getString(form_document.getDefaultLocale());
		
		getXFormsManager().changeBindName(
				new StringBuffer(getId())
				.append(FormManagerUtil.bind_att)
				.append('_')
				.append(default_locale_label)
				.toString()
		);
	}
	
	protected void tellAboutMe() {

		parent.tellComponentId(getId());
		List<String> id_list = parent.getContainedComponentsIdList();

		for (int i = 0; i < id_list.size(); i++) {
			
			if(id_list.get(i).equals(component_id) && i != 0) {
				
				((IFormComponent)parent.getComponent(id_list.get(i-1))).setComponentAfterThis(this);
				break;
			}
		}
	}
	
	public void setComponentAfterThis(IFormComponent component) {
		
		component_after_me = component;
	}
	
	public void setComponentAfterThisRerender(IFormComponent component) {
		
		IXFormsManager xforms_manager = getXFormsManager();
		
		IFormComponent previous_component_after_me = component_after_me;
		component_after_me = component;
		
		if(component != null && previous_component_after_me != null && !previous_component_after_me.getId().equals(component.getId())) {

			xforms_manager.moveComponent(component.getId());
			
		} else if(previous_component_after_me == null && component != null) {
			
			xforms_manager.moveComponent(component.getId());
			
		} else if(component == null && previous_component_after_me != null) {
			
			xforms_manager.moveComponent(null);
		}
	}
	
	public String getId() {
		
		return component_id;
	}
	
	public void setId(String id) {
		
		if(id != null)
			component_id = id;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void setLoad(boolean load) {
		this.load = load;
	}
	
	public Element getHtmlRepresentation(Locale locale) throws Exception {
		
		return getHtmlManager().getHtmlRepresentation(locale);
	}
	
	public PropertiesComponent getProperties() {
		
		if(properties == null) {
			ComponentProperties properties = new ComponentProperties();
			properties.setParentComponent(this);
			this.properties = properties;
		}
		
		return properties;
	}

	public IXFormsManager getXFormsManager() {
		
		if(xforms_manager == null) {
			
			xforms_manager = new XFormsManager();
			xforms_manager.setCacheManager(CacheManager.getInstance());
			xforms_manager.setComponentParent(parent);
			xforms_manager.setFormComponent(this);
			xforms_manager.setFormDocument(form_document);
		}
		
		return xforms_manager;
	}
	
	protected HtmlManager getHtmlManager() {
		
		if(html_manager == null) {
			
			html_manager = new HtmlManager();
			html_manager.setCacheManager(CacheManager.getInstance());
			html_manager.setComponentParent(parent);
			html_manager.setFormComponent(this);
			html_manager.setFormDocument(form_document);
		}
		
		return html_manager;
	}
	
	public String getType() {
		return type;
	}
	
	public void remove() {
		
		getXFormsManager().removeComponentFromXFormsDocument();
		form_document.setFormDocumentModified(true);
		parent.unregisterComponent(getId());
	}
	
	public void setParent(IFormComponentContainer parent) {
		this.parent = parent;
	}
	
	@Override
	public String toString() {
		
		return "\nComponent id: "+getId()+" component class: "+getClass();
	}
	public IXFormsManager getComponentXFormsManager() {
		return getXFormsManager();
	}
	public IFormComponent getComponentAfterThis() {
		return component_after_me;
	}
	public void setFormDocument(IFormComponentDocument form_document) {
		this.form_document = form_document;
	}
	
	public void update(ConstUpdateType what) {
		
		getXFormsManager().update(what);
		
		int update = what.getUpdateType();
		
		switch (update) {
		case ConstUpdateType.label:
			getHtmlManager().clearHtmlComponents();
			form_document.setFormDocumentModified(true);
			changeBindNames();
			break;
			
		case ConstUpdateType.error_msg:
			getHtmlManager().clearHtmlComponents();
			form_document.setFormDocumentModified(true);
			break;
			
		case ConstUpdateType.help_text:
			getHtmlManager().clearHtmlComponents();
			form_document.setFormDocumentModified(true);
			break;
			
		case ConstUpdateType.constraint_required:
			form_document.setFormDocumentModified(true);
			break;
			
		case ConstUpdateType.p3p_type:
			break;
			
		case ConstUpdateType.autofill_key:
			break;

		default:
			break;
		}
	}
	
	public void clear() {
		component_after_me = null;
		component_id = null;
		type = null;
		parent = null;
		properties = null;
		created = false;
		load = false;
		form_document = null;
	}
	
	public Element getDefaultHtmlRepresentationByType(String component_type) {
		
		return getHtmlManager().getDefaultHtmlRepresentationByType(component_type);
	}
}