package de.falco.essentialsxxx.deprecated;

import de.falco.essentialsxxx.exceptions.ItemStackExcecutorException;
import de.falco.essentialsxxx.util.MySql;
import net.minecraft.server.v1_12_R1.NBTTagByte;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagInt;
import net.minecraft.server.v1_12_R1.NBTTagIntArray;
import net.minecraft.server.v1_12_R1.NBTTagList;
import net.minecraft.server.v1_12_R1.NBTTagString;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.SkullMeta;

@Deprecated
public interface ItemStackExcecutor {
	
  /*
   * !VERALTET!
   * 
   * @Info Methods to save an Map<String, Object> in db
   * @param table : tablename where to store data
   * @param map : seralize item 
   * @param id : id of the item have to be unique
   * @param mysql : connection datatype to getconnection with db
   * 
   * 
   */
	
  @SuppressWarnings("unchecked")
  default void save(String table, Map<String, Object> map, int id, MySql mysql) throws ItemStackExcecutorException{
	  
	  
	  int type = 0;
	  int amount = 0;
	  int damage = 0;
	  
	  String ench = null;
	  int repaircost = 0;
	  int hideflags = 0;
	  String candestroy = null;
	  String canplaceon = null;
	  String attributemodifiers = null;
	  boolean unbreakable = false;
	  String metatype = null;
	  String objectid = null;
	  
	  /*
	   * display
	   */
	  
	  int color = 0;
	  String lore = null;
	  String name = null;
	  String mapcolor = null;
	  
	  
	  
	  ResultSet result = mysql.getResult("SELECT * FROM " + table + " WHERE id = '" + id + "'");
	  
	  try {
		  
		  
		if(result.next()) {
		        mysql.command("DELETE FROM " + table + " WHERE id = " + id);
		}
		
		/*
		 * wrong map?
		 */
		
		if(!map.containsKey("type") || !map.containsKey("amount") || !map.containsKey("damage")) {
			throw new ItemStackExcecutorException("missing key in map (type,amount or damage)");
		}
		
		/*
		 * type
		 */
		
		type = (int) map.get("type");
		
		/*
		 * damage
		 */
		
	    damage = (int) map.get("damage");
		
		
		/*
		 * amount
		 */
		
		amount = (int) map.get("amount");
		
		/*
		 * ench
		 */
		
		if(map.containsKey("ench")) {
			
			StringBuffer enchantbuffer = new StringBuffer();
			
			ArrayList<String> enchant = (ArrayList<String>) map.get("ench");
			
			for(String index : enchant) {
				enchantbuffer.append("§" + index);
			}
			
			String re = enchantbuffer.toString().replaceFirst("§", "");
			
			ench = re;
			
		}
		
		/*
		 * repaircost
		 */
		
		if(map.containsKey("repaircost")) {
			
			repaircost = (int) map.get("repaircost");
			
		}
		
		/*
		 * hideflags
		 */
		
		if(map.containsKey("HideFlags")) {
			
			hideflags = (int) map.get("HideFlags");
			
		}
		
		/*
		 * canplaceon
		 */
		
		if(map.containsKey("CanPlaceOn")) {
			
			StringBuffer canplaceonbuffer = new StringBuffer();
			
			ArrayList<String> c = (ArrayList<String>) map.get("CanPlaceOn");
			
			for(String index : c) {
				canplaceonbuffer.append("§" + index);
			}
			
			String re = canplaceonbuffer.toString().replaceFirst("§", "");
			
			canplaceon = re;
			
		}
		
		/*
		 * candestroy
		 */
		
		if(map.containsKey("CanDestroy")) {
			
			StringBuffer candestroybuffer = new StringBuffer();
			
			ArrayList<String> c = (ArrayList<String>) map.get("CanDestroy");
			
			for(String index : c) {
				candestroybuffer.append("§" + index);
			}
			
			String re = candestroybuffer.toString().replaceFirst("§", "");
			
			candestroy = re;
			
		}
		
		/*
		 * attribute
		 */
		
		if(map.containsKey("AttributeModifiers")) {
			
			ArrayList<Map<String,Object>> at = (ArrayList<Map<String, Object>>) map.get("AttributeModifiers");
			
			StringBuffer ids = new StringBuffer();
			
			for(Map<String,Object> attribute : at) {
				
				String attributename = null;
				String tmpname = null;
				String slot = null;
				int operation = 0;
				double tmpamount = 0;
				int uuidmost = 0;
				int uuidleast = 0;
				
				if(attribute.containsKey("AttributeName")) {
					attributename = (String) attribute.get("AttributeName");
				}
				
				if(attribute.containsKey("Name")) {
					tmpname = (String) attribute.get("Name");
				}
				
				if(attribute.containsKey("Slot")) {
					slot = (String) attribute.get("Slot");
				}
				
				if(attribute.containsKey("Operation")) {
					operation = (int) attribute.get("Operation");
				}
				
				if(attribute.containsKey("Amount")) {
					tmpamount = (double) attribute.get("Amount");
				}
				
				if(attribute.containsKey("UUIDMost")) {
					uuidmost = (int) attribute.get("UUIDMost");
				}
				
				if(attribute.containsKey("UUIDLeast")) {
					uuidleast = (int) attribute.get("UUIDLeast");
				}
				
				String command = "SELECT id FROM ATTRIBUTEMODIFIERS ORDER BY id";
				
				ResultSet re = mysql.getResult(command);
				
				int tmpid = 0;
				
				if(re.last()) {
					
					tmpid = re.getInt(1) + 1;
					
					
				}
				
				ids.append("§" + tmpid);
				
				
				command = "INSERT INTO ATTRIBUTEMODIFIERS (id,attributename,name,slot,operation,amount,uuidmost,uuidleast) VALUES (" +
						
						"" + tmpid + "," +
						"'" + attributename + "'," +
						"'" + tmpname + "'," + 
						"'" + slot + "'," +
						"" + operation + "," +
						"" + tmpamount + "," +
						"" + uuidmost + "," +
						"" + uuidleast + "" +
						
						")";
				mysql.command(command);
				
			}
			
			attributemodifiers = ids.toString().replaceFirst("§", "");
			
		}
		
		
		/*
		 * unbreakable
		 */
		
		if(map.containsKey("unbreakable")) {
			unbreakable = (boolean) map.get("unbreakable");
		}
		
		/*
		 * display
		 */
		
		if(map.containsKey("display")) {
			
			Map<String,Object> di = (Map<String, Object>) map.get("display");
			
			if(di.containsKey("color")) {
				color = (int) di.get("color");
			}
			
			if(di.containsKey("Lore")) {
				
				ArrayList<String> tmp = (ArrayList<String>) di.get("Lore");
				
				StringBuilder builder = new StringBuilder();
				
				for(String index : tmp) {
					builder.append("§" + index);
				}
				
				
				
				lore = builder.toString().replaceFirst("§", "");
			}
			
			if(di.containsKey("Name")) {
				name = (String) di.get("Name");
			}
			
			if(di.containsKey("MapColor")) {
				mapcolor = (String) di.get("MapColor");
			}
			
		}
		
		
		/*
		 * metatype
		 */
		
		if(map.containsKey("metatype")) {
			
			metatype = (String) map.get("metatype");
			
			/*
			 * Enchantmentstorage
			 */
			
			if(metatype.equalsIgnoreCase("Enchantmentstorage")) {
				
				ArrayList<String> stored = (ArrayList<String>) map.get("StoredEnchantments");
				
				StringBuffer buffer = new StringBuffer();
				
				for(String index : stored) {
					
					
					buffer.append("§" + index);
					
					
					
				}
				
				objectid = buffer.toString().replaceFirst("§", "");
				
				/*
				 * fireworkeffect
				 */
				
			}else if(metatype.equalsIgnoreCase("fireworkeffect")) {
				
				
				Map<String,Object> tmp = (Map<String, Object>) map.get("Explosion");
				
				int idtmp = 0;
				
				byte flicker = 0;
				byte trail = 0;
				byte typetmp = 0;
				String colors = null;
				String fadecolors = null;
				
				if(tmp.containsKey("Flicker")) {
					flicker = (byte) tmp.get("Flicker");
				}
				if(tmp.containsKey("Trail")) {
					trail = (byte) tmp.get("Trail");
				}
				if(tmp.containsKey("Type")) {
					typetmp = (byte) tmp.get("Type");
				}
				if(tmp.containsKey("Colors")) {
					int[] colorstmp = (int[]) tmp.get("Colors");
					
					StringBuffer buffer = new StringBuffer();
					
					for(int x : colorstmp) {
						buffer.append("§" + x);
					}
					
					colors = buffer.toString().replaceFirst("§", "");
					
				}
				if(tmp.containsKey("FadeColors")) {
					int[] fadecolorstmp = (int[]) tmp.get("FadeColors");
					
					StringBuffer buffer = new StringBuffer();
					
					for(int x : fadecolorstmp) {
						buffer.append("§" + x);
					}
					
					fadecolors = buffer.toString().replaceFirst("§", "");
				}
				
				
				ResultSet re = mysql.getResult("SELECT id FROM FIREWORKEFFECT ORDER BY id");
				
				if(re.last()) {
					idtmp = re.getInt(1) + 1;
				}
				
				String command = "INSERT INTO fireworkeffect (id,flicker,trail,type,colors,fadecolors) VALUES ("
						+ "" + idtmp
						+ "," + flicker
						+ "," + trail
						+ "," + typetmp
						+ ",'" + colors + "'"
						+ ",'" + fadecolors + "'"
						+ ""
						+ ");";
						
				
				mysql.command(command);
				
				objectid = idtmp + "";
				
				
				/*
				 * firework
				 */
				
			}else if(metatype.equalsIgnoreCase("FIREWORK")) {
				
				Map<String,Object> firework = (Map<String, Object>) map.get("Fireworks");
				
				byte flight = 0;
				String explosions = null;
				int ID = 0;
				
				if(firework.containsKey("Flight")) {
					flight = (byte) firework.get("Flight");
				}
				
				if(firework.containsKey("Explosions")) {
					
					ArrayList<Map<String,Object>> index =  (ArrayList<Map<String, Object>>) firework.get("Explosions");
					
					StringBuffer b = new StringBuffer();
					
					
					for(Map<String,Object> tmp : index) {
						
						
						int idtmp = 0;
						
						byte flicker = 0;
						byte trail = 0;
						byte typetmp = 0;
						String colors = null;
						String fadecolors = null;
						
						if(tmp.containsKey("Flicker")) {
							flicker = (byte) tmp.get("Flicker");
						}
						if(tmp.containsKey("Trail")) {
							trail = (byte) tmp.get("Trail");
						}
						if(tmp.containsKey("Type")) {
							typetmp = (byte) tmp.get("Type");
						}
						if(tmp.containsKey("Colors")) {
							int[] colorstmp = (int[]) tmp.get("Colors");
							
							StringBuffer buffer = new StringBuffer();
							
							for(int x : colorstmp) {
								buffer.append("§" + x);
							}
							
							colors = buffer.toString().replaceFirst("§", "");
							
						}
						if(tmp.containsKey("FadeColors")) {
							int[] fadecolorstmp = (int[]) tmp.get("FadeColors");
							
							StringBuffer buffer = new StringBuffer();
							
							for(int x : fadecolorstmp) {
								buffer.append("§" + x);
							}
							
							fadecolors = buffer.toString().replaceFirst("§", "");
						}
						
						
						ResultSet re = mysql.getResult("SELECT id FROM FIREWORKEFFECT ORDER BY id");
						
						if(re.last()) {
							idtmp = re.getInt(1) + 1;
						}
						
						String command = "INSERT INTO fireworkeffect (id,flicker,trail,type,colors,fadecolors) VALUES ("
								+ "" + idtmp
								+ "," + flicker
								+ "," + trail
								+ "," + typetmp
								+ ",'" + colors + "'"
								+ ",'" + fadecolors + "'"
								+ ""
								+ ");";
								
						
						mysql.command(command);
						
						b.append("§" + idtmp);
						
					}
					
					explosions = b.toString().replaceFirst("§", "");
					
					
				}
				
				ResultSet re = mysql.getResult("SELECT id FROM FIREWORK ORDER BY id");
				
				if(re.last()) {
					ID = re.getInt(1) + 1;
				}
				
				String command = "INSERT INTO FIREWORK (id,flight,explosions) VALUES ("
						+ "" + ID
						+ "," + flight
						+ ",'" + explosions + "'"
						+ ");";
				
				mysql.command(command);
				
				objectid = ID + "";
				
				/*
				 * skull
				 */
				
			}else if(metatype.equalsIgnoreCase("Skull")) {
				
				Map<String,Object> skullowner = (Map<String, Object>) map.get("SkullOwner");
				
				String uuid = null;
				String nametmp = null;
				
				int idtmp = 0;
				
				String properties = null;
				
				if(skullowner.containsKey("Id")) {
					uuid = (String) skullowner.get("Id");
				}
				if(skullowner.containsKey("Name")) {
					nametmp = (String) skullowner.get("Name");
				}
				//textures
				if(skullowner.containsKey("Properties")) {
					Map<String,Object> pr = (Map<String, Object>) skullowner.get("Properties");
					
					if(pr.containsKey("textures")) {
						
						StringBuffer ids = new StringBuffer();
						
						ArrayList<Map<String,Object>> te = (ArrayList<Map<String, Object>>) pr.get("textures");
						
						for(Map<String,Object> tmp : te) {
							
							String signature = null;
							String value = null;
							
							if(tmp.containsKey("Signature")) {
								signature = (String) tmp.get("Signature");
							}
							if(tmp.containsKey("Value")) {
								value = (String) tmp.get("Value");
							}
							
							int teid = 0;
							
							String command = "SELECT id FROm TEXTURES ORDER BY id";
							ResultSet re = mysql.getResult(command);
							
							if(re.last()) {
								teid = re.getInt(1) + 1;
							}
							
							command = "INSERT INTO TEXTURES (id,signature,value) VALUES ("
									+ "" + teid
									+ ",'" + signature + "'"
									+ ",'" + value + "'"
									+ ")";
							mysql.command(command);
							
							ids.append("§" + teid);
							
						}
						
						properties = ids.toString().replaceFirst("§", "");
						
					}
				}
				
				String command = "SELECT id FROM SKULL ORDER BY id";
				ResultSet re = mysql.getResult(command);
				
				if(re.last()) {
					idtmp = re.getInt(1) + 1;
				}
				
				objectid = idtmp + "";
				
				command = "INSERT INTO SKULL (id,uuid,name,properties) VALUES ("
						+ "" + idtmp
						+ ",'" + uuid + "'" 
						+ ",'" + nametmp + "'"
						+ ",'" + properties + "'"
						+ ")";
				mysql.command(command);
				
				/*
				 * potion
				 */
				
			}else if(metatype.equalsIgnoreCase("Potion")) {
				
				
				int potioncolor = 0;
				String potion = null;
				String custompotioneffects = null;
				
				int idtmp = 0;
				
				if(map.containsKey("Potion")) {
					potion = (String) map.get("Potion");
				}
				if(map.containsKey("CustomPotionColor")) {
					potioncolor = (int) map.get("CustomPotionColor");
				}
				
				if(map.containsKey("CustomPotionEffects")) {
					
					ArrayList<Map<String,Object>> effects = (ArrayList<Map<String, Object>>) map.get("CustomPotionEffects");
					
					StringBuilder builder = new StringBuilder();
					
					for(Map<String,Object> tmp : effects) {
						
						
						int effectid = 0;
						byte uuid = 0;
						byte amplifier = 0;
						int duration = 0;
						byte ambient = 0;
						byte showicon = 0;
						byte showparticles = 0;
						
						if(tmp.containsKey("Id")) {
							uuid = (byte) tmp.get("Id");
						}
						if(tmp.containsKey("Amplifier")) {
							amplifier = (byte) tmp.get("Amplifier");
						}
						if(tmp.containsKey("Duration")) {
							duration = (int) tmp.get("Duration");
						}
						if(tmp.containsKey("Ambient")) {
							ambient = (byte) tmp.get("Ambient");
						}
						if(tmp.containsKey("ShowIcon")) {
							showicon = (byte) tmp.get("ShowIcon");
						}
						if(tmp.containsKey("ShowParticles")) {
							showparticles = (byte) tmp.get("ShowParticles");
						}
						
						String command = "SELECT id FROM custompotioneffects ORDER BY id";
						ResultSet re = mysql.getResult(command);
						
						if(re.last()) {
							effectid = re.getInt(1) + 1;
						}
						
						command = "INSERT INTO CUSTOMPOTIONEFFECTS (id,uuid,amplifier,duration,ambient,showicon,showparticles) VALUES ("
								+ "" + effectid
								+ "," + uuid
								+ "," + amplifier
								+ "," + duration
								+ "," + ambient
								+ "," + showicon
								+ "," + showparticles
								+ ")";
						mysql.command(command);
						
						builder.append("§" + effectid);
						
					}
					
					custompotioneffects = builder.toString().replaceFirst("§", "");
				}
				
				String command = "SELECT id FROM POTION ORDER BY id";
				ResultSet re = mysql.getResult(command);
				
				if(re.last()) {
					idtmp = re.getInt(1) + 1;
				}
				
				objectid = idtmp + "";
				
				command = "INSERT INTO POTION (id,custompotioncolor,custompotioneffects,potion) VALUES ("
						+ "" + idtmp
						+ "," + potioncolor
						+ ",'" + custompotioneffects + "'"
						+ ",'" + potion + "'"
						+ ")";
				mysql.command(command);
				
				/*
				 * book
				 */
				
			}else if(metatype.equalsIgnoreCase("book")) {
				
				int idtmp = 0;
				
				byte resolved = 0;
				int generation = 0;
				String author = null;
				String title = null;
				String pages = null;
				
				if(map.containsKey("resolved")) {
					resolved = (byte) map.get("resolved");
				}
				if(map.containsKey("generation")) {
					generation = (int) map.get("generation");
				}
				if(map.containsKey("author")) {
					author = (String) map.get("author");
				}
				if(map.containsKey("title")) {
					title = (String) map.get("title");
				}
				if(map.containsKey("pages")) {
					
					StringBuilder builder = new StringBuilder();
					
					ArrayList<String> pagestmp = (ArrayList<String>) map.get("pages");
					
					for(String page : pagestmp) {
						
						builder.append("<html>" + page + "<!html>");
						
					}
					
					pages = builder.toString();
					
				}
				
				String command = "SELECT id FROM BOOK ORDER BY id";
				ResultSet re = mysql.getResult(command);
				
				if(re.last()) {
					idtmp = re.getInt(1) + 1;
				}
				
				command = "INSERT INTO BOOK (id,resolved,generation,author,title,pages) VALUES ("
						+ "" + idtmp
						+ "," + resolved
						+ "," + generation
						+ ",'" + author + "'"
						+ ",'" + title + "'"
						+ ",'" + pages + "'"
						+ ")";
				mysql.command(command);
				
				objectid = idtmp + "";
				
			
				/*
				 * map
				 */
				
			}else if(metatype.equalsIgnoreCase("map")) {
				
				int idtmp = 0;
				
				int maptmp = 0;
				int mapscaledirection = 0;
				String decorations = null;
				
				if(map.containsKey("map")) {
					maptmp = (int) map.get("map");
				}
				if(map.containsKey("map_scale_direction")) {
					mapscaledirection = (int) map.get("map_scale_direction");
				}
				
				if(map.containsKey("Decorations")) {
					
					ArrayList<Map<String,Object>> decorationstmp = (ArrayList<Map<String, Object>>) map.get("Decorations");
					
					StringBuilder builder = new StringBuilder();
					
					for(Map<String,Object> decoration : decorationstmp) {
						
						int ID = 0;
						String uuid = null;
						byte typetmp = 0;
						byte x = 0;
						byte z = 0;
						byte rot = 0;
						
						if(decoration.containsKey("id")) {
							uuid = (String) decoration.get("id");
						}
						if(decoration.containsKey("type")) {
							typetmp = (byte) decoration.get("type");
						}
						if(decoration.containsKey("x")) {
							x = (byte) decoration.get("x");
						}
						if(decoration.containsKey("z")) {
							z = (byte) decoration.get("z");
						}
						if(decoration.containsKey("rot")) {
							rot = (byte) decoration.get("rot");
						}
						
						
						String command = "SELECT id FROM DECORATIONS ORDER BY id";
						ResultSet re = mysql.getResult(command);
						
						if(re.last()) {
							ID = re.getInt(1) + 1;
						}
						
						command = "INSERT INTO DECORATIONS (id,uuid,type,x,z,rot) VALUES ("
								+ "" + ID
								+ ",'" + uuid + "'"
								+ "," + typetmp
								+ "," + x 
								+ "," + z
								+ "," + rot
								+ ")";
						mysql.command(command);
						
						builder.append("§" + ID);
						
					}
					
					decorations = builder.toString().replaceFirst("§", "");
					
				}
				
				String command = "SELECT id FROM MAP ORDER BY id";
				ResultSet re = mysql.getResult(command);
				
				if(re.last()) {
					idtmp = re.getInt(1) + 1;
				}
				
				command = "INSERT INTO MAP (id,map,mapscaledirection,decorations) VALUES ("
						+ "" + idtmp
						+ "," + maptmp
						+ "," + mapscaledirection
						+ ",'" + decorations + "'"
						+ ")";
				mysql.command(command);
				
				objectid = idtmp + "";
				
				
			}else {
				objectid = -1 + "";
			}
		}
		
		
		  String insert = "INSERT INTO " + table + " (id,type,damage,amount,unbreakable,displayname,lore,color,mapcolor,repaircost,hideflags,enchant,attribute,candestroy,canplaceon,metatype,objectid) VALUES ("
			  		+ "" + id
				    + "," + type
			  		+ "," + damage
			  		+ "," + amount
			  		+ "," + unbreakable
			  		+ ",'" + name + "'"
			  		+ ",'" + lore + "'"
			  		+ "," + color
			  		+ ",'" + mapcolor + "'"
			  		+ "," + repaircost
			  		+ "," + hideflags
			  		+ ",'" + ench + "'"
			  		+ ",'" + attributemodifiers + "'"
			  		+ ",'" + candestroy + "'"
			  		+ ",'" + canplaceon + "'"
			  		+ ",'" + metatype + "'"
			  		+ ",'" + objectid + "'"
			  		+ ")";
			  
		  System.out.println("[DEBUG] excecute command: " + insert);
		  mysql.command(insert);
		
		
	} catch (SQLException e) {
		e.printStackTrace();
	}
	  
	  /*
	  
    Object type = null;
    Object amount = Integer.valueOf(1);
    Object damage = Integer.valueOf(0);
    boolean unbreakable = false;
    Object lore = "";
    Object enchant = "";
    Object displayname = "";
    Object attribute = "";
    Object metatype = "";
    Object object = "";
    Object pages = "";
    Object flags = "";
    
    ResultSet result = mysql.getResult("SELECT * FROM " + table + " WHERE id = '" + id + "'");
    
    try {
      if (!result.next()) {
        if (map.containsKey("type"))
          type = map.get("type"); 
        if (map.containsKey("amount"))
          amount = map.get("amount"); 
        if (map.containsKey("damage"))
          damage = map.get("damage"); 
        unbreakable = ((Boolean)map.get("unbreakable")).booleanValue();
        if (map.containsKey("lore"))
          lore = map.get("lore"); 
        if (map.containsKey("enchant"))
          enchant = map.get("enchant"); 
        if (map.containsKey("displayname"))
          displayname = map.get("displayname"); 
        if (map.containsKey("attribute"))
          attribute = map.get("attribute"); 
        if (map.containsKey("metatype"))
          metatype = map.get("metatype"); 
        if (map.containsKey("object"))
          object = map.get("object"); 
        if (map.containsKey("pages"))
          pages = map.get("pages"); 
        if (map.containsKey("flags"))
          flags = map.get("flags"); 
        
        
        mysql.command("INSERT INTO " + table + " (id,type,damage,amount,unbreakable,lore,enchant,displayname,attribute,flags,metatype,object,pages) VALUES (" + id + 
            ",'" + type + 
            "'," + damage + 
            "," + amount + 
            "," + unbreakable + 
            ",'" + lore + 
            "','" + enchant + 
            "','" + displayname + 
            "','" + attribute + 
            "','" + flags + 
            "','" + metatype + 
            "','" + object + 
            "','" + pages + 
            "')");
      } else {
    	  
    	  
        mysql.command("DELETE FROM " + table + " WHERE id = " + id);
        
        
        if (map.containsKey("type"))
          type = map.get("type"); 
        if (map.containsKey("amount"))
          amount = map.get("amount"); 
        if (map.containsKey("damage"))
          damage = map.get("damage"); 
        unbreakable = ((Boolean)map.get("unbreakable")).booleanValue();
        if (map.containsKey("lore"))
          lore = map.get("lore"); 
        if (map.containsKey("enchant"))
          enchant = map.get("enchant"); 
        if (map.containsKey("displayname"))
          displayname = map.get("displayname"); 
        if (map.containsKey("attribute"))
          attribute = map.get("attribute"); 
        if (map.containsKey("metatype"))
          metatype = map.get("metatype"); 
        if (map.containsKey("object"))
          object = map.get("object"); 
        if (map.containsKey("pages"))
          pages = map.get("pages"); 
        if (map.containsKey("flags"))
          flags = map.get("flags"); 
        
        
        mysql.command("INSERT INTO " + table + " (id,type,damage,amount,unbreakable,lore,enchant,displayname,attribute,flags,metatype,object,pages) VALUES (" + id + 
            ",'" + type + 
            "'," + damage + 
            "," + amount + 
            "," + unbreakable + 
            ",'" + lore + 
            "','" + enchant + 
            "','" + displayname + 
            "','" + attribute + 
            "','" + flags + 
            "','" + metatype + 
            "','" + object + 
            "','" + pages + 
            "')");
      } 
      
      
      
    } catch (SQLException e) {
      e.printStackTrace();
    }
    
    
    
    */
    
    
  }
  
 
  
  /*
   * 
   * @Info load a Map<String, Object> from db
   * @param table : db-table saved the items
   * @param id : id of the item you want to load
   * @param mysql : MySql datetype with contains the connection
   * 
   * 
  */
  default Map<String, Object> load(String table, int id, MySql mysql) throws ItemStackExcecutorException {
	  
	/*
    if (MySqlFile.getMySqlFileConfiguration().getBoolean("config.saveloaddebug"))
      System.out.println("[" + Main.getPrefix() + "] [methode] util.load(" + slot + "," + id + ")"); 
    */
	  
	  
    String command = "SELECT * FROM " + table + " WHERE id = '" + id + "'";
    ResultSet result = mysql.getResult(command);
    Map<String, Object> map = new LinkedHashMap<>();
    
    try {
    	
      if (!result.first()) {
          throw new ItemStackExcecutorException("In table " + table + " isnt a item with id " + id);   
      }
      
      
      map.put("type", result.getString(2));
      map.put("damage", Integer.valueOf(result.getInt(3)));
      map.put("amount", Integer.valueOf(result.getInt(4)));
      if (result.getInt(5) == 1) {
        map.put("unbreakable", Boolean.valueOf(true));
      } else {
        map.put("unbreakable", Boolean.valueOf(false));
      } 
      if (!result.getString(6).equals(""))
        map.put("lore", result.getString(6)); 
      if (!result.getString(7).equals(""))
        map.put("enchant", result.getString(7)); 
      if (!result.getString(8).equals(""))
        map.put("displayname", result.getString(8)); 
      if (!result.getString(9).equals(""))
        map.put("attribute", result.getString(9)); 
      if (!result.getString(10).equals(""))
        map.put("flags", result.getString(10)); 
      if (!result.getString(11).equalsIgnoreCase("UNSPECIFIC"))
        map.put("metatype", result.getString(11)); 
      if (!result.getString(12).equals(""))
        map.put("object", result.getString(12)); 
      if (!result.getString(13).equals(""))
        map.put("pages", result.getString(13)); 
    } catch (SQLException e) {
      e.printStackTrace();
    } 
    
    /*
    if (mysql.getMySqlFileConfiguration().getBoolean("config.saveloaddebug"))
      System.out.println("[EC] [return " + map.toString() + "] success"); 
    */
    
    return map;
  }
  
  
  /*
   * 
   * @INFO generate a table to save NORMAL itemstacks 
   * @param name of the table
   * @param mysql : Mysql connection
   * 
   */
  
  default void generatetable(String tablename, MySql mysql) {
	  
	  
	    String command = "CREATE TABLE " + tablename + " (id INT Primary Key, type INT, damage INT" + 
		        ", amount INT , unbreakable TINYINT" + 
		        ", repaircost INT, hideflags INT,attribute TEXT, candestroy TEXT, canplaceon TEXT" + 
		        ", enchant TEXT , displayname TEXT, lore TEXT, color INT, mapcolor TEXT" + 
		        ",  metatype VARCHAR(100), objectid TEXT" + 
		        ");";
	    
	    mysql.command(command);
	    
	  
	    
  }
  
  /*
   * 
   * @INFO generate all other spezielle tables used for save banners or eggs ...
   * @para mysql : MySql connection
   * 
   * 	  String command = "CREATE TABLE IF NOT EXISTS POTION (" + 
			  "id int PrimaryKey" + 
			  "" + 
			  "" + 
			  ")";
		  mysql.command(command);
   * 
   */
  
  default void generatespezielletables(MySql mysql) {
	  
	  /*
	   * metatype = POTION
	   */
	  
	  String command = "CREATE TABLE IF NOT EXISTS POTION (" + 
			  "id INT Primary Key," + 
			  "custompotioncolor INT," + 
			  "custompotioneffects TEXT," + 
			  "potion TEXT" + 
			  ");";
	  mysql.command(command);
	  
	  /*
	   * CustomPotionEffects
	   */
	  
	  command = "CREATE TABLE IF NOT EXISTS CUSTOMPOTIONEFFECTS ("
	  		+ "id INT Primary Key"
	  		+ ",uuid TINYINT"
	  		+ ",amplifier TINYINT"
	  		+ ",duration INT"
	  		+ ",ambient TINYINT"
	  		+ ",showicon TINYINT"
	  		+ ",showparticles TINYINT"
	  		+ ");"; 
	  mysql.command(command);
	  
	  /*
	   * metatype = BOOK
	   */
	  
	  command = "CREATE TABLE IF NOT EXISTS BOOK (" + 
			  "id int Primary Key," + 
			  "resolved INT," + 
			  "generation INT," + 
			  "author TEXT," + 
			  "title TEXT," + 
			  "pages TEXT" + 
			  ");";
	  mysql.command(command);
	  
	  
	  
	  /*
	   * metatype = MAP
	   */
	  
	  command = "CREATE TABLE IF NOT EXISTS MAP (" + 
			  "id int Primary Key," + 
			  "map INT," + 
			  "mapscaledirection INT," + 
			  "decorations TEXT" +
			  ");";
	  mysql.command(command);
	  
	  /*
	   * DECORATIONS
	   */
	  
	  command = "CREATE TABLE IF NOT EXISTS DECORATIONS ("
	  		+ "id int Primary Key,"
	  		+ "uuid TEXT,"
	  		+ "type TINYINT,"
	  		+ "x TINYINT,"
	  		+ "z TINYINT,"
	  		+ "rot TINYINT"
	  		+ ""
	  		+ ");";
	  mysql.command(command);
	  
	  /*
	   * metatype = FIREWORKEFFECT
	   */
	  
	  command = "CREATE TABLE IF NOT EXISTS FIREWORKEFFECT (" + 
			  "id int Primary Key," + 
			  "flicker INT," + 
			  "trail INT," + 
			  "type INT," + 
			  "colors TEXT," + 
			  "fadecolors TEXT" + 
			  ");";
	  mysql.command(command);
	  
	  /*
	   * metatype = FIREWORK
	   */
	  
	  command = "CREATE TABLE IF NOT EXISTS FIREWORK (" + 
			  "id INT Primary Key," + 
			  "flight INT," + 
			  "explosions TEXT" + //number of FIREWORKEFFECTs 
			  ");";
	  mysql.command(command);
	  
	  /*
	   * metatype = BANNER
	   */
	  
	  command = "CREATE TABLE IF NOT EXISTS BANNER (" + 
			  "id int Primary Key," + 
			  "customname TEXT," + 
			  "patterns TEXT" + 
			  ");";
	  mysql.command(command);
	  
	  /*
	   * TEXTURES
	   */
	  
	  command = "CREATE TABLE IF NOT EXISTS TEXTURES (" + 
			  "id int Primary Key," + 
			  "signature TEXT," + 
			  "value TEXT" + 
			  ");";
	  mysql.command(command);
	  
	  /*
	   * metatype = SKULL
	   */
	  
	  command = "CREATE TABLE IF NOT EXISTS SKULL (" + 
			  "id int Primary Key," + 
			  "uuid TEXT," + 
			  "name TEXT," + 
			  "properties TEXT" + 
			  ");";
	  mysql.command(command);
	  
	  
	  
  }
  
  
  /*
   * 
   * @Info serialize a item to a map
   * @param item you want to serialize
   * @return Map<String,Object> as result of the method
   * 
   */
  @SuppressWarnings("unchecked")
default Map<String, Object> serialize(ItemStack item) {
	  
    Map<String, Object> result = new LinkedHashMap<>();
    
    
    result.put("type", item.getType().getId());
    
    
    result.put("damage", item.getDurability()); 	
    
    
    result.put("amount", item.getAmount()); 	
    
    
    ItemMeta meta = item.getItemMeta();
    
    
    /*
     * NBTTagsection
     */
    
    net.minecraft.server.v1_12_R1.ItemStack nmscopy = CraftItemStack.asNMSCopy(item);
    
    NBTTagCompound tag = nmscopy.getTag();
    
    if(nmscopy.hasTag()) {
    	tag = nmscopy.getTag();
    }else {
    	tag = new NBTTagCompound();
    }
    
    System.out.println(tag.toString());
    
    /*
     * unbreakable-section
     */
    
    if (meta.isUnbreakable()) {
        result.put("unbreakable", Boolean.valueOf(true));
      } else {
        result.put("unbreakable", Boolean.valueOf(false));
    }//unbreakable
    
    /*
     * display-section
     */
    
    
    if(tag.hasKey("display")) {
    	
        NBTTagCompound display = (NBTTagCompound) tag.get("display");
    	
        Map<String,Object> di = new LinkedHashMap<>();
        
        
    	/*
    	 * Name
    	 */
    	
        if(display.hasKey("Name")) {
        	di.put("Name", display.getString("Name"));
        }//name
    	
    	/*
    	 * color
    	 */
    	
    	
    	if(display.hasKey("color")) {
        	int color = display.getInt("color");
        	
        	di.put("color", color);
    	}//color
    	
    	/*
    	 * Lore
    	 */
    	
    	if(display.hasKey("Lore")) {
    		
    		ArrayList<String> lo = new ArrayList<>();
    		
    		NBTTagList lore = (NBTTagList) display.get("Lore");
    		
    		
    		for(int x = 0; x < lore.size(); x++) {
    			String line = lore.getString(x);
    			lo.add(line);
    		}
    		
    		
    		di.put("Lore", lo);
    	}//lore
    	
    	if(display.hasKey("MapColor")) {
    		
    		int mapcolor = display.getInt("MapColor");
    		di.put("MapColor", mapcolor);
    		
    	}//mapcolor
    	
    	result.put("display", di);
    	
    }//display
    
    
    /*
     * CustomPotionColor
     */
    
    
    if(tag.hasKey("CustomPotionColor")) {
    	
        int CustomPotionColor = tag.getInt("CustomPotionColor");
    	
    	result.put("CustomPotionColor", CustomPotionColor);
    }//CustomPotionColor
    
    /*
     * ench
     */
    
    
    if(tag.hasKey("ench")) {
    	
        NBTTagList ench = (NBTTagList) tag.get("ench");
    	
    	
        ArrayList<String> a = new ArrayList<>();
        
    	for(int x = 0; x < ench.size(); x++) {
    		
    		NBTTagCompound e = ench.get(x);
    		
    		String id = e.getString("id");
    		int lvl = e.getInt("lvl");
    		
    		
    		a.add(id + ":" + lvl);
    		
    	}
    	
    	
    	result.put("ench", a);
    }//ench
    
    /*
     * CustomPotionEffects
     */
    
    
    if(tag.hasKey("CustomPotionEffects")) {
    	
        NBTTagList CustomPotionEffects = (NBTTagList) tag.get("CustomPotionEffects");
    	
    	ArrayList<Map<String,Object>> re = new ArrayList<>();
    	
    	
    	for(int x = 0; x < CustomPotionEffects.size(); x++) {
    		
    		NBTTagCompound i = CustomPotionEffects.get(x);
    		
    		Map<String,Object> tmp = new LinkedHashMap<>();
    		
    		byte id = i.getByte("Id");
    		byte Amplifier = i.getByte("Amplifier");
    		int duration = i.getInt("Duration");
    		byte ambient = i.getByte("Ambient");
    		byte showicon = i.getByte("ShowIcon");
    		byte showparticles = i.getByte("ShowParticles");
    		
    		tmp.put("Id", id);
    		tmp.put("Amplifier", Amplifier);
    		tmp.put("Duration", duration);
    		tmp.put("Ambient", ambient);
    		tmp.put("ShowIcon", showicon);
    		tmp.put("ShowParticles", showparticles);
    		
    		re.add(tmp);
    		
    	}
    	
    	
    	result.put("CustomPotionEffects", re);
    	
    }//CustomPotionEffects
    
    /*
     * Potion
     */
    
    if(tag.hasKey("Potion")) {
    	
	    String potion = tag.getString("Potion");
    	
    	if(!potion.isEmpty()) {
    		
    		result.put("metatype", "POTION");
        	result.put("Potion", potion);	
        	
    	}
    }//Potion
    
    /*
     * RepairCost
     */
    
    if(tag.hasKey("RepairCost")) {
    	
        int repaircost = tag.getInt("RepairCost");
    	
    	result.put("RepairCost", repaircost);
    }//RepairCost
    
    /*
     * HideFlags
     */
    
    if(tag.hasKey("HideFlags")) {
        int hideflag = tag.getInt("HideFlags");
    	
    	result.put("HideFlags", hideflag);
    }//HideFlags
    
    /*
     * CanDestroy
     */
    
    
    if(tag.hasKey("CanDestroy")) {
    	
        NBTTagList destroylist = (NBTTagList) tag.get("CanDestroy");
    	
        ArrayList<String> de = new ArrayList<>();
        
    	
    	for(int x = 0; x < destroylist.size(); x++) {
    		
    		
    		String test = destroylist.getString(x);
    		
    		de.add(test);
    		
    		
    	}
    	
    	
    	result.put("CanDestroy", de);
    }//CanDestroy
    
    /*
     * CanPlaceOn
     */
    
    
    if(tag.hasKey("CanPlaceOn")) {
    	
        NBTTagList placelist = (NBTTagList) tag.get("CanPlaceOn");
    	
        ArrayList<String> pl = new ArrayList<>();
        
    	
    	for(int x = 0; x < placelist.size(); x++) {
    		
    		
    		String test = placelist.getString(x);
    		
    		pl.add(test);
    		
    	}
    	
    	
    	result.put("CanPlaceOn", pl);
    }//CanPlaceOn
    
    /*
     * AttributeModifiers
     */
    
    
    if(tag.hasKey("AttributeModifiers")) {
    	
        NBTTagList attributes = (NBTTagList) tag.get("AttributeModifiers");
    	
    	
        ArrayList<Map<String,Object>> at = new ArrayList<>();
        
    	for(int x = 0; x < attributes.size(); x++) {
    		
    		NBTTagCompound index = attributes.get(x);
    		
    		Map<String,Object> tmp = new LinkedHashMap<>();
    		
    		String name = index.getString("Name");
    		String attributename = index.getString("AttributeName");
    		double amount = index.getDouble("Amount");
    		int operation = index.getShort("Operation");
    		long UUIDMost = index.getLong("UUIDMost");
    		long UUIDLeast = index.getLong("UUIDLeast");
    		String slot = index.getString("Slot");
    		
    		
    		tmp.put("Name", name);
    		tmp.put("AttributeName", attributename);
    		tmp.put("Amount", amount);
    		tmp.put("Operation", operation);
    		tmp.put("UUIDMost", UUIDMost);
    		tmp.put("UUIDLeast", UUIDLeast);
    		tmp.put("Slot", slot);
    		
    		at.add(tmp);
    		
    	}
    	
    	
    	result.put("AttributeModifiers", at);
    	
    }//AttributeModifiers
    
    
    /*
     * spezial meta types
     */
    
    /*
     * BANNER
     */
    
    if (meta instanceof BannerMeta) {
    	
    	
      result.put("metatype", "BANNER");
      
      if(tag.hasKey("BlockEntityTag")) {
    	  
    	  
    	  
    	  LinkedHashMap<String,Object> blockentitytag;
    	  
    	  if(result.containsKey("BlockEntityTag")) {
    		  blockentitytag = (LinkedHashMap<String, Object>) result.get("BlockEntityTag");
    	  }else {
    		  blockentitytag = new LinkedHashMap<String,Object>();
    	  }
    	  
    	  NBTTagCompound block = (NBTTagCompound) tag.get("BlockEntityTag");
    	  
    	  
          if(block.hasKey("CustomName")) {
        	  blockentitytag.put("CustomName", block.getString("CustomName"));
          }
          
          
          if(block.hasKey("Patterns")) {
        	  
        	  System.out.println("Patterns");
        	  
        	  NBTTagList patterns = (NBTTagList) block.get("Patterns");
        	  
        	  ArrayList<String> test = new ArrayList<>();
        	  
        	  
        	  for(int x = 0; x < patterns.size(); x++) {
        		  
        		  NBTTagCompound i = patterns.get(x);
        		  
        		  int color = i.getInt("Color");
        		  String pattern = i.getString("Pattern");
        		  
        		  System.out.println(color + " " + pattern);
        		  
        		  test.add(pattern + ":" + color);
        		  
        	  }
        	  
        	  
        	  blockentitytag.put("Patterns", test);
        	  
          }//Patterns
    	  
          
          result.put("BlockEntityTag", blockentitytag);
          
      }
      
      
      //Banner
      
      
      /*
       * SKULL
       */
    } else if (meta instanceof SkullMeta) {
      result.put("metatype", "SKULL");
      
      if(tag.hasKey("SkullOwner")) {
    	  
    	  Map<String, Object> skullowner = new LinkedHashMap<>();
    	  
    	  NBTTagCompound skullownerNBT = (NBTTagCompound) tag.get("SkullOwner");
    	  
    	  
    	  if(skullownerNBT.hasKey("Id")) {
    		  
    		  skullowner.put("Id", skullownerNBT.getString("Id"));
    		  
    	  }
    	  
    	  if(skullownerNBT.hasKey("Name")) {
    		  
    		  skullowner.put("Name", skullownerNBT.getString("Name"));
    		  
    	  }
    	  
    	  if(skullownerNBT.hasKey("Properties")) {
    		  
    		  Map<String, Object> properties = new LinkedHashMap<>();
    		  
    		  NBTTagCompound propertiesNBT = skullownerNBT.getCompound("Properties");
    		  
    		  if(propertiesNBT.hasKey("textures")) {
    			  
    			  ArrayList<Map<String, Object>> textures = new ArrayList<>();
    			  
    			  NBTTagList texturesNBT = (NBTTagList) propertiesNBT.get("textures");
    			  
    			  for(int x = 0; x < texturesNBT.size(); x++) {
    				  
    				  NBTTagCompound index = texturesNBT.get(x);
    				  
    				  Map<String,Object> i = new LinkedHashMap<>();
    				  
    				  if(index.hasKey("Signature")) {
    					  i.put("Signature", index.getString("Signature"));
    				  }
    				  
    				  if(index.hasKey("Value")) {
    					  i.put("Value", index.getString("Value"));
    				  }
    				  
    				  textures.add(i);
    				  
    			  }
    			  
    			  properties.put("textures", textures);
    			  
    		  }
    		  
    		  skullowner.put("Properties", properties);
    		  
    	  }
    	  
    	  
    	  result.put("SkullOwner", skullowner);
    	  
      }
      
      
      //SKULL
    } else if (meta instanceof BookMeta) {
      result.put("metatype", "BOOK");
      
      //StringBuilder objectbuilder = new StringBuilder();
      
      if(tag.hasKey("resolved")) {
    	  byte resolved = tag.getByte("resolved");
    	  result.put("resolved", resolved);
      }
      
      if(tag.hasKey("generation")) {
    	  int generation = tag.getInt("generation");
    	  result.put("generation", generation);
      }
      
      if(tag.hasKey("author")) {
    	  String author = tag.getString("author");
    	  result.put("author", author);
      }
      
      if(tag.hasKey("title")) {
    	  String title = tag.getString("title");
    	  result.put("title", title);
      }
      
      if(tag.hasKey("pages")) {
    	  
    	  NBTTagList pages = (NBTTagList) tag.get("pages");
    	  
    	  ArrayList<String> t = new ArrayList<>();
    	  
    	  for(int x = 0; x < pages.size(); x++) {
    		  
    		  String page = pages.getString(x);
    		  
    		  t.add(page);
    		  
    	  }
    	  
    	  result.put("pages", t);
    	  
      }
      
      
      //Book
      
      /*
       * ENCHANTMENTSTORAGE
       */
      
    } else if (meta instanceof EnchantmentStorageMeta) {
    	
    	
      result.put("metatype", "ENCHANTMENTSTORAGE");
      
      if(tag.hasKey("StoredEnchantments")) {
    	  
    	  NBTTagList storedenchantments = (NBTTagList) tag.get("StoredEnchantments");
    	  
    	  System.out.println(storedenchantments);
    	  
    	  ArrayList<String> test = new ArrayList<>();
    	  
    	  for(int x = 0; x < storedenchantments.size(); x++) {
    		  
    		  NBTTagCompound ench = storedenchantments.get(x);
    		  
    		  int id = ench.getInt("id");
    		  int lvl = ench.getInt("lvl");
    		  
    		  System.out.println(id + " " + lvl);
    		  
    		  test.add(id + ":" + lvl);
    		  
    	  }
    	  
    	  result.put("StoredEnchantments", test);
    	  
      }
      
      
       //ENCHANTMENTSTORAGE

      /*
       * FIREWORK
       */
      
    }else if (meta instanceof FireworkMeta) {
    	
    	System.out.println("Fireworkdmeta");
    	
    	result.put("metatype", "FIREWORK");
    	
    	if(tag.hasKey("Fireworks")) {
    		
        	
        	NBTTagCompound fireworks = (NBTTagCompound) tag.get("Fireworks");
        	
        	Map<String,Object> fi = new LinkedHashMap<String,Object>();
        	
        	if(fireworks.hasKey("Flight")) {
            	byte flight = fireworks.getByte("Flight");
            	fi.put("Flight", flight);
        	}
        	
        	
        	if(fireworks.hasKey("Explosions")) {
        		
        		
        		
        		NBTTagList explosions = (NBTTagList) fireworks.get("Explosions");
        		
        		ArrayList<LinkedHashMap<String,Object>> e = new ArrayList<>();
        		
        		if(explosions != null) {
        			
        			
        			for(int x = 0; x < explosions.size(); x++) {
        				
        				
        				NBTTagCompound ex = explosions.get(x);
        				
        				LinkedHashMap<String,Object> tmp = new LinkedHashMap<>();
        				
        	    		byte Flicker = ex.getByte("Flicker");
        	    		byte Trail = ex.getByte("Trail");
        	    		byte Type = ex.getByte("Type");
        	    		int[] Colors = ex.getIntArray("Colors");
        	    		int[] FadeColors = ex.getIntArray("FadeColors");
        	    		
        	    		tmp.put("Flicker", Flicker);
        	    		tmp.put("Trail", Trail);
        	    		tmp.put("Type", Type);
        	    		tmp.put("Colors", Colors);
        	    		tmp.put("FadeColors", FadeColors);
        				
        	    		
        				
        				e.add(tmp);
        				
        			}//for
        			
        			
        			fi.put("Explosions", e);
        			
        			result.put("Fireworks", fi);
        			
        			
        		}
        		
        	}
    	}
    	//FIREWORK
    	
    	/*
    	 * MAP
    	 */
    }else if (meta instanceof MapMeta) {
    	
    	System.out.println("MapMeta");
    	
    	result.put("metatype", "MAP");
    	
    	if(tag.hasKey("map")) {
    		int map = tag.getInt("map");
    		result.put("map", map);
    	}
    	
    	if(tag.hasKey("map_scale_direction")) {
    		int map_scale_direction = tag.getInt("map_scale_direction");
    		result.put("map_scale_direction", map_scale_direction);
    	}
    	
    	if(tag.hasKey("display")) {
    		
    		NBTTagCompound di = (NBTTagCompound) tag.get("display");
    		
    		if(di.hasKey("MapColor")) {
    		
    			int mapcolor = di.getInt("MapColor");
    			
        		if(result.containsKey("display")) {
        			Map<String,Object> t = (Map<String, Object>) result.get("display");
        			t.put("MapColor", mapcolor);
        		}else {
        			Map<String,Object> t = new LinkedHashMap<>();
        			t.put("MapColor", mapcolor);
        			result.put("display", t);
        		}
    			
    		}
    		
    	}
    	
    	
    	if(tag.hasKey("Decorations")) {
    		
    		NBTTagList decorations = (NBTTagList) tag.get("Decorations");
    		
    		ArrayList<Map<String,Object>> de = new ArrayList<>();
    		
    		
    		for(int x = 0; x < decorations.size(); x++) {
    			
    			NBTTagCompound i = decorations.get(x);
    			
    			String id = i.getString("id");
    			byte type = i.getByte("type");
    			byte xx = i.getByte("x");
    			byte z = i.getByte("z");
    			byte rot = i.getByte("rot");
    			
    			Map<String,Object> tmp = new LinkedHashMap<>();
    			
    			tmp.put("id", id);
    			tmp.put("type", type);
    			tmp.put("x", xx);
    			tmp.put("z", z);
    			tmp.put("rot", rot);
    			
    			de.add(tmp);
    			
    		}
    		
    		result.put("Decorations", de);
    		
    		
    	}
      
    	//MAP
    	
    	/*
    	 * FIREWORKEFFECT
    	 */
    	
    }else if (meta instanceof FireworkEffectMeta) {
    	
    	
    	System.out.println("FireworkEffectMeta");
    	
    	result.put("metatype", "FIREWORKEFFECT");
    	
    	if(tag.hasKey("Explosion")) {
    		
    		
    		Map<String,Object> ex = new LinkedHashMap<>();
    		
    		NBTTagCompound e = (NBTTagCompound) tag.get("Explosion");
    		
    		byte Flicker = e.getByte("Flicker");
    		byte Trail = e.getByte("Trail");
    		byte Type = e.getByte("Type");
    		int[] Colors = e.getIntArray("Colors");
    		int[] FadeColors = e.getIntArray("FadeColors");
    		
    		ex.put("Flicker", Flicker);
    		ex.put("Trail", Trail);
    		ex.put("Type", Type);
    		ex.put("Colors", Colors);
    		ex.put("FadeColors", FadeColors);
    		
    		result.put("Explosion", ex);
    		
    		
    	}
    	
    	//Fireworkeffect
    	
    	
    } else {
    	
      if(!result.containsKey("metatype")) {
          result.put("metatype", "UNSPECIFIC");  
      }
      
      
    } 
    
    
    
    
    return result;
  }
  
  
  /*
   * 
   * @Info deserialize a Map<>
   * @param the LinkedHashMap you want to confirm
   * @return the result 
   * 
   */
  @SuppressWarnings("unchecked")
default ItemStack deserialize(Map<String, Object> args) {
	  
	  
    
    Material type = Material.getMaterial((int) args.get("type"));
    
    short damage = 0;
    int amount = 1;
    
    if (args.containsKey("damage"))
      damage = ((Number)args.get("damage")).shortValue(); 
    
    if (args.containsKey("amount"))
      amount = ((Integer)args.get("amount")).intValue(); 
    
    ItemStack result = new ItemStack(type, amount, damage);
    
    ItemMeta resultmeta = result.getItemMeta();
    
    if (args.get("unbreakable").equals(Boolean.valueOf(true)))
      resultmeta.setUnbreakable(true); 
    
    
    result.setItemMeta(resultmeta);
    
    /*
     * NBTTagSection
     */
    
    net.minecraft.server.v1_12_R1.ItemStack nmscopy = CraftItemStack.asNMSCopy(result);
    
    NBTTagCompound tag;
    
    if(nmscopy.hasTag()) {
        tag = nmscopy.getTag();
    }else {
    	tag = new NBTTagCompound();
    }
    
    
    /*
     * ench
     */
    if (args.containsKey("ench")) {
    	
    	
    	ArrayList<String> ench = (ArrayList<String>) args.get("ench");
    	
    	NBTTagList enchantments = new NBTTagList();
    	
    	for(String index : ench) {
    		String[] indexsplit = index.split(":");
    		
    		NBTTagCompound i = new NBTTagCompound();
    		i.set("id", new NBTTagInt(Integer.parseInt(indexsplit[0])));
    		i.set("lvl", new NBTTagInt(Integer.parseInt(indexsplit[1])));
    		
    		
    		enchantments.add(i);
    	}
    	
    	tag.set("ench", enchantments);
    	
    } //enchant
    
    
    
    /*
     * display
     */
    if(args.containsKey("display")) {
    	
    	
    	NBTTagCompound display  = new NBTTagCompound();
    	
    	Map<String,Object> di = (Map<String, Object>) args.get("display");
    	
    	/*
    	 * Name
    	 */
    	
        if(di.containsKey("Name")) {
        	String name = (String) di.get("Name");
        	display.set("Name", new NBTTagString(name));
        }
        
        /*
         * color
         */
        
        if(di.containsKey("color")) {
        	NBTTagInt color = new NBTTagInt((int) di.get("color"));
        	display.set("color", color);
        }
        
        /*
         * Lore
         */
        
        if(di.containsKey("Lore")) {
        	NBTTagList lore = new NBTTagList();
        	
        	ArrayList<String> lo = (ArrayList<String>) di.get("Lore");
        	
        	for(String index : lo) {
        		lore.add(new NBTTagString(index));
        	}
        	
        	display.set("Lore", lore);
        }
        
        if(di.containsKey("MapColor")) {
        	
        	
        	NBTTagInt mapcolor = new NBTTagInt((int) di.get("MapColor"));
        	display.set("MapColor", mapcolor);
        	
        }
        
        if(!display.isEmpty()) {
        	tag.set("display", display);
        }
    }
    
    
    
    
    /*
     * CustomPotionColor
     */
    
    if(args.containsKey("CustomPotionColor")) {
    	
    	NBTTagInt CustomPotionColor = new NBTTagInt((int) args.get("CustomPotionColor"));
    	tag.set("CustomPotionColor", CustomPotionColor);
    }//CustomPotionColor

    
    /*
     * CustomPotionEffects
     */
    
    if(args.containsKey("CustomPotionEffects")) {
    	
    	NBTTagList effects = new NBTTagList();
    	
    	ArrayList<Map<String,Object>> e = (ArrayList<Map<String, Object>>) args.get("CustomPotionEffects");
    	
    	for(Map<String,Object> tmp : e) {
    		NBTTagCompound t = new NBTTagCompound();
    		
    		t.set("Id", new NBTTagByte((byte) tmp.get("Id")));
    		t.set("Amplifier", new NBTTagByte( (byte) tmp.get("Amplifier")));
    		t.set("Duration", new NBTTagInt((int) tmp.get("Duration")));
    		t.set("Ambient", new NBTTagByte((byte) tmp.get("Ambient")));
    		t.set("ShowIcon", new NBTTagByte((byte) tmp.get("ShowIcon")));
    		t.set("ShowParticles", new NBTTagByte((byte) tmp.get("ShowParticles")));
    		
    		effects.add(t);
    	}
    	
    	
    	tag.set("CustomPotionEffects", effects);
    	
    }//CustomPotionEffects
    
    /*
     * Potion
     */
    
    if(args.containsKey("Potion")) {
    	
    	tag.set("Potion", new NBTTagString((String) args.get("Potion")));
    	
    }//Potion
    
    /*
     * RepairCost
     */
    
    if(args.containsKey("RepairCost")) {
    	
    	tag.set("RepairCost", new NBTTagInt((int) args.get("RepairCost")));
    	
    }//RepairCost
    
    /*
     * HideFlags
     */
    
    if(args.containsKey("HideFlags")) {
    	
    	tag.set("HideFlags", new NBTTagInt((int) args.get("HideFlags")));
    	
    }//HideFlags
    
    /*
     * CanDestroySection
     */
    
    if(args.containsKey("CanDestroy")) {
    	
        NBTTagList candestroy = new NBTTagList();
        
        ArrayList<String> d = (ArrayList<String>) args.get("CanDestroy");
        
        for(String index : d) {
        	candestroy.add(new NBTTagString(index));
        }
        
        
        tag.set("CanDestroy", candestroy);
        
    }//CanDestroy
    
    /*
     * CanPlaceOnSection
     */
    
    if(args.containsKey("CanPlaceOn")) {
    	
        NBTTagList canplace = new NBTTagList();
        
        ArrayList<String> c = (ArrayList<String>) args.get("CanPlaceOn");
        
        for(String index : c) {
        	canplace.add(new NBTTagString(index));
        }
        
        
        tag.set("CanPlaceOn", canplace);
        
    }//CanPlaceOn
    
    /*
     * AttributeModifiers
     */
    
    if(args.containsKey("AttributeModifiers")) {
    	
    	NBTTagList attributes = new NBTTagList();
    	
    	ArrayList<Map<String,Object>> at = (ArrayList<Map<String, Object>>) args.get("AttributeModifiers");
    	
    	for(Map<String,Object> tmp : at) {
    		
    		NBTTagCompound e = new NBTTagCompound();
    		
    		e.set("Name",new NBTTagString((String) tmp.get("Name")));
    		e.set("AttributeName",new NBTTagString((String) tmp.get("AttributeName")));
    		e.set("Amount",new NBTTagInt((int) tmp.get("Amount")));
    		e.set("Operation",new NBTTagInt((int) tmp.get("Operation")));
    		e.set("UUIDMost",new NBTTagInt((int) tmp.get("UUIDMost")));
    		e.set("UUIDLeast",new NBTTagInt((int) tmp.get("UUIDLeast")));
    		e.set("Slot",new NBTTagString((String) tmp.get("Slot")));
    		
    		attributes.add(e);
    	}
    	
    	
    	tag.set("AttributeModifiers", attributes);
    }//AttributeModifiers
    
    
    
    
    if (!args.containsKey("metatype")) {
        return result; 
    }
    
    
    /*
     * Skull
     */
    if (args.get("metatype").equals("SKULL")) {
    	
    	if(args.containsKey("SkullOwner")) {
    		
    		NBTTagCompound skullownerNBT = new NBTTagCompound();
    		
    		Map<String,Object> skullowner = (Map<String, Object>) args.get("SkullOwner");
    		
    		if(skullowner.containsKey("Id")) {
    			skullownerNBT.set("Id", new NBTTagString((String) skullowner.get("Id")));
    		}
    		
    		if(skullowner.containsKey("Name")) {
    			skullownerNBT.set("Name", new NBTTagString((String) skullowner.get("Name")));
    		}
    		
    		if(skullowner.containsKey("Properties")) {
    			
    			Map<String,Object> properties = (Map<String, Object>) skullowner.get("Properties");
    			
    			NBTTagCompound propertiesNBT = new NBTTagCompound();
    			
    			if(properties.containsKey("textures")) {
    				
    				ArrayList<Map<String,Object>> textures = (ArrayList<Map<String, Object>>) properties.get("textures");
    				
    				NBTTagList texturesNBT = new NBTTagList();
    				
    				for(Map<String,Object> index : textures) {
    					
    					NBTTagCompound i = new NBTTagCompound();
    					
    					if(index.containsKey("Signature")) {
    						i.set("Signature", new NBTTagString((String) index.get("Signature")));
    					}
    					
    					if(index.containsKey("Value")) {
    						i.set("Value", new NBTTagString((String) index.get("Value")));
    					}
    					
    					texturesNBT.add(i);
    					
    				}
    				
    				propertiesNBT.set("textures", texturesNBT);
    				
    				
    				
    			}
    			
    			skullownerNBT.set("Properties", propertiesNBT);
    			
    		}
    		
    		tag.set("SkullOwner", skullownerNBT);
    		
    	}
    }//skull
    
    
    /*
     * enchantmentstorage
     */
    if (args.get("metatype").equals("ENCHANTMENTSTORAGE")) {
    	
    	if(args.containsKey("StoredEnchantments")) {
    		ArrayList<String> enchants = (ArrayList<String>) args.get("StoredEnchantments");
    		
    		NBTTagList en = new NBTTagList();
    		
    		for(String index : enchants) {
    			
    			NBTTagCompound test = new NBTTagCompound();
    			
    			String[] indexsplit = index.split(":");
    			
    			test.set("id", new NBTTagInt(Integer.parseInt(indexsplit[0])));
    			test.set("lvl", new NBTTagInt(Integer.parseInt(indexsplit[1])));
    			
    			en.add(test);
    		}
    		
    		tag.set("StoredEnchantments", en);
    		
    	}
    	
    }//Enchantmentstoreage
    
    
    /*
     * BlockEntityTag
     */
    if(args.containsKey("BlockEntityTag")) {
    	
    	NBTTagCompound blockentitytag = new NBTTagCompound();
    	
    	Map<String, Object> block = (Map<String, Object>) args.get("BlockEntityTag");
    	
        /*
         * banner
         */
        if (args.get("metatype").equals("BANNER")) {
        	
        	
        	if(block.containsKey("base-color")) {
        		blockentitytag.set("base-color", new NBTTagString((String) args.get("CustomName")));
        	}
        	
        	
        	if(block.containsKey("Patterns")) {
        		
        		ArrayList<String> info = (ArrayList<String>) block.get("Patterns");
        		
        		NBTTagList patterns = new NBTTagList();
        		
        		for(String index : info) {
        			
        			NBTTagCompound i = new NBTTagCompound();
        			
        			String[] indexsplit = index.split(":");
        			
        			i.set("Color", new NBTTagInt(Integer.parseInt(indexsplit[1])));
        			i.set("Pattern", new NBTTagString(indexsplit[0]));
        			
        			patterns.add(i);
        		}
        		
        		blockentitytag.set("Patterns", patterns);
        		
        	}
        	
        }//banner
        
        tag.set("BlockEntityTag", blockentitytag);
    	
    }
    
    
    
    
    /*
     * book
     */
    if (args.get("metatype").equals("BOOK")) {
    	
    	
    	
    	if(args.containsKey("resolved")) {
    		tag.set("resolved", new NBTTagByte((byte) args.get("resolved")));
    	}
    	
    	if(args.containsKey("generation")) {
    		tag.set("generation", new NBTTagInt((int) args.get("generation")));
    	}
    	
    	if(args.containsKey("author")) {
    		tag.set("author", new NBTTagString((String) args.get("author")));
    	}
    	
    	if(args.containsKey("title")) {
    		tag.set("title", new NBTTagString((String) args.get("title")));
    	}
    	
    	if(args.containsKey("pages")) {
    		ArrayList<String> pages = (ArrayList<String>) args.get("pages");
    		
    		NBTTagList t = new NBTTagList();
    		
    		for(String index : pages) {
    			t.add(new NBTTagString(index));
    		}
    		
    		tag.set("pages", t);
    		
    	}
    	
    	
    /*
    	
      if (args.containsKey("object2")) {
        BookMeta book = (BookMeta)result.getItemMeta();
        
        String end = args.get("object2").toString();
        
        String[] endsplit = end.split("<!html><html>");
        
        for(String index : endsplit) {
        	book.addPage(index.replaceAll("<html>", "").replaceAll("<!html>", ""));
        }
        byte b;
        int i;
        String[] arrayOfString;
        for (i = (arrayOfString = ((String)args.get("pages")).split(":")).length, b = 0; b < i; ) {
          String index = arrayOfString[b];
          book.addPage(new String[] { index });
          b++;
        } 
        
        
        result.setItemMeta(book);
      } //object2
      if (args.containsKey("object")) {
        BookMeta book = (BookMeta)result.getItemMeta();
        String author = ((String)args.get("object")).split(",")[0];
        String title = ((String)args.get("object")).split(",")[1];
        String generation = ((String)args.get("object")).split(",")[2];
        book.setAuthor(author);
        book.setTitle(title);
        book.setGeneration(BookMeta.Generation.valueOf(generation));
        result.setItemMeta(book);
      } //object
      
      */
      
    } //book
    
    if(args.get("metatype").equals("FIREWORK")) {
    	
    	NBTTagCompound fireworks = new NBTTagCompound();
    	
    	Map<String,Object> fi = (Map<String, Object>) args.get("Fireworks");
    	
    	if(fi.containsKey("Flight")) {
    		fireworks.set("Flight", new NBTTagByte((byte) fi.get("Flight")));
    	}
    	
    	if(fi.containsKey("Explosions")) {
    		ArrayList<LinkedHashMap<String,Object>> ex = (ArrayList<LinkedHashMap<String, Object>>) fi.get("Explosions");
    		
    		NBTTagList explosions = new NBTTagList();
    		
    		for(LinkedHashMap<String,Object> tmp : ex) {
    			
    			NBTTagCompound tmpex = new NBTTagCompound();
    			
            	if(tmp.containsKey("Flicker")) {
            		tmpex.set("Flicker", new NBTTagByte((byte) tmp.get("Flicker")));
            	}
            	
            	if(tmp.containsKey("Trail")) {
            		tmpex.set("Trail", new NBTTagByte((byte) tmp.get("Trail")));
            	}
            	
            	if(tmp.containsKey("Type")) {
            		tmpex.set("Type", new NBTTagByte((byte) tmp.get("Type")));
            	}
            	
            	if(tmp.containsKey("Colors")) {
            		tmpex.set("Colors", new NBTTagIntArray( (int[]) tmp.get("Colors")));
            	}
            	
            	if(tmp.containsKey("FadeColors")) {
            		tmpex.set("FadeColors", new NBTTagIntArray( (int[]) tmp.get("FadeColors")));
            	}
    			
    			explosions.add(tmpex);
    			
    		}
    		
    		fireworks.set("Explosions", explosions);
    		
    	}
    	
    	tag.set("Fireworks", fireworks);
    	
    	/*
    	if(args.containsKey("object")) {
    		byte flight = (byte) args.get("object");
    		fireworks.set("Flight", new NBTTagByte(flight));
    	}
    	
    	if(args.containsKey("object2")) {
    		
    		String[] ex = args.get("object2").toString().split("§§");
    		
    		NBTTagList explosions = new NBTTagList();
    		
    		for(String index : ex) {
    			
    			NBTTagCompound i = new NBTTagCompound();
    			
    			String[] indexsplit = index.split("§");
    			
    			for(String t : indexsplit) {
    				String[] tsplit  = t.split(":");
    				
    				if(tsplit[0].equals("Flicker")) {
    					
    					NBTTagByte flicker = new NBTTagByte(Byte.parseByte(tsplit[1]));
    					i.set("Flicker", flicker);
    					
    				}else if(tsplit[0].equals("Trail")) {
    					
    					NBTTagByte trail = new NBTTagByte(Byte.parseByte(tsplit[1]));
    					i.set("Trail", trail);
    					
    				}else if(tsplit[0].equals("Type")) {
    					
    					NBTTagByte ty = new NBTTagByte(Byte.parseByte(tsplit[1]));
    					i.set("Type", ty);
    				
    				}else if(tsplit[0].equals("Colors")) {
    				
    					String[] split = tsplit[1].split(",");
    					
    					int[] in = new int[split.length];
    					
    					System.out.println(split.length + " " + in.length);
    					
    					for(int x = 0; x < split.length; x++) {
    						in[x] = Integer.parseInt(split[x]);
    					}
    					
    					NBTTagIntArray colors = new NBTTagIntArray(in);
    					
    					i.set("Colors", colors);
    					
    				}else if(tsplit[0].equals("FadeColors")) {
    					
    					String[] split = tsplit[1].split(",");
    					
    					int[] in = new int[split.length];
    					
    					for(int x = 0; x < split.length; x++) {
    						in[x] = Integer.parseInt(split[x]);
    					}
    					
    					NBTTagIntArray fade = new NBTTagIntArray(in);
    					
    					i.set("FadeColors", fade);
    					
    				}else {
    					
    				}
    			}
    			
    			explosions.add(i);
    			
    		}
    		
    		fireworks.set("Explosions", explosions);
    	}
    	*/
    	
    	tag.set("Fireworks", fireworks);
    	
    }//Firework
    
    if(args.get("metatype").equals("FIREWORKEFFECT")) {
    	
    	
    	if(args.containsKey("Explosion")) {
    		
        	NBTTagCompound explosion = new NBTTagCompound();
    		
        	LinkedHashMap<String,Object> ex = (LinkedHashMap<String, Object>) args.get("Explosion");
        	
        	if(ex.containsKey("Flicker")) {
        		explosion.set("Flicker", new NBTTagByte((byte) ex.get("Flicker")));
        	}
        	if(ex.containsKey("Trail")) {
        		explosion.set("Trail", new NBTTagByte((byte) ex.get("Trail")));
        	}
        	if(ex.containsKey("Type")) {
        		explosion.set("Type", new NBTTagByte((byte) ex.get("Type")));
        	}
        	if(ex.containsKey("Colors")) {
        		explosion.set("Colors", new NBTTagIntArray((int[]) ex.get("Colors")));
        	}
        	if(ex.containsKey("FadeColors")) {
        		explosion.set("FadeColors", new NBTTagIntArray((int[]) ex.get("FadeColors")));
        	}
        	
        	tag.set("Explosion", explosion);
    	}
    	
    	/*
    	String[] split = args.get("object").toString().split("§");
    	
    	for(String index : split) {
    		
    		String[] indexsplit = index.split(":");
    		
    		if(indexsplit[0].equals("Flicker")) {
    			
    			NBTTagByte flicker = new NBTTagByte(Byte.parseByte(indexsplit[1]));
    			explosion.set("Flicker", flicker);
    			System.out.println("find Flicker " + flicker);
    			
    		}else if(indexsplit[0].equals("Trail")) {
    		
    			NBTTagByte trail = new NBTTagByte(Byte.parseByte(indexsplit[1]));
    			explosion.set("Trail", trail);
    			System.out.println("find trail " + trail);
    			
    		}else if(indexsplit[0].equals("Type")) {
    		
    			NBTTagByte ty = new NBTTagByte(Byte.parseByte(indexsplit[1]));
    			explosion.set("Type", ty);
    			System.out.println("find type " + ty);
    			
    		}else if(indexsplit[0].equals("Colors")) {
    		
    			String[] s = indexsplit[1].split(",");
    			
    			int[] end = new int[s.length];
    			
    			for(int x = 0; x < s.length; x++) {
    				end[x] = Integer.parseInt(s[x]);
    			}
    			
    			NBTTagIntArray trail = new NBTTagIntArray(end);
    			explosion.set("Colors", trail);
    			
    		}else if(indexsplit[0].equals("FadeColors")) {
    			
    			String[] s = indexsplit[1].split(",");
    			
    			int[] end = new int[s.length];
    			
    			for(int x = 0; x < s.length; x++) {
    				end[x] = Integer.parseInt(s[x]);
    			}
    			
    			NBTTagIntArray trail = new NBTTagIntArray(end);
    			explosion.set("FadeColors", trail);
    			
    		}else {
    			
    		}
    	}
    	
    	tag.set("Explosion", explosion);
    	*/
    	
    }//Fireworkeffect
    
    if(args.get("metatype").equals("MAP")) {
    	
    	if(args.containsKey("map")) {
    		tag.set("map", new NBTTagInt((int) args.get("map")));
    	}
    	
    	if(args.containsKey("map_scale_direction")) {
    		tag.set("map_scale_direction", new NBTTagInt((int) args.get("map_scale_direction")));
    	}
    	
    	if(args.containsKey("Decorations")) {
    		
    		ArrayList<LinkedHashMap<String,Object>> dec = (ArrayList<LinkedHashMap<String, Object>>) args.get("Decorations");
    		
    		NBTTagList decorations = new NBTTagList();
    		
    		for(LinkedHashMap<String,Object> tmp : dec) {
    			
    			NBTTagCompound t = new NBTTagCompound();
    			
    			if(tmp.containsKey("type")) {
    				t.set("type", new NBTTagByte((byte) tmp.get("type")));
    				
    			}else if(tmp.containsKey("x")) {
    				t.set("x", new NBTTagByte((byte) tmp.get("x")));
    				
    			}if(tmp.containsKey("z")) {
    				t.set("z", new NBTTagByte((byte) tmp.get("z")));
    				
    			}if(tmp.containsKey("rot")) {
    				t.set("rot", new NBTTagByte((byte) tmp.get("rot")));
    				
    			}if(tmp.containsKey("id")) {
    				t.set("id", new NBTTagString((String) tmp.get("id")));
    			}
    			
    			decorations.add(t);
    		}
    		
    		tag.set("Decorations", decorations);
    		
    	}
    	
    	/*
    	if(args.containsKey("object")) {
    		String[] objectsplit = ((String)args.get("object")).split(",");
    		
    		for(String index : objectsplit) {
    			String[] indexsplit = index.split(":");
    			
    			if(indexsplit[0].equals("map")) {
    				tag.set("map", new NBTTagInt(Integer.parseInt(indexsplit[1])));
    			}else if(indexsplit[0].equals("map_scale_direction")) {
    				tag.set("map_scale_direction", new NBTTagInt(Integer.parseInt(indexsplit[1])));
    			}else if(indexsplit[0].equals("mapcolor")) {
    				
    				NBTTagCompound display;
    				
    				if(tag.hasKey("display")) {
    					display = (NBTTagCompound) tag.get("display");
    				}else {
    					display = new NBTTagCompound();
    				}
    				display.set("MapColor", new NBTTagInt(Integer.parseInt(indexsplit[1])));
    			}
    		}
    	}//object
    	
    	
    	
    	if(args.containsKey("object2")) {
    		
    		NBTTagList decorations = new NBTTagList();
    		
    		String[] split = ((String)args.get("object2")).split("§");
    		
    		for(String index : split) {
    			
    			NBTTagCompound i = new NBTTagCompound();
    			
    			String[] indexsplit = index.split(",");
    			
    			for(String f : indexsplit) {
    				
    				String[] fsplit = f.split(":");
    				
    				if(fsplit[0].equals("id")) {
    					i.set("id", new NBTTagString(fsplit[1]));
    				}else if(fsplit[0].equals("type")) {
    					i.set("type", new NBTTagByte(Byte.parseByte(fsplit[1])));
    				}else if(fsplit[0].equals("x")) {
    					i.set("x", new NBTTagDouble(Double.parseDouble(fsplit[1])));
    				}else if(fsplit[0].equals("z")) {
    					i.set("z", new NBTTagDouble(Double.parseDouble(fsplit[1])));
    				}else if(fsplit[0].equals("rot")) {
    					i.set("rot", new NBTTagDouble(Double.parseDouble(fsplit[1])));
    				}
    				
    			}
    			
    			decorations.add(i);
    		}
    		
    		
    		tag.set("Decorations", decorations);
    		
    	}//object2
    	*/
    	
    }//MAP
    
    
    
    /*
     * end of NBTTag
     */
    
    nmscopy.setTag(tag);
    
    result = CraftItemStack.asBukkitCopy(nmscopy);
    
    return result;
  }



  
}


