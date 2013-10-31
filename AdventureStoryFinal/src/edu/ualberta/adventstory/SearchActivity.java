package edu.ualberta.adventstory;

import java.util.ArrayList;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class SearchActivity extends Activity implements OnItemSelectedListener{
	
	private EditText searchEntry = (EditText) findViewById(R.id.searchEntry);
	private Spinner searchBy = (Spinner) findViewById(R.id.spinnerSearchBy);
	private Spinner searchOnline = (Spinner) findViewById(R.id.spinnerOnline);
	
	// Find out of the user is searching stories or pages
	private Bundle bundle = getIntent().getBundleExtra("android.intent.extra.INTENT");
	private boolean isStory = bundle.getBoolean("BOOL_IS_STORY");
	private boolean isTitle = true;
	
	private ArrayList<?> results;
	
	// Access the database
	//private Db DataBase = ((AdventureStoryFinal)getApplication()).DataBase;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story_search);
		
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.searchBy_dropdown, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		searchBy.setAdapter(adapter);
		
		searchBy.setOnItemSelectedListener(this);
		
		// Checks to see if the user has input text into the search box
		searchText();	
		
	}
	
	private void searchText() {
		searchEntry.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable s) {
				// Check if search is by author or title and set isTitle variable
				// THIS IS WHERE THE SPINNER IS READ
				
				// Check if the user is searching pages or stories
				if (isStory && isTitle){
					// Populate results with appropriate stories based on title
					//results = DataBase.get_stories_by_author(searchEntry.getText().toString());
					
				} else if (isStory && !isTitle){
					// Populate results with appropriate stories based on author
					//results = DataBase.get_stories_by_title(searchEntry.getText().toString());
					
				} else if (!isStory && isTitle){
					// Populate results with appropriate pages based on title
					//results = DataBase.get_pages_by_title(searchEntry.getText().toString());
					
				} else {
					// Populate results with appropriate pages based on author
					//results = DataBase.get_pages_by_author(searchEntry.getText().toString());
				}
				
				updateList(results);

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {}
	        
	    }); 
		
	}

	/** Called when the search field is blank. Returns all pages/stories
	protected void emptySearch(boolean isStory){
		if (isStory){
			// Populate results list with all stories
			results = new ArrayList<Story>();
		} else if (!isStory){
			// Populate results list with all pages
			results = new ArrayList<Page>();
		}
		
		updateList(results);
	}**/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, 
            int pos, long id) {
		// Handles user selections from drop down menus
		Spinner searchBy = (Spinner)parent;
        Spinner searchOnline = (Spinner)parent;
		// Set isTitle boolean to reflect spinner selection
		Integer selected = (Integer) parent.getItemAtPosition(pos);
		if(searchBy.getId() == R.id.spinnerSearchBy){
			if (selected == 0){
				// User selected to search by title
				isTitle = true;
			} else if (selected == 1){
				// User selected to search by author
				isTitle = false;
			}
		}
		if(searchOnline.getId() == R.id.spinnerOnline){
			if (selected == 0){
				
			} else if (selected == 1){
				
			}
		}
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {}
	
	// Updates the list view with the results for the user
	public void updateList(ArrayList<?> results){
		
	}

}
