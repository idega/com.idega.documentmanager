package com.idega.documentmanager.business.form.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.idega.documentmanager.business.form.ConstButtonType;
import com.idega.documentmanager.business.form.manager.util.FormManagerUtil;
import com.idega.repository.data.Singleton;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 */
public class FormComponentFactory implements Singleton {
	
	private static FormComponentFactory me;
	
	private Map<String, List<String>> components_tags_classified;
	private static final String type_simple = "type_simple";
	private static final String type_select = "type_select";
	private static final String type_non_display = "type_non_display";
	private static final String type_plain = "type_plain";
	public static final String page_type_tag = FormManagerUtil.case_tag;
	public static final String page_type = "fbcomp_page";
	public static final String confirmation_page_type = "fbcomp_confirmation_page";
	public static final String button_type = FormManagerUtil.trigger_tag;
	public static final String fbcomp_button_area = "fbcomp_button_area";
	public static final String page_type_thx = "thx_page";
	
	private FormComponentFactory() { 
		
		components_tags_classified = new HashMap<String, List<String>>();
		
		List<String> types = new ArrayList<String>();
		types.add("fbcomp_text");
		types.add("fbcomp_textarea");
		types.add("fbcomp_secret");
		types.add("fbcomp_email");
		types.add("fbcomp_upload_file");
		types.add("xf:input");
		types.add("xf:secret");
		types.add("xf:textarea");
		components_tags_classified.put(type_simple, types);
		
		List<String> non_display_types = new ArrayList<String>();
		non_display_types.add(fbcomp_button_area);
		non_display_types.add(page_type);
		non_display_types.add(confirmation_page_type);
		components_tags_classified.put(type_non_display, non_display_types);
		
		types = new ArrayList<String>();
		
		types.add("fbcomp_multiple_select_minimal");
		types.add("xf:select");
		types.add("fbcomp_single_select_minimal");
		types.add("xf:select1");
		types.add("fbcomp_multiple_select");
		types.add("fbcomp_single_select");
		
		components_tags_classified.put(type_select, types);
		
		types = new ArrayList<String>();
		
		types.add("fbcomp_simple_text");
		types.add("fbcomp_header_text");
		types.add("fbcomp_separator");
		
		components_tags_classified.put(type_plain, types);
	}
	
	public static FormComponentFactory getInstance() {
		
		me = null;
		if (me == null) {
			
			synchronized (FormComponentFactory.class) {
				if (me == null) {
					me = new FormComponentFactory();
				}
			}
		}

		return me;
	}
	
	public IFormComponent getFormComponentByType(String component_type) {
		
		IFormComponent component = recognizeFormComponent(component_type);
		component.setType(component_type);
		
		return component;
	}
	
	public IFormComponent recognizeFormComponent(String component_type) {
		
		if(components_tags_classified.get(type_select).contains(component_type))
			return new FormComponentSelect();
		if(component_type.equals(page_type_thx))
			return new FormComponentThankYouPage();
		if(component_type.equals(page_type_tag) || component_type.equals(page_type) || component_type.equals(confirmation_page_type))
			return new FormComponentPage();
		if(component_type.equals(fbcomp_button_area))
			return new FormComponentButtonArea();
		if(component_type.equals(button_type) || ConstButtonType.getButtonTypes().contains(component_type))
			return new FormComponentButton();
		if(components_tags_classified.get(type_plain).contains(component_type))
			return new FormComponentPlain();
		
		return new FormComponent();
	}
	
	public boolean isNormalFormElement(IFormComponent form_component) {
		
		String type = form_component.getType();
		return 
			components_tags_classified.get(type_select).contains(type) ||
			components_tags_classified.get(type_simple).contains(type);
	}
	
	public void filterNonDisplayComponents(List<String> all_components_types) {
		
		List<String> non_disp_types = components_tags_classified.get(type_non_display);
		
		for (Iterator<String> iter = all_components_types.iterator(); iter.hasNext();) {
			
			if(non_disp_types.contains(iter.next()))
				iter.remove();
		}
	}
}