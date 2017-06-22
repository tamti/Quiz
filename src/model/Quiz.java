package model;

import java.sql.Date;
import java.util.ArrayList;

public class Quiz {
	int ID;
	String quizName;
	String description;
	boolean isOnePage;
	boolean feedback;
	boolean random;
	Date date;
	ArrayList <Question> questions;
	User ownes;
	public Quiz(int id, String quizName, Date date) {
		super();
		ID = id;
		this.quizName = quizName;
		this.date = date;
		questions = new ArrayList<>();
	}

	public Quiz(String quizName, Date date) {
		this(-1, quizName, date);
	}

	public User getOwnes() {
		return ownes;
	}

	public void setOwnes(User ownes) {
		this.ownes = ownes;
	}


	public ArrayList <Question> getQuestions() {
		return questions;
	}
	
	public void setQuestions(ArrayList <Question> questions) {
		this.questions = questions;
	}
	
	public String getQuizName() {
		return quizName;
	}
	public void setQuizName(String quizName) {
		this.quizName = quizName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getID() {
		return ID;
	}
	public void setID(int id) {
		ID = id;
	}
	public boolean isOnePage() {
		return isOnePage;
	}
	public void setOnePage(boolean isOnePage) {
		this.isOnePage = isOnePage;
	}
	public boolean isFeedback() {
		return feedback;
	}
	public void setFeedback(boolean feedback) {
		this.feedback = feedback;
	}
	public boolean isRandom() {
		return random;
	}
	public void setRandom(boolean random) {
		this.random = random;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	public static class QuizHandle{
		public int ID;
		public String name;
		public int score;
		public QuizHandle(int iD, String name, int score) {
			super();
			ID = iD;
			this.name = name;
			this.score = score;
		}
		
	}

}
