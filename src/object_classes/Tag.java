package object_classes;

import java.io.Serializable;
/**
 * Class for managing the Tag Object
 * @author Ronald Kilbride
 * @author Radha Katkamwar
 *
 */
public class Tag implements Serializable{
	public String tagType;
	public String tagValue;
	
	/**
	 * constructor for the tag object
	 * @param tagType
	 * @param tagValue
	 */
	public Tag(String tagType, String tagValue) {
		this.tagType = tagType;
		this.tagValue = tagValue;
	}
	/**
	 * gets the type of tag
	 * @return
	 */
	
	public String getTagType() {
		return this.tagType;
	}
	
	/**
	 * gets the value of the tag
	 * @return
	 */
	public String getTagValue() {
		return this.tagValue;
	}
	
}

