package edu.ualberta.adventstory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Stack;
import java.util.TreeMap;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import edu.ualberta.adventstory.R;
import edu.ualberta.controller.*;
import edu.ualberta.multimedia.MultimediaAbstract;
import edu.ualberta.utils.Page;
import edu.ualberta.utils.Story;

/**
 * <code>PageViewActivity</code> is the View responsible for viewing
 * <code>Page</code>.
 * 
 * @author JoeyAndres
 * @version 1.0
 */
public class PageViewActivity extends ActivityExtended {
	private TextView mPageTitleTextView; // Page Title TextView.
	private LinearLayout mOuterLayout; // Outer LinearLayout.
	LinearLayout mPageTextLayout;
	private LinearLayout mButtonLayout;
	private Story mStory; // Story being viewed.
	private Page mPage; // Current page.

	private boolean mViewPageOnly = true; // True if we are viewing a page
											// independent of story.

	private static final int START_EDITPAGE_RESULTCODE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pageview);

		mPage = mDataSingleton.getCurrentPage();
		mStory = mDataSingleton.getCurrentStory();
		if (mStory == null) {
			mViewPageOnly = true;
		}

		mOuterLayout = (LinearLayout) findViewById(R.id.outerLayout);
		mPageTextLayout = (LinearLayout) findViewById(R.id.pageTextLayout);
		mButtonLayout = (LinearLayout) findViewById(R.id.buttonLayout);
		Button buttonRandomNextPage = (Button) findViewById(R.id.randomChoiceButton);
		mPageTitleTextView = (TextView) findViewById(R.id.pageTitle);

		if (mStory != null) {
			setStoryTitle(mStory.getTitle());
		}
		else {
			setStoryTitle("Page View");
		}

		if (mPage.getPages().size() == 0) {
			buttonRandomNextPage.setEnabled(false);
		}
		buttonRandomNextPage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				randomNextPage();
			}
		});

		setPageTitle(mPage.getTitle());
		setStoryText();

		addNextPageButtons();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mDataSingleton.setCurrentActivity(this);
	}
	
	/**
	 * Implemented to turn SoundClipController audio play.
	 */
	@Override
	protected void onPause(){		
		SoundClipController scc = new SoundClipController();
		/*if(scc.isplayingAudio == false){
			scc.stopAudio();
		}*/
		super.onPause();
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
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case R.id.action_help:
        	Toast.makeText(getApplicationContext(), "To edit this page, press the pencil.",
					   Toast.LENGTH_LONG).show();
            return true;
        case R.id.action_home:
        	Intent intent = new Intent(this, StartActivity.class);
    		startActivity(intent);
		}
		return MenuChoice(item);
	}

	private boolean MenuChoice(MenuItem item) {
		CommandAbstract command = mMapMenuToCommand.get(item);
		if (command != null) {
			command.execute();
			return true;
		}
		return false;
	}

	@SuppressLint("NewApi")
	void CreateMenu(Menu menu) {
		if (mPage.getReadOnly() == false) {
			MenuItem mnu1 = menu.add(0, 0, 0, "Edit Page");
			{
				mnu1.setIcon(R.drawable.ic_edit_page);
				mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM); 
			}
			OnStartPageEditListener startEditPageCommand = new OnStartPageEditListener(this);
			mMapMenuToCommand.put(mnu1, startEditPageCommand);
		}
	}

	/**
	 * <code>addNextPageButtons()</code> add the buttons that allows the
	 * user/author to transition to next page.
	 */
	private void addNextPageButtons() {
		for (final Page p : mPage.getPages()) {
			TextView tv = (TextView) View.inflate(this,
					R.layout.next_page_textview, null);
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
			View v = (View) View.inflate(this, R.layout.divider, null);
			mButtonLayout.addView(v);
			mButtonLayout.addView(tv);
		}
		View v = (View) View.inflate(this, R.layout.divider, null);
		mButtonLayout.addView(v);
	}

	/**
	 * Random next page.
	 */
	void randomNextPage() {
		ArrayList<Page> mPageList = mPage.getPages();
		if (mPageList == null || mPageList.size() == 0) {
			Toast.makeText(this, "No next page.", Toast.LENGTH_SHORT).show();
			return;
		}
		int pageSize = mPageList.size();
		Random rand = new Random();
		int nextPageIndex = rand.nextInt(pageSize);
		mDataSingleton.setCurrentPage(mPageList.get(nextPageIndex));
		recreate();
	}

	@SuppressLint("NewApi")
	void setStoryTitle(String title) {
		ActionBar ab = getActionBar();
		ab.setTitle(title);
	}

	void setPageTitle(String pageTitle) {
		pageTitle = "Page: " + pageTitle;		
		mPageTitleTextView.setText(pageTitle);
	}

	/**
	 * <code>setStoryText</code> sets the main content of the Page.
	 */
	void setStoryText() {
		ArrayList<MultimediaAbstract> multimediaList = mPage.getMultimedia();

		if (multimediaList == null) {
			addTextViewInPage(mPage.getText());
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

		// Fragment text base on index of multimedia.
		TreeMap<Integer, String> stringFragments = new TreeMap<Integer, String>();
		TreeMap<String, Integer> stringFragmentsReversed = new TreeMap<String, Integer>();
		int lastIndex = 0;
		for (int i : indexMultimediaHash.keySet()) {
			// If multimedia index i is greater than the length of string then
			// just loop through.
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

		int textLength = mPage.getText().length() - 1;
		if (lastIndex < textLength) {
			char[] temp = new char[textLength - lastIndex];
			mPage.getText().getChars(lastIndex, textLength, temp, 0);
			stringFragments.put(textLength, new String(temp));
			stringFragmentsReversed.put(new String(temp), textLength);
			lastIndex = textLength;
		}

		// Assemble TextViews and ImageViews.
		ArrayList<MultimediaAbstract> displayedMultimedia = new ArrayList<MultimediaAbstract>();
		int multimediaCounter = 0;
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

			addTextViewInPage(stringFragments.get(index));
			lastIndex = index;
		}

		// If there are non-displayed multimedia, place them all at the end.
		if (multimediaCounter < multimediaList.size()) {
			for (final MultimediaAbstract m : multimediaList) {
				if (displayedMultimedia.contains(m) == false) {
					addImageViewInPage(m);
				}
			}
		}
	}

	private void addTextViewInPage(String text) {
		TextView tv = (TextView) LayoutInflater.from(this).inflate(
				R.layout.page_textview, null);
		tv.setText(text);
		mPageTextLayout.addView(tv);
	}

	/**
	 * <code>addImageViewInPage</code> helper method for
	 * <code>setStoryText</code>.
	 */
	@SuppressLint("NewApi")
	private void addImageViewInPage(final MultimediaAbstract m) {
		final ImageView iv = new ImageView(this);
		iv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MultimediaControllerManager.play(getBaseContext(), m);
			}
		});
		iv.setPadding(5, 5, 5, 5);
		LayoutParams lp1 = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		iv.setImageBitmap(MultimediaControllerManager.loadBitmap(this, m));
		iv.setLayoutParams(lp1);
		mPageTextLayout.addView(iv);
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO: HASH THESE STUFF INTO COMMAND.
		if (requestCode == 1) {
			this.restart();
		}
	}
}
