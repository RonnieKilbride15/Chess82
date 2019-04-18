package view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.*;
import java.time.format.DateTimeFormatter;
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
import javafx.stage.Stage;
import object_classes.Album;
import object_classes.Photo;
import object_classes.User;

/**
 * Class for managing the search results scene
 * @author Ronald Kilbride
 * @author Radha Katkamwar
 *
 */

public class user_search_results_view {

	
	@FXML Button ok;
	@FXML Button cancel;
	@FXML Button add;
	@FXML TextField album;
	@FXML TableView<Photo> tableView;
	@FXML TableColumn<Photo,String> photoColumn;
	@FXML TableColumn<Photo,String> captionColumn;
	@FXML Label title;
	@FXML Button logout;
	@FXML Button back;
	
	
	Stage temp;
	ObservableList<Photo> obsList = FXCollections.observableArrayList();
	int currentUserIndex;
	
	/**
	 * this method shows the search results scene
	 * @param mainStage the current stage the scene is in
	 * @param filter indicator for what type of search is being performed
	 * @param field0 a tag type or a start date
	 * @param field1 a tag value or a end date
	 * @param field2 a tag type
	 * @param field3 a tag value
	 * @param userIndex index of the user with respect to the user list
	 * @throws Exception
	 */
	public void start(Stage mainStage, int filter, String field0, String field1, String field2, String field3, int userIndex) throws Exception {
		temp = mainStage;
		temp.setHeight(350);
		currentUserIndex = userIndex;
		FileInputStream fis = new FileInputStream("src/data/users.ser");
        ObjectInputStream in = new ObjectInputStream(fis);
        ArrayList<User> b = (ArrayList<User>)in.readObject();
        User user = b.get(currentUserIndex);
        
        title.setText("Hello " + user.username);
		photoColumn.setCellValueFactory(new PropertyValueFactory<Photo,String>("photoLink"));
		captionColumn.setCellValueFactory(new PropertyValueFactory<Photo,String>("caption"));
		if(filter==0 || filter == 1 || filter == 2) {
			loadPhotosByTags(field0, field1, field2, field3, filter);
		}else{
			loadPhotosByDate(field0, field1);
		}
		
		try {
			tableView.setItems(obsList);
			tableView.getSelectionModel().select(0);
		}
		catch(Exception e) {
			
		}
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
	 * Brings the user back to the previous scece
	 * @param e
	 * @throws Exception
	 */
	public void back(ActionEvent e) throws Exception {
		FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("user_search.fxml"));
        Parent tableViewParent = loader.load();
        Scene scene = new Scene(tableViewParent);
        FileInputStream fis = new FileInputStream("src/data/users.ser");
        ObjectInputStream in = new ObjectInputStream(fis);
        ArrayList<User> b = (ArrayList<User>)in.readObject();
        User user = b.get(currentUserIndex);
        user_search_view controller = loader.getController();
        controller.start(temp, currentUserIndex);
		
		Stage stage = (Stage) ((Node)e.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.show();
	}
	
	/**
	 * loads photos within a specific date range
	 * @param date0 start date
	 * @param date1 end date
	 * @throws Exception
	 */
	public void loadPhotosByDate(String date0, String date1) throws Exception {
		long startDate;
		long endDate;
		DateFormat formatter; 
		Date dateObject0;
		Date dateObject1;
		formatter = new SimpleDateFormat("MM-dd-yyyy");
		dateObject0 = (Date)formatter.parse(date0);
		dateObject1 = (Date)formatter.parse(date1);
		Calendar cal0 = Calendar.getInstance();
		Calendar cal1 = Calendar.getInstance();
		cal0.setTime(dateObject0);
		cal1.setTime(dateObject1);
		
		startDate = cal0.getTimeInMillis();
		endDate = cal1.getTimeInMillis();
		
		FileInputStream fis = new FileInputStream("src/data/users.ser");
        ObjectInputStream in = new ObjectInputStream(fis);
        ArrayList<User> b = (ArrayList<User>)in.readObject();
        User user = b.get(currentUserIndex);
        for(int i = 0; i < user.albums.size(); i++) {
        	for(int j = 0; j < user.albums.get(i).photos.size(); j++) {
        		if(user.albums.get(i).photos.get(j).date.getTimeInMillis() >= startDate && user.albums.get(i).photos.get(j).date.getTimeInMillis() <= endDate) {
        			obsList.add(user.albums.get(i).photos.get(j));
        		}
        	}
        }
        
        fis.close();
	    in.close();
	    tableView.setItems(obsList);
		
	}
	
	/**
	 * loads photos that contain a tag(s)
	 * @param tagType0 first tag type
	 * @param tagValue0 first tag value
	 * @param tagType1 second tag type
	 * @param tagValue1 second tag value
	 * @param filter indicator representing if the search is an OR, AND, or singular search
	 * @throws Exception
	 */
	public void loadPhotosByTags(String tagType0, String tagValue0, String tagType1, String tagValue1, int filter) throws Exception {
		FileInputStream fis = new FileInputStream("src/data/users.ser");
        ObjectInputStream in = new ObjectInputStream(fis);
        ArrayList<User> b = (ArrayList<User>)in.readObject();
        User user = b.get(currentUserIndex);
        
        if(filter == 0) {
        	boolean hasFirstTag = false;
        	boolean hasSecondTag = false;
        	for(int i = 0; i < user.albums.size(); i++) {
        		for(int j = 0; j < user.albums.get(i).photos.size(); j++) {
        			for(int k = 0; k < user.albums.get(i).photos.get(j).tags.size(); k++) {
        				if(user.albums.get(i).photos.get(j).tags.get(k).tagType.toLowerCase().equals(tagType0.toLowerCase()) &&
        						user.albums.get(i).photos.get(j).tags.get(k).tagValue.toLowerCase().equals(tagValue0.toLowerCase())) {
        					hasFirstTag = true;
        				}
        				if(user.albums.get(i).photos.get(j).tags.get(k).tagType.toLowerCase().equals(tagType1.toLowerCase()) &&
        						user.albums.get(i).photos.get(j).tags.get(k).tagValue.toLowerCase().equals(tagValue1.toLowerCase())) {
        					hasSecondTag = true;
        				}
        				if(hasFirstTag && hasSecondTag) {
        					obsList.add(user.albums.get(i).photos.get(j));
        					break;
        				}
        			}
        			
        			
        			hasFirstTag = false;
    				hasSecondTag = false;
        			
        		}
        	}
        	
        	tableView.setItems(obsList);
        }else if(filter == 1) {
        	boolean hasFirstTag = false;
        	boolean hasSecondTag = false;
        	for(int i = 0; i < user.albums.size(); i++) {
        		for(int j = 0; j < user.albums.get(i).photos.size(); j++) {
        			for(int k = 0; k < user.albums.get(i).photos.get(j).tags.size(); k++) {
        				if(user.albums.get(i).photos.get(j).tags.get(k).tagType.toLowerCase().equals(tagType0.toLowerCase()) &&
        						user.albums.get(i).photos.get(j).tags.get(k).tagValue.toLowerCase().equals(tagValue0.toLowerCase())) {
        					hasFirstTag = true;
        				}
        				if(user.albums.get(i).photos.get(j).tags.get(k).tagType.toLowerCase().equals(tagType1.toLowerCase()) &&
        						user.albums.get(i).photos.get(j).tags.get(k).tagValue.toLowerCase().equals(tagValue1.toLowerCase())) {
        					hasSecondTag = true;
        				}
        				if(hasFirstTag || hasSecondTag) {
        					obsList.add(user.albums.get(i).photos.get(j));
        					break;
        				}
        			}
        			
        			
        			hasFirstTag = false;
    				hasSecondTag = false;
        			
        		}
        	}
        	tableView.setItems(obsList);
        }else if(filter == 2) {
        	boolean hasFirstTag = false;
        	for(int i = 0; i < user.albums.size(); i++) {
        		for(int j = 0; j < user.albums.get(i).photos.size(); j++) {
        			for(int k = 0; k < user.albums.get(i).photos.get(j).tags.size(); k++) {
        				if(user.albums.get(i).photos.get(j).tags.get(k).tagType.toLowerCase().equals(tagType0.toLowerCase()) &&
        						user.albums.get(i).photos.get(j).tags.get(k).tagValue.toLowerCase().equals(tagValue0.toLowerCase())) {
        					hasFirstTag = true;
        				}
        				
        				if(hasFirstTag) {
        					obsList.add(user.albums.get(i).photos.get(j));
        					break;
        				}
        			}
        			hasFirstTag = false;
        		}
        	}
        	tableView.setItems(obsList);
        }
	}
	
	/**
	 * Adds all the photos from the search results into a new album
	 * @param e
	 * @throws Exception
	 */
	public void ok(ActionEvent e) throws Exception {
		if(!obsList.isEmpty()) {
			temp.setHeight(350);
			FileInputStream fis = new FileInputStream("src/data/users.ser");
	        ObjectInputStream in = new ObjectInputStream(fis);
	        ArrayList<User> list = (ArrayList<User>)in.readObject();
	        User user = list.get(currentUserIndex);
	        boolean albumExists = false;
	        
	        for(int i = 0; i < user.albums.size(); i++) {
	        	if(user.albums.get(i).name.toLowerCase().equals(album.getText().toLowerCase())) {
	        		Alert alert = new Alert(AlertType.ERROR);
					alert.initOwner(temp);
					alert.setContentText("Album Name Already Exists");
					alert.showAndWait();
					albumExists = true;
					break;
	        	}
	        }
	        
	        if(!albumExists) {
	        	Album a = new Album(album.getText());
	        	for(int i = 0; i < obsList.size(); i++) {
	        		a.photos.add(obsList.get(i));
	        		a.photosAmount += 1;
	        	}
	        	list.get(currentUserIndex).albums.add(a);
	        	ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src/data/users.ser"));
	        	oos.writeObject(list);
	    		oos.close();
	    		in.close();
	    		fis.close();
	        }
		} 
        
	}
	
	/**
	 * Increases the size for the window so the user can enter an album name
	 * @param e
	 */
	public void add(ActionEvent e) {
		if(!obsList.isEmpty()) {
			temp.setHeight(500);
		}
	}
	/**
	 * Decreases the size of the 
	 * window if the user wants to 
	 * not add the photos to an album anymore
	 * @param e
	 */
	public void cancel(ActionEvent e) {
		temp.setHeight(360);
	}
}
