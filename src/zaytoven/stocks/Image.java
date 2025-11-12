/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zaytoven.stocks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import zaytoven.controller.ConnectionDB;

/**
 *
 * @author Helmut
 */
public class Image {
    public static void main(String[] args) {
      Connection con=ConnectionDB.getInstance().getCon();
      
      File img = new File("new.png");
        FileInputStream fin;
        try {
            fin = new FileInputStream(img);
            
            PreparedStatement pst = con.prepareStatement("INSERT INTO pb_header(company_logo) VALUES(?)");
            pst.setBinaryStream(1, fin, (int) img.length());
            pst.executeUpdate();
            if (pst!=null) {
                System.err.println("Its in the system");
            }
        } catch (FileNotFoundException | SQLException ex) {
            Logger.getLogger(Image.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
}
