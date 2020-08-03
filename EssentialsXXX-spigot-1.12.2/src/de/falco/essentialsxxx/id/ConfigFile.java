package de.falco.essentialsxxx.id;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import de.falco.essentialsxxx.EssentialsXXX;
import de.falco.essentialsxxx.util.ConfigAdapter;

/*
 * config for the id-secion 
 */
public class ConfigFile extends ConfigAdapter{
	
	private EssentialsXXX main = EssentialsXXX.getEssentialsXXXmain();
	
	/*
	 * mysql variables
	 */
	private String port;
	private String host;
	private String database;
	private String user;
	private String pw;

	/*
	 * constructor
	 */
	public ConfigFile(String prefix) {
		super(prefix, true);
	}
	
	@Override
	public void onload() {
		
		if(this.getConfig().get("id.mysql.host") == null) {
			System.out.println("[" + main.getPluginName() + "] id.yml id.mysql.host ist null replace through default wort");
			this.getConfig().set("id.mysql.host", "localhost");
		}
		
		if(this.getConfig().get("id.mysql.port") == null) {
			System.out.println("[" + main.getPluginName() + "] id.yml id.mysql.port ist null replace through default wort");
			this.getConfig().set("id.mysql.port", "3306");
		}
		
		if(this.getConfig().get("id.mysql.database") == null) {
			System.out.println("[" + main.getPluginName() + "] id.yml id.mysql.database ist null replace through default wort");
			this.getConfig().set("id.mysql.database", "userid");
		}
		
		if(this.getConfig().get("id.mysql.user") == null) {
			System.out.println("[" + main.getPluginName() + "] id.yml id.mysql.user ist null replace through default wort");
			this.getConfig().set("id.mysql.user", "userid");
		}
		
		if(this.getConfig().get("id.mysql.pw") == null) {
			System.out.println("[" + main.getPluginName() + "] id.yml id.mysql.pw ist null replace through default wort");
			this.getConfig().set("id.mysql.pw", "userid");
		}
		
		/*
		 * mysql-variables
		 */
		this.host = this.getConfig().getString("id.mysql.host");
	    this.port = this.getConfig().getString("id.mysql.port");
	    this.database = this.getConfig().getString("id.mysql.database");
	    this.user = this.getConfig().getString("id.mysql.user");
	    this.pw = this.getConfig().getString("id.mysql.pw");
		
	}

	@Override
	public void onfirstload() {
		
	}

	
	/*
	 * getter for mysql fields
	 */
	public String getHost() {
		return host;
	}
	public String getPort() {
		return port;
	}
	public String getDatabase() {
		return database;
	}
	public String getUser() {
		return user;
	}
	public String getPw() {
		return pw;
	}

}
