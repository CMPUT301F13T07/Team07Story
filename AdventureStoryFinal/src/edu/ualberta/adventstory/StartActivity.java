/**
 * Purpose: This is the main screen that the user sees when the application starts. 
 * From here the user can navigate to the various functionalities of the application.
 * 
 * Outstanding Issues: N/A
 * 
 * Author: Kelsey Gaboriau wrote the set up for this class then each respective programmer
 * filled in necessary components in the buttons they are using.
 */
package edu.ualberta.adventstory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import edu.ualberta.data.DataManager;
import edu.ualberta.utils.Page;

public class StartActivity extends Activity {

	private Button mkStory, publish, searchPage, searchStory, mkPage;
	public DataManager database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        database = DataSingleton.database;
        
        // Define Buttons
        mkStory = (Button)findViewById(R.id.newStory);
    	publish = (Button)findViewById(R.id.publish);
    	searchPage = (Button)findViewById(R.id.findPage);
    	searchStory = (Button)findViewById(R.id.findStory);
    	mkPage = (Button)findViewById(R.id.newPage);
    	
    	// Check for user button clicks
    	mkStory.setOnClickListener(new OnClickListener(){
    		public void onClick(View v){
    			// Bring up EditOptionActivity
    			newStory();
    		}
    	});
    	
    	publish.setOnClickListener(new OnClickListener(){
    		public void onClick(View v){
    			// Call code for publishing a story
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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
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
}

