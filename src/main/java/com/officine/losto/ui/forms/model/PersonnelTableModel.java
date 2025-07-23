package com.officine.losto.ui.forms.model;


import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import com.officine.losto.backend.entity.AppUser;
import com.officine.losto.backend.entity.Rayon;
import com.officine.losto.backend.entity.Typpe;
import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.ui.shared.model.DefaultTableModel;

@Component
public class PersonnelTableModel extends DefaultTableModel<AppUser> {
    private static final long serialVersionUID = 1L;
	private static final int CODE = 0;
    private static final int MATRICULE = 1;
    private static final int NOM = 2;
    private static final int PRENOM = 3;
    private static final int GROUPE = 4;
    private static final int LOGIN = 5;
    private static final int MOT_DE_PASSE = 6;


    @Override
    public String[] getColumnLabels() {
        return new String[]{
                ConstMessagesEN.Labels.CODE,
                ConstMessagesEN.Labels.MATRICULE,
                ConstMessagesEN.Labels.NOM,
                ConstMessagesEN.Labels.PRENOM,
                ConstMessagesEN.Labels.GROUPE,
                ConstMessagesEN.Labels.LOGIN,
                ConstMessagesEN.Labels.MOT_DE_PASSE,
               };
    }
    

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        AppUser appUser   = entities.get(rowIndex);

        switch (columnIndex) {
            case CODE:
                return    appUser.getId(); 
            case MATRICULE:
                return appUser.getMatricule(); 
            case NOM:
                return appUser.getNom(); 
            case PRENOM:
                return appUser.getPrenom(); 
            case GROUPE:
            
                return (appUser.getAppGroupe() == null ? Strings.EMPTY
    					: appUser.getAppGroupe().getGroupeName().concat(" ").concat(appUser.getAppGroupe().getGroupeCode()));
            case LOGIN:
                return appUser.getUsername(); 
            case MOT_DE_PASSE:
                return appUser.getPassword(); 
            default:
                return Strings.EMPTY;
        }
    }

}
