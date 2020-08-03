package de.falco.essentialsxxx.phoenixstack.metatypes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import de.falco.essentialsxxx.phoenixstack.PhoenixMeta;
import de.falco.essentialsxxx.phoenixstack.PhoenixStackException;
import de.falco.essentialsxxx.phoenixstack.datatypes.CustomPotionEffect;
import de.falco.essentialsxxx.phoenixstack.metatypes.Potion.PotionEffect;
import de.falco.essentialsxxx.util.MySql;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagInt;
import net.minecraft.server.v1_12_R1.NBTTagList;
import net.minecraft.server.v1_12_R1.NBTTagString;

public class Potion extends PhoenixMeta{
	
	private PotionEffect potion = PotionEffect.UncraftablePotion;
	private int custompotioncolor = -1;
	private ArrayList<CustomPotionEffect> custompotioneffects = new ArrayList<>();
	
	/*
	 * >> constructors <<
	 */
	
	public Potion(NBTTagCompound tag) {
		
		serialize(tag);
		
	}
	
	public Potion() {
		
	}
	
	/*
	 * >> static <<
	 */
	
	public static Potion load(int id, MySql mysql, String tablename, Potion p) throws PhoenixStackException {
		
		String command = "SELECT * FROM potion WHERE id=" + id;
		ResultSet result = mysql.getResult(command);
		
		try {
			if(result.last() == true) {
				
				String potionname = null;
				int custompotioncolor = -1;
				String custompotioneffects = null;
				
				try {
					potionname = result.getString(2);
					custompotioncolor = result.getInt(3);
					custompotioneffects = result.getString(4);
				} catch (SQLException e) {
					throw new PhoenixStackException("[essentialsxxx-phoenixstack] table exception in Potion.load()");
				}
				
				System.out.println("potion: " + potionname);
				
				p.setCustompotioncolor(custompotioncolor);
				p.setPotion(PotionEffect.getPotionEffectbyname(potionname));
				
				ArrayList<CustomPotionEffect> effects = new ArrayList<>();
				
				if(custompotioneffects != null) {
					
					System.out.println(custompotioneffects);
				
					for(String effectname : custompotioneffects.split("§")) {
						
						int effectid = -1;
						
						try {
							effectid = Integer.parseInt(effectname);
						}catch(NumberFormatException ex) {
							throw new PhoenixStackException("[essentialsxxx-phoenixstack] couldnt cast " + effectname + " to int error in table content look at the " + tablename + "! Potion.load()");
						}
						
						effects.add(CustomPotionEffect.load(effectid, mysql, "custompotioneffect"));
						
					}
					
				}
				
				
				p.setCustompotioneffects(effects);
				
				
				return p;
				
			}else {
				throw new PhoenixStackException("[essentialsxxx-phoenixstack] couldnt find item with id " + id + " in Potion.load()");
			}
			
		} catch (SQLException e) {
			throw new PhoenixStackException("[essentialsxxx-phoenixstack] couldnt find item with id " + id + " in Potion.load()");
		}
		
	}
	
	public static void save(int id, MySql mysql, String tablename, Potion p) {
		
		String custompotioneffects = "";
		
		int number = -1;
		
		String command = "SELECT id FROM custompotioneffect ORDER BY id";
		
		ResultSet res = mysql.getResult(command);
		
		try {
			if(res.last()) {
				number = res.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		for(CustomPotionEffect effect : p.getCustompotioneffects()) {
			
			number++;
			
			CustomPotionEffect.save(effect, number, mysql, "CustomPotionEffect");
			
			custompotioneffects = custompotioneffects + "§" + number;
			
		}
		
		
		
		if(!custompotioneffects.equals("")) {
			custompotioneffects = "'" + custompotioneffects.toString().replaceFirst("§", "") + "'";
		}else {
			custompotioneffects = null;
		}
		
		
		String potionname = p.getPotion().getName();
		
		if(potionname != null) {
			potionname = "'" + potionname + "'";
		}
		
		command = "INSERT INTO potion "
				+ "("
				+ "id"
				+ ",potion"
				+ ",custompotioncolor"
				+ ",custompotioneffects"
				+ ") VALUES ("
				+ "" + id
				+ ","+ potionname
				+ "," + p.getCustompotioncolor()
				+ "," + custompotioneffects
				+ ")";
		mysql.command(command);
		
	}
	
	public static void createtable(MySql mysql, String tablename) {
		
		String command = "CREATE TABLE IF NOT EXISTS " + tablename + " ("
				+ "id INT Primary Key,"
				+ "potion TEXT,"
				+ "custompotioncolor INT,"
				+ "custompotioneffects TEXT"
				+ ");";
		mysql.command(command);
		
	}
	
	/*
	 * >> methods <<
	 */
	
	@Override
	public void serialize(NBTTagCompound tag) {
		
		
		super.serialize(tag);
		
		if(tag.hasKey("CustomPotionEffects")) {
			
			try {
			
				NBTTagList custompotioneffectstag = (NBTTagList) tag.get("CustomPotionEffects");
				
				for(int x = 0; x < custompotioneffectstag.size(); x++) {
					
					NBTTagCompound tmp = custompotioneffectstag.get(x);
					
					CustomPotionEffect effect = new CustomPotionEffect(tmp);
					
					custompotioneffects.add(effect);
					
				}
				
			}catch(Exception ex) {
				ex.printStackTrace();
			}
			
			
		}
		
		if(tag.hasKey("CustomPotionColor")) {
			
			try {
				custompotioncolor = tag.getInt("CustomPotionColor");
			}catch(Exception ex) {
				ex.printStackTrace();
			}
			
			
		}
		
		if(tag.hasKey("Potion")) {
			
			try {
				potion = PotionEffect.getPotionEffectbyname(tag.getString("Potion"));
			}catch(Exception ex) {
				ex.printStackTrace();
			}
			
			
		}
		
		
	}

	
	@Override
	public NBTTagCompound deserialize() {
		
		NBTTagCompound result = super.deserialize();
		
		if(custompotioncolor > -1) {
			result.set("CustomPotionColor", new NBTTagInt(custompotioncolor));	
		}
		
		
		result.set("Potion", new NBTTagString(potion.getName()));
		
		NBTTagList custompotioneffectstag = new NBTTagList();
		
		for(CustomPotionEffect tmp : custompotioneffects) {
			
			custompotioneffectstag.add(tmp.toNBTTagCompound());
		}
		
		if(custompotioneffectstag.isEmpty() == false) {
			result.set("CustomPotionEffects", custompotioneffectstag);
		}
		
		return result;
		
	}
	
	/*
	 * >> enum <<
	 */
	
	public enum PotionEffect {
		
		Potion(null,null,null),
		UncraftablePotion("empty",null,null),
		WaterBottle("water",null,null),
		MundanePotion("mundane",null,null),
		ThickPotion("thick",null,null),
		AwkwardPotion("awkard",null,null),
		NightVision("night_vision",null,"long_night_vision"),
		Invisibility("invisibility",null,"long_invisibility"),
		Leaping("leaping","strong_leaping","long_leaping"),
		FireResistance("fire_resistance",null,"long_fire_resistance"),
		Swiftness("swiftness","strong_swiftness","long_swiftness"),
		Slowness("slowness","strong_slowness","long_slowness"),
		WaterBreathing("water_breathing",null,"long_water_breathing"),
		InstantHealth("healing","strong_healing",null),
		Harming("harming","strong_harming",null),
		Poison("posion","strong_poison","long_poison"),
		Regeneration("regeneration","strong_regeneration","long_regeneration"),
		Strength("strength","strong_strength","long_strength"),
		Weakness("weakness",null,"long_weakness"),
		Luck("luck",null,null),
		TheTurtleMaster("turtle_master","strong_turtle_master","long_turtle_master"),
		SlowFalling("slow_falling",null,"long_slow_falling"),
		
		;
		
		private boolean level2;
		private boolean extended;
		
		private String name;
		private String level2name;
		private String extendedname;
		
		PotionEffect(String name, String level2name, String extendedname) {
			this.name = name;
			this.level2name = level2name;
			this.extendedname = extendedname;
		}
		
		public boolean cansetlevel2() {
			if(this.level2name != null) {
				return true;
			}else {
				return false;
			}
		}
		
		public boolean canextended() {
			if(this.extendedname != null) {
				return true;
			}else {
				return false;
			}
		}
		
		/*
		 * getter
		 */
		
		public String getName() {
			
			if(this.extended == true) {
				
				if(this.extendedname != null) {
					return this.extendedname;
				}
				
			}
				
			if(this.level2 == true) {
				
				if(this.level2name != null) {
					return this.level2name;
				}
				
			}
				
			return this.name;
			
			
		}
		
		public static PotionEffect getPotionEffectbyname(String name) {
			
			name = name.replaceAll("minecraft:", "");
			
			System.out.println(name);
			
			for(PotionEffect effect : PotionEffect.values()) {
				
				if(effect.name != null) {
					
					if(effect.name.equals(name)) {
						return effect;
					}	
				}
				
				if(effect.extendedname != null) {
					if(effect.extendedname.equals(name)) {
						
						PotionEffect tmp = effect;
						tmp.setExtended(true);
						
						return tmp;
					}	
				}
				
				if(effect.level2name != null) {
					if(effect.level2name.equals(name)) {
						
						
						PotionEffect tmp = effect;
						tmp.setLevel2(true);
						
						return tmp;
						
						
					}					
				}

				
			}
			
			
			return PotionEffect.Potion;
			
		}
		
		/*
		 * >> setter << 
		 */
		
		public void setExtended(boolean extended) {
			this.extended = extended;
		}
		public void setLevel2(boolean level2) {
			this.level2 = level2;
		}
		
		
		
	}//enum
	

	/*
	 * >> getter <<
	 */
	
	public PotionEffect getPotion() {
		return potion;
	}
	public int getCustompotioncolor() {
		return custompotioncolor;
	}
	public ArrayList<CustomPotionEffect> getCustompotioneffects() {
		return custompotioneffects;
	}
	
	
	/*
	 * >> setter <<
	 */
	
	public void setPotion(PotionEffect potion) {
		this.potion = potion;
	}
	public void setCustompotioncolor(int custompotioncolor) {
		this.custompotioncolor = custompotioncolor;
	}
	public void setCustompotioneffects(ArrayList<CustomPotionEffect> custompotioneffects) {
		this.custompotioneffects = custompotioneffects;
	}
	
	
}
