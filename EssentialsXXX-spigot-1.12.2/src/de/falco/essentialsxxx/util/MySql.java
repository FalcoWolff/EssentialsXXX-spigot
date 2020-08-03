package de.falco.essentialsxxx.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class for an abstract database connection
 * 
 * @author Falco Wolf
 * @version 1.0
 *
 */
public class MySql {
	
  private Connection con = null;
  private String path;
  private String user;
  private String pw;
  
  //"jdbc:mysql://"
  //?autoReconnect=true
  //"com.mysql.jdbc.Driver"
  //System.out.println(prefix + " couldnt find mysql-classes"); disablejoin();
  public MySql(String driver,String path, String user, String pw) throws ClassNotFoundException {
	Class.forName( driver );
	this.path = path;
    this.user = user;
    this.pw = pw;
  }
  
  
  /**
   * connect to database
   * 
   * @throws SQLException when there is an error
   */
  public void connect() throws SQLException {
    if (!isconnect()) {
    	this.con = DriverManager.getConnection(path, this.user, this.pw);	
    }
     
  }
  
  /**
   * close the connections WHEN the connections is open
   * 
   * @throws SQLException
   */
  public void disconnect() throws SQLException {
    if (isconnect()) {
        this.con.close();
    }
  }
  
  /**
   * check the connections state
   * 
   * @return false if the connections is null or close
   */
  public boolean isconnect(){
    if (this.con == null) {
    	return false;
    }
    try {
		if(this.con.isClosed()) {
			return false;
		}
	} catch (SQLException e) {
		return false;
	}
    
    
    return true;
  }
  
  /**
   * execute mysql statement
   * 
   * @param sql
   */
  public void command(String sql) {
    try {
      PreparedStatement state = this.con.prepareStatement(sql);
      state.execute();
    } catch (SQLException e) {
      e.printStackTrace();
    } 
  }
  
  /**
   * execute mysql statement
   * 
   * @param sql
   * @return ResultSet as result from the mysql statement
   */
  public ResultSet getResult(String sql) {
    try {
      PreparedStatement state = this.con.prepareStatement(sql);
      return state.executeQuery();
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  /*
   * setter
   */
  public void setCon(Connection con) {
	this.con = con;
}
  public void setPath(String path) {
	this.path = path;
}
  public void setPw(String pw) {
	this.pw = pw;
}
  public void setUser(String user) {
	this.user = user;
}
  
  /*
   * getter
   */
  public Connection getCon() {
	return con;
}
  public String getPath() {
	return path;
}
  public String getPw() {
	return pw;
}
  public String getUser() {
	return user;
}
  
}
