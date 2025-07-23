package com.officine.losto.ui.shared.lookAndFeel;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.officine.losto.backend.springcontext.session.UserSession;

public class  LookAndFeel {

	public  void LookAndFeel() {}
	



	public static DefaultTableCellRenderer   colorRenderer = new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            c.setBackground(new Color(220, 240, 255)); // couleur bleu clair
            return c;
        }
    };		

	
	
	
	
	
}