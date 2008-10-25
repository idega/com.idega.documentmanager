package com.idega.documentmanager.component;

import com.idega.documentmanager.business.component.properties.PropertiesComponent;
import com.idega.documentmanager.component.beans.ComponentDataBean;
import com.idega.documentmanager.component.impl.FormComponentContainerImpl;
import com.idega.documentmanager.component.properties.impl.ConstUpdateType;

/**
 * this interface represents "internal to the xform document manager" form
 * component representation. Shouldn't be used outside the xform document
 * manager
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.8 $
 * 
 *          Last modified: $Date: 2008/10/25 18:30:19 $ by $Author: civilis $
 */
public interface FormComponent {

	/**
	 * creates xforms component and adds to the xform document. The sufficient
	 * information should be already added to this component (like parent
	 * container)
	 * 
	 * @see FormComponentContainerImpl addComponent method for example
	 */
	public abstract void create();

	/**
	 * loads xforms component from the xform document. The sufficient
	 * information should be already added to this component (like parent
	 * container)
	 * 
	 * @see FormComponentContainerImpl loadContainerComponents method for
	 *      example
	 */
	public abstract void load();

//	public abstract void render();

	/**
	 * specifies the component, before which this component should go in the
	 * xforms document
	 * 
	 * @param component
	 *            sibling component
	 */
	public abstract void setNextSibling(FormComponent component);

	public abstract FormComponent getNextSibling();

	/**
	 * moves this component to new location before next sibling specified
	 * @param nextSibling
	 */
	public abstract void setNextSiblingRerender(FormComponent nextSibling);

	public abstract String getId();

	public abstract void setId(String id);

	public abstract void setType(String type);

	public abstract String getType();

	public abstract PropertiesComponent getProperties();

	public abstract void remove();

	// public abstract void setLoad(boolean load);

	public abstract void setParent(FormComponentContainer parent);

	public abstract void setFormDocument(FormDocument form_document);

	/**
	 * performs adding component to the confirmation page
	 */
	public abstract void addToConfirmationPage();

	public abstract FormComponentContainer getParent();

	public abstract void update(ConstUpdateType what);

	public abstract ComponentDataBean getXformsComponentDataBean();

	public abstract void setXformsComponentDataBean(
			ComponentDataBean xformsComponentDataBean);

	public abstract FormDocument getFormDocument();

//	public abstract DMContext getContext();
//
//	public abstract void setContext(DMContext context);

	// public abstract void setReadonly(boolean readonly);
	//	
	// public abstract boolean isReadonly();

//	public abstract void setPdfForm(boolean generatePdf);
//
//	public abstract boolean isPdfForm();
}