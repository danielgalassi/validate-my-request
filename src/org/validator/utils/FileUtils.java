package org.validator.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.InputSource;

/***
 * Set of methods to handle IO file operations.
 * @author danielgalassi@gmail.com
 *
 */
public class FileUtils {

	private static final Logger logger = LogManager.getLogger(FileUtils.class.getName());

	/**
	 * Converts text files to ArrayLists
	 * @param file full path of the text file
	 */
	public static ArrayList<String> file2array(String filename) {
		ArrayList<String> list = new ArrayList<String>();
		BufferedReader in = null;
		String line = "";
		try {
			in = new BufferedReader(new FileReader(filename));
			while ((line = in.readLine()) != null) {
				list.add(line);
			}
			in.close();
		} catch (Exception e) {
			logger.error("Oops, error found while reading file: " + e.getMessage());
		}
		return list;
	}

	/**
	 * Creates a stream reference of a file
	 * @param file text file
	 * @return InputSource
	 */
	public static InputSource getStream(File file) {
		InputStream stream = null;
		InputSource source = null;

		try {
			stream = new FileInputStream(file);
			source = new InputSource(new InputStreamReader(stream));
			source.setEncoding("UTF-8");
		} catch (Exception e) {
			logger.error(e.getMessage());
			source = null;
		}
		return source;
	}

	/**
	 * Validates an argument is a Zip file using a "magic" number.
	 * @param zip potential Zip file to assess
	 * @return true if the file is in Zip format
	 */
	public static boolean isZipFile(File zip) {
		RandomAccessFile raf = null;
		long n = 0;

		try {
			raf = new RandomAccessFile(zip, "r");
			n = raf.readInt();
			raf.close();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		return (n == 0x504B0304);
	}

	/**
	 * Deletes a file (or folder --including its children, recursively).
	 * @param file a file or directory in the filesystem
	 * @return true if the file or directory was deleted
	 */
	public static boolean deleteAll(File file) {
		File[] files = file.listFiles();
		boolean isDeleted = false;
		if (files != null) {
			for (File f : files) {
				if (f.isDirectory()) {
					deleteAll(f);
				}
				else {
					f.delete();
				}
			}
			isDeleted = file.delete();
		}
		return isDeleted;
	}

	/**
	 * Creates a directory with the name of the argument.
	 * If it already exists, it will be deleted and re-created.
	 * @param newFolder name of the folder
	 * @return true if the directory has been created
	 */
	public static boolean setupWorkDirectory(String newFolder) {
		File workDirectory = new File(newFolder);
		if (workDirectory.exists()) {
			logger.warn("Cleaning up directory {}", newFolder);
			deleteAll(workDirectory);
		}
		workDirectory.mkdir();
		logger.info("Setting up working directory {}", newFolder);
		return workDirectory.exists();
	}

	/**
	 * Extracts a file from Zip archives to the folder specified in the argument.
	 * @param zipFile compressed file
	 * @param outputFolder folder where contents of the Zip file will be saved to
	 * @return reference of the extracted file
	 */
	public static File extract(String zipFile, String outputFolder) {
		byte[] buffer = new byte[1024];
		File newFile = null;

		try{
			logger.trace("Extracting to {} to {}", zipFile, outputFolder);
			//get the zip file content
			ZipInputStream zip = new ZipInputStream(new FileInputStream(zipFile));
			//get the zipped file list entry
			ZipEntry entry = zip.getNextEntry();

			while (entry!=null) {
				String file = entry.getName();
				newFile = new File(outputFolder + file);
				logger.trace("Extracting {}", file);
				//create all non exists folders
				//else you will hit FileNotFoundException for compressed folder
				new File(newFile.getParent()).mkdirs();

				FileOutputStream fos = new FileOutputStream(newFile);             

				int len;
				while ((len = zip.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}

				fos.close();   
				entry = zip.getNextEntry();
			}

			zip.closeEntry();
			zip.close();

		} catch(IOException exception) {
			logger.error(exception.getMessage());
		}

		return newFile;
	}

	/**
	 * Generates a Zip file with one or more entries.
	 * @param resultCatalogLocation source and target location in the filesystem
	 * @param pages entries to compress
	 * @param zipFilename name of the Zip file
	 */
	public static void archive(String resultCatalogLocation, Vector<String> pages, String zipFilename) {
		byte[] buffer = new byte[1024];

		try {
			logger.trace("Archiving to {}", zipFilename);
			FileOutputStream fos = new FileOutputStream(resultCatalogLocation + zipFilename);
			ZipOutputStream zip = new ZipOutputStream(fos);
			for (String page : pages) {
				logger.trace("Adding {}", page);
				ZipEntry entry = new ZipEntry(page + ".html");
				zip.putNextEntry(entry);
				FileInputStream in = new FileInputStream(resultCatalogLocation + page + ".html");

				int len;
				while ((len = in.read(buffer)) > 0) {
					zip.write(buffer, 0, len);
				}

				in.close();
				zip.closeEntry();
			}
			//remember close it
			zip.close();
		} catch (IOException exception) {
			logger.error(exception.getMessage());
		}
	}
}
