package de.falco.essentialsxxx.phoenixstack.metatypes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import de.falco.essentialsxxx.phoenixstack.PhoenixMeta;
import de.falco.essentialsxxx.util.MySql;
import net.minecraft.server.v1_12_R1.NBTTagByte;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagInt;
import net.minecraft.server.v1_12_R1.NBTTagList;
import net.minecraft.server.v1_12_R1.NBTTagString;

public class Book extends PhoenixMeta{
	
	private byte resolved = -1;
	private int generation = -1;
	private String author = null;
	private String title = null;
	private ArrayList<String> pages = new ArrayList<>();
	
	public Book(NBTTagCompound tag) {
		serialize(tag);
	}
	
	public Book() {
		
	}
	
	public void serialize(NBTTagCompound tag) {
		
		super.serialize(tag);
		
		if(tag.hasKey("resolved")) {
			resolved = tag.getByte("resolved");
		}
		
		if(tag.hasKey("generation")) {
			generation = tag.getInt("generation");
		}
		
		if(tag.hasKey("author")) {
			author = tag.getString("author");
		}
		
		if(tag.hasKey("title")) {
			title = tag.getString("title");
		}
		
		if(tag.hasKey("pages")) {
			NBTTagList pages = (NBTTagList) tag.get("pages");
			
			for(int x = 0; x < pages.size(); x++) {
				String i = pages.getString(x);
				this.pages.add(i);
			}
			
		}
		
	}
	
	public NBTTagCompound deserialize() {
		
		NBTTagCompound tag = super.deserialize();
		
		if(resolved != -1) {
			tag.set("resolved", new NBTTagByte(resolved));
		}
		
		if(generation != -1) {
			tag.set("generation", new NBTTagInt(generation));
		}
		
		if(author != null) {
			tag.set("author", new NBTTagString(author));
		}
		
		if(title != null) {
			tag.set("title", new NBTTagString(title));
		}
		
		if(pages.isEmpty() == false) {
			
			NBTTagList list = new NBTTagList();
			
			for(String i : pages) {
				list.add(new NBTTagString(i));
			}
			
			tag.set("pages", list);
		}
		
		return tag;
		
	}

	public static void createtable(MySql mysql, String tablename) {
		String command = "CREATE TABLE IF NOT EXISTS " + tablename + " "
				+ "("
				+ "id int Primary Key"
				+ ",resolved TINYINT"
				+ ",generation TINYINT"
				+ ",author TEXT"
				+ ",title TEXT"
				+ ",pages TEXT"
				+ ");";
		
		mysql.command(command);
	}
	
	public static void save(Book book, int id, MySql mysql, String tablename) {
		
		byte resolved = book.resolved;
		int generation = book.generation;
		String author = null;
		String title = null;
		String pages = null;
		
		if(book.author != null) {
			author = "'" + book.author + "'";
		}
		
		if(book.title != null) {
			title = "'" + book.title + "'";
		}
		
		if(book.pages.isEmpty() == false) {
			StringBuilder builder = new StringBuilder();
			
			for(String i : book.pages) {
				builder.append("<break>" + i);
			}
			
			pages = "'" + builder.toString().replaceFirst("<break>", "") + "'";
		}
		
		
		String command = "INSERT INTO " + tablename + " "
				+ "("
				+ "id,resolved,generation,author,title,pages"
				+ ") VALUES ("
				+ "" + id
				+ "," + resolved
				+ "," + generation
				+ "," + author
				+ "," + title
				+ "," + pages
				+ ");"
				+ "";
		mysql.command(command);
	}
	
	public static Book load(Book book, int id, MySql mysql, String tablename) {
		
		ResultSet result = mysql.getResult("SELECT * FROM " + tablename + " WHERE id=" + id);
		
		try {
			if(result.next()) {
				
				book.resolved = result.getByte(2);
				book.generation = result.getInt(3);
				
				book.author = result.getString(4);
				book.title = result.getString(5);
				
				String pages = result.getString(6);
				
				if(pages != null) {
					
					for(String i : pages.split("<break>")) {
						book.pages.add(i);
					}
				}
				
				return book;
				
			}else {
				return book;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return book;
		}
		
	}
	
	
	
}
