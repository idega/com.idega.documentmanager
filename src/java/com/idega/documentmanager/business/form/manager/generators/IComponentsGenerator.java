package com.idega.documentmanager.business.form.manager.generators;

import org.w3c.dom.Document;

/**
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 *
 */
public interface IComponentsGenerator {

	public abstract boolean isInitiated();

	/**
	 * 
	 * Generates xml components document from xforms components document
	 * 
	 * @return HTML components xml document
	 * @throws Exception. Concrete exceptions throwed depends on implementation.
	 */
	public abstract Document generateBaseComponentsDocument() throws Exception;
	
	/**
	 * set input document
	 * @param doc - document, to generate components from
	 */
	public abstract void setDocument(Document doc);
	
//	public abstract void setFormComponentsBaseUri(String base_uri);
	
//	public abstract Document generateFormHtmlDocument() throws NullPointerException, ParserConfigurationException, XFormsException, Exception;
}