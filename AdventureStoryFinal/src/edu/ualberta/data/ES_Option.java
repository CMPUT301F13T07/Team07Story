package edu.ualberta.data;

public class ES_Option {
	private int page_id;
	private int next_page_id;
	
	public ES_Option(int page_id, int next_page_id) {
		this.page_id = page_id;
		this.next_page_id = next_page_id;
	}

	public int getPage_id() {
		return page_id;
	}

	public void setPage_id(int page_id) {
		this.page_id = page_id;
	}

	public int getNext_page_id() {
		return next_page_id;
	}

	public void setNext_page_id(int next_page_id) {
		this.next_page_id = next_page_id;
	}
	
	
}
