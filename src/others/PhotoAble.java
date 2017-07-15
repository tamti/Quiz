package others;

public class PhotoAble {
	private int photoID;
	private String photoUrl;

	public PhotoAble() {
		photoID = -1;
		photoUrl = "";
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
	
	public String getPhoto() {
		return photoUrl;
	}
	
	public void removePhoto() {
		photoID = -1;
		photoUrl = "";
	}
	
	public boolean hasPhoto() {
		return !photoUrl.isEmpty();
	}
}
