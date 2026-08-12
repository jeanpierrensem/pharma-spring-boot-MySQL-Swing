package com.officine.client.ui.register;

import com.officine.client.api.ApiException;
import com.officine.client.auth.AuthService;
import com.officine.client.ui.SceneNavigator;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;

public class RegisterController {

	@FXML private TextField usernameField;
	@FXML private TextField nameField;
	@FXML private TextField emailField;
	@FXML private PasswordField passwordField;
	@FXML private Label errorLabel;
	@FXML private ProgressIndicator loadingIndicator;

	private final AuthService authService = AuthService.getInstance();

	@FXML
	void onRegister() {
		errorLabel.setVisible(false);
		errorLabel.setManaged(false);
		String username = trim(usernameField);
		String name = trim(nameField);
		String email = trim(emailField);
		String password = passwordField.getText() == null ? "" : passwordField.getText();
		if (username.isBlank() || name.isBlank() || email.isBlank() || password.isBlank()) {
			showError("Tous les champs sont obligatoires.");
			return;
		}
		loadingIndicator.setVisible(true);
		loadingIndicator.setManaged(true);
		Task<Void> task = new Task<>() {
			@Override
			protected Void call() {
				authService.register(username, name, email, password);
				return null;
			}
		};
		task.setOnSucceeded(e -> Platform.runLater(() -> SceneNavigator.showDashboard()));
		task.setOnFailed(e -> Platform.runLater(() -> {
			loadingIndicator.setVisible(false);
			loadingIndicator.setManaged(false);
			Throwable ex = task.getException();
			showError(ex instanceof ApiException apiEx ? apiEx.getMessage() : "Inscription impossible");
		}));
		new Thread(task, "register-task").start();
	}

	@FXML
	void onBackToLogin() {
		SceneNavigator.showLogin();
	}

	private static String trim(TextField field) {
		return field.getText() == null ? "" : field.getText().trim();
	}

	private void showError(String message) {
		errorLabel.setText(message);
		errorLabel.setVisible(true);
		errorLabel.setManaged(true);
	}
}
