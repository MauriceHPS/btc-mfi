/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zaytoven.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
/**
 *
 * @author MAURICE
 */
public class ImageInsert {
    Connection connect;
    public ImageInsert(){
        connect=ConnectionDB.getInstance().getCon();
        
        JFileChooser fileChooser=new JFileChooser();
        fileChooser.showOpenDialog(null);
        File file=fileChooser.getSelectedFile();
        
        try {
            connect.setAutoCommit(false);
            FileInputStream input=new FileInputStream(file);
//            'SMART APPLICATIONS LIMITED', '2016-09-08', lo_import('Pictures/smart_2.png', 68593), true
            try (PreparedStatement pr = connect.prepareStatement("update pb_header set company_logo=?")) {
                //            'SMART APPLICATIONS LIMITED', '2016-09-08', lo_import('Pictures/smart_2.png', 68593), true
                pr.setBinaryStream(1, input, file.length());
                pr.executeUpdate();
                System.err.println(pr.toString());
                
                if(pr!=null){
                    JOptionPane.showMessageDialog(null, "Update is Successful");
                }
                connect.commit();
                connect.setAutoCommit(true);
//                input.close();
            }
        } catch (FileNotFoundException | SQLException ex) {
            try {
                connect.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(ImageInsert.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(ImageInsert.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
        public static void main(String[] args) {
        ImageInsert imageInsert = new ImageInsert();
    }
                
}
