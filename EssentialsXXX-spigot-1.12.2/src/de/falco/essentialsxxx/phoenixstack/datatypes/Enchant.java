package de.falco.essentialsxxx.phoenixstack.datatypes;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Nullable;

import de.falco.essentialsxxx.util.MySql;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagInt;

/*
 * @Info class to configurate Enchantments of MinecraftItems 
 * @see de.falco.essentialsxxx.phoenixstack.PhoenixStack
 */
public class Enchant {
	
	
	/*
	 * >> fields <<
	 */
	
	private int lvl = -1;
	private Ench id = Ench.unknown;
	
	/*
	 * >> methods <<
	 */
	
	/*
	 * @Info cast a Enchant to a NBTTagCompound
	 */
	public NBTTagCompound toNBTTagCompound() {
		
		NBTTagCompound tag = new NBTTagCompound();
		
		tag.set("lvl", new NBTTagInt(lvl));
		tag.set("id", new NBTTagInt(id.getenchid()));
		
		return tag;
		
	}
	
	/*
	 * >> constructors <<
	 */
	
	/*
	 * @Info constructor 
	 * @param lvl of the enchantment
	 * @param id saved as Ench 
	 */
	public Enchant(int lvl, @Nullable Ench id) {
		this.lvl = lvl;
		this.id = id;
	}
	
	/*
	 * @Info constructor
	 * @param the NBTTagCompound
	 */
	public Enchant(NBTTagCompound tag) {
		
		int lvl = 0;
		Ench id = Ench.unknown;
		
		if(tag.hasKey("lvl")) {
			lvl = tag.getInt("lvl");
		}
		if(tag.hasKey("id")) {
			id = Ench.getenchbyid(tag.getInt("id"));
		}
		
		this.lvl = lvl;
		this.id = id;
		
	}
	
	public Enchant() {
		
	}
	
	/*
	 * >> getter <<
	 */
	
	public Ench getId() {
		return id;
	}
	public int getLvl() {
		return lvl;
	}
	
	/*
	 * >> setter <<
	 */
	public void setId(Ench id) {
		this.id = id;
	}
	public void setLvl(int lvl) {
		this.lvl = lvl;
	}
	
	/*
	 * >> static <<
	 */
	
	public static void createtable(MySql mysql, String tablename) {
		
		String command = "CREATE TABLE IF NOT EXISTS " + tablename + " "
				+ "("
				+ "id INT Primary Key"
				+ ",enchid INT"
				+ ",lvl INT"
				+ ""
				+ ");";
		mysql.command(command);
		
	}

	public static void save(Enchant e, MySql mysql, String tablename, int id) {
		
		String command = "INSERT INTO " + tablename + " "
				+ "("
				+ "id,enchid,lvl"
				+ ") VALUES ("
				+ "" + id
				+ "," + e.id.enchid
				+ "," + e.lvl
				+ ");";
		mysql.command(command);
	}
	
	public static Enchant load(MySql mysql, String tablename, int id) {
		
		Enchant e = new Enchant();
		
		ResultSet result = mysql.getResult("SELECT * FROM " + tablename + " WHERE id=" + id);
		try {
			if(result.next()) {
				int enchid = result.getInt(2);
				int lvl = result.getInt(3);
				
				e.setLvl(lvl);
				e.setId(Ench.getenchbyid(enchid));
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		return e;
		
	}
	/*
	 * >> enums <<
	 */
	
	/*
	 * @Info enum to edit the id and name of the enchantment
	 */
	public enum Ench{
		
		//!!! WRONG SITE ONLY FOR POKETEDITION https://minecraft.gamepedia.com/Enchanting#Summary_of_enchantments
		//https://www.planetminecraft.com/blog/minecraft-enchantment-ids/
		
		unknown(-1,"unknown"),
		Protection(0,"protection"),
		FireProtection(1,"fire_protection"),
		FeatherFalling(2,"feather_falling"),
		BlastProtection(3,"blast_protection"),
		ProjectileProtection(4,"projectile_protection"),
		Respiration(5,"respiration"),
		AquaAffinity(6,"aqua_affinity"),
		Thorns(7,"Thorns"),
		DepthStrider(8,"depth strider"),//10
		
		CurseOfBinding(10,"binding_curse"),
		
		Sharpness(16,"sharpness"),
		Smite(17,"smite"),
		BaneOfArthropods(18,"bane_of_arthropods"),
		Knockback(19,"knockback"),
		FireAspect(20,"fire_aspect"),
		Looting(21,"looting"),
		SweepingEdge(22,"sweeping"),
		
		Efficiency(32,"efficiency"),
		SilkTouch(33,"silk_touch"),
		Unbreaking(34,"unbreaking"),//20
		Fortune(35,"fortune"),
		
		Power(48,"power"),
		Punch(49,"punch"),
		Flame(50,"flame"),
		Infinity(51,"infinity"),
		
		LuckOfTheSea(61,"luck_of_the_sea"),
		Lure(62,"lure"),
		Loyalty(65,"loyalty"),
		Impaling(66,"impaling"),
		Riptide(67,"riptide"),//30
		Channeling(68,"channeling"),
		
		Mending(70,"mending"),
		CurseOfVanishing(71,"vanishing_curse"),
		
		;//33
		
		/*
		 * fields
		 */
		
		private int enchid;
		private String enchname;
		
		/*
		 * constructor
		 */
		Ench(int id, String enchname) {
			this.enchid = id;
			this.enchname = enchname;
		}
		
		public static Ench getenchbyid(int id) {
			
			for(Ench ench : Ench.values()) {
				if(ench.getenchid() == id) {
					return ench;
				}
			}
			
			return Ench.unknown;
			
		}
		
		public static Ench getenchbyname(String name) {
			
			for(Ench ench : Ench.values()) {
				if(ench.getEnchname().equals(name)) {
					return ench;
				}
			}
			
			return Ench.unknown;
			
		}
		
		/*
		 * getter
		 */
		
		public int getenchid() {
			return this.enchid;
		}
		
		public String getEnchname() {
			return enchname;
		}
		
		
	}

}
