package com.idega.documentmanager.manager;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2007/10/05 12:27:16 $ by $Author: civilis $
 */
import java.util.Locale;

import org.w3c.dom.Element;

import com.idega.documentmanager.context.DMContext;

public interface HtmlManager {

	public abstract Element getHtmlRepresentation(DMContext context, Locale locale)
			throws Exception;

	public abstract Element getDefaultHtmlRepresentationByType(String component_type);

	public abstract void clearHtmlComponents(DMContext context);
}