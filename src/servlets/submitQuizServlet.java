package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.sun.org.glassfish.external.statistics.Statistic;

import databaseManagement.StatisticsDAO;
import model.AccountManager;
import model.Answer;
import model.Question;
import model.Quiz;
import model.Statistics;
import model.User;

/**
 * Servlet implementation class submitQuizServlet
 */
@WebServlet("/submitQuizServlet")
public class submitQuizServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public submitQuizServlet() {
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
		// TODO Auto-generated method stub
		long time = System.currentTimeMillis();
		java.sql.Timestamp timestamp = new java.sql.Timestamp(time);
		String userName = (String) request.getSession().getAttribute("username");
		AccountManager accountman = new AccountManager();
		User user = accountman.getUser(userName);
		System.out.println("user  "+user);
		String json = addQuizServlet.readAll(request.getInputStream());
		System.out.println(json);
		JsonArray answers = new JsonParser().parse(json).getAsJsonArray();
		Quiz quiz = (Quiz)request.getSession().getAttribute("quiz");
		List<Question> questions = quiz.getQuestions();
		int correct = 0;
		int index = 0;
		double point = 0;
		for(int i=0; i<questions.size(); i++){
			ArrayList<String> correctAnswer = questions.get(i).getQuestionsCorrectAnswer(); 
			for(int j=0; j<correctAnswer.size(); j++){
				int  cor = 0;
				System.out.println("question: "+i+","+j+" answer: "+index);
				if((correctAnswer.get(j)).equals(answers.get(index))){
					if(questions.get(i).getAnswers().size()==1){
						cor = -1;
						point = point + questions.get(i).getMaxPoints();
					}else{
						cor ++;
					}
					correct++;
					index++;
				}else{
					index++;
				}
				if(j == questions.get(i).getAnswers().size() -1){
					if(cor != -1){
						point = point + ((cor/questions.get(i).getAnswers().size()) * questions.get(i).getMaxPoints());
						System.out.println(point);
						//System.out.println(correct);
					}
				}
			}
			
			
		}
		System.out.println("aqamde");
		Statistics stat = new  Statistics(quiz.getID(),user.getID(), 300, correct, point, false);
		StatisticsDAO statDao = new StatisticsDAO();
		statDao.insertStatistics(stat);
		response.getOutputStream().print("{\"value\": \"correct "+correct+"\", \"url\": \"homepage.jsp\"}");
		response.getOutputStream().flush();
	}

}
