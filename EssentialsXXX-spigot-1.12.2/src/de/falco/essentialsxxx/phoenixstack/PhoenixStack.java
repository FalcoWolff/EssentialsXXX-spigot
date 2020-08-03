package de.falco.essentialsxxx.phoenixstack;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import de.falco.essentialsxxx.phoenixstack.datatypes.AttributeModifier;
import de.falco.essentialsxxx.phoenixstack.datatypes.CustomPotionEffect;
import de.falco.essentialsxxx.phoenixstack.datatypes.Enchant;
import de.falco.essentialsxxx.phoenixstack.datatypes.Texture;
import de.falco.essentialsxxx.phoenixstack.datatypes.Enchant.Ench;
import de.falco.essentialsxxx.phoenixstack.metatypes.Book;
import de.falco.essentialsxxx.phoenixstack.metatypes.Firework;
import de.falco.essentialsxxx.phoenixstack.metatypes.FireworkExplosion;
import de.falco.essentialsxxx.phoenixstack.metatypes.PlayerHead;
import de.falco.essentialsxxx.phoenixstack.metatypes.Potion;
import de.falco.essentialsxxx.phoenixstack.metatypes.StoredEnchantment;
import de.falco.essentialsxxx.phoenixstack.metatypes.Potion.PotionEffect;
import de.falco.essentialsxxx.util.MySql;
import net.minecraft.server.v1_12_R1.Item;
import net.minecraft.server.v1_12_R1.NBTTagByte;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagInt;
import net.minecraft.server.v1_12_R1.NBTTagList;
import net.minecraft.server.v1_12_R1.NBTTagString;
import net.minecraft.server.v1_12_R1.RegistryMaterials;

/*
 * @Info alternative to the Spigot or Bukkit Itemstack class
 * 		 use it to set attributes in 1.12.2
 * 		 also useable when you want to save items in your database
 * @see de.falco.essentialsxxx.phoenixstack.*
 * @Author FalcoWolf
 * @Copyright Phoenixgames.net
 * @History 
 * day 1 = new 1800 lines now 1800 lines / Structur and all basic classes
 * day 2 = new 500 lines now 2300 lines / potion metatype
 * day 3 = new 200 lines now 2500 lines / bugfixes
 * day 4 = new 500 lines now 3000 lines /Color and FireworkExplosion class
 * day 5 = new 600 lines now 3600 lines /Firework class and bugfixes 
 * ...
 * now: 5104
 * 
 * @Attention dont support following items or attributes:
 * 			- blockdata like banner pattern or items in workbench
 * 			- spawner and spawneggs
 */
public class PhoenixStack {
	
    /*
     * >> variables <<
     */
	
	/*
	 * fields for default options
	 */
	private int count;
	private int id;
	private int damage;
	
	
	/*
	 * type to save the meta data
	 */
	private PhoenixMeta meta;
	
    /*
     * >> constructor's <<
     */
	
	/*
	 * @Info constructor 
	 * @param ItemStack from spigot-api you want to cast to Phoenixstack
	 * @see de.falco.phoenixstack.PhoenixStackmeta(NBTTagCompound tag)
	 */
   	public PhoenixStack(org.bukkit.inventory.ItemStack item) {
		
		net.minecraft.server.v1_12_R1.ItemStack tag = CraftItemStack.asNMSCopy(item);
		
		NBTTagCompound t;
		if(tag.getTag() != null) {
			t = tag.getTag();
		}else {
			t = new NBTTagCompound();
		}
		
		System.out.println("keys " + t.c());
		
		
		System.out.println("---");
		
		System.out.println("tag: " + t);
		
		if(t.hasKey("Potion")) {
			meta = new Potion(t);
		}else if(t.hasKey("Fireworks")) {
			meta = new Firework(t);
		}else if(t.hasKey("Explosion")) {
			meta = new FireworkExplosion(t);
		}else if(t.hasKey("SkullOwner")) {
			meta = new PlayerHead(t);
		}else if(t.hasKey("resolved") || t.hasKey("generation") || t.hasKey("author") || t.hasKey("title") || t.hasKey("pages")) {
			meta = new Book(t);
		}else if(t.hasKey("StoredEnchantments")) {
			meta = new StoredEnchantment(t);
		}else {
			meta = new PhoenixMeta(t);	
		}
		
		this.damage = tag.getData();
		this.count = tag.getCount();
		this.id = Item.getId(tag.getItem());
		
		
	}
	
	/*
	 * @Info constructor
	 * @param the Material from spigot-api
	 */
	public PhoenixStack(Material material) {
		
		this.count = 1;
		this.id = material.getId();
		this.damage = 0;
		
		meta = new PhoenixMeta();
		
	}
	
	/*
	 * @Info constructor
	 * @param the Material
	 * @param count as amount of the item
	 */
	public PhoenixStack(Material material, int count) {
		
		
		this.count = count;
		this.id = material.getId();
		this.damage = 0;
		
		meta = new PhoenixMeta();
		
	}
	
	/*
	 * @Info constructor
	 * @param the material
	 * @param count as amount of the item
	 * @param damage of the item (NOT THE REAL DAMAGE in this case the metadata)
	 */
	public PhoenixStack(Material material, int count, int damage) {
		this.count = count;
		this.id = material.getId();
		this.damage = damage;
		
		meta = new PhoenixMeta();
	}
	
	/*
	 * >> methods <<
	 */
	
	/*
	 * @Info convernt a PhoenixStack in a ItemStack from spigot-api
	 * @return ItemStack you create in methode based on the PhoenixStack information
	 * @see de.falco.essentialsxxx.phoenixstack.PhoenixMeta.deserialize()
	 */
	public ItemStack toItemStack() {
		
		if(this.id == 0 || this.damage < 0) {
			throw new NullPointerException("[essentialsxxx] error while trying to look for item id or damage");
		}
		
		ItemStack re = new ItemStack(this.id, count);
		
		net.minecraft.server.v1_12_R1.ItemStack tmp = CraftItemStack.asNMSCopy(re);
		
		NBTTagCompound tag = meta.deserialize();
		
		tmp.setTag(tag);
		tmp.setData(this.damage);
		
		re = CraftItemStack.asBukkitCopy(tmp);
		
		return re;
		
	}
	
	/*
	 * >> static <<
	 */
	
	public static void generatespezialtables(MySql mysql) {
		
		  /*
		   * >> tables for helpfully datatypes
		   */
		  
		  /*
		   * AttributeModifiers
		   */
		  
		  AttributeModifier.createtable(mysql, "attributemodifiers");
		  
		  
		  /*
		   * CustomPotionEffect
		   */
		  
		  CustomPotionEffect.createtable(mysql, "custompotioneffect");
		  
		  /*
		   * FireworkExplosion
		   */
		  
		  FireworkExplosion.createtable(mysql, "fireworkexplosion");
		  
		  /*
		   * enchant
		   */
		  
		  Enchant.createtable(mysql, "enchant");
		  
		  
		  /*
		   * >> meta types <<
		   */
		  
		  /*
		   * PlayerHead
		   */
		  
		  PlayerHead.createtable(mysql, "playerhead");
		  
		  /*
		   * potion
		   */
		  
		  Potion.createtable(mysql, "potion");
		  
		  /*
		   * Firework
		   */
		  
		  Firework.createtable(mysql, "firework");
		    
		  /*
		   * Texture
		   */
		  
		  Texture.createtable(mysql, "texture");
		  
		  /*
		   * Book
		   */
		  
		  Book.createtable(mysql, "book");
		
		  /*
		   * StoredEnchantment
		   */
		  
		  StoredEnchantment.createtable(mysql, "storedenchantment");
		  
	}
	
	/*
	 * @Info create default table for normal item
	 * @param tablename
	 * @param mysql connection
	 */
	public static void generatetable(String tablename, MySql mysql) {
		  
		  /*
		   * >> default table <<
		   */
		  String command = "CREATE TABLE IF NOT EXISTS " + tablename +
		  		  " (id INT Primary Key, "
		  		  + " type INT, "
		  		  + " damage INT, "
			      + " amount TINYINT, "
		  		  
			      + " unbreakable TINYINT, "
			      
			      + " repaircost INT, "
			      + " enchant TEXT, "
			      
			      + " hideflags INT, "
			      + " attribute TEXT, "
			      
			      + " candestroy TEXT, "
			      + " canplaceon TEXT, "
			      
			      + " displayname TEXT, "
			      + " lore TEXT, "
			      + " color INT, "
			      
			      + " metatype VARCHAR(100), "
			      + " objectid TEXT " + 
			      ");";
		    
		  mysql.command(command);
		  
		    
	}
	
	/*
	 * @Info save an item in database
	 * @param the id of the item
	 * @param mysql connection
	 * @param tablename
	 */
	public static void save(PhoenixStack ph, int id, MySql mysql, String tablename) {
		
		int typetmp = ph.id;
		int counttmp = ph.count;
		int damage = ph.damage;
		
		int unbreakabletmp = ph.meta.getUnbreakable();
		
		int repaircosttmp = ph.meta.getRepaircost();
		
		String enchanttmp = null;
		
		int hideflags = ph.meta.getHideflags();
		
		String candestroy = null;
		String canplaceon = null;
		
		String display = ph.meta.getDisplay_name();
		String lore = null;
		int color = ph.meta.getDisplay_color();
		
		String attributes = null;
		
		ResultSet result = mysql.getResult("SELECT id FROM " + tablename + " WHERE id=" + id);
		try {
			
			if(result.next()) {
				try {
					PhoenixStack.delete(id, mysql, tablename);
				} catch (PhoenixStackException e) {
					e.printStackTrace();
				}
			}
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		
		/*
		 * AttributeModifiers
		 */
		
		if(ph.meta.getAttributemodifiers().isEmpty() == false) {
			
			StringBuffer str = new StringBuffer();
			
			int number = -1;
			
			String command = "SELECT id FROM attributemodifiers";
			ResultSet re = mysql.getResult(command);
			
			try {
				if(re.last()) {
					number = re.getInt(1);
				}
			} catch (SQLException e) {
				
			}
			
			for(AttributeModifier at : ph.meta.getAttributemodifiers()) {
				
				number += 1;
				
				str.append("§" + number);
				
				AttributeModifier.save(at, number, mysql, "attributemodifier");
				
				
				
			}
			
			attributes = str.toString().replaceFirst("§", "");
			
		}
		
		/*
		 * lore
		 */
		
		if(ph.meta.getDisplay_lore().isEmpty() == false) {
			StringBuilder str = new StringBuilder();
			
			for(String i : ph.meta.getDisplay_lore()) {
				str.append("§" + i);
			}
			
			lore = str.toString().replaceFirst("§", "");
		}
		
		/*
		 * CanDestroy
		 */
		if(ph.meta.getCandestroy().isEmpty() == false) {
			
			StringBuilder str = new StringBuilder();
			
			for(String i : ph.meta.getCandestroy()) {
				str.append("§" + i);
			}
			
			candestroy = str.toString().replaceFirst("§", "");
			
		}
		
		/*
		 * CanPlaceOn
		 */
		
		if(ph.meta.getCanplaceon().isEmpty() == false) {
			
			StringBuilder str = new StringBuilder();
			
			for(String i : ph.meta.getCanplaceon()) {
				str.append("§" + i);
			}
			
			canplaceon = str.toString().replaceFirst("§", "");
			
		}
		
		/*
		 * Ench
		 */
		
		if(ph.meta.getEnchantments().isEmpty() == false) {
			
			StringBuilder str = new StringBuilder();
			
			for(Enchant i: ph.meta.getEnchantments()) {
				
				int idtmp = i.getId().getenchid();
				int lvltmp = i.getLvl();
				
				str.append("§" + idtmp + ":" + lvltmp);
			}
			
			enchanttmp = str.toString().replaceFirst("§", "");
			
		}
		
		/*
		 * >> meta <<
		 */
		
		String metatype = null;
		
		String objectid = null;
		
		if(ph.getMeta() instanceof Potion) {
			
			Potion potion = (Potion) ph.getMeta();
			
			String command = "SELECT id FROM potion ORDER BY id";
			ResultSet re = mysql.getResult(command);
			
			metatype = "potion";
			
			int number = 0;
			
			try {
				if(re.last()) {
					
					number = re.getInt(1) + 1;
					
				}
			} catch (SQLException e) {
			}
			
			objectid = number + "";
			
			
			Potion.save(number, mysql, "potion", potion);
			
			
			
		}else if(ph.getMeta() instanceof Firework) {
			
			Firework meta = (Firework) ph.getMeta();
			
			metatype = "firework";
			
			
			String command = "SELECT id FROM firework";
			ResultSet re = mysql.getResult(command);
			
			int number = 0;
			
			try {
				if(re.last()) {
					number = re.getInt(1) + 1;
				}
			} catch (SQLException e) {
			}
			
			objectid = number + "";
			
			Firework.save(number, mysql, "firework", meta);
			
		}else if(ph.getMeta() instanceof FireworkExplosion) {
			
			FireworkExplosion meta = (FireworkExplosion) ph.getMeta();
			
			metatype = "fireworkexplosion";
			
			String command = "SELECT id FROM fireworkexplosion";
			ResultSet re = mysql.getResult(command);
			
			int number = 0;
			
			try {
				if(re.last()) {
					number = re.getInt(1) + 1;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			objectid = number + "";
			
			FireworkExplosion.save(meta, number, mysql, "fireworkexplosion");
			
		}else if(ph.getMeta() instanceof PlayerHead) {
			
			PlayerHead meta = (PlayerHead) ph.getMeta();
			
			metatype = "playerhead";
			
			String command = "SELECT id FROM playerhead";
			ResultSet re = mysql.getResult(command);
			
			int number = 0;
			
			try {
				if(re.last()) {
					number = re.getInt(1) + 1;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			objectid = number + "";
			
			PlayerHead.save(meta, number, mysql, "playerhead");
		
		}else if(ph.getMeta() instanceof Book) {
			
			Book meta = (Book) ph.getMeta();
			
			metatype = "book";
			
			String command = "SELECT id FROM book";
			ResultSet re = mysql.getResult(command);
			
			int number = 0;
			
			try {
				if(re.last()) {
					number = re.getInt(1) + 1;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			objectid = number + "";
			
			Book.save(meta, number, mysql, "book");
			
		}else if(ph.getMeta() instanceof StoredEnchantment) {
			
			StoredEnchantment meta = (StoredEnchantment) ph.getMeta();
			
			metatype = "storedenchantment";
			
			String command = "SELECT id FROM storedenchantment";
			ResultSet re = mysql.getResult(command);
			
			int number = 0;
			
			try {
				if(re.last()) {
					number = re.getInt(1) + 1;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			objectid = number + "";
			
			StoredEnchantment.save(meta, mysql, number, "storedenchantment");
			
		}else {//potion
			
			metatype = "unknown";
			
		}
		
		
		/*
		 * finish
		 */
		
		if(enchanttmp != null) {
			enchanttmp = "'" + enchanttmp + "'";
		}
		if(attributes != null) {
			attributes = "'" + attributes + "'";
		}
		if(candestroy != null) {
			candestroy = "'" + candestroy + "'";
		}
		if(canplaceon != null) {
			canplaceon = "'" + canplaceon + "'";
		}
		if(display != null) {
			display = "'" + display + "'";
		}
		if(lore != null) {
			lore = "'" + lore + "'";
		}
		if(objectid != null) {
			objectid = "'" + objectid + "'";
		}
		
		String command = "INSERT INTO " + tablename + " ("
				+ "id,"
				+ "type,"
				+ "damage,"
				+ "amount,"
				+ "unbreakable,"
				+ "repaircost,"
				+ "enchant,"
				+ "hideflags,"
				+ "attribute,"
				+ "candestroy,"
				+ "canplaceon,"
				+ "displayname,"
				+ "lore,"
				+ "color,"
				+ "metatype,"
				+ "objectid"
				+ ") VALUES ("
				+ "" + id
				+ "," + typetmp
				+ "," + damage
				+ "," + counttmp
				+ "," + unbreakabletmp
				+ "," + repaircosttmp
				+ "," + enchanttmp
				+ "," + hideflags
				+ "," + attributes
				+ "," + candestroy
				+ "," + canplaceon
				+ "," + display
				+ "," + lore
				+ "," + color
				+ ",'" + metatype + "'"
				+ "," + objectid
				+ ""
				+ ");";
		
		System.out.println("[essentialsxxx-phoenixstack] save item with command: ");
		System.out.println("[essentialsxxx-phoenixstack] " + command);
		
		mysql.command(command);
		
		
		
	}
	
	/*
	 * @Info load an item from a database
	 * @param id of the item
	 * @param mysql connection
	 * @param tablename
	 */
	public static PhoenixStack load(int id, MySql mysql, String tablename) throws PhoenixStackException {
		
		ResultSet res = mysql.getResult("SELECT * FROM " + tablename + " WHERE id=" + id);
		
		try {
			if(!res.next()) {
				throw new PhoenixStackException("[essentialsxxx-phoenixstack] couldnt find item with id " + id);
			}
			
			
			int type = res.getInt(2);
			int count = res.getInt(4);
			int damage = res.getInt(3);
			
			byte unbreakable = res.getByte(5);
			
			int repaircost = res.getInt(6);
			
			String enchantments = res.getString(7);
			
			int hideflags = res.getInt(8);
			
			String attributemodifiers = res.getString(9);
			
			String candestroy = res.getString(10);
			String canplaceon = res.getString(11);
			
			String displayname = res.getString(12);
			String lore = res.getString(13);
			int color = res.getInt(14);
			
			String metatype = res.getString(15);
			String objectid = res.getString(16);
			
			int idtmp = 0;
			
			if(objectid != null) {
			
				try {
					idtmp = Integer.parseInt(objectid);
				}catch(NumberFormatException ex) {
					ex.printStackTrace();
					
					throw new PhoenixStackException("[essentialsxxx-phoenixstack] couldnt cast " + objectid + " to int");
				}
				
			}
			
			
			
			
			PhoenixStack re = new PhoenixStack(Material.getMaterial(type),count,damage);
			
			PhoenixMeta meta = null;
			
			if(metatype.equals("potion")) {
				meta = new Potion();
			}else if(metatype.equals("firework")) {
				meta = new Firework();
			}else if(metatype.equals("fireworkexplosion")) {
				meta = new FireworkExplosion();
			}else if(metatype.equals("playerhead")) {
				meta = new PlayerHead();
			}else if(metatype.equals("book")) {
				meta = new Book();
			}else if(metatype.equals("storedenchantment")) {
				meta = new StoredEnchantment();
			}else {
				meta = new PhoenixMeta();
			}
			
			meta.setRepaircost(repaircost);
			
			meta.setUnbreakable(unbreakable);
			
			meta.setDisplay_color(color);
			meta.setDisplay_name(displayname);
			
			if(hideflags != 0) {
				meta.setHideflags(hideflags);
			}
			
			if(attributemodifiers != null) {
				
				ArrayList<AttributeModifier> attributes = new ArrayList<>();
				
				for(String index : attributemodifiers.split("§")) {
					
					
					int number = Integer.parseInt(index);
					
					AttributeModifier tmp = AttributeModifier.load(number, mysql, "attributemodifier");
					
					attributes.add(tmp);
				}
				
				meta.setAttributemodifiers(attributes);
			}
			
			
			if(lore != null) {
				ArrayList<String> l = new ArrayList<>();
				
				for(String i : lore.split("§")) {
					l.add(i);
				}
				
				meta.setDisplay_lore(l);
			}

			if(candestroy != null) {
				for(String i : candestroy.split("§")) {
					meta.getCandestroy().add(i);
				}	
			}
			
			if(canplaceon != null) {
				for(String i : canplaceon.split("§")) {
					meta.getCanplaceon().add(i);
				}	
			}
			
			
			if(enchantments != null) {
				for(String i : enchantments.split("§")) {
					
					Enchant ench = new Enchant(Integer.parseInt(i.split(":")[1]), Ench.getenchbyid(Integer.parseInt(i.split(":")[0])));
					
					meta.getEnchantments().add(ench);
					
				}
					
			}
			
			
			re.setMeta(meta);
			
			/*
			 * metatype
			 */
			
			
			/*
			 * potion
			 */
			
			if(metatype.equals("potion")) {
				
				Potion potion = (Potion) re.getMeta();
				
				potion = Potion.load(idtmp, mysql, "potion", potion);
				
				re.setMeta(potion);
				
			}else if(metatype.equals("firework")) {
				
				Firework firework = (Firework) meta;
				
				firework = Firework.load(idtmp, mysql, "firework", firework);
				
				re.setMeta(firework);
				
			}else if(metatype.equals("fireworkexplosion")) {
				
				FireworkExplosion fireworkexplosion = (FireworkExplosion) re.getMeta();
				
				fireworkexplosion = FireworkExplosion.load(idtmp, mysql, "fireworkexplosion");
				
				re.setMeta(fireworkexplosion);
				
			}else if(metatype.equals("playerhead")) {
				
				PlayerHead playerhead = (PlayerHead) re.getMeta();
				
				playerhead = PlayerHead.load(playerhead, idtmp, mysql, "playerhead");
				
				re.setMeta(playerhead);
				
			}else if(metatype.equals("book")) {
				
				Book book = (Book) re.getMeta();
				
				book = Book.load(book, idtmp, mysql, "book");
				
				re.setMeta(book);
				
			}else if(metatype.equals("storedenchantment")) {
				
				StoredEnchantment so = (StoredEnchantment) re.getMeta();
				
				so = StoredEnchantment.load(so, mysql, idtmp, "storedenchantment");
				
				re.setMeta(so);
				
			}
			
			return re;
			
			
			
		} catch (SQLException e) {
			throw new PhoenixStackException("[essentialsxxx-phoenixstack] your table is damaged " + tablename);
		}
			
		
	}
	
	/*
	 * @Info delete an item
	 * @param id of the item
	 * @param mysql connection
	 * @param tablename
	 */
	public static void delete(int id, MySql mysql, String tablename) throws PhoenixStackException {
		
		ResultSet result = mysql.getResult("SELECT attribute,metatype,objectid FROM " + tablename + " WHERE id=" + id);
		try {
			if(result.next()) {
				
				String attribute = result.getString(1);
				String metatype = result.getString(2);
				String objectid = result.getString(3);
				
				int object = -1;
				
				if(objectid != null) {
					object = Integer.parseInt(objectid);
				}
				
				if(attribute != null) {
					for(String i : attribute.split("§")) {
						mysql.command("DELETE FROM `attributemodifiers` WHERE id=" + i);
					}
				}
				
				mysql.command("DELETE FROM `" + tablename + "` WHERE id=" + id);
				
				ResultSet re = null;
				
				switch(metatype) {
				
					case "unknown":
					
					break;
					
					case "book":
						
						mysql.command("DELETE FROM `book` WHERE id=" + object);
						
					break;
					
					case "storedenchantment":
						
						re = mysql.getResult("SELECT storedenchantments WHERE id=" + object);
						if(re.next()) {
							String storedenchantments = re.getString(1);
							if(storedenchantments != null) {
								for(String i : storedenchantments.split("§")) {
									mysql.command("DELETE FROM `enchant` WHERE id=" + i);
								}
							}
							mysql.command("DELETE FROM `storedenchantment` WHERE id=" + object);
						}
						
					break;
						
					
					case "playerhead":
						
						re = mysql.getResult("SELECT textures FROM playerhead WHERE id=" + objectid);
						if(re.next()) {
							
							String textures = re.getString(1);
							if(textures != null) {
								for(String i : textures.split("§")) {
									mysql.command("DELETE FROM `texture` WHERE id=" + i);
								}
							}
							
							mysql.command("DELETE FROM `playerhead` WHERE id=" + objectid);
							
						}
						
						
					break;
					
					case "fireworkexplosion":
							
						mysql.command("DELETE FROM `fireworkexplosion` WHERE id=" + object);
						
					case "firework":
						
						re = mysql.getResult("SELECT explosions FROM firework WHERE id="+ object);
						if(re.next()) {
							
							String explosions = re.getString(1);
							
							if(explosions != null) {
								for(String i : explosions.split("§")) {
									mysql.command("DELETE FROM `fireworkexplosion` WHERE id=" + i);
								}
							}
							
							mysql.command("DELETE FROM firework WHERE id=" + object);
							
						}
						
					break;
					
					case "potion":
						
						re = mysql.getResult("SELECT custompotioneffects FROM potion WHERE id=" + object);
						if(re.next()) {
							String custompotioneffects = re.getString(1);
							
							if(custompotioneffects != null) {
								for(String i : custompotioneffects.split("§")) {
									mysql.command("DELETE FROM `custompotioneffect` WHERE id=" + i);
								}
							}
							
							mysql.command("DELETE FROM `potion` WHERE id=" + object);
						}
						
					break;
					
					
				
				
				}
				
			}else {
				throw new PhoenixStackException("[essentialsxxx-phoenixstack] couldnt find item with id " + id);
			}
			
			
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
			throw new PhoenixStackException("[essentialsxxx-phoenixstack] table " + tablename + " is damaged");
		}
		
	}
	
	
	/*
	 * >> setter <<
	 */
	
	
	/*
	 * meta setter
	 */
	
	public void setMeta(PhoenixMeta meta) {
		this.meta = meta;
	}
	
	/*
	 * default-option setter
	 */
	
	public void setDamage(int damage) {
		this.damage = damage;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	/*
	 * >> getter <<
	 */
	
	
	/*
	 * meta
	 */
	
	
	public PhoenixMeta getMeta() {
		return meta;
	}
	
	
	public int getCount() {
		return this.count;
	}
	public int getId() {
		return id;
	}
	public int getDamage() {
		return damage;
	}

	
}
