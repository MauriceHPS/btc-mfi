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
public class PettyCashExpensingSummaryPdf implements java.lang.Runnable{
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
    
    public static java.sql.Connection connect = null;
    
    public java.lang.String dbUserName = null;
    
    org.netbeans.lib.sql.pool.PooledConnectionSource pConnDB = null;
    
    boolean threadCheck = true;
    
    java.lang.Thread threadSample;
    
    com.lowagie.text.Font pFontHeader = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
    com.lowagie.text.Font pFontHeader1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL);
    com.lowagie.text.Font pFontHeader4 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD);
   
    java.lang.Runtime rtThreadSample = java.lang.Runtime.getRuntime();
    java.lang.Process prThread;
    AMSUtility label = new AMSUtility();
    Format2Currency format;
    Date beginDate, endDate;
    
    public void PettyCashExpensingSummaryPdf(Date begin, Date end) {
        
        connect = ConnectionDB.getInstance().getCon();
        beginDate=begin;
        endDate=end;
        
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
            
            try {
                
                try {
                    
                    com.lowagie.text.pdf.PdfWriter.getInstance(docPdf, new java.io.FileOutputStream(tempFile));
//                    ReportUtil.addCenteredTitlePage(docPdf, connect);
                    
                    String date = null;
                    try {
                      java.sql.Statement st3 = connect.createStatement();
                        java.sql.Statement st4 = connect.createStatement();
                        
                        java.sql.ResultSet rset4 = st4.executeQuery("SELECT date(now()) as Date");
                        
                        while(rset4.next())
                            date = rset4.getObject(1).toString();
                        
                    } catch(java.sql.SQLException SqlExec) {
                        
                        javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), SqlExec.getMessage());
                        
                    }
                    
                    com.lowagie.text.HeaderFooter footer = new com.lowagie.text.HeaderFooter(new Phrase("Petty Cash Expensing Summary- Page: "), true);// FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA, 12, Font.BOLDITALIC,java.awt.Color.blue));
                    
                    docPdf.setFooter(footer);
                    
                    
                    docPdf.open();
                    
                    ReportUtil.addCenteredTitlePage(docPdf, connect);
                    try {
                        
                        
                        com.lowagie.text.pdf.PdfPTable table = new com.lowagie.text.pdf.PdfPTable(9);
                        
                        int headerwidths[] = {5, 8, 8, 30, 15, 8, 8, 8, 8};
                        
                        table.setWidths(headerwidths);
                        
                        table.setWidthPercentage((100));
                        table.getDefaultCell().setBorder(Rectangle.BOTTOM);
                        
                        Phrase phrase; 
                        
                        table.getDefaultCell().setColspan(9);
                        
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setBorder(PdfCell.TOP | PdfCell.BOTTOM | PdfCell.LEFT | PdfCell.RIGHT);
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_CENTER);
                        phrase=new Phrase("Petty Cash Expensing Summary", pFontHeader4);
                        table.addCell(phrase);                        
                        
                        
                        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM);
                        
                        table.getDefaultCell().setColspan(9);
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_CENTER);
                        phrase=new Phrase("Period Selected: From "+dateFormat.format(beginDate)+" To "+dateFormat.format(endDate), pFontHeader4);
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
                        phrase = new Phrase("Printed On  :" +date , pFontHeader);
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                        
                        double initialDisbursement=0.00;
                        
                        table.getDefaultCell().setColspan(1);
                        
                        phrase=new Phrase("#", pFontHeader);
                        table.addCell(phrase);
                        
                        phrase=new Phrase("Req No.", pFontHeader);
                        table.addCell(phrase);
                        
                        phrase=new Phrase("Exp. ID", pFontHeader);
                        table.addCell(phrase);
                        
                        phrase=new Phrase("Desc", pFontHeader);
                        table.addCell(phrase);
                        
                        phrase=new Phrase("A/C Code", pFontHeader);
                        table.addCell(phrase);
                        
                        phrase=new Phrase("Amount", pFontHeader);
                        table.addCell(phrase);
                        
                        phrase=new Phrase("Date", pFontHeader);
                        table.addCell(phrase);
                        
                        phrase=new Phrase("Approved", pFontHeader);
                        table.addCell(phrase);
                        
                        phrase=new Phrase("Rejected", pFontHeader);
                        table.addCell(phrase);
                        
                        
                        int count=1;
                        
                        //all the expenses inputted under the requisition no above
                        ResultSet rss1=SQLHelper.getResultset(connect, "select requisition_no, expensing_id, description, accounting_code, amount, entry_time::date, "
                        + "approved, rejection from petty_cash_expensing where entry_time::date between '"+beginDate+"' and '"+endDate+"' order by entry_time asc ");
                        while(rss1.next()){
                            phrase=new Phrase(""+count+"", pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase(dbObject.getDBObject(rss1.getObject(1), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase(dbObject.getDBObject(rss1.getObject(2), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase(dbObject.getDBObject(rss1.getObject(3), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase(dbObject.getDBObject(format.Format2Currency(rss1.getObject(5).toString()), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase(dbObject.getDBObject(rss1.getObject(4), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase(dbObject.getDBObject(rss1.getObject(6), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase(dbObject.getDBObject(rss1.getObject(7), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase(dbObject.getDBObject(rss1.getObject(8), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            count++;
                        }
                        
                        table.getDefaultCell().setColspan(9);
                        
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
                        
                        double expensed=0.00;
                        
                        //initialDisbursement
                        ResultSet rss=SQLHelper.getResultset(connect, "select case when sum(amount) is null then 0.00 else sum(amount) end as case1 from petty_cash_requisition "
                                + "where date::date between '"+beginDate+"' and '"+endDate+"' and approved=true ");
                        while(rss.next()){
                            initialDisbursement=rss.getDouble(1);
                        }
                        
                        phrase=new Phrase("INITIAL DISBURSEMENT:     "+format.Format2Currency(String.valueOf(initialDisbursement)), pFontHeader4);
                        table.addCell(phrase);
                        
                        
                        ResultSet rss2=SQLHelper.getResultset(connect, "select case when sum(amount) is null then 0.00 else sum(amount) end as case1 from petty_cash_expensing "
                                + "where entry_time::date between '"+beginDate+"' and '"+endDate+"' and approved=true ");
                        while(rss2.next()){
                            expensed=rss2.getDouble(1);
                        }
                        
                        phrase=new Phrase("APPROVED EXPENSE:     "+format.Format2Currency(String.valueOf(expensed)), pFontHeader4);
                        table.addCell(phrase);
                        
                        
                        double balance=initialDisbursement-expensed;
                        
                        phrase=new Phrase("BALANCE:     "+format.Format2Currency(String.valueOf(balance)), pFontHeader4);
                        table.addCell(phrase);
                        
                        //end
                        docPdf.add(table);
                        
                      
                    } catch(com.lowagie.text.BadElementException BadElExec) {
                        
                        javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), BadElExec.getMessage());
                        
                    }
                    
                } catch(java.io.FileNotFoundException fnfExec) {
                    
                    javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), fnfExec.getMessage());
                    
                } catch (SQLException | BadElementException ex) {
                    Logger.getLogger(PettyCashExpensingSummaryPdf.class.getName()).log(Level.SEVERE, null, ex);
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

