package com.officine.client.ui.login;

import com.officine.client.api.ApiException;
import com.officine.client.auth.AuthService;
import com.officine.client.ui.SceneNavigator;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;

public class LoginController {

	@FXML private TextField usernameField;
	@FXML private PasswordField passwordField;
	@FXML private CheckBox rememberMeCheckBox;
	@FXML private Label errorLabel;
	@FXML private Button loginButton;
	@FXML private ProgressIndicator loadingIndicator;

	private final AuthService authService = AuthService.getInstance();

	@FXML
	void initialize() {
		hideError();
	}

	@FXML
	void onLogin() {
		hideError();
		String username = usernameField.getText() == null ? "" : usernameField.getText().trim();
		String password = passwordField.getText() == null ? "" : passwordField.getText();
		if (username.isBlank() || password.isBlank()) {
			showError("Veuillez saisir le login et le mot de passe.");
			return;
		}
		setLoading(true);
		Task<Void> task = new Task<>() {
			@Override
			protected Void call() {
				authService.login(username, password, rememberMeCheckBox.isSelected());
				return null;
			}
		};
		task.setOnSucceeded(e -> Platform.runLater(() -> {
			setLoading(false);
			SceneNavigator.showDashboard();
		}));
		task.setOnFailed(e -> Platform.runLater(() -> {
			setLoading(false);
			Throwable ex = task.getException();
			if (ex instanceof ApiException apiEx) {
				showError(apiEx.getMessage());
			} else {
				showError("Connexion impossible : " + (ex != null ? ex.getMessage() : "erreur inconnue"));
			}
		}));
		new Thread(task, "login-task").start();
	}

	@FXML
	void onGoRegister() {
		SceneNavigator.showRegister();
	}

	private void setLoading(boolean loading) {
		loginButton.setDisable(loading);
		loadingIndicator.setVisible(loading);
		loadingIndicator.setManaged(loading);
	}

	private void showError(String message) {
		errorLabel.setText(message);
		errorLabel.setVisible(true);
		errorLabel.setManaged(true);
	}

	private void hideError() {
		errorLabel.setVisible(false);
		errorLabel.setManaged(false);
	}
}
