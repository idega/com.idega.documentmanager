package com.idega.documentmanager.component.beans;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.3 $
 * 
 * Last modified: $Date: 2008/07/16 11:13:14 $ by $Author: arunas $
 */
public class LocalizedStringBean {
	
	private Map<Locale, String> strings;
	
	public LocalizedStringBean() {
		strings = new HashMap<Locale, String>();
	}
	
	public LocalizedStringBean(String defaultString) {
		strings = new HashMap<Locale, String>();
		setString(new Locale("en"), defaultString);
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
		
		StringBuffer toString = new StringBuffer("LocalizedStringBean:");
		
		
		for (Iterator<Locale> iter = strings.keySet().iterator(); iter.hasNext();) {
			Locale locale = iter.next();
			
			toString.append("\nlocale: ")
			.append(locale.toString())
			.append(" value: ")
			.append(strings.get(locale));
		}
		
		return toString.toString();
	}
}
