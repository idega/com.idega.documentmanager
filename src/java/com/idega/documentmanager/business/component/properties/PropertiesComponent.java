package com.idega.documentmanager.business.component.properties;

import com.idega.block.process.variables.Variable;
import com.idega.chiba.web.xml.xforms.validation.ErrorType;
import com.idega.documentmanager.component.beans.LocalizedStringBean;

/**
 * <i><b>Note: </b></i>for changes to take effect, u need to use setter methods for every property change
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.11 $
 *
 * Last modified: $Date: 2008/10/23 13:27:22 $ by $Author: civilis $
 */
public interface PropertiesComponent {
	
	public abstract LocalizedStringBean getErrorMsg(ErrorType errorType);
	
	public abstract LocalizedStringBean getErrorMsg();

	public abstract void setErrorMsg(ErrorType errorType, LocalizedStringBean errorMsg);
	
	public abstract void setErrorMsg(LocalizedStringBean errorMsg);

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
		
	public abstract void setValidationText(LocalizedStringBean validation_text);
	
	public abstract LocalizedStringBean getValidationText();
	
	public abstract Variable getVariable();
		
	public abstract void setVariable(Variable variable);
	
	public abstract void setVariable(String variableStringRepresentation);
	
	public abstract boolean isReadonly();
	
	public abstract void setReadonly(boolean readonly);
	
	public abstract boolean isHasValidationConstraints();
}