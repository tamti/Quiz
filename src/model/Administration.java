package model;

public class Administration extends AccountManager {
	
	private QuizManager qm  = new QuizManager();
	
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
		uDao.deactivateUserAccount(user.getID());
	}
	
	public void activateAccount(User user) {
		uDao.reactivateUserAccount(user.getID());
	}
	
	public void removeQuiz(int quiz_id) {
		qm.removeQuiz(quiz_id);
	}
	
	public void clearHistoryForQuiz(int quiz_id) {
		
	}
	
	public void removeFromAdminPost(String username) {
		User oldAdmin = getUser(username);
		uDao.updateAdminStatus(oldAdmin.getID(), false);
	}
	
}
