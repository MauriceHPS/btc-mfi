/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zaytoven.mpesa;

// Classes and Libraries to import
import java.security.Security;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.crypto.Cipher;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
/**
 *
 * @author ANNE
 */

public class PasswordEncrypter {
    public String encryptedPassword;
    
    public PasswordEncrypter(){
        encryptedPassword=null;
    }
    
// Method to encrypt the initiator credentials
    public void setEncryptInitiatorPassword(String securityCertificate, String password) {
        try {
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
            
            //write the ecnrypted password into a byte array
            byte[] input = password.getBytes();

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
            
            //securityCertificate is the path of the downloaded certificate file
            FileInputStream fin = new FileInputStream(new File(securityCertificate));
            
            //Create a key certificate of type X509
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            
            //creates a X509 key certificate file
            X509Certificate certificate = (X509Certificate) cf.generateCertificate(fin);
            
            //Generate a public certificate key
            PublicKey pk = certificate.getPublicKey();
            cipher.init(Cipher.ENCRYPT_MODE, pk);
            
            //encrypt the array with the Mpesa's public key certificate
            byte[] cipherText = cipher.doFinal(input);

            // Convert the resulting encrypted byte array into a string using base64 encoding
            encryptedPassword = Base64.encode(cipherText);

        } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | FileNotFoundException | CertificateException | 
                InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(PasswordEncrypter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getEncryptedPassword(){
        return encryptedPassword;
    }
    //end of class
}
