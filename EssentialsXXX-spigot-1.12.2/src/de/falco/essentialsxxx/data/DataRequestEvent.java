package de.falco.essentialsxxx.data;

import java.util.UUID;

public class DataRequestEvent {
	
	private UUID uuid;
	private String pname;
	
	public DataRequestEvent() {
		
	}
	
	public DataRequestEvent(UUID uuid, String pname) {
		this.uuid = uuid;
		this.pname = pname;
	}
	
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
	public UUID getUuid() {
		return uuid;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getPname() {
		return pname;
	}

}
