package de.falco.essentialsxxx.data;

import java.util.UUID;

public class DataRequestEvent {
	
	private UUID uuid;
	
	public DataRequestEvent() {
		
	}
	
	public DataRequestEvent(UUID uuid) {
		this.uuid = uuid;
	}
	
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
	public UUID getUuid() {
		return uuid;
	}

}
