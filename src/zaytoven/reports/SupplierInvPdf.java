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
public class SupplierInvPdf implements java.lang.Runnable{
    
  
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
    
   
    java.lang.Runtime rtThreadSample = java.lang.Runtime.getRuntime();
    
    java.lang.Process prThread;
    AMSUtility label = new AMSUtility();
    private String invoice_no;
    
    Format2Currency format;
    
    public void SupplierInvPdf(java.sql.Connection connDb, String inv_no) {
        this.invoice_no=inv_no;
        connectDB = connDb;
        
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
                        while(rset4.next())
                            date = rset4.getObject(1).toString();
                        
                        
                    } catch(java.sql.SQLException SqlExec) {
                        
                        javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), SqlExec.getMessage());
                        
                    }
                    
                    com.lowagie.text.HeaderFooter footer = new com.lowagie.text.HeaderFooter(new Phrase("Supplier's Payment - Page: "), true);// FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA, 12, Font.BOLDITALIC,java.awt.Color.blue));
                    
                    docPdf.setFooter(footer);
                    
                    
                    docPdf.open();
                    ReportUtil.addCenteredTitlePage(docPdf, connectDB);
                    
                    try {
                        
                        
                        com.lowagie.text.pdf.PdfPTable table = new com.lowagie.text.pdf.PdfPTable(7);
                        
                        int headerwidths[] = {11, 11, 24, 12, 14, 14, 14};
                        
                        table.setWidths(headerwidths);
                        
                        table.setWidthPercentage((100));
                        table.getDefaultCell().setBorder(Rectangle.BOTTOM);
                        
                        table.getDefaultCell().setColspan(4);
                        
                        
                        Phrase phrase; 
                        
                        
                        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM);
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                        phrase=new Phrase("Invoice Specifics");
                        table.addCell(phrase);
                        table.getDefaultCell().setColspan(3);
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
                        phrase = new Phrase("Printed On  :" +date , pFontHeader);
                        table.addCell(phrase);
                        
                        
                        Statement str=connectDB.createStatement();
                        ResultSet r=str.executeQuery("SELECT distinct(c.account_no), SUPPLIER_NAMES FROM STOCK_SUPPLIERS, creditors_ledger c "
                                + "WHERE SUPPLIER_ID=c.account_no and invoice_no='"+invoice_no+"'");
                        String acno=null, names=null;
                        while(r.next()){
                            acno=r.getString(1);
                            names=r.getString(2);
                            table.getDefaultCell().setColspan(4);
                            table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                            phrase=new Phrase("ACCOUNT NO:  "+dbObject.getDBObject(r.getString(1), "-"), pFontHeader);
                            table.addCell(phrase);
                            table.getDefaultCell().setColspan(3);
                            table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                            phrase=new Phrase("SUPPLIER NAMES:  "+dbObject.getDBObject(r.getString(2), "-"), pFontHeader);
                            table.addCell(phrase);
                        }
                        
                        table.getDefaultCell().setColspan(7);
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_CENTER);
                        phrase=new Phrase("INVOICE NO: "+invoice_no, pFontHeader);
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
                        
                         table.getDefaultCell().setBackgroundColor(java.awt.Color.WHITE);
                       String receiving_no=null;
                        try {
                            Statement s=connectDB.createStatement();
                            ResultSet rr=s.executeQuery("SELECT DISTINCT(RECEIVING_NO) FROM CREDITORs_LEDGER WHERE INVOICE_NO='"+invoice_no+"'");
                            
                            while(rr.next()){
                                receiving_no=rr.getString(1);
                                
                                table.getDefaultCell().setColspan(7);
                                table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                                phrase=new Phrase("PAYMENT FOR:     "+receiving_no, pFontHeader);
                                table.addCell(phrase);
                                
                                
                                                table.getDefaultCell().setColspan(1);
                                        table.getDefaultCell().setBorder(PdfCell.TOP | PdfCell.LEFT | PdfCell.BOTTOM | PdfCell.RIGHT);
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

                                        phrase = new Phrase("Total",pFontHeader);
                                        table.addCell(phrase);

                                        phrase = new Phrase("Balance",pFontHeader);
                                        table.addCell(phrase);
                                
                                
                                java.sql.Statement st = connectDB.createStatement();
                                java.sql.ResultSet rset = st.executeQuery("SELECT DISTINCT(C.ITEM_CODE), ITEM_NAME, BUYING_PRICE, QUANTITY_RECEIVED, "
                                        + "(BUYING_PRICE*QUANTITY_RECEIVED) AS TOTAL, SUM(credit-DEBIT) as balance  FROM STOCK_ORDERS O, STOCK_ITEMS I, "
                                        + "ST_RECEIVING_ISSUING R, CREDITORS_LEDGER C WHERE O.ITEM_CODE=I.ITEM_CODE AND I.ITEM_CODE=C.ITEM_CODE "
                                        + "AND R.RECEIVING_NO=C.RECEIVING_NO AND C.RECEIVING_NO='"+receiving_no+"'  AND O.ORDER_ID=R.ORDER_ID "
                                        + "GROUP BY 1, 2, 3, 4, 5");
                                int count=1;
                                while (rset.next()) {

                                    table.getDefaultCell().setColspan(1);
                                    
                                    table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_CENTER);
                                    phrase = new Phrase(""+count+"   ", pFontHeader1);
                                    table.addCell(phrase);
                                    
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
                                    phrase = new Phrase(dbObject.getDBObject(format.Format2Currency(rset.getObject(5).toString()), "-"),pFontHeader1);
                                    table.addCell(phrase);

                                    table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                                    phrase = new Phrase(dbObject.getDBObject(format.Format2Currency(rset.getObject(6).toString()), "-"),pFontHeader1);
                                    table.addCell(phrase);

//                                    table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
//                                    phrase = new Phrase(dbObject.getDBObject(rset.getObject(7), "-"),pFontHeader1);
//                                    table.addCell(phrase);
                                    
                                    count++;
                            }
                            }
                            
                            table.getDefaultCell().setColspan(7);
                            phrase=new Phrase(" ");
                            table.addCell(phrase);
                            phrase=new Phrase(" ");
                            table.addCell(phrase);
                            
                            
                            
                             double bal=0.0;
//                             double bals=-bal;
                            Statement finale=connectDB.createStatement();
                            ResultSet f=finale.executeQuery("SELECT SUM(CREDIT-DEBIT) FROM CREDITORS_LEDGER WHERE ACCOUNT_NO='"+acno+"'");
                            
                            while(f.next()){
                                bal=f.getDouble(1);
                            }
                            double bals=-bal;
                            System.out.println();
                            if(bal>0){
                                table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
                                phrase=new Phrase("THE ORGANIZATION OWES "+names+" an amount of KSH. "+format.Format2Currency(String.valueOf(bal)), pFontHeader);
                                table.addCell(phrase);
                            }
                            
                            else{
                                table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
                                phrase=new Phrase("THE ORGANIZATION OVERPAID "+names+" an amount of KSH. "+format.Format2Currency(String.valueOf(bals)), pFontHeader);
                                table.addCell(phrase); 
                        }
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
                    
                } catch (SQLException | BadElementException ex) {
                    Logger.getLogger(SupplierInvPdf.class.getName()).log(Level.SEVERE, null, ex);
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