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

import de.falco.essentialsxxx.data.DataManager;
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
	
	//Config
	private ConfigFile EssentialsXXXconfig;
	
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
	
	//DataManager
	private DataManager manager;
	
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
		
		
		//load error messages
		this.EssentialsXXXnotaplayer = EssentialsXXXconfig.getConfig().getString("config.message.error.notaplayer");
		this.EssentialsXXXnotonline = EssentialsXXXconfig.getConfig().getString("config.message.error.notonline");
		this.EssentialsXXXsyntax = EssentialsXXXconfig.getConfig().getString("config.message.error.syntax");
		this.EssentialsXXXnopex = EssentialsXXXconfig.getConfig().getString("config.message.error.nopex");
		
		manager = new DataManager(this);
		manager.loadfields();
		
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
			System.out.println(prefix + " couldnt find mysql-classe"); 
			disablejoin();
		}
		
		try {
			idmysql.connect();
		}catch(java.sql.SQLException ex) {
			ex.printStackTrace();
			disablejoin();
		}
		
		System.out.println("[" + name + "] mysql-status: " + idmysql.isconnect());
		
		
		
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
	public MySql getIdmysql() {
		return idmysql;
	}
	public DataManager getManager() {
		return manager;
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
