package com.idega.documentmanager.component;

import java.util.List;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.3 $
 * 
 *          Last modified: $Date: 2008/10/25 18:30:19 $ by $Author: civilis $
 */
public interface FormComponentContainer extends FormComponent {

	/**
	 * 
	 * @param componentId
	 * @return component which is contained in this container
	 */
	public abstract FormComponent getContainedComponent(String componentId);

	/**
	 * add child component from the component type
	 * 
	 * @param componentType
	 * @param nextSiblingId
	 *            the sibling that should follow the inserted component. In
	 *            other words, insert before this sibling. If not specified, the
	 *            component will be appended to the children
	 * @return created component
	 */
	public abstract FormComponent addFormComponent(String componentType,
			String nextSiblingId);

	// public abstract void tellComponentId(String component_id);

	// public abstract void rearrangeComponents();

	public abstract List<String> getContainedComponentsIds();

	public abstract void componentsOrderChanged();

	/**
	 * unregisters component from the container children list. This should be
	 * called by the form component itself on remove() method
	 * 
	 * @param componentId
	 */
	public abstract void unregisterComponent(String componentId);

	public abstract FormComponentPage getParentPage();
}