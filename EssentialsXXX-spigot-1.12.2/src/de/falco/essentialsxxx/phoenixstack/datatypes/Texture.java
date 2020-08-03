package de.falco.essentialsxxx.phoenixstack.datatypes;

import java.sql.ResultSet;
import java.sql.SQLException;

import de.falco.essentialsxxx.util.MySql;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagString;

public class Texture {
	
	private String signature = null;
	private String value = null;
	
	public Texture() {
		
	}
	
	public Texture(NBTTagCompound tag) {
		if(tag.hasKey("Signature")) {
			this.signature = tag.getString("Signature");
		}
		if(tag.hasKey("Value")) {
			this.value = tag.getString("Value");
		}
	}
	
	public NBTTagCompound toNBTTagCompound() {
		
		NBTTagCompound tag = new NBTTagCompound();
		
		if(this.signature != null) {
			tag.set("Signature", new NBTTagString(this.signature));
		}
		if(this.value != null) {
			tag.set("Value", new NBTTagString(this.value));
		}
		
		return tag;
	}

	public static void save(Texture te, int id, MySql mysql, String tablename) {
		
		String signature = te.signature;
		String value = te.value;
		
		if(signature != null) {
			signature = "'" + signature + "'";
		}
		
		if(value != null) {
			value = "'" + value + "'";
		}
		
		String command = "INSERT INTO " + tablename + " "
				+ "("
				+ "id,signature,value"
				+ ") VALUES ("
				+ "" + id
				+ "," + signature
				+ "," + value
				+ ");";
		mysql.command(command);
		
	}
	
	public static Texture load(int id, MySql mysql, String tablename) {
		ResultSet result = mysql.getResult("SELECT * FROM " + tablename + " WHERE id=" + id);
		
		Texture te = new Texture();
		
		try {
			if(result.next()) {
				String signature = result.getString(2);
				String value;
				value = result.getString(3);
				
				te.signature = signature;
				te.value = value;
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return te;
		
	}
	
	public static void createtable(MySql mysql, String tablename) {
		
		String command = "CREATE TABLE IF NOT EXISTS " + tablename + " "
				+ "("
				+ "id INT Primary Key,"
				+ "signature TEXT,"
				+ "value TEXT"
				+ ""
				+ ");";
		
		mysql.command(command);
		
	}
}
