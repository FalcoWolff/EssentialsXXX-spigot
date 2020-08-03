package de.falco.essentialsxxx.data;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public interface DataListener {
	
	public abstract String onDataRequest(DataRequestEvent event);

	@SuppressWarnings("deprecation")
	default boolean isPlayerRegistered(String playername) {
		
		Player p = Bukkit.getPlayer(playername);
		
		OfflinePlayer offline = null;
		
		
		//if p ist not online set offline Object
		if(p == null) {
			
			offline = Bukkit.getOfflinePlayer(playername);
			
			//player never join
			if(offline == null) {
				return false;
			}
			
				
		}
		
		return true;
		
	}
	
	default boolean isPlayerRegistered(UUID uuid) {
		
		Player p = Bukkit.getPlayer(uuid);
		
		
		OfflinePlayer offline = null;
		
		
		//if p ist not online set offline Object
		if(p == null) {
			
			offline = Bukkit.getOfflinePlayer(uuid);
			
			//player never join
			if(offline == null) {
				return false;
			}
			
				
		}
		
		return true;
		
	}
	
}
