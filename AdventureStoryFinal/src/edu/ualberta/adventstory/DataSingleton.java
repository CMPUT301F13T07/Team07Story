package edu.ualberta.adventstory;

import edu.ualberta.database.DbManager;
import android.app.Application;

public class DataSingleton extends Application{
	public DbManager database = new DbManager(this);
	
	public DataSingleton(){
		database.open();
	}
}
