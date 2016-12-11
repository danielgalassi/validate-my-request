/**
 * 
 */
package org.validator.ui;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.validator.utils.FileUtils;
import org.validator.utils.XMLUtils;

/**
 * This class transforms the XML result files into user-friendly web pages.
 * @author danielgalassi@gmail.com
 *
 */
public class ResultPublisher {

	private static final Logger logger = LogManager.getLogger(ResultPublisher.class.getName());
	/** Path to the stylesheets used to create HTML pages. */
	String				   viewCatalog = "";
	/** Path where results are stored. */
	String				 resultCatalog = "";
	/** Scope of the servlet session. */
	ServletContext			   context = null;
	/** File containing all result entries. */
	File						 index;
	/** Different HTML pages to generate (names of stylesheets and HTML pages are the same). */
	Vector<String>				 pages = new Vector<String>();
	/** Parameters passed to stylesheets. This makes the process of generating HTML pages more flexible. */
	Map<String, String>			params = new HashMap<String, String> ();

	/**
	 * Publisher constructor. Catalogues, context and publishing parameters will be setup following a builder-type approach. 
	 */
	public ResultPublisher () {
		logger.info("Creating a publisher");
	}

	/**
	 * Sets the directories where results and stylesheets are stored.
	 * @param resultCatalog path to results
	 * @param viewCatalog path to stylesheets
	 */
	public void setCatalogs(String resultCatalog, String viewCatalog) {
		this.resultCatalog = resultCatalog;
		this.viewCatalog = viewCatalog;
		index = new File(resultCatalog + "index.xml");
	}

	/**
	 * Sets the scope of the servlet, it is used to load stylesheets.
	 * @param context scope of servlet session
	 */
	public void setContext(ServletContext context) {
		this.context = context;
	}

	/**
	 * Name, value pairs used to generate stylesheet parameters.
	 * @param name argument referenced in stylesheets
	 * @param value literal evaluated during the transformation
	 */
	public void setParameters(String name, String value) {
		params.put(name, value);
	}

	/**
	 * Creates HTML pages.
	 */
	private void generatePages() {
		pages.add("Results");
		for (String page : pages) {
			logger.info("Generating {} page", page);
			String stylesheet = viewCatalog + page + ".xsl";
			InputStream xsl2html = context.getResourceAsStream(stylesheet);
			//XMLUtils.applyStylesheet(index, xsl2html, resultCatalog + page + ".html", params);
			XMLUtils.applyStylesheet(index, xsl2html, resultCatalog + "Results.html", params);
		}
		//pages.remove("Summary");
	}

	/**
	 * Creates a compressed file with a summary and detail pages.
	 */
	private void generateZip() {
		FileUtils.archive(resultCatalog,  pages, "Results.zip");
	}

	/**
	 * Creates all HTML pages used for browsing and downloading.
	 */
	public void run() {
		if (!ready()) {
			logger.error("Publisher is not ready");
			return;
		}
		logger.trace("Publishing results...");
		generatePages();
		generateZip();
	}

	/**
	 * Verifies that the Publisher has been properly setup
	 * @return true if catalogues, context and parameters are valid
	 */
	private boolean ready() {
		boolean isResultCatalogSet = !resultCatalog.equals("");
		boolean   isViewCatalogSet = !viewCatalog.equals("");
		boolean       isContextSet = !(context == null);
		boolean      isParamMapSet = !params.isEmpty();
		return (isResultCatalogSet && isViewCatalogSet && isContextSet && isParamMapSet);
	}

	/**
	 * Publishes the location of the summary page.
	 * @return the summary page location in the context of the servlet session
	 */
	public String getResultsPage() {
		return File.separator + params.get("SessionFolder") + File.separator + "results" + File.separator + "Results.html";
	}
}
