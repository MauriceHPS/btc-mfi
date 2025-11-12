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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author ANNE
 */
public class Customer2Business {
    
    public Customer2Business(){
        
    }
    
    public  String registerURL(String shortCode, String responseType, String confirmationURL, String validationURL){
        JSONArray jsonArray=new JSONArray();
        JSONObject jsonObject=new JSONObject();
        String register=null;
        try {
            jsonObject.put("ShortCode", shortCode);
            jsonObject.put("ResponseType", responseType);
            jsonObject.put("ConfirmationURL", confirmationURL);
            jsonObject.put("ValidationURL", validationURL);
            
            
            
            
            jsonArray.put(jsonObject);
            
            String requestJson=jsonArray.toString().replaceAll("[\\[\\]]","");
            System.out.println(requestJson);
            
            OkHttpClient client = new OkHttpClient();
            
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, requestJson);
            Request request = new Request.Builder()
                    .url("https://sandbox.safaricom.co.ke/mpesa/c2b/v1/registerurl").post(body).addHeader("content-type", "application/json")
                    .addHeader("authorization", "Bearer "+new Mpesa().authenticate()).addHeader("cache-control", "no-cache").build();
            
            Response response = client.newCall(request).execute();
            System.out.println(response.body().string());
            register= response.body().string();
        } catch (JSONException ex) {
            Logger.getLogger(Mpesa.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Customer2Business.class.getName()).log(Level.SEVERE, null, ex);
        }
        return register;
    }
    
    public String C2BSimulation( String shortCode, String commandID, String amount, String MSISDN, String billRefNumber){
        
        JSONArray jsonArray=new JSONArray();
        JSONObject jsonObject=new JSONObject();
        String simulation=null;
        try {
            jsonObject.put("ShortCode", shortCode);
            jsonObject.put("CommandID", commandID);
            jsonObject.put("Amount", amount);
            jsonObject.put("Msisdn", MSISDN);
            jsonObject.put("BillRefNumber", billRefNumber);
            
            jsonArray.put(jsonObject);
            
            String requestJson=jsonArray.toString().replaceAll("[\\[\\]]","");
            System.out.println(requestJson);
            OkHttpClient client = new OkHttpClient();
            
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, requestJson);
            Request request = new Request.Builder().url("https://sandbox.safaricom.co.ke/safaricom/c2b/v1/simulate").post(body)
                    .addHeader("content-type", "application/json").addHeader("authorization", "Bearer "+new Mpesa().authenticate())
                    .addHeader("cache-control", "no-cache").build();
            
            Response response = client.newCall(request).execute();
            System.out.println(response.body().string());
            simulation= response.body().toString();
        } catch (JSONException | IOException ex) {
            Logger.getLogger(Mpesa.class.getName()).log(Level.SEVERE, null, ex);
        }
        return simulation;
    }
}
