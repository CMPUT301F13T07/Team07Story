package edu.ualberta.adventstory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
	private Button mkStory, publish, searchPage, searchStory, mkPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Define Buttons
        mkStory = (Button)findViewById(R.id.newStory);
    	publish = (Button)findViewById(R.id.publish);
    	searchPage = (Button)findViewById(R.id.findPage);
    	
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
		Intent logViewIntent = new Intent(this, OptionEditActivity.class);
		startActivity(logViewIntent);
	}
    
}

