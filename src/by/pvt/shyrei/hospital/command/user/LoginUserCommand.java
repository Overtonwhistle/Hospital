package by.pvt.shyrei.hospital.command.user;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import by.pvt.shyrei.hospital.command.ActionCommand;
import by.pvt.shyrei.hospital.command.ClientType;
import by.pvt.shyrei.hospital.connectpool.ConnectionPool;
import by.pvt.shyrei.hospital.dao.UserDAO;
import by.pvt.shyrei.hospital.entity.User;
import by.pvt.shyrei.hospital.resources.ConfigurationManager;
import by.pvt.shyrei.hospital.resources.MessageManager;

/**
 * @author Shyrei Uladzimir Login users class and set access level to session
 */
public class LoginUserCommand implements ActionCommand {

	private final String LOGIN = "login";
	private final String PASSWORD = "password";
	private static final Logger logger = LogManager.getLogger(ConnectionPool.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * by.pvt.shyrei.hospital.command.ActionCommand#execute(javax.servlet.http.
	 * HttpServletRequest)
	 */
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String page = ConfigurationManager.getProperty("path.page.login");
		String login = request.getParameter(LOGIN);
		String password = request.getParameter(PASSWORD);
		try {
			User user = UserDAO.getInstance().getUser(login, password);
			if (user == null) {
				HttpSession session = request.getSession(true);
				session.setAttribute("errorLoginPassMessage", MessageManager.getProperty("message.loginformerror"));
			} else {
				if (user.getAccessLevel() == 2) {
					request.setAttribute("user", login);
					HttpSession session = request.getSession(true);
					session.setAttribute("userType", ClientType.ADMINISTRATOR);
					session.setAttribute("user", login);
					page = ConfigurationManager.getProperty("path.page.main");
				}
				if (user.getAccessLevel() == 1) {
					request.setAttribute("user", login);
					HttpSession session = request.getSession(true);
					session.setAttribute("userType", ClientType.USER);
					session.setAttribute("user", login);
					page = ConfigurationManager.getProperty("path.page.user");
				}
			}
		} catch (SQLException e) {
			logger.log(Level.FATAL, "SQLException - can't login user : " + e.toString());
			HttpSession session = request.getSession(true);
			session.setAttribute("errorDBMessage", MessageManager.getProperty("message.DBerror"));
		}
		return page;
	}
}
