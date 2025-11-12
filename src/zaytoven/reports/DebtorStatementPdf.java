/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zaytoven.reports;

import com.lowagie.text.DocumentException;
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

/**
 *
 * @author Helmut
 */
public class DebtorStatementPdf implements java.lang.Runnable{
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
    com.lowagie.text.Font pFontHeader2 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD);
    com.lowagie.text.Font pFontHeader1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL);
    
   
    java.lang.Runtime rtThreadSample = java.lang.Runtime.getRuntime();
    
    java.lang.Process prThread;
    AMSUtility label = new AMSUtility();
    Date beginDate, endDate;
    String customerName;
    Format2Currency format;
    public void DebtorStatementPdf(String customer, Date begin, Date end) {
        
        connectDB =ConnectionDB.getInstance().getCon();
        beginDate=begin;
        endDate=end;
        customerName=customer;
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
        
        if (!threadCheck) {
            
            
            
            Thread.currentThread().stop();
            
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
            
            
                    
                    com.lowagie.text.pdf.PdfWriter.getInstance(docPdf, new java.io.FileOutputStream(tempFile));
//                    ReportUtil.addCenteredTitlePage(docPdf, connectDB);
                    
                    String date = null;
                   
                      java.sql.Statement st3 = connectDB.createStatement();
                        java.sql.Statement st4 = connectDB.createStatement();
                        
                        java.sql.ResultSet rset4 = st4.executeQuery("SELECT date(now()) as Date");
                        
                        while(rset4.next())
                            date = rset4.getObject(1).toString();
                        
                    
                    
                    com.lowagie.text.HeaderFooter footer = new com.lowagie.text.HeaderFooter(new Phrase("Branch Statement- Page: "), true);// FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA, 12, Font.BOLDITALIC,java.awt.Color.blue));
                    
                    docPdf.setFooter(footer);
                    
                    
                    docPdf.open();
                    
                    ReportUtil.addCenteredTitlePage(docPdf, connectDB);
                    
                        
                        
                        com.lowagie.text.pdf.PdfPTable table = new com.lowagie.text.pdf.PdfPTable(9);
                        
                        int headerwidths[] = {7, 10, 9, 17, 12, 12, 12, 12, 12};
                        
                        table.setWidths(headerwidths);
                        
                        table.setWidthPercentage((100));
                        table.getDefaultCell().setBorder(Rectangle.BOTTOM);
                        
                        table.getDefaultCell().setColspan(9);
                        
                        
                        Phrase phrase; 
                        
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setBorder(PdfCell.TOP | PdfCell.BOTTOM | PdfCell.LEFT | PdfCell.RIGHT);
                        
                        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM);
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_CENTER);
                        phrase=new Phrase("CUSTOMER STATEMENT", pFontHeader2);
                        table.addCell(phrase);
                        
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setColspan(5);
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                        phrase=new Phrase("Period Selected:  "+dateFormat.format(beginDate)+" - "+dateFormat.format(endDate), pFontHeader2);
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setColspan(4);
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
                        phrase = new Phrase("Printed On  :" +date , pFontHeader);
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setColspan(9);
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
                         table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_CENTER);
                        phrase=new Phrase("CUSTOMER NAME:   "+customerName, pFontHeader2);
                        table.addCell(phrase);
                        
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
//                        String customerName=null;
                        double total=0.00, amountPaid=0.00, balance=0.00;
                        
                        
                        ResultSet cus=SQLHelper.getResultset(connectDB, "select distinct(c.customer_id),  branch_name FROM customers_registration c, debtors_ledger d "
                                + "where c.customer_id=d.customer_id and names ilike '%"+customerName+"%' ");
                        
                        String branchName=null, customerID=null;
                        
                        while(cus.next()){
                            customerID=cus.getString(1);
                            branchName=cus.getString(2);
                            
                            table.getDefaultCell().setColspan(4);
                            table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_CENTER);
                            phrase=new Phrase("CUSTOMER ID: "+dbObject.getDBObject(cus.getObject(1), "-"), pFontHeader1);
                            table.addCell(phrase);

                            table.getDefaultCell().setColspan(5);
                            phrase=new Phrase("BRANCH NAME:  "+dbObject.getDBObject(cus.getObject(2), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            
                            
                            System.err.println("Customer ID: "+customerID);
                        System.err.println("Branch Name:    "+branchName);
                        
                        table.getDefaultCell().setColspan(9);
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setColspan(1);
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                        
                        phrase = new Phrase("#",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Invoice No",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Item No",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Item Name",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Quantity",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Price",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Total",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Amount Paid",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Balance",pFontHeader);
                        table.addCell(phrase);
                        
//                         table.getDefaultCell().setBackgroundColor(java.awt.Color.WHITE);
                       table.getDefaultCell().setColspan(1);
                       table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                       
                            String invoiceNo=null;
                            int count=1;
                                
                            ResultSet rset = SQLHelper.getResultset(connectDB, "select distinct(debt_no), item_code, item_name, d.quantity, p.selling_price, (d.quantity*p.selling_price) as total \n" +
                            " from debtors_ledger d, saleable_items i, customer_prices p where item_code=i.item_no and item_code=p.item_no and i.item_no=p.item_no \n" +
                            " and spec='SALES' and d.customer_id='"+customerID+"' and date between '"+beginDate+"' and '"+endDate+"' and p.customer_id=d.customer_id");

                            while (rset.next()) {
                                invoiceNo=rset.getString(1);
                                
                                System.err.println("Invoice No: "+invoiceNo);
                                
                                table.getDefaultCell().setColspan(1);

                                phrase = new Phrase(""+count+"   ", pFontHeader1);
                                table.addCell(phrase);

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
                                phrase = new Phrase(dbObject.getDBObject(format.Format2Currency(rset.getObject(6).toString()), "-"),pFontHeader1);
                                table.addCell(phrase);
                                
                                total=rset.getDouble(6);

                                ResultSet rset1=SQLHelper.getResultset(connectDB, "select case when sum(credit) is null then '0.00' else sum(credit) end as clause from debtors_ledger "
                                        + "where debt_no='"+invoiceNo+"' and spec='PAYMENT' and customer_id='"+customerID+"' and date between '"+beginDate+"' and '"+endDate+"'");
                                while(rset1.next()){
                                    amountPaid=Double.parseDouble(rset1.getObject(1).toString());
                                    phrase=new Phrase(dbObject.getDBObject(format.Format2Currency(rset1.getObject(1).toString()), "0.00"), pFontHeader1);
                                    table.addCell(phrase);

                                    balance=total-amountPaid;
                                    phrase=new Phrase(format.Format2Currency(String.valueOf(balance)), pFontHeader1);
                                    table.addCell(phrase);
                                }
                                count++;
                            }
                            
                            table.getDefaultCell().setColspan(9);
                            phrase=new Phrase(" ");
                            table.addCell(phrase);
                            
                            
                            
                            ResultSet rs22=SQLHelper.getResultset(connectDB, "select sum(debit-credit) from debtors_ledger where customer_id='"+customerID+"' "
                                    + " and date between '"+beginDate+"' and '"+endDate+"'");
                            while(rs22.next()){
                                table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
                                phrase=new Phrase("Sub Total Owed:     "+dbObject.getDBObject(format.Format2Currency(rs22.getObject(1).toString()), "-"), pFontHeader2);
                                table.addCell(phrase);
                            }
                        }
                        
                        
                            
                            
                            table.getDefaultCell().setColspan(9);
                            phrase=new Phrase(" ");
                            table.addCell(phrase);
                            
                            
                            
                            ResultSet rs22=SQLHelper.getResultset(connectDB, "select sum(debit-credit) from debtors_ledger d, customers_registration c "
                                    + "where d.customer_id=c.customer_id and names ilike '%"+customerName+"%' and date between '"+beginDate+"' and '"+endDate+"'");
                            while(rs22.next()){
                                table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
                                phrase=new Phrase("Total Amount Owed:     "+dbObject.getDBObject(format.Format2Currency(rs22.getObject(1).toString()), "-"), pFontHeader2);
                                table.addCell(phrase);
                            }
                             docPdf.add(table);
                            
                        
            
                        docPdf.close();
                         deskTop.open(tempFile);

                       
        } catch(java.io.IOException IOexec) {
            
            javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), IOexec.getMessage());
            
        } catch (DocumentException | SQLException ex) {
             Logger.getLogger(DebtorStatementPdf.class.getName()).log(Level.SEVERE, null, ex);
         }   
    }     
}