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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * A basic class to model data refresh items retrieved from the XSLX file.
 * @author danielgalassi@gmail.com
 *
 */
@XmlRootElement(name = "test")
public class RefreshRequest {

	/** Log4j2 main interface */
	private static final Logger logger = 
			LogManager.getLogger(RefreshRequest.class.getName());
	/** The refresh request file in XLSX format stored in
	 * the filesystem under the session directory. */
	private File nzRequest = null;
	@XmlElement(name = "object")
	/** Collection of database objects. */
	private ArrayList<DBObject> objectsList = new ArrayList<DBObject>();
	/** number of database objects in the refresh request */
	private int size = 0;

	/**
	 * Constructor to facilitate the serialisation process
	 */
	public RefreshRequest () {
	}

	/**
	 * Getter method for the requests' collection of objects
	 * @return collection of database objects
	 */
	public ArrayList<DBObject> getObjectList() {
		return objectsList;
	}

	/**
	 * Loads all db objects from the Excel file using the Apache POI package
	 * @param directory path to the refresh request file
	 * @param xslx Excel (template) file
	 */
	public RefreshRequest(String directory, String xslx) {
		this.nzRequest = new File(directory + xslx);
		logger.info("Reading refresh request");

		String objType = "";
		String objSchema = "";
		String objName = "";
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

		//skips the header details of the request
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			if (row.getCell(0).toString().toUpperCase().equals("SCHEMA")) {
				break;
			}
		}
		// Traversing over each row of XLSX file
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();

			//skips blank rows
			try {
			if (row.getCell(0).toString().length() > 0) {
				objSchema	= row.getCell(0).toString().toUpperCase();
				objName		= row.getCell(1).toString().toUpperCase();
				objType		= row.getCell(2).toString().toUpperCase();
				if (objType.contains("STOR") || objType.contains("PROC")) {
					objType = "PROCEDURE";
				}

				if (objType.equals("TABLE") || 
						objType.equals("VIEW") || 
						objType.equals("SYNONYM") ||
						objType.equals("SEQUENCE") ||
						objType.equals("PROCEDURE")) {
					objectsList.add(new DBObject(objSchema, objName, objType));
				}
			}
			} catch (Exception e) {
				logger.error("Exception thrown while reading request: {}", 
						e.getMessage());
			}
		}
		size = objectsList.size();
		logger.info("{} database objects loaded", objectsList.size());
	}

	@XmlAttribute
	public int getSize() {
		return this.size;
	}

	/**
	 * Evaluates whether the file can be found and opened
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
	 * Replaces the original list of objects with a new collection
	 * @param objectsList new objects' list
	 */
	public void override(ArrayList<DBObject> objectsList) {
		this.objectsList = objectsList;
	}

	/**
	 * Position of the database object in this request
	 * @return true if the object name is found
	 */
	public boolean contains(String objectName, String objectType) {
		boolean found = false;
		Iterator<DBObject> it = objectsList.iterator();
		while (it.hasNext()) {
			DBObject object = it.next();
			if (object.getFullObject().equals(objectName) && 
					object.getType().equals(objectType)) {
				found = true;
				break;
			}
		}
		return found;
	}
}
