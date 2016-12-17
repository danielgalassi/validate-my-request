/**
 * 
 */
package org.validator.metadata;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author danielgalassi@gmail.com
 *
 */
@XmlRootElement(name = "object")
public class DBObject {

	private String name;
	private String schema;
	private String type;
	private String comment = "";
	private String full_object;
	private boolean isValid = false;

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

	public String getName() {
		return name;
	}

	@XmlAttribute
	public String getType() {
		return type;
	}

	public String getSchema() {
		return schema;
	}

	public boolean exist() {
		return isValid;
	}

	@XmlAttribute
	/**
	 * 
	 * @return true, as a String, if this object was a match when compared to the master objects lists
	 */
	public String isValid() {
		return isValid+"";
	}

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
		this.comment = comment;
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
}
