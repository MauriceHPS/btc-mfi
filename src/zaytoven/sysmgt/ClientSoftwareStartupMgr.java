/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zaytoven.sysmgt;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.nicholaswilliams.java.licensing.LicenseManagerProperties;
import net.nicholaswilliams.java.licensing.encryption.PublicKeyDataProvider;

/**
 *
 * @author Helmut
 */
public class ClientSoftwareStartupMgr {
    public void startup(){
        try {
            LicenseManagerProperties.setPublicKeyDataProvider((PublicKeyDataProvider) new FileInputStream("D:\\Development\\Tests\\System\\public.key"));
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ClientSoftwareStartupMgr.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
