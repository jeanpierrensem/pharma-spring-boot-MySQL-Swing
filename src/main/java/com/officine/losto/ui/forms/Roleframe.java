package com.officine.losto.ui.forms;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.SystemColor;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.params.constant.ConstMessagesEN.Params;

import lombok.Getter;
import java.awt.Dimension;

@org.springframework.stereotype.Component
@Getter
public class Roleframe extends JDialog {

	private static final long serialVersionUID = 1L;
	private JButton btnQuitter;
	private JButton btnAjouter;

	private JPanel tablePanel;
	private JPanel btnPanel;

	private JComboBox<String> groupeCB;
	private JLabel jLabel1;
	private JScrollPane RoleJScrollPane1;
	private JTable table;
	
	
	private JTableHeader tableHeader; 

	//public Roleframe() {}

	public Roleframe(Object[][] data) {
		DefaultTableModel tableModel = new DefaultTableModel(data, ConstMessagesEN.Labels.columns);
		
	
		table = new javax.swing.JTable(tableModel) {

			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);
				if (!isRowSelected(row)) {
					c.setBackground(row % 2 == 0 ? UIManager.getColor("ToolTip.background") : Color.white);
				}

				if (isRowSelected(row) && isColumnSelected(column))
					((JComponent) c).setBorder(new LineBorder(Color.red));

				return c;
			}

			boolean[] canEdit = new boolean[] { false, false, false, true, true, true, true, true };

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}

			// Types permet de spécifier le type de données de chaque celle du tableau
			Class[] types = new Class[] { java.lang.String.class, java.lang.String.class, java.lang.String.class,
					java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class,
					java.lang.Boolean.class };

			// Ici on place dans chaque cellule du tableau le composant swing correspondant,
			// le cas échéan un Checkbox
			public Class getColumnClass(int columnIndex) {
				return types[columnIndex];
			}
			
		

		};
		table.setForeground(SystemColor.inactiveCaptionText);
		table.setFont(new Font("Dialog", Font.PLAIN, 11));
		table.setGridColor(SystemColor.lightGray);
		
		tableHeader = this.getTable().getTableHeader();
		tableHeader.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, ConstMessagesEN.Labels.POLICE_SIZE));
		tableHeader.setBackground(Color.WHITE);
		this.getTable().setRowHeight(30);
		tableHeader.setPreferredSize(new Dimension(tableHeader.getWidth(), 30));
		
		
		
		

		initComponents();

	}

	private void initComponents() {
		setTitle(ConstMessagesEN.DialogTitles.ROLE);
		setResizable(false);

		tablePanel = new javax.swing.JPanel();
		tablePanel.setBackground(Color.WHITE);
		tablePanel.setBorder(new LineBorder(SystemColor.window, 10, true));
		jLabel1 = new javax.swing.JLabel();
		jLabel1.setFont(new Font("Trebuchet MS", Font.BOLD, 13));
		jLabel1.setForeground(Color.GRAY);

		groupeCB = new JComboBox<String>();
		groupeCB.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 12));
		groupeCB.setBackground(SystemColor.window);
		groupeCB.setForeground(SystemColor.inactiveCaptionText);

		// button section
		btnPanel = new javax.swing.JPanel();
		btnPanel.setBackground(Color.WHITE);
		btnPanel.setBorder(new LineBorder(SystemColor.activeCaption));
		btnAjouter = new javax.swing.JButton();
		btnAjouter.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, 10));
		btnAjouter.setText(ConstMessagesEN.Labels.ENREGISTRER_BTN);
		btnAjouter.setIcon(new ImageIcon(ClassLoader.getSystemResource(Params.BASE_PATH + "images/Apply.png")));

		btnQuitter = new javax.swing.JButton();
		btnQuitter.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, 10));
		btnQuitter.setText(ConstMessagesEN.Labels.QUITTER_BTN);
		btnQuitter.setIcon(new ImageIcon(ClassLoader.getSystemResource(Params.BASE_PATH + "images/Cancel.png")));

		// table section
		RoleJScrollPane1 = new javax.swing.JScrollPane();

		table.setBackground(SystemColor.window);
		table.setGridColor(SystemColor.lightGray);

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		tablePanel.setPreferredSize(new Dimension(720, 40));
		jLabel1.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, 10));
		jLabel1.setText("Groupe Utilisateur");

		javax.swing.GroupLayout gl_tablePanel = new javax.swing.GroupLayout(tablePanel);
		gl_tablePanel.setHorizontalGroup(
			gl_tablePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_tablePanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(jLabel1)
					.addGap(18)
					.addComponent(groupeCB, GroupLayout.PREFERRED_SIZE, 217, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(341, Short.MAX_VALUE))
		);
		gl_tablePanel.setVerticalGroup(
			gl_tablePanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_tablePanel.createSequentialGroup()
					.addGroup(gl_tablePanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(jLabel1)
						.addComponent(groupeCB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		tablePanel.setLayout(gl_tablePanel);

		getContentPane().add(tablePanel, java.awt.BorderLayout.PAGE_START);

		btnPanel.setPreferredSize(new Dimension(720, 40));

		javax.swing.GroupLayout gl_btnPanel = new javax.swing.GroupLayout(btnPanel);
		gl_btnPanel.setHorizontalGroup(gl_btnPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_btnPanel.createSequentialGroup().addContainerGap().addComponent(btnAjouter)
						.addPreferredGap(ComponentPlacement.RELATED, 423, Short.MAX_VALUE).addComponent(btnQuitter)
						.addContainerGap()));
		gl_btnPanel.setVerticalGroup(gl_btnPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_btnPanel.createSequentialGroup().addContainerGap(15, Short.MAX_VALUE).addGroup(gl_btnPanel
						.createParallelGroup(Alignment.BASELINE).addComponent(btnAjouter).addComponent(btnQuitter))
						.addContainerGap()));
		btnPanel.setLayout(gl_btnPanel);
		getContentPane().add(btnPanel, java.awt.BorderLayout.PAGE_END);

		RoleJScrollPane1.setViewportView(table);
		getContentPane().add(RoleJScrollPane1, java.awt.BorderLayout.CENTER);

		
		

		pack();
	}

}
