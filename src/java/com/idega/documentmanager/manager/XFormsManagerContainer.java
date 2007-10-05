package com.idega.documentmanager.manager;

import java.util.List;

import com.idega.documentmanager.context.DMContext;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2007/10/05 11:42:35 $ by $Author: civilis $
 */
public interface XFormsManagerContainer extends XFormsManager {

	public abstract List<String[]> getContainedComponentsTagNamesAndIds(
			DMContext context);

}