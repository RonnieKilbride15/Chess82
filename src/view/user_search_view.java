package view;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import object_classes.User;

/**
 * Class for managing the search view
 * @author Ronald Kilbride
 * @author Radha Katkamwar
 *
 */

public class user_search_view {
	@FXML CheckBox tags;
	@FXML CheckBox timeFrame;
	@FXML Label option;
	@FXML TextField tagType0;
	@FXML TextField tagValue0;
	@FXML TextField tagType1;
	@FXML TextField tagValue1;
	@FXML Button findResults;
	@FXML CheckBox and;
	@FXML CheckBox or;
	@FXML Label andLabel;
	@FXML Label orLabel;
	@FXML Label optionalLabel;
	@FXML Label greet;
	@FXML Button logout;
	@FXML Button back;
	
	Stage temp;
	int currentUserIndex;
	public void start(Stage mainStage, int userIndex) {
		temp = mainStage;
		temp.setHeight(320);
		currentUserIndex = userIndex;
		option.setVisible(false);
		tagType0.setVisible(false);
		tagValue0.setVisible(false);
		tagType1.setVisible(false);
		tagValue1.setVisible(false);
		findResults.setVisible(false);
		and.setVisible(false);
		or.setVisible(false);
		andLabel.setVisible(false);
		orLabel.setVisible(false);
		optionalLabel.setVisible(false);

	}
	
	/**
	 * logouts the user from the application
	 * @param e
	 * @throws Exception
	 */
	public void logout(ActionEvent e) throws Exception {
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("login.fxml"));
        Parent tableViewParent = loader.load();
        Scene scene = new Scene(tableViewParent);
        
        loginView controller = loader.getController();
        controller.start(temp);
		
		Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}
	
	/**
	 * allows the user to go back to the previous page
	 * @param e
	 * @throws Exception
	 */
	public void back(ActionEvent e) throws Exception {
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("user_albums.fxml"));
        Parent tableViewParent = loader.load();
        Scene scene = new Scene(tableViewParent);
        FileInputStream fis = new FileInputStream("src/data/users.ser");
        ObjectInputStream in = new ObjectInputStream(fis);
        ArrayList<User> b = (ArrayList<User>)in.readObject();
        User user = b.get(currentUserIndex);
        user_albums_view controller = loader.getController();
        controller.start(temp, user.username);
		
		Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}
	
	/**
	 * adjusts the scene accordingly when the tag checkbox is selected for the search
	 * @param e
	 */
	public void tags(ActionEvent e) {
		option.setVisible(true);
		tagType0.setVisible(true);
		tagValue0.setVisible(true);
		tagType1.setVisible(true);
		tagValue1.setVisible(true);
		tagType1.setDisable(true);
		tagValue1.setDisable(true);
		findResults.setVisible(true);
		and.setVisible(true);
		or.setVisible(true);
		option.setText("Tags: ");
		timeFrame.setSelected(false);
		tagType0.setPromptText("Tag Type");
		tagValue0.setPromptText("Tag Value");
		tagType1.setPromptText("Tag Type");
		tagValue1.setPromptText("Tag Value");
		andLabel.setVisible(true);
		orLabel.setVisible(true);
		optionalLabel.setVisible(true);
		findResults.setLayoutY(259);
	}
	
	/**
	 * adjusts the scene accordingly when the time frame checkbox is selected for the search
	 * @param e
	 */
	public void timeFrame(ActionEvent e) {
		tags.setSelected(false);
		tagType0.setVisible(true);
		tagValue0.setVisible(true);
		tagType1.setVisible(true);
		tagValue1.setVisible(true);
		findResults.setVisible(true);
		option.setVisible(true);
		tagType1.setVisible(false);
		tagValue1.setVisible(false);
		and.setVisible(false);
		or.setVisible(false);
		andLabel.setVisible(false);
		orLabel.setVisible(false);
		optionalLabel.setVisible(false);
		tagType0.setPromptText("MM-dd-yyyy");
		tagValue0.setPromptText("MM-dd-yyyy");
		option.setText("Frame: ");
		findResults.setLayoutY(169);

	}
	
	/**
	 * finds photos that match the search filters 
	 * @param e
	 */
	public void findResults(ActionEvent e) {
		if(tagType0.getText().trim().isEmpty() || tagValue0.getText().trim().isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(temp);
			alert.setContentText("Both Fields Must Be Filled");
			alert.showAndWait();
			return;
		}
		
		try {
			FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(getClass().getResource("user_search_results.fxml"));
	        Parent tableViewParent = loader.load();
	        Scene scene = new Scene(tableViewParent);
	        user_search_results_view controller = loader.getController();
	        if(tags.isSelected()) {
	        	if(and.isSelected()) {
	        		if(tagType1.getText().trim().isEmpty() || tagValue1.getText().trim().isEmpty()) {
	        			Alert alert = new Alert(AlertType.ERROR);
	    				alert.initOwner(temp);
	    				alert.setContentText("Both Optional Fields Must Be Filled If Being Used");
	    				alert.showAndWait();
	    				return;
	        		}else {
		        		controller.start(temp, 0, tagType0.getText(), tagValue0.getText(), tagType1.getText(), tagValue1.getText(), currentUserIndex);
	        		}
	        	}else if(or.isSelected()) {
	        		if(tagType1.getText().trim().isEmpty() || tagValue1.getText().trim().isEmpty()) {
	        			Alert alert = new Alert(AlertType.ERROR);
	    				alert.initOwner(temp);
	    				alert.setContentText("Both Optional Fields Must Be Filled If Being Used");
	    				alert.showAndWait();
	    				return;
	        		}else {
	        			controller.start(temp, 1, tagType0.getText(), tagValue0.getText(), tagType1.getText(), tagValue1.getText(), currentUserIndex);
	        		}
	        	}else if(!or.isSelected() && !and.isSelected()) {
	        		controller.start(temp, 2, tagType0.getText(), tagValue0.getText(), null, null, currentUserIndex);
	        	}
	        	
	        }else {
	        	controller.start(temp, 3, tagType0.getText(), tagValue0.getText(), null, null, currentUserIndex);
	        }
			
			Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();
		}catch(Exception a) {
			
		}
	}
	
	/**
	 * makes the and checkbox the only checkbox marked
	 * @param e
	 */
	public void and(ActionEvent e) {
		or.setSelected(false);
		tagType1.setDisable(false);
		tagValue1.setDisable(false);
		if(!and.isSelected()) {
			tagType1.setDisable(true);
			tagValue1.setDisable(true);
		}
	}
	/**
	 * makes the or checkbox the only chekcbox marked
	 * @param e
	 */
	public void or(ActionEvent e) {
		and.setSelected(false);
		tagType1.setDisable(false);
		tagValue1.setDisable(false);
		if(!or.isSelected()) {
			tagType1.setDisable(true);
			tagValue1.setDisable(true);
		}
	}
	
}
