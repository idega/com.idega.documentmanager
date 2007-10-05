package com.idega.documentmanager.manager;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2007/10/05 11:42:35 $ by $Author: civilis $
 */
import java.util.Locale;

import org.w3c.dom.Element;

import com.idega.documentmanager.component.FormComponent;
import com.idega.documentmanager.component.FormDocument;
import com.idega.documentmanager.manager.impl.CacheManager;

public interface HtmlManager {

	public abstract Element getHtmlRepresentation(Locale locale)
			throws Exception;

	public abstract Element getDefaultHtmlRepresentationByType(
			String component_type);

	public abstract void clearHtmlComponents();

	public abstract void setFormComponent(FormComponent component);

	public abstract void setCacheManager(CacheManager cache_manager);

	public abstract void setFormDocument(FormDocument form_document);

}