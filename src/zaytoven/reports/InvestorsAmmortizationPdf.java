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

import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfCell;
import java.awt.Desktop;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import zaytoven.controller.AMSUtility;
import zaytoven.controller.ConnectionDB;
import zaytoven.controller.DBObject;
import zaytoven.controller.Format2Currency;
import zaytoven.controller.ReportUtil;
import zaytoven.controller.SQLHelper;


public class InvestorsAmmortizationPdf implements java.lang.Runnable{
  
     java.awt.Desktop deskTop = Desktop.getDesktop();
    
    DBObject dbObject;
    
    int numberSeq = 0;
    
    public static java.sql.Connection connectDB = null;
    
//    public java.lang.String dbUserName = null;
//    
//    org.netbeans.lib.sql.pool.PooledConnectionSource pConnDB = null;
    
    boolean threadCheck = true;
    
    java.lang.Thread threadSample;
    
    com.lowagie.text.Font pFontHeader = FontFactory.getFont(FontFactory.TIMES_ROMAN, 13, Font.BOLD);
    com.lowagie.text.Font pFontHeader2 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD);
    com.lowagie.text.Font pFontHeader1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL);
    
   
    java.lang.Runtime rtThreadSample = java.lang.Runtime.getRuntime();
    
    java.lang.Process prThread;
    AMSUtility label = new AMSUtility();
    double principal, rate, amountPayable, totalInterest, scheduledPayment, time, annualPayments;
    int tableDuration;
    Date startDate;
    Format2Currency format;
    String memberID, memberName;
    public void InvestorsAmmortizationPdf(double p, double r, double t, double i, Date start, double installment, double AP, 
            double interest, int td, String id, String name) {
        connectDB = ConnectionDB.getInstance().getCon();
        principal=p;
        rate=r;
        time=t;
        annualPayments=i;
        startDate=start;
        scheduledPayment=installment;
        amountPayable=AP;
        totalInterest=interest;
        memberID=id;
        memberName=name;
        
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
            
                    
            com.lowagie.text.pdf.PdfWriter.getInstance(docPdf, new java.io.FileOutputStream(tempFile));


            String date = null;
                java.sql.Statement st4 = connectDB.createStatement();

                java.sql.ResultSet rset4 = st4.executeQuery("SELECT date(now()) as Date");
                while(rset4.next())
                    date = rset4.getObject(1).toString();

//                        com.lowagie.text.HeaderFooter headerFoter = new com.lowagie.text.HeaderFooter(new Phrase(""+compName+""),false);// FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA, 14, Font.BOLDITALIC,java.awt.Color.blue)));
//                        headerFoter.setAlignment(com.lowagie.text.HeaderFooter.ALIGN_CENTER);
//                        headerFoter.setRight(5);
//                        docPdf.setHeader(headerFoter);


            com.lowagie.text.HeaderFooter footer = new com.lowagie.text.HeaderFooter(new Phrase("Investment Ammortization Schedule- Page: "), true);// FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA, 12, Font.BOLDITALIC,java.awt.Color.blue));

            docPdf.setFooter(footer);


            docPdf.open();

            ReportUtil.addCenteredTitlePage(docPdf, connectDB);

                com.lowagie.text.pdf.PdfPTable table = new com.lowagie.text.pdf.PdfPTable(6);

                int headerwidths[] = {13, 16, 25, 14, 14, 18};

                table.setWidths(headerwidths);

                table.setWidthPercentage((100));
                table.getDefaultCell().setBorder(Rectangle.BOTTOM);



                Phrase phrase; 
                table.getDefaultCell().setColspan(6);

                phrase=new Phrase(" ");
                table.addCell(phrase);
                
                table.getDefaultCell().setBorder(PdfCell.TOP | PdfCell.BOTTOM | PdfCell.LEFT | PdfCell.RIGHT);
                
                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM);
                
                table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_CENTER);
                phrase=new Phrase("Investment Ammortization Schedule", pFontHeader);
                table.addCell(phrase);

                table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
                phrase = new Phrase("Printed On  :" +date , pFontHeader);
                table.addCell(phrase);
                
                
                table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT); 

                table.getDefaultCell().setColspan(3);
                
                phrase=new Phrase("Investor ID:   "+memberID, pFontHeader2);
                table.addCell(phrase);
                
                phrase=new Phrase("Investor Name:   "+memberName, pFontHeader2);
                table.addCell(phrase);
                
                phrase=new Phrase(" ");
                table.addCell(phrase);
                
                phrase=new Phrase(" ");
                table.addCell(phrase);

                phrase=new Phrase("Principal:   "+format.Format2Currency(String.valueOf(principal)), pFontHeader);
                table.addCell(phrase);

                phrase=new Phrase("Interest Rate:   "+format.Format2Currency(String.valueOf(rate)), pFontHeader);
                table.addCell(phrase);

                phrase=new Phrase("Duration (months):   "+time, pFontHeader);
                table.addCell(phrase);

                phrase=new Phrase("Start Date:   "+dateFormat.format(startDate), pFontHeader);
                table.addCell(phrase);
                
                phrase=new Phrase("Duration (months):   "+time, pFontHeader);
                table.addCell(phrase);


                table.getDefaultCell().setColspan(6);

                phrase=new Phrase(" ");
                table.addCell(phrase);

                table.getDefaultCell().setColspan(3);

                phrase=new Phrase("Scheduled Installment:   "+format.Format2Currency(String.valueOf(scheduledPayment)) , pFontHeader);
                table.addCell(phrase);

                phrase=new Phrase("Total Interest:  "+format.Format2Currency(String.valueOf(totalInterest)), pFontHeader);
                table.addCell(phrase);

                phrase=new Phrase("Amount Payable:  "+format.Format2Currency(String.valueOf(amountPayable)), pFontHeader);
                table.addCell(phrase);
                
                phrase=new Phrase(" ");
                table.addCell(phrase);
                
                phrase=new Phrase(" ");
                table.addCell(phrase);
                
                phrase=new Phrase(" ");
                table.addCell(phrase);

                table.getDefaultCell().setColspan(6);

                phrase=new Phrase(" ");
                table.addCell(phrase);

                table.getDefaultCell().setColspan(1);

                phrase = new Phrase("Month No",pFontHeader);
                table.addCell(phrase);

                phrase = new Phrase("Beginning Bal",pFontHeader);
                table.addCell(phrase);

                phrase = new Phrase("Scheduled Payment",pFontHeader);
                table.addCell(phrase);

                phrase = new Phrase("Principal",pFontHeader);
                table.addCell(phrase);
                
                phrase = new Phrase("Interest",pFontHeader);
                table.addCell(phrase);

//                phrase = new Phrase("Net Balance",pFontHeader);
//                table.addCell(phrase);
//
//                phrase = new Phrase("Interest Recovered",pFontHeader);
//                table.addCell(phrase);

                phrase = new Phrase("Payment Date",pFontHeader);
                table.addCell(phrase);

                table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);

                int noofDays=(int) (360/annualPayments);

                double beginningBal=0.00, netBalance=0.00, innerInterest=0.00, innerPrincipal=0.00, interestRecovered=0.00;
                Date innerDate=null;
                
                this.tableDuration=(int) ((annualPayments/12)*time);
                System.err.println("Table Duration: "+tableDuration);
                
                System.err.println("Start Date: "+startDate);

                SimpleDateFormat df=new SimpleDateFormat("dd-MM-yyyy");
                
                for(int i=1;i<=tableDuration;i++){
                    //month
                    phrase=new Phrase(""+i+"", pFontHeader1);
                    table.addCell(phrase);

                    double ratePerPaymentPeriod=((rate/100)*12)/annualPayments;
                    
                    BigDecimal bg;

                    //for the first
                    if(i==1){
                        beginningBal=principal;
                        //monthly installment is constant for the whole period
                        innerInterest=ratePerPaymentPeriod*beginningBal;
                        innerPrincipal=scheduledPayment-innerInterest;
                        netBalance=beginningBal-innerPrincipal;
                        interestRecovered+=innerInterest;

                        String d=df.format(startDate);
                            innerDate=df.parse(d);


                        System.err.println("Formatted dateChooserField2.getDate():   "+innerDate);

                        phrase=new Phrase(""+format.Format2Currency(String.valueOf(beginningBal))+"", pFontHeader1);
                        table.addCell(phrase);

                        phrase=new Phrase(""+format.Format2Currency(String.valueOf(scheduledPayment))+"", pFontHeader1);
                        table.addCell(phrase);

                        phrase=new Phrase(""+format.Format2Currency(String.valueOf(innerPrincipal))+"", pFontHeader1);
                        table.addCell(phrase);

                        phrase=new Phrase(""+format.Format2Currency(String.valueOf(innerInterest))+"", pFontHeader1);
                        table.addCell(phrase);

//                        phrase=new Phrase(""+format.Format2Currency(String.valueOf(netBalance))+"", pFontHeader1);
//                        table.addCell(phrase);
//
//                        phrase=new Phrase(""+format.Format2Currency(String.valueOf(interestRecovered))+"", pFontHeader1);
//                        table.addCell(phrase);

                        phrase=new Phrase(""+d+"", pFontHeader1);
                        table.addCell(phrase);
                    }
                    else{
                        beginningBal=netBalance;
                        innerInterest=ratePerPaymentPeriod*beginningBal;
                        innerPrincipal=scheduledPayment-innerInterest;
                        netBalance=beginningBal-innerPrincipal;
                        interestRecovered+=innerInterest;
                        
                        bg=new BigDecimal(beginningBal);
                        bg=bg.setScale(2, BigDecimal.ROUND_UP);
                        beginningBal=bg.doubleValue();

                        bg=new BigDecimal(innerInterest);
                        bg=bg.setScale(2, BigDecimal.ROUND_UP);
                        innerInterest=bg.doubleValue();

                        bg=new BigDecimal(innerInterest);
                        bg=bg.setScale(2, BigDecimal.ROUND_UP);
                        innerInterest=bg.doubleValue();

                        bg=new BigDecimal(innerPrincipal);
                        bg=bg.setScale(2, BigDecimal.ROUND_UP);
                        innerPrincipal=bg.doubleValue();

                        bg=new BigDecimal(netBalance);
                        bg=bg.setScale(2, BigDecimal.ROUND_UP);
                        netBalance=bg.doubleValue();
                        
                        bg=new BigDecimal(interestRecovered);
                        bg=bg.setScale(2, BigDecimal.ROUND_UP);
                        interestRecovered=bg.doubleValue();

                        //increment row i-1 date by 1 month;
                        ResultSet rs=SQLHelper.getResultset(connectDB, "select (date '"+innerDate+"' + integer '"+noofDays+"')::date");
                        while(rs.next()){
                            innerDate=rs.getDate(1);
                        }

                        phrase=new Phrase(""+format.Format2Currency(String.valueOf(beginningBal))+"", pFontHeader1);
                        table.addCell(phrase);

                        phrase=new Phrase(""+format.Format2Currency(String.valueOf(scheduledPayment))+"", pFontHeader1);
                        table.addCell(phrase);

                        phrase=new Phrase(""+format.Format2Currency(String.valueOf(innerPrincipal))+"", pFontHeader1);
                        table.addCell(phrase);

                        phrase=new Phrase(""+format.Format2Currency(String.valueOf(innerInterest))+"", pFontHeader1);
                        table.addCell(phrase);

//                        phrase=new Phrase(""+format.Format2Currency(String.valueOf(netBalance))+"", pFontHeader1);
//                        table.addCell(phrase);
//
//                        phrase=new Phrase(""+format.Format2Currency(String.valueOf(interestRecovered))+"", pFontHeader1);
//                        table.addCell(phrase);

                        phrase=new Phrase(""+df.format(innerDate)+"", pFontHeader1);
                        table.addCell(phrase);
                    }
                }
                
                table.getDefaultCell().setColspan(8);
                
                phrase=new Phrase(" ");
                table.addCell(phrase);
                
                phrase=new Phrase("Signature: .................................................................................................", pFontHeader2);
                table.addCell(phrase);
                
                phrase=new Phrase(" ");
                table.addCell(phrase);
                
                phrase=new Phrase("Date: ....................................................................", pFontHeader2);
                table.addCell(phrase);
                
                docPdf.add(table);


                docPdf.close();

                deskTop.open(tempFile);
            } catch(java.sql.SQLException SqlExec) {

                javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), SqlExec.getMessage());

            } catch (IOException | DocumentException | ParseException ex) {
             Logger.getLogger(InvestorsAmmortizationPdf.class.getName()).log(Level.SEVERE, null, ex);
         }
        
    }
    
    
    
}







