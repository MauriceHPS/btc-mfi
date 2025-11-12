package zaytoven.controller;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


//import com.afrisoftech.dbadmin.DBObject;
//import com.afrisoftech.dbadmin.DatePicker;
//import com.afrisoftech.dbadmin.AMSLogger;
//import com.Configuration;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.decorator.Highlighter;
import org.jdesktop.swingx.decorator.HighlighterFactory;

/**
 *
 * @author Kelsas
 */
public class AMSUtility {
    /**
     * Return the Selected Gender from a group of buttons
     * @param bg
     * @return 
     */
    public static Highlighter DEFAULT_HIGHLIGHTER=HighlighterFactory.createAlternateStriping(Color.WHITE, Color.CYAN);
    public static Highlighter SIMPLE_HIGHLIGHTER=HighlighterFactory.createSimpleStriping();
    public static String getSelectedGender(ButtonGroup bg){
        String gender=null;
        for (Enumeration<AbstractButton> buttons = bg.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
             if(button.isSelected()){
                gender=button.getText();
             }
        }
        return gender;
    }
    public static String getSelectedButton(ButtonGroup bg){
        String gender=null;
        for (Enumeration<AbstractButton> buttons = bg.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
             if(button.isSelected()){
                gender=button.getText();
             }
        }
        return gender;
    }
    public static String getDateOfBirthAsString(String age, Date dob, Connection connectDB) {
        if (age.equals("") || age.isEmpty()) {
            String dobStr = new SimpleDateFormat("yyyy-MM-dd").format(dob);
            return dobStr;
        } else {
            try {
                int factor = 365;
                int years = Integer.parseInt(age);
                years *= factor;
                ResultSet rsetDq = SQLHelper.getResultset(connectDB, "select current_date - '" + years + "'::int");
                    String dobStr = null;
                    while (rsetDq.next()) {
                        dobStr = rsetDq.getString(1);
                    }
                    return dobStr;
            } catch (SQLException ex) {
                Logger.getLogger(AMSUtility.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return null;
    }
    public static void showWarningDialog(String msg,String title){
       JOptionPane.showMessageDialog(new JFrame(), msg,title,JOptionPane.WARNING_MESSAGE);
   } 
    public static void showErrorDialog(String msg,String title){
       JOptionPane.showMessageDialog(new JFrame(), msg,"Error Message",JOptionPane.ERROR_MESSAGE);
   }
    public static void showInfoDialog(String msg,String title){
       JOptionPane.showMessageDialog(new JFrame(), msg,title,JOptionPane.INFORMATION_MESSAGE);
   }
    public static void showSearchDialog(JTextField location,JDialog screenDialog){
        java.awt.Point point = location.getLocationOnScreen();
        screenDialog.setSize(400,200);
        screenDialog.setLocation(point);
        screenDialog.setVisible(true);
    }
    public static void showSearchDialog(JComponent location,JDialog screenDialog){
        java.awt.Point point = location.getLocationOnScreen();
        screenDialog.setSize(400,200);
        screenDialog.setLocation(point);
        screenDialog.setVisible(true);
    }
    public static Class typeToClass(int type) {
        Class result;

        switch (type) {
            case Types.BIGINT: result = Long.class;  break;
            case Types.BINARY:  result = String.class; break;
            case Types.BIT: result = Boolean.class; break;
            case Types.CHAR:  result = Character.class;  break;
            case Types.DATE: result = java.sql.Date.class; break;
            case Types.DECIMAL: result = Double.class; break;
            case Types.DOUBLE: result = Double.class; break;
            case Types.FLOAT: result = Float.class; break;
            case Types.INTEGER: result = Integer.class; break;
            case Types.LONGVARBINARY: result = String.class;  break;
            case Types.LONGVARCHAR: result = String.class; break;
            case Types.NULL: result = String.class; break;
            case Types.NUMERIC: result = Double.class; break;
            case Types.OTHER:  result = String.class; break;
            case Types.REAL: result = Double.class; break;
            case Types.SMALLINT: result = Short.class; break;
            case Types.TIME: result = java.sql.Time.class; break;
            case Types.TIMESTAMP:  result = java.sql.Timestamp.class; break;
            case Types.TINYINT:  result = Short.class; break;
            case Types.VARBINARY:  result = String.class; break;
            case Types.VARCHAR: result = String.class; break;
            default: result = null;
        }

        return result;
    }
    /**
     * 
     * @param connectDB
     * @param SQLQuery
     * @param header
     * @param canEdit
     * @return 
     */
    public static DefaultTableModel SQLTableModel(Connection connectDB,String SQLQuery,String[] header,boolean[] canEdit){
        try {
            Vector col=new Vector(Arrays.asList(header));
            Vector rowData = null;
            DBObject dbObject = new DBObject();
            Vector tableData = new Vector(1, 1);
            ArrayList<Class> types=new ArrayList<Class>();
            
                PreparedStatement psmt = connectDB.prepareStatement(SQLQuery);
                ResultSet rset = psmt.executeQuery();
                ResultSetMetaData rsetMetaData = rset.getMetaData();
                int columnCount = rsetMetaData.getColumnCount();
                //Create classtype
                int count=0;
                while (rset.next()) {
                   //create row of data 
                    rowData = new Vector(columnCount);
                    for (int j = 0; j < columnCount; j++) {
                        //Get the class type first time only
                        int colNo = rset.getMetaData().getColumnType(j + 1);
                        if (colNo == Types.NUMERIC || colNo == Types.DOUBLE || colNo == Types.DECIMAL) {
                            rowData.addElement(new Double(dbObject.getDBObject(rset.getObject(j + 1), "")));
                        } else if ((colNo == Types.BOOLEAN) || (colNo == Types.BIT)) {
                            rowData.addElement(Boolean.valueOf(dbObject.getDBObject(rset.getObject(j + 1), "false")));
                        } else {
                            rowData.addElement(dbObject.getDBObject(rset.getString(j + 1), ""));
                        }
                        if(count==1){
                            if(colNo==Types.DATE){
                                types.add(Object.class); 
                            }else{
                               types.add(typeToClass(colNo));
                            }
                        }
                    }
                    //Create a vector of rows of data
                    tableData.add(rowData);
                    //count rows
                    count++;
                }
                 

            final boolean[] editable=canEdit;
            final ArrayList<Class> classType= types;
                DefaultTableModel tableModel = new DefaultTableModel(tableData, col) {
                    boolean[] canEdit = editable;
                    ArrayList<Class> types = classType;
                    @Override
                    public Class getColumnClass(int columnIndex) {
                         if(types.isEmpty())return null;
                        return types.get(columnIndex);
                    }
                    @Override
                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return canEdit[columnIndex];
                    }
                };
            return tableModel;    
        } catch (SQLException ex) {
         
            Logger.getLogger(AMSUtility.class.getName()).log(Level.SEVERE, null, ex);
           return null;
        }
    }
    /**
     *  A JDBC Class TableModel. It creates a table model SQL Query and overrides the class types and headers of the same
     *  @param connectDB 
     *  @param columnHeader 
     *  @param classTypes 
     * @param canEdit 
     *  @param SQLQuery 
     * @return  
     */
    public static DefaultTableModel getJDBCTableModel(Connection connectDB, String columnHeader[],Class[] classTypes,boolean[] canEdit,String SQLQuery) {
        
        DefaultTableModel tableModel = null; 
        DBObject dbObject=new DBObject();
        
        Vector colHeads = new Vector(Arrays.asList(columnHeader));
        Vector dataViewVector = new Vector(1, 1);
        
        try {

            Statement psmt = connectDB.createStatement();
            ResultSet rset = psmt.executeQuery(SQLQuery);
            ResultSetMetaData rsetMetaData = rset.getMetaData();
            int columnCount = rsetMetaData.getColumnCount();
            Vector rawData;
            
            while (rset.next()) {
                rawData = new Vector(columnCount);
                for (int j = 0; j < columnCount; j++) {
                    if (rset.getMetaData().getColumnType(j + 1) == java.sql.Types.NUMERIC) {

                        rawData.addElement(new Double(dbObject.getDBObject(rset.getObject(j + 1), "")));

                    } else if ((rset.getMetaData().getColumnType(j + 1) == java.sql.Types.BOOLEAN) || (rset.getMetaData().getColumnType(j + 1) == java.sql.Types.BIT)) {

                        rawData.addElement(Boolean.valueOf(dbObject.getDBObject(rset.getObject(j + 1), "false")));

                    } else {

                        rawData.addElement(dbObject.getDBObject(rset.getString(j + 1), ""));
                    }
                }
                dataViewVector.add(rawData);
            } 
            final Class[] colTypes=classTypes;
            final boolean[] editable=canEdit;
             tableModel = new DefaultTableModel(dataViewVector, colHeads){
                Class[] types = colTypes;
                boolean[] canEdit=editable;
                 @Override
                public Class getColumnClass(int columnIndex) {
                    return types[columnIndex];
                }
                 @Override
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                 return canEdit [columnIndex];
                 }
             };

        } catch (SQLException ex) {
            Logger.getLogger(AMSUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tableModel;
    }
    /**
     * Rendering of Combobox in Jtable
     * @param connectDB
     * @param SQLQuery
     * @return 
     * @
     */
    public static DefaultCellEditor renderComboBox(Connection connectDB,String SQLQuery){
       JComboBox cb=new JComboBox(ComboBoxModel.ComboBoxModel(connectDB,SQLQuery));
      DefaultCellEditor editor =new DefaultCellEditor(cb);
      return editor;
    }
    public static String getDateLable() {
        java.lang.String date_label = null;
        java.lang.String month_now_strs = null;
        java.lang.String date_now_strs = null;
        java.lang.String year_now_strs = null;
        java.lang.String minute_now_strs = null;
        java.lang.String hour_now_strs = null;

        java.lang.Runtime rt = java.lang.Runtime.getRuntime();

        java.util.Calendar calinst = java.util.Calendar.getInstance();

        java.util.Date date_now = calinst.getTime();

        int date_now_str = date_now.getDate();

        int month_now_str = date_now.getMonth();

        int year_now_str = date_now.getYear();

        int hour_now_str = date_now.getHours();


        int minute_now_str = date_now.getMinutes();

        int year_now_abs = year_now_str - 100;

        if (year_now_abs < 10) {
            year_now_strs = "200" + year_now_abs;
        } else {
            year_now_strs = "20" + year_now_abs;
        }

        switch (month_now_str) {
            case 0:
                month_now_strs = "JAN";
                break;

            case 1:
                month_now_strs = "FEB";


                break;

            case 2:
                month_now_strs = "MAR";

                break;

            case 3:
                month_now_strs = "APR";

                break;

            case 4:
                month_now_strs = "MAY";

                break;

            case 5:
                month_now_strs = "JUN";

                break;

            case 6:
                month_now_strs = "JUL";

                break;

            case 7:
                month_now_strs = "AUG";


                break;

            case 8:
                month_now_strs = "SEP";

                break;

            case 9:
                month_now_strs = "OCT";

                break;

            case 10:
                month_now_strs = "NOV";

                break;

            case 11:
                month_now_strs = "DEC";

                break;

            default:
                if (month_now_str < 10) {

                    month_now_strs = "0" + month_now_str;


                } else {

                    month_now_strs = "" + month_now_str;

                }

        }

        if (date_now_str < 10) {

            date_now_strs = "0" + date_now_str;

        } else {

            date_now_strs = "" + date_now_str;

        }

        if (minute_now_str < 10) {

            minute_now_strs = "0" + minute_now_str;

        } else {


            minute_now_strs = "" + minute_now_str;

        }

        if (hour_now_str < 10) {

            hour_now_strs = "0" + hour_now_str;

        } else {

            hour_now_strs = "" + hour_now_str;

        }

        date_label = date_now_strs + month_now_strs + year_now_strs + "@" + hour_now_strs + minute_now_strs;

        return date_label;

    }
    public static String formatCurrency(String number2format){
        String formattedCurrNo=null;
            if (number2format == null) {
            formattedCurrNo = "0.00";
            return formattedCurrNo;
        } else {
            try {
                java.text.DecimalFormat nf = (java.text.DecimalFormat)java.text.NumberFormat.getInstance();
                nf.setMinimumFractionDigits(2);                
                nf.setMaximumFractionDigits(2);
                java.lang.Number number = nf.parse(number2format);
                formattedCurrNo = nf.format(number);
            } catch (ParseException ex) {
                Logger.getLogger(AMSUtility.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return formattedCurrNo;
    
    }
   public static void logUserAccess(Connection connectDB){
        try {
            // Get hostname by textual representation of IP address
            InetAddress addr = InetAddress.getLocalHost();
            // Get the host name
            String hostname = addr.getHostName();
            String ipAddr = addr.getHostAddress();
             System.out.println("Machine Address : " + addr);
            // Get canonical host name
            String hostnameCanonical = addr.getCanonicalHostName();
            System.out.println("Machine Name : " + hostname);
            System.err.println("Hostname "+hostnameCanonical);
            java.sql.PreparedStatement pstmt = connectDB.prepareStatement("INSERT INTO pb_login(ip_adress, hostname) VALUES (?,?)");
            pstmt.setString(1, ipAddr);
            pstmt.setString(2, hostname);
            pstmt.executeUpdate();
        } catch (UnknownHostException | SQLException ex) {
          AMSLogger.getInstance().getLogger().severe(ex.getMessage());
        }
    

   }
   public static String removeHTML(String htmlString){
          // Remove HTML tag from java String    
        String noHTMLString = htmlString.replaceAll("\\<.*?\\>", "");
        // Remove Carriage return from java String
        noHTMLString = noHTMLString.replaceAll("\r", "<br/>");

        // Remove New line from java string and replace html break
        noHTMLString = noHTMLString.replaceAll("\n", " ");
        noHTMLString = noHTMLString.replaceAll("\'", "&#39;");
        noHTMLString = noHTMLString.replaceAll("\"", "&quot;");
        return noHTMLString;
    }
   
   /**Key listener to numeric on
     * @param evt
     */
   public static void numListener(java.awt.event.KeyEvent evt) {                                     
        char c = evt.getKeyChar();
        if (!((c >= '0') && (c <= '9') || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
            Toolkit.getDefaultToolkit().beep();
            evt.consume();
        }
    }
   /**Retrieves only the whole number and discards the decimal from the doubl
     * @param obj
     * @param obje
     * @return */
   public static int getCastedDouble(Object obj){
        Double val=(Double)obj;
        BigDecimal big= new BigDecimal(val);
        
        return big.intValue();
    }
   /**this method changes a string first letter to capital and the rest to lower case
     * @param givenString
     * @return  */
   public static String toTitleCase(String givenString) {
            String[] arr = givenString.split(" ");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0))).append(arr[i].substring(1).toLowerCase()).append(" ");
            }          
          return sb.toString().trim();
        }  
    public static void autoShutDown(Connection connect) {
        try {
            java.sql.PreparedStatement pstmt = connect.prepareStatement("UPDATE pb_login set end_date = current_date,closed =true WHERE user_name = current-user and closed =false");
            pstmt.executeUpdate();
            
            Configuration.getInstance().save();
            System.exit(0);
        } catch (SQLException ex) {
            Logger.getLogger(AMSUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    public String validateDate(DatePicker d) {
        String now = "";
        if (d.getDate() == null) {
            now = null;
        } else {
            now = String.valueOf(d.getDate());
        }
        return now;
    }

    public String validateComboData(JComboBox cb) {
        String data = null;
        if (cb.getSelectedItem() == null) {
            data = "";
        } else {
            data = cb.getSelectedItem().toString();
        }
        return data;
    }
    
    public static BigInteger getUnscaledValue(double val){
        BigDecimal bd = new BigDecimal(val);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
      return bd.unscaledValue();
    }
    /**Converts the given ImageFile to Byte[]
     * @param file
     * @return java.
     * @throws java.io.FileNotFoundException */
    public static byte [] ImageToByte(File file) throws FileNotFoundException{
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        try {
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum);      
            }
        } catch (IOException ex) {
        }
        byte[] bytes = bos.toByteArray();
     
     return bytes; 
    }
    /**Converts the given byte[] to Image File
     * @param bytes
     * @param imageFile
     * @throws java.io.IOException */
    public static void byteToImage(byte [] bytes,File imageFile) throws IOException{
     
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        Iterator readers = ImageIO.getImageReadersByFormatName("jpeg");
 
        ImageReader reader = (ImageReader) readers.next();
        Object source = bis; // File or InputStream, it seems file is OK
 
        ImageInputStream iis = ImageIO.createImageInputStream(source);

        reader.setInput(iis, true);
        ImageReadParam param = reader.getDefaultReadParam();
 
        Image image = reader.read(0, param);
        //got an image file
 
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
      
        Graphics2D g2 = bufferedImage.createGraphics();
        g2.drawImage(image, null, null);
 
        ImageIO.write(bufferedImage, "jpeg", imageFile);
        //"jpeg" is the format of the image
        //imageFile is the file to be written to.
        System.out.println(imageFile.getPath());
    } 
    
     /**
     * Method changes the System theme dynamically
     * @param lnf
     * @param themepack
     * @param parent
     */
    public static void changeThemeByUrl(java.lang.String lnf, java.net.URL themepack,JFrame parent) {
        try {
            java.lang.String themepackPath = null;
            com.l2fprod.gui.plaf.skin.Skin skin = null;
            try {
                skin = com.l2fprod.gui.plaf.skin.SkinLookAndFeel.loadThemePack(themepack);
            } catch (java.lang.Exception exec) {
            }
            com.l2fprod.gui.plaf.skin.SkinLookAndFeel.setSkin(skin);
            java.awt.Component[] component_array = null; 
            component_array = parent.getComponents();
            
            javax.swing.UIManager.setLookAndFeel(lnf);
            javax.swing.SwingUtilities.updateComponentTreeUI(parent);
            javax.swing.SwingUtilities.updateComponentTreeUI(parent.getRootPane());
           parent.repaint();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(AMSUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void changeTheme(java.lang.String lnf, java.lang.String themepack, JFrame parent) {
        try {
            java.lang.String themepackPath = null;
            com.l2fprod.gui.plaf.skin.Skin skin = null;
            try {
                skin = com.l2fprod.gui.plaf.skin.SkinLookAndFeel.loadThemePack(themepack);
            } catch (java.lang.Exception exec) {
            }
            com.l2fprod.gui.plaf.skin.SkinLookAndFeel.setSkin(skin);
            java.awt.Component[] component_array = null;
            component_array = parent.getComponents();
            javax.swing.UIManager.setLookAndFeel(lnf);
            javax.swing.SwingUtilities.updateComponentTreeUI(parent);
            parent.repaint();
            javax.swing.SwingUtilities.updateComponentTreeUI(parent.getRootPane());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(AMSUtility.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public static void displayCalculator(){
          java.lang.Runtime rt = java.lang.Runtime.getRuntime();
        try {
            if (System.getProperty("os.name").equalsIgnoreCase("Linux")) {
                rt.exec("kcalc");
                System.out.print(System.getProperty("os.name") + "  " + System.getProperty("os.version"));
            } else {
                rt.exec("calc");
                System.out.print(System.getProperty("os.name") + "  " + System.getProperty("os.version"));
            }
        } catch (IOException IOExec) {
            JOptionPane.showMessageDialog(new JFrame(), "Sorry, can't get path to Help utility", "Error Message", JOptionPane.ERROR_MESSAGE);
        }         // Add your handling code here:
    }
    public static void openExplorer(){
         java.lang.Runtime rt = java.lang.Runtime.getRuntime();
        try {
            if (System.getProperty("os.name").equalsIgnoreCase("Linux")) {
                rt.exec("konqueror");
                System.out.print(System.getProperty("os.name") + "  " + System.getProperty("os.version"));
            } else {
                rt.exec("explorer");
                System.out.print(System.getProperty("os.name") + "  " + System.getProperty("os.version"));
            }
        } catch (IOException IOExec) {
            JOptionPane.showMessageDialog(new JFrame(), "Sorry, can't get path to Help utility", "Error Message", JOptionPane.ERROR_MESSAGE);
        }      // Add your handling code here:
    }
    public static ResultSet getResultset(Connection connectDB, String SQLQuery) throws SQLException {
            Statement st = connectDB.createStatement();
            return st.executeQuery(SQLQuery);
    } 
    public static void rollBackTransaction(Connection connectDB) throws SQLException{
        if(connectDB!=null){
            connectDB.rollback();
        } 
    }  
   //this method is used to round of the amount to a given decimal points. 
    public static double Round(double Rval, int Rpl) {
  double p = (double)Math.pow(10,Rpl);
  Rval = Rval * p;
  double tmp = Math.round(Rval);
  return (double)tmp/p;
  
    }
      
    public static String toMonthString(int month) {
        switch (month) {
            case 1:
                return "January";
            case 2:
                return "Febuary";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";

            default:
                return null;
        }
    }
    public static String getMacAddress(){
        try {
            InetAddress address = InetAddress.getLocalHost();
            NetworkInterface nwi = NetworkInterface.getByInetAddress(address);
            byte mac[] = nwi.getHardwareAddress(); 
            
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            return sb.toString();
        } catch (UnknownHostException | SocketException ex) {
            Logger.getLogger(AMSUtility.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public static String getPlainMacAddress(){
        return getMacAddress().replaceAll("-", "").trim();
    }
    
    
    public static void logMenuAccess(Connection connect,String menu,String appName){
        try {
            connect.setAutoCommit(false);
            try (java.sql.PreparedStatement pstmtx = connect.prepareStatement("insert into pb_sys_usage values(?,?,?)")) {
                pstmtx.setObject(1, menu);
                pstmtx.setString(2, "Menu");
                pstmtx.setString(3, appName);
                pstmtx.executeUpdate();
                connect.commit();
                connect.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AMSUtility.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    /**
     * The method reads the Company profile from database and sets to the System
     * @param connectDB
     * @return company name
     */
    public static  String getCompanyName(Connection connectDB) {
        java.lang.String title = null;
        try {
            java.sql.Statement stmt1 = connectDB.createStatement();
            java.sql.ResultSet rs1 = stmt1.executeQuery("select hospital_name from pb_hospitalprofile");
            while (rs1.next()) {
                title = "AMS :: " + (rs1.getString(1)); //- ["+jTabbedPane1.getSelectedComponent().getName()+"]";
            }
        } catch (java.sql.SQLException sqlEx) {
        }
        return title;
    }
    public static String toCurrency(double amount){
        NumberFormat d=NumberFormat.getCurrencyInstance();
        d.setCurrency(Currency.getInstance("KES"));
        return d.format(amount);
    }
}
