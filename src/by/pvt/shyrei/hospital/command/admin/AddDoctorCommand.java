package by.pvt.shyrei.hospital.command.admin;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import by.pvt.shyrei.hospital.command.ActionCommand;
import by.pvt.shyrei.hospital.connectpool.ConnectionPool;
import by.pvt.shyrei.hospital.dao.DoctorDAO;
import by.pvt.shyrei.hospital.entity.Doctor;
import by.pvt.shyrei.hospital.resources.ConfigurationManager;
import by.pvt.shyrei.hospital.resources.MessageManager;

/**
 * @author Shyrei Uladzimir Add doctor command
 */
public class AddDoctorCommand implements ActionCommand {

	private static final String DOCTOR_NAME = "name";
	private static final String DEPARTMENT_ID = "Departments_id";
	private static final String DOCTOR_SPECIALIZATION = "specialization";
	private static final Logger logger = LogManager.getLogger(ConnectionPool.class);
	String errMessage = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * by.pvt.shyrei.hospital.command.ActionCommand#execute(javax.servlet.http.
	 * HttpServletRequest)
	 */
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String page = ConfigurationManager.getProperty("path.page.adddoctor");
		try {
			Doctor doctor = new Doctor();
			doctor.setName(request.getParameter(DOCTOR_NAME));
			doctor.setSpecialization(request.getParameter(DOCTOR_SPECIALIZATION));
			doctor.setDepId(request.getParameter(DEPARTMENT_ID));
			if ((errMessage = checkEmpty(doctor)) != null) {
				request.setAttribute("errorAddDoctorPassMessage", checkEmpty(doctor));
				request.getRequestDispatcher(page).forward(request, response);
				return null;
			}
			DoctorDAO.getInstance().addDoctor(doctor);
			request.setAttribute("successAddDoctorPassMessage", MessageManager.getProperty("message.successadddoctor"));
			request.getRequestDispatcher(page).forward(request, response);
			return null;
		} catch (SQLException e) {
			request.setAttribute("errorAddDoctorPassMessage", MessageManager.getProperty("message.add.doctor.error"));
			logger.log(Level.FATAL, "SQLException - can't add doctor : " + e.toString());
			page = ConfigurationManager.getProperty("path.page.adddoctor");
		}
		return page;
	}

	/**
	 * @param doctor
	 *            check doctor
	 * @return Message
	 */
	private String checkEmpty(Doctor doctor) {
		if ((doctor.getName() == null) || (doctor.getName().equals(""))) {
			return MessageManager.getProperty("message.nameerror");
		}
		if ((doctor.getSpecialization() == null) || (doctor.getSpecialization().equals(""))) {
			return MessageManager.getProperty("message.specializationerror");
		}
		if ((doctor.getDepId() == null) || (doctor.getDepId().equals(""))) {
			return MessageManager.getProperty("message.departmenterror");
		}
		return null;
	}
}
