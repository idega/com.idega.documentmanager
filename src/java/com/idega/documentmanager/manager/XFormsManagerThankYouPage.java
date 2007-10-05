package com.idega.documentmanager.manager;

import com.idega.documentmanager.component.beans.LocalizedStringBean;
import com.idega.documentmanager.component.properties.impl.ConstUpdateType;
import com.idega.documentmanager.context.DMContext;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2007/10/05 11:42:35 $ by $Author: civilis $
 */
public interface XFormsManagerThankYouPage extends XFormsManagerPage {

	public abstract void update(DMContext context, ConstUpdateType what);

	public abstract LocalizedStringBean getThankYouText(DMContext context);

}