package com.officine.losto;

import javax.swing.SwingUtilities;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.officine.losto.ui.forms.controller.LoginController;
import com.officine.losto.uti.ui.LookAndFeelUtils.LookAndFeelUtils;

@SpringBootApplication
public class OfficineApplication { 

	public static void main(String... args) {
		LookAndFeelUtils.setWindowsLookAndFeel();
		ConfigurableApplicationContext context = createApplicationContext(args);		
		displayLoginFrame(context); 
	}
	private static ConfigurableApplicationContext createApplicationContext(String... args) {
		return new SpringApplicationBuilder(OfficineApplication.class).headless(false).run(args);
	}

	private static void displayLoginFrame(ConfigurableApplicationContext context) {
		SwingUtilities.invokeLater(() -> {
		    LoginController loginController = context.getBean(LoginController.class);
		    loginController.prepareAndOpenFrame(null);
		});
	}
}