package object_classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javafx.scene.image.*;

/**
 * Class for managing the Photo Object
 * @author Ronald Kilbride
 * @author Radha Katkamwar
 *
 */
public class Photo implements Serializable{
	
	public String caption;
	public String photoLink;
	public Calendar date;
	Date date1;
	public ArrayList<Tag> tags;
	
	/**
	 * constructor for the photo object
	 * @param photoLink filepath of the photo
	 */
	public Photo(String photoLink) {
		this.photoLink = photoLink;
		this.date = Calendar.getInstance();
		this.date1 = date.getTime();
		this.tags = new ArrayList<Tag>();
	}
	
	/**
	 * gets the caption of the photo
	 * @return
	 */
	public String getCaption() {
		return this.caption;
	}
	
	/**
	 * gets the filepath of the photo
	 * @return
	 */
	public String getPhotoLink() {
		return this.photoLink;
	}
	
	/**
	 * gets the date of the photo when it was taken
	 * @return
	 */
	public Date getDate() {
		return this.date.getInstance().getTime();
	}
	
	
	public String toString() {
		return this.photoLink;
	}
}