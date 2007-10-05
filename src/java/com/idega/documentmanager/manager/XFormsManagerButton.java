package com.idega.documentmanager.manager;


import com.idega.documentmanager.component.FormComponentPage;
import com.idega.documentmanager.context.DMContext;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2007/10/05 11:42:35 $ by $Author: civilis $
 */
public interface XFormsManagerButton extends XFormsManager {
	
	public abstract void renewButtonPageContextPages(DMContext context, FormComponentPage previous, FormComponentPage next);
	
	public abstract void setLastPageToSubmitButton(DMContext context, String last_page_id);
}