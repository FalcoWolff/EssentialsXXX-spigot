package de.falco.essentialsxxx.util;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Class to configure configs
 * 
 * 
 * @author Falco Wolf
 * @version 1.1
 *
 */
public abstract class ConfigAdapter {
  
  private File file;
  private YamlConfiguration config;
  private String prefix; //prefix
  private boolean debug; //debug?
  
  
  /**
   * execute when the setup method is called
   * 
   */
  public abstract void onload();
  
  /**
   * execute when the file created in the setup method
   * 
   */
  public abstract void onfirstload();
  
  
  /**
   * Constructor (small version)
   * 
   */
  public ConfigAdapter() {
  }
  
  
  /**
   * Constructor (big version)
   * 
   * @param prefix
   * @param debug
   */
  public ConfigAdapter(String prefix, boolean debug) {
	  this.prefix = prefix;
	  this.debug = debug;
  }
  
  
  
  public void setup(String f, File path) throws IOException {
	  
	  File file = new File(path, f);
	  setup(file,path);
	  
  }
  
  /**
   * load the variables file and create the file
   * 
   * @param f
   * @param path
   * @throws IOException
   */
  public void setup(File f, File path) throws IOException{
	  
	 
	  
    if (!path.exists()) {
      path.mkdir();
      if (this.debug) {
          System.out.println(prefix + " create folder " + path.getName());   
      }
    }
    
    this.file = f;
    
    if (!f.exists()) {
      if (this.debug) {
          System.out.println(getPrefix() + " create file " + f.getName());
      }
      f.createNewFile();
      this.config = YamlConfiguration.loadConfiguration(f);
      onfirstload();
      onload();
      save();
    } else {
    	
      this.config = YamlConfiguration.loadConfiguration(f);
      onload();
      save();
    } 
  }
  
  /**
   * load the YAMLConfiguration into the file
   * 
   * @throws IOException
   */
  public void save() throws IOException {
    this.config.save(this.file);
  }
  
  /**
   * load the file into the YAMLConfiguration
   * 
   */
  public void reload() {
    this.config = YamlConfiguration.loadConfiguration(this.file);
  }
  
  /*
   * setter
   */
  
  public void setDebug(boolean debug) {
	this.debug = debug;
  }
  public void setPrefix(String prefix) {
	this.prefix = prefix;
  }
  public void setFile(File file) {
	this.file = file;
}
  public void setConfig(YamlConfiguration config) {
	this.config = config;
}
  
  /*
   * getter
   */
  public YamlConfiguration getConfig() {
    return this.config;
  }
  public File getFile() {
    return this.file;
  }
  public String getPrefix() {
    return this.prefix;
  }
  public boolean getDebug() {
	  return debug;
  }
  
  
}
