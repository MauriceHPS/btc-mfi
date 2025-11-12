/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zaytoven.mpesa;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ANNE
 */
public class HttpServerListener {
    private HttpServer server;
    private int port;
    
    public HttpServerListener(){
        
    }
    
    public void listeningImplementation(){
        try {
            //Define the port over which the listener will accept requests
            port = 9000;

            server = HttpServer.create(new InetSocketAddress(port), 0);
            System.err.println("Server started at   "+port);
            /**
             * Create the context (endpoint/URL) which will be triggered when the request
             * hits the above specified port. This will resolve to a URL like
             * 'http://address:port/context'. E.g. the context below would
             * resolve to 'http://127.0.0.1:80/confirm' on the local computer. Then
             * the Handler will handle the request received via the given URL.
             *
             * You may create a separate context for every endpoint you need
             */
            server.createContext("/", new RootHandler());
            server.createContext("/echoHeader", new EchoHeaderHandler());
            server.createContext("/echoGet", new EchoGetHandler());
            server.createContext("/echoPost", new EchoPostHandler());
                

            server.setExecutor(null);

            //start the server
            server.start();
            System.out.println("Server started");
        } catch (IOException ex) {
            Logger.getLogger(HttpServerListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public class SimpleGet implements HttpHandler{

        @Override
        public void handle(HttpExchange exchange) {
            
        }
        
    }
    
    public class SimplePost implements HttpHandler{

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    
}
    
    //its used to display the server status
    public class RootHandler implements HttpHandler{

        @Override
        public void handle(HttpExchange he) {
            String response="<h1>If you see this message, the server started successfully. </h1>" + "<h1>Port: " +port+"</h1>";
            try {
                he.sendResponseHeaders(200, response.length());
                OutputStream os=he.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(HttpServerListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
    // declare echoHeader to display request header information
    public class EchoHeaderHandler implements HttpHandler{

        @Override
        public void handle(HttpExchange he){
            Headers headers=he.getRequestHeaders();
            Set <Map.Entry<String, List <String>>> entries=headers.entrySet();
            String response="";
            for(Map.Entry<String, List<String>> entry: entries)
                response+=entry.toString()+"\n";
            try {
                he.sendResponseHeaders(200, response.length());
                OutputStream os=he.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(HttpServerListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    //GET class [requests for data from specified sources]
    public class EchoGetHandler implements HttpHandler{

        @Override
        public void handle(HttpExchange he){
            //parse request
            Map <String, Object> parameters=new HashMap<>();
            URI requestedURI=he.getRequestURI();
            String query=requestedURI.getQuery();
            System.err.println("Get requested URI:  "+query);
            parseQuery(query, parameters);
            
            //send response
            String response="-";
            for(String key : parameters.keySet())
                response+=key+" = "+parameters.get(key)+"\n";
            try {
                he.sendResponseHeaders(200, response.length());
                OutputStream os=he.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } catch (IOException ex) {
                Logger.getLogger(HttpServerListener.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
        
        //POST CLASS [submits data to processed by the specified resource]
        public class EchoPostHandler implements HttpHandler{

            @Override
            public void handle(HttpExchange he){
                Map <String, Object> parameters=new HashMap();
                try {
                    InputStreamReader isr=new InputStreamReader(he.getRequestBody(), "utf-8");
                    BufferedReader br=new BufferedReader(isr);
                    String query=br.readLine();
                    parseQuery(query, parameters);
                    String response="";
                    for(String key : parameters.keySet())
                        response+=key+" = "+parameters.get(key)+"\n";
                    he.sendResponseHeaders(200, response.length());
                    OutputStream os=he.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                } catch (UnsupportedEncodingException ex) {
                    Logger.getLogger(HttpServerListener.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(HttpServerListener.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }
        
        //methods to parse request parameters
        public static void parseQuery(String query, Map <String, Object> parameters){
            if(query!=null){
                String [] pairs=query.split("[&]");
                for(String pair : pairs){
                    String param[] =pair.split("[=]");
                    String key=null;
                    String value=null;
                    try{
                        
                        if(param.length>0){
                            key=URLDecoder.decode(param[0], System.getProperty("file.encoding"));
                        }
                        if(param.length>1){
                            key=URLDecoder.decode(param[1],  System.getProperty("file.encoding"));
                        }
                        if(parameters.containsKey(key)){
                            Object obj=parameters.get(key);
                            if(obj instanceof List<?>){
                                List <String> values=(List <String>) obj;
                                values.add(value);
                            }
                            else if(obj instanceof String){
                                List <String> values=new ArrayList<>();
                                values.add((String) obj);
                                values.add(value);
                                parameters.put(key, value);
                            }
                        }
                        else{
                            parameters.put(key, value);
                        }
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(HttpServerListener.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
            }
        }
    public static void main(String[] args) {
        new HttpServerListener().listeningImplementation();
    }
}
