package com.officine.losto.ui.forms.model;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import com.officine.losto.backend.entity.Lot;
import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.ui.shared.model.DefaultTableModel;

@Component
public class LotTableModel extends DefaultTableModel<Lot> {
	private static final long serialVersionUID = 1L;
	private static final int CODE = 0;
	private static final int NUMERO_LOT = 1;
	private static final int DATE_PEREMPTION = 2;
	private static final int QUANTITE = 3;
	private static final int FOURNISSEUR = 4;

	@Override
	public String[] getColumnLabels() {
		return new String[] { ConstMessagesEN.Labels.CODE, ConstMessagesEN.Labels.NUMERO_LOT, ConstMessagesEN.Labels.DATE_PEREMPTION,
				ConstMessagesEN.Labels.QUANTITE, ConstMessagesEN.Labels.FOURNISSEUR, };
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Lot lot = entities.get(rowIndex);

		switch (columnIndex) {
		case CODE:
			return lot.getId(); 
		case NUMERO_LOT:
			return lot.getNumeroLot();
		case DATE_PEREMPTION:
			return lot.getDatePeremptionLot();
		case QUANTITE:
			return lot.getQuantiteLot();
		case FOURNISSEUR:
			return (lot.getFournisseurLot() == null ? Strings.EMPTY
					: lot.getFournisseurLot().getFournisseurName()+ " " + lot.getId());
		default:
			return Strings.EMPTY;
		}
	}

}
