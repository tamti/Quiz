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

import model.AccountManager;

/**
 * Servlet implementation class RegistrationServlet
 */
@WebServlet("/RegistrationServlet")
public class RegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletContext servletCon = getServletContext();
		AccountManager accountMan = (AccountManager) servletCon.getAttribute("accountManager");

		String firstName = request.getParameter("FirstName");
		String lastName = request.getParameter("LastName");
		String email = request.getParameter("Mail");
		String username = request.getParameter("Username");
		String password = request.getParameter("Password");
		
		HttpSession session = request.getSession();

		session.setAttribute("name",firstName);
		session.setAttribute("username",username);

		/**if (accountMan.usernameExists(username)) {

			RequestDispatcher dispatch = request.getRequestDispatcher("nameInUse.html");
			dispatch.forward(request, response);

		}*/  if (password.length() < 6) {

			request.setAttribute("signupPasError","Short password, It must be at least 6 characters!");
			request.getRequestDispatcher("signup.html").forward(request, response);
		} else if (accountMan.createAccount(firstName, lastName, email, username, password)) {
			
			RequestDispatcher dispatch = request.getRequestDispatcher("homepage.jsp");
			dispatch.forward(request, response);

		} else {

			RequestDispatcher dispatch = request.getRequestDispatcher("");
			dispatch.forward(request, response);
		}
	}

}
