package edu.ualberta.adventstory;

//TODO: Mock datas are currently being used to test.
//TODO: Record the last scroll index.
//TODO: Handle backbutton to display back story. Ask team if adding an explicit button
//		 is better.
import java.util.ArrayList;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView.BufferType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.widget.TextView;
import android.media.MediaPlayer;

import edu.ualberta.adventstory.R;
import edu.ualberta.adventstory.PageEditActivity.ClickableDeleteSpanEx;
import edu.ualberta.adventstory.PageEditActivity.ClickableMultimediaSpanEx;
import edu.ualberta.adventstory.PageEditActivity.PaddingableImageSpan;
import edu.ualberta.multimedia.MultimediaAbstract;
import edu.ualberta.utils.Page;
import edu.ualberta.utils.Story;

public class PageViewActivity extends ActivityExtended{
	// Not the base activity. Transfer this to the base activity then.
	protected DataSingleton mDataSingleton;			// Application instance.
	
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
	
	private String mStoryTitle;						// Story being viewed.
	private Page mPage;								// Current page.
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mDataSingleton = (DataSingleton)this.getApplicationContext();
				
		
		
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
		setStoryTitle(mStoryTitle, 26);

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
		setStoryText(18);
		
		// Place the inner layout inside the outer layout.
		mOuterLayout.addView(mInnerLayout, mInnerLayoutParam);
		
		AddButtons();
		
		if(mOnVideoViewPreview){
			this.switchToVideoViewPreview(mVideoDirectory);
		}else{
			this.setContentView(mScrollView, mOuterLayoutParam);
		}
	}

	@Override
	public void onStart(){
		super.onStart();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		mDataSingleton.setCurrentActivity(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.page_view, menu);
		CreateMenu(menu);
		return true;
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
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
	
	@SuppressLint("NewApi")
	void CreateMenu(Menu menu){
		MenuItem mnu1 = menu.add(0, 0, 0, "Edit Option");
		{
			mnu1.setIcon(R.drawable.ic_launcher);
			mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);  // This is subject to change.
		}
		
		// I reserve to implement the others in case we decide to automate 
		// caching.
	}
	
	void AddButtons(){
		for(final Page p : mPage.getPages()){
			Button btn = new Button(this);
			btn.setText(p.getTitle());
			btn.setLayoutParams(mInnerComponentParam);
			btn.setOnClickListener(new OnClickListener(){
				@SuppressLint("NewApi")
				@Override
				public void onClick(View view){
					// Go to the next page.
					mDataSingleton.setCurrentPage(p);
					mDataSingleton.getCurrentActivity().recreate();
				}
			});
			mOuterLayout.addView(btn);
		}
	}
	
	void setPage(Page page){
		mPage = page;
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

	void setStoryText( float textSize){
		mStoryTextView.setMovementMethod(LinkMovementMethod.getInstance());						
		mStoryTextView.setText(getSpannableStringBuilder(), BufferType.SPANNABLE);
		mStoryTextView.setTextSize(textSize);
	}
	
	// ClickableSpan for Multimedia.
	// - A developer might notice that this ClickableMultimediaSpanEx 
	//   also exist in PageEditActivity and that these should merit their
	//   own module. On the contrary, that is simply not possible. These
	//   have to be inner classes because they rely on attributes of the
	//   class their nesting in.
	class ClickableMultimediaSpanEx extends ClickableSpan {
		private MultimediaAbstract mMultimedia;
		private ImageSpan mImageSpan;

		public ClickableMultimediaSpanEx(MultimediaAbstract ma,
				ImageSpan multimediaImageSpan) {
			super();
			mMultimedia = ma;
			mImageSpan = multimediaImageSpan;
		}

		@SuppressLint("NewApi")
		@Override
		public void onClick(final View widget) {
			// Set all multimedia mIsSelected to false.
			mMultimedia.play(getBaseContext());
		}
	}
	
	public SpannableStringBuilder getSpannableStringBuilder() {
		ArrayList<MultimediaAbstract> ma = mPage.getMultimedia();

		SpannableStringBuilder stringBuilder = new SpannableStringBuilder(mPage.getText());

		// Return to caller if ma is null to avoid trivial errors.
		if( ma == null ){
			return stringBuilder;
		}
		
		for (MultimediaAbstract multimedia : ma) {
			// Load the multimedia Picture representation.
			Bitmap multimediaBitmap = multimedia.loadPhoto(this);
			ImageSpan multimediaImageSpan = new ImageSpan(this, multimediaBitmap, 20);
			
			
			stringBuilder.insert(multimedia.getIndex(), " ");		// Allocate space for multimediaImageSpan.
			
			stringBuilder.setSpan(multimediaImageSpan, multimedia.getIndex(),
					multimedia.getIndex() + 1,
					Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

			ClickableMultimediaSpanEx multimediaClickableSpan = 
					new ClickableMultimediaSpanEx(multimedia, multimediaImageSpan);

			stringBuilder.setSpan(multimediaClickableSpan, multimedia.getIndex(),
					multimedia.getIndex() + 1,
					Spannable.SPAN_INCLUSIVE_EXCLUSIVE);			
		}
		return stringBuilder;
	}
	
	@Override
	// Video Preview 
	public void switchToVideoViewPreview(String directory){
		super.switchToVideoViewPreview(directory);
	}
	
	@Override
	public void switchToOriginalLayout(){	
		super.switchToOriginalLayout();
		this.setContentView(mScrollView, mOuterLayoutParam);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
	    if (Integer.parseInt(android.os.Build.VERSION.SDK) < 5
	            && keyCode == KeyEvent.KEYCODE_BACK
	            && event.getRepeatCount() == 0) {	
	        onBackPressed();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	// TODO: Make sure to link to last activity when no oldPage.
	@SuppressLint("NewApi")
	@Override
	public void onBackPressed(){
		if( mDataSingleton.getOldPage() == null ){
			super.onBackPressed();
		}else{
			mDataSingleton.revertPage();
			this.recreate();
		}
	}
}
