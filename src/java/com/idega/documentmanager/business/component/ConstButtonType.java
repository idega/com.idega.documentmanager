package com.idega.documentmanager.business.component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.3 $
 *
 * Last modified: $Date: 2007/11/15 09:24:15 $ by $Author: civilis $
 */
public enum ConstButtonType {
	
	PREVIOUS_PAGE_BUTTON {public String toString() { return "fbc_button_previous"; }},
	NEXT_PAGE_BUTTON {public String toString() { return "fbc_button_next"; }},
	SUBMIT_FORM_BUTTON {public String toString() { return "fbc_button_submit"; }},
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