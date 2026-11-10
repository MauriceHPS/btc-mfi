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


public class LoanArrearsPdf implements java.lang.Runnable{
  
     java.awt.Desktop deskTop = Desktop.getDesktop();
    
    DBObject dbObject;
    Format2Currency format;
    int numberSeq = 0;
    
    public static java.sql.Connection connectDB = null;
    
//    public java.lang.String dbUserName = null;
//    
//    org.netbeans.lib.sql.pool.PooledConnectionSource pConnDB = null;
    
    boolean threadCheck = true;
    
    java.lang.Thread threadSample;
    
    com.lowagie.text.Font pFontHeader = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD);
    com.lowagie.text.Font pFontHeader1 = FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL);
    
   
    java.lang.Runtime rtThreadSample = java.lang.Runtime.getRuntime();
    
    java.lang.Process prThread;
    AMSUtility label = new AMSUtility();
    private Date currentDate;
    public void LoanArrearsPdf() {
        connectDB = ConnectionDB.getInstance().getCon();
        
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
            com.lowagie.text.Document docPdf = new com.lowagie.text.Document(PageSize.A4.rotate());
            
            
                    
                    com.lowagie.text.pdf.PdfWriter.getInstance(docPdf, new java.io.FileOutputStream(tempFile));
                    
                    
                    String date = null;
                   
                        java.sql.Statement st4 = connectDB.createStatement();
                        
                        java.sql.ResultSet rset44 = st4.executeQuery("SELECT date(now()) as Date");
                        while(rset44.next())
                            date = rset44.getObject(1).toString();
                            
                        
//                        com.lowagie.text.HeaderFooter headerFoter = new com.lowagie.text.HeaderFooter(new Phrase(""+compName+""),false);// FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA, 14, Font.BOLDITALIC,java.awt.Color.blue)));
//                        headerFoter.setAlignment(com.lowagie.text.HeaderFooter.ALIGN_CENTER);
//                        headerFoter.setRight(5);
//                        docPdf.setHeader(headerFoter);

                        ResultSet rrset1=SQLHelper.getResultset(connectDB, "select current_date");
                        while(rrset1.next()){
                            currentDate=rrset1.getDate(1);
                        }
                    
                    
                    com.lowagie.text.HeaderFooter footer = new com.lowagie.text.HeaderFooter(new Phrase("Loans Arrears- Page: "), true);// FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA, 12, Font.BOLDITALIC,java.awt.Color.blue));
                    
                    docPdf.setFooter(footer);
                    
                    
                    docPdf.open();
                    
                    ReportUtil.addCenteredTitlePage(docPdf, connectDB);
                 
                        
                        
                        com.lowagie.text.pdf.PdfPTable table = new com.lowagie.text.pdf.PdfPTable(8);
                        
                        int headerwidths[] = {5, 10, 10, 25, 10, 15, 10, 15};
                        
                        table.setWidths(headerwidths);
                        
                        table.setWidthPercentage((100));
                        table.getDefaultCell().setBorder(Rectangle.BOTTOM);
                        
                        
                        
                        Phrase phrase; 
                        table.getDefaultCell().setColspan(8);
                        
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setBorder(PdfCell.TOP | PdfCell.BOTTOM | PdfCell.LEFT | PdfCell.RIGHT);
                        
                        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM);
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_CENTER);
                        
                        phrase=new Phrase("Loans Arrears", pFontHeader);
                        table.addCell(phrase);
                        
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
                        
                        phrase = new Phrase("Principal",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Loan Balance",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Arrears [On Installment(s)]",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Overdue Days",pFontHeader);
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                        
                        ResultSet rset1;
                        
                            java.sql.Statement st = connectDB.createStatement();
                            
                            //retrieve the loan no and member no for all receivable loans
                            rset1= st.executeQuery("select DISTINCT(other_identifier), member_no from debtors_ledger where paid = false GROUP BY 1, 2 "
                                    + "HAVING SUM(debit-credit)>0.00 ORDER BY 1");
                            
                            String memberNo=null, loanNo=null, names=null;
                            
                            
                            
                             int count=1;
                             
                             double principal=0.00, arrears=0.00, monthlyInstallment=0.00, cumArrears=0.00, amountPaid=0.00, balance=0.00, expectedAmount=0.00;
                            
                             Date deductionCommencement=null, applicationDate=null;
                            
                             while (rset1.next()) {
                                loanNo=rset1.getString(1);
                                memberNo=rset1.getString(2);
                                
                                System.err.println("Loan No:   "+loanNo);
                                System.err.println("Member No: "+memberNo);
                                
                                //retrieve the personal details
                                ResultSet rset2=SQLHelper.getResultset(connectDB, "select names, amount_applied, deduction_commencement, monthly_installment, "
                                        + "application_date from loan_registration l, members_register m where m.member_no=l.member_no "
                                        + "and loan_no ='"+loanNo+"' and l.member_no='"+memberNo+"' ");
                                while(rset2.next()){
                                    names=rset2.getString(1);
                                    principal=rset2.getDouble(2);
                                    deductionCommencement=rset2.getDate(3);
                                    monthlyInstallment=rset2.getDouble(4);
                                    applicationDate=rset2.getDate(5);
                                    
                                    System.err.println("Names:  "+names);
                                    System.err.println("Principal:  "+principal);
                                    System.err.println("Deduction Commencement: "+deductionCommencement);
                                    System.err.println("Monthly Installment:    "+monthlyInstallment);
                                    System.err.println("Application Date: "+applicationDate);
                                    
                                    //get the number of dates that have been charged interest
                                    int countt=0;
                                    ResultSet rset3=SQLHelper.getResultset(connectDB, "select count(scheduled_payment_date) from scheduling_payments_management "
                                            + "where loan_no='"+loanNo+"' and member_no='"+memberNo+"' and scheduled_payment_date<=current_Date");
                                    while(rset3.next()){
                                        countt=rset3.getInt(1);
                                    }
                                    
                                    
                                    
                                    System.err.println("Scheduled Months on or before the current date:    "+countt);
                                    
                                    Date scheduledPaymentDate=null;
                                    
                                    //if the charged interest months is greater than zero
                                    if(countt>0){
                                        double actualLoanBalance=0.00;
                                        ResultSet rr11=SQLHelper.getResultset(connectDB, "select case when sum(debit-credit) is null then 0.00 else sum(debit-credit) end as case1 "
                                                + "from debtors_ledger where member_no='"+memberNo+"' and other_identifier='"+loanNo+"' ");
                                        while(rr11.next()){
                                            actualLoanBalance=rr11.getDouble(1);
                                        }
                                        
                                        //total receivable amount as at the current date
                                        expectedAmount=countt*monthlyInstallment;
                                        
                                        System.err.println("Expected Amount:    "+expectedAmount);
                                        
                                        int counter=0;
                                        
                                        ResultSet rset5=SQLHelper.getResultset(connectDB, "select case when sum(credit) is null then 0.00 else sum(credit) end as case1 "
                                                + "from debtors_ledger where date::date<=current_date and other_identifier='"+loanNo+"' "
                                                        + "and member_no='"+memberNo+"' ");
                                        while(rset5.next()){
                                            amountPaid=rset5.getDouble(1);
                                        }
                                        System.err.println("Amount Paid on or before the first scheduled date:    "+amountPaid);
                                        
                                        balance=expectedAmount-amountPaid;
                                        
                                        System.err.println("Balance on installments:    "+balance);
                                        
                                        Date tester=null;
                                        
                                        if(balance>0.00){
                                            arrears=expectedAmount-amountPaid;
                                            cumArrears+=arrears;

                                            System.err.println("Total Arrears:    "+arrears);

                                            int overDueDays=0;
                                            
                                            if(countt==1){
                                                ResultSet rset4=SQLHelper.getResultset(connectDB, "select scheduled_payment_date from scheduling_payments_management "
                                                        + "where loan_no='"+loanNo+"' and member_no='"+memberNo+"' order by 1 asc limit 1");
                                                while(rset4.next()){
                                                    scheduledPaymentDate=rset4.getDate(1);
                                                }
                                                ResultSet rset6=SQLHelper.getResultset(connectDB, "select current_date - '"+scheduledPaymentDate+"' ");
                                                while(rset6.next()){
                                                    overDueDays=rset6.getInt(1);
                                                }
                                            }
                                            
                                            else{
                                                if(amountPaid<monthlyInstallment){
                                                    ResultSet rset6=SQLHelper.getResultset(connectDB, "select current_date - interval '"+(countt*30)+" days' ");
                                                    while(rset6.next()){
                                                       tester=rset6.getDate(1);
                                                    }
                                                    
                                                    ResultSet rset66=SQLHelper.getResultset(connectDB, "select current_date - '"+tester+" ' ");
                                                    while(rset66.next()){
                                                       overDueDays=rset66.getInt(1);
                                                    }
                                                }
                                                else{
                                                    double ratio=Math.floor(amountPaid/monthlyInstallment);
                                                    ResultSet rset6=SQLHelper.getResultset(connectDB, "select current_date - interval '"+(ratio*30)+" days' ");
                                                    while(rset6.next()){
                                                        tester=rset6.getDate(1);
                                                    }
                                                    
                                                    ResultSet rset66=SQLHelper.getResultset(connectDB, "select current_date - '"+tester+" ' ");
                                                    while(rset66.next()){
                                                       overDueDays=rset66.getInt(1);
                                                    }
                                                }
                                            }
                                            
                                            

                                            System.err.println("Overdue Days:   "+overDueDays);

                                            phrase=new Phrase(""+count+"", pFontHeader1);
                                            table.addCell(phrase);

                                            phrase=new Phrase(""+loanNo+"", pFontHeader1);
                                            table.addCell(phrase);

                                            phrase=new Phrase(""+memberNo+"", pFontHeader1);
                                            table.addCell(phrase);

                                            phrase=new Phrase(""+names+"", pFontHeader1);
                                            table.addCell(phrase);

                                            phrase=new Phrase(format.Format2Currency(String.valueOf(principal)), pFontHeader1);
                                            table.addCell(phrase);

                                            phrase=new Phrase(format.Format2Currency(String.valueOf(actualLoanBalance)), pFontHeader1);
                                            table.addCell(phrase);

                                            phrase=new Phrase(format.Format2Currency(String.valueOf(arrears)), pFontHeader1);
                                            table.addCell(phrase);

                                            phrase=new Phrase(""+overDueDays+"", pFontHeader1);
                                            table.addCell(phrase);

                                            count++;
                                        }
                                        
//                                        ResultSet rset4=SQLHelper.getResultset(connectDB, "select scheduled_payment_date from scheduling_payments_management "
//                                                + "where loan_no='"+loanNo+"' and member_no='"+memberNo+"' and scheduled_payment_date<=current_Date");
//                                        while(rset4.next()){
//                                            scheduledPaymentDate=rset4.getDate(1);
//                                            
//                                            System.err.println("Scheduled Payment Date: "+scheduledPaymentDate);
//                                            
//                                            //between '"+applicationDate+"' and 
//                                            if(counter==0){
//                                                
//                                                
//                                                balance=principal-amountPaid;
//                                                
//                                                System.err.println("Balance:    "+balance);
//                                                
//                                                
//                                                counter++;
//                                            }
//                                            
//                                            else{
//                                                ResultSet rset7=SQLHelper.getResultset(connectDB, "select case when sum(credit) is null then 0.00 else sum(credit) end as case1 "
//                                                + "from debtors_ledger "
//                                                        + "where date::date between  '"+scheduledPaymentDate+"'::date - interval '29 days' and  '"+scheduledPaymentDate+"'  "
//                                                                + "and other_identifier='"+loanNo+"' and member_no='"+memberNo+"' ");
//                                                while(rset7.next()){
//                                                    amountPaid=rset7.getDouble(1);
//                                                }
//                                                
////                                                ResultSet rset5=SQLHelper.getResultset(connectDB, "select case when sum(credit) is null then 0.00 else sum(credit) end as case1 "
////                                                + "from debtors_ledger where date::date between  '"+scheduledPaymentDate+"'::date - interval '30 days' and  '"+scheduledPaymentDate+"' "
////                                                + "and other_identifier='"+loanNo+"' and member_no='"+memberNo+"' ");
////                                                while(rset5.next()){
////                                                    amountPaid=rset5.getDouble(1);
////                                                }
//                                                
//                                                System.err.println("Amount Paid btn [scheduled date-29] and scheduled date:    "+amountPaid);
//                                                
//                                                balance=principal-amountPaid;
//                                                
//                                                System.err.println("Balance:    "+balance);
//                                                
//                                                if(balance>0.00){
//                                                    if(amountPaid<monthlyInstallment){
//                                                        arrears=monthlyInstallment-amountPaid;
//                                                        cumArrears+=arrears;
//
//                                                        System.err.println("Arrears for month "+(counter+1)+":    "+arrears);
//
//                                                        int overDueDays=0;
//                                                        ResultSet rset6=SQLHelper.getResultset(connectDB, "select current_date - '"+scheduledPaymentDate+"' ");
//                                                        while(rset6.next()){
//                                                            overDueDays=rset6.getInt(1);
//                                                        }
//                                                        
//                                                        System.err.println("Overdue Days:   "+overDueDays);
//
//                                                        phrase=new Phrase(""+count+"", pFontHeader1);
//                                                        table.addCell(phrase);
//
//                                                        phrase=new Phrase(""+loanNo+"", pFontHeader1);
//                                                        table.addCell(phrase);
//
//                                                        phrase=new Phrase(""+memberNo+"", pFontHeader1);
//                                                        table.addCell(phrase);
//
//                                                        phrase=new Phrase(""+names+"", pFontHeader1);
//                                                        table.addCell(phrase);
//
//                                                        phrase=new Phrase(format.Format2Currency(String.valueOf(principal)), pFontHeader1);
//                                                        table.addCell(phrase);
//
//                                                        phrase=new Phrase(format.Format2Currency(String.valueOf(balance)), pFontHeader1);
//                                                        table.addCell(phrase);
//
//                                                        phrase=new Phrase(format.Format2Currency(String.valueOf(arrears)), pFontHeader1);
//                                                        table.addCell(phrase);
//
//                                                        phrase=new Phrase(""+overDueDays+"", pFontHeader1);
//                                                        table.addCell(phrase);
//                                                        
//                                                        count++;
//                                                    }
//                                                }
//                                            }
//                                        }
                                    }//no of scheduled dates
                                }//details
                                
                            }//loan member
                            
                            table.getDefaultCell().setColspan(8);
                            
                            table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
                            
                            phrase=new Phrase("Total Arrears: "+dbObject.getDBObject(format.Format2Currency(String.valueOf(cumArrears)), "-"), pFontHeader);
                            table.addCell(phrase);
                            
                             docPdf.add(table);
                            
            
            docPdf.close();
            
            deskTop.open(tempFile);
            
            
            
            
        } catch(java.io.IOException IOexec) {
            
            javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), IOexec.getMessage());
            
        } catch (DocumentException | SQLException ex) {
             Logger.getLogger(LoanArrearsPdf.class.getName()).log(Level.SEVERE, null, ex);
         }
        
    }
   
}