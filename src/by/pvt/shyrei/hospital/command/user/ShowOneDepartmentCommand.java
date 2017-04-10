package by.pvt.shyrei.hospital.command.user;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.pvt.shyrei.hospital.command.ActionCommand;
import by.pvt.shyrei.hospital.connectpool.ConnectionPool;
import by.pvt.shyrei.hospital.dao.DepartmentDAO;
import by.pvt.shyrei.hospital.entity.Department;
import by.pvt.shyrei.hospital.resources.ConfigurationManager;

public class ShowOneDepartmentCommand implements ActionCommand {
	private static final Logger logger = LogManager.getLogger(ConnectionPool.class);
	private final String DEPID = "depID";

	/* (non-Javadoc)
	 * @see by.pvt.shyrei.hospital.command.ActionCommand#execute(javax.servlet.http.HttpServletRequest)
	 */
	public String execute(HttpServletRequest request) {
		String page = null;
		ArrayList<Department> list;
		try {
			int dep = Integer.parseInt(request.getParameter(DEPID));
			list = DepartmentDAO.getInstance().showOneDepartment(dep);
			int listSize = list.size();
			request.setAttribute("list", list);
			request.setAttribute("listSize", listSize);
			page = ConfigurationManager.getProperty("path.page.showonedepartments");

		} catch (SQLException e) {
			logger.log(Level.FATAL, "SQLException - can't show list of departments : " + e.toString());	
		}
		return page;
	}

}