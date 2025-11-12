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
import java.io.IOException;
import java.sql.ResultSet;
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
public class WatchListSummaryPdf implements java.lang.Runnable{
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
    Date  endDate;
    Format2Currency format;
    
    public void WatchListSummaryPdf(Date end) {
        
        connect = ConnectionDB.getInstance().getCon();
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
            
            com.lowagie.text.Document docPdf = new com.lowagie.text.Document(PageSize.A4.rotate());
                    
            com.lowagie.text.pdf.PdfWriter.getInstance(docPdf, new java.io.FileOutputStream(tempFile));
//                    ReportUtil.addCenteredTitlePage(docPdf, connect);

            String date = null;


            java.sql.ResultSet rset4 = SQLHelper.getResultset(connect, "SELECT date(now()) as Date");

            while(rset4.next()){
                date = rset4.getObject(1).toString();
            }
            System.err.println("Date:   "+date);

            com.lowagie.text.HeaderFooter footer = new com.lowagie.text.HeaderFooter(new Phrase("WatchList Summary- Page: "), true);// FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA, 12, Font.BOLDITALIC,java.awt.Color.blue));

            docPdf.setFooter(footer);


            docPdf.open();

            ReportUtil.addCenteredTitlePage(docPdf, connect);



                com.lowagie.text.pdf.PdfPTable table = new com.lowagie.text.pdf.PdfPTable(7);

                int headerwidths[] = {5, 10, 30, 20, 10, 15, 10};

                table.setWidths(headerwidths);

                table.setWidthPercentage((100));
                table.getDefaultCell().setBorder(Rectangle.BOTTOM);

                Phrase phrase; 

                table.getDefaultCell().setColspan(7);

                
                phrase=new Phrase(" ");
                table.addCell(phrase);

                table.getDefaultCell().setBorder(PdfCell.TOP | PdfCell.BOTTOM | PdfCell.LEFT | PdfCell.RIGHT);
                
                table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_CENTER);
                phrase=new Phrase("Watchlist Summary", pFontHeader4);
                table.addCell(phrase);                        


                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM);

                phrase=new Phrase("As At:   "+dateFormat.format(endDate), pFontHeader4);
                table.addCell(phrase);

                System.err.println("End Date: "+dateFormat.format(endDate));

                table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
                phrase = new Phrase("Printed On  :" +date , pFontHeader);
                table.addCell(phrase);

                System.err.println("Print Date: "+date);


                table.getDefaultCell().setColspan(1);

                table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                phrase = new Phrase("#",pFontHeader);
                table.addCell(phrase);

                phrase = new Phrase("Member No",pFontHeader);
                table.addCell(phrase);

                phrase = new Phrase("Name",pFontHeader);
                table.addCell(phrase);

                phrase = new Phrase("Amount Payable",pFontHeader);
                table.addCell(phrase);

                phrase = new Phrase("Amount Paid",pFontHeader);
                table.addCell(phrase);

                phrase = new Phrase("Payment Date",pFontHeader);
                table.addCell(phrase);

                phrase = new Phrase("Loan No",pFontHeader);
                table.addCell(phrase);

                System.err.println("Before the query");

                    ResultSet rsettt = SQLHelper.getResultset(connect, "select d.member_no,  names, "
                            + "case when amount_applied is null then 0.00 else amount_applied end as payable, case when sum(d.credit) is null then 0.00 else sum(d.credit) end as amount_paid, d.date::date, other_identifier from loan_registration l, debtors_ledger d, "
                            + "members_register m where d.member_no=l.member_no and d.member_no=m.member_no and l.member_no=m.member_no "
                            + "and other_identifier=loan_no and paid=false and '"+endDate+"' -date::date>=60 group by 1, 2, amount_applied, d.date, other_identifier order by 2 asc,  d.date desc");

                    int count=1;
                    System.err.println("Count before loop: "+count);
                    while (rsettt.next()) {

                        phrase = new Phrase(""+count+"", pFontHeader1);
                        table.addCell(phrase);

                        phrase = new Phrase(dbObject.getDBObject(rsettt.getObject(1), "-"),pFontHeader1);
                        table.addCell(phrase);

                        phrase = new Phrase(dbObject.getDBObject(rsettt.getObject(2), "-"),pFontHeader1);
                        table.addCell(phrase);

                        phrase = new Phrase(dbObject.getDBObject(format.Format2Currency(rsettt.getObject(3).toString()), "-"),pFontHeader1);
                        table.addCell(phrase);

                        phrase = new Phrase(dbObject.getDBObject(format.Format2Currency(rsettt.getObject(4).toString()), "-"),pFontHeader1);
                        table.addCell(phrase);

                        phrase = new Phrase(dbObject.getDBObject(rsettt.getObject(5), "-"),pFontHeader1);
                        table.addCell(phrase);

                        phrase = new Phrase(dbObject.getDBObject(rsettt.getObject(6), "-"),pFontHeader1);
                        table.addCell(phrase);

                         System.err.println("Row "+count);
                        count++;
                    }

//                    table.getDefaultCell().setColspan(7);
//                    table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
//
//                    ResultSet rsop=SQLHelper.getResultset(connect, "select case when amount_applied-SUM(CREDIT) is null then 0.00 else "
//                            + "amount_applied-SUM(CREDIT) end as payable  from loan_registration l, debtors_ledger d, members_register m "
//                            + "where d.member_no=l.member_no and d.member_no=m.member_no and l.member_no=m.member_no and other_identifier=loan_no "
//                            + "and paid=false and '"+endDate+"' -date::date>=60 group by amount_applied");
//                    while(rsop.next()){
//                        phrase=new Phrase("GRAND TOTAL: "+dbObject.getDBObject(format.Format2Currency(rsop.getObject(1).toString()), "0.00"), pFontHeader4);
//                        table.addCell(phrase);
//                    }
                     docPdf.add(table);

                     docPdf.close();
                    deskTop.open(tempFile);
                } catch(java.sql.SQLException SqlExec) {
                    javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), SqlExec.getMessage());
            } catch (IOException | DocumentException ex) {
             Logger.getLogger(WatchListSummaryPdf.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
}