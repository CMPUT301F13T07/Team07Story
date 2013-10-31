package edu.ualberta.adventstory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import edu.ualberta.database.Db;

public class StartActivity extends Activity {

	private Button mkStory, publish, searchPage, searchStory, mkPage;
	public Db database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Create instance of the database
        database = new Db(this);
        database.open();
        
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
    			// Call code for publishing a story
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
    
	/* Called when the user clicks the Make a New Story button */
	private void newStory(){
		// Bring up Option Edit Activity
		Intent logViewIntent = new Intent(this, CreateNewStoryActivity.class);
		startActivity(logViewIntent);
	}
	
	/* Called when the user clicks a find button */
	private void search(boolean isStory){
		// Bring up  Activity and pass boolean telling it if we are finding a story or page
		Bundle bundle = new Bundle();
		bundle.putBoolean("BOOL_IS_STORY", isStory);
		// This will be the database bundle.putSerializable(key, value)
		
		Intent searchIntent = new Intent(this, CreateNewStoryActivity.class);
		searchIntent.putExtra("android.intent.extra.INTENT", bundle);
		startActivity(searchIntent);
	}
    
}

