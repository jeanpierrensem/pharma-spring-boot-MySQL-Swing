package com.officine.losto.params.constant;

import java.awt.Color;
import java.awt.SystemColor;

public class ConstMessagesEN {
	public static class DialogTitles {
		public static String PRESCRIPTEUR_MODAL = "PRESCRIPTEUR";
		public static String FOURNISSEUR_MODAL = "Fournisseurs";
		public static String COMMANDE_MODAL = "COMMANDES";
		public static String RECEPTION_MODAL = "RECEPTION DE COMMANDES";
		public static String MENU_MODAL = "MENU";
		public static String LOT_MODAL = "LOT";
		public static String RAYON_MODAL = "RAYON";
		public static String CONDITIONNEMENT_MODAL = "CONDITIONNEMENT";
		public static String CATEGORIE_MODAL = "CATEGORIE";
		public static String FORME_MODAL = "FORME";
		public static String TYPE_MODAL = "TYPES";
		public static String SEUIL_MODAL = "SEUIL";
		public static String JOURNAL_MODAL = "JOURNALs";
		public static String CHANGER_MOT_PASSE_MODAL = "MOT DE PASSE";
		public static String LOGING = "Officine Connexion ";
		public static String OFFICINE = " Bienvenu -  LOSTO Officine";
		public static String ROLE = " Gestion Des Roles";
		public static String GROUPE = " Gestion Des Groupes Utilisateurs";
		public static String MENU = " Gestion Des Menus";
		public static String PERSONNEL = " Gestion Du Personnel ";
		public static String ARTICLE = " Enregistrement Des Articles ";
		public static String DONNEES_DE_BASE = " Données De Base";
	}

public static class Messages {
		public static String WINDOWS_STYLE_LOADING_ERROR_MESSAGE = "There was an error while loading "
				+ "windows look an feel: ";
		public static String ALERT_TILE = "Alert";
		public static String LOGIN_ERROR = "Login ou Mote de passe Non valide";
		public static String NON_ROW_SELECTED = "Aucune Ligne Sélectionnée";
		public static String INFORMATION_TITLE = "Information";
		public static String SUCCESS_MESSAGE = "Enrgistrement effectué";
		public static String MODIF_SUCCESS_MESSAGE = "Modification effectuée avec Succès";
		public static String DELETE_ROW_ERROR = "La ligne ne . Check if there are any connections " + "between tables.";
		public static String CONFIRM_MESSAGE = "Supprimer cet élément ? ";
	}

	public static class Labels {
		public static String CODE = "CODE";

		public static String STATUT = "STATUT";
		public static String QTESTOCK = "QTE STOCK";
		
		public static String VENTE = "Enregistrement Des Ventes";
		public static String DELTA = "DELTA";
		public static String STATUT_RECEPTION = "STATUT RECEPTION";

		public static String VALEUR_SEUIL = "VALEUR SEUIL";

		public static String NOM_FOURNISSEUR = "NOM FOURNISSEUR";
		public static String ADRESSE_FOURNISSEUR = "ADRESSE FOURNISSEUR";

		public static String NUMERO_LOT = "N° LOT";
		public static String ID = "ID";
		public static String LIBELLE = "LIBELLE";
		public static String DATE_PEREMPTION = "DATE PEREMPTION";
		public static String QUANTITE = "QUANTITE";
		public static String FOURNISSEUR = "Fournisseur";
		
		public static String  DESIGNATION ="DESIGNATION";

		public static String DESCRIPTION = "DESCRIPTION";
		public static String NOUVELLE_SAISIE = "Nouvelle Saisie";
		public static String LISTE = "Liste des ";
		public static String RECHERCHER = " Rechercher";
		public static String ENREGISTRER_BTN = "Enregistrer";
		public static String SUPPRIMER_BTN = "Supprimer";
		public static String QUITTER_BTN = "Quitter";
		public static String OUVRIR_BTN = "Ouvrir";
		public static String NOM = "N";
		public static String PRENOM = "Prenom";
		public static String MOT_DE_PASSE = "Mot de Passe";
		public static String LOSTO_VERSION = "LOSTO. v0";

		public static String EMAIL = "E-mail";
		public static String ADDRESS = "Address";

		public static final String GROUPE_CODE = "CODE GROUPE";
		public static final String GROUPE_NAME = "NOM GROUPE";
		public static final String GROUPE_DESCRIPTION = "DESCRIPTION GROUPE";

		public static final String MENU_ID = "ID";
		public static final String MENU_NAME = "ECRAN";
		public static final String MENU_DESCRIPTION = "DESCRIPTION";

		public static final String MATRICULE = "MATRICULE";
		public static final String GROUPE = "GROUPE";
		public static final String LOGIN = "LOGIN";

		public static final String REFERENCE = "REFERENCE";
		public static final String PRIX_UNITAIRE_HT = "Prix U.";
		public static final String PRIX_TOTAL_HT = "PRIX T.(TTC)";
		public static final String REMISE = "REMISE(%)";
		public static final String ARTICLE = "ARTICLE";
		public static final String NAME = "NOM ARTICLE";
		public static final String CODEBARRE = "CODE BARRE";
		public static final String FORME = "Formes";
		public static final String TYPE = "Types";
		public static final String CATEGORIE = "Catégories";
		public static final String RAYON = "Rayon";
		public static final String DOSAGE = "DOSAGE";
		public static final String CONDIONNEMENT = "Conditionnement";
		public static final String LOT = "LOT";
		public static final String NUMCOMMANDE = "N°COMMANDE";
		public static final String DATE = "DATE";
		public static final String COMMANDER_PAR = "COMMANDE PAR";
		public static final String MODELIVRAISON = "MODE LIVRAISON";
		public static final String INDICATION = "INDICATION";
		public static final String QUANTITE_COMMANDEE = "QTE COMMANDEE";
		public static final String QUANTITE_A_RECEVOIR = "DELTA";
		public static final String NUM_LOT = "N°LOT";
		public static final String QUANTITE_RECUE = "QTE RECUE";
		public static final String OBSERVATION = "OBSERVATION";
		public static final String PRIX_ACHAT = "PRIX ACHAT";
		public static final String PRIX_VENTE = "PRIX VENTE";
		public static final String AFFICHER = "Consulter";
		public static final String ENREGISTRER = "Création";
		public static final String MODIFIER = "Modification";
		public static final String SUPPRIMER = "Suppression";
		public static final String IMPRIMER = "Impression";
		public static final int POLICE_SIZE = 11;
		public static final Color HEADER_COLOR_SUB_TABLE =  Color.lightGray;
		public static final Color HEADER_COLOR_TABLE = SystemColor.window;
		public static final Color HEADER_COLOR_ALl = Color.decode("#ccffcc");
		
		
		public static String[] columns = { ConstMessagesEN.Labels.MENU_ID, ConstMessagesEN.Labels.MENU_NAME,
				ConstMessagesEN.Labels.MENU_DESCRIPTION, ConstMessagesEN.Labels.AFFICHER,
				ConstMessagesEN.Labels.ENREGISTRER, ConstMessagesEN.Labels.MODIFIER, ConstMessagesEN.Labels.SUPPRIMER,
				ConstMessagesEN.Labels.IMPRIMER };
		
		
		public static final String NUMVENTE = "N°VENTE";   
		public static final String VENDEUR = "VENDEUR";
		public static final String CLIENT = "CLIENT";
		public static final String TYPEVENTE = "TYPE VENTE"; 
		public static final String MODEPAIEMENT = "MODE PAIE.";
		public static final String MONTANTPAYE = "Mnt PAYE";
		public static final String MONTANTRENDU = "Mnt RENDU";
		public static final String REMARQUE = "REMARQUE";
	}

	public static class Params {
		// BOOT-INF/classes/ - for Prod and empty for dev
		public static String BASE_PATH = "";
		public static String USER_DIR = "user.dir";
		public static int DEFAULT_WIDTH = 700;
		public static int DEFAULT_HEIGHT = 500;
		public static int GROUPE_NB_COLUMNS = 8;
		public static String DATE_FORMAT = "dd/MM/yyyy";
		public static String ReportFilePath = 
				"//Users//nsemjean//eclipse-workspace-202209-WB112//officine//src/main//resources//reports//"; 
		public static int MIN = 1;
		public static int MAX = 9;	
		public static String ENCODING_ALGORITHM = "MD5"; 
		public static final String POLICE_TYPE = "Segoe UI";
	}

	public static class ValidationMessages {
		public static String REQUIRED_DATA_NOT_FILLED_OR_BAD_DATA = "Valeur de Champ manquant, Merci de renseigner les Champs Obligatoire";
		public static String PESEL_LENGTH_INCORRECT = "PESEL should contain 11 characters.";
		public static String DATE_FROM_MUST_BE_EARLIER_THAN_TO_DATE = "Date from must be earlier than date to";
	}

}
