package servlets;

import java.io.IOException;
import java.io.InputStream;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.AccountManager;
import model.Answer;
import model.Question;
import model.Quiz;
import model.QuizManager;
import model.User;
import others.QuestionType;

/**
 * Servlet implementation class addQuizServlet
 */
@WebServlet("/addQuizServlet")
public class addQuizServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public addQuizServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		
		ServletContext servletCon = getServletContext();
		AccountManager accountMan = (AccountManager) servletCon.getAttribute("accountManager");
		User user = accountMan.getUser(username);
		int userID = user.getID();
		System.out.println(userID);
		String json = readAll(request.getInputStream());
		System.out.println(json);
		JsonObject quiz = new JsonParser().parse(json).getAsJsonObject();
		System.out.println(quiz.toString());
		boolean answersImmediately = quiz.get("answersImmediately").getAsBoolean();
		boolean isOnePage = quiz.get("isOnePage").getAsBoolean();
		String name = quiz.get("name").getAsString();
		String description = quiz.get("description").getAsString();
		int allowedTimeInMinutes = quiz.get("allowedTimeInMinutes").getAsInt();
		
		Quiz quizObj = new Quiz(userID, name, description, answersImmediately, isOnePage, allowedTimeInMinutes);
		
		JsonArray questions = quiz.get("questions").getAsJsonArray();
		for(int i=0; i<questions.size(); i++){
			JsonObject question = questions.get(i).getAsJsonObject();
			int questionCategoryId = question.get("category").getAsInt();
			if(questionCategoryId == 1 ){
				String questionText = question.get("question").getAsString();
				String questionAnswer = question.get("answer").getAsString();
				int maxPoint = question.get("maxpoint").getAsInt();
				Question q = new Question(questionText, QuestionType.basicResponse, maxPoint);
				Answer ans = new Answer(questionAnswer, true);
				q.addAnswer(ans);
				quizObj.addQuestion(q);
				System.out.println("q text: "+questionText);
			}
			else if(questionCategoryId == 2 ){
				System.out.println("inininininin");
				String questionText = question.get("question").getAsString();
				String questionAnswer = question.get("answer").getAsString();
				int maxPoint = question.get("maxpoint").getAsInt();
				String[] choices = new String[4];
				for(int j=0; j<choices.length; j++){
					choices[j] = question.get("choice"+(j+1)).getAsString();
				}
				Question q = new Question(questionText, QuestionType.multipleChoice, maxPoint);
				/*Answer ans1 = null;
				Answer ans2 = null;
				Answer ans3 = null;
				Answer ans4 = null;
				*/
			
				System.out.println("answerrrr " +questionAnswer );
				
				boolean[] ansCorrect = { false, false, false, false };
				
				switch (questionAnswer) {
					case "A":
						ansCorrect[0] = true;
						break;
					case "B":
						ansCorrect[1] = true;
						break;
					case "C":
						ansCorrect[2] = true;
						break;
					case "D":
						ansCorrect[3] = true;
						break;
					default:
						throw new IllegalArgumentException("Invalid argument: " + questionAnswer);
				}
				
				Answer ans1 = new Answer(choices[0], ansCorrect[0]);
				Answer ans2 = new Answer(choices[1], ansCorrect[1]);
				Answer ans3 = new Answer(choices[2], ansCorrect[2]);
				Answer ans4 = new Answer(choices[3], ansCorrect[3]);
				
				System.out.println(ans1.getAnswerStr());
				System.out.println(ans2.getAnswerStr());
				System.out.println(ans3.getAnswerStr());
				System.out.println(ans4.getAnswerStr());
				
				/*if (questionAnswer.equals("A")){
					System.out.println("aq aris!");
					ans1 = new Answer(choices[0], true);
					ans2 = new Answer(choices[1], false);
					ans3 = new Answer(choices[2], false);
					ans4 = new Answer(choices[3], false);
					System.out.println(ans1.getAnswerStr());
					System.out.println(ans2.getAnswerStr());
					System.out.println(ans3.getAnswerStr());
					System.out.println(ans4.getAnswerStr());
					
					
				}
				if (questionAnswer == "B"){
					ans1 = new Answer(choices[0], false);
					ans2 = new Answer(choices[1], true);
					ans3 = new Answer(choices[2], false);
					ans4 = new Answer(choices[3], false);
					System.out.println(ans1.getAnswerStr());
					System.out.println(ans2.getAnswerStr());
					System.out.println(ans3.getAnswerStr());
					System.out.println(ans4.getAnswerStr());
					
				}
				if (questionAnswer == "C"){
					ans1 = new Answer(choices[0], false);
					ans2 = new Answer(choices[1], false);
					ans3 = new Answer(choices[2], true);
					ans4 = new Answer(choices[3], false);
					
				}
				if (questionAnswer == "D"){
					ans1 = new Answer(choices[0], false);
					ans2 = new Answer(choices[1], false);
					ans3 = new Answer(choices[2], false);
					ans4 = new Answer(choices[3], true);
					
				}*/
				q.addAnswer(ans1);
				q.addAnswer(ans2);
				q.addAnswer(ans3);
				q.addAnswer(ans4);
				quizObj.addQuestion(q);
				System.out.println("q text: "+questionText);
				
			}
			else if(questionCategoryId == 3 ){
				String pictureUrl = question.get("url").getAsString();
				String questionAnswer = question.get("answer").getAsString();
				String questionText = question.get("question").getAsString();
				int maxPoint = question.get("maxpoint").getAsInt();
				Question q = new Question(questionText, QuestionType.pictureResponse, maxPoint);
				Answer ans = new Answer(questionAnswer, true);
				q.addAnswer(ans);
				q.setPhoto(pictureUrl);
				quizObj.addQuestion(q);
			}
			else  if(questionCategoryId == 4 ){
				String questionText = question.get("question").getAsString();
				JsonArray jsonAnswers = question.get("answers").getAsJsonArray();
				int maxPoint = question.get("maxpoint").getAsInt();
				Question q = new Question(questionText, QuestionType.fillInTheBlank, maxPoint);
				String [] answers = new String[jsonAnswers.size()];
				for(int k=0; k<answers.length; k++){
					answers[k]=jsonAnswers.get(k).getAsString();
					Answer ans = new Answer(answers[k], true);
					q.addAnswer(ans);
					System.out.println(ans.getAnswerStr());
				}
				quizObj.addQuestion(q);
				
				//System.out.println(answers);
			}
		}
		QuizManager man = new QuizManager();
		man.setQuiz(quizObj);
//		System.out.println("name: "+quizObj.getQuizName());
//		System.out.println("random: "+quizObj.isRandom());
		response.getOutputStream().print("homepage.jsp");
	}
	
	static String readAll(InputStream in){
		StringBuilder builder = new StringBuilder();
		while(true){
			try {
				int ch = in.read();
				if(ch == -1) break;
				builder.append((char)ch);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return builder.toString();
	}
}
