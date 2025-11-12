/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zaytoven.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; 

/**
 *
 * @author Kelsas
 */
public class SQLHelper {
    
    public static ResultSet getResultset(Connection connectDB, String SQLQuery) throws SQLException {
            Statement st = connectDB.createStatement();
            return st.executeQuery(SQLQuery);
    } 
    public static void rollBackTransaction(Connection connectDB) throws SQLException{
        if(connectDB!=null){
            connectDB.rollback();
        } 
    }
    public static void endXtCommit(){
        
    }
}
