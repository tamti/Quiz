package servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import model.AccountManager;

/**
 * Servlet implementation class LogInServlet
 */
@WebServlet("/LogInServlet")
public class LogInServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LogInServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String name = (String) request.getSession().getAttribute("username");
		
		response.setContentType("text/plain");
	    response.setCharacterEncoding("UTF-8");
	    response.getWriter().write(name);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletContext servletCont = getServletContext();
		AccountManager accountMan = (AccountManager) servletCont.getAttribute("accountManager");

		String login = request.getParameter("login");
		String password = request.getParameter("password");

		HttpSession session = request.getSession();
		session.setAttribute("username", login);

		if (accountMan.canPass(login, password)) {
			if (accountMan.isAdminAcc(login)) {
				RequestDispatcher dispatch = request.getRequestDispatcher("adminpage.html");
				dispatch.forward(request, response);
			} else {

//				GsonBuilder gBuilder = new GsonBuilder();
//				Gson gson = gBuilder.create();
//				
//			    String json = gson.toJson(login);
//			    
			    response.setContentType("text/plain");
			    response.setCharacterEncoding("UTF-8");
			    response.getWriter().write("ika");
			    
				RequestDispatcher dispatch = request.getRequestDispatcher("homepage.html");
				dispatch.forward(request, response);
			}

		} else {
			RequestDispatcher dispatch = request.getRequestDispatcher("noSuchUser.html");
			dispatch.forward(request, response);
		}
	}

}
