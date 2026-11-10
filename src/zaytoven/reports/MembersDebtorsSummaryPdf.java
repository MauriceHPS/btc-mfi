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


public class MembersDebtorsSummaryPdf implements java.lang.Runnable{
  
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
    Format2Currency format;
    public void MembersDebtorsSummaryPdf() {
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
                    
                    com.lowagie.text.HeaderFooter footer = new com.lowagie.text.HeaderFooter(new Phrase("Outstanding Loan Balances Summary - Page: "), true);// FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA, 12, Font.BOLDITALIC,java.awt.Color.blue));
                    
                    docPdf.setFooter(footer);
                    
                    
                    docPdf.open();
                    
                    ReportUtil.addCenteredTitlePage(docPdf, connectDB);
                    try {
                        
                        
                        com.lowagie.text.pdf.PdfPTable table = new com.lowagie.text.pdf.PdfPTable(9);
                        
                        int headerwidths[] = {5, 7, 20, 10, 10, 12, 12, 12, 12};
                        
                        table.setWidths(headerwidths);
                        
                        table.setWidthPercentage((100));
                        table.getDefaultCell().setBorder(Rectangle.BOTTOM);
                        
                        
                        
                        Phrase phrase; 

                        table.getDefaultCell().setColspan(9);
                        
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
                        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM);
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_CENTER);
                        
                        table.getDefaultCell().setBorder(PdfCell.TOP | PdfCell.BOTTOM | PdfCell.LEFT | PdfCell.RIGHT);
                        
                        phrase=new Phrase("OUTSTANDING LOAN BALANCES SUMMARY", pFontHeader2);
                        table.addCell(phrase);
                        
                        
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
                        phrase = new Phrase("Printed On  :" +date , pFontHeader);
                        table.addCell(phrase);
                        
                        
                        table.getDefaultCell().setColspan(1);
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT); 
                        
                        phrase = new Phrase("#",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Member No",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Name",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Loan No.",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Loan Type",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Principal",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Principal \n(Inc. Interest)",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Principal Recovered",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Balance",pFontHeader);
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setBackgroundColor(java.awt.Color.WHITE);
                       
                        double principal=0.00, penalties=0.00, paid=0.00, principalWithPenalties=0.00, balance=0.00;
                        
                        try {
                            java.sql.ResultSet rset;
                            rset=SQLHelper.getResultset(connectDB, "select sum(credit) from loan_penalties ");
                            while(rset.next()){
                                penalties=rset.getDouble(1);
                            }
                            
                            System.err.println("Penalties:  "+penalties);
                            
                            rset= SQLHelper.getResultset(connectDB, "select d.member_no, names, other_identifier,  loan_type, amount_applied as principal,  "
                                    + "sum(debit) as amount_payable, sum(d.credit) as paid, sum(debit-credit)  as balance from members_register m, loan_registration l, "
                                    + "debtors_ledger d where paid=false and m.member_no=l.member_no "
                                    + "and m.member_no=d.member_no  and l.member_no=d.member_no and l.loan_no=other_identifier "
                                    + " group by 1, 2, 3, 4, 5 having sum(debit-credit)>0 order by 8 desc");
                             
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
                                
                                //principal
                                principal=rset.getDouble(5);
                                
                                System.err.println("Principal:  "+principal);
                                
                                phrase = new Phrase(dbObject.getDBObject(format.Format2Currency(rset.getObject(5).toString()), "-"),pFontHeader1);
                                table.addCell(phrase);
                                
                                //payables
                                
                                
                                phrase = new Phrase(dbObject.getDBObject(format.Format2Currency(rset.getObject(6).toString()), "-"),pFontHeader1);
                                table.addCell(phrase);
                                
                                
                                phrase = new Phrase(dbObject.getDBObject(format.Format2Currency(rset.getObject(7).toString()), "-"),pFontHeader1);
                                table.addCell(phrase);
                                
                                
                                phrase = new Phrase(dbObject.getDBObject(format.Format2Currency(rset.getObject(8).toString()), "-"),pFontHeader1);
                                table.addCell(phrase);
                                
                                count++;
                            }
                            
//                            phrase=new Phrase(" ");
//                            table.addCell(phrase);
                        
                             table.getDefaultCell().setColspan(9);
                             
                             phrase=new Phrase(" ");
                            table.addCell(phrase);
                             
                             table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
                             
                             ResultSet rss1=SQLHelper.getResultset(connectDB, "select sum(debit-credit) from general_ledger  "
                                     + "where accounting_code='C002-007-00403' ");
                             
                             while(rss1.next()){
                                 balance=rss1.getDouble(1);
                            }
                            
                            phrase=new Phrase("OVERALL OUTSTANDING BALANCE:    "+format.Format2Currency(String.valueOf(balance)), pFontHeader2);
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
                    Logger.getLogger(MembersDebtorsSummaryPdf.class.getName()).log(Level.SEVERE, null, ex);
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