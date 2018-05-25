/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.iRetrieve.cloud.configuration;

import com.iRetrieve.cloud.domain.History;
import com.iRetrieve.cloud.domain.Hotspot;
import com.iRetrieve.cloud.domain.Report;
import com.iRetrieve.cloud.domain.User;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 *
 * @author alfie
 */
public class GeneratePdfReport {

    public static ByteArrayInputStream usersReport(List<User> users) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(95);
            table.setWidths(new int[]{2, 8, 6, 6, 6, 3});
            table.setPaddingTop(10);
            table.setSpacingBefore(120f);

            Font headFont = FontFactory.getFont(FontFactory.COURIER);

            PdfPCell hcell;
            hcell = new PdfPCell(new Phrase("Id", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Email", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Name", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Last Name", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Phone", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Active", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            for (User user : users) {

                PdfPCell cell;

                cell = new PdfPCell(new Phrase(new Integer(user.getUserId()).toString(), headFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(user.getEmail(), headFont));
                cell.setPaddingLeft(5);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(user.getName(), headFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPaddingRight(5);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(user.getLastName(), headFont));
                cell.setPaddingLeft(5);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(user.getPhone(), headFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPaddingRight(5);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.valueOf(user.getActive()), headFont));
                cell.setPaddingLeft(5);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

            }

            PdfWriter writer = PdfWriter.getInstance(document, out);

            HeaderFooterPageEvent event = new HeaderFooterPageEvent("USER");
            writer.setPageEvent(event);

            document.open();

            document.add(new Paragraph(Element.ALIGN_CENTER, " "));

            document.add(table);

            document.close();

        } catch (Exception ex) {

        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    public static ByteArrayInputStream reportReport(List<Report> reports) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(95);
            table.setWidths(new int[]{2, 3, 3, 6, 5, 6, 5});
            table.setPaddingTop(10);
            table.setSpacingBefore(120f);

            Font headFont = FontFactory.getFont(FontFactory.COURIER);

            PdfPCell hcell;
            hcell = new PdfPCell(new Phrase("Id", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Type", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Subject", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Description", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Date", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Place", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Contact", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            for (Report report : reports) {

                PdfPCell cell;

                cell = new PdfPCell(new Phrase(new Integer(report.getId()).toString(), headFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(report.getType(), headFont));
                cell.setPaddingLeft(5);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(report.getSubject(), headFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPaddingRight(5);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(report.getDescription(), headFont));
                cell.setPaddingLeft(5);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(report.getDate(), headFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPaddingRight(5);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.valueOf(report.getPlace()), headFont));
                cell.setPaddingLeft(5);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(String.valueOf(report.getContact()), headFont));
                cell.setPaddingLeft(5);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

            }

            PdfWriter writer = PdfWriter.getInstance(document, out);

            HeaderFooterPageEvent event = new HeaderFooterPageEvent("REPORT");
            writer.setPageEvent(event);

            document.open();

            document.add(new Paragraph(Element.ALIGN_CENTER, " "));
            document.add(table);

            document.close();

        } catch (Exception ex) {

        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    public static ByteArrayInputStream historyReport(List<History> histories, List<User> users) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfPTable table = new PdfPTable(9);
            table.setWidthPercentage(95);
            table.setWidths(new int[]{2, 3, 3, 6, 4, 4, 6, 4, 4});
            table.setPaddingTop(10);
            table.setSpacingBefore(120f);
            

            Font headFont = FontFactory.getFont(FontFactory.COURIER);

            PdfPCell hcell;
            hcell = new PdfPCell(new Phrase("REF ID", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Type", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Subject", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Description", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Date", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Location", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Reporter", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Email", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Phone", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            for (History history : histories) {

                for (User user : users) {
                    if (history.getUserId() == user.getUserId()) {
                        PdfPCell cell;

                        cell = new PdfPCell(new Phrase(new Integer(history.getSettleId()).toString(), headFont));
                        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase(history.getType(), headFont));
                        cell.setPaddingLeft(5);
                        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase(history.getSubject(), headFont));
                        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setPaddingRight(5);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase(history.getDescription(), headFont));
                        cell.setPaddingLeft(5);
                        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase(history.getDate(), headFont));
                        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setPaddingRight(5);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase(history.getLocation(), headFont));
                        cell.setPaddingLeft(5);
                        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase(user.getName() + " " + user.getLastName(), headFont));
                        cell.setPaddingLeft(5);
                        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase(user.getEmail(), headFont));
                        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setPaddingRight(5);
                        table.addCell(cell);

                        cell = new PdfPCell(new Phrase(user.getPhone(), headFont));
                        cell.setPaddingLeft(5);
                        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell);

                        break;
                    }
                }

            }

            PdfWriter writer = PdfWriter.getInstance(document, out);

            HeaderFooterPageEvent event = new HeaderFooterPageEvent("HISTORY");
            writer.setPageEvent(event);

            document.open();

            document.add(new Paragraph(Element.ALIGN_CENTER, " "));
            document.add(table);

            document.close();

        } catch (Exception ex) {

        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    public static ByteArrayInputStream hotspotReport(List<com.iRetrieve.cloud.domain.Hotspot> hotspots) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(95);
            table.setWidths(new int[]{2, 8, 8, 5});
            table.setPaddingTop(10);
            table.setSpacingBefore(120f);

            Font headFont = FontFactory.getFont(FontFactory.COURIER);

            PdfPCell hcell;
            hcell = new PdfPCell(new Phrase("Id", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Name", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Place", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("Contact", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            for (Hotspot hotspot : hotspots) {

                PdfPCell cell;

                cell = new PdfPCell(new Phrase(new Integer(hotspot.getId()).toString(), headFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(hotspot.getName(), headFont));
                cell.setPaddingLeft(5);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(hotspot.getPlace(), headFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPaddingRight(5);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(hotspot.getContact(), headFont));
                cell.setPaddingLeft(5);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

            }

            PdfWriter writer = PdfWriter.getInstance(document, out);

            HeaderFooterPageEvent event = new HeaderFooterPageEvent("HOTSPOT");
            writer.setPageEvent(event);

            document.open();

            document.add(new Paragraph(Element.ALIGN_CENTER, " "));
            document.add(table);

            document.close();

        } catch (Exception ex) {

        }
        return new ByteArrayInputStream(out.toByteArray());
    }
}
