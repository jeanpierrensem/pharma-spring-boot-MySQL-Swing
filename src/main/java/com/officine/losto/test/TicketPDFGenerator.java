package com.officine.losto.test;

import java.io.FileOutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.officine.losto.params.constant.ConstMessagesEN.Params;


public class TicketPDFGenerator {
	
	 public void genererTicketPDF(String filePath) {
	        try {
	            Document document = new Document();
	            PdfWriter.getInstance(document, new FileOutputStream(filePath));
	            document.open();

	            // Ajout du logo
	            Image logo = Image.getInstance(ClassLoader.getSystemResource(Params.BASE_PATH + "images/Apply.png")); 
	            logo.scaleToFit(60, 60);
	            logo.setAlignment(Element.ALIGN_CENTER);
	            document.add(logo);

	            // Entête
	            Paragraph entete = new Paragraph("OFFICINE PHARMA\n123 Rue Santé, Paris\nTél : 01 23 45 67 89",
	                    new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD));
	            
	            entete.setAlignment(Element.ALIGN_CENTER);
	            document.add(entete);

	            document.add(new Paragraph("\n"));

	            // Table des ventes
	            PdfPTable table = new PdfPTable(3);
	            table.setWidthPercentage(100);
	            table.addCell("Produit");
	            table.addCell("Qté");
	            table.addCell("Prix");

	            table.addCell("Doliprane 500");
	            table.addCell("2");
	            table.addCell("3.00 €");

	            table.addCell("Vitamine C");
	            table.addCell("1");
	            table.addCell("2.50 €");

	            document.add(table);

	            document.add(new Paragraph("\nTOTAL TTC : 8.50 €"));
	            document.add(new Paragraph("Paiement : Carte bancaire"));
	            document.add(new Paragraph("Date : 2025-07-13  14:32"));

	            document.add(new Paragraph("\nMerci de votre visite !", new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD)));

	            document.close();

	            System.out.println("Ticket PDF généré avec succès.");

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	

    public static void main(String[] args) {
    	
    	TicketPDFGenerator generator = new TicketPDFGenerator();
        generator.genererTicketPDF("ticket_caisse_nsem.pdf");
    }
}

    


