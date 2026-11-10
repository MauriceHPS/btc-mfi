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


public class InvestorsSummaryPdf implements java.lang.Runnable{
  
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
    Date beginDate=null;
    Date endDate=null;
    Format2Currency format;
    public void InvestorsSummaryPdf(Date begin, Date end) {
        beginDate=begin;
        endDate=end;
        dbObject = new DBObject();
        connectDB=ConnectionDB.getInstance().getCon();
        
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
                        
//                        com.lowagie.text.HeaderFooter headerFoter = new com.lowagie.text.HeaderFooter(new Phrase(""+compName+""),false);// FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA, 14, Font.BOLDITALIC,java.awt.Color.blue)));
//                        headerFoter.setAlignment(com.lowagie.text.HeaderFooter.ALIGN_CENTER);
//                        headerFoter.setRight(5);
//                        docPdf.setHeader(headerFoter);
                        
                    } catch(java.sql.SQLException SqlExec) {
                        
                        javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), SqlExec.getMessage());
                        
                    }
                    
                    com.lowagie.text.HeaderFooter footer = new com.lowagie.text.HeaderFooter(new Phrase("Investors' Summary - Page: "), true);// FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA, 12, Font.BOLDITALIC,java.awt.Color.blue));
                    
                    docPdf.setFooter(footer);
                    
                    
                    docPdf.open();
                    
                    ReportUtil.addCenteredTitlePage(docPdf, connectDB);
                    try {
                        
                        
                        com.lowagie.text.pdf.PdfPTable table = new com.lowagie.text.pdf.PdfPTable(8);
                        
                        int headerwidths[] = {5, 10, 10, 25, 13, 12, 13, 12};
                        
                        table.setWidths(headerwidths);
                        
                        table.setWidthPercentage((100));
                        table.getDefaultCell().setBorder(Rectangle.BOTTOM);
                        
                        
                        
                        Phrase phrase; 

                        table.getDefaultCell().setColspan(8);
                        
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
                        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM);
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_CENTER);
                        
                        table.getDefaultCell().setBorder(PdfCell.TOP | PdfCell.BOTTOM | PdfCell.LEFT | PdfCell.RIGHT);
                        
                        phrase=new Phrase("INVESTORS' SUMMARY", pFontHeader2);
                        table.addCell(phrase);
                        
                        phrase=new Phrase("PERIOD SELECTED: From "+dateFormat.format(beginDate)+" To "+dateFormat.format(endDate), pFontHeader2);
                        table.addCell(phrase);
                        
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
                        phrase = new Phrase("Printed On  :" +date , pFontHeader);
                        table.addCell(phrase);
                        
                        
                        table.getDefaultCell().setColspan(1);
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT); 
                        
                        phrase = new Phrase("#",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Loan No.",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Creditor ID",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Creditor Name",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Principal",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Interest Payable",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Interest Paid",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Balance On Interest",pFontHeader);
                        table.addCell(phrase);
                        
                        
                        table.getDefaultCell().setBackgroundColor(java.awt.Color.WHITE);
                        
                        int count=1;
                        String creditorID, investmentID=null;
                        double principal=0.00, interestPayable=0.00, interestPaid=0.00, balanceOnInterest=0.00, cumInterestPayable=0.00, 
                                cumPrincipal=0.00, cumInterestPaid=0.00, cumBalanceOnInterest=0.00;
                        try {
                            ResultSet rset= SQLHelper.getResultset(connectDB, "select distinct(identifier) from creditors_ledger "
                                    + "where  date::date between '"+beginDate+"' and '"+endDate+"' and fully_paid=false ");
                            while (rset.next()) {
                                investmentID=rset.getString(1);
                                
                                System.err.println("Investment ID:  "+investmentID);
                                
                                ResultSet rset1=SQLHelper.getResultset(connectDB, "select distinct(creditor_id) from creditors_ledger "
                                        + "where identifier='"+investmentID+"' and date::date BETWEEN '"+beginDate+"' AND '"+endDate+"'");
                                while(rset1.next()){
                                    creditorID=rset1.getString(1);
                                    
                                    System.err.println("Creditor ID:    "+creditorID);
                                    
                                    phrase=new Phrase(""+count+"", pFontHeader1);
                                    table.addCell(phrase);
                                    
                                    phrase=new Phrase(""+investmentID+"", pFontHeader1);
                                    table.addCell(phrase);
                                    
                                    phrase=new Phrase(""+creditorID+"", pFontHeader1);
                                    table.addCell(phrase);
                                    
                                    
                                    
                                    //credit name, principal and interest
                                    ResultSet rset2=SQLHelper.getResultset(connectDB, "select distinct(l.creditor_id), supplier_names, principal, "
                                    + "(principal*(interest_rate/100)*duration_months) as interest from investments_trail i, creditors_register r, creditors_ledger l \n" +
                                    "where id=l.identifier and i.creditor_id=supplier_id and i.creditor_id=l.creditor_id and supplier_id=l.creditor_id "
                                    + "and l.creditor_id='"+creditorID+"' and l.identifier='"+investmentID+"' and l.date::date between '"+beginDate+"' and '"+endDate+"' ");
                                    while(rset2.next()){
                                        principal=rset2.getDouble(3);
                                        cumPrincipal+=principal;
                                        
                                        interestPayable=rset2.getDouble(4);
                                        cumInterestPayable+=interestPayable;
                                        
                                        phrase=new Phrase(dbObject.getDBObject(rset2.getObject(2), "-"), pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        phrase=new Phrase(dbObject.getDBObject(format.Format2Currency(String.valueOf(rset2.getDouble(3))), "-"), pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        phrase=new Phrase(dbObject.getDBObject(format.Format2Currency(String.valueOf(rset2.getDouble(4))), "-"), pFontHeader1);
                                        table.addCell(phrase);
                                    
                                        //interest paid
                                        ResultSet rset3=SQLHelper.getResultset(connectDB, "select case when sum(debit) is null then 0.00 else sum(debit) end as case1 "
                                                + "from creditors_ledger where creditor_id='"+creditorID+"' "
                                                + "and identifier='"+investmentID+"' and description='REDUCING THE AMOUNT PAYABLE'  "
                                                + "and date::date between '"+beginDate+"' and '"+endDate+"'");
                                        while(rset3.next()){
                                            interestPaid=rset3.getDouble(1);
                                            cumInterestPaid+=interestPaid;

                                            phrase=new Phrase(dbObject.getDBObject(format.Format2Currency(String.valueOf(rset3.getDouble(1))), "-"), pFontHeader1);
                                            table.addCell(phrase);
                                        }
                                        balanceOnInterest=interestPayable-interestPaid;
                                        cumBalanceOnInterest+=balanceOnInterest;

                                        phrase=new Phrase(dbObject.getDBObject(format.Format2Currency(String.valueOf(balanceOnInterest)), "-"), pFontHeader1);
                                        table.addCell(phrase);

                                        count++;
                                    }
                                }
                            }
                            
//                            phrase=new Phrase(" ");
//                            table.addCell(phrase);
                        
//                             table.getDefaultCell().setColspan(8);
//                             
//                             phrase=new Phrase(" ");
//                            table.addCell(phrase);
                            
                             
//                             table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
                            table.getDefaultCell().setColspan(1);
                             
                             phrase=new Phrase(" ");
                            table.addCell(phrase);
                            
                            phrase=new Phrase(" ");
                            table.addCell(phrase);
                            
                            phrase=new Phrase(" ");
                            table.addCell(phrase);
                            
                            phrase=new Phrase(" ");
                            table.addCell(phrase);
                             
                            phrase=new Phrase(format.Format2Currency(String.valueOf(cumPrincipal)), pFontHeader2);
                            table.addCell(phrase);
                            
                            phrase=new Phrase(format.Format2Currency(String.valueOf(cumInterestPayable)), pFontHeader2);
                            table.addCell(phrase);
                             
                             phrase=new Phrase(format.Format2Currency(String.valueOf(cumInterestPaid)), pFontHeader2);
                            table.addCell(phrase);
                            
                            phrase=new Phrase(format.Format2Currency(String.valueOf(cumBalanceOnInterest)), pFontHeader2);
                            table.addCell(phrase);
                            
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
                    Logger.getLogger(InvestorsSummaryPdf.class.getName()).log(Level.SEVERE, null, ex);
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