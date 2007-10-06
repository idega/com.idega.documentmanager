package com.idega.documentmanager.manager;

import com.idega.documentmanager.component.FormComponent;
import com.idega.documentmanager.component.properties.impl.ConstUpdateType;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2007/10/06 07:05:41 $ by $Author: civilis $
 */
public interface XFormsManagerPlain extends XFormsManager {

	public abstract void update(FormComponent component, ConstUpdateType what);

	public abstract String getText(FormComponent component);

}