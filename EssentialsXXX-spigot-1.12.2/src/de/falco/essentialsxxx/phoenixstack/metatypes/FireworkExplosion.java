package de.falco.essentialsxxx.phoenixstack.metatypes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import de.falco.essentialsxxx.phoenixstack.PhoenixMeta;
import de.falco.essentialsxxx.phoenixstack.PhoenixStackException;
import de.falco.essentialsxxx.phoenixstack.datatypes.Color.fireworkcolor;
import de.falco.essentialsxxx.util.MySql;
import net.minecraft.server.v1_12_R1.NBTTagByte;
import net.minecraft.server.v1_12_R1.NBTTagCompound;

public class FireworkExplosion extends PhoenixMeta{
	
	/*
	 * >> variables <<
	 */
	
	private byte flicker = -1;
	private byte trail = -1;
	private fireworktype type = fireworktype.unknown;
	private ArrayList<Integer> colors = new ArrayList<Integer>();
	private ArrayList<Integer> fadecolors = new ArrayList<>();
	
	/*
	 * >> enum <<
	 */
	
	
	public enum fireworktype {
		
		small((byte)0),
		large((byte)1),
		star((byte)2),
		creeper((byte)3),
		burst((byte)4),
		
		unknown((byte)-1),
		
		;
		
		private byte index;
		
		fireworktype(byte index) {
			this.index = index;
		}
		
		public byte getIndex() {
			return index;
		}
		
		public static fireworktype gettpyebyindex(int i) {
			
			for(fireworktype firework : fireworktype.values()) {
				
				if(firework.getIndex() == i) {
					return firework;
				}
				
			}
			
			return fireworktype.unknown;
			
		}
	}
	
	/*
	 * >> methods <<
	 */
	
	public NBTTagCompound deserialize() {
		
		NBTTagCompound re = super.deserialize();
		
		NBTTagCompound result = new NBTTagCompound();
		
		/*
		result.set("Flicker", new NBTTagByte(flicker));
		result.set("Trail", new NBTTagByte(trail));
		result.set("Type", new NBTTagByte(type.getIndex()));
		*/
		
		if(flicker != -1) {
			result.set("Flicker", new NBTTagByte(flicker));
		}
		
		if(trail != -1) {
			result.set("Trail", new NBTTagByte(trail));
		}
		
		if(type.getIndex() != -1) {
			result.set("Type", new NBTTagByte(type.getIndex()));
		}
		
		if(this.colors.isEmpty() == false) {
			
			int[] colorsend = new int[colors.size()];
			
			for(int i = 0; i < colors.size(); i++) {
				
				colorsend[i] = colors.get(i);
				
			}
			
			result.setIntArray("Colors", colorsend);
			
		}
		
		if(this.fadecolors.isEmpty() == false) {
			
			int[] fadecolorsend = new int[this.fadecolors.size()];
			
			for(int i = 0; i < this.fadecolors.size(); i++) {
				
				fadecolorsend[i] = this.fadecolors.get(i);
				
			}
			
			result.setIntArray("FadeColors", fadecolorsend);
			
		}
		
		if(result.isEmpty() == false) {
			re.set("Explosion", result);
		}
		
		return re;
		
	}
	
	public void serialize(NBTTagCompound l) {
		
		super.serialize(l);
		
		if(l.hasKey("Explosion")) {
			
			NBTTagCompound tag = (NBTTagCompound) l.get("Explosion");
		
			if(tag.hasKey("Flicker")) {
				try {
					byte flicker = tag.getByte("Flicker");
					if(flicker == 1 || flicker == 0) {
						this.flicker = flicker;
					}
				}catch(Exception ex) {}
			}
			
			if(tag.hasKey("Trail")) {
				try {
					byte trail = tag.getByte("Trail");
					if(trail == 1 || trail == 0) {
						this.trail = trail;
					}
				}catch(Exception ex) {}
			}
			
			if(tag.hasKey("Type")) {
				try {
					byte type = tag.getByte("Type");
					this.type = fireworktype.gettpyebyindex(type);
				}catch(Exception ex) {}
			}
			
			if(tag.hasKey("Colors")) {
				
				try {
					int colors[] = tag.getIntArray("Colors");
					for(int i : colors) {
						this.colors.add(i);
					}
				}catch(Exception ex) {}
				
			}
			
			if(tag.hasKey("FadeColors")) {
				
				try {
					int fadecolors[] = tag.getIntArray("FadeColors");
					for(int i : fadecolors) {
						this.fadecolors.add(i);
					}
				}catch(Exception ex) {}
				
			}
		}
		
	}
	
	/*
	 * >> constructors <<
	 */
	
	public FireworkExplosion() {
		
	}
	
	/*
	 * @Info create FireworkExplosion
	 * @param NBTTagCompound
	 */
	public FireworkExplosion(NBTTagCompound tag) {
		this.serialize(tag);
	}

	/*
	 * @Info create FireworkExplosion without colors and fadecolors
	 * @param flicker
	 * @param trail
	 * @param type of the fireworkeffect 
	 * @see de.falco.essentialsxxx.phoenixstack.datatypes.FireworkExplosion.fireworktype
	 */
	public FireworkExplosion(byte flicker, byte trail, fireworktype type) {
		this.flicker = flicker;
		this.trail = trail;
		this.type = type;
	}
	
	/*
	 * >> static <<
	 */
	
	/*
	 * @Info save a FireworkExplosion in db
	 * @param the FireworkExplosion
	 * @param id of the FireworkExplosion
	 * @param mysql connection
	 * @param tablename
	 */
	public static void save(FireworkExplosion e, int id, MySql mysql, String tablename) {
		
		byte type = e.getType().getIndex();
		byte flicker = e.getFlicker();
		byte trail = e.getTrail();
		
		StringBuffer colors = new StringBuffer();
		StringBuffer fadecolors = new StringBuffer();
		
		for(Integer i : e.getColors()) {
			colors.append("§" + i);
		}
		
		for(Integer i : e.getFadecolors()) {
			fadecolors.append("§" + i);
		}
		
		String colorsend = null;
		String fadecolorsend = null;
		
		if(colors.length() != 0) {
			colorsend = "'" + colors.toString().replaceFirst("§", "") + "'";
		}
		
		if(fadecolors.length() != 0) {
			fadecolorsend = "'" + fadecolors.toString().replaceFirst("§", "") + "'";
		}
		
		String command = "INSERT INTO " + tablename + ""
				+ "("
				+ "id,type,flicker,trail,colors,fadecolors"
				+ ") VALUES ("
				+ "" + id
				+ "," + type
				+ ","+  flicker
				+ "," + trail
				+ "," + colorsend
				+ "," + fadecolorsend
				+ ");";
		mysql.command(command);
		
		
		
	}
	
	/*
	 * @Info load a FireworkExplosion from a db
	 * @param id of the item
	 * @param mysql connection
	 * @param tablename
	 * @throws PhoenixStackException when there isnt a FireworkExplosion with id 
	 */
	public static FireworkExplosion load(int id, MySql mysql, String tablename) throws PhoenixStackException {
		
		String command = "SELECT * FROM " + tablename + " WHERE id=" + id;
		ResultSet result = mysql.getResult(command);
		
		try {
			if(result.next()) {
				
				byte type = result.getByte(2);
				byte flicker = result.getByte(3);
				byte trail = result.getByte(4);
				String colors = result.getString(5);
				String fadecolors = result.getString(6);
				
				FireworkExplosion ex = new FireworkExplosion(flicker, trail, fireworktype.gettpyebyindex(type));
				
				if(colors != null) {
					ArrayList<Integer> colorsend = new ArrayList<>();
					
					try {
						for(String i : colors.split("§")) {
							int number = Integer.parseInt(i);
							colorsend.add(number);
						}
					}catch(NumberFormatException ex2) {}
					
					ex.setColors(colorsend);
					
					System.out.println("colors: " + ex.colors);
				}
				
				if(fadecolors != null) {
					ArrayList<Integer> fadecolorsend = new ArrayList<>();
					
					try {
						for(String i : fadecolors.split("§")) {
							int number = Integer.parseInt(i);
							fadecolorsend.add(number);
						}
					}catch(NumberFormatException ex2) {}
					
					ex.setFadecolors(fadecolorsend);
				}
				
				
				return ex;
				
				
			}else {
				
				
				throw new PhoenixStackException("[essentialsxxx-phoenixstack] couldnt find item with id " + id);
				
			}
		} catch (SQLException e) {
			
			throw new PhoenixStackException("[essentialsxxx-phoenixstack] couldnt find item with id " + id);
		
		}
		
	}
	
	/*
	 * @Info create a table for FireworkExplosions
	 * @param mysql connection
	 * @param tablename
	 */
	public static void createtable(MySql mysql, String tablename) {
		
		String command = "CREATE TABLE IF NOT EXISTS " + tablename + " "
				+ "(id int Primary Key,"
				+ "type TINYINT,"
				+ "flicker TINYINT,"
				+ "trail TINYINT,"
				+ "colors TEXT,"
				+ "fadecolors TEXT"
				+ ");";
		mysql.command(command);
		
	}
	
	/*
	 * >> add <<
	 */
	
	/*
	 * @Info add a color to FireworkExplosion
	 * @param color in int format
	 */
	public void addcolor(int i) {
		this.colors.add(i);
	}
	
	/*
	 * @Info add a color to FireworkExplosion
	 * @param firworkcolor, will cast to int
	 */
	public void addcolor(fireworkcolor c) {
		this.colors.add(c.getDecimalvalue());
	}
	
	/*
	 * @Info add a fadecolor to FireworkExplosion
	 * @param fadecolor in int format
	 */
	public void addfadecolor(int i) {
		this.fadecolors.add(i);
	}
	
	/*
	 * @Info add fadecolor to FireworkExplosion
	 * @param fireworkcolor, will cast to int
	 */
	public void addfadecolor(fireworkcolor c) {
		this.fadecolors.add(c.getDecimalvalue());
	}
	
	/*
	 * >> setter <<
	 */
	
	public void setColors(ArrayList<Integer> colors) {
		this.colors = colors;
	}
	public void setFadecolors(ArrayList<Integer> fadecolors) {
		this.fadecolors = fadecolors;
	}
	public void setFlicker(byte flicker) {
		this.flicker = flicker;
	}
	public void setTrail(byte trail) {
		this.trail = trail;
	}
	public void setType(fireworktype type) {
		this.type = type;
	}
	
	/*
	 * >> has <<
	 */
	
	public boolean hascolors() {
		if(this.colors.isEmpty()) {
			return false;
		}else {
			return true;
		}
	}
	public boolean hasfadecolors() {
		if(this.fadecolors.isEmpty()) {
			return false;
		}else {
			return true;
		}
	}
	
	/*
	 * >> getter <<
	 */
	public byte getFlicker() {
		return flicker;
	}
	public byte getTrail() {
		return trail;
	}
	public fireworktype getType() {
		return type;
	}
	public ArrayList<Integer> getColors() {
		return colors;
	}
	public ArrayList<Integer> getFadecolors() {
		return fadecolors;
	}
	

}
