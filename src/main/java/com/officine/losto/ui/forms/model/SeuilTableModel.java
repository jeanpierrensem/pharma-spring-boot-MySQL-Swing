package com.officine.losto.ui.forms.model;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import com.officine.losto.backend.entity.Seuil;
import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.ui.shared.model.DefaultTableModel;

@Component
public class SeuilTableModel extends DefaultTableModel<Seuil> {
	private static final long serialVersionUID = 1L;
	private static final int ID = 0;
	private static final int CODE = 1;
	private static final int VALEUR_SEUIL = 2;
	private static final int DESCRIPTION = 3;

	@Override
	public String[] getColumnLabels() {
		return new String[] { ConstMessagesEN.Labels.ID, ConstMessagesEN.Labels.CODE,
				ConstMessagesEN.Labels.VALEUR_SEUIL, ConstMessagesEN.Labels.DESCRIPTION, };
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Seuil seuil = entities.get(rowIndex);

		switch (columnIndex) {
		case ID:
			return seuil.getId();
		case CODE:
			return seuil.getSeuilCode();
		case VALEUR_SEUIL:
			return seuil.getSeuilNiveau();
		case DESCRIPTION:
			return seuil.getSeuilDescription();
		default:
			return Strings.EMPTY;
		}
	}

}
