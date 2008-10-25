package com.idega.documentmanager.component.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
 * @version $Revision: 1.5 $
 *
 * Last modified: $Date: 2008/10/25 18:30:18 $ by $Author: civilis $
 */
public class FormComponentContainerImpl extends FormComponentImpl implements FormComponentContainer, Container {
	
	protected List<String> containedComponentsIdsSequence;
	protected Map<String, FormComponent> containedComponents;

	public void loadContainerComponents() {
		
		final List<String[]> componentsTagNamesAndIds = getXFormsManager().getContainedComponentsTagNamesAndIds(this);
		final List<String> componentsIds = getContainedComponentsIds();
		
		final FormComponentFactory componentsFactory = FormComponentFactory.getInstance();
		
		for (String[] ctnaid : componentsTagNamesAndIds) {
			
			FormComponent component = componentsFactory.getFormComponentByType(ctnaid[0]);
			component.setFormDocument(getFormDocument());
//			component.setContext(getContext());
			String componentId = ctnaid[1];
			
			component.setId(componentId);
			component.setParent(this);
//			component.setLoad(true);
			componentsIds.add(componentId);
			getContainedComponents().put(componentId, component);
			
//			component.render();
			component.load();
		}
	}
	
	@Override
	public XFormsManagerContainer getXFormsManager() {
		
		return getFormDocument().getContext().getXformsManagerFactory().getXformsManagerContainer();
	}
	
	public List<String> getContainedComponentsIds() {
		
		if(containedComponentsIdsSequence == null)
			containedComponentsIdsSequence = new LinkedList<String>();
		
		return containedComponentsIdsSequence;
	}
	
	public FormComponent getContainedComponent(String componentId) {
		
		final FormComponent component;
		
		if(!getContainedComponents().containsKey(componentId)) {
		
			component = null;
			Logger.getLogger(getClass().getName()).log(Level.WARNING, "No component found by id = "+componentId+" in the contrainer (id="+getId()+", form id = "+getFormDocument().getFormId());
		} else
			component = getContainedComponents().get(componentId);
		
		return component;
	}
	
	protected Map<String, FormComponent> getContainedComponents() {
		
		if(containedComponents == null)
			containedComponents = new HashMap<String, FormComponent>();
		
		return containedComponents;
	}

	public Component addComponent(String componentType, String nextSiblingId) {
		return (Component)addFormComponent(componentType, nextSiblingId);
	}
	
	public FormComponent addFormComponent(String componentType, String nextSiblingId) {
		
		FormComponent component = FormComponentFactory.getInstance().getFormComponentByType(componentType);
//		component.setContext(getContext());
		component.setFormDocument(getFormDocument());

		if(nextSiblingId != null) {
			
			FormComponent nextSibling = getContainedComponent(nextSiblingId);
			
			if(nextSibling == null)
				Logger.getLogger(getClass().getName()).log(Level.WARNING, "The next sibling component not found by id provided = "+nextSiblingId+", appending to the end of children. Container id = "+getId()+" and form id = "+getFormDocument().getFormId());
			
			component.setNextSibling(nextSibling);
		}
		
		String generatedComponentId = getFormDocument().generateNewComponentId();
		component.setId(generatedComponentId);
		component.setParent(this);
//		component.setLoad(false);
//		component.render();
		component.create();
		
		getContainedComponents().put(generatedComponentId, component);
		
		return component;
	}

	public Component getComponent(String componentId) {
		return (Component)getContainedComponent(componentId);
	}
	
//	public void tellComponentId(String componentId) {
//		getParent().tellComponentId(componentId);
//	}
	
	public void rearrangeComponents() {
		
		final List<String> containedComponentsIds = getContainedComponentsIds();
		int size = containedComponentsIds.size();
		Map<String, FormComponent> containedComponents = getContainedComponents();
		
		for (int i = size-1; i >= 0; i--) {
			
			String componentId = containedComponentsIds.get(i);
			
			if(containedComponents.containsKey(componentId)) {
				
				FormComponent comp = containedComponents.get(componentId);
				
				if(i != size-1) {
					
					comp.setNextSiblingRerender(
						containedComponents.get(
								containedComponentsIds.get(i+1)
						)
					);
				} else
					comp.setNextSiblingRerender(null);
				
			} else
				throw new NullPointerException("Component, which id was provided in list was not found. Provided: "+componentId);
		}
	}
	
	public void componentsOrderChanged() { 	}
	
	@Override
	protected void changeBindNames() { }
	
	@Override
	public void load() {
		super.load();
		loadContainerComponents();
	}
//	@Override
//	public void render() {
//		
//		boolean load = this.load;
//		super.render();
//		
//		if(load)
//			loadContainerComponents();
//	}
	
	@Override
	public Element getHtmlRepresentation(Locale locale) throws Exception {
		Logger.getLogger(getClass().getName()).log(Level.WARNING, "Called getHtmlRepresentation on container component. Container doesn't have representation. Container id = "+getId()+" and form id = "+getFormDocument().getFormId());
		return null;
	}
	@Override
	public PropertiesComponent getProperties() {
		return null;
	}
	
	public void unregisterComponent(String componentId) {
		
		getContainedComponents().remove(componentId);
		getContainedComponentsIds().remove(componentId);
	}

	/**
	 * adds children components to the confirmation page
	 */
	@Override
	public void addToConfirmationPage() {
		
		List<String> ids = getContainedComponentsIds();
		
		for (int i = ids.size()-1; i >= 0; i--)
			getContainedComponents().get(ids.get(i)).addToConfirmationPage();
	}
	
	public void clear() {
		getContainedComponentsIds().clear();
		getContainedComponents().clear();
		super.clear();
	}
	
	/**
	 * removes all children components first, then removes itself
	 */
	@Override
	public void remove() {

		List<String> containedComps = new ArrayList<String>(getContainedComponentsIds());
		for (String cid : containedComps)
			getComponent(cid).remove();
		
		super.remove();
	}
	
	public FormComponentPage getParentPage() {
		
		FormComponentContainer parent = getParent();
		
		FormComponentPage page = null;
		
		while(page == null) {
		
			if(parent instanceof FormComponentPage)
				page = (FormComponentPage)parent;
			else
				parent = parent.getParent();
		}
		
		return page;
	}
	
//	@Override
//	public boolean isReadonly() {
//		throw new UnsupportedOperationException("Not implemented yet");
//	}
//	
//	@Override
//	public void setReadonly(boolean readonly) {
//
//		for (FormComponent component : getContainedComponents().values())
//			component.setReadonly(readonly);
//	}
}