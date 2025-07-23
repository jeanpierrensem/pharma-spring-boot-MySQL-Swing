package com.officine.losto.ui.forms.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Controller;

import com.officine.losto.backend.commons.validation.ValidationError;
import com.officine.losto.backend.entity.Fournisseur;
import com.officine.losto.backend.entity.Lot;
import com.officine.losto.backend.services.FournisseurService;
import com.officine.losto.backend.services.LotService;
import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.ui.forms.LotFrame;
import com.officine.losto.ui.forms.model.FournisseurComboBoxModel;
import com.officine.losto.ui.forms.model.LotTableModel;
import com.officine.losto.ui.shared.controller.AbstractFrameController;
import com.officine.losto.uti.shared.ConfirmDialog;
import com.officine.losto.uti.shared.Shared;
import com.officine.losto.util.notification.Notifications;
import com.officine.losto.validation.LotValidator;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class LotController extends AbstractFrameController {

	private final LotTableModel tableModel;
	private final LotService LotService;
	private final FournisseurService fournisseurService;
	private final LotValidator validator;
	private final FournisseurComboBoxModel fournisseurComboBoxModel;
	private LotFrame frame;
	

	private void prepareListeners(LotFrame frame) {
		registerAction(frame.getBtnAjouter(), (e) -> save());
		registerAction(frame.getBtnQuitter(), (e) -> closeModalWindow());
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
				findEntitybyCriteria(frame.getTb_search().getText().trim());

			}
		});

		frame.getTb_search().addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				findEntitybyCriteria(frame.getTb_search().getText().trim());

			}
		});
	}

	public void prepareAndOpenFrame(JDialog parent) {
		
		
		showFrame(parent);
				

	}

	private void load() {
		List<Lot> lots = LotService.listLots();
		tableModel.clear();
		tableModel.addEntities(lots);
		
	}

	private void loadFournisseurs() {
		List<String> fournisseurs = fournisseurService.listFournisseurs().stream()
				.map(s -> s.getFournisseurName().concat(" ").concat(s.getId().toString())).collect(Collectors.toList());

		Set<String> fournisseurSet = new HashSet<>(fournisseurs);
		
		System.out.println("fournisseurSet= " + fournisseurSet.size());
		
		fournisseurComboBoxModel.clear();
		fournisseurComboBoxModel.addElements(fournisseurSet);
		frame.getCb_Fournisseur().setModel(fournisseurComboBoxModel);
		if (fournisseurSet.size() > 0)
			frame.getCb_Fournisseur().setSelectedIndex(0);
		
		
	}

	private void findEntitybyCriteria(String numeroLot) {
		List<Lot> lots = LotService.findLotByCriteria(numeroLot);
		tableModel.clear();
		tableModel.addEntities(lots);
	}

	private void showFrame(JDialog parent) {
		frame = new LotFrame(tableModel);
		prepareListeners(frame);
		loadFournisseurs();	
		load();
		Shared.displayFrame(frame,parent);
	}

	private void save() {
		Lot lot = frame.getLotFromForm();	
		
		// Fournisseur
				if (frame.getCb_Fournisseur().getSelectedItem().toString().equalsIgnoreCase(Strings.EMPTY))
					 lot.setFournisseurLot(null); 
				else {
					
				String[] fournissseurIds = frame.getCb_Fournisseur().getSelectedItem().toString().split(" ");
				Fournisseur fournisseur  = fournisseurService.loadFournisseurById(Long.parseLong(fournissseurIds[fournissseurIds.length - 1]));
				     lot.setFournisseurLot(fournisseur);
				}
				

		Optional<ValidationError> errors = validator.validate(lot);
		if (errors.isPresent()) {
			ValidationError validationError = errors.get();
			ConfirmDialog confirmDialog = new ConfirmDialog();
			confirmDialog.showInfo(frame, validationError.message());
			return;
		}
		if (frame.getTb_id().getText().trim().equalsIgnoreCase(Strings.EMPTY)) {
			lot = LotService.save(lot);

			tableModel.addEntity(lot);
			frame.clearForm();
			return;
		}
		LotService.saveAndFlush(lot);
		int selectedRow = frame.getTable().getSelectedRow();
		tableModel.updateEntity(selectedRow, lot);
		frame.clearForm();
	}

	private void closeModalWindow() {
		frame.clearForm();
		frame.dispose();
	}

	private void remove() {
		try {
			JTable table = frame.getTable();
			int selectedRow = table.getSelectedRow();
			if (selectedRow < 0) {

				ConfirmDialog confirmDialog = new ConfirmDialog();
				confirmDialog.showInfo(frame, ConstMessagesEN.Messages.NON_ROW_SELECTED);
				return;

			} else {

				ConfirmDialog confirmDialog = new ConfirmDialog();

				int choice = confirmDialog.showConfirm(frame, ConstMessagesEN.Messages.CONFIRM_MESSAGE);

				if (choice == JOptionPane.YES_OPTION) {
					Lot lot = tableModel.getEntityByRow(selectedRow);
					LotService.remove(lot);
					tableModel.removeRow(selectedRow);
					frame.clearForm();
				}

			}
		} catch (Exception e) {
			Notifications.showDeleteRowErrorMessage();
		}
	}

	

}
