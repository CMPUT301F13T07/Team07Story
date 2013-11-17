package edu.ualberta.adventstory;

import java.util.ArrayList;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
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

import edu.ualberta.adventstory.CommandCollection.Callback;
import edu.ualberta.adventstory.R;
import edu.ualberta.extendedViews.ClickableMultimediaSpan;
import edu.ualberta.multimedia.MultimediaAbstract;
import edu.ualberta.utils.Page;
import edu.ualberta.utils.Story;

/**
 * <code>PageViewActivity</code> is the View responsible for viewing <code>Page</code>.
 * 
 * @author JoeyAndres
 * @version 1.0
 */
public class PageViewActivity extends ActivityExtended{
	private TextView mStoryTitleTextView;			// Story Title TextView.
	private TextView mPageTitleTextView;			// Page Title TextView.
	private TextView mStoryTextView;				// StoryText TextView.
	
	// Layouts.
	private LinearLayout mInnerLayout;				// Inner RelativeLayout.
	private LinearLayout mOuterLayout;				// Outer LinearLayout.
	private ScrollView mScrollView;					// Encapsulate mOuterLayout.

	private RelativeLayout.LayoutParams mInnerLayoutParam;	
	private LinearLayout.LayoutParams mOuterLayoutParam;
	private LinearLayout.LayoutParams mInnerComponentParam;
	private LinearLayout.LayoutParams mOuterComponentParam;
	
	private Story mStory;					// Story being viewed.
	private Page mPage;						// Current page.
	
	private boolean mViewPageOnly = false; 	// True if we are viewing a page independent of story.
	protected MultimediaAbstract mMultimedia;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_pageview);
				
		Intent intent = getIntent();	// Get intent that started this Activity.
		if( intent == null){
			// Unknown state.
			exit();
		}
	
		mPage = mDataSingleton.getCurrentPage();
		mStory = mDataSingleton.getCurrentStory();
		
		if( mStory == null ){ mViewPageOnly = true; }
		
		// Layout for mInnerLayout.
		mInnerLayoutParam = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		// Layout for Option Body Views.
		mInnerComponentParam = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		
		// Layout for mOuterLayout Components.
		mOuterComponentParam = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);

		mScrollView = (ScrollView)findViewById(R.id.scrollView);
				
		mOuterLayout = (LinearLayout)findViewById(R.id.outerLayout);				
		
		// Initialize the Story TextView and its parameters.
		mStoryTitleTextView = (TextView)findViewById(R.id.storyTitle);
		if( mViewPageOnly == false ){
			setStoryTitle(mStory.getTitle(), 26);
		}

		//Initialize the Page Title TextView and its parameters.
		mPageTitleTextView = (TextView)findViewById(R.id.pageTitle);		
		setPageTitle(mPage.getTitle(), 22);
						
		// The Inner Layout.
		mInnerLayout = (LinearLayout)findViewById(R.id.innerLayout);
		
		// Add Inner Layout components.
		mStoryTextView = (TextView)findViewById(R.id.pageText);				
		setStoryText(18);
				
		AddButtons();
	}

	@Override
	protected void exit() {
		mPage.getMultimedia().clear();
		finish();
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
		mPage.getMultimedia().clear();
		super.onDestroy();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		return MenuChoice(item);
	}
	
	private boolean MenuChoice(MenuItem item){
		switch(item.getItemId()){
		case 0:
			startActivity(new Intent(this, PageEditActivity.class));
			return true;
		}
		return false;
	}
	
	@SuppressLint("NewApi")
	void CreateMenu(Menu menu){
		MenuItem mnu1 = menu.add(0, 0, 0, "Edit Page");
		{
			mnu1.setIcon(R.drawable.ic_edit_page);
			mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);  // This is subject to change.
		}		
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
	
	void setStoryTitle(String storyTitle, float textSize) {
		storyTitle = "Story: " + storyTitle;
		mStoryTitleTextView.setSingleLine(true);
		mStoryTitleTextView.setText(storyTitle);
		mStoryTitleTextView.setTextSize(textSize);
	}

	void setPageTitle(String pageTitle, float textSize) {
		pageTitle = "Page: " + pageTitle;
		mPageTitleTextView.setSingleLine(true);
		mPageTitleTextView.setText(pageTitle);
		mPageTitleTextView.setTextSize(textSize);
	}

	void setStoryText( float textSize){
		mStoryTextView.setMovementMethod(LinkMovementMethod.getInstance());						
		mStoryTextView.setText(getSpannableStringBuilder(), BufferType.SPANNABLE);
		mStoryTextView.setTextSize(textSize);
	}
	
	public SpannableStringBuilder getSpannableStringBuilder() {
		ArrayList<MultimediaAbstract> ma = mPage.getMultimedia();

		SpannableStringBuilder stringBuilder = new SpannableStringBuilder(mPage.getText());

		// Return to caller if ma is null to avoid trivial errors.
		if( ma == null ){
			return stringBuilder;
		}
		
		for (final MultimediaAbstract multimedia : ma) {
			// Load the multimedia Picture representation.
			Bitmap multimediaBitmap = multimedia.loadPhoto(this);
			ImageSpan multimediaImageSpan = new ImageSpan(this, multimediaBitmap, 20);
			
			
			stringBuilder.insert(multimedia.getIndex(), " ");		// Allocate space for multimediaImageSpan.
			
			stringBuilder.setSpan(multimediaImageSpan, multimedia.getIndex(),
					multimedia.getIndex() + 1,
					Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

			ClickableMultimediaSpan multimediaClickableSpan = 
													new ClickableMultimediaSpan();
			multimediaClickableSpan.setOnClick(new Callback(){
				@Override
				public void callback(){
					multimedia.play(getBaseContext());
				}
			});

			stringBuilder.setSpan(multimediaClickableSpan, multimedia.getIndex(),
					multimedia.getIndex() + 1,
					Spannable.SPAN_INCLUSIVE_EXCLUSIVE);			
		}
		return stringBuilder;
	}
	
	@Override
	// Video Preview 
	public void playVideo(String directory){
		super.playVideo(directory);
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
