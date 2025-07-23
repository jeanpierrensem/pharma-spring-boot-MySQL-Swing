package com.officine.losto.ui.forms.model;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import com.officine.losto.backend.entity.Fournisseur;
import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.ui.shared.model.DefaultTableModel;

@Component
public class FournisseurTableModel extends DefaultTableModel<Fournisseur> {
	private static final long serialVersionUID = 1L;
	private static final int CODE = 0;
	private static final int NOM_FOURNISSEUR = 1;
	private static final int ADRESSE_FOURNISSEUR = 2;

	@Override
	public String[] getColumnLabels() {
		return new String[] { ConstMessagesEN.Labels.CODE, ConstMessagesEN.Labels.NOM_FOURNISSEUR,
				ConstMessagesEN.Labels.ADRESSE_FOURNISSEUR };
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Fournisseur fournisseur = entities.get(rowIndex);

		switch (columnIndex) {
		case CODE:
			return fournisseur.getId();
		case NOM_FOURNISSEUR:
			return fournisseur.getFournisseurName();
		case ADRESSE_FOURNISSEUR:
			return fournisseur.getFournisseurAdresse();
		default:
			return Strings.EMPTY;
		}
	}

}
