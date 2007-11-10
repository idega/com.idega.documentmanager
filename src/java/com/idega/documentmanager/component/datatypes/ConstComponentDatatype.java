package com.idega.documentmanager.component.datatypes;

import java.util.ArrayList;
import java.util.List;

public class ConstComponentDatatype {
	
	public static final String STRING = "string";
	public static final String LIST = "list";
	public static final String FILE = "file";
	private static List<String> components_datatypes = new ArrayList<String>();
	
	private String datatype;
	
	static {
		components_datatypes.add(STRING);
		components_datatypes.add(LIST);
		components_datatypes.add(FILE);
	}
	
	public ConstComponentDatatype(String datatype) {
		
		if(!components_datatypes.contains(datatype))
			throw new NullPointerException("Provided datatype not supported: " + datatype);
		
		this.datatype = datatype;
	}
	
	public String getDatatype() {
		return datatype;
	}


	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}


	@Override
	public String toString() {
		return "components category set: " + getDatatype();
	}

}
