package org.validator.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.validator.metadata.DBObject;
import org.validator.metadata.RefreshRequest;
import org.validator.utils.FileUtils;


/**
 * The <code>ValidatorEngine</code> orchestrates the test process.
 * Each <code>DBObject</code> in the <code>RefreshRequest</code> is validated 
 * using by this orchestration.
 * The results are serialised using <a href="https://docs.oracle.com/javase/8/docs/api/javax/xml/bind/JAXB.html">Java Architecture for XML Binding</a>.
 * @author danielgalassi@gmail.com
 */
public class ValidatorEngine {

	/** Log4j2 central interface*/
	private static final Logger logger = LogManager.getLogger(
			ValidatorEngine.class.getName());
	/** The target directory where validation results will be saved. */
	private String resultCatalog = "";
	/** An refresh request repository object. */
	private RefreshRequest nzRequest = null;
	/** List of Netezza tables */
	private ArrayList<String> tableList = null;
	/** List of Netezza views */
	private ArrayList<String> viewList = null;
	/** List of Netezza stored procedures */
	private ArrayList<String> procList = null;
	/** List of Netezza synonyms */
	private ArrayList<String> synonList = null;
	/** List of Netezza sequences */
	private ArrayList<String> seqList = null;

	/**
	 * Validator Engine constructor.
	 * Repository and Session Directory are set using independent methods.
	 */
	public ValidatorEngine() {
		logger.info("Initialising Validator Engine");
	}

	/**
	 * Loads all reference data used in the validation process
	 */
	public void loadMasterLists() {
		viewList	= FileUtils.file2array("/tmp/master_objects_list/views");
		tableList	= FileUtils.file2array("/tmp/master_objects_list/tables");
		synonList	= FileUtils.file2array("/tmp/master_objects_list/synonyms");
		seqList		= FileUtils.file2array("/tmp/master_objects_list/sequences");
		procList	= FileUtils.file2array("/tmp/master_objects_list/procedures");
	}

	/**
	 * Sets the refresh request for the validation process
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
	 * The validation process consists of the following steps:
	 * 1. matching of each <code>DBObject</code> against the master list of reference data
	 * 2. identifies whether the schema was included in the object column
	 * 3. identifies whether the object name was entered with quotation marks
	 * 4. identifies incorrect object types (table instead of a view, etc.)
	 * 5. identifies incorrect schemas (table found in a different schema than that in the request)
	 * 6. warns of objects with the same name (i.e. table and synonym)
	 * Upon completion, an index document (catalog) is created.
	 * Each entry in this catalog points to a result file.
	 * TODO: simplify some of these validations
	 */
	public void run() {
		if (!ready()) {
			logger.error("Engine is not ready");
			if (nzRequest.getSize() == 0) {
				logger.error("No objects to validate in this request");
			}
			return;
		}

		logger.info("Executing tests...");

		ArrayList<DBObject> objectsList = nzRequest.getObjectList();
		Iterator<DBObject> it = objectsList.iterator();
		DBObject req_item = null;
		int index = 0;
		while (it.hasNext()) {
			req_item = (it.next());
			if (tableList.contains(req_item.toString()) && req_item.isTable()) {
				req_item.tag();
			}
			if (viewList.contains(req_item.toString()) &&  req_item.isView()) {
				req_item.tag();
			}
			if (synonList.contains(req_item.toString()) && req_item.isSynonym()) {
				req_item.tag();
			}
			if (seqList.contains(req_item.toString()) && req_item.isSequence()) {
				req_item.tag();
			}
			if (procList.contains(req_item.toString()) && req_item.isProcedure()) {
				req_item.tag();
			}

			//validates whether the schema in the request is incorrect
			if (!req_item.exist()) {
				Iterator <String> dbList = null;
				String type = req_item.getType();
				switch (type) {
				case "VIEW" : dbList = viewList.iterator();
				break;
				case "TABLE" : dbList = tableList.iterator();
				break;
				case "SYNONYM" : dbList = synonList.iterator();
				break;
				case "SEQUENCE" : dbList = seqList.iterator();
				break;
				case "PROCEDURE" : dbList = procList.iterator();
				break;
				}
				String[] object;
				while (dbList.hasNext()) {
					object = dbList.next().split("\\.");
					if (object[1].equals(req_item.getName())) {
						req_item.setComment("You may find this object in " + 
								object[0] + " instead...");
						break;
					}
				}
			}

			//checks whether schema.object was entered in 	the object column
			if (!req_item.exist() && req_item.getName().indexOf(".") > -1) {
				req_item.setComment("Maybe... you've included the schema "
						+ "name in the object column?");
			}

			/* Validates whether the object has extra chars in the request 
			 * (such as quotes).
			 */
			String comment = "Please remove quotes or other odd characters.";
			if (!req_item.exist()) {
				if (tableList.contains(req_item.toString().replaceAll("\"", "").replaceAll("[^\\x00-\\x7F]", "")) && 
						req_item.isTable()) {
					req_item.setComment(comment);
				}
				if (viewList.contains(req_item.toString().replaceAll("\"", "").replaceAll("[^\\x00-\\x7F]", "")) && 
						req_item.isView()) {
					req_item.setComment(comment);
				}
				if (synonList.contains(req_item.toString().replaceAll("\"", "").replaceAll("[^\\x00-\\x7F]", "")) && 
						req_item.isSynonym()) {
					req_item.setComment(comment);
				}
				if (seqList.contains(req_item.toString().replaceAll("\"", "").replaceAll("[^\\x00-\\x7F]", "")) && 
						req_item.isSequence()) {
					req_item.setComment(comment);
				}
				if (procList.contains(req_item.toString().replaceAll("\"", "").replaceAll("[^\\x00-\\x7F]", "")) && 
						req_item.isProcedure()) {
					req_item.setComment(comment);
				}
			}

			//validates whether a table is actually / also a view... etc
			if (!req_item.getType().equals("TABLE")) {
				if (tableList.contains(req_item.toString())) {
					if (!req_item.exist()) {
						req_item.setComment("This is a table, not a " + req_item.getType().toLowerCase() + ".");
					} else {
						//i.e. if a synonym has the same name as a table... it gives the requestor a hint
						if (!nzRequest.contains(req_item.getFullObject(), "TABLE")) {
							req_item.setComment("There is a table with the same name, should it be included in this request?");
						}
					}
				}
			}
			if (!req_item.getType().equals("SYNONYM")) {
				if (synonList.contains(req_item.toString())) {
					if (!req_item.exist()) {
						req_item.setComment("This is a synonym, not a " + req_item.getType().toLowerCase() + ".");
					} else {
						if (!nzRequest.contains(req_item.getFullObject(), "SYNONYM")) {
							req_item.setComment("There is a synonym with the same name, should it be included in this request?");
						}
					}
				}
			}
			if (!req_item.getType().equals("SEQUENCE")) {
				if (seqList.contains(req_item.toString())) {
					if (!req_item.exist()) {
						req_item.setComment("This is a sequence, not a " + req_item.getType().toLowerCase() + ".");
					} else {
						if (!nzRequest.contains(req_item.getFullObject(), "SEQUENCE")) {
							req_item.setComment("There is a sequence with the same name, should it be included in this request?");
						}
					}
				}
			}
			if (!req_item.getType().equals("VIEW")) {
				if (viewList.contains(req_item.toString())) {
					if (!req_item.exist()) {
						req_item.setComment("This is a view, not a " + req_item.getType().toLowerCase() + ".");
					} else {
						if (!nzRequest.contains(req_item.getFullObject(), "VIEW")) {
							req_item.setComment("There is a view with the same name, should it be included in this request?");
						}
					}
				}
			}
			if (!req_item.getType().equals("PROCEDURE")) {
				if (procList.contains(req_item.toString())) {
					if (!req_item.exist()) {
						req_item.setComment("This is a stored procedure, not a " + req_item.getType().toLowerCase() + ".");
					} else {
						if (!nzRequest.contains(req_item.getFullObject(), "PROCEDURE")) {
							req_item.setComment("There is a procedure with the same name, should it be included in this request?");
						}
					}
				}
			}
			objectsList.set(index, req_item);
			nzRequest.override(objectsList);
			index++;
		}

		logger.info("All tests executed...");

		serialiseResults();
	}

	/**
	 * JAXB serialisation of the validation results
	 */
	private void serialiseResults() {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(RefreshRequest.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			//Marshal the list in console
			//jaxbMarshaller.marshal(nzRequest, System.out);

			//Marshal the list in file
			jaxbMarshaller.marshal(nzRequest, new File(resultCatalog + "index.xml"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Validates the engine has been fully setup to avoid triggering a process 
	 * without a loaded request or without a target directory for the results
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
