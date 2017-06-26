package others;

/**
 * @author Vazha
 *
 */
public class PhotoAble {
	private int photoID;
	private String photoFile;
	private String defaultPhoto;

	public void setPhoto(String photoFileName, int photoID, boolean isDefault) {
		this.photoID = photoID;
		if (isDefault) {
			defaultPhoto = photoFileName;
		} else {
			photoFile = photoFileName;
		}

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

	public int getPhotoID() {
		return photoID;
	}

	public boolean equeals(Object other) {
		return photoID == ((PhotoAble) other).getPhotoID();
	}
}
