package edu.ualberta.adventstory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import edu.ualberta.database.DbHelper;

public class CreateNewStoryActivity extends Activity implements OnItemSelectedListener{
	
	private EditText mTitleText;
	private EditText mAuthorText;
	private DbHelper mDbHelper;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mTitleText = (EditText) findViewById(R.id.activity_newstory_edittitle);
		mAuthorText = (EditText) findViewById(R.id.activity_newstory_editauthor);
		
		Spinner spinner = (Spinner) findViewById(R.id.activity_newstory_pagespinner);
		Button submitButton = (Button) findViewById(R.id.buttonSubmit);
		Button cancelButton = (Button) findViewById(R.id.buttonCancel);
		
		/*
		 * Set the spinner
		 * from android site
		 * developer.android.com/guide/topics/ui/controls/spinner.html
		 */
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, 
				R.array.first_page_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		/*
		 * When Submit Button is pressed, the story is created and user is transfered to pageEdit Activity
		 */
		submitButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				insertStory();
				/*
				 * Need to make it change to pageEdit Activity
				 */
				setResult(RESULT_OK);
				finish();
				
			}
		});
		
		/*
		 * When the cancel button is pressed, nothing is saved and ends activity
		 */
		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(RESULT_OK);
				finish();
				
			}
		});
		
	}
	
	
	/*
	 * This method adds the story to the database
	 */
	private void insertStory() {
		
	}
	
	/*
	 * This method creates an intent for the PageEdit Activity
	 */
	private void pageEdit() {
		//Intent i = new Intent(this, PageEditActivity.class);
		//startActivityForResult(i, 0);
	}


	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		// An item was selected. You can retrieve the selected item using
		// parent.getItemAtPosition(pos)
		
	}


	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// Another interface callback
		
	}

}
