package edu.ualberta.adventstory;

import java.util.*;

public class Story {
	private String title;
	private String author;
	private Option root;

	public Story(String title, String author, Option root) {
		this.title = title;
		this.author = author;
		this.root = root;
	}
	
	public void setTitle(String t) {title = t;}
	public String getTitle() {return title;}
	public void setAuthor(String a) {author = a;}
	public String getAuthor() {return author;}
	public void setRoot(Option r) {root = r;}
	public Option getRoot() {return root;}
	
	//return all options, see Option class for details
	public ArrayList<Option> getAllOptions() {
		ArrayList<Option> ret = root.getAllOptions();
		ret.add(root);
		return ret;
	}
}
