package com.idega.documentmanager.manager;

import com.idega.documentmanager.component.FormComponent;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2007/10/06 07:05:41 $ by $Author: civilis $
 */
public interface XFormsManagerPage extends XFormsManagerContainer {

	public abstract void loadXFormsComponentFromDocument(FormComponent component,
			String component_id);

	public abstract void addComponentToDocument(FormComponent component);

	public abstract void removeComponentFromXFormsDocument(FormComponent component);

	public abstract void moveComponent(FormComponent component,
			String before_component_id);

	public abstract void pageContextChanged(FormComponent component);

}