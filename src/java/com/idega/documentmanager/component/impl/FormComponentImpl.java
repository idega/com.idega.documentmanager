package com.idega.documentmanager.component.impl;

import java.util.List;
import java.util.Locale;

import org.w3c.dom.Element;

import com.idega.documentmanager.business.component.Component;
import com.idega.documentmanager.business.component.properties.PropertiesComponent;
import com.idega.documentmanager.component.FormComponent;
import com.idega.documentmanager.component.FormComponentContainer;
import com.idega.documentmanager.component.FormComponentPage;
import com.idega.documentmanager.component.FormDocument;
import com.idega.documentmanager.component.beans.LocalizedStringBean;
import com.idega.documentmanager.component.beans.ComponentDataBean;
import com.idega.documentmanager.component.properties.impl.ComponentProperties;
import com.idega.documentmanager.component.properties.impl.ConstUpdateType;
import com.idega.documentmanager.context.DMContext;
import com.idega.documentmanager.manager.HtmlManager;
import com.idega.documentmanager.manager.XFormsManager;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.14 $
 *
 * Last modified: $Date: 2008/08/12 06:07:39 $ by $Author: arunas $
 */
public class FormComponentImpl implements FormComponent, Component {
	
	private  String componentId;
	
	protected FormComponent componentAfterMe;
	protected String type;
	
	protected FormComponentContainer parent;
	
	protected PropertiesComponent properties;
	protected boolean created = false;
	protected boolean load = false;
	
	private FormDocument formDocument;
	
	private ComponentDataBean xformsComponentDataBean;
	
	private DMContext context;
	
	public DMContext getContext() {
		return context;
	}

	public void setContext(DMContext context) {
		this.context = context;
	}
	
	public void render() {
		
		if(load || !created) {
			
			XFormsManager xformsManager = getXFormsManager();
			
			if(load) {
				
				xformsManager.loadXFormsComponentFromDocument(this);
				
			} else {
				xformsManager.loadXFormsComponentByTypeFromComponentsXForm(this, type);
				xformsManager.addComponentToDocument(this);
			}
			
			setProperties();
			
			if(!load)
				changeBindNames();
			
			formDocument.setFormDocumentModified(true);
			tellAboutMe();
			
			if(FormComponentFactory.getInstance().isNormalFormElement(this)) {

				if(load) {
					
					getXFormsManager().loadConfirmationElement(this, null);
					
				} else {
					
					FormComponentPage confirmation_page = (FormComponentPage)formDocument.getConfirmationPage();
					
					if(confirmation_page != null) {
						getXFormsManager().loadConfirmationElement(this, confirmation_page);
					}
				}
			}
			
			created = true;
			load = false;
		}
	}
	
	public void addToConfirmationPage() {
		
		if(FormComponentFactory.getInstance().isNormalFormElement(this)) {
			
			FormComponentPage confirmation_page = (FormComponentPage)formDocument.getConfirmationPage();
			
			if(confirmation_page != null) {
				getXFormsManager().loadConfirmationElement(this, confirmation_page);
			}
		}
	}
	
	protected void setProperties() {
		
		ComponentProperties properties = (ComponentProperties)getProperties();
		
		if(properties == null)
			return;
		
		properties.setPlainLabel(getXFormsManager().getLocalizedStrings(this));
		properties.setPlainRequired(getXFormsManager().isRequired(this));
		properties.setPlainErrorMsg(getXFormsManager().getErrorLabelLocalizedStrings(this));
		properties.setPlainAutofillKey(getXFormsManager().getAutofillKey(this));
		properties.setPlainHelpText(getXFormsManager().getHelpText(this));
		properties.setPlainVariable(getXFormsManager().getVariable(this));
		properties.setPlainReadonly(getXFormsManager().isReadonly(this));
		properties.setPlainValidationText(getXFormsManager().getValidationText(this));

	}
	
	protected void changeBindNames() {
		
		LocalizedStringBean localized_label = getProperties().getLabel();
		String default_locale_label = localized_label.getString(formDocument.getDefaultLocale());
		
		getXFormsManager().changeBindName(this,
				new StringBuffer(default_locale_label)
				.append('_')
				.append(getId())
				.toString()
		);
	}
	
	protected void tellAboutMe() {

		String componentId = getId();
		parent.tellComponentId(componentId);
		List<String> id_list = parent.getContainedComponentsIdList();

		for (int i = 0; i < id_list.size(); i++) {
			
			if(id_list.get(i).equals(componentId) && i != 0) {
				
				((FormComponent)parent.getComponent(id_list.get(i-1))).setComponentAfterThis(this);
				break;
			}
		}
	}
	
	public void setComponentAfterThis(FormComponent component) {
		
		componentAfterMe = component;
	}
	
	public void setComponentAfterThisRerender(FormComponent component) {
		
		XFormsManager xforms_manager = getXFormsManager();
		
		FormComponent previous_component_after_me = componentAfterMe;
		componentAfterMe = component;
		
		if(component != null && previous_component_after_me != null && !previous_component_after_me.getId().equals(component.getId())) {

			xforms_manager.moveComponent(this, component.getId());
			
		} else if(previous_component_after_me == null && component != null) {
			
			xforms_manager.moveComponent(this, component.getId());
			
		} else if(component == null && previous_component_after_me != null) {
			
			xforms_manager.moveComponent(this, null);
		}
	}
	
	public String getId() {
		
		return componentId;
	}
	
	public void setId(String id) {
		
		if(id != null)
			componentId = id;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public void setLoad(boolean load) {
		this.load = load;
	}
	
	public Element getHtmlRepresentation(Locale locale) throws Exception {
		
		return getHtmlManager().getHtmlRepresentation(this, locale);
	}
	
	public PropertiesComponent getProperties() {
		
		if(properties == null) {
			ComponentProperties properties = new ComponentProperties();
			properties.setComponent(this);
			this.properties = properties;
		}
		
		return properties;
	}

	public XFormsManager getXFormsManager() {
		
		return getContext().getXformsManagerFactory().getXformsManager();
	}
	
	protected HtmlManager getHtmlManager() {
		
		return getContext().getHtmlManagerFactory().getHtmlManager();
	}
	
	public String getType() {
		return type;
	}
	
	public void remove() {
		
		getXFormsManager().removeComponentFromXFormsDocument(this);
		formDocument.setFormDocumentModified(true);
		parent.unregisterComponent(getId());
	}
	
	public void setParent(FormComponentContainer parent) {
		this.parent = parent;
	}
	
	public FormComponentContainer getParent() {
		return parent;
	}
	
	@Override
	public String toString() {
		
		return "\nComponent id: "+getId()+" component class: "+getClass();
	}
	public FormComponent getComponentAfterThis() {
		return componentAfterMe;
	}
	public void setFormDocument(FormDocument form_document) {
		this.formDocument = form_document;
	}
	
	public FormDocument getFormDocument() {
		return formDocument;
	}
	
	public void update(ConstUpdateType what) {
		
		getXFormsManager().update(this, what);
		
		switch (what) {
		case LABEL:
			getHtmlManager().clearHtmlComponents(this);
			formDocument.setFormDocumentModified(true);
			changeBindNames();
			break;
			
		case ERROR_MSG:
			getHtmlManager().clearHtmlComponents(this);
			formDocument.setFormDocumentModified(true);
			break;
			
		case HELP_TEXT:
			getHtmlManager().clearHtmlComponents(this);
			formDocument.setFormDocumentModified(true);
			break;
			
		case CONSTRAINT_REQUIRED:
			formDocument.setFormDocumentModified(true);
			break;
		case VALIDATION:
		    	getHtmlManager().clearHtmlComponents(this);
			formDocument.setFormDocumentModified(true);
			break;
		
		case P3P_TYPE:
			break;
			
		case AUTOFILL_KEY:
			break;
			
		case VARIABLE_NAME:
			break;

		default:
			break;
		}
	}
	
	public void clear() {
		componentAfterMe = null;
		setId(null);
		type = null;
		parent = null;
		properties = null;
		created = false;
		load = false;
		formDocument = null;
	}
	
	public ComponentDataBean getXformsComponentDataBean() {
		return xformsComponentDataBean;
	}

	public void setXformsComponentDataBean(
			ComponentDataBean xformsComponentDataBean) {
		this.xformsComponentDataBean = xformsComponentDataBean;
	}
	
	public void setReadonly(boolean readonly) {
		getXFormsManager().setReadonly(this, readonly);
	}
	
	public boolean isReadonly() {
		return getXFormsManager().isReadonly(this);
	}
}