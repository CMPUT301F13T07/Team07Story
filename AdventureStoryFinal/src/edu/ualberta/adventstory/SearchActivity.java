package edu.ualberta.adventstory;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import edu.ualberta.data.DbManager;
import edu.ualberta.utils.Page;
import edu.ualberta.utils.Story;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SearchActivity extends Activity implements OnItemSelectedListener,
														OnItemClickListener{
	
	private EditText searchEntry;
	private ListView listResults;
	private Spinner searchBy;
	
	// Database
	private DbManager database;
	
	//private Spinner searchOnline;
	private Bundle bundle;
	private boolean isStory;
	private boolean isTitle;
	private String parentActivity;
	
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
		parentActivity = bundle.getString("PARENT_ACTIVITY");
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
		if (isStory) {
			results = database.get_stories_by_author(searchEntry.getText().toString());
		} else {
			results = database.get_pages_by_author(searchEntry.getText().toString());
		}
		updateList(results);
		
		// This is to test PageViewActivity -- Feel free to delete this.
		listResults.setClickable(true);
		listResults.setOnItemClickListener(this);
		
		searchBy.setOnItemSelectedListener(this);
		
		// Always empty Page Stack.
		((DataSingleton)getApplicationContext()).clearPageStack();
		
		// Checks to see if the user has input text into the search box
		searchText();	
		
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.new_page:
            		// TODO: add page to database if it is a part of a story
            		Page newPage = new Page("", "", "", null);
            		int id = (int) database.insert_page(newPage);
            		newPage.setID(id);
            		((DataSingleton)getApplicationContext()).setCurrentPage(newPage);
            		((DataSingleton)getApplicationContext()).setCurrentStory(null);
            		startActivity(new Intent(getBaseContext(), PageEditActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Story story;
		Page page;
		String title;
		String text;
		if (isStory) {
			story = (Story) results.get(position);
			((DataSingleton)getApplicationContext()).setCurrentStory(story);
			/*
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			SearchPageListFragment listpages = new SearchPageListFragment();
			
			fragmentTransaction.replace(android.R.id.content, listpages);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
			*/
		} else {
			page = (Page) results.get(position);
			((DataSingleton)getApplicationContext()).setCurrentPage(page);
			title = page.getTitle();
			text = page.getText();
			
			Bundle bundle = new Bundle();
			bundle.putString("title", title);
			bundle.putString("text", text);
			
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			SearchPreviewFragment preview = new SearchPreviewFragment();
			
			//Setting Arguments being passed
			preview.setArguments(bundle);
			fragmentTransaction.replace(android.R.id.content, preview);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
			
			/*
			if (parentActivity.compareTo("CreateNewStoryActivity") == 0) {
				story = ((DataSingleton)getApplicationContext()).getCurrentStory();
				// update story in db
				story.setRoot(page);
				database.update_story(story);
			}*/
		}
	}
}
