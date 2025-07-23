package com.officine.losto.ui.forms.model;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import com.officine.losto.backend.entity.Article;
import com.officine.losto.backend.entity.CommandeLigne;
import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.ui.shared.model.DefaultTableModel;

@Component
public class CommandeLigneTableModel extends DefaultTableModel<CommandeLigne> {
	private static final long serialVersionUID = 1L;
	private static final int ID = 0;
	private static final int ARTICLE = 1;
	private static final int REFERENCE = 2;
	private static final int QUANTITE = 3;
	private static final int PRIX_UNITAIRE_HT = 4;
	private static final int REMISE = 5;
	private static final int PRIX_TOTAL_HT = 6;


	@Override
	public String[] getColumnLabels() {
		return new String[] {ConstMessagesEN.Labels.ID, ConstMessagesEN.Labels.ARTICLE, ConstMessagesEN.Labels.REFERENCE,
				ConstMessagesEN.Labels.QUANTITE, ConstMessagesEN.Labels.PRIX_UNITAIRE_HT, ConstMessagesEN.Labels.REMISE,
				ConstMessagesEN.Labels.PRIX_TOTAL_HT };
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		CommandeLigne commandeLigne = entities.get(rowIndex);

		switch (columnIndex) {
		case ID:
			return commandeLigne.getId();
		case ARTICLE:
			return  commandeLigne.getCommandeLigneArticle().getArticleName().concat(" "+ commandeLigne.getCommandeLigneArticle().getId()); 
		case REFERENCE:
			return commandeLigne.getCommandeLigneReferenceArticle(); 
		case QUANTITE:
			return commandeLigne.getCommandeLigneQuantite();
		
		case PRIX_UNITAIRE_HT:
			return commandeLigne.getCommandeLignePrixUnitaireHT(); 
			
		case REMISE:
			return commandeLigne.getCommandeLigneRemise(); 
		case PRIX_TOTAL_HT:
			return commandeLigne.getCommandeLignePrixTotalHT(); 
		default:
			return Strings.EMPTY;
		}
	}

}
