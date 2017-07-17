package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import databaseManagement.StatisticsDAO;
import model.QuizManager;

/**
 * Servlet implementation class TopInfoServlet
 */
@WebServlet("/TopInfoServlet")
public class TopInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TopInfoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		GsonBuilder gBuilder = new GsonBuilder();
		Gson gson = gBuilder.create();
	    
		StatisticsDAO stat = new StatisticsDAO();
		Map<String, Integer> result = stat.getTop10Quizzes();
		
		request.getSession().setAttribute("quizes", result);

//		for(int i=0;i<n.size();i++){
//			System.out.println(n.get(i));
//		}
		String json = gson.toJson(result);
	   
	    response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().write(json);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
