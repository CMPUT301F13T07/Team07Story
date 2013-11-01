package edu.ualberta.adventstory;

import java.util.ArrayList;
import java.util.HashMap;

import edu.ualberta.multimedia.MultimediaAbstract;
import edu.ualberta.utils.Content;
import edu.ualberta.utils.Page;
import edu.ualberta.utils.Responder;
import edu.ualberta.utils.Responder.Action;
import edu.ualberta.utils.Story;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
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
asdf
public class PageEditActivity extends ActivityExtended {
	// Not the base activity. Transfer this to the base activity then.
	protected ChooseYourAdventure mAdventureTime;

	private TextView mStoryTitleEditTextView; // Story Title TextView
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

	/*
	 * The structure of this Activity is the following: -An outer layout
	 * contains the stationary components such as title and buttons for next
	 * Page(s). A ScrollView and LinearLayout is used to achieve this. The
	 * existence of ScrollView will be discussed next.
	 * 
	 * -Inner Layout on the other hand contains non-stationary objects. This
	 * allows room for movable multimedia objects. Although the text segment of
	 * the story is stationary it is placed in the inner layout since it does
	 * expand vertically. Since multimedia(s) are movable and text segment
	 * expands, we used a ScrollView to make sure that when the inner layout
	 * does expands beyond the limit of the screensize the user will still have
	 * pleasant experience.
	 */
	// Layout.
	private RelativeLayout mInnerLayout; // Inner RelativeLayout.
	private LinearLayout mOuterLayout; // Outer LinearLayout.
	private ScrollView mScrollView; // Encapsulate mOuterLayout.
	private LinearLayout mButtonLayout;	// Encapsulate the buttons.
	
	private Button mButtonAddPage;		// Button for adding page.
	/*
	 * Params: - mInnerLayoutParam: For the mInnerLayout. - mOuterLayoutParam:
	 * For the mOuterLayout. - mInnerComponentParam: Param for the views inside
	 * mInnerLayout. - mOuterLayoutParam: Param for the views in the
	 * mOuterLayout excluding mInnerLayout.
	 */
	private RelativeLayout.LayoutParams mInnerLayoutParam;
	private LinearLayout.LayoutParams mOuterLayoutParam;
	private RelativeLayout.LayoutParams mInnerComponentParam;
	private LinearLayout.LayoutParams mOuterComponentParam;
	
	private Story mStory; // Story being viewed.
	private Page mPage; // Current page.

	// Set to true when something is selected.
	private boolean mIsAnyMultimediaSelected = false;	
	// Set to true when something is just selected.
	private boolean mIsJustSelected = false; 	
	
	// Map a menuitem to a resonder.
	private HashMap<MenuItem, Responder> mMenuMap = new HashMap<MenuItem, Responder>();
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mAdventureTime = (ChooseYourAdventure) this.getApplicationContext();

		// **MOCK DATA**
		mPage = mAdventureTime.getCurrentPage();
		mStory = mAdventureTime.getCurrentStory();
		// **END OF MOCK DATA**.

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

		// Initialize the Story EditText and its parameters.
		mStoryTitleEditTextView = new EditText(this);
		mStoryTitleEditTextView.setLayoutParams(mOuterComponentParam);
		mOuterLayout.addView(mStoryTitleEditTextView);
		setStoryTitle(mStory.getTitle(), 26);

		// Initialize the Page Title EditText and its parameters.
		mPageTitleEditTextView = new EditText(this);
		mPageTitleEditTextView.setLayoutParams(mOuterComponentParam);
		mOuterLayout.addView(mPageTitleEditTextView);
		setPageTitle(mPage.getTitle(), 22);
		
		// Initialize the Page Author EditText.
		mPageAuthorEditTextView = new EditText(this);
		mPageAuthorEditTextView.setLayoutParams(mOuterComponentParam);
		mOuterLayout.addView(mPageAuthorEditTextView);
		setPageAuthor(mPage.getAuthor(), 20);

		// The Inner Layout.
		mInnerLayout = new RelativeLayout(this);

		// Add Inner Layout components.
		// EditTextView.
		mStoryEditTextView = new EditTextEx(this);
		mStoryEditTextView.setLayoutParams(mInnerComponentParam);
		// TextView. - We start off with these.
		mInnerLayout.addView(mStoryEditTextView);
		setStoryText(mPage.getFormattedContent(), 18);
		
		// Place the inner layout inside the outer layout.
		mOuterLayout.addView(mInnerLayout, mInnerLayoutParam);
		
		// Add an 'Add Page' Button to the outerlayout.
		mButtonAddPage = new Button(this);
		mButtonAddPage.setText("Add Page");
		// Most significant byte is alpha.
		// TODO: Place all preference in one place.
		mButtonAddPage.setBackgroundColor(0xFFD74B4B);
		mButtonAddPage.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(v == mButtonAddPage){
					addPage();
				}
			}			
		});
		
		mOuterLayout.addView(mButtonAddPage, mInnerLayoutParam);

		// Initialize the mButtonLayout.
		mButtonLayout = new LinearLayout(this);
		mButtonLayout.setOrientation(LinearLayout.VERTICAL);
		mOuterLayout.addView(mButtonLayout, mOuterLayoutParam);		
		// Add the buttons.
		AddButtons();

		// Determine what was the last state
		// If the last state was watching video, then make sure
		// this Activity opens with VideoViewPreview.
		if (mOnVideoViewPreview) {
			this.switchToVideoViewPreview(mVideoDirectory);
		} else {
			this.setContentView(mScrollView, mOuterLayoutParam);
		}	
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
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
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Responder r = mMenuMap.get(item);
		if(r != null){
			r.response();
		}
		return true;
	}
	
	// TODO: Ask teammates if if add multimedia button does the job.
	// 		 Have a better handling of textSize.
	@SuppressLint("NewApi")
	void CreateMenu(Menu menu) {		
		MenuItem mnu1 = menu.add(0, 0, 0, "Add Multimedia");
		{
			mnu1.setIcon(R.drawable.ic_addmultimedia);
			mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM); 
		}
		Responder addMultimediaResponder = new Responder();
		addMultimediaResponder.setResponse(new Action(){
			@Override
			public void act(){
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
		saveResponder.setResponse(new Action(){
			@Override
			public void act(){
				save();
			}
		});		
		mMenuMap.put(mnu2, saveResponder);
		
		MenuItem mnu3 = menu.add(0, 2, 2, "Cancel");
		{
			mnu1.setIcon(R.drawable.ic_addmultimedia);
			mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM); 
		}		
		
		Responder cancelResponder = new Responder();
		cancelResponder.setResponse(new Action(){
			@Override
			public void act(){
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
		mStoryTitleEditTextView.setSingleLine(true);
		mStoryTitleEditTextView.setText(storyTitle);
		mStoryTitleEditTextView.setTextSize(textSize);
	}

	void setPageTitle(String pageTitle, float textSize) {
		mPageTitleEditTextView.setSingleLine(true);
		mPageTitleEditTextView.setText(pageTitle);
		mPageTitleEditTextView.setTextSize(textSize);
	}
	
	void setPageAuthor(String pageAuthor, float textSize){
		mPageAuthorEditTextView.setSingleLine(true);
		mPageTitleEditTextView.setText(pageAuthor);
		mPageTitleEditTextView.setTextSize(textSize);
	}
	
	// TODO: remove content argument.
	void setStoryText(Content storyText, float textSize) {
		storyText.setEditModeTrue(this);
		String s = "\n\n" + storyText.getParagraph(); // Add some space.
		// Create a new StringExtended to not replace the old one.
		Content se = new Content(s, storyText.getAllMultimedia());
		se.setEditModeTrue(this);
		se.setParagraph(s);

		mStoryEditTextView.setMovementMethod(LinkMovementMethod.getInstance());
		storyText.setEditModeTrue(this);
		se.setEditModeTrue(this);
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
			ArrayList<MultimediaAbstract> ma = mPage.getFormattedContent()
					.getAllMultimedia();
			for (MultimediaAbstract m : ma)
				m.setIsSelected(false);
			// Set the multimedia we want to enable to true.
			mMultimedia.setIsSelected(true);			

			Update();
		}
	}

	// ClickableSpan for deleting.
	class ClickableDeleteSpanEx extends ClickableSpan{
		private MultimediaAbstract mMultimedia;
		public ClickableDeleteSpanEx(MultimediaAbstract ma) {
			super();
			mMultimedia = ma;			
		}
		
		@Override
		public void onClick(View widget) {
			ArrayList<MultimediaAbstract> ma = mPage.getFormattedContent()
					.getAllMultimedia();
			
			for(MultimediaAbstract m: ma){
				if(m == mMultimedia){
					ma.remove(m);
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
	public SpannableStringBuilder getSpannableStringBuilder() {
		ArrayList<MultimediaAbstract> ma = mPage.getMultimedia();

		SpannableStringBuilder stringBuilder = new SpannableStringBuilder(mPage
				.getFormattedContent().getParagraph());

		
		// Load Bitmap for delete button.
		Bitmap deleteBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_delete); 
		for (MultimediaAbstract multimedia : ma) {
			// Load the multimedia Picture representation.
			Bitmap multimediaBitmap = multimedia.loadPhoto(this);
			PaddingableImageSpan multimediaImageSpan = new PaddingableImageSpan(this, multimediaBitmap, 20);
			
			PaddingableImageSpan deleteImageSpan = new PaddingableImageSpan(this, deleteBitmap, 0);
			
			if (multimedia.getIsSelected())
				multimediaImageSpan.enablePadding();
			
			stringBuilder.insert(multimedia.getIndex(), " ");		// Allocate space for multimediaImageSpan.
			stringBuilder.insert(multimedia.getIndex()+1, " ");		// Allocate space for deleteImageSpan.
			
			stringBuilder.setSpan(multimediaImageSpan, multimedia.getIndex(),
					multimedia.getIndex() + 1,
					Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			
			stringBuilder.setSpan(deleteImageSpan, multimedia.getIndex()+1,
					multimedia.getIndex() + 2,
					Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

			ClickableMultimediaSpanEx multimediaClickableSpan = 
					new ClickableMultimediaSpanEx(multimedia, multimediaImageSpan);
			ClickableDeleteSpanEx deleteClickableSpan = 
					new ClickableDeleteSpanEx(multimedia);

			stringBuilder.setSpan(multimediaClickableSpan, multimedia.getIndex(),
					multimedia.getIndex() + 1,
					Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			
			stringBuilder.setSpan(deleteClickableSpan, multimedia.getIndex()+1,
					multimedia.getIndex() + 2,
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
		if (mAdventureTime.getOldPage() == null) {
			// Do nothing.
		} else {
			mAdventureTime.revertPage();
			this.recreate();
		}
	}

	@Override
	public void Update() {
		super.Update();
		// Things to update.
		setStoryText(mPage.getFormattedContent(), 18);
	}

	// Call when want to clear selection.
	void clearSelection() {
		ArrayList<MultimediaAbstract> ma = mPage.getFormattedContent()
				.getAllMultimedia();
		for (MultimediaAbstract m : ma) {
			m.setIsSelected(false);
		}

		mIsAnyMultimediaSelected = false;		
	}
	
	// Called when cursor location is changed.
	void cursorIndexChange(int index) {
		// If a multimedia is selected, move it to where the user clicked
		// and update.
		int  maxIndex = mPage.getFormattedContent().getParagraph().length();
		if(index > maxIndex){
			index = maxIndex;
		}
		
		if (mIsAnyMultimediaSelected && (mIsJustSelected == false) && index >= 0 ) {
			ArrayList<MultimediaAbstract> ma = mPage.getFormattedContent()
					.getAllMultimedia();

			for (MultimediaAbstract m : ma) {
				if (m.getIsSelected()) {
					// Make sure no multimedia have that index.
					for( MultimediaAbstract m2: ma){
						if(m2 != m){
							if( m.getIndex() == index ){
								Toast.makeText(this, "Index is occupied, place multimedia somewhere else.", Toast.LENGTH_LONG).show();
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
		}else if(mIsJustSelected == true && index >= 0){
			mIsJustSelected = false;
		}else{
			// Unknown state.
		}
	}

	// Implement when addMultimedia class is implemented.
	void addMultimedia(){}
	void save(){		
		String pageName = this.mPageTitleEditTextView.getText().toString();
		String pageAuthor = this.mPageAuthorEditTextView.getText().toString();
		String pageStory = this.mPageTitleEditTextView.getText().toString();
		
		if( pageName.length() <= 0 ||
			pageAuthor.length() <= 0 ||
			pageStory.length() <= 0){
			// Things to do if one of the inputs are empty.
		}
		
		// Things to do if the inputs are valid.
	}
	void cancel(){}
	
	// For adding page.
	void addPage(){
		
	}
}
