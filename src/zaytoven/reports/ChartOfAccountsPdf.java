/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zaytoven.reports;

import com.lowagie.text.BadElementException;
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
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import zaytoven.controller.AMSUtility;
import zaytoven.controller.DBObject;
import zaytoven.controller.ReportUtil;
import zaytoven.controller.SQLHelper;

/**
 *
 * @author Helmut
 */
public class ChartOfAccountsPdf implements java.lang.Runnable{
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
    
    com.lowagie.text.Font pFontHeader = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.BOLD);
    com.lowagie.text.Font pFontHeader1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, Font.NORMAL);
    com.lowagie.text.Font pFontHeader4 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD);
   
    java.lang.Runtime rtThreadSample = java.lang.Runtime.getRuntime();
    
    java.lang.Process prThread;
    AMSUtility label = new AMSUtility();
    
    public void ChartOfAccountsPdf(java.sql.Connection connDb) {
        
        connectDB = connDb;
        
        dbObject = new DBObject();
        
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
            com.lowagie.text.Document docPdf = new com.lowagie.text.Document(PageSize.A4);
            
            
                    
                    com.lowagie.text.pdf.PdfWriter.getInstance(docPdf, new java.io.FileOutputStream(tempFile));
//                    ReportUtil.addCenteredTitlePage(docPdf, connectDB);
                    
                    String date = null;
                      java.sql.Statement st3 = connectDB.createStatement();
                        java.sql.Statement st4 = connectDB.createStatement();
                        
                        java.sql.ResultSet rset4 = st4.executeQuery("SELECT date(now()) as Date");
                        
                        while(rset4.next())
                            date = rset4.getObject(1).toString();
                        
                    
                    
                    com.lowagie.text.HeaderFooter footer = new com.lowagie.text.HeaderFooter(new Phrase("Chart of Accounts- Page: "), true);// FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA, 12, Font.BOLDITALIC,java.awt.Color.blue));
                    //+new ReportUtil().addFooterNote()
                    docPdf.setFooter(footer);
                    
                    
                    docPdf.open();
                    
                    ReportUtil.addCenteredTitlePage(docPdf, connectDB);
                    
                        
                        
                        com.lowagie.text.pdf.PdfPTable table = new com.lowagie.text.pdf.PdfPTable(2);
                        
                        int headerwidths[] = {50, 50};
                        
                        table.setWidths(headerwidths);
                        
                        table.setWidthPercentage((100));
                        table.getDefaultCell().setBorder(Rectangle.BOTTOM);
                        
                        Phrase phrase; 
                        
                        table.getDefaultCell().setColspan(2);
                        
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setBorder(PdfCell.TOP | PdfCell.BOTTOM | PdfCell.LEFT | PdfCell.RIGHT);
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_CENTER);
                        phrase=new Phrase("CHART OF ACCOUNTS", pFontHeader4);
                        table.addCell(phrase);                        
                        
//                        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM);
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
                        phrase = new Phrase("Printed On  :" +date , pFontHeader);
                        table.addCell(phrase);
                        
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_CENTER);
                        
                        String masterCode=null, level1Code=null, level2Code=null;
                        ResultSet rs=SQLHelper.getResultset(connectDB, "select accounting_code, category from chart_of_accounts");
                        while(rs.next()){
                            masterCode=rs.getString(1);
                            
                            table.getDefaultCell().setColspan(2);
                            table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_CENTER);
                            
                            //master details
                            phrase=new Phrase("CODE:     "+masterCode+"   NAME:    "+rs.getString(2), pFontHeader4);
                            table.addCell(phrase);
                            
                            phrase=new Phrase(" ");
                            table.addCell(phrase);
                            
                            //level 1 items
                            table.getDefaultCell().setColspan(1);
                            table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                            
                            phrase=new Phrase("CODE", pFontHeader);
                            table.addCell(phrase);
                            
                            phrase=new Phrase("NAME", pFontHeader);
                            table.addCell(phrase);
                            
                            ResultSet rs1=SQLHelper.getResultset(connectDB, "select sub_code, name from coa_specifics where master_code='"+masterCode+"'");
                            while(rs1.next()){
                                level1Code=rs1.getString(1);
                                
                                phrase=new Phrase(level1Code, pFontHeader1);
                                table.addCell(phrase);
                                
                                phrase=new Phrase(dbObject.getDBObject(rs1.getObject(2), "-"), pFontHeader1);
                                table.addCell(phrase);
                                //level 2 items
                                
                                ResultSet rs2=SQLHelper.getResultset(connectDB, "select level2_code, name from coal_level2 where level1_code='"+level1Code+"'");
                                while(rs2.next()){
                                    level2Code=rs2.getString(1);
                                    
                                    phrase=new Phrase("***"+level2Code, pFontHeader1);
                                    table.addCell(phrase);
                                    
                                    phrase=new Phrase("***"+dbObject.getDBObject(rs2.getString(2), "-"), pFontHeader1);
                                    table.addCell(phrase);
                                }
                            }
                        }
                        
                        
                         
                         
                         docPdf.add(table);
                        
                      
                    
                
            try {
                        docPdf.close();
                         deskTop.open(tempFile);

                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(new JFrame(), e.getMessage());

                    }
            
            
        } catch(java.io.IOException IOexec) {
            
            javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), IOexec.getMessage());
            
        } catch (DocumentException | SQLException ex) {
             Logger.getLogger(ChartOfAccountsPdf.class.getName()).log(Level.SEVERE, null, ex);
         }
        
    }
}
