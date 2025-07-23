package com.officine.losto.ui.forms;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.SystemColor;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
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
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import org.apache.logging.log4j.util.Strings;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;

import com.officine.losto.backend.entity.Article;
import com.officine.losto.backend.services.ArticleService;
import com.officine.losto.backend.services.FormeService;
import com.officine.losto.backend.services.FormeServiceImpl;
import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.params.constant.ConstMessagesEN.Params;
import com.officine.losto.ui.forms.model.ArticleTableModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.PopupMenuEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@org.springframework.stereotype.Component
@Getter
public class ArticleFrame extends JDialog {

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
	private JTextField tb_codebarre;
	private JTextField tb_id;
	private JLabel lblNomArticle;
	private JLabel jLabel2_4;
	private JLabel lblForme;
	private JLabel jLabel2_5;
	private JLabel lblType;
	private JLabel jLabel2_6;
	private JLabel lblCatgorie;
	private JLabel jLabel2_7;
	private JLabel lblRayon;
	private JLabel jLabel2_8;
	private JLabel lblDosage;
	private JLabel jLabel2_9;
	private JLabel lblQuantit;
	private JLabel lblPrixAchat;
	private JLabel jLabel3_3;
	private JLabel lblDescription;
	private JTextArea tb_description;
	private JTextField tb_nomarticle;
	private JComboBox<String> cb_lot;
	private JTextField tb_prix_achat;
	private JTextField tb_prix_vente;
	private JTextField tb_dosage;
	private JComboBox<String> cb_forme;
	private JComboBox<String> cb_type;
	private JComboBox<String> cb_categorie;
	private JComboBox<String> cb_rayon;
	private JTextField tb_quantite;
	private JComboBox<String> cb_packaging;
	private JButton btnAddLot;

	private JTableHeader tableHeader;
	private JButton btnAddForme;
	private JButton btnAddType;
	private JButton btnAddCagtegorie;
	private JButton btnAddRayon;
	private JButton btnAddPackaging;
	
	private   JTextField editorforme  ; 
	private   JTextField editorCategory; 
	private JTextField editorType; 
	private   JTextField editorRayon  ; 
	private   JTextField editorConditionnement  ; 
	private   JTextField editorLot  ; 
	
	//public ArticleFrame() {}

	public ArticleFrame(ArticleTableModel tableModel) {
		
		setModal(true);

		initComponents();
		this.getTable().setModel(tableModel);

		this.getTable().getColumnModel().getColumn(0).setPreferredWidth(20);
		this.getTable().setRowHeight(30);
		tableHeader = this.getTable().getTableHeader();
		tableHeader.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, ConstMessagesEN.Labels.POLICE_SIZE));
		tableHeader.setBackground(Color.WHITE);
		tableHeader.setPreferredSize(new Dimension(tableHeader.getWidth(), 30)); 

	}

	
	
	@SuppressWarnings("serial")
	private void initComponents() {
		setResizable(true);
		getContentPane().setPreferredSize(new Dimension(ConstMessagesEN.Params.DEFAULT_HEIGHT * 3 - 100,
				ConstMessagesEN.Params.DEFAULT_WIDTH));

		setTitle(ConstMessagesEN.DialogTitles.ARTICLE);

		// setBorder(new TitledBorder(new TitledBorder(new
		// EtchedBorder(EtchedBorder.LOWERED, null, null), "Gestion des articles",
		// TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), "Gestion
		// des articles", TitledBorder.TRAILING, TitledBorder.TOP, null, new Color(255,
		// 255, 255)));

		BtnjPanel = new javax.swing.JPanel();
		BtnjPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		BtnjPanel.setBackground(Color.WHITE);
		// jPanel1.setBorder(BorderFactory.createTitledBorder("Jpanel1"));
		FormJPanel = new javax.swing.JPanel();
		FormJPanel.setForeground(Color.DARK_GRAY);
		FormJPanel.setBackground(Color.WHITE);
		FormJPanel.setBorder(new LineBorder(UIManager.getColor("Desktop.background")));
		TablePanel = new javax.swing.JPanel();
		// groupeTablePanel.setBorder(null);

		btnAjouter = new javax.swing.JButton();
		btnAjouter.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		btnSupprimer = new javax.swing.JButton();
		btnSupprimer.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		btnQuitter = new javax.swing.JButton();
		btnQuitter.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));

		jLabel1 = new javax.swing.JLabel();
		jLabel1.setBackground(Color.DARK_GRAY);
		jLabel1.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 13));
		jLabel1.setForeground(Color.DARK_GRAY);
		jLabel1.setBounds(6, 0, 134, 22);
		jLabel2 = new javax.swing.JLabel();
		jLabel2.setBackground(Color.DARK_GRAY);
		jLabel2.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 12));
		jLabel2.setForeground(Color.DARK_GRAY);
		jLabel2.setBounds(6, 431, 111, 37);
		jLabel3 = new javax.swing.JLabel();
		jLabel3.setBackground(Color.DARK_GRAY);
		jLabel3.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 12));
		jLabel3.setForeground(Color.DARK_GRAY);
		jLabel3.setBounds(6, 480, 47, 37);

		// jPanel3.setBorder(new LineBorder(Color.GREEN, 2));
		jPanel4 = new javax.swing.JPanel();
		jLabel9 = new javax.swing.JLabel();
		jLabel9.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		tb_search = new javax.swing.JTextField();
		tb_search.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		tablejScrollPane = new javax.swing.JScrollPane();
		table = new javax.swing.JTable() {

			/*
			 * public Class getColumnClass(int column) { return getValueAt(0,
			 * column).getClass();
			 * 
			 * }
			 */

			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);

				if (!isRowSelected(row)) {

					// c.setBackground(row % 2 == 0 ? UIManager.getColor("ToolTip.background") :
					// Color.white);
					c.setBackground(row % 2 == 0 ? new Color(220, 240, 255) : Color.white);

				}

				if (isRowSelected(row) && isColumnSelected(column))
					((JComponent) c).setBorder(new LineBorder(Color.red));

				return c;
			}

		};
		
		
		
		
		table.setBackground(Color.WHITE);
		table.setForeground(Color.DARK_GRAY);
		table.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		table.setGridColor(SystemColor.lightGray);

		// table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
		BtnjPanel.setPreferredSize(new java.awt.Dimension(729, 50));

		btnAjouter.setText(ConstMessagesEN.Labels.ENREGISTRER_BTN);
		btnAjouter.setIcon(new ImageIcon(ClassLoader.getSystemResource(Params.BASE_PATH + "images/Apply.png")));

		btnSupprimer.setText(ConstMessagesEN.Labels.SUPPRIMER_BTN);
		btnSupprimer.setIcon(new ImageIcon(ClassLoader.getSystemResource(Params.BASE_PATH + "images/Delete.png")));

		btnQuitter.setText(ConstMessagesEN.Labels.QUITTER_BTN);
		btnQuitter.setIcon(new ImageIcon(ClassLoader.getSystemResource(Params.BASE_PATH + "images/Cancel.png")));

		javax.swing.GroupLayout gl_BtnjPanel = new javax.swing.GroupLayout(BtnjPanel);
		gl_BtnjPanel.setHorizontalGroup(gl_BtnjPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_BtnjPanel.createSequentialGroup().addContainerGap()
						.addComponent(btnAjouter, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(btnSupprimer, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED, 533, Short.MAX_VALUE)
						.addComponent(btnQuitter, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
						.addGap(14)));
		gl_BtnjPanel.setVerticalGroup(gl_BtnjPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_BtnjPanel.createSequentialGroup().addContainerGap(15, Short.MAX_VALUE)
						.addGroup(gl_BtnjPanel.createParallelGroup(Alignment.BASELINE).addComponent(btnAjouter)
								.addComponent(btnSupprimer).addComponent(btnQuitter))
						.addContainerGap()));
		BtnjPanel.setLayout(gl_BtnjPanel);

		getContentPane().add(BtnjPanel, java.awt.BorderLayout.PAGE_END);

		// jPanel2.setBorder(new LineBorder(Color.BLUE, 2, true));
		FormJPanel.setPreferredSize(new Dimension(350, 530));

		jLabel1.setText("Code barre ");

		jLabel2.setText("Conditionnement");

		jLabel3.setText("N°Lot");

		jLabel2_1 = new JLabel();
		jLabel2_1.setBounds(100, 432, 17, 37);
		jLabel2_1.setForeground(Color.RED);
		jLabel2_1.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		jLabel2_1.setText("*");

		jLabel2_2 = new JLabel();
		jLabel2_2.setBounds(36, 481, 17, 37);
		jLabel2_2.setText("*");
		jLabel2_2.setForeground(Color.RED);
		jLabel2_2.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));

		tb_codebarre = new JTextField();
		tb_codebarre.setForeground(new Color(0, 128, 0));
		tb_codebarre.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 17));
		tb_codebarre.setBounds(6, 22, 326, 40);
		tb_codebarre.setBackground(SystemColor.window);
		tb_codebarre.setColumns(10);

		tb_id = new JTextField();
		tb_id.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		tb_id.setForeground(Color.WHITE);
		tb_id.setBounds(336, 30, 8, 26);
		tb_id.setBackground(Color.WHITE);
		tb_id.setEditable(false);
		tb_id.setColumns(10);

		lblNomArticle = new JLabel();
		lblNomArticle.setBackground(Color.DARK_GRAY);
		lblNomArticle.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 13));
		lblNomArticle.setForeground(Color.DARK_GRAY);
		lblNomArticle.setBounds(6, 62, 82, 26);
		lblNomArticle.setText("Désignation");

		jLabel2_4 = new JLabel();
		jLabel2_4.setBounds(77, 68, 17, 16);
		jLabel2_4.setText("*");
		jLabel2_4.setForeground(Color.RED);
		jLabel2_4.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));

		lblForme = new JLabel();
		lblForme.setBackground(Color.GRAY);
		lblForme.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 12));
		lblForme.setForeground(Color.DARK_GRAY);
		lblForme.setBounds(6, 176, 42, 37);
		lblForme.setText("Forme");

		jLabel2_5 = new JLabel();
		jLabel2_5.setBounds(39, 176, 17, 37);
		jLabel2_5.setText("*");
		jLabel2_5.setForeground(Color.RED);
		jLabel2_5.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));

		lblType = new JLabel();
		lblType.setBackground(Color.DARK_GRAY);
		lblType.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 12));
		lblType.setForeground(Color.DARK_GRAY);
		lblType.setBounds(6, 215, 42, 37);
		lblType.setText("Type");

		jLabel2_6 = new JLabel();
		jLabel2_6.setBounds(35, 215, 17, 37);
		jLabel2_6.setText("*");
		jLabel2_6.setForeground(Color.RED);
		jLabel2_6.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));

		lblCatgorie = new JLabel();
		lblCatgorie.setBackground(Color.DARK_GRAY);
		lblCatgorie.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 12));
		lblCatgorie.setForeground(Color.DARK_GRAY);
		lblCatgorie.setBounds(6, 264, 66, 37);
		lblCatgorie.setText("Catégorie");

		jLabel2_7 = new JLabel();
		jLabel2_7.setBounds(58, 264, 17, 37);
		jLabel2_7.setText("*");
		jLabel2_7.setForeground(Color.RED);
		jLabel2_7.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));

		lblRayon = new JLabel();
		lblRayon.setBackground(Color.DARK_GRAY);
		lblRayon.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 12));
		lblRayon.setForeground(Color.DARK_GRAY);
		lblRayon.setBounds(6, 305, 64, 37);
		lblRayon.setText("Rayon");

		jLabel2_8 = new JLabel();
		jLabel2_8.setBounds(43, 354, 27, 16);
		jLabel2_8.setText("*");
		jLabel2_8.setForeground(Color.RED);
		jLabel2_8.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));

		lblDosage = new JLabel();
		lblDosage.setBackground(Color.DARK_GRAY);
		lblDosage.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 12));
		lblDosage.setForeground(Color.DARK_GRAY);
		lblDosage.setBounds(8, 354, 72, 16);
		lblDosage.setText("Dosage");

		jLabel2_9 = new JLabel();
		jLabel2_9.setBounds(41, 305, 17, 37);
		jLabel2_9.setText("*");
		jLabel2_9.setForeground(Color.RED);
		jLabel2_9.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));

		lblQuantit = new JLabel();
		lblQuantit.setBackground(Color.DARK_GRAY);
		lblQuantit.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 12));
		lblQuantit.setForeground(Color.DARK_GRAY);
		lblQuantit.setBounds(6, 531, 74, 16);
		lblQuantit.setText("Quantité ");

		lblPrixAchat = new JLabel();
		lblPrixAchat.setBackground(Color.DARK_GRAY);
		lblPrixAchat.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 12));
		lblPrixAchat.setForeground(Color.DARK_GRAY);
		lblPrixAchat.setBounds(6, 570, 74, 16);
		lblPrixAchat.setText("Prix achat");

		jLabel3_3 = new JLabel();
		jLabel3_3.setBackground(Color.DARK_GRAY);
		jLabel3_3.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 12));
		jLabel3_3.setForeground(Color.DARK_GRAY);
		jLabel3_3.setBounds(6, 607, 74, 16);
		jLabel3_3.setText("Prix vente");

		lblDescription = new JLabel();
		lblDescription.setBackground(Color.DARK_GRAY);
		lblDescription.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 12));
		lblDescription.setForeground(Color.DARK_GRAY);
		lblDescription.setBounds(6, 382, 82, 16);
		lblDescription.setText("Description");

		tb_description = new JTextArea();
		tb_description.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		tb_description.setWrapStyleWord(true);
		tb_description.setLineWrap(true);
		tb_description.setBounds(90, 382, 246, 37);
		tb_description.setBackground(SystemColor.window);

		tb_nomarticle = new JTextField();
		tb_nomarticle.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		tb_nomarticle.setBackground(SystemColor.window);
		tb_nomarticle.setBounds(6, 87, 329, 37);
		tb_nomarticle.setColumns(10);

		
		

		cb_forme = new JComboBox<String>();
		
	
	
	
		cb_forme.setBounds(90, 176, 216, 37);
		cb_forme.setEditable(true);
		cb_forme.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 12));
		editorforme = (JTextField) cb_forme.getEditor().getEditorComponent();
		
		cb_forme.setRenderer(new DefaultListCellRenderer() {
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

		cb_type = new JComboBox<String>();
		cb_type.setBounds(90, 215, 216, 37);
		cb_type.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 12));
		cb_type.setEditable(true);
		editorType = (JTextField) cb_type.getEditor().getEditorComponent();
		cb_type.setRenderer(new DefaultListCellRenderer() {
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

		cb_categorie = new JComboBox<String>();
		cb_categorie.setBounds(90, 264, 216, 37);
		cb_categorie.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 12));
		cb_categorie.setEditable(true);
		editorCategory = (JTextField) cb_categorie.getEditor().getEditorComponent();
		cb_categorie.setRenderer(new DefaultListCellRenderer() {
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

		cb_rayon = new JComboBox<String>();
		cb_rayon.setBounds(90, 305, 216, 37);
		cb_rayon.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 12));
		cb_rayon.setEditable(true);
		editorRayon = (JTextField) cb_rayon.getEditor().getEditorComponent();
		cb_rayon.setRenderer(new DefaultListCellRenderer() {
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

		cb_lot = new JComboBox<String>();
		cb_lot.setBounds(90, 480, 196, 37);
		cb_lot.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 12));
		cb_lot.setEditable(true);
		editorLot = (JTextField) cb_lot.getEditor().getEditorComponent();
		cb_lot.setRenderer(new DefaultListCellRenderer() {
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

		tb_prix_achat = new JTextField();
		tb_prix_achat.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		tb_prix_achat.setBounds(90, 570, 187, 37);
		tb_prix_achat.setBackground(SystemColor.window);
		tb_prix_achat.setColumns(10);

		tb_prix_vente = new JTextField();
		tb_prix_vente.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		tb_prix_vente.setBounds(90, 607, 187, 37);
		tb_prix_vente.setBackground(SystemColor.window);
		tb_prix_vente.setColumns(10);

		tb_dosage = new JTextField();
		tb_dosage.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		tb_dosage.setBounds(90, 344, 206, 37);
		tb_dosage.setBackground(SystemColor.window);
		tb_dosage.setColumns(10);

		getContentPane().add(FormJPanel, java.awt.BorderLayout.LINE_START);
		FormJPanel.setLayout(null);
		FormJPanel.add(jLabel3);
		FormJPanel.add(jLabel2_2);
		FormJPanel.add(lblNomArticle);
		FormJPanel.add(jLabel2_4);
		FormJPanel.add(jLabel1);
		FormJPanel.add(lblRayon);
		FormJPanel.add(jLabel2_8);
		FormJPanel.add(jLabel2);
		FormJPanel.add(jLabel2_1);
		FormJPanel.add(lblDosage);
		FormJPanel.add(jLabel2_9);
		FormJPanel.add(lblDescription);
		FormJPanel.add(lblQuantit);
		FormJPanel.add(lblCatgorie);
		FormJPanel.add(jLabel2_7);
		FormJPanel.add(jLabel2_6);
		FormJPanel.add(lblForme);
		FormJPanel.add(jLabel2_5);
		FormJPanel.add(lblType);
		FormJPanel.add(tb_nomarticle);
		FormJPanel.add(tb_dosage);
		FormJPanel.add(cb_rayon);
		FormJPanel.add(cb_categorie);
		FormJPanel.add(cb_type);
		FormJPanel.add(cb_forme);
		FormJPanel.add(cb_lot);
		FormJPanel.add(tb_prix_achat);
		FormJPanel.add(tb_prix_vente);
		FormJPanel.add(tb_description);
		FormJPanel.add(tb_codebarre);
		FormJPanel.add(tb_id);
		FormJPanel.add(jLabel3_3);
		FormJPanel.add(lblPrixAchat);

		tb_quantite = new JTextField();
		
		tb_quantite.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		tb_quantite.setColumns(10);
		tb_quantite.setBackground(SystemColor.window);
		tb_quantite.setBounds(90, 526, 184, 37);
		FormJPanel.add(tb_quantite);

		cb_packaging = new JComboBox<String>();
		cb_packaging.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 12));
		editorConditionnement = (JTextField) cb_packaging.getEditor().getEditorComponent();
		cb_packaging.setEditable(true);
		cb_packaging.setBounds(110, 431, 176, 37);
		cb_packaging.setRenderer(new DefaultListCellRenderer() {
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

		FormJPanel.add(cb_packaging);

		btnAddLot = new JButton();
		btnAddLot.setForeground(Color.GRAY);

		btnAddLot.setText("...");
		btnAddLot.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		btnAddLot.setBounds(282, 480, 47, 37);
		FormJPanel.add(btnAddLot);

		btnAddForme = new JButton();
		btnAddForme.setText("...");
		btnAddForme.setForeground(Color.GRAY);
		btnAddForme.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		btnAddForme.setBounds(297, 176, 47, 37);
		FormJPanel.add(btnAddForme);

		btnAddType = new JButton();
		btnAddType.setText("...");
		btnAddType.setForeground(Color.GRAY);
		btnAddType.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		btnAddType.setBounds(297, 215, 47, 37);
		FormJPanel.add(btnAddType);

		btnAddCagtegorie = new JButton();
		btnAddCagtegorie.setText("...");
		btnAddCagtegorie.setForeground(Color.GRAY);
		btnAddCagtegorie.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		btnAddCagtegorie.setBounds(297, 264, 47, 37);
		FormJPanel.add(btnAddCagtegorie);

		btnAddRayon = new JButton();
		btnAddRayon.setText("...");
		btnAddRayon.setForeground(Color.GRAY);
		btnAddRayon.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		btnAddRayon.setBounds(297, 305, 47, 37);
		FormJPanel.add(btnAddRayon);

		btnAddPackaging = new JButton();
		btnAddPackaging.setText("...");
		btnAddPackaging.setForeground(Color.GRAY);
		btnAddPackaging.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		btnAddPackaging.setBounds(282, 432, 47, 37);
		FormJPanel.add(btnAddPackaging);

		TablePanel.setLayout(new java.awt.BorderLayout());

		jPanel4.setPreferredSize(new java.awt.Dimension(529, 40));

		jLabel9.setText(ConstMessagesEN.Labels.RECHERCHER);

		javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
		jPanel4Layout.setHorizontalGroup(
			jPanel4Layout.createParallelGroup(Alignment.LEADING)
				.addGroup(jPanel4Layout.createSequentialGroup()
					.addContainerGap()
					.addComponent(jLabel9)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(tb_search, GroupLayout.DEFAULT_SIZE, 962, Short.MAX_VALUE)
					.addContainerGap())
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

		TablePanel.add(jPanel4, java.awt.BorderLayout.PAGE_END);

		tablejScrollPane.setViewportView(table);
		TablePanel.add(tablejScrollPane, java.awt.BorderLayout.CENTER);

		getContentPane().add(TablePanel, java.awt.BorderLayout.CENTER);

		pack();
	}

	public Article getArticleFromForm() {
		Article article = new Article();
		/*article.setArticleCodeBarre(tb_codebarre.getText().trim().toString());
		article.setArticleName(tb_nomarticle.getText().trim().toString());
		article.setArticleDescription(tb_description.getText().toString());
		article.setArticleDosage(tb_dosage.getText().toString());

		if (tb_quantite.getText().trim().toString() != Strings.EMPTY)
			article.setArticleQuantite_stock(Integer.parseInt(tb_quantite.getText()));
		else
			article.setArticleQuantite_stock(Integer.parseInt("0"));

		if (tb_prix_achat.getText().trim().toString() != Strings.EMPTY)
			article.setArticlePrixAchat(Integer.parseInt(tb_prix_achat.getText()));
		else
			article.setArticlePrixAchat(Integer.parseInt("0"));

		if (tb_prix_vente.getText().trim().toString() != Strings.EMPTY)
			article.setArticlePrixVente(Integer.parseInt(tb_prix_vente.getText()));
		else
			article.setArticlePrixVente(Integer.parseInt("0"));

		if (!tb_id.getText().trim().equalsIgnoreCase(Strings.EMPTY))
			article.setId(Long.parseLong(tb_id.getText().trim().toString()));*/

		return article;

	}

	public void clearForm() {

		/*
		 * for (Component c : this.getComponents()) if (c instanceof JTextField) {
		 * 
		 * if (((JTextField)
		 * c).getClass().getSimpleName().toLowerCase().equalsIgnoreCase("tb_quantite")
		 * || ((JTextField)
		 * c).getClass().getSimpleName().toLowerCase().equalsIgnoreCase("tb_prix_vente")
		 * || ((JTextField)
		 * c).getClass().getSimpleName().toLowerCase().equalsIgnoreCase("tb_prix_achat")
		 * )
		 * 
		 * ((JTextField) c).setText("0");
		 * 
		 * else ((JTextField) c).setText(Strings.EMPTY); }
		 */

		tb_id.setText(Strings.EMPTY);
		tb_nomarticle.setText(Strings.EMPTY);
		tb_dosage.setText(Strings.EMPTY);
		tb_description.setText(Strings.EMPTY);
		tb_quantite.setText("0");
		tb_prix_achat.setText("0");
		tb_prix_vente.setText("0");

	}

}
