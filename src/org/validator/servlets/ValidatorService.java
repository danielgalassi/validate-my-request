package org.validator.servlets;

import java.io.File;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.validator.engine.ValidatorEngine;
import org.validator.metadata.RefreshRequest;
import org.validator.ui.ResultPublisher;


/**
 * This service sets up the <code>ValidatorEngine</code> and the <code>ResultPublisher</code>.
 * The <code>ValidatorEngine</code> executes all "tests" on the uploaded request.
 * The <code>ResultPublisher</code> generates the user-friendly web pages featuring results.
 * @author danielgalassi@gmail.com
 *
 */
@WebServlet(description = "This controller orchestrates the validation services.", urlPatterns = { "/ValidatorService" })
public class ValidatorService extends HttpServlet {

	private static final long	serialVersionUID = 1L;

	private static final Logger logger = LogManager.getLogger(ValidatorService.class.getName());

	/** Application directory where UI-generating code for test results reside. */
	private static final String	viewCatalogue = "/WEB-INF/Views/";

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String         sessionId = request.getRequestedSessionId();
		HttpSession      session = request.getSession();

		String     workDirectory = (String) session.getAttribute("workDir");
		String    refreshRequest = (String) session.getAttribute("metadataFile");
		String     resultsFolder = workDirectory + "results" + File.separator;

		//setting the repository (tags are discarded if not related to the selected subject area)
		RefreshRequest nzRequest = new RefreshRequest(workDirectory, refreshRequest);

		if (!nzRequest.available()) {
			logger.error("Netezza request template missing!");
			request.setAttribute("ErrorMessage", "Netezza request template not found.");
			getServletContext().getRequestDispatcher("/error.jsp").forward(request, response);
			return;
		}

		//setting up the validator engine with the request file and master objects lists
		ValidatorEngine engine = new ValidatorEngine();
		engine.loadMasterLists();
		engine.setNzRequest(nzRequest);
		engine.setResultsFolder(resultsFolder);

		//executing verification
		engine.run();

		//publishes HTML pages featuring results to the session folder
		ResultPublisher publisher = new ResultPublisher();
		publisher.setCatalogs(resultsFolder, viewCatalogue);
		publisher.setContext(getServletContext());
		publisher.setParameters("SessionFolder", sessionId);

		publisher.run();

		//redirects to summary view
		String results = publisher.getResultsPage();
		RequestDispatcher view = request.getRequestDispatcher(results);
		view.forward(request, response);
	}
}
