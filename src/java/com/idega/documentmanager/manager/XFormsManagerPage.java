package com.idega.documentmanager.manager;

import com.idega.documentmanager.context.DMContext;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2007/10/05 11:42:35 $ by $Author: civilis $
 */
public interface XFormsManagerPage extends XFormsManagerContainer {

	public abstract void loadXFormsComponentFromDocument(DMContext context,
			String component_id);

	public abstract void addComponentToDocument(DMContext context);

	public abstract void removeComponentFromXFormsDocument(DMContext context);

	public abstract void moveComponent(DMContext context,
			String before_component_id);

	public abstract void pageContextChanged(DMContext context);

}