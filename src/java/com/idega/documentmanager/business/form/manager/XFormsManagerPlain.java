package com.idega.documentmanager.business.form.manager;

import org.w3c.dom.Element;

import com.idega.documentmanager.business.form.PropertiesPlain;
import com.idega.documentmanager.business.form.beans.ConstUpdateType;
import com.idega.documentmanager.business.form.manager.util.FormManagerUtil;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 */
public class XFormsManagerPlain extends XFormsManager {

	@Override
	public void update(ConstUpdateType what) {
		super.update(what);
		
		int update = what.getUpdateType();
		
		switch (update) {
			
		case ConstUpdateType.text:
			updateText();
			break;

		default:
			break;
		}
	}
	
	protected void updateText() {
		
		PropertiesPlain props = (PropertiesPlain)component.getProperties();
		String text = props.getText();
		Element c_el = xforms_component.getElement();
		
		FormManagerUtil.removeTextNodes(c_el);
		
		if(text != null && !text.equals(""))
			c_el.appendChild(form_document.getXformsDocument().createTextNode(text));
	}
	
	public String getText() {
		
		Element c_el = xforms_component.getElement();
		String text = c_el.getTextContent();
		return text == null ? "" : text;
	}
}