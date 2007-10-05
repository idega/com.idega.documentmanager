package com.idega.documentmanager.component.beans;

import java.io.Serializable;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 */
public class ItemBean implements Serializable {
	
	private static final long serialVersionUID = -1462694199485788168L;
	
	String label;
	String value;
	
	public ItemBean() {
		
	}
	
	public ItemBean(String value, String label) {
		this.value = value;
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		
		return "label: "+label+" value: "+value;
	}
}
