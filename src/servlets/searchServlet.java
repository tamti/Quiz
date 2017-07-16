package servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.AccountManager;
import model.Quiz;
import model.QuizManager;
import model.User;

/**
 * Servlet implementation class searchServlet
 */
@WebServlet("/searchServlet")
public class searchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public searchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stu
		System.out.println(request.getContextPath());
		System.out.println(response.getWriter());
		System.out.println(response.getWriter().append("Served at: ").append(request.getContextPath()));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println( request.getParameter("q"));
		String query = request.getParameter("q");
		RequestDispatcher rd;
		
		AccountManager man = new AccountManager();
		QuizManager quizman = new QuizManager();
		User[] users = man.searchUsers(query);
		Quiz [] quizes = quizman.searchQuizes(query);
		request.setAttribute("users", users);
		request.setAttribute("quizes", quizes);
		rd = request.getRequestDispatcher("search.jsp");
		rd.forward(request, response);
	}

}
