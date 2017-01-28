/**
 * 
 */
package org.validator.metadata;

import java.util.Vector;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Abstraction for a table, view, stored procedure, synonym or sequence
 * @author danielgalassi@gmail.com
 */
@XmlRootElement(name = "object")
public class DBObject {

	/** Object name */
	private String name;
	/** Object schema */
	private String schema;
	/** Object type */
	private String type;
	/** Object comment */
	private String comment = "";
	/** Object schema and name*/
	private String full_object;
	/** found / not found flag */
	private boolean isValid = false;
	/** underlying objects that may be missing from the refresh request */
	private Vector<String> missingReferences = new Vector<String>();

	/**
	 * Default constructor (for JAXB API)
	 */
	public DBObject () {
	}

	/**
	 * Constructor
	 * @param schema an objects schema
	 * @param name a DB objects name
	 * @param type keyword describing the object type (table, view, procedure, sequence or synonym)
	 */
	public DBObject (String schema, String name, String type) {
		this.name = name.toUpperCase().trim();
		this.schema = schema.toUpperCase().trim();
		this.type = type.toUpperCase().trim();
		this.full_object = schema + "." + name;
	}

	@XmlAttribute
	/**
	 * Method created to facilitate the serialisation
	 * @return a concatenated schema.object_name
	 */
	public String getFullObject() {
		return full_object;
	}

	/**
	 * Getter for the object's  name
	 * @return the name of the object
	 */
	public String getName() {
		return name;
	}

	@XmlAttribute
	/**
	 * Getter for the object's type
	 * @return the type of the object
	 */
	public String getType() {
		return type;
	}

	/**
	 * Getter for the schema field
	 * @return the schema of the object
	 */
	public String getSchema() {
		return schema;
	}

	/**
	 * Getter for the validation flag
	 * @return true if the object exists in the master object's list
	 */
	public boolean exist() {
		return isValid;
	}

	@XmlAttribute
	/**
	 * Getter for the validation flag, method created to facilitate the serialisation process
	 * @return true, as a String, if this object was a match when compared to the master objects lists
	 */
	public String isValid() {
		return isValid+"";
	}

	/**
	 * Flags the fact that this object has been matched against the master object's list (and found in it)
	 */
	public void tag() {
		this.isValid = true;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return schema + "." + name;
	}

	/**
	 * Retrieves the comment for this object
	 * @return the comment
	 */
	@XmlAttribute
	public String getComment() {
		return comment;
	}

	/**
	 * Standard setter method
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		if (this.comment.equals("")) {
			this.comment = comment;
		} else {
			this.comment += " " + comment;
		}
	}

	public boolean isTable() {
		return (type.equals("TABLE"));
	}

	public boolean isView() {
		return (type.equals("VIEW"));
	}

	public boolean isSynonym() {
		return (type.equals("SYNONYM"));
	}

	public boolean isProcedure() {
		return (type.equals("PROCEDURE"));
	}

	public boolean isSequence() {
		return (type.equals("SEQUENCE"));
	}

	public boolean equals(String anObjectName) {
		return (this.full_object.equals(anObjectName));
	}

	@XmlElementWrapper
	public Vector<String> getMissingDependencies() {
		return missingReferences;
	}

	public void addMissingDependencies(String signature) {
		missingReferences.add(signature);
	}
}
