package com.idega.documentmanager.business.component.properties;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 * <i><b>WARNING: </b></i>for changes to take effect, u need to use setter methods for every property change
 */
public interface PropertiesPlain extends PropertiesComponent {

	public abstract String getText();
		
	public abstract void setText(String text);
}