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
import com.officine.losto.backend.entity.Rayon;
import com.officine.losto.backend.services.RayonService;
import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.ui.forms.CategorieFrame;
import com.officine.losto.ui.forms.RayonFrame;
import com.officine.losto.ui.forms.model.RayonTableModel;
import com.officine.losto.ui.shared.controller.AbstractFrameController;
import com.officine.losto.uti.shared.ConfirmDialog;
import com.officine.losto.uti.shared.Shared;
import com.officine.losto.util.notification.Notifications;
import com.officine.losto.validation.RayonValidator;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class RayonController extends AbstractFrameController {

	private final RayonTableModel tableModel;
	private final RayonService rayonService;
	private final RayonValidator validator;
	private RayonFrame frame;

	public void prepareListeners(RayonFrame frame) {
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

	
	
	public void prepareAndOpenFrame(JDialog parent) {
		showFrame(parent);
	}
	
	private void showFrame(JDialog parent) {
		frame = new RayonFrame(tableModel);
		prepareListeners(frame); 
		Shared.displayFrame(frame, parent);
	}

	public void load() {
		List<Rayon> rayons = rayonService.listRayons();
		tableModel.clear();
		tableModel.addEntities(rayons);
	}

	private void findEntitybyCriteria(String rayonDescription, String rayonName) {
		List<Rayon> rayons = rayonService.findRayonByCriteria(rayonDescription, rayonName);
		tableModel.clear();
		tableModel.addEntities(rayons);
	}

	/*private void showFrame(JDialog parent) {
		frame = new RayonFrame(tableModel);
		prepareListeners(frame);
		load();
		Shared.displayFrame(frame, parent);
	}*/

	private void save() {
		Rayon rayon = frame.getMenuFromForm();
		Optional<ValidationError> errors = validator.validate(rayon);
		if (errors.isPresent()) {
			ValidationError validationError = errors.get();
			 ConfirmDialog confirmDialog = new ConfirmDialog();
			 confirmDialog.showInfo(frame, validationError.message());
			return; 
		}
		if (frame.getTb_code().getText().trim().equalsIgnoreCase(Strings.EMPTY)) {
			rayonService.save(rayon);

			tableModel.addEntity(rayon);
			frame.clearForm();
			return;
		}
		rayonService.saveAndFlush(rayon);
		int selectedRow = frame.getTable().getSelectedRow();
		tableModel.updateEntity(selectedRow, rayon);
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
					Rayon Rayon = tableModel.getEntityByRow(selectedRow);
					rayonService.remove(Rayon);
					tableModel.removeRow(selectedRow);
					frame.clearForm();
				//}

			}
		} catch (Exception e) {
			Notifications.showDeleteRowErrorMessage();
		}
	}

}
