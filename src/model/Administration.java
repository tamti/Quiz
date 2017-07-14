package model;

public class Administration extends AccountManager {
	
	public Administration() {
		super();
	}
	

	public void addAnnouncement(String announcement, String username) {
		User ad = getUser(username);
		Announcement ann = new Announcement(username, announcement);
		uDao.addAnnouncement(ad.getID(), ann);
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
	
	public void removeFromAdminPost(String username) {
		User oldAdmin = getUser(username);
		uDao.updateAdminStatus(oldAdmin.getID(), false);
	}
	
}
