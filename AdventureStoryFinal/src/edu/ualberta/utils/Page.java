package edu.ualberta.utils;

import java.util.*;

//TO-DO Update attributes and constructors when have multimedia class of some kind.
public class Page {
	private Integer id;
    private String title;
    private String author;
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
	*author: the author of the page
	*text; the text of the page
	*TO-DO: multimedia: the multimedia objects on the page
	*pages: The list of pages the user can choose to go to next
	*/
    public Page(Integer id, String title, String author, String text, ArrayList<Page> pages) {
		this.id = id;
		this.title = title;
		this.author = author;
		this.text = text;
		this.parent = null;
		if (pages == null)
			this.pages = new ArrayList<Page>();
		else
			this.pages = pages;
	}
    public Page(String title, String author, String text, ArrayList<Page> pages) {
		this.id = null;
		this.title = title;
		this.author = author;
		this.text = text;
		this.parent = null;
		if (pages == null)
			this.pages = new ArrayList<Page>();
		else
			this.pages = pages;
	}
	
	public void setID(Integer i) {id = i;}
	public Integer getID() {return id;}
    public void setTitle(String t) {title = t;}
    public String getTitle() {return title;}
    public void setAuthor(String a) {author = a;}
    public String getAuthor() {return author;}
	public void setText(String t) {text = t;}
	public String getText() {return text;}
	public Page getParent() {return parent;}
	public void setPages(ArrayList<Page> o) {pages = o;}
	public ArrayList<Page> getPages() {return pages;};
	public Page getPage(Integer i) {return pages.get(i);}
	public void setPage(Integer i, Page o) {
		o.parent = this;
		pages.set(i, o);
	}
	
	//Sets the parent of a newly added page to the page that it is being added to
	public void addPage(Page o) {
		o.parent = this;
		pages.add(o);
	}
	public void deletePage(Integer i) {pages.remove(i);}
	
	/*Overrides to make sure Pages aren't referencing parent or child nodes they shouldn't when cloned
	*specifically, a cloned page's children will point towards the original, not cloned node. 
	*It will effectively be a stand-alone page until you do something with it
	*It wouldn't make much sense for the child to point to the parent but the parent not to point at the child or 
	*vice versa
	*You can add a clone back to the tree with a call to addPage
	*
	*If you want to get an entire branch of the tree, se cloneAllChildren below
	*/
	public Page clone() {
		return new Page(this.id, this.title, this.author, this.text, null);
	}
	
	//Clones the caller and all Pages below it in the tree. 
	public Page cloneAllChildren() {
		Page root = this.clone();
		ArrayList<Page> pages = this.getPages();
		for (int i=0; i<pages.size(); i++) {
			root.addPage(pages.get(i).cloneAllChildren());
		}
		
		return root;
	}
	
	//get all pages and return them in a simple arraylist. Ordered by depth in tree
	//Get all pages at and below current node. Includes current page
	public ArrayList<Page> getAllPages() {
		ArrayList<Page> ret = new ArrayList<Page>();
		ret.add(this);
		getAllPages(this, 0);
		for (int i=0; i<levellist.size(); i++) 
			ret.addAll(levellist.get(i));
		levellist.clear();
		return ret;
	}
	
	//internal use
	private void getAllPages(Page o, int level) {
		if (level >= levellist.size()) 
			levellist.add(new ArrayList<Page>());
		levellist.get(level).addAll(o.getPages());
		ArrayList<Page> ops = o.getPages();
		for (int i=0; i < ops.size(); i++) 
			getAllPages(ops.get(i), level+1);
	}
	
	//searches do not return in any particular order at the moment, can do this later if desirable
	public ArrayList<Page> searchByTitle(String t) {
		ArrayList<Page> res = new ArrayList<Page>();
		
		if (this.getTitle().equals(t))
			res.add(this);
		ArrayList<Page> ops = this.getPages();
		for (int i=0; i<ops.size(); i++)
			res.addAll(ops.get(i).searchByTitle(t));
		return res;
	}
	
	public ArrayList<Page> searchByAuthor(String a) {
		ArrayList<Page> res = new ArrayList<Page>();
		
		if (this.getAuthor().equals(a))
			res.add(this);
		ArrayList<Page> ops = this.getPages();
		for (int i=0; i<ops.size(); i++)
			res.addAll(ops.get(i).searchByAuthor(a));
		return res;
	}
	
	public ArrayList<Page> searchByID(Integer id) {
		ArrayList<Page> res = new ArrayList<Page>();
		
		if (this.getID() == null && id == null) {
			res.add(this);
		} else {
			if (this.getID().equals(id))
				res.add(this);
		}
		ArrayList<Page> ops = this.getPages();
		for (int i=0; i<ops.size(); i++)
			res.addAll(ops.get(i).searchByID(id));
		return res;
	}
}
