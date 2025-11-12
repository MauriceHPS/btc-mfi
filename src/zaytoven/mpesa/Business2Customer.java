/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zaytoven.mpesa;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 *
 * @author ANNE
 */
public class Business2Customer {
    
    public Business2Customer(){
        
    }
    
    /*
    
    QueueTimeOutURL - The path that stores information of time out transactions.it should be properly validated to make sure that it contains the port, 
    URI and domain name or publicly available IP.
    
    ResultURL - The path that receives results from M-Pesa it should be properly validated to make sure that it contains the port, URI and domain name 
    or publicly available IP.
     
    */
    
    public void b2C(String initiatorName, String securityCredential, String commandID, double amount, String partyA, String partyB, String remarks, 
            String queueTimeOutURL, String resultURL, String occassion){
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"InitiatorName\":"+initiatorName+", \"SecurityCredential\":"+securityCredential+", "
                + "\"CommandID\":"+commandID+", \"Amount\":"+amount+", \"PartyA\":"+partyA+", \"PartyB\":"+partyB+", \"Remarks\":"+remarks+", "
                + "\"QueueTimeOutURL\":"+queueTimeOutURL+", \"ResultURL\":"+resultURL+", \"Occasion\":"+occassion+"}");
        
        Request request = new Request.Builder().url("https://sandbox.safaricom.co.ke/mpesa/b2c/v1/paymentrequest").post(body)
          .addHeader("authorization", "Bearer "+new AuthenticateToken().authenticate()).addHeader("content-type", "application/json").build();
        
        try {
            Response response = client.newCall(request).execute();
            System.err.println("Response:   "+response.body().string());
        } catch (IOException ex) {
            Logger.getLogger(Business2Customer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {
//        new AuthenticateToken().authenticate();
        
        PasswordEncrypter api=new PasswordEncrypter();
        api.setEncryptInitiatorPassword(System.getProperty("user.dir")+System.getProperty("file.separator")+"cert.cer", "n8m6YNsr");
        
        String encryptedPass=api.getEncryptedPassword();
        System.err.println("Encrypted password: "+encryptedPass);
        
        new Business2Customer().b2C("apiop57", encryptedPass, "BusinessPayment", 10, "602977", "254708374149", "Payment of some shit", 
                "http://115eec644.ngrok.io/CustomerDataWeb", "http://115eec64.ngrok.io/CustomerDataWeb", "-");
    }
}
