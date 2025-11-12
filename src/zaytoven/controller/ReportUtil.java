/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zaytoven.controller;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.Barcode128;
import com.lowagie.text.pdf.BarcodeEAN;
import com.lowagie.text.pdf.PdfCell;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author kelsas
 */
public class ReportUtil {

    private static Font catFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD);
    private final Font sigFont = FontFactory.getFont(FontFactory.HELVETICA, 6, Font.BOLD);
    Image im = null;

    public static void addTitlePage(Document document, Connection connectDB) throws DocumentException {
        boolean enableLogo = false;
        Paragraph preface = new Paragraph();
        Statement st;
        String header = null, phone_no=null, web=null;
        Date date = null;
        InputStream imageOut = null;
        BufferedImage imag = null;
        try {
            st = connectDB.createStatement();
//            ResultSet r = st.executeQuery("select header_name,current_date,company_logo,enable_logo from pb_header");
            ResultSet r = st.executeQuery("select header_name, phone_no, web_address from pb_header");

            while (r.next()) {
                header = r.getString(1);
                phone_no=r.getString(2);
                web=r.getString(3);
//                imageOut = r.getBinaryStream(3);
//                enableLogo = r.getBoolean(4);

            }

            if (imageOut != null) {
                try {
                    imag = ImageIO.read(imageOut);
                    System.out.println("--------");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(new JFrame(), "Error Reading Database \n" + e.getMessage());
        }

        if (imag != null && enableLogo) {
            Paragraph p = new Paragraph();
            Image im = null;
            try {
                im = Image.getInstance(imag, null);
            } catch (BadElementException | IOException ex) {
                Logger.getLogger(ReportUtil.class.getName()).log(Level.SEVERE, null, ex);
            }

            im.setAlignment(Image.LEFT | Image.TEXTWRAP);
            //im.scaleAbsolute(96, 102);
            im.scaleToFit(80, 80);

            Chunk c = new Chunk(im, -100, -55, true);
            //c=new Chunk(header,catFont);
            //p.add(c);

//                        preface.add(c);
            document.add(im);
            preface.add(header);
            preface.add(phone_no);
            preface.add(web);
            preface.setAlignment(Element.ALIGN_CENTER);

            //preface.add(p);
            // preface.add(new Paragraph(header, catFont));
        } else {
//            preface.add(new Paragraph(header, catFont));
            preface.add(header);
            preface.add(phone_no);
            preface.add(web);
            preface.setAlignment(Element.ALIGN_CENTER);
        }

        document.add(preface);

        preface = new Paragraph();
        addEmptyLine(preface, 2);

    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    public void addOrdersSignature(Document document, Connection connectDB, String orderNo) throws DocumentException {
        Paragraph preface = new Paragraph();
        Statement st;
        InputStream imgStream = null;
        BufferedImage imag = null;
        String approvedLevel = null;
        try {
            com.lowagie.text.pdf.PdfPTable table = new com.lowagie.text.pdf.PdfPTable(6);
            int headerwidths[] = {20,13,20,13,20,14};
            table.setWidths(headerwidths);
            
            table.getDefaultCell().setColspan(6);
            table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
            table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_CENTER);

            st = connectDB.createStatement();
            ResultSet r = st.executeQuery("SELECT form_levels,image,user_approved, sheet_no,enabled,trim(usernames) FROM approval_master ,st_signature,ut_userapprovals ,ut_formsetup,login_user_details where sheet_no ='" + orderNo + "' and st_signature.staff_name = user_approved and login_name=user_approved and code =form_code::integer and ut_formsetup.form_type ilike 'order' and ut_userapprovals.user_name = user_approved and ut_userapprovals.user_name=user_approved order by approval_master.oid");

            String userNames = null; 
            while (r.next()) {
                approvedLevel = r.getString(1);
                imgStream = r.getBinaryStream(2);
                userNames = r.getString(6);
                
                table.getDefaultCell().setColspan(1);
                Phrase p = new Phrase(approvedLevel, sigFont);
                    if (r.getBoolean(5)) {
                         table.addCell(p + "\n" + userNames);
                        table.addCell(getItextImage(imgStream));
                    } else {
                        table.addCell(p + " Sign");
                        table.addCell("_______________________");
                    }
                    
            }
            table.getDefaultCell().setColspan(6);
                table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                table.getDefaultCell().setHorizontalAlignment(PdfCell.ALIGN_CENTER);
                Phrase p1;
                table.addCell(" ");
//                p1 = new Phrase("Stamp___________________________________________", catFont);
//                table.addCell(p1);

//                table.addCell(" ");
//                p1 = new Phrase("NB: This Purchase Order Is NOT VALID Unless Approved", catFont);
//                table.addCell(p1);
             
            document.add(table);

        } catch (SQLException e) {
            JOptionPane
                    .showMessageDialog(new JFrame(), "Error Reading Database \n" + e.getMessage());
        }

    }

    private Image getItextImage(InputStream imgStream){
        if(imgStream!=null){
            try {
                BufferedImage imag = ImageIO.read(imgStream);
                return Image.getInstance(imag, null);
            } catch (IOException | BadElementException ex) {
                Logger.getLogger(ReportUtil.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
        return null;
    }
    
    /**
     * Generates Barcode128
     *
     * @param docWriter
     * @param code
     * @return <code>com.lowagie.text.Image</code>
     *
     */
    public static com.lowagie.text.Image getBarcode128(PdfWriter docWriter, String code) {
        PdfContentByte cb = docWriter.getDirectContent();
        Barcode128 code128 = new Barcode128();
        code128.setCode(code.trim());
//         code128.setBarHeight(9f);
//         code128.setSize(9f);
        code128.setBarHeight(20f); // great! but what about width???
        code128.setX(0.9f);

//         code128.setCodeType(Barcode128.CODE128);
        com.lowagie.text.Image code128Image = code128.createImageWithBarcode(cb, null, null);
//         code128Image.setAbsolutePosition(10,700);
//         code128Image.scalePercent(10);

        return code128Image;
    }

    public static com.lowagie.text.Image genBarcodeEAN(PdfWriter docWriter, String code) {
        PdfContentByte cb = docWriter.getDirectContent();
        BarcodeEAN codeEAN = new BarcodeEAN();
        codeEAN.setCode(code.trim());
        com.lowagie.text.Image codeEANImage = codeEAN.createImageWithBarcode(cb, null, null);
        return codeEANImage;
    }

    public static com.lowagie.text.Image getBarcodeEAN(PdfWriter docWriter, String code) {
        PdfContentByte cb = docWriter.getDirectContent();
        BarcodeEAN codeEAN = new BarcodeEAN();
        codeEAN.setCode(code.trim());
        codeEAN.setBarHeight(9f);

        codeEAN.setCodeType(BarcodeEAN.EAN13);
        Image codeEANImage = codeEAN.createImageWithBarcode(cb, null, null);
//         codeEANImage.setAbsolutePosition(10,600);
//         codeEANImage.scalePercent(125);

        return codeEANImage;
    }

    public static void addCenteredTitlePage(Document document, Connection connectDB) throws SQLException, IOException, BadElementException, DocumentException {
        byte[] imageData = null;
        String header = null;
        boolean enabled=false;
        String web_url=null, header_h=null, phone_no=null;
        com.lowagie.text.Image logo = null;
        ResultSet rset = SQLHelper.getResultset(connectDB, "select header_name,current_date,company_logo,enable_logo, web_address, "
                + "header_html, phone_no from pb_header");
        while (rset.next()) {
            header = rset.getString(1);
            imageData = rset.getBytes(3);
            enabled = rset.getBoolean(4);
            web_url=rset.getString(5);
            header_h=rset.getString(6);
            phone_no=rset.getString(7);
        }
//        if (imageData != null) {
//            logo = Image.getInstance(imageData);
//            logo.scaleToFit(70f, 60f);
//            logo.setAlignment(Image.ALIGN_CENTER);
//        }
        if (enabled==true) {
            logo = Image.getInstance(imageData);
            logo.scaleToFit(70f, 60f);
            logo.setAlignment(Image.ALIGN_CENTER);
        }
        if (logo != null) {
            document.add(logo);
        }
        Paragraph p = new Paragraph(header, FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD));
        Paragraph z = new Paragraph(web_url, FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD));
        Paragraph x = new Paragraph(header_h, FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD));
        Paragraph y = new Paragraph(phone_no, FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.BOLD));
        
        p.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(p);
        z.setAlignment(Element.ALIGN_CENTER);
        document.add(z);
        x.setAlignment(Element.ALIGN_CENTER);
        document.add(x);
        y.setAlignment(Element.ALIGN_CENTER);
        document.add(y);
                
        addEmptyLine(p, 1);
        addEmptyLine(z, 1);
        addEmptyLine(x, 1);
        addEmptyLine(y, 1);
    }

    public static void addLeftTitlePage(Document document, Connection connectDB) throws SQLException, IOException, BadElementException, DocumentException {
        byte[] imageData = null;
        String header = null;
        boolean enabled;
        com.lowagie.text.Image logo = null;
        ResultSet rset = SQLHelper.getResultset(connectDB, "select header_name,current_date,company_logo,enable_logo from pb_header");
        while (rset.next()) {
            header = rset.getString(1);
            imageData = rset.getBytes(3);
            enabled = rset.getBoolean(4);
        }
        if (imageData != null) {
            logo = Image.getInstance(imageData);
            logo.scaleToFit(80f, 70f);
            logo.setAlignment(Image.ALIGN_CENTER);
        }
        if (logo != null) {
            logo.setAlignment(Image.TEXTWRAP | Image.LEFT);
            document.add(logo);

        }
        Paragraph p = new Paragraph(header, FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL));
        p.setAlignment(Element.ALIGN_CENTER);
        document.add(p);
        addEmptyLine(new Paragraph(), 1);

    }
}

//             table.addCell(" ");
// p1=new Phrase("NB: This Purchase Order Is NOT VALID Unless Fully Signed And Stamped.",catFont);
//             table.addCell("");  
