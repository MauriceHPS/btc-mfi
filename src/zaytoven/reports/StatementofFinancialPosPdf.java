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


public class StatementofFinancialPosPdf implements java.lang.Runnable{
  
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
    com.lowagie.text.Font pFontHeader2= FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
    
   
    java.lang.Runtime rtThreadSample = java.lang.Runtime.getRuntime();
    
    java.lang.Process prThread;
    AMSUtility label = new AMSUtility();
    Date endDate=null;
    Format2Currency format;
    
    public void StatementofFinancialPosPdf(Date end) {
        endDate=end;
        dbObject = new DBObject();
        connectDB=ConnectionDB.getInstance().getCon();
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
                
                java.lang.Thread.currentThread().sleep(200);
                
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
                        java.sql.Statement st4 = connectDB.createStatement();
                        
                        java.sql.ResultSet rset4 = st4.executeQuery("SELECT date(now()) as Date");
                        while(rset4.next())
                            date = rset4.getObject(1).toString();
                    } 
                    catch(java.sql.SQLException SqlExec) {
                        
                        javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), SqlExec.getMessage());
                        
                    }
                    
                    com.lowagie.text.HeaderFooter footer = new com.lowagie.text.HeaderFooter(new Phrase("Statement of Financial Position - Page: "), true);// FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA, 12, Font.BOLDITALIC,java.awt.Color.blue));
                    
                    docPdf.setFooter(footer);
                    
                    
                    docPdf.open();
                    
                    ReportUtil.addCenteredTitlePage(docPdf, connectDB);
                    try {
                        
                        
                        com.lowagie.text.pdf.PdfPTable table = new com.lowagie.text.pdf.PdfPTable(3);
                        
                        int headerwidths[] = {60, 20, 20};
                        
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
                        phrase=new Phrase("Statement of Financial Position As At "+dateFormat.format(endDate), pFontHeader2);
                        table.addCell(phrase);
                        
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
                        phrase = new Phrase("Printed On: " +date , pFontHeader2);
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                        table.getDefaultCell().setColspan(1);
                        
                        phrase = new Phrase("Particulars",pFontHeader2);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Ksh",pFontHeader2);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Ksh",pFontHeader2);
                        table.addCell(phrase);
                        
                         double fixedAssets=0.00, currentAssets=0.00, currentLiabilities=0.00, longLiabilities=0.00, capital=0.00, netProfit=0.00, drawing=0.00;
                        try {
                            
                            //NON-CURRENT ASSETS
                            table.getDefaultCell().setColspan(3);
                            phrase=new Phrase("NON-CURRENT ASSETS", pFontHeader2);
                            table.addCell(phrase);
                            
                            String nonCurrentCode=null, nonCurrentName=null;
                            
                            table.getDefaultCell().setColspan(1);
                            ResultSet rs=SQLHelper.getResultset(connectDB, "select accounting_code, category from chart_of_accounts where accounting_code='C001'");
                            while(rs.next()){
                                nonCurrentCode=rs.getString(1);
                                nonCurrentName=rs.getString(2);
                                
                                System.err.println("Code: "+nonCurrentCode+"    Name:   "+nonCurrentName);
                                
                                
                                ResultSet rs1=SQLHelper.getResultset(connectDB, "select sub_code, name, validator from coa_specifics "
                                        + "where master_code='"+nonCurrentCode+"' order by 3 asc");
                                
                                String sub_code=null, name=null;
                                while(rs1.next()){
                                    sub_code=rs1.getString(1);
                                    name=rs1.getString(2);
                                    
                                    System.err.println("Sub Code: "+sub_code+"  Name:   "+name);
                                    
                                    phrase=new Phrase(name, pFontHeader1);
                                    table.addCell(phrase);
                                    
                                    phrase=new Phrase(" ");
                                    table.addCell(phrase);
                                    
                                    //level 1 categories
                                    
                                    ResultSet rs2=SQLHelper.getResultset(connectDB, "select CASE when sum(debit-credit) is null then '0.00' else sum(debit-credit) end as trial from general_ledger "
                                            + "where accounting_code='"+sub_code+"' and entry_time::date<='"+endDate+"'");
                                    double fixed=0.00;
                                    while(rs2.next()){
                                        fixed=rs2.getDouble(1);
                                        fixedAssets+=fixed;
                                        //level 2 categories
                                        ResultSet rs3=SQLHelper.getResultset(connectDB, "select level2_code, name from coal_level2 where level1_code='"+sub_code+"'");
                                        String level2Code=null, level2Name=null;
                                        
                                        while(rs3.next()){
                                            level2Code=rs3.getString(1);
                                            level2Name=rs.getString(2);
                                            
                                            ResultSet rs4=SQLHelper.getResultset(connectDB, "select case when sum(debit-credit) is null then 0.00 else sum(debit-credit) end as case1 "
                                                    + "from general_ledger where accounting_code='"+level2Code+"' and entry_time::date<='"+endDate+"'");
                                            double level2Fixed=0.00;
                                            while(rs4.next()){
                                                level2Fixed=rs4.getDouble(1);
                                                fixed+=level2Fixed;
                                                fixedAssets+=level2Fixed;
                                                
                                                System.err.println("Level 2 Value:  "+level2Fixed);
                                                System.err.println("Cumulative Value:   "+fixed);
                                            }
                                           
                                        }
                                        
                                        System.err.println("Final Value for the account:    "+fixed);
                                        phrase=new Phrase(""+format.Format2Currency(String.valueOf(fixed)), pFontHeader1);
                                        table.addCell(phrase);
                                        
                                    }
                                    
                                }
                            }
                            
                            System.err.println("Fixed Assets: "+fixedAssets);
                            
                            
                            //CURRENT ASSETS
                            table.getDefaultCell().setColspan(3);
                            phrase=new Phrase("CURRENT ASSETS", pFontHeader2);
                            table.addCell(phrase);
                            
                            table.getDefaultCell().setColspan(1);
                            
                            String currentAssetsCode=null, currentAssetsName=null;
                            ResultSet rs3=SQLHelper.getResultset(connectDB, "select accounting_code, category from chart_of_accounts where accounting_code='C002'");
                            while(rs3.next()){
                                currentAssetsCode=rs3.getString(1);
                                currentAssetsName=rs3.getString(2);
                                
                                System.err.println("Code: "+currentAssetsCode+" Name:   "+currentAssetsName);
                               
                                //level 1 categories
                                ResultSet rs1=SQLHelper.getResultset(connectDB, "select sub_code, name, validator from coa_specifics "
                                        + "where master_code='"+currentAssetsCode+"' order by 3 asc");
                                
                                String sub_code=null, name=null;
                                while(rs1.next()){
                                    sub_code=rs1.getString(1);
                                    name=rs1.getString(2);
                                    
                                    System.err.println("Sub Code: "+sub_code);
                                    System.err.println("Name:   "+name);
                                    
                                    phrase=new Phrase(name, pFontHeader1);
                                    table.addCell(phrase);
                                    
                                    
                                    //level 1 categories
                                    
                                    ResultSet rs2=SQLHelper.getResultset(connectDB, "select CASE when sum(debit-credit) is null then '0.00' else sum(debit-credit) end as trial from general_ledger "
                                            + "where accounting_code='"+sub_code+"' and entry_time::date <='"+endDate+"'");
                                    double current=0.00;
                                    while(rs2.next()){
                                        current=rs2.getDouble(1);
                                        currentAssets+=current;
                                        //level 2 categories
                                        System.err.println("Initial Current:    "+current);
                                        
                                        ResultSet rss3=SQLHelper.getResultset(connectDB, "select level2_code, name from coal_level2 where level1_code='"+sub_code+"'");
                                        
                                        String level2Code=null, level2Name=null;
                                        
                                        while(rss3.next()){
                                            level2Code=rss3.getString(1);
                                            level2Name=rss3.getString(2);
                                            
                                            ResultSet rs4=SQLHelper.getResultset(connectDB, "select case when sum(debit-credit) is null then 0.00 else sum(debit-credit) end as case1 "
                                                    + "from general_ledger where accounting_code='"+level2Code+"' and entry_time::date<='"+endDate+"'");
                                            double level2Current=0.00;
                                            while(rs4.next()){
                                                level2Current=rs4.getDouble(1);
                                                current+=level2Current;
                                                currentAssets+=level2Current;
                                                
                                                System.err.println("Level 2 Value:  "+level2Current);
                                                System.err.println("Cumulative Value:   "+current);
                                            }
                                           
                                        }
                                        System.err.println("Final Value:    "+current);
                                        
                                        phrase=new Phrase(""+format.Format2Currency(String.valueOf(current)), pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        phrase=new Phrase(" ");
                                    table.addCell(phrase);
                                    
                                    }
                                    
                                }
                                
                            }
                            System.err.println("Current Assets: "+currentAssets);
                            
                            phrase=new Phrase(" ");
                            table.addCell(phrase);
                            
                            phrase=new Phrase(" ");
                            table.addCell(phrase);
                                    
                                    
                            phrase=new Phrase(""+format.Format2Currency(String.valueOf(currentAssets))+"", pFontHeader);
                            table.addCell(phrase);
                            
                            double liveAssets=0.00;
                            //LIVE ASSETS
//                            table.getDefaultCell().setColspan(3);
//                            phrase=new Phrase("LIVE ASSETS", pFontHeader2);
//                            table.addCell(phrase);
//                            
//                            table.getDefaultCell().setColspan(1);
//                            
//                            String liveAssetsCode=null, liveAssetsName=null;
//                            ResultSet rs133=SQLHelper.getResultset(connectDB, "select accounting_code, category from chart_of_accounts where accounting_code='C007'");
//                            while(rs133.next()){
//                                liveAssetsCode=rs133.getString(1);
//                                liveAssetsName=rs133.getString(2);
//                                
//                                System.err.println("Code: "+liveAssetsCode+" Name:   "+liveAssetsName);
//                               
//                                //level 1 categories
//                                ResultSet rs1=SQLHelper.getResultset(connectDB, "select sub_code, name, validator from coa_specifics "
//                                        + "where master_code='"+liveAssetsCode+"' order by 3 asc");
//                                
//                                String sub_code=null, name=null;
//                                while(rs1.next()){
//                                    sub_code=rs1.getString(1);
//                                    name=rs1.getString(2);
//                                    
//                                    System.err.println("Sub Code: "+sub_code);
//                                    System.err.println("Name:   "+name);
//                                    
//                                    phrase=new Phrase(name, pFontHeader1);
//                                    table.addCell(phrase);
//                                    
//                                    
//                                    //level 1 categories
//                                    
//                                    ResultSet rs2=SQLHelper.getResultset(connectDB, "select CASE when sum(debit-credit) is null then '0.00' else sum(debit-credit) end as trial from general_ledger "
//                                            + "where accounting_code='"+sub_code+"' and entry_time::date between '"+beginDate+"' and '"+endDate+"'");
//                                    double current=0.00;
//                                    while(rs2.next()){
//                                        current=rs2.getDouble(1);
//                                        liveAssets+=current;
//                                        //level 2 categories
//                                        System.err.println("Initial Current:    "+current);
//                                        System.err.println("Live:   "+liveAssets);
//                                        
//                                        ResultSet rss3=SQLHelper.getResultset(connectDB, "select level2_code, name from coal_level2 where level1_code='"+sub_code+"'");
//                                        String level2Code=null, level2Name=null;
//                                        
//                                        while(rss3.next()){
//                                            level2Code=rss3.getString(1);
//                                            level2Name=rss3.getString(2);
//                                            
//                                            ResultSet rs4=SQLHelper.getResultset(connectDB, "select sum(debit-credit) from general_ledger where accounting_code='"+level2Code+"' "
//                                                    + "and entry_time::date between '"+beginDate+"' and '"+endDate+"'");
//                                            double level2Current=0.00;
//                                            while(rs4.next()){
//                                                level2Current=rs4.getDouble(1);
//                                                current+=level2Current;
//                                                liveAssets+=level2Current;
//                                                
//                                                System.err.println("Level 2 Value:  "+level2Current);
//                                                System.err.println("Cumulative Value:   "+current);
//                                            }
//                                           
//                                        }
//                                        System.err.println("Final Value:    "+current);
//                                        
//                                        phrase=new Phrase(format.Format2Currency(String.valueOf(current)), pFontHeader1);
//                                        table.addCell(phrase);
//                                        
//                                        phrase=new Phrase(" ");
//                                        table.addCell(phrase);
//                                    
//                                    }
//                                    
//                                }
//                                
//                            }
//                            System.err.println("Live Assets: "+liveAssets);
//                            
//                            phrase=new Phrase(" ");
//                            table.addCell(phrase);
//                            
//                            phrase=new Phrase(" ");
//                            table.addCell(phrase);
//                                    
//                                    
//                            phrase=new Phrase(format.Format2Currency(String.valueOf(liveAssets)), pFontHeader);
//                            table.addCell(phrase);
                            
                            
                            
                            
                            phrase=new Phrase("TOTAL ASSETS", pFontHeader2);
                            table.addCell(phrase);
                            
                            phrase=new Phrase(" ");
                            table.addCell(phrase);
                            
                            phrase=new Phrase((format.Format2Currency(String.valueOf(fixedAssets+currentAssets+liveAssets))), pFontHeader2);
                            table.addCell(phrase);
                            
                            System.err.println("Total Assets "+(fixedAssets+currentAssets+liveAssets));
                            
                            
                            //CURRENT LIABILITIES
                            table.getDefaultCell().setColspan(3);
                            
                            phrase=new Phrase(" ");
                            table.addCell(phrase);
                            
                            phrase=new Phrase("CURRENT LIABILITIES", pFontHeader2);
                            table.addCell(phrase);
                            
                            table.getDefaultCell().setColspan(1);
                            
                            String masterCLCode=null, masterCLName=null;
                            
                            ResultSet rs6=SQLHelper.getResultset(connectDB, "select accounting_code, category from chart_of_accounts where accounting_code='C003'");
                            while(rs6.next()){
                                masterCLCode=rs6.getString(1);
                                masterCLName=rs6.getString(2);
                                
                                System.err.println("Desc: "+masterCLCode+"  Name:   "+masterCLName);
                                
                                ResultSet rs1=SQLHelper.getResultset(connectDB, "select sub_code, name, validator from coa_specifics "
                                        + "where master_code=(select accounting_code from chart_of_accounts where accounting_code='"+masterCLCode+"') order by 3 asc");
                                
                                String sub_code=null, name=null;
                                while(rs1.next()){
                                    sub_code=rs1.getString(1);
                                    name=rs1.getString(2);
                                    
                                    System.err.println("Sub Code: "+sub_code);
                                    System.err.println("Name:   "+name);
                                    
                                    phrase=new Phrase(name, pFontHeader1);
                                    table.addCell(phrase);
                                    
                                    
                                    //level 1 categories
                                    
                                    ResultSet rs2=SQLHelper.getResultset(connectDB, "select CASE when sum(credit-debit) is null then '0.00' else sum(credit-debit) end as trial from general_ledger "
                                            + "where accounting_code='"+sub_code+"' and entry_time::date<='"+endDate+"'");
                                    double currentLiab=0.00;
                                    while(rs2.next()){
                                        currentLiab=rs2.getDouble(1);
                                        currentLiabilities+=currentLiab;
                                        //level 2 categories
                                        ResultSet rss3=SQLHelper.getResultset(connectDB, "select level2_code, name from coal_level2 where level1_code='"+sub_code+"'");
                                        String level2Code=null, level2Name=null;
                                        
                                        while(rss3.next()){
                                            level2Code=rss3.getString(1);
                                            level2Name=rss3.getString(2);
                                            
                                            ResultSet rs4=SQLHelper.getResultset(connectDB, "select case when sum(credit-debit) is null then 0.00 else sum(credit-debit) end as case1 "
                                                    + "from general_ledger where accounting_code='"+level2Code+"' "
                                                    + "and entry_time::date<='"+endDate+"'");
                                            double level2Current=0.00;
                                            while(rs4.next()){
                                                level2Current=rs4.getDouble(1);
                                                currentLiabilities+=level2Current;
                                                currentLiab+=level2Current;
                                                
                                                System.err.println("Level 2 Value:  "+level2Current);
                                                System.err.println("Cumulative account value:   "+currentLiab);
                                            }
                                           
                                        }
                                        
                                        System.err.println("Final Value for the account:    "+currentLiab);
                                        phrase=new Phrase("("+format.Format2Currency(String.valueOf(currentLiab))+")", pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        phrase=new Phrase(" ");
                                        table.addCell(phrase);
                                    
                                    }
                                    
                                }
                            }
                            
                            System.err.println("Current Liabilities:    "+currentLiabilities);
                            
                            //NON-CURRENT LIABILITIES
                            table.getDefaultCell().setColspan(3);
                            phrase=new Phrase("NON-CURRENT LIABILITIES", pFontHeader2);
                            table.addCell(phrase);
                            
                            
                            String masterLLLCode=null, masterLLLName=null;
                            table.getDefaultCell().setColspan(1);
                            ResultSet rs9=SQLHelper.getResultset(connectDB, "select accounting_code, category from chart_of_accounts where accounting_code='C004'");
                            while(rs9.next()){
                                masterLLLCode=rs9.getString(1);
                                masterLLLName=rs9.getString(2);
                                System.err.println("Desc:   "+masterLLLCode+"   Name:   "+masterLLLName);
                                
                                ResultSet rs1=SQLHelper.getResultset(connectDB, "select sub_code, name, validator from coa_specifics "
                                        + "where master_code=(select accounting_code from chart_of_accounts where accounting_code='"+masterLLLCode+"') order by 3 asc");
                                
                                String sub_code=null, name=null;
                                while(rs1.next()){
                                    sub_code=rs1.getString(1);
                                    name=rs1.getString(2);
                                    
                                    System.err.println("Sub Code: "+sub_code);
                                    System.err.println("Name:   "+name);
                                    
                                    phrase=new Phrase(name, pFontHeader1);
                                    table.addCell(phrase);
                                    
                                    
                                    //level 1 categories
                                    
                                    ResultSet rs2=SQLHelper.getResultset(connectDB, "select CASE when sum(credit-debit) is null then '0.00' else sum(credit-debit) end as trial from general_ledger "
                                            + "where accounting_code='"+sub_code+"' and entry_time::date<='"+endDate+"'");
                                    double currentL=0.00;
                                    while(rs2.next()){
                                        currentL=rs2.getDouble(1);
                                        longLiabilities+=currentL;
                                        
                                        System.err.println("Current Liability for:  "+name+" is "+currentL);
                                        System.err.println("Cumulative value for long liabilities is "+longLiabilities);
                                        //level 2 categories
                                        ResultSet rss3=SQLHelper.getResultset(connectDB, "select level2_code, name from coal_level2 where level1_code='"+sub_code+"'");
                                        String level2Code=null, level2Name=null;
                                        
                                        while(rss3.next()){
                                            level2Code=rss3.getString(1);
                                            level2Name=rss3.getString(2);
                                            
                                            ResultSet rs4=SQLHelper.getResultset(connectDB, "select case when sum(credit-debit) is null then 0.00 else sum(credit-debit) end as case1 "
                                                    + "from general_ledger where accounting_code='"+level2Code+"' "
                                                    + "and entry_time::date<='"+endDate+"'");
                                            double level2Current=0.00;
                                            while(rs4.next()){
                                                level2Current=rs4.getDouble(1);
                                                longLiabilities+=level2Current;
                                                currentL+=level2Current;
                                                
                                                System.err.println("Level 2 Current:    "+level2Current);
                                                System.err.println("Cumulative value:   "+currentL);
                                            }
                                           
                                        }
                                        System.err.println("Final Account Value:    "+currentL);
                                        
                                        phrase=new Phrase("("+format.Format2Currency(String.valueOf(currentL))+")", pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        phrase=new Phrase(" ");
                                        table.addCell(phrase);
                                    
                                    }
                                    
                                }
                            }
                            System.err.println("Non-Current Liabilities:  "+longLiabilities);
                            
                            table.getDefaultCell().setColspan(3);
                            
                            phrase=new Phrase(" ");
                            table.addCell(phrase);
                            
                            table.getDefaultCell().setColspan(1);
                            
                            phrase=new Phrase("TOTAL LIABILITIES", pFontHeader);
                            table.addCell(phrase);
                            
                            phrase=new Phrase(" ");
                            table.addCell(phrase);
                            
                            
                            double totalLiabilities=longLiabilities+currentLiabilities;
                            
                            phrase=new Phrase("("+format.Format2Currency(String.valueOf(totalLiabilities))+")", pFontHeader);
                            table.addCell(phrase);
                            
                            table.getDefaultCell().setColspan(3);
                            
                            phrase=new Phrase(" ");
                            table.addCell(phrase);
                            
                            table.getDefaultCell().setColspan(1);
                            
                            double netAssets=(fixedAssets+currentAssets+liveAssets)-totalLiabilities;
                            
                            phrase=new Phrase("NET ASSETS", pFontHeader2);
                            table.addCell(phrase);
                            
                            phrase=new Phrase(" ");
                            table.addCell(phrase);
                            
                            table.getDefaultCell().setBorderColor(Color.RED);
                            phrase=new Phrase(""+format.Format2Currency(String.valueOf(netAssets))+"", pFontHeader2);
                            table.addCell(phrase);
                            
                            table.getDefaultCell().setColspan(3);
                            
                            table.getDefaultCell().setBorderColor(Color.BLACK);
                            
                            phrase=new Phrase(" ");
                            table.addCell(phrase);
                            
                            phrase=new Phrase("CAPITAL", pFontHeader2);
                            table.addCell(phrase);
                            
                            table.getDefaultCell().setColspan(1);
                            
                            
                            //CAPITAL & DRAWINGS
                            String masterCapital=null, masterCapitalName=null;
                            ResultSet rs12=SQLHelper.getResultset(connectDB, "select accounting_code, category from chart_of_accounts where accounting_code='C005'");
                            while(rs12.next()){
                                masterCapital=rs12.getString(1);
                                masterCapitalName=rs12.getString(2);
                                System.err.println("Desc:   "+masterCapital+" Name: "+masterCapitalName);
                                
                                ResultSet rs13=SQLHelper.getResultset(connectDB, "select sub_code, name, side from coa_specifics where master_code=(select accounting_code "
                                        + "from chart_of_accounts where accounting_code='"+masterCapital+"')");
                                
                                while(rs13.next()){
                                    String sub_code=rs13.getString(1);
                                    String name=rs13.getString(2);
                                    String side=rs13.getString(3);
                                    
                                    System.err.println("Sub Code:   "+sub_code+" Name:  "+name+" Side:  "+side);
                                    
                                    //capital
                                    if(side.equals("CREDIT")){
                                        
                                        phrase=new Phrase(name, pFontHeader1);
                                        table.addCell(phrase);

                                        
                                        double inCapital=0.00;
                                        ResultSet rs14=SQLHelper.getResultset(connectDB, "select  case when sum(credit-debit) is NULL then '0.00' else sum(credit-debit) end as trial "
                                                + "from general_ledger where accounting_code='"+sub_code+"' and entry_time::date<='"+endDate+"'");
                                        while(rs14.next()){
                                            inCapital=rs14.getDouble(1);
                                            capital+=inCapital;
                                        
                                        //level 2 categories
                                        ResultSet rss3=SQLHelper.getResultset(connectDB, "select level2_code, name from coal_level2 where level1_code='"+sub_code+"'");
                                        String level2Code=null, level2Name=null;
                                        
                                        
                                        while(rss3.next()){
                                            level2Code=rss3.getString(1);
                                            level2Name=rss3.getString(2);
                                            
                                            ResultSet rs4=SQLHelper.getResultset(connectDB, "select case when sum(credit-debit) is null then 0.00 else sum(credit-debit) end as case1 "
                                                    + "from general_ledger where accounting_code='"+level2Code+"' "
                                                    + "and entry_time::date<='"+endDate+"'");
                                            double level2Current=0.00;
                                            while(rs4.next()){
                                                level2Current=rs4.getDouble(1);
                                                inCapital+=level2Current;
                                                capital+=level2Current;
                                                
                                                System.err.println("Level II current amount:    "+level2Current);
                                                System.err.println("Level II cumulative:    "+inCapital);
                                            }
                                           
                                        }
                                        
                                        phrase=new Phrase(" ");
                                        table.addCell(phrase);
                                        
                                        System.err.println("Final value for all accounts:   "+inCapital);
                                        
                                        phrase=new Phrase(format.Format2Currency(String.valueOf(inCapital)), pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        }
                                    }
                                    //drawings
                                    else if(side.equals("DEBIT")){
                                        
                                        phrase=new Phrase(name, pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        double inDrawing=0.00;
                                        ResultSet rs14=SQLHelper.getResultset(connectDB, "select  case when sum(debit-credit) is NULL then '0.00' else sum(debit-credit) end as trial "
                                                + "from general_ledger where accounting_code='"+sub_code+"' and entry_time::date<='"+endDate+"'");
                                        while(rs14.next()){
                                            drawing=rs14.getDouble(1);
                                        }
                                        
                                        System.err.println("Level 1 Drawing:    "+drawing);
                                        
                                        //level 2 categories
                                        ResultSet rss3=SQLHelper.getResultset(connectDB, "select level2_code, name from coal_level2 where level1_code='"+sub_code+"'");
                                        String level2Code=null, level2Name=null;
                                        
                                        while(rss3.next()){
                                            level2Code=rss3.getString(1);
                                            level2Name=rss3.getString(2);
                                            
                                            ResultSet rs4=SQLHelper.getResultset(connectDB, "select case when sum(debit-credit) is null then 0.00 else sum(debit-credit) end as case1 "
                                                    + "from general_ledger where accounting_code='"+level2Code+"' "
                                                    + "and entry_time::date<='"+endDate+"'");
                                            double level2Current=0.00;
                                            while(rs4.next()){
                                                level2Current=rs4.getDouble(1);
                                                inDrawing+=level2Current;
                                                
                                                
                                                System.err.println("Level II Current:   "+level2Current);
                                                System.err.println("Level II cumulative:    "+inDrawing);
                                            }
                                           
                                        }
                                        
                                        drawing+=inDrawing;
                                        
                                        System.err.println("Total Drawing:  "+drawing);
                                        
                                        phrase=new Phrase(" ");
                                        table.addCell(phrase);
                                        
                                        phrase=new Phrase("("+format.Format2Currency(String.valueOf(drawing))+")", pFontHeader1);
                                        table.addCell(phrase);
                                        
                                        
                                    }
                                }
                                
                            }
                            System.err.println("Drawings:   "+drawing);
                            System.err.println("Capital:    "+capital);
                            
                            
                            
                            /*THE START OF NET PROFIT CALCULATION*/
                            
                            
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
                                    + "and entry_time::date<='"+endDate+"'");
//                              ResultSet rset=SQLHelper.getResultset(connectDB, "select sum(amount_disbursed) from loan_disbursements "
//                                    + "where disbursement_date between '"+beginDate+"' and '"+endDate+"'");
                                while(rset.next()){
                                    innerRevenue=rset.getDouble(1);
                                    revenue+=innerRevenue;
                                    
                                    if(innerRevenue!=0.00){
//                                        phrase=new Phrase(revenueName,  pFontHeader1);
//                                        table.addCell(phrase);
//
//                                        phrase=new Phrase(" ");
//                                        table.addCell(phrase);
//                                        
//                                        phrase=new Phrase(format.Format2Currency(String.valueOf(innerRevenue)), pFontHeader1);
//                                        table.addCell(phrase);
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
                                    + "and entry_time::date<='"+endDate+"'");
                                    while(rsett3.next()){
                                        
                                        level2Revenue=rsett3.getDouble(1);
                                        revenue+=level2Revenue;
                                        
                                        if(level2Revenue!=0.00){
//                                            phrase=new Phrase(level2Name, pFontHeader1);
//                                            table.addCell(phrase);
//
//                                            phrase=new Phrase(" ");
//                                            table.addCell(phrase);
//
//                                            phrase=new Phrase(format.Format2Currency(String.valueOf(level2Revenue)), pFontHeader1);
//                                            table.addCell(phrase);
                                        }
                                    }
                                }
                            }
                            
                            System.err.println("Revenue: "+revenue);
                            /*
                            
                            //return inwards
                            double returnIn=0.00;
                            ResultSet rset1=SQLHelper.getResultset(connectDB, "select case when SUM(debit-credit) is null then '0.00' else sum(debit-credit) END AS case_1 FROM general_ledger "
                                    + "WHERE accounting_code=(select sub_code from coa_specifics where sub_code='C009-0020') and entry_time::date<= '"+endDate+"'");
                            while(rset1.next()){
                                returnIn=rset1.getDouble(1);
                                
                                
                            }
                            System.err.println("Return Inwards: "+returnIn);
                            
                            //CARRIAGE INWARDS
                            double carriageInwards=0.00;
                            
                            ResultSet rset2=SQLHelper.getResultset(connectDB, "select case when SUM(credit-debit) is null then '0.00' else sum(credit-debit) end as trial FROM general_ledger "
                                    + "WHERE accounting_code=(select sub_code from coa_specifics where sub_code='C009-0022') "
                                    + "and entry_time::date<='"+endDate+"'");
                            while(rset2.next()){
                                carriageInwards=rset2.getDouble(1);
                                
                                
                            }
                            System.err.println("Carriage Inwards:  "+carriageInwards);
                            
                                
                            //return outwards
                            double returnOut=0.00;
                            ResultSet rset3=SQLHelper.getResultset(connectDB, "select case when SUM(credit-debit) is null then '0.00' else sum(credit-debit) END AS case_1 FROM general_ledger "
                                    + "WHERE accounting_code=(select sub_code from coa_specifics where sub_code='C008-0023') "
                                    + "and entry_time::date<='"+endDate+"'");
                            while(rset3.next()){
                                returnOut=rset3.getDouble(1);
                                
                                
                            }
                            System.err.println("Return outwards: "+returnOut);
                            */
                            
                            //calculate the gross profit
                            
                            
//                            double totalTrading=carriageInwards-returnOut;
//                            System.err.println("Trading Val: "+totalTrading);
                            
                            
                            
                            double grossProfit=revenue;     //-returnIn-totalTrading;
                            
                            
                            
                            
                            System.err.println("Gross Profit:   "+grossProfit);
                            
                            
                           
                            
                            
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
                                        + "entry_time::date<='"+endDate+"'");
                                double inRevenue=0.00;
                                while(rs88.next()){
                                    inRevenue=rs88.getDouble(1);
                                    
                                    
                                    
                                    recRevenue+=inRevenue;
                                    
                                    
                                    
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
                                            + "entry_time::date<='"+endDate+"'");
                                    
                                    while(rs10.next()){
                                        level2Rev=rs10.getDouble(1);
                                        
                                        System.err.println("Level 2 Revenue: "+level2Rev);
                                        
                                        recRevenue+=level2Rev;
                                        
                                        
                                    }
                                    
                                }
                            }
                            
                            
                            
                            
                            //Expenses Loop
                            String code=null, name=null;
                            double expenses=0.00;
                            ResultSet rset5=SQLHelper.getResultset(connectDB, "select sub_code, name  from coa_specifics "
                                    + "where sub_code in (select sub_Code from coa_specifics where master_code='C009') ");
                            while(rset5.next()){
                                code=rset5.getString(1);
                                name=rset5.getString(2);

                                System.err.println("Expense Code: "+code+",Expense Name: "+name);

                                //level1 expenses
                                ResultSet rset6=SQLHelper.getResultset(connectDB, "select case when SUM(debit-credit) is null then '0.00' else sum(debit-credit) end as trial from general_ledger where accounting_code='"+code+"'"
                                        + " and entry_time::date<='"+endDate+"'");
                                while(rset6.next()){
                                    double inExp=0.00;
                                    inExp=rset6.getDouble(1);
                                    
                                    System.err.println("In Expense: "+inExp);
                                    
                                    expenses+=inExp;
                                    
                                    
                                    System.err.println("Expense for "+name+":   "+inExp);
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
                                            + "and entry_time::date<='"+endDate+"'");
                                    while(rset12.next()){
                                        inExpense=rset12.getDouble(1);
                                        expenses+=inExpense;
                                        
                                        System.err.println("In Expense: "+inExpense);
                                        
                                        
                                    }
                                }
                            }
                            
                            
                            
                            
                            netProfit=(grossProfit+recRevenue)-expenses;
                            
                                                      
                            phrase=new Phrase("NET PROFIT AS AT ["+dateFormat.format(endDate)+"]", pFontHeader1);
                            table.addCell(phrase);
                            
                            phrase=new Phrase(" ");
                            table.addCell(phrase);
                            
                            if(netProfit<0.00){
                                phrase=new Phrase("("+format.Format2Currency(String.valueOf(netProfit))+")", pFontHeader1);
                                table.addCell(phrase);
                            }
                            else{
                                phrase=new Phrase(format.Format2Currency(String.valueOf(netProfit)), pFontHeader1);
                                table.addCell(phrase);
                            }
                            
                            double secondPart=(capital+netProfit)-drawing;
                            
                            phrase=new Phrase(" ");
                            table.addCell(phrase);
                            
                            phrase=new Phrase(" ");
                            table.addCell(phrase);
                            
                            table.getDefaultCell().setBorderColor(Color.RED);
                            phrase=new Phrase(format.Format2Currency(String.valueOf(secondPart)), pFontHeader2);
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
                    Logger.getLogger(StatementofFinancialPosPdf.class.getName()).log(Level.SEVERE, null, ex);
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







