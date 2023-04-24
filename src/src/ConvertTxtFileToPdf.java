package src;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.*;

public class ConvertTxtFileToPdf {

    public static void main(String[] args) {
        FileInputStream fis = null;
        DataInputStream in = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        File sourceFile = new File("C:\\Users\\User\\IdeaProjects\\noe-to-app2\\data\\hotelData.txt");
        File destFile = new File("C:\\Users\\User\\IdeaProjects\\noe-to-app2\\data\\hotelData.pdf");

        com.itextpdf.text.Document pdfDoc = new com.itextpdf.text.Document();
        PdfWriter writer;

        {
            try {
                writer = PdfWriter.getInstance(pdfDoc, new FileOutputStream(destFile));
                pdfDoc.open();
                pdfDoc.setMarginMirroring(true);
                pdfDoc.setMargins(36, 72, 108, 180);
                pdfDoc.topMargin();

                BaseFont courier = BaseFont.createFont(BaseFont.COURIER,
                        BaseFont.CP1252, BaseFont.EMBEDDED);
                Font myfont = new Font(courier);

                Font bold_font = new Font();

                bold_font.setStyle(Font.NORMAL);
                bold_font.setSize(9);

                pdfDoc.add(new com.itextpdf.text.Paragraph("\n"));

                if (sourceFile.exists()) {
                    fis = new FileInputStream(sourceFile);
                    in = new DataInputStream(fis);
                    isr = new InputStreamReader(in);
                    br = new BufferedReader(isr);
                    String strLine = "";
                    while (strLine == String.valueOf(br.readLine() != null)) {
                        com.itextpdf.text.Paragraph para = new com.itextpdf.text.Paragraph(strLine + "\n", myfont);
                        para.setAlignment(Element.ALIGN_JUSTIFIED);
                        pdfDoc.add(para);
                    }
                    System.out.println("txt file coverted to pdf successfully!");
                } else {
                    System.out.println("no file exists");
                }
                pdfDoc.close();
            } catch (DocumentException e) {
                throw new RuntimeException(e);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
