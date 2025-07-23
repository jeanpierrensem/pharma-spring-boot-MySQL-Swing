package com.officine.losto.ui.forms.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import com.officine.losto.backend.entity.AppMenu;
import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.services.security.MenuService;
import com.officine.losto.ui.forms.MenuFrame;
import com.officine.losto.ui.forms.model.MenuTableModel;
import com.officine.losto.ui.shared.controller.AbstractFrameController;
import com.officine.losto.uti.shared.ConfirmDialog;
import com.officine.losto.uti.shared.Shared;
import com.officine.losto.util.notification.Notifications;
import com.officine.losto.validation.MenuValidator;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class MenuController extends AbstractFrameController {

	private final MenuTableModel tableModel;
	private final MenuService menuService;
	private final MenuValidator validator;
	private  MenuFrame frame;

	@Override
	public void prepareAndOpenFrame(JDialog parent) {
		showFrame();
		
	}

	private void prepareListeners(MenuFrame frame) {

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
	
	

	private void showFrame() {
		frame = new MenuFrame(tableModel); 
		prepareListeners(frame);
		load();
		Shared.displayFrame(frame);
		//Shared.ctrl.menuFrame = new MenuFrame(tableModel);
		//frame = Shared.ctrl.menuFrame;
	
	}

	private void load() {
		List<AppMenu> menus = menuService.listMenus();
		tableModel.clear();
		tableModel.addEntities(menus);
	}

	private void findEntitybyCriteria(String menudescription, String menuname) {
		List<AppMenu> appMenus = menuService.findMenuByCriteria(menudescription, menuname);
		tableModel.clear();
		tableModel.addEntities(appMenus);
	}

	private void save() {

		AppMenu appMenu = frame.getMenuFromForm();

		Optional<ValidationError> errors = validator.validate(appMenu);
		if (errors.isPresent()) {
			ValidationError validationError = errors.get();
			 ConfirmDialog confirmDialog = new ConfirmDialog();
			 confirmDialog.showInfo(frame, validationError.message());
			return; 
		}
		if (frame.getTb_code().getText().trim().equalsIgnoreCase(Strings.EMPTY)) {
			menuService.save(appMenu);

			tableModel.addEntity(appMenu);
			frame.clearForm();
			return;
		}
		menuService.saveAndFlush(appMenu);

		int selectedRow = frame.getTable().getSelectedRow();
		tableModel.updateEntity(selectedRow, appMenu);
		frame.clearForm();

	}

	public void closeModalWindow() {
		System.out.println("private void closeModalWindow()");
		frame.clearForm();
		frame.setVisible(false);
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
					AppMenu appMenu = tableModel.getEntityByRow(selectedRow);
					menuService.remove(appMenu);
					tableModel.removeRow(selectedRow);
					frame.clearForm();
				}

			}
		} catch (Exception e) {
			Notifications.showDeleteRowErrorMessage();
		}
	}

}
