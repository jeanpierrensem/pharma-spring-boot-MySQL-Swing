package com.officine.losto.ui.forms.model;

import java.math.BigDecimal;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.officine.losto.backend.entity.Article;
import com.officine.losto.backend.entity.VenteLigne;
import com.officine.losto.backend.services.ArticleService;
import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.ui.forms.MainMenuFrame;
import com.officine.losto.ui.shared.model.DefaultTableModel;
import com.officine.losto.uti.shared.Shared;

@Component
public class LigneVenteTableModel extends DefaultTableModel<VenteLigne> {
	private static final long serialVersionUID = 1L;
	private static final int CODEBARRE = 0;
	private static final int DESIGNATION = 1;
	private static final int QTESTOCK = 2 ; 
	private static final int PRIX_UNITAIRE_HT = 3;
	private static final int REMISE = 4;
	private static final int QUANTITE = 5;
	private static final int PRIX_TOTAL_HT = 6;

	@Autowired
	private ArticleService articleService;

	@Override
	public String[] getColumnLabels() {
		return new String[] { ConstMessagesEN.Labels.CODEBARRE, ConstMessagesEN.Labels.DESIGNATION, ConstMessagesEN.Labels.QTESTOCK,
				ConstMessagesEN.Labels.PRIX_UNITAIRE_HT, ConstMessagesEN.Labels.REMISE, ConstMessagesEN.Labels.QUANTITE,
				ConstMessagesEN.Labels.PRIX_TOTAL_HT };
	}

	@Override
	public int getRowCount() {
		return entities.size(); 
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return column == 0 || column == 4 || column == 5; // qte reçue et observation
	}

	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex) {

		if (columnIndex != 0 && columnIndex != 4 && columnIndex != 5)
			return;

		switch (columnIndex) {

		case 0:

			Article articleToExtract = articleService.loadByArticleCodeBarre(value.toString());
			if (articleToExtract == null) {

				entities.get(rowIndex).setArticle(null);
				entities.get(rowIndex).setId(null);
				entities.get(rowIndex).setPrixTotal(null);
				entities.get(rowIndex).setQuantite(0);
				entities.get(rowIndex).setRemise(0);
				entities.get(rowIndex).setVente(null);
				entities.get(rowIndex).setPrixTotal(BigDecimal.valueOf(Long.parseLong("" + 0)));
				fireTableCellUpdated(rowIndex, columnIndex);
				setTotalvente();
				return;
			}

			entities.get(rowIndex).setArticle(articleToExtract);
			entities.get(rowIndex).getArticle().setArticleCodeBarre(value.toString());
			entities.get(rowIndex).setQuantite(0);
			entities.get(rowIndex).setRemise(0);

			fireTableCellUpdated(rowIndex, columnIndex);
			setTotalvente();
			return;

		case 4:
			if (entities.get(rowIndex).getArticle() == null) {
				entities.get(rowIndex).setArticle(null);
				entities.get(rowIndex).setId(null);
				entities.get(rowIndex).setPrixTotal(null);
				entities.get(rowIndex).setQuantite(0);
				entities.get(rowIndex).setRemise(0);
				entities.get(rowIndex).setVente(null);
				entities.get(rowIndex).setPrixTotal(BigDecimal.valueOf(Long.parseLong("" + 0)));
				fireTableCellUpdated(rowIndex, columnIndex);
				setTotalvente();
				return;
			}

			entities.get(rowIndex).setRemise(Integer.parseInt(value.toString()));

			entities.get(rowIndex)
					.setPrixTotal(
							getLignePrixTotal(entities.get(rowIndex).getArticle().getArticlePrixVente(),
							entities.get(rowIndex).getRemise(), entities.get(rowIndex).getQuantite()));
			fireTableCellUpdated(rowIndex, columnIndex);
			setTotalvente();
			return;

		case 5:
			if (entities.get(rowIndex).getArticle() == null) {
				entities.get(rowIndex).setArticle(null);
				entities.get(rowIndex).setId(null);
				entities.get(rowIndex).setPrixTotal(null);
				entities.get(rowIndex).setQuantite(0);
				entities.get(rowIndex).setRemise(0);
				entities.get(rowIndex).setVente(null);
				entities.get(rowIndex).setPrixTotal(BigDecimal.valueOf(Long.parseLong("" + 0)));
				fireTableCellUpdated(rowIndex, columnIndex);
				setTotalvente();
				return;
			}

			entities.get(rowIndex).setQuantite(Integer.parseInt(value.toString()));

			entities.get(rowIndex)
					.setPrixTotal(getLignePrixTotal(entities.get(rowIndex).getArticle().getArticlePrixVente(),
							entities.get(rowIndex).getRemise(), entities.get(rowIndex).getQuantite()));
			fireTableCellUpdated(rowIndex, columnIndex);
			setTotalvente();

		default:
			return;

		}
	}

	private BigDecimal getLignePrixTotal(BigDecimal pu, int remise, int qte) {
		BigDecimal remiseCalcule = new BigDecimal(remise).divide(new BigDecimal(100)); 	
		return pu.subtract(remiseCalcule).multiply(new BigDecimal(qte));
	}

	private void setTotalvente() {	
		try {
			BigDecimal prixTotal = new BigDecimal(0);
			for (int i = 0; i < this.getRowCount(); i++) {
				if (this.getValueAt(i, 6) != null) {
					prixTotal = prixTotal.add(new BigDecimal(this.getValueAt(i, 6).toString()));
				}
			}
			if (MainMenuFrame.ctrl.venteFrame != null)
				MainMenuFrame.ctrl.venteFrame.getTb_prix_total().setText("" + prixTotal);
			MainMenuFrame.ctrl.venteFrame.getLbl_enLettre()
					.setText(Shared.toLetter(Double.parseDouble(prixTotal.toString())));

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		VenteLigne venteLigne = entities.get(rowIndex);
		if (venteLigne.getArticle() == null)
			return null;

		switch (columnIndex) {

		case CODEBARRE:
			return (venteLigne.getArticle().getArticleCodeBarre() == null ? Strings.EMPTY
					: venteLigne.getArticle().getArticleCodeBarre());
		case DESIGNATION:
			return (venteLigne.getArticle() == null ? Strings.EMPTY
					: venteLigne.getArticle().getArticleName()
							.concat(" " + venteLigne.getArticle().getArticleDosage()));
		case QTESTOCK:
			return (venteLigne.getArticle().getArticleQuantite_stock() == 0 ? "0"
					: venteLigne.getArticle().getArticleQuantite_stock()) ; 
		case PRIX_UNITAIRE_HT:
			return (venteLigne.getArticle() == null ? null : venteLigne.getArticle().getArticlePrixVente());
		case REMISE:
			return venteLigne.getRemise();
		case QUANTITE:
			return (venteLigne == null ? Strings.EMPTY : venteLigne.getQuantite());
		case PRIX_TOTAL_HT:
			BigDecimal remiseCalcule = new BigDecimal(venteLigne.getRemise()).divide(new BigDecimal(100)); 	
			
			return (venteLigne.getArticle() == null ? BigDecimal.valueOf(0)
					: (venteLigne.getArticle().getArticlePrixVente().subtract(remiseCalcule)
							.multiply(new BigDecimal(venteLigne.getQuantite()))));
			
		default:
			return Strings.EMPTY;
		}
	}

}
