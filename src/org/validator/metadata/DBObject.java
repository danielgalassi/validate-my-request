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
	private boolean isFound;

	public DBObject (String schema, String name, String type) {
		this.name = name;
		this.schema = schema;
		this.type = type;
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
		return isFound;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setExist(boolean matches) {
		isFound = matches;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public String toString() {
		return schema + "." + name;
	}
}
