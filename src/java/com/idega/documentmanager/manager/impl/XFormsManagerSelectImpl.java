package com.idega.documentmanager.manager.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chiba.xml.dom.DOMUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.documentmanager.business.component.properties.PropertiesSelect;
import com.idega.documentmanager.component.FormComponent;
import com.idega.documentmanager.component.beans.LocalizedItemsetBean;
import com.idega.documentmanager.component.beans.XFormsComponentDataBean;
import com.idega.documentmanager.component.beans.XFormsComponentSelectDataBean;
import com.idega.documentmanager.component.properties.impl.ConstUpdateType;
import com.idega.documentmanager.context.DMContext;
import com.idega.documentmanager.manager.XFormsManagerSelect;
import com.idega.documentmanager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2007/10/05 11:42:31 $ by $Author: civilis $
 */
public class XFormsManagerSelectImpl extends XFormsManagerImpl implements XFormsManagerSelect {

	private static Log logger = LogFactory.getLog(XFormsManagerSelectImpl.class);
	
	public void loadXFormsComponentByType(DMContext context, String componentType) throws NullPointerException {
		
		FormComponent component = context.getComponent();
		CacheManager cacheManager = component.getContext().getCacheManager();
		
		cacheManager.checkForComponentType(componentType);
		
		XFormsComponentSelectDataBean xformsComponentDataBean = (XFormsComponentSelectDataBean)cacheManager.getCachedXformsComponent(componentType); 

		if(xformsComponentDataBean != null) {
			xformsComponentDataBean = (XFormsComponentSelectDataBean)xformsComponentDataBean.clone();
			component.setXformsComponentDataBean(xformsComponentDataBean);
			return;
		}
		
		Document componentsXFormsXml = cacheManager.getComponentsXforms();
		Element componentXFormsElement = FormManagerUtil.getElementByIdFromDocument(componentsXFormsXml, FormManagerUtil.body_tag, componentType);
		
		if(componentXFormsElement == null) {
			String msg = "Component cannot be found in components xforms document.";
			logger.error(msg+
				" Should not happen. Take a look, why component is registered in components_types, but is not present in components xforms document.");
			throw new NullPointerException(msg);
		}
		
		synchronized (XFormsManagerSelectImpl.class) {
			
			xformsComponentDataBean = (XFormsComponentSelectDataBean)cacheManager.getCachedXformsComponent(componentType); 

			if(xformsComponentDataBean != null) {
				xformsComponentDataBean = (XFormsComponentSelectDataBean)xformsComponentDataBean.clone();
				component.setXformsComponentDataBean(xformsComponentDataBean);
				return;
			}
			
			loadXFormsComponent(component.getContext(), componentsXFormsXml, componentXFormsElement);
			cacheManager.cacheXformsComponent(componentType, (XFormsComponentSelectDataBean)component.getXformsComponentDataBean().clone());
		}
	}
	
	@Override
	protected XFormsComponentDataBean newXFormsComponentDataBeanInstance() {
		return new XFormsComponentSelectDataBean();
	}
	
	private static final String local_data_source = "_lds";
	private static final String external_data_source = "_eds";
	
	protected void loadXFormsComponent(DMContext context, Document components_xforms, Element xforms_element) {
		
		FormComponent component = context.getComponent();
		
		super.loadXFormsComponent(component.getContext(), components_xforms, xforms_element);
		
		XFormsComponentSelectDataBean xformsComponentDataBean = (XFormsComponentSelectDataBean)component.getXformsComponentDataBean();
		
		Element component_element = xformsComponentDataBean.getElement();
		String component_element_id = component_element.getAttribute(FormManagerUtil.id_att);
		
		Element local_data_source_instance = FormManagerUtil.getElementByIdFromDocument(components_xforms, FormManagerUtil.head_tag, getLocalDataSourceInstanceIdentifier(component_element_id));
		Element external_data_source_instance = FormManagerUtil.getElementByIdFromDocument(components_xforms, FormManagerUtil.head_tag, getExternalDataSourceInstanceIdentifier(component_element_id));
		
		xformsComponentDataBean.setLocalItemsetInstance(local_data_source_instance);
		xformsComponentDataBean.setExternalItemsetInstance(external_data_source_instance);
	}
	private String getLocalDataSourceInstanceIdentifier(String element_id) {
	
		return element_id+local_data_source;
	}
	private String getExternalDataSourceInstanceIdentifier(String element_id) {
		
		return element_id+external_data_source;
	}
	
	@Override
	public void addComponentToDocument(DMContext context) {
		
		FormComponent component = context.getComponent();
		
		super.addComponentToDocument(component.getContext());
		
		Document xforms_doc = component.getFormDocument().getXformsDocument();
		
		XFormsComponentSelectDataBean xformsComponentDataBean = (XFormsComponentSelectDataBean)component.getXformsComponentDataBean();
		
		Element new_xforms_element = xformsComponentDataBean.getLocalItemsetInstance();
		
		Element data_model_instance_element = FormManagerUtil.getElementByIdFromDocument(xforms_doc, FormManagerUtil.head_tag, FormManagerUtil.data_mod);
		
		if(new_xforms_element != null) {

			String local_id = new_xforms_element.getAttribute(FormManagerUtil.id_att);
			
			new_xforms_element = (Element)xforms_doc.importNode(new_xforms_element, true);
			new_xforms_element.setAttribute(FormManagerUtil.id_att, getLocalDataSourceInstanceIdentifier(component.getId()));
			new_xforms_element = (Element)data_model_instance_element.appendChild(new_xforms_element);
			xformsComponentDataBean.setLocalItemsetInstance(new_xforms_element);
			
			Element itemset = DOMUtil.getElementByAttributeValue(xformsComponentDataBean.getElement(), "*", FormManagerUtil.nodeset_att, constructItemsetInstance(local_id, null));
			
			if(itemset != null)
				itemset.setAttribute(FormManagerUtil.nodeset_att, constructItemsetInstance(component.getContext(), PropertiesSelect.LOCAL_DATA_SRC));
		}
		
		new_xforms_element = xformsComponentDataBean.getExternalItemsetInstance();
		
		if(new_xforms_element != null) {
			
			new_xforms_element = (Element)xforms_doc.importNode(new_xforms_element, true);
			new_xforms_element.setAttribute(FormManagerUtil.id_att, getExternalDataSourceInstanceIdentifier(component.getId()));
			new_xforms_element = (Element)data_model_instance_element.appendChild(new_xforms_element);
			xformsComponentDataBean.setExternalItemsetInstance(new_xforms_element);
		}
	}
	
	public Integer getDataSrcUsed(DMContext context) {
		
		XFormsComponentDataBean xformsComponentDataBean = context.getComponent().getXformsComponentDataBean();
		
		Element component_element = xformsComponentDataBean.getElement();
		
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
	
	public String getExternalDataSrc(DMContext context) {
		
		Element external_instance = ((XFormsComponentSelectDataBean)context.getComponent().getXformsComponentDataBean()).getExternalItemsetInstance();
		
		if(external_instance == null)
			return null;
		
		return external_instance.getAttribute(FormManagerUtil.src_att);
	}
	
	public LocalizedItemsetBean getItemset(DMContext context) {

		FormComponent component = context.getComponent();
		
		Element local_instance = ((XFormsComponentSelectDataBean)component.getXformsComponentDataBean()).getLocalItemsetInstance();
		
		if(local_instance == null)
			return null;
		
		LocalizedItemsetBean itemset_bean = new LocalizedItemsetBean();
		itemset_bean.setLocalDataSrcElement(local_instance);
		itemset_bean.setComponentsXFormsDocument(component.getContext().getCacheManager().getComponentsXforms());
		itemset_bean.setComponent(component);
		
		return itemset_bean;
	}
	
	@Override
	public void update(DMContext context, ConstUpdateType what) {
		
		FormComponent component = context.getComponent();
		
		super.update(component.getContext(), what);
		
		int update = what.getUpdateType();
		
		switch (update) {
		case ConstUpdateType.data_src_used:
			updateDataSrcUsed(component.getContext());
			break;
			
		case ConstUpdateType.external_data_src:
			updateExternalDataSrc(component.getContext());
			break;
			
		default:
			break;
		}
	}
	
	protected void updateDataSrcUsed(DMContext context) {
		
		FormComponent component = context.getComponent();
		XFormsComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		PropertiesSelect properties = (PropertiesSelect)component.getProperties();
		Integer data_src_used = properties.getDataSrcUsed();
		
		Element component_element = xformsComponentDataBean.getElement();
		
		if(data_src_used == null) {
			
			Element itemset = DOMUtil.getChildElement(component_element, FormManagerUtil.itemset_tag);
			
			if(itemset != null)
				component_element.removeChild(itemset);
			
		} else {
			
			String itemset_instance_str = null; 
			
			if(data_src_used == PropertiesSelect.EXTERNAL_DATA_SRC) {
				
				itemset_instance_str = constructItemsetInstance(component.getContext(), PropertiesSelect.EXTERNAL_DATA_SRC);
				
			} else if(data_src_used == PropertiesSelect.LOCAL_DATA_SRC) {
				itemset_instance_str = constructItemsetInstance(component.getContext(), PropertiesSelect.LOCAL_DATA_SRC);
			}
			
			if(itemset_instance_str == null)
				return;
			
			Element itemset = DOMUtil.getChildElement(component_element, FormManagerUtil.itemset_tag);
			
			if(itemset == null) {
				
				itemset = FormManagerUtil.getItemElementById(component.getContext().getCacheManager().getComponentsXforms(), "itemset");
				itemset = (Element)component_element.getOwnerDocument().importNode(itemset, true);
				component_element.appendChild(itemset);
			}
			
			itemset.setAttribute(FormManagerUtil.nodeset_att, itemset_instance_str);
		}
	}
	
	private String constructItemsetInstance(DMContext context, Integer data_source) {
	
		return constructItemsetInstance(context.getComponent().getId(), data_source);
	}
	
	private String constructItemsetInstance(String component_id, Integer data_source) {
		
		StringBuffer buf = new StringBuffer();
		
		buf.append("instance('")
		.append(component_id);
		
		if(data_source != null) {
			
			if(data_source == PropertiesSelect.LOCAL_DATA_SRC)
				buf.append("_lds");
			else
				buf.append("_eds");
		}
		
		buf.append("')/localizedEntries[@lang=instance('localized_strings')/current_language]/item");
		
		return buf.toString();
	}

	protected void updateExternalDataSrc(DMContext context) {
		
		FormComponent component = context.getComponent();
		XFormsComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		Element external_instance = ((XFormsComponentSelectDataBean)xformsComponentDataBean).getExternalItemsetInstance();
		
		if(external_instance == null)
			return;
		
		String external_data_src = ((PropertiesSelect)component.getProperties()).getExternalDataSrc();
		
		if(external_data_src == null)
			return;
		
		external_instance.setAttribute(FormManagerUtil.src_att, external_data_src);
	}
	
	public void removeSelectComponentSourcesFromXFormsDocument(DMContext context) {
		
		XFormsComponentSelectDataBean xforms_component = (XFormsComponentSelectDataBean)context.getComponent().getXformsComponentDataBean();
		Element data_src_element = xforms_component.getExternalItemsetInstance();
		
		if(data_src_element != null)
			data_src_element.getParentNode().removeChild(data_src_element);
		
		data_src_element = xforms_component.getLocalItemsetInstance();
		
		if(data_src_element != null)
			data_src_element.getParentNode().removeChild(data_src_element);
	}
}