package com.idega.documentmanager.business.form.manager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chiba.xml.dom.DOMUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.documentmanager.business.form.PropertiesSelect;
import com.idega.documentmanager.business.form.beans.ConstUpdateType;
import com.idega.documentmanager.business.form.beans.IComponentPropertiesParent;
import com.idega.documentmanager.business.form.beans.ILocalizedItemset;
import com.idega.documentmanager.business.form.beans.LocalizedItemsetBean;
import com.idega.documentmanager.business.form.beans.LocalizedStringBean;
import com.idega.documentmanager.business.form.beans.XFormsComponentDataBean;
import com.idega.documentmanager.business.form.beans.XFormsComponentSelectDataBean;
import com.idega.documentmanager.business.form.manager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 */
public class XFormsManagerSelect extends XFormsManager {

	private static Log logger = LogFactory.getLog(XFormsManagerSelect.class);
	
	public void loadXFormsComponentByType(String component_type) throws NullPointerException {
		
		cache_manager.checkForComponentType(component_type);
		
		XFormsComponentSelectDataBean xforms_component = (XFormsComponentSelectDataBean)cache_manager.getCachedXformsComponent(component_type); 

		if(xforms_component != null) {
			this.xforms_component = (XFormsComponentSelectDataBean)xforms_component.clone();
			return;
		}
		
		Document components_xforms = cache_manager.getComponentsXforms();
		Element xforms_element = FormManagerUtil.getElementByIdFromDocument(components_xforms, FormManagerUtil.body_tag, component_type);
		
		if(xforms_element == null) {
			String msg = "Component cannot be found in components xforms document.";
			logger.error(msg+
				" Should not happen. Take a look, why component is registered in components_types, but is not present in components xforms document.");
			throw new NullPointerException(msg);
		}
		
		synchronized (XFormsManagerSelect.class) {
			
			xforms_component = (XFormsComponentSelectDataBean)cache_manager.getCachedXformsComponent(component_type); 

			if(xforms_component != null) {
				this.xforms_component = (XFormsComponentSelectDataBean)xforms_component.clone();
				return;
			}
			
			loadXFormsComponent(components_xforms, xforms_element);
			cache_manager.cacheXformsComponent(component_type, (XFormsComponentSelectDataBean)this.xforms_component.clone());
		}
	}
	
	@Override
	protected XFormsComponentDataBean newXFormsComponentDataBeanInstance() {
		return new XFormsComponentSelectDataBean();
	}
	
	private static final String local_data_source = "_lds";
	private static final String external_data_source = "_eds";
	
	protected void loadXFormsComponent(Document components_xforms, Element xforms_element) {
		
		super.loadXFormsComponent(components_xforms, xforms_element);
		
		XFormsComponentSelectDataBean xforms_component = (XFormsComponentSelectDataBean)this.xforms_component;
		
		Element component_element = xforms_component.getElement();
		String component_element_id = component_element.getAttribute(FormManagerUtil.id_att);
		
		Element local_data_source_instance = FormManagerUtil.getElementByIdFromDocument(components_xforms, FormManagerUtil.head_tag, getLocalDataSourceInstanceIdentifier(component_element_id));
		Element external_data_source_instance = FormManagerUtil.getElementByIdFromDocument(components_xforms, FormManagerUtil.head_tag, getExternalDataSourceInstanceIdentifier(component_element_id));
		
		xforms_component.setLocalItemsetInstance(local_data_source_instance);
		xforms_component.setExternalItemsetInstance(external_data_source_instance);
	}
	private String getLocalDataSourceInstanceIdentifier(String element_id) {
	
		return element_id+local_data_source;
	}
	private String getExternalDataSourceInstanceIdentifier(String element_id) {
		
		return element_id+external_data_source;
	}
	
	@Override
	public void addComponentToDocument() {
		
		super.addComponentToDocument();
		
		Document xforms_doc = form_document.getXformsDocument();
		XFormsComponentSelectDataBean xforms_component = (XFormsComponentSelectDataBean)this.xforms_component;
		
		Element new_xforms_element = xforms_component.getLocalItemsetInstance();
		
		Element data_model_instance_element = FormManagerUtil.getElementByIdFromDocument(xforms_doc, FormManagerUtil.head_tag, FormManagerUtil.data_mod);
		
		if(new_xforms_element != null) {
			
			new_xforms_element = (Element)xforms_doc.importNode(new_xforms_element, true);
			new_xforms_element.setAttribute(FormManagerUtil.id_att, getLocalDataSourceInstanceIdentifier(component.getId()));
			data_model_instance_element.appendChild(new_xforms_element);
			xforms_component.setLocalItemsetInstance(new_xforms_element);
		}
		
		new_xforms_element = xforms_component.getExternalItemsetInstance();
		
		if(new_xforms_element != null) {
			
			new_xforms_element = (Element)xforms_doc.importNode(new_xforms_element, true);
			new_xforms_element.setAttribute(FormManagerUtil.id_att, getExternalDataSourceInstanceIdentifier(component.getId()));
			data_model_instance_element.appendChild(new_xforms_element);
			xforms_component.setExternalItemsetInstance(new_xforms_element);
		}
	}
	
	public Integer getDataSrcUsed() {
		
		Element component_element = xforms_component.getElement();
		
		Element itemset = DOMUtil.getChildElement(component_element, FormManagerUtil.itemset_tag);
		
		if(itemset == null)
			return null;
		
		String nodeset_att_value = itemset.getAttribute(FormManagerUtil.nodeset_att);
		
		if(nodeset_att_value == null)
			return null;
		
		String data_src_instance_id = nodeset_att_value.substring("instance('".length(), nodeset_att_value.indexOf("')"));
		
		if(data_src_instance_id.endsWith("_eds"))
			return PropertiesSelect.EXTERNAL_DATA_SRC;
		else if(data_src_instance_id.endsWith("_lds"))
			return PropertiesSelect.LOCAL_DATA_SRC;
		
		return null;
	}
	public LocalizedStringBean getEmptyElementLabel() {
		
		Element item = DOMUtil.getChildElement(xforms_component.getElement(), FormManagerUtil.item_tag);
		
		if(item == null)
			return null;
		
		Element label = DOMUtil.getChildElement(item, FormManagerUtil.label_tag);
		
		String ref = label.getAttribute("ref");
		
		if(!FormManagerUtil.isRefFormCorrect(ref))
			return new LocalizedStringBean();
		
		String key = FormManagerUtil.getKeyFromRef(ref);
		
		return FormManagerUtil.getLocalizedStrings(key, form_document.getXformsDocument());
	}
	public String getExternalDataSrc() {
		
		Element external_instance = ((XFormsComponentSelectDataBean)xforms_component).getExternalItemsetInstance();
		
		if(external_instance == null)
			return null;
		
		return external_instance.getAttribute(FormManagerUtil.src_att);
	}
	public ILocalizedItemset getItemset() {

		Element local_instance = ((XFormsComponentSelectDataBean)xforms_component).getLocalItemsetInstance();
		
		if(local_instance == null)
			return null;
		
		LocalizedItemsetBean itemset_bean = new LocalizedItemsetBean();
		itemset_bean.setLocalDataSrcElement(local_instance);
		itemset_bean.setComponentsXFormsDocument(cache_manager.getComponentsXforms());
		itemset_bean.setParentComponent((IComponentPropertiesParent)component);
		
		return itemset_bean;
	}
	
	@Override
	public void update(ConstUpdateType what) {
		super.update(what);
		
		int update = what.getUpdateType();
		
		switch (update) {
		case ConstUpdateType.data_src_used:
			updateDataSrcUsed();
			break;
			
		case ConstUpdateType.empty_element_label:
			updateEmptyElementLabel();
			break;
			
		case ConstUpdateType.external_data_src:
			updateExternalDataSrc();
			break;
			
		default:
			break;
		}
	}
	
	protected void updateDataSrcUsed() {
		
		PropertiesSelect properties = (PropertiesSelect)component.getProperties();
		Integer data_src_used = properties.getDataSrcUsed();
		
		Element component_element = xforms_component.getElement();
		
		if(data_src_used == null) {
			
			Element itemset = DOMUtil.getChildElement(component_element, FormManagerUtil.itemset_tag);
			
			if(itemset != null)
				component_element.removeChild(itemset);
			
		} else {
			
			String itemset_instance_str = null; 
			
			if(data_src_used == PropertiesSelect.EXTERNAL_DATA_SRC) {
				
				itemset_instance_str = constructItemsetInstance(PropertiesSelect.EXTERNAL_DATA_SRC);
				
			} else if(data_src_used == PropertiesSelect.LOCAL_DATA_SRC) {
				itemset_instance_str = constructItemsetInstance(PropertiesSelect.LOCAL_DATA_SRC);
			}
			
			if(itemset_instance_str == null)
				return;
			
			Element itemset = DOMUtil.getChildElement(component_element, FormManagerUtil.itemset_tag);
			
			if(itemset == null) {
				
				itemset = FormManagerUtil.getItemElementById(cache_manager.getComponentsXforms(), "itemset");
				itemset = (Element)component_element.getOwnerDocument().importNode(itemset, true);
				component_element.appendChild(itemset);
			}
			
			itemset.setAttribute(FormManagerUtil.nodeset_att, itemset_instance_str);
		}
	}
	
	private String constructItemsetInstance(Integer data_source) {
	
		StringBuffer buf = new StringBuffer();
		
		buf.append("instance('")
		.append(component.getId());
		
		if(data_source == PropertiesSelect.LOCAL_DATA_SRC)
			buf.append("_lds");
		else
			buf.append("_eds");
		
		buf.append("')/localizedEntries[@lang=instance('localized_strings')/current_language]/item");
		
		return buf.toString();
	}

	protected void updateEmptyElementLabel() {
		
		LocalizedStringBean loc_str = ((PropertiesSelect)component.getProperties()).getEmptyElementLabel();
		
		Element item = DOMUtil.getChildElement(xforms_component.getElement(), FormManagerUtil.item_tag);
		
		if(item == null)
			return;
		
		Element label = DOMUtil.getChildElement(item, FormManagerUtil.label_tag);
		
		FormManagerUtil.putLocalizedText(null, null, 
				label,
				form_document.getXformsDocument(),
				loc_str
		);
		
	}
	protected void updateExternalDataSrc() {
		
		Element external_instance = ((XFormsComponentSelectDataBean)xforms_component).getExternalItemsetInstance();
		
		if(external_instance == null)
			return;
		
		String external_data_src = ((PropertiesSelect)component.getProperties()).getExternalDataSrc();
		
		if(external_data_src == null)
			return;
		
		external_instance.setAttribute(FormManagerUtil.src_att, external_data_src);
	}
	
	public void removeSelectComponentSourcesFromXFormsDocument() {
		
		XFormsComponentSelectDataBean xforms_component = (XFormsComponentSelectDataBean)this.xforms_component;
		Element data_src_element = xforms_component.getExternalItemsetInstance();
		
		if(data_src_element != null)
			data_src_element.getParentNode().removeChild(data_src_element);
		
		data_src_element = xforms_component.getLocalItemsetInstance();
		
		if(data_src_element != null)
			data_src_element.getParentNode().removeChild(data_src_element);
	}
}