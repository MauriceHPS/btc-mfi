/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zaytoven.reports;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfCell;
import java.awt.Desktop;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import zaytoven.controller.AMSUtility;
import zaytoven.controller.ConnectionDB;
import zaytoven.controller.DBObject;
import zaytoven.controller.Format2Currency;
import zaytoven.controller.ReportUtil;
import zaytoven.controller.SQLHelper;

/**
 *
 * @author Helmut
 */
public class StocksReceivingPdf implements java.lang.Runnable{
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

    /*
 * @(#)DefaultTableModel.java	1.43 02/03/09
 **/
  
     java.awt.Desktop deskTop = Desktop.getDesktop();
    
    DBObject dbObject;
    
    int numberSeq = 0;
    
    private java.sql.Connection connectDB;
    
    public java.lang.String dbUserName = null;
    
    org.netbeans.lib.sql.pool.PooledConnectionSource pConnDB = null;
    
    boolean threadCheck = true;
    
    java.lang.Thread threadSample;
    
    com.lowagie.text.Font pFontHeader = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD);
    com.lowagie.text.Font pFontHeader1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL);
    
   
    java.lang.Runtime rtThreadSample = java.lang.Runtime.getRuntime();
    
    java.lang.Process prThread;
    AMSUtility label = new AMSUtility();
    private String receive;
    Format2Currency format;
    
    public void StocksReceivingPdf(String receive_no) {
        connectDB = ConnectionDB.getInstance().getCon();
        
        receive=receive_no;
        
        
        
        dbObject = new DBObject();
        
        threadSample = new java.lang.Thread(this, "SampleThread");
        
        System.out.println("threadSample created");
        
        threadSample.start();
        format=new Format2Currency();
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
                        while(rset4.next()){
                            date = rset4.getObject(1).toString();
                        }
//                        com.lowagie.text.HeaderFooter headerFoter = new com.lowagie.text.HeaderFooter(new Phrase(""+compName+""),false);// FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA, 14, Font.BOLDITALIC,java.awt.Color.blue)));
//                        headerFoter.setAlignment(com.lowagie.text.HeaderFooter.ALIGN_CENTER);
//                        headerFoter.setRight(5);
//                        docPdf.setHeader(headerFoter);
                        
                    } catch(java.sql.SQLException SqlExec) {
                        
                        javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), SqlExec.getMessage());
                        
                    }
                    
                    com.lowagie.text.HeaderFooter footer = new com.lowagie.text.HeaderFooter(new Phrase("Stock Receiving - Page: "), true);// FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA, 12, Font.BOLDITALIC,java.awt.Color.blue));
                    
                    docPdf.setFooter(footer);
                    
                    
                    docPdf.open();
                    
                    ReportUtil.addCenteredTitlePage(docPdf, connectDB);
                    try {
                        
                        
                        com.lowagie.text.pdf.PdfPTable table = new com.lowagie.text.pdf.PdfPTable(12);
                        
                        int headerwidths[] = {6, 8, 8, 10, 9, 7, 9, 7, 9, 9, 9, 9};
                        
                        table.setWidths(headerwidths);
                        
                        table.setWidthPercentage((100));
                        table.getDefaultCell().setBorder(Rectangle.BOTTOM);
                        
                        table.getDefaultCell().setColspan(12);
                        
                        
                        Phrase phrase; 
                        
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setBorder(PdfCell.TOP | PdfCell.BOTTOM | PdfCell.LEFT | PdfCell.RIGHT);
                        
                        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM);
                        
                        table.getDefaultCell().setColspan(6);
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                        
                        phrase=new Phrase("Receiving Stock Items", pFontHeader);
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
                        
                        phrase = new Phrase("Printed On  :" +date , pFontHeader);
                        table.addCell(phrase);
                        
                        
                        table.getDefaultCell().setColspan(12);
                        
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_CENTER);
                        phrase=new Phrase(receive, pFontHeader);
                        table.addCell(phrase);
                        
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setColspan(1);
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                        
                         phrase = new Phrase("#",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Item Code",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Item Name",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Qty Ordered",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Qty Received",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Price",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Total",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Supplier",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Date Ordered",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Receive Date",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Order ID",pFontHeader);
                        table.addCell(phrase);    
                        
                        phrase = new Phrase("Received By",pFontHeader);
                        table.addCell(phrase);
                        
                        
                        
                       table.getDefaultCell().setColspan(1);
                       
                       table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                        try {
                            
                            ResultSet rset = SQLHelper.getResultset(connectDB, "select rec.item_code, itm.item_name, ord.quantity_ordered, quantity_received, buying_price, "
                                    + "quantity_received*buying_price as total, sup.supplier_names, date_ordered, receiving_date, rec.order_id, receiver from st_receiving_issuing rec, "
                                    + "stock_items itm, stock_orders ord, stock_suppliers sup where rec.item_code=itm.item_code and ord.item_code=rec.item_code "
                                    + "and ord.order_id=rec.order_id and sup.supplier_id=rec.supplier_id and receiving_no='"+receive+"'");
                             int count=1;
                            while (rset.next()) {
                                
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
                                
                                phrase = new Phrase(dbObject.getDBObject(format.Format2Currency(rset.getObject(6).toString()), "-"),pFontHeader1);
                                table.addCell(phrase);
                                
                                phrase = new Phrase(dbObject.getDBObject(rset.getObject(7), "-"),pFontHeader1);
                                table.addCell(phrase);
                                
                                phrase = new Phrase(dbObject.getDBObject(rset.getObject(8), "-"),pFontHeader1);
                                table.addCell(phrase);
                                
                                phrase = new Phrase(dbObject.getDBObject(rset.getObject(9), "-"),pFontHeader1);
                                table.addCell(phrase);
                                
                                phrase = new Phrase(dbObject.getDBObject(rset.getObject(10), "-"),pFontHeader1);
                                table.addCell(phrase);
                                
                                phrase = new Phrase(dbObject.getDBObject(rset.getObject(11), "-"),pFontHeader1);
                                table.addCell(phrase);
                                
                                count++;
                            }
                            
                            table.getDefaultCell().setColspan(12);
                            phrase=new Phrase(" ");
                            table.addCell(phrase);
                            
                            double gt=0.00;
                            ResultSet news =SQLHelper.getResultset(connectDB, "SELECT  sum(itm.buying_price*quantity_received) as total  FROM st_receiving_issuing REV, \n" +
"                                    stock_items itm where receiving_no='"+receive+"' and itm.item_code=rev.item_code");
                            while(news.next()){
                                gt=news.getDouble(1);
                            }
                            
                            table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
                            phrase=new Phrase("GRAND TOTAL: "+format.Format2Currency(String.valueOf(gt)), pFontHeader);
                            table.addCell(phrase);
                            
                            System.err.println("grand total");
                             
                            docPdf.add(table);
                            
                        } catch(java.sql.SQLException SqlExec) {
                            
                            javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), SqlExec.getMessage());
                            
                        }
                        
                      
                    } catch(com.lowagie.text.BadElementException BadElExec) {
                        
                        javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), BadElExec.getMessage());
                        
                    }
                    
                } catch(java.io.FileNotFoundException fnfExec) {
                    
                    javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), fnfExec.getMessage());
                    
                } catch (SQLException | BadElementException ex) {
                    Logger.getLogger(StocksReceivingPdf.class.getName()).log(Level.SEVERE, null, ex);
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

