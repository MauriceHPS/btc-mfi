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
import java.sql.Statement;
import java.util.ArrayList;
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
public class DebtorsAgeingPdf implements java.lang.Runnable{
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
    
    com.lowagie.text.Font pFontHeader = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD);
    com.lowagie.text.Font pFontHeader1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL);
    com.lowagie.text.Font pFontHeader2 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD);
    
   
    java.lang.Runtime rtThreadSample = java.lang.Runtime.getRuntime();
    
    java.lang.Process prThread;
    AMSUtility label = new AMSUtility();
    Date dates;
    Format2Currency format;
    
    public void DebtorsAgeing(Date d) {
        
        connectDB = ConnectionDB.getInstance().getCon();
        
        dbObject = new DBObject();
        dates=d;
        threadSample = new java.lang.Thread(this, "SampleThread");
        format=new Format2Currency();
        
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
//                    ReportUtil.addCenteredTitlePage(docPdf, connectDB);
                    
                    String date = null;
                      java.sql.Statement st3 = connectDB.createStatement();
                        java.sql.Statement st4 = connectDB.createStatement();
                        
                        java.sql.ResultSet rset4 = st4.executeQuery("SELECT date(now()) as Date");
                        
                        while(rset4.next())
                            date = rset4.getObject(1).toString();
                        
                    
                    com.lowagie.text.HeaderFooter footer = new com.lowagie.text.HeaderFooter(new Phrase("Debtors Ageing Summary- Page: "), true);// FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA, 12, Font.BOLDITALIC,java.awt.Color.blue));
                    
                    docPdf.setFooter(footer);
                    
                    
                    docPdf.open();
                    
                    ReportUtil.addCenteredTitlePage(docPdf, connectDB);
                        
                        
                        com.lowagie.text.pdf.PdfPTable table = new com.lowagie.text.pdf.PdfPTable(8);
                        
                        int headerwidths[] = {6, 11, 23, 12, 12, 12, 12, 12};
                        
                        table.setWidths(headerwidths);
                        
                        table.setWidthPercentage((100));
                        table.getDefaultCell().setBorder(Rectangle.BOTTOM);
                        
                        Phrase phrase;
                        
                        table.getDefaultCell().setColspan(8);
                        
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
//                        table.getDefaultCell().setColspan(4);
                        
                        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM);
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_CENTER);
                        phrase=new Phrase("Debtors Ageing Summary as at "+dateFormat.format(dates), pFontHeader2);
                        table.addCell(phrase);

                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
                        phrase = new Phrase("Printed On  :" +date , pFontHeader);
                        table.addCell(phrase);
                        
                        
                        table.getDefaultCell().setColspan(1);
                        
                        table.getDefaultCell().setBorder(PdfCell.TOP | PdfCell.BOTTOM | PdfCell.LEFT | PdfCell.RIGHT);
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                        phrase = new Phrase("#",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Member ID",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Member Name",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Current",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Over 30",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Over 60",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Over 90",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Over 120",pFontHeader);
                        table.addCell(phrase);
                        
                        
                         table.getDefaultCell().setBackgroundColor(java.awt.Color.WHITE);
                       int count=1;
                                    //Ageing query
                                    ResultSet rs=SQLHelper.getResultset(connectDB, "select distinct(l.member_no), first_name||' '||last_name||' '||surname as names, \n" +
                                    "case when ('"+this.dates+"'-date::date)<=30 then sum(debit-credit) else 0.00 end as current, \n" +
                                    "case when ('"+this.dates+"'-date::date) >30 and ('"+this.dates+"'-date::date)<60 then sum(debit-credit) else 0.00 end as less_60,\n" +
                                    "case when ('"+this.dates+"'-date::date) >=60 and ('"+this.dates+"'-date::date) <90 then sum(debit-credit) else 0.00 end as less_90,\n" +
                                    "case when ('"+this.dates+"'-date::date) >=90 and ('"+this.dates+"'-date::date) <120 then sum(debit-credit) else 0.00 end as less_120,\n" +
                                    "case when ('"+this.dates+"'-date::date)>120 then sum(debit-credit) else 0.00 end as over_120\n" +
                                    "from debtors_ledger l, members_register c where c.member_no=l.member_no  group by 1, 2, date having sum(debit-credit)>0");
                                    while(rs.next()){
                                        
                                        phrase=new Phrase(""+count+"", pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        phrase=new Phrase(dbObject.getDBObject(rs.getString(1), "-"), pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        phrase=new Phrase(dbObject.getDBObject(rs.getString(2), "-"), pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        phrase=new Phrase(dbObject.getDBObject(format.Format2Currency(rs.getObject(3).toString()), "-"), pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        phrase=new Phrase(dbObject.getDBObject(format.Format2Currency(rs.getObject(4).toString()), "-"), pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        phrase=new Phrase(dbObject.getDBObject(format.Format2Currency(rs.getObject(5).toString()), "-"), pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        phrase=new Phrase(dbObject.getDBObject(format.Format2Currency(rs.getObject(6).toString()), "-"), pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        phrase=new Phrase(dbObject.getDBObject(format.Format2Currency(rs.getObject(7).toString()), "-"), pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        count++;
                                    }
                                    /*
                                    System.err.println("Pass less than 30");
                                    
                                    //over 30 days
                                    ResultSet rss1=SQLHelper.getResultset(connectDB, "select l.customer_id, names, sum(debit-credit) from debtors_ledger l, \n" +
                                        "customers_registration c where c.customer_id=l.customer_id and ('"+this.dates+"'-date)>30 and ('"+this.dates+"'-date)<60 "
                                                + "group by 1, 2 having sum(debit-credit)>0");
                                    while(rss1.next()){
                                        phrase=new Phrase(""+count+"");
                                        table.addCell(phrase);
                                        
                                        phrase=new Phrase(dbObject.getDBObject(rs.getString(1), "-"), pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        phrase=new Phrase(dbObject.getDBObject(rs.getString(2), "-"), pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        phrase=new Phrase("0.00", pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        
                                        phrase=new Phrase(dbObject.getDBObject(rs.getObject(3), "-"), pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        
                                        phrase=new Phrase("0.00", pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        phrase=new Phrase("0.00", pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        phrase=new Phrase("0.00", pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        count++;
                                    }
                                    
                                    System.err.println("Pass over 30");
                                    
                                    //over 60 days
                                    ResultSet rss2=SQLHelper.getResultset(connectDB, "select l.customer_id, names, sum(debit-credit) from debtors_ledger l, \n" +
"                                            customers_registration c where c.customer_id=l.customer_id and ('"+dates+"'-date)>60 and ('"+dates+"'-date)<90 group by 1, 2 having sum(debit-credit)>0");
                                while(rss2.next()){
                                        phrase=new Phrase(""+count+"");
                                        table.addCell(phrase);
                                        
                                        phrase=new Phrase(dbObject.getDBObject(rs.getString(1), "-"), pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        phrase=new Phrase(dbObject.getDBObject(rs.getString(2), "-"), pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        phrase=new Phrase("0.00", pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        phrase=new Phrase("0.00", pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        phrase=new Phrase(dbObject.getDBObject(rs.getObject(3), "-"), pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        
                                        
                                        
                                        phrase=new Phrase("0.00", pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        phrase=new Phrase("0.00", pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        count++;
                                }
                                
                                System.err.println("Pass over 60");
                                
                                //over 90
                                ResultSet rss3=SQLHelper.getResultset(connectDB, "select l.customer_id, names, sum(debit-credit) from debtors_ledger l, \n" +
"                               customers_registration c where c.customer_id=l.customer_id and ('"+dates+"'-date)>90 and ('"+dates+"'-date)<120 group by 1, 2 having sum(debit-credit)>0");
                                while(rss3.next()){
                                    phrase=new Phrase(""+count+"", pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        phrase=new Phrase(dbObject.getDBObject(rs.getString(1), "-"), pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        phrase=new Phrase(dbObject.getDBObject(rs.getString(2), "-"), pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        phrase=new Phrase("0.00", pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        phrase=new Phrase("0.00", pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        phrase=new Phrase("0.00", pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        phrase=new Phrase(dbObject.getDBObject(rs.getObject(3), "-"), pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        phrase=new Phrase("0.00", pFontHeader1);
                                        table.addCell(phrase);
                                        count++;
                                }
                                
                                System.err.println("Pass over 90");
                                
                                //over 120 days
                                ResultSet rss5=SQLHelper.getResultset(connectDB, "select l.customer_id, names, sum(debit-credit) from debtors_ledger l, \n" +
"                                            customers_registration c where c.customer_id=l.customer_id and ('"+dates+"'-date)>120 group by 1, 2 having sum(debit-credit)>0");
                                 while(rss5.next()){
                                    phrase=new Phrase(""+count+"", pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        phrase=new Phrase(dbObject.getDBObject(rss5.getString(1), "-"), pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        phrase=new Phrase(dbObject.getDBObject(rss5.getString(2), "-"), pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        phrase=new Phrase("0.00", pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        phrase=new Phrase("0.00", pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        phrase=new Phrase("0.00", pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        phrase=new Phrase("0.00", pFontHeader1);
                                        table.addCell(phrase); 
                                        
                                        phrase=new Phrase(dbObject.getDBObject(rss5.getObject(3), "-"), pFontHeader1);
                                        table.addCell(phrase);
                                        count++;
                                 }
                                
                                 System.err.println("Over 120 days");
                                 */
                                    
                                 //Grand total
                                 table.getDefaultCell().setColspan(8);
                                 phrase=new Phrase(" ");
                                 table.addCell(phrase);
                                 
                                 
                                ResultSet rs10=SQLHelper.getResultset(connectDB, "select sum(debit-credit) from debtors_ledger where date<='"+dates+"'");
                                while(rs10.next()){
                                    table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
                                    phrase=new Phrase("Grand Total:     "+dbObject.getDBObject(format.Format2Currency(rs10.getObject(1).toString()), "-"), pFontHeader2);
                                    table.addCell(phrase);
                                }
                                docPdf.add(table);
                            
                        docPdf.close();
                         deskTop.open(tempFile);

        } catch(java.io.IOException IOexec) {
            javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), IOexec.getMessage());
        } catch (DocumentException | SQLException ex) {
             Logger.getLogger(DebtorsAgeingPdf.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
    
    
    private  ArrayList<Date> getDates(){
        try {
            ArrayList<Date> list=new ArrayList<>();
            Statement student=connectDB.createStatement();
            ResultSet students=student.executeQuery("select distinct(date) from debtors_ledger where date<='"+dates+"' group by 1 having sum(debit-credit)>0 order by 1 asc");
            while(students.next()){
                list.add(students.getDate(1));
            }
            return list;
        } catch (SQLException ex) {
            Logger.getLogger(DebtorsAgeingPdf.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
      
}

