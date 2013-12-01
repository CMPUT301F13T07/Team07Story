/**
 * Purpose: This is the main screen that the user sees when the application starts. 
 * From here the user can navigate to the various functionalities of the application.
 * 
 * Outstanding Issues: N/A
 * 
 * Author: Kelsey Gaboriau wrote the set up for this class then each respective programmer
 * filled in necessary components in the buttons they are using.
 * 
 * The background for this application was found at http://alphacentauri900.blogspot.ca/2012/09/compass-background.html
 */
package edu.ualberta.adventstory;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import edu.ualberta.data.DataManager;
import edu.ualberta.utils.Page;

public class StartActivity extends Activity {

	private Button mkStory, searchPage, searchStory, mkPage, randomStory;
	public DataManager database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		setTitle(R.string.app_name);
        
        database = DataSingleton.database;
        
        // Define Buttons
        mkStory = (Button)findViewById(R.id.newStory);
    	searchPage = (Button)findViewById(R.id.findPage);
    	searchStory = (Button)findViewById(R.id.findStory);
    	mkPage = (Button)findViewById(R.id.newPage);
    	randomStory = (Button) findViewById(R.id.randomStory);
    	
    	// Check for user button clicks
    	mkStory.setOnClickListener(new OnClickListener(){
    		public void onClick(View v){
    			// Bring up EditOptionActivity
    			newStory();
    		}
    	});
    	
    	searchPage.setOnClickListener(new OnClickListener(){
    		public void onClick(View v){
    			// Bring up Story Search Activity
    			search(false);
    		}
    	});
    	
    	mkPage.setOnClickListener(new OnClickListener(){
    		public void onClick(View v){
    			// Bring up PageEditActivity.
    			newPage();
    		}
    	});
    	
    	searchStory.setOnClickListener(new OnClickListener(){
    		public void onClick(View v){
    			// Bring up Story Search Activity
    			search(true);
    		}
    	});
    	
    	randomStory.setOnClickListener(new OnClickListener(){
    		public void onClick(View v){
    			// Check if there are any available stories. If not, inform user
    			// with a toast. Otherwise display root page of story for reading.
    			int numStories = database.number_stories();
    			if (numStories == 0){
    				Toast.makeText(getApplicationContext(), "Sorry, there are no stories available.",
    						   Toast.LENGTH_LONG).show();
    			} else {
    				readRandom(randomGen(numStories));
    			}
    		}
    	});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_help:
            	Toast.makeText(getApplicationContext(), "Press a button to continue.",
						   Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    /**
     * Author: Michelle Naylor
     */
	/* Called when the user clicks the Make a New Story button */
	private void newStory(){
		// Bring up CreateNewStoryActivity
		Intent logViewIntent = new Intent(this, CreateNewStoryActivity.class);
		startActivity(logViewIntent);
	}
	
	/**
	 * Author: Kelsey Gaboriau
	 * 
	 * @param isStory
	 */
	/* Called when the user clicks a find button */
	private void search(boolean isStory){
		// Bring up  Activity and pass boolean telling it if we are finding a story or page
		Bundle bundle = new Bundle();
		bundle.putBoolean("BOOL_IS_STORY", isStory);
		
		Intent searchIntent = new Intent(this, SearchActivity.class);
		searchIntent.putExtra("android.intent.extra.INTENT", bundle);
		startActivity(searchIntent);
	}
	
	/* Called when the user clicks new page button */
	private void newPage(){
		Page newPage = new Page("", "", "", null);
		int id = (int) database.insert_page(newPage);
		newPage.setID(id);
		((DataSingleton)getApplicationContext()).setCurrentPage(newPage);
		((DataSingleton)getApplicationContext()).setCurrentStory(null);
		startActivity(new Intent(getBaseContext(), PageEditActivity.class));
	}
	
	/* Called when the user clicks on read random story button and stories are available */
	private void readRandom(int index){
		// Set the DataSingleton
		((DataSingleton)getApplicationContext()).setCurrentStory(database.get_story_by_id(index));
		((DataSingleton)getApplicationContext()).setCurrentPage(database.get_story_by_id(index).getRoot());
		// Bring up PageViewActivity
		Intent randStoryIntent = new Intent(this, PageViewActivity.class);
		startActivity(randStoryIntent);
	}
	
	/* Random number generator used to find a random story */
	private int randomGen(int high){
		double rand = Math.random();
		double num = rand * high;
		int rtn = (int) num + 1;
		return rtn;
	}
}

