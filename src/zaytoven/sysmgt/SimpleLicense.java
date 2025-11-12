/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zaytoven.sysmgt;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Helmut
 */
public class SimpleLicense {
    public static void main(String[] args) {
        ExpDate d=new ExpDate(new javax.swing.JFrame(), true);
        d.setSize(500, 300);
        d.setVisible(true);
    }
            static void makeLic(Date date){
        try {
            java.io.File licFile = new java.io.File(new java.io.File(System.getProperty("user.dir")),"private.key");
            
            licFile.delete();
            
            licFile.createNewFile();
            
            java.io.FileOutputStream licOutputStream = new java.io.FileOutputStream(licFile);
            
            java.io.ObjectOutputStream licObjectputStream = new java.io.ObjectOutputStream(licOutputStream);
            
            
            
            licObjectputStream.writeObject(date);
        } catch (IOException ex) {
            Logger.getLogger(SimpleLicense.class.getName()).log(Level.SEVERE, null, ex);
        }
            }
}
