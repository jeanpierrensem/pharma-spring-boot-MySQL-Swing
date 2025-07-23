package com.officine.losto.ui.forms.controller;

import javax.swing.JDialog;

import org.springframework.stereotype.Controller;

import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.ui.forms.FormeFrame;
import com.officine.losto.ui.forms.MainMenuFrame;
import com.officine.losto.ui.forms.model.FormeTableModel;
import com.officine.losto.ui.shared.controller.AbstractFrameController;
import com.officine.losto.uti.shared.Shared;
import com.officine.losto.uti.shared.ctrl;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class MainMenuController extends AbstractFrameController {
	private final MainMenuFrame mainMenuFrame;
	private final RoleController roleController;
	private final GroupeController groupeController;
	private final MenuController menuController;
	private final PersonnelController personnelController;
	private final LotController lotController;
	private final ArticleController articleController;
	private final CommandeController commandeController;
	private final ReceptionController receptionController;
	private final DonneesDeBaseController donneesDeBaseController;
	private final VenteController venteController;
	private  final FormeTableModel tableModel ; 
	private final FormeController formeController ; 

	@SuppressWarnings("static-access")
	@Override
	public void prepareAndOpenFrame(JDialog parent) {
		if (Shared.ctrl.mainMenu == null || !Shared.ctrl.mainMenu.isVisible()) {
			Shared.ctrl = new ctrl();
			Shared.ctrl.mainMenu = new MainMenuFrame();

			// Shared.ctrl.mainMenu.setExtendedState(Shared.ctrl.mainMenu.MAXIMIZED_BOTH);

			Shared.ctrl.mainMenu.setSize(ConstMessagesEN.Params.DEFAULT_WIDTH * 2,
					ConstMessagesEN.Params.DEFAULT_HEIGHT * 2);
			Shared.ctrl.mainMenu.setLocationRelativeTo(null);

		}
		registerAction(Shared.ctrl.mainMenu.getMntmUtilisateurs(), (e) -> openPersonnelWindow());
		registerAction(Shared.ctrl.mainMenu.getMntmRoles(), (e) -> openRoleWindow());
		registerAction(Shared.ctrl.mainMenu.getMntmGroupes(), (e) -> openGroupWindow());
		registerAction(Shared.ctrl.mainMenu.getMntmMenu(), (e) -> openMenuWindow());
		registerAction(Shared.ctrl.mainMenu.getMntmCommande(), (e) -> openCommandeWindow());
		registerAction(Shared.ctrl.mainMenu.getMntmReception(), (e) -> openReceptionWindow());
		registerAction(Shared.ctrl.mainMenu.getMntmSuiviLot(), (e) -> openLotWindow());
		registerAction(Shared.ctrl.mainMenu.getMntmProduit(), (e) -> openArticleWindow());
		registerAction(Shared.ctrl.mainMenu.getMntmVente(), (e) -> openVenteWindow());
		registerAction(Shared.ctrl.mainMenu.getMntmDonneesBase(), (e) -> openDonneesDeBaseWindow());
		
		registerAction(Shared.ctrl.mainMenu.getMntmQuitter(), (e) -> closeApp());
		
		
		
		//instanciation des formulaire pour partie artcle
		//tableModel  = new FormeTableModel(); 
		//Shared.ctrl.formeFrame = new FormeFrame(tableModel); 
		//formeController.prepareListeners(Shared.ctrl.formeFrame);
		
		
		
		Shared.ctrl.mainMenu.setVisible(true);

	}

	private void openVenteWindow() {
		venteController.prepareAndOpenFrame(null);

	}

	private void openReceptionWindow() {
		receptionController.prepareAndOpenFrame(null);

	}

	private void openDonneesDeBaseWindow() {
		donneesDeBaseController.prepareAndOpenFrame(null);

	}

	private void openCommandeWindow() {
		commandeController.prepareAndOpenFrame(null);

	}

	private void openArticleWindow() {
		articleController.prepareAndOpenFrame(null);

	}

	private void openLotWindow() {
		lotController.prepareAndOpenFrame(null);

	}

	private void openRoleWindow() {
		System.out.println("private void openRoleWindow() { ");
		roleController.prepareAndOpenFrame(null);
	}

	private void openPersonnelWindow() {
		personnelController.prepareAndOpenFrame(null);
	}

	private void openGroupWindow() {
		groupeController.prepareAndOpenFrame(null);
	}

	private void openMenuWindow() {
		menuController.prepareAndOpenFrame(null);
	}

	private void closeApp() {
		mainMenuFrame.dispose();
		System.exit(0);
	}

}
