package com.idega.documentmanager.manager;

import org.w3c.dom.Element;

import com.idega.documentmanager.component.properties.impl.ConstUpdateType;
import com.idega.documentmanager.context.DMContext;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2007/10/05 11:42:35 $ by $Author: civilis $
 */
public interface XFormsManagerDocument extends XFormsManagerContainer {

	public abstract void setComponentsContainer(DMContext context,
			Element element);

	public abstract Element getAutofillAction(DMContext context);

	public abstract Element getFormDataModelElement(DMContext context);

	public abstract Element getSectionsVisualizationInstanceElement(
			DMContext context);

	public abstract boolean getIsStepsVisualizationUsed(DMContext context);

	public abstract void update(DMContext context, ConstUpdateType what);

}