package others;

import model.User;

/**
 * @author Vazha
 *
 */
public class PhotoAble {
	private long photoID;
	private String photoFile;
	private String defaultPhoto;
	
	public PhotoAble(String defaultPhoto, long photoID) {
		this.defaultPhoto = defaultPhoto;
		photoFile = "";
		this.photoID = photoID;
	}
	
	public PhotoAble() {
		this("", -1);
		photoFile = "";
		defaultPhoto = "";
		photoID = -1;
	}
	
	public void setPhoto(String photoFileName, long photoID) {
		photoFile = photoFileName;
		this.photoID = photoID;
	}
	
	public String getPhoto() {
		if (hasPhoto())
			return photoFile;
		else
			return defaultPhoto;
	}
	
	public void removePhoto() {
		photoFile = "";
		if (!hasDefaultPhoto())
			photoID = -1;
	}
	
	public boolean hasPhoto() {
		return !photoFile.isEmpty();
	}
	
	public boolean hasDefaultPhoto() {
		return !defaultPhoto.isEmpty();
	}
	
	public long getPhotoID() {
		return photoID;
	}

	public boolean equeals(Object other) {
		return photoID == ((PhotoAble) other).getPhotoID();
	}
}
