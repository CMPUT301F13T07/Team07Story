package com.example.proto02;

// TODO: Mock datas are currently being used to test.
// TODO: Record the last scroll index.
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView.BufferType;
import android.widget.ImageView;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.text.style.ClickableSpan;
import android.text.Spannable;
import android.text.Spanned;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.media.MediaPlayer;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioTrack;
import android.media.AudioManager;
import android.media.AudioFormat;

import com.example.utils.Page;
import com.example.utils.Story;
import com.example.multimedia.MultimediaDB;
import com.example.utils.Utility;
public class PageViewActivity extends Activity implements VideoPreviewInterface{
	// Not the base activity. Transfer this to the base activity then.
	protected ChooseYourAdventure07 mAdventureTime;

	
	private TextView mStoryTitleTextView;			// Story Title TextView.
	private TextView mPageTitleTextView;			// Page Title TextView.
	private TextView mStoryTextView;				// StoryText TextView.
	/*
	 * The structure of this Activity is the following:
	 * -An outer layout
	 *  contains the stationary components such as title and buttons for
	 *  next Page(s). A ScrollView and LinearLayout is used to achieve this.
	 *  The existence of ScrollView will be discussed next.
	 *  
	 * -Inner Layout on the other hand contains non-stationary objects.
	 *  This allows room for movable multimedia objects. Although
	 *  the text segment of the story is stationary it is placed in the 
	 *  inner layout since it does expand vertically. Since multimedia(s)
	 *  are movable and text segment expands, we used a ScrollView to 
	 *  make sure that when the inner layout does expands beyond the limit
	 *  of the screensize the user will still have pleasant experience.
	 */
	
	// Layout.
	private RelativeLayout mInnerLayout;			// Inner RelativeLayout.
	private LinearLayout mOuterLayout;				// Outer LinearLayout.
	private ScrollView mScrollView;					// Encapsulate mOuterLayout.
	/*
	 * Params:
	 * - mInnerLayoutParam: For the mInnerLayout.
	 * - mOuterLayoutParam: For the mOuterLayout.
	 * - mInnerComponentParam: Param for the views inside mInnerLayout.
	 * - mOuterLayoutParam: Param for the views in the mOuterLayout excluding mInnerLayout.
	 */
	private RelativeLayout.LayoutParams mInnerLayoutParam;	
	private LinearLayout.LayoutParams mOuterLayoutParam;
	private RelativeLayout.LayoutParams mInnerComponentParam;
	private LinearLayout.LayoutParams mOuterComponentParam;
	
	private Story mStory;							// Story being viewed.
	private Page mPage;								// Current page.
	
	ImageView iv;
	long bIndex;
	long b2;
	Bitmap bm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mAdventureTime = (ChooseYourAdventure07)this.getApplicationContext();
		
		// Testm image loading.
		File imageFile = new File("/sdcard/Pictures/herp_derp.jpg");
		if(imageFile.exists()){
			//Toast.makeText(this, "IMAGE FOUND!!", Toast.LENGTH_LONG).show();
			
			bm = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
		    
		    iv = new ImageView(this);
		    iv.setImageBitmap(bm);
		    
			// Multimedia Database Test.
		    MultimediaDB mdb = new MultimediaDB(this);
		    mdb.open();
		    bIndex = mdb.insertBitmap("herpderp", "jpg", bm);
			mdb.close();
			
			mdb = new MultimediaDB(this);
			mdb.open();
			imageFile = new File("/sdcard/Pictures/troll.jpg");
			bm = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
			b2 = mdb.insertBitmap("troll", "jpg", bm);
			mdb.close();
		    iv.setId(666);
		}else{
			Toast.makeText(this, "IMAGE EPIC FAIL!!", Toast.LENGTH_LONG).show();
		}
		
		//**MOCK DATA**		
		String story = "\tOnce upon a time laser guns create pewpew followed by " +
						"dying, thus the word pewpewdie. Troll cats meme's seemed to emerged " +
						"from this phenomenon, inevitably, troll faces followed." +
						"Mystery still remains wether troll faces came first before trolls or vice versa."+
						" These species of human race are often refered to as HERPDERPS." +
						" HERPDERP is the politcally incorrect alternative to the word stupid, a love child"+
						" of the trolls, troll faces, and troll cats."+
						"\n\n" + "\tHERPDERPS are the world master of puns in youtube. They often "+
						"have nazi germany puns that annefrankly I don't care, I do nazi the purpose "+
						"in using them in sentence. They are beyond mein kampfort zone and also requires" +
						"alot of concentration. Camp these HERPDERP's do something else than offend people " +
						"any fuhrer. This is simply aint Reich. They should burn to heil or something.";
		mPage = new Page(0, "pewpewdie", "Trollmaster", story, null);
		mStory = new Story("Awesomeness", "Trollmaster", mPage);
		//**END OF MOCK DATA**.
		
		// Layout for mOuterLayout.
		mOuterLayoutParam = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		// Layout for mInnerLayout.
		mInnerLayoutParam = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		// Layout for Option Body Views.
		mInnerComponentParam = new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		
		// Layout for mOuterLayout Components.
		mOuterComponentParam = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

		// ScrollView.
		mScrollView = new ScrollView(this);
		
		// Outer Layout.
		mOuterLayout = new LinearLayout(this);
		// Imperative else will display in awkward horizontal order.
		mOuterLayout.setOrientation(LinearLayout.VERTICAL);	
		// Encapsulate with scrollView.
		mScrollView.addView(mOuterLayout, mOuterLayoutParam);
		
		// Initialize the Story TextView and its parameters.
		mStoryTitleTextView = new TextView(this);
		mStoryTitleTextView.setLayoutParams(mOuterComponentParam);
		mOuterLayout.addView(mStoryTitleTextView);
		setStoryTitle(mStory.getTitle(), 26);

		//Initialize the Page Title TextView and its parameters.
		mPageTitleTextView = new TextView(this);
		mPageTitleTextView.setLayoutParams(mOuterComponentParam);
		mOuterLayout.addView(mPageTitleTextView);
		setPageTitle(mPage.getTitle(), 22);
						
		// The Inner Layout.
		mInnerLayout = new RelativeLayout(this);
		
		// Add Inner Layout components.
		mStoryTextView = new TextView(this);
		mStoryTextView.setLayoutParams(mInnerComponentParam);
		mInnerLayout.addView(mStoryTextView);
		setStoryText(mPage.getText(), 18);
		
		// Place the inner layout inside the outer layout.
		mOuterLayout.addView(mInnerLayout, mInnerLayoutParam);
		
		Button btn = new Button(this);
		btn.setText("Next Option");
		btn.setLayoutParams(mInnerComponentParam);
		mOuterLayout.addView(btn);
				
		this.setContentView(mScrollView, mOuterLayoutParam);
	}

	
	long tempI;
	public void onStart(){
		super.onStart();
		// Test audio loading.
		File audioFile = new File("/sdcard/Music/pewpew.mp3");
		if(audioFile.exists()){
			Toast.makeText(this, "AUDIO WOHOOOO!", Toast.LENGTH_LONG).show();			
			try {
				byte[] byteArray = MultimediaDB.load(audioFile, this);
				MultimediaDB mdb = new MultimediaDB(this);
				mdb.open();
				tempI = mdb.insertSound("pewpew", "mp3", byteArray);
				mdb.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}else{
			Toast.makeText(this, "AUDIO EPIC FAIL!!", Toast.LENGTH_LONG).show();
		}
	}
	
	
	protected void onResume(){
		super.onResume();
		mAdventureTime.setCurrentActivity(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.page_view, menu);
		CreateMenu(menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		return MenuChoice(item);
	}
	
	private boolean MenuChoice(MenuItem item){
		switch(item.getItemId()){
		case 0:
			//startActivity(new Intent("com.example.proto01.OptionEditActivity"));
			return true;
		}
		return false;
	}
	
	void CreateMenu(Menu menu){
		MenuItem mnu1 = menu.add(0, 0, 0, "Edit Option");
		{
			mnu1.setIcon(R.drawable.ic_launcher);
			mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);  // This is subject to change.
		}
		
		// I reserve to implement the others in case we decide to automate 
		// caching.
	}
	
	void AddNextOptionButton(/*Option nextOption*/){
		
	}
	
	void setStoryTitle( String storyTitle, float textSize ){
		mStoryTitleTextView.setSingleLine(true);
		mStoryTitleTextView.setText(storyTitle);
		mStoryTitleTextView.setTextSize(textSize);
	}
	
	void setPageTitle( String pageTitle, float textSize ){
		mPageTitleTextView.setSingleLine(true);
		mPageTitleTextView.setText(pageTitle);
		mPageTitleTextView.setTextSize(textSize);
	}

	void setStoryText( String storyText, float textSize){
		int index = storyText.indexOf("HERPDERPS are the world");
		
		// Add Multimedia Test.
		MultimediaDB mdb = new MultimediaDB(this);
		mdb.open();
		Bitmap bitmap = mdb.getBitmap(bIndex);
		Bitmap bitmap2 = mdb.getBitmap(b2);
		mdb.close();
		ImageSpan is = new ImageSpan(this, bitmap);
		ImageSpan is2 = new ImageSpan(this, bitmap2 );
		
		SpannableStringBuilder stringBuilder = new SpannableStringBuilder(storyText);		
		stringBuilder.insert(index, " ");
		stringBuilder.setSpan(is, index, index+1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		
		int start = stringBuilder.getSpanStart(is);
		int end = stringBuilder.getSpanEnd(is);
		
		ClickableSpan cs = new ClickableSpan(){
			@Override
			public void onClick(View widget){
				Toast.makeText(getBaseContext(), "Image Clicked ", Toast.LENGTH_SHORT).show();
				
				PageViewActivity  pva = (PageViewActivity)((ChooseYourAdventure07)getApplicationContext()).getCurrentActivity();
				pva.switchVideoViewPreview();
			}
		};
		stringBuilder.setSpan(cs, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		
		stringBuilder.insert(0, " ");
		stringBuilder.setSpan(is2, 0, 0+1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		ClickableSpan cs2 = new ClickableSpan(){
			@Override
			public void onClick(View widget){
				Toast.makeText(getBaseContext(), "Image Clicked ", Toast.LENGTH_SHORT).show();				
				
				
				MultimediaDB mdb = new MultimediaDB(getBaseContext());
				mdb.open();
				byte[] byteArray = mdb.getSound(tempI);
				mdb.close();
				playMp3(byteArray);	
			}
		};
		stringBuilder.setSpan(cs2, 0, 0+1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		mStoryTextView.setMovementMethod(LinkMovementMethod.getInstance());
		mStoryTextView.setText(stringBuilder, BufferType.SPANNABLE);
		mStoryTextView.setTextSize(textSize);
	}
	
	void playMp3(byte[] mp3SoundByteArray) {
	    try {
	        // create temp file that will hold byte array
	    	File tempMp3 = File.createTempFile("kurchina", "mp3", getCacheDir());
	        tempMp3.deleteOnExit();
	        FileOutputStream fos = new FileOutputStream(tempMp3);
	        fos.write(mp3SoundByteArray);
	        fos.close();

	        // Tried reusing instance of media player
	        // but that resulted in system crashes...  
	        MediaPlayer mediaPlayer = new MediaPlayer();

	        // Tried passing path directly, but kept getting 
	        // "Prepare failed.: status=0x1"
	        // so using file descriptor instead
	        FileInputStream fis = new FileInputStream(tempMp3);
	        mediaPlayer.setDataSource(fis.getFD());

	        mediaPlayer.prepare();
	        mediaPlayer.start();
	    } catch (IOException ex) {
	        String s = ex.toString();
	        ex.printStackTrace();
	    }
	}
	
	void switchVideoViewPreview(){				
		VideoViewPreview vvp = new VideoViewPreview(getBaseContext());
		MediaPlayer mp = vvp.getMediaPlayer();		
		this.setContentView(vvp);
	}
	
	public void switchOriginalLayout(){
		this.setContentView(mScrollView, mOuterLayoutParam);
	}
}
