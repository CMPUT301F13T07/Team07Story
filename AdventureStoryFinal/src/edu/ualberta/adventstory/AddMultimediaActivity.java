package edu.ualberta.adventstory;

/*
 * The Add Multimedia Activity
 * Allows a user to select a piece of media from the list for adding to a page as well as going to the camera
 * app to take a new picture. 
 * #author: Lyle Rolleman
 */

import java.io.File;
import java.util.ArrayList;




import edu.ualberta.data.DbManager;
import edu.ualberta.multimedia.*;
import edu.ualberta.utils.Page;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

public class AddMultimediaActivity extends Activity {
	
	/**
	 * imageuri the Uri for taking pictures.
	 */
	private Uri imageuri;
	
	/*
	 * Constant request code for capturing images from camera app
	 */
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	
	/*
	 * The folder where the pictures are stored on the sdcard
	 */
	private String folder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/";
	
	/*
	 * The names of the media items in the directory. 
	 */
	private ArrayList<String> medialist = new ArrayList<String>();
	
	/*
	 * Array adapter for populating list view
	 */
	private ArrayAdapter<String> adapter;
	
	/*
	 * The database handler class
	 */
	private DbManager database;
	
	/*
	 * the page_id of the page the media is to be added to
	 */
	private int page_id;
	
	/*
	 * The index of where the media should be in the text view
	 */
	private int index;
	
	/*
	 * The current id selected in the list view
	 */
	private Integer currid;
	
	/*
	 * The Page object for the current page
	 */
	private Page currpage;
	
	/*
	 * loads database and current page from application class and loads arguments
	 * @author: Lyle Rolleman
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addmedia);
		database = ((DataSingleton)this.getApplication()).database;
		currpage = ((DataSingleton)this.getApplication()).getCurrentPage();
		Bundle args = getIntent().getExtras();
		page_id = args.getInt("page_id");
		index = args.getInt("index");
		addMultimedia();
	}
	
	/*
	 * Allows for selecting of multimedia and taking of photos
	 * @author: Lyle Rolleman
	 */
	private void addMultimedia() {
		File folderF = new File(folder);
        if (!folderF.exists()) {
            folderF.mkdir();
        }
        File[] files = folderF.listFiles();
		if (files != null) 
			for (int i=0; i<files.length; i++) 
				if (!medialist.contains(files[i].getName()))
					medialist.add(files[i].getName());
		
		Button takephoto = (Button) findViewById(R.id.takephoto);
		Button finish = (Button) findViewById(R.id.finish);
		ListView lv = (ListView) findViewById(R.id.amlist);
		adapter = new ArrayAdapter<String>(this, R.layout.am_list_item, medialist);
		
		lv.setAdapter(adapter);
		
		takephoto.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				takePhoto();
			}
		});
		finish.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setResult(RESULT_OK);
				if (currid != null) {
					savePictureToDB();
				}
				//ArrayList<MultimediaAbstract> ma = new ArrayList<MultimediaAbstract>();
				//ma = database.get_multimedia_by_page_id(page_id);
				//Log.w("result", "There is a result at: " + ma.get(0).getID());
				finish();
			}
		});
		lv.setOnItemClickListener(new ListView.OnItemClickListener() {
	        public void onItemClick(AdapterView<?> a, View v, int pos, long id) {
	        	currid = (int)id;
	        }
	    });
	}
	
	/*
	 * Updates the Page object and saves the multimedia to DB
	 * @author: Lyle Rolleman
	 */
	private void savePictureToDB() {
		String dir = folder + medialist.get(currid);
		Picture pic = new Picture(page_id, index, dir);				
		database.insert_multimedia(pic, currpage.getID());
		
		// Reset the Current page stack.
		currpage.getMultimedia().clear();
		((DataSingleton)this.getApplication()).clearPageStack();
		((DataSingleton)this.getApplication()).setCurrentPage(currpage);		
	}
	
	/*
	 * Like addMultimedia, but has a picture preview after taking a picture
	 * @author: Lyle Rolleman
	 */
	private void retakeMultimedia() {
		File folderF = new File(folder);
        if (!folderF.exists()) {
            folderF.mkdir();
        }
        File[] files = folderF.listFiles();
		if (files != null) 
			for (int i=0; i<files.length; i++) 
				if (!medialist.contains(files[i].getName()))
					medialist.add(files[i].getName());
		
		Button retakePhoto = (Button) findViewById(R.id.retakephoto);
		Button refinish = (Button) findViewById(R.id.refinish);
		ListView lv = (ListView) findViewById(R.id.retakelist);
		adapter = new ArrayAdapter<String>(this, R.layout.am_list_item, medialist);
		
		lv.setAdapter(adapter);
		
		retakePhoto.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				takePhoto();
			}
		});
		refinish.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setResult(RESULT_OK);
				finish();
			}
		});

	}
	
	/*
	 * Goes to the camera app to take a photo
	 * @author: From CameraTest, modified by Lyle Rolleman
	 */
	public void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        
        File folderF = new File(folder);
        if (!folderF.exists()) {
            folderF.mkdir();
        }
        
        String imageFilePath = folder + "/" + String.valueOf(System.currentTimeMillis()) + "jpg";
        File imageFile = new File(imageFilePath);
        imageuri = Uri.fromFile(imageFile);
        
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }
	
	/*
	 * Switches to retake content view and adds the taken picture to the list
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
            	setContentView(R.layout.activity_addmediaretake);
            	ImageView iv = (ImageView) findViewById(R.id.photo);
            	iv.setImageDrawable(Drawable.createFromPath(imageuri.getPath()));
            	File Ffolder = new File(folder);
        		File[] files = Ffolder.listFiles();
        		if (files != null) 
        			for (int i=0; i<files.length; i++) 
        				if (!medialist.contains(files[i].getName()))
        					medialist.add(files[i].getName());
        			
        		adapter.notifyDataSetChanged();
        		retakeMultimedia();
            }
        }
    }
}
