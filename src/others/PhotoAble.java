package others;

public class PhotoAble {
	private int photoID;

	public PhotoAble() {
		photoID = -1;
	}
	
	public int getPhotoID() {
		return photoID;
	}
	
	public void setPhotoID(int photoID) {
		this.photoID = photoID;
	}
	
	public void removePhoto() {
		photoID = -1;
	}
	
	public boolean hasPhoto() {
		return photoID > 0;
	}
}
