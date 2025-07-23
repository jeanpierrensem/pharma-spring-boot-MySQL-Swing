package com.officine.losto.ui.forms.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Controller;

import com.officine.losto.backend.commons.validation.ValidationError;
import com.officine.losto.backend.entity.AppGroupe;
import com.officine.losto.backend.entity.AppGroupeDisplay;
import com.officine.losto.backend.entity.AppMenu;
import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.services.security.GroupeService;
import com.officine.losto.services.security.MenuService;
import com.officine.losto.ui.forms.GroupeFrame;

import com.officine.losto.ui.forms.model.GroupeTableModel;
import com.officine.losto.ui.shared.controller.AbstractFrameController;
import com.officine.losto.uti.shared.ConfirmDialog;
import com.officine.losto.uti.shared.Shared;
import com.officine.losto.util.notification.Notifications;
import com.officine.losto.validation.GroupeValidator;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class GroupeController extends AbstractFrameController {

	private final GroupeTableModel tableModel;
	private final GroupeService groupeService;
	private final MenuService menuService;
	private final GroupeValidator validator;
	private GroupeFrame frame;

	private void prepareListeners(GroupeFrame frame) {
		registerAction(frame.getBtnAjouter(), (e) -> save());
		registerAction(frame.getBtnFermer(), (e) -> closeModalWindow());
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
		showFrame();

	}

	private void showFrame() {
		frame = new GroupeFrame(ConstMessagesEN.DialogTitles.GROUPE, tableModel);
		prepareListeners(frame);
		load();
		Shared.displayFrame(frame);
	}

	private void load() {
		List<AppGroupe> groupes = groupeService.listGroupes();

		List<AppGroupeDisplay> groupesDisplay = new ArrayList<AppGroupeDisplay>();

		for (AppGroupe appGroupe : groupes) {
			AppGroupeDisplay appGroupeDisplay = new AppGroupeDisplay();
			appGroupeDisplay.setId(appGroupe.getId());
			appGroupeDisplay.setGroupeCode(appGroupe.getGroupeCode());
			appGroupeDisplay.setGroupeDescription(appGroupe.getGroupeDescription());
			appGroupeDisplay.setGroupeName(appGroupe.getGroupeName());

			groupesDisplay.add(appGroupeDisplay);
		}

		tableModel.clear();
		tableModel.addEntities(groupesDisplay);
	}

	private void findEntitybyCriteria(String groupedescription, String groupename) {
		List<AppGroupe> groupes = groupeService.findGroupeByCriteria(groupedescription, groupename);

		List<AppGroupeDisplay> groupesDisplay = new ArrayList<AppGroupeDisplay>();

		for (AppGroupe appGroupe : groupes) {
			AppGroupeDisplay appGroupeDisplay = new AppGroupeDisplay();

			appGroupeDisplay.setId(appGroupe.getId());
			appGroupeDisplay.setGroupeCode(appGroupe.getGroupeCode());
			appGroupeDisplay.setGroupeDescription(appGroupe.getGroupeDescription());
			appGroupeDisplay.setGroupeName(appGroupe.getGroupeName());

			groupesDisplay.add(appGroupeDisplay);
		}

		tableModel.clear();
		tableModel.addEntities(groupesDisplay);
	}

	private void save() {

		AppGroupe appGroupe = frame.getGroupeFromForm();
		Optional<ValidationError> errors = validator.validate(appGroupe);
		if (errors.isPresent()) {
			ValidationError validationError = errors.get();
			ConfirmDialog confirmDialog = new ConfirmDialog();
			confirmDialog.showInfo(frame, validationError.message());
			return;
		}
		if (frame.getLbl_IdGroupe().getText().trim().equalsIgnoreCase(Strings.EMPTY)) {

			// for each menu item save group
			List<AppMenu> menus = menuService.listMenus();
			List<AppGroupe> groupes = new ArrayList<AppGroupe>();

			for (AppMenu m : menus) {
				AppGroupe appGroupeToSave = new AppGroupe();
				appGroupeToSave.setGroupeCode(appGroupe.getGroupeCode());
				appGroupeToSave.setGroupeName(appGroupe.getGroupeName());
				appGroupeToSave.setGroupeDescription(appGroupe.getGroupeDescription());

				appGroupeToSave.setMenueId(m.getId());
				appGroupeToSave.setMenuName(m.getMenuName());
				appGroupeToSave.setMenuDescription(m.getMenuDescription());

				appGroupeToSave.setAfficher(false);
				appGroupeToSave.setEnregistrer(false);
				appGroupeToSave.setModifier(false);
				appGroupeToSave.setSupprimer(false);
				appGroupeToSave.setImprimer(false);

				groupes.add(appGroupeToSave);
			}

			groupeService.saveAll(groupes);

			AppGroupeDisplay appGroupeDisplay = new AppGroupeDisplay();

			// appGroupeDisplay.setId(appGroupe.getId());
			appGroupeDisplay.setGroupeCode(appGroupe.getGroupeCode());
			appGroupeDisplay.setGroupeDescription(appGroupe.getGroupeDescription());
			appGroupeDisplay.setGroupeName(appGroupe.getGroupeName());

			tableModel.addEntity(appGroupeDisplay);

			frame.clearForm();
			load();
			return;
		}

		// groupeService.saveAndFlush(appGroupe);

		AppGroupeDisplay appGroupeDisplay = new AppGroupeDisplay();

		appGroupeDisplay.setId(appGroupe.getId());
		appGroupeDisplay.setGroupeCode(appGroupe.getGroupeCode());
		appGroupeDisplay.setGroupeDescription(appGroupe.getGroupeDescription());
		appGroupeDisplay.setGroupeName(appGroupe.getGroupeName());

		int selectedRow = frame.getTable().getSelectedRow();
		tableModel.updateEntity(selectedRow, appGroupeDisplay);
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
					AppGroupeDisplay groupe = tableModel.getEntityByRow(selectedRow);
					groupeService.removeByGroupeCode(groupe.getGroupeCode());
					tableModel.removeRow(selectedRow);
					frame.clearForm();
				}

			}
		} catch (Exception e) {
			Notifications.showDeleteRowErrorMessage();
		}
	}

}
