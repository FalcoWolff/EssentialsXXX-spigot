package de.falco.essentialsxxx.data;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class DataManager {
	
	private static Map<String,DataListener> listener = new LinkedHashMap<>();
	
	public static Map<String,String> sendDataRequest(UUID uuid) {
		
		Map<String,String> data = new LinkedHashMap<>();
		DataRequestEvent e = new DataRequestEvent(uuid);
		
		for(String key : listener.keySet()) {
			DataListener d = listener.get(key);
			String u = d.onDataRequest(e);
			data.put(key, u);
		}
		
		return data;
		
	}
	
	public static void registerDataListener(String field, DataListener data) {
		
		if(isregisteredDataListener(data) || isregisteredDataListener(field)) {
			throw new IllegalArgumentException("DataListener is registered!");
		}
		
		listener.put(field, data);
		
	}
	
	public static void unregisterDataListener(String field) {
		
		if(isregisteredDataListener(field)) {
			listener.remove(field);
		}
		
	}
	
	public static void unregisterDataListener(DataListener data) {
		
		if(isregisteredDataListener(data)) {
			for(String key : listener.keySet()) {
				if(listener.get(key) == data) {
					listener.remove(key);
					return;
				}
			}
		}
		
	}
	
	public static boolean isregisteredDataListener(String field) {
		
		return listener.containsKey(field);
		
	}
	
	public static boolean isregisteredDataListener(DataListener data) {
		
		return listener.values().contains(data);
		
	}

}
