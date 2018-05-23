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
        
        //p.add("Or, to say it in a more colorful way: quick brown ");
        p.add(new Chunk(Image.getInstance(readBytesFromFile()), 0, 0, true));
        p.add(sTitle);
        ColumnText ct = new ColumnText(writer.getDirectContent());
        //ct.setSimpleColumn(new Rectangle(50, 600, 400, 800));
        ct.setSimpleColumn(new Rectangle(10, 10, 100, 800));
        ct.addText(p);
        ct.go();
            
       // ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("IRetrieve Application - " + sTitle), 300, 800, 0);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void onEndPage(PdfWriter writer, Document document) {
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("http://alfiederico.com/iRetrieve-0.0.1/"), 110, 30, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("page " + document.getPageNumber()), 550, 30, 0);
    }

}
