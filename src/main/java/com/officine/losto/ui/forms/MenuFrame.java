package com.officine.losto.ui.forms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import org.apache.logging.log4j.util.Strings;

import com.officine.losto.backend.entity.AppMenu;
import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.params.constant.ConstMessagesEN.Params;
import com.officine.losto.ui.forms.model.MenuTableModel;
import com.officine.losto.uti.shared.Shared;

import lombok.Getter;

@org.springframework.stereotype.Component
@Getter

public class MenuFrame extends JDialog {

	private static final long serialVersionUID = 1L;

	private javax.swing.JButton btnAjouter;
	private javax.swing.JButton btnSupprimer;
	private javax.swing.JButton btnQuitter;

	private JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel9;
	private javax.swing.JPanel BtnjPanel;
	private javax.swing.JPanel FormJPanel;
	private javax.swing.JPanel jPanel4;

	private JScrollPane groupeTablejScrollPane;
	private JTextField tb_code;
	private JTextField tb_libelle;
	private JTextField tb_search;

	private JPanel TablePanel;
	private JTable table;
	private JTextArea tb_description;
	private JLabel jLabel2_1;

	private JTableHeader tableHeader; 
	
	//public MenuFrame() {}

	@SuppressWarnings("static-access")
	public MenuFrame(MenuTableModel tableModel ) {
		setModal(true);
		setAlwaysOnTop(true);
	
		initComponents();
		Shared.setFrameUp(MainMenuFrame.ctrl.menuFrame, this);
		this.getTable().setModel(tableModel);
		
		tableHeader = this.getTable().getTableHeader();
		tableHeader.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, ConstMessagesEN.Labels.POLICE_SIZE));
		tableHeader.setBackground(ConstMessagesEN.Labels.HEADER_COLOR_ALl);
		
	}

	@SuppressWarnings("serial")
	private void initComponents() {
		setTitle(ConstMessagesEN.DialogTitles.MENU);
		//setUndecorated(true);
		setResizable(false);

		BtnjPanel = new javax.swing.JPanel();
		BtnjPanel.setBorder(new LineBorder(Color.GRAY));
		BtnjPanel.setBackground(Color.WHITE);
		// jPanel1.setBorder(BorderFactory.createTitledBorder("Jpanel1"));
		FormJPanel = new javax.swing.JPanel();
		FormJPanel.setBorder(new LineBorder(new Color(153, 180, 209)));
		FormJPanel.setBackground(Color.WHITE);
		FormJPanel.setPreferredSize(new Dimension(270, 300));

		TablePanel = new javax.swing.JPanel();
		TablePanel.setBackground(Color.WHITE);
		// groupeTablePanel.setBorder(null);

		btnAjouter = new javax.swing.JButton();
		btnAjouter.setBounds(10, 11, 112, 21);
		btnAjouter.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, 10));
		btnSupprimer = new javax.swing.JButton();
	
		btnSupprimer.setBounds(132, 11, 105, 21);
		btnSupprimer.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, 10));
		btnQuitter = new javax.swing.JButton();
		
		btnQuitter.setBounds(605, 11, 105, 21);
		btnQuitter.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, 10));

		jLabel1 = new javax.swing.JLabel();
		jLabel1.setBounds(10, 14, 63, 14);
		jLabel2 = new javax.swing.JLabel();
		jLabel2.setBounds(10, 48, 76, 14);
		jLabel3 = new javax.swing.JLabel();
		jLabel3.setBounds(10, 93, 118, 14);

		tb_code = new javax.swing.JTextField();
		tb_code.setBounds(85, 11, 166, 20);
		tb_code.setBackground(SystemColor.window);

		tb_libelle = new javax.swing.JTextField();
		tb_libelle.setBounds(103, 45, 148, 20);
		tb_libelle.setBackground(SystemColor.window);

		// jPanel3.setBorder(new LineBorder(Color.GREEN, 2));
		jPanel4 = new javax.swing.JPanel();
		jPanel4.setBounds(0, 260, 449, 40);
		jLabel9 = new javax.swing.JLabel();
		tb_search = new javax.swing.JTextField();

		// jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		BtnjPanel.setPreferredSize(new Dimension(720, 40));

		btnAjouter.setText(ConstMessagesEN.Labels.ENREGISTRER_BTN);
		btnAjouter.setIcon(new ImageIcon(ClassLoader.getSystemResource(Params.BASE_PATH + "images/Apply.png")));

		btnSupprimer.setText(ConstMessagesEN.Labels.SUPPRIMER_BTN);
		btnSupprimer
				.setIcon(new ImageIcon(ClassLoader.getSystemResource(Params.BASE_PATH + "images/Delete.png")));

		btnQuitter.setText(ConstMessagesEN.Labels.QUITTER_BTN);
		btnQuitter.setIcon(new ImageIcon(ClassLoader.getSystemResource(Params.BASE_PATH + "images/Cancel.png")));

		getContentPane().add(BtnjPanel, java.awt.BorderLayout.PAGE_END);
		BtnjPanel.setLayout(null);
		BtnjPanel.add(btnAjouter);
		BtnjPanel.add(btnSupprimer);
		BtnjPanel.add(btnQuitter);

		// jPanel2.setBorder(new LineBorder(Color.BLUE, 2, true));
		// FormJPanel.setPreferredSize(new Dimension(360, 311));

		jLabel1.setText("Code");

		jLabel2.setText("Désignation");

		jLabel3.setText("Description");

		tb_code.setEditable(false);
		tb_code.setEnabled(false);

		tb_description = new JTextArea();
		tb_description.setBounds(96, 76, 168, 113);
		tb_description.setBackground(SystemColor.window);

		jLabel2_1 = new JLabel();
		jLabel2_1.setBounds(86, 46, 17, 18);
		jLabel2_1.setForeground(Color.RED);
		jLabel2_1.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		jLabel2_1.setText("*");

		getContentPane().add(FormJPanel, java.awt.BorderLayout.LINE_START);
		FormJPanel.setLayout(null);
		FormJPanel.add(jLabel1);
		FormJPanel.add(jLabel2);
		FormJPanel.add(jLabel2_1);
		FormJPanel.add(jLabel3);
		FormJPanel.add(tb_description);
		FormJPanel.add(tb_code);
		FormJPanel.add(tb_libelle);
		TablePanel.setLayout(null);
		groupeTablejScrollPane = new javax.swing.JScrollPane();
		groupeTablejScrollPane.setBounds(0, 0, 449, 259);

		table = new javax.swing.JTable() {
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);
				if (!isRowSelected(row)) {
					c.setBackground(row % 2 == 0 ? UIManager.getColor("ToolTip.background") : Color.white);
				}
				if (isRowSelected(row) && isColumnSelected(column))
					((JComponent) c).setBorder(new LineBorder(Color.red));
				return c;
			}
		};
		table.setForeground(SystemColor.inactiveCaptionText);
		table.setFont(new Font("Dialog", Font.PLAIN, 11));
		table.setGridColor(SystemColor.lightGray);

		table.setBackground(Color.WHITE);

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		groupeTablejScrollPane.setViewportView(table);
		TablePanel.add(groupeTablejScrollPane);

		jPanel4.setPreferredSize(new java.awt.Dimension(529, 40));

		jLabel9.setText(ConstMessagesEN.Labels.RECHERCHER);

		javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
		jPanel4.setLayout(jPanel4Layout);
		jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel4Layout.createSequentialGroup().addContainerGap().addComponent(jLabel9)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(tb_search, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
						.addContainerGap()));
		jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel4Layout.createSequentialGroup().addContainerGap()
						.addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel9).addComponent(tb_search, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		TablePanel.add(jPanel4);

		getContentPane().add(TablePanel, BorderLayout.CENTER);

		pack();
	}

	public AppMenu getMenuFromForm() {
		AppMenu appMenu = new AppMenu();
		appMenu.setMenuName(tb_libelle.getText().trim());
		appMenu.setMenuDescription(tb_description.getText().trim());
		if (!tb_code.getText().trim().equalsIgnoreCase(Strings.EMPTY))
			appMenu.setId(Long.parseLong(tb_code.getText().trim()));
		return appMenu;
	}

	public void clearForm() {
		tb_libelle.setText(Strings.EMPTY);
		tb_code.setText(Strings.EMPTY);
		tb_description.setText(Strings.EMPTY);
	}

	public void loadSelectedRow(MenuTableModel tableModel) {

		if (tableModel.getRowCount() == 0)
			return;
		int i = table.getSelectedRow();

		tb_code.setText(table.getValueAt(i, 0).toString());
		tb_libelle.setText(table.getValueAt(i, 1).toString());
		tb_description.setText(table.getValueAt(i, 2).toString());

		table.getSelectionModel().addSelectionInterval(i, i);

	}

}
