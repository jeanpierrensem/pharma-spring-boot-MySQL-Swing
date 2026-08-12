package com.officine.losto.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.*;
import com.officine.losto.dto.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.List;

@Service
public class UserPdfExportService {

    private static final BaseColor HEADER_BG = new BaseColor(13, 148, 136);
    private static final BaseColor HEADER_TEXT = BaseColor.WHITE;
    private static final BaseColor ROW_ALT = new BaseColor(248, 250, 252);
    private static final BaseColor BORDER_COLOR = new BaseColor(226, 232, 240);
    private static final BaseColor TITLE_COLOR = new BaseColor(15, 23, 42);
    private static final BaseColor SUBTITLE_COLOR = new BaseColor(100, 116, 139);
    private static final float TABLE_WIDTH_PERCENT = 100f;

    private static PdfPCell headerCell(String text, Font font) {
        PdfPCell c = new PdfPCell(new Phrase(text, font));
        c.setBackgroundColor(HEADER_BG);
        c.setPaddingTop(10f);
        c.setPaddingBottom(10f);
        c.setPaddingLeft(8f);
        c.setPaddingRight(8f);
        c.setBorderColor(BORDER_COLOR);
        c.setBorderWidthBottom(0f);
        c.setBorderWidthTop(0f);
        c.setBorderWidthLeft(0f);
        c.setBorderWidthRight(0f);
        c.setHorizontalAlignment(Element.ALIGN_LEFT);
        c.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return c;
    }

    private static PdfPCell bodyCell(String text, Font font, BaseColor background, int horizontalAlign) {
        PdfPCell c = new PdfPCell(new Phrase(text == null ? "" : text, font));
        c.setBackgroundColor(background);
        c.setPadding(8f);
        c.setBorderColor(BORDER_COLOR);
        c.setBorderWidth(0.5f);
        c.setHorizontalAlignment(horizontalAlign);
        c.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return c;
    }

    private static String idText(Long id) {
        return id == null ? "—" : String.valueOf(id);
    }

    private static String groupLabel(EntityRefDto g) {
        if (g == null) {
            return "—";
        }
        if (g.getLabel() != null && !g.getLabel().isBlank()) {
            return g.getLabel();
        }
        if (g.getCode() != null && !g.getCode().isBlank()) {
            return g.getCode();
        }
        return g.getId() != null ? "#" + g.getId() : "—";
    }

    private static void addLine(Document document, String label, String value, Font labelFont, Font valueFont)
            throws DocumentException {
        Paragraph p = new Paragraph();
        p.add(new Chunk(label + ": ", labelFont));
        p.add(new Chunk(value, valueFont));
        document.add(p);
        document.add(Chunk.NEWLINE);
    }

    private static String nz(String s) {
        return s == null ? "" : s;
    }

    private static byte[] decodePhoto(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        String s = raw.trim();
        int comma = s.indexOf("base64,");
        if (comma >= 0) {
            s = s.substring(comma + "base64,".length());
        }
        try {
            return Base64.getDecoder().decode(s.replaceAll("\\s", ""));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public byte[] buildUserSheetPdf(UserPrintRequestDto dto) throws DocumentException {
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, TITLE_COLOR);
        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, SUBTITLE_COLOR);
        Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 11, BaseColor.BLACK);

        document.add(new Paragraph("Officine — User sheet", titleFont));
        document.add(Chunk.NEWLINE);

        if (dto.getUserId() != null) {
            addLine(document, "ID", String.valueOf(dto.getUserId()), labelFont, valueFont);
        }
        addLine(document, "Name", nz(dto.getName()), labelFont, valueFont);
        addLine(document, "Login", nz(dto.getLogin()), labelFont, valueFont);
        addLine(document, "Phone", nz(dto.getPhoneNumber()), labelFont, valueFont);
        addLine(document, "Email", nz(dto.getEmail()), labelFont, valueFont);
        addLine(document, "Group", nz(dto.getGroupLabel()), labelFont, valueFont);
        if (dto.getPasswordMasked() != null && !dto.getPasswordMasked().isBlank()) {
            addLine(document, "Password", dto.getPasswordMasked(), labelFont, valueFont);
        }

        byte[] imageBytes = decodePhoto(dto.getPhotoBase64());
        if (imageBytes != null && imageBytes.length > 0) {
            try {
                document.add(Chunk.NEWLINE);
                document.add(new Paragraph("Photo", labelFont));
                Image img = Image.getInstance(imageBytes);
                img.scaleToFit(160, 160);
                img.setAlignment(Element.ALIGN_LEFT);
                document.add(img);
            } catch (IOException ignored) {
                // Skip photo on decode/render failure
            }
        }

        document.close();
        return out.toByteArray();
    }

    /**
     * Directory-style report: all users in a styled table (no passwords).
     */
    public byte[] buildAllUsersListPdf(List<AppUserResponseDto> users) throws DocumentException {
        Document document = new Document(PageSize.A4.rotate(), 40, 40, 45, 50);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, out);
        writer.setPageEvent(new UsersListFooterEvent());
        document.open();

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.getDefault());
        String generated = fmt.format(LocalDateTime.now());

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, TITLE_COLOR);
        Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 11, SUBTITLE_COLOR);

        Paragraph title = new Paragraph("User directory", titleFont);
        title.setSpacingAfter(4f);
        document.add(title);

        Paragraph subtitle = new Paragraph(
                "Officine · Complete listing of application users (passwords are never included).", subtitleFont);
        subtitle.setSpacingAfter(2f);
        document.add(subtitle);

        Paragraph meta = new Paragraph("Generated: " + generated + " · " + users.size() + " record(s)", subtitleFont);
        meta.setSpacingAfter(14f);
        document.add(meta);

        LineSeparator line = new LineSeparator(1f, 100f, HEADER_BG, Element.ALIGN_LEFT, -2f);
        document.add(new Chunk(line));
        document.add(Chunk.NEWLINE);

        if (users.isEmpty()) {
            Font emptyFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 12, SUBTITLE_COLOR);
            document.add(new Paragraph("No users match this export.", emptyFont));
            document.close();
            return out.toByteArray();
        }

        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, HEADER_TEXT);
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 9, TITLE_COLOR);
        Font cellMuted = FontFactory.getFont(FontFactory.HELVETICA, 9, SUBTITLE_COLOR);

        float[] colWidths = {0.55f, 1.35f, 1.1f, 1.05f, 1.45f, 1.25f};
        PdfPTable table = new PdfPTable(colWidths.length);
        table.setWidthPercentage(TABLE_WIDTH_PERCENT);
        table.setWidths(colWidths);
        table.setSpacingBefore(8f);
        table.setHeaderRows(1);
        table.setSplitLate(false);

        String[] headers = {"#", "Full name", "Login", "Phone", "Email", "Group"};
        for (String h : headers) {
            table.addCell(headerCell(h, headFont));
        }

        int row = 0;
        for (AppUserResponseDto u : users) {
            boolean alt = row % 2 == 1;
            BaseColor bg = alt ? ROW_ALT : BaseColor.WHITE;
            table.addCell(bodyCell(idText(u.getId()), cellMuted, bg, Element.ALIGN_CENTER));
            table.addCell(bodyCell(nz(u.getName()), cellFont, bg, Element.ALIGN_LEFT));
            table.addCell(bodyCell(nz(u.getLogin()), cellFont, bg, Element.ALIGN_LEFT));
            table.addCell(bodyCell(nz(u.getPhoneNumber()), cellFont, bg, Element.ALIGN_LEFT));
            table.addCell(bodyCell(nz(u.getEmail()), cellFont, bg, Element.ALIGN_LEFT));
            table.addCell(bodyCell(groupLabel(u.getGroup()), cellFont, bg, Element.ALIGN_LEFT));
            row++;
        }

        document.add(table);

        PdfPTable footNote = new PdfPTable(1);
        footNote.setWidthPercentage(TABLE_WIDTH_PERCENT);
        footNote.setSpacingBefore(16f);
        PdfPCell note = new PdfPCell(new Phrase(
                "Confidential — For internal use only. Profile photos are identified by URL in the system; not embedded in this list.",
                FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 8, SUBTITLE_COLOR)));
        note.setBorder(Rectangle.NO_BORDER);
        note.setPadding(6f);
        footNote.addCell(note);
        document.add(footNote);

        document.close();
        return out.toByteArray();
    }

    private static final class UsersListFooterEvent extends PdfPageEventHelper {
        private final Font footerFont = FontFactory.getFont(FontFactory.HELVETICA, 8, SUBTITLE_COLOR);

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            float cx = (document.left() + document.right()) / 2f;
            float y = document.bottom() - 12f;
            ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                    new Phrase("Page " + writer.getPageNumber(), footerFont),
                    cx, y, 0);
        }
    }
}
