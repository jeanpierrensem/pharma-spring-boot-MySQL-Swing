package com.officine.losto.ui.forms.model;


import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import com.officine.losto.backend.entity.Forme;
import com.officine.losto.backend.entity.Typpe;
import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.ui.shared.model.DefaultTableModel;

@Component
public class FormeTableModel extends DefaultTableModel<Forme> {
    private static final long serialVersionUID = 1L;
	private static final int CODE = 0;
    private static final int LIBELLE = 1;
    private static final int DESCRIPTION = 2;


    @Override
    public String[] getColumnLabels() {
        return new String[]{
                ConstMessagesEN.Labels.CODE,
                ConstMessagesEN.Labels.LIBELLE,
                ConstMessagesEN.Labels.DESCRIPTION,
               };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Forme forme  = entities.get(rowIndex);

        switch (columnIndex) {
            case CODE:
                return forme.getId();
            case LIBELLE:
                return forme.getFormeName();
            case DESCRIPTION:
                return forme.getFormeDescription(); 
            default:
                return Strings.EMPTY;
        }
    }

}
