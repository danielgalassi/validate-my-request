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
		tableList	= FileUtils.file2array("/tmp/master_objects_list/tables");
		viewList	= FileUtils.file2array("/tmp/master_objects_list/views");
		procList	= FileUtils.file2array("/tmp/master_objects_list/procedures");
		synonList	= FileUtils.file2array("/tmp/master_objects_list/synonyms");
		seqList		= FileUtils.file2array("/tmp/master_objects_list/sequences");
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
		match();

		Iterator<DBObject> it = (nzRequest.getObjectList()).iterator();
		while (it.hasNext()) {
			object_matched = it.next();
			resultRef.put(object_matched.toString(), 0.00);
		}

		nzRequest.toXML(resultCatalog);
	}

	public void match() {
		ArrayList<DBObject> objectsList = nzRequest.getObjectList();
		Iterator<DBObject> it = objectsList.iterator();
		DBObject object_to_match = null;
		int index = 0;
		while (it.hasNext()) {
			object_to_match = (it.next());
			if (tableList.contains(object_to_match.toString()) && object_to_match.isTable()) {
				object_to_match.tag();
			}
			if (viewList.contains(object_to_match.toString()) && object_to_match.isView()) {
				object_to_match.tag();
			}
			if (synonList.contains(object_to_match.toString()) && object_to_match.isSynonym()) {
				object_to_match.tag();
			}
			if (seqList.contains(object_to_match.toString()) && object_to_match.isSequence()) {
				object_to_match.tag();
			}
			if (procList.contains(object_to_match.toString()) && object_to_match.isProcedure()) {
				object_to_match.tag();
			}
			//checks whether schema.object was entered in the object column
			if (!object_to_match.exist() && object_to_match.getName().startsWith(object_to_match.getSchema()+".")) {
				object_to_match.setComment("Maybe... you've included the schema name in the object column?");
			}
			//
			if (!object_to_match.exist()) {
				if (!object_to_match.getType().equals("TABLE")) {
					if (tableList.contains(object_to_match.toString()))
						object_to_match.setComment("This is a table, not a " + object_to_match.getType().toLowerCase());
				}
				if (!object_to_match.getType().equals("SYNONYM")) {
					if (synonList.contains(object_to_match.toString()))
						object_to_match.setComment("This is a synonym, not a " + object_to_match.getType().toLowerCase());
				}
				if (!object_to_match.getType().equals("SEQUENCE")) {
					if (seqList.contains(object_to_match.toString()))
						object_to_match.setComment("This is a sequence, not a " + object_to_match.getType().toLowerCase());
				}
				if (!object_to_match.getType().equals("VIEW")) {
					if (viewList.contains(object_to_match.toString()))
						object_to_match.setComment("This is a view, not a " + object_to_match.getType().toLowerCase());
				}
				if (!object_to_match.getType().equals("PROCEDURE")) {
					if (procList.contains(object_to_match.toString()))
						object_to_match.setComment("This is a stored procedure, not a " + object_to_match.getType().toLowerCase());
				}
			}

			objectsList.set(index, object_to_match);
			index++;
		}
		nzRequest.override(objectsList);
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
