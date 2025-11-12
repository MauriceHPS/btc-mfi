/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zaytoven.reports;

/**
 *
 * @author Helmut
 */

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
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import zaytoven.controller.AMSUtility;
import zaytoven.controller.ConnectionDB;
import zaytoven.controller.DBObject;
import zaytoven.controller.Format2Currency;
import zaytoven.controller.ReportUtil;
import zaytoven.controller.SQLHelper;


public class ReplacingReturnsSummPdf implements java.lang.Runnable{
  
     java.awt.Desktop deskTop = Desktop.getDesktop();
    
    DBObject dbObject;
    
    int numberSeq = 0;
    
    public static java.sql.Connection connectDB = null;
    
    
    
    boolean threadCheck = true;
    
    java.lang.Thread threadSample;
    
    com.lowagie.text.Font pFontHeader = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD);
    com.lowagie.text.Font pFontHeader1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL);
    com.lowagie.text.Font pFontHeader4 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
    
   
    java.lang.Runtime rtThreadSample = java.lang.Runtime.getRuntime();
    
    java.lang.Process prThread;
    AMSUtility label = new AMSUtility();
    Date beginDate=null;
    Date endDate=null;
    
    Format2Currency format;
    
    public void ReplacingReturnsSummPdf(Date begin, Date end) {
        connectDB=ConnectionDB.getInstance().getCon();
        dbObject = new DBObject();
        beginDate=begin;
        endDate=end;
        
        format=new Format2Currency();
        
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
                    
                    
                    Date date = null;
                    try {
                        java.sql.Statement st4 = connectDB.createStatement();
                        
                        java.sql.ResultSet rset4 = st4.executeQuery("SELECT date(now()) as Date");
                        while(rset4.next()){
                            date = rset4.getDate(1);
                        }
                        
                    } catch(java.sql.SQLException SqlExec) {
                        
                        javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), SqlExec.getMessage());
                        
                    }
                    
                    com.lowagie.text.HeaderFooter footer = new com.lowagie.text.HeaderFooter(new Phrase("Replacing Return Inwards  Summary - Page: "), true);// FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA, 12, Font.BOLDITALIC,java.awt.Color.blue));
                    
                    docPdf.setFooter(footer);
                    
                    
                    docPdf.open();
                    
                    ReportUtil.addCenteredTitlePage(docPdf, connectDB);
                    System.err.println("Date:   "+date);
                    try {
                        
                        
                        com.lowagie.text.pdf.PdfPTable table = new com.lowagie.text.pdf.PdfPTable(9);
                        
                        int headerwidths[] = {8, 10, 10, 15, 10, 12, 13, 12, 10};
                        
                        table.setWidths(headerwidths);
                        
                        table.setWidthPercentage((100));
                        table.getDefaultCell().setBorder(Rectangle.BOTTOM);
                        
                        
                        
                        Phrase phrase; 
                        
                        table.getDefaultCell().setColspan(9);
                        
                        table.getDefaultCell().setBorder(PdfCell.TOP | PdfCell.BOTTOM | PdfCell.LEFT | PdfCell.RIGHT);
                        
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
                        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM);
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_CENTER);
                        phrase=new Phrase("Replacing Return Inwards", pFontHeader4);
                        table.addCell(phrase);
                        
                        phrase=new Phrase("Period Selected: From "+dateFormat.format(beginDate)+" To "+dateFormat.format(endDate),pFontHeader);
                        table.addCell(phrase);
                        
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
                        phrase = new Phrase("Printed On  :"+date, pFontHeader);
                        table.addCell(phrase);
                        
                        System.err.println("Date II:   "+date);
                        
                        table.getDefaultCell().setColspan(1);
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT); 
                        phrase = new Phrase("#",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("REPLACEMENT NO.",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("RETURN NO.",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("ITEM NO",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("ITEM NAME",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("QTY REPLACED",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("COST PER ITEM",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("TOTAL",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("DATE",pFontHeader);
                        table.addCell(phrase);
                        
                         table.getDefaultCell().setBackgroundColor(java.awt.Color.WHITE);
                       
                        try {
                            int count=1;
                            String collectionID=null;
                            table.getDefaultCell().setColspan(1);
                            ResultSet rset1=SQLHelper.getResultset(connectDB, "select distinct(crn_no) from credit_notes where replacement_date between '"+beginDate+"' and '"+endDate+"'");
                            while(rset1.next()){
                                collectionID=rset1.getString(1);
                                
                                
                                
                                ResultSet rset = SQLHelper.getResultset(connectDB, "select distinct(crn_no), return_no, c.item_no, item_name, quantity_replaced, p.selling_price, \n" +
"                                        (quantity_replaced*p.selling_price) as total, replacement_date from credit_notes c, stock_items s, customer_prices p\n" +
"                                        where item_code=c.item_no and c.item_no=p.item_no and item_code=p.item_no and replacement_date between '"+beginDate+"' and '"+beginDate+"' "
        + "                                 and crn_no='"+collectionID+"'");
                             
                                table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                                while (rset.next()) {
                                    phrase = new Phrase(""+count+"   ", pFontHeader1);
                                    table.addCell(phrase);

//                                    phrase=new Phrase(""+collectionID+"", pFontHeader1);
//                                    table.addCell(phrase);
                                
                                   table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                                   phrase = new Phrase(dbObject.getDBObject(rset.getObject(1), "-"),pFontHeader1);
                                   table.addCell(phrase);

                                   phrase = new Phrase(dbObject.getDBObject(rset.getObject(2), "-"),pFontHeader1);
                                   table.addCell(phrase);

                                   table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                                   phrase = new Phrase(dbObject.getDBObject(rset.getObject(3), "-"),pFontHeader1);
                                   table.addCell(phrase);

                                   table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                                   phrase = new Phrase(dbObject.getDBObject(rset.getObject(4), "-"),pFontHeader1);
                                   table.addCell(phrase);

                                   table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                                   phrase = new Phrase(dbObject.getDBObject(rset.getObject(5), "-"),pFontHeader1);
                                   table.addCell(phrase);

                                   table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                                   phrase = new Phrase(dbObject.getDBObject(rset.getObject(6), "-"),pFontHeader1);
                                   table.addCell(phrase);

                                   table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                                   phrase = new Phrase(dbObject.getDBObject(format.Format2Currency(rset.getObject(7).toString()), "-"),pFontHeader1);
                                   table.addCell(phrase);
                                   
                                   table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                                   phrase = new Phrase(dbObject.getDBObject(rset.getObject(8), "-"),pFontHeader1);
                                   table.addCell(phrase);
                                   count++;
                                }
                            }
                            
                            
                            table.getDefaultCell().setColspan(9);
                            //TOTAL ITEMS COLLECTED
                            table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
                            ResultSet rs22=SQLHelper.getResultset(connectDB, "select sum((quantity_replaced*p.selling_price)) as total from credit_notes c, stock_items s, customer_prices p\n" +
"                                    where replacement_date between '"+beginDate+"' and '"+endDate+"' and c.item_no=item_code and p.item_no=c.item_no and item_code=p.item_no ");
                            while(rs22.next()){
                                table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
                                phrase=new Phrase("GRAND TOTAL:     "+dbObject.getDBObject(format.Format2Currency(rs22.getObject(1).toString()), "-"), pFontHeader4);
                                table.addCell(phrase);
                            }
                             docPdf.add(table);
                            
                        } catch(java.sql.SQLException SqlExec) {
                            
                            javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), SqlExec.getMessage());
                            
                        }
                        
                      
                    } catch(com.lowagie.text.BadElementException BadElExec) {
                        
                        javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), BadElExec.getMessage());
                        
                    }
                    
                } catch(java.io.FileNotFoundException fnfExec) {
                    
                    javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), fnfExec.getMessage());
                    
                } catch (BadElementException ex) {
                    Logger.getLogger(StaffRegInactivePdf.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(ReplacingReturnsSummPdf.class.getName()).log(Level.SEVERE, null, ex);
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
    
    
}