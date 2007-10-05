package com.idega.documentmanager.manager;

import com.idega.documentmanager.component.beans.LocalizedItemsetBean;
import com.idega.documentmanager.component.properties.impl.ConstUpdateType;
import com.idega.documentmanager.context.DMContext;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2007/10/05 11:42:35 $ by $Author: civilis $
 */
public interface XFormsManagerSelect extends XFormsManager {

	public abstract void loadXFormsComponentByType(DMContext context,
			String componentType) throws NullPointerException;

	public abstract void addComponentToDocument(DMContext context);

	public abstract Integer getDataSrcUsed(DMContext context);

	public abstract String getExternalDataSrc(DMContext context);

	public abstract LocalizedItemsetBean getItemset(DMContext context);

	public abstract void update(DMContext context, ConstUpdateType what);

	public abstract void removeSelectComponentSourcesFromXFormsDocument(
			DMContext context);

}