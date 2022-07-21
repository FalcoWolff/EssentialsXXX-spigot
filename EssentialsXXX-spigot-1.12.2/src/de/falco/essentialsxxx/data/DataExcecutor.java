package de.falco.essentialsxxx.data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import de.falco.essentialsxxx.EssentialsXXX;
import de.falco.essentialsxxx.data.datatypes.DataGroup;
import de.falco.essentialsxxx.id.IdExcecutor;
import de.falco.essentialsxxx.id.NotregisteredException;
import de.falco.essentialsxxx.id.UserProfile;

public interface DataExcecutor extends IdExcecutor{
	
	
	/**
	 * create a field with variables for a player
	 * 
	 * @param playername
	 * @return Map<String,String> with the key: "player" and the value playername
	 */
	default Map<String,String> getData(String playername) {
		
		try {
			
			UserProfile u = getUserProfilebyUsername(playername);
			
			return getData(u.getUuid(),playername);
			
		} catch (NotregisteredException e) {
			
			e.printStackTrace();
			
			Map<String,String> end = new LinkedHashMap<>();
			end.put("player", playername);
			
			return end;
			
		}
		
	}
	
	/**
	 * create a field with variables for a player
	 * execute the method getData with the uuid an "unknown"
	 * 
	 * @param uuid
	 * @return list with keys and values. When player didn't join there will be only one pair key: "player" value: "unknown"
	 */
	default Map<String,String> getData(UUID uuid) {
		
		try {
			UserProfile u = getUserProfilebyuuid(uuid);
			return getData(uuid,u.getLastname());
			
		} catch (NotregisteredException e) {
			e.printStackTrace();
			return getData(uuid,"unknown");
		}
	}
	
	/**
	 * create a field with variables for a player
	 * 
	 * @param uuid
	 * @param pname
	 * @return list with keys and values. When player didn't join there will be only one pair key: "player" value: pname
	 */ 
	default Map<String,String> getData(UUID uuid, String pname) {
		
		EssentialsXXX main = EssentialsXXX.getEssentialsXXXmain();
		
		DataManager manager = main.getManager();
		
		Player p = Bukkit.getPlayer(uuid);
		
		OfflinePlayer offline = Bukkit.getOfflinePlayer(uuid);
		
		DataRequestEvent event = new DataRequestEvent(uuid, pname);
		
		Map<String,String> data = DataListenerManager.sendDataRequest(event);
		
		
		
		//if p ist not online set offline Object
		if(p == null) {
			
			offline = Bukkit.getOfflinePlayer(uuid);
			
			//player never join
			if(offline != null) {
				data.put("player", offline.getName());//set default fields
				//return null;
				//throw new NotregisteredException("[" + main.getName() + "] player with uuid '" + uuid + "' never joint");
			}else {
				data.put("player", pname);
			}
			
			
			data.put("uuid", uuid + "");
				
		}else {
			
			data.put("player", p.getName());//set default fields
			data.put("uuid", uuid + "");
			
		}
		
		
		
		//check the player section before the group section
		for(UUID playername : manager.getPlayers().keySet()) {
			
			if(!uuid.equals(playername)) {
				continue;
			}
			
			
			Map<String,String> d = manager.getPlayer(playername).getFields();
			
			for(String tmp : d.keySet()) {
				data.put(tmp, d.get(tmp));
			}
			
			return data;
			
		}
		
		//group section
		for(String groupname : manager.getGroups().keySet()) {
			
			DataGroup datagroup = manager.getGroup(groupname);
			
			String pexG = datagroup.getPex();
			
			if(offline == null) {
				
				if(pexG.equals("")) {
					
					Map<String,String> d = datagroup.getFields();
					
					for(String tmp : d.keySet()) {
						data.put(tmp, d.get(tmp));
					}
					
					return data;
					
				}
				
			}else if(p != null) {
				
			
				if(!p.hasPermission(pexG) && pexG.equals("") == false) {
					continue;
				}
				
				Map<String,String> d = datagroup.getFields();
				
				for(String tmp : d.keySet()) {
					data.put(tmp, d.get(tmp));
				}
				
				return data;
				
			}else {
				
				
				boolean test = EssentialsXXX.permission.playerHas(null, offline, pexG);
				
				if(test == true) {
					
					Map<String,String> d = datagroup.getFields();
					
					for(String tmp : d.keySet()) {
						data.put(tmp, d.get(tmp));
					}
					
					return data;
					
				}else {
					continue;
				}
				
			}
			
			
		}
		
		
		return data;
		
	}
	
	
	/**
	 * replace a message with fields from the method getData
	 * after this progress the method delete the rest fields
	 * 
	 * @param message
	 * @param data
	 * @param suffix
	 * @return
	 */
	default String changeMessage(String message, Map<String,String> data, String suffix) {
			
		if(message == null || data == null || suffix == null) {
			throw new IllegalArgumentException("parameter couldnt be null");
		}
	
		for(String i : data.keySet()) {
			
			message = message.replaceAll("§" + i + suffix, data.get(i));
				
		}
		
		message = deletePlaceholder(message,suffix);
		
		return message;
		
	}
	
	/**
	 * remove every word ending with suffix
	 * 
	 * @param message
	 * @param suffix
	 * @return
	 */
	default String deletePlaceholder(String message, String suffix) {
		
		String[] args = message.split(" ");
		
		ArrayList<String> end = new ArrayList<>();
		
		for(int x = 0; x < args.length; x++) {
			if(!args[x].endsWith(suffix)) {
				end.add(args[x]);
			}
		}
		
		StringBuilder re = new StringBuilder();
		
		for(String i : end) {
			re.append(i + " ");
		}
		
		return re.replace(re.length() - 1, re.length() - 1, "").toString();
		
	}

	
}
