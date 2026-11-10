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

public class InvestorStatementPdf implements java.lang.Runnable{
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
    String investorID, loanNo;
    Format2Currency format;
    public void InvestorStatementPdf(String member, String loan) {
        
        connectDB =ConnectionDB.getInstance().getCon();
//        beginDate=begin;
//        endDate=end;
        investorID=member;
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

            double principal=0.00, rate=0.00, duration=0.00;

            Date applicationDate=null;

            ResultSet cus=SQLHelper.getResultset(connectDB, "SELECT creditor_id, supplier_names, id, principal, duration_months, interest_rate, "
                    + "application_date from investments_trail, creditors_register where supplier_id=creditor_id and creditor_id='"+investorID+"' and id='"+loanNo+"'");
            while(cus.next()){

                phrase=new Phrase("INVESTOR ID: "+dbObject.getDBObject(cus.getObject(1), "-"), pFontHeader1);
                table.addCell(phrase);

                phrase=new Phrase("INVESTOR NAME:  "+dbObject.getDBObject(cus.getObject(2), "-"), pFontHeader1);
                table.addCell(phrase);

                phrase=new Phrase("LOAN NO:  "+dbObject.getDBObject(cus.getObject(3), "-"), pFontHeader2);
                table.addCell(phrase);

                principal=cus.getDouble(4);
                rate=cus.getDouble(6);
                duration=cus.getDouble(5);
                applicationDate=cus.getDate(7);

                System.err.println("Principal:  "+principal);
                System.err.println("Interest Rate:  "+rate);
                System.err.println("Duration (months):  "+duration);

                phrase=new Phrase("PRINCIPAL:  "+dbObject.getDBObject(format.Format2Currency(cus.getObject(4).toString()), "-"), pFontHeader1);
                table.addCell(phrase);

                phrase=new Phrase("DURATION (MONTHS):  "+dbObject.getDBObject(format.Format2Currency(cus.getObject(5).toString()), "-"), pFontHeader1);
                table.addCell(phrase);

                phrase=new Phrase("INTEREST RATE:  "+dbObject.getDBObject(cus.getObject(6), "-"), pFontHeader1);
                table.addCell(phrase);

                phrase=new Phrase("APPLICATION DATE:  "+dbObject.getDBObject(cus.getObject(7), "-"), pFontHeader1);
                table.addCell(phrase);

            }
            double payableInterest=principal*(rate/100)*duration;

            System.err.println("Payable Interest:   "+payableInterest);
            ResultSet rs;


            phrase=new Phrase("PAYABLE INTEREST:   "+format.Format2Currency(String.valueOf(payableInterest)), pFontHeader1);
            table.addCell(phrase);

            phrase=new Phrase(" ");
            table.addCell(phrase);

            table.getDefaultCell().setColspan(2);

            phrase=new Phrase(" ");
            table.addCell(phrase);

            phrase=new Phrase(" ");
            table.addCell(phrase);

            docPdf.add(table);


            //contains the transactions' data

            com.lowagie.text.pdf.PdfPTable table1 = new com.lowagie.text.pdf.PdfPTable(8);

            int headerwidths1[] = {4, 7, 28, 14, 14, 12, 11, 10};

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

            phrase = new Phrase("Description",pFontHeader);
            table1.addCell(phrase);

            phrase = new Phrase("Payment ID",pFontHeader);
            table1.addCell(phrase);    

            phrase = new Phrase("Date",pFontHeader);
            table1.addCell(phrase);

            phrase = new Phrase("Amount Payable",pFontHeader);
            table1.addCell(phrase);

            phrase = new Phrase("Amount Paid",pFontHeader);
            table1.addCell(phrase);


            phrase = new Phrase("Ending Bal",pFontHeader);
            table1.addCell(phrase);

            java.sql.Statement st = connectDB.createStatement();

            java.sql.ResultSet rset;

            rset= st.executeQuery("select DISTINCT(identifier), credit as payable, debit as paid, description, payment_id, "
                    + "date::Date from creditors_ledger where creditor_id='"+investorID+"' and identifier='"+loanNo+"' "
                            + " ORDER BY 2 desc, 6 asc");

            int count=1;
             table1.getDefaultCell().setColspan(1);
             table1.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
             
             double endingBal=0.00;

             while (rset.next()) {
                 double payable=rset.getDouble(2);

                 double paid=rset.getDouble(3);

                double lastColumn=0.00;
                
                if(count==1){
                    lastColumn=payable-paid;
                    endingBal=lastColumn;
                }
                else{
                    endingBal-=paid;
                }

                phrase = new Phrase(""+count+"   ", pFontHeader1);
                table1.addCell(phrase);

                phrase = new Phrase(dbObject.getDBObject(rset.getObject(1), "-"),pFontHeader1);
                table1.addCell(phrase);

                phrase = new Phrase(dbObject.getDBObject(rset.getObject(4), "-"),pFontHeader1);
                table1.addCell(phrase);

                phrase = new Phrase(dbObject.getDBObject(rset.getObject(5), "-"),pFontHeader1);
                table1.addCell(phrase);

                phrase = new Phrase(dbObject.getDBObject(rset.getObject(6), "-"),pFontHeader1);
                table1.addCell(phrase);

                phrase = new Phrase(dbObject.getDBObject(format.Format2Currency(String.valueOf(payable)), "-"),pFontHeader1);
                table1.addCell(phrase);


                phrase = new Phrase(dbObject.getDBObject(format.Format2Currency(String.valueOf(paid)), "-"),pFontHeader1);
                table1.addCell(phrase);

                phrase = new Phrase(dbObject.getDBObject(format.Format2Currency(String.valueOf(endingBal)), "-"),pFontHeader1);
                table1.addCell(phrase);

                count++;
            }

            docPdf.add(table1);

            docPdf.close();

            deskTop.open(tempFile);
            
        } catch(java.io.IOException IOexec) {
            
            javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), IOexec.getMessage());
            
        } catch (DocumentException | SQLException ex) {
             Logger.getLogger(InvestorStatementPdf.class.getName()).log(Level.SEVERE, null, ex);
         }   
    }
}