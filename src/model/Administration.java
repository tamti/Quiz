package model;

public class Administration extends AccountManager {
	
	public Administration() {
		super();
	}
	

	public void addAnnouncement(String announcement, String username) {
		System.out.println("enterd Administration");
		User ad = getUser(username);
		System.out.println("got user");
		int ad_id = ad.getID();
		Announcement ann = new Announcement(ad_id, announcement);
		uDao.addAnnouncement(ann);
	}

	
	public void promoteToAdmin(String username) {
		User newAdmin = getUser(username);
		uDao.updateAdminStatus(newAdmin.getID(), true);
	}
	
	public void removeAccount(User user) {
		
	}
	
	public void removeQuiz(User user) {
		
	}
	
	public void clearHistory(User user) {
		
	}
	

}
