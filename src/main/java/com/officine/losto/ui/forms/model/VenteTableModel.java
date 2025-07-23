package com.officine.losto.ui.forms.model;


import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import com.officine.losto.backend.entity.Vente;
import com.officine.losto.backend.springcontext.session.SpringContext;
import com.officine.losto.backend.springcontext.session.UserSession;
import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.ui.shared.model.DefaultTableModel;

@Component
public class VenteTableModel extends DefaultTableModel<Vente> {
    private static final long serialVersionUID = 1L;
    private static final int ID = 0;
    private static final int NUMVENTE = 1;   
    private static final int DATE = 2;
    private static final int VENDEUR = 3;
    private static final int CLIENT = 4;
    private static final int TYPEVENTE = 5; 
    private static final int MODEPAIEMENT = 6;
    private static final int MONTANTPAYE = 7;
    private static final int MONTANTRENDU = 8;
    private static final int REMARQUE = 9;
    
   
    
 

    @Override
    public String[] getColumnLabels() {
        return new String[]{
        		ConstMessagesEN.Labels.ID,
                ConstMessagesEN.Labels.NUMVENTE,
                ConstMessagesEN.Labels.DATE,
                ConstMessagesEN.Labels.VENDEUR,
                ConstMessagesEN.Labels.CLIENT,
                ConstMessagesEN.Labels.TYPEVENTE, 
                ConstMessagesEN.Labels.MODEPAIEMENT, 
                ConstMessagesEN.Labels.MONTANTPAYE, 
                ConstMessagesEN.Labels.MONTANTRENDU, 
                ConstMessagesEN.Labels.REMARQUE
               };
    }
    
   
    
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Vente Vente  = entities.get(rowIndex);
        UserSession userSession = SpringContext.getBean(UserSession.class); 

        switch (columnIndex) {
        case ID : 
        	 return Vente.getId(); 
            case NUMVENTE :
                return Vente.getNumero();
            case DATE:
                return (Vente.getVentedate()==null ? null : Vente.getVentedate()); 
            case VENDEUR:
                return userSession.getAppUser().getNom().concat(" " + userSession.getAppUser().getPrenom());            
            case CLIENT:
                return (Vente.getClient() ==null ? null :  Vente.getClient());
            case TYPEVENTE:
                return Vente.getTypeVente().toString(); 
            case MODEPAIEMENT:
                return Vente.getModePaiement(); 
            case MONTANTPAYE:
                return Vente.getMontantPaye(); 
            case MONTANTRENDU:
                return Vente.getMontantRendu(); 
            case REMARQUE:
                return Vente.getRemarque(); 
            default:
                return Strings.EMPTY;
        }
    }

}
