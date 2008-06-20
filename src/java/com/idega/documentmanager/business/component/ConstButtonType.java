package com.idega.documentmanager.business.component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.4 $
 *
 * Last modified: $Date: 2008/06/20 13:53:57 $ by $Author: arunas $
 */
public enum ConstButtonType {
	
	PREVIOUS_PAGE_BUTTON {public String toString() { return "fbc_button_previous"; }},
	NEXT_PAGE_BUTTON {public String toString() { return "fbc_button_next"; }},
	SUBMIT_FORM_BUTTON {public String toString() { return "fbc_button_submit"; }},
	SAVE_FORM_BUTTON {public String toString() { return "fbc_button_save"; }},
	RESET_FORM_BUTTON {public String toString() { return "fbc_button_reset"; }};
	
	public static Set<String> getAllTypesInStrings() {
		
		return getAllStringTypesEnumsMappings().keySet();
	}
	
	private static Map<String, ConstButtonType> allStringTypesEnumsMappings;
	
	private synchronized static Map<String, ConstButtonType> getAllStringTypesEnumsMappings() {
		
		if(allStringTypesEnumsMappings == null) {
			
			allStringTypesEnumsMappings = new HashMap<String, ConstButtonType>();
			
			for (ConstButtonType type : values())
				allStringTypesEnumsMappings.put(type.toString(), type);
		}
		
		return allStringTypesEnumsMappings;
	}
	
	public static ConstButtonType getByStringType(String type) {
		
		return getAllStringTypesEnumsMappings().get(type);
	}
	
	public abstract String toString();
}