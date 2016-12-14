package org.validator.servlets;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.validator.utils.FileUtils;


/**
 * Servlet to handle the first step of the validation process.
 * Work directory and ZIP extraction are managed by this servlet.
 * @author danielgalassi@gmail.com
 *
 */
//@WebServlet(description = "This controller handles file-based requests", urlPatterns = { "/FileHandler" })
@WebServlet(description = "processes file-upload requests", urlPatterns = {"/FileHandler"})
public class FileHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LogManager.getLogger(FileHandler.class.getName());

	private static final int   MEMORY_THRESHOLD = 1024 * 1024 * 2;  // 2MB
	/** Maximum file size. Default value: 5MB. */
	private static final int      MAX_FILE_SIZE = 1024 * 1024 * 5; // 5MB
	private static final int   MAX_REQUEST_SIZE = 1024 * 1024 * 5; // 5MB

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		File           metadata = null;
		HttpSession     session = request.getSession(true);

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
		String sSessionId = request.getRequestedSessionId();
		String uploadPath = getServletContext().getRealPath("") + File.separator + sSessionId + File.separator;
		FileUtils.setupWorkDirectory(uploadPath);

		try {
			//parses the request content to extract file data
			List<FileItem> formItems = upload.parseRequest(request);

			logger.info("Processing upload request");
			if (formItems != null && formItems.size() > 0) {

				for (FileItem item : formItems) {
					if (item.isFormField()) {
						String name = item.getFieldName();
						String value = item.getString();
						logger.trace("Form selection: {}={}", name, value);
						request.setAttribute(name, value);
						session.setAttribute(name, value);
					}

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

						session.setAttribute("workDir", uploadPath);
						session.setAttribute("metadataFile", metadata.getName());
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception: {}", e.getMessage());
			request.setAttribute("message", "There was an error: " + e.getMessage());
		}
		//redirects client to message page
		//getServletContext().getRequestDispatcher("/SubjectAreaSelector").forward(request, response);
		//		getServletContext().getRequestDispatcher("/ValidatorService").forward(request, response);
	}
}
