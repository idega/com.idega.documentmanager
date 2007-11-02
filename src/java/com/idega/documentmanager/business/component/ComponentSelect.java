package com.idega.documentmanager.business.component;

import com.idega.documentmanager.business.component.properties.PropertiesSelect;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2007/11/02 15:04:56 $ by $Author: civilis $
 */
public interface ComponentSelect extends Component {
	
	public abstract PropertiesSelect getProperties();
}