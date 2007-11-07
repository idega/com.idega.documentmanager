package com.idega.documentmanager.manager;

import com.idega.documentmanager.component.FormComponent;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.3 $
 *
 * Last modified: $Date: 2007/11/07 15:02:29 $ by $Author: civilis $
 */
public interface XFormsManagerPage extends XFormsManagerContainer {

	public abstract void addComponentToDocument(FormComponent component);

	public abstract void removeComponentFromXFormsDocument(FormComponent component);

	public abstract void moveComponent(FormComponent component,
			String before_component_id);

	public abstract void pageContextChanged(FormComponent component);

}