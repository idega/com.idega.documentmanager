package com.idega.documentmanager.manager.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.core.cache.IWCacheManager2;
import com.idega.documentmanager.component.beans.ComponentDataBean;
import com.idega.documentmanager.component.datatypes.ComponentType;
import com.idega.documentmanager.component.impl.FormComponentFactory;
import com.idega.idegaweb.IWMainApplication;
import com.idega.repository.data.Singleton;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.3 $
 *
 * Last modified: $Date: 2007/11/10 13:52:48 $ by $Author: alexis $
 */
public class CacheManager implements Singleton {
	
	private Document form_xforms_template;
	private Document components_xforms;
	private Document components_xsd;
	private Document components_xml;
	private List<String> all_components_types;
	private List<String> components_types_to_list;
	private Map<String, ComponentDataBean> cachedXformsComponents;
	private Map<String, List<String>> categorized_types;
	private Map<String, List<ComponentType>> typesByDatatype;
	
	private Map<String, Element> cachedDefaultComponentLocalizations;
	
	private static CacheManager me;
	
	public static CacheManager getInstance() {
		
		if (me == null) {
			
			synchronized (CacheManager.class) {
				if (me == null) {
					me = new CacheManager();
				}
			}
		}

		return me;
	}
	
	protected CacheManager() { }

	public Document getFormXformsTemplateCopy() {
		
		if(form_xforms_template == null)
			throw new NullPointerException("Form xforms template not initialized");
		
		return (Document)form_xforms_template.cloneNode(true);
	}

	public void setFormXformsTemplate(Document form_xforms_template) {
		
		if(form_xforms_template != null)
			this.form_xforms_template = form_xforms_template;
	}

	public List<String> getComponentsTypes() {
		return components_types_to_list;
	}
	
	public void setAllComponentsTypes(List<String> components_types) {
		this.all_components_types = components_types;
		setComponentsTypes(components_types);
	}
	
	public void setCategorizedComponentTypes(Map<String, List<String>> categorized) {
		
		categorized_types = categorized;
	}
	
	public List<ComponentType> getComponentTypesByDatatype(String datatype) {
		return typesByDatatype == null || datatype == null ? null :
			typesByDatatype.get(datatype);
	}
	
	public List<String> getComponentTypesByCategory(String category) {
		return categorized_types == null || category == null ? null :
			categorized_types.get(category);
	}
	
	protected void setComponentsTypes(List<String> components_types) {
		components_types_to_list = new ArrayList<String>(); 
		components_types_to_list.addAll(components_types);
		
		FormComponentFactory.getInstance().filterNonDisplayComponents(components_types_to_list);
	}

	public Document getComponentsXforms() {
		return components_xforms;
	}

	public void setComponentsXforms(Document components_xforms) {
		this.components_xforms = components_xforms;
	}

	public Document getComponentsXml() {
		return components_xml;
	}

	public void setComponentsXml(Document components_xml) {
		this.components_xml = components_xml;
	}

	public Document getComponentsXsd() {
		return components_xsd;
	}

	public void setComponentsXsd(Document components_xsd) {
		this.components_xsd = components_xsd;
	}
	
	public void checkForComponentType(String component_type) throws NullPointerException {
		
		if(all_components_types == null || component_type == null || !all_components_types.contains(component_type)) {
			String msg;
			
			if(component_type == null)
				msg = "Component type is not provided (provided null)";
			
			else if(all_components_types == null)
				msg = "Components types are not initialized";
				
			else
				msg = "Component type given is not known. Given: "+component_type;
			
			throw new NullPointerException(msg);
		}
	}
	
	public List<String> getAvailableFormComponentsTypesList() {
		
		return all_components_types;
	}
	
	public void initAppContext(FacesContext ctx) {
		
		if(ctx == null)
			throw new NullPointerException("FacesContext not set");
		
		IWMainApplication iwma = IWMainApplication.getIWMainApplication(ctx);
		
		@SuppressWarnings("unchecked")
		Map<String, ComponentDataBean> cachedXformsComponents = IWCacheManager2.getInstance(iwma).getCache("cached_xforms_components");
		this.cachedXformsComponents = cachedXformsComponents;
		@SuppressWarnings("unchecked")
		Map<String, Element> cachedDefaultComponentLocalizations = IWCacheManager2.getInstance(iwma).getCache("cached_default_components_localizations");
		this.cachedDefaultComponentLocalizations = cachedDefaultComponentLocalizations;
	}
	
	public void cacheXformsComponent(String key, ComponentDataBean xbean) {
		
		cachedXformsComponents.put(key, xbean);			
	}
	
	public ComponentDataBean getCachedXformsComponent(String key) {
		
		return cachedXformsComponents == null ? null : cachedXformsComponents.get(key);
	}
	
	public Element getCachedDefaultComponentLocalization(String component_type) {
		
		return getCachedDefaultComponentLocalizations().get(component_type);
	}
	
	public void cacheDefaultComponentLocalization(String component_type, Element element) {
		
		getCachedDefaultComponentLocalizations().put(component_type, element);
	}
	
	protected Map<String, Element> getCachedDefaultComponentLocalizations() {
		
		return cachedDefaultComponentLocalizations;
	}

	public Map<String, List<ComponentType>> getTypesByDatatype() {
		return typesByDatatype;
	}

	public void setTypesByDatatype(Map<String, List<ComponentType>> typesByDatatype) {
		this.typesByDatatype = typesByDatatype;
	}
}