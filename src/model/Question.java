package model;

public abstract class Question {
	QuestionType type;
	public static enum QuestionType{
		MULTIPLE_CHOICE,
		PICTURE_QUIZ,
		QUESTION_ANSWER,
		FILL_THE_GAPS
	}
	public Question(QuestionType type){
		this.type = type;
	}
	public QuestionType getType() {
		return type;
	}
	public void setType(QuestionType type) {
		this.type = type;
	}
	//public abstract int checkAnswer(JsonElement elem);
	
}
