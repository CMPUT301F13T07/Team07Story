/**
 * Purpose: This class is used to handle searching for stories and pages. The user can enter a
 * String that is either partly or entirely matching the author or title (after selecting either 
 * author or title). If the user has a blank search it will return all pages or stories.
 * 
 * Author: Kelsey Gaboriau
 */
package edu.ualberta.adventstory;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import edu.ualberta.data.DataManager;
import edu.ualberta.data.DbManager;
import edu.ualberta.data.WebClient;
import edu.ualberta.utils.Page;
import edu.ualberta.utils.Story;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SearchActivity extends Activity implements OnItemSelectedListener,
														OnItemClickListener{
	
	private EditText searchEntry;
	private ListView listResults;
	private Spinner searchBy, searchOnline;
	
	// Database
	private DataManager searchStruct;
	private DbManager database;
	private WebClient webClient;
	
	//private Spinner searchOnline;
	private Bundle bundle;
	private boolean isStory, isTitle;
	String parentActivity;
	private boolean addPage;	// Set to true if adding page in PageEdit.
	
	private ArrayList<?> results;
	private ArrayList<String> displayResults;
	
	// Create Adapter for results list
	private ArrayAdapter<CharSequence> adapter;
	private ArrayAdapter<String> adapter2;
	private ArrayAdapter<CharSequence> adapter3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story_search);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); 
		StrictMode.setThreadPolicy(policy);
		
		if(isStory){
			setTitle(R.string.findStory);
		} else {
			setTitle(R.string.findPage);
		}
		
		// Initialize variables
		isTitle = true;
		bundle = getIntent().getBundleExtra("android.intent.extra.INTENT");
		isStory = bundle.getBoolean("BOOL_IS_STORY");
		parentActivity = bundle.getString("PARENT_ACTIVITY");
		results = new ArrayList<String>();
		displayResults = new ArrayList<String>();
		database = DataSingleton.database;
		webClient = new WebClient();
		searchStruct = database;

		addPage = false;		
		addPage = bundle.getBoolean("ADD_PAGE");
		if(isStory){
			setTitle(R.string.findStory);
		} else {
			setTitle(R.string.findPage);
		}
		// If adding page, don't clear page Stack.
		if( addPage == false){
			// Always empty Page Stack.
			((DataSingleton)getApplicationContext()).clearPageStack();
		}
		
		// Initialize view variables
		searchOnline = (Spinner) findViewById(R.id.spinnerOnline);
		searchBy = (Spinner) findViewById(R.id.spinnerSearchBy);
		listResults = (ListView) findViewById(R.id.listResults);
		searchEntry = (EditText) findViewById(R.id.searchEntry);
		
		// Initialize adapters
		adapter = ArrayAdapter.createFromResource(this, R.array.searchBy_dropdown, android.R.layout.simple_spinner_item);
		if (isStory){
			adapter2 = new ArrayAdapter<String>(this, R.layout.rowstorylayout, R.id.label, displayResults);
		} else {
			adapter2 = new ArrayAdapter<String>(this, R.layout.rowpagelayout, R.id.label, displayResults);
		}
		adapter3 = ArrayAdapter.createFromResource(this, R.array.online_toggle, android.R.layout.simple_spinner_item);

		// Set up adapters
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		searchBy.setAdapter(adapter);
		searchOnline.setAdapter(adapter3);
		listResults.setAdapter(adapter2);
		
		// Populate with all results
		if (isStory) {
			results = database.get_stories_by_author(searchEntry.getText().toString());
		} else {
			results = database.get_pages_by_author(searchEntry.getText().toString());
		}
		
		// This is to test PageViewActivity -- Feel free to delete this.
		listResults.setClickable(true);
		listResults.setOnItemClickListener(this);
		
		searchBy.setOnItemSelectedListener(this);
		searchOnline.setOnItemSelectedListener(this);
			
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
            	Page newPage = new Page("", "", "", null);
            	int id = (int) database.insert_page(newPage);
            	newPage.setID(id);
            	((DataSingleton)getApplicationContext()).setCurrentPage(newPage);
            	((DataSingleton)getApplicationContext()).setCurrentStory(null);
            	startActivity(new Intent(getBaseContext(), PageEditActivity.class));
                return true;
            case R.id.action_help:
            	Toast.makeText(getApplicationContext(), "Enter your search options.\n"
            											+ "Press an item to view it.",
						   Toast.LENGTH_LONG).show();
            	return true;
            case R.id.action_home:
            	finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
	private void searchText() {
		searchEntry.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable s) {	
				searchResults();

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {}
	      
	    }); 
		
	}
	
	public void searchResults(){
		// Check if the user is searching pages or stories
		if (isStory && isTitle){
			// Populate results with appropriate stories based on title
			results = searchStruct.get_stories_by_title(searchEntry.getText().toString());
			
		} else if (isStory && !isTitle){
			// Populate results with appropriate stories based on author
			results = searchStruct.get_stories_by_author(searchEntry.getText().toString());
			
		} else if (!isStory && isTitle){
			// Populate results with appropriate pages based on title
			results = searchStruct.get_pages_by_title(searchEntry.getText().toString());
			
		} else {
			// Populate results with appropriate pages based on author
			results = searchStruct.get_pages_by_author(searchEntry.getText().toString());
		}
		
		updateList(results);
		
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
				searchResults();
			} else if (selected == 1){
				// User selected to search by author
				isTitle = false;
				searchResults();
			}
		}

		if(searchOnline.getId() == R.id.spinnerOnline){
				if (selected == 0){
					searchStruct = database;
					searchResults();
				} else if (selected == 1){
					if(!webClient.isConnected(this)){
						webClient.buildDialog(this).show();
					} else {  
					searchStruct = webClient;
					searchResults();
					}
				}
		}
		
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
		ArrayList<Page> pageList;
		Page page;
		String title;
		String text;
		if(addPage){
			page = (Page) results.get(position);
			this.setResult((int)page.getID());
			finish();
		}else if(isStory) {
			story = (Story) results.get(position);
			((DataSingleton)getApplicationContext()).setCurrentStory(story);
			pageList = story.getAllPages();

			Bundle bundle = new Bundle();
			bundle.putSerializable("pageList", pageList);
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			SearchPageListFragment listpages = new SearchPageListFragment();
			
			// Setting arguments to be passed
			listpages.setArguments(bundle);
			fragmentTransaction.replace(android.R.id.content, listpages);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
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
			
			//Setting arguments to be passed
			preview.setArguments(bundle);
			fragmentTransaction.replace(android.R.id.content, preview);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
			
			if (parentActivity != null && parentActivity.compareTo("CreateNewStoryActivity") == 0) {
				story = ((DataSingleton)getApplicationContext()).getCurrentStory();
				story.setRoot(page);
				database.update_story(story);
			}
		}
	}
}
