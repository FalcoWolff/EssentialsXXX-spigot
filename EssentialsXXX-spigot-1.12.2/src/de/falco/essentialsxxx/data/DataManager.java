package de.falco.essentialsxxx.data;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import de.falco.essentialsxxx.ConfigFile;
import de.falco.essentialsxxx.EssentialsXXX;
import de.falco.essentialsxxx.data.datatypes.DataGroup;
import de.falco.essentialsxxx.data.datatypes.DataPlayer;
import de.falco.essentialsxxx.exceptions.NoPexException;

public class DataManager {
	
	private EssentialsXXX main;
	private ConfigFile config;
	
	public DataManager(EssentialsXXX main) {
		this.main = main;
		config = main.getEssentialsXXXconfig();
	}
	
	private Map<String,DataGroup> groups = new LinkedHashMap<>();
	private Map<UUID, DataPlayer> players = new LinkedHashMap<>();
	
	public DataGroup getGroup(String groupname) {
		
		if(hasGroup(groupname)) {
			return groups.get(groupname);
		}else {
			throw new IllegalStateException(main.getPrefix() + " no group with groupname " + groupname);
		}
		
	}
	
	public DataPlayer getPlayer(UUID uuid) {
		
		if(hasPlayer(uuid)) {
			return players.get(uuid);
		}else {
			throw new IllegalStateException(main.getPrefix() + " no player with uuid " + uuid);
		}
		
	}
	
	public boolean hasGroup(String groupname) {
		return groups.containsKey(groupname);
	}
	
	public boolean hasPlayer(UUID uuid) {
		return players.containsKey(uuid);
	}
	
	//methode to load the config
	public void loadfields() {
		
		if(config.getConfig().getConfigurationSection("config.groups") != null) {
			
			for(String group : config.getConfig().getConfigurationSection("config.groups").getKeys(false)) {
				
				Map<String,String> tmp = new LinkedHashMap<String, String>();
				String pex = null;
				
				for(String index : config.getConfig().getConfigurationSection("config.groups." + group).getKeys(false)) {
					
					if(index.equals("pex")) {
						pex = config.getConfig().getString("config.groups." + group + "." + index);
						continue;
					}
					
					tmp.put(index, config.getConfig().getString("config.groups." + group + "." + index));
					
				}
				
				
				if(pex == null) {
					try {
						throw new NoPexException(main.getPrefix() + " error in group " + group + " no pex! couldnt register group");
					}catch(NoPexException ex) {
						ex.printStackTrace();
					}
				}else {
					
					System.out.println(main.getPrefix() + " add group " + group);
					
					DataGroup g = new DataGroup(pex);
					g.setFields(tmp);
					
					groups.put(group, g);	
					
				}
			}
			
			
		}else {
			System.out.println(main.getPrefix() + " no groups in config");
			//keine groups
		}
		
		
		//load player
		if(config.getConfig().getConfigurationSection("config.player") != null) {
			
			
			for(String player : config.getConfig().getConfigurationSection("config.player").getKeys(false)) {
				
				try {
					
					UUID uuid = UUID.fromString(player);
					
					Map<String,String> tmp = new LinkedHashMap<String,String>();
					
					for(String index : config.getConfig().getConfigurationSection("config.player." + player).getKeys(false)) {
						
							
						tmp.put(index, config.getConfig().getString("config.player." + player + "." + index));
						
					}
					
					
					System.out.println(main.getPrefix() + "load player " + player);
					
					DataPlayer p = new DataPlayer();
					p.setFields(tmp);
					
					players.put(uuid, p);
					
				}catch(Exception ex) {
					System.out.println(main.getPrefix() + " couldnt load player " + player);
				}
				
				
			}
			
			
			
		}else {//no player
			System.out.println(main.getPrefix() + " no player");
		}
		
	}
	
	
	
	public Map<String, DataGroup> getGroups() {
		return groups;
	}
	public Map<UUID, DataPlayer> getPlayers() {
		return players;
	}
	
}