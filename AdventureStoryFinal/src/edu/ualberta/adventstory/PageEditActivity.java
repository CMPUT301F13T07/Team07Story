package edu.ualberta.adventstory;

import java.util.ArrayList;
import java.util.TreeMap;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import edu.ualberta.controller.*;
import edu.ualberta.controller.CallbackIntefaces.*;
import edu.ualberta.controller.OnMoveMultimediaListener;
import edu.ualberta.controller.OnRedoListener;
import edu.ualberta.controller.OnUndoListener;
import edu.ualberta.controller.CommandManager;
import edu.ualberta.controller.OnAddMultimediaListener;
import edu.ualberta.controller.MultimediaControllerManager;
import edu.ualberta.data.WebClient;
import edu.ualberta.multimedia.MultimediaAbstract;
import edu.ualberta.multimedia.TObservable;
import edu.ualberta.utils.Page;
import edu.ualberta.utils.Story;

/**
 * <code>PageEditActivity</code> is a view that is responsible for editing page.
 * It is a subclass of ActivityExtended thus inheriting MVC's
 * <code>Observable</code> Update interface.
 * 
 * @author JoeyAndres
 * 
 */
@SuppressLint("NewApi")
public class PageEditActivity extends ActivityExtended {
	private EditText mPageTitleEditTextView; // Page Title EditText.
	private EditText mPageAuthorEditTextView; // Page Title EditText.
	private ArrayList<EditText> mStoryEditTextArray = new ArrayList<EditText>();

	// For more info concerning views, see activity_pageedit.xml
	private LinearLayout mPageTextLayout;
	private TableLayout mButtonLayout;
	private Button mButtonAddPage;
	private Story mStory;
	private Page mPage;
	private Button mButtonAddMultimedia; // Button for adding multimedias.
		
	private boolean FRAGMENT_INFLATED = false;

	final public static int ADDPAGE_REQUESTCODE = 1;
	final public static int GET_MULTIMEDIA_REQUESTCODE = 2;
	final public static int SWAP_MULTIMEDIA_REQUESTCODE = 3;
	
	private WebClient mWebClient;

	// Current Command being executed. This is used when AddMultimedia is being
	// executed since it requires waiting for the callback.
	CommandAbstract mCurrentCommand = null;
	private CommandManager mCommandManager; // Manages Undo/Redo.

	// Set to true when editing a page only, independent of the story.
	private boolean mEditPageOnly = false;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pageedit);
		setTitle(R.string.createpage);
		mPage = mDataSingleton.getCurrentPage();
		mStory = mDataSingleton.getCurrentStory();

		// Determine if the mStory is null if we are just creating a page
		// independent
		// of the Story.
		if (mStory == null) {
			mEditPageOnly = true;
		}

		// Exit if mPage is null.
		if (mPage == null) {
			Toast.makeText(this, "Error occured, mPage or mStory is null.",
					Toast.LENGTH_LONG).show();
			exit();
		}

		if (mEditPageOnly == false) {
			setStoryTitle();
		}

		mPageTitleEditTextView = (EditText) findViewById(R.id.pageTitle);
		mPageTextLayout = (LinearLayout) findViewById(R.id.pageEditTextLayout);
		mPageAuthorEditTextView = (EditText) findViewById(R.id.pageAuthor);
		mButtonAddMultimedia = (Button) findViewById(R.id.addMultimedia);
		mButtonAddPage = (Button) findViewById(R.id.addPage);
		mButtonLayout = (TableLayout) findViewById(R.id.buttonLayout);
		CheckBox cb = (CheckBox) findViewById(R.id.readOnlyCheckBox);

		setStoryTitle();
		setPageTitle();
		setPageAuthor();
		setStoryText();
		cb.setChecked(mPage.getReadOnly());

		mPageTextLayout.setOnClickListener(new OnClickListener() {
			@Override
			// Click background to deselect any selection.
			public void onClick(View v) {
				clearSelection();
			}
		});

		final ActivityExtended ae = this;
		mButtonAddMultimedia.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addMultimedia();

				OnAddMultimediaListener addMultimediaResponder = new OnAddMultimediaListener(
																				mPage, null, ae);
				mCurrentCommand = addMultimediaResponder;
			}
		});

		// Add an 'Add Page' Button to the outerlayout.
		mButtonAddPage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v == mButtonAddPage) {
					addPage();
				}
			}
		});

		addNextPageButtons();
		mCommandManager = CommandManager.getInstance();
		
		mWebClient = new WebClient();
	}

	/*
	 * onResume is where we placed some duplicates of getCurrentPage and
	 * getCurrentStory since this in situations when we call an Activity to
	 * expect a return, it will go back here,setStoryText and not in onCreate
	 * anymore.
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		/*
		 * These are placed here to acquire data when screen orientation changed
		 * since onCreate is not called in such situation.
		 */
		mPage = mDataSingleton.getCurrentPage();
		mStory = mDataSingleton.getCurrentStory();
		setStoryText();

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
	 * onPause is where the save() instead of onDestroy since onPause is where
	 * the views are still in place and thus we can still extract data's from
	 * them.
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	public void onPause() {
		save();
		super.onPause();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case R.id.action_help:
        	Toast.makeText(getApplicationContext(), "Press the disk button to save your changes.\n"
        											+ "Read-Only makes the page non-editable by "
        											+ "everyone but you.",
					   Toast.LENGTH_LONG).show();
            return true;
        case R.id.action_home:
        	finish();
		}
		CommandAbstract command = mMapMenuToCommand.get(item);
		if (command != null) {
			if (command instanceof OnAddMultimediaListener) {
				mCurrentCommand = command;
				return true;
			}
			mCommandManager.invokeCommand(command);
		}
		return true;
	}

	/**
	 * <code>CreateMenu</code> here is responsible for creating Actionbar items
	 * and mapping these MenuItems to their corresponding Command and Callback
	 * method via Hash table.
	 * 
	 * @see PageEditActivity.mMapMenuToCommand
	 * 
	 * @param menu
	 */
	@SuppressLint("NewApi")
	void CreateMenu(Menu menu) {
<<<<<<< HEAD
		MenuItem mnu1 = menu.add(0, 0, 0, "Add Multimedia");
		{
			mnu1.setIcon(R.drawable.ic_addmultimedia);
			mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}

		OnAddMultimediaListener addMultimediaResponder = new OnAddMultimediaListener(
				mPage, null, this);
		mMapMenuToCommand.put(mnu1, addMultimediaResponder);

		MenuItem mnu2 = menu.getItem(R.id.action_undo);
=======
		MenuItem mnu2 = menu.findItem(R.id.action_undo);
>>>>>>> master
		OnUndoListener undoResonder = new OnUndoListener(new OnUndo() {
			@Override
			public void undo() {
				mCommandManager.undo();
				localUpdate();
			}
		});
		mMapMenuToCommand.put(mnu2, undoResonder);

<<<<<<< HEAD
		MenuItem mnu3 = menu.getItem(R.id.action_redo);
=======
		MenuItem mnu3 = menu.findItem(R.id.action_redo);				
>>>>>>> master
		OnRedoListener redoResonder = new OnRedoListener(new OnRedo() {
			@Override
			public void redo() {
				mCommandManager.redo();
				localUpdate();
			}
<<<<<<< HEAD
		});
=======
		});		
>>>>>>> master
		mMapMenuToCommand.put(mnu3, redoResonder);

		MenuItem mnu4 = menu.findItem(R.id.action_save);
		OnSaveListener saveResponder = new OnSaveListener(new OnSave() {
			@Override
			public void onSave() {
				save();
				exit();
			}
		});
		mMapMenuToCommand.put(mnu4, saveResponder);

<<<<<<< HEAD
		MenuItem mnu5 = menu.add(0, 4, 4, "Cancel");
		{
			mnu5.setIcon(R.drawable.ic_exit);
			mnu5.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}

=======
		MenuItem mnu5 = menu.findItem(R.id.action_cancel);
>>>>>>> master
		OnExitListener cancelResponder = new OnExitListener(
				new OnExit() {
					@Override
					public void onExit() {
						exit();
					}
				});
		mMapMenuToCommand.put(mnu5, cancelResponder);
		
		MenuItem mnu6 = menu.findItem(R.id.action_help);		
		mMapMenuToCommand.put(mnu6, new OnStartHelpActivityListener(this, "editPage.html"));
		
		MenuItem mnu7 = menu.findItem(R.id.action_settings);
		mMapMenuToCommand.put(mnu7, new OnStartSettingsActivityListener(this));
	}

	/**
	 * <code>addNextPageButtons</code> is used for adding listView elements for
	 * next page.
	 */
	private void addNextPageButtons() {
		for (final Page p : mPage.getPages()) {
			TableRow tr = (TableRow) View.inflate(this, R.layout.table_row,
					null);
			TableRow trDivider = (TableRow) View.inflate(this,
					R.layout.table_row, null);
			TextView tv = (TextView) View.inflate(this,
					R.layout.next_page_textview, null);
			ImageButton ib = (ImageButton) View.inflate(this,
					R.layout.delete_imagebutton, null);

			tv.setText(p.getTitle());
			tv.setOnClickListener(new OnClickListener() {
				@SuppressLint("NewApi")
				@Override
				public void onClick(View view) {
					// Go to the next page.
					mDataSingleton.setCurrentPage(p);
					mDataSingleton.getCurrentActivity().recreate();
				}
			});
			ib.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mDataSingleton.database.delete_page_option(mPage, p);
					mDataSingleton.getCurrentActivity().recreate();
				}
			});

			View v = (View) View.inflate(this, R.layout.divider, null);
			tr.addView(tv);
			tr.addView(ib);
			trDivider.addView(v);
			mButtonLayout.addView(tr);
			mButtonLayout.addView(trDivider);
		}
	}

	/**
	 * @return <code>String</code> of the title of the page, stripped-off of
	 *         unnecessary white space.
	 */
	private String getStoryText() {
		ArrayList<String> fragmented = new ArrayList<String>();
		for (EditText et : mStoryEditTextArray) {
			String f[] = et.getText().toString().split(" +");
			for (int j = 0; j < f.length; j++) {
				if (f[j].length() == 0)
					continue;
				fragmented.add(f[j]);
				fragmented.add(" ");
			}
		}

		StringBuilder sb = new StringBuilder();

		for (String s : fragmented) {
			sb.append(s);
		}
		return sb.toString();
	}

	/**
	 * Since there can be multiple fragmented views, this method will return the
	 * offset since the first view to the current view in focus.
	 * 
	 * @param selectedEditText
	 *            The EditText currently in focus.
	 * @return int
	 */
	private int getPageTextIndexOffset(EditText selectedEditText) {
		int offset = 0;
		for (EditText et : mStoryEditTextArray) {
			if (et != selectedEditText) {
				String f[] = et.getText().toString().split(" +");
				for (int j = 0; j < f.length; j++) {
					if (f[j].length() == 0)
						continue;
					offset += f[j].length();
					offset += 1;
				}
			} else {
				break;
			}
		}

		return offset;
	}

	/**
	 * @return <code>String</code> of the author of the page.
	 */
	private String getAuthorText() {
		return mPageAuthorEditTextView.getText().toString();
	}

	/**
	 * @return <code>String</code> of the title of the page.
	 */
	private String getPageTitleText() {
		return mPageTitleEditTextView.getText().toString();
	}

	/**
	 * <code>setStoryTitle</code> set's the <code>TextView</code> for Story
	 * Title. Unlike Other Views here, this is the only <code>TextView</code> as
	 * we don't allow modification of Story Title while editing page.
	 * 
	 * @param textSize
	 *            is the size of the text.
	 */
	void setStoryTitle() {
		ActionBar ab = getActionBar();
		if (mStory == null) {
			Toast.makeText(this, "Error: Story is null", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		ab.setTitle(mStory.getTitle());
	}

	/**
	 * <code>setPageTitle</code> set's the <code>EditText</code> for page title.
	 * This is done via getTitle method in <code>Page</code>
	 * 
	 * @param textSize
	 *            is the size of the text.
	 */
	void setPageTitle() {
		mPageTitleEditTextView.setSingleLine(true);
		mPageTitleEditTextView.setText(mPage.getTitle());		
	}

	/**
	 * <code>setPageAuthor</code> set's the <code>TextView</code> for page
	 * author. This is done by getting the author's name via mPage's getAuthor
	 * method.
	 * 
	 * @param textSize
	 *            is the size of the text.
	 */
	void setPageAuthor() {
		mPageAuthorEditTextView.setSingleLine(true);
		mPageAuthorEditTextView.setText(mPage.getAuthor());		
	}

	/**
	 * <code>setStoryText</code> sets the current Page content or the
	 * "story text". Of course it delegates the compilation of
	 * <code> SpannableStringBuilder </code> to
	 * <code>getSpannableStringBuilder</code>.
	 * 
	 * @param textSize
	 *            is the size of the text.
	 */
	@SuppressWarnings("unused")
	void setStoryText() {
		// Make sure the layout is empty first.
		mPageTextLayout.removeAllViewsInLayout();
		mStoryEditTextArray.clear();

		ArrayList<MultimediaAbstract> multimediaList = mPage.getMultimedia();
		// Add an observer to these models/observable.
		for (MultimediaAbstract m : multimediaList) {
			m.addObserver(this);
		}

		// If no multimedia.
		if (multimediaList == null) {
			addEditTextInPage(mPage.getText());
			return;
		}

		// Pool index-multimedia table.
		TreeMap<Integer, ArrayList<MultimediaAbstract>> indexMultimediaHash = new TreeMap<Integer, ArrayList<MultimediaAbstract>>();
		for (final MultimediaAbstract m : multimediaList) {
			ArrayList<MultimediaAbstract> mList = indexMultimediaHash.get(m
					.getIndex());
			if (mList != null) {
				mList.add(m);
				indexMultimediaHash.put(m.getIndex(), mList);
			} else {
				ArrayList<MultimediaAbstract> mListTemp = new ArrayList<MultimediaAbstract>();
				mListTemp.add(m);
				indexMultimediaHash.put(m.getIndex(), mListTemp);
			}
		}

		// Fragment text base on index.
		TreeMap<Integer, String> stringFragments = new TreeMap<Integer, String>();
		TreeMap<String, Integer> stringFragmentsReversed = new TreeMap<String, Integer>();
		int lastIndex = 0;
		for (int i : indexMultimediaHash.keySet()) {
			if (i >= mPage.getText().length()) {
				continue;
			}
			if (i - lastIndex == 0) {
				continue;
			}
			char[] temp = new char[i - lastIndex];
			mPage.getText().getChars(lastIndex, i, temp, 0);
			stringFragments.put(i, new String(temp));
			stringFragmentsReversed.put(new String(temp), i);
			lastIndex = i;
		}

		int textLength = mPage.getText().length();
		if (lastIndex < textLength) {
			char[] temp = new char[textLength - lastIndex];
			mPage.getText().getChars(lastIndex, textLength, temp, 0);
			stringFragments.put(textLength, new String(temp));
			stringFragmentsReversed.put(new String(temp), textLength);
			lastIndex = textLength;
		}

		// Assemble EditTextEx and ImageViews.
		ArrayList<MultimediaAbstract> displayedMultimedia = new ArrayList<MultimediaAbstract>();
		int multimediaCounter = 0;
		int textViewCounter = 0;
		lastIndex = 0;
		for (int index : stringFragments.keySet()) {
			ArrayList<MultimediaAbstract> mList = indexMultimediaHash
					.get(lastIndex);
			if (mList != null) {
				for (final MultimediaAbstract m : mList) {
					addImageViewInPage(m);
					multimediaCounter++;
					displayedMultimedia.add(m);
				}
			}
			addEditTextInPage(stringFragments.get(index));
			textViewCounter++;
			lastIndex = index;
		}

		Toast.makeText(this, Integer.toString(textViewCounter),
				Toast.LENGTH_SHORT).show();

		// If there are non-displayed multimedia, place them all at the end.
		if (multimediaCounter < multimediaList.size()) {
			for (final MultimediaAbstract m : multimediaList) {
				if (displayedMultimedia.contains(m) == false) {
					addImageViewInPage(m);
				}
			}
		}

		// If by the end still no EditText.
		if (mStoryEditTextArray.size() == 0) {
			addEditTextInPage("");
		}
	}

	/**
	 * <code>addImageViewInPage</code> helper method for
	 * <code>setStoryText</code>.
	 */
	private void addImageViewInPage(final MultimediaAbstract m) {
		final ImageView iv = new ImageView(this);
		if (m.getIsSelected()) {
			iv.setPadding(10, 10, 10, 10);
			iv.setScaleType(ScaleType.CENTER_INSIDE);
			iv.setBackgroundColor(Color.GRAY);
		}
		iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (FRAGMENT_INFLATED)
					return;				
				clearSelection();
				m.setIsSelected(true);
				FRAGMENT_INFLATED = true;
				showMultimediaOptionsFragment(m);
			}
		});

		iv.setPadding(5, 5, 5, 5);

		LayoutParams lp1 = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		iv.setImageBitmap(MultimediaControllerManager.loadBitmap(this, m));
		iv.setLayoutParams(lp1);
		mPageTextLayout.addView(iv);
	}

	/**
	 * set FRAGMENT_INFLATED to val.
	 */
	public void setFRAGMENTINFLATED(boolean value) {
		FRAGMENT_INFLATED = value;
	}

	/**
	 * <code>addEditTextInPage()</code> adds an EditText in Page body.
	 */
	private void addEditTextInPage(String text) {
		final EditText et = (EditText) LayoutInflater.from(this).inflate(
				R.layout.page_edittext, null);
		et.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				cursorIndexChange(et.getSelectionStart(), et);
			}
		});
		
		et.setText(text);
		mPageTextLayout.addView(et);
		mStoryEditTextArray.add(et);
	}

	/**
	 * Is an event handler.
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
	 * This event handler is overriden so backpress will always go back to last
	 * activity.
	 * 
	 * @see android.app.Activity#onBackPressed()
	 */
	@SuppressLint("NewApi")
	@Override
	public void onBackPressed() {
		if (mDataSingleton.peekPage() == null) {
			super.onBackPressed();
		} else {
			mDataSingleton.revertPage();
			this.recreate();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.ualberta.adventstory.ActivityExtended#update(edu.ualberta.multimedia
	 * .TObservable)
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
	public void localUpdate() {
		// Things to update.
		setStoryText();
	}

	/**
	 * <code>clearSelection</code> clear all trace of selection. This is
	 * required as opposed to doing it mannually.
	 */
	void clearSelection() {
		ArrayList<MultimediaAbstract> ma = mPage.getMultimedia();
		for (MultimediaAbstract m : ma) {
			m.setIsSelected(false);
		}
	}

	/**
	 * <code>cursorIndexChange</code> is usually placed inside
	 * <code>EditTextEx</code> callback method.
	 * 
	 * @author JoeyAndres
	 * @param index
	 * @version 1.0
	 */
	void cursorIndexChange(int index, EditText editTextClicked) {
		// If a multimedia is selected, move it to where the user clicked
		// and update.

		// Offset the index to not select any index in the middle of word..
		// TODO: Move this to a method, setStoryText will need this.
		String str = editTextClicked.getText().toString();
		char[] charArray = str.toCharArray();
		while (index != 0 && index != charArray.length
				&& charArray[index] != ' ' && charArray[index] != '\n') {
			index++;
		}
		index = index + getPageTextIndexOffset(editTextClicked);

		int maxIndex = mPage.getText().length();
		if (index > maxIndex) {
			index = maxIndex;
		}

		ArrayList<MultimediaAbstract> ma = mPage.getMultimedia();

		for (MultimediaAbstract m : ma) {
			if (m.getIsSelected()) {
				OnMoveMultimediaListener omml = new OnMoveMultimediaListener(mPage, m, index, this);
				mCommandManager.invokeCommand(omml);
				clearSelection();
				return;
			}
		}
	}

	/**
	 * <code>addMultimedia</code> is called when adding Multimedia in
	 * PageEditActivity.
	 * 
	 * @author JoeyAndres
	 * @version 1.0
	 */
	void addMultimedia() {
		save();
		// Load arguments.
		Bundle info = new Bundle();
		info.putInt("page_id", mPage.getID());

		int index = 0;

		info.putInt("index", index);
		Intent addMultimediaIntent = new Intent(this,
				AddMultimediaActivity.class);
		addMultimediaIntent.putExtras(info);
		startActivityForResult(addMultimediaIntent,
				GET_MULTIMEDIA_REQUESTCODE);
	}

	/**
	 * <code>swapMultimedia</code> is called when wanting to swap a currently
	 * selected Multimedia. Usually from <code>MultimediaOptionsFragment</code>.
	 * 
	 * @param id
	 *            refers to the Multimedia id being swapped.
	 */
	public void swapMultimedia(int id) {
		save();
		// Load arguments.
		Bundle info = new Bundle();
		info.putInt("page_id", mPage.getID());
		info.putInt("selectedMultimediaID", id);

		// Just place them all at start.
		int index = 0;

		info.putInt("index", index);
		Intent addMultimediaIntent = new Intent(this,
				AddMultimediaActivity.class);
		addMultimediaIntent.putExtras(info);
		startActivityForResult(addMultimediaIntent,
				SWAP_MULTIMEDIA_REQUESTCODE);
	}

	/**
	 * <code>save</code> must be called when saving. <code>onPause</code> is the
	 * recommended event to when/where this should be called.
	 */
	void save() {
		// Null pointer check.
		if (mPage == null) {
			Toast.makeText(this, "Error: Page is null.", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		String pageName = getPageTitleText();
		String pageAuthor = getAuthorText();
		String pageStory = getStoryText();

		CheckBox cb = (CheckBox) findViewById(R.id.readOnlyCheckBox);

		mPage.setTitle(pageName);
		mPage.setAuthor(pageAuthor);
		mPage.setText(pageStory);
		mPage.setReadOnly(cb.isChecked());

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
		for (MultimediaAbstract m : mPage.getMultimedia()) {
			mDataSingleton.database.update_multimedia(m, mPage.getID());
		}
		
		mWebClient.insert_page(mPage);
	}

	/**
	 * <code>cancel</code> For now cancel is no use.
	 * 
	 * TODO: Discuss with team what to do when cancelling. - Do I delete the
	 * current page? - If there's only one page, Do I also delete the current
	 * story? - Or do I just save whatever is typed?
	 * 
	 * @deprecated
	 */
	private void cancel() {
		exit();
	}

	/**
	 * <code>addPage</code> is the method to call when adding page. Usually
	 * attached with a button associated for adding page.
	 * 
	 * @author Joey Andres
	 */
	void addPage() {
		// Open the activity for this.
		Intent searchActivity = new Intent(this, SearchActivity.class);
		Bundle info = new Bundle();
		info.putBoolean("ADD_PAGE", true);
		info.putBoolean("BOOL_IS_STORY", false);
		searchActivity.putExtra("android.intent.extra.INTENT", info);
		startActivityForResult(searchActivity, ADDPAGE_REQUESTCODE);
	}

	/**
	 * <code>exit</code> provides a way of exiting to StartActivity.
	 */
	@Override
	protected void exit() {		
		startActivity(new Intent(this, StartActivity.class));
	}

	/**
	 * <code>showMultimediaOptionsFragment</code> launches
	 * MultimediaOptionsFragment to display tools/options to be apply to a
	 * Multimedia object.
	 */
	private void showMultimediaOptionsFragment(MultimediaAbstract m) {
		MultimediaOptionsFragment mof= new MultimediaOptionsFragment(mPage, m);
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(android.R.id.content, mof);
		fragmentTransaction.commit();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ADDPAGE_REQUESTCODE) {
			mPage = mDataSingleton.getCurrentPage();
			Page newPage = mDataSingleton.database.get_page_by_id(resultCode);

			// Have the Activity also return a resultCode concerning the
			// failure/sucess of operation.
			// See if the user canceled the request.
			if (newPage == null) {
				// No new page.
				return;
			}

			mDataSingleton.database.insert_page_option(mPage, newPage);
			mPage.addPage(newPage);
			restart();
			return;
		} else if (requestCode == GET_MULTIMEDIA_REQUESTCODE) {
			mPage = mDataSingleton.getCurrentPage();
			mStory = mDataSingleton.getCurrentStory();
			
			// Multimedia retreival fail.
			if(data == null) {
				mCurrentCommand = null;
				return;
			}
			
			// resultCode is newMultimedia index.
			if(mCurrentCommand instanceof OnAddMultimediaListener){
				Bundle bundle = data.getExtras();
				int newId = (int) bundle.getLong("NewMultimediaId");				
				
				MultimediaAbstract newMultimedia = null;
				// Check if new multimedia exist.				
				for (MultimediaAbstract m : mPage.getMultimedia()) {
					if (m.getID() == newId) {
						newMultimedia = m;
						break;
					}
				}
				
				if(newMultimedia == null) {
					mCurrentCommand = null;
					return;
				}
				
				((OnAddMultimediaListener)mCurrentCommand).setMultimedia(newMultimedia);
				mCommandManager.invokeCommand(mCurrentCommand);				
			}else{
				// Unknonwn state.
			}				
			return;
		} else if (requestCode == SWAP_MULTIMEDIA_REQUESTCODE) {
			mPage = mDataSingleton.getCurrentPage();
			mStory = mDataSingleton.getCurrentStory();

			// Multimedia retreival fail.
			if(data == null) {
				mCurrentCommand = null;
				return;
			}
			
			Bundle bundle = data.getExtras();
			int newId = (int) bundle.getLong("NewMultimediaId");
			int selectedId = bundle.getInt("selectedMultimediaId");

			// Check if selected multimedia exist.
			MultimediaAbstract selectedMultimedia = null;
			ArrayList<MultimediaAbstract> mList = mPage.getMultimedia();
			for (MultimediaAbstract m : mList) {
				if (m.getID() == selectedId) {
					selectedMultimedia = m;
					break;
				}
			}

			if (selectedMultimedia == null) {
				// didn't find the new selected multimedia.
				// ERROR.
				return;
			}

			// Check if to be swap exist.
			MultimediaAbstract toBeSwapMultimedia = null;
			for (MultimediaAbstract m : mList) {
				if (m.getID() == newId) {
					toBeSwapMultimedia = m;
					break;
				}
			}

			if (toBeSwapMultimedia == null) {
				// didn't find the new selected multimedia.
				// ERROR.
				return;
			}

			// The swapping process.
			selectedMultimedia.setFileDir(toBeSwapMultimedia.getFileDir());
			mDataSingleton.database.delete_mult(toBeSwapMultimedia, mPage);
			mDataSingleton.database.update_multimedia(selectedMultimedia,
					mPage.getID());
			localUpdate();
			return;
		} else {
			// UKNOWN STATE.
		}
	}

	/**
	 * This is done so that requestcode is sent automatically via intent too.
	 * 
	 * @param intent
	 * @param requestCode
	 */
	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		if( requestCode == -1 ){
			super.startActivityForResult(intent, 0);
			return;
		}
		
		Bundle data = intent.getExtras();
		data.putLong("requestCode", requestCode);
		intent.putExtras(data);
		super.startActivityForResult(intent, requestCode);
	}
}
