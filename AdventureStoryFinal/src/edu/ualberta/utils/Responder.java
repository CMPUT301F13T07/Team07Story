package edu.ualberta.utils;

public class Responder {
	static public class Action{
		public void act(){};	// Override.
	}
	
	Action mAction;
	public Responder() {
		mAction = new Action();
	}
	
	public void response(){
		mAction.act();
	}
	
	public void setResponse(Action action){
		mAction = action;
	}

}
