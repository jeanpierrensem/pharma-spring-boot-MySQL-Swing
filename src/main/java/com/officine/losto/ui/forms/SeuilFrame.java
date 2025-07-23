package com.officine.losto.ui.forms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import org.apache.logging.log4j.util.Strings;

import com.officine.losto.backend.entity.Seuil;
import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.params.constant.ConstMessagesEN.Params;
import com.officine.losto.ui.forms.model.SeuilTableModel;
import com.toedter.components.JSpinField;

import lombok.Getter;

@org.springframework.stereotype.Component
@Getter
public class SeuilFrame extends JDialog {

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
	private JTextField tb_search;

	private JPanel TablePanel;
	private JTable table; 
	private JTextArea tb_commentaire;
	private JLabel jLabel2_1;
	private JLabel jLabel2_3;
	private JTextField lbl_id;
	private JSpinField tb_seuil;
	
	private JTableHeader tableHeader; 
	
	//public SeuilFrame() {}

	public SeuilFrame(SeuilTableModel tableModel) {
		
		initComponents();
		//Shared.setFrameUp(MainMenuFrame.ctrl.seuiFrame, this);
		this.getTable().setModel(tableModel);
		this.getTable().getColumnModel().getColumn(0).setPreferredWidth(20);
		tableHeader = this.getTable().getTableHeader();
		tableHeader.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, ConstMessagesEN.Labels.POLICE_SIZE));
		tableHeader.setBackground(Color.WHITE);
		tableHeader.setPreferredSize(new Dimension(tableHeader.getWidth(), 30));
		this.getTable().setRowHeight(20);
		
		
	}

	private void initComponents() {
		setUndecorated(false);
		setTitle( ConstMessagesEN.DialogTitles.SEUIL_MODAL);
		setResizable(true);
		//setBorder(new TitledBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "D\u00E9finition des Seuils", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(255, 255, 255)), "D\u00E9finition des seuils", TitledBorder.TRAILING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
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

		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();

		tb_code = new javax.swing.JTextField();
		tb_code.setBackground(SystemColor.window);

		// jPanel3.setBorder(new LineBorder(Color.GREEN, 2));
		jPanel4 = new javax.swing.JPanel();
		jPanel4.setBackground(Color.WHITE);
		jPanel4.setBounds(0, 260, 449, 40);
		jLabel9 = new javax.swing.JLabel();
		tb_search = new javax.swing.JTextField();
		tablejScrollPane = new javax.swing.JScrollPane();
		tablejScrollPane.setBounds(0, 0, 449, 259);
		table = new javax.swing.JTable() {

			/*public Class getColumnClass(int column) {
				return getValueAt(0, column).getClass();

			}*/

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

		jLabel1.setText("Code Seuil");

		jLabel2.setText("Valeur seuil");

		jLabel3.setText("Commentaire");
		
		tb_commentaire = new JTextArea();
		tb_commentaire.setBackground(SystemColor.window);
		
		jLabel2_1 = new JLabel();
		jLabel2_1.setForeground(Color.RED);
		jLabel2_1.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		jLabel2_1.setText("*");
		
		jLabel2_3 = new JLabel();
		jLabel2_3.setText("*");
		jLabel2_3.setForeground(Color.RED);
		jLabel2_3.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		
		tb_seuil = new JSpinField();
		tb_seuil.getSpinner().setBackground(SystemColor.window);
		
		lbl_id = new JTextField();
		lbl_id.setEnabled(false);
		lbl_id.setEditable(false);
		lbl_id.setBackground(SystemColor.window);
		lbl_id.setColumns(10);

		javax.swing.GroupLayout gl_FormJPanel = new javax.swing.GroupLayout(FormJPanel);
		gl_FormJPanel.setHorizontalGroup(
			gl_FormJPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_FormJPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_FormJPanel.createParallelGroup(Alignment.LEADING, false)
						.addComponent(jLabel3)
						.addGroup(gl_FormJPanel.createSequentialGroup()
							.addComponent(jLabel2)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(jLabel2_1, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_FormJPanel.createSequentialGroup()
							.addComponent(jLabel1)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(jLabel2_3, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
							.addGap(12)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_FormJPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_FormJPanel.createSequentialGroup()
							.addComponent(tb_code, GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
							.addGap(11)
							.addComponent(lbl_id, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_FormJPanel.createSequentialGroup()
							.addGap(6)
							.addComponent(tb_seuil, GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE))
						.addComponent(tb_commentaire, GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_FormJPanel.setVerticalGroup(
			gl_FormJPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_FormJPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_FormJPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(jLabel1)
						.addComponent(lbl_id, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(jLabel2_3)
						.addComponent(tb_code, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_FormJPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_FormJPanel.createParallelGroup(Alignment.BASELINE)
							.addComponent(jLabel2)
							.addComponent(jLabel2_1))
						.addComponent(tb_seuil, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(28)
					.addGroup(gl_FormJPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(jLabel3)
						.addComponent(tb_commentaire, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(99, Short.MAX_VALUE))
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

	public Seuil getSeuilFromForm() {
		Seuil seuil = new Seuil(); 
		 seuil.setSeuilCode(tb_code.getText().trim().toString());
		 seuil.setSeuilNiveau(tb_seuil.getValue());
		 seuil.setSeuilDescription(tb_commentaire.getText().trim().toString());
		 

		if (! lbl_id.getText().trim().equalsIgnoreCase(Strings.EMPTY))
			seuil.setId(Long.parseLong(lbl_id.getText().trim()));	
	
		return seuil; 
	}
	
	public void clearForm() {
		lbl_id.setText(Strings.EMPTY);
		
		tb_code.setText(Strings.EMPTY);
		tb_commentaire.setText(Strings.EMPTY);
	}
	
	public void loadSelectedRow(SeuilTableModel tableModel) {
		
		if (tableModel.getRowCount() == 0)
			return;
		int i  =  table.getSelectedRow(); 
		
		lbl_id.setText(table.getValueAt(i, 0).toString());
		tb_code.setText(table.getValueAt(i, 1).toString());
		tb_commentaire.setText(table.getValueAt(i, 3).toString());

		table.getSelectionModel().addSelectionInterval(i, i);

	}

}
