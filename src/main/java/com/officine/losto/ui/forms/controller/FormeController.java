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
import com.officine.losto.backend.entity.Forme;
import com.officine.losto.backend.services.FormeService;
import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.ui.forms.FormeFrame;
import com.officine.losto.ui.forms.model.FormeTableModel;
import com.officine.losto.ui.shared.controller.AbstractFrameController;
import com.officine.losto.uti.shared.ConfirmDialog;
import com.officine.losto.uti.shared.Shared;
import com.officine.losto.validation.FormeValidator;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class FormeController extends AbstractFrameController {
	private  FormeTableModel tableModel;
	private final FormeService formeService;
	private final FormeValidator validator;
	private FormeFrame frame;
	
	/*{
		if (Shared.ctrl.formeFrame ==null )
			Shared.ctrl.formeFrame = new FormeFrame(tableModel); 
		 frame = Shared.ctrl.formeFrame ; 
	}*/

	public void prepareListeners(FormeFrame frame) {
		
		
		registerAction(frame.getBtnAjouter(), (e) -> save());
		registerAction(frame.getBtnSupprimer(), (e) -> remove());

		
		frame.getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				loadSelectedRow(tableModel);
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
		frame = new FormeFrame(tableModel);
		prepareListeners(frame); 
		loadCategories();
		Shared.displayFrame(frame, parent);
	}

	public void loadCategories() {
		List<Forme> Categorie = formeService.listFormes();
		tableModel.clear();
		tableModel.addEntities(Categorie);
	}

	private void findEntitybyCriteria(String FormeDescription, String formeName) {
		List<Forme> formes = formeService.findFormeByCriteria(FormeDescription, formeName);
		tableModel.clear();
		tableModel.addEntities(formes);
	}

	private void save() {

		Forme Forme = frame.getMenuFromForm();

		Optional<ValidationError> errors = validator.validate(Forme);
		if (errors.isPresent()) {
			ValidationError validationError = errors.get();
			ConfirmDialog confirmDialog = new ConfirmDialog();

			if (Shared.DONNEES_BASE_CTRL) {
				frame.getTb_ErrorMessage().setText(validationError.message());
				return;
			}
			confirmDialog.showInfo(frame, validationError.message());
			frame.getTb_ErrorMessage().setText(Strings.EMPTY);
			return;
		}
		if (frame.getTb_code().getText().trim().equalsIgnoreCase(Strings.EMPTY)) {
			formeService.save(Forme);
			tableModel.addEntity(Forme);
			frame.clearForm();
			return;
		}
		formeService.saveAndFlush(Forme);
		int selectedRow = frame.getTable().getSelectedRow();
		tableModel.updateEntity(selectedRow, Forme);
		frame.clearForm();
	}
	
public void loadSelectedRow(FormeTableModel tableModel) {
		
		if (tableModel.getRowCount() == 0)
			return;
		int i  =  frame.getTable().getSelectedRow(); 
		
		frame.getTb_code().setText(frame.getTable().getValueAt(i, 0).toString());
		frame.getTb_libelle().setText(frame.getTable().getValueAt(i, 1).toString());
	
		frame.getTable().getSelectionModel().addSelectionInterval(i, i);

	}

	private void remove() {
		try {
			JTable table = frame.getTable();
			int selectedRow = table.getSelectedRow();
			if (selectedRow < 0)
				return;

			if (!Shared.DONNEES_BASE_CTRL) {
				ConfirmDialog confirmDialog = new ConfirmDialog();
				int choice = confirmDialog.showConfirm(frame, ConstMessagesEN.Messages.CONFIRM_MESSAGE);

				if (choice == JOptionPane.YES_OPTION) {
					Forme forme = tableModel.getEntityByRow(selectedRow);
					formeService.remove(forme);
					tableModel.removeRow(selectedRow);
					frame.clearForm();

				}

				return;

			}

			Forme forme = tableModel.getEntityByRow(selectedRow);
			formeService.remove(forme);
			tableModel.removeRow(selectedRow);
			frame.clearForm();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
