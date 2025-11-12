/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zaytoven.controller;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author fowami
 */

/**
 *
 * @author fowami
 */
public class MD5Encrypter {
    
      public static String encrypt(String s) {
    try {
        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
        digest.update(s.getBytes());
        byte messageDigest[] = digest.digest();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < messageDigest.length; i++) {
            byte b = messageDigest[i];
            String hex = Integer.toHexString((int) 0x00FF & b);
            if (hex.length() == 1) {
                sb.append("0");
            }
            sb.append(hex);
        }

        return sb.toString();
    }
    catch (NoSuchAlgorithmException e) {
        // exception
    }

    return "";
}
      public static void main(String []args){
          System.err.println("rockafella23 " +MD5Encrypter.encrypt("jimmy2017")); 
          if(MD5Encrypter.encrypt("Florence").equals("bbf5e1be3178100ef6a81c2e4ba0304e")){
              System.err.println("ALLOWED");  
          }else{
           System.err.println(" NOT ALLOWED");      
          }
      }
}