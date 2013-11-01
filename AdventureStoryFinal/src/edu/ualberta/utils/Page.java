package edu.ualberta.utils;

/*
 * The Page class
 * This class comes with all the components and functionality needed to represent a page in the Adventure Story
 * It takes the form of a dynamic tree data structure where each page can have a (theoretically) limitless number
 * of children pages. 
 * 
 * See Constructor summary below the attribute list for more details on the constructors
 * 
 * Overrode clone feature since I did previously and this way don't have to deal with casting and exception handling
 * annoyingness
 * 
 * REMINDER ABOUT JAVA OBJECTS: If you are familiar with how Java handles objects in memory and the difference between assigning
 * and cloning, you can ignore this next section. If you don't know what I'm talking about, READ THIS, it might save you
 * many a headache. 
 * In regards to above mentioned clones, remember, changes made to an object not cloned from
 * the tree WILL AFFECT THE TREE, all you are doing is copying the reference to the object, you are not mapping new 
 * memory for it. 
 * Ex: 
 * Page root = new Page("ex", "Lyle", "text", null);
 * Story mystory = new Story("mystory", "Lyle", root);
 * Page page = mystory.getRoot();
 * page.setText("new text");
 * System.out.println(mystory.getRoot().getText());
 * This will print "new text", NOT "text". If I were to add .clone() after getRoot(), it would print "text". Remember, you are copying
 * only a reference to page, not allocating a new object. The sheer number of times I've seen even experienced programmers
 * make this mistake forces me to harp on it. Sorry for being long winded, blame Java's documention on the 
 * difference between pass-by-reference and pass-by-value.
 * 
 * For non=trivial implementation specific information, see the specific functions. The prototypes, in order are;
 * Page clone() : clones the caller, creating a new Page which is SEPARATE from the tree, it will not have a parent or children
 * Page cloneAllChildren() : Like above, but will clone all the children of the caller, returning a clone of the caller
 * ArrayList<Page> getAllPages() : Gets a list of Pages, ordered by level in the tree
 * ArrayList<Page> searchByTitle(String) : return a list of pages whose title matches the passed string. Currently case sensitive
 * ArrayList<Page> searchByAuthor(Sting) : return a list of pages whose author field matches argument. Currently case sensitive
 * ArrayList<Page> searchByID(Integer) : return a list of pages whose ID matches argument. ID can be null
 */

import java.util.*;
import edu.ualberta.multimedia.*;

//TO-DO Update attributes and constructors when have multimedia class of some kind.
public class Page {
	private Integer id;
    private String title;
    private String author;
	private Content content;
	//private ArrayList<MultimediaAbstract> multimedia;
	private ArrayList<Page> pages;
	
	//for internal use only
	private ArrayList<ArrayList<Page>> levellist = new ArrayList<ArrayList<Page>>();
	
	/*Constructors overloaded for when creating new pages or loading them from DB
	*Saving the DB id should make updating the DB simpler and more reliable, but does not really matter
	*for when creating new pages
	*
	*Constructor summary:
	*Option id: the DB id, non-null if the page was loaded from the DB, otherwise not relevant
	*title: the name of the page
	*author: the author of the page
	*text; the text of the page
	*TO-DO: multimedia: the multimedia objects on the page
	*pages: The list of pages the user can choose to go to next
	*/
	
    public Page(Integer id, String title, String author, Content content, ArrayList<Page> pages) {
		this.id = id;
		this.title = title;
		this.author = author;
		this.content = content;
		if (pages == null)
			this.pages = new ArrayList<Page>();
		else
			this.pages = pages;
	}
	
    public Page(Integer id, String title, String author, String text, ArrayList<Page> pages) {
		this.id = id;
		this.title = title;
		this.author = author;
		this.content = new Content(text, null);
		if (pages == null)
			this.pages = new ArrayList<Page>();
		else
			this.pages = pages;
	}
    public Page(String title, String author, String text, ArrayList<Page> pages) {
		this.id = null;
		this.title = title;
		this.author = author;
		this.content = new Content(text, null);
		if (pages == null)
			this.pages = new ArrayList<Page>();
		else
			this.pages = pages;
	}
    public Page(Integer id, String title, String author, String text, ArrayList<MultimediaAbstract> mm, ArrayList<Page> pages) {
    	this.id = id;
    	this.title = title;
    	this.author = author;
    	this.content = new Content(text, mm);
    	if (pages == null)
    		this.pages = new ArrayList<Page>();
    	else
    		this.pages = pages;
    }
    public Page(String title, String author, String text, ArrayList<MultimediaAbstract> mm, ArrayList<Page> pages) {
    	this.id = null;
    	this.title = title;
    	this.author = author;
    	this.content = new Content(text, mm);
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
	public void setText(String t) {content.setParagraph(t);}
	public String getText() {return content.getParagraph();}
	public void addMultimedia(MultimediaAbstract ma) {content.addMultimedia(ma);}
	public ArrayList<MultimediaAbstract> getMultimedia() {return content.getAllMultimedia();}
	public Content getFormattedContent() {return content;}
	public void setPages(ArrayList<Page> o) {pages = o;}
	public ArrayList<Page> getPages() {return pages;};
	public Page getPage(Integer i) {return pages.get(i);}
	public void setPage(Integer i, Page o) {
		pages.set(i, o);
	}
	public void addPage(Page o) {
		pages.add(o);
	}
	public void deletePage(Integer i) {pages.remove(i);}
	
	/*
	*Overridden to avoid casting and exemption garbage, and it was already here for previous design branch not taken
	*If you want to get an entire branch of the tree, se cloneAllChildren below
	*/
	public Page clone() {
		return new Page(this.id, this.title, this.author, this.content.getParagraph(), this.content.getAllMultimedia(), null);
	}
	
	/*Clones the caller and all Pages below it in the tree. 
	 * Implementation: 
	 * clones the current page, then gets the list of children from the ORIGINAL page
	 * iterates through those pages, performing a recursive call on them. The return value is the clone
	 * of the current page, which will be added to the children of the clone
	 */
	public Page cloneAllChildren() {
		Page root = this.clone();
		ArrayList<Page> pages = this.getPages();
		for (int i=0; i<pages.size(); i++) {
			root.addPage(pages.get(i).cloneAllChildren());
		}
		
		return root;
	}
	
	/*
	 * get all pages and return them in a simple arraylist. Ordered by depth in tree
	 * Get all pages at and below current node. Includes current page
	 * Implementation: 
	 * This one is is a hack and duct tape solution that I really don't like but it works and don't 
	 * feel like changing it at the moment. It creates a new array list, addes the caller to the list
	 * then calls the recursive half of the function, which sifts between the levels of the tree, adding them
	 * to their corresponding arraylist in the arraylist. 
	 * When it returns, the populated arraylist of arraylists are combined and returned
	*/
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
	
	/*
	 * searches do not return in any particular order at the moment, can do this later if desirable
	 * Implementation:
	 * All the searches are pretty much the same, look to see if the caller if the caller equals the provided 
	 * string/Integer, adding it to the list if it does. Changes the caller by recursively running through the children
	 * of each, and so on and so forth. each return combines the arraylists of the individual calls providing a list
	 * of all matches. Will return an empty arraylist if there are no matches. 
	 */
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
