package de.falco.essentialsxxx.phoenixstack;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import de.falco.essentialsxxx.util.MySql;
import net.minecraft.server.v1_12_R1.Item;
import net.minecraft.server.v1_12_R1.NBTBase;
import net.minecraft.server.v1_12_R1.NBTTagByte;
import net.minecraft.server.v1_12_R1.NBTTagByteArray;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagDouble;
import net.minecraft.server.v1_12_R1.NBTTagInt;
import net.minecraft.server.v1_12_R1.NBTTagIntArray;
import net.minecraft.server.v1_12_R1.NBTTagList;
import net.minecraft.server.v1_12_R1.NBTTagLong;
import net.minecraft.server.v1_12_R1.NBTTagLongArray;
import net.minecraft.server.v1_12_R1.NBTTagShort;
import net.minecraft.server.v1_12_R1.NBTTagString;

/*
 * interface containing methods useable to save items in database ATTENTION: you have to register the tables with EssentialsXXXNBTTagManager
 * 
 * @author FalcoW
 * @copyright PhoenixGames.net
 * @version 1.0
 * @see EssentialsXXXNBTTagManager
 */
public interface NBTdatabase {
	
	
	/*
	 * delete itemstack
	 * 
	 * @param id of the item
	 * @param mysql connection
	 * @param tablename
	 * @param tablename of NBTTagCompound's
	 * @param tablename of NBTTagList's
	 * @return time it takes
	 */
	default long deleteItemStack(int id, MySql mysql, String tablename, String NBTTagCompoundtablename, String NBTTagListtablename) {
		
		long t1 = System.currentTimeMillis();
		
		ResultSet result = mysql.getResult("SELECT tag FROM " + tablename + " WHERE `id`=" + id);
		try {
			
			mysql.command("DELETE FROM " + tablename + " WHERE `id`=" + id);
			
			if(result.next()) {
				
				int number = result.getInt(1);
				
				if(number > -1) {
					deleteNBTTagCompound(number,mysql,NBTTagCompoundtablename,NBTTagListtablename);	
				}
				
			}
		} catch (SQLException | NullPointerException e) {
			e.printStackTrace();
			System.out.println(" item with id " + id + " is already deleted?");
		}
		
		long t2 = System.currentTimeMillis();
		
		return (t2-t1);
		
	}
	
	
	/*
	 * delete a NBTTagCompound
	 * 
	 * @param id of the NBTTagCompound
	 * @param tablename of the NBTTagCompound's
	 * @param tablename of the NBTTagList's
	 * @return time it takes
	 */
	default long deleteNBTTagCompound(int id, MySql mysql, String tablename, String NBTTagListtablename) {
		
		long t1 = System.currentTimeMillis();
		
		ResultSet result = mysql.getResult("SELECT NBTTagCompound,NBTTagList FROM " + tablename + " WHERE `id`=" + id);
		
		try {
			
			mysql.command("DELETE FROM " + tablename + " WHERE `id`=" + id);
			
			if(result.next()) {
				
				String nbttagcompoundids = result.getString(1);
				
				if(nbttagcompoundids != null) {
					for(String i : nbttagcompoundids.split("§")) {
						try {
							int number = Integer.parseInt(i.split(":")[1]);
							deleteNBTTagCompound(number,mysql,tablename,NBTTagListtablename);
						}catch(NumberFormatException ex) {
							ex.printStackTrace();
						}
					}
				}
				
				String nbttaglistids = result.getString(2);
				
				if(nbttaglistids != null) {
					for(String i : nbttaglistids.split("§")) {
						try {
							int number = Integer.parseInt(i.split(":")[1]);
							deleteNBTTagList(number, mysql, NBTTagListtablename, tablename);
						}catch(NumberFormatException ex) {
							ex.printStackTrace();
						}
					}
				}
			}
		} catch (SQLException | NullPointerException e) {
			e.printStackTrace();
		}
		
		long t2 = System.currentTimeMillis();
		
		return (t2-t1);
		
	}

	
	/*
	 * delete a NBTTagList
	 * 
	 * @param id of the NBTTagList
	 * @param mysql connection
	 * @param tablename of the NBTTagList
	 * @param tablename of the NBTTagCompound's
	 * @return time it takes
	 */
	default long deleteNBTTagList(int id, MySql mysql, String tablename, String NBTTagCompoundtablename) {
		
		long t1 = System.currentTimeMillis();
		
		ResultSet result = mysql.getResult("SELECT NBTTagCompoundlist FROM " + tablename + " WHERE `id`=" + id);
		try {
			if(result.next()) {
				
				mysql.command("DELETE FROM " + tablename + " WHERE `id`=" + id);
				
				String nbttagcompoundlistids = result.getString(1);
				if(nbttagcompoundlistids != null) {
					for(String i : nbttagcompoundlistids.split("§")) {
						try {
							int number = Integer.parseInt(i);
							deleteNBTTagCompound(number, mysql, NBTTagCompoundtablename, tablename);
						}catch(NumberFormatException ex) {
							ex.printStackTrace();
						}
					}	
				}
				
			}
		} catch (SQLException | NullPointerException e) {
			e.printStackTrace();
		}
		
		long t2 = System.currentTimeMillis();
		
		return (t2-t1);
		
	}
	
	
	/*
	 * save a NBTTagList
	 * 
	 * @param the NBTTagList
	 * @param id of the NBTTagList
	 * @param mysql connection
	 * @param table for NBTTagList
	 * @param NBTTagCompoundtablename for the NBTTagCompounds, which are in the NBTTagList
	 * @return the time it takes
	 */
	default long saveNBTTagList(NBTTagList tag, int id, MySql mysql, String tablename, String NBTTagCompoundtablename) throws ServerIsDownException {
		
		long t1 = System.currentTimeMillis();
		
		StringBuilder stringids = new StringBuilder();
		StringBuilder NBTTagCompoundids = new StringBuilder();
		
		for(int x = 0; x < tag.size(); x++) {
			
			NBTTagCompound t = tag.get(x);
			
			if(t.isEmpty()) {//if t.isEmpty() it isn't a NBTTagCompound, then it only could be a String
				
				String tmp = tag.getString(x).replaceAll("'", "<Single Quote>").replaceAll("`", "<Backquote>");
				stringids.append("§" + tmp);
				
			}else {
				
				int number = EssentialsXXXNBTTagManager.getnewnumber(NBTTagCompoundtablename);
				
				NBTTagCompoundids.append("§" + number);
				
				saveNBTTagCompound(t,number,mysql,NBTTagCompoundtablename,tablename);
				
			}
			
		}//for
		
		String stringend = null;
		String NBTTagCompoundend = null;
		
		if(stringids.toString().isEmpty() == false) {
			stringend = "'" + stringids.toString().replaceFirst("§", "") + "'";
		}
		
		if(NBTTagCompoundids.toString().isEmpty() == false) {
			NBTTagCompoundend = "'" + NBTTagCompoundids.toString().replaceFirst("§", "") + "'";
		}
		
		String command = "INSERT INTO `" + tablename + "` "
				+ "(id,stringlist,NBTTagCompoundlist) VALUES ("
				+ "" + id
				+ "," + stringend
				+ "," + NBTTagCompoundend
				+ ""
				+ ");";
		mysql.command(command);
		
		long t2 = System.currentTimeMillis();
		
		return (t2-t1);
		
	}

	/*
	 * save a NBTTagCompound
	 * 
	 * @param the NBTTagCompound
	 * @param the id of the NBTTagCompound
	 * @param mysql connection
	 * @param the tablename where to save the NBTTagCompounds and all NBTTagCompounds, which are in the NBTTagCompound
	 * @param the tablename where to save all NBTTagList's
	 */
	default void saveNBTTagCompound(NBTTagCompound tag, int id, MySql mysql, String tablename, String NBTTagListtablename) throws ServerIsDownException {
		
		StringBuilder byteids = new StringBuilder();
		StringBuilder intids = new StringBuilder();
		StringBuilder shortids = new StringBuilder();
		StringBuilder longids = new StringBuilder();
		StringBuilder doubleids = new StringBuilder();
		
		StringBuilder Stringids = new StringBuilder();
		
		StringBuilder IntArrayids = new StringBuilder();
		StringBuilder ByteArrayids = new StringBuilder();
		
		StringBuilder NBTTagCompoundids = new StringBuilder();
		
		StringBuilder NBTTagListids = new StringBuilder();
		
		String byteend = null;
		String intend = null;
		String shortend = null;
		String longend = null;
		String doubleend = null;
		
		String Stringend = null;
		String IntArrayend = null;
		String ByteArrayend = null;
		
		String NBTTagCompoundend = null;
		String NBTTagListend = null;
		
		
		for(String key : tag.c()) {
			
			NBTBase base = tag.get(key);
			
			if(base instanceof NBTTagByte) {
				byte tmp = tag.getByte(key);
				byteids.append("§" + key + ":" + tmp);
			}
			
			if(base instanceof NBTTagInt) {
				int tmp = tag.getInt(key);
				intids.append("§" + key + ":" + tmp);
			}
			
			if(base instanceof NBTTagShort) {
				short tmp = tag.getShort(key);
				shortids.append("§" + key + ":" + tmp);
			}
			
			if(base instanceof NBTTagDouble) {
				double tmp = tag.getDouble(key);
				doubleids.append("§" + tmp);
			}
			
			if(base instanceof NBTTagLong) {
				long tmp = tag.getLong(key);
				longids.append("§" + key + ":" + tmp);
			}
			
			if(base instanceof NBTTagString) {
				String tmp = tag.getString(key);
				Stringids.append("§" + key + ":" + tmp.replaceAll(":", "<colon>").replaceAll("'", "<Single Quote>").replaceAll("`", "<Backquote>"));
			}
			
			
			if(base instanceof NBTTagIntArray) {
				int[] tmp = tag.getIntArray(key);
				
				StringBuilder t = new StringBuilder();
				
				for(int x : tmp) {
					t.append("/" + x);
				}
				
				IntArrayids.append("§" + key + ":" + t.toString().replaceFirst("/", ""));
				
			}
			
			if(base instanceof NBTTagByteArray) {
				byte[] tmp = tag.getByteArray(key);
				
				StringBuilder t = new StringBuilder();
				
				for(byte x : tmp) {
					t.append("/" + t);
				}
				
				ByteArrayids.append("§" + key + ":" + t.toString().replaceFirst("/", ""));
				
			}
			
			
			if(base instanceof NBTTagCompound) {
				NBTTagCompound t = tag.getCompound(key);
				int number = EssentialsXXXNBTTagManager.getnewnumber(tablename);
				NBTTagCompoundids.append("§" + key + ":" + number);
				this.saveNBTTagCompound(t, number, mysql, tablename, NBTTagListtablename);
			}
			
			if(base instanceof NBTTagList) {
				NBTTagList t = (NBTTagList) tag.get(key);
				int number = EssentialsXXXNBTTagManager.getnewnumber(NBTTagListtablename);
				NBTTagListids.append("§" + key + ":" + number);
				this.saveNBTTagList(t, number, mysql, NBTTagListtablename, tablename);
			}
		}//for
		
		if(byteids.toString().isEmpty() == false) {
			byteend = "'" + byteids.toString().replaceFirst("§", "") + "'";
		}
		if(intids.toString().isEmpty() == false) {
			intend = "'" + intids.toString().replaceFirst("§", "") + "'";
		}
		if(shortids.toString().isEmpty() == false) {
			shortend = "'" + shortids.toString().replaceFirst("§", "") + "'";
		}
		if(longids.toString().isEmpty() == false) {
			longend = "'" + longids.toString().replaceFirst("§", "") + "'";
		}
		if(doubleids.toString().isEmpty() == false) {
			doubleend = "'" + doubleids.toString().replaceFirst("§", "") + "'";
		}
		
		if(Stringids.toString().isEmpty() == false) {
			Stringend = "'" + Stringids.toString().replaceFirst("§", "") + "'";
		}
		if(IntArrayids.toString().isEmpty() == false) {
			IntArrayend = "'" + IntArrayids.toString().replaceFirst("§", "") + "'";
		}
		if(ByteArrayids.toString().isEmpty() == false) {
			ByteArrayend = "'" + ByteArrayids.toString().replaceFirst("§", "") + "'";
		}
		
		
		if(NBTTagCompoundids.toString().isEmpty() == false) {
			NBTTagCompoundend = "'" + NBTTagCompoundids.toString().replaceFirst("§", "") + "'";
		}
		if(NBTTagListids.toString().isEmpty() == false) {
			NBTTagListend = "'" + NBTTagListids.toString().replaceFirst("§", "") + "'";
		}
		
		String command = "INSERT INTO `" + tablename + "` "
				+ "("
				+ "id,byteT,shortT,doubleT,intT,stringT,longT,intarray,bytearray,NBTTagCompound,NBTTagList"
				+ ") VALUES ("
				+ "" + id
				+ "," + byteend
				+ "," + shortend
				+ "," + doubleend
				+ ","  + intend
				+ "," + Stringend
				+ "," + longend
				+ "," + IntArrayend
				+ "," + ByteArrayend
				+ "," + NBTTagCompoundend
				+ "," + NBTTagListend
				+ ");";
		
		//System.out.println("[essentialsxxx-phoenixstack] save item => " + command);
		
		mysql.command(command);
		
	}

	/*
	 * save an item 
	 * 
	 * @param the item you want to save
	 * @param unique id of the item 
	 * @param the itemstack table
	 * @param the table were all nbttagcompounds will save
	 * @param table were all nbttaglist's will save
	 * @return the time it takes
	 */
	default long saveItemStack(ItemStack item, int id, MySql mysql, String tablename, String NBTTagCompoundtablename, String NBTTagListtablename) throws ServerIsDownException {
		
		long t1 = System.currentTimeMillis();
		
		net.minecraft.server.v1_12_R1.ItemStack itemstack = CraftItemStack.asNMSCopy(item);
		int material = Item.getId(itemstack.getItem());
		int count = itemstack.getCount();
		int damage = itemstack.getData();
		
		
		int tag = -1;
		
		if(itemstack.hasTag()) {
			int number = EssentialsXXXNBTTagManager.getnewnumber(NBTTagCompoundtablename);
			tag = number;
			saveNBTTagCompound(itemstack.getTag(), number, mysql, NBTTagCompoundtablename, NBTTagListtablename);
		}
		
		String command = "INSERT INTO " + tablename + " "
				+ "("
				+ "id,material,count,damage,tag"
				+ ") VALUES ("
				+ "" + id
				+ "," + material
				+ "," + count
				+ "," + damage
				+ "," + tag
				+ ");"; 
		mysql.command(command);
		
		long t2 = System.currentTimeMillis();
		
		return (t2-t1);
		
		
		
	}
	
	
	/*
	 * load an item
	 * 
	 * @param id of the item
	 * @param mysql connection
	 * @param tablename where the itemstack is saved
	 * @param tablename of the NBTTagCompound's
	 * @param tablename of the NBTTagList's
	 * @return ItemStack
	 * @throws NullPointerException when there isn't a item with the id
	 */
	default ItemStack loadItemStack(int id, MySql mysql, String tablename, String NBTTagCompoundtablename, String NBTTagListtablename) {
		
		ResultSet result = mysql.getResult("SELECT * FROM " + tablename + " WHERE id=" + id);
		
		try {
			if(result.next()) {
				
				int material = result.getInt(2);
				int amount = result.getInt(3);
				int damage = result.getInt(4);
				int tag = result.getInt(5);
				
				
				ItemStack end = new ItemStack(material,amount);
				
				net.minecraft.server.v1_12_R1.ItemStack item = CraftItemStack.asNMSCopy(end);
				item.setData(damage);
				
				if(tag != -1) {
					
					NBTTagCompound tagtmp = loadNBTTagCompound(tag,mysql,NBTTagCompoundtablename,NBTTagListtablename);
					item.setTag(tagtmp);
					
				}
				
				end = CraftItemStack.asBukkitCopy(item);
				
				return end;
				
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		throw new NullPointerException("couldnt find item with id " + id);
		
	}
	
	/*
	 * load a NBTTagList
	 * 
	 * @param id of the NBTTagList
	 * @param mysql connection
	 * @param tablename of the NBTTagList
	 * @param tablename of the NBTTagCompounds (maybe were saved in the List)
	 * @throws NullPointerException when there isn't a NBTTagList with the id
	 */
	default NBTTagList loadNBTTagList(int id, MySql mysql, String tablename, String NBTTagCompoundtablename) {
		
		NBTTagList list = new NBTTagList();
		
		
		ResultSet result = mysql.getResult("SELECT stringlist,nbttagcompoundlist FROM " + tablename + " WHERE id=" + id);
		try {
			if(result.next()) {
				String stringids = result.getString(1);
				String nbttagcompoundids = result.getString(2);
				
				if(stringids != null) {
					
					stringids = stringids.replaceAll("<Single Quote>", "'").replaceAll("<Backquote>", "`");
					
					for(String i : stringids.split("§")) {
						list.add(new NBTTagString(i));
					}
					
				}else if(nbttagcompoundids != null) {
					
					for(String i : nbttagcompoundids.split("§")) {
						
						int number = Integer.parseInt(i);
						
						NBTTagCompound n = loadNBTTagCompound(number,mysql,NBTTagCompoundtablename,tablename);
						list.add(n);
					}
					
				}
				
				return list;
				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		throw new NullPointerException("there isn't a NBTTagList with id " + id);
		
	}
	
	/*
	 * load a NBTTagCompound
	 * 
	 * @param id of the NBTTagCompound
	 * @param mysql connection
	 * @param tablename where the NBTTagCompound, and all NBTTagCompound's in the Compound, were saved
	 * @param tablename of the NBTTagList's
	 */
	default  NBTTagCompound loadNBTTagCompound(int id, MySql mysql, String tablename, String NBTTagListtablename) {
		
		NBTTagCompound end = new NBTTagCompound();
		
		
		ResultSet result = mysql.getResult("SELECT * FROM `" + tablename + "` WHERE id=" + id);
		try {
			if(result.next()) {
				
				String byteT = result.getString(2);
				String intT = result.getString(3);
				String shortT = result.getString(4);
				String longT = result.getString(5);
				String doubleT = result.getString(6);
				String stringT = result.getString(7);
				
				String intarray = result.getString(8);
				String bytearray = result.getString(9);
				
				String NBTTagCompound = result.getString(10);
				String NBTTagList = result.getString(11);
				
				if(NBTTagList != null) {
					
					for(String one : NBTTagList.split("§")) {
						String key = one.split(":")[0];
						String number = one.split(":")[1];
						int n = Integer.parseInt(number);
						
						NBTTagList tmp = loadNBTTagList(n, mysql, NBTTagListtablename, tablename);
						
						end.set(key, tmp);
					}
					
				}
				
				if(NBTTagCompound != null) {
					
					for(String one : NBTTagCompound.split("§")) {
						String key = one.split(":")[0];
						String number = one.split(":")[1];
						int n = Integer.parseInt(number);
						
						NBTTagCompound tmp = loadNBTTagCompound(n, mysql, tablename, NBTTagListtablename);
						
						end.set(key, tmp);
					}
					
				}
				
				if(intarray != null) {
					
					for(String list : intarray.split("§")) {
						
						String key = list.split(":")[0];
						String worth = list.split(":")[1];
						
						String[] worthsplit = worth.split("/");
						
						int[] tmp = new int[worthsplit.length];
						
						for(int x = 0; x < worthsplit.length; x++) {
							tmp[x] = Integer.parseInt(worthsplit[x]);
						}
						
						end.set(key, new NBTTagIntArray(tmp));
						
					}
				}
				
				if(bytearray != null) {
					
					for(String list : bytearray.split("§")) {
						
						String key = list.split(":")[0];
						String worth = list.split(":")[1];
						
						String[] worthsplit = worth.split("/");
						
						byte[] tmp = new byte[worthsplit.length];
						
						for(int x = 0; x < worthsplit.length; x++) {
							tmp[x] = Byte.parseByte(worthsplit[x]);
						}
						
						end.set(key, new NBTTagByteArray(tmp));
						
					}
				}
				
				//
				if(doubleT != null) {
					for(String i : doubleT.split("§")) {
						try {
							
							double b = Double.parseDouble(i.split(":")[1]);
							String key = i.split(":")[0];
							end.set(key, new NBTTagDouble(b));
							
						}catch(Exception e) {
							System.out.println("[essentialsxxx-phoenixstack] there is an error while trying to load a NBTTagCompound table:" + tablename + " id:" + id);
							e.printStackTrace();
							break;
						}
					}
				}
				if(longT != null) {
					for(String i : longT.split("§")) {
						try {
							
							long b = Long.parseLong(i.split(":")[1]);
							String key = i.split(":")[0];
							end.set(key, new NBTTagLong(b));
							
						}catch(Exception e) {
							System.out.println("[essentialsxxx-phoenixstack] there is an error while trying to load a NBTTagCompound table:" + tablename + " id:" + id);
							e.printStackTrace();
							break;
						}
					}
				}
				if(stringT != null) {
					
					
					for(String i : stringT.split("§")) {
						try {
							
							String b = "";
							
							try {
								b = i.split(":")[1];	
							}catch(ArrayIndexOutOfBoundsException es) {
								//es.printStackTrace();
							}
							
							String key = i.split(":")[0];
							end.set(key, new NBTTagString(b.replaceAll("<colon>", ":").replaceAll("<Single Quote>", "'").replaceAll("<Backquote>", "`")));
							
						}catch(Exception e) {
							System.out.println("[essentialsxxx-phoenixstack] there is an error while trying to load a NBTTagCompound table:" + tablename + " id:" + id);
							e.printStackTrace();
							break;
						}
					}
				}
				if(shortT != null) {
					for(String i : shortT.split("§")) {
						try {
							
							short b = Short.parseShort(i.split(":")[1]);
							String key = i.split(":")[0];
							end.set(key, new NBTTagShort(b));
							
						}catch(Exception e) {
							System.out.println("[essentialsxxx-phoenixstack] there is an error while trying to load a NBTTagCompound table:" + tablename + " id:" + id);
							e.printStackTrace();
							break;
						}
					}
				}
				if(byteT != null) {
					for(String i : byteT.split("§")) {
						try {
							
							byte b = Byte.parseByte(i.split(":")[1]);
							String key = i.split(":")[0];
							end.set(key, new NBTTagByte(b));
							
						}catch(Exception e) {
							System.out.println("[essentialsxxx-phoenixstack] there is an error while trying to load a NBTTagCompound table:" + tablename + " id:" + id);
							e.printStackTrace();
							break;
						}
					}
				}
				if(intT != null) {
					for(String i : intT.split("§")) {
						try {
							
							int b = Integer.parseInt(i.split(":")[1]);
							String key = i.split(":")[0];
							end.set(key, new NBTTagInt(b));
							
						}catch(Exception e) {
							System.out.println("[essentialsxxx-phoenixstack] there is an error while trying to load a NBTTagCompound table:" + tablename + " id:" + id);
							e.printStackTrace();
							break;
						}
					}
				}
				
				return end;
				
			}//result.next()
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		throw new NullPointerException("there isnt a NBTTagCompound with id " + id + " in table " + tablename);
		
	}
	
	
	/*
	 * old
	 */
	
	/*
	default void saveIntArray(int[] tag, int id, MySql mysql, String tablename) {
		
		StringBuilder builder = new StringBuilder();
		
		for(int x = 0; x < tag.length; x++) {
			builder.append("§" + tag[x]);
		}
		
		String end = builder.toString();
		
		if(end != null) {
			
			end = "'" + end.replaceFirst("§", "");
			
		}
		
		mysql.command("INSERT INTO " + tablename + " "
				+ "("
				+ "id,intlist"
				+ ") VALUES ("
				+ "" + id
				+ "," + end
				+ ");");
	}
	
	default void saveByteArray(byte[] tag, int id, MySql mysql, String tablename) {
		
		StringBuilder builder = new StringBuilder();
		
		for(int x = 0; x < tag.length; x++) {
			builder.append("§" + tag[x]);
		}
		
		String end = builder.toString();
		
		if(end != null) {
			
			end = "'" + end.replaceFirst("§", "");
			
		}
		
		mysql.command("INSERT INTO " + tablename + " "
				+ "("
				+ "id,bytelist"
				+ ") VALUES ("
				+ "" + id
				+ "," + end
				+ ");");
		
	}
	*/
	
	/*
	default void saveLongArray(long[] tag, int id, MySql mysql, String tablename) {
		
		StringBuilder builder = new StringBuilder();
		
		for(int x = 0; x < tag.length; x++) {
			builder.append("§" + tag[x]);
		}
		
		String end = builder.toString();
		
		if(end != null) {
			
			end = "'" + end.replaceFirst("§", "");
			
		}
		
		mysql.command("INSERT INTO " + tablename + " "
				+ "("
				+ "id,longlist"
				+ ") VALUES ("
				+ "" + id
				+ "," + end
				+ ");");
		
	}
	*/
	
}
