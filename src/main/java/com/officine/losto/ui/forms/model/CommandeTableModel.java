package com.officine.losto.ui.forms.model;


import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import com.officine.losto.backend.entity.Commande;
import com.officine.losto.backend.springcontext.session.SpringContext;
import com.officine.losto.backend.springcontext.session.UserSession;
import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.ui.shared.model.DefaultTableModel;

@Component
public class CommandeTableModel extends DefaultTableModel<Commande> {
    private static final long serialVersionUID = 1L;
    private static final int ID = 0;
    private static final int NUMCOMMANDE = 1;
    private static final int DATE = 2;
    private static final int COMMANDER_PAR = 3;
    private static final int FOURNISSEUR = 4;
    private static final int MODELIVRAISON = 5; 
    private static final int INDICATION = 6;
    private static final int STATUT = 7;
   

    @Override
    public String[] getColumnLabels() {
        return new String[]{
        		ConstMessagesEN.Labels.ID,
                ConstMessagesEN.Labels.NUMCOMMANDE,
                ConstMessagesEN.Labels.DATE,
                ConstMessagesEN.Labels.COMMANDER_PAR,
                ConstMessagesEN.Labels.FOURNISSEUR,
                ConstMessagesEN.Labels.MODELIVRAISON, 
                ConstMessagesEN.Labels.INDICATION, 
                ConstMessagesEN.Labels.STATUT
               };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Commande commande  = entities.get(rowIndex);
        UserSession userSession = SpringContext.getBean(UserSession.class); 

        switch (columnIndex) {
        case ID : 
        	 return commande.getId(); 
            case NUMCOMMANDE:
                return commande.getCommandeNumero();
            case DATE:
                return (commande.getCommandeDate()==null ? null : commande.getCommandeDate()); 
            case COMMANDER_PAR:
                return userSession.getAppUser().getNom().concat(" " + userSession.getAppUser().getPrenom());            
            case FOURNISSEUR:
                return (commande.getCommandeFournisseur() ==null ? null :  commande.getCommandeFournisseur().getFournisseurName().concat(" "+commande.getCommandeFournisseur().getId()));
            case MODELIVRAISON:
                return commande.getCommandeLivraisonMode().toString(); 
            case INDICATION:
                return commande.getCommandeInstruction(); 
            case STATUT:
                return commande.getCommandeStatut(); 
            default:
                return Strings.EMPTY;
        }
    }

}
