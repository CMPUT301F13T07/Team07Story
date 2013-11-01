package edu.ualberta.adventstory;

import edu.ualberta.database.Db;
import android.app.Application;

public class DataSingleton extends Application{
	public Db database = new Db(this);
	
	public DataSingleton(){
		database.open();
	}
}
