package de.falco.essentialsxxx.phoenixstack.datatypes;

import java.sql.ResultSet;
import java.sql.SQLException;

import de.falco.essentialsxxx.phoenixstack.PhoenixStackException;
import de.falco.essentialsxxx.util.MySql;
import net.minecraft.server.v1_12_R1.NBTTagByte;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagInt;

public class CustomPotionEffect {
	
	/*
	 * >> fields <<
	 */
	
	private CustomPotionEffectId id;
	private byte amplifier;
	private int duration;
	private byte ambient;
	private byte showparticles;
	private byte showicon;
	
	/*
	 * >> methods <<
	 */
	
	/*
	 * >> static <<
	 */
	
	/*
	 * @Info save a CustomPotionEffect 
	 * @param the CustomPotionEffect
	 * @param id of the effect
	 * @param mysql connection
	 * @param tablename
	 */
	public static void save(CustomPotionEffect custompotioneffect, int id, MySql mysql, String tablename) {

		
		String command = "INSERT INTO " + tablename + " "
				+ "("
				+ "id"
				+ ",effect"
				+ ",amplifier"
				+ ",duration"
				+ ",ambient"
				+ ",showparticles"
				+ ",showicon"
				+ ") VALUES ("
				+ "" + id
				+ "," + custompotioneffect.id.getId()
				+ "," + custompotioneffect.amplifier
				+ "," + custompotioneffect.duration
				+ "," + custompotioneffect.ambient
				+ "," + custompotioneffect.showparticles
				+ "," + custompotioneffect.showicon
				+ ");";
		
		mysql.command(command);
	}
	
	/*
	 * @Info load a CustomPotionEffect
	 * @param id of the CustomPotionEffect
	 * @param mysql connection
	 * @param tablename
	 */
	public static CustomPotionEffect load(int id, MySql mysql, String tablename) throws PhoenixStackException {
		
		String command = "SELECT * FROM " + tablename + " WHERE id=" + id;
		ResultSet result = mysql.getResult(command);
		
		try {
			
			if(result.next()) {
				
				byte effectid = 0;
				byte amplifier = 0;
				int duration = 0;
				byte ambient = 0;
				byte showparticles = 0;
				byte showicon = 0;
				
				try {
				
					effectid = result.getByte(2);
					amplifier = result.getByte(3);
					duration = result.getInt(4);
					ambient = result.getByte(5);
					showparticles = result.getByte(6);
					showicon = result.getByte(7);
					
				}catch(SQLException ex) {
					throw new PhoenixStackException("[essentialsxxx-phoenixstack] error in table! CustomPotionEffect.save()");
				}
				
				
				CustomPotionEffect re = new CustomPotionEffect(CustomPotionEffectId.geteffectbyid(effectid), amplifier, ambient, duration, showparticles, showicon);
				
				return re;
				
				/*
				command = "INSERT INTO " + tablename + " ("
						+ "id,effect,amplifier,duration,ambient,showparticles,showicon"
						+ ") VALUES ("
						+ "" + id
						+ "," + effectid
						+ "," + amplifier
						+ "," + duration
						+ "," + ambient
						+ "," + showparticles
						+ "," + showicon
						+ ""
						+ ");";
				mysql.command(command);
				*/
				
			}else {
				throw new PhoenixStackException("[essentialsxxx-phoenixstack] couldnt find item with id " + id);
			}
			
		} catch (SQLException e) {
			throw new PhoenixStackException("[essentialsxxx-phoenixstack] couldnt find item with id " + id);
		}
	}
	
	/*
	 * >> non-static <<
	 */
	
	/*
	 * @Info create a table to save CustomPotionEffects later
	 * @param mysql connection
	 * @param tablename
	 */
	public static void createtable(MySql mysql, String tablename) {
		
		String command = "CREATE TABLE IF NOT EXISTS " + tablename + " "
				+ "("
				+ "id INT PRIMARY KEY,"
				+ "effect TINYINT,"
				+ "amplifier TINYINT,"
				+ "duration INT,"
				+ "ambient TINYINT,"
				+ "showparticles TINYINT,"
				+ "showicon TINYINT"
				+ ");";
		
		mysql.command(command);
		
	}
	
	/*
	 * @Info cast a CustomPotionEffect to NBTTagCompound
	 * @return the NBTTagCompound
	 */
	public NBTTagCompound toNBTTagCompound() {
		
		NBTTagCompound tag = new NBTTagCompound();
		
		tag.set("Id", new NBTTagByte(id.getId()));
		tag.set("Amplifier", new NBTTagByte(amplifier));
		tag.set("Duration", new NBTTagInt(duration));
		tag.set("Ambient", new NBTTagByte(ambient));
		tag.set("ShowParticles", new NBTTagByte(showparticles));
		tag.set("ShowIcon", new NBTTagByte(showicon));
		
		return tag;
		
	}
	
	/*
	 * >> constructor <<
	 */
	
	public CustomPotionEffect(CustomPotionEffectId id, byte amplifier,byte ambient, int duration, byte showparticles, byte showicon) {
		this.id = id;
		this.amplifier = amplifier;
		this.duration = duration;
		this.ambient = ambient;
		this.showicon = showicon;
		this.showparticles = showparticles;
	}

	/*
	 * @Info create a CustomPotionEffect only with a NBTTagCompound
	 * @param the NBTTagCompound 
	 */
	public CustomPotionEffect(NBTTagCompound tag) {
		
		if(tag.hasKey("Id")) {
			this.id = CustomPotionEffectId.geteffectbyid(tag.getByte("Id"));
		}else {
			this.id = CustomPotionEffectId.unknown;
		}
		if(tag.hasKey("Amplifier")) {
			this.amplifier = tag.getByte("Amplifier");
		}else {
			this.amplifier = 0;
		}
		if(tag.hasKey("Duration")) {
			this.duration = tag.getInt("Duration");
		}else {
			this.duration = 0;
		}
		if(tag.hasKey("Ambient")) {
			this.ambient = tag.getByte("Ambient");
		}else {
			this.ambient = 0;
		}
		if(tag.hasKey("ShowParticles")) {
			this.showparticles = tag.getByte("ShowParticles");
		}else {
			this.showparticles = 0;
		}
		if(tag.hasKey("ShowIcon")) {
			this.showicon = tag.getByte("ShowIcon");
		}else {
			this.showicon = 0;
		}
		
	}

	/*
	 * >> enums <<
	 */
	
	public enum CustomPotionEffectId {
		
		/*
		 * constants
		 */
		unknown("unknown",(byte)-1),
		
		Speed("speed",(byte)1),
		Slowness("slowness",(byte)2),
		Haste("haste",(byte)3),
		MiningFatigue("mining_fatigue",(byte)4),
		Strength("strength",(byte)5),
		InstantHealth("instant_health",(byte)6),
		JumpBoost("jump_boost",(byte)8),
		Nausea("nausea",(byte)9),
		Regeneration("regeneration",(byte)10),
		Resistance("resistance",(byte)11),
		FireResistance("fire_resistance",(byte)12),
		WaterBreathing("water_breathing",(byte)13),
		Invisibility("invisibility",(byte)14),
		Blindness("blindness",(byte)15),
		
		NightVision("night_vision",(byte)16),
		Hunger("hunger",(byte)17),
		Weakness("weakness",(byte)18),
		Poison("poison",(byte)19),
		Wither("wither",(byte)20),
		HealthBoost("health_boost",(byte)21),
		Absorption("absorption",(byte)22),
		Saturation("saturation",(byte)23),
		Glowing("glowing",(byte)24),
		Levitation("levitation",(byte)25),
		Luck("luck",(byte)26),
		BadLuck("unluck",(byte)27),
		SlowFalling("slow_falling",(byte)28),
		ConduitPower("conduit_power",(byte)29),
		DolphinsGrace("dolphins_grace",(byte)30),
		
		BadOmen("bad_omen",(byte)31),
		HeroOfTheVillage("hero_of_the_village",(byte)32),
		
		;
		
		/*
		 * fields
		 */
		
		private String name;
		private byte id;
		
		/*
		 * >> constructor <<
		 */
		
		CustomPotionEffectId(String name, byte id) {
			this.name = name;
			this.id = id;
		}
		
		/*
		 * >> getter <<
		 */
		
		public static CustomPotionEffectId geteffectbyname(String name) {
			
			CustomPotionEffectId re = unknown;
			
			for(CustomPotionEffectId index : CustomPotionEffectId.values()) {
				if(index.getName().equals(name)) {
					return index;
				}
			}
			
			return re;
		}
		
		public static CustomPotionEffectId geteffectbyid(int id) {
			
			CustomPotionEffectId re = unknown;
			
			for(CustomPotionEffectId index : CustomPotionEffectId.values()) {
				if(index.getId() == id) {
					return index;
				}
			}
			return re;
			
		}
		
		public String getName() {
			return name;
		}
		
		public byte getId() {
			return id;
		}
		
		
		
	}
	
	

}
