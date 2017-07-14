package others;

public class PhotoAble {
	private int photoID;
	private String photoUrl;

	public PhotoAble() {
		photoID = -1;
	}
	
	public int getPhotoID() {
		return photoID;
	}
	
	public void setPhotoID(int photoID) {
		this.photoID = photoID;
	}
	public void setPhoto(String url){
		this.photoUrl = url;
	}
	
	public void removePhoto() {
		photoID = -1;
	}
	
	public boolean hasPhoto() {
		return photoID > 0;
	}
}
