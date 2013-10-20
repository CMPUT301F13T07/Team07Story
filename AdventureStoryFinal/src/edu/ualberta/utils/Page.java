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
	
	//for internal use only
	private ArrayList<ArrayList<Page>> levellist = new ArrayList<ArrayList<Page>>();
	
	/*Constructors overloaded for when creating new pages or loading them from DB
	*Saving the DB id should make updating the DB simpler and more reliable
	*
	*Constructor summary:
	*Option id: the DB id, non-null if the page was loaded from the DB, otherwise not relevant
	*title: the name of the page
	*text; the text of the page
	*parent: this nodes parent in the tree. See addPage() for more information on parent
	*TO-DO: multimedia: the multimedia objects on the page
	*pages: The list of pages the user can choose to go to next
	*/
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
	
	//Should note here, if you pass null in the parent field of the constructor, parent will be initialized to
	//the caller. Only the root in a Story object or stand-alone pages should be able to have null parent
	public void addPage(Page o) {
		if (o.getParent() == null)
			o.setParent(this);
		pages.add(o);
	}
	public void deletePage(Integer i) {pages.remove(i);}
	
	//get all pages and return them in a simple arraylist, ordered by depth in tree. 
	//Get all pages at and below current node. Does not include the current Page. 
	//should you want it, you can include it at the call site. 
	public ArrayList<Page> getAllPages() {
		ArrayList<Page> ret = new ArrayList<Page>();
		getAllPages(this, 0);
		for (int i=0; i<levellist.size(); i++) 
			ret.addAll(levellist.get(i));
		levellist.clear();
		return ret;
	}
	
	//internal use
	private void getAllPages(Page o, int level) {
		if ((level+1) > levellist.size()) 
			levellist.add(new ArrayList<Page>());
		levellist.get(level).addAll(o.getPages());
		ArrayList<Page> ops = o.getPages();
		int size = ops.size();
		for (int i=0; i < size; i++) 
			getAllPages(ops.get(i), level+1);
	}
}
