/**
 * 
 */
package org.validator.metadata;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.validator.utils.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
	private ArrayList<DBObject> objectsList = new ArrayList<DBObject>();
	private int size = 0;

	public void toXML(String directory) {
		Document index = XMLUtils.createDOMDocument();
		Element   root = index.createElement("results");

		logger.trace("Generating results index...");
		Iterator<DBObject> it = objectsList.iterator();
		DBObject object = null;
		while (it.hasNext()) {
			object = it.next();
			Element xmlDB = index.createElement("object");
			xmlDB.setTextContent(object.toString());
			xmlDB.setAttribute("type", object.getType());
			xmlDB.setAttribute("isValid", object.exist()+"");
			xmlDB.setAttribute("comment", object.getComment());
			root.appendChild(xmlDB);
		}

		index.appendChild(root);
		XMLUtils.saveDocument(index, directory + "index.xml");
	}

	public ArrayList<DBObject> getObjectList() {
		return objectsList;
	}

	/**
	 * Instantiates a refresh request file.
	 * @param directory path to the refresh request file
	 * @param name and extension of the refresh request XLSX file
	 */
	public RefreshRequest(String directory, String xslx) {
		this.nzRequest = new File(directory + xslx);
		logger.info("Reading refresh request");

		//loading HashMap

		String objectType = "";
		String objectSchema = "";
		String objectName = "";
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(nzRequest);
		} catch (FileNotFoundException e) {
			logger.error("XLSX template {} not found", xslx);
		}

		// Finds the workbook instance for XLSX file
		XSSFWorkbook myWorkBook = null;
		try {
			myWorkBook = new XSSFWorkbook (fis);
		} catch (IOException e) {
			logger.error("Error found while opening XSLX workbook...");
		}

		// select the worksheet with the list of DB objects
		XSSFSheet dataRefreshSheet = myWorkBook.getSheet("Data Refresh");

		Iterator<Row> rowIterator = dataRefreshSheet.iterator();

		// Traversing over each row of XLSX file
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			objectSchema	= row.getCell(0).toString().toUpperCase();
			objectName		= row.getCell(1).toString().toUpperCase();
			objectType		= row.getCell(2).toString().toUpperCase();

			if (objectType.equals("TABLE") || 
					objectType.equals("VIEW") || 
					objectType.equals("SYNONYM") ||
					objectType.equals("SEQUENCE") ||
					objectType.equals("PROCEDURE")) {
				objectName = objectSchema + "." + objectName;
				objectsList.add(new DBObject(objectSchema, objectName, objectType));
			}
		}
		size = objectsList.size();
		logger.info("{} database objects loaded", objectsList.size());
	}

	public int getSize() {
		return this.size;
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

	public void matchTables(ArrayList<String> tableList) {
		if (size == 0) {
			logger.error("No objects found for validation");
			return;
		}

		Iterator<DBObject> it = objectsList.iterator();
		DBObject object_to_match = null;
		int index = 0;
		while (it.hasNext()) {
			object_to_match = (it.next());
			if (tableList.contains(object_to_match.toString()) &&
					object_to_match.isTable()) {
				object_to_match.tag();
				objectsList.set(index, object_to_match);
			}
			index++;
		}
	}

	public void matchViews(ArrayList<String> tableList) {
		if (size == 0) {
			logger.error("No objects found for validation");
			return;
		}
		Iterator<DBObject> it = objectsList.iterator();
		DBObject object_to_match = null;
		int index = 0;
		while (it.hasNext()) {
			object_to_match = (it.next());
			if (tableList.contains(object_to_match.toString()) &&
					object_to_match.isView()) {
				object_to_match.tag();
				objectsList.set(index, object_to_match);
			}
			index++;
		}
	}

	public void matchSynonym(ArrayList<String> tableList) {
		if (size == 0) {
			logger.error("No objects found for validation");
			return;
		}
		Iterator<DBObject> it = objectsList.iterator();
		DBObject object_to_match = null;
		int index = 0;
		while (it.hasNext()) {
			object_to_match = (it.next());
			if (tableList.contains(object_to_match.toString()) &&
					object_to_match.isSynonym()) {
				object_to_match.tag();
				objectsList.set(index, object_to_match);
			}
			index++;
		}
	}

	public void matchProcedure(ArrayList<String> tableList) {
		if (size == 0) {
			logger.error("No objects found for validation");
			return;
		}
		Iterator<DBObject> it = objectsList.iterator();
		DBObject object_to_match = null;
		int index = 0;
		while (it.hasNext()) {
			object_to_match = (it.next());
			if (tableList.contains(object_to_match.toString()) &&
					object_to_match.isProcedure()) {
				object_to_match.tag();
				objectsList.set(index, object_to_match);
			}
			index++;
		}
	}

	public void matchSequences(ArrayList<String> tableList) {
		if (size == 0) {
			logger.error("No objects found for validation");
			return;
		}
		Iterator<DBObject> it = objectsList.iterator();
		DBObject object_to_match = null;
		int index = 0;
		while (it.hasNext()) {
			object_to_match = (it.next());
			if (tableList.contains(object_to_match.toString()) &&
					object_to_match.isSequence()) {
				object_to_match.tag();
				objectsList.set(index, object_to_match);
			}
			index++;
		}
	}
}
