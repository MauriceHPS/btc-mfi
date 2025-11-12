/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zaytoven.sysmgt;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyPair;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.nicholaswilliams.java.licensing.encryption.*;
import net.nicholaswilliams.java.licensing.exception.*;
//import net.nicholaswilliams.java.licensing.encryption.RSAKeyPairGeneratorInterface.GeneratedClassDescriptor;

/**
 *
 * @author Helmut
 */
public class KeyGenerator {
    public static void main (String [] args){
         RSAKeyPairGenerator generator=new RSAKeyPairGenerator();
         KeyPair keypair=generator.generateKeyPair();
         
        try {
            
            generator.saveKeyPairToFiles(keypair, "D:\\Development\\Tests\\System\\private.key", "D:\\Development\\Tests\\System\\public.key", new Info().getPrivatePassword().toCharArray(), new Info().getPublicPassword().toCharArray());
            //System.out.println(generator.saveKeyPairToFiles(keypair, "private.key", "public.key", "sestoelemento".toCharArray(), "hennesy".toCharArray()));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(KeyGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | AlgorithmNotSupportedException | InappropriateKeyException | InappropriateKeySpecificationException ex) {
            Logger.getLogger(KeyGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }
}


