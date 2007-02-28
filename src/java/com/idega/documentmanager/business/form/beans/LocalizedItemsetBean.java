package com.idega.documentmanager.business.form.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.chiba.xml.dom.DOMUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.idega.documentmanager.business.form.manager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 */
public class LocalizedItemsetBean implements ILocalizedItemset {
	
	private Map<Locale, List<ItemBean>> itemsets;
	private Map<Locale, Element> localized_entries;
	private Element local_data_src_element;
	private Document components_xforms;
	private IComponentPropertiesParent parent_component;
	
	public LocalizedItemsetBean() {
		itemsets = new HashMap<Locale, List<ItemBean>>();
		localized_entries = new HashMap<Locale, Element>();
	}
	public void setParentComponent(IComponentPropertiesParent parent_component) {
		this.parent_component = parent_component;
	}
	public void setLocalDataSrcElement(Element local_data_src_element_main) {
		local_data_src_element = DOMUtil.getFirstChildElement(local_data_src_element_main);
		
		List<Element> localized_entries_elements = DOMUtil.getChildElements(local_data_src_element);
		
		for (Iterator<Element> iter = localized_entries_elements.iterator(); iter.hasNext();) {
			Element localized_entry = iter.next();
			
			String lang = localized_entry.getAttribute(FormManagerUtil.lang_att);
			
			if(lang == null)
				continue;
			
			Locale locale = new Locale(lang);
			
			localized_entries.put(locale, localized_entry);
			itemsets.put(locale, null);
		}
	}
	
	public Set<Locale> getItemsetKeySet() {
		return itemsets.keySet();
	}
	public void clear() {
		
		List<Element> child_elements = DOMUtil.getChildElements(local_data_src_element);
		
		for (Iterator<Element> iter = child_elements.iterator(); iter.hasNext();)
			local_data_src_element.removeChild(iter.next());
		
		itemsets.clear();
		localized_entries.clear();
	}
	public List<ItemBean> getItems(Locale locale) {
		
		List<ItemBean> items = itemsets.get(locale);
		
		if(items != null)
			return items;
		
		items = new ArrayList<ItemBean>();
		
		Element localized_entry = localized_entries.get(locale);
		
		if(localized_entry != null) {
			
			List<Element> items_elements = DOMUtil.getChildElements(localized_entry);
			
			for (Iterator<Element> iterator = items_elements.iterator(); iterator.hasNext();) {
				Element item_element = iterator.next();
				String label = 
					DOMUtil.getTextNodeAsString(
							DOMUtil.getChildElement(item_element, FormManagerUtil.item_label_tag)
					);
				
				String value = 
					DOMUtil.getTextNodeAsString(
							DOMUtil.getChildElement(item_element, FormManagerUtil.item_value_tag)
					);
				
				ItemBean item = new ItemBean();
				item.setLabel(label);
				item.setValue(value);
				
				items.add(item);
			}
			
			itemsets.put(locale, items);
		}
		return items;
	}
	
	public void setItems(Locale locale, List<ItemBean> items) {
		
		if(items == null) {
			
			if(localized_entries.containsKey(locale)) {
				Element localized_entry = localized_entries.get(locale);
				localized_entry.getParentNode().removeChild(localized_entry);
				localized_entries.remove(locale);
			}
			itemsets.remove(locale);
		} else {
			
			Element localized_entries_element;
			
			if(localized_entries.containsKey(locale)) {
				localized_entries_element = localized_entries.get(locale);
				
				List<Element> child_elements = DOMUtil.getChildElements(localized_entries_element);
				
				for (Iterator<Element> iter = child_elements.iterator(); iter.hasNext();)
					localized_entries_element.removeChild(iter.next());
				
			} else {
				
				localized_entries_element = local_data_src_element.getOwnerDocument().createElement(FormManagerUtil.localized_entries_tag);
				localized_entries_element.setAttribute(FormManagerUtil.lang_att, locale.getLanguage());
				local_data_src_element.appendChild(localized_entries_element);
			}

			if(!items.isEmpty()) {
				
				Element item_element = FormManagerUtil.getItemElementById(components_xforms, "item_src");
				Document owner_document = localized_entries_element.getOwnerDocument();
				
				for (Iterator<ItemBean> iterator = items.iterator(); iterator.hasNext();) {
					ItemBean item = iterator.next();
					
					item_element = (Element)owner_document.importNode(item_element, true);
					localized_entries_element.appendChild(item_element);
					
					String label = item.getLabel();
					String value = item.getValue();
					
					if(label != null) {
						Element label_element = DOMUtil.getChildElement(item_element, FormManagerUtil.item_label_tag);
						DOMUtil.setElementValue(label_element, label);
					}
					
					if(value != null) {
						Element value_element = DOMUtil.getChildElement(item_element, FormManagerUtil.item_value_tag);
						DOMUtil.setElementValue(value_element, value);
					}
				}
			}
			localized_entries.put(locale, localized_entries_element);
			itemsets.put(locale, items);
		}
		
		parent_component.update(new ConstUpdateType(ConstUpdateType.itemset));
	}
	
	public void setComponentsXFormsDocument(Document components_xforms) {
		this.components_xforms = components_xforms;
	}
}