package com.idega.documentmanager.manager;

import java.util.Locale;

import org.w3c.dom.Element;

import com.idega.documentmanager.component.FormComponent;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.3 $
 *
 * Last modified: $Date: 2007/10/06 07:05:40 $ by $Author: civilis $
 */
public interface HtmlManager {

	public abstract Element getHtmlRepresentation(FormComponent component, Locale locale)
			throws Exception;

	public abstract Element getDefaultHtmlRepresentationByType(String component_type);

	public abstract void clearHtmlComponents(FormComponent component);
}