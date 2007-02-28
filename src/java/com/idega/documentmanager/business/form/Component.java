package com.idega.documentmanager.business.form;

import java.util.Locale;

import org.w3c.dom.Element;

/**
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 *
 */
public interface Component {

	public abstract String getId();
	
	public abstract Element getHtmlRepresentation(Locale locale) throws Exception;
	
	public abstract PropertiesComponent getProperties();
	
	public abstract void remove();
	
	public abstract String getType();
}