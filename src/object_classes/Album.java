package object_classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Class for the album object
 * @author Ronald Kilbride
 * @author Radha Katkamwar
 *
 */
public class Album implements Serializable{
	public String name;
	public Date dateFirstPhoto;
	public Date dateLastPhoto;
	public ArrayList<Photo> photos;
	public Integer photosAmount;
	
	/**
	 * consturctor for the Album object
	 * @param name name of the album
	 */
	public Album(String name) {
		this.name = name;
		this.photos = new ArrayList<Photo>();
		this.photosAmount = 0;
	}
	
	/**
	 * returns the name of the album
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * returns the date of the earliest photo taken from the album
	 * @return
	 */
	public Date getDateFirstPhoto() {
		if(this.photos.size() > 0) {
			long smallest = this.photos.get(0).date.getTimeInMillis();
			Calendar c1 = Calendar.getInstance();
			for(int i = 0; i < this.photos.size(); i++) {
				  if(this.photos.get(i).date.getTimeInMillis() < smallest) {
					  smallest = this.photos.get(i).date.getTimeInMillis();
					  c1 = this.photos.get(i).date;
				  }
	
			}
			return c1.getTime();
		}
		return null;
	}
	
	/**
	 * returns the date of the latest photo taken from the album
	 * @return
	 */
	public Date getDateLastPhoto() {
		if(this.photos.size() > 0) {
			long largest = this.photos.get(0).date.getTimeInMillis();
			Calendar c1 = Calendar.getInstance();
			for(int i = 0; i < this.photos.size(); i++) {
				  if(this.photos.get(i).date.getTimeInMillis() > largest) {
					  largest = this.photos.get(i).date.getTimeInMillis();
					  c1 = this.photos.get(i).date;
				  }
	
			}
			return c1.getTime();
		}
		return null;
	}
	
	/**
	 * returns the amount of photos in the album
	 * @return
	 */
	public int getPhotosAmount() {
		return this.photosAmount;
	}
}