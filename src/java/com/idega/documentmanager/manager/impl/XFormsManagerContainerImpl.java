package com.idega.documentmanager.manager.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.chiba.xml.dom.DOMUtil;
import org.w3c.dom.Element;

import com.idega.documentmanager.component.beans.ComponentDataBean;
import com.idega.documentmanager.context.DMContext;
import com.idega.documentmanager.manager.XFormsManagerContainer;
import com.idega.documentmanager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2007/10/05 12:27:16 $ by $Author: civilis $
 */
public class XFormsManagerContainerImpl extends XFormsManagerImpl implements XFormsManagerContainer {
	
	public List<String[]> getContainedComponentsTagNamesAndIds(DMContext context) {

		ComponentDataBean xformsComponentDataBean = context.getComponent().getXformsComponentDataBean();
		
		if(xformsComponentDataBean.getElement() == null)
			throw new NullPointerException("Document container element not set");
		
		@SuppressWarnings("unchecked")
		List<Element> components_elements = DOMUtil.getChildElements(xformsComponentDataBean.getElement());
		List<String[]> components_tag_names_and_ids = new ArrayList<String[]>();
		
		for (Iterator<Element> iter = components_elements.iterator(); iter.hasNext();) {
			
			Element component_element = iter.next();
			String[] tag_name_and_id = new String[2];
			tag_name_and_id[1] = component_element.getAttribute(FormManagerUtil.id_att);
			
			if(tag_name_and_id[1] == null || !tag_name_and_id[1].startsWith(FormManagerUtil.CTID))
				continue;
			
			tag_name_and_id[0] = component_element.getTagName();
			
			String name_val = component_element.getAttribute(FormManagerUtil.name_att);
			
			if(name_val != null && !name_val.equals(""))
				tag_name_and_id[0] = name_val;
			
			components_tag_names_and_ids.add(tag_name_and_id);
		}
		return components_tag_names_and_ids;
	}
}