package servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.AccountManager;

/**
 * Servlet implementation class ChallengeServlet
 */
@WebServlet("/ChallengeServlet")
public class ChallengeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ChallengeServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletContext servletCon = getServletContext();
		AccountManager accountMan = (AccountManager) servletCon.getAttribute("accountManager");

		String ans = request.getParameter("requestType");

		if (ans.equals("ignore")) {
			int challengeID = Integer.parseInt(request.getParameter("challengeID"));

			accountMan.denyChallenge(challengeID);
			
		} else if (ans.equals("accept")) {
			int challengeID = Integer.parseInt(request.getParameter("challengeID"));
			
			String url = request.getParameter("URL");
			
			accountMan.acceptChallenge(challengeID);

			RequestDispatcher dispatch = request.getRequestDispatcher(url);
			dispatch.forward(request, response);

		} else {
			String from = request.getParameter("fromUser");
			String to = request.getParameter("toUser");
			int quizID = Integer.parseInt(request.getParameter("quizID"));
			String msg = request.getParameter("msg");
			
			if (accountMan.usernameExists(to)) {
				accountMan.SendChallenge(from, to, quizID, msg);
			}
		}

	}

}
