/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zaytoven.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kelsas
 */
public class Configuration {

    static private final String FILE = System.getProperty("user.dir") + System.getProperty("file.separator") + "hosprop.properties";
    static private Logger log = null;
    static public FileHandler fh;
    static private Configuration _instance;
    private Properties props;

    private Configuration() {
//        try {
//            fh = new FileHandler("amslog.xml");
//        } catch (IOException ex) {
//            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (SecurityException ex) {
//            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
//        }

        props = new Properties();
        log = Logger.getLogger(Configuration.class.getName());
//        log.addHandler(fh);
        log.info("Loading PT system Properties");
        log.info(FILE);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(FILE);
            props.load(fis);

            updateSystemProperty();

            if (getProperty("dbServerIpAdd") == null) {
                Logger.getLogger(Configuration.class.getName()).log(Level.INFO, "Rewriting New Hosprop file ");
                setDefaults();
                save();
            }
        } catch (FileNotFoundException ex) {
            setDefaults();
            save();
        } catch (IOException io) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, io.getMessage());
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                    Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }


    }
   /*
    * Loads the Current User System Properties to JVM memory
    */
    public void updateSystemProperty() {
       
        System.setProperty("tableColorB", getProperty("tableColorB"));
        System.setProperty("tableColorA", getProperty("tableColorA"));
        System.setProperty("phrase.separator", getProperty("phrase.separator"));
        System.setProperty("receipt_printer", getProperty("receipt_printer"));
        System.setProperty("linesperpage", getProperty("linesperpage"));
        System.setProperty("invoices_printer", getProperty("invoices_printer"));
        System.setProperty("charactersperpage", getProperty("charactersperpage"));
        System.setProperty("tableColorHeader", getProperty("tableColorHeader"));
        System.setProperty("rcptlinesperpage", getProperty("rcptlinesperpage"));
        System.setProperty("activeDatabase", getProperty("activeDatabase"));
        System.setProperty("dbPort", getProperty("dbPort"));
        System.setProperty("cashpoint", getProperty("cashpoint"));
        System.setProperty("company.watermark", getProperty("company.watermark"));
        System.setProperty("defaulttheme", getProperty("defaulttheme"));
        System.setProperty("papersize_width", getProperty("papersize_width"));
        System.setProperty("path2Acrobat", getProperty("path2Acrobat"));
        System.setProperty("charactersperline", getProperty("charactersperline"));
        System.setProperty("font_type_name", getProperty("font_type_name"));

        System.setProperty("rcptcharactersperpage", getProperty("rcptcharactersperpage"));
        System.setProperty("receipt_textpagesize_y", getProperty("receipt_textpagesize_y"));
        System.setProperty("receipt_textpagesize_x", getProperty("receipt_textpagesize_x"));
        System.setProperty("receiptFontSize", getProperty("receiptFontSize"));
        System.setProperty("rcptcharactersperline", getProperty("rcptcharactersperline"));
        System.setProperty("images.dir", getProperty("images.dir"));
        System.setProperty("company.logo", getProperty("company.logo"));
        System.setProperty("docsdir", getProperty("docsdir"));
        System.setProperty("receiptPageMargin", getProperty("receiptPageMargin"));
        System.setProperty("papersize_legnth", getProperty("papersize_legnth"));
        System.setProperty("Mon", getProperty("Mon"));
        System.setProperty("dbServerIpAdd", getProperty("dbServerIpAdd"));
        System.setProperty("invoice_textpagesize_y", getProperty("invoice_textpagesize_y"));
        System.setProperty("defaultsplitpane", getProperty("defaultsplitpane"));
        System.setProperty("invoice_textpagesize_x", getProperty("invoice_textpagesize_x"));
        System.setProperty("backgrdimg", getProperty("backgrdimg"));
        System.setProperty("receiptTitleFontSize", getProperty("receiptTitleFontSize"));
        System.setProperty("defaultlnf", getProperty("defaultlnf"));  //multi-campany
        String company;
        if(getProperty("multi-company")==null){
            setProperty("multi-company", "0");
            save();
        }
       System.setProperty("multi-company", getProperty("multi-company"));
//         System.setProperties(props);
    }
    /*
     * Defaults System Properties of the system
     */
    private void setDefaults() {

        props.put("phrase.separator", "");
        props.put("tableColorB", "-1");
        props.put("tableColorA", "-16711681");
        props.put("receipt_printer", "Generic / Text Printer");
        props.put("linesperpage", "15");
        props.put("invoices_printer", "Auto Epson LX-300+");
        props.put("charactersperpage", "100");
        props.put("tableColorHeader", "-6710785");
        props.put("rcptlinesperpage", "15");
        props.put("activeDatabase", "medic");
        props.put("dbPort", "5432");
        props.put("cashpoint", "MAIN");
        props.put("company.watermark", "fish.png");
        props.put("defaulttheme", "");
        props.put("papersize_width", "595");
        props.put("path2Acrobat", "/usr/bin/kghostview");
        props.put("charactersperline", "72");
        props.put("font_type_name", "Times-Roman");
        props.put("rcptcharactersperpage", "21");
        props.put("receipt_textpagesize_y", "15");
        props.put("receipt_textpagesize_x", "72");
        props.put("receiptFontSize", "10");
        props.put("rcptcharactersperline", "72");
        props.put("images.dir", "C\\:\\hospital\\system");
        props.put("company.logo", "");
        props.put("docsdir", "C\\:\\hospital\\system");
        props.put("receiptPageMargin", "2");
        props.put("papersize_legnth", "240");
        props.put("Mon", "");
        props.put("dbServerIpAdd", "127.0.0.1");
        props.put("invoice_textpagesize_y", "297");
        props.put("defaultsplitpane", "Hospital Operations");
        props.put("invoice_textpagesize_x", "210");
        props.put("backgrdimg", "");
        props.put("receiptTitleFontSize", "12");
        props.put("defaultlnf", "");
        props.put("multi-company", "1");
//        System.setProperties(props);

    }

    public synchronized static Configuration getInstance() {
        if (_instance == null) {
            _instance = new Configuration();
        }
        return _instance;
    }
    /*
     * Stores the properties to file.
     */
    public void save() {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(FILE);
            props.store(fos, "AMS System Configurations");

            updateSystemProperty();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, ex.getMessage());
        } catch (IOException ex) {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, ex.getMessage());
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ex) {
                    Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, ex.getMessage());
                }
            }
        }
    }

    public String getProperty(String key) {
        return props.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }

    public void setProperty(String key, String value) {
        props.setProperty(key, value);
    }

    public Properties getProps() {
        return props;
    }

    public void setProps(Properties props) {
        this.props = props;
    }
}
