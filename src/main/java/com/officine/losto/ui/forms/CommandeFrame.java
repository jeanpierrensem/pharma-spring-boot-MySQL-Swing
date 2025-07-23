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
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import org.apache.logging.log4j.util.Strings;

import com.officine.losto.backend.entity.Commande;
import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.params.constant.ConstMessagesEN.Params;
import com.officine.losto.ui.forms.model.CommandeLigneTableModel;
import com.officine.losto.ui.forms.model.CommandeTableModel;
import com.officine.losto.uti.shared.Shared;
import com.toedter.calendar.JDateChooser;

import lombok.Getter;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

@org.springframework.stereotype.Component
@Getter
public class CommandeFrame extends JDialog {

	private static final long serialVersionUID = 1L;

	private javax.swing.JButton btnEnregistrer;
	private javax.swing.JButton btnSupprimer;
	private javax.swing.JButton btnQuitter;
	private JLabel jLabel1;
	private javax.swing.JLabel jLabel9;
	private javax.swing.JPanel BtnjPanel;
	private javax.swing.JPanel FormJPanel;
	private javax.swing.JPanel jPanel4;

	private JTextField tb_search;
	private JPanel TablePanel;

	private JLabel jLabel2_3;
	private JTextField tb_numero;
	private JTextField tb_id;
	private JLabel lblNomCommande;
	private JLabel jLabel2_4;
	private JLabel lblForme;
	private JLabel jLabel2_5;
	private JLabel lblType;
	private JLabel jLabel2_6;
	private JLabel lblDescription;
	private JTextArea tb_indication;
	private JComboBox<String> cb_fournisseur;
	private JTextField tb_reference;
	private JTextField tb_quantite;
	private JTextField tb_prix_unitaire;
	private JTextField tb_remise;
	private JTextField tb_prix_total;
	private JTextField tb_commander_par;
	private JDateChooser tb_date_commande;
	private JLabel lblModeDeLivraison;
	private JLabel jLabel2;
	private JComboBox<String> cb_mode_livraison;
	private JScrollPane commandetablejScrollPane;
	private JScrollPane ligneCommandetablejScrollPane;
	private JTable tableLigneCommande;
	private JTable tableCommande;
	private JButton btnAjouter;
	private JComboBox<String> cb_article;
	private JButton btnRetirer;
	
	private JTableHeader tableHeader; 
	private JTableHeader tableLigneHeader; 
	private JTextField tbStatut;
	private JLabel lblStatut;
	private JButton btnImprimer;

	// public CommandeFrame() {}

	public CommandeFrame(CommandeTableModel commandeTableModel, CommandeLigneTableModel commandeLigneTableModel) {
		setModal(false);
		

		initComponents();
		this.getTableCommande().setModel(commandeTableModel);
		this.getTableLigneCommande().setModel(commandeLigneTableModel);
		
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
		
		/*tableHeader = this.getTableLigneCommande().getTableHeader();
		tableHeader.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, ConstMessagesEN.Labels.POLICE_SIZE-2));
		tableHeader.setBackground(ConstMessagesEN.Labels.HEADER_COLOR_SUB_TABLE);*/
		
		
		
		
		

	}

	@SuppressWarnings("serial")
	private void initComponents() {
		setResizable(false);
		/*
		 * getContentPane().setPreferredSize(new
		 * Dimension(ConstMessagesEN.Params.DEFAULT_HEIGHT * 3,
		 * ConstMessagesEN.Params.DEFAULT_WIDTH - 100));
		 */

		getContentPane().setPreferredSize(new Dimension(1100, ConstMessagesEN.Params.DEFAULT_WIDTH - 180));

		setTitle(ConstMessagesEN.DialogTitles.COMMANDE_MODAL);

		// setBorder(new TitledBorder(new TitledBorder(new
		// EtchedBorder(EtchedBorder.LOWERED, null, null), "Gestion des Commandes",
		// TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), "Gestion
		// des Commandes", TitledBorder.TRAILING, TitledBorder.TOP, null, new Color(255,
		// 255, 255)));

		BtnjPanel = new javax.swing.JPanel();
		BtnjPanel.setBounds(0, 464, 1100, 56);
		BtnjPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		BtnjPanel.setBackground(Color.WHITE);
		// jPanel1.setBorder(BorderFactory.createTitledBorder("Jpanel1"));
		FormJPanel = new javax.swing.JPanel();
		FormJPanel.setBounds(0, 0, 350, 465);
		FormJPanel.setBackground(Color.WHITE);
		FormJPanel.setBorder(new LineBorder(UIManager.getColor("Desktop.background")));
		TablePanel = new javax.swing.JPanel();
		TablePanel.setBounds(350, 0, 750, 465);
		// groupeTablePanel.setBorder(null);

		btnEnregistrer = new javax.swing.JButton();
		btnEnregistrer.setFont(new Font("Trebuchet MS", Font.BOLD, 10));
		btnSupprimer = new javax.swing.JButton();
		btnSupprimer.setFont(new Font("Trebuchet MS", Font.BOLD, 10));
		btnQuitter = new javax.swing.JButton();
		btnQuitter.setFont(new Font("Trebuchet MS", Font.BOLD, 10));

		jLabel1 = new javax.swing.JLabel();
		jLabel1.setBackground(Color.DARK_GRAY);
		jLabel1.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		jLabel1.setForeground(Color.BLACK);
		jLabel1.setBounds(6, 6, 82, 16);

		// jPanel3.setBorder(new LineBorder(Color.GREEN, 2));
		jPanel4 = new javax.swing.JPanel();
		jPanel4.setBounds(0, 113, 750, 40);
		jLabel9 = new javax.swing.JLabel();
		tb_search = new javax.swing.JTextField();
		ligneCommandetablejScrollPane = new javax.swing.JScrollPane();
		ligneCommandetablejScrollPane.setBounds(0, 155, 750, 248);

		commandetablejScrollPane = new javax.swing.JScrollPane();
		commandetablejScrollPane.setBounds(0, 0, 750, 115);

		tableLigneCommande = new javax.swing.JTable() {
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);

				if (!isRowSelected(row)) {

					// c.setBackground(row % 2 == 0 ? UIManager.getColor("ToolTip.background") :
					// Color.white);

					c.setBackground(row % 2 == 0 ? Color.decode("#f1f2dc") : Color.white);
				}

				if (isRowSelected(row) && isColumnSelected(column))
					((JComponent) c).setBorder(new LineBorder(Color.red));

				return c;
			}

		};

		tableCommande = new javax.swing.JTable() {

			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);

				if (!isRowSelected(row)) {
					// c.setBackground(row % 2 == 0 ? UIManager.getColor("ToolTip.background") :
					// Color.white);
					c.setBackground(row % 2 == 0 ? Color.decode("#ccffcc") : Color.white);
				}
				if (isRowSelected(row) && isColumnSelected(column))
					((JComponent) c).setBorder(new LineBorder(Color.red));

				return c;
			}

		};
		tableCommande.setShowVerticalLines(false);
		tableCommande.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableCommande.setFillsViewportHeight(true);

		tableLigneCommande.setBackground(Color.WHITE);
		tableLigneCommande.setForeground(SystemColor.inactiveCaptionText);
		tableLigneCommande.setFont(new Font("Dialog", Font.PLAIN, 11));
		tableLigneCommande.setGridColor(SystemColor.lightGray);

		tableCommande.setBackground(Color.WHITE);
		tableCommande.setForeground(SystemColor.inactiveCaptionText);
		tableCommande.setFont(new Font("Dialog", Font.PLAIN, 11));
		tableCommande.setGridColor(SystemColor.lightGray);

		getContentPane().setLayout(null);

		BtnjPanel.setPreferredSize(new java.awt.Dimension(729, 50));
		btnEnregistrer.setText(ConstMessagesEN.Labels.ENREGISTRER_BTN);
		btnEnregistrer
				.setIcon(new ImageIcon(ClassLoader.getSystemResource(Params.BASE_PATH + "images/Apply.png")));

		btnSupprimer.setText(ConstMessagesEN.Labels.SUPPRIMER_BTN);
		btnSupprimer
				.setIcon(new ImageIcon(ClassLoader.getSystemResource(Params.BASE_PATH + "images/Delete.png")));

		btnQuitter.setText(ConstMessagesEN.Labels.QUITTER_BTN);
		btnQuitter.setIcon(new ImageIcon(ClassLoader.getSystemResource(Params.BASE_PATH + "images/Cancel.png")));
		
		btnImprimer = new JButton();
		
		btnImprimer.setText("Imprimer Bon de Commande");
		btnImprimer.setFont(new Font("Trebuchet MS", Font.BOLD, 10));

		javax.swing.GroupLayout gl_BtnjPanel = new javax.swing.GroupLayout(BtnjPanel);
		gl_BtnjPanel.setHorizontalGroup(
			gl_BtnjPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_BtnjPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnEnregistrer, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnSupprimer, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(btnImprimer, GroupLayout.PREFERRED_SIZE, 168, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 554, Short.MAX_VALUE)
					.addComponent(btnQuitter, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
					.addGap(14))
		);
		gl_BtnjPanel.setVerticalGroup(
			gl_BtnjPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_BtnjPanel.createSequentialGroup()
					.addContainerGap(17, Short.MAX_VALUE)
					.addGroup(gl_BtnjPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnEnregistrer)
						.addComponent(btnSupprimer)
						.addComponent(btnQuitter)
						.addComponent(btnImprimer))
					.addContainerGap())
		);
		BtnjPanel.setLayout(gl_BtnjPanel);

		getContentPane().add(BtnjPanel);

		// jPanel2.setBorder(new LineBorder(Color.BLUE, 2, true));
		FormJPanel.setPreferredSize(new Dimension(350, 530));

		jLabel1.setText("N°Commande");

		jLabel2_3 = new JLabel();
		jLabel2_3.setBounds(89, 6, 17, 16);
		jLabel2_3.setText("*");
		jLabel2_3.setForeground(Color.RED);
		jLabel2_3.setFont(new Font("Lucida Grande", Font.BOLD, 13));

		tb_numero = new JTextField();
		tb_numero.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		tb_numero.setEditable(false);
		tb_numero.setBounds(98, 1, 210, 27);

		tb_numero.setBackground(Color.decode("#ccffcc"));

		tb_numero.setColumns(10);

		tb_id = new JTextField();
		tb_id.setBounds(308, 1, 35, 27);
		tb_id.setBackground(Color.white);
		tb_id.setEditable(false);
		tb_id.setColumns(10);

		lblNomCommande = new JLabel();
		lblNomCommande.setBackground(Color.DARK_GRAY);
		lblNomCommande.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		lblNomCommande.setForeground(Color.BLACK);
		lblNomCommande.setBounds(6, 44, 51, 16);
		lblNomCommande.setText("Date");

		jLabel2_4 = new JLabel();
		jLabel2_4.setBounds(34, 44, 17, 16);
		jLabel2_4.setText("*");
		jLabel2_4.setForeground(Color.RED);
		jLabel2_4.setFont(new Font("Lucida Grande", Font.BOLD, 13));

		lblForme = new JLabel();
		lblForme.setBackground(Color.DARK_GRAY);
		lblForme.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		lblForme.setForeground(Color.BLACK);
		lblForme.setBounds(6, 81, 87, 16);
		lblForme.setText("Commandé Par");

		jLabel2_5 = new JLabel();
		jLabel2_5.setBounds(89, 81, 17, 16);
		jLabel2_5.setText("*");
		jLabel2_5.setForeground(Color.RED);
		jLabel2_5.setFont(new Font("Lucida Grande", Font.BOLD, 13));

		lblType = new JLabel();
		lblType.setBackground(Color.DARK_GRAY);
		lblType.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		lblType.setForeground(Color.BLACK);
		lblType.setBounds(6, 126, 80, 16);
		lblType.setText("Fournisseur");

		jLabel2_6 = new JLabel();
		jLabel2_6.setBounds(69, 126, 17, 16);
		jLabel2_6.setText("*");
		jLabel2_6.setForeground(Color.RED);
		jLabel2_6.setFont(new Font("Lucida Grande", Font.BOLD, 13));

		lblDescription = new JLabel();
		lblDescription.setBackground(Color.DARK_GRAY);
		lblDescription.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		lblDescription.setForeground(Color.BLACK);
		lblDescription.setBounds(6, 229, 82, 16);
		lblDescription.setText("Indications");

		tb_indication = new JTextArea();
		tb_indication.setWrapStyleWord(true);
		tb_indication.setLineWrap(true);
		tb_indication.setBounds(97, 229, 246, 165);
		tb_indication.setBackground(SystemColor.window);

		cb_fournisseur = new JComboBox<String>();
		cb_fournisseur.setBounds(98, 123, 205, 32);
		cb_fournisseur.setForeground(Color.DARK_GRAY);
		cb_fournisseur.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		cb_fournisseur.setBackground(SystemColor.window);

		getContentPane().add(FormJPanel);
		FormJPanel.setLayout(null);
		FormJPanel.add(lblNomCommande);
		FormJPanel.add(jLabel2_4);
		FormJPanel.add(jLabel1);
		FormJPanel.add(jLabel2_3);
		FormJPanel.add(lblDescription);
		FormJPanel.add(jLabel2_6);
		FormJPanel.add(lblForme);
		FormJPanel.add(jLabel2_5);
		FormJPanel.add(lblType);
		FormJPanel.add(cb_fournisseur);
		FormJPanel.add(tb_indication);
		FormJPanel.add(tb_numero);
		FormJPanel.add(tb_id);

		tb_date_commande = new JDateChooser();
		tb_date_commande.getCalendarButton().setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		tb_date_commande.getCalendarButton().setVerticalAlignment(SwingConstants.TOP);
		tb_date_commande.getCalendarButton().setHorizontalAlignment(SwingConstants.LEFT);
		tb_date_commande.getCalendarButton().setBackground(Color.ORANGE);
		tb_date_commande.setDateFormatString("dd/MM/yyyy");
		tb_date_commande.setBackground(SystemColor.window);
		tb_date_commande.setBounds(98, 44, 187, 26);
		FormJPanel.add(tb_date_commande);

		tb_commander_par = new JTextField();
		tb_commander_par.setEditable(false);
		tb_commander_par.setColumns(10);
		tb_commander_par.setBackground(SystemColor.window);
		tb_commander_par.setBounds(98, 81, 225, 27);
		FormJPanel.add(tb_commander_par);

		lblModeDeLivraison = new JLabel();
		lblModeDeLivraison.setText("Mode Livraison");
		lblModeDeLivraison.setForeground(Color.BLACK);
		lblModeDeLivraison.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		lblModeDeLivraison.setBackground(Color.DARK_GRAY);
		lblModeDeLivraison.setBounds(6, 165, 100, 16);
		FormJPanel.add(lblModeDeLivraison);

		jLabel2 = new JLabel();
		jLabel2.setText("*");
		jLabel2.setForeground(Color.RED);
		jLabel2.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		jLabel2.setBounds(89, 165, 17, 16);
		FormJPanel.add(jLabel2);

		cb_mode_livraison = new JComboBox<String>();
		cb_mode_livraison.setForeground(Color.DARK_GRAY);
		cb_mode_livraison.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
		cb_mode_livraison.setBackground(SystemColor.window);
		cb_mode_livraison.setBounds(98, 156, 205, 27);
		FormJPanel.add(cb_mode_livraison);
		
		tbStatut = new JTextField();
		tbStatut.setEditable(false);
		tbStatut.setBounds(98, 193, 245, 26);
		FormJPanel.add(tbStatut);
		
		lblStatut = new JLabel();
		lblStatut.setText("Statut");
		lblStatut.setForeground(Color.BLACK);
		lblStatut.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		lblStatut.setBackground(Color.DARK_GRAY);
		lblStatut.setBounds(6, 193, 82, 24);
		FormJPanel.add(lblStatut);
		TablePanel.setLayout(null);

		jPanel4.setPreferredSize(new java.awt.Dimension(529, 40));

		jLabel9.setText(" Rechercher Commande(N°,date, fournissuer)");

		javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
		jPanel4Layout
				.setHorizontalGroup(
						jPanel4Layout.createParallelGroup(Alignment.LEADING)
								.addGroup(jPanel4Layout.createSequentialGroup().addContainerGap().addComponent(jLabel9)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(tb_search,
												GroupLayout.PREFERRED_SIZE, 274, GroupLayout.PREFERRED_SIZE)
										.addContainerGap(172, Short.MAX_VALUE)));
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

		JPanel FormJPanel_1 = new JPanel();
		FormJPanel_1.setLayout(null);
		FormJPanel_1.setPreferredSize(new Dimension(350, 530));
		FormJPanel_1.setBorder(new LineBorder(UIManager.getColor("Desktop.background")));
		FormJPanel_1.setBackground(SystemColor.window);
		FormJPanel_1.setBounds(0, 403, 750, 62);
		TablePanel.add(FormJPanel_1);

		JLabel lblProduit = new JLabel();
		lblProduit.setText("Article");
		lblProduit.setForeground(Color.BLACK);
		lblProduit.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		lblProduit.setBackground(Color.DARK_GRAY);
		lblProduit.setBounds(6, 6, 47, 16);
		FormJPanel_1.add(lblProduit);

		JLabel jLabel2_7_1 = new JLabel();
		jLabel2_7_1.setText("*");
		jLabel2_7_1.setForeground(Color.RED);
		jLabel2_7_1.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		jLabel2_7_1.setBounds(47, 6, 17, 16);
		FormJPanel_1.add(jLabel2_7_1);

		JLabel jLabel2_6_1 = new JLabel();
		jLabel2_6_1.setText("*");
		jLabel2_6_1.setForeground(Color.RED);
		jLabel2_6_1.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		jLabel2_6_1.setBounds(467, 6, 17, 16);
		FormJPanel_1.add(jLabel2_6_1);

		JLabel lblQuantit_1 = new JLabel();
		lblQuantit_1.setText("Quantité");
		lblQuantit_1.setForeground(Color.BLACK);
		lblQuantit_1.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		lblQuantit_1.setBackground(Color.DARK_GRAY);
		lblQuantit_1.setBounds(268, 6, 61, 16);
		FormJPanel_1.add(lblQuantit_1);

		JLabel jLabel2_5_1 = new JLabel();
		jLabel2_5_1.setText("*");
		jLabel2_5_1.setForeground(Color.RED);
		jLabel2_5_1.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		jLabel2_5_1.setBounds(322, 6, 17, 16);
		FormJPanel_1.add(jLabel2_5_1);

		JLabel lblRfrence = new JLabel();
		lblRfrence.setText("Référence");
		lblRfrence.setForeground(Color.BLACK);
		lblRfrence.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		lblRfrence.setBackground(Color.DARK_GRAY);
		lblRfrence.setBounds(154, 6, 80, 16);
		FormJPanel_1.add(lblRfrence);

		tb_reference = new JTextField();
		tb_reference.setColumns(10);
		tb_reference.setBackground(Color.WHITE);
		tb_reference.setBounds(154, 26, 93, 26);
		FormJPanel_1.add(tb_reference);

		tb_quantite = new JTextField();
		tb_quantite.setColumns(10);
		tb_quantite.setBackground(Color.WHITE);
		tb_quantite.setBounds(268, 26, 85, 26);
		FormJPanel_1.add(tb_quantite);

		cb_article = new JComboBox<String>();
		cb_article.setForeground(Color.DARK_GRAY);
		cb_article.setFont(new Font("Trebuchet MS", Font.PLAIN, 11));
		cb_article.setBackground(SystemColor.window);
		cb_article.setBounds(6, 28, 149, 27);
		FormJPanel_1.add(cb_article);

		JLabel lblQuantit_1_1 = new JLabel();
		lblQuantit_1_1.setText("Prix unitaire(HT)");
		lblQuantit_1_1.setForeground(Color.BLACK);
		lblQuantit_1_1.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		lblQuantit_1_1.setBackground(Color.DARK_GRAY);
		lblQuantit_1_1.setBounds(374, 6, 101, 16);
		FormJPanel_1.add(lblQuantit_1_1);

		tb_prix_unitaire = new JTextField();
		tb_prix_unitaire.setColumns(10);
		tb_prix_unitaire.setBackground(Color.WHITE);
		tb_prix_unitaire.setBounds(374, 26, 85, 26);
		FormJPanel_1.add(tb_prix_unitaire);

		JLabel lblQuantit_1_1_1 = new JLabel();
		lblQuantit_1_1_1.setText("Remise (HT)");
		lblQuantit_1_1_1.setForeground(Color.BLACK);
		lblQuantit_1_1_1.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		lblQuantit_1_1_1.setBackground(Color.DARK_GRAY);
		lblQuantit_1_1_1.setBounds(488, 6, 101, 16);
		FormJPanel_1.add(lblQuantit_1_1_1);

		JLabel lblQuantit_1_1_2 = new JLabel();
		lblQuantit_1_1_2.setText("Prix Total(HT)");
		lblQuantit_1_1_2.setForeground(Color.BLACK);
		lblQuantit_1_1_2.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		lblQuantit_1_1_2.setBackground(Color.DARK_GRAY);
		lblQuantit_1_1_2.setBounds(577, 6, 80, 16);
		FormJPanel_1.add(lblQuantit_1_1_2);

		tb_remise = new JTextField();
		tb_remise.setColumns(10);
		tb_remise.setBackground(Color.WHITE);
		tb_remise.setBounds(486, 26, 85, 26);
		FormJPanel_1.add(tb_remise);

		tb_prix_total = new JTextField();
		tb_prix_total.setColumns(10);
		tb_prix_total.setBackground(Color.WHITE);
		tb_prix_total.setBounds(577, 26, 85, 26);
		FormJPanel_1.add(tb_prix_total);

		JLabel jLabel2_6_1_1 = new JLabel();
		jLabel2_6_1_1.setText("*");
		jLabel2_6_1_1.setForeground(Color.RED);
		jLabel2_6_1_1.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		jLabel2_6_1_1.setBounds(557, 6, 17, 16);
		FormJPanel_1.add(jLabel2_6_1_1);

		JLabel jLabel2_6_1_2 = new JLabel();
		jLabel2_6_1_2.setText("*");
		jLabel2_6_1_2.setForeground(Color.RED);
		jLabel2_6_1_2.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		jLabel2_6_1_2.setBounds(656, 6, 17, 16);
		FormJPanel_1.add(jLabel2_6_1_2);

		btnAjouter = new JButton();
		btnAjouter.setText("+");
		btnAjouter.setFont(new Font("Trebuchet MS", Font.BOLD, 33));
		btnAjouter.setBounds(668, 9, 76, 29);
		FormJPanel_1.add(btnAjouter);

		btnRetirer = new JButton();
		btnRetirer.setBounds(668, 33, 76, 29);
		FormJPanel_1.add(btnRetirer);
		btnRetirer.setText("-");
		btnRetirer.setFont(new Font("Trebuchet MS", Font.BOLD, 33));

		pack();
	}

	public Commande getCommandeFromForm() {

		Commande commande = new Commande();
		commande.setCommandeNumero(tb_numero.getText().trim().toString());

		SimpleDateFormat sdf = new SimpleDateFormat(ConstMessagesEN.Params.DATE_FORMAT);
		String date = sdf.format(tb_date_commande.getDate());
		commande.setCommandeDate(date);
		// Fournisseur
		// User

		commande.setCommandeLivraisonMode(cb_mode_livraison.getSelectedItem().toString());
		commande.setCommandeInstruction(tb_indication.getText());

		if (!tb_id.getText().trim().equalsIgnoreCase(Strings.EMPTY))
			commande.setId(Long.parseLong(tb_id.getText().trim().toString()));
		return commande;
	}

	public void clearForm() {

		tb_id.setText(Strings.EMPTY);
		tb_numero.setText(Strings.EMPTY);
		tb_indication.setText(Strings.EMPTY);
		tb_numero.setText(Shared.generateRandom("C"));
		lblStatut.setVisible(false);
		tbStatut.setText(Strings.EMPTY);
		
		tbStatut.setForeground(Color.BLACK);
		tbStatut.setBackground(Color.WHITE);
		
	}

	public void clearLigneCommandeForm() {
		tb_reference.setText(Strings.EMPTY);
		tb_quantite.setText("0");
		tb_prix_unitaire.setText("0");
		tb_remise.setText("0");
		tb_prix_total.setText("0");
	}

}
