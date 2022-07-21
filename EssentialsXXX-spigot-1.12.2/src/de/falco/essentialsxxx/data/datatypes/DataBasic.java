package de.falco.essentialsxxx.data.datatypes;

import java.util.LinkedHashMap;
import java.util.Map;

public class DataBasic {
	
	private Map<String,String> fields = new LinkedHashMap<>();
	
	public DataBasic() {
	}
	
	public Map<String, String> getFields() {
		return fields;
	}
	public void setFields(Map<String, String> fields) {
		this.fields = fields;
	}
	
	public void addField(String key, String value) {
		fields.put(key, value);
	}

}
