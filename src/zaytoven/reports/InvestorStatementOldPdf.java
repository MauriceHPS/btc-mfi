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

public class InvestorStatementOldPdf implements java.lang.Runnable{
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
    String creditorID, loanNo;
    Format2Currency format;
    public void InvestorStatementPdf(String member, String loan) {
        
        connectDB =ConnectionDB.getInstance().getCon();
//        beginDate=begin;
//        endDate=end;
        creditorID=member;
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
            com.lowagie.text.Document docPdf = new com.lowagie.text.Document(PageSize.A4.rotate());
            
                
                    
                    com.lowagie.text.pdf.PdfWriter.getInstance(docPdf, new java.io.FileOutputStream(tempFile));
//                    ReportUtil.addCenteredTitlePage(docPdf, connectDB);
                    
                    String date = null;
                      java.sql.Statement st3 = connectDB.createStatement();
                        java.sql.Statement st4 = connectDB.createStatement();
                        
                        java.sql.ResultSet rset4 = st4.executeQuery("SELECT date(now()) as Date");
                        
                        while(rset4.next())
                            date = rset4.getObject(1).toString();
                        
                    
                    
                    com.lowagie.text.HeaderFooter footer = new com.lowagie.text.HeaderFooter(new Phrase("Investor Statement- Page: "), true);// FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA, 12, Font.BOLDITALIC,java.awt.Color.blue));
                    
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
                        phrase=new Phrase("INVESTOR STATEMENT", pFontHeader2);
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setColspan(2);
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
                        phrase = new Phrase("Printed On  :" +date , pFontHeader);
                        table.addCell(phrase);
                        
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
                        double total=0.00, amountPaid=0.00, balance=0.00;
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                        table.getDefaultCell().setColspan(1);
                        
                        double principal=0.00;
                        
                        ResultSet cus=SQLHelper.getResultset(connectDB, "select distinct(i.creditor_id),  supplier_names, id, "
                                + "principal, duration_months, interest_rate, application_date::date, amount_payable from CREDITORS_REGISTER c, investments_trail i "
                                + "where i.creditor_id=supplier_id and i.creditor_id='"+creditorID+"' and id='"+loanNo+"' ");
                        while(cus.next()){
                            principal=cus.getDouble(4);
                            
                            System.err.println("Principal: "+principal);
                            
                            phrase=new Phrase("CREDITOR NO: "+dbObject.getDBObject(cus.getObject(1), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase("CREDITOR NAME:  "+dbObject.getDBObject(cus.getObject(2), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase("LOAN NO:  "+dbObject.getDBObject(cus.getObject(3), "-"), pFontHeader2);
                            table.addCell(phrase);
                            
                            
                            phrase=new Phrase("PRINCIPAL:  "+dbObject.getDBObject(format.Format2Currency(cus.getObject(4).toString()), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            
                            phrase=new Phrase("DURATION (MONTHS):  "+dbObject.getDBObject(format.Format2Currency(cus.getObject(5).toString()), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase("INTEREST RATE:  "+dbObject.getDBObject(cus.getObject(6), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase("APPLICATION DATE:  "+dbObject.getDBObject(cus.getObject(7), "-"), pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase(" ");
                            table.addCell(phrase);
                            
                            phrase=new Phrase(" ");
                            table.addCell(phrase);
                        }
                        
                        table.getDefaultCell().setColspan(2);
                        
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
                        docPdf.add(table);
                        
                        
                        //contains the transactions' data
                        
                        com.lowagie.text.pdf.PdfPTable table1 = new com.lowagie.text.pdf.PdfPTable(6);
                        
                        int headerwidths1[] = {6, 10, 21, 21, 21, 21};
                        
                        table1.setWidths(headerwidths1);
                        
                        table1.setWidthPercentage((100));
                        table1.getDefaultCell().setBorder(Rectangle.BOTTOM);
                        
                        table1.getDefaultCell().setColspan(1);
                        
                        table1.getDefaultCell().setBorder(PdfCell.TOP | PdfCell.BOTTOM | PdfCell.LEFT | PdfCell.RIGHT);
                        
                        table1.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                        
                        phrase = new Phrase("#",pFontHeader);
                        table1.addCell(phrase);
                        
                        phrase = new Phrase("ID",pFontHeader);
                        table1.addCell(phrase);
                        
                        phrase = new Phrase("Payment Date",pFontHeader);
                        table1.addCell(phrase);
                        
                        phrase = new Phrase("Begin Bal",pFontHeader);
                        table1.addCell(phrase);
                        
                        phrase = new Phrase("Amount Paid",pFontHeader);
                        table1.addCell(phrase);
                        
                        phrase = new Phrase("Ending Bal",pFontHeader);
                        table1.addCell(phrase);
                        
                        
                            java.sql.Statement st = connectDB.createStatement();
                            
                            java.sql.ResultSet rset;
                            
                            double beginningBal=0.00, endingBal=0.00, paid=0.00, principalRecovered=0.00, interestRecovered=0.00;
                            
                           //for the row beginningBal=recoverablePrincipal [principal + penalties]
                           beginningBal=principal;
                           
                           System.err.println("Beginning Bal before filling:   "+beginningBal);
                            
                            rset= st.executeQuery("select r.identifier, d.date::date, amount_paid, d.debit as principal_recovered from creditors_ledger d, \n" +
                            "investment_payable r where d.payment_id=r.identifier and r.loan_no=d.identifier and d.creditor_id=r.creditor_id "
                            + "and d.payment_id is not null and r.loan_no='"+loanNo+"' and r.creditor_id='"+creditorID+"' order by 2");
                             
                            int count=1;
                             table1.getDefaultCell().setColspan(1);
                             table1.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                            
                             while (rset.next()) {
                                
                                phrase = new Phrase(""+count+"   ", pFontHeader1);
                                table1.addCell(phrase);
                                
                                phrase = new Phrase(dbObject.getDBObject(rset.getObject(1), "-"),pFontHeader1);
                                table1.addCell(phrase);
                                
                                phrase = new Phrase(dbObject.getDBObject(rset.getObject(2), "-"),pFontHeader1);
                                table1.addCell(phrase);
                                
                                //beginningBalance
                                phrase=new Phrase(dbObject.getDBObject(format.Format2Currency(String.valueOf(beginningBal)), "-"), pFontHeader1);
                                table1.addCell(phrase);
                                
                                //amount paid
                                paid=rset.getDouble(3);
                                
                                 System.err.println("Amount Paid:   "+paid);
                                
                                phrase = new Phrase(dbObject.getDBObject(format.Format2Currency(rset.getObject(3).toString()), "-"),pFontHeader1);
                                table1.addCell(phrase);
                                
                                //ending balance
                                endingBal=beginningBal-paid;
                                
                                phrase=new Phrase(dbObject.getDBObject(format.Format2Currency(String.valueOf(endingBal)), "-"), pFontHeader1);
                                table1.addCell(phrase);
                                
                                beginningBal=endingBal;
                                
                                 System.err.println("Ending Balance:    "+endingBal);
                                
                                count++;
                            }
                             
                             table1.getDefaultCell().setColspan(6);
                             
                             phrase=new Phrase(" ");
                             table1.addCell(phrase);
                             
                             phrase=new Phrase(" ");
                             table1.addCell(phrase);
                            
                             docPdf.add(table1);
                             
                             
                            /*
                            //penalties breakdown
                            com.lowagie.text.pdf.PdfPTable table2 = new com.lowagie.text.pdf.PdfPTable(6);
                        
                        int headerwidths2[] = {10, 15, 15, 15, 15, 30};
                        
                        table2.setWidths(headerwidths2);
                        
                        table2.setWidthPercentage((100));
                        table2.getDefaultCell().setBorder(Rectangle.BOTTOM);
                        
                        table2.getDefaultCell().setColspan(6);
                        
                        phrase=new Phrase(" ");
                        table2.addCell(phrase);
                        
                        table2.getDefaultCell().setBorder(PdfCell.TOP | PdfCell.BOTTOM | PdfCell.LEFT | PdfCell.RIGHT);
                        
                        table2.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_CENTER);
                        
                        phrase=new Phrase("PENALTIES BREAKDOWN", pFontHeader2);
                        table2.addCell(phrase);
                        
                        
                        table2.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                        
                        table2.getDefaultCell().setColspan(1);
                        
                        phrase=new Phrase("#", pFontHeader);
                        table2.addCell(phrase);
                        
                        phrase=new Phrase("ID", pFontHeader);
                        table2.addCell(phrase);
                        
                        phrase=new Phrase("Payment Date", pFontHeader);
                        table2.addCell(phrase);
                        
                        phrase=new Phrase("Amount", pFontHeader);
                        table2.addCell(phrase);
                        
                        phrase=new Phrase("A/C Code", pFontHeader);
                        table2.addCell(phrase);
                        
                        phrase=new Phrase("A/C Name", pFontHeader);
                        table2.addCell(phrase);
                        
                        int counter=1;
                        
                        rs=SQLHelper.getResultset(connectDB, "select idenfier, date::date, credit, accounting_code, name from loan_penalties l, coa_specifics \n" +
                        "where sub_code=accounting_code and loan_no='"+loanNo+"' and member_no='"+creditorID+"'  ");
                        while(rs.next()){
                            phrase=new Phrase(""+counter+"", pFontHeader1);
                            table2.addCell(phrase);
                            
                            phrase=new Phrase(dbObject.getDBObject(rs.getObject(1), "-"), pFontHeader1);
                            table2.addCell(phrase);
                            
                            phrase=new Phrase(dbObject.getDBObject(rs.getObject(2), "-"), pFontHeader1);
                            table2.addCell(phrase);
                            
                            phrase=new Phrase(dbObject.getDBObject(format.Format2Currency(rs.getObject(3).toString()), "-"), pFontHeader1);
                            table2.addCell(phrase);
                            
                            phrase=new Phrase(dbObject.getDBObject(rs.getObject(4), "-"), pFontHeader1);
                            table2.addCell(phrase);
                            
                            phrase=new Phrase(dbObject.getDBObject(rs.getObject(5), "-"), pFontHeader1);
                            table2.addCell(phrase);
                            
                            counter++;
                        }
                        
                        docPdf.add(table2);
                        */
            
                        docPdf.close();
                        deskTop.open(tempFile);

                    
            
            
        } catch(java.io.IOException IOexec) {
            
            javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), IOexec.getMessage());
            
        } catch (DocumentException | SQLException ex) {
             Logger.getLogger(InvestorStatementOldPdf.class.getName()).log(Level.SEVERE, null, ex);
         }   
    }
}