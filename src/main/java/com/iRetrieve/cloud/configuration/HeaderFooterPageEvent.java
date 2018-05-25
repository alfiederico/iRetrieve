/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iRetrieve.cloud.configuration;

/**
 *
 * @author afederico
 */
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class HeaderFooterPageEvent extends PdfPageEventHelper {

    String sTitle = "";

    public HeaderFooterPageEvent() {

    }

    public HeaderFooterPageEvent(String e) {
        sTitle = e;
    }

    private byte[] readBytesFromFile() {

        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;

        try {

            String path = this.getClass().getResource("/static/images").getFile();
            File file = new File(path + "/header.png");
            bytesArray = new byte[(int) file.length()];

            //read file into bytes[]
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return bytesArray;

    }

    public void onStartPage(PdfWriter writer, Document document) {
        try {
            // step 4
            Phrase p = new Phrase("");
            p.add(new Chunk(Image.getInstance(readBytesFromFile()), 0, 0, true));
            ColumnText ct = new ColumnText(writer.getDirectContent());
 //ct.setSimpleColumn(new Rectangle(50, 800, 200, 700));
ct.setSimpleColumn(new Rectangle(150, 800, 300, 700));

            ct.addText(p);

            ct.go();

            LocalDate localDate = LocalDate.now();
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase(sTitle), 20, 700, 0);
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, new Phrase("DATE : " + DateTimeFormatter.ofPattern("yyy/MM/dd").format(localDate)), 550, 700, 0);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void onEndPage(PdfWriter writer, Document document) {
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase("Developed by:"), 20, 80, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase("Alfred G. Federico"), 20, 60, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase("Vernelito S. Madera"), 20, 40, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("IRETRIEVE 2018"), 300, 60, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, new Phrase("Contact US:"), 550, 80, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, new Phrase("GCIretrieve@gmail.com"), 550, 60, 0);
        //ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("page " + document.getPageNumber()), 550, 30, 0);
    }

}
