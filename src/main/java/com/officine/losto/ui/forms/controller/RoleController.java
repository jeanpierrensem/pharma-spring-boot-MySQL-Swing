package com.officine.losto.ui.forms.controller;

import java.awt.event.ItemEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import org.springframework.stereotype.Controller;

import com.officine.losto.backend.entity.AppGroupe;
import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.services.security.GroupeService;
import com.officine.losto.ui.forms.Roleframe;
import com.officine.losto.ui.forms.model.ComboBoxModel;
import com.officine.losto.ui.shared.controller.AbstractFrameController;
import com.officine.losto.uti.shared.Shared;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class RoleController extends AbstractFrameController {
	private final GroupeService service;
	private final ComboBoxModel groupeComboBoxModel;
	private Roleframe frame;
	private Object data[][];

	private void prepareListeners(Roleframe frame) {
		registerAction(frame.getGroupeCB(), (e) -> itemStateChanged(e));
		registerAction(frame.getBtnAjouter(), (e) -> save());
		registerAction(frame.getBtnQuitter(), (e) -> closeModalWindow());
	}

	@Override
	public void prepareAndOpenFrame(JDialog parent) {
		
		showFrame();
		
	}

	private void showFrame() {
		frame = new Roleframe(data);
		prepareListeners(frame);
		loadGroupesUtilisateur();
		Shared.displayFrame(frame);

	}

	private void loadRoles(String groupeCode) {
		List<AppGroupe> groupes = service.listAllGroupes(groupeCode);

		updateTableModel(groupes);
	}

	private void updateTableModel(List<AppGroupe> groupes) {

		data = new Object[groupes.size()][ConstMessagesEN.Params.GROUPE_NB_COLUMNS];

		for (int i = 0; i < groupes.size(); i++) {

			data[i][0] = groupes.get(i).getMenueId();

			data[i][1] = groupes.get(i).getMenuName();
			data[i][2] = groupes.get(i).getMenuDescription();
			data[i][3] = groupes.get(i).isAfficher();

			data[i][4] = groupes.get(i).isEnregistrer();
			data[i][5] = groupes.get(i).isModifier();
			data[i][6] = groupes.get(i).isSupprimer();
			data[i][7] = groupes.get(i).isImprimer();
		}

		/*for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < ConstMessagesEN.Params.GROUPE_NB_COLUMNS; j++) {
				System.out.println("data[i][j] =" + data[i][j]);
			}
		}*/

		DefaultTableModel tableModel = new DefaultTableModel(data, ConstMessagesEN.Labels.columns);
		frame.getTable().setModel(tableModel);
	}

	private void loadGroupesUtilisateur() {
		List<String> groupes = service.listGroupes().stream().map(s -> s.getGroupeCode()).collect(Collectors.toList());

		Set<String> groupeSet = new HashSet<>(groupes);

		groupeComboBoxModel.clear();
		groupeComboBoxModel.addElements(groupeSet);
		frame.getGroupeCB().setModel(groupeComboBoxModel);
		
		if(groupeSet.size() <=0) 
		
		return ; 
		
		frame.getGroupeCB().setSelectedIndex(0);
		String groupeCode = frame.getGroupeCB().getSelectedItem().toString(); 
		loadRoles(groupeCode);

	}

	private void save() {
		String groupeCode = frame.getGroupeCB().getSelectedItem().toString();
		DefaultTableModel tableModel = (DefaultTableModel) frame.getTable().getModel();

		for (int i = 0; i < data.length; i++)
			for (int j = 0; j < ConstMessagesEN.Params.GROUPE_NB_COLUMNS; j++) {

				service.update((boolean) tableModel.getValueAt(i, 3), (boolean) tableModel.getValueAt(i, 4),
						(boolean) tableModel.getValueAt(i, 5), (boolean) tableModel.getValueAt(i, 6),
						(boolean) tableModel.getValueAt(i, 7), (String) groupeCode,
						((long) tableModel.getValueAt(i, 0)));
			}
		
		
		
		JOptionPane.showMessageDialog(frame, ConstMessagesEN.Messages.SUCCESS_MESSAGE,
				ConstMessagesEN.Messages.INFORMATION_TITLE, JOptionPane.INFORMATION_MESSAGE);
	}

	private void closeModalWindow() {
		frame.dispose();
	}

	public void itemStateChanged(ItemEvent e) {
		if (frame.getGroupeCB().getItemCount() > 0) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				String groupeCode = frame.getGroupeCB().getSelectedItem().toString();

				List<AppGroupe> groupes = service.listAllGroupes(groupeCode);
				updateTableModel(groupes);
			}
		}
	}
}
