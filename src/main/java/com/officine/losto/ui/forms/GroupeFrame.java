package com.officine.losto.ui.forms;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import org.apache.logging.log4j.util.Strings;

import com.officine.losto.backend.entity.AppGroupe;
import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.params.constant.ConstMessagesEN.Params;
import com.officine.losto.ui.forms.model.GroupeTableModel;
import lombok.Getter;

@org.springframework.stereotype.Component
@Getter
public class GroupeFrame extends JDialog {

	private static final long serialVersionUID = 1L;
	private javax.swing.JButton btnAjouter;
	private javax.swing.JButton btnSupprimer;
	private javax.swing.JButton btnFermer;
	private JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel9;
	private javax.swing.JPanel groupeBtnjPanel;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel4;
	private JScrollPane groupeTablejScrollPane;
	private JTextField tb_code_groupe;
	private JTextField tb_libelle;
	private JTextField tb_search;
	private JPanel TablePanel;
	private JTable table;
	private JTextArea tb_description;
	private JLabel jLabel2_1;
	private JLabel jLabel2_3;
	private JLabel lbl_IdGroupe;
	
	private JTableHeader tableHeader; 

	public GroupeFrame() {}

	public GroupeFrame(String title, GroupeTableModel tableModel) {

		setTitle(title);
		setResizable(true);
		initComponents();
		this.getTable().setModel(tableModel);
		
		this.getTable().getColumnModel().getColumn(0).setPreferredWidth(20);
		tableHeader = this.getTable().getTableHeader();
		tableHeader.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, ConstMessagesEN.Labels.POLICE_SIZE));
		tableHeader.setBackground(ConstMessagesEN.Labels.HEADER_COLOR_ALl);

	}

	private void initComponents() {
		setUndecorated(false);
		groupeBtnjPanel = new javax.swing.JPanel();
		groupeBtnjPanel.setBorder(null);
		groupeBtnjPanel.setBackground(Color.WHITE);

		jPanel2 = new javax.swing.JPanel();
		// jPanel2.setBorder(BorderFactory.createTitledBorder("Jpanel1"));
		jPanel2.setBackground(Color.WHITE);
		jPanel2.setBorder(new LineBorder(SystemColor.activeCaption));
		TablePanel = new javax.swing.JPanel();
		TablePanel.setBackground(Color.WHITE);
		// groupeTablePanel.setBorder(null);

		btnAjouter = new javax.swing.JButton();
		btnAjouter.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, 10));
		btnSupprimer = new javax.swing.JButton();
		btnSupprimer.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, 10));
		btnFermer = new javax.swing.JButton();
		btnFermer.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, 10));

		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();

		tb_code_groupe = new javax.swing.JTextField();
		tb_code_groupe.setBackground(SystemColor.window);

		tb_libelle = new javax.swing.JTextField();
		tb_libelle.setBackground(SystemColor.window);

		// jPanel3.setBorder(new LineBorder(Color.GREEN, 2));
		jPanel4 = new javax.swing.JPanel();
		jPanel4.setBounds(0, 260, 449, 40);
		jLabel9 = new javax.swing.JLabel();
		tb_search = new javax.swing.JTextField();
		groupeTablejScrollPane = new javax.swing.JScrollPane();
		groupeTablejScrollPane.setBounds(0, 0, 449, 259);
		table = new javax.swing.JTable() {

			private static final long serialVersionUID = 1L;

			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);

				if (!isRowSelected(row)) {

					c.setBackground(row % 2 == 0 ? UIManager.getColor("ToolTip.background") : Color.white);
					//new Color(220, 240, 255)
				}

				if (isRowSelected(row) && isColumnSelected(column))
					((JComponent) c).setBorder(new LineBorder(Color.red));

				return c;
			}

		};
		table.setForeground(SystemColor.inactiveCaptionText);
		table.setFont(new Font("Dialog", Font.PLAIN, 11));
		table.setGridColor(SystemColor.lightGray);

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		groupeBtnjPanel.setPreferredSize(new Dimension(720, 40));

		btnAjouter.setText(ConstMessagesEN.Labels.ENREGISTRER_BTN);
		btnAjouter.setIcon(new ImageIcon(ClassLoader.getSystemResource(Params.BASE_PATH + "images/Apply.png")));

		btnSupprimer.setText(ConstMessagesEN.Labels.SUPPRIMER_BTN);
		btnSupprimer
				.setIcon(new ImageIcon(ClassLoader.getSystemResource(Params.BASE_PATH + "images/Delete.png")));

		btnFermer.setText(ConstMessagesEN.Labels.QUITTER_BTN);
		btnFermer.setIcon(new ImageIcon(ClassLoader.getSystemResource(Params.BASE_PATH + "images/Cancel.png")));

		javax.swing.GroupLayout gl_groupeBtnjPanel = new javax.swing.GroupLayout(groupeBtnjPanel);
		gl_groupeBtnjPanel.setHorizontalGroup(gl_groupeBtnjPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_groupeBtnjPanel.createSequentialGroup().addContainerGap()
						.addComponent(btnAjouter, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(btnSupprimer, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED, 541, Short.MAX_VALUE)
						.addComponent(btnFermer, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));
		gl_groupeBtnjPanel
				.setVerticalGroup(gl_groupeBtnjPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_groupeBtnjPanel.createSequentialGroup().addContainerGap(15, Short.MAX_VALUE)
								.addGroup(gl_groupeBtnjPanel.createParallelGroup(Alignment.BASELINE)
										.addComponent(btnAjouter).addComponent(btnSupprimer).addComponent(btnFermer))
								.addContainerGap()));
		groupeBtnjPanel.setLayout(gl_groupeBtnjPanel);

		getContentPane().add(groupeBtnjPanel, java.awt.BorderLayout.PAGE_END);

		// jPanel2.setBorder(new LineBorder(Color.BLUE, 2, true));
		jPanel2.setPreferredSize(new Dimension(270, 300));

		jLabel1.setText("Code Du Groupe");

		jLabel2.setText("Désignation");

		jLabel3.setText("Description");

		tb_description = new JTextArea();
		tb_description.setWrapStyleWord(true);
		tb_description.setLineWrap(true);
		tb_description.setBackground(SystemColor.window);

		jLabel2_1 = new JLabel();
		jLabel2_1.setForeground(Color.RED);
		jLabel2_1.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		jLabel2_1.setText("*");

		jLabel2_3 = new JLabel();
		jLabel2_3.setText("*");
		jLabel2_3.setForeground(Color.RED);
		jLabel2_3.setFont(new Font("Lucida Grande", Font.BOLD, 13));

		lbl_IdGroupe = new JLabel();
		lbl_IdGroupe.setBackground(SystemColor.window);

		javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
		jPanel2Layout.setHorizontalGroup(
			jPanel2Layout.createParallelGroup(Alignment.LEADING)
				.addGroup(jPanel2Layout.createSequentialGroup()
					.addContainerGap()
					.addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING)
						.addGroup(jPanel2Layout.createSequentialGroup()
							.addComponent(jLabel1)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(jLabel2_3, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tb_code_groupe))
						.addGroup(jPanel2Layout.createSequentialGroup()
							.addComponent(jLabel2)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(jLabel2_1, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tb_libelle))
						.addGroup(Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
							.addComponent(jLabel3, GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(tb_description, GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE)
							.addGap(8)))
					.addGap(43)
					.addComponent(lbl_IdGroupe, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addContainerGap())
		);
		jPanel2Layout.setVerticalGroup(
			jPanel2Layout.createParallelGroup(Alignment.LEADING)
				.addGroup(jPanel2Layout.createSequentialGroup()
					.addGroup(jPanel2Layout.createParallelGroup(Alignment.LEADING)
						.addGroup(jPanel2Layout.createSequentialGroup()
							.addContainerGap()
							.addGroup(jPanel2Layout.createParallelGroup(Alignment.BASELINE)
								.addComponent(jLabel1)
								.addComponent(jLabel2_3)
								.addComponent(tb_code_groupe, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addGroup(jPanel2Layout.createSequentialGroup()
							.addGap(17)
							.addComponent(lbl_IdGroupe)))
					.addGap(14)
					.addGroup(jPanel2Layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(jLabel2)
						.addComponent(jLabel2_1)
						.addComponent(tb_libelle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(28)
					.addGroup(jPanel2Layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(jLabel3)
						.addComponent(tb_description, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(68, Short.MAX_VALUE))
		);
		jPanel2.setLayout(jPanel2Layout);

		getContentPane().add(jPanel2, java.awt.BorderLayout.LINE_START);
		TablePanel.setLayout(null);

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

		groupeTablejScrollPane.setViewportView(table);
		TablePanel.add(groupeTablejScrollPane);

		getContentPane().add(TablePanel, java.awt.BorderLayout.CENTER);

		pack();
	}

	public AppGroupe getGroupeFromForm() {
		AppGroupe appGroupe = new AppGroupe();
		appGroupe.setGroupeCode(tb_code_groupe.getText().trim().toString());
		appGroupe.setGroupeName(tb_libelle.getText().trim().toString());
		appGroupe.setGroupeDescription(tb_description.getText().trim());

		if (!lbl_IdGroupe.getText().trim().equalsIgnoreCase(Strings.EMPTY))
			appGroupe.setId(Long.parseLong(lbl_IdGroupe.getText().trim()));
		return appGroupe;
	}

	public void clearForm() {
		tb_libelle.setText(Strings.EMPTY);
		tb_code_groupe.setText(Strings.EMPTY);
		tb_description.setText(Strings.EMPTY);
		lbl_IdGroupe.setText(Strings.EMPTY);
	}

	public void loadSelectedRow(GroupeTableModel tableModel) {

		if (tableModel.getRowCount() == 0)
			return;
		int i = table.getSelectedRow();

		tb_code_groupe.setText(table.getValueAt(i, 0).toString());
		tb_libelle.setText(table.getValueAt(i, 1).toString());
		tb_description.setText(table.getValueAt(i, 2).toString());
		lbl_IdGroupe.setText(table.getValueAt(i, 2).toString());

		table.getSelectionModel().addSelectionInterval(i, i);

	}

}
