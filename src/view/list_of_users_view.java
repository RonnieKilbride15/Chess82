package view;

import java.io.*;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import object_classes.*;
/**
 * Class for managing the list of viewers scene
 * @author Ronald Kilbride
 * @author Radha Katkamwar
 *
 */
public class list_of_users_view{
	
	@FXML Button add;
	@FXML Button delete;
	@FXML Button ok;
	@FXML Button cancel;
	@FXML TextField username;
	@FXML Button logout;
	
	Stage temp;
	@FXML ListView<String> listView;
	ObservableList<String> obsList = FXCollections.observableArrayList();
	

	/**
	 * loads the users into a list
	 * @throws Exception
	 */
	public void loadUsers() throws Exception {
		try {
			FileInputStream fis = new FileInputStream("src/data/users.ser");
	        ObjectInputStream in = new ObjectInputStream(fis);
	        ArrayList<User> list = (ArrayList<User>)in.readObject();
	        for(int i = 0; i < list.size(); i++) {
	        	obsList.add(list.get(i).toString());
	        }
		} catch(Exception e) {
			
		}
	}
	
	/**
	 * starts the list of users scene
	 * @param mainStage stage for the current scene
	 * @throws Exception
	 */
	public void start(Stage mainStage) throws Exception {
		temp = mainStage;
		temp.setHeight(420);
		loadUsers();
		try {
			listView.setItems(obsList);
			listView.getSelectionModel().select(0);
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
	 * adjusts the window size accordinly so the admin can add a user
	 * @param e
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void add(ActionEvent e) throws FileNotFoundException, IOException {
		temp.setHeight(510);
		add.setDisable(true);
		delete.setDisable(true);
		listView.setDisable(true);
		cancel.setDisable(false);
		username.setText("");
		
	}
	
	/**
	 * deletes the user from the serialized data
	 * @param index
	 * @throws Exception
	 */
	public void deleteUser(int index) throws Exception {
		FileInputStream fis = new FileInputStream("src/data/users.ser");
        ObjectInputStream in = new ObjectInputStream(fis);
        ArrayList<User> list = (ArrayList<User>)in.readObject();
        list.remove(index);
        fis.close();
        in.close();
        
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src/data/users.ser"));
        oos.writeObject(list);
        oos.close();
	}
	
	/**
	 * adjusts the window size accordingly so the admin can delete a user
	 * @param e
	 * @throws Exception
	 */
	public void delete(ActionEvent e) throws Exception {
		if(!obsList.isEmpty()) {
			int index = listView.getSelectionModel().getSelectedIndex();
			deleteUser(index);
			obsList.clear();
			FileInputStream fis = new FileInputStream("src/data/users.ser");
			ObjectInputStream in = new ObjectInputStream(fis);
		    ArrayList<User> list = (ArrayList<User>)in.readObject();
		    for(int i = 0; i < list.size(); i++) {
		       obsList.add(list.get(i).toString());
		    }
		    fis.close();
		    in.close();
		    
		    if(obsList.size() == index) {
				listView.getSelectionModel().select(obsList.size()-1);
			}else {
				listView.getSelectionModel().select(index);
			}
		}
	}
	
	/**
	 * adds a user to the serialized data
	 * @param u the user that will be deleted
	 * @return
	 * @throws Exception
	 */
	public int addUser(User u) throws Exception {
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
		
		for(int i = 0; i < list.size(); i++) {
			if(u.username.toLowerCase().compareTo(list.get(i).username.toLowerCase()) == 0) {
				oos.writeObject(list);
				oos.close();
				return -1;
			}
			else if(u.username.toLowerCase().compareTo(list.get(i).username.toLowerCase()) < 0){
				list.add(i, u);
				oos.writeObject(list);
				oos.close();
				return i;
			}
		}
		
		list.add(u);
		oos.writeObject(list);
		oos.close();
		return list.size()-1;
		
		
	}
	/**
	 * adds the suer to the observable list
	 * @param e
	 * @throws Exception
	 */
	public void ok(ActionEvent e) throws Exception {
		User a = new User(username.getText());
		int index = addUser(a);
		if(index == -1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(temp);
			alert.setContentText("Username Already Exists");
			alert.showAndWait();
		}else if(username.getText().trim().isEmpty()){
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(temp);
			alert.setContentText("Username Field Cannot Be Empty");
			alert.showAndWait();
		}else {
			obsList.clear();
			FileInputStream fis = new FileInputStream("src/data/users.ser");
			ObjectInputStream in = new ObjectInputStream(fis);
		    ArrayList<User> list = (ArrayList<User>)in.readObject();
		    for(int i = 0; i < list.size(); i++) {
		       obsList.add(list.get(i).toString());
		    }
		    fis.close();
		    in.close();
		    
		}
		temp.setHeight(420);
		add.setDisable(false);
		delete.setDisable(false);
		listView.setDisable(false);
		cancel.setDisable(false);
		username.setText("");
		listView.getSelectionModel().select(index);
		
	}
	
	/**
	 * cancels the add
	 * @param e
	 */
	public void cancel(ActionEvent e) {
		temp.setHeight(420);
		add.setDisable(false);
		delete.setDisable(false);
		listView.setDisable(false);
		cancel.setDisable(false);
		username.setText("");
	}
}
