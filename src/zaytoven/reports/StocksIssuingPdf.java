/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zaytoven.reports;

import com.lowagie.text.BadElementException;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import zaytoven.controller.AMSUtility;
import zaytoven.controller.DBObject;
import zaytoven.controller.Format2Currency;
import zaytoven.controller.ReportUtil;

/**
 *
 * @author Helmut
 */
public class StocksIssuingPdf implements java.lang.Runnable{
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
    
    com.lowagie.text.Font pFontHeader = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD);
    com.lowagie.text.Font pFontHeader1 = FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL);
    
   
    java.lang.Runtime rtThreadSample = java.lang.Runtime.getRuntime();
    
    java.lang.Process prThread;
    AMSUtility label = new AMSUtility();
    private String issue;
    
    Format2Currency format;
    
    public void StocksIssuingPdf(java.sql.Connection connDb, String issue_no) {
        this.issue=issue_no;
        connectDB = connDb;
        
        format=new Format2Currency();
        
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
                    
                    com.lowagie.text.HeaderFooter footer = new com.lowagie.text.HeaderFooter(new Phrase("Issuing List - Page: "), true);// FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA, 12, Font.BOLDITALIC,java.awt.Color.blue));
                    
                    docPdf.setFooter(footer);
                    
                    
                    docPdf.open();
                    
                    ReportUtil.addCenteredTitlePage(docPdf, connectDB);
                    try {
                        
                        
                        com.lowagie.text.pdf.PdfPTable table = new com.lowagie.text.pdf.PdfPTable(8);
                        
                        int headerwidths[] = {10, 12, 20, 10, 15, 12, 10, 11};
                        
                        table.setWidths(headerwidths);
                        
                        table.setWidthPercentage((100));
                        table.getDefaultCell().setBorder(Rectangle.BOTTOM);
                        
                        table.getDefaultCell().setColspan(4);
                        
                        
                        Phrase phrase; 
                        
                        
                        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM);
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                        phrase=new Phrase("Items Issued");
                        table.addCell(phrase);
                        table.getDefaultCell().setColspan(4);
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
                        phrase = new Phrase("Printed On  :" +date , pFontHeader);
                        table.addCell(phrase);
                        
                        String receive_nuos=null;
                        
                        Statement sta=connectDB.createStatement();
                        ResultSet rst=sta.executeQuery("select issue_no from st_receiving_issuing where issue_no='"+issue+"'");
                        while(rst.next()){
                        receive_nuos=rst.getString(1);
                        }
                        table.getDefaultCell().setColspan(8);
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                        
                        phrase=new Phrase(receive_nuos, pFontHeader);
                        table.addCell(phrase);
                        table.getDefaultCell().setColspan(1);
                        table.getDefaultCell().setBorder(PdfCell.TOP | PdfCell.BOTTOM | PdfCell.LEFT | PdfCell.RIGHT);
                         phrase = new Phrase("Count",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Item Code",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Item Name",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Qty_issued",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Receiving Department",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Received By",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Issued By",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Issue Date",pFontHeader);
                        table.addCell(phrase);
                        
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
                        
                         table.getDefaultCell().setBackgroundColor(java.awt.Color.WHITE);
                       
                        try {
                            /*Statement s=connectDB.createStatement();
                            ResultSet r=s.executeQuery("SELECT  sum(ord.price*quantity_received) total  FROM st_receiving_issuing REV, stock_orders ord, stock_items itm "
                                    + "where receiving_no='"+issue+"' and itm.item_code=ord.item_code and itm.item_code=rev.item_code");*/
                            
                            java.sql.Statement st = connectDB.createStatement();
                            
                            java.sql.ResultSet rset = st.executeQuery("select st.item_code, item_name, quantity_issued, receiving_dept, receiver, issuer, issuing_date from st_receiving_issuing st,"
                                    + " stock_items itm where issue_no='"+issue+"'and itm.item_code=st.item_code");
                             int count=1;
                            while (rset.next()) {
                                
                                table.getDefaultCell().setColspan(1);
                                table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_CENTER);
                                table.getDefaultCell().setColspan(1);
                                numberSeq += 1;
                                
                                phrase = new Phrase(""+count+"   ", pFontHeader1);
                                table.addCell(phrase);
                                
                                table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                                phrase = new Phrase(dbObject.getDBObject(rset.getObject(1), "-"),pFontHeader1);
                                
                                table.addCell(phrase);
                                phrase = new Phrase(dbObject.getDBObject(rset.getObject(2), "-"),pFontHeader1);
                                
                                table.addCell(phrase);
                                table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                                phrase = new Phrase(dbObject.getDBObject(rset.getObject(3), "-"),pFontHeader1);
                               table.addCell(phrase);
                                
                                table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                                phrase = new Phrase(dbObject.getDBObject(rset.getObject(4), "-"),pFontHeader1);
                                
                                table.addCell(phrase);
                                
                                table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                                phrase = new Phrase(dbObject.getDBObject(rset.getObject(5), "-"),pFontHeader1);
                                table.addCell(phrase);
                                
                                table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                                phrase = new Phrase(dbObject.getDBObject(rset.getObject(6), "-"),pFontHeader1);
                                table.addCell(phrase);
                                
                                table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                                phrase = new Phrase(dbObject.getDBObject(rset.getObject(7), "-"),pFontHeader1);
                                table.addCell(phrase);
                                
                                count++;
                            }
                            /*table.getDefaultCell().setColspan(12);
                            phrase=new Phrase(" ");
                            table.addCell(phrase);
                            
                            String gt=null;
                            while(r.next()){
                            gt=dbObject.getDBObject(r.getObject(1), "-");
                            }
                            table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
                            phrase=new Phrase("GRAND TOTAL: "+gt, pFontHeader1);
                            table.addCell(phrase);
                            System.err.println("grand total");
                            */
                             docPdf.add(table);
                            
                        } catch(java.sql.SQLException SqlExec) {
                            
                            javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), SqlExec.getMessage());
                            
                        }
                        
                      
                    } catch(com.lowagie.text.BadElementException BadElExec) {
                        
                        javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), BadElExec.getMessage());
                        
                    } catch (SQLException ex) {
                        Logger.getLogger(OrdersPdf.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                } catch(java.io.FileNotFoundException fnfExec) {
                    
                    javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), fnfExec.getMessage());
                    
                } catch (SQLException ex) {
                    Logger.getLogger(StocksIssuingPdf.class.getName()).log(Level.SEVERE, null, ex);
                } catch (BadElementException ex) {
                    Logger.getLogger(StocksIssuingPdf.class.getName()).log(Level.SEVERE, null, ex);
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

