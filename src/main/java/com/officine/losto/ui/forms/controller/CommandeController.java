package com.officine.losto.ui.forms.controller;

import java.awt.Color;
import java.awt.Dialog.ModalExclusionType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Controller;

import com.officine.losto.backend.commons.validation.ValidationError;
import com.officine.losto.backend.entity.AppUser;
import com.officine.losto.backend.entity.Article;
import com.officine.losto.backend.entity.Commande;
import com.officine.losto.backend.entity.CommandeLigne;
import com.officine.losto.backend.entity.Fournisseur;
import com.officine.losto.backend.entity.utilities.Statut;
import com.officine.losto.backend.services.ArticleService;
import com.officine.losto.backend.services.CommandeLigneService;
import com.officine.losto.backend.services.CommandeService;
import com.officine.losto.backend.services.FournisseurService;
import com.officine.losto.backend.springcontext.session.SpringContext;
import com.officine.losto.backend.springcontext.session.UserSession;
import com.officine.losto.business.businessreporting.GeneratingReportThread;
import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.params.constant.ConstMessagesEN.Params;
import com.officine.losto.ui.forms.CommandeFrame;
import com.officine.losto.ui.forms.model.ArticleComboBoxModel;
import com.officine.losto.ui.forms.model.CommandeLigneTableModel;
import com.officine.losto.ui.forms.model.CommandeTableModel;
import com.officine.losto.ui.forms.model.FournisseurComboBoxModel;
import com.officine.losto.ui.forms.model.ModeLivraisonComboBoxModel;
import com.officine.losto.ui.shared.controller.AbstractFrameController;
import com.officine.losto.uti.shared.ConfirmDialog;
import com.officine.losto.uti.shared.Shared;
import com.officine.losto.util.notification.Notifications;
import com.officine.losto.validation.CommandeLigneValidator;
import com.officine.losto.validation.CommandeValidator;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

@Controller
@AllArgsConstructor
public class CommandeController extends AbstractFrameController {

	private final CommandeTableModel commandeTableModel;
	private final CommandeLigneTableModel commandeLigneTableModel;
	private final CommandeService commandeService;
	private final ArticleService articleService;
	private final FournisseurService fournisseurService;
	private final CommandeLigneService commandeLigneService;
	private final ModeLivraisonComboBoxModel modeLivraisonComboBoxModel;
	private final FournisseurComboBoxModel fournisseurComboBoxModel;
	private final ArticleComboBoxModel articleComboBoxModel;
	private final CommandeValidator validator;

	private final CommandeLigneValidator commandeLigneValidator;
	private CommandeFrame frame;
	private GeneratingReportThread GT;

	private final UserSession userSession = SpringContext.getBean(UserSession.class);

	private void prepareListeners(CommandeFrame frame) {
		registerAction(frame.getBtnEnregistrer(), (e) -> saveOrUpdate(e));
		registerAction(frame.getBtnAjouter(), (e) -> addLigne());
		registerAction(frame.getBtnQuitter(), (e) -> closeModalWindow());
		registerAction(frame.getBtnRetirer(), (e) -> removeLigne());
		registerAction(frame.getBtnSupprimer(), (e) -> remove());
		registerAction(frame.getBtnImprimer(), (e) -> imprimerBC());

		frame.getTableCommande().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				loadSelectedRow(commandeTableModel);
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

	}

	public void prepareAndOpenFrame(JDialog parent) {
		showFrame(parent);
	}

	private void closeModalWindow() {
		frame.clearForm();
		frame.dispose();
	}

	private void loadLignesCommande(long commandeId) {
		commandeLigneTableModel.clear();
		Commande commande = new Commande();
		commande.setId(commandeId);
		List<CommandeLigne> commandeLignes = commandeLigneService.findByCommande(commande);
		commandeLigneTableModel.addEntities(commandeLignes);
	}

	public void loadSelectedRow(CommandeTableModel tableModel) {

		if (tableModel.getRowCount() == 0)
			return;
		int i = frame.getTableCommande().getSelectedRow();

		// id
		if (commandeTableModel.getValueAt(i, 0) != null) {
			frame.getTb_id().setText(commandeTableModel.getValueAt(i, 0).toString());

		}

		// numcommande
		if (commandeTableModel.getValueAt(i, 1) != null)
			frame.getTb_numero().setText(commandeTableModel.getValueAt(i, 1).toString());

		// date

		if (commandeTableModel.getValueAt(i, 2) != null) {
			Date date;
			try {
				date = new SimpleDateFormat(ConstMessagesEN.Params.DATE_FORMAT)
						.parse((String) commandeTableModel.getValueAt(i, 2));
				frame.getTb_date_commande().setDate(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		// commander par
		if (commandeTableModel.getValueAt(i, 3) != null)
			frame.getTb_commander_par().setText(commandeTableModel.getValueAt(i, 3).toString());

		// Fournisseur
		if (commandeTableModel.getValueAt(i, 4) != null)
			frame.getCb_fournisseur().setSelectedItem(commandeTableModel.getValueAt(i, 4).toString());

		// Mode livraison
		if (commandeTableModel.getValueAt(i, 5) != null)
			frame.getCb_mode_livraison().setSelectedItem(commandeTableModel.getValueAt(i, 5));

		// indication
		if (commandeTableModel.getValueAt(i, 6) != null)
			frame.getTb_indication().setText(commandeTableModel.getValueAt(i, 6).toString());

		frame.getTableCommande().getSelectionModel().addSelectionInterval(i, i);

		// statut
		if (commandeTableModel.getValueAt(i, 7) != null) {

			frame.getTbStatut().setText(commandeTableModel.getValueAt(i, 7).toString());
			frame.getLblStatut().setVisible(true);
			frame.getTbStatut().setBackground(Color.GREEN);
			if (commandeTableModel.getValueAt(i, 7).toString().equalsIgnoreCase(Statut.COMPLETE.toString())) {

				frame.getTbStatut().setForeground(Color.WHITE);
				// frame.getLblStatut().setBackground(Color.GREEN);

			} else if (commandeTableModel.getValueAt(i, 7).toString().equalsIgnoreCase(Statut.NON.toString())) {
				frame.getTbStatut().setBackground(Color.ORANGE);

				// frame.getTbStatut().setBackground(Color.ORANGE);
			} else
				frame.getTbStatut().setBackground(Color.RED);
		}

		frame.getTableCommande().getSelectionModel().addSelectionInterval(i, i);

		// loading all command ligne
		if (commandeTableModel.getValueAt(i, 0) != null)
			loadLignesCommande(Long.parseLong(commandeTableModel.getValueAt(i, 0).toString()));

	}

	private void imprimerBC() {
		// ClassLoader.getSystemResource("/Users//nsemjean//eclipse-workspace-202209-WB112//officine//src/main//resources//reports//uneCommande.jrxml").getFile();
		// ClassLoader.getSystemResource(Params.BASE_PATH +
		// "reports/uneCommande.jrxml").getFile();
		// String exportPath = Params.BASE_PATH.concat("/");
		// location
		// /Users/nsemjean/eclipse-workspace-202209-WB112/officine/src/main/resources/reports/uneCommande.jrxml


		try {
			if (commandeTableModel.getRowCount() == 0)
				return;
			if (frame.getTb_numero().getText().trim() == Strings.EMPTY)
				return;
			int i = frame.getTableCommande().getSelectedRow();

		
		
			Map<String, Object> params = new HashMap<>();
			params.put("commandeNumero", frame.getTb_numero().getText().trim());

			if (commandeTableModel.getValueAt(i, 2) != null) {
				params.put("commandeDate", "" + commandeTableModel.getValueAt(i, 2).toString());		
			} else {
				params.put("commandeDate", Strings.EMPTY);
			}

			if (commandeTableModel.getValueAt(i, 3) != null) {
				params.put("commanderPar", commandeTableModel.getValueAt(i, 3).toString());
			} else {
				params.put("commanderPar", Strings.EMPTY);
			}

			if (commandeTableModel.getValueAt(i, 4) != null) {
				params.put("fournisseur", commandeTableModel.getValueAt(i, 4).toString());
			} else {
				params.put("fournisseur", Strings.EMPTY);
			}

			if (commandeTableModel.getValueAt(i, 7) != null) {
				params.put("statut", commandeTableModel.getValueAt(i, 7).toString()); 
			} else {
				params.put("statut", Strings.EMPTY);
			}

			if (commandeTableModel.getValueAt(i, 5) != null) {
				params.put("modelivraison", commandeTableModel.getValueAt(i, 5));
			} else {
				params.put("modelivraison", Strings.EMPTY);
			}

			// savaAll lines
			List<CommandeLigne> commandeLignes = new ArrayList<>();
			for (int j = 0; j < commandeLigneTableModel.getRowCount(); j++) {
				CommandeLigne commandeLigne = commandeLigneTableModel.getEntityByRow(j);
				commandeLigne.setCommandeLigneArticleName(			
				commandeLigne.getCommandeLigneArticle().getArticleName().concat(" "+
				commandeLigne.getCommandeLigneArticle().getArticleDosage()
			));
				
				commandeLignes.add(commandeLigne);

			}

			String userHome = System.getProperty("user.home"); 
			String pdfPath = userHome + File.separator + "Documents" + File.separator + "uneCommandeTest.pdf";

			JRBeanCollectionDataSource datasource = new JRBeanCollectionDataSource(commandeLignes);

			/*JasperReport report = JasperCompileManager.compileReport(filePath);
			System.out.println("JasperCompileManager");
			JasperPrint print = JasperFillManager.fillReport(report, params, datasource);
			System.out.println("RJasperFillManager");

			JasperExportManager.exportReportToPdfFile(print,

					pdfPath

			);*/

			//System.out.println("Report Created and exported");
			
			

			//JOptionPane.showMessageDialog(null, "avant chargement etat") ; 
    		JasperDesign jDesign=JRXmlLoader.load(ConstMessagesEN.Params.ReportFilePath+"uneCommande.jrxml");
    		
      		//JasperReport jReportSR=JasperCompileManager.compileReport(jDesignSR);
    		JasperReport jReport=JasperCompileManager.compileReport(jDesign);
    	
    		
    		
			JasperPrint Jprint=JasperFillManager.fillReport(jReport, params, datasource); 
			JasperViewer Reportviewer=new JasperViewer(Jprint,false);
			
			frame.setEnabled(false); 
			
			Reportviewer.setTitle(" Officine - Reporting");
			Reportviewer.setLocationRelativeTo(null);
			Reportviewer.setAlwaysOnTop(true );
			Reportviewer.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
			Reportviewer.setVisible(true);
			frame.setEnabled(true); 
			
			
			
			
			
			
			
			 

			
			
			
			
			  /*GT = new GeneratingReportThread(0, 
					  ConstMessagesEN.Params.ReportFilePath, 
					  "uneCommande.jrxml", 
					  params, 
					  datasource, 
					  frame
					  );
			  GT.start();*/
			 
			 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void loadArticles() {
		List<String> articles = articleService.listArticles().stream()
				.map(s -> s.getArticleName().concat(" ").concat(s.getId().toString())).collect(Collectors.toList());

		Set<String> articlesSet = new HashSet<>(articles);

		articleComboBoxModel.clear();
		articleComboBoxModel.addElements(articlesSet);
		frame.getCb_article().setModel(articleComboBoxModel);
		if (articlesSet.size() > 0)
			frame.getCb_article().setSelectedIndex(0);

	}

	private void loadFournisseurs() {
		List<String> fournisseurs = fournisseurService.listFournisseurs().stream()
				.map(s -> s.getFournisseurName().concat(" ").concat(s.getId().toString())).collect(Collectors.toList());

		Set<String> fournisseurSet = new HashSet<>(fournisseurs);

		fournisseurComboBoxModel.clear();
		fournisseurComboBoxModel.addElements(fournisseurSet);
		frame.getCb_fournisseur().setModel(fournisseurComboBoxModel);
		if (fournisseurSet.size() > 0)
			frame.getCb_fournisseur().setSelectedIndex(0);

	}

	private void loadCommandes() {
		List<Commande> commandes = commandeService.listCommandes();
		commandeTableModel.clear();
		commandeTableModel.addEntities(commandes);
	}

	private void loadModeLIvraison() {

		List<String> modeList = new ArrayList<>();
		modeList.add("Standard");
		modeList.add("Express");
		modeList.add("Retrait");

		modeLivraisonComboBoxModel.clear();
		modeLivraisonComboBoxModel.addElements(modeList);
		frame.getCb_mode_livraison().setModel(modeLivraisonComboBoxModel);
		if (modeList.size() > 0)
			frame.getCb_mode_livraison().setSelectedIndex(0);

	}

	public void addLigne() {

		CommandeLigne commandeLigne = new CommandeLigne();
		// article
		String[] ids = frame.getCb_article().getSelectedItem().toString().split(" ");
		Article article = articleService.findArticleById(Long.parseLong(ids[ids.length - 1]));
		commandeLigne.setCommandeLigneArticle(article);
		// reference
		commandeLigne.setCommandeLigneReferenceArticle(frame.getTb_reference().getText().trim());
		// quantite
		if (frame.getTb_quantite().getText().trim().toString() != "")
			commandeLigne.setCommandeLigneQuantite(Integer.parseInt(frame.getTb_quantite().getText().trim()));
		else
			commandeLigne.setCommandeLigneQuantite(Integer.parseInt("0"));

		// Prixunitaire
		if (frame.getTb_prix_unitaire().getText().trim().toString() != "")
			commandeLigne
					.setCommandeLignePrixUnitaireHT(Integer.parseInt(frame.getTb_prix_unitaire().getText().trim()));
		else
			commandeLigne.setCommandeLignePrixUnitaireHT(Integer.parseInt("0"));

		// Remise
		if (frame.getTb_remise().getText().trim().toString() != "")
			commandeLigne.setCommandeLigneRemise(Integer.parseInt(frame.getTb_remise().getText().trim()));
		else
			commandeLigne.setCommandeLigneRemise(Integer.parseInt("0"));

		// prix totale
		if (frame.getTb_prix_total().getText().trim().toString() != "")
			commandeLigne.setCommandeLignePrixTotalHT(Integer.parseInt(frame.getTb_prix_total().getText().trim()));
		else
			commandeLigne.setCommandeLignePrixTotalHT(Integer.parseInt("0"));
		// commande
		Commande command = frame.getCommandeFromForm();

		// retrieve user
		command.setCommandeUser(userSession.getAppUser());

		// retrieve Fournisseur
		String[] fournisseurIds = frame.getCb_fournisseur().getSelectedItem().toString().split(" ");
		Fournisseur fournisseur = fournisseurService
				.loadFournisseurById(Long.parseLong(fournisseurIds[fournisseurIds.length - 1]));
		command.setCommandeFournisseur(fournisseur);
		commandeLigne.setCommandeLigneCommande(command);

		Optional<ValidationError> errorsCommand = validator.validate(command);
		if (errorsCommand.isPresent()) {
			ValidationError validationError = errorsCommand.get();
			ConfirmDialog confirmDialog = new ConfirmDialog();
			confirmDialog.showInfo(frame, validationError.message());
			return;
		}

		Optional<ValidationError> errorsLigneCommande = commandeLigneValidator.validate(commandeLigne);
		if (errorsLigneCommande.isPresent()) {
			ValidationError validationError = errorsLigneCommande.get();
			ConfirmDialog confirmDialog = new ConfirmDialog();
			confirmDialog.showInfo(frame, validationError.message());
			return;
		}

		int selectedRow = frame.getTableLigneCommande().getSelectedRow();

		if (selectedRow < 0)
			commandeLigneTableModel.addEntity(commandeLigne);

		else
			commandeLigneTableModel.updateEntity(selectedRow, commandeLigne);

		frame.clearLigneCommandeForm();
	}

	private void removeLigne() {
		int selectedRow = frame.getTableLigneCommande().getSelectedRow();

		System.out.println("selectedRow =" + selectedRow);

		if (selectedRow < 0)
			return;
		commandeLigneTableModel.removeRow(selectedRow);
		frame.clearLigneCommandeForm();
	}

	private void findEntitybyCriteria(String commandeNumero, String commandeDate) {

		List<Commande> commandes = commandeService.findTypeByCriteria(commandeNumero, commandeDate);

		commandeTableModel.clear();
		commandeTableModel.addEntities(commandes);

	}

	private void showFrame(JDialog parent) {
		frame = new CommandeFrame(commandeTableModel, commandeLigneTableModel);
		prepareListeners(frame);
		loadCommandes();
		loadFournisseurs();
		loadModeLIvraison();
		loadArticles();
		frame.getTb_numero().setText(Shared.generateRandom("C"));
		frame.getTb_commander_par()
				.setText(userSession.getAppUser().getNom().concat(" " + userSession.getAppUser().getPrenom()));

		frame.getTb_date_commande().setDate(new Date());

		frame.clearLigneCommandeForm();

		Shared.displayFrame(frame, parent);
	}

	private void saveOrUpdate(ActionEvent e) {
		// Commande commandeSaved;
		Commande command = frame.getCommandeFromForm();
		// retrieve user
		command.setCommandeUser(userSession.getAppUser());
		// retrieve fournisseur
		String[] fournisseurIds = frame.getCb_fournisseur().getSelectedItem().toString().split(" ");
		Fournisseur fournisseur = fournisseurService
				.loadFournisseurById(Long.parseLong(fournisseurIds[fournisseurIds.length - 1]));
		command.setCommandeFournisseur(fournisseur);
		// statut
		command.setCommandeStatut(Statut.NON);

		Optional<ValidationError> errors = validator.validate(command);
		if (errors.isPresent()) {
			ValidationError validationError = errors.get();
			ConfirmDialog confirmDialog = new ConfirmDialog();
			confirmDialog.showInfo(frame, validationError.message());
			return;
		}

		if (frame.getTb_id().getText().trim().equalsIgnoreCase(Strings.EMPTY)) {
			save(command);
			return;
		}

		int selectedRow = frame.getTableCommande().getSelectedRow();
		if (selectedRow < 0)
			return;
		update(command, selectedRow);

	}

	public void save(Commande command) {

		Commande commandeSaved = commandeService.save(command);
		commandeTableModel.addEntity(commandeSaved);

		// savaAll lines
		List<CommandeLigne> commandeLignes = new ArrayList<>();
		for (int i = 0; i < commandeLigneTableModel.getRowCount(); i++) {
			CommandeLigne commandeLigne = commandeLigneTableModel.getEntityByRow(i);
			commandeLigne.getCommandeLigneCommande().setId(commandeSaved.getId());

			commandeLignes.add(commandeLigne);

		}
		List<CommandeLigne> savedCommandeLignes = commandeLigneService.saveAllAndFlush(commandeLignes);
		commandeLigneTableModel.clear();
		commandeLigneTableModel.addEntities(savedCommandeLignes);

		JOptionPane.showMessageDialog(frame, ConstMessagesEN.Messages.SUCCESS_MESSAGE,
				ConstMessagesEN.Messages.INFORMATION_TITLE, JOptionPane.INFORMATION_MESSAGE);
		frame.clearForm();

	}

	public void update(Commande command, int selectedRow) {

		commandeService.saveAndFlush(command);
		commandeTableModel.updateEntity(selectedRow, command);

		// Save all Command Line = deleteAll + saveAll
		commandeLigneService.deleteByCommandeLigneCommande(command);

		// +
		List<CommandeLigne> commandeLignes = new ArrayList<>();
		for (int i = 0; i < commandeLigneTableModel.getRowCount(); i++) {
			CommandeLigne commandeLigne = commandeLigneTableModel.getEntityByRow(i);
			commandeLigne.setCommandeLigneCommande(command);
			commandeLigne.setId(null);

			commandeLignes.add(commandeLigne);
		}
		commandeLigneService.saveAllAndFlush(commandeLignes);
		commandeLigneTableModel.clear();
		// commandeLigneTableModel.addEntities(savedCommandeLignes);
		frame.clearForm();
		JOptionPane.showMessageDialog(frame, ConstMessagesEN.Messages.MODIF_SUCCESS_MESSAGE,
				ConstMessagesEN.Messages.INFORMATION_TITLE, JOptionPane.INFORMATION_MESSAGE);

	}

	private void remove() {
		try {
			JTable table = frame.getTableCommande();
			int selectedRow = table.getSelectedRow();
			if (selectedRow < 0) {
				ConfirmDialog confirmDialog = new ConfirmDialog();
				confirmDialog.showInfo(frame, ConstMessagesEN.Messages.NON_ROW_SELECTED);
				return;

			} else {

				ConfirmDialog confirmDialog = new ConfirmDialog();
				int choice = confirmDialog.showConfirm(frame, ConstMessagesEN.Messages.CONFIRM_MESSAGE);

				if (choice == JOptionPane.YES_OPTION) {
					Commande commande = commandeTableModel.getEntityByRow(selectedRow);

					// deleteCL => deleteC
					commandeLigneService.deleteByCommandeLigneCommande(commande);
					commandeService.remove(commande);

					commandeLigneTableModel.clear();
					commandeTableModel.removeRow(selectedRow);

					frame.clearForm();

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			Notifications.showDeleteRowErrorMessage();
		}

	}

}
