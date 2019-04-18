package view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import object_classes.*;

/**
 * Class for managing the albums scene
 * @author Ronald Kilbride
 * @author Radha Katkamwar
 *
 */

public class user_albums_view {

	
	@FXML Button add;
	@FXML Button edit;
	@FXML Button delete;
	@FXML TableView<Album> tableView;
	@FXML TableColumn<Album, String> addColumn;
	@FXML TableColumn<Album, Date> dateFirstColumn;
	@FXML TableColumn<Album, Date> dateLastColumn;
	@FXML TableColumn<Album, Integer> photoAmountColumn;
	@FXML Button viewPhotos;
	@FXML Button ok;
	@FXML Button cancel;
	@FXML Button search;
	@FXML Label greet;
	@FXML Label addEditTitle;
	@FXML TextField albumName;
	@FXML Button logout;
		
	int currentUserIndex = 0;
	Stage temp;
	ObservableList<Album> obsList = FXCollections.observableArrayList();

	/**
	 * starts the album view scene
	 * @param mainStage the stage that the current scene belongs to
	 * @param user the current user
	 */
	public void start(Stage mainStage, String user) {
		temp = mainStage;
		temp.setHeight(400);
		greet.setText("Hello " + user);
		addColumn.setCellValueFactory(new PropertyValueFactory<Album,String>("name"));
		dateFirstColumn.setCellValueFactory(new PropertyValueFactory<Album,Date>("dateFirstPhoto"));
		dateLastColumn.setCellValueFactory(new PropertyValueFactory<Album,Date>("dateLastPhoto"));
		photoAmountColumn.setCellValueFactory(new PropertyValueFactory<Album,Integer>("photosAmount"));
		loadAlbums(user);
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
	 * loads the albums for the current user
	 * @param user the current user
	 */
	public void loadAlbums(String user) {
		try {
			FileInputStream fis = new FileInputStream("src/data/users.ser");
	        ObjectInputStream in = new ObjectInputStream(fis);
	        ArrayList<User> list = (ArrayList<User>)in.readObject();
	        for(int i = 0; i < list.size(); i++) {
	        	if(list.get(i).username.equals(user)) {
	        		currentUserIndex = i;
	        		for(Album a : list.get(i).albums) {
	        			obsList.add(a);
	        		}
	        		tableView.setItems(obsList);
	        		tableView.getSelectionModel().select(0);
	        		break;
	        	}
	        }
	        
		} catch(Exception e) {
			
		}
	}
	
	/**
	 * adjusts the window size accrodingly so the user can add an album
	 * @param e
	 */
	public void add(ActionEvent e) {
		temp.setHeight(510);
		add.setDisable(true);
		delete.setDisable(true);
		edit.setDisable(true);
		viewPhotos.setDisable(true);
		search.setDisable(true);
		tableView.setDisable(true);
		addEditTitle.setText("Add An Album");
		albumName.setText("");
	}
	
	/**
	 * edits the album and serializes the data
	 * @param index the index of the album in the users album list
	 * @return
	 * @throws Exception
	 */
	public int editAlbum(int index) throws Exception {
		if(!obsList.isEmpty()) {
			FileInputStream fis = new FileInputStream("src/data/users.ser");
			ObjectInputStream in = new ObjectInputStream(fis);
		    ArrayList<User> list = (ArrayList<User>)in.readObject();
		    User user = list.get(currentUserIndex);
		    for(int i = 0; i < user.albums.size(); i++) {
		       if(user.albums.get(i).name.toLowerCase().equals(albumName.getText().toLowerCase())) {
		    	   return -1;
		       }
		    }
		    
		    list.get(currentUserIndex).albums.get(index).name = albumName.getText();
		    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src/data/users.ser"));
	        oos.writeObject(list);
	        oos.close();
		    fis.close();
		    in.close();
		    
		}
		
		return 0;
	}
	
	/**
	 * adjusts the window size accrodingly so the user can edit an album
	 * @param e
	 */
	public void edit(ActionEvent e) {
		temp.setHeight(510);
		add.setDisable(true);
		delete.setDisable(true);
		edit.setDisable(true);
		viewPhotos.setDisable(true);
		search.setDisable(true);
		tableView.setDisable(true);
		addEditTitle.setText("Edit Album Name");
		albumName.setText("");
	}
	
	/**
	 * deletes the album from the serialized data
	 * @param index
	 * @throws Exception
	 */
	public void deleteAlbum(int index) throws Exception {
		FileInputStream fis = new FileInputStream("src/data/users.ser");
        ObjectInputStream in = new ObjectInputStream(fis);
        ArrayList<User> list = (ArrayList<User>)in.readObject();
        User user = list.get(currentUserIndex);
        user.albums.remove(index);
        fis.close();
        in.close();
        
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src/data/users.ser"));
        oos.writeObject(list);
        oos.close();
	}
	
	/**
	 * adjusts the window size accrodingly so the user can delete an album
	 * @param e
	 */
	public void delete(ActionEvent e) throws Exception {
		if(!obsList.isEmpty()) {
			int index = tableView.getSelectionModel().getSelectedIndex();
			deleteAlbum(index);
			obsList.clear();
			FileInputStream fis = new FileInputStream("src/data/users.ser");
			ObjectInputStream in = new ObjectInputStream(fis);
		    ArrayList<User> list = (ArrayList<User>)in.readObject();
		    User user = list.get(currentUserIndex);
		    for(int i = 0; i < user.albums.size(); i++) {
		       obsList.add(user.albums.get(i));
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
	 * Changes the scene and allows the user to view the photos
	 * @param e
	 */
	public void viewPhotos(ActionEvent e) {
		try {
			int currentAlbumIndex = tableView.getSelectionModel().getSelectedIndex();
			FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(getClass().getResource("user_photos.fxml"));
	        Parent tableViewParent = loader.load();
	        Scene scene = new Scene(tableViewParent);
	        
	        user_photos_view controller = loader.getController();
	        controller.start(temp, currentUserIndex, currentAlbumIndex);
			
			Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();
		}catch(Exception a) {
			
		}
	}
	
	/**
	 * changes the scene and allows the user to search for photos
	 * @param e
	 */
	public void search(ActionEvent e) {
		try {
			int currentAlbumIndex = tableView.getSelectionModel().getSelectedIndex();
			FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(getClass().getResource("user_search.fxml"));
	        Parent tableViewParent = loader.load();
	        Scene scene = new Scene(tableViewParent);
	        
	        user_search_view controller = loader.getController();
	        controller.start(temp, currentUserIndex);
			
			Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();
		}catch(Exception a) {
			
		}
	}
	
	/**
	 * adjusts the window size accrodingly so the user can cancel an action
	 * @param e
	 */
	public void cancel(ActionEvent e) {
		temp.setHeight(420);
		add.setDisable(false);
		delete.setDisable(false);
		edit.setDisable(false);
		viewPhotos.setDisable(false);
		search.setDisable(false);
		tableView.setDisable(false);
		cancel.setDisable(false);
		albumName.setText("");
	}
	
	/**
	 * adds the album to the serialized data
	 * @param album the current album
	 * @return returns -1 if the album already exists, 0 otherwise
	 * @throws Exception
	 */
	public int addAlbum(String album) throws Exception{
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
		for(int i = 0; i < list.get(currentUserIndex).albums.size(); i++) {
			if(album.toLowerCase().compareTo(list.get(currentUserIndex).albums.get(i).name.toLowerCase()) == 0) {
				oos.writeObject(list);
				oos.close();
				return -1;
			}
		}
		list.get(currentUserIndex).albums.add(new Album(album));
		oos.writeObject(list);
		oos.close();

		return 0;
	}
	
	/**
	 * adds or edits the album name
	 * @param e
	 * @throws Exception
	 */
	public void ok(ActionEvent e) throws Exception {
		if(addEditTitle.getText().equals("Add An Album")) {
			if(albumName.getText().trim().isEmpty()){
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(temp);
				alert.setContentText("Album Name Field Cannot Be Empty");
				alert.showAndWait();
			}else {
				int index = addAlbum(albumName.getText());
				if(index == -1) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.initOwner(temp);
					alert.setContentText("Album Name Already Exists");
					alert.showAndWait();
				}else {
					obsList.clear();
					FileInputStream fis = new FileInputStream("src/data/users.ser");
					ObjectInputStream in = new ObjectInputStream(fis);
				    ArrayList<User> list = (ArrayList<User>)in.readObject();
				    User user = list.get(currentUserIndex);
				    for(Album i : user.albums) {
				    	obsList.add(i);
				    }
				    fis.close();
				    in.close();
				    
				}	
				
			}
			temp.setHeight(420);
			add.setDisable(false);
			delete.setDisable(false);
			edit.setDisable(false);
			viewPhotos.setDisable(false);
			search.setDisable(false);
			tableView.setDisable(false);
			cancel.setDisable(false);
			albumName.setText("");
			tableView.setItems(obsList);
		}else {
			if(albumName.getText().trim().isEmpty()){
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(temp);
				alert.setContentText("Album Name Field Cannot Be Empty");
				alert.showAndWait();
			}else {
				int index = tableView.getSelectionModel().getSelectedIndex();
				int a = editAlbum(index);
				if(a == -1) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.initOwner(temp);
					alert.setContentText("Album Name Already Exists");
					alert.showAndWait();
				}else {
					obsList.clear();
					FileInputStream fis = new FileInputStream("src/data/users.ser");
					ObjectInputStream in = new ObjectInputStream(fis);
				    ArrayList<User> list = (ArrayList<User>)in.readObject();
				    User user = list.get(currentUserIndex);
				    for(Album i : user.albums) {
				    	obsList.add(i);
				    }
				    fis.close();
				    in.close();
				    
				}
			}
		}
		temp.setHeight(420);
		add.setDisable(false);
		delete.setDisable(false);
		edit.setDisable(false);
		viewPhotos.setDisable(false);
		search.setDisable(false);
		tableView.setDisable(false);
		cancel.setDisable(false);
		albumName.setText("");
		tableView.setItems(obsList);
	}
	
	

}
