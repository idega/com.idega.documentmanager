package com.idega.documentmanager.component;

import com.idega.documentmanager.business.component.properties.PropertiesComponent;
import com.idega.documentmanager.component.beans.ComponentDataBean;
import com.idega.documentmanager.component.properties.impl.ConstUpdateType;
import com.idega.documentmanager.context.DMContext;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.7 $
 *
 * Last modified: $Date: 2008/10/03 07:41:15 $ by $Author: arunas $
 */
public interface FormComponent {

	public abstract void render();

	public abstract void setComponentAfterThis(FormComponent component);
	
	public abstract FormComponent getComponentAfterThis();
	
	public abstract void setComponentAfterThisRerender(FormComponent component);

	public abstract String getId();

	public abstract void setId(String id);

	public abstract void setType(String type);
	
	public abstract String getType();

	public abstract PropertiesComponent getProperties();
	
	public abstract void remove();
	
	public abstract void setLoad(boolean load);
	
	public abstract void setParent(FormComponentContainer parent);
	
	public abstract void setFormDocument(FormDocument form_document);
	
	public abstract void addToConfirmationPage();
	
	public abstract FormComponentContainer getParent();
	
	public abstract void update(ConstUpdateType what);
	
	public abstract ComponentDataBean getXformsComponentDataBean();

	public abstract void setXformsComponentDataBean(ComponentDataBean xformsComponentDataBean);
	
	public abstract FormDocument getFormDocument();
	
	public abstract DMContext getContext();

	public abstract void setContext(DMContext context);
	
	public abstract void setReadonly(boolean readonly);
	
	public abstract boolean isReadonly();
	
	public abstract void setPdfForm(boolean generatePdf);
	
	public abstract boolean isPdfForm();
}