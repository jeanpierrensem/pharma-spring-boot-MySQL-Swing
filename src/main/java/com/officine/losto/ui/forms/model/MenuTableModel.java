package com.officine.losto.ui.forms.model;


import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import com.officine.losto.backend.entity.AppMenu;
import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.ui.shared.model.DefaultTableModel;

@Component
public class MenuTableModel extends DefaultTableModel<AppMenu> {
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
        AppMenu menu  = entities.get(rowIndex);

        switch (columnIndex) {
            case CODE:
                return menu.getId();
            case LIBELLE:
                return menu.getMenuName();
            case DESCRIPTION:
                return menu.getMenuDescription();
            default:
                return Strings.EMPTY;
        }
    }

}
