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
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import org.apache.logging.log4j.util.Strings;

import com.officine.losto.backend.entity.Typpe;
import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.params.constant.ConstMessagesEN.Params;
import com.officine.losto.ui.forms.model.CommandeTableModel;
import com.officine.losto.ui.forms.model.TypeTableModel;

import lombok.Getter;

@org.springframework.stereotype.Component
@Getter

public class TypeFrame extends JDialog {

	private static final long serialVersionUID = 1L;

	private javax.swing.JButton btnAjouter;
	private javax.swing.JButton btnSupprimer;

	private JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel9;
	private javax.swing.JPanel BtnjPanel;
	private javax.swing.JPanel FormJPanel;
	private javax.swing.JPanel jPanel4;

	private JScrollPane tablejScrollPane;
	private JTextField tb_code;
	private JTextField tb_libelle;
	private JTextField tb_search;

	
	
	private JPanel TablePanel;
	private JTable table; 
	private JTextArea tb_description;
	private JLabel jLabel2_1;
	private JLabel jLabel2_3;
	
	private JTableHeader tableHeader; 
	
	//public TypeFrame() {}

	public TypeFrame(TypeTableModel tableModel) {
		initComponents();
		//Shared.setFrameUp(MainMenuFrame.ctrl.typeFrame, this);
		this.getTable().setModel(tableModel);
		
		
		this.getTable().getColumnModel().getColumn(0).setPreferredWidth(20);
		tableHeader = this.getTable().getTableHeader();
		tableHeader.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, ConstMessagesEN.Labels.POLICE_SIZE));
		tableHeader.setBackground(Color.WHITE);
		tableHeader.setPreferredSize(new Dimension(tableHeader.getWidth(), 30));
		this.getTable().setRowHeight(20);
		
		
	}

	private void initComponents() {
		//setUndecorated(true);
		setUndecorated(false);
		setTitle( ConstMessagesEN.DialogTitles.TYPE_MODAL);
		setResizable(true);
		
		BtnjPanel = new javax.swing.JPanel();
		BtnjPanel.setBorder(null);
		BtnjPanel.setBackground(Color.WHITE);
		// jPanel1.setBorder(BorderFactory.createTitledBorder("Jpanel1"));
		FormJPanel = new javax.swing.JPanel();
		FormJPanel.setBackground(Color.WHITE);
		FormJPanel.setBorder(new LineBorder(SystemColor.activeCaption));
		TablePanel = new javax.swing.JPanel();
		TablePanel.setBackground(Color.WHITE);
		//groupeTablePanel.setBorder(null);
		
		

		btnAjouter = new javax.swing.JButton();
		btnAjouter.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, 10));
		btnSupprimer = new javax.swing.JButton();
		btnSupprimer.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, 10));
		
		//btnQuitter.setBackground(Color.red);
		//btnQuitter.setOpaque(true);
		//btnQuitter.setBorderPainted(false);

		jLabel1 = new javax.swing.JLabel();  
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();

		tb_code = new javax.swing.JTextField();
		tb_code.setBackground(SystemColor.window);

		tb_libelle = new javax.swing.JTextField();
		tb_libelle.setBackground(SystemColor.window);

		// jPanel3.setBorder(new LineBorder(Color.GREEN, 2));
		jPanel4 = new javax.swing.JPanel();
		jPanel4.setBackground(Color.WHITE);
		jPanel4.setBounds(0, 260, 449, 40);
		jLabel9 = new javax.swing.JLabel();
		tb_search = new javax.swing.JTextField();
		tablejScrollPane = new javax.swing.JScrollPane();
		tablejScrollPane.setBounds(0, 0, 449, 259);
		table = new javax.swing.JTable() {

			public Class getColumnClass(int column) {
				return getValueAt(0, column).getClass();

			}

			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);

				if (!isRowSelected(row)) {

					//c.setBackground(row % 2 == 0 ? UIManager.getColor("ToolTip.background") : Color.white);
					c.setBackground(row % 2 == 0 ? Color.decode("#f1f2dc") : Color.white);
				}

				if (isRowSelected(row) && isColumnSelected(column))
					((JComponent) c).setBorder(new LineBorder(Color.red));

				return c;
			}

		};
		table.setForeground(SystemColor.inactiveCaptionText);
		table.setFont(new Font("Dialog", Font.PLAIN, 11));
		table.setGridColor(SystemColor.lightGray);
		
		
		//table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		BtnjPanel.setPreferredSize(new Dimension(720, 40));

		btnAjouter.setText(ConstMessagesEN.Labels.ENREGISTRER_BTN);
		btnAjouter.setIcon(new ImageIcon(ClassLoader.getSystemResource(Params.BASE_PATH + "images/Apply.png")));

		btnSupprimer.setText(ConstMessagesEN.Labels.SUPPRIMER_BTN);
		btnSupprimer
				.setIcon(new ImageIcon(ClassLoader.getSystemResource(Params.BASE_PATH + "images/Delete.png")));

		javax.swing.GroupLayout gl_BtnjPanel = new javax.swing.GroupLayout(BtnjPanel);
		gl_BtnjPanel.setHorizontalGroup(
			gl_BtnjPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_BtnjPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnAjouter, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnSupprimer, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(488, Short.MAX_VALUE))
		);
		gl_BtnjPanel.setVerticalGroup(
			gl_BtnjPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_BtnjPanel.createSequentialGroup()
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_BtnjPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnAjouter)
						.addComponent(btnSupprimer))
					.addContainerGap())
		);
		BtnjPanel.setLayout(gl_BtnjPanel);

		getContentPane().add(BtnjPanel, java.awt.BorderLayout.PAGE_END);

		// jPanel2.setBorder(new LineBorder(Color.BLUE, 2, true));
		FormJPanel.setPreferredSize(new Dimension(270, 300));

		jLabel1.setText("Code Type");

		jLabel2.setText("Désignation");

		jLabel3.setText("Description");

		tb_code.setEditable(false);
		tb_code.setEnabled(false);
		
		tb_description = new JTextArea();
		tb_description.setBackground(SystemColor.window);
		
		jLabel2_1 = new JLabel();
		jLabel2_1.setForeground(Color.RED);
		jLabel2_1.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		jLabel2_1.setText("*");
		
		jLabel2_3 = new JLabel();
		jLabel2_3.setText("*");
		jLabel2_3.setForeground(Color.RED);
		jLabel2_3.setFont(new Font("Lucida Grande", Font.BOLD, 13));

		javax.swing.GroupLayout gl_FormJPanel = new javax.swing.GroupLayout(FormJPanel);
		gl_FormJPanel.setHorizontalGroup(
			gl_FormJPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_FormJPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_FormJPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_FormJPanel.createSequentialGroup()
							.addComponent(jLabel3)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_FormJPanel.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_FormJPanel.createSequentialGroup()
									.addComponent(tb_code, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)
									.addGap(181))
								.addGroup(gl_FormJPanel.createSequentialGroup()
									.addComponent(tb_description, GroupLayout.PREFERRED_SIZE, 169, GroupLayout.PREFERRED_SIZE)
									.addGap(157))))
						.addGroup(gl_FormJPanel.createSequentialGroup()
							.addComponent(jLabel1)
							.addPreferredGap(ComponentPlacement.RELATED, 313, Short.MAX_VALUE)
							.addComponent(jLabel2_3, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
							.addGap(15))
						.addGroup(gl_FormJPanel.createSequentialGroup()
							.addComponent(jLabel2)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(jLabel2_1, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
							.addGap(2)
							.addComponent(tb_libelle, GroupLayout.PREFERRED_SIZE, 153, GroupLayout.PREFERRED_SIZE)))
					.addGap(149))
		);
		gl_FormJPanel.setVerticalGroup(
			gl_FormJPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_FormJPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_FormJPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(jLabel1)
						.addComponent(tb_code, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(jLabel2_3))
					.addGap(14)
					.addGroup(gl_FormJPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(jLabel2)
						.addComponent(tb_libelle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(jLabel2_1))
					.addGap(28)
					.addGroup(gl_FormJPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(jLabel3)
						.addComponent(tb_description, GroupLayout.PREFERRED_SIZE, 113, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(85, Short.MAX_VALUE))
		);
		FormJPanel.setLayout(gl_FormJPanel);

		getContentPane().add(FormJPanel, java.awt.BorderLayout.LINE_START);
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

		
		
		tablejScrollPane.setViewportView(table);
		TablePanel.add(tablejScrollPane);

		getContentPane().add(TablePanel, java.awt.BorderLayout.CENTER);

		pack();
	}

	public Typpe getTypeFromForm() {
		Typpe type = new Typpe(); 
		type.setTyppeName(tb_libelle.getText().trim());
		type.setTyppeDescription(tb_description.getText().trim());
		if (! tb_code.getText().trim().equalsIgnoreCase(Strings.EMPTY))
			type.setId(Long.parseLong(tb_code.getText().trim()));	
		    //System.out.println("===========" + Type.getId() + "==============");
		return type; 
	}
	
	public void clearForm() {
		tb_libelle.setText(Strings.EMPTY);
		tb_code.setText(Strings.EMPTY);
		tb_description.setText(Strings.EMPTY);
	}
	
	public void loadSelectedRow(TypeTableModel tableModel) {
		
		if (tableModel.getRowCount() == 0)
			return;
		int i  =  table.getSelectedRow(); 
		
		tb_code.setText(table.getValueAt(i, 0).toString());
		tb_libelle.setText(table.getValueAt(i, 1).toString());
		tb_description.setText(table.getValueAt(i, 2).toString());

		table.getSelectionModel().addSelectionInterval(i, i);

	}

}
