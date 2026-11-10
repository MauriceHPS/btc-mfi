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
import java.awt.Desktop;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import zaytoven.controller.AMSUtility;
import zaytoven.controller.ConnectionDB;
import zaytoven.controller.DBObject;
import zaytoven.controller.ReportUtil;
import zaytoven.controller.SQLHelper;


public class MembersByGroupPdf implements java.lang.Runnable{
  
     java.awt.Desktop deskTop = Desktop.getDesktop();
    
    DBObject dbObject;
    
    int numberSeq = 0;
    
    public static java.sql.Connection connectDB = null;
    
//    public java.lang.String dbUserName = null;
//    
//    org.netbeans.lib.sql.pool.PooledConnectionSource pConnDB = null;
    
    boolean threadCheck = true;
    
    java.lang.Thread threadSample;
    
    com.lowagie.text.Font pFontHeader = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.BOLD);
    com.lowagie.text.Font pFontHeader1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.NORMAL);
    
    private  String groupID;
   
    java.lang.Runtime rtThreadSample = java.lang.Runtime.getRuntime();
    
    java.lang.Process prThread;
    
    AMSUtility label = new AMSUtility();
    
    public void MembersByGroupPdf(String group) {
        
        connectDB =ConnectionDB.getInstance().getCon();
        
        groupID=group;
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
                    
                    com.lowagie.text.HeaderFooter footer = new com.lowagie.text.HeaderFooter(new Phrase("Members List By Group- Page: "), true);// FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA, 12, Font.BOLDITALIC,java.awt.Color.blue));
                    
                    docPdf.setFooter(footer);
                    
                    
                    docPdf.open();
                    
                    ReportUtil.addCenteredTitlePage(docPdf, connectDB);
                    try {
                        
                        
                        com.lowagie.text.pdf.PdfPTable table = new com.lowagie.text.pdf.PdfPTable(7);
                        
                        int headerwidths[] = {10, 13, 25, 11, 11, 10, 20};
                        
                        table.setWidths(headerwidths);
                        
                        table.setWidthPercentage((100));
                        table.getDefaultCell().setBorder(Rectangle.BOTTOM);
                        
                        
                        
                        Phrase phrase; 
                        table.getDefaultCell().setColspan(7);
                        
                        phrase=new Phrase(" ");
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setColspan(4);
                        table.getDefaultCell().setBorder(PdfCell.TOP | PdfCell.BOTTOM | PdfCell.LEFT | PdfCell.RIGHT);
                        java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM);
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                        phrase=new Phrase("Members' List by Group", pFontHeader);
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setColspan(3);
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_RIGHT);
                        phrase = new Phrase("Printed On  :" +date , pFontHeader);
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setColspan(7);
                        
                        String groupName=null;
                        ResultSet g=SQLHelper.getResultset(connectDB, "select group_name from member_groups where group_id='"+groupID+"'");
                        while(g.next()){
                            groupName=g.getString(1);
                        }
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_CENTER); 
                        phrase=new Phrase("Group ID:    "+groupID+"     Group Name:     "+groupName, pFontHeader);
                        table.addCell(phrase);
                        
                        
                        table.getDefaultCell().setColspan(1);
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT); 
                        phrase = new Phrase("#",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Member ID",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Name",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("ID No",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Email",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Phone No",pFontHeader);
                        table.addCell(phrase);
                        
                        phrase = new Phrase("Address",pFontHeader);
                        table.addCell(phrase);
                        
                        table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_LEFT);
                        
                        try {
                            java.sql.Statement st = connectDB.createStatement();
                            java.sql.ResultSet rset = st.executeQuery("select member_no, first_name||' '||last_name||' '||surname as names, id_no, email_address, phone_no, "
                                    + "address from members_register where group_id='"+groupID+"' order by 2 asc");
                             int count=1;
                            while (rset.next()) {
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
                                
                                phrase = new Phrase(dbObject.getDBObject(rset.getObject(5), "-"),pFontHeader1);
                                table.addCell(phrase);
                                
                                phrase = new Phrase(dbObject.getDBObject(rset.getObject(6), "-"),pFontHeader1);
                                table.addCell(phrase);
                                
                                count++;
                            }
                            
                             docPdf.add(table);
                            
                        } catch(java.sql.SQLException SqlExec) {
                            
                            javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), SqlExec.getMessage());
                            
                        }
                        
                      
                    } catch(com.lowagie.text.BadElementException BadElExec) {
                        
                        javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), BadElExec.getMessage());
                        
                    }
                    
                } catch(java.io.FileNotFoundException fnfExec) {
                    
                    javax.swing.JOptionPane.showMessageDialog(new javax.swing.JFrame(), fnfExec.getMessage());
                    
                } catch (BadElementException | SQLException ex) {
                    Logger.getLogger(MembersByGroupPdf.class.getName()).log(Level.SEVERE, null, ex);
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