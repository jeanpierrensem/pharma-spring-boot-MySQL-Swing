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
import com.officine.losto.backend.entity.AppGroupe;
import com.officine.losto.backend.entity.AppUser;
import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.security.cryptographic.Cryptographic;
import com.officine.losto.services.security.AccountService;
import com.officine.losto.services.security.GroupeService;
import com.officine.losto.ui.forms.PersonnelFrame;
import com.officine.losto.ui.forms.model.ComboBoxModel;
import com.officine.losto.ui.forms.model.PersonnelTableModel;
import com.officine.losto.ui.shared.controller.AbstractFrameController;
import com.officine.losto.uti.shared.ConfirmDialog;
import com.officine.losto.uti.shared.Shared;
import com.officine.losto.util.notification.Notifications;
import com.officine.losto.validation.PersonnelValidator;

import lombok.AllArgsConstructor;;

@Controller
@AllArgsConstructor
public class PersonnelController extends AbstractFrameController {

	private final PersonnelTableModel tableModel;
	private final AccountService accountService;
	private final PersonnelValidator validator;
	private final GroupeService groupeService;
	private final ComboBoxModel groupeComboBoxModel;
	private PersonnelFrame frame;

	@SuppressWarnings("PMD.UnusedPrivateMethod")

	private void prepareListeners(PersonnelFrame frame) {
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
				findEntitybyCriteria(frame.getTb_search().getText().trim(), frame.getTb_search().getText().trim(),
						frame.getTb_search().getText().trim(), frame.getTb_search().getText().trim(),
						frame.getTb_search().getText().trim());

			}
		});

		frame.getTb_search().addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				findEntitybyCriteria(frame.getTb_search().getText().trim(), frame.getTb_search().getText().trim(),
						frame.getTb_search().getText().trim(), frame.getTb_search().getText().trim(),
						frame.getTb_search().getText().trim());
			}
		});

	}

	public void prepareAndOpenFrame(JDialog parent) {
		showFrame();
	}

	private void showFrame() {
		frame = new PersonnelFrame(tableModel);
		prepareListeners(frame);
		loadGroupesUtilisateur();
		loadPersonnels();
		Shared.displayFrame(frame);
	}

	private void loadPersonnels() {
		List<AppUser> users = accountService.listUsers();
		tableModel.clear();
		tableModel.addEntities(users);
	}

	private void loadGroupesUtilisateur() {
		List<String> groupes = groupeService.listGroupes().stream()
				.map(s -> s.getGroupeName().concat(" ").concat(s.getGroupeCode())).collect(Collectors.toList());

		Set<String> groupeSet = new HashSet<>(groupes);

		groupeComboBoxModel.clear();
		groupeComboBoxModel.addElements(groupeSet);
		frame.getCb_groupe().setModel(groupeComboBoxModel);
		if (groupeSet.size() > 0)
			frame.getCb_groupe().setSelectedIndex(0);
	}

	private void findEntitybyCriteria(String matricule, String nom, String prenom, String groupeName, String login) {
		List<AppUser> personnels = accountService.findUserByCriteria(matricule, nom, prenom, groupeName, login);
		tableModel.clear();
		tableModel.addEntities(personnels);
	}

	private void save() {

		// password confirm
		String password = frame.getTb_mot_passe().getText();
		String login = frame.getTb_login().getText().trim().toString();
		String confirm_password = frame.getTb_mot_passe_confirm().getText();

		if (!Cryptographic.isPasswordCorrect(password, confirm_password)) {
			ConfirmDialog confirmDialog = new ConfirmDialog();
			confirmDialog.showInfo(frame, " Mots de Passe non identiques ! Merci de Réessayer");
			return;
		}

		AppUser appUser = frame.getPersonnelFromForm();

		String chaineCrypter = Cryptographic.encode(login.concat(password.toString()), ConstMessagesEN.Params.ENCODING_ALGORITHM);

		appUser.setPassword(chaineCrypter);

		// retrieve_group_in_controller
		String[] groupeCodes = frame.getCb_groupe().getSelectedItem().toString().split(" ");
		AppGroupe appGroupe = groupeService.listAllGroupes(groupeCodes[groupeCodes.length - 1]).get(0);
		appUser.setAppGroupe(appGroupe);

		Optional<ValidationError> errors = validator.validate(appUser);

		if (errors.isPresent()) {
			ValidationError validationError = errors.get();
			ConfirmDialog confirmDialog = new ConfirmDialog();
			confirmDialog.showInfo(frame, validationError.message());
			return;
		}
		if (frame.getLbl_id().getText().trim().equalsIgnoreCase(Strings.EMPTY)) {
			accountService.save(appUser);

			tableModel.addEntity(appUser);
			frame.clearForm();
			return;
		}
		accountService.saveAndFlush(appUser);
		int selectedRow = frame.getTable().getSelectedRow();
		tableModel.updateEntity(selectedRow, appUser);
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
					AppUser appUser = tableModel.getEntityByRow(selectedRow);
					accountService.remove(appUser);
					tableModel.removeRow(selectedRow);
					frame.clearForm();
				}

			}
		} catch (Exception e) {
			Notifications.showDeleteRowErrorMessage();
		}
	}

}
