package de.falco.essentialsxxx.phoenixstack.datatypes;

import java.sql.ResultSet;
import java.sql.SQLException;

import de.falco.essentialsxxx.phoenixstack.PhoenixStackException;
import de.falco.essentialsxxx.util.MySql;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagDouble;
import net.minecraft.server.v1_12_R1.NBTTagInt;
import net.minecraft.server.v1_12_R1.NBTTagLong;
import net.minecraft.server.v1_12_R1.NBTTagString;

/*
 * @Info class to handle attributs in combination with minecraftitems
 * @see de.falco.essentialsxxx.phoenixstack.PhoenixMeta
 */
public class AttributeModifier {
	
	/*
	 * >> fields <<
	 */
	private AttributeName attributename;
	private String name;
	private Slot slot;
	private Operation operation;
	private double amount;
	private long uuidmost;
	private long uuidleast;
	
	
	/*
	 * >> methods <<
	 */
	
	/*
	 * >> static <<
	 */
	
	/*
	 * @Info create a table to save attributemodifers
	 * @param mysql connection
	 * @param tablename
	 */
	public static void createtable(MySql mysql, String tablename) {
		
		
		  String command = "CREATE TABLE IF NOT EXISTS " + tablename + " (" + 
				  " id int Primary Key," + 
				  " attributename TEXT," + 
				  " name TEXT," + 
				  " slot TEXT," + 
				  " operation INT," + 
				  " amount DOUBLE," + 
				  " uuidleast BIGINT," + 
				  " uuidmost BIGINT" +
				  ");";
		  mysql.command(command);
		
	}
	
	/*
	 * @Info load a Attribute from a db
	 * @param id of the item
	 * @param mysql connection
	 * @param tablename
	 */
	public static AttributeModifier load(int id, MySql mysql, String tablename) throws PhoenixStackException {
		
		ResultSet result = mysql.getResult("SELECT * FROM attributemodifiers WHERE id='" + id + "'");
		
		try {
			
			if(result.next()) {
				
				String attributename = result.getString(2);
				String name = result.getString(3);
				String slot = result.getString(4);
				int operation = result.getInt(5);
				double amount  = result.getDouble(6);
				long uuidleast = result.getLong(7);
				long uuidmost = result.getLong(8);
				
				AttributeModifier modifier = new AttributeModifier(AttributeName.getattributenamebyname(attributename), name, Slot.getbyname(slot), Operation.getbyid(operation), amount, uuidmost, uuidleast);  
				
				return modifier;
				
			}else {
				throw new PhoenixStackException("[essentialsxxx-phoenixstack] couldn't find item with id " + id);
			}
			
		} catch (SQLException e) {
			throw new PhoenixStackException("[essentialsxxx-phoenixstack] couldn't find item with id " + id);
		}
		
		
		
	}
	
	/*
	 * @Info save an AttributeModifier
	 * @param id of the item
	 * @param mysql connection
	 * @param tablename
	 */
	public static void save(AttributeModifier at, int id, MySql mysql, String tablename) {
		
		
		String attributename = at.getAttributename().getName();
		String name = at.getName();
		String slot = at.getSlot().getName();
		int operation = at.getOperation().getOperation();
		double amount = at.getAmount();
		long uuidmost = at.getUuidmost();
		long uuidleast = at.getUuidleast();
		
		if(name != null) {
			name = "'" + name + "'";
		}
		if(attributename != null) {
			attributename = "'" + attributename + "'";
		}
		if(slot != null) {
			slot = "'" + slot + "'";
		}
		
		
		String command = "INSERT INTO attributemodifiers ("
				+ "id,"
				+ "attributename,"
				+ "name,"
				+ "slot,"
				+ "operation,"
				+ "amount,"
				+ "uuidleast,"
				+ "uuidmost)"
				+ " VALUES "
				+ "("
				+ "" + id
				+ "," + attributename
				+ "," + name
				+ "," + slot
				+ "," + operation
				+ "," + amount
				+ "," + uuidleast
				+ "," + uuidmost
				+ ");";
		
		
		mysql.command(command);
	}
	
	/*
	 * >> non static <<
	 */
	
	/*
	 * @Info change a AttributeModifier into a NTBTagCompound
	 * @return the created NBTTagCompound
	 */
	public NBTTagCompound toNBTTagCompound() {
		
		NBTTagCompound tag = new NBTTagCompound();
		
		tag.set("AttributeName", new NBTTagString(attributename.getName()));
		tag.set("Name", new NBTTagString(name));
		tag.set("Slot", new NBTTagString(slot.getName()));
		tag.set("Operation", new NBTTagInt(operation.getOperation()));
		tag.set("Amount", new NBTTagDouble(amount));
		tag.set("UUIDMost", new NBTTagLong(uuidmost));
		tag.set("UUIDLeast", new NBTTagLong(uuidleast));
		
		return tag;
	}
	
	
	/*
	 * >> enums <<
	 */
	
	enum AttributeName{
		maxHealth,followRange,
		knockbackResistance,movementSpeed,
		attackDamage,armor,
		armorToughness,attackKnockback,
		attackSpeed,luck,
		jumpStrength,flyingSpeed,
		spawnReinforcements, unknown;
		
		public String getName() {
			return "generic." + this.toString();
		}
		
		public static AttributeName getattributenamebyname(String name) {
			for(AttributeName at : AttributeName.values()) {
				if(at.getName().equals(name)) {
					return at;
				}
			}
			
			return unknown;
		}
	}
	
	enum Operation{
		add(0),multiple(1),multipleadd(2),unknown(3);
		
		private final int operation;
		
		Operation(int i) {
			this.operation = i;
		}
		
		public int getOperation() {
			return operation;
		}
		
		public static Operation getbyid(int id) {
			for(Operation op : Operation.values()) {
				if(op.getOperation() == id) {
					return op;
				}
			}
			
			return unknown;
		}
	}
	
	enum Slot{
		
		mainhand,offhand,feet,legs,chest,head,unknown;
		
		public String getName() {
			return this.toString();
		}
		
		public static Slot getbyname(String name) {
			for(Slot slot : Slot.values()) {
				if(slot.getName().equals(name)) {
					return slot;
				}
			}
			
			return unknown;
		}
		
	}
	
	
	/*
	 * >> constructors <<
	 */
	
	/*
	 * @Info create a AttributeModifier with many components 
	 * @param attributename a unique name for a single attribute (list of these Attributes in enum AttributeName)
	 * @param name a nearly useless configuration option 
	 * @param slot you want to activate the attribute (list of these Slots in enum Slot)
	 * @param the operation you want to change uuid most and uuidleast (list of operations in enum Operation)
	 * @param amount of the effect
	 * @param uuidmost as one of two unique numbers
	 * @param uuidleast as one of two unique numbers
	 */
	public AttributeModifier(AttributeName at, String name, Slot slot, Operation op, double amount, long uuidmost, long uuidleast) {
		
		this.attributename = at;
		this.name = name;
		this.slot = slot;
		this.operation = op;
		this.amount = amount;
		this.uuidleast = uuidleast;
		this.uuidmost = uuidmost;
		
	}
	
	/*
	 * @Info use this constructor when get NBTTagCompound
	 * @param NBTTagCompound you want to cast
	 */
	public AttributeModifier(NBTTagCompound tag) {
		
		if(tag.hasKey("AttributeName")) {
			attributename = AttributeName.getattributenamebyname(tag.getString("AttributeName"));
		}
		if(tag.hasKey("Name")) {
			name = tag.getString("Name");
		}
		if(tag.hasKey("Slot")) {
			slot = Slot.getbyname(tag.getString("Slot"));
		}
		if(tag.hasKey("Operation")) {
			operation = Operation.getbyid(tag.getInt("Operation"));
		}
		if(tag.hasKey("Amount")) {
			amount = tag.getDouble("Amount");
		}
		if(tag.hasKey("UUIDMost")) {
			uuidmost = tag.getLong("UUIDMost");
		}
		if(tag.hasKey("UUIDLeast")) {
			uuidleast = tag.getLong("UUIDLeast");
		}
		
	}
	
	/*
	 * >> setter <<
	 */
	
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public void setAttributename(AttributeName attributename) {
		this.attributename = attributename;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setOperation(Operation operation) {
		this.operation = operation;
	}
	public void setSlot(Slot slot) {
		this.slot = slot;
	}
	public void setUuidleast(long uuidleast) {
		this.uuidleast = uuidleast;
	}
	public void setUuidmost(long uuidmost) {
		this.uuidmost = uuidmost;
	}
	
	/*
	 * >> getter <<
	 */
	
	public double getAmount() {
		return amount;
	}
	public AttributeName getAttributename() {
		return attributename;
	}
	public String getName() {
		return name;
	}
	public Operation getOperation() {
		return operation;
	}
	public Slot getSlot() {
		return slot;
	}
	public long getUuidleast() {
		return uuidleast;
	}
	public long getUuidmost() {
		return uuidmost;
	}

}
