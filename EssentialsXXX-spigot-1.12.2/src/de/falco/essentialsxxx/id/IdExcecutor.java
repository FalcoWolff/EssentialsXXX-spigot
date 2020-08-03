package de.falco.essentialsxxx.id;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.InvalidParameterException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import de.falco.essentialsxxx.EssentialsXXX;


/**
 * 
 * interface useable to get player data, saved in the db or in the mojang db. 
 * <br>
 * <br>
 * 
 * getidbyuuid(UUID uuid):int <br>
 * getuuidbyid(int id):UUID <br>
 * getidsbyip(String ip):ArrayList String <br>
 * getipsbyid(int id):ArrayList String <br>
 * getUUIDbyUsername(String username):UUID <br>
 * getUserProfilebyUsername(String username):UserProfile <br>
 * getUserProfilebyuuid(UUID uuid):UserProfile <br>
 * 
 * @author Falco Wolf
 * @version 1.0
 * @see de.falco.essentialsxxx.id.NotregisteredException
 * @see de.falco.essentialsxxx.id.UserProfile
 * @see de.falco.essentialsxxx.id.ConfigFile
 *
 */
public interface IdExcecutor {
	
	
	/**
	 * get user id (saved in db)
	 * 
	 * @param uuid
	 * @return
	 * @throws NotregisteredException when there is an sql exception
	 */
	default int getidbyuuid(UUID uuid) throws NotregisteredException {
		
		ResultSet result = EssentialsXXX.getEssentialsXXXmain().getIdmysql().getResult("SELECT id FROM id WHERE uuid='" + uuid.toString() + "'");
		try {
			result.first();
			return result.getInt(1);
		} catch (SQLException e) {
			//e.printStackTrace();
			throw new NotregisteredException(EssentialsXXX.getEssentialsXXXmain().getPrefix() + " uuid " + uuid + " wasn't found");
		}
		
	}
	
	/**
	 * get user uuid (saved in db)
	 * 
	 * @param id
	 * @return
	 * @throws NotregisteredException when there is an sql exception
	 */
	default UUID getuuidbyid(int id) throws NotregisteredException {
		
		ResultSet result = EssentialsXXX.getEssentialsXXXmain().getIdmysql().getResult("SELECT uuid FROM id WHERE id='" + id + "'");
		
		try {
			result.first();
			return UUID.fromString(result.getString(1));
		} catch (SQLException e) {
			//e.printStackTrace();
			throw new NotregisteredException(EssentialsXXX.getEssentialsXXXmain().getPrefix() + " id " + id + " wasn't found");
		}
		
	}
	
	/**
	 * get the ids of the user have joined with the ip (saved in db)
	 * 
	 * @param ip
	 * @return
	 * @throws NotregisteredException when there is an sql exception
	 */
	default ArrayList<String> getidsbyip(String ip) throws NotregisteredException {
		
		ResultSet result = EssentialsXXX.getEssentialsXXXmain().getIdmysql().getResult("SELECT id FROM id WHERE ips LIKE '%" + ip + "%'");
		
		ArrayList<String> ids = new ArrayList<>();
		
		try {
			while(result.next()) {
				ids.add(result.getString(1));
			}
		} catch (SQLException e) {
			//e.printStackTrace();
			throw new NotregisteredException(EssentialsXXX.getEssentialsXXXmain().getPrefix() + " ip " + ip + " wasn't found");
		}
		
		
		if(ids.isEmpty()) {
			throw new NotregisteredException(EssentialsXXX.getEssentialsXXXmain().getPrefix() + " ip " + ip + " wasn't found");
			//return null;
		}
		
		
		return ids;
		
	}
	
	/**
	 * get all ips from a user with the id
	 * 
	 * @param id
	 * @return
	 * @throws NotregisteredException when there is a sqlexception
	 */
	default ArrayList<String> getipsbyid(int id) throws NotregisteredException {
		
		ResultSet result = EssentialsXXX.getEssentialsXXXmain().getIdmysql().getResult("SELECT ips FROM id WHERE id=" + id);
		
		try {
			result.next();
			
			ArrayList<String> ips = new ArrayList<>();
			
			String i = result.getString(1);
			for(String index : i.split(",")) {
				ips.add(index);
			}
			
			return ips;
			
		} catch (SQLException e) {
			//e.printStackTrace();
			throw new NotregisteredException(EssentialsXXX.getEssentialsXXXmain().getPrefix() + " id " + id + " wasn't found");
		}
		
	}
	
	/**
	 * get the UUID from a ''username'' username doenst mean the real username. It means the uuid without '-'
	 * 
	 * @param username
	 * @return
	 * @throws NotregisteredException when the uuid is null
	 * @throws InvalidParameterException when username is null
	 * @throws IndexOutOfBoundsException when the length of username isn't 32 (the normal uuid length without '-')
	 */
	default UUID getUUIDbyUsername(String username) throws NotregisteredException {
		
		if(username == null) {
			throw new InvalidParameterException("username couldnt be null");
		}
		
		String[] split = username.split("");
		
		if(split.length != 32) {
			throw new IndexOutOfBoundsException("username has length " + split.length + " should be 32");
		}
		
		StringBuilder builder = new StringBuilder();
		
		for(int x = 0; x < 8; x++) {
			builder.append(split[x]);
		}
		
		builder.append("-");
		
		for(int x = 8; x < 12; x++) {
			builder.append(split[x]);
		}
		
		builder.append("-");
		
		for(int x = 12; x < 16; x++) {
			builder.append(split[x]);
		}
		
		builder.append("-");
		
		for(int x = 16; x < 20; x++) {
			builder.append(split[x]);
		}
		
		builder.append("-");
		
		for(int x = 20; x < 32; x++) {
			builder.append(split[x]);
		}
		
		
		UUID uuid = UUID.fromString(builder.toString());
		
		if(uuid == null) {
			throw new NotregisteredException(EssentialsXXX.getEssentialsXXXmain().getPrefix() + " uuid " + builder.toString() + " wasn't found");
		}
		
		return uuid;
		
	}
	
	/**
	 * get the UserProfile with the username
	 * 
	 * @param username
	 * @return object containing all informations
	 * @throws NotregisteredException when the response content isnt 'OK' or when the methods getUUIDbyUsername | getUserProfilebyuuid throws the NotregisteredException
	 */
	default UserProfile getUserProfilebyUsername(String username) throws NotregisteredException {
		
		//https://api.mojang.com/users/profiles/minecraft/<username>?at=<timestamp>
		String url = "https://api.mojang.com/users/profiles/minecraft/" + username + "?at=" + System.currentTimeMillis();
		//System.out.println("send get request " + url);
		
		URL obj;
		
		try {
			obj = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
		
		HttpURLConnection con;
		
		try {
			con = (HttpURLConnection) obj.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		try {
			con.setRequestMethod("GET");
		} catch (ProtocolException e) {
			e.printStackTrace();
			return null;
		}
		
		String responsemes = null;
		try {
			responsemes = con.getResponseMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		if(!responsemes.equals("OK")) {
			//throw new CouldntfindUUIDException(EssentialsXXX.getEssentialsXXXmain().getName() + " no user with uuid " + uuid);
			System.out.println(EssentialsXXX.getEssentialsXXXmain().getPrefix() + " response status is " + responsemes);
			throw new NotregisteredException(EssentialsXXX.getEssentialsXXXmain().getPrefix() + " couldnt find user with playername " + username);
		}
		
		//System.out.println("Sending 'GET' request to URL : " + url);
		//System.out.println("Response Code : " + responseCode);
		
		BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		}
		
		StringBuffer response = new StringBuffer();
		String i;
		try {
			while((i = in.readLine()) != null) {
				response.append(i);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//System.out.println(response.toString());
		
		JSONParser parser = new JSONParser();
		Object ob;
		try {
			ob = parser.parse(response.toString());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		
		JSONObject object = (JSONObject) ob;
		
		if(!object.containsKey("id")) {
			
			System.out.println(EssentialsXXX.getEssentialsXXXmain() + " no key id");
			return null;
			
		}
		
		String uuid = (String) object.get("id");
		
		//System.out.println(uuid);
		
		UUID u = getUUIDbyUsername(uuid);
		
		UserProfile re = getUserProfilebyuuid(u);		
		
		return re;
		
	}
	
	/**
	 * get the UserProfile with the uuid
	 * 
	 * @param uuid
	 * @return object containing all informations
	 * @throws NotregisteredException when the response content isnt 'OK'
	 */
	default UserProfile getUserProfilebyuuid(UUID uuid) throws NotregisteredException {
		
		
		String url = "https://api.mojang.com/user/profiles/" + uuid.toString().replaceAll("-", "") + "/names";
		//System.out.println("send get request " + url);
		URL obj;
		
		try {
			obj = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
		
		HttpURLConnection con;
		
		try {
			con = (HttpURLConnection) obj.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		try {
			con.setRequestMethod("GET");
		} catch (ProtocolException e) {
			e.printStackTrace();
			return null;
		}
		
		String responsemes = null;
		try {
			responsemes = con.getResponseMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
		if(!responsemes.equals("OK")) {
			//throw new CouldntfindUUIDException(EssentialsXXX.getEssentialsXXXmain().getName() + " no user with uuid " + uuid);
			System.out.println(EssentialsXXX.getEssentialsXXXmain().getPrefix() + " response status is " + responsemes);
			throw new NotregisteredException(EssentialsXXX.getEssentialsXXXmain().getPrefix() + " couldnt find userprofile with uuid " + uuid.toString());
		}
		
		//System.out.println("Sending 'GET' request to URL : " + url);
		//System.out.println("Response Code : " + responseCode);
		
		BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		}
		
		StringBuffer response = new StringBuffer();
		String i;
		try {
			while((i = in.readLine()) != null) {
				response.append(i);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//System.out.println(response.toString());
		
		JSONParser parser = new JSONParser();
		Object ob;
		try {
			ob = parser.parse(response.toString());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		
		
		
		JSONArray array = (JSONArray) ob;
		
		//System.out.println(array);
		
		String firstname = null;
		Map<Long,String> names = new LinkedHashMap<>();
		
		for(int x = 0; x < array.size(); x++) {
			
			JSONObject myResponse = (JSONObject) array.get(x);
			
			//System.out.println(myResponse);
			
			if(!myResponse.containsKey("changedToAt")) {
				
				//System.out.println("no");
				
				firstname = (String) myResponse.get("name");
				
			}else {
				
				//System.out.println("yes");
				
				long changedtoat = (long) myResponse.get("changedToAt");
				
				String name = (String) myResponse.get("name");
				
				names.put(changedtoat,name);
				
			}
			
		}
		
		UserProfile u = new UserProfile(uuid, firstname,names);
		
		return u;
		
	}
	
	
	default boolean isDemoPlayer(String username) throws NotregisteredException {
		
		//https://api.mojang.com/users/profiles/minecraft/<username>?at=<timestamp>
		String url = "https://api.mojang.com/users/profiles/minecraft/" + username + "?at=" + System.currentTimeMillis();
		//System.out.println("send get request " + url);
		
		URL obj;
		
		try {
			obj = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		}
		
		HttpURLConnection con;
		
		try {
			con = (HttpURLConnection) obj.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		try {
			con.setRequestMethod("GET");
		} catch (ProtocolException e) {
			e.printStackTrace();
			return false;
		}
		
		String responsemes = null;
		try {
			responsemes = con.getResponseMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		if(!responsemes.equals("OK")) {
			//throw new CouldntfindUUIDException(EssentialsXXX.getEssentialsXXXmain().getName() + " no user with uuid " + uuid);
			System.out.println(EssentialsXXX.getEssentialsXXXmain().getPrefix() + " response status is " + responsemes);
			throw new NotregisteredException(EssentialsXXX.getEssentialsXXXmain().getPrefix() + " couldnt find user with playername " + username);
		}
		
		//System.out.println("Sending 'GET' request to URL : " + url);
		//System.out.println("Response Code : " + responseCode);
		
		BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}
		
		StringBuffer response = new StringBuffer();
		String i;
		try {
			while((i = in.readLine()) != null) {
				response.append(i);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//System.out.println(response.toString());
		
		JSONParser parser = new JSONParser();
		Object ob;
		try {
			ob = parser.parse(response.toString());
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		
		JSONObject object = (JSONObject) ob;
		
		if(object.containsKey("demo")) {
			return true;
		}
		
		return false;
		
	}

	default boolean isLegacyPlayer(String username) throws NotregisteredException {
		
		//https://api.mojang.com/users/profiles/minecraft/<username>?at=<timestamp>
		String url = "https://api.mojang.com/users/profiles/minecraft/" + username + "?at=" + System.currentTimeMillis();
		//System.out.println("send get request " + url);
		
		URL obj;
		
		try {
			obj = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		}
		
		HttpURLConnection con;
		
		try {
			con = (HttpURLConnection) obj.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		try {
			con.setRequestMethod("GET");
		} catch (ProtocolException e) {
			e.printStackTrace();
			return false;
		}
		
		String responsemes = null;
		try {
			responsemes = con.getResponseMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		if(!responsemes.equals("OK")) {
			//throw new CouldntfindUUIDException(EssentialsXXX.getEssentialsXXXmain().getName() + " no user with uuid " + uuid);
			System.out.println(EssentialsXXX.getEssentialsXXXmain().getPrefix() + " response status is " + responsemes);
			throw new NotregisteredException(EssentialsXXX.getEssentialsXXXmain().getPrefix() + " couldnt find user with playername " + username);
		}
		
		//System.out.println("Sending 'GET' request to URL : " + url);
		//System.out.println("Response Code : " + responseCode);
		
		BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}
		
		StringBuffer response = new StringBuffer();
		String i;
		try {
			while((i = in.readLine()) != null) {
				response.append(i);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//System.out.println(response.toString());
		
		JSONParser parser = new JSONParser();
		Object ob;
		try {
			ob = parser.parse(response.toString());
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
		
		JSONObject object = (JSONObject) ob;
		
		if(object.containsKey("legacy")) {
			return true;
		}
		
		return false;
		
	}
	
	@Deprecated
	default String getnamebyid(int id) {
		
		ResultSet result = EssentialsXXX.getEssentialsXXXmain().getIdmysql().getResult("SELECT name FROM id WHERE id= '" + id + "'");
		
		try {
			result.first();
			return result.getString(1);
		} catch (SQLException e) {
			//e.printStackTrace();
			//throw new NotregisteredException("" + EssentialsXXX.getEssentialsXXXmain().getPrefix() + " id " + id + " wasnt found");
			return null;
		}
		
	}
	
	@Deprecated
	default int getidbyname(String name) {
		
		ResultSet result = EssentialsXXX.getEssentialsXXXmain().getIdmysql().getResult("SELECT id FROM id WHERE name='" + name + "'");
		try {
			result.first();
			return result.getInt(1);
		} catch (SQLException e) {
			//e.printStackTrace();
			//throw new NotregisteredException(EssentialsXXX.getEssentialsXXXmain().getPrefix() + " name " + name + " wasn't found");
			return -1;
		}
		
	}
	
	
	

}
