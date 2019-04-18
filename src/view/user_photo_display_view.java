package view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import object_classes.Album;
import object_classes.Photo;
import object_classes.Tag;
import object_classes.User;

/**
 * Class for managing the photo display
 * @author Ronald Kilbride
 * @author Radha Katkamwar
 *
 */
public class user_photo_display_view {

	@FXML ImageView imageView;
	@FXML Button leftArrow;
	@FXML Button rightArrow;
	@FXML TableView<Tag> tableView;
	@FXML TableColumn<Tag,String> tagTypeColumn;
	@FXML TableColumn<Tag, String> tagValueColumn;
	@FXML TextField tagTypeCaption;
	@FXML TextField tagValue;
	@FXML Label tagTypeCaptionLabel;
	@FXML Label tagValueLabel;
	@FXML Button ok;
	@FXML Button cancel;
	@FXML Button edit;
	@FXML Button addTag;
	@FXML Button delete;
	@FXML Label captionLabel;
	@FXML Label dateLabel;
	@FXML Label title;
	@FXML Label greet;
	@FXML Button logout;
	@FXML Button back;
	
	Stage temp;
	int currentUserIndex;
	int currentAlbumIndex;
	int currentPhotoIndex;
	ObservableList<Tag> obsList = FXCollections.observableArrayList();
	
	/**
	 * starts the photo display scene
	 * @param mainStage stage of the current scene
	 * @param userIndex index of the current user from the user list
	 * @param albumIndex index of the current album from the user's album list
	 * @param photoIndex index of the current photo from the user's list of photos in an album
	 * @throws Exception
	 */
	public void start(Stage mainStage, int userIndex, int albumIndex, int photoIndex) throws Exception {
		temp = mainStage;
		temp.setHeight(610);
		currentUserIndex = userIndex;
		currentAlbumIndex = albumIndex;
		currentPhotoIndex = photoIndex;
		FileInputStream fis = new FileInputStream("src/data/users.ser");
        ObjectInputStream in = new ObjectInputStream(fis);
        ArrayList<User> b = (ArrayList<User>)in.readObject();
        User user = b.get(currentUserIndex);
        
        greet.setText("Hello " + user.username);
		tagTypeColumn.setCellValueFactory(new PropertyValueFactory<Tag, String>("tagType"));
		tagValueColumn.setCellValueFactory(new PropertyValueFactory<Tag,String>("tagValue"));
		loadPhoto();
		
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
	 * allows the user to go back to the previous scene
	 * @param e
	 * @throws Exception
	 */
	public void back(ActionEvent e) throws Exception {
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("user_photos.fxml"));
        Parent tableViewParent = loader.load();
        Scene scene = new Scene(tableViewParent);
        FileInputStream fis = new FileInputStream("src/data/users.ser");
        ObjectInputStream in = new ObjectInputStream(fis);
        ArrayList<User> b = (ArrayList<User>)in.readObject();
        User user = b.get(currentUserIndex);
        user_photos_view controller = loader.getController();
        controller.start(temp, currentUserIndex, currentAlbumIndex);
		
		Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}
	
	
	/**
	 * loads the photo from the serialzied data
	 * @throws Exception
	 */
	public void loadPhoto() throws Exception {
		obsList.clear();
		FileInputStream fis = new FileInputStream("src/data/users.ser");
        ObjectInputStream in = new ObjectInputStream(fis);
        ArrayList<User> list = (ArrayList<User>)in.readObject();
        String photoLink = list.get(currentUserIndex).albums.get(currentAlbumIndex).photos.get(currentPhotoIndex).photoLink;
        imageView.setImage(new Image(photoLink));
        captionLabel.setText(list.get(currentUserIndex).albums.get(currentAlbumIndex).photos.get(currentPhotoIndex).caption);
        dateLabel.setText("" + list.get(currentUserIndex).albums.get(currentAlbumIndex).photos.get(currentPhotoIndex).date.getTime());
        ArrayList<Tag> tags = list.get(currentUserIndex).albums.get(currentAlbumIndex).photos.get(currentPhotoIndex).tags;
        for(int i = 0; i < tags.size(); i++) {
        	obsList.add(tags.get(i));
        }
        tableView.setItems(obsList);
        tableView.getSelectionModel().select(0);
	}
	
	/**
	 * allows the user to go to the photo right before the current one
	 * @param e
	 * @throws Exception
	 */
	public void goLeft(ActionEvent e) throws Exception {
		if(currentPhotoIndex != 0) {
			currentPhotoIndex -= 1;
			loadPhoto();
		}
		
	}
	
	/**
	 * allows the user to go to the photo right after the current one
	 * @param e
	 * @throws Exception
	 */
	public void goRight(ActionEvent e) throws Exception {
		FileInputStream fis = new FileInputStream("src/data/users.ser");
        ObjectInputStream in = new ObjectInputStream(fis);
        ArrayList<User> list = (ArrayList<User>)in.readObject();
		if(currentPhotoIndex + 1 != list.get(currentUserIndex).albums.get(currentAlbumIndex).photos.size()) {
			currentPhotoIndex += 1;
			loadPhoto();
			return;
		}
	}

	/**
	 * confirms the add tag or edit action
	 * @param e
	 * @throws Exception
	 */
	public void ok(ActionEvent e) throws Exception {
		
		
		if(title.getText().equals("Edit Caption")){
			FileInputStream fis = new FileInputStream("src/data/users.ser");
	        ObjectInputStream in = new ObjectInputStream(fis);
	        ArrayList<User> list = (ArrayList<User>)in.readObject();
	        list.get(currentUserIndex).albums.get(currentAlbumIndex).photos.get(currentPhotoIndex).caption = tagTypeCaption.getText();
	        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src/data/users.ser"));
		    oos.writeObject(list);
		    fis.close();
		    oos.close();
		    in.close();
		    loadPhoto();
	    }else if(title.getText().equals("Add Tag")) {
	    	boolean tagExists = false;
	    	FileInputStream fis = new FileInputStream("src/data/users.ser");
	        ObjectInputStream in = new ObjectInputStream(fis);
	        ArrayList<User> list = (ArrayList<User>)in.readObject();
	        if(tagTypeCaption.getText().trim().isEmpty()) {
	        	Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(temp);
				alert.setContentText("Tag Type Field Is Empty");
				alert.showAndWait();
	        }else if(tagValue.getText().trim().isEmpty()) {
	        	Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(temp);
				alert.setContentText("Tag Value Field Is Empty");
				alert.showAndWait();
	        }else {
	        	
	        	ArrayList<Tag> tags = list.get(currentUserIndex).albums.get(currentAlbumIndex).photos.get(currentPhotoIndex).tags;
	            for(int i = 0; i < tags.size(); i++) {
	            	if(tags.get(i).tagType.toLowerCase().equals(tagTypeCaption.getText().toLowerCase())
	            			&& tags.get(i).tagValue.toLowerCase().equals(tagValue.getText().toLowerCase())) {
	            		Alert alert = new Alert(AlertType.ERROR);
	    				alert.initOwner(temp);
	    				alert.setContentText("Tag Already Exists");
	    				tagExists = true;
	    				alert.showAndWait();
	    				break;
	            	}else if(tagTypeCaption.getText().toLowerCase().equals("location")) {
	    	            for(int j = 0; j < tags.size(); j++) {
	    	            	if(tags.get(j).tagType.toLowerCase().equals("location")) {
	    	            		Alert alert = new Alert(AlertType.ERROR);
	    	    				alert.initOwner(temp);
	    	    				alert.setContentText("Location Tag Already Exists");
	    	    				tagExists = true;
	    	    				alert.showAndWait();
	    	    				break;
	    	            	}
	    	            }
	    	            break;
	    	            
	    	        }
	            }
	            
	            if(!tagExists) {
	            	obsList.clear();
	            	list.get(currentUserIndex).albums.get(currentAlbumIndex).photos.get(currentPhotoIndex).tags.add(new Tag(tagTypeCaption.getText(), tagValue.getText()));
	            	ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src/data/users.ser"));
	    		    oos.writeObject(list);
	    		    loadPhoto();
	    		    fis.close();
	    		    oos.close();
	    		    in.close();
	    		    
	            }
	        }
	        
		}
		
		temp.setHeight(610);
		delete.setDisable(false);
		edit.setDisable(false);
		addTag.setDisable(false);
		tableView.setDisable(false);
		tagTypeCaptionLabel.setText("");
		tagValue.setVisible(true);
		tagValueLabel.setVisible(true);
		leftArrow.setDisable(false);
		rightArrow.setDisable(false);
		ok.setLayoutY(685);
		cancel.setLayoutY(685);
	}
	
	/**
	 * cancels the users action
	 * @param e
	 */
	public void cancel(ActionEvent e) {
		temp.setHeight(610);
		delete.setDisable(false);
		edit.setDisable(false);
		addTag.setDisable(false);
		tableView.setDisable(false);
		title.setText("");
		tagTypeCaptionLabel.setText("");
		tagValue.setText("");
		tagValue.setVisible(true);
		tagValueLabel.setVisible(true);
		leftArrow.setDisable(false);
		rightArrow.setDisable(false);
		ok.setLayoutY(685);
		cancel.setLayoutY(685);
	}
	
	/**
	 * adjusts the window size accordingly so the user can edit the photo caption
	 * @param e
	 */
	public void edit(ActionEvent e) {
		temp.setHeight(700);
		delete.setDisable(true);
		edit.setDisable(true);
		addTag.setDisable(true);
		tableView.setDisable(true);
		title.setText("Edit Caption");
		tagTypeCaptionLabel.setText("Caption:");
		tagValue.setText("");
		tagTypeCaption.setText("");
		tagValue.setVisible(false);
		tagValueLabel.setVisible(false);
		leftArrow.setDisable(true);
		rightArrow.setDisable(true);
		ok.setLayoutY(639);
		cancel.setLayoutY(639);
	}
	
	/**
	 * adjusts the window size accoridingly so the user can add a tag
	 * @param e
	 */
	public void addTag(ActionEvent e) {
		temp.setHeight(750);
		delete.setDisable(true);
		edit.setDisable(true);
		addTag.setDisable(true);
		tableView.setDisable(true);
		title.setText("Add Tag");
		tagTypeCaptionLabel.setText("Tag Type:");
		tagValue.setVisible(true);
		tagValue.setText("");
		tagTypeCaption.setText("");
		tagValueLabel.setVisible(true);
		leftArrow.setDisable(true);
		rightArrow.setDisable(true);
		
	}
	/**
	 * deletes the tag from the serialized data for a photo
	 * @param e
	 * @throws Exception
	 */
	public void deleteTag(ActionEvent e) throws Exception {
		if(!obsList.isEmpty()) {
			int index = tableView.getSelectionModel().getSelectedIndex();
			FileInputStream fis = new FileInputStream("src/data/users.ser");
	        ObjectInputStream in = new ObjectInputStream(fis);
	        ArrayList<User> list = (ArrayList<User>)in.readObject();
	        list.get(currentUserIndex).albums.get(currentAlbumIndex).photos.get(currentPhotoIndex).tags.remove(index);
	        obsList.remove(index);
	        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src/data/users.ser"));
	        oos.writeObject(list);
	        oos.close();
		    fis.close();
		    in.close();
		    if(obsList.size() == index) {
				tableView.getSelectionModel().select(obsList.size()-1);
			}else {
				tableView.getSelectionModel().select(index);
			}
		}
	}

}
