package edu.ualberta.utils;

import java.util.*;

//TO-DO Update attributes and constructors when have multimedia class of some kind.
public class Page {
	private Integer id;
        private String title;
	private String text;
	private Page parent;
	//private ArrayList<Multimedia> multimedia;
	private ArrayList<Page> pages;
	
	//Constructors overloaded for when creating new pages or loading them from DB
	//Saving the DB id should make updating the DB simpler and more reliable
    public Page(Integer id, String title, String text, Page parent, ArrayList<Page> pages) {
		this.id = id;
		this.title = title;
		this.text = text;
		this.parent = parent;
		if (pages == null)
			this.pages = new ArrayList<Page>();
		else
			this.pages = pages;
	}
    public Page(String title, String text, Page parent, ArrayList<Page> pages) {
		this.id = null;
		this.title = title;
		this.text = text;
		this.parent = parent;
		if (pages == null)
			this.pages = new ArrayList<Page>();
		else
			this.pages = pages;
	}
	
	public void setID(Integer i) {id = i;}
	public Integer getID() {return id;}
    public void setTitle(String t) {title = t;}
    public String getTitle() {return title;}
	public void setText(String t) {text = t;}
	public String getText() {return text;}
	public void setParent(Page p) {parent = p;}
	public Page getParent() {return parent;}
	public void setPages(ArrayList<Page> o) {pages = o;}
	public ArrayList<Page> getPages() {return pages;};
	public Page getPage(Integer i) {return pages.get(i);}
	public void setPage(Integer i, Page o) {pages.set(i, o);}
	public void addPage(Page o) {pages.add(o);}
	public void deletePage(Integer i) {pages.remove(i);}
	
	//get all pages and return them in a simple arraylist. 
	//Get all pages at and below current node. Does not include the current option. 
	//should you want it, you can include it at the call site. 
	public ArrayList<Page> getAllPages() {
		return getAllPages(this);
	}
	
	//get all pages at and below the designated node
	public ArrayList<Page> getAllPages(Page o) {
		ArrayList<Page> ops = o.getPages();
		for (int i=0; i < ops.size(); i++) {
			ops.addAll(getAllPages(ops.get(i)));
		}
		return ops;
	}
}
