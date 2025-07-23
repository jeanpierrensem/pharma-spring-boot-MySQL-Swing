package com.officine.losto.ui.forms.controller;

import java.awt.event.ActionEvent;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.springframework.orm.hibernate5.SpringSessionContext;
import org.springframework.stereotype.Controller;

import com.officine.losto.backend.entity.AppUser;
import com.officine.losto.backend.springcontext.session.SpringContext;
import com.officine.losto.backend.springcontext.session.UserSession;
import com.officine.losto.params.constant.ConstMessagesEN;
import com.officine.losto.security.cryptographic.Cryptographic;
import com.officine.losto.services.security.AccountService;
import com.officine.losto.ui.forms.Loginframe;
import com.officine.losto.ui.shared.controller.AbstractFrameController;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class LoginController extends AbstractFrameController {

	private final Loginframe loginFrame;
	private final AccountService accountService;
	private final MainMenuController mainMenuController;
	

	@Override
	public void prepareAndOpenFrame(JDialog parent) {
		

		registerAction(loginFrame.getBtnOk(), (e) -> authenticate());
		registerAction(loginFrame.getBtnClose(), (e) -> closeWindows());
		loginFrame.setVisible(true);
	}

	private void authenticate() {
		
		UserSession  userSession = SpringContext.getBean(UserSession.class); 

		String login = loginFrame.getTxtLogin().getText().trim().toString();
		String password = loginFrame.getTxtPassword().getText().toString();
		String chaineCrypter = Cryptographic.encode(login.concat(password.toString()), 
				               ConstMessagesEN.Params.ENCODING_ALGORITHM);
			
		AppUser appUser = accountService.Authenticate(login, chaineCrypter);

		if (appUser == null) {
			loginFrame.setVisible(true);
			JOptionPane.showMessageDialog(loginFrame, ConstMessagesEN.Messages.LOGIN_ERROR,
					ConstMessagesEN.Messages.ALERT_TILE, JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		userSession.setAppUser(appUser);
		
		loginFrame.setVisible(false); loginFrame.dispose();
		mainMenuController.prepareAndOpenFrame(null);
	}

	private void closeWindows() {
		System.exit(0);
	}
}
