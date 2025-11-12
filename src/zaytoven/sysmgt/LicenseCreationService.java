/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zaytoven.sysmgt;
import net.nicholaswilliams.java.licensing.License;
import net.nicholaswilliams.java.licensing.licensor.LicenseCreator;


/**
 *
 * @author Helmut
 */

public class LicenseCreationService {
     public void createLicense(){
         License license;
         
         license = new License.Builder().withProductKey(new Info().getProductKey()).withHolder(new Info().getHolder()).
                 withGoodBeforeDate(99).build();
         byte[] licenseData=LicenseCreator.getInstance().signAndSerializeLicense(license, "bugattiskrr!!".toCharArray());
         
         
     }
   }


