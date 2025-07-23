package com.officine.losto.ui.forms.controller;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.officine.losto.backend.commons.validation.ValidationError;
import com.officine.losto.backend.entity.Article;
import com.officine.losto.backend.entity.Vente;
import com.officine.losto.backend.entity.VenteLigne;
import com.officine.losto.backend.services.ArticleService;
import com.officine.losto.backend.services.VenteService;
import com.officine.losto.backend.springcontext.session.SpringContext;
import com.officine.losto.backend.springcontext.session.UserSession;
import com.officine.losto.business.businessreporting.GeneratingReportThread;
import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.params.constant.ConstMessagesEN.Params;
import com.officine.losto.ui.forms.VenteFrame;
import com.officine.losto.ui.forms.model.LigneVenteTableModel;
import com.officine.losto.ui.forms.model.ModePaiementComboBoxModel;
import com.officine.losto.ui.forms.model.TypeVenteComboBoxModel;
import com.officine.losto.ui.forms.model.VenteTableModel;
import com.officine.losto.ui.shared.controller.AbstractFrameController;
import com.officine.losto.uti.shared.ConfirmDialog;
import com.officine.losto.uti.shared.Shared;
import com.officine.losto.uti.shared.ctrl;
import com.officine.losto.util.notification.Notifications;
import com.officine.losto.validation.VenteLigneValidator;
import com.officine.losto.validation.VenteValidator;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class VenteController extends AbstractFrameController {

	private final VenteTableModel venteTableModel;
	private final LigneVenteTableModel ligneVenteTableModel;
	private final VenteService venteService;
	private final VenteValidator validator;

	private final VenteLigneValidator VenteLigneValidator;
	private final ModePaiementComboBoxModel modePaiementComboBoxModel;
	private final TypeVenteComboBoxModel typeVenteComboBoxModel;

	private final ArticleService articleService;

	private VenteFrame frame;

	private final UserSession userSession = SpringContext.getBean(UserSession.class);

	private void prepareListeners(VenteFrame frame) {
		registerAction(frame.getBtnEnregistrer(), (e) -> saveOrUpdate(e));
		registerAction(frame.getBtnRetour(), (e) -> validerRetour());
		registerAction(frame.getBtnQuitter(), (e) -> closeModalWindow());
		// registerAction(frame.getBtnRetirer(), (e) -> removeLigne());
		// registerAction(frame.getBtnSupprimer(), (e) -> remove());
		registerAction(frame.getBtnImprimer(), (e) -> imprimerBC());

		frame.getTableVente().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				loadSelectedRow(venteTableModel);
			}
		});
		frame.getTb_search().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				findEntitybyCriteria(frame.getTb_search().getText().trim(), frame.getTb_search().getText().trim());

			}
		});

		frame.getTb_search().addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				findEntitybyCriteria(frame.getTb_search().getText().trim(), frame.getTb_search().getText().trim());

			}
		});

		frame.getTb_mnt_paye().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				BigDecimal mntPaye = new BigDecimal(0);
				BigDecimal mntTotal = new BigDecimal(0);

				if (frame.getTb_mnt_paye().toString() != Strings.EMPTY)
					mntPaye = new BigDecimal(frame.getTb_mnt_paye().getText());

				if (frame.getTb_prix_total().toString() != Strings.EMPTY)
					mntTotal = new BigDecimal(frame.getTb_prix_total().getText());

				frame.getTb_mnt_rendu().setText("" + mntPaye.subtract(mntTotal));
			}
		});

		// Mode de Paiement
		List<String> paiementModeData = new ArrayList<>();
		for (int i = 0; i < frame.getCb_paiementMode().getItemCount(); i++)
			paiementModeData.add(frame.getCb_paiementMode().getItemAt(i));
		frame.getEditorModePaiement().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				Shared.customizeDropdownCompoment(e, frame.getCb_paiementMode(), paiementModeData,
						frame.getEditorModePaiement());
			}
		});

		// type vente
		List<String> typeVenteData = new ArrayList<>();
		for (int i = 0; i < frame.getCb_type_vente().getItemCount(); i++)
			typeVenteData.add(frame.getCb_type_vente().getItemAt(i));
		frame.getEditorTypeVente().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				Shared.customizeDropdownCompoment(e, frame.getCb_type_vente(), typeVenteData,
						frame.getEditorTypeVente());
			}
		});
	}

	private void findEntitybyCriteria(String venteNumero, String venteDate) {

		List<Vente> Ventes = venteService.findTypeByCriteria(venteNumero, venteDate);
		venteTableModel.clear();
		venteTableModel.addEntities(Ventes);
	}

	public void prepareAndOpenFrame(JDialog parent) {
		showFrame(parent);
	}

	private void showFrame(JDialog parent) {
		frame = new VenteFrame(venteTableModel, initLigneVenteTableModel(ligneVenteTableModel));
		prepareListeners(frame);
		loadVentes();

		frame.getTb_numero().setText(Shared.generateRandom("V"));
		frame.getTb_vendu_par()
				.setText(userSession.getAppUser().getNom().concat(" " + userSession.getAppUser().getPrenom()));
		frame.getTb_date().setText("" + LocalDateTime.now());
		frame.clearLigneVenteForm();

		if (Shared.ctrl.venteFrame == null || !Shared.ctrl.venteFrame.isVisible()) {
			Shared.ctrl = new ctrl();
			Shared.ctrl.venteFrame = frame;
			Shared.displayFrame(frame, parent);
		}
	}

	private LigneVenteTableModel initLigneVenteTableModel(LigneVenteTableModel ligneVenteTableModel) {
		ligneVenteTableModel.clear();
		for (int i = 0; i < 100; i++) {
			VenteLigne v = new VenteLigne();
			ligneVenteTableModel.addEntity(v);
		}

		return ligneVenteTableModel;
	}

	private void closeModalWindow() {
		frame.clearForm();
		frame.dispose();
	}

	private void loadVentes() {
		List<Vente> ventes = venteService.listVentes();
		venteTableModel.clear();
		venteTableModel.addEntities(ventes);
	}

	private void saveOrUpdate(ActionEvent e) {
		Vente vente = frame.getVenteFromForm();
		Optional<ValidationError> errors = validator.validate(vente);
		if (errors.isPresent()) {
			ValidationError validationError = errors.get();
			ConfirmDialog confirmDialog = new ConfirmDialog();
			confirmDialog.showInfo(frame, validationError.message());
			return;
		}

		if (frame.getTb_id().getText().trim().equalsIgnoreCase(Strings.EMPTY)) {
			vente = getAllLigneOfVente(vente);
			venteService.save(vente);
			articleService.decrementProductWarehouseQuantity(vente);
			ConfirmDialog confirmDialog = new ConfirmDialog();
			confirmDialog.showInfo(frame, ConstMessagesEN.Messages.SUCCESS_MESSAGE);
			return;
		}

		int selectedRow = frame.getTableVente().getSelectedRow();
		if (selectedRow < 0)
			return;
		update(vente, selectedRow);

	}

	public void update(Vente vente, int selectedRow) {
		vente.getLignes().clear();
		vente = getAllLigneOfVente(vente);
		articleService.decrementProductWarehouseQuantity(vente);
		venteService.save(vente);
	
		ConfirmDialog confirmDialog = new ConfirmDialog();
		confirmDialog.showInfo(frame, ConstMessagesEN.Messages.MODIF_SUCCESS_MESSAGE);
	}

	public Vente getAllLigneOfVente(Vente vente) {
		for (int i = 0; i < ligneVenteTableModel.getRowCount(); i++) {
			VenteLigne venteLigne = ligneVenteTableModel.getEntityByRow(i);
			if (venteLigne.getArticle() != null) {
				venteLigne.setVente(vente);
				vente.getLignes().add(venteLigne);
			}
		}
		return vente;
	}

	public void loadSelectedRow(VenteTableModel tableModel) {
		if (tableModel.getRowCount() == 0)
			return;

		int i = frame.getTableVente().getSelectedRow();
		if (i < 0) {
			ligneVenteTableModel.clear();
			return;
		}

		Vente vente = tableModel.getEntityByRow(i);

		// id
		frame.getTb_id().setText("" + vente.getId());

		// numVente
		frame.getTb_numero().setText(vente.getNumero());

		// date
		frame.getTb_date().setText(vente.getVentedate());

		// vendeur
		frame.getTb_vendu_par().setText(vente.getVendeur());

		// client
		frame.getTb_client().setText(vente.getClient());

		// typevente

		frame.getCb_type_vente().setSelectedItem(vente.getTypeVente());

		// mode paiement

		frame.getCb_paiementMode().setSelectedItem(vente.getModePaiement());

		// montant paye paye

		frame.getTb_mnt_paye().setText("" + vente.getMontantPaye());

		// montant rendu

		frame.getTb_mnt_rendu().setText("" + vente.getMontantRendu());

		// remarque

		frame.getTb_remarque().setText(vente.getRemarque());

		frame.getTableVente().getSelectionModel().addSelectionInterval(i, i);

		ligneVenteTableModel.clear();
		ligneVenteTableModel.addEntities(vente.getLignes());
		BigDecimal total = new BigDecimal(0);
		for (VenteLigne ligne : vente.getLignes()) {
			total = total.add(ligne.getPrixTotal());
		}
		frame.getTb_prix_total().setText("" + total);

	}

	public void validerRetour() {

		try {
			JTable table = frame.getTableVente();
			int selectedRow = table.getSelectedRow();
			if (selectedRow < 0) {

				ConfirmDialog confirmDialog = new ConfirmDialog();
				confirmDialog.showInfo(frame, ConstMessagesEN.Messages.NON_ROW_SELECTED);
				return;

			} else {

				ConfirmDialog confirmDialog = new ConfirmDialog();
				int choice = confirmDialog.showConfirm(frame, ConstMessagesEN.Messages.CONFIRM_MESSAGE);

				if (choice == JOptionPane.YES_OPTION) {
					Vente venteToDelete = venteTableModel.getEntityByRow(selectedRow);
					articleService.incrementProductWarehouseQuantity(venteToDelete);
					venteService.remove(venteToDelete);
					venteTableModel.removeRow(selectedRow);
					frame.clearForm();
				}

			}
		} catch (Exception e) {
			Notifications.showDeleteRowErrorMessage();
		}
	}

	private void imprimerBC() {
		int i = frame.getTableVente().getSelectedRow();
		if (i < 0)
			return;

		Vente vente = venteTableModel.getEntityByRow(i);

		File pdf;
		try {
			if (vente.getLignes().size()<=0) return; 
			pdf = createTicketPDF(vente);
			if (vente.getLignes().size() == 0)
				return;
			showPreviewAndPrint(pdf);
			// printPDF(pdf);
		} catch (Exception e) {

			e.printStackTrace();
		}
		// lignes.add(new LigneVenteTest("Article3", 20, 35));
	}

	public static File createTicketPDF(Vente vente) throws Exception {

		List<VenteLigne> lignes  = vente.getLignes(); 
			

		Document document = new Document(new Rectangle(300, 300));
		File file = File.createTempFile("ticket".concat(lignes.get(0).getVente().getNumero()), ".pdf");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		// NumberFormat nf = NumberFormat.getInstance(Locale.FRANCE);

		PdfWriter.getInstance(document, new FileOutputStream(file));
		document.open();

		// Contend du ticket

		// Logo
		Image logo = Image.getInstance(ClassLoader.getSystemResource(Params.BASE_PATH + "images/officinelogo.png"));
		logo.scaleToFit(50, 50);
		logo.setAlignment(Element.ALIGN_CENTER);
		document.add(logo);

		// Ligne horizontale
		// LineSeparator line = new LineSeparator();
		// document.add(line);

		// Titre
		Paragraph title = new Paragraph("OFFICINE PHARMA", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD));
		title.setAlignment(Element.ALIGN_CENTER);
		document.add(title);

		// Font bold = new Font(Font.FontFamily.COURIER, 10, Font.BOLD);
		Font police = new Font(Font.FontFamily.COURIER, 8, Font.NORMAL); // new Font(Font.FontFamily.HELVETICA, 8)
		// document.add(new Paragraph("Pharmacie Centrale", bold));
		document.add(new Paragraph("Date :" + LocalDateTime.now().format(formatter), police));
		// document.add(line);
		document.add(new Paragraph("Adresse : 123 Rue Santé, Ville", police));
		document.add(new Paragraph("Téléphone : 01 23 45 67 89", police));
		
		if (lignes.size() > 0) 
			document.add(new Paragraph("*************"+vente.getNumero()+"******************", police));
		 
		
		BigDecimal total = new BigDecimal(0);
		for (VenteLigne ligne : lignes) {
			String item = String.format("%-25s x%d %.0f =%.0f FCFA",
					ligne.getArticle().getArticleName().concat(" " + ligne.getArticle().getArticleDosage()),
					ligne.getQuantite(), ligne.getArticle().getArticlePrixVente(), ligne.getPrixTotal());
			document.add(new Paragraph(item, police));
			total = total.add(ligne.getPrixTotal());
		}
		document.add(new Paragraph("-----------------------------------------------", police));
		document.add(
				new Paragraph("Total TTC :                                    " + String.format("%.0f FCFA", total),
						new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD)));
		// Pied de page
		// document.add(Chunk.NEWLINE);
		document.add(new Paragraph("Merci pour votre visite !", new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC)));
		document.close();
		return file;

	}

	// Aperçu simple et impression
	public static void showPreviewAndPrint(File pdfFile) throws Exception {
		/*
		 * int option = JOptionPane.showConfirmDialog(null,
		 * "Afficher et imprimer le ticket ?", "Aperçu", JOptionPane.YES_NO_OPTION);
		 * 
		 * if (option == JOptionPane.YES_OPTION) { if (Desktop.isDesktopSupported()) {
		 * Desktop.getDesktop().open(pdfFile); // Aperçu dans le lecteur PDF du système
		 * } //printPDF(pdfFile); }
		 */
		Desktop.getDesktop().open(pdfFile); // Aperçu dans le lecteur PDF du système
	}

	public static void printPDF(File fileToPrint, Vente vente) throws Exception {
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
		aset.add(new JobName("Ticket_de_Caisse".concat(vente.getNumero()), null));
		// Nom visible sur l'imprimante
		aset.add(new Copies(1));

		printJob.print(pdfDoc, aset);
		fis.close();
	}

}
