package de.falco.essentialsxxx.phoenixstack.metatypes;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import de.falco.essentialsxxx.phoenixstack.PhoenixMeta;
import de.falco.essentialsxxx.phoenixstack.datatypes.Texture;
import de.falco.essentialsxxx.util.MySql;
import net.minecraft.server.v1_12_R1.Item;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.NBTBase;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;
import net.minecraft.server.v1_12_R1.NBTTagString;

public class PlayerHead extends PhoenixMeta{
	
	private String idold = null;
	
	private String name = null;
	
	private ArrayList<Texture> textures = new ArrayList<>();
	
	
	public PlayerHead(NBTTagCompound tag) {
		serialize(tag);
	}
	
	public PlayerHead() {
		
	}
	
	public void serialize(NBTTagCompound t) {
		super.serialize(t);
		
		if(t.hasKey("SkullOwner")) {
			NBTTagCompound skullowner = (NBTTagCompound) t.get("SkullOwner");
			
			if(skullowner.hasKey("Name")) {
				try {
					this.name = skullowner.getString("Name");
				}catch(Exception ex) {
					ex.printStackTrace();
				}
			}
			
			if(skullowner.hasKey("Id")) {
				
				String b = null;
				try {
					b = skullowner.getString("Id");
				}catch(Exception ex) {
					ex.printStackTrace();
				}
				
				this.idold = b;
				
			}
			
			if(skullowner.hasKey("Properties")) {
				
				try {
					
					NBTTagCompound pro = (NBTTagCompound) skullowner.get("Properties");
					
					if(pro.hasKey("textures")) {
						
						NBTTagList tex = (NBTTagList) pro.get("textures");
						
						for(int x = 0; x < tex.size(); x++) {
							NBTTagCompound tmp = tex.get(x);
							
							Texture a = new Texture(tmp);
							
							this.textures.add(a);
						}
						
					}
					
				}catch(Exception ex) {
					ex.printStackTrace();
				}
				
				
			}
			
		}
		
	}
	
	public NBTTagCompound deserialize() {
		
		NBTTagCompound tag = super.deserialize();
		
		NBTTagCompound pro = new NBTTagCompound();
		
		if(this.name != null) {
			pro.set("Name", new NBTTagString(this.name));
		}
		
		
		if(this.idold != null) {
			pro.set("Id", new NBTTagString(this.idold));
		}
		
		if(this.textures.isEmpty() == false) {
			NBTTagList textures = new NBTTagList();
			
			for(Texture te : this.textures) {
				NBTTagCompound tmp = te.toNBTTagCompound();
				textures.add(tmp);
			}
			
			NBTTagCompound properties = new NBTTagCompound();
			
			properties.set("textures", textures);
			
			pro.set("Properties", properties);
			
		}
		
		tag.set("SkullOwner", pro);
		
		return tag;
	}
	
	public static void save(PlayerHead p, int id, MySql mysql, String tablename) {
		
		String i = null;
		
		if(p.idold != null) {
			
			i = "'" + p.idold + "'";
			
		}
		
		String name = null;
		
		if(p.name != null) {
			name = "'" + p.name + "'";
		}
		
		String ids = null;
		
		if(p.textures.isEmpty() == false) {
			
			StringBuilder builder = new StringBuilder();
			
			int number = 0;
			
			ResultSet result = mysql.getResult("SELECT id FROM texture");
			
			try {
				if(result.last()) {
					number = result.getInt(1);
				}
			} catch (SQLException e) {
				e.printStackTrace(); 
			}
			
			for(Texture te : p.textures) {
				number++;
				builder.append("§" + number);
				
				Texture.save(te, number, mysql, "texture");
			}
			
			ids = "'" + builder.toString().replaceFirst("§", "") + "'";
			
		}
		
		String command = "INSERT INTO " + tablename + " "
				+ "("
				+ "id,name,uid,textures"
				+ ") VALUES ("
				+ "" + id
				+ "," + name
				+ "," + i
				+ "," + ids
				+ ""
				+ ");";
		mysql.command(command);
		
		
	}
	
	public static PlayerHead load(PlayerHead p, int id, MySql mysql, String tablename) {
		
		ResultSet result = mysql.getResult("SELECT * FROM " + tablename + " WHERE id=" + id);
		
		try {
			if(result.next()) {
				
				String name = result.getString(2);
				String idtmp = result.getString(3);
				String textureids = result.getString(4);
				
				for(String i : textureids.split("§")) {
					
					int c = 0;
					
					try {
						c = Integer.parseInt(i);
					}catch(NumberFormatException ex) {
						ex.printStackTrace();
						break;
					}
					
					Texture te = Texture.load(c, mysql, "texture");
					
					p.textures.add(te);
				}
				
				p.name = name;
				
				p.idold = idtmp;
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return p;
		
	}
	
	public static void createtable(MySql mysql, String tablename) {
		
		String command = "CREATE TABLE IF NOT EXISTS " + tablename + " "
				+ "("
				+ "id INT Primary key,"
				+ "name TEXT,"
				+ "uid TEXT,"
				+ "textures TEXT"
				+ ");";
		mysql.command(command);
		
	}
	

}
