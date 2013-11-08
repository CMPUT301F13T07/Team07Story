package edu.ualberta.adventstory;

import java.util.ArrayList;
import java.util.HashMap;

import edu.ualberta.multimedia.MultimediaAbstract;
import edu.ualberta.utils.Page;
import edu.ualberta.utils.Story;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.BufferType;

public class PageEditActivity extends ActivityExtended {
	// Not the base activity. Transfer this to the base activity then.
	protected DataSingleton mDataSingleton;

	private TextView mStoryTitleTextView; // Story Title TextView
	private EditText mPageTitleEditTextView; // Page Title EditText.
	private EditText mPageAuthorEditTextView; // Page Title EditText.

	// EditText is needed to be extended for mStoryEditTextView
	// to modify/extend it's onSelectionChanged method.
	class EditTextEx extends EditText {
		public EditTextEx(Context context) {
			super(context);
		}

		@Override
		protected void onSelectionChanged(int selStart, int selEnd) {
			cursorIndexChange(selStart);
			super.onSelectionChanged(selStart, selEnd);
		}
	}

	private EditTextEx mStoryEditTextView; // StoryText TextView.
	
	private LinearLayout mInnerLayout; // Inner RelativeLayout.
	private LinearLayout mOuterLayout; // Outer LinearLayout.
	private ScrollView mScrollView; // Encapsulate mOuterLayout.
	private LinearLayout mButtonLayout; // Encapsulate the buttons.

	private Button mButtonAddPage; // Button for adding page.
	
	private LinearLayout.LayoutParams mOuterLayoutParam;
	private LinearLayout.LayoutParams mInnerComponentParam;	

	private Story mStory; // Story being viewed.
	private Page mPage; // Current page.

	// Set to true when something is selected.
	private boolean mIsAnyMultimediaSelected = false;
	// Set to true when something is just selected.
	private boolean mIsJustSelected = false;

	private int request_code = 1;	// Request code when adding page.
	
	static public class Responder {
		static public class Action {
			public void act() {
			}; // Override.
		}
		protected Action mAction;
		public Responder() {mAction = new Action();}
		public void response() {mAction.act();}
		public void setResponse(Action action) {mAction = action;}
	}

	// Map a menuitem to a resonder.
	private HashMap<MenuItem, Responder> mMenuMap = new HashMap<MenuItem, Responder>();
	
	// Set to true when editing a page only, independent of the story.
	private boolean mEditPageOnly = false;

	private Button mButtonAddMultimedia;
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pageedit);
		
		mDataSingleton = (DataSingleton)getApplicationContext();
		
		Intent intent = getIntent();	// Get intent that started this Activity.
		if( intent == null){
			// Unknown state.
			try {
				this.finalize();
			} catch (Throwable e) {
				e.printStackTrace();
			}	// End before errors occur.
		}
			
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

		// Initialize the Story EditText and its parameters.
		mStoryTitleTextView = (TextView)findViewById(R.id.storyTitle);
		if( mEditPageOnly == false ){
			setStoryTitle(mStory.getTitle(), 26);
		}

		// Initialize the Page Title EditText and its parameters.
		mPageTitleEditTextView = (EditText)findViewById(R.id.pageTitle);
		setPageTitle(mPage.getTitle(), 22);

		// Initialize the Page Author EditText.
		mPageAuthorEditTextView = (EditText)findViewById(R.id.pageAuthor);
		if( mEditPageOnly == false ){
			setPageAuthor(mPage.getAuthor(), 20);
		}

		mInnerLayout = (LinearLayout)findViewById(R.id.innerLayout);

		// Add Inner Layout components.
		// EditTextView.
		
		mStoryEditTextView = new EditTextEx(this);
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
		AddButtons();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		mPage = mDataSingleton.getCurrentPage();
		mStory = mDataSingleton.getCurrentStory();
		
		setStoryText(18);
		
		mDataSingleton.setCurrentActivity(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.page_view, menu);
		CreateMenu(menu);
		return true;
	}

	public void onPause(){
		save();
		super.onPause();
	}
	
	@Override
	public void onDestroy() {		
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Responder r = mMenuMap.get(item);
		if (r != null) {
			r.response();
		}
		return true;
	}

	@SuppressLint("NewApi")
	void CreateMenu(Menu menu) {
		MenuItem mnu1 = menu.add(0, 0, 0, "Add Multimedia");
		{
			mnu1.setIcon(R.drawable.ic_addmultimedia);
			mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}
		Responder addMultimediaResponder = new Responder();
		addMultimediaResponder.setResponse(new Responder.Action() {
			@Override
			public void act() {
				addMultimedia();
			}
		});

		mMenuMap.put(mnu1, addMultimediaResponder);

		MenuItem mnu2 = menu.add(0, 1, 1, "Save");
		{
			mnu1.setIcon(R.drawable.ic_addmultimedia);
			mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}

		Responder saveResponder = new Responder();
		saveResponder.setResponse(new Responder.Action() {
			@Override
			public void act() {
				save();
				exit();
			}
		});
		mMenuMap.put(mnu2, saveResponder);

		MenuItem mnu3 = menu.add(0, 2, 2, "Cancel");
		{
			mnu1.setIcon(R.drawable.ic_addmultimedia);
			mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}

		Responder cancelResponder = new Responder();
		cancelResponder.setResponse(new Responder.Action() {
			@Override
			public void act() {
				cancel();
			}
		});
		mMenuMap.put(mnu3, cancelResponder);
	}

	void AddButtons() {
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

	void setPage(Page page) {
		mPage = page;
	}

	void setStoryTitle(String storyTitle, float textSize) {
		mStoryTitleTextView.setSingleLine(true);
		mStoryTitleTextView.setText(storyTitle);
		mStoryTitleTextView.setTextSize(textSize);
	}

	void setPageTitle(String pageTitle, float textSize) {
		mPageTitleEditTextView.setSingleLine(true);
		mPageTitleEditTextView.setText(pageTitle);
		mPageTitleEditTextView.setTextSize(textSize);
	}

	void setPageAuthor(String pageAuthor, float textSize) {
		mPageAuthorEditTextView.setSingleLine(true);
		mPageAuthorEditTextView.setText(pageAuthor);
		mPageAuthorEditTextView.setTextSize(textSize);
	}

	// TODO: remove content argument.
	void setStoryText(float textSize) {
		mStoryEditTextView.setMovementMethod(LinkMovementMethod.getInstance());
		mStoryEditTextView.setText(getSpannableStringBuilder(),
				BufferType.SPANNABLE);
		mStoryEditTextView.setTextSize(textSize);
		mInnerLayout.removeAllViews();
		mInnerLayout.addView(mStoryEditTextView);

	}

	// TODO: Just rely on Page data type full blown next time.
	// ClickableSpan for multimedia.
	class ClickableMultimediaSpanEx extends ClickableSpan {
		private MultimediaAbstract mMultimedia;
		private PaddingableImageSpan mPaddingableImageSpan;

		public ClickableMultimediaSpanEx(MultimediaAbstract ma,
				PaddingableImageSpan paddinableImageSpan) {
			super();
			mMultimedia = ma;
			mPaddingableImageSpan = paddinableImageSpan;
		}

		@SuppressLint("NewApi")
		@Override
		public void onClick(final View widget) {
			mIsAnyMultimediaSelected = true;
			mIsJustSelected = true;
			mPaddingableImageSpan.enablePadding();

			// Set all multimedia mIsSelected to false.
			ArrayList<MultimediaAbstract> ma = mPage.getMultimedia();
			for (MultimediaAbstract m : ma)
				m.setIsSelected(false);
			// Set the multimedia we want to enable to true.
			mMultimedia.setIsSelected(true);

			Update();
		}
	}

	// ClickableSpan for deleting.
	class ClickableDeleteSpanEx extends ClickableSpan {
		private MultimediaAbstract mMultimedia;

		public ClickableDeleteSpanEx(MultimediaAbstract ma) {
			super();
			mMultimedia = ma;
		}

		@Override
		public void onClick(View widget) {
			ArrayList<MultimediaAbstract> ma = mPage.getMultimedia();

			for (MultimediaAbstract m : ma) {
				if (m == mMultimedia) {
					ma.remove(m);
					mDataSingleton.database.delete_mult(m, mPage);
					break;
				}
			}

			Update();
		}

	}

	class PaddingableImageSpan extends ImageSpan {
		/**
		 * The padding that will be added to the images sides
		 */
		private int mExtraPadding = 0;
		private int mHeight = -1;
		private int mWidth = -1;

		boolean mPadding = false;
		Bitmap mBitmap;
		Point mPoint; // Coordinate of drawing.

		public PaddingableImageSpan(Context context, Bitmap bitmap,
				int extraPadding) {
			this(context, bitmap, extraPadding, -1, -1);
		}

		public PaddingableImageSpan(Context context, Bitmap bitmap,
				int extraPadding, int width, int height) {
			super(context, bitmap);
			mBitmap = bitmap;
			
			this.mExtraPadding = extraPadding;
			this.mWidth = width;
			this.mHeight = height;
		}

		@Override
		public Drawable getDrawable() {
			Drawable drawable = super.getDrawable();
			if ((mHeight > 0) || (mWidth > 0)) {
				Rect bounds = drawable.getBounds();
				if (mHeight > 0) {
					bounds.bottom = mHeight;
				}
				if (mWidth > 0) {
					bounds.right = mWidth;
				}
				drawable.setBounds(bounds);
			}
			return drawable;
		}

		@Override
		public int getSize(Paint paint, CharSequence text, int start, int end,
				Paint.FontMetricsInt fm) {
			// adding the padding to the original image size
			int size = super.getSize(paint, text, start, end, fm);
			size += (2 * this.mExtraPadding);
			return size;
		}

		@Override
		public void draw(Canvas canvas, CharSequence text, int start, int end,
				float x, int top, int y, int bottom, Paint paint) {
			// Draw a border.
			if (mPadding) {
				int w = mBitmap.getWidth();
				int h = mBitmap.getHeight();
				paint.setColor(Color.DKGRAY);
				RectF rect = new RectF(x, top, x + w + mExtraPadding + 20,
						bottom + mExtraPadding);
				canvas.drawRect(rect, paint);
			}

			// Draw someting
			// adding the padding to the transformation so it will
			// be padding on both sides
			super.draw(canvas, text, start, end, x + this.mExtraPadding, top,
					y, bottom, paint);
		}

		public int GetSize(Paint paint, CharSequence text, int start, int end,
				Paint.FontMetricsInt fm) {
			return Math.round(MeasureText(paint, text, start, end));
		}

		private float MeasureText(Paint paint, CharSequence text, int start,
				int end) {
			return paint.measureText(text, start, end);
		}

		public void enablePadding() {
			mPadding = true;
		}

		public void disablePadding() {
			mPadding = false;
		}
	}

	// Build Spannable String.
	private String mStoryText;  
	public SpannableStringBuilder getSpannableStringBuilder() {
		ArrayList<MultimediaAbstract> ma = mPage.getMultimedia();
		
		SpannableStringBuilder stringBuilder = new SpannableStringBuilder(
				mPage.getText());

		// Return to caller if ma is null to avoid trivial errors.
		if (ma == null) {
			return stringBuilder;
		}

		// Load Bitmap for delete button.
		Bitmap deleteBitmap = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.ic_delete);
		for (MultimediaAbstract multimedia : ma) {
			// Load the multimedia Picture representation.
			Bitmap multimediaBitmap = multimedia.loadPhoto(this);
			PaddingableImageSpan multimediaImageSpan = new PaddingableImageSpan(
					this, multimediaBitmap, 20);

			PaddingableImageSpan deleteImageSpan = new PaddingableImageSpan(
					this, deleteBitmap, 0);
			
			if (multimedia.getIsSelected())
				multimediaImageSpan.enablePadding();
			
			int index = multimedia.getIndex();
			if(index == -1){ index = stringBuilder.toString().length()-1; }
			stringBuilder.insert(index, " ");		// Allocate space for multimediaImageSpan.
			stringBuilder.insert(index+1, " ");		// Allocate space for deleteImageSpan.									
			
			stringBuilder.setSpan(multimediaImageSpan, index,
					index + 1,
					Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

			stringBuilder.setSpan(deleteImageSpan, index + 1,
					index + 2,
					Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

			ClickableMultimediaSpanEx multimediaClickableSpan = new ClickableMultimediaSpanEx(
					multimedia, multimediaImageSpan);
			ClickableDeleteSpanEx deleteClickableSpan = new ClickableDeleteSpanEx(
					multimedia);

			stringBuilder.setSpan(multimediaClickableSpan,
					index, index + 1,
					Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

			stringBuilder.setSpan(deleteClickableSpan,
					index + 1, index + 2,
					Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

		}
		return stringBuilder;
	}

	// Video Preview
	@Override
	public void switchToVideoViewPreview(String directory) {
		super.switchToVideoViewPreview(directory);
	}

	@Override
	public void switchToOriginalLayout() {
		super.switchToOriginalLayout();
		this.setContentView(mScrollView, mOuterLayoutParam);
	}

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

	@Override
	public void Update() {
		super.Update();
		// Things to update.
		setStoryText(18);
	}

	// Call when want to clear selection.
	void clearSelection() {
		ArrayList<MultimediaAbstract> ma = mPage.getMultimedia();
		for (MultimediaAbstract m : ma) {
			m.setIsSelected(false);
		}

		mIsAnyMultimediaSelected = false;
	}

	// Called when cursor location is changed.
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
					Update();
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

	// Implement when addMultimedia class is implemented.
	void addMultimedia() {
		save();
		// Load arguments.
		// TODO: Request to just have a singleton that will start Activities in the DataSingleton.
		Bundle info = new Bundle();
		info.putInt("page_id", mPage.getID());
		
		int index = mStoryEditTextView.getSelectionStart() == -1 ? 
						0 : mStoryEditTextView.getSelectionStart();
		info.putInt("index", index);
		Intent addMultimediaIntent = new Intent(this, AddMultimediaActivity.class);
		addMultimediaIntent.putExtras(info);
		startActivity(addMultimediaIntent);
	}

	// Get story texts. This is device so I can get rid of unecessary spaces due to ImageSpan in 
	// getSpannableStringBuilder method.
	private String getStoryText(){
		String pageStory = this.mStoryEditTextView.getText().toString();
		
		String f[] = pageStory.split(" +");
		ArrayList<String> fragmented = new ArrayList<String>();
		for( int i = 0; i < f.length; i++){
			fragmented.add(f[i]);
						
			//if( i != (f.length - 1) )
				fragmented.add(" ");	
		}
		
		StringBuilder sb = new StringBuilder();
		
		for( String s : fragmented){
			sb.append(s);
		}
		return sb.toString();
	}
	
	private void save() {
		String pageName = this.mPageTitleEditTextView.getText().toString();
		String pageAuthor = this.mPageAuthorEditTextView.getText().toString();
		String pageStory = getStoryText();
		
		mPage.setTitle(pageName);
		mPage.setAuthor(pageAuthor);
		mPage.setText(pageStory);
		
		if (pageName.length() <= 0 || pageAuthor.length() <= 0
				|| pageStory.length() <= 0) {
			// Things to do if one of the inputs are empty.
			
			Toast.makeText(this, "One of the text inputs are invalid.", 
												Toast.LENGTH_LONG).show();
			return;
		}

		mDataSingleton.database.update_page(mPage);
		/*
		 * Multimedia's are only updated as opposed to modified.
		 */
		for( MultimediaAbstract m : mPage.getMultimedia()){
			mDataSingleton.database.update_multimedia(m, mPage.getID());
		}
	}

	private void cancel() {
		if( mEditPageOnly == true ){
			// Don't save anything.
			try {
				this.finalize();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		exit();
	}

	void addPage() {
		// Open the activity for this.
		Intent searchActivity = new Intent(this, SearchActivity.class);
		Bundle info = new Bundle();
		info.putBoolean("ADD_PAGE", true);
		info.putBoolean("BOOL_IS_STORY", false);		
		searchActivity.putExtra("android.intent.extra.INTENT",info);
		startActivityForResult(searchActivity, request_code);
	}
	
	void exit(){
		save();
		Intent startActivityIntent = new Intent(this, StartActivity.class);
		startActivity(startActivityIntent);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		mPage = mDataSingleton.getCurrentPage();
		
		Page newPage = mDataSingleton.database.get_page_by_id(resultCode);
		mDataSingleton.database.insert_page_option(mPage, newPage);
		mPage.addPage(newPage);
		
		// Restart Activity.
		Intent i = getIntent();
		finish();
		startActivity(i);
	}
}
