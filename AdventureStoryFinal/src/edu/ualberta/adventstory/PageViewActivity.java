package edu.ualberta.adventstory;

import java.util.ArrayList;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView.BufferType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.widget.TextView;

import edu.ualberta.adventstory.R;
import edu.ualberta.controller.MultimediaControllerManager;
import edu.ualberta.controller.CommandCollection.Callback;
import edu.ualberta.controller.CommandCollection.Command;
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
	private LinearLayout mOuterLayout;				// Outer LinearLayout.
	private LinearLayout mButtonLayout;
	private Story mStory;					// Story being viewed.
	private Page mPage;						// Current page.
	
	private boolean mViewPageOnly = true; 	// True if we are viewing a page independent of story.	
	
	private static final int START_EDITPAGE_RESULTCODE = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_pageview);
		
		mPage = mDataSingleton.getCurrentPage();
		mStory = mDataSingleton.getCurrentStory();
		if( mStory == null ){ mViewPageOnly = true; }
		
		mOuterLayout = (LinearLayout)findViewById(R.id.outerLayout);
		mButtonLayout = (LinearLayout)findViewById(R.id.buttonLayout);
		mStoryTitleTextView = (TextView)findViewById(R.id.storyTitle);
		if( mViewPageOnly == false ){setStoryTitle(mStory.getTitle(), 26);}		
		mPageTitleTextView = (TextView)findViewById(R.id.pageTitle);		
		mStoryTextView = (TextView)findViewById(R.id.pageText);				
		
		setPageTitle(mPage.getTitle(), 22);		
		setStoryText(18);
		
		AddButtons();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		mDataSingleton.setCurrentActivity(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
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
		Command command = mMapMenuToCommand.get(item);
		if(command != null){
			command.execute();
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
		Command startEditPageCommand = new Command(new Callback(){
			@Override
			public void callback(){
				startPageEditActivity();
			}
		});
		mMapMenuToCommand.put(mnu1, startEditPageCommand);
	}
	
	/**
	 * <code>AddButtons()</code> add the buttons that allows the user/author to transition
	 * to next page.
	 */
	private void AddButtons(){
		for(final Page p : mPage.getPages()){
			TextView tv = (TextView)View.inflate(this, R.layout.next_page_textview, null);
			tv.setText(p.getTitle());
			tv.setOnClickListener(new OnClickListener(){
				@SuppressLint("NewApi")
				@Override
				public void onClick(View view){
					// Go to the next page.
					mDataSingleton.setCurrentPage(p);					
					mDataSingleton.getCurrentActivity().recreate();
				}
			});
			View v = (View)View.inflate(this, R.layout.divider, null);
			mButtonLayout.addView(v);
			mButtonLayout.addView(tv);			
			}
		View v = (View)View.inflate(this, R.layout.divider, null);
		mButtonLayout.addView(v);
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
			Bitmap multimediaBitmap = MultimediaControllerManager.loadBitmap(this, multimedia);
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
					MultimediaControllerManager.play(getBaseContext(), multimedia);
				}
			});

			stringBuilder.setSpan(multimediaClickableSpan, multimedia.getIndex(),
					multimedia.getIndex() + 1,
					Spannable.SPAN_INCLUSIVE_EXCLUSIVE);			
		}
		return stringBuilder;
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
	
	/**
	 * starts PageEditActivity().
	 */
	private void startPageEditActivity(){
		startActivityForResult(new Intent(this, PageEditActivity.class), START_EDITPAGE_RESULTCODE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		// TODO: HASH THESE STUFF INTO COMMAND.
		if( requestCode == 1){			
			this.restart();
		}
	}
}
