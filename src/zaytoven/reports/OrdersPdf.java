/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zaytoven.reports;

import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfCell;
import java.sql.*;
import java.awt.Desktop;
import java.util.logging.Level;
import java.util.logging.Logger;
import zaytoven.controller.AMSUtility;
import zaytoven.controller.DBObject;
import zaytoven.controller.Format2Currency;
import zaytoven.controller.ReportUtil;

/**
 *
 * @author Helmut
 */
public class OrdersPdf implements java.lang.Runnable{
    /*
 * @(#)DefaultTableModel.java	1.43 02/03/09
 **/
  
     java.awt.Desktop deskTop = Desktop.getDesktop();
    
    DBObject dbObject;
    
    int numberSeq = 0;
    
    public static java.sql.Connection connectDB = null;
    
    public java.lang.String dbUserName = null;
    
    org.netbeans.lib.sql.pool.PooledConnectionSource pConnDB = null;
    
    boolean threadCheck = true;
    
    java.lang.Thread threadSample;
    
    com.lowagie.text.Font pFontHeader = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD);
    com.lowagie.text.Font pFontHeader1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL);
    com.lowagie.text.Font pFontHeader2 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD);
    
   
    java.lang.Runtime rtThreadSample = java.lang.Runtime.getRuntime();
    
    java.lang.Process prThread;
    AMSUtility label = new AMSUtility();
    private String orders;
    Format2Currency format;
    
    public void OrdersPdf(java.sql.Connection connDb, String order_no) {
        this.orders=order_no;
        connectDB = connDb;
        format=new Format2Currency();
        
        dbObject = new DBObject();
        
        threadSample = new java.lang.Thread(this, "SampleThread");
        
        System.out.println("threadSample created");
        
        threadSample.start();
        
        System.out.println("threadSample fired");
        
    }
    
    
     @Override
    public void run() {
        
        System.out.println("System has entered running mode");
        
        while (threadCheck) {
            
            System.out.println("O.K. see how we execute target program");
            
            this.generatePdf();
            
            try {
                
                System.out.println("Right, let's wait for task to complete of fail");
                
                java.lang.Thread.currentThread().sleep(200);
                
                System.out.println("It's time for us threads to get back to work after the nap");
                
            } catch(java.lang.InterruptedException IntExec) {
                
                System.out.println(IntExec.getMessage());
                
            }
            
            threadCheck = false;
            
            
            System.out.println("We shall be lucky to get back to start in one piece");
            
        }
        
        if (!threadCheck) {
            
            
            
            Thread.currentThread().stop();
            
        }
        
    }
    
    public void generatePdf() {
       
        try {
            
            java.io.File tempFile = java.io.File.createTempFile("REP"+label.getDateLable()+"_", ".pdf");
            
            tempFile.deleteOnExit();
            
            java.lang.Runtime rt = java.lang.Runtime.getRuntime();
            
            java.lang.String debitTotal = null;
            
            java.lang.String creditTotal = null;
            com.lowagie.text.Document docPdf = new com.lowagie.text.Document(PageSize.A4.rotate());
            
            try {
                
                try {
                    
                    com.lowagie.text.pdf.PdfWriter.getInstance(docPdf, new java.io.FileOutputStream(tempFile));
                    
                    
                    String date = null;
                    try {
                        java.sql.Statement st4 = connectDB.createStatement();
                        
                        java.sql.ResultSet rset4 = st4.executeQuery("SELECT date(now()) as Date");
                        while(rset4.next())
                            date = rset4.getObject(1).toString();
                        
                    } catch(java.sql.SQLException SqlExec) {
                        
                        javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), SqlExec.getMessage());
                        
                    }
                    
                    com.lowagie.text.HeaderFooter footer = new com.lowagie.text.HeaderFooter(new Phrase("Orders List - Page: "), true);// FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA, 12, Font.BOLDITALIC,java.awt.Color.blue));
                    
                    docPdf.setFooter(footer);
                    
                    
                    docPdf.open();
                    
                    
                    try {
                    ReportUtil.addCenteredTitlePage(docPdf, connectDB);    
                        
                        com.lowagie.text.pdf.PdfPTable table = new com.lowagie.text.pdf.PdfPTable(10);
                        
                        int headerwidths[] = {5, 10, 13, 10, 13, 10, 10, 10, 10, 10};
                        
                        table.setWidths(headerwidths);
                        
                        table.setWidthPercentage((100));
                        table.getDefaultCell().setBorder(Rectangle.BOTTOM);
                        
                        
                        
                        
                        Phrase phrase; 
                        
                        table.getDefaultCell().setColspan(10);
                        
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_CENTER);
                        
                        phrase=new Phrase("Purchase Order", pFontHeader2);
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setBorder(PdfCell.TOP | PdfCell.LEFT | PdfCell.BOTTOM | PdfCell.RIGHT);
                        
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
                        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM);
                        
                        String order_nuos=null;
                        
                        Statement sta=connectDB.createStatement();
                        ResultSet rst=sta.executeQuery("select order_id from stock_orders where order_id='"+orders+"'");
                        while(rst.next()){
                            order_nuos=rst.getString(1);
                        }
                        
                        table.getDefaultCell().setColspan(5);
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                        
                        phrase=new Phrase(order_nuos, pFontHeader2);
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
                        
                        phrase = new Phrase("Printed On  :" +date , pFontHeader);
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setColspan(10);
                        
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setColspan(1);
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                        
                         phrase = new Phrase("Count",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Item Code",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Item Name",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Quantity",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Price",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Date Ordered",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Expected Delivery",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Supplier",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Ordered By",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Total",pFontHeader);
                        table.addCell(phrase);
                        
                        
                         table.getDefaultCell().setBackgroundColor(java.awt.Color.WHITE);
                       
                        try {
                            Statement s=connectDB.createStatement();
                            
                            java.sql.Statement st = connectDB.createStatement();
                            
                            java.sql.ResultSet rset = st.executeQuery("SELECT ord.item_code, itm.item_name, quantity_ordered, price, date_ordered, expected_supply_date, "
                                    + "s.supplier_names, ordered_by, ord.price*ord.quantity_ordered  FROM stock_orders ord, stock_items itm, stock_suppliers s where order_id='"+orders+"' "
                                    + "and ord.item_code=itm.item_code and s.supplier_id=ord.supplier_id");
                             int count=1;
                            while (rset.next()) {
                                
                                table.getDefaultCell().setColspan(1);
                                
                                phrase = new Phrase(""+count+"   ", pFontHeader1);
                                table.addCell(phrase);
                                
                                phrase = new Phrase(dbObject.getDBObject(rset.getObject(1), "-"),pFontHeader1);
                                table.addCell(phrase);
                                
                                phrase = new Phrase(dbObject.getDBObject(rset.getObject(2), "-"),pFontHeader1);
                                table.addCell(phrase);
                                
                                phrase = new Phrase(dbObject.getDBObject(rset.getObject(3), "-"),pFontHeader1);
                               table.addCell(phrase);
                                
                                phrase = new Phrase(dbObject.getDBObject(rset.getObject(4), "-"),pFontHeader1);
                                table.addCell(phrase);
                                
                                phrase = new Phrase(dbObject.getDBObject(rset.getObject(5), "-"),pFontHeader1);
                                table.addCell(phrase);
                                
                                phrase = new Phrase(dbObject.getDBObject(rset.getObject(6), "-"),pFontHeader1);
                                table.addCell(phrase);
                                
                                phrase = new Phrase(dbObject.getDBObject(rset.getObject(7), "-"),pFontHeader1);
                                table.addCell(phrase);
                                
                                phrase = new Phrase(dbObject.getDBObject(rset.getObject(8), "-"),pFontHeader1);
                                table.addCell(phrase);
                                
                                phrase = new Phrase(dbObject.getDBObject(format.Format2Currency(rset.getObject(9).toString()), "-"),pFontHeader1);
                                table.addCell(phrase);
                                
                                count++;
                            }
                            
                            table.getDefaultCell().setColspan(10);
                            phrase=new Phrase(" ");
                            table.addCell(phrase);
                            
                            double gt=0.00;
                            
                            ResultSet r=s.executeQuery("SELECT  SUM(price*quantity_ordered)  FROM stock_orders where order_id='"+orders+"' ");
                            
                            while(r.next()){
                                gt=r.getDouble(1);
                            }
                            table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
//                            table.getDefaultCell().setColspan(8);
                            phrase=new Phrase("GRAND TOTAL: "+format.Format2Currency(String.valueOf(gt)), pFontHeader2);
                            table.addCell(phrase);
//                            table.getDefaultCell().setColspan(2);
//                            table.addCell(new Phrase(""));
                            System.err.println("grand total "+gt);
                             docPdf.add(table);
                            
                        } catch(java.sql.SQLException SqlExec) {
                            
                            javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), SqlExec.getMessage());
                            
                        }
                        
                      
                    } catch(com.lowagie.text.BadElementException BadElExec) {
                        
                        javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), BadElExec.getMessage());
                        
                    } catch (SQLException ex) {
                        Logger.getLogger(OrdersPdf.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                } catch(java.io.FileNotFoundException fnfExec) {
                    
                    javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), fnfExec.getMessage());
                    
                }
            } catch(com.lowagie.text.DocumentException lwDocexec) {
                
                javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), lwDocexec.getMessage());
                
            }
            
            docPdf.close();
            
            deskTop.open(tempFile);
            
            
            
        } catch(java.io.IOException IOexec) {
            
            javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), IOexec.getMessage());
            
        }
        
        
        
    }
    
    
    public java.lang.Object[] getListofStaffNos() {
        
        java.lang.Object[] listofStaffNos = null;
        
        java.util.Vector listStaffNoVector = new java.util.Vector(1,1);
        
        
        
        listofStaffNos = listStaffNoVector.toArray();
        System.out.println("Done list of Staff Nos ...");
        return listofStaffNos;
    }
     
    
    
    
}







