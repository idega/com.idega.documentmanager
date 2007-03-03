package com.idega.documentmanager.business.form.beans;

import com.idega.documentmanager.business.form.ComponentSelect;
import com.idega.documentmanager.business.form.PropertiesSelect;
import com.idega.documentmanager.business.form.manager.CacheManager;
import com.idega.documentmanager.business.form.manager.IXFormsManager;
import com.idega.documentmanager.business.form.manager.XFormsManagerSelect;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 */
public class FormComponentSelect extends FormComponent implements ComponentSelect {
	
	public IXFormsManager getXFormsManager() {
		
		if(xforms_manager == null) {
			
			xforms_manager = new XFormsManagerSelect();
			xforms_manager.setCacheManager(CacheManager.getInstance());
			xforms_manager.setComponentParent(parent);
			xforms_manager.setFormComponent(this);
			xforms_manager.setFormDocument(form_document);
		}
		
		return xforms_manager;
	}
	
	public PropertiesSelect getProperties() {
		
		if(properties == null) {
			ComponentPropertiesSelect properties = new ComponentPropertiesSelect();
			properties.setParentComponent(this);
			this.properties = properties;
		}
		
		return (PropertiesSelect)properties;
	}
	
	protected void setProperties() {
		
		super.setProperties();
		
		ComponentPropertiesSelect properties = (ComponentPropertiesSelect)getProperties();
		XFormsManagerSelect xforms_manager = (XFormsManagerSelect)getXFormsManager();
		
		properties.setDataSrcUsedPlain(xforms_manager.getDataSrcUsed());
		properties.setEmptyElementLabelPlain(xforms_manager.getEmptyElementLabel());
		properties.setExternalDataSrcPlain(xforms_manager.getExternalDataSrc());
		properties.setItemsetPlain(xforms_manager.getItemset());
	}
	
	@Override
	public void update(ConstUpdateType what) {
		super.update(what);
		
		int update = what.getUpdateType();
		
		switch (update) {
		case ConstUpdateType.data_src_used:
			getHtmlManager().clearHtmlComponents();
			form_document.setFormDocumentModified(true);
			break;
			
		case ConstUpdateType.itemset:
			getHtmlManager().clearHtmlComponents();
			form_document.setFormDocumentModified(true);
			break;
			
		case ConstUpdateType.empty_element_label:
			getHtmlManager().clearHtmlComponents();
			form_document.setFormDocumentModified(true);
			break;
			
		case ConstUpdateType.external_data_src:
			getHtmlManager().clearHtmlComponents();
			form_document.setFormDocumentModified(true);
			break;

		default:
			break;
		}
	}
	
	public void remove() {
		
		((XFormsManagerSelect)getXFormsManager()).removeSelectComponentSourcesFromXFormsDocument();
		super.remove();
	}
}