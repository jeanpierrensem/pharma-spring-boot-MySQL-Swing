package com.officine.client.ui;

import com.officine.client.auth.AuthService;
import com.officine.client.auth.SessionManager;
import java.io.IOException;
import java.util.Objects;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class SceneNavigator {

	private static Stage primaryStage;

	private SceneNavigator() {
	}

	public static void init(Stage stage) {
		primaryStage = stage;
	}

	public static void showLogin() {
		show("/fxml/login.fxml", "Officine — Connexion", 520, 640);
	}

	public static void showRegister() {
		show("/fxml/register.fxml", "Officine — Inscription", 560, 720);
	}

	public static void showDashboard() {
		show("/fxml/dashboard.fxml", "Officine — Dashboard", 980, 640);
	}

	public static void bootstrap() {
		AuthService authService = AuthService.getInstance();
		if (authService.tryRestoreSession()) {
			showDashboard();
		} else {
			showLogin();
		}
	}

	private static void show(String fxml, String title, double width, double height) {
		try {
			FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource(fxml));
			Parent root = loader.load();
			Scene scene = new Scene(root, width, height);
			primaryStage.setTitle(title);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException ex) {
			throw new IllegalStateException("Impossible de charger " + fxml, ex);
		}
	}

	public static void requireAuthenticated() {
		if (!SessionManager.getInstance().isAuthenticated()) {
			showLogin();
		}
	}
}
