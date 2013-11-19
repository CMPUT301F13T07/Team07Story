package edu.ualberta.adventstory;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.BufferType;

import edu.ualberta.adventstory.CommandCollection.Callback;
import edu.ualberta.adventstory.CommandCollection.Command;
import edu.ualberta.extendedViews.ClickableMultimediaSpan;
import edu.ualberta.extendedViews.EditTextEx;
import edu.ualberta.extendedViews.PaddingableImageSpan;
import edu.ualberta.extendedViews.EditTextEx.OnSelectionChangedListener;
import edu.ualberta.multimedia.MultimediaAbstract;
import edu.ualberta.multimedia.TObservable;
import edu.ualberta.utils.Page;
import edu.ualberta.utils.Story;

/**
 * <code>PageEditActivity</code> is a view that is responsible
 * for editing page. It is a subclass of ActivityExtended thus
 * inheriting MVC's <code>Observable</code> Update interface.
 * 
 * @author JoeyAndres
 *
 */
@SuppressLint("NewApi")
public class PageEditActivity extends ActivityExtended {
	private EditText mPageTitleEditTextView; // Page Title EditText.
	private EditText mPageAuthorEditTextView; // Page Title EditText.
	
	private EditTextEx mStoryEditTextView; // StoryText TextView.
	
	// For more info concerning views, see activity_pageedit.xml
	private LinearLayout mInnerLayout; // Inner RelativeLayout.
	private LinearLayout mOuterLayout; // Outer LinearLayout.
	private ScrollView mScrollView; // Encapsulate mOuterLayout.
	private LinearLayout mButtonLayout; // Encapsulate the buttons.
	private Button mButtonAddPage; // Button for adding page.	
	private LinearLayout.LayoutParams mOuterLayoutParam;
	private LinearLayout.LayoutParams mInnerComponentParam;	
	private Story mStory; // Story being viewed.
	private Page mPage; // Current page.
	private Button mButtonAddMultimedia;	// Button for adding multimedias.
	
	private boolean mIsAnyMultimediaSelected = false; 	// Set to true when something is selected.
	private boolean mIsJustSelected = false; 			// Set to true when something is just selected.

	final private int ADDPAGE_REQUESTCODE = 1;			// Request code when adding page.
	final private int GET_MULTIMEDIA_REQUESTCODE = 2;	// Request code for getting multimedia.
	
	// Set to true when editing a page only, independent of the story.
	private boolean mEditPageOnly = false;

	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pageedit);		
			
		mPage = mDataSingleton.getCurrentPage();
		mStory = mDataSingleton.getCurrentStory();
		
		// Determine if the mStory is null if we are just creating a page independent
		// of the Story.
		if( mStory == null){ mEditPageOnly = true; }
		
		// Exit if mPage is null.
		if( mPage == null){
			Toast.makeText(this, "Error occured, mPage or mStory is null.", Toast.LENGTH_LONG).show();
			exit();
		}

		// ScrollView.
		mScrollView = (ScrollView)findViewById(R.id.scrollView);

		// Outer Layout.
		mOuterLayout = (LinearLayout)findViewById(R.id.outerLayout);
		
		if( mEditPageOnly == false ){
			setStoryTitle();
		}

		// Initialize the Page Title EditText and its parameters.
		mPageTitleEditTextView = (EditText)findViewById(R.id.pageTitle);
		setPageTitle(22);

		// Initialize the Page Author EditText.
		mPageAuthorEditTextView = (EditText)findViewById(R.id.pageAuthor);		
		setPageAuthor(20);

		mInnerLayout = (LinearLayout)findViewById(R.id.innerLayout);

		// EditTextView.
		mStoryEditTextView = new EditTextEx(this);
		mStoryEditTextView.setOnSelectionChangedListener(new OnSelectionChangedListener(){
			@Override
			public void onSelectionChangedListener(int selStart, int selEnd){
				cursorIndexChange(selStart);
			}
		});
		// Mannually add it since it's costum EditText.
		mInnerComponentParam = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		mStoryEditTextView.setLayoutParams(mInnerComponentParam);
		// TextView. - We start off with these.
		mInnerLayout.addView(mStoryEditTextView);
		setStoryText(18);

		mButtonAddMultimedia = (Button)findViewById(R.id.addMultimedia);
		mButtonAddMultimedia.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				addMultimedia();
			}			
		});		
		
		// Add an 'Add Page' Button to the outerlayout.
		mButtonAddPage = (Button)findViewById(R.id.addPage);
		mButtonAddPage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v == mButtonAddPage) {
					addPage();
				}
			}
		});

		// Initialize the mButtonLayout.
		mButtonLayout = (LinearLayout)findViewById(R.id.buttonLayout);				
		// Add the buttons.
		addNextPageButtons();
	}

	/*
	 * onResume is where we placed some duplicates of getCurrentPage and getCurrentStory
	 * since this in situations when we call an Activity to expect a return, it will go back here,
	 * and not in onCreate anymore.
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();		
		/*
		 * These are placed here to acquire data when 
		 * screen orientation changed since onCreate is 
		 * not called in such situation.
		 */
		mPage = mDataSingleton.getCurrentPage();
		mStory = mDataSingleton.getCurrentStory();
		setStoryText(18);
		
		mDataSingleton.setCurrentActivity(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.page_edit, menu);
		CreateMenu(menu);
		return true;
	}

	/*
	 * onPause is where the save() instead of onDestroy
	 * since onPause is where the views are still in place 
	 * and thus we can still extract data's from them.
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	public void onPause(){
		save();	
		super.onPause();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Command command = mMapMenuToCommand.get(item);
		if (command != null) {
			command.execute();
		}
		return true;
	}

	/**
	 * <code>CreateMenu</code> here is responsible for creating
	 * Actionbar items and mapping these MenuItems to their 
	 * corresponding Command and Callback method via Hash table.
	 * 
	 * @see PageEditActivity.mMapMenuToCommand 
	 *  
	 * @param menu
	 */
	@SuppressLint("NewApi")
	void CreateMenu(Menu menu) {
		MenuItem mnu1 = menu.add(0, 0, 0, "Add Multimedia");
		{
			mnu1.setIcon(R.drawable.ic_addmultimedia);
			mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}
		
		Command addMultimediaResponder = new CommandCollection.AddMultimediaCommand(new Callback(){
			@Override
			public void callback() {
				addMultimedia();
			}
		});
		mMapMenuToCommand.put(mnu1, addMultimediaResponder);

		MenuItem mnu2 = menu.add(0, 1, 1, "Save");
		{
			mnu1.setIcon(R.drawable.ic_addmultimedia);
			mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}

		Command saveResponder = new CommandCollection.SaveCommand(new Callback() {
			@Override
			public void callback() {
				save();
				exit();
			}
		});		
		mMapMenuToCommand.put(mnu2, saveResponder);

		MenuItem mnu3 = menu.add(0, 2, 2, "Cancel");
		{
			mnu1.setIcon(R.drawable.ic_addmultimedia);
			mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}

		Command cancelResponder = new CommandCollection.ExitActivityCommand(this, new Callback() {
			@Override
			public void callback() {
				cancel();
			}
		});
		mMapMenuToCommand.put(mnu3, cancelResponder);
	}
	
	/**
	 * <code>addNextPageButtons</code> is used for adding listView elements for next
	 * page.
	 */
	void addNextPageButtons() {
		for (final Page p : mPage.getPages()) {
			Button btn = new Button(this);
			btn.setText(p.getTitle());
			btn.setLayoutParams(mInnerComponentParam);
			btn.setOnClickListener(new OnClickListener() {
				@SuppressLint("NewApi")
				@Override
				public void onClick(View view) {
					// This doesn't apply here since we are just editing
					// the page.
				}
			});
			mButtonLayout.addView(btn);
		}
	}
	
	/**
	 * <code>setStoryTitle</code> set's the <code>TextView</code> for Story Title.
	 * Unlike Other Views here, this is the only <code>TextView</code> as we don't allow
	 * modification of Story Title while editing page.
	 * 
	 * @param textSize is the size of the text.
	 */
	void setStoryTitle() {		
		//ActionBar actionBar = getActionBar();
		//actionBar.setTitle(mStory.getTitle());
		
	}
	
	/**
	 * <code>setPageTitle</code> set's the <code>EditText</code> for page title.
	 * This is done via getTitle method in <code>Page</code>
	 * @param textSize is the size of the text.
	 */
	void setPageTitle(float textSize) {
		mPageTitleEditTextView.setSingleLine(true);
		mPageTitleEditTextView.setText(mPage.getTitle());
		mPageTitleEditTextView.setTextSize(textSize);
	}
	
	/**
	 * <code>setPageAuthor</code> set's the <code>TextView</code> for page author.
	 * This is done by getting the author's name via mPage's getAuthor
	 * method.
	 * 
	 * @param textSize is the size of the text.
	 */
	void setPageAuthor(float textSize) {
		mPageAuthorEditTextView.setSingleLine(true);
		mPageAuthorEditTextView.setText(mPage.getAuthor());
		mPageAuthorEditTextView.setTextSize(textSize);
	}
	
	/**
	 * <code>setStoryText</code> sets the current Page content or the "story text".
	 * Of course it delegates the compilation of <code> SpannableStringBuilder </code>
	 * to <code>getSpannableStringBuilder</code>.
	 * 
	 * @param textSize is the size of the text.
	 */
	void setStoryText(float textSize) {
		mStoryEditTextView.setMovementMethod(LinkMovementMethod.getInstance());
		mStoryEditTextView.setText(getSpannableStringBuilder(),
				BufferType.SPANNABLE);
		mStoryEditTextView.setTextSize(textSize);
		mInnerLayout.removeAllViews();	/*ImageSpans inside*/
		mInnerLayout.addView(mStoryEditTextView);
	}
	
	/**
	 * <code>getSpannableStringBuilder</code> is responsible for 
	 * merging text and multimedia icons ( as proxy ) to be display
	 * in EditText. This method is likely the heart of this module.
	 * This is where the callback method are defined for when the
	 * <code>MultimediaAbstract</code> images are clicked.
	 * 
	 * @return SpannableStringBuilder allows this program
	 * to display images via <code>EditText</code> or <code>TextView</code>.
	 * 
	 * @author Joey Andres
	 */
	public SpannableStringBuilder getSpannableStringBuilder() {
		ArrayList<MultimediaAbstract> ma = mPage.getMultimedia();
		
		// Add an observer to these models/observable.
		for( MultimediaAbstract m : ma )
			m.addObserver(this);
			
		SpannableStringBuilder stringBuilder = new SpannableStringBuilder(
				mPage.getText());

		// Return to caller if ma is null to avoid trivial errors.
		if (ma == null) {
			return stringBuilder;
		}
		
		for (final MultimediaAbstract multimedia : ma) {
			// Load the multimedia Picture representation.
			Bitmap multimediaBitmap = multimedia.loadPhoto(this);
			final PaddingableImageSpan multimediaImageSpan = new PaddingableImageSpan(
					this, multimediaBitmap, 20);
			
			if (multimedia.getIsSelected()){
				multimediaImageSpan.enablePadding();
				// Display delete image.
			}
			
			int index = multimedia.getIndex();
			
			if(index >= mPage.getText().length()){
				index = mPage.getText().length()-1;
			}
			
			stringBuilder.insert(index, " ");		// Allocate space for multimediaImageSpan.
			
			stringBuilder.setSpan(multimediaImageSpan, index,
					index + 1,
					Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

			ClickableMultimediaSpan multimediaClickableSpan = 
											new ClickableMultimediaSpan();
			multimediaClickableSpan.setOnClick(new Callback(){
				@Override 
				public void callback(){
					mIsAnyMultimediaSelected = true;
					mIsJustSelected = true;
					multimediaImageSpan.enablePadding();
					
					clearSelection();					
					// Set the multimedia we want to enable to true.
					multimedia.setIsSelected(true);					
					// Display MultimediaFragmentOptions.
					showMultimediaOptionsFragment();
				}
			});

			stringBuilder.setSpan(multimediaClickableSpan,
					index, index + 1,
					Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
		}
		return stringBuilder;
	}
	
	/**
	 * @deprecated This method is only here in case it will be
	 * used later in development. This is not recommended to be used.
	 */
	@Override
	public void playVideo(String directory) {
		super.playVideo(directory);
	}
	
	/**
	 * Is an event handler when back key is pressed.
	 */
	@SuppressWarnings("deprecation")
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (Integer.parseInt(android.os.Build.VERSION.SDK) < 5
				&& keyCode == KeyEvent.KEYCODE_BACK
				&& event.getRepeatCount() == 0) {
			onBackPressed();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/*
	 * This event handler is overriden so backpress will always go back
	 * to last activity.
	 * 
	 * @see android.app.Activity#onBackPressed()
	 */
	// TODO: Make sure to link to last activity when no oldPage.
	@SuppressLint("NewApi")
	@Override
	public void onBackPressed() {
		if (mDataSingleton.getOldPage() == null) {
			super.onBackPressed();
		} else {
			mDataSingleton.revertPage();
			this.recreate();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see edu.ualberta.adventstory.ActivityExtended#update(edu.ualberta.multimedia.TObservable)
	 */
	@Override
	public void update(TObservable o) {
		super.update(o);
		localUpdate();
	}
	
	/**
	 * See parent class ActivityExtended.
	 */
	@Override
	public void localUpdate(){
		// Things to update.
		setStoryText(18);
	}
	
	/**
	 * <code>clearSelection</code> clear all trace of selection.
	 * This is required as opposed to doing it mannually.
	 */
	void clearSelection() {
		ArrayList<MultimediaAbstract> ma = mPage.getMultimedia();
		for (MultimediaAbstract m : ma) {
			m.setIsSelected(false);
		}

		mIsAnyMultimediaSelected = false;
	}

	/**
	 * <code>cursorIndexChange</code> is usually placed inside
	 * <code>EditTextEx</code> callback method.
	 * 
	 * @author JoeyAndres
	 * @param index
	 * @version 1.0
	 */
	void cursorIndexChange(int index) {
		// If a multimedia is selected, move it to where the user clicked
		// and update.
		int maxIndex = mPage.getText().length();
		if (index > maxIndex) {
			index = maxIndex;
		}

		if (mIsAnyMultimediaSelected && (mIsJustSelected == false)
				&& index >= 0) {
			ArrayList<MultimediaAbstract> ma = mPage.getMultimedia();

			for (MultimediaAbstract m : ma) {
				if (m.getIsSelected()) {
					// Make sure no multimedia have that index.
					for (MultimediaAbstract m2 : ma) {
						if (m2 != m) {
							if (m.getIndex() == index) {
								Toast.makeText(
										this,
										"Index is occupied, place multimedia somewhere else.",
										Toast.LENGTH_LONG).show();
								return;
							}
						}
					}
					m.setIndex(index);

					clearSelection();
					localUpdate();
					// It is assumed that only one MultimediaAbstract is
					// selected.
					// Exit as soon as one is found.
					return;
				}
			}
			// index >= 0 is a really important condition.
			// Make sure that although mIsJustSelected is true,
			// this must NOT be set to false when the user selected
			// an illegal destionation (index = -1)..
		} else if (mIsJustSelected == true && index >= 0) {
			mIsJustSelected = false;
		} else {
			// Unknown state.
		}
	}

	/**
	 * <code>addMultimedia</code> is called when adding Multimedia
	 * in PageEditActivity.
	 * 
	 * @author JoeyAndres
	 * @version 1.0
	 */
	void addMultimedia() {
		save();
		// Load arguments.
		Bundle info = new Bundle();
		info.putInt("page_id", mPage.getID());
		
		int index = mStoryEditTextView.getSelectionStart() == -1 ? 
					mPage.getText().length()-1 : mStoryEditTextView.getSelectionStart();
		
		// Check if index is within 1 indexes within others.
		for( MultimediaAbstract m : mPage.getMultimedia()){
			if( Math.abs(m.getIndex()-index)<= 1 ){
				Toast.makeText(this, "Select a different location.", Toast.LENGTH_LONG).show();
				return;
			}
		}
		
		info.putInt("index", index);
		Intent addMultimediaIntent = new Intent(this, AddMultimediaActivity.class);
		addMultimediaIntent.putExtras(info);
		startActivity(addMultimediaIntent);
	}

	/**
	 * @return <code>String</code> of the title of the page, stripped-off of unnecessary
	 * white space. 
	 */
	private String getStoryText(){
		String pageStory = this.mStoryEditTextView.getText().toString();
		
		String f[] = pageStory.split(" +");
		ArrayList<String> fragmented = new ArrayList<String>();
		for( int i = 0; i < f.length; i++){
			fragmented.add(f[i]);
			fragmented.add(" ");	
		}
		
		StringBuilder sb = new StringBuilder();
		
		for( String s : fragmented){
			sb.append(s);
		}
		return sb.toString();
	}
	
	/**
	 * @return <code>String</code> of the author of the page. 
	 */
	private String getAuthorText(){
		return mPageAuthorEditTextView.getText().toString();
	}
	
	/**
	 * @return <code>String</code> of the title of the page. 
	 */
	protected String getPageTitleText(){
		return mPageTitleEditTextView.getText().toString();
	}
	
	/**
	 * <code>save</code> must be called when saving. <code>onPause</code> 
	 * is the recommended event to when/where this should be called.
	 */
	void save() {
		String pageName = getPageTitleText();
		String pageAuthor =	getAuthorText(); 
		String pageStory = getStoryText();
		
		mPage.setTitle(pageName);
		mPage.setAuthor(pageAuthor);
		mPage.setText(pageStory);
		
		if (pageName.length() <= 0 || pageAuthor.length() <= 0
				|| pageStory.length() <= 0) {
			// Things to do if one of the inputs are empty.
			// - Just have all of them have some default values.
			Toast.makeText(this, "One of the text inputs are invalid.", 
												Toast.LENGTH_LONG).show();
			mDataSingleton.database.update_page(new Page(mPage.getID(), 
								"Untitled", "Unknown", "Unfilled", null));
		}

		mDataSingleton.database.update_page(mPage);
		/*
		 * Multimedia's are only updated as opposed to modified.
		 */
		for( MultimediaAbstract m : mPage.getMultimedia()){
			mDataSingleton.database.update_multimedia(m, mPage.getID());
		}
	}
	
	/**
	 * <code>cancel</code>
	 * For now cancel is no use.
	 * 
	 * TODO: Discuss with team what to do when cancelling.
	 * 		- Do I delete the current page?
	 * 		- If there's only one page, Do I also delete the current story?
	 * 		- Or do I just save whatever is typed?
	 * 
	 * @deprecated
	 */
	private void cancel() {
		exit();
	}
	
	/**
	 * <code>addPage</code> is the method to call when adding page.
	 * Usually attached with a button associated for adding page.
	 * @author Joey Andres
	 */
	void addPage() {
		// Open the activity for this.
		Intent searchActivity = new Intent(this, SearchActivity.class);
		Bundle info = new Bundle();
		info.putBoolean("ADD_PAGE", true);
		info.putBoolean("BOOL_IS_STORY", false);		
		searchActivity.putExtra("android.intent.extra.INTENT",info);
		startActivityForResult(searchActivity, ADDPAGE_REQUESTCODE);
	}
	
	/**
	 * <code>exit</code> provides a way of exiting to StartActivity.
	 */
	@Override
	protected void exit(){
		save();
		super.exit();
	}
	
	/**
	 * <code>showMultimediaOptionsFragment</code> launches MultimediaOptionsFragment
	 * to display tools/options to be apply to a Multimedia object.
	 */
	private void showMultimediaOptionsFragment(){
		MultimediaOptionsFragment mof = MultimediaOptionsFragment.MultimediaOptionsFragmentFactory(mPage);
		
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();		
		fragmentTransaction.replace(android.R.id.content, mof);
		fragmentTransaction.commit();
	}
	
	// TODO: Accomodate AddMultimedia's return here.
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		mPage = mDataSingleton.getCurrentPage();
		
		Page newPage = mDataSingleton.database.get_page_by_id(resultCode);
		mDataSingleton.database.insert_page_option(mPage, newPage);
		mPage.addPage(newPage);
		
		restart();
	}
}
