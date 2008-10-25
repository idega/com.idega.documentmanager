package com.idega.documentmanager.business.component;

import java.util.List;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 * 
 *          Last modified: $Date: 2008/10/25 18:30:19 $ by $Author: civilis $
 */
public interface Container extends Component {

	/**
	 * 
	 * @param componentId
	 * @return component which is contained in this container
	 */
	public abstract Component getComponent(String componentId);

	public abstract List<String> getContainedComponentsIds();

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
	public abstract Component addComponent(String componentType,
			String nextSiblingId);

	/**
	 * rearranges components in xform document regarding the order of the list
	 * retrieved using getContainedComponentsIds() method
	 */
	public abstract void rearrangeComponents();
}