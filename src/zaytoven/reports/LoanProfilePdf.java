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
public class LoanProfilePdf implements java.lang.Runnable{
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
    
    com.lowagie.text.Font pFontHeader = FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.BOLD);
    com.lowagie.text.Font pFontHeader1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL);
    com.lowagie.text.Font pFontHeader4 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD);
   
    java.lang.Runtime rtThreadSample = java.lang.Runtime.getRuntime();
    java.lang.Process prThread;
    AMSUtility label = new AMSUtility();
    Format2Currency format;
    String loanNo;
    
    public void LoanProfilePdf(String loan) {
        
        connect = ConnectionDB.getInstance().getCon();
        loanNo=loan;
        
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
            com.lowagie.text.Document docPdf = new com.lowagie.text.Document(PageSize.A4);
            
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
                    
                    com.lowagie.text.HeaderFooter footer = new com.lowagie.text.HeaderFooter(new Phrase("Loan Profile- Page: "), true);// FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA, 12, Font.BOLDITALIC,java.awt.Color.blue));
                    
                    docPdf.setFooter(footer);
                    
                    
                    docPdf.open();
                    
                    ReportUtil.addCenteredTitlePage(docPdf, connect);
                    try {
                        
                        
                        com.lowagie.text.pdf.PdfPTable table = new com.lowagie.text.pdf.PdfPTable(2);
                        
                        int headerwidths[] = {50, 50};
                        
                        table.setWidths(headerwidths);
                        
                        table.setWidthPercentage((100));
                        table.getDefaultCell().setBorder(Rectangle.BOTTOM);
                        
                        Phrase phrase; 
                        
                        table.getDefaultCell().setColspan(2);
                        
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setBorder(PdfCell.TOP | PdfCell.BOTTOM | PdfCell.LEFT | PdfCell.RIGHT);
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_CENTER);
                        phrase=new Phrase("Loan Profile", pFontHeader4);
                        table.addCell(phrase);                        
                        
                        
                        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM);
                        
                        table.getDefaultCell().setColspan(1);
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                        phrase=new Phrase("Loan No:  "+loanNo, pFontHeader4);
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
                        phrase = new Phrase("Printed On  :" +date , pFontHeader);
                        table.addCell(phrase);
                        
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                        ResultSet rs=SQLHelper.getResultset(connect, "SELECT l.member_no, names, application_date, loan_type, amount_applied, duration_months, \n" +
                        "annual_interest_rate, processing_rate, transaction_fees,annual_payments,monthly_installment, \n" +
                        "reason_for_loan, current_exposure, l.business_type, l.business_duration, sales_turnover, monthly_overheads, gross_profit, \n" +
                        "fixed_assets, no_of_employees, \n" +
                        " guarantor_name1, guarantor_idno1, guarantor_pinno1, guarantor_relationship1, guarantor_postal_address1, guarantor_physical_address1, \n" +
                        "guarantor_phone_no1, guarantor_email_address1, guarantor_security_item1, guarantor_estimate_value1, \n" +
                        "guarantor_attached_copy_path1, appraisal_recommendations, decision, approved\n" +
                        " FROM public.loan_registration l, members_register m where m.member_no=l.member_no and loan_no='"+loanNo+"'");
                        while(rs.next()){
                           table.getDefaultCell().setColspan(2);
                           
                           phrase=new Phrase(" ");
                           table.addCell(phrase);
                           
                           table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_CENTER);
                           
                           phrase=new Phrase("MEMBER AND LOAN INFORMATION", pFontHeader);
                           table.addCell(phrase);
                           
                           table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                           
                           table.getDefaultCell().setColspan(1);
                           
                            phrase=new Phrase("Member No:    "+dbObject.getDBObject(rs.getObject(1), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase("Member Name:    "+dbObject.getDBObject(rs.getObject(2), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase("Application Date:    "+dbObject.getDBObject(rs.getObject(3), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase("Loan Type:    "+dbObject.getDBObject(rs.getObject(4), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase("Principal:    "+dbObject.getDBObject(format.Format2Currency(rs.getObject(5).toString()), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase("Duration (Months):    "+dbObject.getDBObject(rs.getObject(6), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase("Interest Rate (%):    "+dbObject.getDBObject(format.Format2Currency(rs.getObject(7).toString()), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase("Processing Rate (%):    "+dbObject.getDBObject(format.Format2Currency(rs.getObject(8).toString()), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase("Transaction Fee:    "+dbObject.getDBObject(rs.getObject(9), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase("No of Annual Payments:    "+dbObject.getDBObject(rs.getObject(10), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            table.getDefaultCell().setColspan(2);
                            phrase=new Phrase("Scheduled Payment:    "+dbObject.getDBObject(format.Format2Currency(rs.getObject(11).toString()), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase("Reason for Loan:    "+dbObject.getDBObject(rs.getObject(12), "-"), pFontHeader);
                            table.addCell(phrase);
                            
                            phrase=new Phrase("Current Exposure    "+dbObject.getDBObject(format.Format2Currency(rs.getObject(13).toString()), "-"), pFontHeader);
                            table.addCell(phrase);
                            
                            phrase=new Phrase(" ");
                           table.addCell(phrase);
                           
                           table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_CENTER);
                           
                            phrase=new Phrase("BUSINESS DETAILS", pFontHeader);
                            table.addCell(phrase);
                            
                            table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                            
                            table.getDefaultCell().setColspan(1);
                            
                            phrase=new Phrase("Business Type:    "+dbObject.getDBObject(rs.getObject(14), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase("Business Duration:    "+dbObject.getDBObject(rs.getObject(15), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase("Sales Turnover:    "+dbObject.getDBObject(format.Format2Currency(rs.getObject(16).toString()), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase("Monthly Overheads:    "+dbObject.getDBObject(format.Format2Currency(rs.getObject(17).toString()), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase("Gross Profit:    "+dbObject.getDBObject(format.Format2Currency(rs.getObject(18).toString()), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase("Fixed Assets:    "+dbObject.getDBObject(rs.getObject(19), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            table.getDefaultCell().setColspan(2);
                            phrase=new Phrase("No of Employees:    "+dbObject.getDBObject(rs.getObject(20), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase(" ");
                            table.addCell(phrase);
                            
                            table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_CENTER);
                            
                            phrase=new Phrase("GUARANTOR DETAILS", pFontHeader);
                            table.addCell(phrase);
                            
                            table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                            
                            table.getDefaultCell().setColspan(1);
                            
                            phrase=new Phrase("Name:    "+dbObject.getDBObject(rs.getObject(21), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase("ID NO:    "+dbObject.getDBObject(rs.getObject(22), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase("PIN NO:    "+dbObject.getDBObject(rs.getObject(23), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase("Relationship:    "+dbObject.getDBObject(rs.getObject(24), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase("Postal Address:    "+dbObject.getDBObject(rs.getObject(25), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase("Physical Address:    "+dbObject.getDBObject(rs.getObject(26), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase("Phone No:    "+dbObject.getDBObject(rs.getObject(27), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase("Email Address:    "+dbObject.getDBObject(rs.getObject(28), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase("Security Item:    "+dbObject.getDBObject(rs.getObject(29), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase("Estimated Value:    "+dbObject.getDBObject(format.Format2Currency(rs.getObject(30).toString()), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            table.getDefaultCell().setColspan(2);
                            
                            phrase=new Phrase(" ");
                           table.addCell(phrase);
                            
                            phrase=new Phrase("Attached Copies Directory:    "+dbObject.getDBObject(rs.getObject(31), "-"), pFontHeader);
                            table.addCell(phrase);
                            
                            phrase=new Phrase("Appraisal Comments:    "+dbObject.getDBObject(rs.getObject(32), "-"), pFontHeader);
                            table.addCell(phrase);
                            
                            phrase=new Phrase("Approving Authority Decision:    "+dbObject.getDBObject(rs.getObject(33), "-"), pFontHeader);
                            table.addCell(phrase);
                            
                            phrase=new Phrase("Approved?    "+dbObject.getDBObject(rs.getObject(34), "-"), pFontHeader);
                            table.addCell(phrase);
                        }
                        
                       
                        docPdf.add(table);
                      
                    } catch(com.lowagie.text.BadElementException BadElExec) {
                        
                        javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), BadElExec.getMessage());
                        
                    }
                    
                } catch(java.io.FileNotFoundException fnfExec) {
                    
                    javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), fnfExec.getMessage());
                    
                } catch (SQLException | BadElementException ex) {
                    Logger.getLogger(LoanProfilePdf.class.getName()).log(Level.SEVERE, null, ex);
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

