/**
 * 
 */
package org.validator.metadata;

/**
 * @author danielgalassi@gmail.com
 *
 */
public class DBObject {

	private String name;
	private String schema;
	private String type;
	private boolean isValid = false;
	private String comment;

	public DBObject (String schema, String name, String type) {
		this.name = name.toUpperCase().trim();
		this.schema = schema.toUpperCase().trim();
		this.type = type.toUpperCase().trim();
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getSchema() {
		return schema;
	}

	public boolean exist() {
		return isValid;
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
