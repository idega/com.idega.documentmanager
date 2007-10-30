package com.idega.documentmanager.component.properties.impl;

import com.idega.documentmanager.business.component.properties.PropertiesButton;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2007/10/30 21:57:44 $ by $Author: civilis $
 */
public class ComponentPropertiesButton extends ComponentProperties implements PropertiesButton {
	
	private String referAction;
	
	public String getReferAction() {
		return referAction;
	}
	public void setReferAction(String referAction) {
		this.referAction = referAction;
	}
	public void setReferActionPlain(String referAction) {
		
		component.update(ConstUpdateType.BUTTON_REFER_TO_ACTION);
	}
}