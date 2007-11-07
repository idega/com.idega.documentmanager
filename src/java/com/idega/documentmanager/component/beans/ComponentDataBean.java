package com.idega.documentmanager.component.beans;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.w3c.dom.Element;

import com.idega.documentmanager.xform.Bind;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2007/11/07 15:02:29 $ by $Author: civilis $
 */
public class ComponentDataBean implements Cloneable {
	
	private Element element;
	private Bind bind;
	private Element previewElement;
	private Element keyExtInstance;
	private Element keySetvalue;
	
	private Element unlocalizedHtmlComponent;
	private Map<Locale, Element> localizedHtmlComponents;
	
	public Element getPreviewElement() {
		return previewElement;
	}
	public void setPreviewElement(Element preview_element) {
		this.previewElement = preview_element;
	}
	public Bind getBind() {
		return bind;
	}
	public void setBind(Bind bind) {
		this.bind = bind;
	}
	public Element getElement() {
		return element;
	}
	public void setElement(Element element) {
		this.element = element;
	}
	
	@Override
	public Object clone() {
		
		ComponentDataBean clone = getDataBeanInstance();
		
		if(getElement() != null)
			clone.setElement((Element)getElement().cloneNode(true));
		
		if(getBind() != null)
			clone.setBind(getBind().clone());
		
		if(getPreviewElement() != null)
			clone.setPreviewElement((Element)getPreviewElement().cloneNode(true));
		
		if(getKeyExtInstance() != null)
			clone.setKeyExtInstance((Element)getKeyExtInstance().cloneNode(true));
		
		if(getKeySetvalue() != null)
			clone.setKeySetvalue((Element)getKeySetvalue().cloneNode(true));
		
		return clone;
	}
	
	protected ComponentDataBean getDataBeanInstance() {
		
		return new ComponentDataBean();
	}
	public Element getKeyExtInstance() {
		return keyExtInstance;
	}
	public void setKeyExtInstance(Element key_ext_instance) {
		this.keyExtInstance = key_ext_instance;
	}
	public Element getKeySetvalue() {
		return keySetvalue;
	}
	public void setKeySetvalue(Element key_setvalue) {
		this.keySetvalue = key_setvalue;
	}
	public Element getUnlocalizedHtmlComponent() {
		return unlocalizedHtmlComponent;
	}
	public void setUnlocalizedHtmlComponent(Element unlocalizedHtmlComponent) {
		this.unlocalizedHtmlComponent = unlocalizedHtmlComponent;
	}
	public Map<Locale, Element> getLocalizedHtmlComponents() {
		
		if(localizedHtmlComponents == null)
			localizedHtmlComponents = new HashMap<Locale, Element>();
		
		return localizedHtmlComponents;
	}
	public void setLocalizedHtmlComponents(
			Map<Locale, Element> localizedHtmlComponents) {
		this.localizedHtmlComponents = localizedHtmlComponents;
	}
}