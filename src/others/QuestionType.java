package others;

public enum QuestionType {
	basicResponse(1),
	fillInTheBlank(2),
	multipleChoice(3),
	pictureResponse(4),
	multiAnswer(5),
	multiChoiceWithMultiAnswers(6),
	matching(7),
	graded(8),
	timed(9);
	
	private int ID;
	
	QuestionType(int ID) {
		this.ID = ID;
	}
	
	public int getID() {
		return ID;
	}
	
}