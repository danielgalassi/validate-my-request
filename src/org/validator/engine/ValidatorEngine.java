package org.validator.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.validator.metadata.DBObject;
import org.validator.metadata.RefreshRequest;
import org.validator.utils.FileUtils;


/**
 * The validator engine orchestrates and controls the execution of the test suite.
 * A <code>ValidatorEngine</code> consists of two main components, the repository refresh request and a collection of tests.
 * The refresh request in the <code>Repository</code> will be validated using <code>Test</code> instances.
 * Each time the engine runs, the execution of each test is timed.
 * @author danielgalassi@gmail.com
 *
 */
public class ValidatorEngine {

	private static final Logger logger = LogManager.getLogger(ValidatorEngine.class.getName());
	/** The target directory where validation results will be saved. */
	private String resultCatalog = "";
	/** An OBIEE refresh request repository object. */
	private RefreshRequest nzRequest = null;
	private ArrayList<String> tableList = null;
	private ArrayList<String> viewList = null;
	private ArrayList<String> procList = null;
	private ArrayList<String> synonList = null;
	private ArrayList<String> seqList = null;

	/**
	 * Validator Engine constructor.
	 * Repository and Session Directory are set using independent methods.
	 */
	public ValidatorEngine() {
		logger.info("Initialising Validator Engine");
	}

	public void loadMasterLists() {
		tableList	= FileUtils.file2array("/home/danno/tmp/master_objects_list/tables");
		viewList	= FileUtils.file2array("/home/danno/tmp/master_objects_list/views");
		procList	= FileUtils.file2array("/home/danno/tmp/master_objects_list/procedures");
		synonList	= FileUtils.file2array("/home/danno/tmp/master_objects_list/synonyms");
		seqList		= FileUtils.file2array("/home/danno/tmp/master_objects_list/sequences");
	}

	/**
	 * Sets the request file to assess in this validator engine.
	 * @param nzRequest refresh request repository object
	 */
	public void setNzRequest(RefreshRequest nzRequest) {
		this.nzRequest = nzRequest;
	}

	/**
	 * Sets the internal directory all results will be saved to.
	 * @param resultCatalogLocation path to the target directory
	 */
	public void setResultsFolder(String resultCatalogLocation) {
		this.resultCatalog = resultCatalogLocation;
		FileUtils.setupWorkDirectory(resultCatalogLocation);		
	}

	/**
	 * Executes the validation of the refresh request.
	 * Upon completion, an index document (catalog) is created. Each entry in this catalog
	 * points to a result file.
	 */
	public void run() {
		Map <String, Double> resultRef = new HashMap<String, Double>();
		DBObject		object_matched = null;
		if (!ready()) {
			logger.error("Engine is not ready");
			if (nzRequest.getSize() == 0) {
				logger.error("No objects to validate in this request");
			}
			return;
		}

		logger.info("Executing tests...");
		nzRequest.matchTables(tableList);
		nzRequest.matchViews(viewList);
		nzRequest.matchSynonym(synonList);
		nzRequest.matchSequences(seqList);
		nzRequest.matchProcedure(procList);

		Iterator<DBObject> it = (nzRequest.getObjectList()).iterator();
		while (it.hasNext()) {
			object_matched = it.next();
			resultRef.put(object_matched.toString(), 0.00);
		}

		nzRequest.toXML(resultCatalog);
	}

	/**
	 * Validates the repository and test suite have been setup and are available.
	 * @return true if all dependencies are met
	 */
	private boolean ready() {
		boolean isNZRequestSet = (nzRequest != null && nzRequest.getSize() > 0);
		boolean isResultDirSet	= !resultCatalog.equals("");

		logger.info("isNZRequestSet = {}", isNZRequestSet);
		logger.info("isResultDirSet = {}", isResultDirSet);
		return (isNZRequestSet && isResultDirSet);
	}
}
