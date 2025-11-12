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
public class ApprovedLoansPdf implements java.lang.Runnable{
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
    
    public void ApprovedLoansPdf(Date begin, Date end) {
        
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
            
            
                    
            com.lowagie.text.pdf.PdfWriter.getInstance(docPdf, new java.io.FileOutputStream(tempFile));
//                    ReportUtil.addCenteredTitlePage(docPdf, connect);

            String date = null;
            java.sql.Statement st3 = connect.createStatement();
            java.sql.Statement st4 = connect.createStatement();

            java.sql.ResultSet rset4 = st4.executeQuery("SELECT date(now()) as Date");

              while(rset4.next())
                  date = rset4.getObject(1).toString();
                        
                    
                com.lowagie.text.HeaderFooter footer = new com.lowagie.text.HeaderFooter(new Phrase("Approved Loans- Page: "), true);// FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA, 12, Font.BOLDITALIC,java.awt.Color.blue));

                docPdf.setFooter(footer);


                docPdf.open();

                ReportUtil.addCenteredTitlePage(docPdf, connect);


                com.lowagie.text.pdf.PdfPTable table = new com.lowagie.text.pdf.PdfPTable(9);

                int headerwidths[] = {5, 8, 9, 20, 10, 12, 12, 12, 12};

                table.setWidths(headerwidths);

                table.setWidthPercentage((100));
                table.getDefaultCell().setBorder(Rectangle.BOTTOM);

                Phrase phrase; 

                table.getDefaultCell().setColspan(9);



                phrase=new Phrase(" ");
                table.addCell(phrase);

                table.getDefaultCell().setBorder(PdfCell.TOP | PdfCell.BOTTOM | PdfCell.LEFT | PdfCell.RIGHT);

                table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_CENTER);
                phrase=new Phrase("Approved Loans", pFontHeader4);
                table.addCell(phrase);                        


                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM);

                table.getDefaultCell().setColspan(5);

                table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                phrase=new Phrase("From "+dateFormat.format(beginDate)+" To "+dateFormat.format(endDate));
                table.addCell(phrase);


                table.getDefaultCell().setColspan(4);
                table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
                phrase = new Phrase("Printed On  :" +date , pFontHeader);
                table.addCell(phrase);


                table.getDefaultCell().setColspan(1);


                table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                phrase = new Phrase("#",pFontHeader);
                table.addCell(phrase);

                phrase = new Phrase("Loan No",pFontHeader);
                table.addCell(phrase);

                phrase = new Phrase("Member No",pFontHeader);
                table.addCell(phrase);

                phrase = new Phrase("Name",pFontHeader);
                table.addCell(phrase);

                phrase = new Phrase("Loan Type",pFontHeader);
                table.addCell(phrase);

                phrase = new Phrase("Amount Applied",pFontHeader);
                table.addCell(phrase);

                phrase = new Phrase("Amount Payable",pFontHeader);
                table.addCell(phrase);

                phrase = new Phrase("Monthly Installment",pFontHeader);
                table.addCell(phrase);

                phrase = new Phrase("App. Date",pFontHeader);
                table.addCell(phrase);


                java.sql.ResultSet rset = SQLHelper.getResultset(connect, "select loan_no, l.member_no, names, "
                        + "loan_type, amount_applied,amount_payable, monthly_installment, application_date from loan_registration l, "
                        + "members_register m where m.member_no=l.member_no and approved=true and application_date between '"+beginDate+"' and '"+endDate+"' "
                                + "order by application_date desc");
                int count=1;
                while (rset.next()) {
                    table.getDefaultCell().setColspan(1);

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

                    phrase = new Phrase(dbObject.getDBObject(format.Format2Currency(rset.getObject(5).toString()), "-"),pFontHeader1);
                    table.addCell(phrase);

                    phrase = new Phrase(dbObject.getDBObject(format.Format2Currency(rset.getObject(6).toString()), "-"),pFontHeader1);
                    table.addCell(phrase);

                    phrase = new Phrase(dbObject.getDBObject(format.Format2Currency(rset.getObject(7).toString()), "-"),pFontHeader1);
                    table.addCell(phrase);

                    phrase = new Phrase(dbObject.getDBObject(rset.getObject(8), "-"),pFontHeader1);
                    table.addCell(phrase);

                    count++;
                }
                docPdf.add(table);
                docPdf.close();
                deskTop.open(tempFile);
                
        }catch (IOException | DocumentException | SQLException ex) {
             Logger.getLogger(ApprovedLoansPdf.class.getName()).log(Level.SEVERE, null, ex);
         }
    }  
}

