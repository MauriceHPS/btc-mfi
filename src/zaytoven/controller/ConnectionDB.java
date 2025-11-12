/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zaytoven.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author maurice
 */
public class ConnectionDB {
  private  Connection con;
  private static ConnectionDB instance;
  private String url;

    public ConnectionDB() {
        dbConnection();
    }

    public static ConnectionDB getInstance() {
        if(instance==null){
            instance=new ConnectionDB();
        }
        return instance;
    }

    public static void setInstance(ConnectionDB instance) {
        ConnectionDB.instance = instance;
    }
   
    public Connection getCon() {
        return con;
    }

    public void setCon(Connection con) {
        this.con = con;
    }
    
private void dbConnection(){
 
      try {
//          Class.forName("org.postgresql.Driver");
                Class.forName("org.postgresql.Driver");
          Properties credentials = new Properties();
          String connfile=System.getProperty("user.dir") + System.getProperty("file.separator") + "SYSPROP.properties";
          credentials.load(new FileInputStream(connfile));
          
          Properties props = new Properties();
          props.setProperty("user", credentials.getProperty("username"));
          props.setProperty("password", credentials.getProperty("password"));
          
          String url=(new StringBuilder()).append("jdbc:postgresql://").append(credentials.getProperty("host")).append(":").append(credentials.getProperty("port")).append("/").append(credentials.getProperty("database")).toString();
          System.err.println(url);
          setUrl(url);
          con = DriverManager.getConnection(url, props);

          if(con!=null){
              System.out.println("CONNECTION SUCCESSFUL");
          }
          else{
              System.out.println("UNABLE TO CONNECT");
              JOptionPane.showMessageDialog(null, "Check the parameters set in SYSPROP.properties file", "Unable to Establish a Database Connection", JOptionPane.INFORMATION_MESSAGE);
              System.exit(0);
          }
      } catch (ClassNotFoundException | SQLException | IOException ex) {
          Logger.getLogger(ConnectionDB.class.getName()).log(Level.SEVERE, null, ex);
          JOptionPane.showMessageDialog(null, ex.getMessage());
          System.exit(0);
      }
         
    }
    
    public void setUrl(String url){
        this.url=url;
    }
    public String getUrl(){
        return url;
    }
}