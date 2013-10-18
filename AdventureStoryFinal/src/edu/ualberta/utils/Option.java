package edu.ualberta.utils;

import java.util.*;

//TO-DO Update attributes and constructors when have multimedia class of some kind.
public class Option {
	private Integer id;
	private String text;
	private Option parent;
	//private ArrayList<Multimedia> multimedia;
	private ArrayList<Option> options;
	
	//Constructors overloaded for when creating new options or loading them from DB
	//Saving the DB id should make updating the DB simpler and more reliable
	public Option(Integer id, String text, Option parent, ArrayList<Option> options) {
		this.id = id;
		this.text = text;
		this.parent = parent;
		if (options == null)
			this.options = new ArrayList<Option>();
		else
			this.options = options;
	}
	public Option(String text, Option parent, ArrayList<Option> options) {
		this.id = null;
		this.text = text;
		this.parent = parent;
		if (options == null)
			this.options = new ArrayList<Option>();
		else
			this.options = options;
	}
	
	public void setID(Integer i) {id = i;}
	public Integer getID() {return id;}
	public void setText(String t) {text = t;}
	public String getText() {return text;}
	public void setParent(Option p) {parent = p;}
	public Option getParent() {return parent;}
	public void setOptions(ArrayList<Option> o) {options = o;}
	public ArrayList<Option> getOptions() {return options;};
	public Option getOption(Integer i) {return options.get(i);}
	public void setOption(Integer i, Option o) {options.set(i, o);}
	public void addOption(Option o) {options.add(o);}
	public void deleteOption(Integer i) {options.remove(i);}
	
	//get all options and return them in a simple arraylist. 
	//Get all options at and below current node. Does not include the current option. 
	//should you want it, you can include it at the call site. 
	public ArrayList<Option> getAllOptions() {
		return getAllOptions(this);
	}
	
	//get all options at and below the designated node
	public ArrayList<Option> getAllOptions(Option o) {
		ArrayList<Option> ops = o.getOptions();
		for (int i=0; i < ops.size(); i++) {
			ops.addAll(getAllOptions(ops.get(i)));
		}
		return ops;
	}
}
