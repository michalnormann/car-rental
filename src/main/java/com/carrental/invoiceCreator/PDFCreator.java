package com.carrental.invoiceCreator;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import java.io.File;
import java.io.IOException;

public class PDFCreator {

    public static final String DEST = "C:/Users/Michal/Downloads/car-rental-invoice.pdf";
    public static final String IMAGE = "https://cdn0.iconfinder.com/data/icons/automotive/128/CARS-512.png";


    public static void main(String[] args) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new PDFCreator().createPdf(DEST);
    }

    public void createPdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        PageSize pageSize = new PageSize(new Rectangle(600, 600));
        Document doc = new Document(pdfDoc, pageSize);

        PdfCanvas canvas = new PdfCanvas(pdfDoc.addNewPage());
        ImageData img = ImageDataFactory.create(IMAGE);
        canvas.saveState();
        PdfExtGState state = new PdfExtGState();
        state.setFillOpacity(0.2f);
        canvas.setExtGState(state);
        canvas.addImage(img, 0, 0, pageSize.getWidth(), false);
        canvas.restoreState();

        //Paragraph p1 = new Paragraph();
        PdfFont fontBold = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        Text heading = new Text("CAR RENTAL INVOICE").setFont(fontBold).setFontSize(40);
        Text company = new Text("Rental Car Company\n\n").setFontSize(26);
        doc.add(new Paragraph(heading).setTextAlignment(TextAlignment.CENTER));
        doc.add(new Paragraph(company).setTextAlignment(TextAlignment.CENTER));


        Table table1 = new Table(new float[3]);
        table1.setWidth(UnitValue.createPercentValue(100))
                .setTextAlignment(TextAlignment.LEFT)
                .setHorizontalAlignment(HorizontalAlignment.CENTER);

        table1.addHeaderCell("ITEM:").setBold();
        table1.addHeaderCell("DESCRIPTION:").setBold();
        table1.addHeaderCell("PRICE:").setBold();
        table1.addCell("car rental service");
        table1.addCell("?");
        table1.addCell("?");
        doc.add(table1);

        Paragraph paragraph = new Paragraph(new Text("\n"));
        doc.add(paragraph);

        Table table2 = new Table(new float[]{ 2, 2, 2 }); //?
        table2.setWidth(UnitValue.createPercentValue(100))
                .setTextAlignment(TextAlignment.LEFT)
                .setHorizontalAlignment(HorizontalAlignment.CENTER);

        table2.addHeaderCell("YOUR BILL:").setBold();
        table2.addHeaderCell("GROSS");
        table2.addHeaderCell("?");
        table2.addCell("");
        table2.addCell("NET");
        table2.addCell("?");
        table2.addCell("");
        table2.addCell("IN TOTAL:").setBold();
        table2.addCell("?").setBold();
        doc.add(table2);

        //add text align left

        doc.close();
        System.out.println("PDF Created");
    }
}
