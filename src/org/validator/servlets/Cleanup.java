package org.validator.servlets;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.validator.utils.FileUtils;

/**
 * Servlet to cleanup stale entries from the result catalogue.
 * @author danielgalassi@gmail.com
 */
@WebServlet(description = "This servlet can be used to cleanup old results", urlPatterns = { "/Cleanup" })
public class Cleanup extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LogManager.getLogger(FileHandler.class.getName());

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] sessions = request.getParameterValues("sessionFolder");
		String uploadPath = getServletContext().getRealPath("") + File.separator;

		if (sessions != null) {
			for (int i=0; i < sessions.length; i++) {
				FileUtils.deleteAll(new File(uploadPath + sessions[i]));
				logger.info("Session " + sessions[i] + " deleted.");
			}
		}
		response.sendRedirect(request.getContextPath() + "/cleanup");
	}
}
