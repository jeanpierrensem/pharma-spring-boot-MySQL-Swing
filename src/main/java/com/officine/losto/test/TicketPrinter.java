package com.officine.losto.test;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.JobName;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

public class TicketPrinter {

	public static void main(String[] args) {
		JButton btn = new JButton("Imprimer Ticket");
		btn.addActionListener(e -> {
			try { 
				//Data preparation 
				List<LigneVenteTest> lignes =new ArrayList<>(); 
				
				lignes.add(new LigneVenteTest("Article1", 10, 35)); 
				lignes.add(new LigneVenteTest("Article2", 15, 35)); 
				lignes.add(new LigneVenteTest("Article3", 20, 35)); 
				lignes.add(new LigneVenteTest("Article4", 20, 35)); 
				lignes.add(new LigneVenteTest("Article3", 20, 35)); 
				lignes.add(new LigneVenteTest("Article3", 20, 35)); 
				lignes.add(new LigneVenteTest("Article3", 20, 35)); 
				lignes.add(new LigneVenteTest("Article3", 20, 35)); 
				lignes.add(new LigneVenteTest("Article3", 20, 35)); 
				lignes.add(new LigneVenteTest("Article3", 20, 35)); 
				lignes.add(new LigneVenteTest("Article3", 20, 35));
				lignes.add(new LigneVenteTest("Article3", 20, 35));
				lignes.add(new LigneVenteTest("Article1", 10, 35)); 
				lignes.add(new LigneVenteTest("Article2", 15, 35)); 
				lignes.add(new LigneVenteTest("Article3", 20, 35)); 
				lignes.add(new LigneVenteTest("Article4", 20, 35)); 
				lignes.add(new LigneVenteTest("Article3", 20, 35)); 
				lignes.add(new LigneVenteTest("Article3", 20, 35)); 
				lignes.add(new LigneVenteTest("Article3", 20, 35)); 
				lignes.add(new LigneVenteTest("Article3", 20, 35)); 
				lignes.add(new LigneVenteTest("Article3", 20, 35));
				
				File pdf = createTicketPDF(lignes);
				lignes.add(new LigneVenteTest("Article3", 20, 35)); 
				showPreviewAndPrint(pdf); 
				//printPDF(pdf);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		JFrame frame = new JFrame("Ticket de Caisse");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(btn);
		frame.setSize(200, 100);
		frame.setVisible(true);

	}

	public static File createTicketPDF(List<LigneVenteTest> lignes) throws Exception {
		Document document = new Document(new Rectangle(300, 300));
		File file = File.createTempFile("ticket", ".pdf");
		PdfWriter.getInstance(document, new FileOutputStream(file));
		document.open();

		// Contend du ticket
		Font bold = new Font(Font.FontFamily.COURIER, 10, Font.BOLD);
		document.add(new Paragraph("Pharmacie Centrale", bold));
		document.add(new Paragraph("Date :" + LocalDate.now()));
		document.add(new Paragraph("----------------------------------"));
		
		 double total = 0.0;

		for(LigneVenteTest ligne : lignes) 
		{
			String item = String.format("%-15s x%d %.2f€", ligne.libelle,ligne.quantite, ligne.getTotal());
		    document.add(new Paragraph(item));
	        total += ligne.getTotal();


		}
		   document.add(new Paragraph("----------------------------------"));
		    document.add(new Paragraph("Total TTC :            " + String.format("%.2f€", total)));
		    document.add(new Paragraph("Merci et à bientôt !"));
		    document.close();

		
		
		
	
		return file;

	}
	
	 // Aperçu simple et impression
    public static void showPreviewAndPrint(File pdfFile) throws Exception {
        int option = JOptionPane.showConfirmDialog(null, "Afficher et imprimer le ticket ?", "Aperçu", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(pdfFile); // Aperçu dans le lecteur PDF du système
            }
            printPDF(pdfFile);
        }
    }


	public static void printPDF(File fileToPrint) throws Exception {
		FileInputStream fis = new FileInputStream(fileToPrint);
		Doc pdfDoc = new SimpleDoc(fis, DocFlavor.INPUT_STREAM.AUTOSENSE, null);
		PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
		
		if (printService == null) {
			System.out.println("Aucune imprimante détectée.");
			
			return;
		}
		System.out.println("Nom imprimante :" + printService.getName());
		DocPrintJob printJob = printService.createPrintJob();
		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
		aset.add(new JobName("Ticket de Caisse", null)); // Nom visible sur l'imprimante
		aset.add(new Copies(1));

		printJob.print(pdfDoc, aset);
		fis.close();
	}

}
