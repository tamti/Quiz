package others;

public class PhotoAble {
	private int photoID;
	private String photoFile;

	public PhotoAble() {
		photoID = -1;
		photoFile = "";
	}

	public void setPhoto(int photoID, String photoFileName) {
		this.photoID = photoID;
		photoFile = photoFileName;
	}

	public String getPhoto() {
		return photoFile;
	}

	public void removePhoto() {
		photoFile = "";
	}

	public boolean hasPhoto() {
		return !photoFile.isEmpty();
	}

	public int getPhotoID() {
		return photoID;
	}
}
