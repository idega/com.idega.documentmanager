package com.idega.documentmanager.manager;

import org.w3c.dom.Element;

import com.idega.documentmanager.component.FormComponentPage;
import com.idega.documentmanager.component.beans.LocalizedStringBean;
import com.idega.documentmanager.component.properties.impl.ConstUpdateType;
import com.idega.documentmanager.context.DMContext;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2007/10/05 11:42:35 $ by $Author: civilis $
 */
public interface XFormsManager {

	/**
	 * Gets full component by component type. 
	 * 
	 * @param component_type - used to find correct xforms component implementation
	 * @return element node.
	 * @throws NullPointerException - component implementation could not be found by component type
	 */
	public abstract void loadXFormsComponentByType(DMContext context, String component_type)
			throws NullPointerException;

	public abstract void loadXFormsComponentFromDocument(DMContext context, String component_id);

	public abstract void addComponentToDocument(DMContext context);

	public abstract void update(DMContext context, ConstUpdateType what);
	
	public abstract void moveComponent(DMContext context, String before_component_id);

	public abstract void removeComponentFromXFormsDocument(DMContext context);

	public abstract String insertBindElement(DMContext context, Element new_bind_element,
			String bind_id);

	public abstract void changeBindName(DMContext context, String new_bind_name);
	
	public abstract LocalizedStringBean getLocalizedStrings(DMContext context);
	
	public abstract LocalizedStringBean getErrorLabelLocalizedStrings(DMContext context);
	
	public abstract LocalizedStringBean getHelpText(DMContext context);
	
	public abstract void loadConfirmationElement(DMContext context, FormComponentPage confirmation_page);
	
	public abstract String getAutofillKey(DMContext context);
	
	public abstract boolean getIsRequired(DMContext context);
}