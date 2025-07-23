package com.officine.losto.ui.forms.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.springframework.stereotype.Controller;

import com.officine.losto.backend.entity.Commande;
import com.officine.losto.backend.entity.CommandeLigne;
import com.officine.losto.backend.entity.ReceptionLigne;
import com.officine.losto.backend.services.CommandeLigneService;
import com.officine.losto.backend.services.CommandeService;
import com.officine.losto.backend.services.ReceptionLigneService;
import com.officine.losto.backend.springcontext.session.UserSession;
import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.ui.forms.ReceptionFrame;
import com.officine.losto.ui.forms.model.CommandeTableModel;
import com.officine.losto.ui.forms.model.ReceptionLigneTableModel;
import com.officine.losto.ui.shared.controller.AbstractFrameController;
import com.officine.losto.uti.shared.Shared;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class ReceptionController extends AbstractFrameController {

	private final CommandeTableModel commandeTableModel;
	private final ReceptionLigneTableModel receptionLigneTableModel;

	private final CommandeLigneService commandeLigneService;
	private final CommandeService commandeService;

	private ReceptionFrame frame;
	private final UserSession userSession;
	private final ReceptionLigneService receptionLigneService;

	private void prepareListeners(ReceptionFrame frame) {
		registerAction(frame.getBtnValiderStocker(), (e) -> saveReception());
		registerAction(frame.getBtnQuitter(), (e) -> closeModalWindow());
		// registerAction(frame.getBtnGenererBonDeReception(), (e) ->
		// genererBonCommande());
		// registerAction(frame.getBtnSignalerUneAnnomalie(), (e) ->
		// signalerAnomalie());

		// registerAction(frame.getBtnAnuler(), (e) -> AnnulerReception());

		frame.getTableCommande().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
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

	private void closeModalWindow() {
		frame.clearForm();
		frame.dispose();
		commandeTableModel.clear();
		receptionLigneTableModel.clear();
	}

	public void loadSelectedRow(CommandeTableModel tableModel) {

		if (tableModel.getRowCount() == 0)
			return;

		int i = frame.getTableCommande().getSelectedRow();

		// id
		if (commandeTableModel.getValueAt(i, 0) != null) {
			frame.getTb_idCommande().setText(commandeTableModel.getValueAt(i, 0).toString());
		}

		// numcommande
		if (commandeTableModel.getValueAt(i, 1) != null) {
			String numReception = commandeTableModel.getValueAt(i, 1).toString();
			frame.getTb_numCommande().setText(commandeTableModel.getValueAt(i, 1).toString());
		}

		// date de reception
		// frame.getTb_date_Reception().setDate(new Date());

		// receptionner par
		frame.getTb_Recptionner_par()
				.setText(userSession.getAppUser().getNom().concat(" ").concat(userSession.getAppUser().getPrenom()));

		// Fournisseur
		if (commandeTableModel.getValueAt(i, 4) != null)
			frame.getTb_fournisseur().setText(commandeTableModel.getValueAt(i, 4).toString());


		frame.getTableCommande().getSelectionModel().addSelectionInterval(i, i);

		// loading all command ligne = Statut.NON_RECEPTIONNEE
		loadLignesCommande(Long.parseLong(commandeTableModel.getValueAt(i, 0).toString()));

	
	}

	private void loadLignesCommande(long commandeId) {
		receptionLigneTableModel.clear();
		Commande commande = commandeService.loadCommandeById(commandeId);
		List<CommandeLigne> commandeLignes = commandeLigneService.findByCommande(commande);
		List<ReceptionLigne> receptionLignes = new ArrayList<>();
		for (CommandeLigne commandeLigne : commandeLignes) {
			ReceptionLigne receptionLigne = new ReceptionLigne();
			// id
			receptionLigne.setId(commandeLigne.getId());
			// article
			receptionLigne.setCommandeLigne(commandeLigne);
			
			receptionLignes.add(receptionLigne);
			
		}
		receptionLigneTableModel.addEntities(receptionLignes);
		
	}

	private void saveReception() {
		CommandeLigne commandLigne;
		for (int i = 0; i < receptionLigneTableModel.getRowCount(); i++) {
			ReceptionLigne receptionLigneToSave = receptionLigneTableModel.getEntityByRow(i);
			commandLigne = commandeLigneService.loadCommandeLigneById(receptionLigneToSave.getId());

			ReceptionLigne receptionLigne = new ReceptionLigne();
			// ID
			receptionLigne.setId(null);
			// appUsrer
			receptionLigne.setAppUsrer(userSession.getAppUser());
			// commandeLigne
			receptionLigne.setCommandeLigne(commandLigne);
			// date
			SimpleDateFormat sdf = new SimpleDateFormat(ConstMessagesEN.Params.DATE_FORMAT);
			String date = sdf.format(new Date());
			receptionLigne.setDate(date);
			// observation
			receptionLigne.setObservation(receptionLigneToSave.getObservation());
			// quantiteRecue
			receptionLigne.setQuantiteRecue(receptionLigneToSave.getQuantiteRecue());
			receptionLigne.setQuantiteManquante( commandLigne.getCommandeLigneQuantite() - receptionLigneToSave.getQuantiteRecue());

			receptionLigneService.save(receptionLigne);

		}

		JOptionPane.showMessageDialog(frame, ConstMessagesEN.Messages.SUCCESS_MESSAGE,
				ConstMessagesEN.Messages.INFORMATION_TITLE, JOptionPane.INFORMATION_MESSAGE);
		frame.clearForm();

	}

	public void prepareAndOpenFrame(JDialog parent) {
		showFrame();

	}

	private void loadCommandes() {
		List<Commande> commandes = commandeService.listCommandes();
		commandeTableModel.clear();
		commandeTableModel.addEntities(commandes);
	}

	private void findEntitybyCriteria(String commandeNumero, String commandeDate) {
		
		List<Commande> commandes = commandeService.findTypeByCriteria(commandeNumero, commandeDate);
		commandeTableModel.clear();
		commandeTableModel.addEntities(commandes);
	}

	private void showFrame() {
		frame = new ReceptionFrame(commandeTableModel, receptionLigneTableModel);
		prepareListeners(frame);
		loadCommandes();

		Shared.displayFrame(frame);
	}

	

}
