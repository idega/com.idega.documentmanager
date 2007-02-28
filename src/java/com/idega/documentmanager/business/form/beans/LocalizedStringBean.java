package com.idega.documentmanager.business.form.beans;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 * Just the wrapper around Map object, representing localized string
 */
public class LocalizedStringBean {
	
	private Map<Locale, String> strings;
	
	public LocalizedStringBean() {
		strings = new HashMap<Locale, String>();
	}
	
	public Set<Locale> getLanguagesKeySet() {
		return strings.keySet();
	}
	
	/**
	 * if You don't want to change the text, provide text value as null
	 * 
	 * @param locale
	 * @param text
	 */
	public void setString(Locale locale, String text) {
		
		if(locale == null)
			throw new NullPointerException("Locale is not provided");
		
		strings.put(locale, text);
	}
	
	public String getString(Locale locale) {
		return strings.get(locale);
	}
	
	public void removeString(Locale locale) {
		strings.remove(locale);
	}
	
	public void clear() {
		strings.clear();
	}
	public String toString() {
		
		StringBuffer to_string = new StringBuffer("LocalizedStringBean:");
		
		
		for (Iterator<Locale> iter = strings.keySet().iterator(); iter.hasNext();) {
			Locale locale = iter.next();
			
			to_string.append("\nlocale: ")
			.append(locale.getLanguage())
			.append(" value: ")
			.append(strings.get(locale));
		}
		
		return to_string.toString();
	}
}
