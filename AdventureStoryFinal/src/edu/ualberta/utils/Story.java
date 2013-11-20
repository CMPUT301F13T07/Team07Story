package edu.ualberta.utils;

import java.util.*;

/**
 * This class represents the "book cover" of the story for the most part, and as such, most of the functionality
 * in this class is actually just wrappers around functions in Page.java. 
 * Refer to that for further information. 
 * @author: Lyle Rolleman (except toString, that was Kelsey Gaboriau)
 */

public class Story {
	private Integer id;
	private String title;
	private String author;
	private Page root;

	//Like Page, overloaded constructors for when creating new story or loading from DB
	/**
	 * 
	 * @param title
	 * @param author
	 * @param root
	 */
	public Story(String title, String author, Page root) {
		this.id = null;
		this.title = title;
		this.author = author;
		this.root = root;
	}
	/**
	 * 
	 * @param id
	 * @param title
	 * @param author
	 * @param root
	 */
	public Story(Integer id, String title, String author, Page root) {
		this.id = id;
		this.title = title;
		this.author = author;
		this.root = root;
	}
	
	public void setID(Integer i) {id = i;}
	public Integer getID() {return id;}
	public void setTitle(String t) {title = t;}
	public String getTitle() {return title;}
	public void setAuthor(String a) {author = a;}
	public String getAuthor() {return author;}
	public void setRoot(Page r) {root = r;}
	public Page getRoot() {return root;}
	public ArrayList<Page> searchByTitle(String t) {return root.searchByTitle(t);}
	public ArrayList<Page> searchByID(Integer id) {return root.searchByID(id);}
	public ArrayList<Page> searchByAuthor(String a) {return root.searchByAuthor(a);}
	
	//Clones the story object and ALL its pages. See Page.java for more information on cloning
	public Story cloneEntireStory() {
		Story nstory = this.clone();
		nstory.root = this.root.cloneAllChildren();
		return nstory;
	}
	
	//overriding so as not to have to deal with casting and exception nonsense
	public Story clone() {
		return new Story(this.id, this.title, this.author, this.root);
	
	}
	
	//return all pages, see Page class for more details
	public ArrayList<Page> getAllPages() {
		return root.getAllPages();
	}
	
	@Override
	/**
	 * Creates a string representation of a story
	 * @return: The string version of story
	 * @author: Kelsey Gaboriau 
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return "Title: " + this.getTitle() + "\n" + "Author: " + this.getAuthor();
	}
}
