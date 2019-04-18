package object_classes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class for managing the User object
 * @author Ronald Kilbride
 * @author Radha Katkamwar
 *
 */
public class User implements Serializable{
	public String username;
	public ArrayList<Album> albums;
	
	/**
	 * constructor for the user object
	 * @param username username for the user object
	 */
	public User(String username) {
		this.username = username;
		this.albums = new ArrayList<Album>();
	}
	
	public String toString() {
		return this.username;
	}
	
	
	
	
	
	
}
