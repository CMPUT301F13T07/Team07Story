package edu.ualberta.utils;

public class Responder {
	static public class Action{
		public void act(){};	// Override.
	}
	
	Action mAction;
	public Responder(Action action) {
		mAction = action;
	}
	
	public void response(){
		mAction.act();
	}
	
	public void setResponse(Action action){
		mAction = action;
	}

}
