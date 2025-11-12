/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zaytoven.mpesa;

/**
 *
 * @author ANNE
 */
public class MpesaMain {
    public MpesaMain(){
        
    }
    public static void main(String[] args) {
        AuthenticateToken a=new AuthenticateToken();
        a.authenticate();
    }
}
