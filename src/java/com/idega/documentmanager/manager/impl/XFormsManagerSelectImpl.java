package com.idega.documentmanager.manager.impl;

import org.chiba.xml.dom.DOMUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.documentmanager.business.component.properties.PropertiesSelect;
import com.idega.documentmanager.component.FormComponent;
import com.idega.documentmanager.component.beans.LocalizedItemsetBean;
import com.idega.documentmanager.component.beans.ComponentDataBean;
import com.idega.documentmanager.component.beans.ComponentSelectDataBean;
import com.idega.documentmanager.component.properties.impl.ConstUpdateType;
import com.idega.documentmanager.manager.XFormsManagerSelect;
import com.idega.documentmanager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.6 $
 *
 * Last modified: $Date: 2007/11/07 15:02:29 $ by $Author: civilis $
 */
public class XFormsManagerSelectImpl extends XFormsManagerImpl implements XFormsManagerSelect {

	@Override
	protected ComponentDataBean newXFormsComponentDataBeanInstance() {
		return new ComponentSelectDataBean();
	}
	
	private static final String local_data_source = "_lds";
	private static final String external_data_source = "_eds";
	
	@Override
	protected void loadXFormsComponentDataBean(FormComponent component, Document xform, Element componentElement) {
		
		super.loadXFormsComponentDataBean(component, xform, componentElement);
		
		ComponentSelectDataBean xformsComponentDataBean = (ComponentSelectDataBean)component.getXformsComponentDataBean();
		
		componentElement = xformsComponentDataBean.getElement();
		String componentElementId = componentElement.getAttribute(FormManagerUtil.id_att);
		
		Element localDataSourceInstance = FormManagerUtil.getElementByIdFromDocument(xform, FormManagerUtil.head_tag, getLocalDataSourceInstanceIdentifier(componentElementId));
		Element externalDataSourceInstance = FormManagerUtil.getElementByIdFromDocument(xform, FormManagerUtil.head_tag, getExternalDataSourceInstanceIdentifier(componentElementId));
		
		xformsComponentDataBean.setLocalItemsetInstance(localDataSourceInstance);
		xformsComponentDataBean.setExternalItemsetInstance(externalDataSourceInstance);
	}
	private String getLocalDataSourceInstanceIdentifier(String element_id) {
	
		return element_id+local_data_source;
	}
	private String getExternalDataSourceInstanceIdentifier(String element_id) {
		
		return element_id+external_data_source;
	}
	
	@Override
	public void addComponentToDocument(FormComponent component) {
		
		super.addComponentToDocument(component);
		
		Document xforms_doc = component.getContext().getXformsXmlDoc();
		
		ComponentSelectDataBean xformsComponentDataBean = (ComponentSelectDataBean)component.getXformsComponentDataBean();
		
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
				itemset.setAttribute(FormManagerUtil.nodeset_att, constructItemsetInstance(component, PropertiesSelect.LOCAL_DATA_SRC));
		}
		
		new_xforms_element = xformsComponentDataBean.getExternalItemsetInstance();
		
		if(new_xforms_element != null) {
			
			new_xforms_element = (Element)xforms_doc.importNode(new_xforms_element, true);
			new_xforms_element.setAttribute(FormManagerUtil.id_att, getExternalDataSourceInstanceIdentifier(component.getId()));
			new_xforms_element = (Element)data_model_instance_element.appendChild(new_xforms_element);
			xformsComponentDataBean.setExternalItemsetInstance(new_xforms_element);
		}
	}
	
	public Integer getDataSrcUsed(FormComponent component) {
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
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
	
	public String getExternalDataSrc(FormComponent component) {
		
		Element external_instance = ((ComponentSelectDataBean)component.getXformsComponentDataBean()).getExternalItemsetInstance();
		
		if(external_instance == null)
			return null;
		
		return external_instance.getAttribute(FormManagerUtil.src_att);
	}
	
	public LocalizedItemsetBean getItemset(FormComponent component) {

		Element local_instance = ((ComponentSelectDataBean)component.getXformsComponentDataBean()).getLocalItemsetInstance();
		
		if(local_instance == null)
			return null;
		
		LocalizedItemsetBean itemset_bean = new LocalizedItemsetBean();
		itemset_bean.setLocalDataSrcElement(local_instance);
		itemset_bean.setComponentsXFormsDocument(component.getContext().getCacheManager().getComponentsXforms());
		itemset_bean.setComponent(component);
		
		return itemset_bean;
	}
	
	@Override
	public void update(FormComponent component, ConstUpdateType what) {
		
		super.update(component, what);
		
		switch (what) {
		case DATA_SRC_USED:
			updateDataSrcUsed(component);
			break;
			
		case EXTERNAL_DATA_SRC:
			updateExternalDataSrc(component);
			break;
			
		default:
			break;
		}
	}
	
	protected void updateDataSrcUsed(FormComponent component) {
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
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
				
				itemset_instance_str = constructItemsetInstance(component, PropertiesSelect.EXTERNAL_DATA_SRC);
				
			} else if(data_src_used == PropertiesSelect.LOCAL_DATA_SRC) {
				itemset_instance_str = constructItemsetInstance(component, PropertiesSelect.LOCAL_DATA_SRC);
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
	
	private String constructItemsetInstance(FormComponent component, Integer data_source) {
	
		return constructItemsetInstance(component.getId(), data_source);
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

	protected void updateExternalDataSrc(FormComponent component) {
		
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		Element external_instance = ((ComponentSelectDataBean)xformsComponentDataBean).getExternalItemsetInstance();
		
		if(external_instance == null)
			return;
		
		String external_data_src = ((PropertiesSelect)component.getProperties()).getExternalDataSrc();
		
		if(external_data_src == null)
			return;
		
		external_instance.setAttribute(FormManagerUtil.src_att, external_data_src);
	}
	
	public void removeSelectComponentSourcesFromXFormsDocument(FormComponent component) {
		
		ComponentSelectDataBean xforms_component = (ComponentSelectDataBean)component.getXformsComponentDataBean();
		Element data_src_element = xforms_component.getExternalItemsetInstance();
		
		if(data_src_element != null)
			data_src_element.getParentNode().removeChild(data_src_element);
		
		data_src_element = xforms_component.getLocalItemsetInstance();
		
		if(data_src_element != null)
			data_src_element.getParentNode().removeChild(data_src_element);
	}
}