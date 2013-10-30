package edu.ualberta.adventstory;

import java.util.ArrayList;
import edu.ualberta.utils.Page;
import edu.ualberta.utils.Story;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.widget.EditText;

public class StorySearchActivity extends Activity {
	
	private EditText searchEntry = (EditText) findViewById(R.id.searchEntry);
	
	// Find out of the user is searching stories or pages
	private Bundle bundle = getIntent().getBundleExtra("android.intent.extra.INTENT");
	private boolean isStory = bundle.getBoolean("BOOL_IS_STORY");
	
	private ArrayList<?> results;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story_search);
		
		// Checks to see if the user has input text into the search box
		searchText();	
		
	}
	
	private void searchText() {
	    searchEntry.addTextChangedListener(new TextWatcher(){
	        public void afterTextChanged(Editable s) {
	            // Check if the user is searching pages or stories
	        	if (isStory){
	        		// Check for blank entry
	        		if (searchEntry.getText().equals("")){
	        			emptySearch(isStory);
	        		}
	        		// Populate results with appropriate stories
	        		results = new ArrayList<Story>();
	        		
	        	} else{
	        		// Check for blank entry
	        		if (searchEntry.getText().equals("")){
	        			emptySearch(isStory);
	        		}
	        		// Populate results with appropriate pages
	        		results = new ArrayList<Page>();
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
	protected void emptySearch(boolean isStory){
		if (isStory){
			// Populate results list with all stories
			results = new ArrayList<Story>();
		} else if (!isStory){
			// Populate results list with all pages
			results = new ArrayList<Page>();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

}
