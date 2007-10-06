package com.idega.documentmanager.manager;

import org.w3c.dom.Element;

import com.idega.documentmanager.component.FormComponent;
import com.idega.documentmanager.component.FormComponentPage;
import com.idega.documentmanager.component.beans.LocalizedStringBean;
import com.idega.documentmanager.component.properties.impl.ConstUpdateType;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.3 $
 *
 * Last modified: $Date: 2007/10/06 13:07:12 $ by $Author: civilis $
 */
public interface XFormsManager {

	public abstract void loadXFormsComponentByType(FormComponent component, String component_type)
			throws NullPointerException;

	public abstract void loadXFormsComponentFromDocument(FormComponent component, String component_id);

	public abstract void addComponentToDocument(FormComponent component);

	public abstract void update(FormComponent component, ConstUpdateType what);
	
	public abstract void moveComponent(FormComponent component, String before_component_id);

	public abstract void removeComponentFromXFormsDocument(FormComponent component);

	public abstract String insertBindElement(FormComponent component, Element new_bind_element,
			String bind_id);

	public abstract void changeBindName(FormComponent component, String new_bind_name);
	
	public abstract LocalizedStringBean getLocalizedStrings(FormComponent component);
	
	public abstract LocalizedStringBean getErrorLabelLocalizedStrings(FormComponent component);
	
	public abstract LocalizedStringBean getHelpText(FormComponent component);
	
	public abstract void loadConfirmationElement(FormComponent component, FormComponentPage confirmation_page);
	
	public abstract String getAutofillKey(FormComponent component);
	
	public abstract boolean getIsRequired(FormComponent component);
	
	public abstract String getVariableName(FormComponent component);
}