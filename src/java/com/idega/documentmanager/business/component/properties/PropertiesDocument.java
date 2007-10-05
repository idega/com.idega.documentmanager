package com.idega.documentmanager.business.component.properties;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 * <i><b>Note: </b></i>for changes to take effect, u need to use setter methods for every property change
 */
public interface PropertiesDocument extends PropertiesComponent {
	
	public abstract boolean isStepsVisualizationUsed();
	public abstract void setStepsVisualizationUsed(boolean steps_visualization_used);
}