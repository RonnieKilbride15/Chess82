package view;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import object_classes.User;

/**
 * Class for managing the login view
 * @author Ronald Kilbride
 * @author Radha Katkamwar
 *
 */

public class loginView {
	@FXML Button login;
	@FXML Button cancel;
	@FXML TextField username;
	Stage temp;

	/**
	 * This method starts the photos application
	 * @param mainStage The first stage of the application
	 * @throws Exception
	 */
	public void start(Stage mainStage) throws Exception { 
		temp = mainStage;
		temp.setHeight(216);
	}
	
	/**
	 * This method logs in a user
	 * @param e
	 * @throws Exception
	 */
	public void login(ActionEvent e) throws Exception {
		
		if(username.getText().trim().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(temp);
			alert.setContentText("Username Field Cannot Be Empty");
			alert.showAndWait();
		}else if(username.getText().equals("admin")) {
			
			FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(getClass().getResource("list_of_users.fxml"));
	        Parent tableViewParent = loader.load();
	        Scene scene = new Scene(tableViewParent);
	        
	        list_of_users_view controller = loader.getController();
	        controller.start(temp);
			
			Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();
			
		}else {
			boolean userExists = false;
			try {
				FileInputStream fis = new FileInputStream("src/data/users.ser");
		        ObjectInputStream in = new ObjectInputStream(fis);
		        ArrayList<User> list = (ArrayList<User>)in.readObject();
		        
		        for(int i = 0; i < list.size(); i++) {
		        	if(list.get(i).username.equals(username.getText())) {
		        		FXMLLoader loader = new FXMLLoader();
		    	        loader.setLocation(getClass().getResource("user_albums.fxml"));
		    	        Parent tableViewParent = loader.load();
		    	        Scene scene = new Scene(tableViewParent);
		    			
		    	        user_albums_view controller = loader.getController();
		    			controller.start(temp, username.getText());
		    			
		    			Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
		    			stage.setScene(scene);
		    			stage.show();
		    			userExists = true;
		    			break;
		        	}
		        }
			} catch(Exception a) {
				
			}
			
			if(!userExists) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(temp);
				alert.setContentText("User Does Not Exist");
				alert.showAndWait();
			}
			
		}
		
	}
	
	/**
	 * this method shuts down the application
	 * @param e
	 */
	public void cancel(ActionEvent e) {
		temp.close();
	}
}
