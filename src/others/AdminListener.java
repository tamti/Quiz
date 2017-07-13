package others;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import model.Administration;


/**
 * Application Lifecycle Listener implementation class AdminListener
 *
 */
@WebListener
public class AdminListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public AdminListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  { 
    	ServletContext servletCont = arg0.getServletContext();
    	Administration admin = new Administration();
    	servletCont.setAttribute("admin", admin);
    }
	
}
