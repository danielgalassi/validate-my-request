package org.validator.servlets;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.validator.engine.ValidatorEngine;
import org.validator.metadata.RefreshRequest;
import org.validator.ui.ResultPublisher;
import org.validator.utils.FileUtils;


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

	private static final int   MEMORY_THRESHOLD = 1024 * 1024 * 5;  // 5MB
	/** Maximum file size. Default value: 5MB. */
	private static final int      MAX_FILE_SIZE = 1024 * 1024 * 5; // 5MB
	private static final int   MAX_REQUEST_SIZE = 1024 * 1024 * 5; // 5MB

	private static final Logger logger = LogManager.getLogger(ValidatorService.class.getName());

	/** Application directory where UI-generating code for test results reside. */
	private static final String	uiCatalog = "/WEB-INF/Views/";

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		File             metadata = null;
//		HttpSession       session = request.getSession(true);

		//checks if the request actually contains upload file
		if (!ServletFileUpload.isMultipartContent(request)) {
			logger.error("Not a multipart request");
			request.setAttribute("ErrorMessage", "Something went horribly wrong.");
			getServletContext().getRequestDispatcher("/error.jsp").forward(request, response);
		}

		//configures upload settings,
		//sets memory threshold - beyond which files are stored in disk
		//and sets temporary location to store files
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(MEMORY_THRESHOLD);
		factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setFileSizeMax(MAX_FILE_SIZE);
		//sets maximum size of request (include file + form data)
		upload.setSizeMax(MAX_REQUEST_SIZE);

		//sets up the working directory for this session
		String sessionId = request.getRequestedSessionId();
		String uploadPath = getServletContext().getRealPath("") + File.separator + sessionId + File.separator;
		FileUtils.setupWorkDirectory(uploadPath);

		try {
			//parses the request content to extract file data
			List<FileItem> formItems = upload.parseRequest(request);

			logger.info("Processing upload request");
			if (formItems != null && formItems.size() > 0) {

				for (FileItem item : formItems) {

					System.out.println(item.toString());

					if (!item.isFormField()) {

						logger.info("Processing file...");
						String fileName = new File(item.getName()).getName();

						if (fileName.equals("")) {
							logger.error("No template file selected.");
							request.setAttribute("ErrorMessage", "A template file must be selected before submitting a validation request.");
							getServletContext().getRequestDispatcher("/error.jsp").forward(request, response);
							return;
						}

						String path = uploadPath + fileName;
						File uploaded = new File(path);
						//saves the file on disk
						item.write(uploaded);
						metadata = uploaded;

						//session.setAttribute("workDir", uploadPath);
						//session.setAttribute("metadataFile", metadata.getName());
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception: {}", e.getMessage());
			request.setAttribute("message", "There was an error: " + e.getMessage());
		}

		//		String         sessionId = request.getRequestedSessionId();
		//		HttpSession      session = request.getSession();

		//		String     workDirectory = (String) session.getAttribute("workDir");
		//String    refreshRequest = (String) session.getAttribute("metadataFile");
		String refreshRequest = metadata.getName();
		String  resultsFolder = uploadPath + "results" + File.separator;
System.out.println("XYZ");
		//setting the repository (tags are discarded if not related to the selected subject area)
		RefreshRequest nzRequest = new RefreshRequest(uploadPath, refreshRequest);

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
		publisher.setCatalogs(resultsFolder, uiCatalog);
		publisher.setContext(getServletContext());
		publisher.setParameters("SessionFolder", sessionId);

		publisher.run();

		//redirects to summary view
		String results = publisher.getResultsPage();
//		RequestDispatcher rd = request.getRequestDispatcher(results);
//		rd.forward(request, response);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		response.sendRedirect("./"+results);
		
	}
}
