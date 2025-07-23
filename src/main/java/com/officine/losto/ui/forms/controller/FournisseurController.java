package com.officine.losto.ui.forms.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Optional;

import javax.swing.JDialog;
import javax.swing.JTable;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Controller;

import com.officine.losto.backend.commons.validation.ValidationError;
import com.officine.losto.backend.entity.Fournisseur;
import com.officine.losto.backend.services.FournisseurService;
import com.officine.losto.ui.forms.FournisseurFrame;
import com.officine.losto.ui.forms.model.FournisseurTableModel;
import com.officine.losto.ui.shared.controller.AbstractFrameController;
import com.officine.losto.uti.shared.ConfirmDialog;
import com.officine.losto.util.notification.Notifications;
import com.officine.losto.validation.FournisseurValidator;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class FournisseurController extends AbstractFrameController {

	private final FournisseurTableModel tableModel;
	private final FournisseurService FournisseurService;
	private final FournisseurValidator validator;
	private FournisseurFrame frame;

	public void prepareListeners(FournisseurFrame frame) {
		registerAction(frame.getBtnAjouter(), (e) -> save());
		registerAction(frame.getBtnSupprimer(), (e) -> remove());

		frame.getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				frame.loadSelectedRow(tableModel);
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

	public void prepareAndOpenFrame(JDialog parent) {}

	public void loadFournisseurs() {
		List<Fournisseur> fournisseur = FournisseurService.listFournisseurs();
		tableModel.clear();
		tableModel.addEntities(fournisseur);
	}

	private void findEntitybyCriteria(String fournisseurdescription, String fournisseurname) {
		List<Fournisseur> Fournisseurs = FournisseurService.findFournisseurByCriteria(fournisseurdescription, fournisseurname);
		tableModel.clear();
		tableModel.addEntities(Fournisseurs);
	}

	private void save() {
		Fournisseur Fournisseur = frame.getFournisseurFromForm();
		Optional<ValidationError> errors = validator.validate(Fournisseur);
		if (errors.isPresent()) {
			ValidationError validationError = errors.get();
			 ConfirmDialog confirmDialog = new ConfirmDialog();
			confirmDialog.showInfo(frame, validationError.message());
			return; 
		}
		if (frame.getTb_code().getText().trim().equalsIgnoreCase(Strings.EMPTY)) {
			FournisseurService.save(Fournisseur);

			tableModel.addEntity(Fournisseur);
			frame.clearForm();
			return;
		}
		FournisseurService.saveAndFlush(Fournisseur);
		int selectedRow = frame.getTable().getSelectedRow();
		tableModel.updateEntity(selectedRow, Fournisseur);
		frame.clearForm();
	}

	
	
	private void remove() {
		try {
			JTable table = frame.getTable();
			int selectedRow = table.getSelectedRow();
			if (selectedRow < 0) {

				/*ConfirmDialog confirmDialog = new ConfirmDialog();
				  confirmDialog.showInfo(frame, ConstMessagesEN.Messages.NON_ROW_SELECTED);*/
				return; 
				
			} else {

				/*ConfirmDialog confirmDialog = new ConfirmDialog();
				int choice = confirmDialog.showConfirm(frame, ConstMessagesEN.Messages.CONFIRM_MESSAGE);

				if (choice == JOptionPane.YES_OPTION) {*/
					Fournisseur Fournisseur = tableModel.getEntityByRow(selectedRow);
					FournisseurService.remove(Fournisseur);
					tableModel.removeRow(selectedRow);
					frame.clearForm();
				}

			//}
		} catch (Exception e) {
			Notifications.showDeleteRowErrorMessage();
		}
	}

}
