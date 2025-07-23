package com.officine.losto.ui.forms.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;

import org.springframework.stereotype.Controller;

import com.officine.losto.backend.entity.Categorie;
import com.officine.losto.backend.entity.Forme;
import com.officine.losto.ui.forms.ArticleFrame;
import com.officine.losto.ui.forms.CategorieFrame;
import com.officine.losto.ui.forms.DonneesDeBaseFrame;
import com.officine.losto.ui.forms.FormeFrame;
import com.officine.losto.ui.forms.FournisseurFrame;
import com.officine.losto.ui.forms.MenuFrame;
import com.officine.losto.ui.forms.PackagingFrame;
import com.officine.losto.ui.forms.RayonFrame;
import com.officine.losto.ui.forms.SeuilFrame;
import com.officine.losto.ui.forms.TypeFrame;
import com.officine.losto.ui.shared.controller.AbstractFrameController;
import com.officine.losto.uti.shared.Shared;
import com.officine.losto.uti.shared.ctrl;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor

public class DonneesDeBaseController extends AbstractFrameController {

	private DonneesDeBaseFrame frame;
	private FormeFrame formeframe;
	private FormeController formeController;

	private FournisseurFrame fournisseurFrame;
	private FournisseurController fournisseurController;

	private PackagingFrame packagingFrame;
	private PackagingController packagingController;

	private RayonFrame rayonFrame;
	private RayonController rayonController;

	private TypeFrame typeFrame;
	private TyppeController typpeController;

	private CategorieFrame categorieFrame;
	private CategorieController categorieController;

	private SeuilFrame seuilFrame;
	private SeuilController seuilController;

	public void DonneesDeBaseController() {
	}

	private void prepareListeners(DonneesDeBaseFrame frame) {

	}

	@Override
	public void prepareAndOpenFrame(JDialog parent) {
		showFrame();

	}

	private void showFrame() {
		frame = new DonneesDeBaseFrame(
				formeframe, formeController, fournisseurFrame, fournisseurController,
				packagingFrame, packagingController, rayonFrame, rayonController,
			    typeFrame, typpeController,categorieFrame, categorieController,
			    seuilFrame, seuilController
			    );

		prepareListeners(frame);

		if (Shared.ctrl.DonneesDeBase == null || !Shared.ctrl.DonneesDeBase.isVisible()) {
			Shared.ctrl = new ctrl();
			Shared.ctrl.DonneesDeBase = frame;
		}
		// ajouter les

		// frame.getTabPanel().setTabComponentAt(0, formeframe.getContentPane());

		Shared.displayFrame(frame);

		/*
		 * JTabbedPane tabPanel = new JTabbedPane(); // Create the first tab (page1) and
		 * add a JLabel to it JInternalFrame internalFrame1 = new
		 * JInternalFrame("CREATION DES FORMES", true, true, true, true);
		 * internalFrame1.setSize(500, 500); internalFrame1.setVisible(true);
		 * 
		 * // Create the third tab (page3) and add a JLabel to it JPanel page3 = new
		 * JPanel(); page3.add(new JLabel("This is Tab 3"));
		 * 
		 * 
		 * // Add the JTabbedPane to the JFrame's content //this.add(tabPanel);
		 */

	}

	public void closeModalWindow() {
		System.out.println("private void closeModalWindow()");
		frame.setVisible(false);
		frame.dispose();

	}

}
