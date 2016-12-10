package org.validator.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.validator.metadata.RefreshRequest;
import org.validator.utils.FileUtils;
import org.validator.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


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

	/**
	 * Validator Engine constructor.
	 * Repository and Session Directory are set using independent methods.
	 */
	public ValidatorEngine() {
		logger.info("Initialising Validator Engine");
	}

	public void loadMasterLists() {
		tableList	= FileUtils.file2array("/tmp/master_objects_list/tables");
		viewList	= FileUtils.file2array("/tmp/master_objects_list/views");
		procList	= FileUtils.file2array("/tmp/master_objects_list/procedures");
		synonList	= FileUtils.file2array("/tmp/master_objects_list/synonyms");
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
		Map <String, Double>    resultRef = new HashMap<String, Double>();

		if (!ready()) {
			logger.error("Engine is not ready");
			return;
		}

		//executes all scripts in test suite and times them
		logger.info("Executing tests...");
		/*
		for (Test test : testSuite) {
			startTimer = System.currentTimeMillis();
			String result = resultCatalogue + test.getName() + ".xml";
			test.assertMetadata(repository, result);
			resultRef.put(result, (double) (System.currentTimeMillis() - startTimer) / 1000);
		}
		 */

		createIndexDocument(resultRef);
	}

	/**
	 * Validates the repository and test suite have been setup and are available.
	 * @return true if all dependencies are met
	 */
	private boolean ready() {
		boolean isRepositorySet = (nzRequest != null);
		boolean isResultDirSet	= !resultCatalog.equals("");

		logger.info("isRepositorySet = {}", isRepositorySet);
		logger.info("isResultDirSet = {}", isResultDirSet);
		return (isRepositorySet && isResultDirSet);
	}

	/**
	 * Generates a catalog file with a list of tests.
	 * @param resultRefs a Map with a (result file, elapsed time) entry for each test executed
	 */
	private void createIndexDocument (Map <String, Double> resultRefs) {
		Document index = XMLUtils.createDOMDocument();
		Element   root = index.createElement("index");

		logger.trace("Creating results index");
		for (Map.Entry <String, Double> ref : resultRefs.entrySet()) {
			Element node = index.createElement("results");
			node.setTextContent(ref.getKey());
			root.appendChild(node);
		}

		index.appendChild(root);
		XMLUtils.saveDocument(index, resultCatalog + "index.xml");
	}
}
