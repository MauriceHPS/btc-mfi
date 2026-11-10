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

public class LoanStatementBreakdownPdf implements java.lang.Runnable{
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


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
//    Date beginDate, endDate;
    String memberNo, loanNo;
    Format2Currency format;
    public void LoanStatementBreakdownPdf(String member, String loan) {
        
        connectDB =ConnectionDB.getInstance().getCon();
//        beginDate=begin;
//        endDate=end;
        memberNo=member;
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
        
              
        
    }
    
    public void generatePdf() {
       
        try {
            
            java.io.File tempFile = java.io.File.createTempFile("REP"+AMSUtility.getDateLable()+"_", ".pdf");
            
            tempFile.deleteOnExit();
            
            java.lang.Runtime rt = java.lang.Runtime.getRuntime();
            
            java.lang.String debitTotal = null;
            
            java.lang.String creditTotal = null;
            com.lowagie.text.Document docPdf = new com.lowagie.text.Document(PageSize.A4);
            
                
                    
                    com.lowagie.text.pdf.PdfWriter.getInstance(docPdf, new java.io.FileOutputStream(tempFile));
//                    ReportUtil.addCenteredTitlePage(docPdf, connectDB);
                    
                    String date = null;
                      java.sql.Statement st3 = connectDB.createStatement();
                        java.sql.Statement st4 = connectDB.createStatement();
                        
                        java.sql.ResultSet rset4 = st4.executeQuery("SELECT date(now()) as Date");
                        
                        while(rset4.next())
                            date = rset4.getObject(1).toString();
                        
                    
                    
                    com.lowagie.text.HeaderFooter footer = new com.lowagie.text.HeaderFooter(new Phrase("Member's Loan Statement- Page: "), true);// FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA, 12, Font.BOLDITALIC,java.awt.Color.blue));
                    
                    docPdf.setFooter(footer);
                    
                    
                    docPdf.open();
                    
                    ReportUtil.addCenteredTitlePage(docPdf, connectDB);
                        
                        
                        com.lowagie.text.pdf.PdfPTable table = new com.lowagie.text.pdf.PdfPTable(2);
                        
                        int headerwidths[] = {50, 50};
                        
                        table.setWidths(headerwidths);
                        
                        table.setWidthPercentage((100));
                        table.getDefaultCell().setBorder(Rectangle.BOTTOM);
                        
                        table.getDefaultCell().setColspan(2);
                        
                        
                        Phrase phrase; 
                        
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setBorder(PdfCell.TOP | PdfCell.BOTTOM | PdfCell.LEFT | PdfCell.RIGHT);
                        
                        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM);
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_CENTER);
                        phrase=new Phrase("LOAN STATEMENT BREAKDOWN", pFontHeader2);
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setColspan(2);
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
                        phrase = new Phrase("Printed On  :" +date , pFontHeader);
                        table.addCell(phrase);
                        
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
                        double total=0.00, amountPaid=0.00, balance=0.00;
                        String loanNo=null;
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                        table.getDefaultCell().setColspan(1);
                        
                        double amountPayable=0.00;
                        ResultSet cus=SQLHelper.getResultset(connectDB, "select distinct(d.member_no), names, loan_no, "
                                + "amount_applied, payable_amount_with_processing, loan_type, duration_months from members_register m, loan_registration l, debtors_ledger d "
                                + "where paid=false and m.member_no=l.member_no and m.member_no=d.member_no and m.member_no=d.member_no "
                                + "and l.loan_no=other_identifier and d.member_no='"+memberNo+"' and other_identifier='"+this.loanNo+"'");
                        while(cus.next()){
                            
                            phrase=new Phrase("MEMBER NO: "+dbObject.getDBObject(cus.getObject(1), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase("MEMBER NAME:  "+dbObject.getDBObject(cus.getObject(2), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase("LOAN NO:  "+dbObject.getDBObject(cus.getObject(3), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            loanNo=cus.getString(3);
                            
                            phrase=new Phrase("LOAN AMOUNT:  "+dbObject.getDBObject(format.Format2Currency(cus.getObject(4).toString()), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase("AMOUNT PAYABLE:  "+dbObject.getDBObject(format.Format2Currency(cus.getObject(5).toString()), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            amountPayable=cus.getDouble(5);
                            
                            phrase=new Phrase("LOAN TYPE:  "+dbObject.getDBObject(cus.getObject(6), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            table.getDefaultCell().setColspan(2);
                            
                            phrase=new Phrase("DURATION:  "+dbObject.getDBObject(cus.getObject(7), "-"), pFontHeader1);
                            table.addCell(phrase);
                        }
                        
                        
                        
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
                        
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
                        docPdf.add(table);
                        
                        com.lowagie.text.pdf.PdfPTable table1 = new com.lowagie.text.pdf.PdfPTable(4);
                        
                        int headerwidths1[] = {10, 30, 30, 30};
                        
                        table1.setWidths(headerwidths1);
                        
                        table1.setWidthPercentage((100));
                        table1.getDefaultCell().setBorder(Rectangle.BOTTOM);
                        
                        table1.getDefaultCell().setColspan(1);
                        
                        table1.getDefaultCell().setBorder(PdfCell.TOP | PdfCell.BOTTOM | PdfCell.LEFT | PdfCell.RIGHT);
//                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                        phrase = new Phrase("#",pFontHeader);
                        table1.addCell(phrase);
                        
                        phrase = new Phrase("Payment ID",pFontHeader);
                        table1.addCell(phrase);
                        
                        phrase = new Phrase("Amount Paid",pFontHeader);
                        table1.addCell(phrase);
                        
                        phrase = new Phrase("Payment Date",pFontHeader);
                        table1.addCell(phrase);
                        
                       
                     
                            
                            java.sql.Statement st = connectDB.createStatement();
                            
                            java.sql.ResultSet rset = st.executeQuery("select receiving_payment_id, credit, date::date from debtors_ledger where  member_no='"+memberNo+"' "
                                    + "and other_identifier='"+loanNo+"' and receiving_payment_id is not null order by date desc");
                             
                            int count=1;
                             table1.getDefaultCell().setColspan(1);
                             table1.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                            
                             while (rset.next()) {
                                
                                phrase = new Phrase(""+count+"   ", pFontHeader1);
                                table1.addCell(phrase);
                                
                                phrase = new Phrase(dbObject.getDBObject(rset.getObject(1), "-"),pFontHeader1);
                                table1.addCell(phrase);
                                
                                phrase = new Phrase(dbObject.getDBObject(format.Format2Currency(rset.getObject(2).toString()), "-"),pFontHeader1);
                                table1.addCell(phrase);
                                
                                phrase = new Phrase(dbObject.getDBObject(rset.getObject(3), "-"),pFontHeader1);
                                table1.addCell(phrase);
                                
                                count++;
                            }
                            
                            table1.getDefaultCell().setColspan(4);
                            
                            phrase=new Phrase(" ");
                            table1.addCell(phrase);
                            
                            phrase=new Phrase(" ");
                            table1.addCell(phrase);
                            
                            table1.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
                            
                            double bal=0.00;
                            ResultSet rs22=SQLHelper.getResultset(connectDB, "select case when sum(credit) is null then 0.00 else sum(credit) end as case1 "
                                    + "from debtors_ledger where  member_no='"+memberNo+"' and other_identifier='"+loanNo+"' and receiving_payment_id is not null");
                            
                            
                            while(rs22.next()){
                                bal=rs22.getDouble(1);
                                
                                phrase=new Phrase("Remaining Loan Balance:  "+dbObject.getDBObject(format.Format2Currency(String.valueOf(amountPayable-bal)), "-"), pFontHeader2);
                                table1.addCell(phrase);
                                
                                phrase=new Phrase(" ");
                                table1.addCell(phrase);
                            }
                             
                            docPdf.add(table1);
                            
                 
            
                            docPdf.close();
                         deskTop.open(tempFile);

                    
            
            
        } catch(java.io.IOException IOexec) {
            
            javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), IOexec.getMessage());
            
        } catch (DocumentException | SQLException ex) {
             Logger.getLogger(LoanStatementBreakdownPdf.class.getName()).log(Level.SEVERE, null, ex);
         }   
    }
}