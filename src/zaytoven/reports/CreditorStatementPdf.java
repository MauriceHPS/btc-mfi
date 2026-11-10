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
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
public class CreditorStatementPdf implements java.lang.Runnable{
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
    
    public static java.sql.Connection connectDB = null;
    
    public java.lang.String dbUserName = null;
    
    org.netbeans.lib.sql.pool.PooledConnectionSource pConnDB = null;
    
    boolean threadCheck = true;
    
    java.lang.Thread threadSample;
    
    com.lowagie.text.Font pFontHeader = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
    com.lowagie.text.Font pFontHeader1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL);
    com.lowagie.text.Font pFontHeader2 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD);
   
    java.lang.Runtime rtThreadSample = java.lang.Runtime.getRuntime();
    
    java.lang.Process prThread;
    AMSUtility label = new AMSUtility();
    Date beginDate, endDate;
    String supplierNo;
    
    Format2Currency format;
    public void CreditorStatementPdf(String supplier, Date begin, Date end) {
        
        connectDB =ConnectionDB.getInstance().getCon();
        beginDate=begin;
        endDate=end;
        supplierNo=supplier;
        dbObject = new DBObject();
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
                
                Thread.sleep(200);
                
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
            
            java.io.File tempFile = java.io.File.createTempFile("REP"+AMSUtility.getDateLable()+"_", ".pdf");
            
            tempFile.deleteOnExit();
            
            java.lang.Runtime rt = java.lang.Runtime.getRuntime();
            
            java.lang.String debitTotal = null;
            
            java.lang.String creditTotal = null;
            com.lowagie.text.Document docPdf = new com.lowagie.text.Document(PageSize.A4.rotate());
            
            try {
                
                try {
                    
                    com.lowagie.text.pdf.PdfWriter.getInstance(docPdf, new java.io.FileOutputStream(tempFile));
//                    ReportUtil.addCenteredTitlePage(docPdf, connectDB);
                    
                    String date = null;
                    try {
                      java.sql.Statement st3 = connectDB.createStatement();
                        java.sql.Statement st4 = connectDB.createStatement();
                        
                        java.sql.ResultSet rset4 = st4.executeQuery("SELECT date(now()) as Date");
                        
                        while(rset4.next())
                            date = rset4.getObject(1).toString();
                        
                    } catch(java.sql.SQLException SqlExec) {
                        
                        javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), SqlExec.getMessage());
                        
                    }
                    
                    com.lowagie.text.HeaderFooter footer = new com.lowagie.text.HeaderFooter(new Phrase("Creditor Statement- Page: "), true);// FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA, 12, Font.BOLDITALIC,java.awt.Color.blue));
                    
                    docPdf.setFooter(footer);
                    
                    
                    docPdf.open();
                    
                    ReportUtil.addCenteredTitlePage(docPdf, connectDB);
                    try {
                        
                        com.lowagie.text.pdf.PdfPTable table = new com.lowagie.text.pdf.PdfPTable(10);
                        
                        int headerwidths[] = {4, 9, 11, 8, 14, 8, 10, 12, 12, 12};
                        
                        table.setWidths(headerwidths);
                        
                        table.setWidthPercentage((100));
                        table.getDefaultCell().setBorder(Rectangle.BOTTOM);
                        
                        table.getDefaultCell().setColspan(10);
                        
                        
                        Phrase phrase; 
                        
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
                        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM);
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_CENTER);
                        phrase=new Phrase("CREDITOR STATEMENT", pFontHeader2);
                        table.addCell(phrase);
                        
                        phrase=new Phrase("Period Selected: "+dateFormat.format(beginDate)+" - "+dateFormat.format(endDate), pFontHeader2);
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
                        phrase = new Phrase("Printed On  :" +date , pFontHeader);
                        table.addCell(phrase);
                        
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
                        
                        ResultSet cus=SQLHelper.getResultset(connectDB, "select supplier_id, supplier_names, phone_no, email_address from creditors_register "
                                + "where active=TRUE and supplier_id='"+supplierNo+"'");
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                        String supplierID=null;
                        while(cus.next()){
                            table.getDefaultCell().setColspan(5);    
                            phrase=new Phrase("SUPPLIER ID: "+dbObject.getDBObject(cus.getObject(1), "-"), pFontHeader2);
                            table.addCell(phrase);
                            supplierID=cus.getString(1);
                            
                            phrase=new Phrase("SUPPLIER NAME:  "+dbObject.getDBObject(cus.getObject(2), "-"), pFontHeader2);
                            table.addCell(phrase);
                            
                            phrase=new Phrase("PHONE NO: "+dbObject.getDBObject(cus.getObject(3), "-"), pFontHeader2);
                            table.addCell(phrase);
                            
                            phrase=new Phrase("EMAIL: "+dbObject.getDBObject(cus.getObject(4), "-"), pFontHeader2);
                            table.addCell(phrase);
                        }
                        
                        table.getDefaultCell().setColspan(10);
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setColspan(1);
                        table.getDefaultCell().setBorder(PdfCell.TOP | PdfCell.BOTTOM | PdfCell.LEFT | PdfCell.RIGHT);
                        
                        phrase = new Phrase("#",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Invoice No",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Receiving No",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Item Code",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Item Name",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Quantity",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Price", pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Total" ,pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Amount Paid",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Balance",pFontHeader);
                        table.addCell(phrase);
                        
                        System.err.println("Supplier ID:    "+supplierID);
                        
                        table.getDefaultCell().setBackgroundColor(java.awt.Color.WHITE);
                       
                        try {
                            String  invoiceNo=null;
                            double total=0.00, amountPaid=0.00, balance=0.00;
                            
                            java.sql.Statement st = connectDB.createStatement();
                            
                            java.sql.ResultSet rset = st.executeQuery("select DISTINCT(INVOICE_NO), c.receiving_no, c.item_code, item_name, quantity_received, buying_price, \n" +
                            "(quantity_received*buying_price) as total, case when sum(debit-CREDIT) is null then '0.00' else sum(debit-CREDIT) end as AMOUNT_PAID "
                            +"from st_receiving_issuing s, stock_items i, creditors_ledger c where s.receiving_no=c.receiving_no and s.item_code=i.item_code and s.item_code=c.item_code "
                                    + "and i.item_code=c.item_code and date between '"+beginDate+"' and '"+endDate+"' and invoice_no!='-' GROUP BY 1, 2, 3, 4, 5, 6, 7");
                             
                            int count=1;
                            while (rset.next()) {
                                total=rset.getDouble(7);
                                amountPaid=rset.getDouble(8);
                                
                                balance=total-amountPaid;
                                
                                phrase=new Phrase(""+count+"", pFontHeader1);
                                table.addCell(phrase);
                                
                                phrase=new Phrase(dbObject.getDBObject(rset.getObject(1), "-"), pFontHeader1);
                                table.addCell(phrase);
                                
                                phrase=new Phrase(dbObject.getDBObject(rset.getObject(2), "-"), pFontHeader1);
                                table.addCell(phrase);
                                
                                phrase=new Phrase(dbObject.getDBObject(rset.getObject(3), "-"), pFontHeader1);
                                table.addCell(phrase);
                                
                                phrase=new Phrase(dbObject.getDBObject(rset.getObject(4), "-"), pFontHeader1);
                                table.addCell(phrase);
                                
                                phrase=new Phrase(dbObject.getDBObject(rset.getObject(5), "-"), pFontHeader1);
                                table.addCell(phrase);
                                
                                phrase=new Phrase(dbObject.getDBObject(rset.getObject(6), "-"), pFontHeader1);
                                table.addCell(phrase);
                                
                                phrase=new Phrase(dbObject.getDBObject(format.Format2Currency(rset.getObject(7).toString()), "-"), pFontHeader1);
                                table.addCell(phrase);
                                
                                phrase=new Phrase(dbObject.getDBObject(format.Format2Currency(rset.getObject(8).toString()), "-"), pFontHeader1);
                                table.addCell(phrase);
                                
                                phrase=new Phrase(format.Format2Currency(String.valueOf(balance)), pFontHeader1);
                                table.addCell(phrase);
                                
                                count++;
                            }
                            
                            table.getDefaultCell().setColspan(10);
                            phrase=new Phrase(" ");
                            table.addCell(phrase);
                            
                            phrase=new Phrase(" ");
                            table.addCell(phrase);
                            
                            ResultSet rs22=SQLHelper.getResultset(connectDB, "select case when sum(credit-debit) is null then '0.00' else sum(credit-debit) end as case1 "
                                    + "from creditors_ledger where date between '"+beginDate+"' and '"+endDate+"'");
                            while(rs22.next()){
                                table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
                                phrase=new Phrase("Outstanding Balance:     "+dbObject.getDBObject(format.Format2Currency(rs22.getObject(1).toString()), "-"), pFontHeader2);
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
                    
                } catch (SQLException | BadElementException ex) {
                    Logger.getLogger(CreditorStatementPdf.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch(com.lowagie.text.DocumentException lwDocexec) {
                
                javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), lwDocexec.getMessage());
                
            }
            try {
                        docPdf.close();
                         deskTop.open(tempFile);

                } catch (IOException e) {
                        JOptionPane.showMessageDialog(new JFrame(), e.getMessage());

                    }
            
            
        } catch(java.io.IOException IOexec) {
            
            javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), IOexec.getMessage());
            
        }
        
        
        
    }
    
      
}

