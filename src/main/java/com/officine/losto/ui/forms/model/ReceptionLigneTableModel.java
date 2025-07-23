package com.officine.losto.ui.forms.model;

import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import org.apache.logging.log4j.util.Strings;

import com.officine.losto.backend.entity.CommandeLigne;
import com.officine.losto.backend.entity.ReceptionLigne;
import com.officine.losto.backend.entity.utilities.Statut;
import com.officine.losto.backend.services.ReceptionLigneService;
import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.ui.shared.model.DefaultTableModel;

@org.springframework.stereotype.Component
public class ReceptionLigneTableModel extends DefaultTableModel<ReceptionLigne> {
	private static final long serialVersionUID = 1L;
	private static final int ID = 0;
	private static final int ARTICLE = 1;
	private static final int QUANTITE_COMMANDEE = 2;
	private static final int QUANTITE_RECUE = 3;
	private static final int QUANTITE_A_RECEVOIR = 4;
	private static final int STATUT = 5;
	private static final int NUM_LOT = 6;
	private static final int DATE_PEREMPTION = 7;
	private static final int OBSERVATION = 8;
	public DefaultTableCellRenderer colorRenderer;
	private ReceptionLigneService receptionLigneService;

	public ReceptionLigneTableModel(ReceptionLigneService receptionLigneService) {
		this.receptionLigneService = receptionLigneService;
	}

	@Override
	public String[] getColumnLabels() {
		return new String[] { ConstMessagesEN.Labels.ID, ConstMessagesEN.Labels.ARTICLE,
				ConstMessagesEN.Labels.QUANTITE_COMMANDEE, ConstMessagesEN.Labels.QUANTITE_RECUE,
				ConstMessagesEN.Labels.QUANTITE_A_RECEVOIR, ConstMessagesEN.Labels.STATUT,
				ConstMessagesEN.Labels.NUM_LOT, ConstMessagesEN.Labels.DATE_PEREMPTION,
				ConstMessagesEN.Labels.OBSERVATION };
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		ReceptionLigne receptionLigne = entities.get(rowIndex);
		CommandeLigne commandeLigne = receptionLigne.getCommandeLigne();

		List<ReceptionLigne> savedReceptionLignes = receptionLigneService.findReceptionsByCommandeLigne(commandeLigne);

		System.out.println("Cumul reception ="
				+ savedReceptionLignes.stream().collect(Collectors.summingInt(ReceptionLigne::getQuantiteRecue)));

		switch (columnIndex) {
		case ID:
			return receptionLigne.getId();
		case ARTICLE:
			return receptionLigne.getCommandeLigne().getCommandeLigneArticle().getArticleName()
					.concat(" " + receptionLigne.getCommandeLigne().getCommandeLigneArticle().getId());
		case QUANTITE_COMMANDEE:
			return receptionLigne.getCommandeLigne().getCommandeLigneQuantite();
		case QUANTITE_RECUE:
			return savedReceptionLignes.stream().collect(Collectors.summingInt(ReceptionLigne::getQuantiteRecue));
		case QUANTITE_A_RECEVOIR:
			return receptionLigne.getQuantiteRecue();
		case STATUT:
			if (receptionLigne.getCommandeLigne().getCommandeLigneQuantite() - savedReceptionLignes.stream()
					.collect(Collectors.summingInt(ReceptionLigne::getQuantiteRecue)) == 0)
				return Statut.COMPLETE.toString();
			else if (savedReceptionLignes.stream().collect(Collectors.summingInt(ReceptionLigne::getQuantiteRecue)) > 0)
				return Statut.PARTIELLE;
			else
				return Statut.NON;
		case NUM_LOT:
			return receptionLigne.getCommandeLigne().getCommandeLigneArticle().getArticleLot().getNumeroLot()
					.concat(" " + receptionLigne.getCommandeLigne().getCommandeLigneArticle().getArticleLot().getId());
		case DATE_PEREMPTION:
			return receptionLigne.getCommandeLigne().getCommandeLigneArticle().getArticleLot().getDatePeremptionLot();
		case OBSERVATION:
			return receptionLigne.getObservation();
 
		default:
			return Strings.EMPTY;
		}
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return column == 4 || column == 8; // qte reçue et observation
	}

	@Override
	public int getRowCount() {
		return entities.size();
	}

	@Override
	public int getColumnCount() {
		return getColumnLabels().length;
	}

	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {
		if (columnIndex != 4 && columnIndex != 8)
			return;
		try {
			// ici tu peux contrôler la valeur saisie
			if (value != null && !value.toString().trim().isEmpty()) {
				if (columnIndex == 4) {
					entities.get(rowIndex).setQuantiteRecue(Integer.parseInt(value.toString()));
				}

				if (columnIndex == 8)
					entities.get(rowIndex).setObservation(value.toString());

				fireTableCellUpdated(rowIndex, columnIndex);
			}
		} catch (Exception ex) {
			// model.setValueAt("Erreur", row, 1);
			// entities.get(rowIndex).setReceptionLigneQuantiteManquante(-100000);
			JOptionPane.showMessageDialog(null, "Veuillez entrer un nombre entier.", "Erreur de saisie",
					JOptionPane.ERROR_MESSAGE);
			entities.get(rowIndex).setQuantiteManquante(-1000000);

		}

	}

}
