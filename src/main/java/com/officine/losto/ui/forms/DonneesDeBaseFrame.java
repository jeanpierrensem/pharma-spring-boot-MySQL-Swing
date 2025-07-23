package com.officine.losto.ui.forms;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.springframework.stereotype.Component;

import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.ui.forms.controller.CategorieController;
import com.officine.losto.ui.forms.controller.FormeController;
import com.officine.losto.ui.forms.controller.FournisseurController;
import com.officine.losto.ui.forms.controller.PackagingController;
import com.officine.losto.ui.forms.controller.RayonController;
import com.officine.losto.ui.forms.controller.SeuilController;
import com.officine.losto.ui.forms.controller.TyppeController;
import com.officine.losto.uti.shared.Shared;

import lombok.Data;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


@Component
public class DonneesDeBaseFrame extends JDialog {

	private static final long serialVersionUID = 1L;
	JTabbedPane tabPanel = new JTabbedPane();
	private FormeFrame formeframe;
	private FormeController formeController;
	
	private  FournisseurFrame fournisseurFrame  ;
	private  FournisseurController fournisseurController ; 
	
	private PackagingFrame packagingFrame    ;
	private  PackagingController packagingController; 
	
	private RayonFrame rayonFrame     ;
	private  RayonController rayonController; 
	
	
	private TypeFrame typeFrame; 
	private TyppeController typpeController ; 
	
	private CategorieFrame categorieFrame; 
	private CategorieController categorieController ; 
	
	private SeuilFrame seuilFrame ; 
	private SeuilController seuilController; 

	
	
	
	public DonneesDeBaseFrame(
			FormeFrame formeFrame, FormeController formeController, 
			FournisseurFrame fournisseurFrame , FournisseurController fournisseurController, 
			PackagingFrame packagingFrame , PackagingController packagingController, 
			RayonFrame rayonFrame   ,  RayonController rayonController, 
			TypeFrame typeFrame, TyppeController typpeController, 
			CategorieFrame categorieFrame, CategorieController categorieController , 
			SeuilFrame seuilFrame , SeuilController seuilController
			
			) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				Shared.DONNEES_BASE_CTRL = true; 
			}
			@Override
			public void windowClosed(WindowEvent e) {
				Shared.DONNEES_BASE_CTRL = false; 
			}
		});
		
		this.formeframe = formeFrame;
		this.formeController = formeController;
		
		this.fournisseurFrame = fournisseurFrame ; 
		this.fournisseurController = fournisseurController; 
		
		this.packagingFrame = packagingFrame ; 
		this.packagingController = packagingController; 
		
		this.rayonFrame = rayonFrame ; 
		this.rayonController = rayonController; 
		
		this.typeFrame = typeFrame; 
		this.typpeController = typpeController; 
		
		this.categorieFrame = categorieFrame ; 
		this.categorieController = categorieController; 
		
		this.seuilFrame = seuilFrame ; 
		this.seuilController = seuilController; 
		
		
		
		initCompoenets();

	}

	public void initCompoenets() {
		setTitle(ConstMessagesEN.DialogTitles.DONNEES_DE_BASE);
		setResizable(false);
		setSize(new Dimension(800, 450 ));
		setLocationRelativeTo(null);
		setUndecorated(false);

		formeController.prepareListeners(formeframe);
		formeController.loadCategories(); 
		tabPanel.addTab(ConstMessagesEN.Labels.FORME, formeframe.getContentPane());
		tabPanel.setBackground(Color.WHITE);
	
		tabPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        tabPanel.setFont(new Font(ConstMessagesEN.Params.POLICE_TYPE, Font.BOLD, ConstMessagesEN.Labels.POLICE_SIZE));
        tabPanel.setBackground(Color.WHITE);
        tabPanel.setTabPlacement(JTabbedPane.LEFT);
		
		fournisseurController.prepareListeners(fournisseurFrame);
		fournisseurController.loadFournisseurs(); 
		tabPanel.addTab(ConstMessagesEN.Labels.FOURNISSEUR, fournisseurFrame.getContentPane());
		tabPanel.setToolTipText("Données de base");

		packagingController.prepareListeners(packagingFrame);
		packagingController.load(); 
		tabPanel.addTab(ConstMessagesEN.Labels.CONDIONNEMENT, packagingFrame.getContentPane());

		
		rayonController.prepareListeners(rayonFrame);
		rayonController.load(); 
		tabPanel.addTab(ConstMessagesEN.Labels.RAYON, rayonFrame.getContentPane());

		
		typpeController.prepareListeners(typeFrame);
		typpeController.load(); 
		tabPanel.addTab(ConstMessagesEN.Labels.TYPE, typeFrame.getContentPane());
		
		categorieController.prepareListeners(categorieFrame);
		categorieController.load(); 
		tabPanel.addTab(ConstMessagesEN.Labels.CATEGORIE, categorieFrame.getContentPane());
			
		seuilController.prepareListeners(seuilFrame);
		seuilController.load(); 
		tabPanel.addTab("Seuils", seuilFrame.getContentPane());
		
		/*tabPanel.setBackgroundAt(0, Color.GREEN);
		tabPanel.setBackgroundAt(1, Color.WHITE);
		tabPanel.setBackgroundAt(2, Color.GREEN);
		tabPanel.setBackgroundAt(3, Color.WHITE);
		tabPanel.setBackgroundAt(4, Color.GREEN);
		tabPanel.setBackgroundAt(5, Color.WHITE);
		tabPanel.setBackgroundAt(6, Color.GREEN);*/
		
		tabPanel.setBackgroundAt(0, new Color(220, 240, 255));
		tabPanel.setBackgroundAt(1, new Color(220, 240, 255));
		tabPanel.setBackgroundAt(2, new Color(220, 240, 255));
		tabPanel.setBackgroundAt(3, new Color(220, 240, 255));
		tabPanel.setBackgroundAt(4, new Color(220, 240, 255));
		tabPanel.setBackgroundAt(5, new Color(220, 240, 255));
		tabPanel.setBackgroundAt(6, new Color(220, 240, 255));
		
		
		
		
		
		getContentPane().add(tabPanel);

		
	}

}
