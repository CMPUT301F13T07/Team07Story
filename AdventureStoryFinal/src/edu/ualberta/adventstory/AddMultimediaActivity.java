package edu.ualberta.adventstory;

import java.io.File;
import java.util.*;

import edu.ualberta.database.DbManager;
import edu.ualberta.multimedia.*;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class AddMultimediaActivity extends Activity {
	
	private ArrayList<String> medialist = new ArrayList<String>();
	private ArrayAdapter<String> adapter;
	private DbManager database;
	private int page_id;
	
	private Uri imageuri;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private String folder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/media";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//database = ((ChooseYourAdventure07)this.getApplication()).database;
		database = new DbManager(this);
		database.open();
		
		File folderF = new File(folder);
        if (!folderF.exists()) {
            folderF.mkdir();
        }
        ListView lv = (ListView) findViewById(R.id.amlist);
        ListView rlv = (ListView) findViewById(R.id.retakelist);
        
        adapter = new ArrayAdapter<String>(this, R.layout.amlist_item, medialist);
		lv.setAdapter(adapter);
		rlv.setAdapter(adapter);
		
		setClickListeners();
		lv.setOnItemClickListener(new ListView.OnItemClickListener() {
	        public void onItemClick(AdapterView<?> a, View v, int pos, long id) {
	        	saveToDB((int)id);
	        }
	    });
		rlv.setOnItemClickListener(new ListView.OnItemClickListener() {
	        public void onItemClick(AdapterView<?> a, View v, int pos, long id) {
	        	saveToDB((int)id);
	        }
	    });
	}
	
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		setContentView(R.layout.activity_addmedia);
		
		File Ffolder = new File(folder);
		File[] files = Ffolder.listFiles();
		if (files != null) 
			for (int i=0; i<files.length; i++) 
				if (!medialist.contains(files[i].getName()))
					medialist.add(files[i].getName());
			
		adapter.notifyDataSetChanged();
		
		Bundle args = getIntent().getExtras();
		page_id = args.getInt("page_id");
	}
	
	private void saveToDB(int id) {
		String entry = folder + medialist.get(id);
		Picture pic = new Picture(0, entry);
		database.insert_multimedia(pic, page_id);
	}
	
	private void setClickListeners() {
		Button takephoto = (Button) findViewById(R.id.takephoto);
		Button retakePhoto = (Button) findViewById(R.id.retakephoto);
		Button gallery = (Button) findViewById(R.id.gallery);
		Button finish = (Button) findViewById(R.id.finish);
		
		takephoto.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				takePhoto();
			}
		});
		retakePhoto.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				takePhoto();
			}
		});
		gallery.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
			}
		});
		finish.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				setResult(RESULT_OK);
				finish();
			}
		});
		
	}
	
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
            }
        }
    }
}
