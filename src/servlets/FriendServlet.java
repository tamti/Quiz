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
 * Servlet implementation class FriendServlet
 */
@WebServlet("/FriendServlet")
public class FriendServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FriendServlet() {
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
		
		String requestType = (String) request.getParameter("requestType");

		String user1 = (String) request.getParameter("fromUser");
		String user2 = (String) request.getParameter("toUser");

		switch (requestType) {
		case "addFriend":
			accountMan.sendFriendRequest(user1, user2);
			break;
		case "removeFriend":
			accountMan.removeFromFriendsOf(user1, user2);
			break;
		case "chalRequest" :
			int challengeID = Integer.parseInt(request.getParameter("challengeID"));
			String ans = (String) request.getParameter("response");
			
			if (ans.equals("accept")) {
				accountMan.acceptChallenge(challengeID);
				
				String quizURL = request.getParameter("quizURL");
				
				RequestDispatcher dispatch = request.getRequestDispatcher(quizURL);
				dispatch.forward(request, response);
				
			} else {
				accountMan.denyChallenge(challengeID);
			}
			
			break;
		}
	}

}
