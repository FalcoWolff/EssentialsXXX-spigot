package de.falco.essentialsxxx.id;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserProfile {
	
	private UUID uuid;
	private String firstname;
	private Map<Long,String> names = new LinkedHashMap<>();

	public UserProfile(UUID uuid, String firstname) {
		this(uuid,firstname,new LinkedHashMap<Long,String>());
	}
	
	public UserProfile(UUID uuid, String firstname, Map<Long,String> names) {
		this.uuid = uuid;
		this.firstname = firstname;
		this.names = names;
	}
	
	public String toString() {
		
		StringBuilder buffer = new StringBuilder();
		buffer.append("[");
		
		for(Long t : names.keySet()) {
			buffer.append("{" + t + ":" + names.get(t) + "}");
		}
		
		buffer.append("]");
		
		String tmp = this.getClass().getName() + "[uuid=" + uuid + ",firstname=" + firstname + ",names=" + buffer + "]";
		
		return tmp;
		
	}
	
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
	
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	public void setNames(Map<Long, String> names) {
		this.names = names;
	}
	
	public void addPair(Long time, String username) {
		
		if(hasTime(time) == false) {
			names.put(time, username);
		}
		
	}
	
	private List<Long> sortTime() {
		List<Long> list = names.keySet().stream().collect(Collectors.toList());
		
		Collections.sort(list);
		return list;
	}
	
	public boolean hasTime(long time) {
		return names.keySet().contains(time);
	}

	public boolean hasUserName(String username) {
		return names.values().contains(username);
 	}
	
	public long getTimeByUserName(String username) {
		
		if(hasUserName(username)) {
			
			for(Long tmp : names.keySet()) {
				if(names.get(tmp).equals(username)) {
					return (long) tmp;
				}
			}
			
		}
		
		
		
		return -1;
		
	}
	
	public String getUserNamebyTime(long time) {
		
		List<Long> list = sortTime();
		
		if(list.size() == 0) {
			return firstname;    
		}
		
		if(time < list.get(0)) {
			return firstname;
		}
		
		String name = null;
		
		for(int x = list.size() - 1; x >= 0; x++) {
			
			if(time > list.get(x)) {
				name = names.get(list.get(x));
			}
			
		}
		
		return name;
		
		/*
		for(Long tmp : list) {
			if(time > tmp) {
				name = names.get(tmp);
			}
		}
		*/
		
		
		
	}
	
	
	public UUID getUuid() {
		return uuid;
	}
	
	
	public String getFirstname() {
		return firstname;
	}
	
	
	public String getLastname() {
		
		List<Long> list = sortTime();
		
		
		try {
			String re =  names.get(list.get(list.size() - 1));
			return re;
		}catch(IndexOutOfBoundsException ex) {
			return firstname;
		}
		
	}

}
