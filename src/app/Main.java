package app;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import view.loginView;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
/**
 * Class for managing Chess game
 * @author Ronald Kilbride
 *
 */

public class Main extends Application {
	@Override
	
	/***
	 * This method start the Photos application
	 * @param 
	 */
	public void start(Stage primaryStage) throws Exception {
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/view/login.fxml"));
		
		AnchorPane root = (AnchorPane)loader.load();
		loginView controller = loader.getController();
		controller.start(primaryStage);
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
		
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
