package com.officine.losto.ui.forms;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import org.apache.logging.log4j.util.Strings;

import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.params.constant.ConstMessagesEN.Params;
import com.officine.losto.ui.forms.model.CommandeTableModel;
import com.officine.losto.ui.forms.model.ReceptionLigneTableModel;
import com.officine.losto.ui.shared.lookAndFeel.LookAndFeel;
import com.toedter.calendar.JDateChooser;

import lombok.Getter;

@org.springframework.stereotype.Component
@Getter
public class ReceptionFrame extends JDialog {

	private static final long serialVersionUID = 1L;

	private javax.swing.JButton btnValiderStocker;
	private javax.swing.JButton btnAnuler;
	private javax.swing.JButton btnQuitter;
	private JLabel jLabel1;
	private javax.swing.JLabel jLabel9;
	private javax.swing.JPanel BtnjPanel;
	private javax.swing.JPanel FormJPanel;
	private javax.swing.JPanel jPanel4;

	private JTextField tb_search;
	private JPanel TablePanel;
	private JTextField tb_numCommande;
	private JLabel lblForme;
	private JLabel lblType;
	private JTextField tb_Recptionner_par;
	private JScrollPane commandetablejScrollPane;
	private JScrollPane ligneCommandetablejScrollPane;
	private JTable tableLigneCommande;
	private JTable tableCommande;
	private JTextField tb_fournisseur;
	private JButton btnGenererBonDeReception;
	private JButton btnSignalerUneAnnomalie;
	private JTextField tb_idCommande;

	private JTableHeader tableHeader;
	private JTableHeader tableLigneHeader; 

	// public CommandeFrame() {}

	public ReceptionFrame(CommandeTableModel commandeTableModel, ReceptionLigneTableModel receptionLigneTableModel) {
		setModal(true);
		initComponents();
		this.getTableCommande().setModel(commandeTableModel);
		this.getTableLigneCommande().setModel(receptionLigneTableModel);

		initTablelookAndFeel();
	}

	public void initTablelookAndFeel() {

		
		
		
		this.getTableCommande().getColumnModel().getColumn(0).setPreferredWidth(20);
		tableHeader = this.getTableCommande().getTableHeader();
		tableHeader.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, ConstMessagesEN.Labels.POLICE_SIZE));
		tableHeader.setBackground(Color.WHITE);
		tableHeader.setPreferredSize(new Dimension(tableHeader.getWidth(), 25));
		this.getTableCommande().setRowHeight(20);
	
		
		this.getTableCommande().getColumnModel().getColumn(1).setPreferredWidth(150);
		this.getTableLigneCommande().getColumnModel().getColumn(0).setPreferredWidth(20);
		this.getTableLigneCommande().getColumnModel().getColumn(1).setPreferredWidth(150);
		this.getTableLigneCommande().getColumnModel().getColumn(2).setPreferredWidth(110);
		this.getTableLigneCommande().getColumnModel().getColumn(3).setPreferredWidth(80);
		this.getTableLigneCommande().getColumnModel().getColumn(6).setPreferredWidth(150);
		
		
		tableLigneHeader = this.getTableLigneCommande().getTableHeader();
		tableLigneHeader.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, ConstMessagesEN.Labels.POLICE_SIZE));
		tableLigneHeader.setBackground(Color.WHITE);
		tableLigneHeader.setPreferredSize(new Dimension(tableHeader.getWidth(), 25));
		this.getTableLigneCommande().setRowHeight(20);
		
		
		//
		this.getTableLigneCommande().getColumnModel().getColumn(4).setCellRenderer(LookAndFeel.colorRenderer);

	}

	private void initComponents() {
		setResizable(false);
		getContentPane().setPreferredSize(new Dimension(1200, 600));
		setTitle(ConstMessagesEN.DialogTitles.RECEPTION_MODAL);

		BtnjPanel = new javax.swing.JPanel();
		BtnjPanel.setBounds(0, 538, 1200, 62);
		BtnjPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		BtnjPanel.setBackground(Color.WHITE);

		FormJPanel = new javax.swing.JPanel();
		FormJPanel.setBorder(BorderFactory.createTitledBorder("Jpanel1"));
		FormJPanel.setBounds(0, 0, 273, 539);
		FormJPanel.setBackground(Color.WHITE);
		FormJPanel.setBorder(new LineBorder(UIManager.getColor("Desktop.background")));
		TablePanel = new javax.swing.JPanel();
		TablePanel.setBounds(275, 0, 925, 539);

		btnValiderStocker = new javax.swing.JButton();
		btnValiderStocker.setFont(new Font("Trebuchet MS", Font.BOLD, 10));
		btnAnuler = new javax.swing.JButton();

		btnAnuler.setFont(new Font("Trebuchet MS", Font.BOLD, 10));
		btnQuitter = new javax.swing.JButton();
		btnQuitter.setFont(new Font("Trebuchet MS", Font.BOLD, 10));

		jLabel1 = new javax.swing.JLabel();
		jLabel1.setBackground(Color.DARK_GRAY);
		jLabel1.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		jLabel1.setForeground(Color.DARK_GRAY);
		jLabel1.setBounds(16, 23, 80, 16);

		// jPanel3.setBorder(new LineBorder(Color.GREEN, 2));
		jPanel4 = new javax.swing.JPanel();
		jPanel4.setBounds(0, 223, 919, 40);
		jLabel9 = new javax.swing.JLabel();
		tb_search = new javax.swing.JTextField();
		ligneCommandetablejScrollPane = new javax.swing.JScrollPane();
		ligneCommandetablejScrollPane.setBounds(0, 263, 919, 276);

		commandetablejScrollPane = new javax.swing.JScrollPane();
		commandetablejScrollPane.setBounds(0, 0, 925, 224);

		tableLigneCommande = new javax.swing.JTable() {
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);

				if (!isRowSelected(row)) {

					// c.setBackground(row % 2 == 0 ? UIManager.getColor("ToolTip.background") :
					// Color.white);
					// c.setBackground(row % 2 == 0 ? new Color(230, 240, 255) : Color.white);
				} else {
					// c.setBackground(new Color(184, 207, 229));

					c.setBackground(new Color(255, 255, 255)); 
					c.setForeground(new Color(0, 0, 0));			}
				return c;
			}

		};
		// Permettre l'édition des cellules
		tableLigneCommande.setSurrendersFocusOnKeystroke(true);
		tableLigneCommande.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
		tableLigneCommande.setRowHeight(20);

		// Police personnalisée
		// ClassLoader.getSystemResource(Params.BASE_PATH +
		// "images/Roboto-Regular.ttf")
		URL url = ClassLoader.getSystemResource(Params.BASE_PATH + "fonts/SegoeUI.ttf");

		try {
			// Font roboto = Font.createFont(Font.TRUETYPE_FONT, new
			// File(url.getFile())).deriveFont(Font.BOLD, 12f);
			// Font roboto = new Font("Segoe UI", Font.PLAIN, 14);
			Font roboto = Font.createFont(Font.TRUETYPE_FONT, new File(url.getFile())).deriveFont(Font.BOLD, 14f);

			tableLigneCommande.setFont(roboto);
			tableLigneCommande.getTableHeader().setFont(roboto.deriveFont(Font.BOLD, 12f));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Design
		tableLigneCommande.setGridColor(new Color(220, 220, 220));
		tableLigneCommande.setShowGrid(true);
		tableLigneCommande.setSelectionBackground(new Color(200, 230, 255));
		tableLigneCommande.getTableHeader().setBackground(new Color(240, 240, 240));
		tableLigneCommande.getTableHeader().setReorderingAllowed(false);

		tableCommande = new javax.swing.JTable() {

			/*
			 * public Class getColumnClass(int column) { return getValueAt(0,
			 * column).getClass();
			 * 
			 * }
			 */

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

		tableCommande.setShowGrid(false);
		tableCommande.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		tableLigneCommande.setBackground(Color.WHITE);
		tableLigneCommande.setForeground(Color.DARK_GRAY);
		tableLigneCommande.setFont(new Font("Dialog", Font.PLAIN, 13));
		tableLigneCommande.setGridColor(SystemColor.lightGray);

		tableCommande.setBackground(Color.WHITE);
		tableCommande.setForeground(SystemColor.inactiveCaptionText);
		tableCommande.setFont(new Font("Dialog", Font.PLAIN, 13));
		tableCommande.setGridColor(SystemColor.lightGray);

		getContentPane().setLayout(null);

		BtnjPanel.setPreferredSize(new java.awt.Dimension(729, 50));
		btnValiderStocker.setText("Valider & Stocker");
		btnValiderStocker
				.setIcon(new ImageIcon(ClassLoader.getSystemResource(Params.BASE_PATH + "images/Apply.png")));

		btnAnuler.setText("Annuler Réception");
		btnAnuler.setIcon(new ImageIcon(ClassLoader.getSystemResource(Params.BASE_PATH + "images/Delete.png")));

		btnQuitter.setText("Fermer");
		btnQuitter.setIcon(new ImageIcon(ClassLoader.getSystemResource(Params.BASE_PATH + "images/Cancel.png")));

		btnGenererBonDeReception = new JButton();
		btnGenererBonDeReception.setText("Générer bon de livraison");
		btnGenererBonDeReception.setFont(new Font("Trebuchet MS", Font.BOLD, 10));

		btnSignalerUneAnnomalie = new JButton();
		btnSignalerUneAnnomalie.setText("Signaler une Annomalie");
		btnSignalerUneAnnomalie.setFont(new Font("Trebuchet MS", Font.BOLD, 10));

		javax.swing.GroupLayout gl_BtnjPanel = new javax.swing.GroupLayout(BtnjPanel);
		gl_BtnjPanel.setHorizontalGroup(gl_BtnjPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_BtnjPanel
				.createSequentialGroup().addContainerGap()
				.addComponent(btnValiderStocker, GroupLayout.PREFERRED_SIZE, 139, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addComponent(btnGenererBonDeReception, GroupLayout.PREFERRED_SIZE, 164, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(btnSignalerUneAnnomalie, GroupLayout.PREFERRED_SIZE, 138, GroupLayout.PREFERRED_SIZE)
				.addGap(18).addComponent(btnAnuler).addPreferredGap(ComponentPlacement.RELATED, 357, Short.MAX_VALUE)
				.addComponent(btnQuitter, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE).addGap(14)));
		gl_BtnjPanel.setVerticalGroup(gl_BtnjPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_BtnjPanel.createSequentialGroup().addGap(6)
						.addGroup(gl_BtnjPanel.createParallelGroup(Alignment.LEADING).addComponent(btnAnuler)
								.addComponent(btnValiderStocker).addComponent(btnSignalerUneAnnomalie)
								.addComponent(btnQuitter).addComponent(btnGenererBonDeReception))
						.addGap(17)));
		gl_BtnjPanel.linkSize(SwingConstants.VERTICAL,
				new Component[] { btnAnuler, btnQuitter, btnSignalerUneAnnomalie });
		BtnjPanel.setLayout(gl_BtnjPanel);

		getContentPane().add(BtnjPanel);

		// jPanel2.setBorder(new LineBorder(Color.BLUE, 2, true));
		FormJPanel.setPreferredSize(new Dimension(350, 530));

		jLabel1.setText("N°Commande");

		tb_numCommande = new JTextField();
		tb_numCommande.setFont(new Font("Lucida Grande", Font.PLAIN, 17));
		tb_numCommande.setForeground(new Color(0, 128, 0));
		tb_numCommande.setEditable(false);
		tb_numCommande.setBounds(16, 44, 251, 51);
		tb_numCommande.setBackground(SystemColor.window);
		tb_numCommande.setColumns(10);

		lblForme = new JLabel();
		lblForme.setBackground(Color.DARK_GRAY);
		lblForme.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		lblForme.setForeground(Color.DARK_GRAY);
		lblForme.setBounds(16, 124, 101, 16);
		lblForme.setText("Réceptionnée Par");

		lblType = new JLabel();
		lblType.setBackground(Color.DARK_GRAY);
		lblType.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		lblType.setForeground(Color.DARK_GRAY);
		lblType.setBounds(16, 206, 80, 16);
		lblType.setText("Fournisseur");

		getContentPane().add(FormJPanel);
		FormJPanel.setLayout(null);
		FormJPanel.add(jLabel1);
		FormJPanel.add(lblForme);
		FormJPanel.add(lblType);
		FormJPanel.add(tb_numCommande);

		tb_Recptionner_par = new JTextField();
		tb_Recptionner_par.setEditable(false);
		tb_Recptionner_par.setColumns(10);
		tb_Recptionner_par.setBackground(SystemColor.window);
		tb_Recptionner_par.setBounds(16, 143, 251, 27);
		FormJPanel.add(tb_Recptionner_par);

		tb_fournisseur = new JTextField();
		tb_fournisseur.setEditable(false);
		tb_fournisseur.setColumns(10);
		tb_fournisseur.setBackground(SystemColor.window);
		tb_fournisseur.setBounds(16, 222, 251, 39);
		FormJPanel.add(tb_fournisseur);

		tb_idCommande = new JTextField();
		tb_idCommande.setForeground(Color.WHITE);
		tb_idCommande.setEditable(false);
		tb_idCommande.setColumns(10);
		tb_idCommande.setBackground(Color.WHITE);
		tb_idCommande.setBounds(124, 23, 64, 16);
		FormJPanel.add(tb_idCommande);
		TablePanel.setLayout(null);

		jPanel4.setPreferredSize(new java.awt.Dimension(529, 40));

		jLabel9.setText(" Rechercher Commande(N°,date)");

		javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
		jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING)
				.addGroup(jPanel4Layout.createSequentialGroup().addContainerGap().addComponent(jLabel9)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(tb_search, GroupLayout.DEFAULT_SIZE, 626, Short.MAX_VALUE).addContainerGap()));
		jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING).addGroup(jPanel4Layout
				.createSequentialGroup().addContainerGap()
				.addGroup(jPanel4Layout.createParallelGroup(Alignment.BASELINE).addComponent(jLabel9).addComponent(
						tb_search, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addContainerGap(8, Short.MAX_VALUE)));
		jPanel4.setLayout(jPanel4Layout);

		TablePanel.add(jPanel4);

		// ligneCommandetablejScrollPane.setColumnHeaderView(tableLigneCommande);
		// commandetablejScrollPane.setColumnHeaderView(tableCommande);

		ligneCommandetablejScrollPane.setViewportView(tableLigneCommande);
		TablePanel.add(ligneCommandetablejScrollPane);

		commandetablejScrollPane.setViewportView(tableCommande);
		TablePanel.add(commandetablejScrollPane);

		getContentPane().add(TablePanel);

		pack();
	}

	/*@SuppressWarnings("deprecation")
	public Reception getReceptionFromForm() {

		Reception reception = new Reception();
		reception.setReceptionNumero(tb_numeroReception.getText());

		SimpleDateFormat sdf = new SimpleDateFormat(ConstMessagesEN.Params.DATE_FORMAT);
		String date = sdf.format(tb_date_Reception.getDate());
		reception.setReceptionDate(date);
		reception.setReceptionStatutExplication(tb_explication.getText().toString());

		// Commande

		// Statut

		// User

		if (!tb_idReception.getText().trim().equalsIgnoreCase(Strings.EMPTY))
			reception.setId(Long.parseLong(tb_idReception.getText().trim().toString()));

		return reception;
	} */

	public void clearForm() {
		tb_numCommande.setText(Strings.EMPTY);
		tb_fournisseur.setText(Strings.EMPTY);
		tb_Recptionner_par.setText(Strings.EMPTY);
	}

}
