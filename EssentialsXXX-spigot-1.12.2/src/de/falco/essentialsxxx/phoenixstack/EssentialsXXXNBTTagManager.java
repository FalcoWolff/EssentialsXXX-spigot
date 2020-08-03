package de.falco.essentialsxxx.phoenixstack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import de.falco.essentialsxxx.util.MySql;

/*
 * use this class to interact directly with the id-server
 * 
 * @author FalcoW
 * @copyright PhoenixGames.net
 * @version 1.0
 */
public class EssentialsXXXNBTTagManager {
	
	//prefix set before every debug message
	private static String prefix = "[EssentialsXXXNBTTagManager]";
	
	//numbers this server can use to save their data
	//key means the tablename and the Array all numbers available
	private static Map<String,ArrayList<Integer>> numbers = new LinkedHashMap<>();
	
	//ip and port of the ip-server
	private static String ip = "localhost";
	private static int port = 2233;
	
	//this field defines how many numbers you will get every time in method @see wantnumbers(String tablename);
	private static int wantnumbers = 200;
	
	
	/*
	 * create table for normal itemstacks
	 * 
	 * @param tablename
	 * @param mysql connection
	 * @return the time it takes to confirm the action
	 */
	public static long createtableItemStack(String tablename, MySql mysql) {
		
		long t1 = System.currentTimeMillis();
		
		String command = "CREATE TABLE IF NOT EXISTS `" + tablename + "` "
				+ "("
				+ "id int Primary Key"
				+ ",material int "
				+ ",count int"
				+ ",damage int"
				+ ",tag int"
				+ ");";
		mysql.command(command);
		
		long t2 = System.currentTimeMillis();
		
		return (t2-t1);
		
	}
	
	
	/*
	 * create table for NBTTagCompounds (when you want to save itemstacks you also have to save its nbttag-data)
	 * 
	 * @param tablename
	 * @param mysql connection
	 * @return the time it takes to confirm the action
	 */
	public static long createtableNBTTagCompound(String tablename, MySql mysql) {
		
		long t1 = System.currentTimeMillis();
			
		String command = "CREATE TABLE IF NOT EXISTS `" + tablename + "` "
				+ "("
				+ "id INT PRIMARY KEY"
				+ ",byteT TEXT"
				+ ",intT TEXT"
				+ ",shortT TEXT"
				+ ",longT TEXT"
				+ ",doubleT TEXT"
				+ ",stringT TEXT"
				+ ",intarray TEXT"
				+ ",bytearray TEXT"
				+ ",NBTTagCompound TEXT"
				+ ",NBTTagList TEXT"
				+ ");";
			
		mysql.command(command);
		
		long t2 = System.currentTimeMillis();
		
		return (t2-t1);
			
			
	}
	
	/*
	 * create table for NBTTagList (need for saving items)
	 * 
	 * @param tablename
	 * @param mysql connection
	 * @return the time it takes
	 */
	public static long createtableNBTTagList(String tablename, MySql mysql) {
		
		long t1 = System.currentTimeMillis();
		
		String command = "CREATE TABLE IF NOT EXISTS `" + tablename + "` "
				+ "("
				+ "id INT Primary Key"
				+ ",stringlist TEXT"
				+ ",NBTTagCompoundlist TEXT"
				+ ""
				+ ");";
		mysql.command(command);
		
		long t2 = System.currentTimeMillis();
		
		return (t2-t1);
		
		
	}
	
	
	/*
	 * register a table in the id-server 
	 * 
	 * @param the tablename
	 * @param mysql connection
	 * @return time the action takes
	 */
	public static long registerTable(String tablename, MySql mysql) throws ServerIsDownException {
		
		long t1 = System.currentTimeMillis();
		
		System.out.println(prefix + " try to register table " + tablename);
		
		Socket so = null;
		
		
		try {
			
			so = new Socket(ip,port);
			
			System.out.println(prefix + " connect to server");
			
		} catch (UnknownHostException e) {
			
			throw new ServerIsDownException(prefix + " couldnt connect to server down?");
			
		} catch (IOException e) {
			
			throw new ServerIsDownException(prefix + " server exception");
		}
		
		String path = mysql.getPath();
		String user = mysql.getUser();
		String pw = mysql.getPw();
		
		
		System.out.println(prefix + " path: " + path + " user: " + user + " pw.length: " + pw.length());
		
		PrintWriter writer = null;
		try {
			
			writer = new PrintWriter(so.getOutputStream());
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
			try {
				so.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			throw new ServerIsDownException(prefix + " connection lost. Server down?");
		}
		
		//parameter for the id-server
		writer.write("registertable\n");
		
		//writer.write(host + "\n");
		writer.write(port + "\n");
		//writer.write(database + "\n");
		writer.write(user + "\n");
		writer.write(pw + "\n");
		writer.write(tablename + "\n");
		
		writer.flush();
		
		writer.close();
		
		try {
			so.close();
		} catch (IOException e) {
			
			e.printStackTrace();
			
			throw new ServerIsDownException(prefix + " server exception");
			
		}
		
		System.out.println(prefix + " successfully register table");
		
		long t2 = System.currentTimeMillis();
		
		return (t2-t1);
		
		
	}
	
	/*
	 * get a list of new numbers from the id-server
	 * 
	 * @param the tablename you want to get numbers
	 * @return a list of numbers
	 */
	private static ArrayList<Integer> wantnumbers(String tablename) throws ServerIsDownException {
		
		System.out.println(prefix + " try to get " + wantnumbers + " ids from table: " + tablename);
		
		Socket so = null;
		
		try {
			so = new Socket(ip,port);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ServerIsDownException(prefix + " connection exception");
		}
		
		PrintWriter writer = null;
		BufferedReader reader = null;
		try {
			writer = new PrintWriter(so.getOutputStream());
			reader = new BufferedReader(new InputStreamReader(so.getInputStream()));
		} catch (IOException e) {
			
			e.printStackTrace();
			
			//close all connections
			try {
				so.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			throw new ServerIsDownException(prefix + " connection lost");
		}
		
		//parameter for the id-server
		writer.write("wantnumbers\n");
		writer.write(tablename + "\n");
		writer.write(wantnumbers + "\n");
		writer.flush();
		
		String list = null;
		
		try {
			
			list = reader.readLine();//get the result from the server
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
			//close connections
			writer.close();
			try {
				so.close();
				reader.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			throw new ServerIsDownException(prefix + " connection lost");
		}
		
		ArrayList<Integer> re = new ArrayList<>();
		
		for(String i : list.split("§")) {
			try {
				int tmp = Integer.parseInt(i);
				re.add(tmp);
			}catch(NumberFormatException ex) {
				ex.printStackTrace();
			}
		}
		
		
		try {
			
			//try to close all connections
			writer.close();
			reader.close();
			so.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new ServerIsDownException(prefix + " connection lost");
		}
		
		System.out.println(prefix + " success!");
		
		return re;
		
	}
	
	/*
	 * send the numbers you dont use to the id-server, which save the list
	 * 
	 * @param the tablename you want to save there
	 * @return the time it takes
	 */
	public static long addoldnumbers(String tablename) throws ServerIsDownException {
		
		long t1 = System.currentTimeMillis();
		
		System.out.println(prefix + " try to save ids from table: " + tablename);
		
		Socket so = null;
		try {
			so = new Socket(ip,port);
		} catch (IOException e) {
			e.printStackTrace();
			throw new ServerIsDownException(prefix + " connection lost");
		}
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(so.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			
			try {
				so.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			throw new ServerIsDownException(prefix + " connection lost");
		}
		
		//parameter the id-server need
		writer.write("addoldnumbers\n");
		writer.write(tablename + "\n");
		
		//write all numbers in one line
		if(numbers.containsKey(tablename)) {
			
			
			
			StringBuilder end = new StringBuilder();
				
			for(int i = 0; i < numbers.get(tablename).size(); i++) {
				
				end.append("§" + numbers.get(tablename).get(i));
			}
			
			String e = end.toString().replaceFirst("§", "");
			
			
			writer.write(e + "\n");
				
			
			
		}
		
		writer.flush();
		
		writer.close();
		try {
			so.close();
		} catch (IOException e) {
			e.printStackTrace();
			
			throw new ServerIsDownException(prefix + " connection lost");
			
		}
		
		System.out.println(prefix + " success!");
		
		long t2 = System.currentTimeMillis();
		
		return (t2-t1);
		
	}
	
	/*
	 * get a new number
	 * 
	 * @param tablename
	 * @return the number
	 * @see wantnumbers(String tablename)
	 */
	public static int getnewnumber(String tablename) throws ServerIsDownException {
		
		//if there isn't a available number in the arraylist get a new list
		if(!numbers.containsKey(tablename)) {
			ArrayList<Integer> tmp = wantnumbers(tablename);
			numbers.put(tablename, tmp);
		}else {
			if(numbers.get(tablename).isEmpty()) {
				ArrayList<Integer> tmp = wantnumbers(tablename);
				numbers.put(tablename, tmp);
			}	
		}
		
		
		int end = numbers.get(tablename).get(0);
		numbers.get(tablename).remove(0);
		
		return end;
		
	}
	
	
	/*
	 * setter and getter for static fields port ip and wantnumbers
	 */
	public static int getPort() {
		return port;
	}
	public static String getIp() {
		return ip;
	}
	public static void setIp(String ip) {
		EssentialsXXXNBTTagManager.ip = ip;
	}
	public static void setPort(int port) {
		EssentialsXXXNBTTagManager.port = port;
	}
	public static int getWantnumbers() {
		return wantnumbers;
	}
	public static void setWantnumbers(int wantnumbers) {
		EssentialsXXXNBTTagManager.wantnumbers = wantnumbers;
	}
	
	
	//veraltet!
	/*
	public static void setup(MySql mysql, String ip, int port) throws ServerIsDownException {
		
		long t1 = System.currentTimeMillis();
		
		EssentialsXXXNBTTagManager.ip = ip;
		EssentialsXXXNBTTagManager.port = port;
		
		System.out.println("[essentialsxxx-phoenixstack] start EssentialsXXXNBTTagManager.setup()");
		
		EssentialsXXXNBTTagManager.createtableNBTTagCompound("NBTTagCompound",mysql);
		EssentialsXXXNBTTagManager.createtableNBTTagList("NBTTagList", mysql);
		
		//createtableLongArray("LongArray",mysql);
		//EssentialsXXXNBTTagManager.createtableByteArray("ByteArray", mysql);
		//EssentialsXXXNBTTagManager.createtableIntArray("IntArray", mysql);
		
		EssentialsXXXNBTTagManager.createtableItemStack("test1", mysql);
		
		System.out.println("[essentialsxxx-phoenixstack] register tables");
		
		EssentialsXXXNBTTagManager.registerTable("NBTTagCompound", mysql);
		EssentialsXXXNBTTagManager.registerTable("NBTTagList", mysql);
		
		//EssentialsXXXNBTTagManager.registerTable("ByteArray", mysql);
		//EssentialsXXXNBTTagManager.registerTable("IntArray", mysql);
		
		EssentialsXXXNBTTagManager.registerTable("test1", mysql);
		
		System.out.println("[essentialsxxx-phoenixstack] finish the register process");
		
		
		long t3 = System.currentTimeMillis();
		
		System.out.println("[essentialsxxx-phoenixstack] finish EssentialsXXXNBTTagManager.setup() Time: (" + (t3-t1) + ") milliseconds");
		
	}
	*/
	
	/*
	public static Map<String, Integer> getNumbers(String tablename, MySql mysql) {
		
		Map<String,Integer> end = new LinkedHashMap<>();
		
		String command = "SELECT `tablename`,`number` FROM `" + tablename + "`;";
		//System.out.println("getNumbers => " + command);
		
		ResultSet result = mysql.getResult(command);
		try {
			while(result.next()) {
				String t = result.getString(1);
				int number = result.getInt(2);
				end.put(t, number);
			}
		} catch (NullPointerException | SQLException e) {
			e.printStackTrace();
		}
		
		return end;
		
	}
	
	
	public static int getPair(String tablename, String key, MySql mysql) {
		
		ResultSet result = mysql.getResult("SELECT number FROM `" + tablename + "` WHERE tablename='" + key + "'");
		try {
			if(result.next()) {
				int r = result.getInt(1);
				return r;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return -1;
		
	}
	
	public static void setPair(String tablename, String key, int number, MySql mysql) {
		
		int p = getPair(tablename, key, mysql);
		
		if(p != -1) {
			
			mysql.command("UPDATE `" + tablename + "` "
					+ "SET number=" + number
					+ " WHERE tablename='" + key + "'"
					+ ";");
			
			
		}else {
			
			int id = 0;
			
			ResultSet re = mysql.getResult("SELECT id FROM `" + tablename + "` ORDER BY `id`;");
			try {
				if(re.last()) {
					id = re.getInt(1);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			mysql.command("INSERT INTO `" + tablename + "` "
					+ "(id,tablename,number) VALUES ("
					+ "" + id
					+ ",'" + key + "'"
					+ "," + number
					+ ");");
		}
		
	}
	
		public static void createtableByteArray(String tablename, MySql mysql) {
		
		String command = "CREATE TABLE IF NOT EXISTS `" + tablename + "` "
				+ "("
				+ "id INT Primary Key"
				+ ",bytelist TEXT"
				+ ");";
		mysql.command(command);
		
		
	}
	
	public static void createtableIntArray(String tablename, MySql mysql) {
		
		String command = "CREATE TABLE IF NOT EXISTS `" + tablename + "` "
				+ "("
				+ "id INT Primary Key"
				+ ",intlist TEXT"
				+ ");";
		mysql.command(command);
		
	}
	
	 */
	
	/*
	public static void createtableLongArray(String tablename, MySql mysql) {
		
		String command = "CREATE TABLE IF NOT EXISTS " + tablename + " "
				+ "("
				+ "id INT Primary Key"
				+ ",longlist TEXT"
				+ ""
				+ ""
				+ ");";
		mysql.command(command);
		
		registerTable(tablename, mysql);
	}
	*/
	
	/*
	public static Map<String,Integer> loadNumbers(String tablename, MySql mysql) {
		
		Map<String, Integer> numbers = new LinkedHashMap<>();
		
		ResultSet result = mysql.getResult("SELECT tablename,number FROM `" + tablename + "`");
		
		try {
			while(result.next()) {
				String table = result.getString(1);
				int number = result.getInt(2);
				numbers.put(table, number);
			}
		} catch (NullPointerException | SQLException e) {
			e.printStackTrace();
		}
		
		
		return numbers;
		
		
	}

	public static void saveNumbers(String tablename, MySql mysql, Map<String,Integer> numbers) {
		
		for(String key : numbers.keySet()) {
			int number = numbers.get(key);
			
			int n = -1;
			ResultSet re = mysql.getResult("SELECT id FROM `" + tablename + "`;");
			try {
				if(re.last()) {
					n = re.getInt(1);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			ResultSet result = mysql.getResult("SELECT * FROM " + tablename + " WHERE tablename='" + key + "';");
			try {
				if(result.next()) {
					
					mysql.command("UPDATE " + tablename + " "
							+ "SET number=" + number
							+ " WHERE tablename=" + key
							+ ";");
					
				}else {
					n++;
					mysql.command("INSERT INTO " + tablename + " "
							+ "("
							+ "id,tablename,number"
							+ ") VALUES ("
							+ "" + n
							+ "," + key
							+ "," + number
							+ ");");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			
		}
		
	}
	*/
}
