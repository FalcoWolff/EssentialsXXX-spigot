package de.falco.essentialsxxx.phoenixstack.metatypes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import de.falco.essentialsxxx.phoenixstack.PhoenixMeta;
import de.falco.essentialsxxx.phoenixstack.datatypes.Enchant;
import de.falco.essentialsxxx.util.MySql;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;

public class StoredEnchantment extends PhoenixMeta{
	
	private ArrayList<Enchant> stored = new ArrayList<>();
	
	public StoredEnchantment(NBTTagCompound tag) {
		this.serialize(tag);
	}
	
	public StoredEnchantment() {
		
	}
	
	public void serialize(NBTTagCompound tag) {
		super.serialize(tag);
		
		if(tag.hasKey("StoredEnchantments")) {
			
			try {
				
				NBTTagList list = (NBTTagList) tag.get("StoredEnchantments");
				
				for(int x = 0; x < list.size(); x++) {
					NBTTagCompound tmp = list.get(x);
					
					Enchant enchant = new Enchant(tmp);
					
					this.stored.add(enchant);
					
				}
				
			}catch(Exception ex) {
				ex.printStackTrace();
			}
			
		}
	}
	
	public NBTTagCompound deserialize() {
		
		NBTTagCompound tag = super.deserialize();
		
		if(this.stored.isEmpty() == false) {
			
			NBTTagList list = new NBTTagList();
			
			for(Enchant e : stored) {
				NBTTagCompound tmp = e.toNBTTagCompound();
				list.add(tmp);
			}
			
			
			tag.set("StoredEnchantments", list);
		}
		
		return tag;
		
	}
	
	public static void createtable(MySql mysql, String tablename) {
		String command = "CREATE TABLE IF NOT EXISTS " + tablename + " "
				+ "("
				+ "id INT Primary Key"
				+ ",storedenchantments TEXT"
				+ ");";
		mysql.command(command);
	}

	public static void save(StoredEnchantment so, MySql mysql, int id, String tablename) {
		
		String stored = null;
		
		if(so.stored.isEmpty() == false) {
			StringBuilder builder = new StringBuilder();
			
			int number = -1;
			
			ResultSet result = mysql.getResult("SELECT id FROM enchant");
			try {
				if(result.last()) {
					number = result.getInt(1);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
					
			
			for(Enchant e : so.stored) {
				number++;
				builder.append("§" + number);
				Enchant.save(e, mysql, "enchant", number);
			}
			
			stored = "'" + builder.toString().replaceFirst("§", "") + "'";
			
		}
		
		String command = "INSERT INTO " + tablename + " "
				+ "("
				+ "id,storedenchantments"
				+ ") VALUES ("
				+ "" + id
				+ "," + stored
				+ ""
				+ ""
				+ ");";
		mysql.command(command);
		
	}

	public static StoredEnchantment load(StoredEnchantment so, MySql mysql, int id, String tablename) {
		
		
		ResultSet result = mysql.getResult("SELECT * FROM " + tablename + " WHERE id=" + id);
		try {
			if(result.next()) {
				String storedenchantments = result.getString(2);
				
				for(String i : storedenchantments.split("§")) {
					int number = Integer.parseInt(i);
					
					Enchant e = Enchant.load(mysql, "enchant", number);
					
					so.stored.add(e);
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return so;
		
	}
}
