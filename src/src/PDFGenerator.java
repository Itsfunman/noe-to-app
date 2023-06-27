package src;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Generates a pdf based on capacity and occupancy choice
 */
public class PDFGenerator {

    //Fonts are static so that they can be referenced everywhere
    private static PDType1Font headFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

    private static PDType1Font bodyFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

    private String fileName;

    String [] capacityNames = {"KATEGORIE", "BETRIEBE", "ZIMMER", "BETTEN", "Ø ZIMMER", "Ø BETTEN"};
    String [] occupancyNames = {"JAHR/MONAT", "ANZAHL", "ZIMMER", "BENUTZT", "BETTEN", "BENUTZT"};

    /**
     * Initializes the PDFGenerator
     * @param capacity
     * @param occupancy
     */
    public PDFGenerator(String [][] capacity, String [][] occupancy){

        try {

            //Create Document
            PDDocument document = new PDDocument();
            PDDocumentInformation pdDocumentInformation = document.getDocumentInformation();

            InitPageONE(document, pdDocumentInformation);
            InitPages(document, capacity, occupancy);

            fileName = "exportData/" + cleanFileName(pdDocumentInformation.getTitle().trim()) + "_"
                    + pdDocumentInformation.getAuthor().trim() + ".pdf";

            document.save(fileName);
            document.close();
            System.out.println("PDF created successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Initializes the cover pages
     * @param document
     * @param pdDocumentInformation
     * @throws IOException
     */
    private void InitPageONE(PDDocument document, PDDocumentInformation pdDocumentInformation) throws IOException {
        //Add page
        PDPage page = new PDPage();
        document.addPage(page);

        InitHeader(document, page, pdDocumentInformation);

        InitPageNumber(document, page, 1);



    }

    /**
     * Initializes the other pages
     * @param document
     * @param capacity
     * @param occupancy
     * @throws IOException
     */
    private void InitPages(PDDocument document, String[][] capacity, String[][] occupancy) throws IOException {
        // Add page

        int totalPagesNeeded;
        double result = occupancy.length / 30.0;


        if (result == (int) result){
            totalPagesNeeded = (int) result + 2;
        } else {
            totalPagesNeeded = (int) result + 3;
        }

        System.out.println(totalPagesNeeded);

        for (int pages = 2; pages <= totalPagesNeeded; pages++){


            if (pages == 2){
                PDPage page = new PDPage();
                document.addPage(page);

                InitPageNumber(document, page, pages);
                InitTable(document, page, capacity, capacityNames, "KAPAZITÄTEN");

            } else {

                PDPage page = new PDPage();
                document.addPage(page);

                InitPageNumber(document, page, pages);

                String [][] partData;

                if (pages < totalPagesNeeded){
                    partData = getPartData(occupancy, pages, totalPagesNeeded);
                } else if (pages == totalPagesNeeded && totalPagesNeeded != 3){
                    partData = lastPage(occupancy, pages, totalPagesNeeded);
                } else {
                    partData = occupancy;
                }

                InitTable(document, page, partData, occupancyNames, "BELEGUNG");

            }



        }


    }

    /**
     * Extra method for last page because it is formatted differently
     * @param occupancy
     * @param currentPage
     * @param totalPagesNeeded
     * @return
     */
    private String [][] lastPage(String [][] occupancy, int currentPage, int totalPagesNeeded){

        int startIndex = (currentPage * 30) - (3*30);
        int endIndex = occupancy.length;

        String [][] partData = new String[endIndex - startIndex][6];

        for (int i = 0; i < endIndex - startIndex; i++){
            for (int j = 0; j < 6; j++){

                partData[i][j] = occupancy[startIndex + i][j];
            }
        }


        return partData;

    }

    /**
     * Gets the occupancy data for the page
     * @param occupancy
     * @param currentPage
     * @param totalPagesNeeded
     * @return
     */
    private String [][] getPartData(String [][] occupancy, int currentPage, int totalPagesNeeded){

        int startIndex = (currentPage * 30) - (3*30);

        String [][] partData = new String[30][6];

        for (int i = 0; i < partData.length; i++){
            for (int j = 0; j < 6; j++){
                partData[i][j] = occupancy[startIndex + i][j];
            }
        }


        return partData;

    }

    /**
     * Draws the text for the cover page
     * @param document
     * @param page
     * @param pdDocumentInformation
     * @throws IOException
     */
    private void InitHeader(PDDocument document, PDPage page, PDDocumentInformation pdDocumentInformation) throws IOException {
        //Set Document Information:

        pdDocumentInformation.setTitle("NOE-TO EXPORT - " + getCurrentDate());
        pdDocumentInformation.setCreationDate(Calendar.getInstance());
        pdDocumentInformation.setAuthor("NOE-TO");


        PDPageContentStream headStream = new PDPageContentStream(document, page,
                PDPageContentStream.AppendMode.APPEND, true, true);
        headStream.beginText();

        headStream.setFont(headFont, 36);


        // Set the initial x position in the middle
        float initialX = page.getMediaBox().getWidth() / 5; //Width = 612
        float initialY = page.getMediaBox().getHeight() / 2; //Height = 792

        headStream.newLineAtOffset(initialX, initialY); //Start Position
        headStream.showText("NOE-TO REPORT");

        headStream.newLineAtOffset(0, -40); //offset based on start position
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String formattedDate = dateFormat.format(Calendar.getInstance().getTime());
        headStream.showText(formattedDate);

        headStream.endText();
        headStream.close();

        // Start a new content stream for drawing the image
        PDPageContentStream imageStream = new PDPageContentStream(document, page,
                PDPageContentStream.AppendMode.APPEND, true, true);
        PDImageXObject pdLogo = PDImageXObject.createFromFile("assets/NOETOLogo.jpg", document);
        imageStream.drawImage(pdLogo, 434, 692, 168, 90);
        imageStream.close();

    }

    /**
     * Draws the page numbers
     * @param document
     * @param page
     * @param pageNumber
     * @throws IOException
     */
    private void InitPageNumber(PDDocument document, PDPage page, int pageNumber) throws IOException {

        //New content stream for page number
        PDPageContentStream pageNumberStream = new PDPageContentStream(document, page,
                PDPageContentStream.AppendMode.APPEND, true, true);
        pageNumberStream.beginText();
        pageNumberStream.newLineAtOffset(page.getMediaBox().getWidth() - 20,20);
        pageNumberStream.setFont(headFont, 8);
        pageNumberStream.showText(String.valueOf(pageNumber));
        pageNumberStream.endText();
        pageNumberStream.close();

    }

    /**
     * Draws the table
     * @param document
     * @param page
     * @param tableData
     * @param columnNames
     * @param header
     * @throws IOException
     */
    private void InitTable(PDDocument document, PDPage page, String [][] tableData, String [] columnNames, String header)
            throws IOException {



        PDPageContentStream tableStream = new PDPageContentStream(document, page,
                PDPageContentStream.AppendMode.APPEND, true, true);

        tableStream.beginText();
        tableStream.setFont(headFont, 12);
        tableStream.newLineAtOffset(286, 760);
        tableStream.showText(header);
        tableStream.endText();

        int y = 740; //Cell height 20
        int x = 30; //Cell width 92

        for (int headerIndex = 0; headerIndex < columnNames.length; headerIndex++){
            drawCell(columnNames[headerIndex], headerIndex, y, x, tableStream);
        }

        for (int rowIndex = 0; rowIndex < tableData.length; rowIndex++){
            for (int columnIndex = 0; columnIndex < tableData[rowIndex].length; columnIndex++){
                    drawCell(tableData[rowIndex][columnIndex], columnIndex, y - (20 * (rowIndex + 1)), x, tableStream);
            }

            if (rowIndex == tableData.length - 1){
                break;
            }

        }

        tableStream.close();

    }

    /**
     * Draws the cells on the page
     * @param content
     * @param index
     * @param y
     * @param x
     * @param tableStream
     * @throws IOException
     */
    private void drawCell(String content, int index, int y, int x, PDPageContentStream tableStream) throws IOException {

        tableStream.beginText();
        tableStream.setFont(bodyFont, 8);
        tableStream.newLineAtOffset(x + (92 * index) + 15, y - 25);
        tableStream.showText(content);
        tableStream.endText();

        //Draw cell shape
        tableStream.setStrokingColor(Color.BLACK);
        tableStream.addRect(x + (92 * index), y - 30,92,20);
        tableStream.stroke();

    }

    /**
     * Gets tje current date for the cover page
     * @return
     */
    private static Date getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        return currentDate;
    }

    /**
     * Checks filenames for invalid characters and removes them
     * @param fileName
     * @return
     */
    private static String cleanFileName(String fileName) {
        String invalidCharsRegex = "[<>:\"/\\\\|?*]";
        String replacement = "_";
        return fileName.replaceAll(invalidCharsRegex, replacement);
    }

    /**
     * returns the filename
     * @return
     */
    public String getFileName() {
        return fileName;
    }

}
