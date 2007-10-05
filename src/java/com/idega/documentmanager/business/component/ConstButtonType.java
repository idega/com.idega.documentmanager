package com.idega.documentmanager.business.component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 */
public final class ConstButtonType {

	public static final String previous_page_button = "fbcomp_button_previous";
	public static final String next_page_button = "fbcomp_button_next";
	public static final String reset_form_button = "fbcomp_button_reset";
	public static final String submit_form_button = "fbcomp_button_submit";
	private static List<String> button_types = new ArrayList<String>();
	
	private String button_type;
	
	static {
		button_types.add(previous_page_button);
		button_types.add(next_page_button);
//		button_types.add(reset_form_button);
		button_types.add(submit_form_button);
	}
	
	public ConstButtonType(String button_type) {
		
		if(!button_types.contains(button_type))
			throw new NullPointerException("Provided button type not supported: "+button_type);
		
		this.button_type = button_type;
	}
	
	public String getButtonType() {
		return button_type;
	}
	
	public static List<String> getButtonTypes() {
		return button_types;
	}
	
	@Override
	public String toString() {
		return "button type set: "+getButtonType();
	}
}