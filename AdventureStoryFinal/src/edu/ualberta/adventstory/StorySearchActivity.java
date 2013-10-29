package edu.ualberta.adventstory;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.widget.EditText;

public class StorySearchActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story_search);
		
		final EditText searchEntry = (EditText) findViewById(R.id.searchEntry);
		
		// Checks to see if the user has input text into the search box
	    searchEntry.addTextChangedListener(new TextWatcher(){
	        public void afterTextChanged(Editable s) {
	            // Check if the user is searching pages or stories
	        	if (is stories){
	        		// Check for blank entry
	        		if (searchEntry.getText().equals("")){
	        			emptySearch("story");
	        		}
	        		// Populate results with appropriate stories
	        		
	        	} else{
	        		// Check for blank entry
	        		if (searchEntry.getText().equals("")){
	        			emptySearch("page");
	        		}
	        		// Populate results with appropriate pages
	        	}
	        }

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {}
	        
	    }); 
	}
	
	// Called when the search field is blank. Returns all pages/stories
	protected void emptySearch(String type){
		if (type.equals("story")){
			// Populate results list with all stories
		} else if (type.equals("page")){
			// Populate results list with all pages
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

}
