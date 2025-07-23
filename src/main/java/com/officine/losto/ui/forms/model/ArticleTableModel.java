package com.officine.losto.ui.forms.model;

import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import com.officine.losto.backend.entity.Article;
import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.ui.shared.model.DefaultTableModel;

@Component
public class ArticleTableModel extends DefaultTableModel<Article> {
	private static final long serialVersionUID = 1L;
	private static final int ID = 0;
	private static final int NAME = 1;
	private static final int CODEBARRE = 2;
	private static final int DESCRIPTION = 3;
	private static final int FORME = 4;
	private static final int TYPE = 5;
	private static final int CATEGORIE = 6;
	private static final int RAYON = 7;
	private static final int DOSAGE = 8;
	private static final int CONDITIONNEMENT = 9;
	private static final int LOT = 10;
	private static final int QUANTITE = 11;
	private static final int PRIX_ACHAT = 12;
	private static final int PRIX_VENTE = 13;

	@Override
	public String[] getColumnLabels() {
		return new String[] { ConstMessagesEN.Labels.ID, ConstMessagesEN.Labels.NAME, ConstMessagesEN.Labels.CODEBARRE,
				ConstMessagesEN.Labels.DESCRIPTION, ConstMessagesEN.Labels.FORME, ConstMessagesEN.Labels.TYPE,
				ConstMessagesEN.Labels.CATEGORIE, ConstMessagesEN.Labels.RAYON, ConstMessagesEN.Labels.DOSAGE,
				ConstMessagesEN.Labels.CONDIONNEMENT, ConstMessagesEN.Labels.LOT,  ConstMessagesEN.Labels.QUANTITE,
				ConstMessagesEN.Labels.PRIX_ACHAT, ConstMessagesEN.Labels.PRIX_VENTE };
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Article article = entities.get(rowIndex);

		switch (columnIndex) {
		case ID:
			return article.getId();
		case NAME: 
			return (article.getArticleName() == null ? Strings.EMPTY : 
					article.getArticleName());
		case CODEBARRE:
			return  (article.getArticleCodeBarre() == null ? Strings.EMPTY : 
					article.getArticleCodeBarre());
		case DESCRIPTION:
			return article.getArticleDescription();
		case FORME:
			return  (article.getArticleForme() == null ? Strings.EMPTY : 
					article.getArticleForme().getFormeName());
		case TYPE:
			return (article.getArticleTyppe() == null ? Strings.EMPTY : 
					article.getArticleTyppe().getTyppeName() + " " + article.getArticleTyppe().getId());
		case CATEGORIE:
			return (article.getArticleCategorie() == null ? Strings.EMPTY :  
					article.getArticleCategorie().getCategorieName() + " " + article.getArticleCategorie().getId());
		case RAYON:

			return (article.getArticleRayon() == null ? Strings.EMPTY
					: article.getArticleRayon().getRayonName() + " " + article.getArticleRayon().getId());
		case DOSAGE:
			return article.getArticleDosage();
		case CONDITIONNEMENT:
			return (article.getArticlePackaging() == null ? Strings.EMPTY
					: article.getArticlePackaging().getPackagingName() + " " + article.getArticlePackaging().getId());
		case LOT:
			return (article.getArticleLot() == null ? Strings.EMPTY : 
					article.getArticleLot().getNumeroLot().concat(" "+article.getArticleLot().getId()));
			
		case QUANTITE:
			return article.getArticleQuantite_stock();
		case PRIX_ACHAT:
			return article.getArticlePrixAchat();
		case PRIX_VENTE:
			return article.getArticlePrixVente();
		default:
			return Strings.EMPTY;
		}
	}

}
