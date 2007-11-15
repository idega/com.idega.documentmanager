package com.idega.documentmanager.manager;

import java.util.Locale;

import org.w3c.dom.Element;

import com.idega.documentmanager.component.FormComponent;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.4 $
 *
 * Last modified: $Date: 2007/11/15 09:24:15 $ by $Author: civilis $
 */
public interface HtmlManager {

	public abstract Element getHtmlRepresentation(FormComponent component, Locale locale)
			throws Exception;

	public abstract void clearHtmlComponents(FormComponent component);
}