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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext servletCon = getServletContext();
		AccountManager accountMan = (AccountManager) servletCon.getAttribute("accountManager");
		
		int challengeID = Integer.parseInt(request.getParameter("challengeID"));
		
		String ans = request.getParameter("response");
		
		if (ans.equals("accept")) {
			accountMan.acceptChallenge(challengeID);
			
			String quizURL = request.getParameter("quizURL");
			
			RequestDispatcher dispatch = request.getRequestDispatcher(quizURL);
			dispatch.forward(request, response);
			
		} else {
			accountMan.denyChallenge(challengeID);

			String requestUrl = request.getParameter("requestURL");
			
			RequestDispatcher dispatch = request.getRequestDispatcher(requestUrl);
			dispatch.forward(request, response);
		}
	}

}
