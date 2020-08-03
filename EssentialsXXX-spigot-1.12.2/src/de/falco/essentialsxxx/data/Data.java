package de.falco.essentialsxxx.data;

import java.util.Map;


public class Data {
	
	private Map<String,String> index;
	
	public Data(Map<String,String> index) {
		this.index = index;
	}
	
	
	public Map<String, String> getFields() {
		return index;
	}
	
	public boolean isSetUserName() {
		
		return false;
		
	}
	

}
