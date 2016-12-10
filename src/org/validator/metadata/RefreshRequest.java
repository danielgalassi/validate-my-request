/**
 * 
 */
package org.validator.metadata;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A basic class to model data refresh items.
 * @author danielgalassi@gmail.com
 *
 */
public class RefreshRequest {

	private static final Logger logger = LogManager.getLogger(RefreshRequest.class.getName()); 
	/** The refresh request file in XLSX format stored in the filesystem under the session directory. */
	private File nzRequest = null;
	/** The session directory where the refresh request is stored. */
//	private File directory = null;

	/**
	 * Instantiates a refresh request file.
	 * @param directory path to the repository refresh request file
	 * @param repository name and extension of the refresh request repository file
	 * @param selectedSubjectArea the name of the subject area selected from the combo box
	 */
	public RefreshRequest(String directory, String repository) {
		//this.directory  = new File(directory);
		this.nzRequest = new File(directory + repository);
		logger.info("Creating refresh request");
	}

	/**
	 * Evaluates the status of the file.
	 * @return true if the file is found and can be read
	 */
	public boolean available() {
		boolean isAvailable = false;
		if (nzRequest != null) {
			isAvailable = (nzRequest.exists() && nzRequest.canRead());
		}
		return isAvailable;
	}

	/**
	 * Returns a file representing the repository.
	 * @return a File reference to the repository file
	 */
	public File toFile() {
		return nzRequest;
	}
}
