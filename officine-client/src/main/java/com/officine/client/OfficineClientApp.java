package com.officine.client;

import com.officine.client.ui.SceneNavigator;
import javafx.application.Application;
import javafx.stage.Stage;

public class OfficineClientApp extends Application {

	@Override
	public void start(Stage stage) {
		SceneNavigator.init(stage);
		stage.setMinWidth(480);
		stage.setMinHeight(560);
		SceneNavigator.bootstrap();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
