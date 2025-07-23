package com.officine.losto.ui.forms.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Optional;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Controller;

import com.officine.losto.backend.commons.validation.ValidationError;
import com.officine.losto.backend.entity.Seuil;
import com.officine.losto.backend.services.SeuilService;
import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.ui.forms.SeuilFrame;
import com.officine.losto.ui.forms.model.SeuilTableModel;
import com.officine.losto.ui.shared.controller.AbstractFrameController;
import com.officine.losto.uti.shared.ConfirmDialog;
import com.officine.losto.uti.shared.Shared;
import com.officine.losto.util.notification.Notifications;
import com.officine.losto.validation.SeuilValidator;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class SeuilController extends AbstractFrameController {

	private final SeuilTableModel tableModel;
	private final SeuilService SeuilService;
	private final SeuilValidator validator;
	private SeuilFrame frame;

	public void prepareListeners(SeuilFrame frame) {
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

	public void prepareAndOpenFrame(JDialog parent) {}

	public void load() {
		List<Seuil> seuils = SeuilService.listSeuils();
		tableModel.clear();
		tableModel.addEntities(seuils);
	}

	private void findEntitybyCriteria(String seuilCode) {
		List<Seuil> seuils = SeuilService.findSeuilByCriteria(seuilCode);
		tableModel.clear();
		tableModel.addEntities(seuils);
	}



	private void save() {
		Seuil seuil = frame.getSeuilFromForm();
		Optional<ValidationError> errors = validator.validate(seuil);
		if (errors.isPresent()) {
			ValidationError validationError = errors.get();
			 ConfirmDialog confirmDialog = new ConfirmDialog();
			confirmDialog.showInfo(frame, validationError.message());
			return; 
		}
		if (frame.getLbl_id().getText().trim().equalsIgnoreCase(Strings.EMPTY)) {
			seuil = SeuilService.save(seuil);
			tableModel.addEntity(seuil);
			frame.clearForm();
			return;
		}
		SeuilService.saveAndFlush(seuil);
		int selectedRow = frame.getTable().getSelectedRow();
		tableModel.updateEntity(selectedRow, seuil);
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
					Seuil Seuil = tableModel.getEntityByRow(selectedRow);
					SeuilService.remove(Seuil);
					tableModel.removeRow(selectedRow);
					frame.clearForm();
				//}

			}
		} catch (Exception e) {
			Notifications.showDeleteRowErrorMessage();
		}
	}

}
