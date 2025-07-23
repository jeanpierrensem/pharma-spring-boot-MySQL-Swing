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
import com.officine.losto.backend.entity.Categorie;
import com.officine.losto.backend.services.CategorieService;
import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.ui.forms.CategorieFrame;
import com.officine.losto.ui.forms.TypeFrame;
import com.officine.losto.ui.forms.model.CategorieTableModel;
import com.officine.losto.ui.shared.controller.AbstractFrameController;
import com.officine.losto.uti.shared.ConfirmDialog;
import com.officine.losto.uti.shared.Shared;
import com.officine.losto.util.notification.Notifications;
import com.officine.losto.validation.CategorieValidator;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class CategorieController extends AbstractFrameController {

	private final CategorieTableModel tableModel;
	private final CategorieService categorieService;
	private final CategorieValidator validator;
	private CategorieFrame frame;

	public void prepareListeners(CategorieFrame frame) {
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
		frame = new CategorieFrame(tableModel);
		prepareListeners(frame); 
		Shared.displayFrame(frame, parent);
	}
	
	
	

	public void load() {
		List<Categorie> categories = categorieService.listCategories();
		tableModel.clear();
		tableModel.addEntities(categories);
	}

	private void findEntitybyCriteria(String categorieDescription, String categorieName) {
		List<Categorie> Categories = categorieService.findCategorieByCriteria(categorieDescription, categorieName);
		tableModel.clear();
		tableModel.addEntities(Categories);
	}



	private void save() {
		Categorie categorie = frame.getMenuFromForm();
		Optional<ValidationError> errors = validator.validate(categorie);
		if (errors.isPresent()) {
			ValidationError validationError = errors.get();
			 ConfirmDialog confirmDialog = new ConfirmDialog();
			 confirmDialog.showInfo(frame, validationError.message());
			return; 
		}
		if (frame.getTb_code().getText().trim().equalsIgnoreCase(Strings.EMPTY)) {
			categorieService.save(categorie);

			tableModel.addEntity(categorie);
			frame.clearForm();
			return;
		}
		categorieService.saveAndFlush(categorie);
		int selectedRow = frame.getTable().getSelectedRow();
		tableModel.updateEntity(selectedRow, categorie);
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
					Categorie categorie = tableModel.getEntityByRow(selectedRow);
					categorieService.remove(categorie);
					tableModel.removeRow(selectedRow);
					frame.clearForm();
				//}

			}
		} catch (Exception e) {
			Notifications.showDeleteRowErrorMessage();
		}
	}

}
