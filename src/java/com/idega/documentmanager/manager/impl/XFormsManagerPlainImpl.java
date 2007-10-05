package com.idega.documentmanager.manager.impl;

import org.w3c.dom.Element;

import com.idega.documentmanager.business.component.properties.PropertiesPlain;
import com.idega.documentmanager.component.FormComponent;
import com.idega.documentmanager.component.beans.ComponentDataBean;
import com.idega.documentmanager.component.properties.impl.ConstUpdateType;
import com.idega.documentmanager.context.DMContext;
import com.idega.documentmanager.manager.XFormsManagerPlain;
import com.idega.documentmanager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2007/10/05 12:27:16 $ by $Author: civilis $
 */
public class XFormsManagerPlainImpl extends XFormsManagerImpl implements XFormsManagerPlain {

	@Override
	public void update(DMContext context, ConstUpdateType what) {
		
		FormComponent component = context.getComponent();
		
		super.update(component.getContext(), what);
		
		int update = what.getUpdateType();
		
		switch (update) {
			
		case ConstUpdateType.text:
			updateText(component.getContext());
			break;

		default:
			break;
		}
	}
	
	protected void updateText(DMContext context) {
		
		FormComponent component = context.getComponent();
		ComponentDataBean xformsComponentDataBean = component.getXformsComponentDataBean();
		
		PropertiesPlain props = (PropertiesPlain)component.getProperties();
		String text = props.getText();
		Element c_el = xformsComponentDataBean.getElement();
		
		FormManagerUtil.removeTextNodes(c_el);
		
		if(text != null && !text.equals(""))
			c_el.appendChild(c_el.getOwnerDocument().createTextNode(text));
	}
	
	public String getText(DMContext context) {
		
		Element c_el = context.getComponent().getXformsComponentDataBean().getElement();
		String text = c_el.getTextContent();
		return text == null ? "" : text;
	}
	
	@Override
	protected boolean removeTextNodes() {
		return false;
	}
}