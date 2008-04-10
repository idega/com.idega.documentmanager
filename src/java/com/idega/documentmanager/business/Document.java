package com.idega.documentmanager.business;

import java.util.List;

import org.w3c.dom.Element;

import com.idega.documentmanager.business.component.Container;
import com.idega.documentmanager.business.component.Page;
import com.idega.documentmanager.business.component.PageThankYou;
import com.idega.documentmanager.business.component.properties.ParametersManager;
import com.idega.documentmanager.business.component.properties.PropertiesDocument;
import com.idega.documentmanager.business.ext.FormVariablesHandler;
import com.idega.documentmanager.component.beans.LocalizedStringBean;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.8 $
 *
 * Last modified: $Date: 2008/04/10 01:08:00 $ by $Author: civilis $
 */
public interface Document extends Container {

	/**
	 * <p>
	 * Creates new form page,
	 * inserts it before specific component OR after all components in form component list.
	 * New xforms document is saved and newly created component id is returned.
	 * </p>
	 * <p>
	 * <i><b>Note: </b></i>Of course, form document should be created or imported before.
	 * </p>
	 * 
	 * @param component_after_this_id - where new component should be placed.
	 * If provided, new component will be inserted <b>before</b> component with component_after_this_id id.
	 * Provide <i>null</i> if component needs to be appended to the end of all the components.
	 * @return newly created form page id
	 * @throws NullPointerException - form document was not created/imported before, 
	 * component_after_new_id was provided, but such component was not found in document
	 * @throws Exception - something else is wrong
	 * @return created page object
	 */
	public abstract Page addPage(String page_after_this_id) throws NullPointerException;
	
	public abstract String getFormSourceCode() throws Exception;
	
	public abstract void setFormSourceCode(String new_source_code) throws Exception;
	
	public abstract LocalizedStringBean getFormTitle();
	
	public abstract Long getFormId();
	
	public abstract void setFormTitle(LocalizedStringBean form_name) throws Exception;
	
	/**
	 * using getContainedPagesIdList method get components id list, then use this list to change the order of components,
	 * and then call this method for changes to take an effect
	 *
	 */
	public abstract void rearrangeDocument();
	
	public abstract Page getPage(String page_id);
	
	public abstract List<String> getContainedPagesIdList();
	
	public abstract void save() throws Exception;
	
	public abstract org.w3c.dom.Document getXformsDocument();
	
	public abstract Page getConfirmationPage();
	
	public abstract PageThankYou getThxPage();
	
	public abstract Page addConfirmationPage(String page_after_this_id);
	
	public abstract PropertiesDocument getProperties();
	
	public abstract ParametersManager getParametersManager();
	
	public abstract Element getSubmissionInstanceElement();
	
	public abstract FormVariablesHandler getFormVariablesHandler();
	
	public abstract void setReadonly(boolean readonly);
}