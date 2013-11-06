package edu.ualberta.adventstory;

import java.io.Serializable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import edu.ualberta.database.DbManager;
import edu.ualberta.utils.Page;
import edu.ualberta.utils.Story;

public class CreateNewStoryActivity extends Activity {
	private EditText mTitle;
	private EditText mAuthor;
	private DbManager database;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_newstory);
		database = DataSingleton.database;
	}
	
	public void cancel(View view) {
		finish();
	}
	
	public void submit(View view) {
		Spinner spin = (Spinner) findViewById(R.id.activity_newstory_pagespinner);
		long selection = spin.getSelectedItemId();
		if (selection == 1) {
			new_root();
		}
	}
	
	
	private void new_root() {
		// add new page to db
		Page root = new Page("", "", "", null);
		int root_id = (int)
		database.insert_page(root);
		root.setID(root_id);
		
		// add new story to db with new page as root
		mTitle = (EditText) findViewById(R.id.activity_newstory_edittitle);
		mAuthor = (EditText) findViewById(R.id.activity_newstory_editauthor);
		Story story = new Story(mTitle.getText().toString(), 
								mAuthor.getText().toString(), root);
		int story_id = (int) database.insert_story(story);
		
		// go to PageEditView
		if (root_id != 0 && story_id != 0) {
			pageEdit(root, story);
		}
		else {
			System.out.println("Database error. ID from insert == null.");
		}
	}
	/*
	 * This method creates an intent for the PageEdit Activity
	 */
	private void pageEdit(Page page, Story story) {
		Intent intent = new Intent(this, PageEditActivity.class);		
		((DataSingleton)getApplicationContext()).setCurrentPage(page);
		((DataSingleton)getApplicationContext()).setCurrentStory(story);
		startActivity(intent);
	}

}
