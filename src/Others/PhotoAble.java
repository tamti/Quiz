/**
 * 
 */
package Others;

/**
 * @author Vazha
 *
 */
public class PhotoAble {
	private String photoFile;
	private String defaultPhoto;
	
	public PhotoAble(String defaultPhoto) {
		this.defaultPhoto = defaultPhoto;
		photoFile = "";
	}
	
	public PhotoAble() {
		photoFile = "";
		defaultPhoto = "";
	}
	
	public void setPhoto(String photoFileName) {
		photoFile = photoFileName;
	}
	
	public String getPhoto() {
		if (hasPhoto())
			return photoFile;
		else
			return defaultPhoto;
	}
	
	public void removePhoto() {
		photoFile = "";
	}
	
	public boolean hasPhoto() {
		return !photoFile.isEmpty();
	}
	
	public boolean hasDefaultPhoto() {
		return !defaultPhoto.isEmpty();
	}
}
