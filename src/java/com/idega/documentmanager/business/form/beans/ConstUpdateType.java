package com.idega.documentmanager.business.form.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 */
public final class ConstUpdateType {

	public static final int label = 1;
	public static final int constraint_required = 2;
	public static final int error_msg = 3;
	public static final int p3p_type = 4;
	public static final int data_src_used = 5;
	public static final int itemset = 6;
	public static final int empty_element_label = 7;
	public static final int external_data_src = 8;
	public static final int thankyou_text = 9;
	public static final int autofill_key = 10;
	public static final int text = 11;
	
	private static List<Integer> update_types = new ArrayList<Integer>();
	
	private Integer update_type;
	
	static {
		update_types.add(label);
		update_types.add(constraint_required);
		update_types.add(error_msg);
		update_types.add(p3p_type);
		update_types.add(data_src_used);
		update_types.add(itemset);
		update_types.add(empty_element_label);
		update_types.add(external_data_src);
		update_types.add(thankyou_text);
		update_types.add(autofill_key);
		update_types.add(text);
	}
	
	public ConstUpdateType(Integer update_type) {
		
		if(!update_types.contains(update_type))
			throw new NullPointerException("Provided update type not supported: "+update_type);
		
		this.update_type = update_type;
	}
	
	public Integer getUpdateType() {
		return update_type;
	}
	
	public static List<Integer> getUpdateTypes() {
		return update_types;
	}
	
	@Override
	public String toString() {
		return "update type set: "+update_type;
	}
}