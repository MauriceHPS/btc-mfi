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


public class StatementofProfitLossPdf implements java.lang.Runnable{
  
     java.awt.Desktop deskTop = Desktop.getDesktop();
    
    DBObject dbObject;
    
    int numberSeq = 0;
    
    public static java.sql.Connection connectDB = null;
    double netProfit=0.00;
    public java.lang.String dbUserName = null;
    
    org.netbeans.lib.sql.pool.PooledConnectionSource pConnDB = null;
    
    boolean threadCheck = true;
    
    java.lang.Thread threadSample;
    
    com.lowagie.text.Font pFontHeader = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD);
    com.lowagie.text.Font pFontHeader1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL);
    com.lowagie.text.Font pFontHeader2 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
   
    java.lang.Runtime rtThreadSample = java.lang.Runtime.getRuntime();
    
    java.lang.Process prThread;
    AMSUtility label = new AMSUtility();
    Date beginDate=null;
    Date endDate=null;
    
    Format2Currency format;
    
    public void StatementofProfitLossPdf(Date begin, Date end) {
        connectDB=ConnectionDB.getInstance().getCon();
        beginDate=begin;
        endDate=end;
        dbObject = new DBObject();
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
            com.lowagie.text.Document docPdf = new com.lowagie.text.Document(PageSize.A4);
            
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
                    
                    com.lowagie.text.HeaderFooter footer = new com.lowagie.text.HeaderFooter(new Phrase("Statement of Profit or Loss - Page: "), true);// FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA, 12, Font.BOLDITALIC,java.awt.Color.blue));
                    
                    docPdf.setFooter(footer);
                    
                    
                    docPdf.open();
                    
                    ReportUtil.addCenteredTitlePage(docPdf, connectDB);
                    try {
                        
                        
                        com.lowagie.text.pdf.PdfPTable table = new com.lowagie.text.pdf.PdfPTable(3);
                        
                        int headerwidths[] = {70, 15, 15};
                        
                        table.setWidths(headerwidths);
                        
                        table.setWidthPercentage((100));
                        table.getDefaultCell().setBorder(Rectangle.BOTTOM);
                        
                        
                        
                        Phrase phrase; 
                        
                        table.getDefaultCell().setColspan(3);
                        
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
                        
                        table.getDefaultCell().setBorder(PdfCell.TOP | PdfCell.BOTTOM | PdfCell.LEFT | PdfCell.RIGHT);
                        
                        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM);
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_CENTER);
                        phrase=new Phrase(" Statement of Profit or Loss for the year ending: "+dateFormat.format(endDate), pFontHeader2);
                        table.addCell(phrase);
                        
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                        phrase=new Phrase("Period Selected: "+dateFormat.format(beginDate)+" - "+dateFormat.format(endDate), pFontHeader2);
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setColspan(1);
                        
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setColspan(2);
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
                        phrase = new Phrase("Printed On  :" +date , pFontHeader2);
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setColspan(3);
                        
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setColspan(1);
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT); 
                        phrase = new Phrase("Particulars",pFontHeader);
                        table.addCell(phrase);
                        
//                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_CENTER);
                        phrase = new Phrase("KSH",pFontHeader);
                        table.addCell(phrase);
                        
//                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_CENTER);
                        phrase = new Phrase("KSH",pFontHeader);
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setColspan(3);
                        
                        phrase=new Phrase("Revenue [Trading A/C]", pFontHeader2);
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setColspan(1);
//                         table.getDefaultCell().setBackgroundColor(java.awt.Color.WHITE);
                       
                        try {
                            //REVENUE
                            double revenue=0.00;
                            String revenueCode=null, revenueName=null;
                            //Level 1
                            ResultSet rsett=SQLHelper.getResultset(connectDB, "select sub_code, name from coa_specifics where master_code='C008' "
                                    + "and validator='trading'");
                            while(rsett.next()){
                                revenueCode=rsett.getString(1);
                                revenueName=rsett.getString(2);
                                
                                double innerRevenue=0.00;
                                ResultSet rset =SQLHelper.getResultset(connectDB, "select case when sum(credit-debit) is null then '0.00' else sum(credit-debit) end as trial "
                                    + "from general_ledger where accounting_code=(select sub_code from coa_specifics where sub_code='"+revenueCode+"') "
                                    + "and entry_time::date between '"+beginDate+"' and '"+endDate+"'");
//                              ResultSet rset=SQLHelper.getResultset(connectDB, "select sum(amount_disbursed) from loan_disbursements "
//                                    + "where disbursement_date between '"+beginDate+"' and '"+endDate+"'");
                                while(rset.next()){
                                    innerRevenue=rset.getDouble(1);
                                    revenue+=innerRevenue;
                                    
                                    if(innerRevenue!=0.00){
                                        phrase=new Phrase(revenueName,  pFontHeader);
                                        table.addCell(phrase);

                                        phrase=new Phrase(" ");
                                        table.addCell(phrase);
                                        
                                        phrase=new Phrase(format.Format2Currency(String.valueOf(innerRevenue)), pFontHeader1);
                                        table.addCell(phrase);
                                    }
                                }
                                //level 2
                                String level2Code=null, level2Name=null;
                                ResultSet rsett2=SQLHelper.getResultset(connectDB, "select level2_code, name from coal_level2 where level1_code='"+revenueCode+"'");
                                while(rsett2.next()){
                                    level2Code=rsett2.getString(1);
                                    level2Name=rsett2.getString(2);
                                    
                                    double level2Revenue=0.00;
                                    ResultSet rsett3=SQLHelper.getResultset(connectDB, "select case when sum(credit-debit) is null then '0.00' else sum(credit-debit) end as trial "
                                    + "from general_ledger where accounting_code=(select sub_code from coa_specifics where sub_code='"+level2Code+"') "
                                    + "and entry_time::date between '"+beginDate+"' and '"+endDate+"'");
                                    while(rsett3.next()){
                                        
                                        level2Revenue=rsett3.getDouble(1);
                                        revenue+=level2Revenue;
                                        
                                        if(level2Revenue!=0.00){
                                            phrase=new Phrase(level2Name, pFontHeader);
                                            table.addCell(phrase);

                                            phrase=new Phrase(" ");
                                            table.addCell(phrase);

                                            phrase=new Phrase(format.Format2Currency(String.valueOf(level2Revenue)), pFontHeader1);
                                            table.addCell(phrase);
                                        }
                                    }
                                }
                            }
                            
//                            ResultSet rsett=SQLHelper.getResultset(connectDB, "select sum(credit) from debtors_ledger "
//                                    + "where date::date between '"+beginDate+"' and '"+endDate+"'");
//                            while(rsett.next()){
//                                amountReceived=rsett.getDouble(1);
//                            }
//                            revenue=amountReceived-amountDisbursed;
                                
                            
                            
//                            if(revenue>=0.00){
//                                phrase=new Phrase(format.Format2Currency(String.valueOf(revenue)), pFontHeader1);
//                                table.addCell(phrase);
//                            }
//                            else{
//                                phrase=new Phrase("("+format.Format2Currency(String.valueOf(revenue))+")", pFontHeader1);
//                                table.addCell(phrase);
//                            }

                            
                            
                            System.err.println("Revenue: "+revenue);
                            
                            /*
                            
                            //return inwards
                            double returnIn=0.00;
                            ResultSet rset1=SQLHelper.getResultset(connectDB, "select case when SUM(debit-credit) is null then '0.00' else sum(debit-credit) END AS case_1 FROM general_ledger "
                                    + "WHERE accounting_code=(select sub_code from coa_specifics where sub_code='C009-0020') and entry_time::date between '"+beginDate+"' and '"+endDate+"'");
                            while(rset1.next()){
                                returnIn=rset1.getDouble(1);
                                
                                if(returnIn!=0.00){
                                    phrase=new Phrase("Less Return Inwards", pFontHeader);
                                    table.addCell(phrase);
                                    
                                    phrase=new Phrase(" ");
                                    table.addCell(phrase);

                                    phrase=new Phrase("("+format.Format2Currency(String.valueOf(returnIn))+")", pFontHeader1);
                                    table.addCell(phrase);
                                }
                            }
                            System.err.println("Return Inwards: "+returnIn);
                            
                            //CARRIAGE INWARDS
                            double carriageInwards=0.00;
                            
                            ResultSet rset2=SQLHelper.getResultset(connectDB, "select case when SUM(credit-debit) is null then '0.00' else sum(credit-debit) end as trial FROM general_ledger "
                                    + "WHERE accounting_code=(select sub_code from coa_specifics where sub_code='C009-0022') "
                                    + "and entry_time::date between '"+beginDate+"' and '"+endDate+"'");
                            while(rset2.next()){
                                carriageInwards=rset2.getDouble(1);
                                
                                if(carriageInwards!=0.00){
                                    phrase=new Phrase("Carriage Inwards", pFontHeader);
                                    table.addCell(phrase);

                                    phrase=new Phrase(format.Format2Currency(String.valueOf(carriageInwards)), pFontHeader1);
                                    table.addCell(phrase);


                                    phrase=new Phrase(" ");
                                    table.addCell(phrase);
                                }
                            }
                            System.err.println("Carriage Inwards:  "+carriageInwards);
                            
                                
                            //return outwards
                            double returnOut=0.00;
                            ResultSet rset3=SQLHelper.getResultset(connectDB, "select case when SUM(credit-debit) is null then '0.00' else sum(credit-debit) END AS case_1 FROM general_ledger "
                                    + "WHERE accounting_code=(select sub_code from coa_specifics where sub_code='C008-0023') "
                                    + "and entry_time::date between '"+beginDate+"' and '"+endDate+"'");
                            while(rset3.next()){
                                returnOut=rset3.getDouble(1);
                                
                                if(returnOut!=0.00){
                                    phrase=new Phrase("Less Return Outwards", pFontHeader);
                                    table.addCell(phrase);

                                    phrase=new Phrase("("+format.Format2Currency(String.valueOf(returnOut))+")", pFontHeader1);
                                    table.addCell(phrase);

                                    phrase=new Phrase(" ");
                                    table.addCell(phrase);
                                }
                            }
                            System.err.println("Return outwards: "+returnOut);
                            */
                            
                            //calculate the gross profit
                            table.getDefaultCell().setColspan(3);
                            phrase=new Phrase(" ");
                            table.addCell(phrase);
                            
                            table.getDefaultCell().setColspan(1);
                            phrase=new Phrase(" ");
                            table.addCell(phrase);
                            
                            phrase=new Phrase(" ");
                            table.addCell(phrase);
                            
//                            double totalTrading=carriageInwards-returnOut;
//                            System.err.println("Trading Val: "+totalTrading);
                            
                            //not needed unless inventory is invloved
//                            phrase=new Phrase("("+format.Format2Currency(String.valueOf(totalTrading))+")", pFontHeader);
//                            table.addCell(phrase);
                            
                            double grossProfit=revenue;     //-returnIn-totalTrading;
                            
                            
                            table.getDefaultCell().setColspan(3);
                            
                            phrase=new Phrase(" ");
                            table.addCell(phrase);
                            
                            table.getDefaultCell().setColspan(1);
                            phrase=new Phrase("Gross Profit/(-Gross Loss)", pFontHeader2);
                            table.addCell(phrase);
                            
                            phrase=new Phrase(" ");
                            table.addCell(phrase);
                            
                            table.getDefaultCell().setBorderColor(Color.RED);
                            phrase=new Phrase(""+format.Format2Currency(String.valueOf(grossProfit))+"", pFontHeader2);
                            table.addCell(phrase);
                            
                            System.err.println("Gross Profit:   "+grossProfit);
                            
                            
                            table.getDefaultCell().setColspan(3);
                            
                            table.getDefaultCell().setBorderColor(Color.BLACK);
                            
                            phrase=new Phrase(" ");
                            table.addCell(phrase);
                            
                            phrase=new Phrase("Revenue [Not in the Trading A/C]", pFontHeader2);
                            table.addCell(phrase);
                            
                            table.getDefaultCell().setColspan(1);
                            
                            
                            //Revenue that is not part of the trading account
                            String subCode=null, subName=null;
                            double recRevenue=0.00;
                            ResultSet rs7=SQLHelper.getResultset(connectDB, "select SUB_code, name from coa_specifics where validator='-' \n" +
                                                                        "and master_code in (select ACCOUNTING_code from chart_of_accounts where accounting_code='C008')");
                            while(rs7.next()){
                                subCode=rs7.getString(1);
                                subName=rs7.getString(2);
                                
                                System.err.println("Sub Code: "+subCode+" Sub Name: "+subName);
                                
                                //level 1 revenue
                                ResultSet rs88=SQLHelper.getResultset(connectDB, "select sum(credit-debit) from general_ledger where accounting_code='"+subCode+"' and "
                                        + "entry_time::date between '"+beginDate+"' and '"+endDate+"'");
                                double inRevenue=0.00;
                                while(rs88.next()){
                                    inRevenue=rs88.getDouble(1);
                                    
                                    System.err.println("In Revenue: "+inRevenue);
                                    
                                    recRevenue+=inRevenue;
                                    
                                    if(inRevenue!=0.00 || inRevenue!=0.0){
                                        phrase=new Phrase(subName, pFontHeader);
                                        table.addCell(phrase);

                                        phrase=new Phrase(" ");
                                        table.addCell(phrase);

                                        phrase=new Phrase(""+format.Format2Currency(String.valueOf(inRevenue))+"", pFontHeader1);
                                        table.addCell(phrase);
                                    }
                                    
                                }
                                System.err.println("Level II revenue codes");
                                
                                //level2 codes
                                String level2Code=null, level2Name=null;
                                
                                ResultSet rs99=SQLHelper.getResultset(connectDB, "select level2_code, name from coal_level2 where level1_code='"+subCode+"'");
                                
                                System.err.println("In the level II loop");
                                
                                while(rs99.next()){
                                    level2Code=rs99.getString(1);
                                    level2Name=rs99.getString(2);
                                    
                                    System.err.println("Level 2 Code:   "+level2Code+" Level 2 Name:    "+level2Code);
                                    
                                    double level2Rev=0.00;
                                    
                                    ResultSet rs10=SQLHelper.getResultset(connectDB, "select sum(credit-debit) from general_ledger where accounting_code='"+level2Code+"' and "
                                            + "entry_time::date between '"+beginDate+"' and '"+endDate+"'");
                                    
                                    while(rs10.next()){
                                        level2Rev=rs10.getDouble(1);
                                        
                                        System.err.println("Level 2 Revenue: "+level2Rev);
                                        
                                        recRevenue+=level2Rev;
                                        
                                        if(level2Rev!=0.00){
                                            phrase=new Phrase(level2Name, pFontHeader);
                                            table.addCell(phrase);

                                            phrase=new Phrase(" ");
                                            table.addCell(phrase);

                                            phrase=new Phrase(format.Format2Currency(String.valueOf(level2Rev)), pFontHeader1);
                                            table.addCell(phrase);
                                        }
                                    }
                                    
                                }
                            }
                            
                            table.getDefaultCell().setColspan(3);
                            
                            phrase=new Phrase(" ");
                            table.addCell(phrase);
                            
                            phrase=new Phrase("Less Expenses", pFontHeader2);
                            table.addCell(phrase);
                            
                            table.getDefaultCell().setColspan(1);
                            
                            
                            //Expenses Loop
                            String code=null, name=null;
                            double expenses=0.00;
                            ResultSet rset5=SQLHelper.getResultset(connectDB, "select sub_code, name  from coa_specifics "
                                    + "where sub_code in (select sub_Code from coa_specifics where master_code='C009')");
                            while(rset5.next()){
                                code=rset5.getString(1);
                                name=rset5.getString(2);

                                System.err.println("Expense Code: "+code+",Expense Name: "+name);

                                //level1 expenses
                                ResultSet rset6=SQLHelper.getResultset(connectDB, "select case when SUM(debit-credit) is null then '0.00' else sum(debit-credit) end as trial from general_ledger where accounting_code='"+code+"'"
                                        + " and entry_time::date between '"+beginDate+"' and '"+endDate+"'");
                                while(rset6.next()){
                                    double inExp=0.00;
                                    inExp=rset6.getDouble(1);
                                    
                                    System.err.println("In Expense: "+inExp);
                                    
                                    expenses+=inExp;
                                    
                                    if(inExp!=0.00){
                                        phrase=new Phrase(""+name+"", pFontHeader);
                                        table.addCell(phrase);

                                        phrase=new Phrase(""+format.Format2Currency(String.valueOf(inExp))+"", pFontHeader1);
                                        table.addCell(phrase);

                                        phrase=new Phrase("");
                                        table.addCell(phrase);


                                        System.err.println("Expense for "+name+":   "+inExp);
                                    }
                                }
                                //level2 expenses
                                
                                String level2Code=null, level2Name=null;
                                double inExpense=0.00;
                                ResultSet rset11=SQLHelper.getResultset(connectDB, "select level2_code, name from coal_level2 where level1_code='"+code+"'");
                                while(rset11.next()){
                                    level2Code=rset11.getString(1);
                                    level2Name=rset11.getString(2);
                                    
                                    System.err.println("Level 2 Exp Code:   "+level2Code+" Level 2 Exp Name:    "+level2Name);
                                    
                                    ResultSet rset12=SQLHelper.getResultset(connectDB, "select sum(debit-credit) from general_ledger where accounting_code='"+level2Code+"' "
                                            + "and entry_time::date between '"+beginDate+"' and '"+endDate+"'");
                                    while(rset12.next()){
                                        inExpense=rset12.getDouble(1);
                                        expenses+=inExpense;
                                        
                                        System.err.println("In Expense: "+inExpense);
                                        
                                        if(inExpense!=0.00){
                                            phrase=new Phrase(level2Name, pFontHeader);
                                            table.addCell(phrase);

                                            phrase=new Phrase(format.Format2Currency(String.valueOf(inExpense)), pFontHeader1);
                                            table.addCell(phrase);


                                            phrase=new Phrase(" ");
                                            table.addCell(phrase);
                                        }
                                    }
                                }
                            }
                            
                            
                            phrase=new Phrase(" ");
                            table.addCell(phrase);
                            
                            phrase=new Phrase(" ");
                            table.addCell(phrase);
                            
                            phrase=new Phrase("("+format.Format2Currency(String.valueOf(expenses))+")", pFontHeader);
                            table.addCell(phrase);
                            
                            netProfit=(grossProfit+recRevenue)-expenses;
                            
                            table.getDefaultCell().setColspan(3);
                            
                            phrase=new Phrase(" ");
                            table.addCell(phrase);
                            
                            table.getDefaultCell().setColspan(1);
                            
                            phrase=new Phrase("Net Profit/(-Net Loss)", pFontHeader2);
                            table.addCell(phrase);
                            
                            phrase=new Phrase(" ");
                            table.addCell(phrase);
                            
                            table.getDefaultCell().setBorderColor(Color.RED);
                            phrase=new Phrase(""+format.Format2Currency(String.valueOf(netProfit))+"", pFontHeader2);
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
                    Logger.getLogger(StatementofProfitLossPdf.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public double getNetProfit(Date begin, Date end){
        
        return netProfit;
    }
}