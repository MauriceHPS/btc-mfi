/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zaytoven.db;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;
import org.flywaydb.core.api.migration.MigrationChecksumProvider;
import org.flywaydb.core.api.migration.MigrationInfoProvider;
import org.flywaydb.core.api.migration.jdbc.JdbcMigration;
import zaytoven.controller.ConnectionDB;

/**
 *
 * @author Helmut
 */
public class DBMigrator implements JdbcMigration, MigrationChecksumProvider, MigrationInfoProvider{
    Flyway fly;
    Connection connect;
    public DBMigrator(){
        connect=ConnectionDB.getInstance().getCon();
    }
    
    public void getDataSource(){
        Properties credentials = new Properties();
        
        
        String connfile=System.getProperty("user.dir") + System.getProperty("file.separator") + "SYSPROP.properties";
        try {
            credentials.load(new FileInputStream(connfile));
            
            /*props1.setProperty("user", credentials.getProperty("username"));
            props.setProperty("password", credentials.getProperty("password"));*/
            
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DBMigrator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DBMigrator.class.getName()).log(Level.SEVERE, null, ex);
        }
        String url=(new StringBuilder()).append("jdbc:postgresql://").append(credentials.getProperty("host")).append(":").append(credentials.getProperty("port")).append("/").append(credentials.getProperty("database")).toString();
        
        fly=new Flyway();
        fly.setDataSource(url, credentials.getProperty("username"), credentials.getProperty("password"));
        
        
        
//        fly.migrate();
    }

    @Override
    public void migrate(Connection connection) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer getChecksum() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MigrationVersion getVersion() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getDescription() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isUndo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}