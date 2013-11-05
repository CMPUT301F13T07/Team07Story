package edu.ualberta.adventstory;

import java.io.File;
import java.util.ArrayList;



import edu.ualberta.database.DbManager;
import edu.ualberta.multimedia.*;
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
	
	private Uri imageuri;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private String folder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures";
	private ArrayList<String> medialist = new ArrayList<String>();
	private ArrayAdapter<String> adapter;
	private DbManager database;
	private int page_id;
	private int index;
	private Integer currid;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addmedia);
		database = ((DataSingleton)this.getApplication()).database;
		Bundle args = getIntent().getExtras();
		page_id = args.getInt("page_id");
		index = args.getInt("index");
		addMultimedia();
	}
	
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
				if (currid != null)
					savePictureToDB();
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
	
	private void savePictureToDB() {
		String dir = folder + medialist.get(currid);
		Picture pic = new Picture(page_id, index, dir);
		database.insert_multimedia(pic, page_id);
	}
	
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
		//Log.w("debug", "returned");
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
