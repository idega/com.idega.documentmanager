package com.idega.documentmanager.business.component.properties;

/**
 * <i><b>Note: </b></i>for changes to take effect, u need to use setter methods for every property change
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2007/10/06 13:07:12 $ by $Author: civilis $
 */
public interface PropertiesDocument extends PropertiesComponent {
	
	public abstract boolean isStepsVisualizationUsed();
	public abstract void setStepsVisualizationUsed(boolean steps_visualization_used);
	
	public abstract void setSubmissionAction(String submissionAction);
	public abstract String getSubmissionAction();
}