package com.idega.documentmanager.business.form;

import com.idega.documentmanager.business.form.beans.LocalizedStringBean;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 * <i><b>Note: </b></i>for changes to take effect, u need to use setter methods for every property change
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
}