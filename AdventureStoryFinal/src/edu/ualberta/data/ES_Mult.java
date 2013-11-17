package edu.ualberta.data;

public class ES_Mult {
	private int mult_id;
	private String directory;
	private int page_id;
	private int index;
	private String mult_type;
	
	public ES_Mult(int mult_id, String directory, int page_id, int index,
			String mult_type) {
		this.mult_id = mult_id;
		this.directory = directory;
		this.page_id = page_id;
		this.index = index;
		this.mult_type = mult_type;
	}

	public int getMult_id() {
		return mult_id;
	}

	public void setMult_id(int mult_id) {
		this.mult_id = mult_id;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public int getPage_id() {
		return page_id;
	}

	public void setPage_id(int page_id) {
		this.page_id = page_id;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getMult_type() {
		return mult_type;
	}

	public void setMult_type(String mult_type) {
		this.mult_type = mult_type;
	}
	
	
}
