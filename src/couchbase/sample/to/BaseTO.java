package couchbase.sample.to;

public class BaseTO {
	
	private String type = null;
	private String schema = null;
	private int version = 1;
	private int createdon = -1;
	private int modifiedon = -1;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getCreatedon() {
		return createdon;
	}

	public void setCreatedon(int createdon) {
		this.createdon = createdon;
	}

	public int getModifiedon() {
		return modifiedon;
	}

	public void setModifiedon(int modifiedon) {
		this.modifiedon = modifiedon;
	}


}
