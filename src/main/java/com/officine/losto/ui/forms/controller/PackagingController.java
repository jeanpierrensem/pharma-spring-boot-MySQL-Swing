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
import com.officine.losto.backend.entity.Packaging;
import com.officine.losto.backend.services.PackagingService;
import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.ui.forms.CategorieFrame;
import com.officine.losto.ui.forms.PackagingFrame;
import com.officine.losto.ui.forms.PackagingFrame;
import com.officine.losto.ui.forms.model.PersonnelTableModel;
import com.officine.losto.ui.forms.model.PackagingTableModel;
import com.officine.losto.ui.shared.controller.AbstractFrameController;
import com.officine.losto.uti.shared.ConfirmDialog;
import com.officine.losto.uti.shared.Shared;
import com.officine.losto.util.notification.Notifications;
import com.officine.losto.validation.PackagingValidator;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class PackagingController extends AbstractFrameController {

	private final PackagingTableModel tableModel;
	private final PackagingService packagingService;
	private final PackagingValidator validator;
	private  PackagingFrame frame;

	public void prepareListeners(PackagingFrame frame) {
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
		frame = new PackagingFrame(tableModel);
		prepareListeners(frame); 
		Shared.displayFrame(frame, parent);
	}

	public void load() {
		List<Packaging> packagings = packagingService.listPackagings();
		tableModel.clear();
		tableModel.addEntities(packagings);
	}

	private void findEntitybyCriteria(String packagingDescription, String packagingName) {
		List<Packaging> packagings = packagingService.findPackagingByCriteria(packagingDescription, packagingName);
		tableModel.clear();
		tableModel.addEntities(packagings);
	}

	/*private void showFrame(JDialog parent) {
		frame = new PackagingFrame(tableModel);
		prepareListeners(frame);
		load(); 
		Shared.displayFrame(frame, parent);
	}*/

	private void save() {
		Packaging packaging = frame.getMenuFromForm();
		Optional<ValidationError> errors = validator.validate(packaging);
		if (errors.isPresent()) {
			ValidationError validationError = errors.get();
			 ConfirmDialog confirmDialog = new ConfirmDialog();
			 confirmDialog.showInfo(frame, validationError.message());
			return; 
		}
		if (frame.getTb_code().getText().trim().equalsIgnoreCase(Strings.EMPTY)) {
			packagingService.save(packaging);

			tableModel.addEntity(packaging);
			frame.clearForm();
			return;
		}
		packagingService.saveAndFlush(packaging);
		int selectedRow = frame.getTable().getSelectedRow();
		tableModel.updateEntity(selectedRow, packaging);
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
					Packaging Packaging = tableModel.getEntityByRow(selectedRow);
					packagingService.remove(Packaging);
					tableModel.removeRow(selectedRow);
					frame.clearForm();
				//}

			}
		} catch (Exception e) {
			Notifications.showDeleteRowErrorMessage();
		}
	}

}
