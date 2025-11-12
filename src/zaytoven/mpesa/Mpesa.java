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

public class Mpesa {
    String appKey;
    String appSecret;
    
    public Mpesa(String app_key, String app_secret){
        appKey=app_key;
        appSecret=app_secret;
    }
    public Mpesa(){
        
    }
    
    public String authenticate() {
        String app_key = appKey;
        String app_secret = appSecret;
        String appKeySecret = app_key + ":" + app_secret;
        byte[] bytes;
        JSONObject jsonObject = null;
        String retrievedToken=null;
        try {
            bytes = appKeySecret.getBytes("ISO-8859-1");
            String encoded = Base64.getEncoder().encodeToString(bytes);

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder().url("https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials").get()
                    .addHeader("authorization", "Basic "+encoded).addHeader("cache-control", "no-cache").build();

            Response response;
            
        
            response = client.newCall(request).execute();
            jsonObject=new JSONObject(response.body().string());
            
            System.out.println(jsonObject.getString("access_token"));
            retrievedToken=jsonObject.toString();
            
            System.err.println("Retrieved Token:    "+retrievedToken);
            
        } catch (IOException | JSONException ex) {
            Logger.getLogger(Mpesa.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return retrievedToken;
    }
    
    

    public String B2CRequest( String initiatorName, String securityCredential,String commandID, String  amount, String partyA,String partyB, 
        String remarks, String queueTimeOutURL, String resultURL, String occassion) {
        JSONArray jsonArray=new JSONArray();
        JSONObject jsonObject=new JSONObject();
        String b2C=null;
        try {
            jsonObject.put("InitiatorName", initiatorName);
            jsonObject.put("SecurityCredential", securityCredential);
            jsonObject.put("CommandID", commandID);
            jsonObject.put("Amount", amount);
            jsonObject.put("PartyA", partyA);
            jsonObject.put("PartyB", partyB);
            jsonObject.put("Remarks", remarks);
            jsonObject.put("QueueTimeOutURL", queueTimeOutURL);
            jsonObject.put("ResultURL", resultURL);
            jsonObject.put("Occassion", occassion);
            
            
            jsonArray.put(jsonObject);
            
            String requestJson=jsonArray.toString().replaceAll("[\\[\\]]","");
            
            
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, requestJson);
            Request request = new Request.Builder().url( "https://sandbox.safaricom.co.ke/mpesa/b2c/v1/paymentrequest").post(body)
                    .addHeader("content-type", "application/json").addHeader("authorization", "Bearer "+authenticate()).addHeader("cache-control", "no-cache")
                    .build();
            
            Response response = client.newCall(request).execute();
            System.out.println(response.body().string());
            b2C= response.body().toString();
        } catch (JSONException | IOException ex) {
            Logger.getLogger(Mpesa.class.getName()).log(Level.SEVERE, null, ex);
        }
        return b2C;
    }

    public String B2BRequest( String initiatorName, String accountReference,String securityCredential,String commandID, String senderIdentifierType,
	String receiverIdentifierType,float  amount, String partyA,String partyB, String remarks, String queueTimeOutURL, String resultURL, String occassion) throws IOException {
        JSONArray jsonArray=new JSONArray();
        JSONObject jsonObject=new JSONObject();
        String B2B=null;
        try {
            jsonObject.put("Initiator", initiatorName);
            jsonObject.put("SecurityCredential", securityCredential);
            jsonObject.put("CommandID", commandID);
            jsonObject.put("SenderIdentifierType", senderIdentifierType);
            jsonObject.put("RecieverIdentifierType",receiverIdentifierType);
            jsonObject.put("Amount", amount);
            jsonObject.put("PartyA", partyA);
            jsonObject.put("PartyB", partyB);
            jsonObject.put("Remarks", remarks);
            jsonObject.put("AccountReference", accountReference);
            jsonObject.put("QueueTimeOutURL", queueTimeOutURL);
            jsonObject.put("ResultURL", resultURL);
            
            
            
            jsonArray.put(jsonObject);
            
            String requestJson=jsonArray.toString().replaceAll("[\\[\\]]","");
            System.out.println(requestJson);
            
            OkHttpClient client = new OkHttpClient();
            
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, requestJson);
            Request request = new Request.Builder()
                    .url("https://sandbox.safaricom.co.ke/safaricom/b2b/v1/paymentrequest")
                    .post(body)
                    .addHeader("content-type", "application/json")
                    .addHeader("authorization", "Bearer "+authenticate())
                    .addHeader("cache-control", "no-cache")
                    
                    .build();
            
            Response response = client.newCall(request).execute();
            B2B= response.body().string();
        } catch (JSONException ex) {
            Logger.getLogger(Mpesa.class.getName()).log(Level.SEVERE, null, ex);
        }
        return B2B;
    }
    public String STKPushSimulation(String businessShortCode, String password, String timestamp,String transactionType, String amount, String phoneNumber, String partyA, String partyB, String callBackURL, String queueTimeOutURL,String accountReference, String transactionDesc) throws IOException {
        JSONArray jsonArray=new JSONArray();
        JSONObject jsonObject=new JSONObject();
        String stk=null;
        try {
            jsonObject.put("BusinessShortCode", businessShortCode);
            jsonObject.put("Password", password);
            jsonObject.put("Timestamp", timestamp);
            jsonObject.put("TransactionType", transactionType);
            jsonObject.put("Amount",amount);
            jsonObject.put("PhoneNumber", phoneNumber);
            jsonObject.put("PartyA", partyA);
            jsonObject.put("PartyB", partyB);
            jsonObject.put("CallBackURL", callBackURL);
            jsonObject.put("AccountReference", accountReference);
            jsonObject.put("QueueTimeOutURL", queueTimeOutURL);
            jsonObject.put("TransactionDesc", transactionDesc);
            
            
            
            jsonArray.put(jsonObject);
            
            String requestJson=jsonArray.toString().replaceAll("[\\[\\]]","");
            
            OkHttpClient client = new OkHttpClient();
            String url="https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest";
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, requestJson);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("content-type", "application/json")
                    .addHeader("authorization", "Bearer "+authenticate())
                    .addHeader("cache-control", "no-cache")
                    .build();
            
            
            Response response = client.newCall(request).execute();
            System.out.println(response.body().string());
            stk= response.body().toString();
        } catch (JSONException ex) {
            Logger.getLogger(Mpesa.class.getName()).log(Level.SEVERE, null, ex);
        }
        return stk;
    }
    public String STKPushTransactionStatus( String businessShortCode, String password, String timestamp, String checkoutRequestID) throws IOException {
        JSONArray jsonArray=new JSONArray();
        JSONObject jsonObject=new JSONObject();
        String stk=null;
        try {
            jsonObject.put("BusinessShortCode", businessShortCode);
            jsonObject.put("Password", password);
            jsonObject.put("Timestamp", timestamp);
            jsonObject.put("CheckoutRequestID", checkoutRequestID);
            
            
            
            
            jsonArray.put(jsonObject);
            
            String requestJson=jsonArray.toString().replaceAll("[\\[\\]]","");
            
            
            OkHttpClient client = new OkHttpClient();
            
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, requestJson);
            Request request = new Request.Builder()
                    .url("https://sandbox.safaricom.co.ke/mpesa/stkpushquery/v1/query")
                    .post(body)
                    .addHeader("authorization", "Bearer "+authenticate())
                    .addHeader("content-type", "application/json")
                    .build();
            
            Response response = client.newCall(request).execute();
            System.out.println(response.body().string());
            stk= response.body().toString();
        } catch (JSONException ex) {
            Logger.getLogger(Mpesa.class.getName()).log(Level.SEVERE, null, ex);
        }
        return stk;
    }
    public String reversal(String initiator, String securityCredential, String commandID, String transactionID, String amount, String receiverParty, String recieverIdentifierType, String resultURL,String queueTimeOutURL, String remarks, String ocassion) throws IOException {
        JSONArray jsonArray=new JSONArray();
        JSONObject jsonObject=new JSONObject();
        String reversal=null;
        try {
            jsonObject.put("Initiator", initiator);
            jsonObject.put("SecurityCredential", securityCredential);
            jsonObject.put("CommandID", commandID);
            jsonObject.put("TransactionID", transactionID);
            jsonObject.put("Amount",amount);
            jsonObject.put("ReceiverParty", receiverParty);
            jsonObject.put("RecieverIdentifierType", recieverIdentifierType);
            jsonObject.put("QueueTimeOutURL", queueTimeOutURL);
            jsonObject.put("ResultURL", resultURL);
            jsonObject.put("Remarks", remarks);
            jsonObject.put("Occasion", ocassion);
            
            
            
            jsonArray.put(jsonObject);
            
            String requestJson=jsonArray.toString().replaceAll("[\\[\\]]","");
            System.out.println(requestJson);
            
            OkHttpClient client = new OkHttpClient();
            
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, requestJson);
            Request request = new Request.Builder()
                    .url("https://sandbox.safaricom.co.ke/safaricom/reversal/v1/request")
                    .post(body)
                    .addHeader("content-type", "application/json")
                    .addHeader("authorization", "Bearer xNA3e9KhKQ8qkdTxJJo7IDGkpFNV")
                    .addHeader("cache-control", "no-cache")
                    .build();
            
            Response response = client.newCall(request).execute();
            System.out.println(response.body().string());
            reversal= response.body().string();
        } catch (JSONException ex) {
            Logger.getLogger(Mpesa.class.getName()).log(Level.SEVERE, null, ex);
        }
        return reversal;
    }
    
    public String balanceInquiry(String initiator, String commandID, String securityCredential, String partyA, String identifierType, String remarks, 
	String queueTimeOutURL, String resultURL) throws IOException {
        JSONArray jsonArray=new JSONArray();
        JSONObject jsonObject=new JSONObject();
        String balanceInquiry=null;
        try {
            jsonObject.put("Initiator", initiator);
            jsonObject.put("SecurityCredential", securityCredential);
            jsonObject.put("CommandID", commandID);
            jsonObject.put("PartyA", partyA);
            jsonObject.put("IdentifierType",identifierType);
            jsonObject.put("Remarks", remarks);
            jsonObject.put("QueueTimeOutURL", queueTimeOutURL);
            jsonObject.put("ResultURL", resultURL);
            
            
            
            
            jsonArray.put(jsonObject);
            
            String requestJson=jsonArray.toString().replaceAll("[\\[\\]]","");
            System.out.println(requestJson);
            
            OkHttpClient client = new OkHttpClient();
            
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, requestJson);
            Request request = new Request.Builder()
                    .url("https://sandbox.safaricom.co.ke/safaricom/accountbalance/v1/query")
                    .post(body)
                    .addHeader("content-type", "application/json")
                    .addHeader("authorization", "Bearer fwu89P2Jf6MB1A2VJoouPg0BFHFM")
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "2aa448be-7d56-a796-065f-b378ede8b136")
                    .build();
            
            Response response = client.newCall(request).execute();
            balanceInquiry= response.body().string();
        } catch (JSONException ex) {
            Logger.getLogger(Mpesa.class.getName()).log(Level.SEVERE, null, ex);
        }
        return balanceInquiry;
    }
    
    
    public static void main(String[] args) throws IOException {
//        Mpesa m=new Mpesa("oXIZ1kGpyv3HoTUiMgs76IA088rKJgGy","hdGoS9tVz01FhVwf");
//        m.authenticate();
        
        Customer2Business c=new Customer2Business();
        c.registerURL("602977", "Cancelled", "http://headpoint.co.ke", "http://headpoint.co.ke");
        /*m.C2BSimulation("600576","CustomerPayBillOnline","2","254708374149","hkjhjkhjkh");
        m.authenticate();*/
/*
        m.B2CRequest("testapi","BVeDP3XWGFG+NCQri04jHp6c0rCajO1JAOccQ7Bsu/Mup3Rh2Gd9IHQEE0SeA1oBXAt/VBAL/cJP+VKU9qRF6voqCa0P1XG8pcv5hTZUcBkbbb8Qqvqn28+s/tBvsLXwsB4QaageFDDZgS6b6gbK1p7+UZ/hRYHL8WclTpYBrQGfhqKZxduh0bPWvK4rt+uqR3hdVlO0RdJSkcOVCVp+FxizPSk3nI6LFq14Jj2G0TwuQ4a13J/KVu5eeFG65gzE1NnIVouHKeBPz9b9xvove156aR16uxh4rBq5U6UAKC/kUhaJ0wOLTvb762CioudL87C6xaPVdTF4qcSD6jM4PA==","BusinessPayment","22","600576","254708374149","This","http://obscure-bayou-52273.herokuapp.com/api/Mpesa/C2BConfirmation","http://obscure-bayou-52273.herokuapp.com/api/Mpesa/C2BValidation","http://obscure-bayou-52273.herokuapp.com/api/Mpesa/C2BValidation");
*/
/*
        m.B2BRequest("testapi","his","BVeDP3XWGFG+NCQri04jHp6c0rCajO1JAOccQ7Bsu/Mup3Rh2Gd9IHQEE0SeA1oBXAt/VBAL/cJP+VKU9qRF6voqCa0P1XG8pcv5hTZUcBkbbb8Qqvqn28+s/tBvsLXwsB4QaageFDDZgS6b6gbK1p7+UZ/hRYHL8WclTpYBrQGfhqKZxduh0bPWvK4rt+uqR3hdVlO0RdJSkcOVCVp+FxizPSk3nI6LFq14Jj2G0TwuQ4a13J/KVu5eeFG65gzE1NnIVouHKeBPz9b9xvove156aR16uxh4rBq5U6UAKC/kUhaJ0wOLTvb762CioudL87C6xaPVdTF4qcSD6jM4PA==","BusinessPayBill","1", "4",22,"600576","600000","This","http://obscure-bayou-52273.herokuapp.com/api/Mpesa/C2BConfirmation","http://obscure-bayou-52273.herokuapp.com/api/Mpesa/C2BValidation","http://obscure-bayou-52273.herokuapp.com/api/Mpesa/C2BValidation");
*/
/*
        m.STKPushSimulation("174379","MTc0Mzc5YmZiMjc5ZjlhYTliZGJjZjE1OGU5N2RkNzFhNDY3Y2QyZTBjODkzMDU5YjEwZjc4ZTZiNzJhZGExZWQyYzkxOTIwMTcwODI0MTU1MDU1","20170824155055","CustomerPayBillOnline","1","254724513769","254724513769","174379","http://obscure-bayou-52273.herokuapp.com/api/Mpesa/C2BValidation","http://obscure-bayou-52273.herokuapp.com/api/Mpesa/C2BValidation","sasas","asdasd");
*/
/*
        m.reversal("testapi","BVeDP3XWGFG+NCQri04jHp6c0rCajO1JAOccQ7Bsu/Mup3Rh2Gd9IHQEE0SeA1oBXAt/VBAL/cJP+VKU9qRF6voqCa0P1XG8pcv5hTZUcBkbbb8Qqvqn28+s/tBvsLXwsB4QaageFDDZgS6b6gbK1p7+UZ/hRYHL8WclTpYBrQGfhqKZxduh0bPWvK4rt+uqR3hdVlO0RdJSkcOVCVp+FxizPSk3nI6LFq14Jj2G0TwuQ4a13J/KVu5eeFG65gzE1NnIVouHKeBPz9b9xvove156aR16uxh4rBq5U6UAKC/kUhaJ0wOLTvb762CioudL87C6xaPVdTF4qcSD6jM4PA==","TransactionReversal","2121","2","22","4","http://obscure-bayou-52273.herokuapp.com/api/Mpesa/C2BValidation","http://obscure-bayou-52273.herokuapp.com/api/Mpesa/C2BConfirmation","Remarks","Ocassions");
*/
/*
        m.registerURL("600576","Cancelled","http://obscure-bayou-52273.herokuapp.com/api/Mpesa/C2BValidation","http://obscure-bayou-52273.herokuapp.com/api/Mpesa/C2BValidation");
*/
/*
        System.out.println("Hello World!");
*/
/*
        m.balanceInquiry("testapi","AccountBalance","BVeDP3XWGFG+NCQri04jHp6c0rCajO1JAOccQ7Bsu/Mup3Rh2Gd9IHQEE0SeA1oBXAt/VBAL/cJP+VKU9qRF6voqCa0P1XG8pcv5hTZUcBkbbb8Qqvqn28+s/tBvsLXwsB4QaageFDDZgS6b6gbK1p7+UZ/hRYHL8WclTpYBrQGfhqKZxduh0bPWvK4rt+uqR3hdVlO0RdJSkcOVCVp+FxizPSk3nI6LFq14Jj2G0TwuQ4a13J/KVu5eeFG65gzE1NnIVouHKeBPz9b9xvove156aR16uxh4rBq5U6UAKC/kUhaJ0wOLTvb762CioudL87C6xaPVdTF4qcSD6jM4PA==", "600576","4","These","http://obscure-bayou-52273.herokuapp.com/api/Mpesa/C2BConfirmation","http://obscure-bayou-52273.herokuapp.com/api/Mpesa/C2BConfirmation");
*/
/*
        m.STKPushTransactionStatus("174379","MTc0Mzc5YmZiMjc5ZjlhYTliZGJjZjE1OGU5N2RkNzFhNDY3Y2QyZTBjODkzMDU5YjEwZjc4ZTZiNzJhZGExZWQyYzkxOTIwMTcwODI0MTU1MDU1","20170824155055","ws_CO_27102017101215530");
*/
    }
}