package com.officine.client.ui.dashboard;

import com.officine.client.api.ApiClient;
import com.officine.client.api.ApiException;
import com.officine.client.auth.AuthService;
import com.officine.client.auth.RouteGuard;
import com.officine.client.auth.SessionManager;
import com.officine.client.model.CurrentUser;
import com.officine.client.ui.SceneNavigator;
import java.util.Map;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class DashboardController {

	@FXML private Label welcomeLabel;
	@FXML private Label roleBadge;
	@FXML private Label usernameValue;
	@FXML private Label roleValue;
	@FXML private Label accessInfoLabel;
	@FXML private Button adminAreaButton;

	private final SessionManager sessionManager = SessionManager.getInstance();
	private final AuthService authService = AuthService.getInstance();
	private final ApiClient apiClient = ApiClient.getInstance();

	@FXML
	void initialize() {
		SceneNavigator.requireAuthenticated();
		CurrentUser user = sessionManager.currentUser();
		if (user == null) {
			SceneNavigator.showLogin();
			return;
		}
		welcomeLabel.setText("Bienvenue, " + (user.name() != null ? user.name() : user.login()));
		usernameValue.setText(user.login());
		String primaryRole = sessionManager.roles().isEmpty() ? "USER" : sessionManager.roles().getFirst();
		roleValue.setText(primaryRole);
		roleBadge.setText(primaryRole);
		boolean admin = RouteGuard.canAccessAdmin(sessionManager);
		adminAreaButton.setVisible(admin);
		adminAreaButton.setManaged(admin);
		accessInfoLabel.setText(admin
				? "Vous disposez des droits administrateur (ROLE_ADMIN)."
				: "Accès utilisateur standard (ROLE_USER). Les modules métier seront filtrés selon votre rôle.");
	}

	@FXML
	void onLogout() {
		authService.logout();
		SceneNavigator.showLogin();
	}

	@FXML
	void onOpenAdminArea() {
		if (!RouteGuard.canAccessAdmin(sessionManager)) {
			new Alert(Alert.AlertType.WARNING, "Accès réservé aux administrateurs.").showAndWait();
			return;
		}
		Task<String> task = new Task<>() {
			@Override
			protected String call() {
				Map<String, Object> response = apiClient.getMap("admin/overview");
				return String.valueOf(response.get("message"));
			}
		};
		task.setOnSucceeded(e -> Platform.runLater(() ->
				new Alert(Alert.AlertType.INFORMATION, task.getValue()).showAndWait()));
		task.setOnFailed(e -> Platform.runLater(() -> {
			Throwable ex = task.getException();
			String msg = ex instanceof ApiException apiEx ? apiEx.getMessage() : "Erreur API admin";
			new Alert(Alert.AlertType.ERROR, msg).showAndWait();
		}));
		new Thread(task, "admin-area-task").start();
	}
}
