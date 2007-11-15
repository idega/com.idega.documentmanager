package com.idega.documentmanager.business.component.properties;

import com.idega.documentmanager.component.beans.LocalizedStringBean;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.3 $
 *
 * Last modified: $Date: 2007/11/15 09:24:15 $ by $Author: civilis $
 */
public interface PropertiesPlain extends PropertiesComponent {

	public abstract LocalizedStringBean getText();

	public abstract void setText(LocalizedStringBean text);
}