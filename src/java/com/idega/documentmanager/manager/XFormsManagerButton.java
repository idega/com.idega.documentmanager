package com.idega.documentmanager.manager;

import com.idega.documentmanager.component.FormComponent;
import com.idega.documentmanager.component.FormComponentPage;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2007/10/06 07:05:40 $ by $Author: civilis $
 */
public interface XFormsManagerButton extends XFormsManager {
	
	public abstract void renewButtonPageContextPages(FormComponent component, FormComponentPage previous, FormComponentPage next);
	
	public abstract void setLastPageToSubmitButton(FormComponent component, String last_page_id);
}