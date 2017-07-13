package servlets;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.SortedMap;

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
import model.Question;
import model.Quiz;
import model.User;

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
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
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
		
		String json = readAll(request.getInputStream());
		System.out.println(json);
		JsonObject quiz = new JsonParser().parse(json).getAsJsonObject();
		
		boolean answersImmediately = quiz.get("answersImmediately").getAsBoolean();
		boolean isOnePage = quiz.get("isOnePage").getAsBoolean();
		String name = quiz.get("name").getAsString();
		String description = quiz.get("description").getAsString();
		int allowedTimeInMinutes = quiz.get("allowedTimeInMinutes").getAsInt();
		Quiz quizObj = new Quiz(userID, name, description, answersImmediately, isOnePage, allowedTimeInMinutes);
		SortedMap<Integer, Question> questionsList = quizObj.getQuestions();
		
		JsonArray questions = quiz.get("questions").getAsJsonArray();
		for(int i=0; i<questions.size(); i++){
			JsonObject question = questions.get(i).getAsJsonObject();
			int questionCategoryId = question.get("category").getAsInt();
			if(questionCategoryId == 1 ){
				String questionText = question.get("question").getAsString();
				String questionAnswer = question.get("answer").getAsString();
				//QuestinAnswerQuestion q = new QuestinAnswerQuestion();
//				q.setQuestion(questionText);
//				q.setAnswer(questionAnswer);
//				q.setScore(1);
//				questionsList.add(q);
				System.out.println("q text: "+questionText);
			}
			else if(questionCategoryId == 2 ){
				String questionText = question.get("question").getAsString();
				String questionAnswer = question.get("answer").getAsString();
				String[] choices = new String[4];
				for(int j=0; j<choices.length; j++){
					choices[j] = question.get("choice"+(j+1)).getAsString();
				}
//				MultipleChoiceQuestion q = new MultipleChoiceQuestion(questionText, choices, questionAnswer, 1);
//				questionsList.add(q);
			}
			else if(questionCategoryId == 3 ){
				String pictureUrl = question.get("url").getAsString();
				String questionAnswer = question.get("answer").getAsString();
//				PictureQuizQuestion q = new PictureQuizQuestion(pictureUrl, questionAnswer, 1);
//				questionsList.add(q);
			}
			else  if(questionCategoryId == 4 ){
				String questionText = question.get("question").getAsString();
				JsonArray jsonAnswers = question.get("answers").getAsJsonArray();
				String [] answers = new String[jsonAnswers.size()];
				for(int k=0; k<answers.length; k++){
					answers[k]=jsonAnswers.get(i).getAsString();
				}
				System.out.println(answers);
//				FillTheGapsQuestion q = new FillTheGapsQuestion(questionText, answers, 1);
//				questionsList.add(q);
			}
		}
//		quizObj.setOwnes((User)request.getSession().getAttribute("user"));
//		DBHelper.addQuizIntoDatabase(quizObj);
//		System.out.println("name: "+quizObj.getQuizName());
//		System.out.println("random: "+quizObj.isRandom());
		response.getOutputStream().print("home.jsp");
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
