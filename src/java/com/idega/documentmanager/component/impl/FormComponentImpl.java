package com.idega.documentmanager.component.impl;

import java.util.List;
import java.util.Locale;

import org.w3c.dom.Document;
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
import com.idega.documentmanager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2007/10/05 12:27:16 $ by $Author: civilis $
 */
public class FormComponentImpl implements FormComponent, Component {
	
	protected FormComponent componentAfterMe;
	private  String componentId;
	protected String type;
	
	protected FormComponentContainer parent;
	
	protected PropertiesComponent properties;
	protected boolean created = false;
	protected boolean load = false;
	
	protected XFormsManager xforms_manager;
	protected HtmlManager html_manager;
	private FormDocument formDocument;
	
	private ComponentDataBean xformsComponentDataBean;
	
	private DMContext context;
	
	public DMContext getContext() {
		context.setComponent(this);
		return context;
	}

	public void setContext(DMContext context) {
		this.context = context;
	}
	
	public void render() {
		
		Document xforms_doc = formDocument.getXformsDocument();
		
		if(xforms_doc == null)
			throw new NullPointerException("Form Xforms document was not provided");
		
		if(load || !created) {
			
			XFormsManager xforms_manager = getXFormsManager();
			
			if(load) {
				
				xforms_manager.loadXFormsComponentFromDocument(getContext(), getId());
				
			} else {
				xforms_manager.loadXFormsComponentByType(getContext(), type);
				xforms_manager.addComponentToDocument(getContext());
			}
			setProperties();
			
			if(!load)
				changeBindNames();
			
			formDocument.setFormDocumentModified(true);
			tellAboutMe();
			
			if(FormComponentFactory.getInstance().isNormalFormElement(this)) {

				if(load) {
					
					getXFormsManager().loadConfirmationElement(getContext(), null);
					
				} else {
					
					FormComponentPage confirmation_page = (FormComponentPage)formDocument.getConfirmationPage();
					
					if(confirmation_page != null) {
						getXFormsManager().loadConfirmationElement(getContext(), confirmation_page);
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
				getXFormsManager().loadConfirmationElement(getContext(), confirmation_page);
			}
		}
	}
	
	protected void setProperties() {
		
		ComponentProperties properties = (ComponentProperties)getProperties();
		
		if(properties == null)
			return;
		
		properties.setPlainLabel(getXFormsManager().getLocalizedStrings(getContext()));
		properties.setPlainRequired(getXFormsManager().getIsRequired(getContext()));
		properties.setPlainErrorMsg(getXFormsManager().getErrorLabelLocalizedStrings(getContext()));
		properties.setPlainAutofillKey(getXFormsManager().getAutofillKey(getContext()));
		properties.setPlainHelpText(getXFormsManager().getHelpText(getContext()));
	}
	
	protected void changeBindNames() {
		
		LocalizedStringBean localized_label = getProperties().getLabel();
		String default_locale_label = localized_label.getString(formDocument.getDefaultLocale());
		
		getXFormsManager().changeBindName(getContext(),
				new StringBuffer(getId())
				.append(FormManagerUtil.bind_att)
				.append('_')
				.append(default_locale_label)
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

			xforms_manager.moveComponent(getContext(), component.getId());
			
		} else if(previous_component_after_me == null && component != null) {
			
			xforms_manager.moveComponent(getContext(), component.getId());
			
		} else if(component == null && previous_component_after_me != null) {
			
			xforms_manager.moveComponent(getContext(), null);
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
		
		return getHtmlManager().getHtmlRepresentation(getContext(), locale);
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
		
		getXFormsManager().removeComponentFromXFormsDocument(getContext());
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
		
		getXFormsManager().update(getContext(), what);
		
		int update = what.getUpdateType();
		
		switch (update) {
		case ConstUpdateType.label:
			getHtmlManager().clearHtmlComponents(getContext());
			formDocument.setFormDocumentModified(true);
			changeBindNames();
			break;
			
		case ConstUpdateType.error_msg:
			getHtmlManager().clearHtmlComponents(getContext());
			formDocument.setFormDocumentModified(true);
			break;
			
		case ConstUpdateType.help_text:
			getHtmlManager().clearHtmlComponents(getContext());
			formDocument.setFormDocumentModified(true);
			break;
			
		case ConstUpdateType.constraint_required:
			formDocument.setFormDocumentModified(true);
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
		componentAfterMe = null;
		setId(null);
		type = null;
		parent = null;
		properties = null;
		created = false;
		load = false;
		formDocument = null;
		xforms_manager = null;
	}
	
	public Element getDefaultHtmlRepresentationByType(String component_type) {
		
		return getHtmlManager().getDefaultHtmlRepresentationByType(component_type);
	}

	public ComponentDataBean getXformsComponentDataBean() {
		return xformsComponentDataBean;
	}

	public void setXformsComponentDataBean(
			ComponentDataBean xformsComponentDataBean) {
		this.xformsComponentDataBean = xformsComponentDataBean;
	}
}