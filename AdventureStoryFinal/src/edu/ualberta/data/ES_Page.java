package edu.ualberta.data;

import edu.ualberta.utils.Page;

public class ES_Page {
	private String page_title;
	private String page_author;
	private int page_id;
	private String page_text;
	
	public ES_Page(String page_title, String page_author, String text, int page_id) {
		this.page_title = page_title;
		this.page_author = page_author;
		this.page_id = page_id;
		this.page_text = text;
	}
	
	public Page toPage() {
		return new Page(page_id, page_title, page_author, page_text, null);
	}

	public String getPage_title() {
		return page_title;
	}

	public void setPage_title(String page_title) {
		this.page_title = page_title;
	}

	public String getPage_author() {
		return page_author;
	}

	public void setPage_author(String page_author) {
		this.page_author = page_author;
	}

	public int getPage_id() {
		return page_id;
	}

	public void setPage_id(int page_id) {
		this.page_id = page_id;
	}
	
	
	
}
