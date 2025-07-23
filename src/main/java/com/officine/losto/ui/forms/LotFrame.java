package com.officine.losto.ui.forms;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.text.SimpleDateFormat;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import org.apache.logging.log4j.util.Strings;

import com.officine.losto.backend.entity.Lot;
import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.params.constant.ConstMessagesEN.Params;
import com.officine.losto.ui.forms.model.LotTableModel;
import com.toedter.calendar.JDateChooser;

import lombok.Getter;

@org.springframework.stereotype.Component
@Getter
public class LotFrame extends JDialog {

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
	private JScrollPane tablejScrollPane;
	private JTextField tb_search;
	private JPanel TablePanel;
	private JTable table; 
	private JLabel jLabel2_1;
	private JLabel jLabel2_2;
	private JLabel jLabel2_3;
	private JDateChooser tb_date_peremption;
	private JSpinner tb_quantite_lot;
	private JTextField tb_numeroLot;
	private JTextField tb_id;
	private JLabel lblFourbnisseur;
	private JLabel jLabel2_4;
	private JComboBox<String> cb_Fournisseur;
	
	private JTableHeader tableHeader; 
	
	//public LotFrame() {}

	public LotFrame(LotTableModel tableModel) {
		
		 
		
		
		initComponents();
		this.getTable().setModel(tableModel);
		this.getTable().getColumnModel().getColumn(0).setPreferredWidth(20);
		tableHeader = this.getTable().getTableHeader();
		tableHeader.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, ConstMessagesEN.Labels.POLICE_SIZE));
		tableHeader.setBackground(Color.WHITE);
		tableHeader.setPreferredSize(new Dimension(tableHeader.getWidth(), 20));
		this.getTable().setRowHeight(20);
		
	
		
	}

	@SuppressWarnings("serial")
	private void initComponents() {

		setTitle( ConstMessagesEN.DialogTitles.LOT_MODAL);
		setResizable(true);
		setUndecorated(false);
		//setBorder(new TitledBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Lot / Enr\u00E9gistrement", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), "Lot / Enr\u00E9gistrement", TitledBorder.TRAILING, TitledBorder.TOP, null, Color.WHITE));
		
		BtnjPanel = new javax.swing.JPanel();
		BtnjPanel.setBorder(null);
		BtnjPanel.setBackground(Color.WHITE);
		// jPanel1.setBorder(BorderFactory.createTitledBorder("Jpanel1"));
		FormJPanel = new javax.swing.JPanel();
		FormJPanel.setBackground(Color.WHITE);
		FormJPanel.setBorder(new LineBorder(SystemColor.activeCaption));
		TablePanel = new javax.swing.JPanel();
		//groupeTablePanel.setBorder(null);
		

		btnAjouter = new javax.swing.JButton();
		btnAjouter.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, 10));
		btnSupprimer = new javax.swing.JButton();
		btnSupprimer.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, 10));
		btnQuitter = new javax.swing.JButton();
		btnQuitter.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, 10));

		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();

		// jPanel3.setBorder(new LineBorder(Color.GREEN, 2));
		jPanel4 = new javax.swing.JPanel();
		jPanel4.setBounds(0, 260, 529, 40);
		jLabel9 = new javax.swing.JLabel();
		tb_search = new javax.swing.JTextField();
		tablejScrollPane = new javax.swing.JScrollPane();
		tablejScrollPane.setBounds(0, 0, 449, 259);
		table = new javax.swing.JTable() {

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

		// jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		BtnjPanel.setPreferredSize(new Dimension(720, 40));

		btnAjouter.setText(ConstMessagesEN.Labels.ENREGISTRER_BTN);
		btnAjouter.setIcon(new ImageIcon(ClassLoader.getSystemResource(Params.BASE_PATH + "images/Apply.png")));

		btnSupprimer.setText(ConstMessagesEN.Labels.SUPPRIMER_BTN);
		btnSupprimer
				.setIcon(new ImageIcon(ClassLoader.getSystemResource(Params.BASE_PATH + "images/Delete.png")));

		btnQuitter.setText(ConstMessagesEN.Labels.QUITTER_BTN);
		btnQuitter.setIcon(new ImageIcon(ClassLoader.getSystemResource(Params.BASE_PATH + "images/Cancel.png")));

		javax.swing.GroupLayout gl_BtnjPanel = new javax.swing.GroupLayout(BtnjPanel);
		gl_BtnjPanel.setHorizontalGroup(
			gl_BtnjPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_BtnjPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnAjouter, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnSupprimer, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 533, Short.MAX_VALUE)
					.addComponent(btnQuitter, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
					.addGap(14))
		);
		gl_BtnjPanel.setVerticalGroup(
			gl_BtnjPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_BtnjPanel.createSequentialGroup()
					.addContainerGap(15, Short.MAX_VALUE)
					.addGroup(gl_BtnjPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnAjouter)
						.addComponent(btnSupprimer)
						.addComponent(btnQuitter))
					.addContainerGap())
		);
		BtnjPanel.setLayout(gl_BtnjPanel);

		getContentPane().add(BtnjPanel, java.awt.BorderLayout.PAGE_END);

		// jPanel2.setBorder(new LineBorder(Color.BLUE, 2, true));
		FormJPanel.setPreferredSize(new Dimension(270, 300));

		jLabel1.setText("N° lot");

		jLabel2.setText("Date Péremption");

		jLabel3.setText("Quantité");
		
		jLabel2_1 = new JLabel();
		jLabel2_1.setForeground(Color.RED);
		jLabel2_1.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		jLabel2_1.setText("*");
		
		jLabel2_2 = new JLabel();
		jLabel2_2.setText("*");
		jLabel2_2.setForeground(Color.RED);
		jLabel2_2.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		
		jLabel2_3 = new JLabel();
		jLabel2_3.setText("*");
		jLabel2_3.setForeground(Color.RED);
		jLabel2_3.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		
		tb_date_peremption = new JDateChooser();
		tb_date_peremption.setBackground(SystemColor.menu);
		tb_date_peremption.setDateFormatString(ConstMessagesEN.Params.DATE_FORMAT); 
		
		
		tb_quantite_lot = new JSpinner();
		tb_quantite_lot.setBackground(SystemColor.menu);
		
		tb_numeroLot = new JTextField();
		tb_numeroLot.setBackground(SystemColor.window);
		tb_numeroLot.setColumns(10);
		
		tb_id = new JTextField();
		tb_id.setBackground(SystemColor.window);
		tb_id.setEditable(false);
		tb_id.setColumns(10);
		
		lblFourbnisseur = new JLabel();
		lblFourbnisseur.setText("Fournisseur");
		
		jLabel2_4 = new JLabel();
		jLabel2_4.setText("*");
		jLabel2_4.setForeground(Color.RED);
		jLabel2_4.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		
		cb_Fournisseur = new JComboBox<String>();
		cb_Fournisseur.setForeground(Color.DARK_GRAY);
		cb_Fournisseur.setFont(new Font("Trebuchet MS", Font.PLAIN, 11));
		cb_Fournisseur.setBackground(SystemColor.window);

		javax.swing.GroupLayout gl_FormJPanel = new javax.swing.GroupLayout(FormJPanel);
		gl_FormJPanel.setHorizontalGroup(
			gl_FormJPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_FormJPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_FormJPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_FormJPanel.createSequentialGroup()
							.addComponent(jLabel1)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(jLabel2_3)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tb_numeroLot, GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tb_id, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_FormJPanel.createSequentialGroup()
							.addComponent(jLabel2)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(jLabel2_1)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tb_date_peremption, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(Alignment.LEADING, gl_FormJPanel.createSequentialGroup()
							.addComponent(jLabel3)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(jLabel2_2, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(tb_quantite_lot, GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE))
						.addGroup(gl_FormJPanel.createSequentialGroup()
							.addComponent(lblFourbnisseur, GroupLayout.PREFERRED_SIZE, 77, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(jLabel2_4, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(cb_Fournisseur, 0, 150, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_FormJPanel.setVerticalGroup(
			gl_FormJPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_FormJPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_FormJPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(jLabel1)
						.addComponent(jLabel2_3)
						.addComponent(tb_numeroLot, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(tb_id, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(28)
					.addGroup(gl_FormJPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_FormJPanel.createParallelGroup(Alignment.BASELINE)
							.addComponent(jLabel2)
							.addComponent(jLabel2_1))
						.addComponent(tb_date_peremption, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(25)
					.addGroup(gl_FormJPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(jLabel3)
						.addComponent(jLabel2_2)
						.addComponent(tb_quantite_lot, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(28)
					.addGroup(gl_FormJPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(cb_Fournisseur, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblFourbnisseur)
						.addComponent(jLabel2_4))
					.addContainerGap(106, Short.MAX_VALUE))
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

	public Lot getLotFromForm() {
		Lot lot = new Lot(); 
		
		 SimpleDateFormat sdf = new SimpleDateFormat(ConstMessagesEN.Params.DATE_FORMAT);
		 String date = sdf.format(tb_date_peremption.getDate());
		 lot.setDatePeremptionLot(date);    
		 lot.setNumeroLot(tb_numeroLot.getText().trim().toString()); 
		 lot.setQuantiteLot((int) tb_quantite_lot.getValue());
		if (! tb_id.getText().trim().equalsIgnoreCase(Strings.EMPTY))
			lot.setId(Long.parseLong(tb_id.getText().trim().toString()));	
		  
		return lot; 
	}
	
	public void clearForm() {
		//tb_date_peremption.getDate(Strings.EMPTY);
		tb_numeroLot.setText(Strings.EMPTY);
		tb_quantite_lot.setValue(0);
		tb_id.setText(Strings.EMPTY);
	}
	
	public void loadSelectedRow(LotTableModel tableModel) {
		
		if (tableModel.getRowCount() == 0)
			return;
		int i  =  table.getSelectedRow(); 
		
		tb_id.setText(table.getValueAt(i, 0).toString());
		tb_numeroLot.setText(table.getValueAt(i, 1).toString());
		//tb_quantite_lot.setValue(table.getValueAt(i, 2).toString());

		table.getSelectionModel().addSelectionInterval(i, i);

	}
}
