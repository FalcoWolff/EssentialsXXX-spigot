package de.falco.essentialsxxx.phoenixstack.metatypes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import de.falco.essentialsxxx.phoenixstack.PhoenixMeta;
import de.falco.essentialsxxx.phoenixstack.PhoenixStackException;
import de.falco.essentialsxxx.phoenixstack.datatypes.CustomPotionEffect;
import de.falco.essentialsxxx.phoenixstack.datatypes.FireworkExplosion;
import de.falco.essentialsxxx.util.MySql;
import net.minecraft.server.v1_12_R1.NBTTagByte;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;

public class Firework extends PhoenixMeta{
	
	/*
	 * >> Variables <<
	 */
	
	public byte flight = 0;
	public ArrayList<FireworkExplosion> explosions = new ArrayList<>();
	
	/*
	 * >> constructor <<
	 */
	
	public Firework(NBTTagCompound tag) {
		this.serialize(tag);
	}
	
	public Firework() {
		
	}
	
	/*
	 * >> methods <<
	 */
	
	@Override
	public void serialize(NBTTagCompound t) {
		super.serialize(t);
		
		NBTTagCompound tag = (NBTTagCompound) t.get("Fireworks");
		
		if(tag.hasKey("Flight")) {
			try {
				this.flight = tag.getByte("Flight");
			}catch(Exception ex) {}
		}
		
		if(tag.hasKey("Explosions")) {
			
			try {
				
				NBTTagList list = (NBTTagList) tag.get("Explosions");
				
				for(int x = 0; x < list.size(); x++) {
					NBTTagCompound c = list.get(x);
					
					this.explosions.add(new FireworkExplosion(c));
				}
				
			}catch(Exception ex) {}
			
		}
		
	}
	
	@Override
	public NBTTagCompound deserialize() {
		
		NBTTagCompound result = super.deserialize();
		
		result.set("Flight", new NBTTagByte(flight));
		
		if(this.hasexplosions()) {
			
			NBTTagList explosions = new NBTTagList();
			
			for(FireworkExplosion ex : this.getExplosions()) {
				NBTTagCompound exposion = ex.toNBTTagCompound();
				explosions.add(exposion);
			}
			
			result.set("Explosions", explosions);
			
		}
		
		NBTTagCompound re = new NBTTagCompound();
		re.set("Fireworks", result);
		
		
		return re;
	}
	
	/*
	 * >> static <<
	 */
	
	public static void createtable(MySql mysql, String tablename) {
		String command = "CREATE TABLE IF NOT EXISTS " + tablename + ""
				+ "("
				+ "id int Primary Key,"
				+ "flight TINYINT,"
				+ "explosions TEXT"
				+ ");";
		mysql.command(command);
	}
	
	public static Firework load(int id, MySql mysql, String tablename, Firework fw) throws PhoenixStackException {
		
		
		String command = "SELECT * FROM firework WHERE id=" + id;
		
		ResultSet result = mysql.getResult(command);
		
		try {
			
			if(result.next()) {
				
				byte flight = result.getByte(2);
				
				String explosions = result.getString(3);
				
				fw.setFlight(flight);
				
				if(explosions != null) {
					
					ArrayList<FireworkExplosion> explosionsend = new ArrayList<>();
					
					for(String i : explosions.split("§")) {
						
						int idtmp = 0;
						
						try {
							idtmp = Integer.parseInt(i);
						}catch(NumberFormatException ex) {
							ex.printStackTrace();
							break;
						}
						
						
						FireworkExplosion ex = FireworkExplosion.load(idtmp, mysql, "fireworkexplosion");
						
						explosionsend.add(ex);
						
					}
					
					fw.setExplosions(explosionsend);
					
					
				}
				
				return fw;
				
			}else {
				return fw;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return fw;
		}
	}
	
	public static void save(int id, MySql mysql, String tablename, Firework fw) {
		
		byte flight = fw.getFlight();
		
		String explosions = null;
		
		if(fw.explosions.isEmpty() == false) {
			
			StringBuilder builder = new StringBuilder();
			
			int number = -1;
			
			String command = "SELECT id FROM fireworkexplosion";
			ResultSet re = mysql.getResult(command);
			
			try {
				if(re.last()) {
					number = re.getInt(1);
				}
			} catch (SQLException e) {}
			
			for(FireworkExplosion ex : fw.explosions) {
				
				number++;
				
				builder.append("§" + number);
				
				FireworkExplosion.save(ex, number, mysql, "fireworkexplosion");
				
			}
			
			explosions = "'" + builder.toString().replaceFirst("§", "") + "'";
			
		}
		
		
		String command = "INSERT INTO firework"
				+ "("
				+ "id,flight,explosions"
				+ ") VALUES ("
				+ "" + id
				+ "," + flight
				+ "," + explosions
				+ ");";
		mysql.command(command);
		
		
	}
	
	/*
	 * >> has <<
	 */
	
	public boolean hasexplosions() {
		if(explosions.isEmpty()) {
			return false;
		}else {
			return true;
		}
	}
	
	/*
	 * >> setter <<
	 */
	
	public void setExplosions(ArrayList<FireworkExplosion> explosions) {
		this.explosions = explosions;
	}
	public void setFlight(byte flight) {
		this.flight = flight;
	}

	/*
	 * >> add <<
	 */
	
	public void addexplosion(FireworkExplosion ex) {
		explosions.add(ex);
	}
	
	/*
	 * >> getter <<
	 */
	
	public byte getFlight() {
		return flight;
	}
	public ArrayList<FireworkExplosion> getExplosions() {
		return explosions;
	}
	
}
