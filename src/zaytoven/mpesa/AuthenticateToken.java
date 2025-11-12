/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zaytoven.mpesa;

import java.io.IOException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author ANNE
 */
public class AuthenticateToken {
    private String app_key;
    private String app_secret;
    public String authToken;
    public AuthenticateToken(){
        
    }
    
    public String authenticate(){
        app_key = "9xdpnA2cIisTGGu5lFPvnNEGifhBYWgD";
        app_secret = "EG7NiAGU1l2gDa70";
        
        String appKeySecret = app_key + ":" + app_secret;
        byte[] bytes = null;
        try {
            bytes = appKeySecret.getBytes("ISO-8859-1");
            String encoded = Base64.getEncoder().encodeToString(bytes);
//                String encoded=Base64.getEncoder().encode(bytes).toString();
                System.err.println("Encoded app_key:consumer_Secret:    "+encoded);
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder().url("https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials")
                                            .get().addHeader("authorization", "Basic "+encoded).addHeader("cache-control", "no-cache").build();

            Response response = client.newCall(request).execute();
            
            String toJson=response.body().string();
            
            JSONObject jSONObject = new JSONObject(toJson);
            System.err.println("Authentication Access Token:   "+jSONObject.getString("access_token"));
             authToken=jSONObject.getString("access_token");
        } catch (IOException | JSONException ex) {
            Logger.getLogger(AuthenticateToken.class.getName()).log(Level.SEVERE, null, ex);
        }
        return authToken;
    }
    public static void main(String[] args) {
        new AuthenticateToken().authenticate();
    }
}
