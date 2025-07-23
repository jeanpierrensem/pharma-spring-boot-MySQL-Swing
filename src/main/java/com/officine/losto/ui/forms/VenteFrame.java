package com.officine.losto.ui.forms;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;
import java.math.BigDecimal;
import java.util.List;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
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
import javax.swing.border.TitledBorder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import org.apache.logging.log4j.util.Strings;

import com.officine.losto.backend.entity.Vente;
import com.officine.losto.backend.entity.utilities.ModePaiement;
import com.officine.losto.backend.entity.utilities.TypeVente;
import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.params.constant.ConstMessagesEN.Params;
import com.officine.losto.ui.forms.model.LigneVenteTableModel;
import com.officine.losto.ui.forms.model.VenteTableModel;
import com.officine.losto.uti.shared.Shared;

import lombok.Getter;

@org.springframework.stereotype.Component
@Getter
public class VenteFrame extends JDialog {

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
	private JLabel lblNomVente;
	private JLabel lblForme;
	private JLabel lblType;
	private JLabel jLabel2_6;
	private JLabel lblDescription;
	private JTextArea tb_remarque;
	//private JComboBox<String> cb_modePaiement;
	private JTextField tb_prix_total;
	private JTextField tb_vendu_par;
	private JLabel lblModeDeLivraison;
	private JLabel jLabel2;
	private JComboBox<String> cb_type_vente;
	private JScrollPane VentetablejScrollPane;
	private JScrollPane ligneVentetablejScrollPane;
	private JTable tableLigneVente;
	private JTable tableVente;

	private JTableHeader tableHeader;
	private JTableHeader tableLigndeHeader;
	private JTextField tb_client;
	private JLabel lblStatut;
	private JButton btnImprimer;
	private JLabel lblMontantPay;
	private JLabel lblMontantRendu;
	private JLabel jLabel2_1;
	private JTextField tb_date;
	private JTextField tb_mnt_paye;
	private JTextField tb_mnt_rendu;
	private JLabel lbl_enLettre;
	private JButton btnRetour;

	private JTextField editorTypeVente;
	private JTextField editorModePaiement;
	private JComboBox<String> cb_paiementMode;
	private JLabel jLabel2_2;

	// public VenteFrame() {}

	public VenteFrame(VenteTableModel VenteTableModel, LigneVenteTableModel LigneVenteTableModel) {
		setModal(true);

		initComponents();
		this.getTableVente().setModel(VenteTableModel);
		this.getTableLigneVente().setModel(LigneVenteTableModel);

		this.getTableVente().getColumnModel().getColumn(0).setPreferredWidth(20);
		this.getTableVente().getColumnModel().getColumn(1).setPreferredWidth(150);
		this.getTableVente().setRowHeight(30);

		tableHeader = this.getTableVente().getTableHeader();
		tableHeader
				.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, ConstMessagesEN.Labels.POLICE_SIZE));
		tableHeader.setBackground(Color.WHITE);
		tableHeader.setPreferredSize(new Dimension(tableHeader.getWidth(), 30));

		this.getTableLigneVente().setRowHeight(30);
		tableLigndeHeader = this.getTableLigneVente().getTableHeader();
		tableLigndeHeader.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, 11));
		tableLigndeHeader.setBackground(Color.white);
		tableLigndeHeader.setPreferredSize(new Dimension(tableLigndeHeader.getWidth(), 30));

	}

	@SuppressWarnings("serial")
	private void initComponents() {
		setResizable(false);

		getContentPane().setPreferredSize(new Dimension(1100, ConstMessagesEN.Params.DEFAULT_WIDTH - 180));
		setTitle(ConstMessagesEN.Labels.VENTE);

		BtnjPanel = new javax.swing.JPanel();
		BtnjPanel.setBounds(0, 464, 1100, 56);
		BtnjPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		BtnjPanel.setBackground(Color.WHITE);
		// jPanel1.setBorder(BorderFactory.createTitledBorder("Jpanel1"));
		FormJPanel = new javax.swing.JPanel();
		FormJPanel.setBounds(0, 0, 350, 465);
		FormJPanel.setBackground(Color.WHITE);
		// FormJPanel.setBorder(new
		// LineBorder(UIManager.getColor("Desktop.background")));
		TitledBorder border = BorderFactory.createTitledBorder("collecte d'information");
		border.setTitleFont(
				new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.CENTER_BASELINE, ConstMessagesEN.Labels.POLICE_SIZE));
		border.setTitleJustification(TitledBorder.CENTER); // Centrage
		border.setTitlePosition(TitledBorder.TOP); // Position en
		FormJPanel.setBorder(border);

		TablePanel = new javax.swing.JPanel();
		TablePanel.setBounds(350, 0, 750, 465);

		btnEnregistrer = new javax.swing.JButton();
		btnEnregistrer.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		btnSupprimer = new javax.swing.JButton();
		btnSupprimer.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		btnQuitter = new javax.swing.JButton();
		btnQuitter.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));

		jLabel1 = new javax.swing.JLabel();
		jLabel1.setBackground(Color.DARK_GRAY);
		jLabel1.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		jLabel1.setForeground(Color.DARK_GRAY);
		jLabel1.setBounds(6, 6, 52, 16);

	
		jPanel4 = new javax.swing.JPanel();
		jPanel4.setBounds(0, 113, 750, 40);
		jLabel9 = new javax.swing.JLabel();
		jLabel9.setForeground(Color.DARK_GRAY);
		jLabel9.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		
		tb_search = new javax.swing.JTextField();
		tb_search.setForeground(Color.DARK_GRAY);
		tb_search.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		ligneVentetablejScrollPane = new javax.swing.JScrollPane();
		ligneVentetablejScrollPane.setBounds(0, 155, 750, 248);

		VentetablejScrollPane = new javax.swing.JScrollPane();
		VentetablejScrollPane.setBounds(0, 0, 750, 115);

		tableLigneVente = new javax.swing.JTable() {
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

		tableVente = new javax.swing.JTable() {

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
		tableVente.setShowVerticalLines(false);
		tableVente.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableVente.setFillsViewportHeight(true);

		tableLigneVente.setBackground(Color.WHITE);
		tableLigneVente.setForeground(SystemColor.inactiveCaptionText);
		tableLigneVente.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		tableLigneVente.setGridColor(SystemColor.lightGray);

		tableVente.setBackground(Color.WHITE);
		tableVente.setForeground(SystemColor.inactiveCaptionText);
		tableVente.setFont(new Font("Dialog", Font.PLAIN, 11));
		tableVente.setGridColor(SystemColor.lightGray);

		getContentPane().setLayout(null);

		BtnjPanel.setPreferredSize(new java.awt.Dimension(729, 50));
		btnEnregistrer.setText(ConstMessagesEN.Labels.ENREGISTRER_BTN);
		btnEnregistrer.setIcon(new ImageIcon(ClassLoader.getSystemResource(Params.BASE_PATH + "images/Apply.png")));

		btnSupprimer.setText(ConstMessagesEN.Labels.SUPPRIMER_BTN);
		btnSupprimer.setIcon(new ImageIcon(ClassLoader.getSystemResource(Params.BASE_PATH + "images/Delete.png")));

		btnQuitter.setText(ConstMessagesEN.Labels.QUITTER_BTN);
		btnQuitter.setIcon(new ImageIcon(ClassLoader.getSystemResource(Params.BASE_PATH + "images/Cancel.png")));

		btnImprimer = new JButton();

		btnImprimer.setText("Imprimer Ticket Caisse");
		btnImprimer.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));

		btnRetour = new JButton();
		btnRetour.setText("Valider Retour");
		btnRetour.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));

		javax.swing.GroupLayout gl_BtnjPanel = new javax.swing.GroupLayout(BtnjPanel);
		gl_BtnjPanel.setHorizontalGroup(gl_BtnjPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_BtnjPanel
				.createSequentialGroup().addContainerGap()
				.addComponent(btnEnregistrer, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.RELATED)
				.addComponent(btnSupprimer, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE).addGap(18)
				.addComponent(btnImprimer, GroupLayout.PREFERRED_SIZE, 168, GroupLayout.PREFERRED_SIZE).addGap(18)
				.addComponent(btnRetour, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.RELATED, 426, Short.MAX_VALUE)
				.addComponent(btnQuitter, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE).addGap(14)));
		gl_BtnjPanel.setVerticalGroup(gl_BtnjPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_BtnjPanel.createSequentialGroup().addContainerGap(17, Short.MAX_VALUE)
						.addGroup(gl_BtnjPanel.createParallelGroup(Alignment.BASELINE).addComponent(btnEnregistrer)
								.addComponent(btnSupprimer).addComponent(btnQuitter).addComponent(btnImprimer)
								.addComponent(btnRetour))
						.addContainerGap()));
		BtnjPanel.setLayout(gl_BtnjPanel);

		getContentPane().add(BtnjPanel);

		// jPanel2.setBorder(new LineBorder(Color.BLUE, 2, true));
		FormJPanel.setPreferredSize(new Dimension(350, 530));

		jLabel1.setText("N° Vente");

		jLabel2_3 = new JLabel();
		jLabel2_3.setBounds(58, 6, 17, 16);
		jLabel2_3.setText("*");
		jLabel2_3.setForeground(Color.RED);
		jLabel2_3.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));

		tb_numero = new JTextField();
		tb_numero.setForeground(Color.DARK_GRAY);
		tb_numero.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, 17));
		tb_numero.setEditable(false);
		tb_numero.setBounds(6, 21, 307, 34);

		tb_numero.setBackground(Color.decode("#ccffcc"));

		tb_numero.setColumns(10);

		tb_id = new JTextField();
		tb_id.setForeground(Color.DARK_GRAY);
		tb_id.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		tb_id.setBounds(309, 21, 35, 34);
		tb_id.setBackground(Color.white);
		tb_id.setEditable(false);
		tb_id.setColumns(10);

		lblNomVente = new JLabel();
		lblNomVente.setBackground(Color.DARK_GRAY);
		lblNomVente.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		lblNomVente.setForeground(Color.DARK_GRAY);
		lblNomVente.setBounds(6, 53, 82, 28);
		lblNomVente.setText("Date/heure");

		lblForme = new JLabel();
		lblForme.setBackground(Color.DARK_GRAY);
		lblForme.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		lblForme.setForeground(Color.DARK_GRAY);
		lblForme.setBounds(6, 81, 46, 27);
		lblForme.setText("vendeur");

		lblType = new JLabel();
		lblType.setBackground(Color.DARK_GRAY);
		lblType.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		lblType.setForeground(Color.DARK_GRAY);
		lblType.setBounds(6, 126, 108, 32);
		lblType.setText("Mode de paiement ");

		jLabel2_6 = new JLabel();
		jLabel2_6.setBounds(97, 126, 17, 32);
		jLabel2_6.setText("*");
		jLabel2_6.setForeground(Color.RED);
		jLabel2_6.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));

		lblDescription = new JLabel();
		lblDescription.setBackground(Color.DARK_GRAY);
		lblDescription.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		lblDescription.setForeground(Color.DARK_GRAY);
		lblDescription.setBounds(6, 328, 82, 16);
		lblDescription.setText("Remarque");

		tb_remarque = new JTextArea();
		tb_remarque.setForeground(Color.DARK_GRAY);
		tb_remarque.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		tb_remarque.setWrapStyleWord(true);
		tb_remarque.setLineWrap(true);
		tb_remarque.setBounds(107, 328, 237, 46);
		tb_remarque.setBackground(SystemColor.window);

		/*Vector<String> data = new Vector<>(
				List.of(ModePaiement.ESPECE.toString(), ModePaiement.CARTE.toString(), ModePaiement.CHEQUE.toString(),
						ModePaiement.VIREMENT.toString(), ModePaiement.MOBILE_MONEY.toString()));
		cb_modePaiement = new JComboBox<String>(data);
		cb_modePaiement.setEditable(true);
		//cb_modePaiement.setBounds(110, 431, 176, 37);
		cb_modePaiement.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 12));
		if (data.size() > 0)
			cb_modePaiement.setSelectedIndex(0);
		editorModePaiement = (JTextField) cb_modePaiement.getEditor().getEditorComponent(); 
		
		cb_modePaiement.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 12));
				setForeground(new Color(20, 20, 20));
				if (isSelected) {
					setBackground(new Color(180, 200, 255));
				} else {
					setBackground(Color.WHITE);
				}
				return c;
			}
		});
		FormJPanel.add(cb_modePaiement);*/

		getContentPane().add(FormJPanel);
		FormJPanel.setLayout(null);
		FormJPanel.add(lblNomVente);
		FormJPanel.add(jLabel1);
		FormJPanel.add(jLabel2_3);
		FormJPanel.add(lblDescription);
		FormJPanel.add(jLabel2_6);
		FormJPanel.add(lblForme);
		FormJPanel.add(lblType);
		//FormJPanel.add(cb_modePaiement);
		FormJPanel.add(tb_remarque);
		FormJPanel.add(tb_numero);
		FormJPanel.add(tb_id);

		tb_vendu_par = new JTextField();
		tb_vendu_par.setForeground(Color.DARK_GRAY);
		tb_vendu_par.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		tb_vendu_par.setEditable(false);
		tb_vendu_par.setColumns(10);
		tb_vendu_par.setBackground(SystemColor.window);
		tb_vendu_par.setBounds(107, 81, 225, 33);
		FormJPanel.add(tb_vendu_par);

		lblModeDeLivraison = new JLabel();
		lblModeDeLivraison.setText("Type de vente");
		lblModeDeLivraison.setForeground(Color.DARK_GRAY);
		lblModeDeLivraison.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		lblModeDeLivraison.setBackground(Color.DARK_GRAY);
		lblModeDeLivraison.setBounds(6, 165, 74, 16);
		FormJPanel.add(lblModeDeLivraison);

		jLabel2 = new JLabel();
		jLabel2.setText("*");
		jLabel2.setForeground(Color.RED);
		jLabel2.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		jLabel2.setBounds(78, 165, 17, 16);
		FormJPanel.add(jLabel2);

		Vector<String> dataTypeVente = new Vector<>(List.of(TypeVente.ORDONNANCE.toString(),
				TypeVente.COMPTANT.toString(), TypeVente.LIVRAISON.toString(), TypeVente.AUTRE.toString()));
		cb_type_vente = new JComboBox<String>(dataTypeVente);
		cb_type_vente.setEditable(true);
		cb_type_vente.setBounds(107, 165, 221, 20);

		if (dataTypeVente.size() > 0)
			cb_type_vente.setSelectedIndex(0);
		editorTypeVente = (JTextField) cb_type_vente.getEditor().getEditorComponent();

		cb_type_vente.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 12));
		if (dataTypeVente.size() > 0)
			cb_type_vente.setSelectedIndex(0);
		editorTypeVente = (JTextField) cb_type_vente.getEditor().getEditorComponent();
		cb_type_vente.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 12));
				setForeground(new Color(20, 20, 20));
				if (isSelected) {
					setBackground(new Color(180, 200, 255));
				} else {
					setBackground(Color.WHITE);
				}
				return c;
			}
		});

		FormJPanel.add(cb_type_vente);

		tb_client = new JTextField();
		tb_client.setBackground(SystemColor.window);
		tb_client.setForeground(Color.DARK_GRAY);
		tb_client.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		tb_client.setBounds(107, 193, 226, 41);
		FormJPanel.add(tb_client);

		lblStatut = new JLabel();
		lblStatut.setText("Client");
		lblStatut.setForeground(Color.DARK_GRAY);
		lblStatut.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		lblStatut.setBackground(Color.DARK_GRAY);
		lblStatut.setBounds(6, 193, 35, 41);
		FormJPanel.add(lblStatut);

		lblMontantPay = new JLabel();
		lblMontantPay.setText("Montant Payé");
		lblMontantPay.setForeground(Color.DARK_GRAY);
		lblMontantPay.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		lblMontantPay.setBackground(Color.DARK_GRAY);
		lblMontantPay.setBounds(6, 246, 74, 34);
		FormJPanel.add(lblMontantPay);

		lblMontantRendu = new JLabel();
		lblMontantRendu.setText("Montant Rendu");
		lblMontantRendu.setForeground(Color.DARK_GRAY);
		lblMontantRendu.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		lblMontantRendu.setBackground(Color.DARK_GRAY);
		lblMontantRendu.setBounds(6, 281, 97, 24);
		FormJPanel.add(lblMontantRendu);

		jLabel2_1 = new JLabel();
		jLabel2_1.setText("*");
		jLabel2_1.setForeground(Color.RED);
		jLabel2_1.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		jLabel2_1.setBounds(78, 246, 17, 34);
		FormJPanel.add(jLabel2_1);

		tb_date = new JTextField();
		tb_date.setForeground(Color.DARK_GRAY);
		tb_date.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		tb_date.setEditable(false);
		tb_date.setColumns(10);
		tb_date.setBackground(Color.WHITE);
		tb_date.setBounds(107, 53, 221, 28);
		FormJPanel.add(tb_date);

		tb_mnt_paye = new JTextField();

		tb_mnt_paye.setForeground(Color.DARK_GRAY);
		tb_mnt_paye.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		tb_mnt_paye.setColumns(10);
		tb_mnt_paye.setBackground(SystemColor.window);
		tb_mnt_paye.setBounds(107, 246, 175, 34);
		FormJPanel.add(tb_mnt_paye);

		tb_mnt_rendu = new JTextField();
		tb_mnt_rendu.setEditable(false);
		tb_mnt_rendu.setForeground(Color.DARK_GRAY);
		tb_mnt_rendu.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		tb_mnt_rendu.setColumns(10);
		tb_mnt_rendu.setBackground(SystemColor.window);
		tb_mnt_rendu.setBounds(107, 281, 175, 34);
		FormJPanel.add(tb_mnt_rendu);
		
		
		
		
		Vector<String> dataPaiementMode = new Vector<>(
				List.of(ModePaiement.ESPECE.toString(), ModePaiement.CARTE.toString(), ModePaiement.CHEQUE.toString(),
						ModePaiement.VIREMENT.toString(), ModePaiement.MOBILE_MONEY.toString()));
		cb_paiementMode = new JComboBox<String>(dataPaiementMode);
		cb_paiementMode.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE,  Font.PLAIN, 12));
		cb_paiementMode.setEditable(true);
		cb_paiementMode.setBounds(107, 127, 221, 20);
		
		if (dataPaiementMode.size() > 0)
			cb_paiementMode.setSelectedIndex(0);
		   editorModePaiement = (JTextField) cb_paiementMode.getEditor().getEditorComponent();
		
		   cb_paiementMode.setRenderer(new DefaultListCellRenderer() {
				@Override
				public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
						boolean cellHasFocus) {
					Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
					setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 12));
					setForeground(new Color(20, 20, 20));
					if (isSelected) {
						setBackground(new Color(180, 200, 255));
					} else {
						setBackground(Color.WHITE);
					}
					return c;
				}
			});

		   
		
		FormJPanel.add(cb_paiementMode);
		
		jLabel2_2 = new JLabel();
		jLabel2_2.setText("*");
		jLabel2_2.setForeground(Color.RED);
		jLabel2_2.setFont(new Font("Dialog", Font.PLAIN, 11));
		jLabel2_2.setBounds(41, 193, 17, 34);
		FormJPanel.add(jLabel2_2);
		TablePanel.setLayout(null);

		jPanel4.setPreferredSize(new java.awt.Dimension(529, 40));

		jLabel9.setText(" Rechercher Vente");

		javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
		jPanel4Layout.setHorizontalGroup(
			jPanel4Layout.createParallelGroup(Alignment.LEADING)
				.addGroup(jPanel4Layout.createSequentialGroup()
					.addContainerGap()
					.addComponent(jLabel9)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(tb_search, GroupLayout.PREFERRED_SIZE, 423, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(218, Short.MAX_VALUE))
		);
		jPanel4Layout.setVerticalGroup(
			jPanel4Layout.createParallelGroup(Alignment.LEADING)
				.addGroup(jPanel4Layout.createSequentialGroup()
					.addContainerGap()
					.addGroup(jPanel4Layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(jLabel9)
						.addComponent(tb_search, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(10, Short.MAX_VALUE))
		);
		jPanel4.setLayout(jPanel4Layout);

		TablePanel.add(jPanel4);

		// ligneVentetablejScrollPane.setColumnHeaderView(tableLigneVente);
		// VentetablejScrollPane.setColumnHeaderView(tableVente);

		ligneVentetablejScrollPane.setViewportView(tableLigneVente);
		TablePanel.add(ligneVentetablejScrollPane);

		VentetablejScrollPane.setViewportView(tableVente);
		TablePanel.add(VentetablejScrollPane);

		getContentPane().add(TablePanel);

		JPanel FormJPanel_1 = new JPanel();
		FormJPanel_1.setLayout(null);
		FormJPanel_1.setPreferredSize(new Dimension(350, 530));
		FormJPanel_1.setBorder(new LineBorder(UIManager.getColor("Desktop.background")));
		FormJPanel_1.setBackground(SystemColor.window);
		FormJPanel_1.setBounds(0, 403, 750, 62);
		TablePanel.add(FormJPanel_1);

		JLabel lblQuantit_1_1_1 = new JLabel();
		lblQuantit_1_1_1.setText("Arrêté la somme de ");
		lblQuantit_1_1_1.setForeground(Color.DARK_GRAY);
		lblQuantit_1_1_1.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, 11));
		lblQuantit_1_1_1.setBackground(Color.DARK_GRAY);
		lblQuantit_1_1_1.setBounds(6, 33, 114, 16);
		FormJPanel_1.add(lblQuantit_1_1_1);

		JLabel lblQuantit_1_1_2 = new JLabel();
		lblQuantit_1_1_2.setText("Prix Total(HT)");
		lblQuantit_1_1_2.setForeground(Color.DARK_GRAY);
		lblQuantit_1_1_2.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, 11));
		lblQuantit_1_1_2.setBackground(Color.DARK_GRAY);
		lblQuantit_1_1_2.setBounds(498, 6, 80, 16);
		FormJPanel_1.add(lblQuantit_1_1_2);

		tb_prix_total = new JTextField();
		tb_prix_total.setEditable(false);
		tb_prix_total.setHorizontalAlignment(SwingConstants.RIGHT);
		tb_prix_total.setForeground(Color.DARK_GRAY);
		tb_prix_total.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, 11));
		tb_prix_total.setColumns(10);
		tb_prix_total.setBackground(Color.WHITE);
		tb_prix_total.setBounds(588, 1, 156, 26);
		FormJPanel_1.add(tb_prix_total);

		lbl_enLettre = new JLabel();
		lbl_enLettre.setForeground(Color.DARK_GRAY);
		lbl_enLettre.setFont(new Font("Dialog", Font.ITALIC, 9));
		lbl_enLettre.setBackground(Color.DARK_GRAY);
		lbl_enLettre.setBounds(121, 33, 623, 16);
		FormJPanel_1.add(lbl_enLettre);

		pack();
	}

	public Vente getVenteFromForm() {

		Vente vente = new Vente();
		vente.setNumero(tb_numero.getText().trim().toString());

		// date
		/*
		 * SimpleDateFormat sdf = new
		 * SimpleDateFormat(ConstMessagesEN.Params.DATE_FORMAT); String date =
		 * sdf.format(tb_date.getText());
		 */
		vente.setVentedate(tb_date.getText());
		// vendeur
		vente.setVendeur(tb_vendu_par.getText());
		// client
		vente.setClient(tb_client.getText());
		// type vente
		vente.setTypeVente(cb_type_vente.getSelectedItem().toString());
		// paiement
		vente.setModePaiement(cb_paiementMode.getSelectedItem().toString());
		// montant total
		if (tb_prix_total.getText().trim().equalsIgnoreCase(Strings.EMPTY))
			vente.setMontantTotal(BigDecimal.valueOf(0)); 
		else
		   vente.setMontantTotal(new BigDecimal(tb_prix_total.getText()));
		
		// montant payé
		if (tb_mnt_paye.getText().trim().equalsIgnoreCase(Strings.EMPTY))
			vente.setMontantPaye(BigDecimal.valueOf(0)); 
		else
			vente.setMontantPaye(new BigDecimal(tb_mnt_paye.getText()));
		
		// montant rendu
		if (tb_mnt_rendu.getText().trim().equalsIgnoreCase(Strings.EMPTY))
			vente.setMontantRendu(BigDecimal.valueOf(0)); 
		else
		vente.setMontantRendu(new BigDecimal(tb_mnt_rendu.getText()));
		// remarque
		vente.setRemarque(tb_remarque.getText());

		if (!tb_id.getText().trim().equalsIgnoreCase(Strings.EMPTY))
			vente.setId(Long.parseLong(tb_id.getText().trim().toString()));

		return vente;
	}

	public void clearForm() {

		tb_id.setText(Strings.EMPTY);
		tb_numero.setText(Shared.generateRandom("C"));
		lblStatut.setVisible(false);
		tb_client.setText(Strings.EMPTY);
		tb_remarque.setText(Strings.EMPTY);

		tb_client.setForeground(Color.BLACK);
		tb_client.setBackground(Color.WHITE);

	}

	public void clearLigneVenteForm() {
		tb_prix_total.setText("" + new BigDecimal("0"));
	}

}
