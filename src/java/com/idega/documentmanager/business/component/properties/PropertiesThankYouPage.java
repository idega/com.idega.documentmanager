package com.idega.documentmanager.business.component.properties;

import com.idega.documentmanager.component.beans.LocalizedStringBean;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2007/10/30 21:57:44 $ by $Author: civilis $
 */
public interface PropertiesThankYouPage extends PropertiesPage {
	
	public abstract LocalizedStringBean getText();
	
	public abstract void setText(LocalizedStringBean text);
}