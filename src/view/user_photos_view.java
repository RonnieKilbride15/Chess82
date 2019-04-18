package view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import object_classes.*;
import javafx.util.Callback;

/**
 * Class for managing the users photos scene
 * @author Ronald Kilbride
 * @author Radha Katkamwar
 *
 */
public class user_photos_view {
	
	@FXML Button add;
	@FXML Button edit;
	@FXML Button delete;
	@FXML Button fullView;
	@FXML Button tag;
	@FXML Button search;
	@FXML Button ok;
	@FXML Button cancel;
	@FXML TextField linkAndTagType;
	@FXML TextField tagValue;
	@FXML Label title;
	@FXML Label linkAndTagTypeLabel;
	@FXML Label tagValueLabel;
	@FXML Label greet;
	@FXML Button logout;
	@FXML Button back;
	
	@FXML TableView<Photo> tableView;
	@FXML TableColumn<Photo, String> photoColumn;
	@FXML TableColumn<Photo, String> captionColumn;
	
	Stage temp;
	int currentUserIndex;
	int currentAlbumIndex;
	ObservableList<Photo> obsList = FXCollections.observableArrayList();
	
	
	/**
	 * starts the scene
	 * @param mainStage stage for the current scene
	 * @param userIndex the index of the current user in the serialized user list
	 * @param albumIndex the index of the current album
	 * @throws Exception
	 */
	public void start(Stage mainStage,int userIndex, int albumIndex) throws Exception {
		
		temp = mainStage;
		mainStage.setHeight(355);
		currentUserIndex = userIndex;
		currentAlbumIndex = albumIndex;
		FileInputStream fis = new FileInputStream("src/data/users.ser");
        ObjectInputStream in = new ObjectInputStream(fis);
        ArrayList<User> b = (ArrayList<User>)in.readObject();
        User user = b.get(currentUserIndex);
        
        greet.setText("Hello " + user.username);
		photoColumn.setCellValueFactory(new PropertyValueFactory<Photo, String>("photoLink"));
		captionColumn.setCellValueFactory(new PropertyValueFactory<Photo,String>("caption"));

		loadPhotos();
		
		
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
	 * loads the photos from the serialized data
	 * @throws Exception
	 */
	public void loadPhotos() throws Exception {
		try {
			FileInputStream fis = new FileInputStream("src/data/users.ser");
	        ObjectInputStream in = new ObjectInputStream(fis);
	        ArrayList<User> list = (ArrayList<User>)in.readObject();
	        ArrayList<Photo> photoList = list.get(currentUserIndex).albums.get(currentAlbumIndex).photos;
	        for(int i = 0; i < photoList.size(); i++) {
	        	obsList.add(photoList.get(i));
	        }
	        tableView.setItems(obsList);
	        tableView.getSelectionModel().select(0);
		} catch(Exception e) {
			
		}
	}
	
	/**
	 * adjusts the window size accordingly so the user can add a photo
	 * @param e
	 */
	public void add(ActionEvent e) {
		temp.setHeight(450);
		add.setDisable(true);
		delete.setDisable(true);
		edit.setDisable(true);
		fullView.setDisable(true);
		tag.setDisable(true);
		tableView.setDisable(true);
		title.setText("Add A Photo");
		linkAndTagTypeLabel.setText("Photo Link:");
		tagValue.setVisible(false);
		tagValueLabel.setVisible(false);
		ok.setLayoutY(391);
		cancel.setLayoutY(391);
		
	}
	
	/**
	 * adjusts the window size accoridingly so the user can edit a photo caption
	 * @param e
	 */
	public void edit(ActionEvent e) {
		temp.setHeight(450);
		add.setDisable(true);
		delete.setDisable(true);
		edit.setDisable(true);
		fullView.setDisable(true);
		tag.setDisable(true);
		tableView.setDisable(true);
		title.setText("Edit Caption");
		linkAndTagTypeLabel.setText("Caption:");
		tagValue.setVisible(false);
		tagValueLabel.setVisible(false);
		ok.setLayoutY(391);
		cancel.setLayoutY(391);
	}
	
	/**
	 * deletes the photo from the serialzied data
	 * @param index the index of the photo in the albums list
	 * @throws Exception
	 */
	
	public void deletePhoto(int index) throws Exception {
		FileInputStream fis = new FileInputStream("src/data/users.ser");
        ObjectInputStream in = new ObjectInputStream(fis);
        ArrayList<User> list = (ArrayList<User>)in.readObject();
        User user = list.get(currentUserIndex);
        user.albums.get(currentAlbumIndex).photos.remove(index);
        fis.close();
        in.close();
        list.get(currentUserIndex).albums.get(currentAlbumIndex).photosAmount -= 1;
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src/data/users.ser"));
        oos.writeObject(list);
        oos.close();
	}
	
	/**
	 * delete the photo from the serialized data
	 * @param e
	 * @throws Exception
	 */
	public void delete(ActionEvent e) throws Exception {
		if(!obsList.isEmpty()) {
			int index = tableView.getSelectionModel().getSelectedIndex();
			deletePhoto(index);
			obsList.clear();
			FileInputStream fis = new FileInputStream("src/data/users.ser");
			ObjectInputStream in = new ObjectInputStream(fis);
		    ArrayList<User> list = (ArrayList<User>)in.readObject();
		    Album a = list.get(currentUserIndex).albums.get(currentAlbumIndex);
		    for(int i = 0; i < a.photos.size(); i++) {
		       obsList.add(a.photos.get(i));
		    }
		    fis.close();
		    in.close();
		    if(obsList.size() == index) {
				tableView.getSelectionModel().select(obsList.size()-1);
			}else {
				tableView.getSelectionModel().select(index);
			}
		}
	}
	
	/**
	 * changes the scene to the full photo display scene
	 * @param e
	 */
	public void fullView(ActionEvent e) {
		int photoIndex = tableView.getSelectionModel().getSelectedIndex();
		try {
			FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(getClass().getResource("user_photo_display.fxml"));
	        Parent tableViewParent = loader.load();
	        Scene scene = new Scene(tableViewParent);
	        
	        user_photo_display_view controller = loader.getController();
	        controller.start(temp, currentUserIndex, currentAlbumIndex,photoIndex);
			
			Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();
		}catch(Exception a) {
			
		}
	}
	
	/**
	 * adjusts the window size accordingly so the user can make a tag
	 * @param e
	 */
	public void tag(ActionEvent e) {
		temp.setHeight(500);
		add.setDisable(true);
		delete.setDisable(true);
		edit.setDisable(true);
		fullView.setDisable(true);
		tag.setDisable(true);
		tableView.setDisable(true);
		title.setText("Add A Tag");
		linkAndTagTypeLabel.setText("Tag Type:");
		tagValueLabel.setText("Tag Value");
		tagValue.setVisible(true);
		tagValueLabel.setVisible(true);
	}
	
	
	/**
	 * adds the photo to the serialized data
	 * @param filePath filePath of the photo
	 * @return
	 * @throws Exception
	 */
	public int addPhoto(String filePath) throws Exception {
		File file = new File("src/data/users.ser");
		ArrayList<User> list = new ArrayList<User>();
		try {
			
			FileInputStream fis = new FileInputStream("src/data/users.ser");
	        ObjectInputStream in = new ObjectInputStream(fis);
	        ArrayList<User> b = (ArrayList<User>)in.readObject();
	        list = b;
	        fis.close();
	        in.close();
	        
		} catch(Exception e) {
			
		}
		
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src/data/users.ser"));
		ArrayList<Photo> photos = list.get(currentUserIndex).albums.get(currentAlbumIndex).photos;
		for(Photo i : photos) {
			if(i.photoLink.equals(filePath)) {
				oos.writeObject(list);
				oos.close();
				return -1;
			}
		}
		Photo p = new Photo(filePath);
		list.get(currentUserIndex).albums.get(currentAlbumIndex).photosAmount += 1;
		list.get(currentUserIndex).albums.get(currentAlbumIndex).photos.add(p);
		oos.writeObject(list);
		oos.close();
		
		return 0;
	}
	
	/**
	 * confirms the add photo/tag, edit, or copy/move to action
	 * @param e
	 * @throws Exception
	 */
	public void ok(ActionEvent e) throws Exception {
		if(title.getText().equals("Add A Photo")) {
			if(linkAndTagType.getText().trim().isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(temp);
				alert.setContentText("Album Name Field Cannot Be Empty");
				alert.showAndWait();
			}else {
				int index = addPhoto(linkAndTagType.getText());
				if(index == -1) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.initOwner(temp);
					alert.setContentText("Photo Already Exists");
					alert.showAndWait();
				}else {
					obsList.clear();
					FileInputStream fis = new FileInputStream("src/data/users.ser");
					ObjectInputStream in = new ObjectInputStream(fis);
				    ArrayList<User> list = (ArrayList<User>)in.readObject();
				    Album albumList = list.get(currentUserIndex).albums.get(currentAlbumIndex);
				    for(Photo i : albumList.photos) {
				    	obsList.add(i);
				    }
				    fis.close();
				    in.close();
				}
			}
			
			temp.setHeight(355);
			add.setDisable(false);
			delete.setDisable(false);
			edit.setDisable(false);
			fullView.setDisable(false);
			tag.setDisable(false);
			tableView.setDisable(false);
			tagValue.setVisible(true);
			tagValueLabel.setVisible(true);
			ok.setLayoutY(432);
			cancel.setLayoutY(432);
			linkAndTagType.setText("");
			tableView.setItems(obsList);
		}else if(title.getText().equals("Edit Caption")) {
			int index = tableView.getSelectionModel().getSelectedIndex();
			
			obsList.clear();
			FileInputStream fis = new FileInputStream("src/data/users.ser");
			ObjectInputStream in = new ObjectInputStream(fis);
		    ArrayList<User> list = (ArrayList<User>)in.readObject();
		    list.get(currentUserIndex).albums.get(currentAlbumIndex).photos.get(index).caption = linkAndTagType.getText();
		    for(Photo i : list.get(currentUserIndex).albums.get(currentAlbumIndex).photos) {
		    	obsList.add(i);
		    }
		    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src/data/users.ser"));
		    oos.writeObject(list);
		    fis.close();
		    oos.close();
		    in.close();
		
			temp.setHeight(355);
			add.setDisable(false);
			delete.setDisable(false);
			edit.setDisable(false);
			fullView.setDisable(false);
			tag.setDisable(false);
			tableView.setDisable(false);
			tagValue.setVisible(true);
			tagValueLabel.setVisible(true);
			ok.setLayoutY(432);
			cancel.setLayoutY(432);
			linkAndTagType.setText("");
			tableView.setItems(obsList);
		}else if(title.getText().equals("Add A Tag")) {
			int index = tableView.getSelectionModel().getSelectedIndex();
			String tagTypeStr = linkAndTagType.getText().toLowerCase();
			String tagValueStr = linkAndTagType.getText().toLowerCase();
			FileInputStream fis = new FileInputStream("src/data/users.ser");
			ObjectInputStream in = new ObjectInputStream(fis);
		    ArrayList<User> list = (ArrayList<User>)in.readObject();
			if(tagTypeStr.trim().isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(temp);
				alert.setContentText("Tag Type Field Is Empty");
				alert.showAndWait();
			}else if(tagValueStr.trim().isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(temp);
				alert.setContentText("Tag Value Field Is Empty");
				alert.showAndWait();
			}else if(tagTypeStr.trim().toLowerCase().equals("location")) {
			    ArrayList<Tag> tags = list.get(currentUserIndex).albums.get(currentAlbumIndex).photos.get(index).tags;
			    for(int i = 0; i < tags.size(); i++) {
			    	if(tags.get(i).tagType.equals("location")) {
			    		Alert alert = new Alert(AlertType.ERROR);
						alert.initOwner(temp);
						alert.setContentText("Location Tag Already Exists");
						alert.showAndWait();
						break;
			    	}
			    }
			}
			list.get(currentUserIndex).albums.get(currentAlbumIndex).photos.get(index).tags.add(new Tag(tagTypeStr, tagValueStr));
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src/data/users.ser"));
		    oos.writeObject(list);
		    fis.close();
		    oos.close();
		    in.close();
		    
		    temp.setHeight(355);
			add.setDisable(false);
			delete.setDisable(false);
			edit.setDisable(false);
			fullView.setDisable(false);
			tag.setDisable(false);
			tableView.setDisable(false);
			tagValue.setVisible(true);
			tagValueLabel.setVisible(true);
			ok.setLayoutY(432);
			cancel.setLayoutY(432);
			linkAndTagType.setText("");
			tableView.setItems(obsList);

		}
		else if(title.getText().equals("Move To Album")) {
			int index = tableView.getSelectionModel().getSelectedIndex();
			String albumName = linkAndTagType.getText();
			FileInputStream fis = new FileInputStream("src/data/users.ser");
			ObjectInputStream in = new ObjectInputStream(fis);
		    ArrayList<User> list = (ArrayList<User>)in.readObject();
		    ArrayList<Album> albums = list.get(currentUserIndex).albums;
		    
		    boolean albumExists = false;
		    for(int i = 0; i < albums.size(); i++) {
		    	if(albums.get(i).name.toLowerCase().equals(albumName.toLowerCase())) {
		    		list.get(currentUserIndex).albums.get(i).photos.add(list.get(currentUserIndex).albums.get(currentAlbumIndex).photos.get(index));
		    		list.get(currentUserIndex).albums.get(i).photosAmount += 1;
		    		list.get(currentUserIndex).albums.get(currentAlbumIndex).photos.remove(index);
		    		list.get(currentUserIndex).albums.get(currentAlbumIndex).photosAmount -= 1;
		    		obsList.remove(index);
		    		albumExists = true;
		    		break;
		    	}
		    }
		    
		    if(albumExists) {
			    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src/data/users.ser"));
			    oos.writeObject(list);
			    fis.close();
			    oos.close();
			    in.close();
		    }else {
		    	Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(temp);
				alert.setContentText("Album Does Not Exist");
				alert.showAndWait();
		    }
		    
		    temp.setHeight(355);
			add.setDisable(false);
			delete.setDisable(false);
			edit.setDisable(false);
			fullView.setDisable(false);
			tag.setDisable(false);
			tableView.setDisable(false);
			tagValue.setVisible(true);
			tagValueLabel.setVisible(true);
			ok.setLayoutY(432);
			cancel.setLayoutY(432);
			linkAndTagType.setText("");
			tableView.setItems(obsList);

		}
		else if(title.getText().equals("Copy To Album")) {
			int index = tableView.getSelectionModel().getSelectedIndex();
			String albumName = linkAndTagType.getText();
			FileInputStream fis = new FileInputStream("src/data/users.ser");
			ObjectInputStream in = new ObjectInputStream(fis);
		    ArrayList<User> list = (ArrayList<User>)in.readObject();
		    ArrayList<Album> albums = list.get(currentUserIndex).albums;
		    boolean albumExists = false;
		    for(int i = 0; i < albums.size(); i++) {
		    	if(albums.get(i).name.toLowerCase().equals(albumName.toLowerCase())) {
		    		list.get(currentUserIndex).albums.get(i).photos.add(list.get(currentUserIndex).albums.get(currentAlbumIndex).photos.get(index));
		    		list.get(currentUserIndex).albums.get(i).photosAmount += 1;
		    		albumExists = true;
		    		
		    		break;
		    	}
		    }
		    
		    if(albumExists) {
			    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src/data/users.ser"));
			    oos.writeObject(list);
			    fis.close();
			    oos.close();
			    in.close();
		    }else {
		    	Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(temp);
				alert.setContentText("Album Does Not Exist");
				alert.showAndWait();
		    }
		    
		    temp.setHeight(355);
			add.setDisable(false);
			delete.setDisable(false);
			edit.setDisable(false);
			fullView.setDisable(false);
			tag.setDisable(false);
			tableView.setDisable(false);
			tagValue.setVisible(true);
			tagValueLabel.setVisible(true);
			ok.setLayoutY(432);
			cancel.setLayoutY(432);
			linkAndTagType.setText("");
			tableView.setItems(obsList);
		    

		}
	}

	/**
	 * cancels the users action
	 * @param e
	 */
	public void cancel(ActionEvent e) {
		temp.setHeight(355);
		add.setDisable(false);
		delete.setDisable(false);
		edit.setDisable(false);
		fullView.setDisable(false);
		tag.setDisable(false);
		tableView.setDisable(false);
		tagValue.setVisible(true);
		tagValueLabel.setVisible(true);
		ok.setLayoutY(432);
		cancel.setLayoutY(432);
	}
	
	/**
	 * adjusts the window size accordingly so the user can enter the album name
	 * for the photo to be moved to
	 * @param e
	 */
	public void moveTo(ActionEvent e) {
		temp.setHeight(450);
		add.setDisable(true);
		delete.setDisable(true);
		edit.setDisable(true);
		fullView.setDisable(true);
		tag.setDisable(true);
		tableView.setDisable(true);
		title.setText("Move To Album");
		linkAndTagTypeLabel.setText("Album Name:");
		tagValue.setVisible(false);
		tagValueLabel.setVisible(false);
		ok.setLayoutY(391);
		cancel.setLayoutY(391);
		
	}
	
	/**
	 * adjusts the window size accordingly so the user can enter the album name
	 * for the photo to be copied to
	 * @param e
	 */
	public void copyTo(ActionEvent e) {
		temp.setHeight(450);
		add.setDisable(true);
		delete.setDisable(true);
		edit.setDisable(true);
		fullView.setDisable(true);
		tag.setDisable(true);
		tableView.setDisable(true);
		title.setText("Copy To Album");
		linkAndTagTypeLabel.setText("Album Name:");
		tagValue.setVisible(false);
		tagValueLabel.setVisible(false);
		ok.setLayoutY(391);
		cancel.setLayoutY(391);
		
	}
	
	
	
	
	
}
