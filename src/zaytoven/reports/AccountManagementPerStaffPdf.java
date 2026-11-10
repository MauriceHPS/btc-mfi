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
public class AccountManagementPerStaffPdf implements java.lang.Runnable{
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
    String staffID;
    Format2Currency format;
    public void AccountManagementPerStaffPdf(String staff) {
        
        connectDB =ConnectionDB.getInstance().getCon();
        staffID=staff;
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
            
            
                    
                    com.lowagie.text.pdf.PdfWriter.getInstance(docPdf, new java.io.FileOutputStream(tempFile));
//                    ReportUtil.addCenteredTitlePage(docPdf, connectDB);
                    
                    String date = null;
                   
                      java.sql.Statement st3 = connectDB.createStatement();
                        java.sql.Statement st4 = connectDB.createStatement();
                        
                        java.sql.ResultSet rset4 = st4.executeQuery("SELECT date(now()) as Date");
                        
                        while(rset4.next())
                            date = rset4.getObject(1).toString();
                        
                    
                    
                    com.lowagie.text.HeaderFooter footer = new com.lowagie.text.HeaderFooter(new Phrase("Account Management Per Staff- Page: "), true);// FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA, 12, Font.BOLDITALIC,java.awt.Color.blue));
                    
                    docPdf.setFooter(footer);
                    
                    
                    docPdf.open();
                    
                    ReportUtil.addCenteredTitlePage(docPdf, connectDB);
                    
                        
                        
                        com.lowagie.text.pdf.PdfPTable table = new com.lowagie.text.pdf.PdfPTable(6);
                        
                        int headerwidths[] = {7, 11, 47, 15, 15, 15};
                        
                        table.setWidths(headerwidths);
                        
                        table.setWidthPercentage((100));
                        table.getDefaultCell().setBorder(Rectangle.BOTTOM);
                        
                        table.getDefaultCell().setColspan(6);
                        
                        
                        Phrase phrase; 
                        
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setBorder(PdfCell.TOP | PdfCell.BOTTOM | PdfCell.LEFT | PdfCell.RIGHT);
                        
                        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM);
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_CENTER);
                        phrase=new Phrase("ACCOUNTS' MANAGEMENT PER STAFF", pFontHeader2);
                        table.addCell(phrase);
                        
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
                        phrase = new Phrase("Printed On  :" +date , pFontHeader);
                        table.addCell(phrase);
                        
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
                        System.err.println("Staff ID:   "+staffID);
                        
                         table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_CENTER);
                        phrase=new Phrase("STAFF ID:   "+staffID, pFontHeader2);
                        table.addCell(phrase);
                        
                        String staffName=null;
                        ResultSet rss=SQLHelper.getResultset(connectDB, "select names from staff_registration where staff_id='"+staffID+"'");
                        while(rss.next()){
                            staffName=rss.getString(1);
                        }
                        
                        System.err.println("Staff Name: "+staffName);
                        
                        phrase=new Phrase("Staff Name:  "+staffName, pFontHeader2);
                        table.addCell(phrase);
                        
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
                        
                        table.getDefaultCell().setColspan(1);
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                        
                        phrase = new Phrase("#",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Loan No",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Names",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Amount Applied",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Balance",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Application Date",pFontHeader);
                        table.addCell(phrase);
                        
                       table.getDefaultCell().setColspan(1);
                       
                       table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                       
                            int count=1;
                            
                            double balance=0.00, masterBalance=0.00;
                            
                            String loanNo=null;
                            
                            Date applicationDate=null;
                                
                            ResultSet rset = SQLHelper.getResultset(connectDB, "select a.loan_no, names, amount_applied,  application_date "
                                    + "from loans_approval a, loan_registration l, members_register m where l.loan_no=a.loan_no and  l.member_no=m.member_no "
                                    + "and manager_id='"+staffID+"' and a.loan_no not in (select other_identifier from debtors_ledger where paid=true group by 1 "
                                            + "having sum(debit-credit)<=0.00)");

                            while (rset.next()) {
                                loanNo=rset.getString(1);
                                
                                applicationDate=rset.getDate(4);
                                
                                System.err.println("Loan No:    "+loanNo);
                                
                                System.err.println("Application Date:   "+applicationDate);
                                
                                phrase = new Phrase(""+count+"   ", pFontHeader1);
                                table.addCell(phrase);

                                phrase = new Phrase(dbObject.getDBObject(rset.getObject(1), "-"),pFontHeader1);
                                table.addCell(phrase);

                                phrase = new Phrase(dbObject.getDBObject(rset.getObject(2), "-"),pFontHeader1);
                                table.addCell(phrase);
                                
                                phrase = new Phrase(dbObject.getDBObject(format.Format2Currency(rset.getObject(3).toString()), "-"),pFontHeader1);
                                table.addCell(phrase);
                                
                                ResultSet rs=SQLHelper.getResultset(connectDB, "SELECT CASE  WHEN sum(debit-credit) IS NULL then 0.00 "
                                        + "else sum(debit-credit) END as case1 FROM debtors_ledger WHERE other_identifier='"+loanNo+"'");
                                while(rs.next()){
                                    balance=rs.getDouble(1);
                                    masterBalance+=balance;
                                    
                                    System.err.println("Balance:    "+balance);
                                    
                                    phrase = new Phrase(dbObject.getDBObject(format.Format2Currency(String.valueOf(balance)), "-"),pFontHeader1);
                                    table.addCell(phrase);
                                }
                                
                                
                                
                                phrase = new Phrase(dbObject.getDBObject(applicationDate, "-"),pFontHeader1);
                                table.addCell(phrase);
                                
                                count++;
                            }
                            
                            table.getDefaultCell().setColspan(6);
                            
                            phrase=new Phrase(" ");
                            table.addCell(phrase);
                            
                            table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
                            
                            phrase = new Phrase("AMOUNT RECEIVABLE: "+dbObject.getDBObject(format.Format2Currency(String.valueOf(masterBalance)), "-"),pFontHeader2);
                            table.addCell(phrase);
                            
                            docPdf.add(table);
            
                        docPdf.close();
                         deskTop.open(tempFile);

        } catch(java.io.IOException IOexec) {
            
            javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), IOexec.getMessage());
            
        } catch (DocumentException | SQLException ex) {
             Logger.getLogger(AccountManagementPerStaffPdf.class.getName()).log(Level.SEVERE, null, ex);
         }   
    }     
}