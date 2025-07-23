package com.officine.losto.ui.forms.model;


import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import com.officine.losto.backend.entity.AppGroupe;
import com.officine.losto.backend.entity.AppGroupeDisplay;
import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.ui.shared.model.DefaultTableModel;

@Component
public class GroupeTableModel extends DefaultTableModel<AppGroupeDisplay> {
    private static final long serialVersionUID = 1L;
	private static final int ID = 0;
	private static final int CODE = 1;
    private static final int LIBELLE = 2;
    private static final int DESCRIPTION = 3;


    @Override
    public String[] getColumnLabels() {
        return new String[]{
        	     ConstMessagesEN.Labels.ID,
                ConstMessagesEN.Labels.CODE,
                ConstMessagesEN.Labels.LIBELLE,
                ConstMessagesEN.Labels.DESCRIPTION,
               };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        AppGroupeDisplay appGroupeDisplay  = entities.get(rowIndex);

        switch (columnIndex) {
            case ID : 
            	 return appGroupeDisplay.getId(); 
            case CODE:
                return appGroupeDisplay.getGroupeCode(); 
            case LIBELLE:
                return appGroupeDisplay.getGroupeName(); 
            case DESCRIPTION:
                return appGroupeDisplay.getGroupeDescription(); 
            default:
                return Strings.EMPTY;
        }
    }

}
