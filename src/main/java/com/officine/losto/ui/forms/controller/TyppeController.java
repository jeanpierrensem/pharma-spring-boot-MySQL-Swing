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
import com.officine.losto.backend.entity.Typpe;
import com.officine.losto.backend.services.TyppeService;
import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.ui.forms.FormeFrame;
import com.officine.losto.ui.forms.TypeFrame;
import com.officine.losto.ui.forms.model.TypeTableModel;
import com.officine.losto.ui.shared.controller.AbstractFrameController;
import com.officine.losto.uti.shared.ConfirmDialog;
import com.officine.losto.uti.shared.Shared;
import com.officine.losto.util.notification.Notifications;
import com.officine.losto.validation.TypeValidator;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class TyppeController extends AbstractFrameController {

	private final TypeTableModel tableModel;
	private final TyppeService TypeService;
	private final TypeValidator validator;
	private TypeFrame frame;

	
	public void prepareListeners(TypeFrame frame) {
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
		frame = new TypeFrame(tableModel);
		prepareListeners(frame); 
		Shared.displayFrame(frame, parent);
	}


	public void load() {
		List<Typpe> types = TypeService.listTypes();
		tableModel.clear();
		tableModel.addEntities(types);
	}

	private void findEntitybyCriteria(String typedescription, String typename) {
		List<Typpe> Types = TypeService.findTypeByCriteria(typedescription, typename);
		tableModel.clear();
		tableModel.addEntities(Types);
	}


	private void save() {
		Typpe Type = frame.getTypeFromForm();
		Optional<ValidationError> errors = validator.validate(Type);
		if (errors.isPresent()) {
			ValidationError validationError = errors.get();
			ConfirmDialog confirmDialog = new ConfirmDialog();
			confirmDialog.showInfo(frame, validationError.message());
			return;

		}

		if (frame.getTb_code().getText().trim().equalsIgnoreCase(Strings.EMPTY)) {
			TypeService.save(Type);
			tableModel.addEntity(Type);
			frame.clearForm();
			return;
		}
		TypeService.saveAndFlush(Type);
		int selectedRow = frame.getTable().getSelectedRow();
		tableModel.updateEntity(selectedRow, Type);
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
					Typpe Type = tableModel.getEntityByRow(selectedRow);
					TypeService.remove(Type);
					tableModel.removeRow(selectedRow);
					frame.clearForm();
				//}

			}
		} catch (Exception e) {
			Notifications.showDeleteRowErrorMessage();
		}
	}

	


}
