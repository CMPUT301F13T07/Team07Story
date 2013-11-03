package edu.ualberta.adventstory;

import android.app.Application;
import edu.ualberta.database.DbManager;

public class DataSingleton extends Application{
	public static DbManager database;
	
	@Override
	public void onCreate() {
		System.out.println("Database created in DataSingleton");
		super.onCreate();
		database = new DbManager(this);
		database.open();
	}

}
