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
import java.awt.Color;
import java.awt.Desktop;
import java.sql.Connection;
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


public class NewTrialBalancePdf implements java.lang.Runnable{
  
     java.awt.Desktop deskTop = Desktop.getDesktop();
    
    DBObject dbObject;
    
    int numberSeq = 0;
    
    private Connection connect;
    
    public java.lang.String dbUserName = null;
    
    org.netbeans.lib.sql.pool.PooledConnectionSource pConnDB = null;
    
    boolean threadCheck = true;
    
    java.lang.Thread threadSample;
    
    com.lowagie.text.Font pFontHeader = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD);
    com.lowagie.text.Font pFontHeader1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.NORMAL);
    
   
    java.lang.Runtime rtThreadSample = java.lang.Runtime.getRuntime();
    
    java.lang.Process prThread;
    AMSUtility label = new AMSUtility();
    Date endDate=null;
    Format2Currency format;
    
    public void NewTrialBalancePdf(Date end) {
        connect=ConnectionDB.getInstance().getCon();
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
            com.lowagie.text.Document docPdf = new com.lowagie.text.Document(PageSize.A4);
            
            try {
                
                try {
                    
                    com.lowagie.text.pdf.PdfWriter.getInstance(docPdf, new java.io.FileOutputStream(tempFile));
                    
                    
                    String date = null;
                    try {
                        java.sql.Statement st4 = connect.createStatement();
                        
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
                    
                    com.lowagie.text.HeaderFooter footer = new com.lowagie.text.HeaderFooter(new Phrase("Trial Balance - Page: "), true);// FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA, 12, Font.BOLDITALIC,java.awt.Color.blue));
                    
                    docPdf.setFooter(footer);
                    
                    
                    docPdf.open();
                    
                    ReportUtil.addCenteredTitlePage(docPdf, connect);
                    try {
                        
                        
                        com.lowagie.text.pdf.PdfPTable table = new com.lowagie.text.pdf.PdfPTable(4);
                        
                        int headerwidths[] = {15, 55, 15, 15};
                        
                        table.setWidths(headerwidths);
                        
                        table.setWidthPercentage((100));
                        table.getDefaultCell().setBorder(Rectangle.BOTTOM);
                        
                        
                        
                        Phrase phrase; 
                        
                        
                        table.getDefaultCell().setColspan(4);
                        
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
                        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM);
                        
                        table.getDefaultCell().setBorder(PdfCell.TOP | PdfCell.BOTTOM | PdfCell.LEFT | PdfCell.RIGHT);
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_CENTER);
                        phrase=new Phrase("Trial Balance As at "+dateFormat.format(endDate), pFontHeader);
                        table.addCell(phrase);
                        
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
                        phrase = new Phrase("Printed On  :" +date , pFontHeader);
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setColspan(1);
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_CENTER);
                        phrase = new Phrase("Ledger Code",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Particulars",pFontHeader);
                        table.addCell(phrase);
                        
                        
                        phrase = new Phrase("Dr (Ksh)",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Cr (Ksh)",pFontHeader);
                        table.addCell(phrase);
                        
                         String subCode=null, subName=null, subSide=null, level2Code=null, level2Name=null, level2Side=null;
                         double totalDebit=0.00, totalCredit=0.00;
                         
                         
                         //SELECT DISTINCT SUB CODES FROM COA_SPECIFICS
                         
                         
                         table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT); 
                        ResultSet rs=SQLHelper.getResultset(connect, "select distinct(sub_code), name, side from coa_specifics order by 1");
                        while(rs.next()){
                            subCode=rs.getString(1);
                            subName=rs.getString(2);
                            subSide=rs.getString(3);
                            
                            System.err.println("Sub Code: "+subCode+"   Sub Name: "+subName+"   SubSide: "+subSide);
                            
                            //SELECT ALL CODES WHOSE FOREIGN KEY IS A SUB CODE FROM COA_SPECIFICS
                            
                            ResultSet rs1=SQLHelper.getResultset(connect, "select distinct(level2_code), name, side from coal_level2 where level1_code='"+subCode+"'");
                            while(rs1.next()){
                                level2Code=rs1.getString(1);
                                level2Name=rs1.getString(2);
                                level2Side=rs1.getString(3);
                                
                                System.err.println("Level 2 Code: "+level2Code+"    Level 2 Name: "+level2Name+"    Level 2 Side: "+level2Side);
                                
                                //FOR LEVEL 2 ACCOUNTING CODES
                                
                                if(level2Code!=null){
                                    if(level2Side.equals("DEBIT")){
                                        ResultSet rs2=SQLHelper.getResultset(connect, "select sum(debit-credit) from general_ledger where accounting_code='"+level2Code+"' "
                                                + "and entry_time::date<='"+endDate+"'");
                                        double debit=0.00;
                                        while(rs2.next()){
                                            debit=rs2.getDouble(1);
                                        }
                                        System.err.println("Debit value for "+level2Code+" is "+debit);
                                        
                                        totalDebit+=debit;
                                        
                                        System.err.println("Total Debit Value so far: "+totalDebit);
                                        
                                        if(debit!=0.00){
                                            phrase=new Phrase(level2Code, pFontHeader1);
                                            table.addCell(phrase);

                                            phrase=new Phrase(level2Name, pFontHeader1);
                                            table.addCell(phrase);

                                            phrase=new Phrase(format.Format2Currency(String.valueOf(debit)), pFontHeader1);
                                            table.addCell(phrase);

                                            phrase=new Phrase(" ");
                                            table.addCell(phrase);
                                        }
                                    }
                                    else if(level2Side.equals("CREDIT")){
                                        ResultSet rs2=SQLHelper.getResultset(connect, "select sum(credit-debit) from general_ledger where accounting_code='"+level2Code+"' "
                                                + "and entry_time::date<='"+endDate+"'");
                                        double credit=0.00;
                                        while(rs2.next()){
                                            credit=rs2.getDouble(1);
                                        }
                                        System.err.println("Credit Value for "+level2Code+" is "+credit);
                                        
                                        totalCredit+=credit;
                                        
                                        System.err.println("Total Credit so far "+totalCredit);
                                        
                                        if(credit!=0.00){
                                            phrase=new Phrase(level2Code, pFontHeader1);
                                            table.addCell(phrase);

                                            phrase=new Phrase(level2Name, pFontHeader1);
                                            table.addCell(phrase);

                                            phrase=new Phrase(" ");
                                            table.addCell(phrase);
                                            
                                            phrase=new Phrase(format.Format2Currency(String.valueOf(credit)), pFontHeader1);
                                            table.addCell(phrase);
                                            
                                        }
                                    }
                                }
                            }
                                //IF LEVEL CODE II DOES NOT EXIST
//                                else if(level2Code==null){
                                    if(subSide.equals("DEBIT")){
                                        double debit=0.00;
                                        ResultSet rs3=SQLHelper.getResultset(connect, "select sum(debit-credit) from general_ledger where accounting_code='"+subCode+"' "
                                                + "and entry_time::date<='"+endDate+"'");
                                        while(rs3.next()){
                                            debit=rs3.getDouble(1);
                                        }
                                        System.err.println("Debit value for "+subCode+" is "+debit);
                                        
                                        totalDebit+=debit;
                                        
                                        System.err.println("Total Debit value "+totalDebit);
                                        
                                        if(debit!=0.00){
                                            phrase=new Phrase(subCode, pFontHeader1);
                                            table.addCell(phrase);

                                            phrase=new Phrase(subName, pFontHeader1);
                                            table.addCell(phrase);

                                            phrase=new Phrase(format.Format2Currency(String.valueOf(debit)), pFontHeader1);
                                            table.addCell(phrase);

                                            phrase=new Phrase(" ", pFontHeader1);
                                            table.addCell(phrase);
                                        }
                                    }

                                    if(subSide.equals("CREDIT")){
                                        double credit=0.00;
                                        ResultSet rs3=SQLHelper.getResultset(connect, "select sum(credit-debit) from general_ledger where accounting_code='"+subCode+"' "
                                                + "and entry_time::date <='"+endDate+"'");
                                        while(rs3.next()){
                                            credit=rs3.getDouble(1);
                                        }
                                        System.err.println("Credit value for "+subCode+" is "+credit);
                                        
                                        totalCredit+=credit;
                                        
                                        System.err.println("Total Credit value is "+totalCredit);
                                        if(credit!=0.00){
                                            phrase=new Phrase(subCode, pFontHeader1);
                                            table.addCell(phrase);

                                            phrase=new Phrase(subName, pFontHeader1);
                                            table.addCell(phrase);


                                            phrase=new Phrase(" ", pFontHeader1);
                                            table.addCell(phrase);

                                            phrase=new Phrase(format.Format2Currency(String.valueOf(credit)), pFontHeader1);
                                            table.addCell(phrase);

                                        }
                                    }
//                                }
//                            }
                        }
                       /*     
                        //DEBTORS OR ACCOUNTS RECEIVABLE

                        //level1 debtors
                        ResultSet rs6=SQLHelper.getResultset(connect, "select sub_code from coa_specifics where sub_code='C002-007'");
                        while(rs6.next()){
                            subCode=rs6.getString(1);
                        }
                        ResultSet rs7=SQLHelper.getResultset(connect, "select distinct(d.member_no), first_name||' '||last_name||' '||surname, sum(debit-credit) from debtors_ledger d, members_Register m \n" +
"                                where m.member_no=d.member_no and date between '"+beginDate+"' and '"+endDate+"' group by 1, 2 --having sum(debit-credit)!=0.00");
                        while(rs7.next()){
                            double debit=Double.parseDouble(rs7.getObject(3).toString());
                            totalDebit+=debit;
                            
                            if(debit!=0.00){
                                phrase=new Phrase(subCode, pFontHeader1);
                                table.addCell(phrase);

                                phrase=new Phrase(dbObject.getDBObject(rs7.getObject(2), "-"), pFontHeader1);
                                table.addCell(phrase);

                                phrase=new Phrase(dbObject.getDBObject(format.Format2Currency(rs7.getObject(3).toString()), "-"), pFontHeader1);
                                table.addCell(phrase);
                                
                                
                                phrase=new Phrase(" ", pFontHeader1);
                                table.addCell(phrase);
                            }
                        }


                        //level 2 debtors
                        ResultSet rs4=SQLHelper.getResultset(connect, "select level2_code, name from coal_level2 where level1_code='C002-007'");
                        while(rs4.next()){
                            level2Code=rs4.getString(1);
                            level2Name=rs4.getString(2);

                            //get any calculations from level2 ledgers
                            ResultSet rs5=SQLHelper.getResultset(connect, "select sum(debit-credit) from general_ledger where accounting_code='"+level2Code+"' and "
                                    + "entry_time::date between '"+beginDate+"' and '"+endDate+"'");
                            double debtors=0.00;
                            while(rs5.next()){
                                debtors=rs5.getDouble(1);

                                totalDebit+=debtors;

                                if(debtors!=0.00){
                                    phrase=new Phrase(level2Code, pFontHeader1);
                                    table.addCell(phrase);

                                    phrase=new Phrase(level2Name, pFontHeader1);
                                    table.addCell(phrase);

                                    phrase=new Phrase(format.Format2Currency(String.valueOf(debtors)), pFontHeader1);
                                    table.addCell(phrase);

                                    phrase=new Phrase(" ", pFontHeader1);
                                    table.addCell(phrase);
                                }
                            }    
                        }
                        
                        //ACCOUNTS PAYABLE OR CREDITORS
                        ResultSet rs8=SQLHelper.getResultset(connect, "select sub_code from coa_specifics where sub_code='C003-0010'");
                        while(rs8.next()){
                            subCode=rs8.getString(1);
                        }
                        
                        //level 1 creditors
                        ResultSet rs9=SQLHelper.getResultset(connect, "select distinct(c.account_no), supplier_names, sum(credit-debit) from creditors_ledger c, stock_suppliers s "
                                + "where c.account_no=supplier_id and date between '"+beginDate+"' and '"+endDate+"' group by 1, 2 --having sum(credit-debit)!=0.00");
                        while(rs9.next()){
                            double credit=rs9.getDouble(3);
                            totalCredit+=credit;
                            
                            if(credit!=0.00){
                                phrase=new Phrase(subCode, pFontHeader1);
                                table.addCell(phrase);
                                
                                phrase=new Phrase(dbObject.getDBObject(rs9.getObject(2), "-"), pFontHeader1);
                                table.addCell(phrase);
                                
                                phrase=new Phrase(" ", pFontHeader1);
                                table.addCell(phrase);
                                
                                phrase=new Phrase(format.Format2Currency(String.valueOf(credit)), pFontHeader1);
                                table.addCell(phrase);
                                
                            }
                        }
                        
                        //level 2 creditors
                        
                        ResultSet rs10=SQLHelper.getResultset(connect, "select level2_code, name from coal_level2 where level1_code='C003-0010'");
                        while(rs10.next()){
                            level2Code=rs10.getString(1);
                            level2Name=rs10.getString(2);
                            
                            ResultSet rs11=SQLHelper.getResultset(connect, "select sum(credit-debit) from general_ledger where accounting_code='"+level2Code+"' "
                                + "and entry_time::date between '"+beginDate+"' and '"+endDate+"'");
                            while(rs11.next()){
                                double credit=rs11.getDouble(1);
                                totalCredit+=credit;
                                
                                if(credit!=0.00){
                                    phrase=new Phrase(level2Code, pFontHeader1);
                                    table.addCell(phrase);
                                    
                                    phrase=new Phrase(level2Name, pFontHeader1);
                                    table.addCell(phrase);
                                    
                                    phrase=new Phrase(" ", pFontHeader1);
                                    table.addCell(phrase);
                                    
                                    phrase=new Phrase(format.Format2Currency(String.valueOf(credit)), pFontHeader1);
                                    table.addCell(phrase);
                                }
                            }
                        }
                   */     
                        
                        
                        //the final part of displaying the totals
                        
                        phrase=new Phrase(" ", pFontHeader1);
                        table.addCell(phrase);
                        
                        phrase=new Phrase(" ", pFontHeader1);
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setBorderColor(Color.RED);
                        phrase=new Phrase(format.Format2Currency(String.valueOf(totalDebit)), pFontHeader);
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setBorderColor(Color.RED);
                        phrase=new Phrase(format.Format2Currency(String.valueOf(totalCredit)), pFontHeader);
                        table.addCell(phrase);
                        
                        System.err.println("Difference: "+(totalDebit-totalCredit));
                        
                        
                        docPdf.add(table);
                        
                      
                    } catch(com.lowagie.text.BadElementException BadElExec) {
                        
                        javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), BadElExec.getMessage());
                        
                    }
                    
                } catch(java.io.FileNotFoundException fnfExec) {
                    
                    javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), fnfExec.getMessage());
                    
                } catch (BadElementException ex) {
                    Logger.getLogger(StaffRegInactivePdf.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(NewTrialBalancePdf.class.getName()).log(Level.SEVERE, null, ex);
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