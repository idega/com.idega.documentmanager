package com.idega.documentmanager.business;

import java.util.List;

import org.chiba.xml.dom.DOMUtil;
import org.w3c.dom.Element;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 */
public class SubmittedDataBean {
	
	private String id;
	private Element submitted_data_element;
	
	String label1;
	String label2;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Element getSubmittedDataElement() {
		return submitted_data_element;
	}
	public void setSubmittedDataElement(Element submitted_data_element) {
		this.submitted_data_element = submitted_data_element;
	}
	
	public String getLabel1() {
		
		if(label1 != null)
			return label1;
		
		parseLabelValues();
		
		return label1;
	}
	
	public String getLabel2() {
		
		if(label2 != null)
			return label2;
		
		parseLabelValues();
		
		return label2;
	}
	
	public void parseLabelValues() {
		
		if(submitted_data_element == null) {
			label1 = "";
			label2 = "";
			return;
		}
		
		List<Element> child_elements = DOMUtil.getChildElements(submitted_data_element);
		
		int child_count = child_elements.size(); 
		
		if(child_count > 1) {
			
			label1 = DOMUtil.getTextNodeAsString(child_elements.get(1));
			
			if(child_count > 2) {
				label2 = DOMUtil.getTextNodeAsString(child_elements.get(2));
			} else {
				label2 = "";
			}
			
		} else {
			label1 = "";
			label2 = "";
		}
	}
}