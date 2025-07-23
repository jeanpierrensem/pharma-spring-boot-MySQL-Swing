package com.officine.losto.ui.forms;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import org.springframework.stereotype.Component;

import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.params.constant.ConstMessagesEN.Params;
import com.officine.losto.uti.shared.ctrl;

import lombok.Getter;

@Component
@Getter
public class MainMenuFrame extends JFrame {

	public static ctrl ctrl = null;
	private static final long serialVersionUID = 1L;

	public JPanel contentPane;
	private JMenu mnPara;
	private JMenu mnTraitement;
	private JMenu mnTrai;
	private JMenu mnRech;
	private JMenu mnAid;
	private JMenuItem mntmMen;
	private JMenuItem mntmPieceId;
	private JMenuItem mntmTypPiece;
	private JMenuItem mntmPres;
	private JMenuItem mntmTypPres;
	private JMenuItem mntmCon;
	private JMenuItem mntmEtFac;
	private JMenuItem mntmMotif;
	private JMenuItem mntmTypCon;
	private JMenuItem mntmDom;
	private JMenuItem mntmEta;
	private JMenuItem mntmIti;
	private JMenuItem mntmBanPas;
	private JMenuItem mntmPosTra;
	private JMenuItem mntmProDeDec;
	private JMenuItem mntmTypTra;
	private JMenuItem mntmTitrePai;
	private JMenuItem mntmTypeTitrePaiement;
	private JMenuItem mntmTax;
	private JMenuItem mntmBanq;
	private JMenuItem mntmCom;
	private JMenuItem mntmReg;
	private JMenuItem mntmVil;
	private JMenu mnRechFac;
	private JMenuItem mntmImp;
	private JMenuItem mntmExp;
	private JMenuItem mntmTiPay;
	private JMenuItem mntmProDec;
	private JMenuItem mntmRechFac;
	private JMenuItem mntmRechTitrePay;
	private JMenuItem mntmRechEtaFac;
	private JMenuItem mntmAPropos;
	private JMenuItem mntmDconnexion;
	private JMenuItem mntmFacture;
	private JSeparator separator_PosTra;
	private JMenuItem mntmValidation;
	private JMenuItem mntmAide;
	private JMenu mnEtats;
	private JSeparator separator_31;
	private JSeparator separator_32;
	private JMenuItem mntmNewMenuItem;
	private JMenuItem mntmFactures;
	private JMenuItem mntmBordereauDeTransmission;
	private JSeparator separator_Validation;
	private JSeparator separator_Ville;
	private JSeparator separator_27;
	private JSeparator separator_Reg;
	private JLabel lblWelcome;
	private JMenuItem mntmBordereauDesVirements;
	private JMenuItem mntmOrdresDeTransfert;
	private JMenu mnAvantProjetsDe;
	private JMenuItem mntmEtatDesFactures;
	private JMenuItem mntmEtatDesFactures_1;
	private JMenuItem mntmEtatsDesFacture;
	private JMenuItem mntmEtatsDesFactures;
	private JMenuItem mntmEtatsDesFactures_1;
	private JMenuItem mntmEtatDesFactures_2;
	private JMenuItem mntmEtatReceptionDes;
	private JMenuItem mntmFacturesEnCirculation;
	private JMenuItem mntmEtatDesConsomations;
	private JPanel wellcomePanel;
	private JSeparator separator_3;
	private JSeparator separator_1;
	private JSeparator separator_2;
	private JLabel lb_welcome;
	private JMenuItem mntmChagerMotDe;
	private JMenuItem mntmJournal;
	private JSeparator separator_12;
	private JMenu mnUtilisateursGroupes;
	private JMenuItem mntmUtilisateurs;
	private JMenuItem mntmGroupes;
	private JMenuItem mntmRoles;
	private JMenuItem mntmProduit;
	private JMenuItem mntmCommande;
	private JSeparator separator_14;
	private JMenuItem mntmReception;
	private JMenuItem mntmQuitter;
	private JSeparator separator_8;
	private JSeparator separator_13;
	private JSeparator separator_15;
	private Image img;
	private JMenuItem mntmMenu;
	private JLabel lbl_bg;
	private JLabel lbl_bg2;
	private JMenu mnAchat;
	private JSeparator separator_10;
	private JMenuItem mntnMiseRayon;
	private JSeparator separator_17;
	private JMenuItem mntmVente;
	private JSeparator separator_18;
	private JMenuItem mntmRappel;
	private JSeparator separator_19;
	private JMenuItem mntmPeremption;
	private JSeparator separator_20;
	private JMenu mnVentePeriode;
	private JSeparator separator_21;
	private JMenuItem mntmSortie_2;
	private JSeparator separator_22;
	private JMenuItem mntmProduitPlusVendus;
	private JSeparator separator_23;
	private JMenuItem mntmRentabilités;
	private JSeparator separator_24;
	private JMenuItem mntmPerte;
	private JSeparator separator_25;
	private JMenuItem mntmSuiviLot;
	private JMenu mnVenteIAAnalytics;
	private JSeparator separator_5;
	private JMenuItem mntmSortie;
	private JSeparator separator_11;
	private JMenuItem mntmRassortAutomatique;
	private JSeparator separator_35;
	private JMenuItem mntmalertesPersonnalises;
	private JSeparator separator_37;
	private JMenuItem mntnEntree;
	private JSeparator separator_36;
	private JMenuItem mntmDonneesBase;

	public MainMenuFrame() {
		setResizable(false);
		initComponents();
		setFrameUp();
	}

	private void setFrameUp() {

	}

	public void initComponents() {
		setTitle(" Bienvenu -  LOSTO");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, 19));
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(ClassLoader.getSystemResource(Params.BASE_PATH + "images/Connecte.png")));
		JMenuBar menuBar = new JMenuBar();
		menuBar.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 11));
		setJMenuBar(menuBar);

		mnPara = new JMenu("REFERENTIEL");

		mnPara.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, 13));
		menuBar.add(mnPara);

		mntmQuitter = new JMenuItem("Quitter");
		mntmQuitter.setIcon(new ImageIcon(ClassLoader.getSystemResource(Params.BASE_PATH + "images/Erase.png")));
		mntmDonneesBase = new JMenuItem("Données De Base");
		mntmDonneesBase.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 12));
		mnPara.add(mntmDonneesBase);

		separator_3 = new JSeparator();
		separator_3.setForeground(Color.LIGHT_GRAY);
		mnPara.add(separator_3);

		mntmChagerMotDe = new JMenuItem("Change Mot De Passe");
		mntmChagerMotDe.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 12));

		mnUtilisateursGroupes = new JMenu("Sécurité");
		mnUtilisateursGroupes.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 12));
		mnPara.add(mnUtilisateursGroupes);

		mntmUtilisateurs = new JMenuItem("Utilisateurs");
		mntmUtilisateurs.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 13));
		mntmUtilisateurs.setIcon(new ImageIcon(ClassLoader.getSystemResource(Params.BASE_PATH + "images/user.png")));

		mnUtilisateursGroupes.add(mntmUtilisateurs);

		separator_8 = new JSeparator();
		mnUtilisateursGroupes.add(separator_8);

		mntmGroupes = new JMenuItem("Groupes");
		mntmGroupes.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 13));
		mntmGroupes.setIcon(new ImageIcon(ClassLoader.getSystemResource(Params.BASE_PATH + "images/group.png")));
		mnUtilisateursGroupes.add(mntmGroupes);

		separator_13 = new JSeparator();
		mnUtilisateursGroupes.add(separator_13);

		mntmRoles = new JMenuItem("Habilitation");
		mntmRoles.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 13));
		mntmRoles.setIcon(new ImageIcon(ClassLoader.getSystemResource(Params.BASE_PATH + "images/role.png")));
		mnUtilisateursGroupes.add(mntmRoles);

		separator_15 = new JSeparator();
		mnUtilisateursGroupes.add(separator_15);

		mntmMenu = new JMenuItem("Rôle");
		mntmMenu.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 13));
		mntmMenu.setIcon(new ImageIcon(ClassLoader.getSystemResource(Params.BASE_PATH + "images/menu.png")));
		mnUtilisateursGroupes.add(mntmMenu);

		separator_12 = new JSeparator();
		separator_12.setForeground(Color.LIGHT_GRAY);
		mnPara.add(separator_12);
		mnPara.add(mntmChagerMotDe);

		JSeparator separator_4 = new JSeparator();
		separator_4.setForeground(Color.LIGHT_GRAY);
		mnPara.add(separator_4);

		mntmJournal = new JMenuItem("Journal");
		mntmJournal.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 12));

		mnPara.add(mntmJournal);

		JSeparator separator_6 = new JSeparator();
		separator_6.setForeground(Color.LIGHT_GRAY);
		mnPara.add(separator_6);
		mnPara.add(mntmQuitter);

		mnTraitement = new JMenu("APPROVISIONNEMENT");
		mnTraitement.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, 13));
		menuBar.add(mnTraitement);

		separator_2 = new JSeparator();
		separator_2.setForeground(Color.LIGHT_GRAY);
		mnTraitement.add(separator_2);

		mntmProduit = new JMenuItem("Produit");
		mntmProduit.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 13));
		mntmProduit.setIcon(new ImageIcon(ClassLoader.getSystemResource(Params.BASE_PATH + "images/light3.png")));

		mnTraitement.add(mntmProduit);

		separator_14 = new JSeparator();
		separator_14.setForeground(Color.LIGHT_GRAY);
		mnTraitement.add(separator_14);

		mntmCommande = new JMenuItem("Commande");
		mntmCommande.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 13));
		mnTraitement.add(mntmCommande);

		separator_1 = new JSeparator();
		separator_1.setForeground(Color.LIGHT_GRAY);
		mnTraitement.add(separator_1);

		mntmReception = new JMenuItem("Réception & Stcokage");

		mntmReception.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 13));
		mnTraitement.add(mntmReception);

		JSeparator separator_7 = new JSeparator();
		separator_7.setForeground(Color.LIGHT_GRAY);
		mnTraitement.add(separator_7);

		mntmMen = new JMenuItem("REGIONS");

		mnAchat = new JMenu("STOCK");
		mnAchat.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, 13));
		menuBar.add(mnAchat);

		separator_10 = new JSeparator();
		separator_10.setForeground(Color.LIGHT_GRAY);
		mnAchat.add(separator_10);

		mntnEntree = new JMenuItem("Entrée");
		mntnEntree.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 13));
		mnAchat.add(mntnEntree);

		separator_36 = new JSeparator();
		separator_36.setForeground(Color.LIGHT_GRAY);
		mnAchat.add(separator_36);

		mntnMiseRayon = new JMenuItem("Mise en Rayon");
		mntnMiseRayon.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 13));
		mnAchat.add(mntnMiseRayon);

		separator_17 = new JSeparator();
		separator_17.setForeground(Color.LIGHT_GRAY);
		mnAchat.add(separator_17);

		mntmVente = new JMenuItem("Vente");
		mntmVente.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 13));
		mnAchat.add(mntmVente);

		separator_18 = new JSeparator();
		separator_18.setForeground(Color.LIGHT_GRAY);
		mnAchat.add(separator_18);

		mntmRappel = new JMenuItem("Rappel");
		mntmRappel.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 13));
		mnAchat.add(mntmRappel);

		separator_19 = new JSeparator();
		separator_19.setForeground(Color.LIGHT_GRAY);
		mnAchat.add(separator_19);

		mntmPeremption = new JMenuItem("Péremption");
		mntmPeremption.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 13));
		mnAchat.add(mntmPeremption);

		separator_20 = new JSeparator();
		separator_20.setForeground(Color.LIGHT_GRAY);
		mnAchat.add(separator_20);

		mntmSuiviLot = new JMenuItem("Suivi Lot");
		mntmSuiviLot.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 13));
		mnAchat.add(mntmSuiviLot);

		mnVenteIAAnalytics = new JMenu("IA & ANALYTIQUE");
		mnVenteIAAnalytics.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, 13));
		menuBar.add(mnVenteIAAnalytics);

		separator_5 = new JSeparator();
		separator_5.setForeground(Color.LIGHT_GRAY);
		mnVenteIAAnalytics.add(separator_5);

		mntmSortie = new JMenuItem("Prévision Rupture");
		mntmSortie.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 13));
		mnVenteIAAnalytics.add(mntmSortie);

		separator_11 = new JSeparator();
		separator_11.setForeground(Color.LIGHT_GRAY);
		mnVenteIAAnalytics.add(separator_11);

		mntmRassortAutomatique = new JMenuItem("Analyse de Rentabilité");
		mntmRassortAutomatique.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 13));
		mnVenteIAAnalytics.add(mntmRassortAutomatique);

		separator_35 = new JSeparator();
		separator_35.setForeground(Color.LIGHT_GRAY);
		mnVenteIAAnalytics.add(separator_35);

		mntmalertesPersonnalises = new JMenuItem("⁠Alertes personnalisées");
		mntmalertesPersonnalises.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 13));
		mnVenteIAAnalytics.add(mntmalertesPersonnalises);

		separator_37 = new JSeparator();
		separator_37.setForeground(Color.LIGHT_GRAY);
		mnVenteIAAnalytics.add(separator_37);

		mnVentePeriode = new JMenu("STATISTIQUES");
		mnVentePeriode.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, 13));
		menuBar.add(mnVentePeriode);

		separator_21 = new JSeparator();
		separator_21.setForeground(Color.LIGHT_GRAY);
		mnVentePeriode.add(separator_21);

		mntmSortie_2 = new JMenuItem("Vente par Période");
		mntmSortie_2.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 13));
		mnVentePeriode.add(mntmSortie_2);

		separator_22 = new JSeparator();
		separator_22.setForeground(Color.LIGHT_GRAY);
		mnVentePeriode.add(separator_22);

		mntmProduitPlusVendus = new JMenuItem("Produits les plus vendus");
		mntmProduitPlusVendus.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 13));
		mnVentePeriode.add(mntmProduitPlusVendus);

		separator_23 = new JSeparator();
		separator_23.setForeground(Color.LIGHT_GRAY);
		mnVentePeriode.add(separator_23);

		mntmRentabilités = new JMenuItem("Rentabilité");
		mntmRentabilités.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 13));
		mnVentePeriode.add(mntmRentabilités);

		separator_24 = new JSeparator();
		separator_24.setForeground(Color.LIGHT_GRAY);
		mnVentePeriode.add(separator_24);

		mntmPerte = new JMenuItem("Perte");
		mntmPerte.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.PLAIN, 13));
		mnVentePeriode.add(mntmPerte);

		separator_25 = new JSeparator();
		separator_25.setForeground(Color.LIGHT_GRAY);
		mnVentePeriode.add(separator_25);

		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.menu);

		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);

		contentPane.setLayout(null);

		wellcomePanel = new JPanel();
		wellcomePanel.setSize(
				new Dimension(1395, 900));

		wellcomePanel.setBackground(SystemColor.window);
		wellcomePanel.setBounds(0, 0, ConstMessagesEN.Params.DEFAULT_WIDTH * 2,
				ConstMessagesEN.Params.DEFAULT_HEIGHT * 2);
		contentPane.add(wellcomePanel);
		wellcomePanel.setLayout(null);
		

		lbl_bg = new JLabel("");
		lbl_bg.setIcon(new ImageIcon(ClassLoader.getSystemResource(Params.BASE_PATH + "images/arriereplan5.png")));
		//lbl_bg.setIcon(new ImageIcon(ClassLoader.getSystemResource(Params.BASE_PATH + "images/artiom-vallat-tYoMpP0tyEk-unsplash.jpg")));
		
		lbl_bg.setBounds(0, 0, 1395 , 900 );
		
		wellcomePanel.add(lbl_bg);

	}
}
