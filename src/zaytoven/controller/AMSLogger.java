/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zaytoven.controller;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author Kelsas
 */
public class AMSLogger {
    private final Logger logger=Logger.getLogger("AMS Application");
    private static AMSLogger instance;
    private AMSLogger(){
        try {
             String homeDir = System.getProperty("user.dir");
              String canonDirname = homeDir + System.getProperty("file.separator") + "var";
               File folderToCreate = new File(canonDirname);
               if(!folderToCreate.exists()){
               folderToCreate.mkdir();
               }
            Handler handler = new FileHandler("var/ams.log",1024*1024,1,true);
            SimpleFormatter sf=new SimpleFormatter();
            handler.setFormatter(sf);
            logger.addHandler(handler);
        } catch (IOException ex) {
            Logger.getLogger(AMSLogger.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(AMSLogger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static AMSLogger getInstance() {
        if(instance==null){
            instance=new AMSLogger();
        }
        return instance;
    }

    public  Logger getLogger() {
        return logger;
    }
    
}
