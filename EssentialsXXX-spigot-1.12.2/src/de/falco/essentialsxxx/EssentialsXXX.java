package de.falco.essentialsxxx;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import de.falco.essentialsxxx.exceptions.NoPexException;
import de.falco.essentialsxxx.id.IdExcecutor;
import de.falco.essentialsxxx.id.NotregisteredException;
import de.falco.essentialsxxx.id.UserProfile;
import de.falco.essentialsxxx.util.MySql;
import net.milkbowl.vault.permission.Permission;
//import net.minecraft.server.v1_12_R1.NBTTagCompound;

public class EssentialsXXX extends JavaPlugin implements Listener, IdExcecutor{
	
	//main
	private static EssentialsXXX EssentialsXXXmain;
	private static String prefix;
	
	//Plugin Description
	private String name;
	private String version;
	private String author;
	
	//Commands
	//private Create create;
	//private Copy copy;
	//private Info info;
	
	//Config
	private ConfigFile EssentialsXXXconfig;
	
	//config fields
	private Map<String,Map<String,String>> EssentialsXXXgroups;
	private Map<UUID,Map<String,String>> EssentialsXXXplayer;
	
	//error message fields
	private String EssentialsXXXnopex;
	private String EssentialsXXXsyntax;
	private String EssentialsXXXnotonline;
	private String EssentialsXXXnotaplayer;
	
	//id-section
	private de.falco.essentialsxxx.id.ConfigFile idconfig;
	private MySql idmysql;
	
	//vault
	public static Permission permission = null;
	
	//disable join
	private boolean join = true;
	
	
	public void onEnable() {
		
		EssentialsXXXmain = this;
		
		name = EssentialsXXXmain.getDescription().getName();
		version = EssentialsXXXmain.getDescription().getVersion();
		author = EssentialsXXXmain.getDescription().getAuthors().get(0);
		
		
		prefix = "[" + name + "]";
		
		System.out.println(prefix + " Author: " + author + " version: " + version);
		
		//commands
		//create = new Create("create",this);
		//copy = new Copy("copy",this);
		//info = new Info("info",this);
		
		//this.getCommand("create").setExecutor(create);
		//this.getCommand("info").setExecutor(info);
		//this.getCommand("copy").setExecutor(copy);
		
		this.getServer().getPluginManager().registerEvents(this, this);
		
		//configs
		this.EssentialsXXXconfig = new ConfigFile(prefix,true);
		try {
			EssentialsXXXconfig.setup("config.yml", EssentialsXXXmain.getDataFolder());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(prefix + " config error");
			disablejoin();
			return;
		}
		
		loadfields();
		
		//vault
		if(!this.getServer().getPluginManager().isPluginEnabled("Vault")) {
			System.out.println(prefix + " couldnt find vault!");
			disablejoin();
			return;
		}
		
		setupPermissions();
		
		
		
		//id-section
		idconfig = new de.falco.essentialsxxx.id.ConfigFile(name);
		try {
			idconfig.setup("id.yml",EssentialsXXXmain.getDataFolder());
		} catch (IOException e) {
			e.printStackTrace();
			disablejoin();
			return;
		}
		String path = "jdbc:mysql://" + idconfig.getHost() + ":" + idconfig.getPort() + "/" + idconfig.getDatabase()+ "?autoReconnect=true";
		try {
			idmysql = new MySql("com.mysql.jdbc.Driver",path, idconfig.getUser(), idconfig.getPw());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println(prefix + " couldnt find mysql-classes"); 
			disablejoin();
		}
		
		try {
			idmysql.connect();
		}catch(java.sql.SQLException ex) {
			ex.printStackTrace();
			disablejoin();
		}
		
		System.out.println("[" + name + "] mysql-status: " + idmysql.isconnect());
		
		//UUID uuid = getUUIDbyUsername("8a7bc16c89 fa46ff8510 7729986d72 3c");
		UUID uuid = null;
		try {
			uuid = getUUIDbyUsername("505dc1c68e56471b919093c5d1435e94");
			System.out.println("changed uuid " + uuid);
		} catch (NotregisteredException e) {
			e.printStackTrace();
		}
		
		UserProfile u = null;
		try {
			u = getUserProfilebyuuid(uuid);
			System.out.println("user " + u);
			
			System.out.println("firstname " + u.getFirstname());
			System.out.println("lastname " + u.getLastname());
			System.out.println("" + u.getTimeByUserName("KnockyYT"));
		} catch (NotregisteredException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			UserProfile uu = getUserProfilebyUsername("KnockyYT");
			System.out.println("user2 " + uu);
		} catch (NotregisteredException e) {
			e.printStackTrace();
		}
	}
	
	private void disablejoin() {
		System.out.println(prefix + " disable joining...");
		join = false;
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.kickPlayer(prefix + " §aerror in plugin §6EssentialsXXX \n §aPlease report the error on your discord §6https://discord.gg/8beV42b");
		}
	}
	
	@EventHandler
	private void onJoinEvent(PlayerLoginEvent event) {
		if(join == false) {
			String message = prefix + " §aerror in plugin §6EssentialsXXX \n §aPlease report the error on our discordserver §6https://discord.gg/8beV42b";
			
			Result result = Result.KICK_OTHER;
			
			event.disallow(result, message);
		}
	}

	public void onDisable() {
		
		System.out.println(prefix + " stop plugin...");
		
		System.out.println(prefix + " try to close mysql connecton");
		
		try {
			idmysql.disconnect();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
		System.out.println(prefix + " good by :)");
	}

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }
	
	//methode to load the config
	private void loadfields() {
		
		//load groups
		this.EssentialsXXXgroups = new LinkedHashMap<>();
		
		if(this.EssentialsXXXconfig.getConfig().getConfigurationSection("config.groups") != null) {
			
			for(String group : EssentialsXXXconfig.getConfig().getConfigurationSection("config.groups").getKeys(false)) {
				
				Map<String,String> tmp = new LinkedHashMap<String, String>();
				boolean pex = false;
				
				for(String index : EssentialsXXXconfig.getConfig().getConfigurationSection("config.groups." + group).getKeys(false)) {
					
					if(index.equals("pex")) {
					pex = true;
					}
					
					tmp.put(index, EssentialsXXXconfig.getConfig().getString("config.groups." + group + "." + index));
					
				}
				
				
				if(pex == false) {
					try {
						throw new NoPexException(prefix + " error in group " + group + " no pex! couldnt register group");
					}catch(NoPexException ex) {
						ex.printStackTrace();
					}
				}else {
					
					System.out.println(prefix + " add group " + group);
					
					this.EssentialsXXXgroups.put(group, tmp);	
				}
			}
			
			
		}else {
			System.out.println(prefix + " no groups in config");
			//keine groups
		}
		
		
		//load player
		this.EssentialsXXXplayer = new LinkedHashMap<>();
		
		if(EssentialsXXXconfig.getConfig().getConfigurationSection("config.player") != null) {
			
			
			for(String player : EssentialsXXXconfig.getConfig().getConfigurationSection("config.player").getKeys(false)) {
				
				try {
					UUID uuid = UUID.fromString(player);
					
					Map<String,String> tmp = new LinkedHashMap<String,String>();
					
					for(String index : EssentialsXXXconfig.getConfig().getConfigurationSection("config.player." + player).getKeys(false)) {
						
							
						tmp.put(index, EssentialsXXXconfig.getConfig().getString("config.player." + player + "." + index));
						
					}
					
					
					System.out.println(prefix + "load player " + player);
					
					this.EssentialsXXXplayer.put(uuid, tmp);
					
				}catch(Exception ex) {
					System.out.println(prefix + " couldnt load player " + player);
				}
				
				
			}
			
			
			
		}else {//no player
			System.out.println(prefix + " no player");
		}
		
		//load error messages
		this.EssentialsXXXnotaplayer = EssentialsXXXconfig.getConfig().getString("config.message.error.notaplayer");
		this.EssentialsXXXnotonline = EssentialsXXXconfig.getConfig().getString("config.message.error.notonline");
		this.EssentialsXXXsyntax = EssentialsXXXconfig.getConfig().getString("config.message.error.syntax");
		this.EssentialsXXXnopex = EssentialsXXXconfig.getConfig().getString("config.message.error.nopex");
	}
	
	/*
	 * getter
	 */
	public static EssentialsXXX getEssentialsXXXmain() {
		return EssentialsXXXmain;
	}
	public ConfigFile getEssentialsXXXconfig() {
		return EssentialsXXXconfig;
	}
	public String getPluginName() {
		return name;
	}
	
	
	public Map<String, Map<String, String>> getEssentialsXXXgroups() {
		return EssentialsXXXgroups;
	}
	public Map<UUID, Map<String, String>> getEssentialsXXXplayer() {
		return EssentialsXXXplayer;
	}
	public MySql getIdmysql() {
		return idmysql;
	}
	
	
	public String getEssentialsXXXnopex() {
		return EssentialsXXXnopex;
	}
	public String getEssentialsXXXnotaplayer() {
		return EssentialsXXXnotaplayer;
	}
	public String getEssentialsXXXnotonline() {
		return EssentialsXXXnotonline;
	}
	public String getEssentialsXXXsyntax() {
		return EssentialsXXXsyntax;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
}
