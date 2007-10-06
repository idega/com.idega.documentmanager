package com.idega.documentmanager.business.component.properties;

import com.idega.documentmanager.component.beans.LocalizedStringBean;

/**
 * <i><b>Note: </b></i>for changes to take effect, u need to use setter methods for every property change
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2007/10/06 13:07:12 $ by $Author: civilis $
 */
public interface PropertiesComponent {
	
	public abstract LocalizedStringBean getErrorMsg();

	public abstract void setErrorMsg(LocalizedStringBean error_msg);

	public abstract LocalizedStringBean getLabel();

	public abstract void setLabel(LocalizedStringBean label);

	public abstract boolean isRequired();

	public abstract void setRequired(boolean required);
	
	public abstract String getP3ptype();
	
	public abstract void setP3ptype(String p3ptype);
	
	public abstract String getAutofillKey();
		
	public abstract void setAutofillKey(String autofill_key);
	
	public abstract LocalizedStringBean getHelpText();
	
	public abstract void setHelpText(LocalizedStringBean help_text);
	
	public abstract String getVariableName();
		
	public abstract void setVariableName(String variableName);
}