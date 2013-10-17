package edu.ualberta.utils;

import java.util.*;


public class Story {
	private Integer id;
	private String title;
	private String author;
	private Option root;

	//Like Option, overloaded constructors for when creating new story or loading from DB
	public Story(String title, String author, Option root) {
		this.id = null;
		this.title = title;
		this.author = author;
		this.root = root;
	}
	public Story(Integer id, String title, String author, Option root) {
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
	public void setRoot(Option r) {root = r;}
	public Option getRoot() {return root;}
	
	//return all options, see Option class for more details
	public ArrayList<Option> getAllOptions() {
		ArrayList<Option> ret = root.getAllOptions();
		ret.add(root);
		return ret;
	}
}
