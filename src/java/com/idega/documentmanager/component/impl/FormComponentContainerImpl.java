package com.idega.documentmanager.component.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Element;

import com.idega.documentmanager.business.component.Component;
import com.idega.documentmanager.business.component.Container;
import com.idega.documentmanager.business.component.properties.PropertiesComponent;
import com.idega.documentmanager.component.FormComponent;
import com.idega.documentmanager.component.FormComponentContainer;
import com.idega.documentmanager.component.FormComponentPage;
import com.idega.documentmanager.manager.XFormsManagerContainer;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.3 $
 *
 * Last modified: $Date: 2007/12/06 20:31:30 $ by $Author: civilis $
 */
public class FormComponentContainerImpl extends FormComponentImpl implements FormComponentContainer, Container {
	
	protected List<String> contained_components_id_sequence;
	protected Map<String, FormComponent> contained_components;

	public void loadContainerComponents() {
		
		List<String[]> components_tag_names_and_ids = getXFormsManager().getContainedComponentsTagNamesAndIds(this);
		
		FormComponentFactory components_factory = FormComponentFactory.getInstance();
		
		List<String> components_id_list = getContainedComponentsIdList();
		
		for (String[] ctnaid : components_tag_names_and_ids) {
			
			FormComponent component = components_factory.getFormComponentByType(ctnaid[0]);
			component.setFormDocument(getFormDocument());
			component.setContext(getContext());
			String component_id = ctnaid[1];
			
			component.setId(component_id);
			component.setParent(this);
			component.setLoad(true);
			components_id_list.add(component_id);
			getContainedComponents().put(component_id, component);
			
			component.render();
		}
	}
	
	@Override
	public XFormsManagerContainer getXFormsManager() {
		
		return getContext().getXformsManagerFactory().getXformsManagerContainer();
	}
	
	public List<String> getContainedComponentsIdList() {
		
		if(contained_components_id_sequence == null)
			contained_components_id_sequence = new LinkedList<String>();
		
		return contained_components_id_sequence;
	}
	
	public FormComponent getContainedComponent(String component_id) {
		
		return getContainedComponents().get(component_id);
	}
	
	protected Map<String, FormComponent> getContainedComponents() {
		
		if(contained_components == null)
			contained_components = new HashMap<String, FormComponent>();
		
		return contained_components;
	}

	public Component addComponent(String component_type, String component_after_this_id) throws NullPointerException {
		
		FormComponent component = FormComponentFactory.getInstance().getFormComponentByType(component_type);
		component.setContext(getContext());
		component.setFormDocument(getFormDocument());

		if(component_after_this_id != null) {
			
			FormComponent comp_after_new = getContainedComponent(component_after_this_id);
			
			if(comp_after_new == null)
				throw new NullPointerException("Component after not found");
			
			component.setComponentAfterThis(comp_after_new);
		}
		
		String component_id = getFormDocument().generateNewComponentId();
		component.setId(component_id);
		component.setParent(this);
		component.setLoad(false);
		component.render();
		
		getContainedComponents().put(component_id, component);
		
		return (Component)component;
	}

	public Component getComponent(String component_id) {
		
		if(!getContainedComponents().containsKey(component_id))
			throw new NullPointerException("Component was not found in the container by provided id: "+component_id);
		
		return (Component)getContainedComponents().get(component_id);
	}
	
	public void tellComponentId(String component_id) {
		parent.tellComponentId(component_id);
	}
	
	public void rearrangeComponents() {
		
		List<String> contained_components_id_list = getContainedComponentsIdList();
		int size = contained_components_id_list.size();
		Map<String, FormComponent> contained_components = getContainedComponents();
		
		for (int i = size-1; i >= 0; i--) {
			
			String component_id = contained_components_id_list.get(i);
			
			if(contained_components.containsKey(component_id)) {
				
				FormComponent comp = contained_components.get(component_id);
				
				if(i != size-1) {
					
					comp.setComponentAfterThisRerender(
						contained_components.get(
								contained_components_id_list.get(i+1)
						)
					);
				} else
					comp.setComponentAfterThisRerender(null);
				
			} else
				throw new NullPointerException("Component, which id was provided in list was not found. Provided: "+component_id);
		}
	}
	
	public void componentsOrderChanged() { 	}
	
	@Override
	protected void changeBindNames() { }
	
	@Override
	public void render() {
		
		boolean load = this.load;
		super.render();
		
		if(load)
			loadContainerComponents();
	}
	
	@Override
	public Element getHtmlRepresentation(Locale locale) throws Exception {
		throw new NullPointerException("Not available for this type of component");
	}
	@Override
	public PropertiesComponent getProperties() {
		return null;
	}
	
	public void unregisterComponent(String component_id) {
		
		getContainedComponents().remove(component_id);
		getContainedComponentsIdList().remove(component_id);
	}

	@Override
	public void addToConfirmationPage() {
		
		List<String> ids = getContainedComponentsIdList();
		
		for (int i = ids.size()-1; i >= 0; i--)
			getContainedComponents().get(ids.get(i)).addToConfirmationPage();
	}
	
	public void clear() {
		getContainedComponentsIdList().clear();
		getContainedComponents().clear();
		super.clear();
	}
	
	@Override
	public void remove() {

		List<String> contained_comps = new ArrayList<String>(getContainedComponentsIdList());
		for (String cid : contained_comps)
			getComponent(cid).remove();
		
		super.remove();
	}
	
	public FormComponentPage getParentPage() {
		
		FormComponentContainer parent = getParent();
		
		if(parent instanceof FormComponentPage)
			return (FormComponentPage)parent;
		
		return null;
	}
	
	@Override
	public boolean isReadonly() {
		throw new UnsupportedOperationException("Not implemented yet");
	}
	
	@Override
	public void setReadonly(boolean readonly) {

		for (Entry<String, FormComponent> componentEntry : getContainedComponents().entrySet())
			componentEntry.getValue().setReadonly(readonly);
	}
}