package edu.ualberta.adventstory;

import java.util.ArrayList;
import edu.ualberta.database.DbManager;
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
import android.widget.ListView;
import android.widget.Spinner;

public class SearchActivity extends Activity implements OnItemSelectedListener{
	
	private EditText searchEntry;
	private ListView listResults;
	private Spinner searchBy;
	
	// Database
	private DbManager database;
	
	//private Spinner searchOnline;
	private Bundle bundle;
	private boolean isStory;
	private boolean isTitle;
	
	private ArrayList<?> results;
	private ArrayList<String> displayResults;
	
	// Create Adapter for results list
	private ArrayAdapter<String> adapter2;
	// Create an ArrayAdapter using the string array and a default spinner layout
	private ArrayAdapter<CharSequence> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story_search);
		
		// Initialize variables
		isTitle = true;
		bundle = getIntent().getBundleExtra("android.intent.extra.INTENT");
		isStory = bundle.getBoolean("BOOL_IS_STORY");
		results = new ArrayList<String>();
		displayResults = new ArrayList<String>();
		database = DataSingleton.database;
		
		// Initialize view variables
		//searchOnline = (Spinner) findViewById(R.id.spinnerOnline);
		searchBy = (Spinner) findViewById(R.id.spinnerSearchBy);
		listResults = (ListView) findViewById(R.id.listResults);
		searchEntry = (EditText) findViewById(R.id.searchEntry);
		
		// Initialize adapters
		adapter = ArrayAdapter.createFromResource(this, R.array.searchBy_dropdown, android.R.layout.simple_spinner_item);
		adapter2 = new ArrayAdapter<String>(this, R.layout.list_results, displayResults);
		
		// Set up adapters
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		searchBy.setAdapter(adapter);
		listResults.setAdapter(adapter2);
		
		// Populate with all results
		results = database.get_stories_by_author(searchEntry.getText().toString());
		updateList(results);
		
		searchBy.setOnItemSelectedListener(this);
		
		// Checks to see if the user has input text into the search box
		searchText();	
		
	}
	
	private void searchText() {
		searchEntry.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable s) {	
				// Check if the user is searching pages or stories
				if (isStory && isTitle){
					// Populate results with appropriate stories based on title
					results = database.get_stories_by_title(searchEntry.getText().toString());
					
				} else if (isStory && !isTitle){
					// Populate results with appropriate stories based on author
					results = database.get_stories_by_author(searchEntry.getText().toString());
					
				} else if (!isStory && isTitle){
					// Populate results with appropriate pages based on title
					results = database.get_pages_by_title(searchEntry.getText().toString());
					
				} else {
					// Populate results with appropriate pages based on author
					results = database.get_pages_by_author(searchEntry.getText().toString());
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
        //Spinner searchOnline = (Spinner)parent;
		// Set isTitle boolean to reflect spinner selection
		long selected = (long) parent.getItemIdAtPosition(pos);
		if(searchBy.getId() == R.id.spinnerSearchBy){
			if (selected == 0){
				// User selected to search by title
				isTitle = true;
			} else if (selected == 1){
				// User selected to search by author
				isTitle = false;
			}
		}
		/*if(searchOnline.getId() == R.id.spinnerOnline){
			if (selected == 0){
				
			} else if (selected == 1){
				
			}
		}*/
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {}
	
	// Updates the list view with the results for the user
	public void updateList(ArrayList<?> results){
		displayResults.clear();
		System.out.println("size of results in updateList: " + results.size());
		// Change the array to contain the strings to display
		for (int i = 0; i < results.size(); i++){
			displayResults.add(results.get(i).toString());
		}
		// update view
		adapter2.notifyDataSetChanged();
		
	}

}
