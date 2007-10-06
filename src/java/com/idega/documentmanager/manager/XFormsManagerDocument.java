package com.idega.documentmanager.manager;

import org.w3c.dom.Element;

import com.idega.documentmanager.component.FormComponent;
import com.idega.documentmanager.component.properties.impl.ConstUpdateType;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2007/10/06 07:05:40 $ by $Author: civilis $
 */
public interface XFormsManagerDocument extends XFormsManagerContainer {

	public abstract void setComponentsContainer(FormComponent component,
			Element element);

	public abstract Element getAutofillAction(FormComponent component);

	public abstract Element getFormDataModelElement(FormComponent component);

	public abstract Element getSectionsVisualizationInstanceElement(
			FormComponent component);

	public abstract boolean getIsStepsVisualizationUsed(FormComponent component);

	public abstract void update(FormComponent component, ConstUpdateType what);

}