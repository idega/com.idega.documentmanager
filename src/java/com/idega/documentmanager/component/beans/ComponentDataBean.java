package com.idega.documentmanager.component.beans;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.w3c.dom.Element;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2007/10/05 12:27:16 $ by $Author: civilis $
 */
public class ComponentDataBean implements Cloneable {
	
	private Element element;
	private Element bind;
	private Element nodeset;
	private Element preview_element;
	private Element key_ext_instance;
	private Element key_setvalue;
	
	private Element unlocalizedHtmlComponent;
	private Map<Locale, Element> localizedHtmlComponents;
	
	public Element getPreviewElement() {
		return preview_element;
	}
	public void setPreviewElement(Element preview_element) {
		this.preview_element = preview_element;
	}
	public Element getBind() {
		return bind;
	}
	public void setBind(Element bind) {
		this.bind = bind;
	}
	public Element getElement() {
		return element;
	}
	public void setElement(Element element) {
		this.element = element;
	}
	public Element getNodeset() {
		return nodeset;
	}
	public void setNodeset(Element nodeset) {
		this.nodeset = nodeset;
	}
	
	public Object clone() {
		
		ComponentDataBean clone = getDataBeanInstance();
		
		if(getElement() != null)
			clone.setElement((Element)getElement().cloneNode(true));
		
		if(getBind() != null)
			clone.setBind((Element)getBind().cloneNode(true));
		
		if(getNodeset() != null)
			clone.setNodeset((Element)getNodeset().cloneNode(true));
		
		if(getPreviewElement() != null)
			clone.setNodeset((Element)getPreviewElement().cloneNode(true));
		
		if(getKeyExtInstance() != null)
			clone.setNodeset((Element)getKeyExtInstance().cloneNode(true));
		
		if(getKeySetvalue() != null)
			clone.setNodeset((Element)getKeySetvalue().cloneNode(true));
		
		return clone;
	}
	
	protected ComponentDataBean getDataBeanInstance() {
		
		return new ComponentDataBean();
	}
	public Element getKeyExtInstance() {
		return key_ext_instance;
	}
	public void setKeyExtInstance(Element key_ext_instance) {
		this.key_ext_instance = key_ext_instance;
	}
	public Element getKeySetvalue() {
		return key_setvalue;
	}
	public void setKeySetvalue(Element key_setvalue) {
		this.key_setvalue = key_setvalue;
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