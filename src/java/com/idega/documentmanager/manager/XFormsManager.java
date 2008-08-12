package com.idega.documentmanager.manager;

import org.w3c.dom.Element;

import com.idega.documentmanager.component.FormComponent;
import com.idega.documentmanager.component.FormComponentPage;
import com.idega.documentmanager.component.beans.LocalizedStringBean;
import com.idega.documentmanager.component.properties.impl.ConstUpdateType;
import com.idega.jbpm.variables.Variable;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.10 $
 *
 * Last modified: $Date: 2008/08/12 06:07:39 $ by $Author: arunas $
 */
public interface XFormsManager {

	public abstract void loadXFormsComponentByTypeFromComponentsXForm(FormComponent component, String component_type)
			throws NullPointerException;

	public abstract void loadXFormsComponentFromDocument(FormComponent component);

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
		
	public abstract LocalizedStringBean getValidationText(FormComponent component);

	public abstract void loadConfirmationElement(FormComponent component, FormComponentPage confirmation_page);
	
	public abstract String getAutofillKey(FormComponent component);
	
	public abstract boolean isRequired(FormComponent component);
	
	public abstract Variable getVariable(FormComponent component);
	
	public abstract boolean isReadonly(FormComponent component);
	
	public void setReadonly(FormComponent component, boolean readonly);
}