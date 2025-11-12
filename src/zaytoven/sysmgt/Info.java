/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zaytoven.sysmgt;

import java.util.Calendar;

/**
 *
 * @author Helmut
 */
public class Info {
    public Info(){
        
    }
    private String privatepassword, productKey, publicpassword, holder;
    
    public String getPrivatePassword(){
        return privatepassword;
    }
    
    private void setPrivatePassword(String p){
        p="sestoelemento";
        privatepassword=p;
    }
    
    public String getProductKey(){
        return productKey;
    }
    
    private void setProductKey(){
        productKey="PDK2H-17JBD-ABDCE-1HJ2C";
    }
    
    public String getPublicPassword(){
        return publicpassword;
    }
    
    private void setPublicPassword(){
        publicpassword="hennesy";
    }
    
    private void setHolder(String holder){
        holder="www";
        this.holder=holder;
    holder="LEGIT INFORMATION SYSTEMS";
    }
    
    public String getHolder(){
    return holder;
    }
    public long setGoodBefore(){
        Calendar c=Calendar.getInstance();
        c.set(Calendar.YEAR, 0, 31);
        return c.getTimeInMillis();   
    }
    private void getGoodBefore(){
//        return 
    }
    public static void main(String[] args) {
        
        System.out.println(new Info().getProductKey());
    }
}

        