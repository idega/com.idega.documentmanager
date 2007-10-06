package com.idega.documentmanager.manager;

import java.util.List;

import com.idega.documentmanager.component.FormComponent;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2007/10/06 07:05:40 $ by $Author: civilis $
 */
public interface XFormsManagerContainer extends XFormsManager {

	public abstract List<String[]> getContainedComponentsTagNamesAndIds(
			FormComponent component);

}