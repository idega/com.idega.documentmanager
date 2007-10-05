package com.idega.documentmanager.component;

import java.util.List;

import com.idega.documentmanager.business.component.Component;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 */
public interface FormComponentContainer extends FormComponent {

	public abstract FormComponent getContainedComponent(String component_id);
	
	public abstract Component addComponent(String component_type, String component_after_this_id) throws NullPointerException;

	public abstract Component getComponent(String component_id);
	
	public abstract void tellComponentId(String component_id);
	
	public abstract void rearrangeComponents();
	
	public abstract List<String> getContainedComponentsIdList();
	
	public abstract void componentsOrderChanged();
	
	public abstract void unregisterComponent(String component_id);
	
	public abstract FormComponentPage getParentPage();
}